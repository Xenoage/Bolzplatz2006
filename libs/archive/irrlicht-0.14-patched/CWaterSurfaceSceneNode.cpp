// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CWaterSurfaceSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "S3DVertex.h"
#include "SViewFrustrum.h"
#include "ICameraSceneNode.h"
#include "IMeshManipulator.h"
#include "SMesh.h"
#include "os.h"

namespace irr
{
namespace scene
{



//! constructor
CWaterSurfaceSceneNode::CWaterSurfaceSceneNode(f32 waveHeight, f32 waveSpeed, f32 waveLength, 
		IMesh* mesh, ISceneNode* parent, ISceneManager* mgr, s32 id,
		const core::vector3df& position, const core::vector3df& rotation,
		const core::vector3df& scale)
: CMeshSceneNode(mesh, parent, mgr, id, position, rotation, scale), OriginalMesh(0)
{
	#ifdef _DEBUG
	setDebugName("CWaterSurfaceSceneNode");
	#endif

	WaveSpeed = waveSpeed;//300.0f;
	WaveLength = waveLength;//10.0f;
	WaveHeight = waveHeight;//2.0f;

	// create copy of the mesh
	if (!mesh)
		return;

	IMesh* clone = SceneManager->getMeshManipulator()->createMeshCopy(mesh);
	OriginalMesh = Mesh;
	Mesh = clone;
}



//! destructor
CWaterSurfaceSceneNode::~CWaterSurfaceSceneNode()
{
	if (OriginalMesh)
		OriginalMesh->drop();
}



//! frame
void CWaterSurfaceSceneNode::OnPreRender()
{
	if (IsVisible)
		SceneManager->registerNodeForRendering(this);

	animateWaterSurface();

	ISceneNode::OnPreRender();
}


void CWaterSurfaceSceneNode::animateWaterSurface()
{
	if (!Mesh)
		return;

	s32 meshBufferCount = Mesh->getMeshBufferCount();
	f32 time = os::Timer::getTime() / WaveSpeed;

    for (s32 b=0; b<meshBufferCount; ++b)
	{
		s32 vtxCnt = Mesh->getMeshBuffer(b)->getVertexCount();

		switch(Mesh->getMeshBuffer(b)->getVertexType())
		{
		case video::EVT_STANDARD:
			{
				video::S3DVertex* v = 
					(video::S3DVertex*)Mesh->getMeshBuffer(b)->getVertices();

				video::S3DVertex* v2 = 
					(video::S3DVertex*)OriginalMesh->getMeshBuffer(b)->getVertices();

				for (s32 i=0; i<vtxCnt; ++i)
				{
					v[i].Pos.Y = v2[i].Pos.Y + 
					((f32)sin(((v2[i].Pos.X/WaveLength) + time)) * WaveHeight) + 
					((f32)cos(((v2[i].Pos.Z/WaveLength) + time)) * WaveHeight);
				}

			}
			break;
		case video::EVT_2TCOORDS:
			{
				video::S3DVertex2TCoords* v = 
					(video::S3DVertex2TCoords*)Mesh->getMeshBuffer(b)->getVertices();

				video::S3DVertex2TCoords* v2 = 
					(video::S3DVertex2TCoords*)OriginalMesh->getMeshBuffer(b)->getVertices();

				for (s32 i=0; i<vtxCnt; ++i)
				{
					v[i].Pos.Y = v2[i].Pos.Y + 
					((f32)sin(((v2[i].Pos.X/WaveLength) + time)) * WaveHeight) + 
					((f32)cos(((v2[i].Pos.Z/WaveLength) + time)) * WaveHeight);
				}
			}
			break;
		}// end switch

	}// end for all mesh buffers

	SceneManager->getMeshManipulator()->recalculateNormals(Mesh);

}


} // end namespace scene
} // end namespace irr

