
#include "CImageWriterJPG.h"
#include "CColorConverter.h"
#include "IWriteFile.h"
#include "os.h" // for logging
#include <string.h>
#include <stdio.h>

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_LIBJPEG_
extern "C"
{
#ifndef _IRR_USE_NON_SYSTEM_JPEG_LIB_
	#include <jpeglib.h>
	#include <jerror.h>
#else
	#include "jpeglib/jpeglib.h"
	#include "jpeglib/jerror.h"
#endif
}
#endif // _IRR_COMPILE_WITH_LIBJPEG_


namespace irr
{
namespace video
{

IImageWriter* createImageWriterJPG()
{
	return new CImageWriterJPG;
}

CImageWriterJPG::CImageWriterJPG()
{
#ifdef _DEBUG
	setDebugName("CImageWriterJPG");
#endif
}

bool CImageWriterJPG::isAWriteableFileExtension(const c8* fileName)
{
	return strstr(fileName, ".jpg") != 0 || strstr(fileName, ".jpeg") != 0;
}

bool CImageWriterJPG::writeImage(io::IWriteFile *file, IImage *image)
{
#ifndef _IRR_COMPILE_WITH_LIBJPEG_
	return false;
#else
	os::Printer::log("JPG writer not yet implemented. Image not written.", ELL_WARNING);
	return false;
#endif
}

}; // namespace video
}; // namespace irr

