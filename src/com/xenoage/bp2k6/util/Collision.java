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
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.math.*;
import javax.vecmath.*;

public class Collision
{

  /**
   * Checks, if a <code>MoveablePhysicsObject</code> hits
   * another <code>PhysicsObject</code> on its way from
   * <code>oldPos</code> to <code>newPos</code>. In this case
   * <code>true</code> is returned, <code>retPosNew</code>
   * is set to new target point after reflection and
   * <code>retVelNew</code> is set to the new velocity,
   * otherwise <code>false</code> is returned.
   * <code>MoveablePhysicsObject</code> objects.
   */
  public static boolean collisionPhysicsObjects(
    Vector3f posOld, Vector3f velOld, Vector3f posNew,
    MoveablePhysicsObject mo, PhysicsObject o,
    Vector3f retPosNew, Vector3f retVelNew)
  {
    if (mo instanceof SphereInterface)
    {
      SphereInterface sphere = (SphereInterface) mo;
      //check if a sphere collides with another object...
      if (o instanceof Wall)
      {
        //check if sphere collides with wall
        Wall wall = (Wall) o;
        return collisionMovingSphereRectangle(posOld, velOld, posNew,
          sphere.getRadius(), wall.getRectangle(),
          o.cullBackface(), retPosNew, retVelNew);
      }
      else if (o instanceof ComplexWall)
      {
        //check if sphere collides with complex wall
        ComplexWall complexWall = (ComplexWall) o;
        for (int i = 0; i < complexWall.getRectangleCount(); i++)
        {
          if (collisionMovingSphereRectangle(posOld, velOld, posNew,
          sphere.getRadius(), complexWall.getRectangle(i),
          o.cullBackface(), retPosNew, retVelNew))
          {
            return true;
          }
        }
        return false;
      }
      else
      {
        //add other collision objects here if needed...
      }
    }
    else
    {
      //add other MoveablePhysicsObjects here if needed...
    }
    return false;
  }

  //check if a moving sphere collides with a rectangle
  public static boolean collisionMovingSphereRectangle(
    Vector3f posOld, Vector3f velOld, Vector3f posNew,
    float radius, Rectangle rect, boolean cullBackface,
    Vector3f retPosAfterReflection, Vector3f retVelAfterReflection)
  {
    //we can ignore the radius of the spere, if
    //we simply move the rectangle <radius> units in the direction
    //of its normal vector.
    //after that we move the edges of the rectangle in
    //all four directions <radius> units outward.
    //then we have just to test
    //the center point of the sphere
    Rectangle newRect = new Rectangle(rect);
    if (cullBackface)
    {
      //if backface culling activated:
      //move plane in normal vector direction
      newRect.moveEdgesUp(radius);
    }
    else
    {
      //if backface culling deactivated:
      //if sphere is flying to the plane "from behind", move plane
      //in inverse normal vector direction, else in normal vector
      //direction
      Vector3f p = new Vector3f(newRect.getPoint(0));
      p.add(velOld);
      if (MathTools.distancePointPlane(p, newRect.getPlane()) > 0)
      {
        newRect.moveEdgesUp(-radius);
      }
      else
      {
        newRect.moveEdgesUp(radius);
      }
    }
    newRect.moveEdgesOutward(radius);
    //check if sphere crossed the plane of the rectangle
    float dOld = MathTools.distancePointPlane(posOld, newRect.getPlane());
    if (cullBackface && dOld < 0)
    {
      //if backface culling activated:
      //sphere was already behind plane. it can not collide
      //with its front face any more.
      return false;
    }
    float dNew = MathTools.distancePointPlane(posNew, newRect.getPlane());
    if (cullBackface && dNew > 0)
    {
      //if backface culling activated:
      //sphere does not collide with the rectangle
      return false;
    }
    if (!cullBackface &&
      ((dNew > 0 && dOld > 0) || (dNew < 0 && dOld < 0)))
    {
      //sphere did not cross the plane
      return false;
    }
    //now we know, that the sphere crossed the plane of the rectangle.
    //but did it really hit the rectangle?
    Vector3f dir = new Vector3f();
    dir.sub(posNew, posOld);
    Vector3f collisionPoint = new Vector3f();
    if (!MathTools.intersectionLineRectangle(posOld, dir,
      newRect, collisionPoint))
    {
      //no rectangle intersection
      return false;
    }
    //intersection!
    //calculate new position (after reflection)
    retPosAfterReflection.set(MathTools.mirrorPointAcrossPlane(
      posNew, newRect.getPlane()));
    //calculate new velocity vector
    retVelAfterReflection.sub(retPosAfterReflection, collisionPoint);
    MathTools.normalize(retVelAfterReflection);
    retVelAfterReflection.scale(velOld.length());
    return true;
  }


  //check if a fixed sphere intersects a PhysicsObject
  public static boolean intersectionSpherePhysicsObjects(
    Sphere sphere, PhysicsObject o)
  {
    //check if the sphere collides with another object...
    if (o instanceof Wall)
    {
      //check if sphere intersects with wall
      Wall wall = (Wall) o;
      return intersectionSphereRectangle(sphere, wall.getRectangle());
    }
    else if (o instanceof ComplexWall)
    {
      //check if sphere intersects with complex wall
      ComplexWall complexWall = (ComplexWall) o;
      for (int i = 0; i < complexWall.getRectangleCount(); i++)
      {
        if (intersectionSphereRectangle(sphere, complexWall.getRectangle(i)))
        {
          return true;
        }
      }
      return false;
    }
    else
    {
      //add other collision objects here if needed...
    }
    return false;
  }


  //check if a fixed sphere collides with a rectangle
  public static boolean intersectionSphereRectangle(
    SphereInterface sphere, Rectangle rect)
  {
    Rectangle newRect = new Rectangle(rect);
    //does sphere intersect with the plane of the rectangle?
    if (Math.abs(MathTools.distancePointPlane(
      sphere.getPosition(), newRect.getPlane())) > sphere.getRadius())
    {
      return false; //no intersection!
    }
    //we move the edges of the rectangle in
    //all four directions <radius> units outward.
    //then we have just to test
    //the center point of the sphere
    newRect.moveEdgesOutward(sphere.getRadius());
    //we know, that the sphere crossed the plane of the rectangle.
    //but did it really hit the rectangle?
    if (!MathTools.isPointInRectangle(sphere.getPosition(), newRect))
    {
      //no rectangle intersection
      return false;
    }
    //intersection!
    return true;
  }


}
