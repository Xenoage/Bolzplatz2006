// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_3DS_MESH_FILE_LOADER_H_INCLUDED__
#define __C_3DS_MESH_FILE_LOADER_H_INCLUDED__

#include "IMeshLoader.h"
#include "IFileSystem.h"
#include "IVideoDriver.h"
#include "irrString.h"
#include "SMesh.h"

namespace irr
{
namespace scene
{

//! Meshloader capable of loading 3ds meshes.
class C3DSMeshFileLoader : public IMeshLoader
{
public:

	//! Constructor
	C3DSMeshFileLoader(io::IFileSystem* fs, video::IVideoDriver* driver);

	//! destructor
	virtual ~C3DSMeshFileLoader();

	//! returns true if the file maybe is able to be loaded by this class
	//! based on the file extension (e.g. ".cob")
	virtual bool isALoadableFileExtension(const c8* fileName);

	//! creates/loads an animated mesh from the file.
	//! \return Pointer to the created mesh. Returns 0 if loading failed.
	//! If you no longer need the mesh, you should call IAnimatedMesh::drop().
	//! See IUnknown::drop() for more information.
	virtual IAnimatedMesh* createMesh(irr::io::IReadFile* file);

private:

	// byte-align structures
	#ifdef _MSC_VER
	#	pragma pack( push, packing )
	#	pragma pack( 1 )
	#	define PACK_STRUCT
	#elif defined( __GNUC__ )
	#	define PACK_STRUCT	__attribute__((packed))
	#else
	#	error compiler not supported
	#endif

	struct ChunkHeader
	{
		u16 id;
		s32 length;
	} PACK_STRUCT;

	// Default alignment
	#ifdef _MSC_VER
	#	pragma pack( pop, packing )
	#endif

	#undef PACK_STRUCT


	struct ChunkData
	{
		ChunkData() : read(0) {}

		ChunkHeader header;
		s32 read;
	};

	struct SCurrentMaterial
	{
		SCurrentMaterial() {};
		SCurrentMaterial(SCurrentMaterial& o) : Material(o.Material) {};

		~SCurrentMaterial() { 	};

		video::SMaterial Material;
		core::stringc Name;
		core::stringc Filename;
	};

	struct SMaterialGroup
	{
		SMaterialGroup() : faceCount(0), faces(0) {};

		SMaterialGroup(SMaterialGroup& o)
		{
			*this = o;
		}

		~SMaterialGroup() 
		{ 
			clear();
		}

		void clear() 
		{ 
			if (faces) delete [] faces;
			faces = 0;
			faceCount = 0;
		}

		void operator =(const SMaterialGroup& o)
		{
			MaterialName = o.MaterialName;
			faceCount = o.faceCount;
			faces = new s16[faceCount];
			for (s32 i=0; i<faceCount; ++i)
				faces[i] = o.faces[i];
		}

		core::stringc MaterialName;
		u16 faceCount;
		s16* faces;
	};

	bool readChunk(io::IReadFile* file, ChunkData* parent);
	bool readMaterialChunk(io::IReadFile* file, ChunkData* parent);
	bool readObjectChunk(io::IReadFile* file, ChunkData* parent);
	bool readColorChunk(io::IReadFile* file, ChunkData* chunk, video::SColor& out);

	void readChunkData(io::IReadFile* file, ChunkData& data);
	void readAndIgnoreString(io::IReadFile* file, ChunkData& data);
	void readString(io::IReadFile* file, ChunkData& data, core::stringc& out);
	void readVertices(io::IReadFile* file, ChunkData& data);
	void readIndices(io::IReadFile* file, ChunkData& data);
	void readMaterialGroup(io::IReadFile* file, ChunkData& data);
	void readTextureCoords(io::IReadFile* file, ChunkData& data);

	void composeObject(io::IReadFile* file);
	void loadMaterials(io::IReadFile* file);
	void cleanUp();
	core::stringc getTextureFileName(core::stringc texture, core::stringc model);

	io::IFileSystem* FileSystem;
	video::IVideoDriver* Driver;

	f32* Vertices;
	u16 CountVertices;
	u16* Indices;
	u16 CountIndices;
	u16 CountTCoords;
	f32* TCoords;
	core::array<SMaterialGroup> MaterialGroups;

	SCurrentMaterial CurrentMaterial;
	core::array<SCurrentMaterial> Materials;

	SMesh* Mesh;
};

} // end namespace scene
} // end namespace irr

#endif

