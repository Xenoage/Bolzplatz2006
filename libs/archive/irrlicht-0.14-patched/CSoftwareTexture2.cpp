// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSoftwareTexture2.h"
#include "os.h"

namespace irr
{
namespace video  
{

//! constructor
CSoftwareTexture2::CSoftwareTexture2(IImage* image)
: Texture(0)
{
	#ifdef _DEBUG
	setDebugName("CSoftwareTexture2");
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
CSoftwareTexture2::~CSoftwareTexture2()
{
	if (Image)
		Image->drop();

	if (Texture)
		Texture->drop();
}



//! lock function
void* CSoftwareTexture2::lock()
{
	return Image->lock();
}



//! unlock function
void CSoftwareTexture2::unlock()
{
	if (Image != Texture)
	{
		os::Printer::log("Performance warning, slow unlock of non power of 2 texture.", ELL_WARNING);
		Image->copyToScaling(Texture);
	}

	Image->unlock();
}


//! Returns original size of the texture.
const core::dimension2d<s32>& CSoftwareTexture2::getOriginalSize()
{
	return OrigSize;
}


//! Returns (=size) of the texture.
const core::dimension2d<s32>& CSoftwareTexture2::getSize()
{
	return Image->getDimension();
}


//! returns unoptimized surface
CImage* CSoftwareTexture2::getImage()
{
	return Image;
}



//! returns texture surface
CImage* CSoftwareTexture2::getTexture()
{
	return Texture;
}



//! returns the size of a texture which would be the optimize size for rendering it
inline s32 CSoftwareTexture2::getTextureSizeFromSurfaceSize(s32 size)
{
	s32 ts = 0x01;
	while(ts < size)
		ts <<= 1;

	return ts;
}



//! returns driver type of texture (=the driver, who created the texture)
E_DRIVER_TYPE CSoftwareTexture2::getDriverType()
{
	return EDT_SOFTWARE2;
}



//! returns color format of texture
ECOLOR_FORMAT CSoftwareTexture2::getColorFormat()
{
	return ECF_A1R5G5B5;
}



//! returns pitch of texture (in bytes)
s32 CSoftwareTexture2::getPitch()
{
	return Image->getDimension().Width * 2;
}


//! Regenerates the mip map levels of the texture. Useful after locking and 
//! modifying the texture
void CSoftwareTexture2::regenerateMipMapLevels()
{
	// our software textures don't have mip maps
}



} // end namespace video
} // end namespace irr
