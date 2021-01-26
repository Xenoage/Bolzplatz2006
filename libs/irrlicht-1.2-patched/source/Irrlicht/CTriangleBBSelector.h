// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_TRIANGLE_BB_SELECTOR_H_INCLUDED__
#define __C_TRIANGLE_BB_SELECTOR_H_INCLUDED__

#include "CTriangleSelector.h"

namespace irr
{
namespace scene
{

//! Stupid triangle selector without optimization
class CTriangleBBSelector : public CTriangleSelector
{
public:

	//! Constructs a selector based on a mesh
	CTriangleBBSelector(ISceneNode* node);

	virtual ~CTriangleBBSelector() {};

	//! Gets all triangles.
	void getTriangles(core::triangle3df* triangles, s32 arraySize, s32& outTriangleCount, 
		const core::matrix4* transform=0);
};

} // end namespace scene
} // end namespace irr


#endif

