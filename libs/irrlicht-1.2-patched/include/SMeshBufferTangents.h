// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __S_MESH_BUFFER_TANGENTS_H_INCLUDED__
#define __S_MESH_BUFFER_TANGENTS_H_INCLUDED__

#include "irrArray.h"
#include "IMeshBuffer.h"

namespace irr
{
namespace scene
{
	//! Simple implementation of the IMeshBuffer interface with S3DVertexTangents vertices.
	struct SMeshBufferTangents : public IMeshBuffer
	{
		//! constructor
		SMeshBufferTangents()
		{
			#ifdef _DEBUG
			setDebugName("SMeshBufferTangents");
			#endif
		}

		//! destructor
		~SMeshBufferTangents() {}; 

		//! returns the material of this meshbuffer
		virtual const video::SMaterial& getMaterial() const
		{
			return Material;
		}

		//! returns the material of this meshbuffer
		virtual video::SMaterial& getMaterial()
		{
			return Material;
		}

		//! returns pointer to vertices
		virtual const void* getVertices() const
		{
			return Vertices.const_pointer();
		} 

		//! returns pointer to vertices
		virtual void* getVertices()
		{
			return Vertices.pointer();
		} 

		//! returns amount of vertices
		virtual s32 getVertexCount() const
		{
			return Vertices.size();
		}

		//! returns pointer to Indices
		virtual const u16* getIndices() const
		{
			return Indices.const_pointer();
		}

		//! returns pointer to Indices
		virtual u16* getIndices()
		{
			return Indices.pointer();
		}

		//! returns amount of indices
		virtual s32 getIndexCount() const
		{
			return Indices.size();
		}

		//! returns an axis aligned bounding box
		virtual const core::aabbox3d<f32>& getBoundingBox() const
		{
			return BoundingBox;
		}

		//! returns an axis aligned bounding box
		virtual core::aabbox3d<f32>& getBoundingBox()
		{
			return BoundingBox;
		}

		//! recalculates the bounding box. should be called if the mesh changed.
		void recalculateBoundingBox()
		{
			if (Vertices.empty())
				BoundingBox.reset(0,0,0);
			else
			{
				BoundingBox.reset(Vertices[0].Pos);
				for (u32 i=1; i<Vertices.size(); ++i)
					BoundingBox.addInternalPoint(Vertices[i].Pos);
			}
		}

		//! returns which type of vertex data is stored.
		virtual video::E_VERTEX_TYPE getVertexType() const
		{
			return video::EVT_TANGENTS;
		}

		video::SMaterial Material; //! material for this meshBuffer.
		core::array<video::S3DVertexTangents> Vertices; //! Array of vertices
		core::array<u16> Indices; //! Array of the Indices.
		core::aabbox3d<f32> BoundingBox;
	};

} // end namespace scene
} // end namespace irr

#endif

