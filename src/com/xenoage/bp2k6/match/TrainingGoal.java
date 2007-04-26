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
package com.xenoage.bp2k6.match;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.util.math.*;
import javax.vecmath.Vector3f;
import net.sf.jirr.*;

//TraingGoal (german: "Torwand"). A wall with 2 holes in it
//to practice goal shooting.
public class TrainingGoal
  extends ComplexWall
{

  private ISceneNode nodeTrainingGoal;

  //constructor
  public TrainingGoal(MatchManager matchManager, Vector3f position, float yRotation)
  {

    super(null, 0.5f, PhysicsObject.TYPE_TRAININGGOAL);

    Rectangle rects[] = new Rectangle[6];
    for (int i = 0; i < 6; i++)
      rects[i] = new Rectangle();

    rects[0].setP(new Vector3f[] {
      new Vector3f(-2, 2, 0),
      new Vector3f(2, 2, 0),
      new Vector3f(2, 1.75f, 0),
      new Vector3f(-2, 1.75f, 0)});
    rects[1].setP(new Vector3f[] {
      new Vector3f(-2, 1.75f, 0),
      new Vector3f(0.8f, 1.75f, 0),
      new Vector3f(0.8f, 1, 0),
      new Vector3f(-2, 1, 0)});
    rects[2].setP(new Vector3f[] {
      new Vector3f(1.6f, 1.75f, 0),
      new Vector3f(2, 1.75f, 0),
      new Vector3f(2, 1, 0),
      new Vector3f(1.6f, 1, 0)});
    rects[3].setP(new Vector3f[] {
      new Vector3f(-2, 1, 0),
      new Vector3f(-1.6f, 1, 0),
      new Vector3f(-1.6f, 0.25f, 0),
      new Vector3f(-2, 0.25f, 0)});
    rects[4].setP(new Vector3f[] {
      new Vector3f(-0.8f, 1, 0),
      new Vector3f(2, 1, 0),
      new Vector3f(2, 0.25f, 0),
      new Vector3f(-0.8f, 0.25f, 0)});
    rects[5].setP(new Vector3f[] {
      new Vector3f(-2, 0.25f, 0),
      new Vector3f(2, 0.25f, 0),
      new Vector3f(2, 0, 0),
      new Vector3f(-2, 0, 0)});

    super.setRectangles(rects);


    ISceneManager smgr = GameEngine.getSceneManager();
    //create node
    IAnimatedMesh meshTrainingGoal = smgr.getMesh("data/meshes/torwand.x");
    nodeTrainingGoal = smgr.addAnimatedMeshSceneNode(meshTrainingGoal);
    float meshSizeFactor = 2.5f;
    nodeTrainingGoal.setScale(new vector3df(meshSizeFactor, meshSizeFactor, meshSizeFactor));

    nodeTrainingGoal.setPosition(new vector3df(
      position.x * GameEngine.UNITS_GAME_TO_IRRLICHT,
      position.y * GameEngine.UNITS_GAME_TO_IRRLICHT,
      position.z * GameEngine.UNITS_GAME_TO_IRRLICHT));
    nodeTrainingGoal.setRotation(new vector3df(0, yRotation, 0));

    for (int i = 0; i < 6; i++)
      rects[i].move(position, yRotation);

    matchManager.getPhysicsManager().addPhysicsObject(this);
  }



}
