// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h
// 
// This file was originally written by Salvatore Russo.
// I (Nikolaus Gebhardt) did some minor modifications and changes to it and integrated 
// it into Irrlicht.
// Thanks a lot to Salvatore for his work on this and that he gave me
// his permission to add it into Irrlicht using the zlib license.

/*
  CDMFLoader by Salvatore Russo (September 2005)

  See the header file for additional information including use and distribution rights.
*/

#include "CDMFLoader.h"
#include "ISceneManager.h"
#include "IAttributes.h"
#include "SAnimatedMesh.h"
#include "SMeshBufferLightMap.h"
#include "irrString.h"
#include "irrMath.h"
#include <string.h>
#include "dmfsupport.h"

namespace irr
{
namespace scene
{

/** Constructor*/
CDMFLoader::CDMFLoader(video::IVideoDriver* driver, ISceneManager* smgr)
: Driver(driver) , SceneMgr(smgr)
{
	#ifdef _DEBUG
	IUnknown::setDebugName("CDMFLoader");
	#endif	

	if (Driver) 
		Driver->grab();
}


/** Destructor*/
CDMFLoader::~CDMFLoader()
{
	if (Driver) 
		Driver->drop();
}



/** Given first three points of a face, returns a face normal*/
void CDMFLoader::GetFaceNormal(f32 a[3], //First point
							   f32 b[3], //Second point
                               f32 c[3], //Third point
							   f32 out[3]) //Normal computed
{
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

	f32 dist = (f32)sqrtf((out[0] * out[0]) + (out[1] * out[1]) + (out[2] * out[2]));

	if (dist == 0.0f) 
		dist = 0.001f;
	
	out[0] /= dist;
	out[1] /= dist;
	out[2] /= dist;
}


/**Creates/loads an animated mesh from the file.
 \return Pointer to the created mesh. Returns 0 if loading failed.
 If you no longer need the mesh, you should call IAnimatedMesh::drop().
 See IUnknown::drop() for more information.*/
IAnimatedMesh* CDMFLoader::createMesh(io::IReadFile* file) 
{
	SMesh * Mesh = new SMesh();

	u32 i;
    
	if (!file) 
		return false;

    //Load stringlist
    StringList dmfRawFile(file);
    
    if (dmfRawFile.size()==0) 
		return false;
    
    //begin logging with
    //fprintf(flog,"DMF Loader version %1.1f log file\nTrying to load: %s\n",DMFloader_vers,file->getFileName());
    dmfHeader header;

    //load header
    if (GetDMFHeader(dmfRawFile, header))
	{
       //print in log file some statistics
       /*fprintf(flog,"DeleD Map Detected\nDeleD Map Version:%1.1f\nScene Name:%s\n"
                    "Ambient Color(r,g,b):%d,%d,%d\nShadow Intensity:%f\n"
                    "Number of Objects:%u\nNumber of Materials:%u\n"
                    "Total Vertices: %u\nTotal Faces: %u\n"
                    "Total Number of Dynamic Lights: %u\n"
                    "Trying to Load Materials...",
                    header.dmfVersion,header.dmfName,
                    header.dmfAmbient.getRed(),header.dmfAmbient.getGreen(),header.dmfAmbient.getBlue(),
                    header.dmfShadow,header.numObjects,header.numMaterials,
                    header.numVertices,header.numFaces,header.numLights);*/

       //let's set ambient light             
       Driver->setAmbientLight( header.dmfAmbient);
      
       //let's create the correct number of materials, vertices and faces
       dmfMaterial *materiali=new dmfMaterial[header.numMaterials];
       dmfVert *verts=new dmfVert[header.numVertices];
       dmfFace *faces=new dmfFace[header.numFaces];       
       
       //let's get the materials
       bool use_mat_dirs=false;
       use_mat_dirs=SceneMgr->getParameters()->getAttributeAsBool(DMF_USE_MATERIALS_DIRS);
       
       if ( GetDMFMaterials(dmfRawFile , materiali,header.numMaterials,use_mat_dirs))
	   {
               //print to log file some stats
               /*fprintf(flog,"Materials Loaded\nShowing 1st and last infos:\n"
                            " -1st Material (id,texname,lmapname):%u,%s,%s\n"
                            " -Last Material (id,texname,lmapname):%u,%s,%s\n",
                            materiali[0].materialID,materiali[0].textureName,
                            materiali[0].lightmapName,materiali[header.numMaterials-1].materialID,
                            materiali[header.numMaterials-1].textureName,materiali[header.numMaterials-1].lightmapName);*/
       }
       /*else 
		   fprintf(flog,"Cannot Load Materials.\n");

       fprintf(flog,"Trying to load Vertices and Faces...");*/
       
       //let's get vertices and faces
       if(GetDMFVerticesFaces(dmfRawFile, verts,faces))
	   {
              /* fprintf(flog,"Loaded\nShowing 1st face and last vertex infos:\n"
                            " -1st Face (1st vertex,vert num,material id): %u,%u,%u\n"
                            " -Last vertice (position;tex uv;lmap uv): %f,%f,%f;%f,%f;%f,%f\n",
                            faces[0].firstVert,faces[0].numVerts,faces[0].materialID,
                            verts[header.numVertices-1].pos[0],verts[header.numVertices-1].pos[1],verts[header.numVertices-1].pos[2],
                            verts[header.numVertices-1].tc[0],verts[header.numVertices-1].tc[1],
                            verts[header.numVertices-1].lc[0],verts[header.numVertices-1].lc[1]);*/
       }
	   /*else 
		   fprintf(flog,"Cannot Load Vertices and Faces.\n");*/
              
       //create a meshbuffer for each material, then we'll remove empty ones
       for (i=0; i<header.numMaterials; i++)
	   {
			//create a new SMeshBufferLightMap for each material
			SMeshBufferLightMap* buffer = new SMeshBufferLightMap();
			buffer->Material.MaterialType = video::EMT_LIGHTMAP_LIGHTING  ;
			buffer->Material.Wireframe = false;
			buffer->Material.Lighting = true;
			buffer->Material.BilinearFilter = true;
			Mesh->addMeshBuffer(buffer);
			buffer->drop();
	   }
  
  // Build the mesh buffers 
	for (i = 0; i < header.numFaces; i++) 
	{
		if (faces[i].numVerts < 3) 
			continue;

		f32 normal[3];

		GetFaceNormal(verts[faces[i].firstVert].pos, 
			verts[faces[i].firstVert+1].pos, verts[faces[i].firstVert+2].pos, normal);

		SMeshBufferLightMap * meshBuffer = (SMeshBufferLightMap*)Mesh->getMeshBuffer(
			faces[i].materialID);

		u32 base = meshBuffer->Vertices.size();
		
		// Add this face's verts
		u32 v;
		for (v = 0; v < faces[i].numVerts; v++)
		{
			dmfVert * vv = &verts[faces[i].firstVert + v];
			video::S3DVertex2TCoords vert(vv->pos[0], vv->pos[1], vv->pos[2],
				normal[0], normal[1], normal[2], video::SColor(0,255,255,255), 0.0f, 0.0f);
			if ( materiali[faces[i].materialID].textureBlend==4 && 
				 SceneMgr->getParameters()->getAttributeAsBool(DMF_FLIP_ALPHA_TEXTURES))
			{
				vert.TCoords.set(vv->tc[0],-vv->tc[1]);
				vert.TCoords2.set(vv->lc[0],vv->lc[1]);
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
		// I do it this way instead of a simple fan because it usually
		// looks  a lot better in wireframe, for example.
		u32 h = faces[i].numVerts - 1, l = 0, c; // High, Low, Center
		for (v = 0; v < faces[i].numVerts - 2; v++)
		{
			if (v & 1) 
				c = h - 1; 
			else 
				c = l + 1;

			meshBuffer->Indices.push_back(base + h);
			meshBuffer->Indices.push_back(base + l);
			meshBuffer->Indices.push_back(base + c);

			if (v & 1) 
				h--; 
			else 
				l++;
		}
    }

    //load textures and lightmaps in materials.
    //don't worry if you receive a could not load texture, cause if you don't need
    //a particular material in your scene it will be loaded and then destroyed.
    for (i=0; i<header.numMaterials; i++)
	{
		String path;
        path = "";
        if ( !SceneMgr->getParameters()->existsAttribute(DMF_TEXTURE_PATH) )
		{
			//get the right path for textures
			StringList filepath = SubdivideString(String(file->getFileName()),"\\");
			StringList filepath1 = SubdivideString(String(file->getFileName()),"/");
			if(filepath1.size()>filepath.size())
			{
				filepath.clear();
				filepath=filepath1;
			}

			for (int j=0; j<(int)(filepath.size()-1); j++)
				path = path + filepath[j] + String("\\");
        }
        else 
			path = path + 
				String( SceneMgr->getParameters()->getAttributeAsString(DMF_TEXTURE_PATH)) + String("\\");

        //texture and lightmap
        ITexture *tex = 0;
        ITexture *lig = 0;
        
		
        //currrent buffer to apply material
        SMeshBufferLightMap* buffer = (SMeshBufferLightMap*)Mesh->getMeshBuffer(i);

		//Primary texture is normal
        if (materiali[i].textureFlag==0) 
			if(materiali[i].textureBlend==4) Driver->setTextureCreationFlag  (ETCF_ALWAYS_32_BIT,true);
			tex = Driver->getTexture((path+String(materiali[i].textureName)).c_str());

        //Primary texture is just a colour
        if(materiali[i].textureFlag==1) 
		{
			String colour(materiali[i].textureName);
			String alpha,red,green,blue;

			alpha.append((char*)&colour[0]);
			alpha.append((char*)&colour[1]);
			blue.append((char*)&colour[2]);
			blue.append((char*)&colour[3]);
			green.append((char*)&colour[4]);
			green.append((char*)&colour[5]);
			red.append((char*)&colour[6]);
			red.append((char*)&colour[7]);

			SColor color(axtoi(alpha.c_str()),
				axtoi(red.c_str()),axtoi(green.c_str()),
				axtoi(blue.c_str()));

			s32 col = color.color;
			s32 buf[64];

			for (int k=0; k<64; k++)
				buf[k]=col;

            //just for compatibility with older Irrlicht versions
			//to support transparent materials
			if(color.getAlpha()!=255 && materiali[i].textureBlend==4) Driver->setTextureCreationFlag  (ETCF_ALWAYS_32_BIT,true);

			IImage *immagine=Driver->createImageFromData(ECF_A8R8G8B8,
				core::dimension2d<s32>(8,8),buf);

			tex = Driver->addTexture("", immagine);
			
			//to support transparent materials
			if(color.getAlpha()!=255 && materiali[i].textureBlend==4){
			    buffer->Material.MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL ;
                buffer->Material.MaterialTypeParam =(((float) (color.getAlpha()-1))/255.0f);
			}
			immagine->drop();
        }

        //Lightmap is present
        if (materiali[i].lightmapFlag == 0)
            lig = Driver->getTexture((path+String(materiali[i].lightmapName)).c_str());
        else //no lightmap
		{
             lig = 0;
             buffer->Material.MaterialType = video::EMT_SOLID;
             float mult = 100.0f - header.dmfShadow;
             mult = mult/100.0f;
             buffer->Material.AmbientColor=header.dmfAmbient.getInterpolated  (  SColor(255,0,0,0),mult);
        }		
        
        if(materiali[i].textureBlend==4) {
        	buffer->Material.MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL;
        	buffer->Material.MaterialTypeParam =SceneMgr->getParameters()->getAttributeAsFloat(DMF_ALPHA_CHANNEL_REF);
        	}
             
        core::dimension2d<s32> texsize;
        core::dimension2d<s32> ligsize;
        
        if (tex && header.dmfVersion<1.1)
			texsize=tex->getSize();

        if (lig && header.dmfVersion<1.1)
			ligsize=lig->getSize();					
                    
        // fprintf(flog,"%s real size w:%d h:%d\n",materiali[i].textureName,texsize.Width,texsize.Height);
        
        //if texture is present mirror vertically owing to DeleD rapresentation
        if (tex && header.dmfVersion<1.1)
		{
			void* pp = tex->lock();
            if (pp)
			{
				video::ECOLOR_FORMAT format = tex->getColorFormat();
				if (format == video::ECF_A1R5G5B5)
				{
					s16* p = (s16*)pp;
					s16 tmp=0;
					for (s32 x=0; x<texsize.Width; x++)
						for (s32 y=0; y<texsize.Height/2; y++)
						{
							tmp=p[y*texsize.Width + x];
                            p[y*texsize.Width + x] = p[(texsize.Height-y-1)*texsize.Width + x];
                            p[(texsize.Height-y-1)*texsize.Width + x]=tmp;
						}

					//fprintf(flog,"  Color Format: ECF_A1R5G5B5\n"	);
				}
				else
				if (format == video::ECF_A8R8G8B8)
				{
					s32* p = (s32*)pp;
					s32 tmp=0;
                    for (s32 x=0; x<texsize.Width; x++)
						for (s32 y=0; y<texsize.Height/2; y++)
						{
							tmp=p[y*texsize.Width + x];
                            p[y*texsize.Width + x] = p[(texsize.Height-y-1)*texsize.Width + x];
                            p[(texsize.Height-y-1)*texsize.Width + x]=tmp;
						}
			     //fprintf(flog,"  Color Format: ECF_A8R8G8B8\n"	);
				}
				
		}
    }

    if(tex && header.dmfVersion<1.1)
	{
       tex->unlock();   
       tex->regenerateMipMapLevels();
    }

    //if lightmap is present mirror vertically owing to DeleD rapresentation
    if (lig && header.dmfVersion<1.1)
	{
			void* pp = lig->lock();
            if (pp)
			{
				video::ECOLOR_FORMAT format = lig->getColorFormat();
				if (format == video::ECF_A1R5G5B5)
				{
					s16* p = (s16*)pp;
					s16 tmp=0;
					for (s32 x=0; x<ligsize.Width; x++)
						for (s32 y=0; y<ligsize.Height/2; y++)
						{
							tmp=p[y*ligsize.Width + x];
                            p[y*ligsize.Width + x] = p[(ligsize.Height-y-1)*ligsize.Width + x];
                            p[(ligsize.Height-y-1)*ligsize.Width + x]=tmp;
						}

					//fprintf(flog,"  Color Format: ECF_A1R5G5B5\n"	);
				}
				else
				if (format == video::ECF_A8R8G8B8)
				{
					s32* p = (s32*)pp;
					s32 tmp=0;
                    for (s32 x=0; x<ligsize.Width; x++)
						for (s32 y=0; y<ligsize.Height/2; y++)
						{
							tmp=p[y*ligsize.Width + x];
                            p[y*ligsize.Width + x] = p[(ligsize.Height-y-1)*ligsize.Width + x];
                            p[(ligsize.Height-y-1)*ligsize.Width + x]=tmp;
						}

					//fprintf(flog,"  Color Format: ECF_A8R8G8B8\n"	);
				}
				
			}
    }

    if (lig && header.dmfVersion<1.1)
	{
            lig->unlock();
            lig->regenerateMipMapLevels();
    }       

    buffer->Material.Texture1=tex;
    buffer->Material.Texture2=lig;
  }
  
  delete verts;
  delete faces;
  delete materiali;
}
//else
// fprintf(flog,"Cannot load %s, please check it's generated with DeleD 0.91+.");   
    
    //closing log file
    //fclose(flog);

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
	for (i = 0; i < Mesh->MeshBuffers.size(); i++) 
	{
		((SMeshBufferLightMap*)Mesh->MeshBuffers[i])->recalculateBoundingBox();
	}
	Mesh->recalculateBoundingBox();


	// Set up an animated mesh to hold the mesh
	SAnimatedMesh* AMesh = new SAnimatedMesh();
	AMesh->Type = EAMT_UNKNOWN;
	AMesh->addMesh(Mesh);
	AMesh->recalculateBoundingBox();
	Mesh->drop();

	return AMesh;
}


/** \brief Tell us if this file is able to be loaded by this class
 based on the file extension (e.g. ".bsp")
 \return true if file is loadable.*/
bool CDMFLoader::isALoadableFileExtension(const c8* filename)
{
	return strstr(filename, ".dmf") != 0;
}


} // end namespace scene
} // end namespace irr
