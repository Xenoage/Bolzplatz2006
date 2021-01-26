// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SCENE_NODE_ANIMATOR_FLY_STRAIGHT_H_INCLUDED__
#define __C_SCENE_NODE_ANIMATOR_FLY_STRAIGHT_H_INCLUDED__

#include "ISceneNode.h"

namespace irr
{
namespace scene
{
	class CSceneNodeAnimatorFlyStraight : public ISceneNodeAnimator
	{
	public:

		//! constructor
		CSceneNodeAnimatorFlyStraight(const core::vector3df& startPoint, 
									  const core::vector3df& endPoint, u32 timeForWay,
									  bool loop, u32 now);

		//! destructor
		virtual ~CSceneNodeAnimatorFlyStraight();

		//! animates a scene node
		virtual void animateNode(ISceneNode* node, u32 timeMs);

	private:

		core::vector3df Start;
		core::vector3df End;
		core::vector3df Vector;
		f32 WayLength;
		f32 TimeFactor;
		u32 StartTime;
		u32 EndTime;
		u32 TimeForWay;
		bool Loop;
	};


} // end namespace scene
} // end namespace irr

#endif

