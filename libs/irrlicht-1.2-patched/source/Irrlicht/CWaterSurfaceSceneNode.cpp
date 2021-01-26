// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CWaterSurfaceSceneNode.h"
#include "ISceneManager.h"
#include "IMeshManipulator.h"
#include "S3DVertex.h"
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
: CMeshSceneNode(mesh, parent, mgr, id, position, rotation, scale), WaveLength(waveLength),
	WaveSpeed(waveSpeed), WaveHeight(waveHeight), OriginalMesh(0)
{
	#ifdef _DEBUG
	setDebugName("CWaterSurfaceSceneNode");
	#endif

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
	{
		SceneManager->registerNodeForRendering(this);

		animateWaterSurface();

		CMeshSceneNode::OnPreRender();
	}
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
					(sinf(((v2[i].Pos.X/WaveLength) + time)) * WaveHeight) +
					(cosf(((v2[i].Pos.Z/WaveLength) + time)) * WaveHeight);
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
					(sinf(((v2[i].Pos.X/WaveLength) + time)) * WaveHeight) +
					(cosf(((v2[i].Pos.Z/WaveLength) + time)) * WaveHeight);
				}
			}
			break;
		} // end switch
	}// end for all mesh buffers

	SceneManager->getMeshManipulator()->recalculateNormals(Mesh);
}



//! Writes attributes of the scene node.
void CWaterSurfaceSceneNode::serializeAttributes(io::IAttributes* out, io::SAttributeReadWriteOptions* options)
{

	out->addFloat("WaveLength",	WaveLength);
	out->addFloat("WaveSpeed",	WaveSpeed);
	out->addFloat("WaveHeight", WaveHeight);
	
	// serialize original mesh
	scene::IMesh *swap = 0;

	if (Mesh)
	{
		swap = Mesh;
		Mesh = OriginalMesh;
	}
	CMeshSceneNode::serializeAttributes(out, options);
	if (swap)
	{
		Mesh = swap;
		OriginalMesh = Mesh;
	}
}


//! Reads attributes of the scene node.
void CWaterSurfaceSceneNode::deserializeAttributes(io::IAttributes* in, io::SAttributeReadWriteOptions* options)
{
	WaveLength	= in->getAttributeAsFloat("WaveLength");
	WaveSpeed	= in->getAttributeAsFloat("WaveSpeed");
	WaveHeight	= in->getAttributeAsFloat("WaveHeight");
	
	if (Mesh)
	{
		Mesh->drop();
		Mesh = OriginalMesh;
		OriginalMesh = 0;
	}
	// deserialize original mesh
	CMeshSceneNode::deserializeAttributes(in, options);

	if (Mesh)
	{
		IMesh* clone = SceneManager->getMeshManipulator()->createMeshCopy(Mesh);
		OriginalMesh = Mesh;
		Mesh = clone;
	}
}

} // end namespace scene
} // end namespace irr

