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
package com.xenoage.bp2k6.match.physics;


public interface PhysicsObject
{

  //return only true if class is instance of MoveablePhysicsObject
  //this method is used instead of <code>instanceof</code>
  //because of performance reasons.
  public boolean isMoveable();

  //return true, if only front face should be used
  public boolean cullBackface();

  //2: double bounce, ..., 1: full bounce, ..., 0: no bounce
  public float getBounce();

  //id of the type of this object
  public static final int TYPE_UNKNOWN = 0;
  public static final int TYPE_FLOOR = 1;
  public static final int TYPE_BALL = 2;
  public static final int TYPE_CHARACTER = 3;
  public static final int TYPE_ALUMINUMGOALPOST = 4;
  public static final int TYPE_WOODENGOALPOST = 5;
  public static final int TYPE_GARBAGECANSGOALPOST = 6;
  public static final int TYPE_GOALNET = 7;
  public static final int TYPE_ADBOARD = 8;
  public static final int TYPE_TRAININGGOAL = 9;
  public static final int TYPE_INVISIBLESTADIUMWALL = 10;
  public static final int TYPE_HOTDOGSTAND = 11;
  public static final int TYPE_BEERBAR = 12;
  public int getObjectTypeID();

  //if not enabled, do not work with it in the physics engine
  public void setPhysicsEnabled(boolean enabled);
  public boolean isPhysicsEnabled();

  /*
  ////body was moved by physics engine... rotation: euler angles
  //public void physicsBodyMoved(Vector3f position, Vector3f rotation);

  //body was moved by physics engine...
  public void physicsBodyMoved(Vector3f position, Vector3f velocity,
    float deltaTime);
  */

}
