// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SCENE_NODE_ANIMATOR_FOLLOW_SPLINE_H_INCLUDED__
#define __C_SCENE_NODE_ANIMATOR_FOLLOW_SPLINE_H_INCLUDED__

#include "ISceneNode.h"
#include "irrArray.h"

namespace irr
{
namespace scene
{
	//! Scene node animator based free code Matthias Gall wrote and sent in. (Most of 
	//! this code is written by him, I only modified bits.)
	class CSceneNodeAnimatorFollowSpline : public ISceneNodeAnimator
	{
	public:
	
		//! constructor
		CSceneNodeAnimatorFollowSpline(u32 startTime, 
			const core::array< core::vector3df >& points,
			f32 speed = 1.0f, f32 tightness = 0.5f);

		//! destructor
		virtual ~CSceneNodeAnimatorFollowSpline();

		//! animates a scene node
		virtual void animateNode(ISceneNode* node, u32 timeMs);

	protected:

		//! clamps a the value idx to fit into range 0..size-1
		s32 clamp(s32 idx, s32 size);

		core::array< core::vector3df > Points;
		f32 Speed;
		f32 Tightness;
		u32 StartTime;
		u32 NumPoints;
	};


} // end namespace scene
} // end namespace irr

#endif

