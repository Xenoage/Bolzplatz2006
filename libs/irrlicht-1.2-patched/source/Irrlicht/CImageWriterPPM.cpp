
#include "CImageWriterPPM.h"
#include "IWriteFile.h"
#include "IImage.h"
#include "dimension2d.h"
#include <string.h>
#include <stdio.h>

#include "IrrCompileConfig.h"
// this should really go into the compile config header.
// there is a similar macro in irrlicht.h, but i feel that
// macro should be moved to compile config also.
#ifdef _IRR_WINDOWS_
#  define snprintf _snprintf
#endif

namespace irr
{
namespace video
{

IImageWriter* createImageWriterPPM()
{
	return new CImageWriterPPM;
}

CImageWriterPPM::CImageWriterPPM()
{
#ifdef _DEBUG
	setDebugName("CImageWriterPPM");
#endif
}

bool CImageWriterPPM::isAWriteableFileExtension(const c8* fileName)
{
	return strstr(fileName, ".ppm") != 0;
}

bool CImageWriterPPM::writeImage(io::IWriteFile *file, IImage *image)
{
	char cache[70];
	char size;

	const core::dimension2d<s32>& imageSize = image->getDimension();

	size = snprintf(cache, 70, "P3\n");
	if (file->write(cache, size) != size)
		return false;

	size = snprintf(cache, 70, "%d %d\n", imageSize.Width, imageSize.Height);
	if (file->write(cache, size) != size)
		return false;

	size = snprintf(cache, 70, "255\n");
	if (file->write(cache, size) != size)
		return false;

	s32 n = 0;

	s32 r;
	for (r = 0; r < imageSize.Height; ++r)
	{
		s32 c;
		for (c = 0; c < imageSize.Width; ++c, ++n)
		{
			const video::SColor& pixel = image->getPixel(c, r);
			size = snprintf(cache, 70, "%.3d %.3d %.3d%s", pixel.getRed(), pixel.getGreen(), pixel.getBlue(), n % 5 == 4 ? "\n" : "  ");	
			if (file->write(cache, size) != size)
				return false;
		}
	}

	return true;
}

}; // namespace video
}; // namespace irr
