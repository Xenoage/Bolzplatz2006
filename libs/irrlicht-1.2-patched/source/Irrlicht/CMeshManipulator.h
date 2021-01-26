// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_MESH_MANIPULATOR_H_INCLUDED__
#define __C_MESH_MANIPULATOR_H_INCLUDED__

#include "IMeshManipulator.h"

namespace irr
{
namespace scene
{

//! An interface for easily manipulate meshes.
/** Scale, set alpha value, flip surfaces, and so on. This exists for fixing problems 
 with wrong imported or exported meshes quickly after loading. It is not intended for doing mesh
 modifications and/or animations during runtime.
*/
class CMeshManipulator : public IMeshManipulator
{
public:

	//! Constructor
	CMeshManipulator();

	//! destructor
	virtual ~CMeshManipulator();

	//! Flips the direction of surfaces. Changes backfacing triangles to frontfacing
	//! triangles and vice versa.
	//! \param mesh: Mesh on which the operation is performed.
	virtual void flipSurfaces(scene::IMesh* mesh) const;

	//! Sets the alpha vertex color value of the whole mesh to a new value
	//! \param mesh: Mesh on which the operation is performed.
	//! \param alpha: New alpha for the vertex color.
	virtual void setVertexColorAlpha(scene::IMesh* mesh, s32 alpha) const;

	//! Sets the colors of all vertices to one color
	virtual void setVertexColors(IMesh* mesh, video::SColor color) const;

	//! Recalculates all normals of the mesh.
	/** \param mesh: Mesh on which the operation is performed. */
	virtual void recalculateNormals(scene::IMesh* mesh, bool smooth = false) const;

	//! Recalculates all normals of the mesh buffer.
	/** \param buffer: Mesh buffer on which the operation is performed. */
	virtual void recalculateNormals(IMeshBuffer* buffer, bool smooth = false) const;

	//! Scales the whole mesh.
	//! \param mesh: Mesh on which the operation is performed.
	//! \param scale: 3D Vector, defining the value, for each axis, to scale the mesh by.
	virtual void scaleMesh(scene::IMesh* mesh, const core::vector3df& scale) const;

	//! Clones a static IMesh into a modifyable SMesh.
	virtual SMesh* createMeshCopy(scene::IMesh* mesh) const;

	//! Creates a planar texture mapping on the mesh
	//! \param mesh: Mesh on which the operation is performed.
	//! \param resolution: resolution of the planar mapping. This is the value
	//! specifying which is the releation between world space and 
	//! texture coordinate space.
	virtual void makePlanarTextureMapping(scene::IMesh* mesh, f32 resolution) const;

	//! Creates a copy of the mesh, which will only consist of S3DVertexTangents vertices.
	//! This is useful if you want to draw tangent space normal mapped geometry because
	//! it calculates the tangent and binormal data which is needed there.
	//! \param mesh: Input mesh
	//! \return Mesh consiting only of S3DVertexNormalMapped vertices.
	//! If you no longer need the cloned mesh, you should call IMesh::drop().
	//! See IUnknown::drop() for more information.
	virtual IMesh* createMeshWithTangents(IMesh* mesh) const;

	virtual IMesh* createMeshUniquePrimitives(IMesh* mesh) const;

	//! Recalculates the bounding box for a meshbuffer
	virtual void recalculateBoundingBox(scene::IMeshBuffer* buffer) const;

	//! Returns amount of polygons in mesh.
	virtual s32 getPolyCount(scene::IMesh* mesh) const;

	//! Returns amount of polygons in mesh.
	virtual s32 getPolyCount(scene::IAnimatedMesh* mesh) const;

private:

	static void calculateTangents(core::vector3df& normal, 
		core::vector3df& tangent, 
		core::vector3df& binormal, 
		core::vector3df& vt1, core::vector3df& vt2, core::vector3df& vt3,
		core::vector2df& tc1, core::vector2df& tc2, core::vector2df& tc3);
};

} // end namespace scene
} // end namespace irr


#endif

