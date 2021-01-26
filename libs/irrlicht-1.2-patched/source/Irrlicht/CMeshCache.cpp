// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CMeshCache.h"
#include "IAnimatedMesh.h"
#include "IMesh.h"

namespace irr
{
namespace scene
{


CMeshCache::~CMeshCache()
{
	for (u32 i=0; i<Meshes.size(); ++i)
		Meshes[i].Mesh->drop();
}


//! adds a mesh to the list
void CMeshCache::addMesh(const c8* filename, IAnimatedMesh* mesh)
{
	mesh->grab();

	MeshEntry e;
	e.Mesh = mesh;
	e.Name = filename;
	e.Name.make_lower();

	Meshes.push_back(e);
}


//! Returns amount of loaded meshes
s32 CMeshCache::getMeshCount()
{
	return Meshes.size();
}


//! Returns a mesh based on its index number
IAnimatedMesh* CMeshCache::getMeshByIndex(s32 number)
{
	if (number < 0 || number >= (s32)Meshes.size())
		return 0;

	return Meshes[number].Mesh;
}



//! Returns current number of the mesh 
s32 CMeshCache::getMeshIndex(IAnimatedMesh* mesh)
{
	for (int i=0; i<(int)Meshes.size(); ++i)
		if (Meshes[i].Mesh == mesh)
			return i;

	return -1;
}



//! Returns name of a mesh based on its index number
const c8* CMeshCache::getMeshFilename(s32 number)
{
	if (number < 0 || number >= (s32)Meshes.size())
		return 0;

	return Meshes[number].Name.c_str();
}



//! Returns the filename of a loaded mesh, if there is any. Returns 0 if there is none.
const c8* CMeshCache::getMeshFilename(IAnimatedMesh* mesh)
{
	for (s32 i=0; i<(s32)Meshes.size(); ++i)
	{
		if (Meshes[i].Mesh == mesh)
			return Meshes[i].Name.c_str();
	}

	return 0;
}


//! Returns the filename of a loaded mesh, if there is any. Returns 0 if there is none.
const c8* CMeshCache::getMeshFilename(IMesh* mesh)
{
	for (s32 i=0; i<(s32)Meshes.size(); ++i)
	{
		if (Meshes[i].Mesh && Meshes[i].Mesh->getMesh(0) == mesh)
			return Meshes[i].Name.c_str();
	}

	return 0;
}



//! returns if a mesh already was loaded
bool CMeshCache::isMeshLoaded(const c8* filename)
{
	core::stringc name = filename;
	name.make_lower();
	return findMesh(name.c_str()) != 0;
}


//! returns an already loaded mesh
IAnimatedMesh* CMeshCache::findMesh(const c8* lowerMadeFilename)
{
	MeshEntry e;
	e.Name = lowerMadeFilename;
	s32 id = Meshes.binary_search(e);
	return (id != -1) ? Meshes[id].Mesh : 0;
}


//! Removes a mesh from the cache.
void CMeshCache::removeMesh(IAnimatedMesh* mesh)
{
	if ( mesh )
	for (int i=0; i<(int)Meshes.size(); ++i)
	{
		if (Meshes[i].Mesh == mesh)
		{
			Meshes[i].Mesh->drop();
			Meshes.erase(i);
			return;
		}
	}
}

} // end namespace scene
} // end namespace irr


