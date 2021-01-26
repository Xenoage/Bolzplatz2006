// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_IMAGE_LOADER_TGA_H_INCLUDED__
#define __C_IMAGE_LOADER_TGA_H_INCLUDED__

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

	 struct STGAHeader{
	    u8 IdLength;
	    u8 ColorMapType;
	    u8 ImageType;
	    u8 FirstEntryIndex[2];
	    u16 ColorMapLength;
	    u8 ColorMapEntrySize;
	    u8 XOrigin[2];
	    u8 YOrigin[2];
	    u16 ImageWidth;
	    u16 ImageHeight;
	    u8 PixelDepth;
	    u8 ImageDescriptor;
    } PACK_STRUCT;

	struct STGAFooter
	{
		u32 ExtensionOffset;
		u32 DeveloperOffset;
		c8  Signature[18];
	} PACK_STRUCT;

// Default alignment
#ifdef _MSC_VER
#	pragma pack( pop, packing )
#endif

#undef PACK_STRUCT

/*!
	Surface Loader for targa images
*/
class CImageLoaderTGA : public IImageLoader
{
public:

	//! constructor
	CImageLoaderTGA();

	//! destructor
	virtual ~CImageLoaderTGA();

	//! returns true if the file maybe is able to be loaded by this class
	//! based on the file extension (e.g. ".tga")
	virtual bool isALoadableFileExtension(const c8* fileName);

	//! returns true if the file maybe is able to be loaded by this class
	virtual bool isALoadableFileFormat(irr::io::IReadFile* file);

	//! creates a surface from the file
	virtual IImage* loadImage(irr::io::IReadFile* file);

private:

	//! loads a compressed tga. Was written and sent in by Jon Pry, thank you very much!
	u8* loadCompressedImage(irr::io::IReadFile *file, const STGAHeader& header);

};


} // end namespace video
} // end namespace irr


#endif

