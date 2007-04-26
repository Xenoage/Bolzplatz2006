/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.match.characters;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.actions.*;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.match.physics.PhysicsObject;
import com.xenoage.bp2k6.util.Cast;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;



/**
 * Abstract base class for all persons:
 * soccer players (outfield player and goalkeaper),
 * referees, lawnmower man, and so on.
 * Spectators are not figures, because they
 * are no 3d models.
 *
 * @author Andreas Wenger
 */
public abstract class Character
{
  
  //the current ActionSetSet
  protected ActionSet currentActionSet;
  //the index of the current Action in the current ActionSet
  protected int currentActionIndex;
  //true, if the current Action is a RunToAction
  protected boolean currentActionIsRunToAction = false;
  
  //the next ActionSet, when the current one is over
  protected ActionSet nextActionSet;
  //set this flag to true, when the nextActionSet should be
  //applied immediately after the current action
  protected boolean nextActionSetAtOnce = false;
  
  //default action set for this character: waiting
  protected ActionSet defaultActionSet = ActionSetFactory.createNo();
  
  //action with very high priority, it is played even
  //between the actions of the current action set as soon as set
  protected Action priorityAction = null;
  
  //mesh of this character with additional information
  protected CharacterMesh mesh;
  //scene node of this character
  protected IAnimatedMeshSceneNode node;
  //the shadow of this character
  protected IAnimatedMeshSceneNode shadowNode;
  
  //the current animation stage
  protected AnimationStage animationStage = AnimationStage.None;
  //the start time of the current animation stage
  protected float animationStageStartTime = 0;
  //the animation frame number from the last time
  //updateAnimation() was called
  protected int lastFrame = 0;
  
  //the position, velocity, y-rotation, speed and target
  protected Vector3f position = new Vector3f();
  protected Vector3f velocity = new Vector3f();
  protected float rotationY;
  protected Speed speed = Speed.Walk;
  protected Vector3f target = null;
  protected Speed tackleSpeed = Speed.Walk;
  protected Vector3f diveDirection = null;
  protected float diveSpeed = 0;
  
  //irrlicht variables
  protected vector3df nativePosition = new vector3df();
  protected vector3df nativeRotation = new vector3df();
  
  //physics
  private boolean physicsEnabled = false;
  
  //last time the step()-method method was called
  protected float lastStepTime = 0;
  
  //TEST
  private static float addHigh = 0;
  
  
  /**
   * Constructor.
   */
  public Character(CharacterMesh mesh, ISceneManager sceneManager)
  {
    //set mesh
    this.mesh = mesh;
    
    //create scene node
    ISceneManager smgr = GameEngine.getSceneManager();
    node = smgr.addAnimatedMeshSceneNode(mesh.getMesh());
    
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_NORMALIZE_NORMALS, true);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);

    //set scene node settings
    node.setAnimationSpeed(mesh.getAnimationSpeed());
    node.setScale(Cast.Vector3ftovector3df(mesh.getScale()));
    node.setRotation(Cast.Vector3ftovector3df(mesh.getRotation()));
    
    //create the shadow
    IAnimatedMesh shadow = sceneManager.getMesh("data/meshes/playershadow.x");
    shadowNode = smgr.addAnimatedMeshSceneNode(shadow);
    node.addChild(shadowNode);
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    shadowNode.setScale(new vector3df(0.3f * u, 1, 0.3f * u));
    shadowNode.setPosition(new vector3df(0,
      //addHigh: every shadow needs its own y-pos (really?)
      addHigh + 0.02f * u, 0));
    addHigh += 0.001f; //TODO: set back to 0 in next match
    shadowNode.setMaterialType(
      E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
    shadowNode.getMaterial(0).setMaterialTypeParam(0.01f);
    
  }
  
  
  /**
   * Gets the current animation stage of this
   * character.
   */
  public AnimationStage getAnimationStage()
  {
    return animationStage;
  }
  
  
  /**
   * Gets the current position of this character.
   */
  public Vector3f getPosition()
  {
    return position;
  }
  
  
  /**
   * Sets the position of this character at once.
   * All actions for this character are deleted.
   * So use this method only in special situations,
   * like when initializing the scene.
   */
  public void setPosition(Vector3f position)
  {
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    this.position = new Vector3f(position);
    this.nativePosition.set(position.x * u, position.y * u, position.z * u);
    node.setPosition(nativePosition);
    removeAllActions();
  }
  
  
  /**
   * Gets the current velocity of this character.
   */
  public Vector3f getVelocity()
  {
    return velocity;
  }
  
  
  /**
   * Gets the current speed of this character.
   */
  public Speed getSpeed()
  {
    return speed;
  }
  
  
  /**
   * Gets the y-rotation (running direction) of this
   * character in degrees.
   */
  public float getRotationY()
  {
    return rotationY;
  }
  
  
  /**
   * Sets the target of this character.
   * The character will go to this position until
   * he reached it or until this method is called
   * again with parameter <code>null</code>.
   * If he is already 0.5 m or nearer to the target,
   * he does not move any more.
   * Call this method only from
   * <code>Action</code>-subclasses.
   */
  public void setTarget(Vector3f target, Speed speed)
  {
    if (target != null &&
      MathTools.distanceSquared(target, position) > 0.5f * 0.5f)
    {
      this.target = new Vector3f(target);
      this.speed = speed;
    }
    else
    {
      this.target = null;
      this.speed = Speed.None;
    }
  }
  
  
  /**
   * Lets the character run to the given position.
   * Works only if the current action is not a
   * RunToAction.
   * Use this for "normal" movement instead of
   * ActionSets.
   */
  public void runTo(Vector3f position, Speed speed)
  {
    if (!currentActionIsRunToAction)
    {
      setTarget(position, speed);
    }
  }
  
  
  /**
   * Shows immediately the given emotion and stops
   * the movement of this character.
   */
  public abstract void setEmotion(Emotion emotion, boolean sound);
  
  
  /**
   * Passes a ball into the given direction.
   * @param ball   The ball to pass.
   * @param dir    The direction the ball will be passed.
   *               Must be normalized. Normal passes have y=0,
   *               but also other values are allowed for
   *               high passes.
   * @param power  Value between 0 (weak pass) and 1 (very powerful).
   * @param force  If <code>true</code>, the ball is passed even when
   *               this character is not in ball possession.
   */
  public abstract void pass(Ball ball, Vector3f dirPass, float power, boolean force);
  
  
  /**
   * Passes a ball to the given character.
   * @param ball       The ball to pass.
   * @param character  The target character
   * @param force      If <code>true</code>, the ball is passed even when
   *                   this character is not in ball possession.
   */
  public abstract void pass(Ball ball, Character character, boolean force);
  
  
  /**
   * Shoots a ball to the given goal.
   * @param ball       The ball to shoot.
   * @param dirShoot   Where to shoot the ball.
   * @param power      Value between 0 (weak shot) and 1 (very powerful).
   * @param goal       -1: right goal (left team), 1: left goal (right team)
   * @param force  If <code>true</code>, the ball is passed even when
   *               this character is not in ball possession.
   */
  public abstract void shoot(Ball ball, ShootDirection dirShoot, float power,
    int goal, boolean force);
  
  
  /**
   * Kicks/throws a ball with the given velocity.
   * @param ball     The ball to pass.
   * @param velocity New velocity of the ball
   * @param anim     If <code>true</code>, kick animation is shown.
   * @param force    If <code>true</code>, the ball is passed even when
   *                 this character is not in ball possession.
   */
  public abstract void kick(Ball ball, Vector3f velocity,
    boolean anim, boolean force);
  
  
  /**
   * Tackles in the running direction.
   */
  public abstract void tackle(Speed speed);
  
  
  /**
   * Tumbles in the running direction.
   */
  public abstract void tumble();
  
  
  /**
   * Dives for the ball with the given velocity.
   * @param direction   Dive direction (normalized).
   * @param speed       Dive speed in m/s.
   */
  public abstract void dive(Vector3f direction, float speed);
  
  
  /**
   * Computes the player's actions, position and animation
   * and so on. Call this function as <code>super.step()</code>
   * at the beginning of the subclass's implementation
   * of this method.
   * The delta time (duration between last call and now)
   * is returned.
   */
  public float step()
  {
    float deltaTime = 0;
    
    //apply next action, if the current one is over
    if (priorityAction != null)
    {
      if (priorityAction.isDone())
      {
        //priority action is over
        priorityAction = null;
      }
    }
    else if (currentActionSet == null)
    {
      //no current action set.
      //use normal waiting.
      setActionSet(defaultActionSet);
    }
    if (getCurrentAction().isDone())
    {
      //the current action is finished.
      //switch to the next action
      currentActionIndex++;
      if (currentActionSet == null ||
        currentActionIndex >= currentActionSet.getActionCount() ||
        nextActionSetAtOnce)
      {
        //this action set is over or the nextActionSetAtOnce-flag is set.
        //switch to the next action set.
        nextActionSetAtOnce = false; //set flag back to false
        if (nextActionSet != null)
        {
          currentActionSet = nextActionSet;
        }
        else
        {
          //there is no next action set. wait.
          currentActionSet = defaultActionSet;
        }
        currentActionIndex = 0;
        nextActionSet = null;
        performCurrentAction();
      }
      else
      {
        //switch to the next action within
        //the current ActionSet
        performCurrentAction();
      }
    }
    
    
    //compute time
    float timeNow = GameEngine.getFrameTime(true);
    if (lastStepTime > 0)
    {
      deltaTime = timeNow - lastStepTime;

      //does the current animation stage allows rotation?
      if (animationStage.isRotationFlexible())
      {
        rotate(deltaTime);
      }
      
      //if a target is set and more than 20 cm away,
      //move the character, dependent on his
      //rotation and speed
      //also move when tackling, tumbling or diving
      float distanceToTargetSquared = 0;
      if (target != null)
        distanceToTargetSquared = MathTools.distanceSquaredWithoutY(target, position);
      if ((target != null && distanceToTargetSquared > 0.2f * 0.2f) ||
        animationStage.isTackling() || animationStage.isTumbling() ||
        animationStage.isDiving())
      {
        if (!animationStage.isRunning() && !animationStage.isTackling()
          && !animationStage.isTumbling() && !animationStage.isDiving())
          setAnimationStage(AnimationStage.RunLeft);
        
        Speed speed;
        if (animationStage.isTackling())
          speed = tackleSpeed;
        else
          speed = this.speed;
        
        if (animationStage.isDiving()) 
        {
          velocity.set(diveDirection);
          velocity.scale(diveSpeed);
        }
        else
        {
          MathTools.computeVectorFromYRotation((int) rotationY, velocity);
          velocity.scale(computeSpeed());
          speed.scale(velocity);
        }
        
        //tackling: slow down
        if (animationStage.isTackling())
        {
          float f = (timeNow - animationStageStartTime) * 0.5f;
          if (f > 1) f = 1;
          f = 1 - f;
          velocity.scale(f * 1.2f); //additional tackling speed: 1.2f
        }
        
        //tumbling: slow down
        if (animationStage.isTumbling())
        {
          float f = (timeNow - animationStageStartTime);
          if (f > 1) f = 1;
          f = 1 - f;
          velocity.scale(f);
        }
        
        //diving: slow down
        if (animationStage.isDiving())
        {
          float f = (timeNow - animationStageStartTime);
          if (f > 1) f = 1;
          f = 1 - f;
          velocity.scale(f);
        }
        
        float deltaPosX = velocity.x * deltaTime;
        float deltaPosZ = velocity.z * deltaTime;
        
        float newPosX = position.x + deltaPosX;
        float newPosZ = position.z + deltaPosZ;
        
        //apply new position
        position.x = newPosX;
        position.z = newPosZ;
        nativePosition.set(position.x * GameEngine.UNITS_GAME_TO_IRRLICHT,
          position.y * GameEngine.UNITS_GAME_TO_IRRLICHT,
          position.z * GameEngine.UNITS_GAME_TO_IRRLICHT);
        node.setPosition(nativePosition);

      }
      else
      {
        if (animationStage.isRunning())
          setAnimationStage(AnimationStage.Wait);
        speed = Speed.None;
        velocity.set(0, 0, 0);
      }

      //check if we are at the end of an animation stage
      updateAnimation();

    }
    lastStepTime = timeNow;

    return deltaTime;
    
  }
  
  
  /**
   * Sets a new action set.
   * If it has a higher priority than
   * the current action set and there is no priority action
   * running, it is immediately applied (if
   * the current action is not interruptible, it is applied
   * after that action).
   * If it has the same or lower priority, it is stored
   * in a 1-element-queue, but only if the queue is empty 
   * or if the element there has a lower or the same
   * priority than the given ActionSet.
   * Otherwise this method has no effect and the given
   * ActionSet is forgotten.
   */
  public void setActionSet(ActionSet actionSet)
  {
    if (currentActionSet == null)
    {
      if (priorityAction == null)
      {
        //there is no current action set or priority action.
        //apply the given action set at once and perform
        //the first action
        currentActionIndex = 0;
        currentActionSet = actionSet;
        performCurrentAction();
      }
      else
      {
        //a priority action is running
        //apply the given action set after the current action.
        nextActionSetAtOnce = true;
        nextActionSet = actionSet;
      }
    }
    else if (actionSet.getPriority() > currentActionSet.getPriority())
    {
      //the given action set has a higher priority than
      //the current one. try to apply it at once.
      if (!getCurrentAction().isInterruptible() ||
        priorityAction != null)
      {
        //the current action is not interruptable or
        //a priority action is running
        //apply the given action set after the current action.
        nextActionSetAtOnce = true;
        nextActionSet = actionSet;
      }
      else
      {
        //the current action is interruptible and there
        //is no priority action running. apply the
        //given action set at once and perform
        //the first action
        currentActionIndex = 0;
        currentActionSet = actionSet;
        performCurrentAction();
      }
    }
    else if (nextActionSet == null ||
      actionSet.getPriority() >= nextActionSet.getPriority())
    {
      //the next action set is not set or the given action set
      //has a higher or the same priority than the next action set.
      //queue the given action set.
      nextActionSet = actionSet;
    }
    else
    {
      //the current and the next action set have a higher
      //priority than the given one. forget the given one
      //and do nothing.
    }
  }
  
  
  /**
   * Sets a priority action (like tumble).
   * This action is immediately
   * performed, even within a current action set.
   * The current action set is continued as soon as
   * the priority action is over.
   */
  public void setPriorityAction(Action priorityAction)
  {
    this.priorityAction = priorityAction;
    priorityAction.perform();
  }
  
  
  /**
   * Gets the current action.
   */
  protected Action getCurrentAction()
  {
    if (priorityAction != null)
      return priorityAction;
    else if (currentActionSet == null)
      return new NoAction(); //should not happen, but it does
    else
      return currentActionSet.getAction(currentActionIndex);
  }
  
  
  /**
   * Performs the current action.
   */
  protected void performCurrentAction()
  {
    Action action = getCurrentAction();
    action.perform();
    currentActionIsRunToAction = (action instanceof RunToAction);
  }
  
  
  /**
   * Remove all actions of this player, including
   * the current one. Only call this method in
   * special situations, for example when
   * initializing the scene after a goal.
   */
  public void removeAllActions()
  {
    currentActionSet = null;
    currentActionIndex = 0;
    nextActionSet = null;
    nextActionSetAtOnce = false;
  }
  
  
  /**
   * Sets the frameloop of a character-<code>IAnimatedMeshSceneNode</code>
   * according to the given animation stage.
   * @param node     The <code>IAnimatedMeshSceneNode</code>, which
   *                 frame loop will be changed
   * @param anim     The new animation stage
   * Returns <code>true</code>, if the given animation was applied,
   * or <code>false</code> if the given animation is not supported
   * by the character subclass.
   */
  public boolean setAnimationStage(AnimationStage anim)
  {
    //first check if animation stage is supported
    int[] frames = mesh.getFrames(anim);
    float speedFactor = mesh.getSpeedFactor(anim) *
      (1 + MathTools.randomCenter(0.1f));
    if (speedFactor == 0f)
      return false;
    
    //apply animation
    if (anim != AnimationStage.None)
    {
      animationStageStartTime = GameEngine.getFrameTime(true);
      lastFrame = frames[0];
      node.setFrameLoop(frames[0], frames[1]);
      node.setAnimationSpeed((int)
        (mesh.getAnimationSpeed() * speedFactor));
      animationStage = anim;
    }
    return true;
  }
  
  
  /**
   * Sets the main texture (body) of the character.
   */
  public void setTexture(ITexture textureBody)
  {
    node.setMaterialTexture(0, textureBody);
  }
  
  
  /**
   * Remove all irrlicht objects from the used ISceneManager.
   * Do not use the character any more after this,
   * because the program can crash (there is no node any more).
   */
  public void removeFromScene()
  {
    node.removeAll();
    node.remove();
  }
  
  
  /**
   * Rotates this character. Call this method only from
   * stop() and only if the current animation
   * stage allows rotation.
   * Each subclass of character has individual rules
   * where to rotate to, so this method is abstract.
   */
  public abstract void rotate(float deltaTime);
  
  
  /**
   * Updates the current animation.
   * If we are at the end of an animation stage,
   * a new animation stage will be started.
   */
  public abstract void updateAnimation();
  
  
  /**
   * Computes the possible speed of this player.
   */
  protected abstract float computeSpeed();
  
  
  /**
   * Method for physics engine. May be overwritten.
   */
  public float getBounce()
  {
    return 0f;
  }


  /**
   * Method for physics engine. May be overwritten.
   */
  public boolean cullBackface()
  {
    return false;
  }


  /**
   * Method for physics engine. May be overwritten.
   */
  public boolean isMoveable()
  {
    //character moves of course, but not with the help
    //of the physics engine
    return false;
  }


  /**
   * Method for physics engine. May be overwritten.
   */
  public int getObjectTypeID()
  {
    return PhysicsObject.TYPE_CHARACTER;
  }


  /**
   * Method for physics engine. May be overwritten.
   */
  public void setPhysicsEnabled(boolean enabled)
  {
    this.physicsEnabled = enabled;
  }
  
  
  /**
   * Method for physics engine. May be overwritten.
   */
  public boolean isPhysicsEnabled()
  {
    return physicsEnabled;
  }
  
  
  /**
   * Hides or shows this character.
   */
  public void setVisible(boolean visible)
  {
    node.setVisible(false);
  }
  
  
  public IAnimatedMeshSceneNode getNode()
  {
    return node;
  }
  
}
