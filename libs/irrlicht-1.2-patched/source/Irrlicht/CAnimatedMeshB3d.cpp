// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

//B3D file loader by Luke Hoschke, File format by Mark Sibly

#include "CAnimatedMeshB3d.h"
#include "os.h"
#include "IVideoDriver.h"


namespace irr
{
namespace scene
{


struct B3dChunkHeader
{
	c8 name[4];
	s32 size;
};



//! constructor
CAnimatedMeshB3d::CAnimatedMeshB3d(video::IVideoDriver* driver)
: Driver(driver)
{
	if (Driver)
		Driver->grab();

}



//! destructor
CAnimatedMeshB3d::~CAnimatedMeshB3d()
{
	if (Driver)
		Driver->drop();
}



core::stringc CAnimatedMeshB3d::readString(io::IReadFile* file)
{
	core::stringc newstring;

	while (file->getPos() <= file->getSize() )
	{
		c8 character;
		file->read(&character,sizeof(character));

		if (character==0)
			return newstring;

		newstring.append(character);
	}
	return newstring;
}



core::stringc CAnimatedMeshB3d::stripPathString(core::string<c8> oldstring, bool keepPath)
{
	s32 lastA=oldstring.findLast('/'); // forward slash
	s32 lastB=oldstring.findLast('\\'); // back slash

	if (keepPath==false)
		if (lastA==-1 && lastB==-1) return oldstring;
	else
		if (lastA==-1 && lastB==-1) return core::stringc();


	if (lastA>lastB)
	{
		if (keepPath==false)
			return oldstring.subString(lastA+1,oldstring.size()-(lastA+1));
		else
			return oldstring.subString(0,lastA+1);
	}
	else
	{
		if (keepPath==false)
			return oldstring.subString(lastB+1,oldstring.size()-(lastB+1));
		else
			return oldstring.subString(0,lastB+1);
	}
}



void CAnimatedMeshB3d::readFloats(io::IReadFile* file, f32* vec, u32 count)
{
	file->read(vec, count*sizeof(f32));
	#ifdef __BIG_ENDIAN__
	for (u32 n=0; n<count; ++n)
		vec[n] = os::Byteswap::byteswap(vec[n]);
	#endif
}



bool CAnimatedMeshB3d::ReadChunkTEXS(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize)
{

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		core::stringc TextureName=readString(file);

		TextureName=stripPathString(file->getFileName(),true) + stripPathString(TextureName,false);

		SB3dTexture B3dTexture;

		B3dTexture.Texture=Driver->getTexture ( TextureName.c_str() );

		file->read(&B3dTexture.Flags, sizeof(s32));
		file->read(&B3dTexture.Blend, sizeof(s32));
		readFloats(file, &B3dTexture.Xpos, 1);
		readFloats(file, &B3dTexture.Ypos, 1);
		readFloats(file, &B3dTexture.Xscale, 1);
		readFloats(file, &B3dTexture.Yscale, 1);
		readFloats(file, &B3dTexture.Angle, 1);

		#ifdef __BIG_ENDIAN__
			B3dTexture.Flags = os::Byteswap::byteswap(B3dTexture.Flags);
			B3dTexture.Blend = os::Byteswap::byteswap(B3dTexture.Blend);
		#endif

		Textures.push_back(B3dTexture);

		/*
		Flags:
		1: Color (default)
		2: Alpha
		4: Masked
		8: Mipmapped
		16: Clamp U
		32: Clamp V
		64: Spherical environment map
		128: Cubic environment map
		256: Store texture in vram
		512: Force the use of high color textures
		65536: texture uses secondary UV values

		Blend:
		0: Do not blend
		1: No blend, or Alpha (alpha when texture loaded with alpha flag - not recommended for multitexturing - see below)
		2: Multiply (default)
		3: Add
		4: Dot3
		5: Multiply 2

		*/

	}

	--B3dStackSize;

	return true;
}

bool CAnimatedMeshB3d::ReadChunkBRUS(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize)
{

	s32 n_texs;

	file->read(&n_texs, sizeof(s32));

	#ifdef __BIG_ENDIAN__
		n_texs = os::Byteswap::byteswap(n_texs);
	#endif

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		// This is what blitz basic calls a brush, like a Irrlicht Material
		core::stringc MaterialName=readString(file); //Not used but we still need the read it

		SB3dMaterial B3dMaterial;

		B3dMaterial.Material=new irr::video::SMaterial();

		B3dMaterial.Textures[0]=0;
		B3dMaterial.Textures[1]=0;

		s32 texture_id[8];
		texture_id[0]=-1;
		texture_id[1]=-1;

		file->read(&B3dMaterial.red, sizeof(B3dMaterial.red));
		file->read(&B3dMaterial.green, sizeof(B3dMaterial.green));
		file->read(&B3dMaterial.blue, sizeof(B3dMaterial.blue));
		file->read(&B3dMaterial.alpha, sizeof(B3dMaterial.alpha));
		file->read(&B3dMaterial.shininess, sizeof(B3dMaterial.shininess));
		file->read(&B3dMaterial.blend, sizeof(B3dMaterial.blend));
		file->read(&B3dMaterial.fx, sizeof(B3dMaterial.fx));

		for (s32 n=0;n<=(n_texs-1);n++)
		{
			file->read(&texture_id[n], sizeof(s32)); //I'm not sure of getting the sizeof an array
			//cout << "Material is using texture id:"<< texture_id[n] <<endl;//for debuging
		}

		#ifdef __BIG_ENDIAN__
			B3dMaterial.red = os::Byteswap::byteswap(B3dMaterial.red);
			B3dMaterial.green = os::Byteswap::byteswap(B3dMaterial.green);
			B3dMaterial.blue = os::Byteswap::byteswap(B3dMaterial.blue);
			B3dMaterial.alpha = os::Byteswap::byteswap(B3dMaterial.alpha);
			B3dMaterial.shininess = os::Byteswap::byteswap(B3dMaterial.shininess);
			B3dMaterial.blend = os::Byteswap::byteswap(B3dMaterial.blend);
			B3dMaterial.fx = os::Byteswap::byteswap(B3dMaterial.fx);

			for (s32 n=0;n<=(n_texs-1);n++)
				texture_id[n] = os::Byteswap::byteswap(texture_id[n]);
		#endif

		if (texture_id[0]!=-1)
			B3dMaterial.Textures[0]=&Textures[texture_id[0]];
		if (texture_id[1]!=-1)
			B3dMaterial.Textures[1]=&Textures[texture_id[1]];

		//Hack, Fixes problems when the lightmap is on the first texture
		if (texture_id[0]!=-1)
		if (Textures[texture_id[0]].Flags &65536) // 65536 = secondary UV
		{
			SB3dTexture *TmpTexture;
			TmpTexture=B3dMaterial.Textures[1];
			B3dMaterial.Textures[1]=B3dMaterial.Textures[0];
			B3dMaterial.Textures[0]=TmpTexture;
		} 

		if (B3dMaterial.Textures[0]!=0)
			B3dMaterial.Material->Texture1 = B3dMaterial.Textures[0]->Texture;
		if (B3dMaterial.Textures[1]!=0)
			B3dMaterial.Material->Texture2 = B3dMaterial.Textures[1]->Texture;

		//the other textures are skipped, irrlicht I think can only have 2 Textures per Material

		if (B3dMaterial.Textures[1]!=0 && B3dMaterial.Textures[0]==0) //It could happen
		{
			B3dMaterial.Textures[0]=B3dMaterial.Textures[1];
			B3dMaterial.Textures[1]=0;
		}

		//Hacky code to convert blitz fx to irrlicht...
		if (B3dMaterial.Textures[1]) //Two textures
			{
				if (B3dMaterial.alpha==1)
				{
					if (B3dMaterial.Textures[1]->Blend &5)
					{
						//B3dMaterial.Material->MaterialType = video::EMT_LIGHTMAP;
						B3dMaterial.Material->MaterialType = video::EMT_LIGHTMAP_M2 ;
					}
					else
					{
						B3dMaterial.Material->MaterialType = video::EMT_LIGHTMAP;
					}
				}
				else
				{
					//B3dMaterial.Material->MaterialType = video::EMT_LIGHTMAP;
					//B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL;
					//B3dMaterial.Material->MaterialTypeParam=B3dMaterial.alpha;
					B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_VERTEX_ALPHA;
				}
			}
		else if (B3dMaterial.Textures[0]) //one texture
			{
				if (B3dMaterial.Textures[0]->Flags &2) //Alpha mapped
				{
					B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL;
				}
				else if (B3dMaterial.Textures[0]->Flags &4) //Masked
				{
					//Not working like blitz basic, because Irrlicht is using alpha to mask, not colour like blitz basic

					B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL_REF;

				}
				else if (B3dMaterial.alpha==1)
				{
					B3dMaterial.Material->MaterialType = video::EMT_SOLID;
				}
				else
				{
					//B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_ALPHA_CHANNEL;
					//B3dMaterial.Material->MaterialTypeParam=B3dMaterial.alpha;
					B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_VERTEX_ALPHA;
				}
			}
		else //No texture
		{
			if (B3dMaterial.alpha==1)
			{
				B3dMaterial.Material->MaterialType = video::EMT_SOLID;
			}
			else
			{
				B3dMaterial.Material->MaterialType = video::EMT_TRANSPARENT_VERTEX_ALPHA;
			}
		}

		//Material fx...

		if (B3dMaterial.fx &1) //full-bright
		{
			//B3dMaterial.Material->AmbientColor = video::SColorf(1, 1, 1, 1).toSColor ();
			B3dMaterial.Material->AmbientColor = video::SColorf(1, 1, 1, 1).toSColor ();
			//B3dMaterial.Material->EmissiveColor = video::SColorf(1, 1, 1, 0).toSColor ();//Would be too bright and brighter than blitz basic
			B3dMaterial.Material->Lighting = false;
		}
		else
		{
			B3dMaterial.Material->AmbientColor = video::SColorf(B3dMaterial.red, B3dMaterial.green, B3dMaterial.blue, B3dMaterial.alpha).toSColor ();
		}

		if (B3dMaterial.fx &4) //flatshaded
			B3dMaterial.Material->GouraudShading=false;

		if (B3dMaterial.fx &16) //disable backface culling
			B3dMaterial.Material->BackfaceCulling=false;


		B3dMaterial.Material->DiffuseColor = video::SColorf(B3dMaterial.red, B3dMaterial.green, B3dMaterial.blue, B3dMaterial.alpha).toSColor ();
		B3dMaterial.Material->EmissiveColor = video::SColorf(0.5, 0.5, 0.5, 0).toSColor ();//Thoughts, I'm no sure what I should set it to?
		//B3dMaterial.Material->SpecularColor = video::SColorf(0, 0, 0, 0).toSColor (); //?
		B3dMaterial.Material->Shininess = B3dMaterial.shininess;

		Materials.push_back(B3dMaterial);
	}

	--B3dStackSize;

	return true;
}



bool CAnimatedMeshB3d::ReadChunkMESH(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode)
{

	s32 Vertices_Start=Vertices.size(); //B3Ds have Vertex ID's local within the mesh I don't want this

	s32 brush_id;

	file->read(&brush_id, sizeof(brush_id));

	#ifdef __BIG_ENDIAN__
		brush_id = os::Byteswap::byteswap(brush_id);
	#endif

	NormalsInFile=false;

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{

		++B3dStackSize;

		if (B3dStackSize>255) //stacks should not get this big, should they?
		{
			os::Printer::log("Stack overflow. Loading failed", file->getFileName(), ELL_ERROR);
			return false;
		}

		B3dChunkHeader header;
		file->read(&header, sizeof(header));

		#ifdef __BIG_ENDIAN__
			header.size = os::Byteswap::byteswap(header.size);
		#endif

		B3dStack[B3dStackSize].name[0]=header.name[0];
		B3dStack[B3dStackSize].name[1]=header.name[1]; //Not sure of an easier way
		B3dStack[B3dStackSize].name[2]=header.name[2];
		B3dStack[B3dStackSize].name[3]=header.name[3];

		B3dStack[B3dStackSize].length=header.size+8;

		B3dStack[B3dStackSize].startposition=file->getPos()-8;

		bool read=false;

		if ( strncmp( B3dStack[B3dStackSize].name, "VRTS", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkVRTS(file, B3dStack, B3dStackSize,InNode ,0, Vertices_Start)==false)
					return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "TRIS", 4 ) == 0 )
		{
			read=true;

			SB3DMeshBuffer *MeshBuffer = new SB3DMeshBuffer();

			if (brush_id!=-1)
				MeshBuffer->Material=(*Materials[brush_id].Material);

			if(ReadChunkTRIS(file, B3dStack, B3dStackSize,InNode ,MeshBuffer, Vertices_Start)==false)
				return false;

			MeshBuffer->recalculateBoundingBox();

			if (NormalsInFile==false && MeshBuffer->Material.Lighting != false) //No point wasting time of lightmapped levels
			{
					s32 i;

					for ( i=0; i<(s32)MeshBuffer->Indices.size(); i+=3)
					{
						core::plane3d<f32> p(	MeshBuffer->Vertices[MeshBuffer->Indices[i+0]].Pos,
												MeshBuffer->Vertices[MeshBuffer->Indices[i+1]].Pos,
												MeshBuffer->Vertices[MeshBuffer->Indices[i+2]].Pos);

						MeshBuffer->Vertices[MeshBuffer->Indices[i+0]].Normal += p.Normal;
						MeshBuffer->Vertices[MeshBuffer->Indices[i+1]].Normal += p.Normal;
						MeshBuffer->Vertices[MeshBuffer->Indices[i+2]].Normal += p.Normal;
					}

					for ( i = 0; i<(s32)MeshBuffer->Vertices.size(); ++i )
					{
						MeshBuffer->Vertices[i].Normal.normalize ();
					}
			}

			Buffers.push_back(MeshBuffer);

		}

		if (read==false)
		{
			os::Printer::log("Unknown chunk found in mesh - skipping");

			file->seek( (B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length) , false);


			--B3dStackSize;
		}
	}

	--B3dStackSize;

	return true;
}

/*
VRTS:
  int flags                   ;1=normal values present, 2=rgba values present
  int tex_coord_sets          ;texture coords per vertex (eg: 1 for simple U/V) max=8
  int tex_coord_set_size      ;components per set (eg: 2 for simple U/V) max=4
  {
  float x,y,z                 ;always present
  float nx,ny,nz              ;vertex normal: present if (flags&1)
  float red,green,blue,alpha  ;vertex color: present if (flags&2)
  float tex_coords[tex_coord_sets][tex_coord_set_size]	;tex coords
  }
*/

bool CAnimatedMeshB3d::ReadChunkVRTS(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode, SB3DMeshBuffer *MeshBuffer,s32 Vertices_Start)
{

	s32 flags, tex_coord_sets, tex_coord_set_size;

	file->read(&flags, sizeof(flags));
	file->read(&tex_coord_sets, sizeof(tex_coord_sets));
	file->read(&tex_coord_set_size, sizeof(tex_coord_set_size));

	#ifdef __BIG_ENDIAN__
		flags = os::Byteswap::byteswap(flags);
		tex_coord_sets = os::Byteswap::byteswap(tex_coord_sets);
		tex_coord_set_size = os::Byteswap::byteswap(tex_coord_set_size);
	#endif

	if (tex_coord_sets>=3 || tex_coord_set_size>=4)//Something is wrong
	{
		os::Printer::log("tex_coord_sets or tex_coord_set_size too big", file->getFileName(), ELL_ERROR);
		return false;
	}

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		f32 x=0.0f, y=0.0f ,z=0.0f;
		f32 nx=0.0f, ny=0.0f, nz=0.0f;
		f32 red=1.0f, green=1.0f, blue=1.0f, alpha=1.0f;
		f32 tex_coords[3][4];

		file->read(&x, sizeof(x));
		file->read(&y, sizeof(y));
		file->read(&z, sizeof(z));

		if (flags&1)
		{
			NormalsInFile=true;
			file->read(&nx, sizeof(nx));
			file->read(&ny, sizeof(ny));
			file->read(&nz, sizeof(nz));
		}

		if (flags&2)
		{
			file->read(&red, sizeof(red));
			file->read(&green, sizeof(green));
			file->read(&blue, sizeof(blue));
			file->read(&alpha, sizeof(alpha));
		}

		for (s32 i=0;i<tex_coord_sets;++i)
			for (s32 j=0;j<tex_coord_set_size;++j)
				file->read(&tex_coords[i][j], sizeof(f32));

		#ifdef __BIG_ENDIAN__
			x = os::Byteswap::byteswap(x);
			y = os::Byteswap::byteswap(y);
			z = os::Byteswap::byteswap(z);

			if (flags&1)
			{
				nx = os::Byteswap::byteswap(nx);
				ny = os::Byteswap::byteswap(ny);
				nz = os::Byteswap::byteswap(nz);
			}

			if (flags&2)
			{
				red = os::Byteswap::byteswap(red);
				green = os::Byteswap::byteswap(green);
				blue = os::Byteswap::byteswap(blue);
				alpha = os::Byteswap::byteswap(alpha);
			}

			for (s32 i=0;i<=tex_coord_sets-1;i++)
				for (s32 j=0;j<=tex_coord_set_size-1;j++)
					tex_coords[i][j] = os::Byteswap::byteswap(tex_coords[i][j]);
		#endif

		f32 tu=0.0f, tv=0.0f;

		if (tex_coord_sets>=1 && tex_coord_set_size>=2)
		{
			tu=tex_coords[0][0];
			tv=tex_coords[0][1];
		}

		f32 tu2=0.0f, tv2=0.0f;

		if (tex_coord_sets>=2 && tex_coord_set_size>=2)
		{
			tu2=tex_coords[1][0];
			tv2=tex_coords[1][1];
		}

		//Create Vertex...
		video::S3DVertex2TCoords *Vertex=new video::S3DVertex2TCoords
					(x, y, z, video::SColorf(red, green, blue, alpha).toSColor(), tu, tv, tu2, tv2);

		Vertex->Normal=core::vector3df(nx, ny, nz);//should this be effected by the Node's Global Matrix (eg rotation)?

		//Transform the Vertex position by nested node...
		core::matrix4 VertexMatrix;
		VertexMatrix.setTranslation(Vertex->Pos);

		Vertices_GlobalMatrix.push_back(VertexMatrix);

		VertexMatrix=InNode->GlobalMatrix*VertexMatrix;

		Vertex->Pos=VertexMatrix.getTranslation();

		//Add it...
		Vertices.push_back(Vertex);

		Vertices_Moved.push_back(false);

		AnimatedVertices_VertexID.push_back(-1);
		AnimatedVertices_MeshBuffer.push_back(0);
	}

	--B3dStackSize;

	return true;
}



bool CAnimatedMeshB3d::ReadChunkTRIS(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode, SB3DMeshBuffer *MeshBuffer,s32 Vertices_Start)
{

	s32 triangle_brush_id; //irrlicht can't have different brushes for each triangle (I'm using a workaround)

	file->read(&triangle_brush_id, sizeof(triangle_brush_id));

	#ifdef __BIG_ENDIAN__
		triangle_brush_id = os::Byteswap::byteswap(triangle_brush_id);
	#endif

	SB3dMaterial *B3dMaterial;

	if (triangle_brush_id!=-1)
		B3dMaterial=&Materials[triangle_brush_id];
	else
		B3dMaterial=0;

	if (B3dMaterial)
		MeshBuffer->Material=(*B3dMaterial->Material);

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		s32 vertex_id[3];

		file->read(&vertex_id[0], sizeof(s32));
		file->read(&vertex_id[1], sizeof(s32));
		file->read(&vertex_id[2], sizeof(s32));

		#ifdef __BIG_ENDIAN__
			vertex_id[0] = os::Byteswap::byteswap(vertex_id[0]);
			vertex_id[1] = os::Byteswap::byteswap(vertex_id[1]);
			vertex_id[2] = os::Byteswap::byteswap(vertex_id[2]);
		#endif


		vertex_id[0]+=Vertices_Start;
		vertex_id[1]+=Vertices_Start;
		vertex_id[2]+=Vertices_Start;

		for(s32 i=0;i<3;i++)
		{
			if (AnimatedVertices_VertexID[ vertex_id[i] ]==-1)
			{
				MeshBuffer->Vertices.push_back(*Vertices[vertex_id[i]] );
				AnimatedVertices_VertexID[ vertex_id[i] ]=MeshBuffer->Vertices.size()-1;
				AnimatedVertices_MeshBuffer[ vertex_id[i] ]=MeshBuffer;

				//Apply Material...
				irr::video::S3DVertex2TCoords *Vertex=&MeshBuffer->Vertices[MeshBuffer->Vertices.size()-1];

				if (B3dMaterial) // Fixes crashes when mesh has no material
				Vertex->Color.setAlpha( (s32)(B3dMaterial->alpha*255.0) );

			}
		}


		MeshBuffer->Indices.push_back( AnimatedVertices_VertexID[ vertex_id[0] ] );
		MeshBuffer->Indices.push_back( AnimatedVertices_VertexID[ vertex_id[1] ] );
		MeshBuffer->Indices.push_back( AnimatedVertices_VertexID[ vertex_id[2] ] );
	}

	--B3dStackSize;

	return true;
}



bool CAnimatedMeshB3d::ReadChunkNODE(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode)
{
	core::stringc NodeName=readString(file);

	f32 position[3];
	f32 scale[3];
	f32 rotation[4];

	s32 n;

	for (n=0; n<=2; n++)
		file->read(&position[n], sizeof(f32));

	for (n=0; n<=2; n++)
		file->read(&scale[n], sizeof(f32));

	for (n=0; n<=3; n++)
		file->read(&rotation[n], sizeof(f32));

	#ifdef __BIG_ENDIAN__
		for (n=0; n<=2; n++)
			position[n] = os::Byteswap::byteswap(position[n]);

		for (n=0; n<=2; n++)
			scale[n] = os::Byteswap::byteswap(scale[n]);

		for (n=0; n<=4; n++)
			rotation[n] = os::Byteswap::byteswap(rotation[n]);
	#endif

	SB3dNode *Node=new SB3dNode();

	Node->Name=NodeName;

	Node->Animate=true;

	Node->position=core::vector3df(position[0],position[1],position[2]);

	Node->scale=core::vector3df(scale[0],scale[1],scale[2]);

	Node->rotation=core::quaternion(rotation[1],rotation[2],rotation[3],rotation[0]);//meant to be in this order

	irr::core::matrix4 positionMatrix;
	positionMatrix.setTranslation(Node->position);

	irr::core::matrix4 scaleMatrix;
	scaleMatrix.setScale(Node->scale);

	irr::core::matrix4 rotationMatrix=Node->rotation.getMatrix();

	Node->LocalMatrix=positionMatrix*rotationMatrix*scaleMatrix;

	if (InNode==0)
	{
		Node->GlobalMatrix=Node->LocalMatrix;
		RootNodes.push_back(Node);
	}
	else
	{
		Node->GlobalMatrix=InNode->GlobalMatrix*Node->LocalMatrix;
	}

	Node->GlobalInversedMatrix=Node->GlobalMatrix;
	Node->GlobalInversedMatrix.makeInverse(); //slow

	Nodes.push_back(Node);


	if (InNode!=0)
		InNode->Nodes.push_back(Node);

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		++B3dStackSize;

		if (B3dStackSize>255) //stacks should not get this big, should they?
		{
			os::Printer::log("Stack overflow. Loading failed", file->getFileName(), ELL_ERROR);
			return false;
		}

		B3dChunkHeader header;
		file->read(&header, sizeof(header));

		#ifdef __BIG_ENDIAN__
			header.size = os::Byteswap::byteswap(header.size);
		#endif

		B3dStack[B3dStackSize].name[0]=header.name[0];
		B3dStack[B3dStackSize].name[1]=header.name[1]; //Not sure of an easier way
		B3dStack[B3dStackSize].name[2]=header.name[2];
		B3dStack[B3dStackSize].name[3]=header.name[3];

		B3dStack[B3dStackSize].length=header.size+8;

		B3dStack[B3dStackSize].startposition=file->getPos()-8;

		bool read=false;

		if ( strncmp( B3dStack[B3dStackSize].name, "NODE", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkNODE(file, B3dStack, B3dStackSize,Node)==false)
					return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "MESH", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkMESH(file, B3dStack, B3dStackSize,Node)==false)
					return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "ANIM", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkANIM(file, B3dStack, B3dStackSize,Node)==false)
					return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "BONE", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkBONE(file, B3dStack, B3dStackSize,Node)==false)
					return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "KEYS", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkKEYS(file, B3dStack, B3dStackSize,Node)==false)
					return false;
		}

		if (read==false)
		{
			os::Printer::log("Unknown chunk found in node - skipping");

			file->seek( (B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length) , false);

			--B3dStackSize;
		}
	}

	--B3dStackSize;

	return true;
}



bool CAnimatedMeshB3d::ReadChunkBONE(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode)
{

	if (B3dStack[B3dStackSize].length>8)
	{
		while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
		{
			SB3dBone Bone;

			file->read(&Bone.vertex_id, sizeof(Bone.vertex_id));
			file->read(&Bone.weight, sizeof(Bone.weight));

			#ifdef __BIG_ENDIAN__
				Bone.vertex_id = os::Byteswap::byteswap(Bone.vertex_id);
				Bone.weight = os::Byteswap::byteswap(Bone.weight);
			#endif

			InNode->Bones.push_back(Bone);
		}
	}

	--B3dStackSize;
	return true;
}



bool CAnimatedMeshB3d::ReadChunkKEYS(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode)
{

	s32 flags;
	file->read(&flags, sizeof(flags));
	#ifdef __BIG_ENDIAN__
		flags = os::Byteswap::byteswap(flags);
	#endif

	while(B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos()) //this chunk repeats
	{
		SB3dKey Key;

		Key.flags=flags;

		f32 position[3];
		f32 scale[3];
		f32 rotation[4];

		file->read(&Key.frame, sizeof(Key.frame));

		if (flags&1)
			readFloats(file, position, 3);

		if (flags&2)
			readFloats(file, scale, 3);

		if (flags&4)
			readFloats(file, rotation, 4);

		#ifdef __BIG_ENDIAN__
		Key.frame = os::Byteswap::byteswap(Key.frame);
		#endif

		Key.frame*=100;

		Key.position=core::vector3df(position[0],position[1],position[2]);

		Key.scale=core::vector3df(scale[0],scale[1],scale[2]);

		Key.rotation=core::quaternion(rotation[1],rotation[2],rotation[3],rotation[0]);//meant to be in this order

		InNode->Keys.push_back(Key);
	}

	--B3dStackSize;
	return true;
}



bool CAnimatedMeshB3d::ReadChunkANIM(io::IReadFile* file, B3dChunk *B3dStack, s16 &B3dStackSize, SB3dNode *InNode)
{

	file->read(&AnimFlags, sizeof(s32));
	file->read(&AnimFrames, sizeof(s32));
	readFloats(file, &AnimFPS, 1);

	#ifdef __BIG_ENDIAN__
		AnimFlags = os::Byteswap::byteswap(AnimFlags);
		AnimFrames = os::Byteswap::byteswap(AnimFrames);
	#endif

	AnimFrames*=100;

	totalTime=(f32)AnimFrames;
	HasAnimation=1;
	lastCalculatedFrame=-1;

	--B3dStackSize;
	return true;
}



bool CAnimatedMeshB3d::loadFile(io::IReadFile* file)
{

	if (!file)
		return false;

	totalTime=0;
	HasAnimation=0;
	lastCalculatedFrame=-1;

	AnimFlags=0; //Unused for now
	AnimFrames=1; //how many frames in anim
	AnimFPS=0.0f;

	InterpolationMode=1; //Set linear interpolation animation
	AnimateMode=3; //Update both the nodes and the skin in animation

	B3dChunkHeader header;

	file->read(&header, sizeof(header));

	#ifdef __BIG_ENDIAN__
		header.size = os::Byteswap::byteswap(header.size);
	#endif

	if ( strncmp( header.name, "BB3D", 4 ) != 0 )
	{
		os::Printer::log("File is not a b3d file. Loading failed", file->getFileName(), ELL_ERROR);
		return false;
	}

	//header Chunk size here


	B3dChunk B3dStack[255];		//should have used an irrlicht array, can someone change it

	s16 B3dStackSize=0; //starting at 0

	B3dStack[0].name[0]=header.name[0];
	B3dStack[0].name[1]=header.name[1]; //Not sure of an easier way
	B3dStack[0].name[2]=header.name[2];
	B3dStack[0].name[3]=header.name[3];

	B3dStack[B3dStackSize].startposition=file->getPos()-8;

	B3dStack[B3dStackSize].length=header.size+8;

	//Get file version...

	u32 FileVersion;
	file->read(&FileVersion, sizeof(FileVersion));
	#ifdef __BIG_ENDIAN__
		FileVersion = os::Byteswap::byteswap(FileVersion);
	#endif

	while (B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length>file->getPos())
	{
		++B3dStackSize;

		if (B3dStackSize>255) //stacks should not get this big, should they?
		{
			os::Printer::log("Stack overflow. Loading failed", file->getFileName(), ELL_ERROR);
			return false;
		}

		file->read(&header, sizeof(header));

		#ifdef __BIG_ENDIAN__
			header.size = os::Byteswap::byteswap(header.size);
		#endif

		B3dStack[B3dStackSize].name[0]=header.name[0];
		B3dStack[B3dStackSize].name[1]=header.name[1]; //Not sure of an easier way
		B3dStack[B3dStackSize].name[2]=header.name[2];
		B3dStack[B3dStackSize].name[3]=header.name[3];

		B3dStack[B3dStackSize].startposition=file->getPos()-8;

		B3dStack[B3dStackSize].length=header.size+8;

		bool read=false;

		if ( strncmp( B3dStack[B3dStackSize].name, "TEXS", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkTEXS(file, B3dStack, B3dStackSize)==false)
				return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "BRUS", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkBRUS(file, B3dStack, B3dStackSize)==false)
				return false;
		}
		else if ( strncmp( B3dStack[B3dStackSize].name, "NODE", 4 ) == 0 )
		{
			read=true;
			if(ReadChunkNODE(file, B3dStack, B3dStackSize,(SB3dNode*)0)==false)
				return false;
		}

		if (read==false)
		{
			os::Printer::log("Unknown chunk found in mesh base - skipping");

			file->seek( (B3dStack[B3dStackSize].startposition + B3dStack[B3dStackSize].length) , false);

			--B3dStackSize;
		}
	}


	//Get BoundingBox...
	if (Buffers.empty())
		BoundingBox.reset(0,0,0);
	else
	{
		BoundingBox.reset(Buffers[0]->BoundingBox.MaxEdge);
		for (s32 i=0; i<(s32)Buffers.size(); ++i)
		{
			BoundingBox.addInternalPoint(Buffers[i]->BoundingBox.MaxEdge);
			BoundingBox.addInternalPoint(Buffers[i]->BoundingBox.MinEdge);
		}
	}


	return true;
}



//! returns the amount of frames in milliseconds. If the amount is 1, it is a static (=non animated) mesh.
s32 CAnimatedMeshB3d::getFrameCount()
{
	return AnimFrames;
}



//! returns the animated mesh based on a detail level. 0 is the lowest, 255 the highest detail. Note, that some Meshes will ignore the detail level.
IMesh* CAnimatedMeshB3d::getMesh(s32 frame, s32 detailLevel, s32 startFrameLoop, s32 endFrameLoop)
{
	animate(frame,startFrameLoop, endFrameLoop);
	return this;
}



//! Returns a pointer to a transformation matrix of a part of the
//! mesh based on a frame time.
core::matrix4* CAnimatedMeshB3d::getMatrixOfJoint(s32 jointNumber, s32 frame)
{
	if (!HasAnimation || jointNumber < 0 || jointNumber >= (s32)Nodes.size())
		return 0;

	return &Nodes[jointNumber]->GlobalAnimatedMatrix;
}

core::matrix4* CAnimatedMeshB3d::getLocalMatrixOfJoint(s32 jointNumber)
{
	if (!HasAnimation || jointNumber < 0 || jointNumber >= (s32)Nodes.size())
		return 0;

	return &Nodes[jointNumber]->LocalMatrix;
}


core::matrix4* CAnimatedMeshB3d::getMatrixOfJointUnanimated(s32 jointNumber)
{
	if (!HasAnimation || jointNumber < 0 || jointNumber >= (s32)Nodes.size())
		return 0;

	return &Nodes[jointNumber]->GlobalMatrix;
}

//! Gets joint count.
s32 CAnimatedMeshB3d::getJointCount() const
{
	return Nodes.size();
}


//! Gets the name of a joint.
void CAnimatedMeshB3d::setJointAnimation(s32 jointNumber, bool On)
{
	if (jointNumber < 0 || jointNumber >= (s32)Nodes.size())
		return;
	Nodes[jointNumber]->Animate=On;
}


//! Gets the name of a joint.
const c8* CAnimatedMeshB3d::getJointName(s32 number) const
{
	if (number < 0 || number >= (s32)Nodes.size())
		return 0;
	return Nodes[number]->Name.c_str();
}



//! Gets a joint number from its name
s32 CAnimatedMeshB3d::getJointNumber(const c8* name) const
{
	for (s32 i=0; i<(s32)Nodes.size(); ++i)
		if (Nodes[i]->Name == name)
			return i;

	return -1;
}


void CAnimatedMeshB3d::CalculateGlobalMatrixes(SB3dNode *Node,SB3dNode *ParentNode)
{
	if (Node==0 && ParentNode!=0)//bit of protection from endless loops
		return;

	if (Node==0)
	{
		for (s32 i=0; i<(s32)RootNodes.size(); ++i)
			CalculateGlobalMatrixes(RootNodes[i],0);
		return;
	}

	if (ParentNode==0)
		Node->GlobalAnimatedMatrix=Node->LocalAnimatedMatrix;
	else
		Node->GlobalAnimatedMatrix=ParentNode->GlobalAnimatedMatrix * Node->LocalAnimatedMatrix;

	for (s32 j=0; j<(s32)Node->Nodes.size(); ++j)
		CalculateGlobalMatrixes(Node->Nodes[j],Node);
}



void CAnimatedMeshB3d::animateSkin(f32 frame,f32 startFrame, f32 endFrame,SB3dNode *Node,SB3dNode *ParentNode)
{
	// Get animated matrix...
	if (ParentNode==0)
		Node->GlobalAnimatedMatrix=Node->LocalAnimatedMatrix;
	else
		Node->GlobalAnimatedMatrix=ParentNode->GlobalAnimatedMatrix * Node->LocalAnimatedMatrix;

	core::matrix4 VerticesMatrixMove= Node->GlobalAnimatedMatrix * Node->GlobalInversedMatrix ;

	for (s32 i=0; i<(s32)Node->Bones.size(); ++i)
	{
		u16 VertexID = AnimatedVertices_VertexID[ Node->Bones[i].vertex_id ];

		SB3DMeshBuffer *MeshBuffer=AnimatedVertices_MeshBuffer[ Node->Bones[i].vertex_id ];

		video::S3DVertex2TCoords *Vertex=&MeshBuffer->Vertices[VertexID];

		core::matrix4 VertexMatrixMove=VerticesMatrixMove;

		VertexMatrixMove*=Vertices_GlobalMatrix[ Node->Bones[i].vertex_id ];

		core::vector3df GlobalAnimatedVertexVector=VertexMatrixMove.getTranslation();

		if (Vertices_Moved[Node->Bones[i].vertex_id]==false)
		{
			Vertices_Moved[Node->Bones[i].vertex_id]=true;

			Vertex->Pos.X=GlobalAnimatedVertexVector.X*Node->Bones[i].weight;
			Vertex->Pos.Y=GlobalAnimatedVertexVector.Y*Node->Bones[i].weight;
			Vertex->Pos.Z=GlobalAnimatedVertexVector.Z*Node->Bones[i].weight;
		}
		else
		{
			Vertex->Pos.X=Vertex->Pos.X+ GlobalAnimatedVertexVector.X*Node->Bones[i].weight;
			Vertex->Pos.Y=Vertex->Pos.Y+ GlobalAnimatedVertexVector.Y*Node->Bones[i].weight;
			Vertex->Pos.Z=Vertex->Pos.Z+ GlobalAnimatedVertexVector.Z*Node->Bones[i].weight;
		}
	}

	for (s32 j=0; j<(s32)Node->Nodes.size(); ++j)
		animateSkin(frame,startFrame, endFrame,Node->Nodes[j],Node);

}


void CAnimatedMeshB3d::resetSkin()
{
	s32 i;

	for (i=0; i<(s32)Vertices_Moved.size(); ++i)
		Vertices_Moved[i]=false;


	for (i=0; i<(s32)Nodes.size(); ++i)
	{
		SB3dNode *Node=Nodes[i];
		for (s32 j=0; j<(s32)Node->Bones.size(); ++j)
		{
			u16 VertexID = AnimatedVertices_VertexID[ Node->Bones[j].vertex_id ];
			SB3DMeshBuffer *MeshBuffer=AnimatedVertices_MeshBuffer[ Node->Bones[j].vertex_id ];
			video::S3DVertex2TCoords *Vertex=&MeshBuffer->Vertices[VertexID];
			Vertex->Pos=Vertices[Node->Bones[j].vertex_id]->Pos;
		}
	}

}




void CAnimatedMeshB3d::getNodeAnimation(f32 frame,SB3dNode *Node,core::vector3df &position, core::vector3df &scale, core::quaternion &rotation)
{

			bool foundPosition=false;
			bool foundScale=false;
			bool foundRotation=false;

			s32 LastPositionKeyID=-1;
			s32 LastScaleKeyID=-1;
			s32 LastRotationKeyID=-1;

			for (s32 j=0; j<(s32)Node->Keys.size(); j++) //Finding the right key could use a speed boast
			{
				SB3dKey Key=Node->Keys[j];

				if (foundPosition==false && Key.flags&1)
					if (Key.frame>=frame)
				{
					foundPosition=true;

					if (LastPositionKeyID==-1)
					{
						position=Key.position;
					}
					else
					{
						if (InterpolationMode==0)
						{
							//Constant interpolate...
							position=Node->Keys[LastPositionKeyID].position;
						}
						else if (InterpolationMode==1)
						{
							//Linear interpolate...
							SB3dKey LastKey=Node->Keys[LastPositionKeyID];
							f32 fd1=frame-LastKey.frame;
							f32 fd2=Key.frame-frame;
							position.X=(((Key.position.X-LastKey.position.X)/(fd1+fd2))*fd1)+LastKey.position.X;
							position.Y=(((Key.position.Y-LastKey.position.Y)/(fd1+fd2))*fd1)+LastKey.position.Y;
							position.Z=(((Key.position.Z-LastKey.position.Z)/(fd1+fd2))*fd1)+LastKey.position.Z;
						}


					}
				}
			else
				LastPositionKeyID=j;

		if (foundScale==false && Key.flags&2)
			if (Key.frame>=frame)
			{
				foundScale=true;

				if (LastScaleKeyID==-1)
				{
					scale=Key.scale;
				}
				else
				{
					if (InterpolationMode==0)
					{
						//Constant interpolate...
						scale=Node->Keys[LastScaleKeyID].scale;
					}
					else if (InterpolationMode==1)
					{
						//Linear interpolate...
						SB3dKey LastKey=Node->Keys[LastScaleKeyID];
						f32 fd1=frame-LastKey.frame;
						f32 fd2=Key.frame-frame;
						scale.X=(((Key.scale.X-LastKey.scale.X)/(fd1+fd2))*fd1)+LastKey.scale.X;
						scale.Y=(((Key.scale.Y-LastKey.scale.Y)/(fd1+fd2))*fd1)+LastKey.scale.Y;
						scale.Z=(((Key.scale.Z-LastKey.scale.Z)/(fd1+fd2))*fd1)+LastKey.scale.Z;
					}
				}
			}
			else
				LastScaleKeyID=j;

		if (foundRotation==false && Key.flags&4)
			if (Key.frame>=frame)
			{
				foundRotation=true;

				if (LastRotationKeyID==-1)
				{
					rotation=Key.rotation;
				}
				else
				{
					if (InterpolationMode==0)
					{
						//Constant interpolate...
						rotation=Node->Keys[LastRotationKeyID].rotation;
					}
					else if (InterpolationMode==1)
					{
						//Linear interpolate...
						SB3dKey LastKey=Node->Keys[LastRotationKeyID];
						f32 fd1=frame-LastKey.frame;
						f32 fd2=Key.frame-frame;
						f32 t=(1.0f/(fd1+fd2))*fd1;
						rotation.slerp(LastKey.rotation,Key.rotation,t);
					}
				}
			}
			else
				LastRotationKeyID=j;

	}
}



void CAnimatedMeshB3d::animateNodes(f32 frame,f32 startFrame, f32 endFrame)
{

	for (s32 i=0; i<(s32)Nodes.size(); i++)
	{
		SB3dNode *Node=Nodes[i];

		if (Node->Animate)
		{

			//Get keyframe...

			core::vector3df position=Node->position;
			core::vector3df scale=Node->scale;
			core::quaternion rotation=Node->rotation;

			getNodeAnimation(frame, Node, position, scale, rotation);

			Node->Animatedposition=position;
			Node->Animatedscale=scale;
			Node->Animatedrotation=rotation;


			//Calculate Matrix...
			core::matrix4 positionMatrix;
			positionMatrix.setTranslation(Node->Animatedposition);
			core::matrix4 scaleMatrix;
			scaleMatrix.setScale(Node->Animatedscale);
			core::matrix4 rotationMatrix;
			rotationMatrix=Node->Animatedrotation.getMatrix();
			Node->LocalAnimatedMatrix=positionMatrix * rotationMatrix * scaleMatrix;

		}
	}
}


void CAnimatedMeshB3d::animate(s32 intframe,s32 startFrameLoop, s32 endFrameLoop)// Why cannot this be "animate(f32 frame)", it would make so much nicer animations
{
	if (!HasAnimation || lastCalculatedFrame == intframe)
		return;

	lastCalculatedFrame = intframe;

	f32 frame = (f32)intframe;

	f32 startFrame= (f32)startFrameLoop;
	f32 endFrame= (f32)endFrameLoop;

	if (AnimateMode&2) //Update skin
		resetSkin();

	if (AnimateMode&1) //Update Nodes
		animateNodes(frame, startFrame, endFrame);

	if (AnimateMode&2) //Update skin
		for (s32 i=0; i<(s32)RootNodes.size(); ++i)
		{
			animateSkin(frame,startFrame, endFrame,RootNodes[i],0);
		}
}



//! returns amount of mesh buffers.
s32 CAnimatedMeshB3d::getMeshBufferCount()
{
	return Buffers.size();
}



//! returns pointer to a mesh buffer
IMeshBuffer* CAnimatedMeshB3d::getMeshBuffer(s32 nr)
{
	return Buffers[nr];
}



//! returns an axis aligned bounding box
const core::aabbox3d<f32>& CAnimatedMeshB3d::getBoundingBox() const
{
	return BoundingBox;
}



//! returns an axis aligned bounding box
core::aabbox3d<f32>& CAnimatedMeshB3d::getBoundingBox()
{
	return BoundingBox;
}



//! sets a flag of all contained materials to a new value
void CAnimatedMeshB3d::setMaterialFlag(video::E_MATERIAL_FLAG flag, bool newvalue)
{
	for (s32 i=0; i<(s32)Buffers.size(); ++i)
		Buffers[i]->Material.Flags[flag] = newvalue;
}



//! Returns the type of the animated mesh.
E_ANIMATED_MESH_TYPE CAnimatedMeshB3d::getMeshType() const
{
	return EAMT_B3D;
}



//!Sets Interpolation Mode
//!0- Constant
//!1- Linear (default)
void CAnimatedMeshB3d::SetInterpolationMode(s32 mode)
{
	InterpolationMode=mode;
}



//!Want should happen on when animating
//!0-Nothing
//!1-Update nodes only
//!2-Update skin only
//!3-Update both nodes and skin (default)
void CAnimatedMeshB3d::SetAnimateMode(s32 mode)
{
	AnimateMode=mode;
}



void CAnimatedMeshB3d::StoreAnimationSkelton(core::array<core::matrix4> &Matrixs)
{
	for (s32 i=0;i<(s32)Nodes.size();++i)
		Matrixs[i]=Nodes[i]->LocalAnimatedMatrix;
}



void CAnimatedMeshB3d::RecoverAnimationSkelton(core::array<core::matrix4> &Matrixs)
{
	for (s32 i=0;i<(s32)Nodes.size();++i)
		Nodes[i]->LocalAnimatedMatrix=Matrixs[i];
	lastCalculatedFrame=-1;
}


void CAnimatedMeshB3d::StoreAnimationSkelton(core::array<ISceneNode*> &JointChildSceneNodes)
{
	//Note: This function works because of the way the b3d fomat nests nodes, other mesh loaders may need a different function
	for (s32 i=0;i<(s32)Nodes.size();++i)
	{
		ISceneNode* node=JointChildSceneNodes[i];
		SB3dNode *B3dNode=Nodes[i];

		node->setPosition( B3dNode->LocalAnimatedMatrix.getTranslation() );
		node->setRotation( B3dNode->LocalAnimatedMatrix.getRotationDegrees() );
		//node->setScale( B3dNode->LocalAnimatedMatrix.getScale() );

		node->updateAbsolutePosition();//works because of nests nodes

	}
}



void CAnimatedMeshB3d::RecoverAnimationSkelton(core::array<ISceneNode*> &JointChildSceneNodes)
{
	for (s32 i=0;i<(s32)Nodes.size();++i)
	{
		ISceneNode* node=JointChildSceneNodes[i];
		SB3dNode *B3dNode=Nodes[i];
		//B3dNode->LocalAnimatedMatrix=node->getRelativeTransformation();
		B3dNode->LocalAnimatedMatrix.setTranslation( node->getPosition() );
		B3dNode->LocalAnimatedMatrix.setRotationDegrees( node->getRotation() );
		//B3dNode->LocalAnimatedMatrix.setScale( node->getScale() );
	}
	//CalculateGlobalMatrixes(0,0);
	lastCalculatedFrame=-1;

}



void CAnimatedMeshB3d::CreateAnimationSkelton(ISceneManager* SceneManager,core::array<ISceneNode*> &JointChildSceneNodes, CAnimatedMeshSceneNode *AnimatedMeshSceneNode)
{
	//Note: This function works because of the way the b3d fomat nests nodes, other mesh loaders may need a different function
	CreateAnimationSkelton_Helper(SceneManager,JointChildSceneNodes,AnimatedMeshSceneNode,0,0,0);
}



void CAnimatedMeshB3d::CreateAnimationSkelton_Helper(ISceneManager* SceneManager, core::array<ISceneNode*> &JointChildSceneNodes, CAnimatedMeshSceneNode *AnimatedMeshSceneNode, ISceneNode* ParentNode, SB3dNode *ParentB3dNode,SB3dNode *B3dNode)
{
	//Note: This function works because of the way the b3d fomat nests nodes, other mesh loaders may need a different function

	if (!ParentNode)
	{
		for (s32 i=0;i<(s32)RootNodes.size();++i)
		{
			B3dNode=RootNodes[i];

			ISceneNode* node=new CEmptySceneNode(0,SceneManager,0);//Is this ok?, no other node seems to fit my needs

			node->setParent(AnimatedMeshSceneNode);

			//node->drop(); //Should I call this??

			JointChildSceneNodes.push_back(node);

			for (s32 j=0;j<(s32)B3dNode->Nodes.size();++j)
				CreateAnimationSkelton_Helper(SceneManager, JointChildSceneNodes,AnimatedMeshSceneNode,node,B3dNode,B3dNode->Nodes[j]);
		}
	}
	else
	{
		ISceneNode* node=new CEmptySceneNode(0,SceneManager,0);

		node->setParent(ParentNode);

		//node->drop(); //Should I call this??

		JointChildSceneNodes.push_back(node);

		for (s32 j=0;j<(s32)B3dNode->Nodes.size();++j)
			CreateAnimationSkelton_Helper(SceneManager, JointChildSceneNodes,AnimatedMeshSceneNode,node,B3dNode,B3dNode->Nodes[j]);
	}

}


} // end namespace scene
} // end namespace irr

