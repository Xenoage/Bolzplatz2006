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
package com.xenoage.bp2k6.match.stadium;

import net.sf.jirr.IAnimatedMesh;
import net.sf.jirr.ISceneManager;
import net.sf.jirr.ITexture;
import net.sf.jirr.IVideoDriver;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.UniqueIDCreator;


/**
 * Class for an adboard.
 * 
 * @author Andreas Wenger
 */
public class Adboard
  extends StadiumObject
{
  
  //only for career stadiums: to which block does this adboard belong to?
  private int careerBlockAlignment;
  

  /**
   * Creates an adboard.
   * @param id The id of the adboard ("adboard1" to "adboard4")
   */
  public Adboard(String id)
  {
    super(id);
  }
  
  
  /**
   * Adds this adboard to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    try
    { 
      //load mesh
      String meshFileName = id + ".x";
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Stand mesh \"" +
          meshFileName + "\" could not be loaded!");
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Adboard \"" + id + "\" added to scene.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Adboard \"" + id + "\" could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
    if (physicsManager != null)
      createWall(physicsManager);
  }
  
  
  /**
   * Creates a physical representation of this adboard
   * (for the physics engine).
   */
  public void createWall(PhysicsManager physicsManager)
  {
    new AdboardWall(physicsManager, position, rotation.y);
  }
  
  
  /**
   * Sets the texture of this adboard.
   */
  public void setTexture(ITexture texture)
  {
    node.setMaterialTexture(0, texture);
  }
  
  
  /**
   * Sets this adboard to a block (only for career stadiums).
   */
  public void setCareerBlock(int alignment)
  {
    this.careerBlockAlignment = alignment;
  }
  
  
  /**
   * Gets the alignment of the block, to which this adboard belongs to,
   * or 0 if it does not belong to a career block.
   */
  public int getCareerBlockAlignment()
  {
    return this.careerBlockAlignment;
  }
  
}
