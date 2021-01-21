// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SCENE_NODE_ANIMATOR_ROTATION_H_INCLUDED__
#define __C_SCENE_NODE_ANIMATOR_ROTATION_H_INCLUDED__

#include "ISceneNode.h"

namespace irr
{
namespace scene
{
	class CSceneNodeAnimatorRotation : public ISceneNodeAnimator
	{
	public:

		//! constructor
		CSceneNodeAnimatorRotation(u32 time, const core::vector3df& rotation);

		//! destructor
		virtual ~CSceneNodeAnimatorRotation();

		//! animates a scene node
		virtual void animateNode(ISceneNode* node, u32 timeMs);

	private:

		core::vector3df Rotation;
		u32 StartTime;
	};


} // end namespace scene
} // end namespace irr

#endif

