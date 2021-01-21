// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSceneCollisionManager.h"
#include "ISceneNode.h"
#include "ICameraSceneNode.h"
#include "ITriangleSelector.h"

#include "os.h"
#include <stdio.h>

namespace irr
{
namespace scene
{

//! constructor
CSceneCollisionManager::CSceneCollisionManager(ISceneManager* smanager, video::IVideoDriver* driver)
: Driver(driver), SceneManager(smanager)
{
	#ifdef _DEBUG
	setDebugName("CSceneCollisionManager");
	#endif

	if (Driver)
		Driver->grab();
}



//! destructor
CSceneCollisionManager::~CSceneCollisionManager() 
{
	if (Driver)
		Driver->drop();
}



//! Returns the scene node, which is currently visible under the overgiven 
//! screencoordinates, viewed from the currently active camera. 
ISceneNode* CSceneCollisionManager::getSceneNodeFromScreenCoordinatesBB(
	core::position2d<s32> pos, s32 idBitMask)
{
	if (!SceneManager || !Driver)
		return 0; 

	ICameraSceneNode* camera = SceneManager->getActiveCamera();
	if (!camera)
		return 0;

	const scene::SViewFrustrum* f = camera->getViewFrustrum();

	core::vector3df farLeftUp = f->getFarLeftUp();
	core::vector3df lefttoright = f->getFarRightUp() - farLeftUp;
	core::vector3df uptodown = f->getFarLeftDown() - farLeftUp;

	core::dimension2d<s32> screenSize = Driver->getScreenSize();

	f32 dx = pos.X / (f32)screenSize.Width;
	f32 dy = pos.Y / (f32)screenSize.Height;

	core::vector3df end = farLeftUp + (lefttoright * dx) + (uptodown * dy);
 
	return getSceneNodeFromRayBB(core::line3d<f32>(f->cameraPosition, end), idBitMask);
}



//! Returns the nearest scene node which collides with a 3d ray and 
//! which id matches a bitmask. 
ISceneNode* CSceneCollisionManager::getSceneNodeFromRayBB(core::line3d<f32> ray,
														s32 idBitMask)
{
	ISceneNode* best = 0;
	f32 dist = 9999999999.0f;

	getPickedNodeBB(SceneManager->getRootSceneNode(), ray.getMiddle(),
		ray.getVector().normalize(), ray.start, (f32)(ray.getLength() * 0.5),
		idBitMask, dist, best);

	return best;
}


//! recursive method for going through all scene nodes
void CSceneCollisionManager::getPickedNodeBB(ISceneNode* root,
									   const core::vector3df& linemiddle, 
									   const core::vector3df& linevect,
									   const core::vector3df& pos, 
									   f32 halflength, s32 bits,
									   f32& outbestdistance,
									   ISceneNode*& outbestnode)
{
	const core::list<ISceneNode*>& children = root->getChildren();

	core::list<ISceneNode*>::Iterator it = children.begin();

	for (; it != children.end(); ++it)
	{
		ISceneNode* current = *it;

		if (current->isVisible() && 
			(bits==0 || (bits != 0 && (current->getID() & bits))))
		{
			if (current->getTransformedBoundingBox().intersectsWithLine(
				linemiddle, linevect, halflength))
			{
				f32 dist = 	(f32)current->getAbsolutePosition().getDistanceFrom(pos);
				if (dist < outbestdistance)
				{
					outbestnode = current;
					outbestdistance = dist;
				}
			}
		}

		getPickedNodeBB(current, linemiddle, linevect, pos, 
			halflength, bits, outbestdistance, outbestnode);
	}
}



//! Returns the scene node, at which the overgiven camera is looking at and
//! which id matches the bitmask.
ISceneNode* CSceneCollisionManager::getSceneNodeFromCameraBB(
	ICameraSceneNode* camera, s32 idBitMask)
{
	if (!camera)
		return 0;

	core::vector3df start = camera->getAbsolutePosition();
	core::vector3df end = camera->getTarget();

	end = start + ((end - start).normalize() * camera->getFarValue());
	core::line3d<f32> line(start, end);

	return getSceneNodeFromRayBB(line, idBitMask);
}



//! Finds the collision point of a line and lots of triangles, if there is one.
bool CSceneCollisionManager::getCollisionPoint(const core::line3d<f32>& ray,
	ITriangleSelector* selector, core::vector3df& outIntersection,
	core::triangle3df& outTriangle)
{
	if (!selector)
	{
		_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
		return false;
	}

	s32 totalcnt = selector->getTriangleCount();
	Triangles.set_used(totalcnt);

	s32 cnt = 0;
	selector->getTriangles(Triangles.pointer(), totalcnt, cnt, ray);

	core::vector3df linevect = ray.getVector();
	core::vector3df intersection;
	f32 nearest = 9999999999999.0f;
	bool found = false;
	f32 tmp, tmp2;
	f32 raylenght = (f32)ray.getLengthSQ();
    
	for (s32 i=0; i<cnt; ++i)
	{
		if (Triangles[i].getIntersectionWithLine(ray.start, linevect, intersection))
		{
			tmp = (f32)intersection.getDistanceFromSQ(ray.start);
			tmp2 = (f32)intersection.getDistanceFromSQ(ray.end);

            if (tmp < raylenght && tmp2 < raylenght &&
				tmp < nearest)
			{
				nearest = tmp;
				outTriangle = Triangles[i];
				outIntersection = intersection;
				found = true;
			}
		}
	}

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return found;
}



//! Collides a moving ellipsoid with a 3d world with gravity and returns
//! the resulting new position of the ellipsoid. 
core::vector3df CSceneCollisionManager::getCollisionResultPosition(
	ITriangleSelector* selector,
	const core::vector3df &position,	const core::vector3df& radius, 
	const core::vector3df& direction,
	core::triangle3df& triout,
	bool& outFalling,
	f32 slidingSpeed,
	const core::vector3df& gravity)
{
	if (!selector || radius.X == 0.0f || radius.Y == 0.0f || radius.Z == 0.0f)
		return position;

	return collideEllipsoidWithWorld(selector, position,
		radius, direction, slidingSpeed, gravity, triout, outFalling);
}


void CSceneCollisionManager::testTriangleIntersection(SCollisionData* colData, 
			const core::triangle3df& triangle)
{
	core::plane3d<f32> trianglePlane = triangle.getPlane();

	// only check front facing polygons
	if (trianglePlane.isFrontFacing(colData->normalizedVelocity))
	{
		// get interval of plane intersection

		f64 t1, t0;
		bool embeddedInPlane = false;

		// calculate signed distance from sphere position to triangle plane
		f64 signedDistToTrianglePlane = trianglePlane.getDistanceTo(
			colData->basePoint);
			
		f32 normalDotVelocity =
			trianglePlane.Normal.dotProduct(colData->velocity);

		if (normalDotVelocity == 0.0f)
		{
			// sphere is traveling paralell to plane

			if (fabs(signedDistToTrianglePlane) >= 1.0f)
				return; // no collision possible
			else
			{
				// sphere is embedded in plane
				embeddedInPlane = true;
				t0 = 0.0;
				t1 = 1.0;
			}
		}
		else
		{
			// N.D is not 0. Calculate intersection interval
			t0 = (-1.0-signedDistToTrianglePlane)/normalDotVelocity;
			t1 = (1.0-signedDistToTrianglePlane)/normalDotVelocity;

			// Swap so t0 < t1
			if (t0 > t1) { f64 tmp = t1; t1 = t0; t0 = tmp;	}

			// check if at least one value is within the range
			if (t0 > 1.0f || t1 < 0.0f)
				return; // both t values are outside 1 and 0, no collision possible

			// clamp to 0 and 1
			if (t0 < 0.0) t0 = 0.0;
			if (t1 < 0.0) t1 = 0.0;
			if (t0 > 1.0) t0 = 1.0;
			if (t1 > 1.0) t1 = 1.0;
		}

		// at this point we have t0 and t1, if there is any intersection, it
		// is between this interval

		core::vector3df collisionPoint;
		bool foundCollision = false;
		float t = 1.0f;

		// first check the easy case: Collision within the triangle;
		// if this happens, it must be at t0 and this is when the sphere
		// rests on the front side of the triangle plane. This can only happen
		// if the sphere is not embedded in the triangle plane.

		if (!embeddedInPlane)
		{
			core::vector3df planeIntersectionPoint =
				(colData->basePoint - trianglePlane.Normal)
				+ (colData->velocity * (f32)t0);

			if (triangle.isPointInsideFast(planeIntersectionPoint))
			{
				foundCollision = true;
				t = (f32)t0;
				collisionPoint = planeIntersectionPoint;
			}
		}

		// if we havent found a collision already we will have to sweep
		// the sphere against points and edges of the triangle. Note: A 
		// collision inside the triangle will always happen before a 
		// vertex or edge collision. 

		if (!foundCollision)
		{
			core::vector3df velocity = colData->velocity;
			core::vector3df base = colData->basePoint;

			f32 velocitySqaredLength = (f32)velocity.getLengthSQ();
			f32 a,b,c;
			f32 newT;

			// for each edge or vertex a quadratic equation has to be solved:
			// a*t^2 + b*t + c = 0. We calculate a,b, and c for each test.

			// check against points
			a = velocitySqaredLength;

			// p1
			b = 2.0f * (velocity.dotProduct(base - triangle.pointA));
			c = (f32)((triangle.pointA-base).getLengthSQ() - 1.0);
			if (getLowestRoot(a,b,c,t, &newT))
			{
				t = newT;
				foundCollision = true;
				collisionPoint = triangle.pointA;
			}

			// p2 
			if (!foundCollision)
			{
				b = 2.0f * (velocity.dotProduct(base - triangle.pointB));
				c = (f32)((triangle.pointB-base).getLengthSQ() - 1.0);
				if (getLowestRoot(a,b,c,t, &newT))
				{
					t = newT;
					foundCollision = true;
					collisionPoint = triangle.pointB;
				}
			}

			// p3
			if (!foundCollision)
			{
				b = 2.0f * (velocity.dotProduct(base - triangle.pointC));
				c = (f32)((triangle.pointC-base).getLengthSQ() - 1.0);
				if (getLowestRoot(a,b,c,t, &newT))
				{
					t = newT;
					foundCollision = true;
					collisionPoint = triangle.pointC;
				}
			}

			// check against edges:

			// p1 --- p2
			core::vector3df edge = triangle.pointB - triangle.pointA;
			core::vector3df baseToVertex = triangle.pointA - base;
			f32 edgeSqaredLength = (f32)edge.getLengthSQ();
			f32 edgeDotVelocity = edge.dotProduct(velocity);
			f32 edgeDotBaseToVertex = edge.dotProduct(baseToVertex);

			// calculate parameters for equation
			a = edgeSqaredLength* -velocitySqaredLength + 
				edgeDotVelocity*edgeDotVelocity;
			b = edgeSqaredLength* (2*velocity.dotProduct(baseToVertex)) -
				2.0f*edgeDotVelocity*edgeDotBaseToVertex;
			c = (f32)(edgeSqaredLength* (1-baseToVertex.getLengthSQ()) +
				edgeDotBaseToVertex*edgeDotBaseToVertex);

			// does the swept sphere collide against ininite edge?
			if (getLowestRoot(a,b,c,t,&newT))
			{
				f32 f = (edgeDotVelocity*newT - edgeDotBaseToVertex) / edgeSqaredLength;
				if (f >=0.0f && f <= 1.0f)
				{
					// intersection took place within segment
					t = newT;
					foundCollision = true;
					collisionPoint = triangle.pointA + (edge*f);
				}
			}

			// p2 --- p3
			edge = triangle.pointC-triangle.pointB;
			baseToVertex = triangle.pointB - base;
			edgeSqaredLength = (f32)edge.getLengthSQ();
			edgeDotVelocity = edge.dotProduct(velocity);
			edgeDotBaseToVertex = edge.dotProduct(baseToVertex);

			// calculate parameters for equation
			a = edgeSqaredLength* -velocitySqaredLength + 
				edgeDotVelocity*edgeDotVelocity;
			b = edgeSqaredLength* (2*velocity.dotProduct(baseToVertex)) -
				2.0f*edgeDotVelocity*edgeDotBaseToVertex;
			c = (f32)(edgeSqaredLength* (1-baseToVertex.getLengthSQ()) +
				edgeDotBaseToVertex*edgeDotBaseToVertex);

			// does the swept sphere collide against ininite edge?
			if (getLowestRoot(a,b,c,t,&newT))
			{
				f32 f = (edgeDotVelocity*newT-edgeDotBaseToVertex) / 
					edgeSqaredLength;
				if (f >=0.0f && f <= 1.0f)
				{
					// intersection took place within segment
					t = newT;
					foundCollision = true;
					collisionPoint = triangle.pointB + (edge*f);
				}
			}


			// p3 --- p1
			edge = triangle.pointA-triangle.pointC;
			baseToVertex = triangle.pointC - base;
			edgeSqaredLength = (f32)edge.getLengthSQ();
			edgeDotVelocity = edge.dotProduct(velocity);
			edgeDotBaseToVertex = edge.dotProduct(baseToVertex);

			// calculate parameters for equation
			a = edgeSqaredLength* -velocitySqaredLength + 
				edgeDotVelocity*edgeDotVelocity;
			b = edgeSqaredLength* (2*velocity.dotProduct(baseToVertex)) -
				2.0f*edgeDotVelocity*edgeDotBaseToVertex;
			c = (f32)(edgeSqaredLength* (1-baseToVertex.getLengthSQ()) +
				edgeDotBaseToVertex*edgeDotBaseToVertex);

			// does the swept sphere collide against ininite edge?
			if (getLowestRoot(a,b,c,t,&newT))
			{
				f32 f = (edgeDotVelocity*newT-edgeDotBaseToVertex) / 
					edgeSqaredLength;
				if (f >=0.0f && f <= 1.0f)
				{
					// intersection took place within segment
					t = newT;
					foundCollision = true;
					collisionPoint = triangle.pointC + (edge*f);
				}
			}
		}// end no collision found

		// set result:
		if (foundCollision)
		{
			// distance to collision is t
			f32 distToCollision = (f32)(t*colData->velocity.getLength());

			// does this triangle qualify for closest hit?
			if (!colData->foundCollision ||
				distToCollision	< colData->nearestDistance) 
			{
				colData->nearestDistance = distToCollision;
				colData->intersectionPoint = collisionPoint;
				colData->foundCollision = true;
				colData->intersectionTriangle = triangle;
				++colData->triangleHits;
			}

		}// end found collision

	}// end if is front facing
}



//! Collides a moving ellipsoid with a 3d world with gravity and returns
//! the resulting new position of the ellipsoid. 
core::vector3df CSceneCollisionManager::collideEllipsoidWithWorld(
	ITriangleSelector* selector, const core::vector3df &position,
	const core::vector3df& radius,  const core::vector3df& velocity,
	f32 slidingSpeed,
	const core::vector3df& gravity,
	core::triangle3df& triout, bool& outFalling)
{
	if (!selector || radius.X == 0.0f || radius.Y == 0.0f || radius.Z == 0.0f)
		return position;

	// This code is based on the paper "Improved Collision detection and Response"
	// by Kasper Fauerby, but some parts are modified.

	SCollisionData colData;
	colData.R3Position = position;
	colData.R3Velocity = velocity;
	colData.eRadius = radius;
	colData.nearestDistance = 9999999999999.0f;
	colData.selector = selector;
	colData.slidingSpeed = slidingSpeed;
	colData.triangleHits = 0;

	core::vector3df eSpacePosition = colData.R3Position / colData.eRadius;
	core::vector3df eSpaceVelocity = colData.R3Velocity / colData.eRadius;

	// iterate until we have our final position

	core::vector3df finalPos = collideWithWorld(
		0, colData, eSpacePosition, eSpaceVelocity);

	outFalling = false;

	// add gravity

	if (gravity != core::vector3df(0,0,0))
	{
		colData.R3Position = finalPos * colData.eRadius;
		colData.R3Velocity = gravity;
		colData.triangleHits = 0;

		eSpaceVelocity = gravity/colData.eRadius;

		finalPos = collideWithWorld(0, colData,
			finalPos, eSpaceVelocity);

		outFalling = (colData.triangleHits == 0);
	}

	if (colData.triangleHits)
	{
		triout = colData.intersectionTriangle;
		triout.pointA *= colData.eRadius;
		triout.pointB *= colData.eRadius;
		triout.pointC *= colData.eRadius;
	}

	finalPos *= colData.eRadius;
	return finalPos;
}

core::vector3df CSceneCollisionManager::collideWithWorld(s32 recursionDepth,
	SCollisionData &colData, core::vector3df pos, core::vector3df vel)
{
	f32 veryCloseDistance = colData.slidingSpeed;

	if (recursionDepth > 5)
		return pos;

	colData.velocity = vel;
	colData.normalizedVelocity = vel;
	colData.normalizedVelocity.normalize();
	colData.basePoint = pos;
	colData.foundCollision = false;
	colData.nearestDistance = 9999999999999.0f;

	//------------------ collide with world

	// get all triangles with which we might collide
	core::aabbox3d<f32> box(colData.R3Position);
	box.addInternalPoint(colData.R3Position + colData.R3Velocity);
	box.MinEdge -= colData.eRadius;
	box.MaxEdge += colData.eRadius;

	s32 totalTriangleCnt = colData.selector->getTriangleCount();
	Triangles.set_used(totalTriangleCnt);

	core::matrix4 scaleMatrix;
	scaleMatrix.setScale(
		core::vector3df(1.0f / colData.eRadius.X, 1.0f / colData.eRadius.Y, 1.0f / colData.eRadius.Z));

	s32 triangleCnt = 0;
	colData.selector->getTriangles(Triangles.pointer(), totalTriangleCnt, triangleCnt, box, &scaleMatrix);
	//colData.selector->getTriangles(Triangles.pointer(), totalTriangleCnt, triangleCnt, &scaleMatrix);

	for (s32 i=0; i<triangleCnt; ++i)
		testTriangleIntersection(&colData, Triangles[i]);

	//---------------- end collide with world

	if (!colData.foundCollision)
		return pos + vel;

	// original destination point
	core::vector3df destinationPoint = pos + vel;
	core::vector3df newBasePoint = pos;

	// only update if we are not already very close
	// and if so only move very close to intersection, not to the
	// exact point
	if (colData.nearestDistance >= veryCloseDistance)
	{
		core::vector3df v = vel;
		v.setLength((f32)(colData.nearestDistance - veryCloseDistance));
		newBasePoint = colData.basePoint + v;

		v.normalize();
		colData.intersectionPoint -= (v * veryCloseDistance);
    }

	// calculate sliding plane

	core::vector3df slidePlaneOrigin = colData.intersectionPoint;
	core::vector3df slidePlaneNormal = newBasePoint - colData.intersectionPoint;
	slidePlaneNormal.normalize();
	core::plane3d<f32> slidingPlane(slidePlaneOrigin, slidePlaneNormal);

	core::vector3df newDestinationPoint = 
		destinationPoint - 
		(slidePlaneNormal * slidingPlane.getDistanceTo(destinationPoint));

	// generate slide vector

	core::vector3df newVelocityVector = newDestinationPoint - 
		colData.intersectionPoint;

	if (newVelocityVector.getLength() < veryCloseDistance)
		return newBasePoint;

	return collideWithWorld(recursionDepth+1, colData, 
		newBasePoint, newVelocityVector);
}


//! Returns a 3d ray which would go through the 2d screen coodinates.
core::line3d<f32> CSceneCollisionManager::getRayFromScreenCoordinates(
	core::position2d<s32> pos, ICameraSceneNode* camera)
{
	core::line3d<f32> ln;

	if (!SceneManager)
		return ln;	

	if (!camera)
		camera = SceneManager->getActiveCamera();

	if (!camera)
		return ln;

	const scene::SViewFrustrum* f = camera->getViewFrustrum();

	core::vector3df farLeftUp = f->getFarLeftUp();
	core::vector3df lefttoright = f->getFarRightUp() - farLeftUp;
	core::vector3df uptodown = f->getFarLeftDown() - farLeftUp;

	core::dimension2d<s32> screenSize = Driver->getScreenSize();

	f32 dx = pos.X / (f32)screenSize.Width;
	f32 dy = pos.Y / (f32)screenSize.Height;

	if (camera->isOrthogonal()) 
		ln.start = f->cameraPosition + (lefttoright * (dx-0.5f)) + (uptodown * (dy-0.5f)); 
	else 
		ln.start = f->cameraPosition;

	ln.start = f->cameraPosition;
	ln.end = farLeftUp + (lefttoright * dx) + (uptodown * dy);

	return ln;
}



//! Calculates 2d screen position from a 3d position.
core::position2d<s32> CSceneCollisionManager::getScreenCoordinatesFrom3DPosition(
	core::vector3df pos3d, ICameraSceneNode* camera)
{
	core::position2d<s32> pos2d(-1000,-1000);

	if (!SceneManager || !Driver)
		return pos2d;	

	if (!camera)
		camera = SceneManager->getActiveCamera();

	if (!camera)
		return pos2d;

	core::dimension2d<s32> dim = Driver->getScreenSize();
	dim.Width /= 2;
	dim.Height /= 2;

	f32 transformedPos[4];

	core::matrix4 trans = camera->getProjectionMatrix();
	trans *= camera->getViewMatrix();

	transformedPos[0] = pos3d.X;
	transformedPos[1] = pos3d.Y;
	transformedPos[2] = pos3d.Z;
	transformedPos[3] = 1.0f;

	trans.multiplyWith1x4Matrix(transformedPos);

	if (transformedPos[3] < 0)
		return core::position2d<s32>(-10000,-10000);

	f32 zDiv = transformedPos[3] == 0.0f ? 1.0f :
		(1.0f / transformedPos[3]);

	pos2d.X = (s32)(dim.Width * transformedPos[0] * zDiv) + dim.Width;
	pos2d.Y = ((s32)(dim.Height - (dim.Height * (transformedPos[1] * zDiv))));

	return pos2d;
}


} // end namespace scene
} // end namespace irr

