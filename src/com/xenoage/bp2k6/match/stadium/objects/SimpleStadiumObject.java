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
package com.xenoage.bp2k6.match.stadium.objects;

import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.util.*;


/**
 * Class for any stadium 3d object.
 * 
 * @author Andreas Wenger
 */
public class SimpleStadiumObject
  extends StadiumObject
{
  
  private float scale = 1;
  private boolean teamAdsTexture = false;
  
  
  /**
   * Creates a 3d stadium object.
   */
  public SimpleStadiumObject(String id)
  {
    super(id);
  }
  
  
  /**
   * Adds this object to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    try
    { 
      //load mesh
      String meshFileName = id;
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Object " + id + " mesh could not be loaded!");
      //load object properties, if there
      String alpha = "";
      int animationSpeed = -1;
      try
      {
        ConfigFile c = new ConfigFile("data/meshes/" + id + ".xml");
        scale = c.getValueFloat("scale");
        alpha = c.getValue("alpha");
        animationSpeed = c.getValueInt("animationspeed");
        teamAdsTexture = (c.getValue("texture").equals("%ads%"));
      }
      catch (Exception ex)
      {
      }
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        scale * GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      if (alpha.equals("channel"))
        node.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
      if (animationSpeed != -1)
        node.setAnimationSpeed(animationSpeed);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Object " + id + " added to scene.");
      
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Object " + id + " could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
  }


  /**
   * Returns true, if this object uses the
   * team ads texture.
   */
  public boolean isTeamAdsTexture()
  {
    return teamAdsTexture;
  }
  
  
  /**
   * Sets the texture of this adboard.
   */
  public void setTexture(ITexture texture)
  {
    node.setMaterialTexture(0, texture);
  }
  
  
  /**
   * Sets the scaling factor.
   */
  public void setScale(float scale)
  {
    node.setScale(new vector3df(scale, scale, scale));
  }

  
}