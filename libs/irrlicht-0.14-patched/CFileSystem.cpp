// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CFileSystem.h"
#include "IReadFile.h"
#include "IWriteFile.h"
#include "CZipReader.h"
#include "CFileList.h"
#include "CXMLReader.h"
#include "CXMLWriter.h"
#include "stdio.h"
#include "os.h"
#include "IrrCompileConfig.h"

#ifdef _IRR_WINDOWS_
#include <direct.h> // for _chdir
#endif

#ifdef LINUX
#include <unistd.h>
#endif

namespace irr
{
namespace io
{


//! constructor
CFileSystem::CFileSystem()
{
	#ifdef _DEBUG
	setDebugName("CFileSystem");
	#endif
}



//! destructor
CFileSystem::~CFileSystem()
{
	for (u32 i=0; i<ZipFileSystems.size(); ++i)
		ZipFileSystems[i]->drop();
}



//! opens a file for read access
IReadFile* CFileSystem::createAndOpenFile(const c8* filename)
{
	IReadFile* file = 0;

	for (u32 i=0; i<ZipFileSystems.size(); ++i)
	{
		file = ZipFileSystems[i]->openFile(filename);
		if (file)
			return file;
	}

	file = createReadFile(filename);
	return file;
}


//! Opens a file for write access.
IWriteFile* CFileSystem::createAndWriteFile(const c8* filename, bool append)
{
	return createWriteFile(filename, append);
}



//! adds an zip archive to the filesystem
bool CFileSystem::addZipFileArchive(const c8* filename, bool ignoreCase, bool ignorePaths)
{
	IReadFile* file = createReadFile(filename);
	if (file)
	{
		CZipReader* zr = new CZipReader(file, ignoreCase, ignorePaths);
		if (zr)
			ZipFileSystems.push_back(zr);

		file->drop();

		bool ret = (zr != 0);
		_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
		return ret;
	}

	#ifdef _DEBUG
	os::Printer::log("Could not open file. Zipfile not added", filename, ELL_ERROR);
	#endif

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return false;
}



//! Returns the string of the current working directory
const c8* CFileSystem::getWorkingDirectory()
{
#ifdef _IRR_WINDOWS_
	_getcwd(WorkingDirectory, FILE_SYSTEM_MAX_PATH);
	return WorkingDirectory;
#endif

#ifdef LINUX
	getcwd(WorkingDirectory, (size_t)FILE_SYSTEM_MAX_PATH);
	return WorkingDirectory;
#endif

	return 0;
}



//! Changes the current Working Directory to the string given.
//! The string is operating system dependent. Under Windows it will look
//! like this: "drive:\directory\sudirectory\"
//! \return
//! Returns true if successful, otherwise false.
bool CFileSystem::changeWorkingDirectoryTo(const c8* newDirectory)
{
#ifdef _IRR_WINDOWS_
	return (_chdir(newDirectory) == 0);
#endif

#ifdef LINUX
	return !(chdir(newDirectory) == 0);
#endif

	return false;
}


//! Creates a list of files and directories in the current working directory 
IFileList* CFileSystem::createFileList()
{
	return new CFileList();
}


//! determinates if a file exists and would be able to be opened.
bool CFileSystem::existFile(const c8* filename)
{
	for (u32 i=0; i<ZipFileSystems.size(); ++i)
		if (ZipFileSystems[i]->findFile(filename)!=-1)
			return true;

	FILE* f = fopen(filename, "rb");
	if (f) 
	{
		fclose(f);
		return true;
	}

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return false;
}


//! Creates a XML Reader from a file.
IXMLReader* CFileSystem::createXMLReader(const c8* filename)
{
	IReadFile* file = createAndOpenFile(filename);
	if (!file)
		return 0;

	IXMLReader* reader = createXMLReader(file);
	file->drop();
	return reader;
}


//! Creates a XML Reader from a file.
IXMLReader* CFileSystem::createXMLReader(IReadFile* file)
{
	if (!file)
		return 0;

	return createIXMLReader(file);
}

//! Creates a XML Reader from a file.
IXMLReaderUTF8* CFileSystem::createXMLReaderUTF8(const c8* filename)
{
	IReadFile* file = createAndOpenFile(filename);
	if (!file)
		return 0;

	IXMLReaderUTF8* reader = createIXMLReaderUTF8(file);
	file->drop();
	return reader;
}

//! Creates a XML Reader from a file.
IXMLReaderUTF8* CFileSystem::createXMLReaderUTF8(IReadFile* file)
{
	if (!file)
		return 0;

	return createIXMLReaderUTF8(file);
}


//! Creates a XML Writer from a file.
IXMLWriter* CFileSystem::createXMLWriter(const c8* filename)
{
	IWriteFile* file = createAndWriteFile(filename);
	IXMLWriter* writer = createXMLWriter(file);
	file->drop();
	return writer;
}


//! Creates a XML Writer from a file.
IXMLWriter* CFileSystem::createXMLWriter(IWriteFile* file)
{
	return new CXMLWriter(file);
}


//! creates a filesystem which is able to open files from the ordinary file system,
//! and out of zipfiles, which are able to be added to the filesystem.
IFileSystem* createFileSystem()
{
	return new CFileSystem();
}


} // end namespace irr
} // end namespace io

