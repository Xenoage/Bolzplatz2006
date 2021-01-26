// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSceneNodeAnimatorTexture.h"

namespace irr
{
namespace scene
{


//! constructor
CSceneNodeAnimatorTexture::CSceneNodeAnimatorTexture(const core::array<video::ITexture*>& textures, 
													 s32 timePerFrame, bool loop, u32 now)
: Loop(loop), StartTime(now), TimePerFrame(timePerFrame)
{
	#ifdef _DEBUG
	setDebugName("CSceneNodeAnimatorTexture");
	#endif

	for (u32 i=0; i<textures.size(); ++i)
	{
		if (textures[i])
			textures[i]->grab();

		Textures.push_back(textures[i]);
	}

	EndTime = now + (timePerFrame * Textures.size());
}



//! destructor
CSceneNodeAnimatorTexture::~CSceneNodeAnimatorTexture()
{
	for (u32 i=0; i<Textures.size(); ++i)
		if (Textures[i])
			Textures[i]->drop();
}



//! animates a scene node
void CSceneNodeAnimatorTexture::animateNode(ISceneNode* node, u32 timeMs)
{
	u32 t = (timeMs-StartTime);

	s32 idx = 0;

	if (!Loop && timeMs >= EndTime)
		idx = Textures.size() - 1;
	else
		idx = (t/TimePerFrame) % Textures.size();

	if (idx < (s32)Textures.size())
		node->setMaterialTexture(0, Textures[idx]);
}



} // end namespace scene
} // end namespace irr
