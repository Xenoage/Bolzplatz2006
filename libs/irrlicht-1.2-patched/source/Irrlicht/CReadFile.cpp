// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CReadFile.h"
#include <stdio.h>

namespace irr
{
namespace io
{


CReadFile::CReadFile(const c8* fileName)
: FileSize(0)
{
	#ifdef _DEBUG
	setDebugName("CReadFile");
	#endif

	Filename = fileName;
	openFile();
}



CReadFile::~CReadFile()
{
	if (File)
		fclose(File);
}



//! returns if file is open
inline bool CReadFile::isOpen()
{
	return File != 0;
}



//! returns how much was read
s32 CReadFile::read(void* buffer, s32 sizeToRead)
{
	if (!isOpen())
		return 0;

	return fread(buffer, 1, sizeToRead, File);
}



//! changes position in file, returns true if successful
//! if relativeMovement==true, the pos is changed relative to current pos,
//! otherwise from begin of file
bool CReadFile::seek(s32 finalPos, bool relativeMovement)
{
	if (!isOpen())
		return false;

	return fseek(File, finalPos, relativeMovement ? SEEK_CUR : SEEK_SET) == 0;
}



//! returns size of file
s32 CReadFile::getSize()
{
	return FileSize;
}



//! returns where in the file we are.
s32 CReadFile::getPos()
{
	return ftell(File);
}



//! opens the file
void CReadFile::openFile()
{
	if (Filename.size() == 0) // bugfix posted by rt
	{
		File = 0;
		return; 
	}

	File = fopen(Filename.c_str(), "rb");

	if (File)
	{
		// get FileSize

		fseek(File, 0, SEEK_END);
		FileSize = ftell(File);
		fseek(File, 0, SEEK_SET);
	}
}



//! returns name of file
const c8* CReadFile::getFileName()
{
	return Filename.c_str();
}



IReadFile* createReadFile(const c8* fileName)
{
	CReadFile* file = new CReadFile(fileName);
	if (file->isOpen())
		return file;

	file->drop();
	return 0;
}


} // end namespace io
} // end namespace irr

