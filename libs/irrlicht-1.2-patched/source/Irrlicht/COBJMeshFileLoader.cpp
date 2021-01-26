// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include <string.h>
#include <ctype.h>

#include "COBJMeshFileLoader.h"
#include "SMeshBuffer.h"
#include "SAnimatedMesh.h"
#include "fast_atof.h"

namespace irr
{
namespace scene
{

//! Constructor
COBJMeshFileLoader::COBJMeshFileLoader(io::IFileSystem* fs, video::IVideoDriver* driver)
: FileSystem(fs), Driver(driver), Mesh(0)
{
	if (FileSystem)
		FileSystem->grab();

	if (Driver)
		Driver->grab();
}



//! destructor
COBJMeshFileLoader::~COBJMeshFileLoader()
{
	if (FileSystem)
		FileSystem->drop();

	if (Driver)
		Driver->drop();

	if (Mesh)
		Mesh->drop();
}



//! returns true if the file maybe is able to be loaded by this class
//! based on the file extension (e.g. ".bsp")
bool COBJMeshFileLoader::isALoadableFileExtension(const c8* filename)
{
	return strstr(filename, ".obj")!=0;
}



//! creates/loads an animated mesh from the file.
//! \return Pointer to the created mesh. Returns 0 if loading failed.
//! If you no longer need the mesh, you should call IAnimatedMesh::drop().
//! See IUnknown::drop() for more information.
IAnimatedMesh* COBJMeshFileLoader::createMesh(io::IReadFile* file)
{
	const s32 WORD_BUFFER_LENGTH = 255;
	c8 wordBuffer[WORD_BUFFER_LENGTH];

	if (Mesh)
		Mesh->drop();
	Mesh = new SMesh();

	core::array<core::vector3df> vertexBuffer;
	core::array<core::vector2df> textureCoordBuffer;
	core::array<core::vector3df> normalsBuffer;
	SObjGroup * pCurrGroup = 0;
	SObjMtl * pCurrMtl = new SObjMtl();
	pCurrMtl->name="";
	materials.push_back(pCurrMtl);

	s32 filesize = (s32)file->getSize();
	if (!filesize)
		return false;

	// ********************************************************************
	// Patch to locate the file in the same folder as the .obj.
	// If you load the file as "data/some.obj" and mtllib contains
	// "mtlname test.mtl" (as usual), the loading will fail. Instead it
	// must look for data/test.tml. This patch does exactly that.
	//
	// patch by mandrav@codeblocks.org
	// ********************************************************************
	core::stringc obj_fullname = file->getFileName();
	core::stringc obj_relpath = "";
	s32 pathend = obj_fullname.findLast('/');
	if (pathend == -1)
		pathend = obj_fullname.findLast('\\');
	if (pathend != -1)
		obj_relpath = obj_fullname.subString(0, pathend + 1);
	// ********************************************************************
	// end of mtl folder patch
	// ********************************************************************

	c8* pBuf = new c8[filesize];
	file->read((void*)pBuf, filesize);
	const c8* pBufEnd = pBuf+filesize;

	// Process obj information
	c8* pBufPtr = pBuf;
	SObjMtl *pUseMtl=0;
	while(pBufPtr && (pBufPtr-pBuf<filesize))
	{
		switch(pBufPtr[0])
		{
		case '#':       // comment 
			// eat up rest of line
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;

		case 'm':	// mtllib (material)
			pBufPtr = goAndCopyNextWord(wordBuffer, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
			readMTL(wordBuffer, obj_relpath);
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;

		case 'v':               // v, vn, vt
			switch(pBufPtr[1])
			{
			case ' ':          // vertex
				{
					// vector
					c8 v1[WORD_BUFFER_LENGTH];
					c8 v2[WORD_BUFFER_LENGTH];
					c8 v3[WORD_BUFFER_LENGTH];
					pBufPtr = goAndCopyNextWord(v1, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(v2, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(v3, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);

					vertexBuffer.push_back(core::vector3df(-core::fast_atof(v1),
																core::fast_atof(v2),
																core::fast_atof(v3)));
				}
				pBufPtr = goNextLine(pBufPtr, pBufEnd);
				break;

			case 'n':       // normal
				{
					// vector
					c8 v1[WORD_BUFFER_LENGTH];
					c8 v2[WORD_BUFFER_LENGTH];
					c8 v3[WORD_BUFFER_LENGTH];
					pBufPtr = goAndCopyNextWord(v1, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(v2, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(v3, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);

					normalsBuffer.push_back(core::vector3df(-core::fast_atof(v1),
						core::fast_atof(v2),
						core::fast_atof(v3)));
				}
				pBufPtr = goNextLine(pBufPtr, pBufEnd);
				break;

			case 't':       // texcoord
				{
					// vector
					c8 v1[WORD_BUFFER_LENGTH];
					c8 v2[WORD_BUFFER_LENGTH];
					pBufPtr = goAndCopyNextWord(v1, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(v2, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);

					textureCoordBuffer.push_back(core::vector2df(core::fast_atof(v1),
						-core::fast_atof(v2)));
				}
				pBufPtr = goNextLine(pBufPtr, pBufEnd);
				break;

			default:
				// eat up rest of line
				pBufPtr = goNextLine(pBufPtr, pBufEnd);
				break;
			}
			break;

		case 'g':               // group
			// get name of group
			{
				c8 lineBuffer[WORD_BUFFER_LENGTH];
				copyLine(lineBuffer, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
				goAndCopyNextWord(wordBuffer, lineBuffer, WORD_BUFFER_LENGTH, pBufEnd);
				pCurrGroup = findOrAddGroup(wordBuffer);
			}
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;

		case 'u':				// usemtl
			// get name of material
			pBufPtr = goAndCopyNextWord(wordBuffer, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
			// retrieve the material
			pUseMtl = findMtl( wordBuffer );
			// only change material if we found it
			if (pUseMtl)
				pCurrMtl = pUseMtl;
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;

		case 'f':               // face
		{
			c8 vertexWord[WORD_BUFFER_LENGTH];	// for retriving vertex data
			video::S3DVertex v;
			s32 currentVertexCount = pCurrMtl->pMeshbuffer->Vertices.size();
			u32 facePointCount = 0;	// number of vertices in this face

			// Assign vertex colour from currently active material's diffuse colour
			if (pCurrMtl)
				v.Color = pCurrMtl->pMeshbuffer->Material.DiffuseColor;

			// get all vertices data in this face (current line of obj file)
			copyLine(wordBuffer, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
			c8 * pLinePtr = wordBuffer;
				
			// read in all vertices
			pLinePtr = goNextWord(pLinePtr, pBufEnd);
			while (0 != pLinePtr[0])
			{
				// Array to communicate with retrieveVertexIndices()
				// sends the buffer sizes and gets the actual indices
				// if index not set returns -1
				s32 Idx[3];
				Idx[0]=vertexBuffer.size();
				Idx[1]=textureCoordBuffer.size();
				Idx[2]=normalsBuffer.size();

				// read in next vertex's data
				copyWord(vertexWord, pLinePtr, WORD_BUFFER_LENGTH, pBufEnd);
				// this function will also convert obj's 1-based index to c++'s 0-based index
				retrieveVertexIndices(vertexWord, Idx, pBufEnd);
				if ( -1 != Idx[0] )
				{
					v.Pos = vertexBuffer[Idx[0]];
				}
				if ( -1 != Idx[1] )
				{
					v.TCoords = textureCoordBuffer[Idx[1]];
				}
				if ( -1 != Idx[2] )
				{
					v.Normal = normalsBuffer[Idx[2]];
				}
				pCurrMtl->pMeshbuffer->Vertices.push_back(v);
				++facePointCount;

				// go to next vertex
				pLinePtr = goNextWord(pLinePtr, pBufEnd);
			}

			// Add indices for first 3 vertices
			pCurrMtl->pMeshbuffer->Indices.push_back( currentVertexCount );
			pCurrMtl->pMeshbuffer->Indices.push_back( ( facePointCount - 1 ) + currentVertexCount );
			pCurrMtl->pMeshbuffer->Indices.push_back( ( facePointCount - 2 ) + currentVertexCount );
			// Add indices for subsequent vertices
			for ( u32 i = 0; i < facePointCount - 3; ++i )
			{
				pCurrMtl->pMeshbuffer->Indices.push_back( currentVertexCount );
				pCurrMtl->pMeshbuffer->Indices.push_back( ( facePointCount - 2 - i ) + currentVertexCount );
				pCurrMtl->pMeshbuffer->Indices.push_back( ( facePointCount - 3 - i ) + currentVertexCount );
			}

			pBufPtr = goNextLine(pBufPtr, pBufEnd);
		}
			break;

		default:
			// eat up rest of line
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;
		}	// end switch(pBufPtr[0])
	}	// end while(pBufPtr && (pBufPtr-pBuf<filesize))

	// Combine all the groups (meshbuffers) into the mesh
	for ( u32 m = 0; m < materials.size(); ++m )
	{
		if ( materials[m]->pMeshbuffer->getIndexCount() > 0 )
		{
			materials[m]->pMeshbuffer->recalculateBoundingBox();
			Mesh->addMeshBuffer( materials[m]->pMeshbuffer );
		}
	}

	// Create the Animated mesh if there's anything in the mesh
	SAnimatedMesh* pAM = 0;
	if ( 0 != Mesh->getMeshBufferCount() )
	{
		Mesh->recalculateBoundingBox();
		pAM = new SAnimatedMesh();
		pAM->Type = EAMT_OBJ;
		pAM->addMesh(Mesh);
		pAM->recalculateBoundingBox();
	}

	// Clean up the allocate obj file contents
	delete [] pBuf;
	// more cleaning up
	cleanUp();
	Mesh->drop();
	Mesh = 0;

    return pAM;
}


void COBJMeshFileLoader::readMTL(const c8* pFileName, core::stringc relPath)
{
	const s32 WORD_BUFFER_LENGTH = 255;

	io::IReadFile * pMtlReader;
	if (FileSystem->existFile(pFileName))
		pMtlReader = FileSystem->createAndOpenFile(pFileName);
	else
		// try to read in the relative path, the .obj is loaded from
		pMtlReader = FileSystem->createAndOpenFile((relPath + pFileName).c_str());
	if (!pMtlReader)	// fail to open and read file
		return;

	s32 filesize = pMtlReader->getSize();
	if (!filesize)
		return;

	c8* pBuf = new c8[filesize];
	pMtlReader->read((void*)pBuf, filesize);
	const c8* pBufEnd = pBuf+filesize;

	SObjMtl* pCurrMaterial = 0;

	c8* pBufPtr = pBuf;
	while(pBufPtr && (pBufPtr-pBuf<filesize))
	{
		switch(*pBufPtr)
		{
		case 'n':               // newmtl
			{
				// if there's an existing material, store it first
				if ( 0 != pCurrMaterial )
				{
					materials.push_back( pCurrMaterial );
				}

				// extract new material's name
				c8 mtlNameBuf[WORD_BUFFER_LENGTH];
				pBufPtr = goAndCopyNextWord(mtlNameBuf, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);

				pCurrMaterial = new SObjMtl;
				pCurrMaterial->name = mtlNameBuf;

				// go to next line
				pBufPtr = goNextLine(pBufPtr, pBufEnd);
			}
			break;
		case 'i':		// illum - illumination
			if ( 0 != pCurrMaterial )
			{
				const s32 COLOR_BUFFER_LENGTH = 16;
				c8 illumStr[COLOR_BUFFER_LENGTH];

				pBufPtr = goAndCopyNextWord(illumStr, pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
				pCurrMaterial->illumination = (c8)atol(illumStr);
			}
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;
		case 'N':				// Ns - shininess
			if ( 0 != pCurrMaterial )
			{
				const s32 COLOR_BUFFER_LENGTH = 16;
				c8 nsStr[COLOR_BUFFER_LENGTH];

				pBufPtr = goAndCopyNextWord(nsStr, pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
				f32 shininessValue = core::fast_atof(nsStr);

				// wavefront shininess is from [0, 1000], so scale for OpenGL
				shininessValue *= 0.128f;
				pCurrMaterial->pMeshbuffer->Material.Shininess = shininessValue;
			}
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;
		case 'K':
			if ( 0 != pCurrMaterial )
			{
				switch(pBufPtr[1])
				{
				case 'd':		// Kd = diffuse
					{
						pBufPtr = readColor(pBufPtr, pCurrMaterial->pMeshbuffer->Material.DiffuseColor, pBufEnd);

					}
					break;

				case 's':		// Ks = specular
					{
						pBufPtr = readColor(pBufPtr, pCurrMaterial->pMeshbuffer->Material.SpecularColor, pBufEnd);
					}
					break;

				case 'a':		// Ka = ambience
					{
						pBufPtr=readColor(pBufPtr, pCurrMaterial->pMeshbuffer->Material.AmbientColor, pBufEnd);
					}
					break;
				case 'e':		// Ke = emissive
					{
						pBufPtr=readColor(pBufPtr, pCurrMaterial->pMeshbuffer->Material.EmissiveColor, pBufEnd);
					}
					break;

				default:
					// eat up rest of line
					pBufPtr = goNextLine(pBufPtr, pBufEnd);
					break;
				}	// end switch(pBufPtr[1])
			}	// end case 'K': if ( 0 != pCurrMaterial )...
			break;
		case 'm':			// map_Kd - diffuse texture map
			if (0 != pCurrMaterial)
			{
				char type=0;
				if (!strncmp(pBufPtr,"map_bump",8))
					type=1;
				else if (!strncmp(pBufPtr,"map_d",5))
					type=2;
				else if (!strncmp(pBufPtr,"map_refl",8))
					type=2;
				// extract new material's name
				c8 textureNameBuf[WORD_BUFFER_LENGTH];
				pBufPtr = goAndCopyNextWord(textureNameBuf, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
				if (type==1) {
					pBufPtr = goAndCopyNextWord(textureNameBuf, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
					pCurrMaterial->pMeshbuffer->Material.MaterialTypeParam=core::fast_atof(textureNameBuf);
					pBufPtr = goAndCopyNextWord(textureNameBuf, pBufPtr, WORD_BUFFER_LENGTH, pBufEnd);
				}

				video::ITexture * pTexture = 0;
				if (FileSystem->existFile(textureNameBuf))
					pTexture = Driver->getTexture( textureNameBuf );
				else
					// try to read in the relative path, the .obj is loaded from
					pTexture = Driver->getTexture( (relPath + textureNameBuf).c_str() );
				if ( 0 != pTexture )
				{
					if (type==0)
						pCurrMaterial->pMeshbuffer->Material.Texture1 = pTexture;
					else if (type==1) {
						Driver->makeNormalMapTexture(pTexture);
						pCurrMaterial->pMeshbuffer->Material.Texture2 = pTexture;
						pCurrMaterial->pMeshbuffer->Material.MaterialType=video::EMT_PARALLAX_MAP_SOLID;
					}
					else if (type==2) {
						pCurrMaterial->pMeshbuffer->Material.Texture1 = pTexture;
						pCurrMaterial->pMeshbuffer->Material.MaterialType=video::EMT_TRANSPARENT_ADD_COLOR;
					}
					// Set diffuse material colour to white so as not to affect texture colour
					// Because Maya set diffuse colour Kd to black when you use a diffuse colour map
					// But is this the right thing to do?
					pCurrMaterial->pMeshbuffer->Material.DiffuseColor.set(
						pCurrMaterial->pMeshbuffer->Material.DiffuseColor.getAlpha(), 255, 255, 255 );
				}
			}
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;
		case 'd':				// d - transparency
			if ( 0 != pCurrMaterial )
			{
				const s32 COLOR_BUFFER_LENGTH = 16;
				c8 dStr[COLOR_BUFFER_LENGTH];

				pBufPtr = goAndCopyNextWord(dStr, pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
				f32 dValue = core::fast_atof(dStr);

				pCurrMaterial->pMeshbuffer->Material.DiffuseColor.setAlpha( (s32)(dValue * 255) );
				if (dValue<1.0f)
					pCurrMaterial->pMeshbuffer->Material.MaterialType = irr::video::EMT_TRANSPARENT_VERTEX_ALPHA;
			}

			pBufPtr = goNextLine(pBufPtr, pBufEnd);
            break;

		case 'T':
			if ( 0 != pCurrMaterial )
			{
				switch ( pBufPtr[1] )
				{
				case 'f':		// Tf - Transmitivity
					const s32 COLOR_BUFFER_LENGTH = 16;
					c8 redStr[COLOR_BUFFER_LENGTH];
					c8 greenStr[COLOR_BUFFER_LENGTH];
					c8 blueStr[COLOR_BUFFER_LENGTH];

					pBufPtr = goAndCopyNextWord(redStr,   pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(greenStr, pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
					pBufPtr = goAndCopyNextWord(blueStr,  pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);

					f32 transparency = ( core::fast_atof(redStr) + core::fast_atof(greenStr) + core::fast_atof(blueStr) ) / 3;

					pCurrMaterial->pMeshbuffer->Material.DiffuseColor.setAlpha( (s32)(transparency * 255) );
					pCurrMaterial->pMeshbuffer->Material.MaterialType = irr::video::EMT_TRANSPARENT_VERTEX_ALPHA;
				}
			}

			pBufPtr = goNextLine(pBufPtr, pBufEnd);
            break;

        default:               // comments or not recognised
			// eat up rest of line
			pBufPtr = goNextLine(pBufPtr, pBufEnd);
			break;
		}	// end switch(pBufPtr[0])
	}	// end while (pBufPtr)

	// end of file. if there's an existing material, store it
	if ( 0 != pCurrMaterial )
	{
		materials.push_back( pCurrMaterial );
		pCurrMaterial = 0;
	}

	delete [] pBuf;
	pMtlReader->drop();
}

c8* COBJMeshFileLoader::readColor(c8* pBufPtr, video::SColor& color, const c8* pBufEnd)
{
	const s32 COLOR_BUFFER_LENGTH = 16;
	c8 colStr[COLOR_BUFFER_LENGTH];

	color.setAlpha(255);
	pBufPtr = goAndCopyNextWord(colStr,   pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
	color.setRed((s32)(core::fast_atof(colStr) * 255));
	pBufPtr = goAndCopyNextWord(colStr,   pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
	color.setGreen((s32)(core::fast_atof(colStr) * 255));
	pBufPtr = goAndCopyNextWord(colStr,   pBufPtr, COLOR_BUFFER_LENGTH, pBufEnd);
	color.setBlue((s32)(core::fast_atof(colStr) * 255));

	return goNextLine(pBufPtr, pBufEnd);
}


COBJMeshFileLoader::SObjMtl* COBJMeshFileLoader::findMtl(const c8* pMtlName)
{
	for (u32 i = 0; i < materials.size(); ++i)
	{
		if ( materials[i]->name == pMtlName )
			return materials[i];
	}
	return 0;
}



COBJMeshFileLoader::SObjGroup * COBJMeshFileLoader::findGroup(const c8* pGroupName)
{
	for (u32 i = 0; i < groups.size(); ++i)
	{
		if ( groups[i]->name == pGroupName )
			return groups[i];
	}
	return 0;
}


COBJMeshFileLoader::SObjGroup * COBJMeshFileLoader::findOrAddGroup(const c8* pGroupName)
{
	SObjGroup * pGroup = findGroup( pGroupName );
	if ( 0 != pGroup )
	{
		// group found, return it
		return pGroup;
	}
	// group not found, create a new group
	SObjGroup* group = new SObjGroup();
	group->name = pGroupName;
	groups.push_back(group);
	return group;
}


c8* COBJMeshFileLoader::goFirstWord(c8* buf, const c8* pBufEnd)
{
	s32 i = 0;
	// skip pass non-printable characters
	while(buf[i] && isspace(buf[i]))
	{
		if ( &(buf[i]) == pBufEnd )
		{
			// end of buffer check
			return 0;
		}
		++i;
	}

	return &buf[i];
}


c8* COBJMeshFileLoader::goNextWord(c8* buf, const c8* pBufEnd)
{
	if (!buf)
		return 0;

	s32 i = 0;
	// look for non-printable characters
	while(buf[i])
	{
		if ( &(buf[i]) == pBufEnd )
		{
			// end of buffer check
			return 0;
		}
		if (buf[i]==' ' || buf[i]=='\n' || buf[i]=='\r' || buf[i]=='\t')
			break;

		++i;
	}

	// look for printable character after non-printable character
	c8* nextWord = goFirstWord(&buf[i], pBufEnd);
	// if can't find, return null
	return nextWord == buf ? 0 : nextWord;
}


c8* COBJMeshFileLoader::goNextLine(c8* buf, const c8* pBufEnd)
{
	// look for newline characters
	while(*buf)
	{
		// end of buffer check
		if ( buf == pBufEnd )
			return 0;
		// found it, so leave
		if (*buf=='\n' || *buf=='\r')
			break;
		++buf;
	}
	while (isspace((u8)*++buf)) /* skip */ ;

	return buf;
}


void COBJMeshFileLoader::copyWord(c8* outBuf, const c8* inBuf, s32 outBufLength, const c8* pBufEnd)
{
	if (!inBuf)
		return;

	s32 i = 0;
	while(inBuf[i])
	{
		if (inBuf[i]==' ' || inBuf[i]=='\n' || inBuf[i]=='\r' || inBuf[i]=='\t' || &(inBuf[i]) == pBufEnd)
			break;
		++i;
	}

	for (s32 j=0; j<i && j<outBufLength-1; ++j)
		outBuf[j] = inBuf[j];

	outBuf[i] = 0x0;
}


void COBJMeshFileLoader::copyLine(c8* outBuf, const c8* inBuf, s32 outBufLength, const c8* pBufEnd)
{
	if (!inBuf)
		return;

	s32 i = 0;
	while(inBuf[i])
	{
		if (inBuf[i]=='\n' || inBuf[i]=='\r' || &(inBuf[i]) == pBufEnd)
			break;
		++i;
	}

	for (s32 j=0; j<i && j<outBufLength-1; ++j)
		outBuf[j] = inBuf[j];

	outBuf[i] = 0x0;
}


c8* COBJMeshFileLoader::goAndCopyNextWord(c8* outBuf, c8* inBuf, s32 outBufLength, const c8* pBufEnd)
{
	inBuf = goNextWord(inBuf, pBufEnd);
	copyWord(outBuf, inBuf, outBufLength, pBufEnd);
	return inBuf;
}


bool COBJMeshFileLoader::retrieveVertexIndices(c8* pVertexData, s32* pIdx, const c8* pBufEnd)
{
	c8 word[16] = "";
	c8* pChar = goFirstWord(pVertexData, pBufEnd);
	c8 idxType = 0;		// 0 = posIdx, 1 = texcoordIdx, 2 = normalIdx
	s32 index;

	u32 i = 0;
	while ( pChar != pBufEnd )
	{
		if ( (isdigit(*pChar)) || (*pChar == '-') )
		{
			// build up the number
			word[i++] = *pChar;
		}
		else if ( *pChar == '/' || *pChar == '\0' )
		{
			// number is completed. Convert and store it
			word[i] = '\0';
			// if no number was found index will become 0 and later on -1 by decrement
			index = atoi( word );
			if (index<0)
				index += pIdx[idxType];
			else
				--index;
			pIdx[idxType] = index;

			// reset the word
			word[0] = '\0';
			i = 0;

			// go to the next kind of index type
			if (*pChar == '/')
			{
				if ( ++idxType > 2 )
				{
					// error checking, shouldn't reach here unless file is wrong
					idxType = 0;
				}
			}
			else if (*pChar == '\0')
			{
				// set all missing values to disable (=-1)
				while (++idxType < 3)
					pIdx[idxType]=-1;
				++pChar;
				break; // while
			}
		}

		// go to the next char
		++pChar;
	}

	return true;
}


void COBJMeshFileLoader::cleanUp()
{
	u32 i;

	for (i = 0; i < materials.size(); ++i )
	{
		materials[i]->pMeshbuffer->drop();
		delete materials[i];
	}

	materials.clear();

	for (i = 0; i < groups.size(); ++i )
	{
		delete groups[i];
	}

	groups.clear();
}


} // end namespace scene
} // end namespace irr

