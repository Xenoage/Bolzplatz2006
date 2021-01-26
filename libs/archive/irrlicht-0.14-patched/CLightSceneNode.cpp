// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CLightSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"

namespace irr
{
namespace scene
{

//! constructor
CLightSceneNode::CLightSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id,	
	const core::vector3df& position, video::SColorf color,f32 radius)
: ILightSceneNode(parent, mgr, id, position)
{
	#ifdef _DEBUG
	setDebugName("CLightSceneNode");
	#endif

	AutomaticCullingEnabled = false;
	LightData.Radius = radius;
	LightData.DiffuseColor = color;
	LightData.Position = position;

	// set some useful specular color
	LightData.SpecularColor = color.getInterpolated(video::SColor(255,255,255,255),0.5f);
}

CLightSceneNode::~CLightSceneNode()
{
}


//! pre render event
void CLightSceneNode::OnPreRender()
{
	if (IsVisible)
	{
		SceneManager->registerNodeForRendering(this, ESNRP_LIGHT_AND_CAMERA);
		ISceneNode::OnPreRender();
	}
}


//! render
void CLightSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	if (!driver)
		return;

	if ( LightData.Type != video::ELT_DIRECTIONAL )
		LightData.Position = getAbsolutePosition();

	if (DebugDataVisible)
	{
		driver->setTransform(video::ETS_WORLD, AbsoluteTransformation);
		video::SMaterial m;
		m.Lighting = false;
		driver->setMaterial(m);
		driver->draw3DBox(BBox, LightData.DiffuseColor.toSColor());

		core::aabbox3df radius = BBox;
		radius.MaxEdge *= LightData.Radius;
		radius.MinEdge *= LightData.Radius;
		driver->draw3DBox(radius, LightData.DiffuseColor.toSColor());
	}

	driver->addDynamicLight(LightData);
}


//! returns the light data
video::SLight& CLightSceneNode::getLightData()
{
	return LightData;
}

//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CLightSceneNode::getBoundingBox() const
{
	return BBox;
}

} // end namespace scene
} // end namespace irr
