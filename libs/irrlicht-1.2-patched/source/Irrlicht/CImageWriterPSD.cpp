
#include "CImageWriterPSD.h"
#include "CImageLoaderPSD.h"
#include "IWriteFile.h"
#include "os.h" // for logging
#include <string.h>

namespace irr
{
namespace video
{

IImageWriter* createImageWriterPSD()
{
	return new CImageWriterPSD;
}

CImageWriterPSD::CImageWriterPSD()
{
#ifdef _DEBUG
	setDebugName("CImageWriterPSD");
#endif
}

bool CImageWriterPSD::isAWriteableFileExtension(const c8* fileName)
{
	return strstr(fileName, ".psd") != 0;
}

bool CImageWriterPSD::writeImage(io::IWriteFile *file, IImage *image)
{
	os::Printer::log("PSD writer not yet implemented. Image not written.", ELL_WARNING);
	return false;
}

}; // namespace video
}; // namespace irr
