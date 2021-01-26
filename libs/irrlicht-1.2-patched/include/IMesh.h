// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_MESH_H_INCLUDED__
#define __I_MESH_H_INCLUDED__

#include "IUnknown.h"
#include "IMeshBuffer.h"

namespace irr
{
namespace scene
{
	//! Class for accessing a mesh with multiple mesh buffers.
	/** An IMesh is nothing more than a collection of some mesh buffers (IMeshBuffer).
	SMesh is a simple implementation of an IMesh.
	*/
	class IMesh : public virtual IUnknown
	{
	public:

		//! destructor
		virtual ~IMesh(){};

		//! Returns the amount of mesh buffers.
		/** \return Returns the amount of mesh buffers (IMeshBuffer) in this mesh. */
		virtual s32 getMeshBufferCount() = 0;

		//! Returns pointer to a mesh buffer.
 		/** \param nr: Zero based index of the mesh buffer. The maximum value is
		getMeshBufferCount() - 1;
		\return Returns the pointer to the mesh buffer or 
		NULL if there is no such mesh buffer. */
		virtual IMeshBuffer* getMeshBuffer(s32 nr) = 0;

		//! Returns an axis aligned bounding box of the mesh.
		/** \return A bounding box of this mesh is returned. */
		virtual const core::aabbox3d<f32>& getBoundingBox() const = 0;

		//! Returns an axis aligned bounding box of the mesh.
		/** \return A bounding box of this mesh is returned. */
		virtual core::aabbox3d<f32>& getBoundingBox() = 0;

		//! Sets a flag of all contained materials to a new value.
		/** \param flag: Flag to set in all materials.
		 \param newvalue: New value to set in all materials. */
		virtual void setMaterialFlag(video::E_MATERIAL_FLAG flag, bool newvalue) = 0;
	};

} // end namespace scene
} // end namespace irr

#endif

