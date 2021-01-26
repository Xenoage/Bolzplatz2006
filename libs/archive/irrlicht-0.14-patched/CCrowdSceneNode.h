// Copyright (C) 2006 Andreas Wenger

#ifndef __C_CROWD_SCENE_NODE_H_INCLUDED__
#define __C_CROWD_SCENE_NODE_H_INCLUDED__

#include "ISceneNode.h"
#include "S3DVertex.h"
#include "ICrowdSceneNode.h"
#include <vector>


namespace irr
{
namespace scene
{

	class CCrowdSceneNode : public ICrowdSceneNode
	{
	public:

		//! constructor
		CCrowdSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id,
      s32 characterCount,
			const core::vector3df& position = core::vector3df(0,0,0),
			const core::vector3df& rotation = core::vector3df(0,0,0),
			const core::vector3df& scale = core::vector3df(1.0f, 1.0f, 1.0f));

		//! destructor
		virtual ~CCrowdSceneNode();

		virtual void OnPreRender();

		//! renders the node.
		virtual void render();

		//! returns the axis aligned bounding box of this node
		virtual const core::aabbox3d<f32>& getBoundingBox() const;

		//! returns the material based on the zero based index i. To get the amount
		//! of materials used by this scene node, use getMaterialCount().
		//! This function is needed for inserting the node into the scene hirachy on a
		//! optimal position for minimizing renderstate changes, but can also be used
		//! to directly modify the material of a scene node.
		virtual video::SMaterial& getMaterial(s32 i);

		//! returns amount of materials used by this scene node.
		virtual s32 getMaterialCount();

    void CCrowdSceneNode::createCharacter(
      s32 characterIndex, f32 x, f32 y, f32 z, f32 rotY, f32 width, f32 height);

    void CCrowdSceneNode::updateBox();

    void CCrowdSceneNode::setCharacterTCords(s32 characterIndex,
      f32 x0, f32 y0, f32 x1, f32 y1, f32 x2, f32 y2, f32 x3, f32 y3);

	private:

    s32 CharacterCount;
		core::aabbox3d<f32> Box;
		std::vector<u16> Indices;
		std::vector<video::S3DVertex> Vertices;
		video::SMaterial Material;
	};

} // end namespace scene
} // end namespace irr

#endif
