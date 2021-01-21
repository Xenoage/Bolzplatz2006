// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_COLOR_CONVERTER_H_INCLUDED__
#define __C_COLOR_CONVERTER_H_INCLUDED__

namespace irr
{
namespace video
{

class CColorConverter
{
public:

	//! converts a 4 bit palettized image into R5G5B5
	static void convert4BitTo16BitFlipMirror(const c8* in, s16* out, s32 width, s32 height, s32 pitch, const s32* palette);

	//! converts a 8 bit palettized image into R5G5B5
	static void convert8BitTo16BitFlipMirror(const c8* in, s16* out, s32 width, s32 height, s32 pitch, const s32* palette);

	//! converts a 8 bit palettized image into R5G5B5
	static void convert8BitTo16Bit(const c8* in, s16* out, s32 width, s32 height, s32 pitch, const s32* palette);

	//! converts a monochrome bitmap to A1R5G5B5 data
	static void convert1BitTo16BitFlipMirror(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! converts R8G8B8 24 bit data to A1R5G5B5 data, and flips and 
	//! mirrors the image during the process.
	static void convert24BitTo16BitFlipMirror(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! converts R8G8B8 16 bit data to A1R5G5B5 data, and flips and 
	//! mirrors the image during the process.
	static void convert16BitTo16BitFlipMirror(const s16* in, s16* out, s32 width, s32 height, s32 pitch);

	//! copies R8G8B8 24 bit data to 24 data, and flips and 
	//! mirrors the image during the process.
	static void convert24BitTo24BitFlipMirrorColorShuffle(const c8* in, c8* out, s32 width, s32 height, s32 pitch);

	//! converts R8G8B8 24 bit data to A1R5G5B5 data (used e.g for JPG to A1R5G5B5)
	//! accepts colors in different order.
	static void convert24BitTo16BitColorShuffle(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! converts R8G8B8 24 bit data to A1R5G5B5 data (used e.g for JPG to A1R5G5B5)
	//! accepts colors in different order.
	static void convert24BitTo16BitFlipColorShuffle(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! converts X8R8G8B8 32 bit data to A1R5G5B5 data, and flips and 
	//! accepts colors in different order.
	static void convert32BitTo16BitColorShuffle(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! converts X8R8G8B8 32 bit data to A1R5G5B5 data, and flips and 
	//! mirrors the image during the process, accepts colors in different order.
	static void convert32BitTo16BitFlipMirrorColorShuffle(const c8* in, s16* out, s32 width, s32 height, s32 pitch);

	//! copies X8R8G8B8 32 bit data, and flips and 
	//! mirrors the image during the process.
	static void convert32BitTo32BitFlipMirror(const s32* in, s32* out, s32 width, s32 height, s32 pitch);

	//! Resizes the surface to a new size and converts it at the same time
	//! to an A8R8G8B8 format, returning the pointer to the new buffer.
	//! The returned pointer has to be deleted.
	static void convert16bitToA8R8G8B8andResize(const s16* in, s32* out, s32 newWidth, s32 newHeight, s32 currentWidth, s32 currentHeight);
};


} // end namespace video
} // end namespace irr

#endif

