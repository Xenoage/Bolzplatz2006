// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_MESH_MANIPULATOR_H_INCLUDED__
#define __I_MESH_MANIPULATOR_H_INCLUDED__

#include "IUnknown.h"
#include "vector3d.h"
#include "IMeshBuffer.h"
#include "aabbox3d.h"

namespace irr
{
namespace scene
{

	class IMesh;
	class IAnimatedMesh;
	struct SMesh;

	//! An interface for easily manipulate meshes.
	/** Scale, set alpha value, flip surfaces, and so on. This exists for fixing problems 
	with wrong imported or exported meshes quickly after loading. It is not intended for doing mesh
	modifications and/or animations during runtime.
	*/
	class IMeshManipulator : public virtual IUnknown
	{
	public:

		//! destructor
		virtual ~IMeshManipulator() {};

		//! Flips the direction of surfaces. 
		/** Changes backfacing triangles to frontfacing
		triangles and vice versa.
		\param mesh: Mesh on which the operation is performed. */
		virtual void flipSurfaces(IMesh* mesh) const = 0;

		//! Sets the alpha vertex color value of the whole mesh to a new value.
		/** \param mesh: Mesh on which the operation is performed.
		 \param alpha: New alpha value. Must be a value between 0 and 255. */
		virtual void setVertexColorAlpha(IMesh* mesh, s32 alpha) const = 0;

		//! Sets the colors of all vertices to one color
		virtual void setVertexColors(IMesh* mesh, video::SColor color) const = 0;

		//! Recalculates all normals of the mesh.
		/** \param mesh: Mesh on which the operation is performed. */
		virtual void recalculateNormals(IMesh* mesh, bool smooth = false) const = 0;

		//! Recalculates all normals of the mesh buffer.
		/** \param buffer: Mesh buffer on which the operation is performed. */
		virtual void recalculateNormals(IMeshBuffer* buffer, bool smooth = false) const = 0;

		//! Scales the whole mesh.
		/** \param mesh: Mesh on which the operation is performed.
		 \param scale: Scale factor. */
		virtual void scaleMesh(IMesh* mesh, const core::vector3df& scale) const = 0;

		//! Clones a static IMesh into a modifyable SMesh.
		/** All meshbuffers in the returned SMesh
		are of type SMeshBuffer or SMeshBufferLightMap.
		\param mesh: Mesh to copy.
		\return Returns the cloned mesh.
		If you no longer need the cloned mesh, you should call SMesh::drop().
		See IUnknown::drop() for more information. */
		virtual SMesh* createMeshCopy(IMesh* mesh) const = 0;

		//! Creates a planar texture mapping on the mesh
		/** \param mesh: Mesh on which the operation is performed.
		\param resolution: resolution of the planar mapping. This is the value
		specifying which is the relation between world space and 
		texture coordinate space. */
		virtual void makePlanarTextureMapping(IMesh* mesh, f32 resolution=0.001f) const = 0;

		//! Creates a copy of the mesh, which will only consist of S3DVertexTangents vertices.
        /** This is useful if you want to draw tangent space normal mapped geometry because
		it calculates the tangent and binormal data which is needed there.
		\param mesh: Input mesh
		\return Mesh consiting only of S3DVertexTangents vertices.
		If you no longer need the cloned mesh, you should call IMesh::drop().
		See IUnknown::drop() for more information. */
		virtual IMesh* createMeshWithTangents(IMesh* mesh) const = 0;

		//! Unweld vertices
		virtual IMesh* createMeshUniquePrimitives(IMesh* mesh) const = 0;

		//! Recalculates the bounding box for a meshbuffer
		virtual void recalculateBoundingBox(scene::IMeshBuffer* buffer) const = 0;

		//! Returns amount of polygons in mesh.
		virtual s32 getPolyCount(scene::IMesh* mesh) const = 0;

		//! Returns amount of polygons in mesh.
		virtual s32 getPolyCount(scene::IAnimatedMesh* mesh) const = 0;
	};

} // end namespace scene
} // end namespace irr


#endif

