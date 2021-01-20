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
package com.xenoage.bp2k6.match.stadium.floodlight;

import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.UniqueIDCreator;
import com.xenoage.bp2k6.util.language.Language;

import javax.vecmath.Vector3f;


/**
 * Class for a floodlight tower.
 * 
 * @author Andreas Wenger
 */
public class Floodlight
  extends StadiumObject
{
  
  private float scale = 1;
  private int stage = 1;
  private ISceneNode nodeLight;
  
  //for career stadiums: position of the floodlight
  public static final int POSITION_NE = 1;
  public static final int POSITION_SE = 2;
  public static final int POSITION_SW = 3;
  public static final int POSITION_NW = 4;
  private int careerPosition;
  
  
  /**
   * Creates a floodlight tower
   * @param stage           1, 2 or 3
   * @param careerPosition  0 or POSITION_-value
   */
  public Floodlight(int stage, int careerPosition)
  {
    super("floodlight");
    this.stage = stage;
    this.careerPosition = careerPosition;
    if (careerPosition > 0)
    {
      //set to career default position
      Vector3f position = new Vector3f(
        (careerPosition > 2 ? -25 : 25), 0,
        (careerPosition == 1 || careerPosition == 4 ? 24 : -24));
      Vector3f rotation = new Vector3f(
        0, (135 + careerPosition * 90) % 360, 0);
      setPosition(position);
      setRotation(rotation);
    }
  }
  
  
  /**
   * Adds this floodlight tower to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    try
    { 
      //load mesh
      String meshFileName = "floodlight" + stage + ".x";
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Floodlight tower " + stage + "mesh could not be loaded!");
      if (stage == 1)
        scale = 0.5f;
      else if (stage == 2)
        scale = 0.9f;
      else if (stage == 3)
        scale = 0.25f;
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        scale * GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Floodlight tower " + stage + " added to scene.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Floodlight tower " + stage + " could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
    
  }
  
  
  /**
   * Activates the light.
   * Call this method after addToScene()!
   */
  public void setLightOn(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {

    String meshLightFileName = "floodlight" + stage + "_light.x";
    IAnimatedMesh meshLight = sceneManager.getMesh(
      "data/meshes/" + meshLightFileName);
    if (meshLight == null)
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "Floodlight light " + stage + " mesh could not be loaded!");
    nodeLight = SceneNodeFactory.createAnimatedMeshSceneNode(
      sceneManager, meshLight, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
      1, false, false);
    node.addChild(nodeLight);
    nodeLight.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Floodlight light " + stage + " added to scene.");
      
    setPosition(position);
    setRotation(rotation);
    
  }


  /**
   * Gets the position of the floodlight tower
   * (only for career stadiums).
   */
  public int getCareerPosition()
  {
    return careerPosition;
  }


  /**
   * Gets the stage of this floodlight.
   */
  public int getStage()
  {
    return stage;
  }
  
  
  /**
   * Gets the local name of this light.
   */
  public String getLocalName()
  {
    return Language.get("floodlight" + stage);
  }
  
  
  /**
   * Gets the cost of a floodlight with the given stage.
   */
  public static int getCost(int stage)
  {
    if (stage == 1)
      return 500;
    else if (stage == 2)
      return 1500;
    else if (stage == 3)
      return 4000;
    else
      return 0;
  }

  
}