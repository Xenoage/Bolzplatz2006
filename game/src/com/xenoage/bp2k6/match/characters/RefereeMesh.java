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
import com.xenoage.bp2k6.match.characters.enums.*;

import javax.vecmath.Vector3f;
import net.sf.jirr.*;


/**
 * Class that stores a referee mesh and information
 * about it (for example the meaning of its keyframes).
 *
 * @author   Andreas Wenger
 */
public class RefereeMesh
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
    0.02f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.02f * GameEngine.UNITS_GAME_TO_IRRLICHT,
    0.02f * GameEngine.UNITS_GAME_TO_IRRLICHT); //Vector3f(2.54f, 2.54f, 2.54f); //.x: vector3df(25f, 25f, 25f);
  private final Vector3f rotation = new Vector3f(0f, 0f, 0f); //.x: new vector3df(0f, 180f, 0f)

  /** The mesh of the player (with animation) */
  private IAnimatedMesh meshPlayer;

  public RefereeMesh()
  {
    //milkshape: e.g. frame 0-15 -> irrlicht: frame 0-14....?!
    AnimationStage as = null;
    keyframes[as.RunRight.getID()][0] = 0;
    keyframes[as.RunRight.getID()][1] = 19;
    speedFactor[as.RunRight.getID()] = 2f;
    keyframes[as.RunLeft.getID()][0] = 20;
    keyframes[as.RunLeft.getID()][1] = 39;
    speedFactor[as.RunLeft.getID()] = 2f;
    keyframes[as.Wait.getID()][0] = 485;
    keyframes[as.Wait.getID()][1] = 534;
    speedFactor[as.Wait.getID()] = 1f;
    keyframes[as.WaitBored1.getID()][0] = 295;
    keyframes[as.WaitBored1.getID()][1] = 454;
    speedFactor[as.WaitBored1.getID()] = 1f;
    keyframes[as.WaitBored2.getID()][0] = 535;
    keyframes[as.WaitBored2.getID()][1] = 619;
    speedFactor[as.WaitBored2.getID()] = 1f;
    keyframes[as.ElationJump.getID()][0] = 455;
    keyframes[as.ElationJump.getID()][1] = 484;
    speedFactor[as.ElationJump.getID()] = 1f;
    keyframes[as.ElationApplause.getID()][0] = 620;
    keyframes[as.ElationApplause.getID()][1] = 689;
    speedFactor[as.ElationApplause.getID()] = 1f;
    keyframes[as.DesperationHeadshake.getID()][0] = 690;
    keyframes[as.DesperationHeadshake.getID()][1] = 769;
    speedFactor[as.DesperationHeadshake.getID()] = 1f;
    keyframes[as.TackleTumble1.getID()][0] = 115;
    keyframes[as.TackleTumble1.getID()][1] = 169;
    speedFactor[as.TackleTumble1.getID()] = 1f;
    keyframes[as.TackleTumble2.getID()][0] = 170;
    keyframes[as.TackleTumble2.getID()][1] = 239;
    speedFactor[as.TackleTumble2.getID()] = 1f;
    keyframes[as.TackleJump1.getID()][0] = 240;
    keyframes[as.TackleJump1.getID()][1] = 264;
    speedFactor[as.TackleJump1.getID()] = 1f;
    keyframes[as.TackleJump2.getID()][0] = 265;
    keyframes[as.TackleJump2.getID()][1] = 294;
    speedFactor[as.TackleJump2.getID()] = 1f;
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
      GameEngine.fatalError(this, new Exception("Referee mesh \"" +
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
