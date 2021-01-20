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
package com.xenoage.bp2k6.match;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.math.*;
import java.util.ArrayList;
import javax.vecmath.Vector3f;


public class PhysicsManager
{

  //gravity in our world
  Vector3f gravity;
  //list of all fixed and moveable objects in the world
  ArrayList<PhysicsObject> objects;
  //the last time step() was called
  private float lastTime;


  public PhysicsManager()
  {
    //set gravity along y axis
    gravity = new Vector3f(0f, -9.81f, 0f);
    //create list for objects
    objects = new ArrayList<PhysicsObject>();
  }


  /**
   * Adds the floor (soccer field)
   */
  public void addFloor()
  {
    //floor
    Rectangle r1 = new Rectangle();
    r1.setP(new Vector3f[] { new Vector3f(-100, 0, 100), new Vector3f(100, 0, 100),
      new Vector3f(100, 0, -100), new Vector3f(-100, 0, -100) });
    Wall w1 = new Wall(r1, 0.7f, PhysicsObject.TYPE_FLOOR);
    objects.add(w1);
  }


  public void addPhysicsObject(PhysicsObject o)
  {
    objects.add(o);
  }


  /**
   * Step simulation ahead
   */
  public void step(Match match)
  {
    if (lastTime == 0)
    {
      lastTime = GameEngine.getFrameTime(true);
    }
    else
    {
      float timeNow = GameEngine.getFrameTime(true);
      float deltaTime = timeNow - lastTime;
      lastTime = timeNow;
      //System.out.println(timeNow);
      if (deltaTime > 0)
      {
        //move objects
        Vector3f pos = new Vector3f();
        Vector3f vel = new Vector3f();
        Vector3f grav = new Vector3f();
        Vector3f move = new Vector3f();
        Vector3f newPos = new Vector3f();
        Vector3f newVel = new Vector3f();
        Vector3f newPosC = new Vector3f();
        Vector3f newVelC = new Vector3f();
        for (PhysicsObject o : objects)
        {
          //enabled?
          if (o.isPhysicsEnabled())
          {
            //moveable?
            if (o.isMoveable())
            {
              MoveablePhysicsObject mo = (MoveablePhysicsObject) o;
              //calculate new position
              pos.set(mo.getPosition());
              vel.set(mo.getVelocity());
              //gravity
              grav.set(gravity);
              grav.scale(deltaTime);
              if (grav.lengthSquared() < 1) //error reduction: more that 1 m/s^2 not allowed
                vel.add(grav);
              else
                continue;
              move.set(vel);
              move.scale(deltaTime);
              newPos.set(pos);
              newPos.add(move);
              if (Float.isNaN(newPos.x))
                newPos = null;
              newVel.set(vel);
              //check for collision with other fixed objects
              for (int iC = 0; iC < objects.size(); iC++)
              {
                PhysicsObject oC = objects.get(iC);
                if (oC.isPhysicsEnabled() && !oC.isMoveable())
                {
                  if (Collision.collisionPhysicsObjects(pos, vel, newPos, mo, oC,
                    newPosC, newVelC))
                  {

                    newPos.set(newPosC);
                    newVel.set(newVelC);
                    newVel.scale(oC.getBounce());

                    //post hit
                    if (Math.random() < 0.5 &&
                      (oC.getObjectTypeID() == PhysicsObject.TYPE_ALUMINUMGOALPOST ||
                        oC.getObjectTypeID() == PhysicsObject.TYPE_WOODENGOALPOST ||
                        oC.getObjectTypeID() == PhysicsObject.TYPE_GARBAGECANSGOALPOST))
                    {
                      match.commentatorEvent(Commentator.CommentatorEvent.PostHit);
                    }
                    
                    //play sounds, create particle systems
                    if (oC.getObjectTypeID() == PhysicsObject.TYPE_ADBOARD)
                      SoundEffectPool.Sound.BallBoard.play();
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_ALUMINUMGOALPOST)
                      SoundEffectPool.Sound.BallAluminum.play();
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_WOODENGOALPOST)
                      SoundEffectPool.Sound.BallWood.play();
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_GARBAGECANSGOALPOST)
                    {
                      SoundEffectPool.Sound.BallGarbageCan.play();
                      if (MathTools.random() < 0.2f)
                        SoundEffectPool.Sound.SpecialMeow.play();
                      //banana peels
                      match.createSimpleParticleSystem(
                        new Vector3f(pos.x, 1.5f, pos.z),
                        "data/meshes/bananapeel.png");
                    }
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_HOTDOGSTAND)
                    {
                      SoundEffectPool.Sound.BallBoard.play();
                      //hot dogs
                      match.createSimpleParticleSystem(
                        new Vector3f(pos.x, 1.5f, pos.z),
                        "data/meshes/sausage.png");
                    }
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_BEERBAR)
                    {
                      SoundEffectPool.Sound.BallBoard.play();
                      //beer cans
                      match.createSimpleParticleSystem(
                        new Vector3f(pos.x, 1.5f, pos.z),
                        "data/meshes/beercan.png");
                    }
                    else if (oC.getObjectTypeID() == PhysicsObject.TYPE_TRAININGGOAL)
                      SoundEffectPool.Sound.BallBoard.play();
                    else if (newVelC.y > 1f)
                      SoundEffectPool.Sound.BallLawn.play();

                    break;
                  }
                }
              }
              //avoid ball flutter on the ground
              if (mo.getObjectTypeID() == PhysicsObject.TYPE_BALL)
              {
                Ball ball = (Ball) mo;
                if (ball.getPosition().y < ball.getMinYPosition() + 0.1)
                {
                  if (ball.getVelocity().lengthSquared() < 0.1f * 0.1f)
                  {
                    newPos.y = ball.getMinYPosition();
                    newVel.y = 0;
                  }
                }
              }
              //apply new position and velocity
              mo.setVelocity(newVel);
              mo.setPosition(newPos);
            }
          }
        }
      }
    }
  }


  public boolean checkPlayerObjectCollision(Player player)
  {
    //check for collision with other fixed objects
    for (int iC = 0; iC < objects.size(); iC++)
    {
      PhysicsObject oC = objects.get(iC);
      //object must be fixed, and may not be the player itself
      //or the floor
      if (!oC.isMoveable() && oC != player &&
        !(oC.getObjectTypeID() == PhysicsObject.TYPE_FLOOR))
      {
        if (Collision
          .intersectionSpherePhysicsObjects(player.getCollisionSphere(), oC))
        {
          return true;
        }
      }
    }
    return false;
  }


  /**
   * Clears the physics manager
   * (remove all objects).
   */
  public void clear()
  {
    objects.clear();
  }

}
