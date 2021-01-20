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

import javax.vecmath.Vector3f;
import net.sf.jirr.*;
import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.util.*;


/**
 * Class for a football.
 *
 * @author Andi
 */
public class Ball
  implements MoveablePhysicsObject, SphereInterface
{

  private Vector3f pos; //current position of the ball (in meters)
  private Vector3f vel; //current velocity of the ball (in meters/second)

  private float radius; //radius of the ball (in meters)

  private Player player; //the player that possesses the ball, or null
  private Player lastPlayer; //the last player that was in possession of this ball
  private boolean inHands; //ball in the hands of a keeper?

  private ISceneNode nodeBall; //irrlicht ball node
  private vector3df nativePos; //irrlicht position of the ball
  private vector3df nativeRot; //irrlicht rotation of the ball
  private matrix4 nativeMRot; //irrlicht rotation matrix of the ball
  private matrix4 nativeMTemp; //irrlicht temp rotation matrix

  private ISceneNode nodeBallShadow; //irrlicht ball shadow node
  private vector3df nativeBallShadowPos; //irrlicht ball shadow position
  
  //physics
  private boolean physicsEnabled = true;


  //constructor
  public Ball(MatchManager matchManager)
  {

    radius = 0.2f; //0.25f;

    ISceneManager smgr = GameEngine.getSceneManager();

    //create ball

    String path = "data/meshes/ball.x";
    IAnimatedMesh meshBall = smgr.getMesh(path);

    if (meshBall == null)
      GameEngine.fatalError(this,
        new Exception("Ball mesh could not be loaded: \"" + path + "\""));


    nodeBall = smgr.addAnimatedMeshSceneNode(meshBall);
    float meshBallSizeFactor = radius * 2 / 5.0f * GameEngine.UNITS_GAME_TO_IRRLICHT;
    nodeBall.setScale(new vector3df(meshBallSizeFactor, meshBallSizeFactor, meshBallSizeFactor));
    //nodeBall.setMaterialTexture(0, driver.getTexture("media/ball.tga"));
    nodeBall.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);

    pos = new Vector3f();
    nativePos = nodeBall.getPosition();
    vel = new Vector3f(0, 0, 0); //(0, 2, 10);
    nativeRot = nodeBall.getRotation();

    nativeMRot = new matrix4();
    nativeMRot.setRotationDegrees(new vector3df(0, 0, 0));
    nativeMTemp = new matrix4();

    //create ball shadow
    //this is no "real shadow" computed by the Irrlicht engine, but
    //a mesh we control.
    path = "data/meshes/ballshadow.x";
    IAnimatedMesh meshBallShadow = smgr.getMesh(path);

    if (meshBallShadow == null)
      GameEngine.fatalError(this,
        new Exception("Ball shadow mesh could not be loaded: \"" + path + "\""));

    nodeBallShadow = smgr.addAnimatedMeshSceneNode(meshBallShadow);

    /*
    path = "data/meshes/ballshadow.x";
    ITexture textureBallShadow = MediaLoader.loadTexture("meshes/ballshadow.png");

    if (textureBallShadow == null)
      GameEngine.fatalError(this,
        new Exception("Ball shadow texture could not be loaded: \"" + path + "\""));

    nodeBallShadow.setMaterialTexture(0, textureBallShadow);
*/

    nodeBallShadow.setScale(new vector3df(
      radius * 2 * GameEngine.UNITS_GAME_TO_IRRLICHT,
      1f * GameEngine.UNITS_GAME_TO_IRRLICHT,
      radius * 2 * GameEngine.UNITS_GAME_TO_IRRLICHT));
    nativeBallShadowPos = new vector3df();
    nodeBallShadow.setPosition(nativeBallShadowPos);
    nodeBallShadow.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
    nodeBallShadow.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);

    setPosition(new Vector3f(0, 0.5f, 0));
    matchManager.getPhysicsManager().addPhysicsObject(this);
  }


  public boolean isMoveable()
  {
    return true;
  }

  public boolean cullBackface()
  {
    return true;
  }


  public Vector3f getPosition()
  {
    return pos;
  }


  public Vector3f getVelocity()
  {
    return vel;
  }
  
  
  /**
   * Gets the x/z-position of the ball (y = 0)
   * in 0.2 s.
   */
  public Vector3f getFutureXZPosition()
  {
    Vector3f ret = new Vector3f();
    ret.x = pos.x + 0.2f * vel.x;
    ret.z = pos.z + 0.2f * vel.z;
    return ret;
  }


  public void setPosition(Vector3f p)
  {
    float deltaPosX = p.x - pos.x;
    float deltaPosZ = p.z - pos.z;

    pos.set(p);
    
    //error reduction
    if (pos.y < radius - 0.01f)
    {
      pos.y = getMinYPosition();
    }
    
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    nativePos.set(pos.x * u, pos.y * u, pos.z * u);
    nodeBall.setPosition(nativePos);
    nativeBallShadowPos.set((pos.x - radius) * u, 0.03f * u,
      (pos.z - radius - 0.1f) * u);
    nodeBallShadow.setPosition(nativeBallShadowPos);

    //rotation (only a visual effect)
    if (vel.z != 0 || vel.x != 0)
    {
      float distance =
        (float) Math.sqrt(deltaPosX * deltaPosX + deltaPosZ * deltaPosZ);
      //rotation angle = distance / (2 * pi * radius) * (2 * pi) * rotSpeed
      float rotAngle = distance / radius * 0.5f; //rotSpeed;
      Vector3f v = new Vector3f(vel.z, 0, -vel.x);
      MathTools.normalize(v);
      MathTools.buildRotationMatrix(rotAngle, v, nativeMTemp);
      nativeMRot = nativeMTemp.multiply(nativeMRot);
      nativeRot = nativeMRot.getRotationDegrees();
      nodeBall.setRotation(nativeRot);
    }

  }


  public void setVelocity(Vector3f vel)
  {
    this.vel.set(vel);
  }


  public float getRadius()
  {
    return radius;
  }


  public float getBounce()
  {
    return 0.8f;
  }


  public vector3df getNativePos()
  {
    return nativePos;
  }

  public int getObjectTypeID()
  {
    return PhysicsObject.TYPE_BALL;
  }

  
  /**
   * Gets the player that possesses the ball at the moment.
   */
  public Player getPlayer()
  {
    return player;
  }

  
  /**
   * Sets the player that possesses the ball at the moment,
   * or <code>null</code> if the player loses it.
   */
  public void setPlayer(Player player, boolean inHands)
  {
    if (this.player != null)
    {
      this.player.setBall(null, false); //old player loses ball
      this.inHands = false;
    }

    if (player != null)
      player.setBall(this, inHands);
    this.player = player;
    this.inHands = inHands;

    if (player != null) lastPlayer = player;
  }

  
  /**
   * Gets the last player that was in possession of the ball.
   */
  public Player getLastPlayer()
  {
    return lastPlayer;
  }
  

  /**
   * Sets the last player that was in possession of the ball.
   * (Only needed in special situations with no real
   * ball-possession like goal kick or throw in)
   */
  public void setLastPlayer(Player lastPlayer)
  {
    this.lastPlayer = lastPlayer;
  }



  /**
   * Gets the y-position for this ball lying on the floor
   * (this is radius + a very small value).
   */
  public float getMinYPosition()
  {
    return radius + 0.01f;
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
   * Remove all irrlicht objects from the used ISceneManager
   */
  public void removeFromScene()
  {
    nodeBall.remove();
    nodeBallShadow.remove();
  }


  /**
   * Returns true if the ball is currently
   * in the hands of a keeper.
   */
  public boolean isInHands()
  {
    return inHands;
  }


}