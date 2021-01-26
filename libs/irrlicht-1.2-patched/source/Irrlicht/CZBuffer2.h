// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_Z_BUFFER_H_INCLUDED__
#define __C_Z_BUFFER_H_INCLUDED__

#include "IZBuffer2.h"

namespace irr
{
namespace video
{

	class CZBuffer2 : public IZBuffer2
	{
	public:

		//! constructor
		CZBuffer2(const core::dimension2d<s32>& size);

		//! destructor
		virtual ~CZBuffer2();

		//! clears the zbuffer
		virtual void clear();

		//! sets the new size of the zbuffer
		virtual void setSize(const core::dimension2d<s32>& size);

		//! returns the size of the zbuffer
		virtual const core::dimension2d<s32>& getSize();

		//! locks the zbuffer
		virtual TZBufferType2* lock();

		//! unlocks the zbuffer
		virtual void unlock();

	private:

		TZBufferType2* Buffer;
		core::dimension2d<s32> Size;
		s32 TotalSize;
	};
	
} // end namespace video
} // end namespace irr

#endif

