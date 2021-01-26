// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CLightSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "os.h"

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

	setAutomaticCulling(false);
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
		SceneManager->registerNodeForRendering(this, ESNRP_LIGHT);
		ISceneNode::OnPreRender();
	}
}


//! render
void CLightSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	if (!driver)
		return;

	switch ( LightData.Type )
	{
		case video::ELT_POINT:
			LightData.Position = getAbsolutePosition();
			break;

		case video::ELT_DIRECTIONAL:
			// misuse Position as direction..
			LightData.Position = getAbsolutePosition();
			LightData.Position.invert();
			if ( LightData.Position.getLengthSQ() == 0.0 )
			{
				LightData.Position.set ( 0.f, -1.f, 0.f );
				os::Printer::log ( "Invalid Directional Light Direction" );
			}
			else
			{
				LightData.Position.normalize();
			}
			break;
	}

	if (DebugDataVisible)
	{
		driver->setTransform(video::ETS_WORLD, AbsoluteTransformation);
		video::SMaterial m;
		m.Lighting = false;
		driver->setMaterial(m);
//		driver->draw3DBox(BBox, LightData.DiffuseColor.toSColor());

		core::aabbox3df radius = BBox;
		switch ( LightData.Type )
		{
			case video::ELT_POINT:
				radius.MaxEdge *= LightData.Radius;
				radius.MinEdge *= LightData.Radius;
				driver->draw3DBox(radius, LightData.DiffuseColor.toSColor());
				break;

			case video::ELT_DIRECTIONAL:
				driver->draw3DLine (	core::vector3df ( 0.f, 0.f, 0.f ), 
										core::vector3df ( 0.f, 0.f, 0.f ) + (LightData.Position * 10.f ),
										LightData.DiffuseColor.toSColor()
									);
				break;
		}
							
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


//! Writes attributes of the scene node.
void CLightSceneNode::serializeAttributes(io::IAttributes* out, io::SAttributeReadWriteOptions* options)
{
	ILightSceneNode::serializeAttributes(out, options);

	out->addColorf	("AmbientColor", LightData.AmbientColor);
	out->addColorf	("DiffuseColor", LightData.DiffuseColor);
	out->addColorf	("SpecularColor", LightData.SpecularColor);
	out->addFloat	("Radius", LightData.Radius);
	out->addBool	("CastShadows", LightData.CastShadows);
	out->addEnum	("LightType", LightData.Type, video::LightTypeNames);
}

//! Reads attributes of the scene node.
void CLightSceneNode::deserializeAttributes(io::IAttributes* in, io::SAttributeReadWriteOptions* options)
{
	LightData.AmbientColor =	in->getAttributeAsColorf("AmbientColor");
	LightData.DiffuseColor =	in->getAttributeAsColorf("DiffuseColor");
	LightData.SpecularColor =	in->getAttributeAsColorf("SpecularColor");
	LightData.Radius =			in->getAttributeAsFloat("Radius");
	LightData.CastShadows =		in->getAttributeAsBool("CastShadows");
	LightData.Type =			(video::E_LIGHT_TYPE)in->getAttributeAsEnumeration("LightType", video::LightTypeNames);

	ILightSceneNode::deserializeAttributes(in, options);
}

} // end namespace scene
} // end namespace irr
