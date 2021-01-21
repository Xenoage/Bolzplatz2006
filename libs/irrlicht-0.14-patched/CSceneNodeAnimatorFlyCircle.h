// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SCENE_NODE_ANIMATOR_FLY_CIRCLE_H_INCLUDED__
#define __C_SCENE_NODE_ANIMATOR_FLY_CIRCLE_H_INCLUDED__

#include "ISceneNode.h"

namespace irr
{
namespace scene
{
	class CSceneNodeAnimatorFlyCircle : public ISceneNodeAnimator
	{
	public:

		//! constructor
		CSceneNodeAnimatorFlyCircle(u32 time, const core::vector3df& center, f32 radius, f32 speed);

		//! destructor
		virtual ~CSceneNodeAnimatorFlyCircle();

		//! animates a scene node
		virtual void animateNode(ISceneNode* node, u32 timeMs);

	private:

		core::vector3df Center;
		f32 Radius;
		f32 Speed;
		u32 StartTime;
	};


} // end namespace scene
} // end namespace irr

#endif

