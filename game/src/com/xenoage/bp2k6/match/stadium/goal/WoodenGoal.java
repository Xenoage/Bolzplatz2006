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
import com.xenoage.bp2k6.util.math.Rectangle;


/**
 * Class for an wooden goal.
 * 
 * @author Andreas Wenger
 */
public class WoodenGoal
  implements Goal
{

  //walls of the goal: left post, cross bar, right post,
  //left net, back net, right net, top net
  private Wall walls[] = new Wall[7];
  
  private Vector3f position;
  private float yRotation;

  private ISceneNode node;


  /**
   * Constructor.
   */
  public WoodenGoal(Vector3f position, float yRotation)
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
    Rectangle rects[] = new Rectangle[7];
    
    float bouncePost = 0.25f;
    float bounceNet = 0.1f;

    //left post
    rects[0] = new Rectangle(new Vector3f[] {
      new Vector3f(-2, 0, 0),
      new Vector3f(-2, 2, 0),
      new Vector3f(-1.9f, 1.9f, 0),
      new Vector3f(-1.9f, 0, 0)});
    walls[0] = new Wall(null, bouncePost, PhysicsObject.TYPE_WOODENGOALPOST);

    //cross bar
    rects[1] = new Rectangle(new Vector3f[] {
      new Vector3f(-2, 2, 0),
      new Vector3f(2, 2, 0),
      new Vector3f(2, 1.9f, 0),
      new Vector3f(-2, 1.9f, 0)});
    walls[1] = new Wall(null, bouncePost, PhysicsObject.TYPE_WOODENGOALPOST);

    //right post
    rects[2] = new Rectangle(new Vector3f[] {
      new Vector3f(1.9f, 0, 0),
      new Vector3f(1.9f, 2, 0),
      new Vector3f(2, 2, 0),
      new Vector3f(2, 0, 0)});
    walls[2] = new Wall(null, bouncePost, PhysicsObject.TYPE_WOODENGOALPOST);

    //left net
    rects[3] = new Rectangle(new Vector3f[] {
      new Vector3f(-2, 0, 0),
      new Vector3f(-2, 2, 0),
      new Vector3f(-2, 2, 1.6f),
      new Vector3f(-2, 0, 1.6f)});
    walls[3] = new Wall(null, bounceNet, PhysicsObject.TYPE_GOALNET);

    //back net
    rects[4] = new Rectangle(new Vector3f[] {
      new Vector3f(-2, 0, 1.6f),
      new Vector3f(-2, 2, 0.6f),
      new Vector3f(2, 2, 0.6f),
      new Vector3f(2, 0, 1.6f)});
    walls[4] = new Wall(null, bounceNet, PhysicsObject.TYPE_GOALNET);

    //right net
    rects[5] = new Rectangle(new Vector3f[] {
      new Vector3f(2, 0, 0),
      new Vector3f(2, 0, 1.6f),
      new Vector3f(2, 2, 1.6f),
      new Vector3f(2, 2, 0)});
    walls[5] = new Wall(null, bounceNet, PhysicsObject.TYPE_GOALNET);

    //top net
    rects[6] = new Rectangle(new Vector3f[] {
      new Vector3f(-2, 2, 0),
      new Vector3f(2, 2, 0),
      new Vector3f(2, 2, 0.6f),
      new Vector3f(-2, 2, 0.6f)});
    walls[6] = new Wall(null, bounceNet, PhysicsObject.TYPE_GOALNET);


    //create node
    videoDriver.setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, false);
    String path = "data/meshes/woodengoal.x";
    IAnimatedMesh meshGoal = sceneManager.getMesh(path);

    if (meshGoal == null)
      GameEngine.fatalError(this,
        new Exception("Goal mesh could not be loaded: \"" + path + "\""));

    node = sceneManager.addAnimatedMeshSceneNode(meshGoal);
    //Flowmatix's model: 1:1 scaling
    float meshSizeFactor = GameEngine.UNITS_GAME_TO_IRRLICHT;
    node.setScale(new vector3df(meshSizeFactor, meshSizeFactor, meshSizeFactor));

    node.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);

    node.setPosition(new vector3df(
      position.x * GameEngine.UNITS_GAME_TO_IRRLICHT,
      position.y * GameEngine.UNITS_GAME_TO_IRRLICHT,
      position.z * GameEngine.UNITS_GAME_TO_IRRLICHT));
    node.setRotation(new vector3df(0, yRotation, 0));
    node.updateAbsolutePosition();

    for (int i = 0; i < 7; i++)
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
    if (node != null)
      node.remove();
  }

}