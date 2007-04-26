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
package com.xenoage.bp2k6.match.stadium.scoreboard;

import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.util.*;

import javax.vecmath.Vector3f;


/**
 * Class for a scoreboard.
 * 
 * @author Andreas Wenger
 */
public class Scoreboard
  extends StadiumObject
{
  
  private int stage = 1;
  
  private float scale = 1;
  private int digitMaterialIndex[] = new int[2];
  
  private IVideoDriver videoDriver;
  
  
  /**
   * Creates a scoreboard
   * @param stage           1, 2 or 3
   */
  public Scoreboard(int stage)
  {
    super("scoreboard");
    this.stage = stage;
  }
  
  
  /**
   * Adds this scoreboard to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    this.videoDriver = videoDriver;
    try
    { 
      //load mesh
      String meshFileName = "scoreboard" + stage + ".x";
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Scoreboard " + stage + "mesh could not be loaded!");
      if (stage == 1)
      {
        scale = 0.2f;
        digitMaterialIndex[0] = 15;
        digitMaterialIndex[1] = 16;
      }
      else if (stage == 2)
      {
        scale = 1.5f;
        digitMaterialIndex[0] = 1;
        digitMaterialIndex[1] = 2;
      }
      else if (stage == 3)
      {
        scale = 0.03f;
        digitMaterialIndex[0] = 2;
        digitMaterialIndex[1] = 3;
      }
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        scale * GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      node.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Scoreboard " + stage + " added to scene.");
      
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Scoreboard " + stage + " could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
  }


  /**
   * Gets the stage of this scoreboard.
   */
  public int getStage()
  {
    return stage;
  }
  
  
  /**
   * Sets the displayed score.
   * @param side   0: left side, 1: right side
   * @param score  number between 0 and 9
   */
  public void setScore(int side, int score)
  {
    side = MathTools.clamp(side, 0, 1);
    score = MathTools.clamp(score, 0, 9);
    if (node != null)
    {
      String s = (side == 0 ? "l" : "r");
      node.getMaterial(digitMaterialIndex[side]).setTexture(0,
        videoDriver.getTexture("data/meshes/scoreboard" +
          stage + "_" + s + score + ".png"));
    }
  }
  
  
  /**
   * Gets the cost of a scoreboard with the given stage.
   */
  public static int getCost(int stage)
  {
    if (stage == 1)
      return 500;
    else if (stage == 2)
      return 2500;
    else if (stage == 3)
      return 8000;
    else
      return 0;
  }
  
  
  /**
   * For career stadium scoreboard: Move to
   * default career position
   */
  public void moveToCareerPosition()
  {
    Vector3f position = new Vector3f();
    Vector3f rotation = new Vector3f();
    if (stage == 1)
    {
      position.set(5, 0, 17);
    }
    else if (stage == 2)
    {
      position.set(0, 3, 24);
    }
    else if (stage == 3)
    {
      position.set(0, 13, 29);
    }
    setPosition(position);
    setRotation(rotation);
  }
  
  
  /**
   * Gets the crowd factor (attendance) for this scoreboard.
   */
  public float getCrowdAttendance()
  {
    if (stage == 1)
      return 0.8f;
    else if (stage == 2)
      return 0.9f;
    else if (stage == 3)
      return 1.0f;
    else
      return 0;
  }
  
}