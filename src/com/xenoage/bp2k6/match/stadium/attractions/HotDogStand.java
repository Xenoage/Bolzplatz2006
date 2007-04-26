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

import net.sf.jirr.IAnimatedMesh;
import net.sf.jirr.ISceneManager;
import net.sf.jirr.IVideoDriver;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.physics.PhysicsObject;
import com.xenoage.bp2k6.match.physics.Wall;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.UniqueIDCreator;
import com.xenoage.bp2k6.util.math.Rectangle;

import javax.vecmath.Vector3f;


/**
 * Class for a hot dog stand.
 * 
 * @author Andreas Wenger
 */
public class HotDogStand
  extends StadiumObject
{
  

  /**
   * Creates a hot dog stand.
   */
  public HotDogStand()
  {
    super("hotdogstand");
  }
  
  
  /**
   * Adds this hot dog stand to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    try
    { 
      //load mesh
      String meshFileName = "hotdogstand.x";
      IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
      if (mesh == null)
        throw new Exception("Hot dog stand mesh could not be loaded!");
      node = SceneNodeFactory.createAnimatedMeshSceneNode(
        sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
        0.7f * GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Hot dog stand added to scene.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Hot dog stand could not be added to the scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
    
    if (physicsManager != null)
    {
      Rectangle rect = new Rectangle(
        new Vector3f[] {
        new Vector3f(-2, 0, 3),
        new Vector3f(-2, 3, 3),
        new Vector3f(-2, 3, -3),
        new Vector3f(-2, 0, -3)});
      rect.move(position, rotation.y);
      physicsManager.addPhysicsObject(new Wall(rect, 0.2f, PhysicsObject.TYPE_HOTDOGSTAND));
    }
    
  }
  
  
  /**
   * Gets the cost of a hotdog stand.
   */
  public static int getCost()
  {
    return 1000;
  }
  
  
  /**
   * For career stadium: Move to default position
   */
  public void moveToCareerPosition()
  {
    setPosition(new Vector3f(-28, 0, -3));
    setRotation(new Vector3f(0, 165, 0));
  }

  
}
