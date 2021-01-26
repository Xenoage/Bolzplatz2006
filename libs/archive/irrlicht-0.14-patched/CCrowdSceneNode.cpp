// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CCrowdSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "S3DVertex.h"
#include "os.h"


namespace irr
{
namespace scene
{

//! constructor
CCrowdSceneNode::CCrowdSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id,
  s32 characterCount,
	const core::vector3df& position, const core::vector3df& rotation,	const core::vector3df& scale)
: ICrowdSceneNode(parent, mgr, id, characterCount, position, rotation, scale)
{

	Material.Wireframe = false;
	Material.Lighting = true;

  CharacterCount = characterCount;

  Indices.resize(characterCount * 6);
  Vertices.resize(characterCount * 4);

  //set indices
  //3--2
  //|\ |
  //| \|
  //0--1
  //face 0: 0-3-1
  //face 1: 1-3-2
  for (int i = 0; i < characterCount; ++i)
	{
    int offset = i * 4;
		Indices[i * 6 + 0] = offset + 0;
    Indices[i * 6 + 1] = offset + 3;
    Indices[i * 6 + 2] = offset + 1;
    Indices[i * 6 + 3] = offset + 1;
    Indices[i * 6 + 4] = offset + 3;
    Indices[i * 6 + 5] = offset + 2;
	}

}


void CCrowdSceneNode::createCharacter(
  s32 characterIndex, f32 x, f32 y, f32 z, f32 rotY, f32 width, f32 height)
{
  s32 offset = characterIndex * 4;
  f32 sinr = sin(rotY);
  f32 cosr = cos(rotY);
  video::S3DVertex v0;
  v0.Pos.X = x - sinr * width * 0.5f;
  v0.Pos.Y = y;
  v0.Pos.Z = z + cosr * width * 0.5f;
  v0.Color.set(255, 255, 255, 255);
  Vertices[offset + 0] = v0;
  video::S3DVertex v1;
  v1.Pos.X = x + sinr * width * 0.5f;
  v1.Pos.Y = y;
  v1.Pos.Z = z - cosr * width * 0.5f;
  v1.Color.set(255, 255, 255, 255);
  Vertices[offset + 1] = v1;
  video::S3DVertex v2;
  v2.Pos.X = x + sinr * width * 0.5f;
  v2.Pos.Y = y + height;
  v2.Pos.Z = z - cosr * width * 0.5f;
  v2.Color.set(255, 255, 255, 255);
  Vertices[offset + 2] = v2;
  video::S3DVertex v3;
  v3.Pos.X = x - sinr * width * 0.5f;
  v3.Pos.Y = y + height;
  v3.Pos.Z = z + cosr * width * 0.5f;
  v3.Color.set(255, 255, 255, 255);
  Vertices[offset + 3] = v3;
}


void CCrowdSceneNode::setCharacterTCords(s32 characterIndex,
  f32 x0, f32 y0, f32 x1, f32 y1, f32 x2, f32 y2, f32 x3, f32 y3)
{
  s32 offset = characterIndex * 4;
  Vertices[offset + 0].TCoords.X = x0;
  Vertices[offset + 0].TCoords.Y = y0;
  Vertices[offset + 1].TCoords.X = x1;
  Vertices[offset + 1].TCoords.Y = y1;
  Vertices[offset + 2].TCoords.X = x2;
  Vertices[offset + 2].TCoords.Y = y2;
  Vertices[offset + 3].TCoords.X = x3;
  Vertices[offset + 3].TCoords.Y = y3;
}


void CCrowdSceneNode::updateBox()
{
  Box.reset(0,0,0); 
	for (int i=0; i < CharacterCount * 4; ++i)
	{
		Box.addInternalPoint(Vertices[i].Pos);
	}
}


//! destructor
CCrowdSceneNode::~CCrowdSceneNode()
{
}


//! renders the node.
void CCrowdSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	driver->setMaterial(Material);
	driver->setTransform(video::ETS_WORLD, AbsoluteTransformation);
	driver->drawIndexedTriangleList(
    &Vertices[0], CharacterCount * 4, &Indices[0], CharacterCount * 2);
}



//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CCrowdSceneNode::getBoundingBox() const
{
	return Box;
}


void CCrowdSceneNode::OnPreRender()
{
	if (IsVisible)
		SceneManager->registerNodeForRendering(this);

	ISceneNode::OnPreRender();
}


//! returns the material based on the zero based index i. To get the amount
//! of materials used by this scene node, use getMaterialCount().
//! This function is needed for inserting the node into the scene hirachy on a
//! optimal position for minimizing renderstate changes, but can also be used
//! to directly modify the material of a scene node.
video::SMaterial& CCrowdSceneNode::getMaterial(s32 i)
{
	return Material;
}


//! returns amount of materials used by this scene node.
s32 CCrowdSceneNode::getMaterialCount()
{
	return 1;
}




} // end namespace scene
} // end namespace irr
