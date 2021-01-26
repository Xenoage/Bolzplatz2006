// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CQ3LevelMesh.h"
#include "ISceneManager.h"
#include "os.h"
#include "SMeshBufferLightMap.h"
#include "irrString.h"
#include "CColorConverter.h"
#include "ILightSceneNode.h"
#include "fast_atof.h"

#include <stdio.h>

namespace irr
{
namespace scene
{


//! constructor
CQ3LevelMesh::CQ3LevelMesh(io::IFileSystem* fs, video::IVideoDriver* driver, scene::ISceneManager* smgr)
: Textures(0), LightMaps(0),
 Vertices(0), Faces(0),	Planes(0), Nodes(0), Leafs(0), LeafFaces(0),
	MeshVerts(0), Brushes(0), Driver(driver), FileSystem(fs), SceneManager ( smgr )
{
	#ifdef _DEBUG
	IUnknown::setDebugName("CQ3LevelMesh");
	#endif	

	Mesh = new SMesh();

	if (Driver)
		Driver->grab();

	if (FileSystem)
		FileSystem->grab();

}


//! destructor
CQ3LevelMesh::~CQ3LevelMesh()
{
	if (Textures)
		delete [] Textures;

	if (LightMaps)
		delete [] LightMaps;
	
	if (Vertices)
		delete [] Vertices;

	if (Faces)
		delete [] Faces;

	if (Planes)
		delete [] Planes;

	if (Nodes)
		delete [] Nodes;

	if (Leafs)
		delete [] Leafs;

	if (LeafFaces)
		delete [] LeafFaces;
	
	if (MeshVerts)
		delete [] MeshVerts;

	if (Brushes)
		delete [] Brushes;

	if (Driver)
		Driver->drop();

	if (FileSystem)
		FileSystem->drop();

	if (Mesh)
		Mesh->drop();
}



//! loads a level from a .bsp-File. Also tries to load all needed textures. Returns true if successful.
bool CQ3LevelMesh::loadFile(io::IReadFile* file)
{
	if (!file)
		return false;

	LevelName = file->getFileName();

	tBSPHeader header;
	file->read(&header, sizeof(tBSPHeader));

	#ifdef __BIG_ENDIAN__
		header.strID = os::Byteswap::byteswap(header.strID);
		header.version = os::Byteswap::byteswap(header.version);
	#endif

	if (header.strID != 0x50534249 || header.version != 0x2e)
	{
		os::Printer::log("Could not load .bsp file, unknown header.", file->getFileName(), ELL_ERROR);
		return false;
	}

	// now read lumps

	file->read(&Lumps[0], sizeof(tBSPLump)*kMaxLumps);

	#ifdef __BIG_ENDIAN__
	for (int i=0;i<kMaxLumps;++i)
	{
		Lumps[i].offset = os::Byteswap::byteswap(Lumps[i].offset);
		Lumps[i].length = os::Byteswap::byteswap(Lumps[i].length);
	}
	#endif

	// load everything

	loadTextures(&Lumps[kTextures], file);		// Load the textures
	loadLightmaps(&Lumps[kLightmaps], file);	// Load the lightmaps
	loadVerts(&Lumps[kVertices], file);		// Load the vertices
	loadFaces(&Lumps[kFaces], file);		// Load the faces
	loadPlanes(&Lumps[kPlanes], file);		// Load the Planes of the BSP
	loadNodes(&Lumps[kNodes], file);		// load the Nodes of the BSP
	loadLeafs(&Lumps[kLeafs], file);		// load the Leafs of the BSP
	loadLeafFaces(&Lumps[kLeafFaces], file);	// load the Faces of the Leafs of the BSP
	loadVisData(&Lumps[kVisData], file);		// load the visibility data of the clusters
	loadEntities(&Lumps[kEntities], file);		// load the entities
	loadModels(&Lumps[kModels], file);		// load the models
	loadMeshVerts(&Lumps[kMeshVerts], file);	// load the mesh vertices
	loadBrushes(&Lumps[kBrushes], file);		// load the brushes of the BSP
	loadBrushSides(&Lumps[kBrushSides], file);	// load the brushsides of the BSP
	loadLeafBrushes(&Lumps[kLeafBrushes], file);	// load the brushes of the leaf*/

	constructMesh();

	loadTextures();

	return true;
}



//! returns the amount of frames in milliseconds. If the amount is 1, it is a static (=non animated) mesh.
s32 CQ3LevelMesh::getFrameCount()
{
	return 1;
}



//! returns the animated mesh based on a detail level. 0 is the lowest, 255 the highest detail. Note, that some Meshes will ignore the detail level.
IMesh* CQ3LevelMesh::getMesh(s32 frameInMs, s32 detailLevel, s32 startFrameLoop, s32 endFrameLoop)
{
	// Dirty Hack
	if ( frameInMs == 1 )
		return (IMesh*) ((void*)&LightData);

	return Mesh;
}


void CQ3LevelMesh::loadTextures(tBSPLump* l, io::IReadFile* file)
{
	NumTextures = l->length / sizeof(tBSPTexture);
	Textures = new tBSPTexture[NumTextures];

	file->seek(l->offset);
	file->read(Textures, l->length);

	#ifdef __BIG_ENDIAN__
	for (int i=0;i<NumTextures;i++)
	{
		Textures[i].flags = os::Byteswap::byteswap(Textures[i].flags);
		Textures[i].contents = os::Byteswap::byteswap(Textures[i].contents);
	}
	#endif
}


void CQ3LevelMesh::loadLightmaps(tBSPLump* l, io::IReadFile* file)
{
	NumLightMaps = l->length / sizeof(tBSPLightmap);
	LightMaps = new tBSPLightmap[NumLightMaps];

	file->seek(l->offset);
	file->read(LightMaps, l->length);
}


void CQ3LevelMesh::loadVerts(tBSPLump* l, io::IReadFile* file)
{
	NumVertices = l->length / sizeof(tBSPVertex);
	Vertices = new tBSPVertex[NumVertices];

	file->seek(l->offset);
	file->read(Vertices, l->length);

	#ifdef __BIG_ENDIAN__
	for (int i=0;i<NumVertices;i++)
	{
		Vertices[i].vPosition[0] = os::Byteswap::byteswap(Vertices[i].vPosition[0]);
		Vertices[i].vPosition[1] = os::Byteswap::byteswap(Vertices[i].vPosition[1]);
		Vertices[i].vPosition[2] = os::Byteswap::byteswap(Vertices[i].vPosition[2]);
		Vertices[i].vTextureCoord[0] = os::Byteswap::byteswap(Vertices[i].vTextureCoord[0]);
		Vertices[i].vTextureCoord[1] = os::Byteswap::byteswap(Vertices[i].vTextureCoord[1]);
		Vertices[i].vLightmapCoord[0] = os::Byteswap::byteswap(Vertices[i].vLightmapCoord[0]);
		Vertices[i].vLightmapCoord[1] = os::Byteswap::byteswap(Vertices[i].vLightmapCoord[1]);
		Vertices[i].vNormal[0] = os::Byteswap::byteswap(Vertices[i].vNormal[0]);
		Vertices[i].vNormal[1] = os::Byteswap::byteswap(Vertices[i].vNormal[1]);
		Vertices[i].vNormal[2] = os::Byteswap::byteswap(Vertices[i].vNormal[2]);
	}
	#endif
}


void CQ3LevelMesh::loadFaces(tBSPLump* l, io::IReadFile* file)
{
	NumFaces = l->length / sizeof(tBSPFace);
	Faces = new tBSPFace[NumFaces];

	file->seek(l->offset);
	file->read(Faces, l->length);

	#ifdef __BIG_ENDIAN__
	for (int i=0;i<NumFaces;i++)
	{
		Faces[i].textureID = os::Byteswap::byteswap(Faces[i].textureID);
		Faces[i].effect = os::Byteswap::byteswap(Faces[i].effect);
		Faces[i].type = os::Byteswap::byteswap(Faces[i].type);
		Faces[i].vertexIndex = os::Byteswap::byteswap(Faces[i].vertexIndex);
		Faces[i].numOfVerts = os::Byteswap::byteswap(Faces[i].numOfVerts);
		Faces[i].meshVertIndex = os::Byteswap::byteswap(Faces[i].meshVertIndex);
		Faces[i].numMeshVerts = os::Byteswap::byteswap(Faces[i].numMeshVerts);
		Faces[i].lightmapID = os::Byteswap::byteswap(Faces[i].lightmapID);
		Faces[i].lMapCorner[0] = os::Byteswap::byteswap(Faces[i].lMapCorner[0]);
		Faces[i].lMapCorner[1] = os::Byteswap::byteswap(Faces[i].lMapCorner[1]);
		Faces[i].lMapSize[0] = os::Byteswap::byteswap(Faces[i].lMapSize[0]);
		Faces[i].lMapSize[1] = os::Byteswap::byteswap(Faces[i].lMapSize[1]);
		Faces[i].lMapPos[0] = os::Byteswap::byteswap(Faces[i].lMapPos[0]);
		Faces[i].lMapPos[1] = os::Byteswap::byteswap(Faces[i].lMapPos[1]);
		Faces[i].lMapPos[2] = os::Byteswap::byteswap(Faces[i].lMapPos[2]);
		Faces[i].lMapBitsets[0][0] = os::Byteswap::byteswap(Faces[i].lMapBitsets[0][0]);
		Faces[i].lMapBitsets[0][1] = os::Byteswap::byteswap(Faces[i].lMapBitsets[0][1]);
		Faces[i].lMapBitsets[0][2] = os::Byteswap::byteswap(Faces[i].lMapBitsets[0][2]);
		Faces[i].lMapBitsets[1][0] = os::Byteswap::byteswap(Faces[i].lMapBitsets[1][0]);
		Faces[i].lMapBitsets[1][1] = os::Byteswap::byteswap(Faces[i].lMapBitsets[1][1]);
		Faces[i].lMapBitsets[1][2] = os::Byteswap::byteswap(Faces[i].lMapBitsets[1][2]);
		Faces[i].vNormal[0] = os::Byteswap::byteswap(Faces[i].vNormal[0]);
		Faces[i].vNormal[1] = os::Byteswap::byteswap(Faces[i].vNormal[1]);
		Faces[i].vNormal[2] = os::Byteswap::byteswap(Faces[i].vNormal[2]);
		Faces[i].size[0] = os::Byteswap::byteswap(Faces[i].size[0]);
		Faces[i].size[1] = os::Byteswap::byteswap(Faces[i].size[1]);
	}
	#endif
}


void CQ3LevelMesh::loadPlanes(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadNodes(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadLeafs(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadLeafFaces(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadVisData(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadEntities(tBSPLump* l, io::IReadFile* file)
{
	core::array<u8> entity;
	entity.set_used ( l->length + 2 );
	entity[l->length + 1 ] = 0;

	file->seek(l->offset);
	file->read ( entity.pointer(), l->length);

	parser_parse ( entity.pointer(), l->length );
	// ignore
}


void CQ3LevelMesh::parser_nextToken ()
{
	c8 symbol;

	Parser.token = "";

	// skip white space
	do
	{
		if ( Parser.index >= Parser.sourcesize )
		{
			Parser.tokenresult = Q3_TOKEN_EOF;
			return;
		}

		symbol = Parser.source [ Parser.index ];
		Parser.index += 1;
	} while ( symbol == ' ' || symbol == '\t' || symbol == '\n' );

	// one byte 
	switch ( symbol )
	{
		case '{':
			Parser.tokenresult = Q3_TOKEN_START_LIST;
			break;
		case '}':
			Parser.tokenresult = Q3_TOKEN_END_LIST;
			break;
		case '"':
		{
			do
			{
				if ( Parser.index >= Parser.sourcesize )
				{
					Parser.tokenresult = Q3_TOKEN_EOF;
					return;
				}
				symbol = Parser.source [ Parser.index ];
				Parser.index += 1;
				if ( symbol != '"' )
					Parser.token.append ( symbol );
			} while ( symbol != '"' );
			Parser.tokenresult = Q3_TOKEN_IDENTITY;
		} break;
	}

}


void CQ3LevelMesh::parser_parse ( const void * data, const u32 size )
{
	Parser.source = (const c8*) data;
	Parser.sourcesize = size;
	Parser.index = 0;

	SQ3VarGroup group;
	SQ3Variable entity;

	do
	{
		parser_nextToken ();
		switch ( Parser.tokenresult )
		{
			case Q3_TOKEN_START_LIST:
			{
				group.clear ();
			}  break;

			case Q3_TOKEN_IDENTITY:
			{
				strcpy ( entity.name, Parser.token.c_str () );
				parser_nextToken ();
				strcpy ( entity.content, Parser.token.c_str () );
				group.Variable.push_back ( entity );
			}  break;

			case Q3_TOKEN_END_LIST:
			{
				const c8 *classname = group.get ( "classname" );

				if ( 0 == strcmp ( classname, "light" ) )
				{
					addLightData ( group );
				}

			} break;

		}
	} while ( Parser.tokenresult != Q3_TOKEN_EOF );
}

// simple assozitave array
const c8 * CQ3LevelMesh::SQ3VarGroup::get( const char * name ) const
{
	SQ3Variable search;
	strcpy ( search.name, name );

	s32 index = Variable.linear_search ( search );
	if ( index < 0 )
		return 0;

	return Variable [index].content;
}



const c8 * CQ3LevelMesh::parser_get_float ( f32 &out, const c8* in ) const
{
	if ( 0 == in )
		return 0;
	return core::fast_atof_move ( in, out );
}

void CQ3LevelMesh::parser_get_vector ( core::vector3df &vector, const c8* string ) const
{
	const c8 * in = string;

	in = parser_get_float ( vector.X, in );
	in = parser_get_float ( vector.Y, in );
	in = parser_get_float ( vector.Z, in );
}


// extract light info
void CQ3LevelMesh::addLightData ( const SQ3VarGroup &group )
{
	video::SLight light;
	light.Type = video::ELT_POINT;
	light.Radius = 200;

	parser_get_vector ( light.Position, group.get ( "origin" ) );
	parser_get_float ( light.Radius, group.get ( "light" ) );
	LightData.push_back ( light );
}


void CQ3LevelMesh::loadModels(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadMeshVerts(tBSPLump* l, io::IReadFile* file)
{
	NumMeshVerts = l->length / sizeof(s32);
	MeshVerts = new s32[NumMeshVerts];

	file->seek(l->offset);
	file->read(MeshVerts, l->length);

	#ifdef __BIG_ENDIAN__
	for (int i=0;i<NumMeshVerts;i++)
		MeshVerts[i] = os::Byteswap::byteswap(MeshVerts[i]);
	#endif
}


void CQ3LevelMesh::loadBrushes(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadBrushSides(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


void CQ3LevelMesh::loadLeafBrushes(tBSPLump* l, io::IReadFile* file)
{
	// ignore
}


//! constructs a mesh from the quake 3 level file.
void CQ3LevelMesh::constructMesh()
{
	// reserve buffer. 
	s32 i; // new ISO for scoping problem with some compilers

	for (i=0; i<(NumTextures+1) * (NumLightMaps+1); ++i)
	{
		scene::SMeshBufferLightMap* buffer = new scene::SMeshBufferLightMap();

		buffer->Material.MaterialType = video::EMT_LIGHTMAP_M4;
		buffer->Material.Wireframe = false;
		buffer->Material.Lighting = false;
		buffer->Material.BilinearFilter = true;

		Mesh->addMeshBuffer(buffer);

		buffer->drop();
	}

	// go through all faces and add them to the buffer.

	for (i=0; i<NumFaces; ++i)
	{
		if (Faces[i].lightmapID < -1)
			Faces[i].lightmapID = -1;

		if (Faces[i].lightmapID > NumLightMaps-1)
			Faces[i].lightmapID = -1;

		// there are lightmapsids and textureid with -1
		s32 meshBufferIndex = ((Faces[i].lightmapID+1) * (NumTextures+1)) + (Faces[i].textureID+1);
		SMeshBufferLightMap* meshBuffer = ((SMeshBufferLightMap*)Mesh->getMeshBuffer(meshBufferIndex));

		switch(Faces[i].type)
		{
			//case 3: // mesh vertices
			case 1: // normal polygons
				{
					for (s32 tf=0; tf<Faces[i].numMeshVerts; tf+=3)
					{
						s32 idx = meshBuffer->getVertexCount();
						s32 vidxes[3];

						vidxes[0] = MeshVerts[Faces[i].meshVertIndex + tf +0] 
							+ Faces[i].vertexIndex;
						vidxes[1] = MeshVerts[Faces[i].meshVertIndex + tf +1] 
							+ Faces[i].vertexIndex;
						vidxes[2] = MeshVerts[Faces[i].meshVertIndex + tf +2]
							+ Faces[i].vertexIndex;

						// add all three vertices

						for (s32 vu=0; vu<3; ++vu)
						{
							video::S3DVertex2TCoords currentVertex;
							tBSPVertex *v = &Vertices[vidxes[vu]];

							currentVertex.Color = video::SColor(v->color[3], v->color[0], v->color[1], v->color[2]);
							
							// round the position values or the mesh has small gaps
							currentVertex.Pos.X = floorf(v->vPosition[0]+0.5f);
							currentVertex.Pos.Y = floorf(v->vPosition[2]+0.5f);
							currentVertex.Pos.Z =  floorf(v->vPosition[1]+0.5f);
							currentVertex.Normal.X = v->vNormal[0];
							currentVertex.Normal.Y = v->vNormal[2];
							currentVertex.Normal.Z = v->vNormal[1];
							currentVertex.TCoords.X = v->vTextureCoord[0];
							currentVertex.TCoords.Y = v->vTextureCoord[1];
							currentVertex.TCoords2.X = v->vLightmapCoord[0];
							currentVertex.TCoords2.Y = v->vLightmapCoord[1];

							meshBuffer->Vertices.push_back(currentVertex);
						}

						// add indexes

						meshBuffer->Indices.push_back(idx);
						meshBuffer->Indices.push_back(idx+1);
						meshBuffer->Indices.push_back(idx+2);
					}
				}
				break;
			case 2: // curved surfaces
				createCurvedSurface(meshBuffer, i);
				break;
			case 4: // billboards
				break;
			default:
				break;
		} // end switch
	}
	

	// create bounding box

	for (u32 j=0; j<Mesh->MeshBuffers.size(); ++j)
		((SMeshBufferLightMap*)Mesh->MeshBuffers[j])->recalculateBoundingBox();

	Mesh->recalculateBoundingBox();
}

// helper method for creating curved surfaces, sent in by Dean P. Macri.
inline f32 CQ3LevelMesh::Blend( f32 s[3], f32 t[3], tBSPVertex *v[9], int offset)
{
	f32 res = 0.0f;
	f32 *ptr;

	for( int i=0; i<3; i++ )
		for( int j=0; j<3; j++ )
		{
			ptr = (f32 *)( (char*)v[i*3+j] + offset );
			res += s[i] * t[j] *  (*ptr);
		}
	return res;
}


void CQ3LevelMesh::createCurvedSurface(SMeshBufferLightMap* meshBuffer, s32 i)
{
	// this implementation for loading curved surfaces was
	// sent in by Dean P. Macri. It was a little bit modified
	// by me afterwards.
	s32 idx;
	s32 cpidx[9];

	for( s32 row=0; row<Faces[i].size[1]-2; row+=2 )
		for( s32 col=0; col<Faces[i].size[0]-2; col+=2 )
		{
			cpidx[0] = (row * Faces[i].size[0] + col) + Faces[i].vertexIndex;
			cpidx[1] = (row * Faces[i].size[0] + col + 1) + Faces[i].vertexIndex;
			cpidx[2] = (row * Faces[i].size[0] + col + 2) + Faces[i].vertexIndex;
			cpidx[3] = ((row+1) * Faces[i].size[0] + col) + Faces[i].vertexIndex;
			cpidx[4] = ((row+1) * Faces[i].size[0] + col + 1) + Faces[i].vertexIndex;
			cpidx[5] = ((row+1) * Faces[i].size[0] + col + 2) + Faces[i].vertexIndex;
			cpidx[6] = ((row+2) * Faces[i].size[0] + col) + Faces[i].vertexIndex;
			cpidx[7] = ((row+2) * Faces[i].size[0] + col + 1) + Faces[i].vertexIndex;
			cpidx[8] = ((row+2) * Faces[i].size[0] + col + 2) + Faces[i].vertexIndex;

			f32 s, sstep;
			f32 t, tstep;
			f32 cs[3], ct[3], nxs[3], nxt[3];
			// For some reason if we tesselate more than this,
			// some of the patches don't show up!
			sstep = 1.0f / 3.0f;
			tstep = 1.0f / 3.0f;

			tBSPVertex *v[9] = 
			{
					&Vertices[cpidx[0]],
					&Vertices[cpidx[1]],
					&Vertices[cpidx[2]],
					&Vertices[cpidx[3]],
					&Vertices[cpidx[4]],
					&Vertices[cpidx[5]],
					&Vertices[cpidx[6]],
					&Vertices[cpidx[7]],
					&Vertices[cpidx[8]]
			};
			video::S3DVertex2TCoords currentVertex[4];
			for( s=0; s<1.0f; s+= sstep )
			{
				cs[0] = (1.0f-s)*(1.0f-s);
				cs[1] = 2.0f * (1.0f - s) * s;
				cs[2] = s * s;
				nxs[0] = (1.0f-s-sstep)*(1.0f-s-sstep);
				nxs[1] = 2.0f * (1.0f - s -sstep) * (s+sstep);
				nxs[2] = (s+sstep) * (s+sstep);
				for( t=0; t<1.0f; t+=tstep )
				{
					idx = meshBuffer->getVertexCount();
					ct[0] = (1.0f-t)*(1.0f-t);
					ct[1] = 2.0f * (1.0f - t) * t;
					ct[2] = t * t;
					nxt[0] = (1.0f-t-tstep)*(1.0f-t-tstep);
					nxt[1] = 2.0f * (1.0f - t - tstep) * (t+tstep);
					nxt[2] = (t+tstep) * (t+tstep);

					// Vert 1
					currentVertex[0].Color.set(255,255,255,255);
					currentVertex[0].Pos.X = floorf( Blend( cs, ct, v, (char*)&v[0]->vPosition[0] - (char*)v[0])+ 0.5f);
					currentVertex[0].Pos.Y = floorf( Blend( cs, ct, v, (char*)&v[0]->vPosition[2] - (char*)v[0])+ 0.5f);
					currentVertex[0].Pos.Z = floorf( Blend( cs, ct, v, (char*)&v[0]->vPosition[1] - (char*)v[0])+ 0.5f);
					currentVertex[0].Normal.X = Blend( cs, ct, v, (char*)&v[0]->vNormal[0] - (char*)v[0]);
					currentVertex[0].Normal.Y = Blend( cs, ct, v, (char*)&v[0]->vNormal[2] - (char*)v[0]);
					currentVertex[0].Normal.Z = Blend( cs, ct, v, (char*)&v[0]->vNormal[1] - (char*)v[0]);
					currentVertex[0].TCoords.X = Blend( cs, ct, v, (char*)&v[0]->vTextureCoord[0] - (char*)v[0]);
					currentVertex[0].TCoords.Y = Blend( cs, ct, v, (char*)&v[0]->vTextureCoord[1] - (char*)v[0]);
					currentVertex[0].TCoords2.X = Blend( cs, ct, v, (char*)&v[0]->vLightmapCoord[0] - (char*)v[0]);
					currentVertex[0].TCoords2.Y = Blend( cs, ct, v, (char*)&v[0]->vLightmapCoord[1] - (char*)v[0]);
					// Vert 2
					currentVertex[1].Color.set(255,255,255,255);
					currentVertex[1].Pos.X = floorf( Blend( cs, nxt, v, (char*)&v[0]->vPosition[0] - (char*)v[0])+ 0.5f);
					currentVertex[1].Pos.Y = floorf( Blend( cs, nxt, v, (char*)&v[0]->vPosition[2] - (char*)v[0])+ 0.5f);
					currentVertex[1].Pos.Z = floorf( Blend( cs, nxt, v, (char*)&v[0]->vPosition[1] - (char*)v[0])+ 0.5f);
					currentVertex[1].Normal.X = Blend( cs, nxt, v, (char*)&v[0]->vNormal[0] - (char*)v[0]);
					currentVertex[1].Normal.Y = Blend( cs, nxt, v, (char*)&v[0]->vNormal[2] - (char*)v[0]);
					currentVertex[1].Normal.Z = Blend( cs, nxt, v, (char*)&v[0]->vNormal[1] - (char*)v[0]);
					currentVertex[1].TCoords.X = Blend( cs, nxt, v, (char*)&v[0]->vTextureCoord[0] - (char*)v[0]);
					currentVertex[1].TCoords.Y = Blend( cs, nxt, v, (char*)&v[0]->vTextureCoord[1] - (char*)v[0]);
					currentVertex[1].TCoords2.X = Blend( cs, nxt, v, (char*)&v[0]->vLightmapCoord[0] - (char*)v[0]);
					currentVertex[1].TCoords2.Y = Blend( cs, nxt, v, (char*)&v[0]->vLightmapCoord[1] - (char*)v[0]);
					// Vert 3
					currentVertex[2].Color.set(255,255,255,255);
					currentVertex[2].Pos.X = floorf( Blend( nxs, ct, v, (char*)&v[0]->vPosition[0] - (char*)v[0])+ 0.5f);
					currentVertex[2].Pos.Y = floorf( Blend( nxs, ct, v, (char*)&v[0]->vPosition[2] - (char*)v[0])+ 0.5f);
					currentVertex[2].Pos.Z = floorf( Blend( nxs, ct, v, (char*)&v[0]->vPosition[1] - (char*)v[0])+ 0.5f);
					currentVertex[2].Normal.X = Blend( nxs, ct, v, (char*)&v[0]->vNormal[0] - (char*)v[0]);
					currentVertex[2].Normal.Y = Blend( nxs, ct, v, (char*)&v[0]->vNormal[2] - (char*)v[0]);
					currentVertex[2].Normal.Z = Blend( nxs, ct, v, (char*)&v[0]->vNormal[1] - (char*)v[0]);
					currentVertex[2].TCoords.X = Blend( nxs, ct, v, (char*)&v[0]->vTextureCoord[0] - (char*)v[0]);
					currentVertex[2].TCoords.Y = Blend( nxs, ct, v, (char*)&v[0]->vTextureCoord[1] - (char*)v[0]);
					currentVertex[2].TCoords2.X = Blend( nxs, ct, v, (char*)&v[0]->vLightmapCoord[0] - (char*)v[0]);
					currentVertex[2].TCoords2.Y = Blend( nxs, ct, v, (char*)&v[0]->vLightmapCoord[1] - (char*)v[0]);
					// Vert 4
					currentVertex[3].Color.set(255,255,255,255);
					currentVertex[3].Pos.X = floorf(Blend( nxs, nxt, v, (char*)&v[0]->vPosition[0] - (char*)v[0])+ 0.5f);
					currentVertex[3].Pos.Y = floorf(Blend( nxs, nxt, v, (char*)&v[0]->vPosition[2] - (char*)v[0])+ 0.5f);
					currentVertex[3].Pos.Z = floorf(Blend( nxs, nxt, v, (char*)&v[0]->vPosition[1] - (char*)v[0])+ 0.5f);
					currentVertex[3].Normal.X = Blend( nxs, nxt, v, (char*)&v[0]->vNormal[0] - (char*)v[0]);
					currentVertex[3].Normal.Y = Blend( nxs, nxt, v, (char*)&v[0]->vNormal[2] - (char*)v[0]);
					currentVertex[3].Normal.Z = Blend( nxs, nxt, v, (char*)&v[0]->vNormal[1] - (char*)v[0]);
					currentVertex[3].TCoords.X = Blend( nxs, nxt, v, (char*)&v[0]->vTextureCoord[0] - (char*)v[0]);
					currentVertex[3].TCoords.Y = Blend( nxs, nxt, v, (char*)&v[0]->vTextureCoord[1] - (char*)v[0]);
					currentVertex[3].TCoords2.X = Blend( nxs, nxt, v, (char*)&v[0]->vLightmapCoord[0] - (char*)v[0]);
					currentVertex[3].TCoords2.Y = Blend( nxs, nxt, v, (char*)&v[0]->vLightmapCoord[1] - (char*)v[0]);
					// Put the vertices in the mesh buffer
					meshBuffer->Vertices.push_back(currentVertex[0]);
					meshBuffer->Vertices.push_back(currentVertex[2]);
					meshBuffer->Vertices.push_back(currentVertex[1]);

					meshBuffer->Vertices.push_back(currentVertex[1]);
					meshBuffer->Vertices.push_back(currentVertex[2]);
					meshBuffer->Vertices.push_back(currentVertex[3]);

					// add indexes
					meshBuffer->Indices.push_back(idx);
					meshBuffer->Indices.push_back(idx+1);
					meshBuffer->Indices.push_back(idx+2);
					// add indexes
					meshBuffer->Indices.push_back(idx+3);
					meshBuffer->Indices.push_back(idx+4);
					meshBuffer->Indices.push_back(idx+5);
				}
			}

		}
}


//! loads the textures
void CQ3LevelMesh::loadTextures()
{
	if (!Driver)
		return;

	core::stringc s;
	core::stringc extensions[2];
	extensions[0] = ".jpg";
	extensions[1] = ".tga";

	// load textures

	core::array<video::ITexture*> tex;
	tex.set_used(NumTextures+1);

	tex[0] = 0;
	
	s32 t;// new ISO for scoping problem with some compilers

	for (t=1; t<(NumTextures+1); ++t)
	{
		tex[t] = 0;

		for (s32 e=0; e<2; ++e)
		{
			s = Textures[t-1].strName;
			s.append(extensions[e]);
			if (FileSystem->existFile(s.c_str()))
			{
				tex[t] = Driver->getTexture(s.c_str());
				break;
			}
		}

		if (!tex[t])
			os::Printer::log("Could not find texture in Q3 .bsp", Textures[t-1].strName, ELL_WARNING);
	}

	// load lightmaps.

	core::array<video::ITexture*> lig;
	lig.set_used(NumLightMaps+1);

	lig[0] = 0;

	c8 lightmapname[255];
	core::dimension2d<s32> lmapsize(128,128);

	bool oldMipMapState = Driver->getTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS);
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, false);

	for (t=1; t<(NumLightMaps+1); ++t)
	{
		sprintf(lightmapname, "%s.lightmap.%d", LevelName.c_str(), t);
		lig[t] = Driver->addTexture(lmapsize, lightmapname);

		if (lig[t]->getSize() != lmapsize)
			os::Printer::log("Created lightmap is not 128x128", ELL_ERROR);

		if (lig[t])
		{
			void* pp = lig[t]->lock();

			if (pp)
			{
				video::ECOLOR_FORMAT format = lig[t]->getColorFormat();
				if (format == video::ECF_A1R5G5B5)
				{
					s16* p = (s16*)pp;

					tBSPLightmap* lm;
					lm = &LightMaps[t-1];

					for (s32 x=0; x<128; ++x)
						for (s32 y=0; y<128; ++y)
						{
							p[x*128 + y] = video::RGB16(
								lm->imageBits[x][y][0],
								lm->imageBits[x][y][1],
								lm->imageBits[x][y][2]);
						}
				}
				else
				if (format == video::ECF_A8R8G8B8)
				{
					s32* p = (s32*)pp;

					tBSPLightmap* lm;
					lm = &LightMaps[t-1];

					for (s32 x=0; x<128; ++x)
						for (s32 y=0; y<128; ++y)
						{
							p[x*128 + y] = video::SColor(255,
								lm->imageBits[x][y][0],
								lm->imageBits[x][y][1],
								lm->imageBits[x][y][2]).color;
						}
				}
				else
					os::Printer::log("Could not create lightmap, unsupported texture format.", ELL_ERROR);
			}

			lig[t]->unlock();
		}
		else
			os::Printer::log("Could not create lightmap, driver created no texture.", ELL_ERROR);
		
	}

	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, oldMipMapState);

	// attach textures to materials.

	for (s32 l=0; l<NumLightMaps+1; ++l)
		for (t=0; t<NumTextures+1; ++t)
		{
			SMeshBufferLightMap* b = (SMeshBufferLightMap*)Mesh->getMeshBuffer(l*(NumTextures+1) + t);
			b->Material.Texture2 = lig[l];
			b->Material.Texture1 = tex[t];

			if (!b->Material.Texture2)
				b->Material.MaterialType = video::EMT_SOLID;
		}

	// delete all buffers without geometry in it.

	s32 i = 0;
	while(i < (s32)Mesh->MeshBuffers.size())
	{
		if (Mesh->MeshBuffers[i]->getVertexCount() == 0 ||
			Mesh->MeshBuffers[i]->getIndexCount() == 0 ||
			Mesh->MeshBuffers[i]->getMaterial().Texture1 == 0)
		{
			// Meshbuffer löschen
			Mesh->MeshBuffers[i]->drop();
			Mesh->MeshBuffers.erase(i);		
		}
		else
			++i;
	}
}



//! Returns an axis aligned bounding box of the mesh.
//! \return A bounding box of this mesh is returned.
const core::aabbox3d<f32>& CQ3LevelMesh::getBoundingBox() const
{
	return Mesh->getBoundingBox();
}


//! Returns the type of the animated mesh.
E_ANIMATED_MESH_TYPE CQ3LevelMesh::getMeshType() const
{
	return scene::EAMT_BSP;
}

} // end namespace scene
} // end namespace irr

