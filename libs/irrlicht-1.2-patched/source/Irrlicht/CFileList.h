// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_FILE_LIST_H_INCLUDED__
#define __C_FILE_LIST_H_INCLUDED__

#include "IFileList.h"
#include "irrString.h"
#include "irrArray.h"

namespace irr
{
namespace io
{

/*!
	FileSystem, which manages where files are, so that modules which
	use the the io do not need to know where every file is located.
*/
class CFileList : public IFileList
{
public:

	//! constructor
	CFileList();

	//! destructor
	virtual ~CFileList();

	//! Returns the amount of files in the filelist.
	//! \return
	//! Returns the amount of files and directories in the file list.
	virtual s32 getFileCount();

	//! Gets the name of a file in the list, based on an index.
	//! \param index is the zero based index of the file which name should
	//!   be returned. The index has to be smaller than the amount getFileCount() returns.
	//! \return
	//! Returns the file name of the file. Returns 0, if an error occured.
	virtual const c8* getFileName(s32 index);

	//! Gets the full name of a file in the list, path included, based on an index.
	virtual const c8* getFullFileName(s32 index);

	//! Returns of the file is a directory
	//! \param index is the zero based index of the file which name should
	//!  be returned. The index has to be smaller than the amount getFileCount() returns.
	//! \return
	//! Returns true, if the file is a directory, and false, if it is not.
	//!  If an error occurs, the result is undefined.
	virtual bool isDirectory(s32 index);

private:

	struct FileEntry
	{
		core::stringc Name;
		core::stringc FullName;
		s32 Size;
		bool isDirectory;
		bool operator<(const struct FileEntry& other) const {return Name<other.Name;}
	};

	core::stringc Path;
	core::array< FileEntry > Files;
};

} // end namespace irr
} // end namespace io


#endif

