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

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.match.characters.enums.AnimationStage;

import javax.vecmath.Vector3f;
import net.sf.jirr.*;


/**
 * Class that stores a goalkeeper mesh and information
 * about it (for example the meaning of its keyframes).
 *
 * @author   Andreas Wenger
 */
public class GoalkeeperMesh
  implements CharacterMesh
{

  //player-animation-speed in ?/s
  private int aniSpeed = 1000;

  //[animation stage][2 frames: (start, stop)]
  private int keyframes[][] = new int[AnimationStage.animationCount][2];
  //speed factors for each animation
  private float speedFactor[] = new float[AnimationStage.animationCount];

  private static final float keyframeFactor = 33.333f; //.x: 160; //ms3d-mode1: ca 33;

  private final Vector3f scale = new Vector3f(
    0.022f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.020f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.022f * GameEngine.UNITS_GAME_TO_IRRLICHT);
  /* private final Vector3f scale = new Vector3f(
    0.024f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.022f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.024f * GameEngine.UNITS_GAME_TO_IRRLICHT); */ //Vector3f(2.54f, 2.54f, 2.54f); //.x: vector3df(25f, 25f, 25f);
  private final Vector3f rotation = new Vector3f(0f, 0f, 0f); //.x: new vector3df(0f, 180f, 0f)

  /** The mesh of the player (with animation) */
  private IAnimatedMesh meshPlayer;

  public GoalkeeperMesh()
  {
    //milkshape: e.g. frame 0-15 -> irrlicht: frame 0-14....?!
    AnimationStage as = null;
    keyframes[as.RunRight.getID()][0] = 0;
    keyframes[as.RunRight.getID()][1] = 19;
    speedFactor[as.RunRight.getID()] = 2f;
    keyframes[as.RunLeft.getID()][0] = 20;
    keyframes[as.RunLeft.getID()][1] = 39;
    speedFactor[as.RunLeft.getID()] = 2f;
    keyframes[as.ShootStandLeft.getID()][0] = 55;
    keyframes[as.ShootStandLeft.getID()][1] = 69;
    speedFactor[as.ShootStandLeft.getID()] = 1f;
    keyframes[as.ShootStandRight.getID()][0] = 40;
    keyframes[as.ShootStandRight.getID()][1] = 54;
    speedFactor[as.ShootStandRight.getID()] = 1f;
    keyframes[as.Wait.getID()][0] = 0;
    keyframes[as.Wait.getID()][1] = 0;
    speedFactor[as.Wait.getID()] = 1f;
    keyframes[as.DiveLeft.getID()][0] = 70;
    keyframes[as.DiveLeft.getID()][1] = 119;
    speedFactor[as.DiveLeft.getID()] = 1f;
    keyframes[as.DiveRight.getID()][0] = 120;
    keyframes[as.DiveRight.getID()][1] = 169;
    speedFactor[as.DiveRight.getID()] = 1f;
    keyframes[as.ThrowIn.getID()][0] = 170;
    keyframes[as.ThrowIn.getID()][1] = 260;
    speedFactor[as.ThrowIn.getID()] = 1;
    keyframes[as.CupInHands.getID()][0] = 195;
    keyframes[as.CupInHands.getID()][1] = 205;
    speedFactor[as.CupInHands.getID()] = 0.5f;
  }


  /**
   * Loads a player mesh that conforms with the rules
   * in the design document.
   * @return  <code>true</code>, if model was loaded correctly
   *          or <code>false</code> if an error occured.
   */
  public void loadMesh(String filename)
  {
    ISceneManager smgr = GameEngine.getSceneManager();
    meshPlayer = smgr.getMesh(filename);
    //int i = meshPlayer.getFrameCount();
    if (meshPlayer == null)
    {
      GameEngine.fatalError(this, new Exception("Goalkeeper mesh \"" +
        filename + "\" could not be loaded!"));
    }
  }

  /**
   * Gets the animation speed of the mesh in ?*frames/s.
   */
  public int getAnimationSpeed()
  {
    return aniSpeed;
  }

  /**
   * Gets the scale factor of the mesh, that has to be
   * applied so that the player has the right size.
   */
  public Vector3f getScale()
  {
    return scale;
  }

  /**
   * Gets the rotation of the mesh, that has to be applied
   * so that the player looks to the right side.
   */
  public Vector3f getRotation()
  {
    return rotation;
  }



  /**
   * Gets the open mesh or <code>null</code> if no mesh is loaded.
   */
  public IAnimatedMesh getMesh()
  {
    return meshPlayer;
  }
  
  
  /**
   * Gets the start and end frame of the given animation stage.
   */
  public int[] getFrames(AnimationStage animationStage)
  {
    return new int[] {
      (int) (keyframes[animationStage.getID()][0] * keyframeFactor),
      (int) (keyframes[animationStage.getID()][1] * keyframeFactor)};
  }
  
  
  /**
   * Gets the speed factor of the given animation stage.
   * If it is not supported, 0 is returned.
   */
  public float getSpeedFactor(AnimationStage animationStage)
  {
    return speedFactor[animationStage.getID()];
  }


}
