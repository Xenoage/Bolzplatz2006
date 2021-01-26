
#include "CImageWriterPCX.h"
#include "CImageLoaderPCX.h"
#include "IWriteFile.h"
#include "os.h" // for logging
#include <string.h>

namespace irr
{
namespace video
{

IImageWriter* createImageWriterPCX()
{
	return new CImageWriterPCX;
}

CImageWriterPCX::CImageWriterPCX()
{
#ifdef _DEBUG
	setDebugName("CImageWriterPCX");
#endif
}

bool CImageWriterPCX::isAWriteableFileExtension(const c8* fileName)
{
	return strstr(fileName, ".pcx") != 0;
}

bool CImageWriterPCX::writeImage(io::IWriteFile *file, IImage *image)
{
	os::Printer::log("PCX writer not yet implemented. Image not written.", ELL_WARNING);
	return false;
}

}; // namespace video
}; // namespace irr
