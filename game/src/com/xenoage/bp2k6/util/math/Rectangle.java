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

import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.math.Plane;
import javax.vecmath.Vector3f;

public class Rectangle
{

  //           ---> vNorm[0] (normalized)
  //           -------------------------> v[0]
  //
  //         p[0]                       p[1]
  //     ^     +------------------------+     |       | vNorm[1]
  //     |     |                        |     |       v
  //     |     |                        |     | v[1]
  //  ^  |     |                        |     |
  //  |  |     +------------------------+     v
  // v[3]    p[3]                       p[2]
  //   vNorm[3]
  //           <------------------------- v[2]
  //                                 <--- vNorm[2]

  private Vector3f p[] = new Vector3f[4];
  private Vector3f v[] = new Vector3f[4];
  private Vector3f vNorm[] = new Vector3f[4];
  private Plane plane;

  public Rectangle()
  {
    for (int i = 0; i < 4; i++)
    {
      p[i] = new Vector3f();
      v[i] = new Vector3f();
      vNorm[i] = new Vector3f();
    }
    plane = new Plane();
  }

  public Rectangle(Rectangle template)
  {
    this();
    set(template);
  }

  public Rectangle(Vector3f p[])
  {
    this();
    setP(p);
  }


  public void set(Rectangle template)
  {
    for (int i = 0; i < 4; i++)
      p[i].set(template.getPoint(i));
    for (int i = 0; i < 4; i++)
      v[i].set(template.getEdge(i));
    for (int i = 0; i < 4; i++)
      vNorm[i].set(template.getEdgeNormalized(i));
    plane = new Plane(template.getPlane());
  }

  public void setP(Vector3f p[])
  {
    if (p.length == 4)
    {
      //set points
      for (int i = 0; i < 4; i++)
        this.p[i] = p[i];
      //calculate edges
      updateEdges();
      //calculate plane
      updatePlane();
    }
  }


  private void updateEdges()
  {
    //calculate edges
    for (int i = 0; i < 4; i++)
    {
      v[i].sub(p[(i+1)%4], p[i]);
      MathTools.normalize(vNorm[i]);
    }
    //normalize edges
    for (int i = 0; i < 4; i++)
    {
      vNorm[i].set(v[i]);
      MathTools.normalize(vNorm[i]);
    }
  }


  private void updatePlane()
  {
    //calculate plane
    Vector3f normal = new Vector3f();
    normal.x = p[0].y * (p[1].z - p[2].z) +
      p[1].y * (p[2].z - p[0].z) +
      p[2].y * (p[0].z - p[1].z);
    normal.y = p[0].z * (p[1].x - p[2].x) +
      p[1].z * (p[2].x - p[0].x) +
      p[2].z * (p[0].x - p[1].x);
    normal.z = p[0].x * (p[1].y - p[2].y) +
      p[1].x * (p[2].y - p[0].y) +
      p[2].x * (p[0].y - p[1].y);
    normal.normalize();
    plane.a = normal.x;
    plane.b = normal.y;
    plane.c = normal.z;
    plane.d = -normal.dot(p[0]); //-p[0].x * (p[1].y * p[2].z - p[2].y * p[1].z) -
      //p[1].x * (p[2].y * p[0].z - p[0].y * p[2].z) -
     // p[2].x * (p[0].y * p[1].z - p[1].y * p[0].z);
    //original...
    //A = y1(z2-z3) + y2(z3-z1) + y3(z1-z2)
    //B = z1(x2-x3) + z2(x3-x1) + z3(x1-x2)
    //C = x1(y2-y3) + x2(y3-y1) + x3(y1-y2)
    //D = -x1(y2*z3 - y3*z2) - x2(y3*z1 - y1*z3) - x3(y1*z2 - y2*z1)
  }


  public Vector3f getPoint(int i)
  {
    return p[i];
  }


  public Vector3f getEdge(int i)
  {
    return v[i];
  }


  public Vector3f getEdgeNormalized(int i)
  {
    return vNorm[i];
  }


  public Plane getPlane()
  {
    return plane;
  }


  //move edges up (in direction of the normal vector)
  public void moveEdgesUp(float d)
  {
    //move the four points
    Vector3f move = new Vector3f(plane.getNormal());
    move.scale(d);
    for (int i = 0; i < 4; i++)
    {
      p[i].add(move);
    }
    //subtract d from the distance parameter of the plane
    this.plane.d -= d;
  }



  //move edges outward (width and height of the rectangle
  //are increased by 2 * d)
  public void moveEdgesOutward(float d)
  {
    //move the four points
    Vector3f dir = new Vector3f();
    for (int i = 0; i < 4; i++)
    {
      dir.set(vNorm[i]);
      dir.scale(d);
      p[i].sub(dir);
      p[(i+3)%4].sub(dir);
    }
    //calculate edges
    updateEdges();
  }


  //move rectangle (first rotation, then position)
  public void move(Vector3f addPosition, float addYRotation)
  {
    for (int i = 0; i < 4; i++)
    {
      if (p[i].x > MathTools.EPSILON || p[i].x < -MathTools.EPSILON)
      {
        double theta = Math.atan(p[i].z / p[i].x);
        double r = p[i].x / Math.cos(theta);
        p[i].x = (float) (r * Math.cos(theta - addYRotation * MathTools.DEG_TO_RAD));
        p[i].z = (float) (r * Math.sin(theta - addYRotation * MathTools.DEG_TO_RAD));
      }
      p[i].add(addPosition);
    }
    updateEdges();
    updatePlane();
  }

}
