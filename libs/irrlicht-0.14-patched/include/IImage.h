// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_IMAGE_H_INCLUDED__
#define __I_IMAGE_H_INCLUDED__

#include "IUnknown.h"
#include "position2d.h"
#include "rect.h"
#include "SColor.h"

namespace irr
{
namespace video  
{

//! An enum for the color format of textures used by the Irrlicht Engine.
/** A color format specifies how color information is stored. The Irrlicht Engine
mostly uses the format ECF_A1R5G5B5.
*/
enum ECOLOR_FORMAT
{
	//! 16 bit color format used by the software driver, and thus preferred
	//! by all other irrlicht engine video drivers. There are 5 bits for every
	//! color component, and a single bit is left for alpha information.
	ECF_A1R5G5B5 = 0,	

	//! Standard 16 bit color format.
	ECF_R5G6B5,

	//! 24 bit color, no alpha channel, but 8 bit for red, green and blue.
	ECF_R8G8B8,

	//! Default 32 bit color format. 8 bits are used for every component:
	//! red, green, blue and alpha.
	ECF_A8R8G8B8,
};


//!	Interface for software image data.
/** Image loaders create these images from files. IVideoDrivers convert
these images into their (hardware) textures. 
*/
class IImage : public IUnknown
{
public:

	//! destructor
	virtual ~IImage() {};

	//! Lock function. Use this to get a pointer to the image data. After you
	//! don't need the pointer anymore, you must call unlock().
	//! \return Returns pointer to the image data. What type of data
	//! is pointed to depends on the color format of the image. For example
	//! if the color format is ECF_A8R8G8B8, it is of s32.
	//! Be sure to call unlock() after you don't need the pointer any more.
	virtual void* lock() = 0;

	//! Unlock function.
	//! Should be called after the pointer received by lock() is not
	//! needed anymore.
	virtual void unlock() = 0;

	//! Returns width and height of image data.
	virtual const core::dimension2d<s32>& getDimension() = 0;

	//! Returns bits per pixel. 
	virtual s32 getBitsPerPixel() = 0;

	//! Returns bytes per pixel
	virtual s32 getBytesPerPixel() = 0;

	//! Returns image data size in bytes
	virtual s32 getImageDataSizeInBytes() = 0;

	//! Returns image data size in pixels
	virtual s32 getImageDataSizeInPixels() = 0;

	//! returns a pixel
	virtual SColor getPixel(s32 x, s32 y) = 0;

	//! returns the color format
	virtual ECOLOR_FORMAT getColorFormat() = 0;
};

} // end namespace video
} // end namespace irr

#endif

