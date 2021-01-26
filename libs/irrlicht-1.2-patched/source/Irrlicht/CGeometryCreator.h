// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GEOMETRY_CREATOR_H_INCLUDED__
#define __C_GEOMETRY_CREATOR_H_INCLUDED__

#include "IAnimatedMesh.h"
#include "IImage.h"



namespace irr
{
namespace video
{
	class IVideoDriver;
}

namespace scene
{

//! class for creating geometry on the fly
class CGeometryCreator
{
public:

	static IAnimatedMesh* createHillPlaneMesh(
		const core::dimension2d<f32>& tileSize, const core::dimension2d<s32>& tileCount,
		video::SMaterial* material,	f32 hillHeight, const core::dimension2d<f32>& countHills,
		const core::dimension2d<f32>& textureRepeatCount);

	static IAnimatedMesh* createTerrainMesh(video::IImage* texture, 
		video::IImage* heightmap, const core::dimension2d<f32>& stretchSize, f32 maxHeight,
		video::IVideoDriver* driver, 
		const core::dimension2d<s32> defaultVertexBlockSize,
		bool debugBorders=false);
};


} // end namespace scene
} // end namespace irr

#endif

