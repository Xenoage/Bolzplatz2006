// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CTriangleSelector.h"
#include "ISceneNode.h"

namespace irr
{
namespace scene
{

//! constructor
CTriangleSelector::CTriangleSelector(ISceneNode* node)
: SceneNode(node)
{
	#ifdef _DEBUG
	setDebugName("CTriangleSelector");
	#endif
}


//! constructor
CTriangleSelector::CTriangleSelector(IMesh* mesh, ISceneNode* node)
: SceneNode(node)
{
	#ifdef _DEBUG
	setDebugName("CTriangleSelector");
	#endif

	s32 cnt = mesh->getMeshBufferCount();
	for (s32 i=0; i<cnt; ++i)
	{
		IMeshBuffer* buf = mesh->getMeshBuffer(i);

		s32 idxCnt = buf->getIndexCount();
		const u16* indices = buf->getIndices();
		core::triangle3df tri;

		switch (buf->getVertexType())
		{
		case video::EVT_STANDARD:
			{
				video::S3DVertex* vtx = (video::S3DVertex*)buf->getVertices();
				for (s32 j=0; j<idxCnt; j+=3)
				{
					tri.pointA = vtx[indices[j+0]].Pos;
					tri.pointB = vtx[indices[j+1]].Pos;
					tri.pointC = vtx[indices[j+2]].Pos;
					Triangles.push_back(tri);
				}
			}
			break;
		case video::EVT_2TCOORDS:
			{
				video::S3DVertex2TCoords* vtx = (video::S3DVertex2TCoords*)buf->getVertices();
				for (s32 j=0; j<idxCnt; j+=3)
				{
					tri.pointA = vtx[indices[j+0]].Pos;
					tri.pointB = vtx[indices[j+1]].Pos;
					tri.pointC = vtx[indices[j+2]].Pos;
					Triangles.push_back(tri);
				}
			}
			break;
		}
	}
}


//! constructor
CTriangleSelector::CTriangleSelector(core::aabbox3d<f32> box, ISceneNode* node)
: SceneNode(node)
{
	#ifdef _DEBUG
	setDebugName("CTriangleSelector");
	#endif

	// TODO
}



//! destructor
CTriangleSelector::~CTriangleSelector()
{
}



//! Gets all triangles.
void CTriangleSelector::getTriangles(core::triangle3df* triangles,
									 s32 arraySize, s32& outTriangleCount, 
									const core::matrix4* transform)
{
	s32 cnt = Triangles.size();
	if (cnt > arraySize)
		cnt = arraySize;

	core::matrix4 mat;

	if (transform)
		mat = (*transform);

	if (SceneNode)
		mat *= SceneNode->getAbsoluteTransformation();

	for (s32 i=0; i<cnt; ++i)
	{
		triangles[i] = Triangles[i];
		mat.transformVect(triangles[i].pointA);
		mat.transformVect(triangles[i].pointB);
		mat.transformVect(triangles[i].pointC);
	}

	outTriangleCount = cnt;
}



//! Gets all triangles which lie within a specific bounding box.
void CTriangleSelector::getTriangles(core::triangle3df* triangles, 
									 s32 arraySize, s32& outTriangleCount, 
									const core::aabbox3d<f32>& box,
									const core::matrix4* transform)
{
	// return all triangles
	getTriangles(triangles, arraySize, outTriangleCount, transform);
}


//! Gets all triangles which have or may have contact with a 3d line.
void CTriangleSelector::getTriangles(core::triangle3df* triangles, s32 arraySize,
	s32& outTriangleCount, const core::line3d<f32>& line, 
	const core::matrix4* transform)
{
	// return all triangles
	getTriangles(triangles, arraySize, outTriangleCount, transform);
}


//! Returns amount of all available triangles in this selector
s32 CTriangleSelector::getTriangleCount() const
{
	return Triangles.size();
}



} // end namespace scene
} // end namespace irr

