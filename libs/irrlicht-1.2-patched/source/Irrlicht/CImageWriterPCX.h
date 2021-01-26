#ifndef _C_IMAGE_WRITER_PCX_H_INCLUDED__
#define _C_IMAGE_WRITER_PCX_H_INCLUDED__

#include "IImageWriter.h"

namespace irr
{
namespace video
{

class CImageWriterPCX : public IImageWriter
{
public:
	//! constructor
	CImageWriterPCX();

	//! return true if this writer can write a file with the given extension
	virtual bool isAWriteableFileExtension(const c8* fileName);

	//! write image to file
	virtual bool writeImage(io::IWriteFile *file, IImage *image);
};

}; // namespace video
}; // namespace irr

#endif // _C_IMAGE_WRITER_PCX_H_INCLUDED__
