// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __IRR_TRIANGLE_3D_H_INCLUDED__
#define __IRR_TRIANGLE_3D_H_INCLUDED__

#include "vector3d.h"
#include "line3d.h"
#include "plane3d.h"
#include "aabbox3d.h"

namespace irr
{
namespace core
{
	
	//! 3d triangle template class for doing collision detection and other things.
	template <class T>
	class triangle3d  
	{
	public:

		//! Determinates if the triangle is totally inside a bounding box.
		//! \param box: Box to check.
		//! \return Returns true if the triangle is withing the box,
		//! and false if it is not.
		bool isTotalInsideBox(const aabbox3d<T>& box) const
		{
			return (box.isPointInside(pointA) && 
				box.isPointInside(pointB) &&
				box.isPointInside(pointC));
		}

		bool operator==(const triangle3d<T>& other) const { return other.pointA==pointA && other.pointB==pointB && other.pointC==pointC; }
		bool operator!=(const triangle3d<T>& other) const { return other.pointA!=pointA || other.pointB!=pointB || other.pointC!=pointC; }

		//! Returns the closest point on a triangle to a point on the same plane.
		//! \param p: Point which must be on the same plane as the triangle.
		core::vector3d<T> closestPointOnTriangle(const core::vector3d<T>& p) const
		{
			core::vector3d<T> rab = line3d<T>(pointA, pointB).getClosestPoint(p);
			core::vector3d<T> rbc = line3d<T>(pointB, pointC).getClosestPoint(p);
			core::vector3d<T> rca = line3d<T>(pointC, pointA).getClosestPoint(p);

			T d1 = rab.getDistanceFrom(p);
			T d2 = rbc.getDistanceFrom(p);
			T d3 = rca.getDistanceFrom(p);

			if (d1 < d2)
				return d1 < d3 ? rab : rca;
            
			return d2 < d3 ? rbc : rca;
		}

		//! Returns if a point is inside the triangle
		//! \param p: Point to test. Assumes that this point is already on the plane
		//! of the triangle.
		//! \return Returns true if the point is inside the triangle, otherwise false.
		bool isPointInside(const vector3d<T>& p) const
		{
			return (isOnSameSide(p, pointA, pointB, pointC) &&
				isOnSameSide(p, pointB, pointA, pointC) &&
				isOnSameSide(p, pointC, pointA, pointB));
		}

		//! Returns if a point is inside the triangle. This method is an implementation
		//! of the example used in a paper by Kasper Fauerby original written
		//! by Keidy from Mr-Gamemaker.
		//! \param p: Point to test. Assumes that this point is already on the plane
		//! of the triangle.
		//! \return Returns true if the point is inside the triangle, otherwise false.
		bool isPointInsideFast(const vector3d<T>& p) const
		{
			vector3d<T> f = pointB - pointA;
			vector3d<T> g = pointC - pointA;

			f32 a = f.dotProduct(f);
			f32 b = f.dotProduct(g);
			f32 c = g.dotProduct(g);

			f32 ac_bb = (a*c)-(b*b);
			vector3d<T> vp = p - pointA;

			f32 d = vp.dotProduct(f);
			f32 e = vp.dotProduct(g);
			f32 x = (d*c)-(e*b);
			f32 y = (e*a)-(d*b);
			f32 z = x+y-ac_bb;

			return (( ((u32&)z)& ~(((u32&)x)|((u32&)y))) & 0x80000000)!=0;
		}


		bool isOnSameSide(const vector3d<T>& p1, const vector3d<T>& p2, 
			const vector3d<T>& a, const vector3d<T>& b) const
		{
			vector3d<T> bminusa = b - a;
			vector3d<T> cp1 = bminusa.crossProduct(p1 - a);
			vector3d<T> cp2 = bminusa.crossProduct(p2 - a);
			return (cp1.dotProduct(cp2) >= 0.0f);
		}


		//! Returns an intersection with a 3d line.
		//! \param line: Line to intersect with.
		//! \param outIntersection: Place to store the intersection point, if there is one.
		//! \return Returns true if there was an intersection, false if there was not.
		bool getIntersectionWithLimitedLine(const line3d<T>& line,
			vector3d<T>& outIntersection) const
		{
			return getIntersectionWithLine(line.start,
				line.getVector(), outIntersection) &&
				outIntersection.isBetweenPoints(line.start, line.end);
		}


		//! Returns an intersection with a 3d line.
		//! Please note that also points are returned as intersection, which
		//! are on the line, but not between the start and end point of the line.
		//! If you want the returned point be between start and end, please
		//! use getIntersectionWithLimitedLine().
		//! \param lineVect: Vector of the line to intersect with.
		//! \param linePoint: Point of the line to intersect with.
		//! \param outIntersection: Place to store the intersection point, if there is one.
		//! \return Returns true if there was an intersection, false if there was not.
		bool getIntersectionWithLine(const vector3d<T>& linePoint,
			const vector3d<T>& lineVect, vector3d<T>& outIntersection) const
		{
			if (getIntersectionOfPlaneWithLine(linePoint, lineVect, outIntersection))
				return isPointInside(outIntersection);

			return false;			
		}


		//! Calculates the intersection between a 3d line and 
		//! the plane the triangle is on.
		//! \param lineVect: Vector of the line to intersect with.
		//! \param linePoint: Point of the line to intersect with.
		//! \param outIntersection: Place to store the intersection point, if there is one.
		//! \return Returns true if there was an intersection, false if there was not.
		bool getIntersectionOfPlaneWithLine(const vector3d<T>& linePoint,
			const vector3d<T>& lineVect, vector3d<T>& outIntersection) const
		{
			vector3d<T> normal = getNormal().normalize();
			T t2 = normal.dotProduct(lineVect);
            
			if (t2 == 0.0f)
				return false;

			T d = pointA.dotProduct(normal);
			T t = -(normal.dotProduct(linePoint) - d) / t2;
			outIntersection = linePoint + (lineVect * t);
			return true;
		}

		
		//! Returns the normal of the triangle.
		//! Please note: The normal is not normalized.
		vector3d<T> getNormal() const
		{
			return (pointB - pointA).crossProduct(pointC - pointA);
		}

		//! Returns if the triangle is front of backfacing.
		//! \param lookDirection: Look direction.
		//! \return Returns true if the plane is front facing, which mean it would
		//! be visible, and false if it is backfacing.
		bool isFrontFacing(const vector3d<T>& lookDirection) const
		{
			vector3d<T> n = getNormal();
			n.normalize();
			return n.dotProduct(lookDirection) <= 0.0f;
		}

		//! Returns the plane of this triangle.
		plane3d<T> getPlane() const
		{
			return plane3d<T>(pointA, pointB, pointC);
		}

		void set(const core::vector3d<T>& a, const core::vector3d<T>& b, const core::vector3d<T>& c)
		{
			pointA = a;
			pointB = b;
			pointC = c;
		}

		//! the three points of the triangle
		vector3d<T> pointA; 
		vector3d<T> pointB; 
		vector3d<T> pointC; 
	};


	//! Typedef for a f32 3d triangle.
	typedef triangle3d<f32> triangle3df;

	//! Typedef for an integer 3d triangle.
	typedef triangle3d<s32> triangle3di;

} // end namespace core
} // end namespace irr

#endif

