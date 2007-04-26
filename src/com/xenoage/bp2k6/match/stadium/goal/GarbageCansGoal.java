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
package com.xenoage.bp2k6.match.stadium.goal;

import javax.vecmath.Vector3f;
import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.PhysicsManager;
import com.xenoage.bp2k6.match.physics.PhysicsObject;
import com.xenoage.bp2k6.match.physics.Wall;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.math.Rectangle;


/**
 * Class for a goal consisting of two garbage cans.
 * 
 * @author Andreas Wenger
 */
public class GarbageCansGoal
  implements Goal
{

  //walls of the goal: left post, cross bar, right post,
  //left net, back net, right net, top net
  private Wall walls[] = new Wall[7];
  
  private Vector3f position;
  private float yRotation;

  private ISceneNode nodes[] = new ISceneNode[2];


  /**
   * Constructor.
   * @param physicsManager May be <code>null</code> if no physics are needed.
   */
  public GarbageCansGoal(Vector3f position, float yRotation)
  {
    this.position = position;
    this.yRotation = yRotation;
  }
  
  
  /**
   * Adds this goal to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
    
    Rectangle rects[] = new Rectangle[2];
    
    float bouncePost = 0.2f;

    //left post
    rects[0] = new Rectangle(new Vector3f[] {
      new Vector3f(-2.1f, 0, 0),
      new Vector3f(-2.1f, 1.2f, 0),
      new Vector3f(-1.8f, 1.2f, 0),
      new Vector3f(-1.8f, 0, 0)});
    walls[0] = new Wall(null, bouncePost, PhysicsObject.TYPE_GARBAGECANSGOALPOST);

    //right post
    rects[1] = new Rectangle(new Vector3f[] {
      new Vector3f(1.8f, 0, 0),
      new Vector3f(1.8f, 1.2f, 0),
      new Vector3f(2.1f, 1.2f, 0),
      new Vector3f(2.1f, 0, 0)});
    walls[1] = new Wall(null, bouncePost, PhysicsObject.TYPE_GARBAGECANSGOALPOST);


    //create node
    String path = "data/meshes/garbagecan.x";
    IAnimatedMesh meshGoal = sceneManager.getMesh(path);

    if (meshGoal == null)
      GameEngine.fatalError(this,
        new Exception("Goal mesh could not be loaded: \"" + path + "\""));

    for (int i = 0; i < 2; i++)
    {
      nodes[i] = sceneManager.addAnimatedMeshSceneNode(meshGoal);
      //float meshSizeFactor = 0.025f * GameEngine.UNITS_GAME_TO_IRRLICHT;
      float meshSizeFactor = 0.035f * GameEngine.UNITS_GAME_TO_IRRLICHT;
      nodes[i].setScale(new vector3df(meshSizeFactor, meshSizeFactor, meshSizeFactor));
  
      nodes[i].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
      nodes[i].setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
  
      nodes[i].setPosition(new vector3df(
        position.x * GameEngine.UNITS_GAME_TO_IRRLICHT,
        position.y * GameEngine.UNITS_GAME_TO_IRRLICHT,
        (position.z - 2 + i * 4) * GameEngine.UNITS_GAME_TO_IRRLICHT));
      nodes[i].setRotation(new vector3df(0,
        yRotation + MathTools.random(360), 0));
      nodes[i].updateAbsolutePosition();
    }

    for (int i = 0; i < 2; i++)
    {
      rects[i].move(position, yRotation);
      walls[i].setRectangle(rects[i]);
      if (physicsManager != null)
        physicsManager.addPhysicsObject(walls[i]);
    }

  }
  
  
  /**
   * Removes this goal from the scene.
   */
  public void removeFromScene()
  {
    for (int i = 0; i < 2; i++)
    {
      if (nodes[i] != null)
        nodes[i].remove();
    }
  }

}