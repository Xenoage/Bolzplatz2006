// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSoftwareTexture.h"
#include "os.h"

namespace irr
{
namespace video  
{

//! constructor
CSoftwareTexture::CSoftwareTexture(IImage* image, const char* name)
: ITexture(name), Texture(0)
{
	#ifdef _DEBUG
	setDebugName("CSoftwareTexture");
	#endif	

	if (image)
	{
		core::dimension2d<s32> optSize;
		core::dimension2d<s32> origSize = image->getDimension();
		OrigSize = origSize;

		optSize.Width = getTextureSizeFromSurfaceSize(origSize.Width);
		optSize.Height = getTextureSizeFromSurfaceSize(origSize.Height);

		Image = new CImage(ECF_A1R5G5B5, image);

		if (optSize == origSize)
		{
			Texture = Image;
			Texture->grab();
		}
		else
		{
			Texture = new CImage(ECF_A1R5G5B5, optSize);
			Image->copyToScaling(Texture);			
		}
	}
}



//! destructor
CSoftwareTexture::~CSoftwareTexture()
{
	if (Image)
		Image->drop();

	if (Texture)
		Texture->drop();
}



//! lock function
void* CSoftwareTexture::lock()
{
	return Image->lock();
}



//! unlock function
void CSoftwareTexture::unlock()
{
	if (Image != Texture)
	{
		os::Printer::log("Performance warning, slow unlock of non power of 2 texture.", ELL_WARNING);
		Image->copyToScaling(Texture);
	}

	Image->unlock();
}


//! Returns original size of the texture.
const core::dimension2d<s32>& CSoftwareTexture::getOriginalSize()
{
	return OrigSize;
}


//! Returns (=size) of the texture.
const core::dimension2d<s32>& CSoftwareTexture::getSize()
{
	return Image->getDimension();
}


//! returns unoptimized surface
CImage* CSoftwareTexture::getImage()
{
	return Image;
}



//! returns texture surface
CImage* CSoftwareTexture::getTexture()
{
	return Texture;
}



//! returns the size of a texture which would be the optimize size for rendering it
inline s32 CSoftwareTexture::getTextureSizeFromSurfaceSize(s32 size)
{
	s32 ts = 0x01;
	while(ts < size)
		ts <<= 1;

	return ts;
}



//! returns driver type of texture (=the driver, who created the texture)
E_DRIVER_TYPE CSoftwareTexture::getDriverType()
{
	return EDT_SOFTWARE;
}



//! returns color format of texture
ECOLOR_FORMAT CSoftwareTexture::getColorFormat() const
{
	return ECF_A1R5G5B5;
}



//! returns pitch of texture (in bytes)
s32 CSoftwareTexture::getPitch()
{
	return Image->getDimension().Width * 2;
}


//! Regenerates the mip map levels of the texture. Useful after locking and 
//! modifying the texture
void CSoftwareTexture::regenerateMipMapLevels()
{
	// our software textures don't have mip maps
}



} // end namespace video
} // end namespace irr
