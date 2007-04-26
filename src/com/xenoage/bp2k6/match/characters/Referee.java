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
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.match.referee.RefereeAI;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;

import net.sf.jirr.ISceneManager;
import net.sf.jirr.ITexture;


/**
 * Class for a referee. This class contains
 * all additional methods, that are not
 * in the <code>Character</code>-class
 * but that a referee must have.
 *
 * @author   Andreas Wenger
 */
public class Referee
  extends Character
  implements PhysicsObject
{
  
  //info on this referee
  protected RefereeInfo info;
  
  //AI of this referee
  protected RefereeAI ai;
  
  //sphere used for collision detection
  protected Sphere collisionSphere;
  protected float collisionSphereRadius = 0.5f;
  
  //the current match
  protected Match match;
  
  //texture
  protected ITexture bodyTexture;
  
  
  /**
   * Constructor.
   */
  public Referee(String id, CharacterMesh mesh,
    ISceneManager sceneManager, PhysicsManager physicsManager, Match match)
  {
    //call super constructor
    super(mesh, sceneManager);
    
    //create referee info
    info = new RefereeInfo(id);
    
    //create texture
    String dest = "data/temp/referee-body.png";
    if (info.getColors().createTexture("outfieldplayer", true, dest))
    {
      bodyTexture = GameEngine.getVideoDriver().getTexture(dest);
      new java.io.File(dest).delete();
      node.setMaterialTexture(0, bodyTexture);
    }
    
    //create referee AI
    ai = new RefereeAI(this, info);

    //create collision sphere and enable physics
    collisionSphere = new Sphere(new Vector3f(), collisionSphereRadius);
    if (physicsManager != null)
      physicsManager.addPhysicsObject(this);
    setPhysicsEnabled(true);
    
    //the current match
    this.match = match;

  }
  
  
  /**
   * Computes the referee's actions, position and animation.
   * Call this function in the main loop.
   */
  @Override public float step()
  {
    ai.step();
    float deltaTime = super.step();
    return deltaTime;
  }
  
  
  /**
   * Tumbles in the running direction.
   */
  @Override public void tumble()
  {
    //referee can only tumble if not if not tumbling already
    if (!animationStage.isTumbling())
    {
      if (MathTools.random() < 0.5f)
        setAnimationStage(AnimationStage.TackleTumble1);
      else
        setAnimationStage(AnimationStage.TackleTumble2);
    }
    //referee cries
    SoundEffectPool.Sound.PlayerCry.play();
  }
  
  
  /**
   * Dives for the ball with the given velocity:
   * Not supported.
   */
  @Override public void dive(Vector3f direction, float speeed)
  {
  }

  
  /**
   * Gets the collision sphere of this player-
   */
  public Sphere getCollisionSphere()
  {
    return collisionSphere;
  }
  
  
  
  @Override public boolean setAnimationStage(AnimationStage animationStage)
  {
    return super.setAnimationStage(animationStage);
  }
  
  
  /**
   * Rotates this player. Call this method only if
   * the current animation stage allows rotation.
   * If in ball possession, no rotation is done.
   * If running, he rotates towards his target.
   * If not in ball possession, he rotates towards the ball.
   */
  @Override public void rotate(float deltaTime)
  {
    Vector3f rotationTarget = null;
    
    if (animationStage.isRunning() && target != null)
    {
      //when running, rotate towards the target
      rotationTarget = target;
    }
    else
    {
      //when not running and not in ball possession, rotate towards the ball
      //animation for stand-rotation coule be added here...
      rotationTarget = match.getBall().getPosition();
    }
    
    if (rotationTarget != null &&
      MathTools.distanceSquaredWithoutY(position, rotationTarget) > 0.1f)
    {
      Vector3f dir = new Vector3f();
      dir.sub(rotationTarget, position);

      float dirAngle = MathTools.Vector3fToAngle(dir);
      if (Math.abs(360f + dirAngle - rotationY) % 360f > 5f)
      {
        rotationY = dirAngle;
        nativeRotation.set(
          mesh.getRotation().x,
          mesh.getRotation().y + rotationY,
          mesh.getRotation().z);
        node.setRotation(nativeRotation);
      }
    }
  }
  
  
  /**
   * Computes the possible speed of this player.
   */
  @Override protected float computeSpeed()
  {
    float ret = PlayerSettings.getPlayerSpeed();
    return ret;
  }


  /**
   * Gets the AI of this referee.
   */
  public RefereeAI getAI()
  {
    return ai;
  }
  
  
  /**
   * Passes a ball into the given direction: Not supported.
   */
  @Override public void pass(Ball ball,
    Vector3f dirPass, float power, boolean force)
  {
  }
  
  
  /**
   * Passes a ball to the given player: Not supported.
   */
  @Override public void pass(Ball ball,
    Character character, boolean force)
  {
  }
  
  
  /**
   * Shoots a ball to the given goal: Not supported.
   */
  @Override public void shoot(Ball ball, ShootDirection dirShoot, float power,
    int goal, boolean force)
  {
  }
  
  
  /**
   * Kicks/throws a ball with the given velocity: Not supported.
   */
  @Override public void kick(Ball ball, Vector3f velocity, boolean anim, boolean force)
  {
  }
  
  
  /**
   * Tackles in the running direction: Not supported.
   */
  @Override public void tackle(Speed speed)
  {
  }
  
  
  /**
   * Shows immediately the given emotion and stops
   * the movement of this character.
   */
  @Override public void setEmotion(Emotion emotion, boolean sound)
  {
    setTarget(null, null);
    if (emotion == Emotion.Elation)
    {
      //referee is happy
      if (MathTools.random() < 0.5f)
        setAnimationStage(AnimationStage.ElationApplause);
      else
        setAnimationStage(AnimationStage.ElationJump);
    }
    else if (emotion == Emotion.Desperation)
    {
      //referee is unhappy
      setAnimationStage(AnimationStage.DesperationHeadshake);
    }
  }
  
  
  /**
   * Updates the current animation.
   * If we are at the end of an animation stage,
   * a new animation stage will be started.
   */
  @Override public void updateAnimation()
   {
     if (lastFrame > node.getFrameNr() || animationStage == AnimationStage.None)
     {
       //animation stage is over. use the default
       //follower animation.
       AnimationStage s;
       if (animationStage == AnimationStage.RunLeft)
       {
         s = AnimationStage.RunRight;
       }
       else if (animationStage == AnimationStage.RunRight)
       {
         s = AnimationStage.RunLeft;
       }
       else if (animationStage == AnimationStage.Wait)
       {
         //when waiting, use bored-animation from time to time
         double m = Math.random();
         if (m < 0.05)
         {
           s = AnimationStage.WaitBored1;
         }
         else if (m < 0.1)
         {
           s = AnimationStage.WaitBored2;
         }
         else
         {
           s = AnimationStage.Wait;
         }
       }
       else if (animationStage.isTumbling())
       {
         //stop character after or tumbling
         s = AnimationStage.Wait;
         setTarget(null, null);
       }
       else if (animationStage.isEmotion())
       {
         //emotion is over
         s = AnimationStage.Wait;
       }
       else
       {
         //default: waiting
         s = AnimationStage.Wait;
       }
       setAnimationStage(s);
     }
     lastFrame = node.getFrameNr();
   }


  
  public RefereeInfo getInfo()
  {
    return info;
  }
  
  
  
}