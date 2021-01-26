// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_Z2_BUFFER_H_INCLUDED__
#define __I_Z2_BUFFER_H_INCLUDED__

#include "IUnknown.h"
#include "dimension2d.h"
#include "S4DVertex.h"

namespace irr
{
namespace video
{
	class IZBuffer2 : public virtual IUnknown
	{
	public:

		//! destructor
		virtual ~IZBuffer2() {};

		//! clears the zbuffer
		virtual void clear() = 0;

		//! sets the new size of the zbuffer
		virtual void setSize(const core::dimension2d<s32>& size) = 0;

		//! returns the size of the zbuffer
		virtual const core::dimension2d<s32>& getSize() = 0;

		//! locks the zbuffer
		virtual TZBufferType2* lock() = 0;

		//! unlocks the zbuffer
		virtual void unlock() = 0;
	};


	//! creates a ZBuffer
	IZBuffer2* createZBuffer2(const core::dimension2d<s32>& size);

} // end namespace video
} // end namespace irr

#endif

