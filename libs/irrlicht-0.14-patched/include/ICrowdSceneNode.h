// Copyright (C) 2006 Andreas Wenger

#ifndef __I_CROWD_SCENE_NODE_H_INCLUDED__
#define __I_CROWD_SCENE_NODE_H_INCLUDED__

#include "ISceneNode.h"
#include "S3DVertex.h"


namespace irr
{
namespace scene
{

	class ICrowdSceneNode : public ISceneNode
	{
	public:

		//! constructor
		ICrowdSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id,
      s32 characterCount,
			const core::vector3df& position = core::vector3df(0,0,0),
			const core::vector3df& rotation = core::vector3df(0,0,0),
			const core::vector3df& scale = core::vector3df(1.0f, 1.0f, 1.0f))
      : ISceneNode(parent, mgr, id, position) {}

    virtual void createCharacter(
      s32 characterIndex, f32 x, f32 y, f32 z, f32 rotY, f32 width, f32 height) = 0;

    virtual void updateBox() = 0;

    virtual void setCharacterTCords(s32 characterIndex,
      f32 x0, f32 y0, f32 x1, f32 y1, f32 x2, f32 y2, f32 x3, f32 y3) = 0;

	};

} // end namespace scene
} // end namespace irr

#endif
