#ifndef __C_SCENE_COLLISION_MANAGER_H_INCLUDED__
#define __C_SCENE_COLLISION_MANAGER_H_INCLUDED__

// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "ISceneCollisionManager.h"
#include "ISceneManager.h"
#include "IVideoDriver.h"
#include <math.h>

namespace irr
{
namespace scene
{

	//!	The Scene Collision Manager provides methods for performing collision tests and picking on scene nodes.
	class CSceneCollisionManager : public ISceneCollisionManager
	{
	public:

		//! constructor
		CSceneCollisionManager(ISceneManager* smanager, video::IVideoDriver* driver);

		//! destructor
		virtual ~CSceneCollisionManager();

		//! Returns the scene node, which is currently visible under the overgiven 
		//! screencoordinates, viewed from the currently active camera. 
		virtual ISceneNode* getSceneNodeFromScreenCoordinatesBB(core::position2d<s32> pos,
			s32 idBitMask=0);

		//! Returns the nearest scene node which collides with a 3d ray and 
		//! which id matches a bitmask. 
		virtual ISceneNode* getSceneNodeFromRayBB(core::line3d<f32> ray, s32 idBitMask=0);

		//! Returns the scene node, at which the overgiven camera is looking at and
		//! which id matches the bitmask.
		virtual ISceneNode* getSceneNodeFromCameraBB(ICameraSceneNode* camera, s32 idBitMask=0);

		//! Finds the collision point of a line and lots of triangles, if there is one.
		virtual bool getCollisionPoint(const core::line3d<f32>& ray,
			ITriangleSelector* selector, core::vector3df& outCollisionPoint,
			core::triangle3df& outTriangle);

		//! Collides a moving ellipsoid with a 3d world with gravity and returns
		//! the resulting new position of the ellipsoid. 
		virtual core::vector3df getCollisionResultPosition(
			ITriangleSelector* selector,
			const core::vector3df &ellipsoidPosition,	const core::vector3df& ellipsoidRadius, 
			const core::vector3df& ellipsoidDirectionAndSpeed,
			core::triangle3df& triout,
			bool& outFalling,
			f32 slidingSpeed,
			const core::vector3df& gravityDirectionAndSpeed);

		//! Returns a 3d ray which would go through the 2d screen coodinates.
		virtual core::line3d<f32> getRayFromScreenCoordinates(
			core::position2d<s32> pos, ICameraSceneNode* camera = 0);

		//! Calculates 2d screen position from a 3d position.
		virtual core::position2d<s32> getScreenCoordinatesFrom3DPosition(
			core::vector3df pos, ICameraSceneNode* camera=0);

	private:

		//! recursive method for going through all scene nodes
		void getPickedNodeBB(ISceneNode* root, const core::vector3df& linemiddle, 
				const core::vector3df& linevect,
				const core::vector3df& pos, 
				f32 halflength, s32 bits, f32& outdist,
				ISceneNode*& outbest);

		struct SCollisionData
		{
			core::vector3df eRadius;

			core::vector3df R3Velocity; 
			core::vector3df R3Position;

			core::vector3df velocity;
			core::vector3df normalizedVelocity;
			core::vector3df basePoint;

			bool foundCollision;
			f64 nearestDistance;
			core::vector3df intersectionPoint;

			core::triangle3df intersectionTriangle;
			s32 triangleHits;

			f32 slidingSpeed;

			ITriangleSelector* selector;
		};

		void testTriangleIntersection(SCollisionData* colData, 
			const core::triangle3df& triangle);

		//! recursive method for doing collision response
		core::vector3df collideEllipsoidWithWorld(ITriangleSelector* selector, 
			const core::vector3df &position,
			const core::vector3df& radius,  const core::vector3df& velocity,
			f32 slidingSpeed,
			const core::vector3df& gravity, core::triangle3df& triout,
			bool& outFalling);

		core::vector3df collideWithWorld(s32 recursionDepth, SCollisionData &colData,
			core::vector3df pos, core::vector3df vel);

		inline bool getLowestRoot(f32 a, f32 b, f32 c, f32 maxR, f32* root)
		{
			// check if solution exists
			f32 determinant = b*b - 4.0f*a*c;

			// if determinant is negative, no solution
			if (determinant < 0.0f) return false;

			// calculate two roots: (if det==0 then x1==x2
			// but lets disregard that slight optimization)

			f32 sqrtD = (f32)sqrt(determinant);
			f32 r1 = (-b - sqrtD) / (2*a);
			f32 r2 = (-b + sqrtD) / (2*a);

			// sort so x1 <= x2
			if (r1 > r2) { f32 tmp=r2; r2=r1; r1=tmp; }

			// get lowest root
			if (r1 > 0 && r1 < maxR)
			{
				*root = r1;
				return true;
			}

			// its possible that we want x2, this can happen if x1 < 0
			if (r2 > 0 && r2 < maxR)
			{
				*root = r2;
				return true;
			}

			return false;
		}

		ISceneManager* SceneManager;
		video::IVideoDriver* Driver;
		core::array<core::triangle3df> Triangles; // triangle buffer 
	};


} // end namespace scene
} // end namespace irr

#endif

