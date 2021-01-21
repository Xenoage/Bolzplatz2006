// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SCENE_NODE_ANIMATOR_TEXTURE_H_INCLUDED__
#define __C_SCENE_NODE_ANIMATOR_TEXTURE_H_INCLUDED__

#include "irrArray.h"
#include "ISceneNode.h"

namespace irr
{
namespace scene
{
	class CSceneNodeAnimatorTexture : public ISceneNodeAnimator
	{
	public:

		//! constructor
		CSceneNodeAnimatorTexture(const core::array<video::ITexture*>& textures,
			s32 timePerFrame, bool loop, u32 now);

		//! destructor
		virtual ~CSceneNodeAnimatorTexture();

		//! animates a scene node
		virtual void animateNode(ISceneNode* node, u32 timeMs);

	private:

		core::array<video::ITexture*> Textures;
		u32 TimePerFrame;
		u32 StartTime;
		u32 EndTime;
		bool Loop;
	};


} // end namespace scene
} // end namespace irr

#endif

