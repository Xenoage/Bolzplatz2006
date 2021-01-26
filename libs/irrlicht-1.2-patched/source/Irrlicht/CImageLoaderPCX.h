// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_IMAGE_LOADER_PCX_H_INCLUDED__
#define __C_IMAGE_LOADER_PCX_H_INCLUDED__

#include "IImageLoader.h"

namespace irr
{
namespace video
{


// byte-align structures
#ifdef _MSC_VER
#	pragma pack( push, packing )
#	pragma pack( 1 )
#	define PACK_STRUCT
#elif defined( __GNUC__ )
#	define PACK_STRUCT	__attribute__((packed))
#else
#	error compiler not supported
#endif

	struct SPCXHeader
	{
		u8	Manufacturer;
		u8	Version;
		u8	Encoding;
		u8	BitsPerPixel;
		u16	XMin;
		u16	YMin;
		u16	XMax;
		u16	YMax;
		u16	HorizDPI;
		u16	VertDPI;
		u8	Palette[48];
		u8	Reserved;
		u8	Planes;
		u16	BytesPerLine;
		u16	PaletteType;
		u16	HScrSize;
		u16	VScrSize;
		u8	Filler[54];
	} PACK_STRUCT;


// Default alignment
#ifdef _MSC_VER
#	pragma pack( pop, packing )
#endif

#undef PACK_STRUCT


/*!
	Image Loader for Windows PCX bitmaps.
	This loader was written and sent in by Dean P. Macri. I modified
	only some small bits of it.
*/
class CImageLoaderPCX : public IImageLoader
{
public:

	//! constructor
	CImageLoaderPCX();

	//! destructor
	virtual ~CImageLoaderPCX();

	//! returns true if the file maybe is able to be loaded by this class
	//! based on the file extension (e.g. ".tga")
	virtual bool isALoadableFileExtension(const c8* fileName);

	//! returns true if the file maybe is able to be loaded by this class
	virtual bool isALoadableFileFormat(irr::io::IReadFile* file);

	//! creates a surface from the file
	virtual IImage* loadImage(irr::io::IReadFile* file);

private:

	u8* PCXData;
	s32* PaletteData;
};


} // end namespace video
} // end namespace irr


#endif

