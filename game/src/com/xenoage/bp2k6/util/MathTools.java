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

//some useful mathematical functions
//
//(c) Andreas Wenger, 2005-07

import com.xenoage.bp2k6.util.math.*;
import javax.vecmath.*;
import net.sf.jirr.matrix4;
import net.sf.jirr.vector3df;


public class MathTools
{

  public static final float PI = (float) (Math.PI);
  public static final float EPSILON = 0.000000001f;
  public static final float HALFSQRT2 = 0.70710678118654752440084436210485f;

  public static final float RAD_TO_DEG = (float) (180f / Math.PI);
  public static final float DEG_TO_RAD = (float) (Math.PI / 180f);
  
  private static float sinTable[] = new float[360];
  private static float cosTable[] = new float[360];
  
  
  //create sine and cosine table
  static
  {
    for (int i = 0; i < 360; i++)
      sinTable[i] = (float) Math.sin(i * DEG_TO_RAD);
    for (int i = 0; i < 360; i++)
      cosTable[i] = (float) Math.cos(i * DEG_TO_RAD);
  }



  //Builds a rotation matrix based on an angle (in radians) and an axis.
  //Thanks to jox, http://irrlicht.sourceforge.net/phpBB2/viewtopic.php?p=35614
  //axis must be normalized!
  public static void buildRotationMatrix(float phi, Vector3f axis, matrix4 mRet)
  {
    //TODO: speed up

    float u = axis.x;
    float v = axis.y;
    float w = axis.z;

    float rcos = (float) Math.cos(phi);
    float rsin = (float) Math.sin(phi);

    float m[] = new float[16];

    //(*this)(0,0) =      rcos + u*u*(1-rcos);
    //mRet.setM(0, rcos + u*u*(1-rcos));
    m[0] = rcos + u*u*(1-rcos);
    //(*this)(1,0) =  w * rsin + v*u*(1-rcos);
    //mRet.setM(1, w * rsin + v*u*(1-rcos));
    m[1] = w * rsin + v*u*(1-rcos);
    //(*this)(2,0) = -v * rsin + w*u*(1-rcos);
    //mRet.setM(2, -v * rsin + w*u*(1-rcos));
    m[2] = -v * rsin + w*u*(1-rcos);
    //(*this)(3,0) = 0;
    //mRet.setM(3, 0);
    m[3] = 0;

    //(*this)(0,1) = -w * rsin + u*v*(1-rcos);
    //mRet.setM(4, -w * rsin + u*v*(1-rcos));
    m[4] = -w * rsin + u*v*(1-rcos);
    //(*this)(1,1) =      rcos + v*v*(1-rcos);
    //mRet.setM(5, rcos + v*v*(1-rcos));
    m[5] = rcos + v*v*(1-rcos);
    //(*this)(2,1) =  u * rsin + w*v*(1-rcos);
    //mRet.setM(6, u * rsin + w*v*(1-rcos));
    m[6] = u * rsin + w*v*(1-rcos);
    //(*this)(3,1) = 0;
    //mRet.setM(7, 0);
    m[7] = 0;

    //(*this)(0,2) =  v * rsin + u*w*(1-rcos);
    //mRet.setM(8, v * rsin + u*w*(1-rcos));
    m[8] = v * rsin + u*w*(1-rcos);
    //(*this)(1,2) = -u * rsin + v*w*(1-rcos);
    //mRet.setM(9, -u * rsin + v*w*(1-rcos));
    m[9] = -u * rsin + v*w*(1-rcos);
    //(*this)(2,2) =      rcos + w*w*(1-rcos);
    //mRet.setM(10, rcos + w*w*(1-rcos));
    m[10] = rcos + w*w*(1-rcos);
    //(*this)(3,2) = 0;
    //mRet.setM(11, 0);
    m[11] = 0;

    //(*this)(0,3) = 0;
    //mRet.setM(12, 0);
    m[12] = 0;
    //(*this)(1,3) = 0;
    //mRet.setM(13, 0);
    m[13] = 0;
    //(*this)(2,3) = 0;
    //mRet.setM(14, 0);
    m[14] = 0;
    //(*this)(3,3) = 1;
    //mRet.setM(15, 1);
    m[15] = 1;

    mRet.setMatrixData(m);

  }


  /**
   * Returns the distance between a plane and a point.
   * If the point is behind the plane (not that side
   * the normal vector of the plane is pointing to),
   * the returned value is negative.
   */
  public static float distancePointPlane(Vector3f point, Plane plane)
  {
    try
    {
      Vector3f normal = new Vector3f(plane.getNormal());
      float ret = normal.dot(point) + plane.d;
      return ret;
    }
    catch (Exception ex)
    {
      return 0;
    }
  }


  /**
   * Returns <code>true</code> if the given line intersects
   * the given triangle, otherwise <code>false</code>.
   * UNTESTET
   */
  public static boolean intersectionLineTriangle(
    Vector3f lineP, Vector3f lineDir,
    Vector3f triangleP1, Vector3f triangleP2, Vector3f triangleP3,
    Vector3f retIntersectionPoint)
  {
    //find vectors for two edges sharing triangleP1
    Vector3f edge1 = new Vector3f();
    Vector3f edge2 = new Vector3f();
    edge1.sub(triangleP2, triangleP1);
    edge2.sub(triangleP3, triangleP1);
    //begin calculating determinant - also used to calculate U parameter
    Vector3f pvec = new Vector3f();
    pvec.cross(lineDir, edge2);
    //if determinant is near zero, ray lies in plane of triangle
    float det = edge1.dot(pvec);
    if (det < EPSILON) //negative values: no intersection (backface culling)
      return false;
    //calculate distance from vert0 to ray origin
    Vector3f tvec = new Vector3f();
    tvec.sub(lineP, triangleP1);
    //calculate U parameter and test bounds
    float u = tvec.dot(pvec);
    if (u < 0.0 || u > det)
      return false;
    //prepare to test V parameter
    Vector3f qvec = new Vector3f();
    qvec.cross(tvec, edge1);
    //calculate V parameter and test bounds
    float v = lineDir.dot(qvec);
    if (v < 0.0 || u + v > det)
      return false;
    //calculate t, scale parameters, ray intersects triangle
    float t = edge2.dot(qvec);
    float detInv = 1.0f / det;
    t *= detInv;
    u *= detInv;
    v *= detInv;
    //calculate intersection point
    retIntersectionPoint.set(triangleP1);
    edge1.scale(u);
    retIntersectionPoint.add(edge1);
    edge2.scale(v);
    retIntersectionPoint.add(edge2);
    return true;
  }


  /**
   * Returns <code>true</code> if the given line intersects
   * the given rectangle, otherwise <code>false</code>.
   */
  public static boolean intersectionLineRectangle(
    Vector3f lineP, Vector3f lineDir, Rectangle rect,
    Vector3f retIntersectionPoint)
  {
    //find intersection point on the plane
    Plane plane = rect.getPlane();
    float d = plane.getNormal().dot(lineDir);
    if (d < EPSILON && d > -EPSILON)
      return false; //plane and line are parallel
    float t = - (plane.a * lineP.x + plane.b *
      lineP.y + plane.c * lineP.z + plane.d) / d;
    retIntersectionPoint.set(lineP.x + lineDir.x * t,
      lineP.y + lineDir.y * t, lineP.z + lineDir.z * t);
    //does the intersection point really lie within the rectangle bounds?

    Vector3f edge;
    Vector3f pToIntersection = new Vector3f();
    for (int i = 0; i < 4; i++)
    {
      edge = rect.getEdgeNormalized(i);
      pToIntersection.sub(retIntersectionPoint, rect.getPoint(i));
      pToIntersection.normalize();
      if (edge.dot(pToIntersection) < 0)
        return false;
    }

    //line intersects the rectangle!
    return true;
  }


  /**
   * Returns the point that appears when a point
   * is mirrored across a plane
   */
  public static Vector3f mirrorPointAcrossPlane(
    Vector3f point, Plane plane)
  {
    //simply add two times the inverse normal vector
    //scaled by the distance of the point to the plane
    float d = distancePointPlane(point, plane);
    Vector3f v = plane.getNormal();
    v.scale(-2 * d);
    Vector3f ret = new Vector3f(point);
    ret.add(v);
    return ret;
  }

  /**
   * Returns the squared distance between to points
   */
  public static float distanceSquared(Vector3f p1, Vector3f p2)
  {
    return (p1.x - p2.x) * (p1.x - p2.x) +
      (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z);
  }


  /**
   * Returns the distance between to points
   */
  public static float distance(Vector3f p1, Vector3f p2)
  {
    return (float) Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) +
      (p1.y - p2.y) * (p1.y - p2.y) + (p1.z - p2.z) * (p1.z - p2.z));
  }


  /**
   * Returns the squared distance between to points (without y component)
   */
  public static float distanceSquaredWithoutY(Vector3f p1, Vector3f p2)
  {
    return (p1.x - p2.x) * (p1.x - p2.x) + (p1.z - p2.z) * (p1.z - p2.z);
  }

  
  /**
   * Fills the given vector with
   * a normalized vector that points to the direction
   * given in the rotY parameter (between 0 and 360 degrees).
   * This method uses a internal sine and cosine table
   * and therefore has a int parameter instead of float.
  */
  public static void computeVectorFromYRotation(int rotY, Vector3f ret)
  {
    rotY = (rotY + 360) % 360;
    ret.set(sinTable[rotY], 0, cosTable[rotY]);
  }
  
  
  /**
   * Returns a normalized vector that points to the direction
   * given in the rotY parameter (between 0 and 360 degrees).
  */
  public static Vector3f computeVectorFromYRotation(float rotY)
  {
    rotY *= DEG_TO_RAD;
    return new Vector3f((float) Math.sin(rotY), 0, (float) Math.cos(rotY));
  }
  
  
  /**
   * Returns <code>true</code>, if the given point
   * is within the rectangle borders (needs not to
   * be on the plane, it may be in the "infinite cuboid"
   * defined by the normals of the plane and the edges
   * of the rectangle).
   */
  public static boolean isPointInRectangle(
    Vector3f p, Rectangle rect)
  {
    Vector3f edge;
    Vector3f vertexToP = new Vector3f();
    for (int i = 0; i < 4; i++)
    {
      edge = rect.getEdgeNormalized(i);
      vertexToP.sub(p, rect.getPoint(i));
      vertexToP.normalize();
      if (edge.dot(vertexToP) < 0)
        return false;
    }
    return true;
  }

  /**
   * Gets the smaller angle between to given angles.
   */
  public static float getAngle(float angle1, float angle2)
  {
    float ret = Math.abs(angle1 - angle2);
    if (ret > 180) ret = 360 - ret;
    return ret;
  }

  /**
   * Converts the given Vector3f into a y-rotation angle
   * (for example: (1, 0, 1) will return 45).
   */
  public static float Vector3fToAngle(Vector3f dir)
  {
    double d = Math.atan2(dir.z, dir.x) * RAD_TO_DEG;
    d = (720 - (d - 90)) % 360;
    return (float) d;
  }


  /**
   * Computes the squared distance of a point to a line segment.
   * It uses 3D vectors, but ignores the y component.
   * Algorithm: http://www.vb-helper.com/howto_distance_point_to_line.html
   * retT: 0: nearest point is p1, 1: nearest point is p2,
   * 0<retT<1: interpolation
   */
  public static float distanceSquaredPointLineSegment2D(
    Vector3f lineP1, Vector3f lineP2, Vector3f point, float retT)
  {
    float dx = lineP2.x - lineP1.x;
    float dz = lineP2.z - lineP1.z;
    if (Math.abs(dx) < EPSILON && Math.abs(dz) < EPSILON)
    {
      //it is a point, not a line segment
      dx = point.x - lineP1.x;
      dz = point.z - lineP1.z;
      retT = 0;
    }
    else
    {
      //calculate the t that minimizes the distance
      retT = ((point.x - lineP1.x) * dx + (point.z - lineP1.z) * dz) /
        (dx * dx + dz * dz);
      //see if this represents one of the segment's end points
      //or a point in the middle
      if (retT <= 0)
      {
        dx = point.x - lineP1.x;
        dz = point.z - lineP1.z;
      }
      else if (retT >= 1)
      {
        dx = point.x - lineP2.x;
        dz = point.z - lineP2.z;
      }
      else
      {
        dx = point.x - (lineP1.x + retT * dx);
        dz = point.z - (lineP1.z + retT * dz);
      }
    }
    return dx * dx + dz * dz;
  }


  /**
   * Creates a random float number between 0 and 1.
   */
  public static float random()
  {
    return (float) Math.random();
  }


  /**
   * Creates a random float number between 0 and <code>max</code>.
   */
  public static float random(float max)
  {
    return ((float) Math.random()) * max;
  }
  
  
  /**
   * Creates a random int number between 0 and <code>max</code> - 1.
   */
  public static int randomInt(int max)
  {
    return (int) (Math.random() * max);
  }


  /**
   * Creates a random float number between -<code>max</code>
   * and <code>max</code> ("centered" around 0).
   */
  public static float randomCenter(float max)
  {
    return ((float) Math.random()) * 2 * max - max;
  }


  /**
   * Normalizes a Vector3f, if possible. Call this method instead
   * of Vector3f.normalize(), because there no 0-Vector-Test is done.
   */
  public static void normalize(Vector3f v)
  {
    if (v.lengthSquared() > EPSILON)
      v.normalize();
  }
  
  
  /**
   * Gets the sine value of the given angle in radians
   * from a precalculated sine table (precision: 1 degree)
   */
  public static float sin(float rad)
  {
    int index = ((int) (rad * RAD_TO_DEG)) % 180;
    return sinTable[index];
  }
  
  
  /**
   * Returns <code>number</code>, if smaller than
   * <code>max</code>, otherwise <code>max</code>.
   */
  public static int trim(int number, int max)
  {
    return number < max ? number : max;
  }
  
  
  /**
   * Returns max, if x > max, min, if x < min, else x.
   */
  public static float clamp(float x, float min, float max)
  {
    if (x < min)
      return min;
    else if (x > max)
      return max;
    else
      return x;
  }
  
  
  /**
   * Returns max, if x > max, min, if x < min, else x.
   */
  public static int clamp(int x, int min, int max)
  {
    if (x < min)
      return min;
    else if (x > max)
      return max;
    else
      return x;
  }
  

  /**
   * Returns <code>true</code> if the two given rectangles
   * intersect.
   */ /*
  public static boolean intersectionRectangleRectangle(
    Rectangle rect1, Rectangle rect2)
  {

  }*/



  /**
   * Returns <code>true</code> if the two given triangles
   * intersect.
   */ /*
  public static boolean intersectionRectangleRectangle(
    Vector3f v0, Vector3f v1, Vector3f v2,
    Vector3f u0, Vector3f u1, Vector3f u2)
  {

    float du0,du1,du2,dv0,dv1,dv2;

    float N1[3],N2[3],d1,d2;
    float du0,du1,du2,dv0,dv1,dv2;
    float D[3];
    float isect1[2], isect2[2];
    float du0du1,du0du2,dv0dv1,dv0dv2;
    short index;
    float vp0,vp1,vp2;
    float up0,up1,up2;
    float bb,cc,max;
    float a,b,c,x0,x1;
    float d,e,f,y0,y1;
    float xx,yy,xxyy,tmp;

    //compute plane equation of triangle(v0, v1, v2)
    Vector3f e1 = new Vector3f(), e2 = new Vector3f();
    e1.sub(v1, v0);
    e2.sub(v2, v0);
    Vector3f n1 = new Vector3f();
    n1.cross(e1, e2);
    float d1 = -n1.dot(v0);
    //plane equation 1: n1.X + d1 = 0

    //put u0, u1, u2 into plane equation 1 to compute signed distances to the plane
    float du0 = n1.dot(u0) + d1;
    float du1 = n1.dot(u1) + d1;
    float du2 = n1.dot(u2) + d1;

    //coplanarity robustness check
    //if USE_EPSILON_TEST==TRUE
      if(Math.abs(du0) < EPSILON) du0 = 0f;
      if(Math.abs(du1) < EPSILON) du1 = 0f;
      if(Math.abs(du2) < EPSILON) du2 = 0f;
    //endif
    float du0du1 = du0 * du1;
    float du0du2 = du0 * du2;

    if (du0du1 > 0f && du0du2 > 0f) //same sign on all of them + not equal 0 ?
      return 0; //no intersection occurs

    //compute plane of triangle (u0, u1, u2)
    e1.sub(u1, u0);
    e2.sub(u2, u0);
    Vector3f n2 = new Vector3f();
    n2.cross(e1, e2);
    float d2 = -n2.dot(u0);
    //plane equation 2: n2.X + d2 =0

    //put v0, v1, v2 into plane equation 2
    float dv0 = n2.dot(v0) + d2;
    float dv1 = n2.dot(v1) + d2;
    float dv2 = n2.dot(v2) + d2;

    //if USE_EPSILON_TEST==TRUE
      if(Math.abs(dv0) < EPSILON) dv0 = 0f;
      if(Math.abs(dv1) < EPSILON) dv1 = 0f;
      if(Math.abs(dv2) < EPSILON) dv2 = 0f;
    //endif

    float dv0dv1 = dv0 * dv1;
    float dv0dv2 = dv0*dv2;

    if(dv0dv1 > 0f && dv0dv2 > 0f) //same sign on all of them + not equal 0 ?
      return 0; //no intersection occurs

    //compute direction of intersection line
    Vector3f d = new Vector3f();
    d.cross(n1, n2);

    //compute and index to the largest component of D
    float max = Math.abs(d.x);
    float index = 0f;
    float bb = Math.abs(d.y);
    float cc = Math.abs(d.z);
    if (bb > max) { max = bb; index = 1; }
    if (cc > max) { max = cc; index = 2; }

    //this is the simplified projection onto L
    v0.
    Vector3f vp = new Vector3f(v0.
    vp0=V0[index];
    vp1=V1[index];
    vp2=V2[index];

    up0=U0[index];
    up1=U1[index];
    up2=U2[index];

    //compute interval for triangle 1
    NEWCOMPUTE_INTERVALS(vp0,vp1,vp2,dv0,dv1,dv2,dv0dv1,dv0dv2,a,b,c,x0,x1);

    //compute interval for triangle 2
    NEWCOMPUTE_INTERVALS(up0,up1,up2,du0,du1,du2,du0du1,du0du2,d,e,f,y0,y1);

    xx=x0*x1;
    yy=y0*y1;
    xxyy=xx*yy;

    tmp=a*xxyy;
    isect1[0]=tmp+b*x1*yy;
    isect1[1]=tmp+c*x0*yy;

    tmp=d*xxyy;
    isect2[0]=tmp+e*xx*y1;
    isect2[1]=tmp+f*xx*y0;

    SORT(isect1[0],isect1[1]);
    SORT(isect2[0],isect2[1]);

    if(isect1[1]<isect2[0] || isect2[1]<isect1[0]) return 0;
    return 1;

  }*/


  /**
   * Rotate a point around the origin
   * rot: rotation in degrees
   * UNTESTED
   */
 /*  public static Vector3f rotatePoint(Vector3f point, Vector3f rot)
  {
    Vector3f rotRad = new Vector3f(rot);
    rotRad.scale(DEG_TO_RAD);
    Vector3f sin = new Vector3f(
      (float) Math.sin(rotRad.x),
      (float) Math.sin(rotRad.y),
      (float) Math.sin(rotRad.z));
    Vector3f cos = new Vector3f(
      (float) Math.cos(rotRad.x),
      (float) Math.cos(rotRad.y),
      (float) Math.cos(rotRad.z));
    float tempY = point.y * cos.y - point.z * sin.y;
    float tempZ = point.y * sin.y + point.z * cos.y;
    float tempX = point.x * cos.x - tempZ * sin.x;
    Vector3f ret = new Vector3f(
      point.x * sin.x + tempZ * cos.x,
      tempX * cos.z - tempY * sin.z,
      tempX * sin.z + tempY * cos.z);
    return ret;
  } */



  /*
  //Builds a rotation matrix based on an angle (in radians) and an axis.
  //Thanks to jox, http://irrlicht.sourceforge.net/phpBB2/viewtopic.php?p=35614
  //axis must be normalized!
  public static Matrix4f buildRotationMatrix(float phi, Vector3f axis)
  {
    Matrix4f mRet = new Matrix4f();

    float u = axis.x;
    float v = axis.y;
    float w = axis.z;

    float rcos = (float) Math.cos(phi);
    float rsin = (float) Math.sin(phi);

    //(*this)(0,0) =      rcos + u*u*(1-rcos);
    mRet.m00 = rcos + u*u*(1-rcos);
    //(*this)(1,0) =  w * rsin + v*u*(1-rcos);
    mRet.m10 = w * rsin + v*u*(1-rcos);
    //(*this)(2,0) = -v * rsin + w*u*(1-rcos);
    mRet.m20 = -v * rsin + w*u*(1-rcos);
    //(*this)(3,0) = 0;
    mRet.m30 = 0;

    //(*this)(0,1) = -w * rsin + u*v*(1-rcos);
    mRet.m01 = -w * rsin + u*v*(1-rcos);
    //(*this)(1,1) =      rcos + v*v*(1-rcos);
    mRet.m11 = rcos + v*v*(1-rcos);
    //(*this)(2,1) =  u * rsin + w*v*(1-rcos);
    mRet.m21 = u * rsin + w*v*(1-rcos);
    //(*this)(3,1) = 0;
    mRet.m31 = 0;

    //(*this)(0,2) =  v * rsin + u*w*(1-rcos);
    mRet.m02 = v * rsin + u*w*(1-rcos);
    //(*this)(1,2) = -u * rsin + v*w*(1-rcos);
    mRet.m12 = -u * rsin + v*w*(1-rcos);
    //(*this)(2,2) =      rcos + w*w*(1-rcos);
    mRet.m22 = rcos + w*w*(1-rcos);
    //(*this)(3,2) = 0;
    mRet.m32 = 0;

    //(*this)(0,3) = 0;
    mRet.m03 = 0;
    //(*this)(1,3) = 0;
    mRet.m13 = 0;
    //(*this)(2,3) = 0;
    mRet.m23 = 0;
    //(*this)(3,3) = 1;
    mRet.m33 = 1;

    return mRet;
  }
*/



  /* -*
   * Converts the given quaternion into a
   * Irrlicht 3d rotation vector (in degrees).
  *-/
  public static Vector3f convertQuaternionToEuler(Quat4f quat)
  {

    Vector3f ret = new Vector3f();
    float q[] = new float[4];
    quat.get(q);

    float unit = q[0] + q[1] + q[2] + q[3]; //if normalised is one, otherwise is correction factor
    float test = q[0] * q[1] + q[2] * q[3];
    if (test > 0.499 * unit)
    {
      //singularity at north pole
      ret.x = (float) (2 * Math.atan2(q[0], q[3])) * RAD_TO_DEG;
      ret.y = (float) (Math.PI/2) * RAD_TO_DEG;
      ret.z = 0;
      return ret;
    }
    if (test < -0.499 * unit)
    {
      //singularity at south pole
      ret.x = (float) (-2 * Math.atan2(q[0], q[3])) * RAD_TO_DEG;
      ret.y = (float) (-Math.PI/2) * RAD_TO_DEG;
      ret.z = 0;
      return ret;
    }

    float q2[] = new float[4];
    for (int i = 0; i < 4; i++)
      q2[i] = q[i] * q[i];

    ret.x = (float) (Math.atan2(2 * q[1] * q[3] - 2 * q[0] * q[2],
      q2[0] - q2[1] - q2[2] + q2[3])) * RAD_TO_DEG;
    if (unit != 0)
      ret.y = (float) (Math.asin(2*test/unit)) * RAD_TO_DEG;
    ret.z = (float) (Math.atan2(2 * q[0] * q[3] - 2 * q[1] * q[2],
      -q2[0] + q2[1] - q2[2] + q2[3])) * RAD_TO_DEG;

    //System.out.println(ret.x + ", " + ret.y + ", " +  ret.z);
    return ret;

  }


  /*
  //rotate matrix relative to its current rotation
  public static matrix4 rotate(matrix4 m, vector3df rot)
  {
    matrix4 n = new matrix4();
    n.setRotationDegrees(rot);
    m = m.multiply(n);
    return m;
  }

  //turn matrix left or right
  public static matrix4 turn(matrix4 m, float rot)
  {
    m = rotate(m, new vector3df(0.0f, rot, 0.0f));
    return m;
  }

  //pitch matrix up or down
  public static matrix4 pitch(matrix4 m, float rot)
  {
    m = rotate(m, new vector3df(rot, 0.0f, 0.0f));
    return m;
  }

  //roll matrix left or right
  public static matrix4 roll(matrix4 m, float rot)
  {
    m = rotate(m, new vector3df(0.0f, 0.0f, rot));
    return m;
  }

  //Builds a rotation matrix based on an angle (in radians) and an axis.
  //Thanks to jox, http://irrlicht.sourceforge.net/phpBB2/viewtopic.php?p=35614
  //axis must be normalized!
  public static matrix4 buildRotationMatrix(float phi, vector3df axis)
  {
    matrix4 mRet = new matrix4();

    float u = axis.getX();
    float v = axis.getY();
    float w = axis.getZ();

    float rcos = (float) Math.cos(phi);
    float rsin = (float) Math.sin(phi);

    float m[] = new float[16];

    //(*this)(0,0) =      rcos + u*u*(1-rcos);
    //mRet.setM(0, rcos + u*u*(1-rcos));
    m[0] = rcos + u*u*(1-rcos);
    //(*this)(1,0) =  w * rsin + v*u*(1-rcos);
    //mRet.setM(1, w * rsin + v*u*(1-rcos));
    m[1] = w * rsin + v*u*(1-rcos);
    //(*this)(2,0) = -v * rsin + w*u*(1-rcos);
    //mRet.setM(2, -v * rsin + w*u*(1-rcos));
    m[2] = -v * rsin + w*u*(1-rcos);
    //(*this)(3,0) = 0;
    //mRet.setM(3, 0);
    m[3] = 0;

    //(*this)(0,1) = -w * rsin + u*v*(1-rcos);
    //mRet.setM(4, -w * rsin + u*v*(1-rcos));
    m[4] = -w * rsin + u*v*(1-rcos);
    //(*this)(1,1) =      rcos + v*v*(1-rcos);
    //mRet.setM(5, rcos + v*v*(1-rcos));
    m[5] = rcos + v*v*(1-rcos);
    //(*this)(2,1) =  u * rsin + w*v*(1-rcos);
    //mRet.setM(6, u * rsin + w*v*(1-rcos));
    m[6] = u * rsin + w*v*(1-rcos);
    //(*this)(3,1) = 0;
    //mRet.setM(7, 0);
    m[7] = 0;

    //(*this)(0,2) =  v * rsin + u*w*(1-rcos);
    //mRet.setM(8, v * rsin + u*w*(1-rcos));
    m[8] = v * rsin + u*w*(1-rcos);
    //(*this)(1,2) = -u * rsin + v*w*(1-rcos);
    //mRet.setM(9, -u * rsin + v*w*(1-rcos));
    m[9] = -u * rsin + v*w*(1-rcos);
    //(*this)(2,2) =      rcos + w*w*(1-rcos);
    //mRet.setM(10, rcos + w*w*(1-rcos));
    m[10] = rcos + w*w*(1-rcos);
    //(*this)(3,2) = 0;
    //mRet.setM(11, 0);
    m[11] = 0;

    //(*this)(0,3) = 0;
    //mRet.setM(12, 0);
    m[12] = 0;
    //(*this)(1,3) = 0;
    //mRet.setM(13, 0);
    m[13] = 0;
    //(*this)(2,3) = 0;
    //mRet.setM(14, 0);
    m[14] = 0;
    //(*this)(3,3) = 1;
    //mRet.setM(15, 1);
    m[15] = 1;

    mRet.setM(m);

    return mRet;
  }

  //square distance between two points
  public static float distanceSquared(vector3df p1, vector3df p2)
  {
    return (p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) +
      (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()) + (p1.getZ() - p2.getZ()) * (p1.getZ() - p2.getZ());
  }

  //multiply a vector with a scalar
  public static vector3df vec3mul(vector3df v, float s)
  {
    return new vector3df(v.getX() * s, v.getY() * s, v.getZ() * s);
  }
  */

}
