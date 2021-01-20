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
package com.xenoage.bp2k6.util.math;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.util.Vector3fFlags;


/**
 * Class for a hermite spline.
 * It contains methods to add points and
 * get interpolated positions.
 * 
 * @author Andreas Wenger
 */
public class HermiteSpline
{
  private ArrayList<Vector3fFlags> points;
  private float startTime;
  private float speed;
  private float tightness = 0.5f; //0.5: catmull-rom-spline
  private boolean loop = false;
  
  
  /**
   * Constructor. Creates a new hermite spline
   * with the given points, start time and
   * speed (in points/second)
   */
  public HermiteSpline(ArrayList<Vector3fFlags> points,
    float startTime, float speed)
  {
    this.points = points;
    this.startTime = startTime;
    this.speed = speed;
  }
  
  
  /**
   * Sets the speed for this spline.
   */
  public void setSpeed(float speed)
  {
    this.speed = speed;
  }
  
  
  /**
   * Sets the start time in seconds.
   */
  public void setStartTime(float startTime)
  {
    this.startTime = startTime;
  }
  
  
  /**
   * Sets the loop on or off.
   */
  public void setLoop(boolean loop)
  {
    this.loop = loop;
  }
  
  
  /**
   * Gets the interpolated position at the given time.
   * This method is based on the
   * CSceneNodeAnimatorFollowSpline::animateNode
   * method of the Irrlicht engine.
   * @param time   current time in seconds
   * @param loop   if <code>false</code> and the spline
   *               is not in its first loop any more,
   *               the last point of the spline is returned.
   */
  public Vector3f getPointAtTime(float time)
  {
    int numPoints = points.size();
    if (numPoints == 0)
      return null;
    
    Vector3f ret = new Vector3f();
    Vector3f p0, p1, p2, p3;
    Vector3f t1, t2;

    
    float dt = (time - startTime) * speed;
    if (!loop && (int)(dt) > numPoints - 3)
    {
      //no loop
      ret.set(points.get(numPoints - 1));
      return ret;
    }
    int idx = (int)(dt) % numPoints; //index of the current point
    float u = dt % numPoints - idx; //value between 0 and 1
      
    p0 = points.get(clamp(idx - 1, numPoints));
    p1 = points.get(clamp(idx, numPoints));
    p2 = points.get(clamp(idx + 1, numPoints));
    p3 = points.get(clamp(idx + 2, numPoints));

    //hermite polynomials
    float h1 = 2.0f * u * u * u - 3.0f * u * u + 1.0f;
    float h2 = -2.0f * u * u * u + 3.0f * u * u;
    float h3 = u * u * u - 2.0f * u * u + u;
    float h4 = u * u * u - u * u;

    //tangents
    t1 = new Vector3f();
    t1.sub(p2, p0);
    t1.scale(tightness);
    t2 = new Vector3f();
    t2.sub(p3, p1);
    t2.scale(tightness);

    //interpolated point
    ret.x = p1.x * h1 + p2.x * h2 + t1.x * h3 + t2.x * h4;
    ret.y = p1.y * h1 + p2.y * h2 + t1.y * h3 + t2.y * h4;
    ret.z = p1.z * h1 + p2.z * h2 + t1.z * h3 + t2.z * h4;
      
    return ret;
  }
  
  
  /**
   * Helper function: Returns a valid index for the points array.
   * For example -1 returns the last point in the array.
   */
  private int clamp(int idx, int size)
  {
    return (idx < 0 ? size + idx : (idx >= size ? idx - size : idx));
  }
  
  
  
}
