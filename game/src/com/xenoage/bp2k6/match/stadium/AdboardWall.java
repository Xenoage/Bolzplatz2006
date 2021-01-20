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

import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.physics.*;

import javax.vecmath.Vector3f;


/**
 * This class is the physical representation of
 * an adboard (for the physics engine).
 * 
 * @author Andreas Wenger
 */
public class AdboardWall
  extends Wall
{

  //constructor
  public AdboardWall(PhysicsManager physicsManager, Vector3f position, float yRotation)
  {

    super(null, 0.5f, PhysicsObject.TYPE_ADBOARD);
    rect.setP(new Vector3f[] {
      new Vector3f(-4, 1.35f, 0),
      new Vector3f(4, 1.35f, 0),
      new Vector3f(4, 0, -0.25f),
      new Vector3f(-4, 0, -0.25f)});

    rect.move(position, yRotation);

    physicsManager.addPhysicsObject(this);
  }



}