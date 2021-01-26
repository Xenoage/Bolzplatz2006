// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h
//
// originally written by Murphy McCauley, see COCTLoader.h for details.
//
// COCTLoader by Murphy McCauley (February 2005)
// An Irrlicht loader for OCT files
//
// See the header file for additional information including use and distribution rights.

#include "COCTLoader.h"
#include "ISceneManager.h"
#include "os.h"
#include "SAnimatedMesh.h"
#include "SMeshBufferLightMap.h"
#include "irrString.h"
#include <string.h>

namespace irr
{
namespace scene
{

//! constructor
COCTLoader::COCTLoader(video::IVideoDriver* driver)
: Driver(driver)
{
	#ifdef _DEBUG
	IUnknown::setDebugName("COCTLoader");
	#endif	

	if (Driver) 
		Driver->grab();
}


//! destructor
COCTLoader::~COCTLoader()
{
	if (Driver) 
		Driver->drop();
}



// Doesn't really belong here, but it's jammed in for now.
void COCTLoader::OCTLoadLights(irr::io::IReadFile* file, irr::scene::ISceneManager * scene, irr::scene::ISceneNode * parent, f32 radius, f32 intensityScale, bool rewind)
{
	if (rewind) file->seek(0);

	octHeader header;
	file->read(&header, sizeof(octHeader));
 	
	file->seek(sizeof(octVert)*header.numVerts, true);
	file->seek(sizeof(octFace)*header.numFaces, true);
	file->seek(sizeof(octTexture)*header.numTextures, true);
	file->seek(sizeof(octLightmap)*header.numLightmaps, true);

	octLight * lights = new octLight[header.numLights];
	file->read(lights, header.numLights * sizeof(octLight));

	//TODO: Skip past my extended data just for good form

	u32 i;
	for (i = 0; i < header.numLights; i++)
	{
		f32 intensity;
		intensity = lights[i].intensity * intensityScale;

		//irr::scene::ISceneNode* node = scene->addCubeSceneNode(30,parent,-1, core::vector3df(lights[i].pos[0], lights[i].pos[2], lights[i].pos[1]));
		//node->getMaterial(0).AmbientColor = video::SColorf(lights[i].color[0] * intensity, lights[i].color[1] * intensity, lights[i].color[2] * intensity).toSColor();

		scene->addLightSceneNode(parent, core::vector3df(lights[i].pos[0], lights[i].pos[2], lights[i].pos[1]), 
			video::SColorf(lights[i].color[0] * intensity, lights[i].color[1] * intensity, lights[i].color[2] * intensity, 1.0f),
			radius);
	}
}


//! given three points representing a face, return a face normal
void COCTLoader::GetFaceNormal(f32 a[3], f32 b[3], f32 c[3], f32 out[3]) {
	f32 v1[3], v2[3];

	v1[0] = a[0] - b[0];
	v1[1] = a[1] - b[1];
	v1[2] = a[2] - b[2];

	v2[0] = b[0] - c[0];
	v2[1] = b[1] - c[1];
	v2[2] = b[2] - c[2];

	out[0] = (v1[1] * v2[2]) - (v1[2] * v2[1]);
	out[1] = (v1[2] * v2[0]) - (v1[0] * v2[2]);
	out[2] = (v1[0] * v2[1]) - (v1[1] * v2[0]);

	f32 dist = (f32)sqrt((out[0] * out[0]) + (out[1] * out[1]) + (out[2] * out[2]));
	if (dist == 0.0f) dist = 0.001f;
	
	out[0] /= dist;
	out[1] /= dist;
	out[2] /= dist;
}


//! creates/loads an animated mesh from the file.
//! \return Pointer to the created mesh. Returns 0 if loading failed.
//! If you no longer need the mesh, you should call IAnimatedMesh::drop().
//! See IUnknown::drop() for more information.
IAnimatedMesh* COCTLoader::createMesh(irr::io::IReadFile* file) 
{
	SMesh * Mesh = new SMesh();

	u32 i;

	if (!file) return false;

	octHeader header;
	file->read(&header, sizeof(octHeader));

	octVert * verts = new octVert[header.numVerts];
	octFace * faces = new octFace[header.numFaces];
	octTexture * textures = new octTexture[header.numTextures];
	octLightmap * lightmaps = new octLightmap[header.numLightmaps];
	octLight * lights = new octLight[header.numLights];

	file->read(verts, sizeof(octVert) * header.numVerts);
	file->read(faces, sizeof(octFace) * header.numFaces);
	//TODO: Make sure id is in the legal range for Textures and Lightmaps
	for (i = 0; i < header.numTextures; i++) {
		octTexture t;
		file->read(&t, sizeof(octTexture));
		textures[t.id] = t;
	}	
	for (i = 0; i < header.numLightmaps; i++) {
		octLightmap t;
		file->read(&t, sizeof(octLightmap));
		lightmaps[t.id] = t;
	}
	file->read(lights, sizeof(octLight) * header.numLights);

	//TODO: Now read in my extended OCT header (flexible lightmaps and vertex normals)
	

	// This is the method Nikolaus Gebhardt used in the Q3 loader -- create a
	// meshbuffer for every possible combination of lightmap and texture including
	// a "null" texture and "null" lightmap.  Ones that end up with nothing in them
	// will be removed later.
	for (i=0; i<(header.numTextures+1) * (header.numLightmaps+1); ++i)
	{
		scene::SMeshBufferLightMap* buffer = new scene::SMeshBufferLightMap();

		buffer->Material.MaterialType = video::EMT_LIGHTMAP;
		buffer->Material.Wireframe = false;
		buffer->Material.Lighting = false;
		buffer->Material.BilinearFilter = true;

		Mesh->addMeshBuffer(buffer);

		buffer->drop();
	}

	
	// Build the mesh buffers
	for (i = 0; i < header.numFaces; i++) {
		if (faces[i].numVerts < 3) continue;

		f32 normal[3];
		GetFaceNormal(verts[faces[i].firstVert].pos,verts[faces[i].firstVert+1].pos,verts[faces[i].firstVert+2].pos, normal);

		u32 textureID = core::min_(s32(faces[i].textureID), s32(header.numTextures - 1)) + 1;
		u32 lightmapID = core::min_(s32(faces[i].lightmapID),s32(header.numLightmaps - 1)) + 1;
		SMeshBufferLightMap * meshBuffer = (SMeshBufferLightMap*)Mesh->getMeshBuffer(lightmapID * (header.numTextures + 1) + textureID);
		u32 base = meshBuffer->Vertices.size();
		
		// Add this face's verts
		u32 v;
		for (v = 0; v < faces[i].numVerts; v++)
		{
			octVert * vv = &verts[faces[i].firstVert + v];
			video::S3DVertex2TCoords vert = video::S3DVertex2TCoords();
			vert.Pos.set(vv->pos[0], vv->pos[1], vv->pos[2]);
			vert.Color = irr::video::SColor(0,255,255,255);
			vert.Normal.set(normal[0], normal[1], normal[2]);

			if (textureID == 0)
			{
				// No texure -- just a lightmap.  Thus, use lightmap coords for texture 1.
				// (the actual texture will be swapped later)
				vert.TCoords.set(vv->lc[0], vv->lc[1]);
			}
			else
			{
				vert.TCoords.set(vv->tc[0], vv->tc[1]);
				vert.TCoords2.set(vv->lc[0], vv->lc[1]);
			}

			meshBuffer->Vertices.push_back(vert);
		}

		// Now add the indices
		// This weird loop turns convex polygons into triangle strips.
		// I do it this way instead of a simple fan because it usually looks a lot better in wireframe, for example.
		u32 h = faces[i].numVerts - 1, l = 0, c; // High, Low, Center
		for (v = 0; v < faces[i].numVerts - 2; v++)
		{
			if (v & 1) c = h - 1; else c = l + 1;

			meshBuffer->Indices.push_back(base + h);
			meshBuffer->Indices.push_back(base + l);
			meshBuffer->Indices.push_back(base + c);

			if (v & 1) h--; else l++;
		}
	} 


	// load textures
	core::array<video::ITexture*> tex;
	tex.set_used(header.numTextures + 1);
	tex[0] = 0;
	
	for (i = 1; i < (header.numTextures + 1); i++)
	{
		tex[i] = Driver->getTexture(textures[i-1].fileName);
	}


	// prepare lightmaps
	core::array<video::ITexture*> lig;
	lig.set_used(header.numLightmaps + 1);

	u32 lightmapWidth = 128, lightmapHeight = 128;
	lig[0] = 0;
	core::dimension2d<s32> lmapsize(lightmapWidth, lightmapHeight);

	bool oldMipMapState = Driver->getTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS);
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, false);

	for (i = 1; i < (header.numLightmaps + 1); i++)
	{
		core::stringc lightmapname = file->getFileName();
		lightmapname += ".lightmap.";
		lightmapname += (int)i;
		lig[i] = Driver->addTexture(lmapsize, lightmapname.c_str());

		if (lig[i]->getSize() != lmapsize)
			os::Printer::log("OCTLoader: Created lightmap is not of the requested size", ELL_ERROR);

		if (lig[i])
		{
			void* pp = lig[i]->lock();

			if (pp)
			{
				video::ECOLOR_FORMAT format = lig[i]->getColorFormat();
				if (format == video::ECF_A1R5G5B5)
				{
					s16* p = (s16*)pp;

					octLightmap * lm;					
					lm = &lightmaps[i-1];

					for (u32 x=0; x<lightmapWidth; ++x)
						for (u32 y=0; y<lightmapHeight; ++y)
						{
							p[x*128 + y] = video::RGB16(
								lm->data[x][y][2],
								lm->data[x][y][1],
								lm->data[x][y][0]);
						}
				}
				else
				if (format == video::ECF_A8R8G8B8)
				{
					s32* p = (s32*)pp;

					octLightmap* lm;
					lm = &lightmaps[i-1];

					for (u32 x=0; x<lightmapWidth; ++x)
						for (u32 y=0; y<lightmapHeight; ++y)
						{
							p[x*128 + y] = video::SColor(255,
								lm->data[x][y][2],
								lm->data[x][y][1],
								lm->data[x][y][0]).color;
						}
				}
				else
					os::Printer::log(
						"OCTLoader: Could not create lightmap, unsupported texture format.", ELL_ERROR);
			}

			lig[i]->unlock();
		}
		else
			os::Printer::log("OCTLoader: Could not create lightmap, driver created no texture.", ELL_ERROR);
	}
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, oldMipMapState);


	// Free stuff
	delete [] verts;
	delete [] faces;
	delete [] textures;
	delete [] lightmaps;
	delete [] lights;


	// attach materials
	u32 j;
	for (i = 0; i < header.numLightmaps + 1; i++)
	{
		for (j = 0; j < header.numTextures + 1; j++)
		{
			u32 mb = i * (header.numTextures + 1) + j;
			SMeshBufferLightMap * meshBuffer = (SMeshBufferLightMap*)Mesh->getMeshBuffer(mb);
			meshBuffer->Material.Texture1 = tex[j];
			meshBuffer->Material.Texture2 = lig[i];

			if (meshBuffer->Material.Texture1 == 0) {
				// This material has no texture, so we'll just show the lightmap if there is one.
				// We swapped the texture coordinates earlier.
				meshBuffer->Material.Texture1 = meshBuffer->Material.Texture2;
				meshBuffer->Material.Texture2 = 0;
			}
			if (meshBuffer->Material.Texture2 == 0)
			{
				// If there is only one texture, it should be solid and lit.
				// Among other things, this way you can preview OCT lights.
				meshBuffer->Material.MaterialType = video::EMT_SOLID;
				meshBuffer->Material.Lighting = true;
			}
		}
	}


	// delete all buffers without geometry in it.
	i = 0;
	while(i < Mesh->MeshBuffers.size())
	{
		if (Mesh->MeshBuffers[i]->getVertexCount() == 0 ||
			Mesh->MeshBuffers[i]->getIndexCount() == 0 ||
			Mesh->MeshBuffers[i]->getMaterial().Texture1 == 0)
		{
			// Meshbuffer is empty -- drop it
			Mesh->MeshBuffers[i]->drop();
			Mesh->MeshBuffers.erase(i);		
		}
		else
		{
			i++;
		}
	}


	// create bounding box
	for (i = 0; i < Mesh->MeshBuffers.size(); i++) {
		((SMeshBufferLightMap*)Mesh->MeshBuffers[i])->recalculateBoundingBox();
	}
	Mesh->recalculateBoundingBox();


	// Set up an animated mesh to hold the mesh
	SAnimatedMesh* AMesh = new SAnimatedMesh();
	AMesh->Type = EAMT_OCT;
	AMesh->addMesh(Mesh);
	AMesh->recalculateBoundingBox();
	Mesh->drop();

	return AMesh;
}


//! returns true if the file maybe is able to be loaded by this class
//! based on the file extension (e.g. ".bsp")
bool COCTLoader::isALoadableFileExtension(const c8* filename)
{
	return strstr(filename, ".oct")!=0;
}


} // end namespace scene
} // end namespace irr
