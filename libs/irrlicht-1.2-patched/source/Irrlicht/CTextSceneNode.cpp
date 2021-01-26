// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CTextSceneNode.h"
#include "ISceneManager.h"


namespace irr
{
namespace scene
{

//! constructor
CTextSceneNode::CTextSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id,
							   gui::IGUIFont* font, scene::ISceneCollisionManager* coll,
	const core::vector3df& position, const wchar_t* text,
	video::SColor color)
	: ITextSceneNode(parent, mgr, id, position), Font(font), Coll(coll), Color(color)
{
	#ifdef _DEBUG
	setDebugName("CTextSceneNode");
	#endif

	Text = text;
	setAutomaticCulling(false);

	if (Font)
		Font->grab();
}

//! destructor
CTextSceneNode::~CTextSceneNode()
{
	if (Font)
		Font->drop();
}

void CTextSceneNode::OnPreRender()
{
	if (IsVisible)
	{
		SceneManager->registerNodeForRendering(this, ESNRP_SHADOW);
		ISceneNode::OnPreRender();
	}
}

//! renders the node.
void CTextSceneNode::render()
{
	if (!Font || !Coll)
		return;

	core::position2d<s32> pos = Coll->getScreenCoordinatesFrom3DPosition(getAbsolutePosition(), 
		SceneManager->getActiveCamera());

	core::rect<s32> r(pos, core::dimension2d<s32>(1,1));
	Font->draw(Text.c_str(), r, Color, true, true);
}


//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CTextSceneNode::getBoundingBox() const
{
	return Box;
}

//! returns amount of materials used by this scene node.
s32 CTextSceneNode::getMaterialCount()
{
	return 0;
}

//! sets the text string
void CTextSceneNode::setText(const wchar_t* text)
{
	Text = text;
}

//! sets the color of the text
void CTextSceneNode::setTextColor(video::SColor color)
{
	Color = color;
}


} // end namespace scene
} // end namespace irr

