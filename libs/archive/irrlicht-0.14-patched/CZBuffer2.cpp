// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CZBuffer2.h"

namespace irr
{
namespace video
{



//! constructor
CZBuffer2::CZBuffer2(const core::dimension2d<s32>& size)
: Buffer(0), Size(0,0), TotalSize(0), BufferEnd(0)
{
	#ifdef _DEBUG
	setDebugName("CZBuffer2");
	#endif

	setSize(size);
}



//! destructor
CZBuffer2::~CZBuffer2()
{
	if (Buffer)
		delete [] Buffer;
}



//! clears the zbuffer
void CZBuffer2::clear()
{
	u32 zMaxValue;

	f32 zMax = 1.f;
	zMaxValue = *(u32*) &zMax;

	memset32 ( Buffer, zMaxValue, TotalSize * sizeof ( TZBufferType2 ) );
}



//! sets the new size of the zbuffer
void CZBuffer2::setSize(const core::dimension2d<s32>& size)
{
	if (size == Size)
		return;

	Size = size;

	if (Buffer)
		delete [] Buffer;

	TotalSize = size.Width * size.Height;
	Buffer = new TZBufferType2[TotalSize];
	BufferEnd = Buffer + TotalSize;
}



//! returns the size of the zbuffer
const core::dimension2d<s32>& CZBuffer2::getSize()
{
	return Size;
}



//! locks the zbuffer
TZBufferType2* CZBuffer2::lock()
{
	return Buffer;
}



//! unlocks the zbuffer
void CZBuffer2::unlock()
{
}



//! creates a ZBuffer
IZBuffer2* createZBuffer2(const core::dimension2d<s32>& size)
{
	return new CZBuffer2(size);
}

	
} // end namespace video
} // end namespace irr


