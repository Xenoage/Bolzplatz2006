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

import javax.vecmath.Vector3f;

import net.sf.jirr.ISceneManager;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.Commentator.CommentatorEvent;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.util.MathTools;


/**
 * Class for a goalkeeper.
 *
 * @author   Andreas Wenger
 */
public class Goalkeeper
  extends Player
{


  /**
   * Constructor.
   */
  public Goalkeeper(PlayerInfo info, Team team,
    PlayerPosition playerPosition, GoalkeeperMesh mesh,
    ISceneManager sceneManager, PhysicsManager physicsManager)
  {
    //call super constructor
    super(info, team, playerPosition, mesh, sceneManager, physicsManager);
  }
  
  
  /**
   * Shows immediately the given emotion and stops
   * the movement of this character.
   */
  @Override public void setEmotion(Emotion emotion, boolean sound)
  {
    setTarget(null, null);
    //goalkeeper has no emotions yet.
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
       else
       {
         //default: waiting
         s = AnimationStage.Wait;
       }
       setAnimationStage(s);
     }
     lastFrame = node.getFrameNr();
   }
   
   
   /**
    * Dives for the ball with the given velocity.
    * @param direction   Dive direction (normalized).
    * @param speed       Dive speed in m/s.
    */
  @Override public void dive(Vector3f direction, float speed)
   {
     //rotate to ball
     rotateToBall(false);
     //dive left or right?
     float rotY = (rotationY + 360) % 360; 
     float angle = MathTools.Vector3fToAngle(direction);
     if (angle < rotY) angle += 360;
     if (angle > rotY && angle < rotY + 180)
       setAnimationStage(AnimationStage.DiveLeft);
     else
       setAnimationStage(AnimationStage.DiveRight);
     //set direction and speed
     diveDirection = direction;
     diveSpeed = speed;
   }
   
   
   /**
    * Call this method in every step() when the player
    * is diving for the ball.
    */
  @Override protected void diveStep()
   {
     Ball b = team.getMatch().getBall();
     if (MathTools.distanceSquaredWithoutY(b.getPosition(), position) < 0.5f * 0.5f &&
       b.getPosition().y < 1.8f)
     {
       float bounceSpeed = PlayerSettings.getBounceGoalkeeperBallSpeed() *
         (0.5f + info.getDefense() * 0.1f);
       if (b.getVelocity().length() > bounceSpeed ||
         b.getPosition().y > 1.5f) //ball higher than 1.5 always bounce
       {
         if (b.getVelocity().x * team.getCurrentSide() < 0)
         {
           //do not bounce balls that go away form the goal
           //(like after goal bar/post hits)
         }
         else
         {
           //bounce the ball
           b.setLastPlayer(this); //player touches ball
           ballLostTime = GameEngine.getFrameTime(true);
           //if it was a high ball, a good goalkeeper will perhaps
           //clear to a corner kick (push ball over the bar)
           if (b.getPosition().y > 1.5f &&
             Math.random() * 10 < Math.random() * (2 + getInfo().getDefense()))
           {
             //push over bar (at least try it)
             b.getVelocity().scale(PlayerSettings.getBounceVelFactor());
             b.getVelocity().x = -b.getVelocity().x; //x direction stays the same
             b.getVelocity().y += 5; //push up
           }
           else
           {
             //normal bounce
             b.getVelocity().scale(PlayerSettings.getBounceVelFactor());
           }
           //random bounce velocity change
           b.getVelocity().add(new Vector3f( //additional random direction
             MathTools.randomCenter(PlayerSettings.getBounceVelRandom()),
             MathTools.randomCenter(PlayerSettings.getBounceVelRandom()),
             MathTools.randomCenter(PlayerSettings.getBounceVelRandom())));
           //soundeffect and comment
           SoundEffectPool.Sound.BallKeeper.play();
           if (MathTools.random() < 0.5f)
             team.getMatch().commentatorEvent(CommentatorEvent.ShootSave);
         }
       }
       else
       {
         //catch the ball, even when other player is in
         //ball possession
         b.setPlayer(this, true);
         ballInHands = true;
       }
     }
   }


}

