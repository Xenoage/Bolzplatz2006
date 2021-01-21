// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_IMAGE_LOADER_JPG_H_INCLUDED__
#define __C_IMAGE_LOADER_JPG_H_INCLUDED__

#include "IImageLoader.h"

#include <stdio.h>

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_LIBJPEG_
extern "C" {
	#include "jpeglib/jconfig.h"
	#ifndef _IRR_USE_NON_SYSTEM_JPEG_LIB_
	#include <jpeglib.h> // use system lib
	#else  // _IRR_USE_NON_SYSTEM_JPEG_LIB_
	#include "jpeglib/jpeglib.h" // use irrlicht jpeglib
#endif // _IRR_USE_NON_SYSTEM_JPEG_LIB_
}
#endif // _IRR_COMPILE_WITH_LIBJPEG_


namespace irr
{
namespace video
{


//! Surface Loader fow JPG images
class CImageLoaderJPG : public IImageLoader
{
public:

	//! constructor
	CImageLoaderJPG();

	//! destructor
	virtual ~CImageLoaderJPG();

	//! returns true if the file maybe is able to be loaded by this class
	//! based on the file extension (e.g. ".tga")
	virtual bool isALoadableFileExtension(const c8* fileName);

	//! returns true if the file maybe is able to be loaded by this class
	virtual bool isALoadableFileFormat(irr::io::IReadFile* file);

	//! creates a surface from the file
	virtual IImage* loadImage(irr::io::IReadFile* file);

private:

    #ifdef _IRR_COMPILE_WITH_LIBJPEG_
    
	/*	Initialize source.  This is called by jpeg_read_header() before any
	data is actually read.  Unlike init_destination(), it may leave
	bytes_in_buffer set to 0 (in which case a fill_input_buffer() call
	will occur immediately). */
	static void init_source (j_decompress_ptr cinfo);

	/*	This is called whenever bytes_in_buffer has reached zero and more
	data is wanted.  In typical applications, it should read fresh data
	into the buffer (ignoring the current state of next_input_byte and
	bytes_in_buffer), reset the pointer & count to the start of the
	buffer, and return TRUE indicating that the buffer has been reloaded.
	It is not necessary to fill the buffer entirely, only to obtain at
	least one more byte.  bytes_in_buffer MUST be set to a positive value
	if TRUE is returned.  A FALSE return should only be used when I/O
	suspension is desired (this mode is discussed in the next section). */
	static boolean fill_input_buffer (j_decompress_ptr cinfo);

	/* Skip num_bytes worth of data.  The buffer pointer and count should
	be advanced over num_bytes input bytes, refilling the buffer as
	needed.  This is used to skip over a potentially large amount of
	uninteresting data (such as an APPn marker).  In some applications
	it may be possible to optimize away the reading of the skipped data,
	but it's not clear that being smart is worth much trouble; large
	skips are uncommon.  bytes_in_buffer may be zero on return.
	A zero or negative skip count should be treated as a no-op. */
	static void skip_input_data (j_decompress_ptr cinfo, long num_bytes);

	/* This routine is called only when the decompressor has failed to find
	a restart (RSTn) marker where one is expected.  Its mission is to
	find a suitable point for resuming decompression.  For most
	applications, we recommend that you just use the default resync
	procedure, jpeg_resync_to_restart().  However, if you are able to back
	up in the input data stream, or if you have a-priori knowledge about
	the likely location of restart markers, you may be able to do better.
	Read the read_restart_marker() and jpeg_resync_to_restart() routines
	in jdmarker.c if you think you'd like to implement your own resync
	procedure. */
	static void resync_to_restart (j_decompress_ptr cinfo, long desired);

	/* Terminate source --- called by jpeg_finish_decompress() after all
	data has been read.  Often a no-op. */
	static void term_source (j_decompress_ptr cinfo);
	
	#endif // _IRR_COMPILE_WITH_LIBJPEG_
};


} // end namespace video
} // end namespace irr


#endif

