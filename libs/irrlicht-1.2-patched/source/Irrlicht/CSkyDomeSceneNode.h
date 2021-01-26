// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h
// Code for this scene node has been contributed by Anders la Cour-Harbo (alc)

#ifndef __C_SKY_DOME_SCENE_NODE_H_INCLUDED__
#define __C_SKY_DOME_SCENE_NODE_H_INCLUDED__

#include "ISceneNode.h"
#include "SMeshBuffer.h"

namespace irr
{
namespace scene
{

class CSkyDomeSceneNode : public ISceneNode
{
	public:
		CSkyDomeSceneNode(video::ITexture* texture, u32 horiRes, u32 vertRes,
		f64 texturePercentage, f64 spherePercentage, ISceneNode* root,
		ISceneManager* smgr, s32 id);
		virtual ~CSkyDomeSceneNode();
		virtual void OnPreRender();
		virtual void render();
		virtual const core::aabbox3d<f32>& getBoundingBox() const;
		virtual video::SMaterial& getMaterial(s32 i);
		virtual s32 getMaterialCount();
		virtual ESCENE_NODE_TYPE getType() { return ESNT_SKY_BOX; }

	private:
		SMeshBuffer Buffer;
};


}
}

#endif

