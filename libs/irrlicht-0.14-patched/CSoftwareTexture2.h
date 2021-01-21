// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_SOFTWARE_2_TEXTURE_H_INCLUDED__
#define __C_SOFTWARE_2_TEXTURE_H_INCLUDED__

#include "ITexture.h"
#include "CImage.h"

namespace irr
{
namespace video  
{

/*!
	interface for a Video Driver dependent Texture.
*/
class CSoftwareTexture2 : public ITexture
{
public:

	//! constructor
	CSoftwareTexture2(IImage* surface);

	//! destructor
	virtual ~CSoftwareTexture2();

	//! lock function
	virtual void* lock();

	//! unlock function
	virtual void unlock();

	//! Returns original size of the texture.
	virtual const core::dimension2d<s32>& getOriginalSize();

	//! Returns (=size) of the texture.
	virtual const core::dimension2d<s32>& getSize();

	//! returns unoptimized surface
	virtual CImage* getImage();

	//! returns texture surface
	virtual CImage* getTexture();

	//! returns driver type of texture (=the driver, who created the texture)
	virtual E_DRIVER_TYPE getDriverType();

	//! returns color format of texture
	virtual ECOLOR_FORMAT getColorFormat();

	//! returns pitch of texture (in bytes)
	virtual s32 getPitch();

	//! Regenerates the mip map levels of the texture. Useful after locking and 
	//! modifying the texture
	virtual void regenerateMipMapLevels();

private:

	//! returns the size of a texture which would be the optimize size for rendering it
	inline s32 getTextureSizeFromSurfaceSize(s32 size);

	CImage* Image;
	CImage* Texture;
	core::dimension2d<s32> OrigSize;

};


} // end namespace video
} // end namespace irr

#endif

