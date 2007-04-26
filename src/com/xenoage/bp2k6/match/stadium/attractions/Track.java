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
package com.xenoage.bp2k6.match.stadium.attractions;

import net.sf.jirr.*;
import net.sf.jirr.IAnimatedMesh;
import net.sf.jirr.ISceneManager;
import net.sf.jirr.IVideoDriver;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.UniqueIDCreator;

import javax.vecmath.Vector3f;


/**
 * Class for an athletic track.
 * 
 * @author Andreas Wenger
 */
public class Track
  extends StadiumObject
{
  

  /**
   * Creates a high jump set.
   */
  public Track()
  {
    super("track");
  }
  
  
  /**
   * Adds this track to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    try
    { 
      //load mesh
      String meshFileName = "track.x";
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Track mesh could not be loaded!");
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        1.1f * GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Track added to scene.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Track could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
    
  }
  
  
  /**
   * Sets the position of this object.
   */
  @Override public void setPosition(Vector3f position)
  {
    this.position = position;
    if (node != null)
    {
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      //place 5 cm higher
      node.setPosition(new vector3df(
        position.x * u, (position.y + 0.005f) * u, position.z * u));
      node.updateAbsolutePosition();
    }
  }
  
  
  /**
   * Sets the rotation of the object.
   */
  @Override public void setRotation(Vector3f rotation)
  {
    this.rotation = rotation;
    if (node != null)
    {
      //flip 180 degrees
      node.setRotation(new vector3df(
        rotation.x + 180, rotation.y, rotation.z));
      node.updateAbsolutePosition();
    }
  }

  
  /**
   * Gets the cost of a track.
   */
  public static int getCost()
  {
    return 8000;
  }
  
  
  /**
   * For career stadium: Move to default position
   */
  public void moveToCareerPosition()
  {
    setPosition(new Vector3f(0, 0, 0));
    setRotation(new Vector3f(0, 0, 0));
  }
  
}