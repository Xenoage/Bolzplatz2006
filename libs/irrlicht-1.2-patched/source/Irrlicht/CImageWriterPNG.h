#ifndef _C_IMAGE_WRITER_PNG_H_INCLUDED__
#define _C_IMAGE_WRITER_PNG_H_INCLUDED__

#include "IImageWriter.h"

namespace irr
{
namespace video
{

class CImageWriterPNG : public IImageWriter
{
public:
	//! constructor
	CImageWriterPNG();

	//! return true if this writer can write a file with the given extension
	virtual bool isAWriteableFileExtension(const c8* fileName);

	//! write image to file
	virtual bool writeImage(io::IWriteFile *file, IImage *image);
private:
	u8** RowPointers; //Used to point to image rows
};

}; // namespace video
}; // namespace irr

#endif // _C_IMAGE_WRITER_PNG_H_INCLUDED__
