// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "SoftwareDriver2_compile_config.h"
#include "SoftwareDriver2_helper.h"
#include "CSoftwareTexture2.h"
#include "os.h"

namespace irr
{
namespace video  
{

//! constructor
CSoftwareTexture2::CSoftwareTexture2(IImage* image, const char* name, bool generateMipLevels)
: ITexture(name), HasMipMaps(generateMipLevels),MipMapLOD(0)
{
	#ifndef SOFTWARE_DRIVER_2_MIPMAPPING
		HasMipMaps = false;
	#endif

	for ( s32 i = 0; i!= SOFTWARE_DRIVER_2_MIPMAPPING_MAX; ++i )
	{
		MipMap[i] = 0;
	}

	if (image)
	{
		
		core::dimension2d<s32> optSize;
		core::dimension2d<s32> origSize = image->getDimension();
		OrigSize = origSize;

		optSize.Width = getTextureSizeFromSurfaceSize(origSize.Width);
		optSize.Height = getTextureSizeFromSurfaceSize(origSize.Height);
		
		OrigSize = optSize;

		if ( origSize == optSize )
		{
			MipMap[0] = new CImage(ECF_SOFTWARE2, image);
		}
		else
		{
			MipMap[0] = new CImage(ECF_SOFTWARE2, optSize);

			// temporary CImage needed
			CImage * temp = new CImage ( ECF_SOFTWARE2, image );
			temp->copyToScaling(MipMap[0]);
			temp->drop ();
		}


	}

	regenerateMipMapLevels ();
	setCurrentMipMapLOD ( 0 );
}



//! destructor
CSoftwareTexture2::~CSoftwareTexture2()
{
	for ( s32 i = 0; i!= SOFTWARE_DRIVER_2_MIPMAPPING_MAX; ++i )
	{
		if ( MipMap[i] )
			MipMap[i]->drop ();
	}
}



//! lock function
void* CSoftwareTexture2::lock()
{
	return MipMap[MipMapLOD]->lock();
}



//! unlock function
void CSoftwareTexture2::unlock()
{
	MipMap[MipMapLOD]->unlock();
}


//! Returns original size of the texture.
const core::dimension2d<s32>& CSoftwareTexture2::getOriginalSize()
{
	return MipMap[0]->getDimension();
}


//! Returns (=size) of the texture.
const core::dimension2d<s32>& CSoftwareTexture2::getSize()
{
	return MipMap[MipMapLOD]->getDimension();
}


//! returns unoptimized surface
CImage* CSoftwareTexture2::getImage()
{
	return MipMap[0];
}



//! returns texture surface
CImage* CSoftwareTexture2::getTexture()
{
	return MipMap[MipMapLOD];
}



//! returns the size of a texture which would be the optimize size for rendering it
inline s32 CSoftwareTexture2::getTextureSizeFromSurfaceSize(s32 size)
{
	s32 ts = 0x01;
	while(ts < size)
		ts <<= 1;

/*
	if (ts > size && ts > 64)
		ts >>= 1;
*/
	return ts;
}



//! returns driver type of texture (=the driver, who created the texture)
E_DRIVER_TYPE CSoftwareTexture2::getDriverType()
{
	return EDT_SOFTWARE2;
}



//! returns color format of texture
ECOLOR_FORMAT CSoftwareTexture2::getColorFormat() const
{
	return ECF_SOFTWARE2;
}



//! returns pitch of texture (in bytes)
s32 CSoftwareTexture2::getPitch()
{
	return MipMap[MipMapLOD]->getPitch();
}

//! Select a Mipmap Level
void CSoftwareTexture2::setCurrentMipMapLOD ( s32 lod )
{
	if ( HasMipMaps )
		MipMapLOD = lod;
}


//! Regenerates the mip map levels of the texture. Useful after locking and 
//! modifying the texture
void CSoftwareTexture2::regenerateMipMapLevels()
{
	if ( false == HasMipMaps )
		return;

	s32 i;

	// release
	for ( i = 1; i!= SOFTWARE_DRIVER_2_MIPMAPPING_MAX; ++i )
	{
		if ( MipMap[i] )
			MipMap[i]->drop ();
	}

	core::dimension2d<s32> newSize;
	core::dimension2d<s32> currentSize;

	i = 1;
	CImage * c = MipMap[0];
	while ( i < SOFTWARE_DRIVER_2_MIPMAPPING_MAX )
	{
		currentSize = c->getDimension();
		newSize.Width = max ( 1, currentSize.Width >> 1 );
		newSize.Height = max ( 1, currentSize.Height >> 1 );

		MipMap[i] = new CImage(ECF_SOFTWARE2, newSize);
		MipMap[0]->copyToScalingBoxFilter ( MipMap[i], 0 );
		c = MipMap[i];
		i += 1;
	}
}


} // end namespace video
} // end namespace irr
