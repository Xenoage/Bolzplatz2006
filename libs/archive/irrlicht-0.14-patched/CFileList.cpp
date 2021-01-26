// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CFileList.h"
#include "IrrCompileConfig.h"

namespace irr
{
namespace io
{

#ifdef LINUX
#include <dirent.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <sys/types.h> 
#include <sys/stat.h> 
#include <unistd.h>
#endif

#ifdef _IRR_WINDOWS_
#include <stdlib.h>
#include <stdio.h>
#include <io.h>
#include <direct.h>
#endif


CFileList::CFileList()
{
	// --------------------------------------------
	// Windows version
	#ifdef _IRR_WINDOWS_

	char tmp[_MAX_PATH];
	_getcwd(tmp, _MAX_PATH);
	Path = tmp;

	struct _finddata_t c_file;
	s32 hFile;
	FileEntry entry;

	if( (hFile = _findfirst( "*", &c_file )) != -1L )
	{
		do
		{
			entry.Name = c_file.name;
			entry.Size = c_file.size;
			entry.isDirectory = (_A_SUBDIR & c_file.attrib) != 0;
			Files.push_back(entry);
		}
		while( _findnext( hFile, &c_file ) == 0 );

		_findclose( hFile );
	}

	//TODO add drives
	//entry.Name = "E:\\";
	//entry.isDirectory = true;
	//Files.push_back(entry);
	#endif
	
	// --------------------------------------------
	// Linux version
	#ifdef LINUX

	struct dirent **namelist; 
	struct stat	buf; 

	FileEntry entry; 

	// Add default parent - even when at /, this is available 
	entry.Name = ".."; 
	entry.Size = 0; 
	entry.isDirectory = true; 
	Files.push_back(entry);

	Path = getcwd(NULL,0); 

	s32	n =	scandir(".", &namelist,	0, alphasort); 

	if (n >= 0) 
	{ 
		while(n--) 
		{ 
			if((strcmp(namelist[n]->d_name,	".")==0) || 
			   (strcmp(namelist[n]->d_name, "..")==0)) 
			{ 
				// Only	add	entries	that aren't	.. or .	since they are already handled 
				free(namelist[n]); 
				continue; 
			} 

			entry.Name = namelist[n]->d_name; 

			if (stat(namelist[n]->d_name, &buf)==0) 
			{ 
				entry.Size = buf.st_size; 
				entry.isDirectory =	S_ISDIR(buf.st_mode); 
			} 
            else 
			{ 
				entry.Size = 0;	
				entry.isDirectory =	namelist[n]->d_type	== DT_DIR; 
			} 
			
			Files.push_back(entry);	
			free(namelist[n]); 
		} 

		free(namelist);	
	}
	#endif
}


CFileList::~CFileList()
{
}


s32 CFileList::getFileCount()
{
	return Files.size();
}


const c8* CFileList::getFileName(s32 index)
{
	if (index < 0 || index > (s32)Files.size())
		return 0;

	return Files[index].Name.c_str();
}


//! Gets the full name of a file in the list, path included, based on an index.
const c8* CFileList::getFullFileName(s32 index)
{
	if (index < 0 || index > (s32)Files.size())
		return 0;

	if (Files[index].FullName.size() < Files[index].Name.size())
	{
		// create full name
		Files[index].FullName = Path;

		if (Path.size() > 3)
			Files[index].FullName.append("/");

		Files[index].FullName.append(Files[index].Name);
	}

	return Files[index].FullName.c_str();
}


bool CFileList::isDirectory(s32 index)
{
	if (index < 0 || index > (s32)Files.size())
		return false;

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return Files[index].isDirectory;
}

} // end namespace irr
} // end namespace io

