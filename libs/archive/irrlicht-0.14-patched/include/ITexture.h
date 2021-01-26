// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_TEXTURE_H_INCLUDED__
#define __I_TEXTURE_H_INCLUDED__

#include "IUnknown.h"
#include "IImage.h"
#include "dimension2d.h"
#include "EDriverTypes.h"

namespace irr
{
namespace video  
{


//! Enumeration flags telling the video driver in which format textures
//! should be created.
enum E_TEXTURE_CREATION_FLAG
{
	//! Forces the driver to create 16 bit textures always, indepenent of
	//! which format the file on disk has. When choosing this you may loose
	//! some color detail, but gain much speed and memory. 16 bit textures
	//! can be transferred twice as fast as 32 bit textures and only use 
	//! half of the space in memory.
	//! When using this flag, it does not make sence to use the flags
	//! ETCF_ALWAYS_32_BIT, ETCF_OPTIMIZED_FOR_QUALITY,
	//! or ETCF_OPTIMIZED_FOR_SPEED at the same time.
	ETCF_ALWAYS_16_BIT = 0x00000001,

	//! Forces the driver to create 32 bit textures always, indepenent of
	//! which format the file on disk has. Please note that some drivers 
	//! (like the software device) will ignore this, because they only are
	//! able to create and use 16 bit textures.
	//! When using this flag, it does not make sence to use the flags
	//! ETCF_ALWAYS_16_BIT, ETCF_OPTIMIZED_FOR_QUALITY,
	//! or ETCF_OPTIMIZED_FOR_SPEED at the same time.
	ETCF_ALWAYS_32_BIT = 0x00000002,

	//! Lets the driver decide in which format the texutures are created and
	//! tries to make the textures look as good as possible.
	//! Usually it simply chooses the format in which the texture was stored on disk.
	//! When using this flag, it does not make sence to use the flags
	//! ETCF_ALWAYS_16_BIT, ETCF_ALWAYS_32_BIT, 
	//! or ETCF_OPTIMIZED_FOR_SPEED at the same time.
	ETCF_OPTIMIZED_FOR_QUALITY  = 0x00000004,

	//! Lets the driver decide in which format the texutures are created and
	//! tries to create them maximizing render speed.
	//! When using this flag, it does not make sence to use the flags
	//! ETCF_ALWAYS_16_BIT, ETCF_ALWAYS_32_BIT, or ETCF_OPTIMIZED_FOR_QUALITY,
	//! at the same time.
	ETCF_OPTIMIZED_FOR_SPEED = 0x00000008,

	//! Automaticly creates mip map levels for the textures.
	ETCF_CREATE_MIP_MAPS = 0x00000010,

	//! This flag is never used, it only forces the compiler to 
    //! compile these enumeration values to 32 bit.
	ETCF_FORCE_32_BIT_DO_NOT_USE = 0x7fffffff,
};


//! Helper function, helps to get the wished texture creation format from the flags.
//! Returns either ETCF_ALWAYS_32_BIT, ETCF_ALWAYS_16_BIT, ETCF_OPTIMIZED_FOR_QUALITY,
//! or ETCF_OPTIMIZED_FOR_SPEED.
inline E_TEXTURE_CREATION_FLAG getTextureFormatFromFlags(u32 flags)
{
	if (flags & ETCF_OPTIMIZED_FOR_SPEED)
		return ETCF_OPTIMIZED_FOR_SPEED;
	if (flags & ETCF_ALWAYS_16_BIT)
		return ETCF_ALWAYS_16_BIT;
	if (flags & ETCF_ALWAYS_32_BIT)
		return ETCF_ALWAYS_32_BIT;
	if (flags & ETCF_OPTIMIZED_FOR_QUALITY)
		return ETCF_OPTIMIZED_FOR_QUALITY;
	return ETCF_OPTIMIZED_FOR_SPEED;
}


//! Interface for a Video Driver dependent Texture.
/**
	An ITexture is created by an IVideoDriver by using IVideoDriver::addTexture or
	IVideoDriver::getTexture. After this, this texture may only be used by this VideoDriver.
	As you can imagine, textures of the DirectX and the OpenGL device will not be compatible,
	for example.
	An exception is the Software device and the NULL device, their textures are compatible.
	If you try to use a texture created by one device with an other device, the device
	will refuse to do that and write a warning or an error message to the output buffer.
*/
class ITexture : public IUnknown
{
public:

	//! destructor
	virtual ~ITexture() {};

	//! Lock function. 
	/** Locks the Texture and returns a pointer to access the 
	pixels. After lock() has been called and all operations on the pixels
	are done, you must call unlock().
	\return Returns a pointer to the pixel data. The format of the pixel can
	be determinated by using getColorFormat(). NULL is returned, if
	the texture cannot be locked. */
	virtual void* lock() = 0;

	//! Unlock function. Must be called after a lock() to the texture.
	virtual void unlock() = 0;
	
	//! Sets the given number of pixels on the texture to white (snow) (ANDI)
	virtual void snowPixel(s32 count)
	{
  }

	//! Returns original size of the texture.
	/** The texture is usually
	scaled, if it was created with an unoptimal size. For example if the size
	of the texture file it was loaded from was not a power of two. This returns
	the size of the texture, it had before it was scaled. Can be useful
	when drawing 2d images on the screen, which should have the exact size
	of the original texture. Use ITexture::getSize() if you want to know 
	the real size it has now stored in the system.
	\return Returns the original size of the texture. */
	virtual const core::dimension2d<s32>& getOriginalSize() = 0;

	//! Returns dimension (=size) of the texture.
	/** \return Returns the size of the texture. */
	virtual const core::dimension2d<s32>& getSize() = 0;

	//! Returns driver type of texture. 
	/** This is the driver, which created the texture.
	This method is used internally by the video devices, to check, if they may
	use a texture because textures may be incompatible between different
	devices.
	\return Returns driver type of texture. */
	virtual E_DRIVER_TYPE getDriverType() = 0;

	//! Returns the color format of texture.
	/** This format is in most cases
	ECF_A1R5G5B5 or ECF_A8R8G8B8.
	\return Returns the color format of texture. */
	virtual ECOLOR_FORMAT getColorFormat() = 0;

	//! Returns pitch of texture (in bytes).
	/** The pitch is the amount of bytes
	used for a row of pixels in a texture.
	\return Returns pitch of texture in bytes. */
	virtual s32 getPitch() = 0;

	//! Regenerates the mip map levels of the texture.
	/** Useful after locking and modifying the texture */
	virtual void regenerateMipMapLevels() = 0;
};


} // end namespace video
} // end namespace irr

#endif

