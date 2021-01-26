// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CDefaultMeshFormatLoader.h"
#include "CAnimatedMeshMD2.h"
#include "CStaticMeshOBJ.h"
#include "CAnimatedMeshMS3D.h"
#include "CQ3LevelMesh.h"
#include <string.h>

namespace irr
{
namespace scene
{

//! Constructor
CDefaultMeshFormatLoader::CDefaultMeshFormatLoader(io::IFileSystem* fs, video::IVideoDriver* driver)
: FileSystem(fs), Driver(driver)
{
	if (FileSystem)
		FileSystem->grab();

	if (Driver)
		Driver->grab();
}



//! destructor
CDefaultMeshFormatLoader::~CDefaultMeshFormatLoader()
{
	if (FileSystem)
		FileSystem->drop();

	if (Driver)
		Driver->drop();
}



//! returns true if the file maybe is able to be loaded by this class
//! based on the file extension (e.g. ".bsp")
bool CDefaultMeshFormatLoader::isALoadableFileExtension(const c8* filename)
{
	return (strstr(filename, ".md2") || strstr(filename, ".obj") || 
			strstr(filename, ".ms3d") || strstr(filename, ".bsp"));
}



//! creates/loads an animated mesh from the file.
//! \return Pointer to the created mesh. Returns 0 if loading failed.
//! If you no longer need the mesh, you should call IAnimatedMesh::drop().
//! See IUnknown::drop() for more information.
IAnimatedMesh* CDefaultMeshFormatLoader::createMesh(irr::io::IReadFile* file)
{
	IAnimatedMesh* msh = 0;

	// This method loads a mesh if it cans.
	// Someday I will have to refactor this, and split the DefaultMeshFormatloader
	// into one loader for every format.

	bool success = false;

	// load quake 2 md2 model
	if (strstr(file->getFileName(), ".md2"))
	{
		file->seek(0);
	
		msh = new CAnimatedMeshMD2();
		success = ((CAnimatedMeshMD2*)msh)->loadFile(file);
		if (success)
			return msh;

		msh->drop();
	}

	// load maya obj
	if (strstr(file->getFileName(), ".obj"))
	{
		file->seek(0);

		msh = new CStaticMeshOBJ();
		success = ((CStaticMeshOBJ*)msh)->loadFile(file);
		if (success)
			return msh;

		msh->drop();
	}

	// load milkshape
	if (strstr(file->getFileName(), ".ms3d"))
	{
		file->seek(0);

		msh = new CAnimatedMeshMS3D(Driver);
		success = ((CAnimatedMeshMS3D*)msh)->loadFile(file);
		if (success)
			return msh;

		msh->drop();
	}

	// load quake 3 bsp
	if (strstr(file->getFileName(), ".bsp"))
	{
		file->seek(0);

		msh = new CQ3LevelMesh(FileSystem, Driver);
		success = ((CQ3LevelMesh*)msh)->loadFile(file);
		if (success)
			return msh;

		msh->drop();
	}

	return 0;
}

} // end namespace scene
} // end namespace irr

