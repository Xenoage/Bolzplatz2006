// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSoftwareDriver2.h"
#include "CSoftwareTexture2.h"
#include "os.h"
#include "S3DVertex.h"
#include "S4DVertex.h"

namespace irr
{
namespace video
{


//! constructor
CSoftwareDriver2::CSoftwareDriver2(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter)
: CNullDriver(io, windowSize), CurrentTriangleRenderer(0),
	 ZBuffer(0), RenderTargetTexture(0), RenderTargetSurface(0)
{
	#ifdef _DEBUG
	setDebugName("CSoftwareDriver2");
	#endif

	Texture[0] = 0;
	Texture[1] = 0;

	// create backbuffer

	BackBuffer = new CImage(ECF_A1R5G5B5, windowSize);
	BackBuffer->fill(0);
	
	// get presenter

	Presenter = presenter;

	// create z buffer

	ZBuffer = irr::video::createZBuffer2(BackBuffer->getDimension());

	// create triangle renderers

	memset32 ( TriangleRenderers, 0, sizeof ( TriangleRenderers ) );
	//TriangleRenderers[ETR_FLAT] = createTriangleRendererFlat2(ZBuffer);
	//TriangleRenderers[ETR_FLAT_WIRE] = createTriangleRendererFlatWire2(ZBuffer);
	//TriangleRenderers[ETR_GOURAUD] = createTriangleRendererGouraud2(ZBuffer);
	//TriangleRenderers[ETR_GOURAUD_WIRE] = createTriangleRendererGouraudWire2(ZBuffer);
	//TriangleRenderers[ETR_TEXTURE_FLAT] = createTriangleRendererTextureFlat2(ZBuffer);
	//TriangleRenderers[ETR_TEXTURE_FLAT_WIRE] = createTriangleRendererTextureFlatWire2(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD] = createTriangleRendererTextureGouraud2(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_LIGHTMAP] = createTriangleRendererTextureLightMap2_M1(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_LIGHTMAP_M2] = createTriangleRendererTextureLightMap2_M2(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_LIGHTMAP_M4] = createTriangleRendererTextureLightMap2_M4(ZBuffer);

	TriangleRenderers[ETR_TEXTURE_GOURAUD_ALPHA] = createTriangleRendererTextureGouraudAlpha2(ZBuffer);

	//TriangleRenderers[ETR_TEXTURE_GOURAUD_WIRE] = createTriangleRendererTextureGouraudWire2(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_NOZ] = createTriangleRendererTextureGouraudNoZ2();
	//TriangleRenderers[ETR_TEXTURE_GOURAUD_ADD] = createTriangleRendererTextureGouraudAdd2(ZBuffer);

	// select render target

	setRenderTarget(BackBuffer);

	// select the right renderer

	selectRightTriangleRenderer();


}



//! destructor
CSoftwareDriver2::~CSoftwareDriver2()
{
	// delete Backbuffer
	BackBuffer->drop();

	// delete triangle renderers

	for (s32 i=0; i<ETR2_COUNT; ++i)
		if (TriangleRenderers[i])
			TriangleRenderers[i]->drop();

	// delete zbuffer

	if (ZBuffer)
		ZBuffer->drop();

	// delete current texture

	if (Texture[0])
		Texture[0]->drop();

	if (Texture[1])
		Texture[1]->drop();

	if (RenderTargetTexture)
		RenderTargetTexture->drop();

	if (RenderTargetSurface)
		RenderTargetSurface->drop();
}



//! switches to a triangle renderer
void CSoftwareDriver2::switchToTriangleRenderer(ETriangleRenderer2 renderer)
{
	CurrentTriangleRenderer = TriangleRenderers[renderer];
	if ( 0 == CurrentTriangleRenderer )
		return;

	video::IImage* s[2] = { 0 ,0 };

	if (Texture[0])
		s[0] = ((CSoftwareTexture2*)Texture[0])->getTexture();

	if (Texture[1])
		s[1] = ((CSoftwareTexture2*)Texture[1])->getTexture();


	CurrentTriangleRenderer->setTexture(0, s[0]);
	CurrentTriangleRenderer->setTexture(1, s[1]);
	CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
}


//! void selects the right triangle renderer based on the render states.
void CSoftwareDriver2::selectRightTriangleRenderer()
{
	ETriangleRenderer2 renderer = ETR_TEXTURE_GOURAUD;

	switch ( Material.MaterialType )
	{
		case EMT_TRANSPARENT_ADD_COLOR:
		case EMT_TRANSPARENT_ALPHA_CHANNEL:
		case EMT_TRANSPARENT_VERTEX_ALPHA:
			renderer = ETR_TEXTURE_GOURAUD_ALPHA;
			break;

		case EMT_LIGHTMAP:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP;
			break;

		case EMT_LIGHTMAP_M2:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP_M2;
			break;

		case EMT_LIGHTMAP_M4:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP_M4;
			break;

		default:
			if (!Material.ZBuffer && !Material.ZWriteEnable)
			{
				renderer = ETR_TEXTURE_GOURAUD_NOZ;
			}
			break;

	}



	switchToTriangleRenderer(renderer);

}





//! queries the features of the driver, returns true if feature is available
bool CSoftwareDriver2::queryFeature(E_VIDEO_DRIVER_FEATURE feature)
{
	switch (feature)
	{
	case EVDF_BILINEAR_FILTER:
		return true;
	case EVDF_RENDER_TO_TARGET:
		return true;
	case EVDF_HARDWARE_TL:
		return false;
	case EVDF_MIP_MAP:
		return false;
	};

	return false;
}



//! sets transformation
void CSoftwareDriver2::setTransform(E_TRANSFORMATION_STATE state, const core::matrix4& mat)
{
	TransformationMatrix[state] = mat;

	switch ( state )
	{
		case ETS_WORLD:
			TransformationMatrix[ETS_CURRENT] = TransformationMatrix[ETS_VIEW_PROJECTION];
			TransformationMatrix[ETS_CURRENT] *= TransformationMatrix[ETS_WORLD];

		case ETS_VIEW:
			TransformationMatrix[ETS_VIEW_PROJECTION] = TransformationMatrix[ETS_PROJECTION];
			TransformationMatrix[ETS_VIEW_PROJECTION] *= TransformationMatrix[ETS_VIEW];
			break;
	}
}




//! sets the current Texture
void CSoftwareDriver2::setTexture(u32 stage, video::ITexture* texture)
{
	if (texture && texture->getDriverType() != EDT_SOFTWARE2)
	{
		os::Printer::log("Fatal Error: Tried to set a texture not owned by this driver.", ELL_ERROR);
		return;
	}

	video::ITexture* oldTexture = texture;

	if (Texture[stage])
		Texture[stage]->drop();

	Texture[stage] = texture;

	if (Texture[stage])
		Texture[stage]->grab();

	selectRightTriangleRenderer();
}



//! sets a material
void CSoftwareDriver2::setMaterial(const SMaterial& material)
{
	Material = material;
	setTexture( 0, Material.Texture1 );
	setTexture( 1, Material.Texture2 );
}


//! clears the zbuffer
bool CSoftwareDriver2::beginScene(bool backBuffer, bool zBuffer, SColor color)
{
	CNullDriver::beginScene(backBuffer, zBuffer, color);


	if (backBuffer)
		BackBuffer->fill(color.toA1R5G5B5());

	if (ZBuffer && zBuffer)
		ZBuffer->clear();

	return true;
}

//! presents the rendered scene on the screen, returns false if failed
bool CSoftwareDriver2::endScene()
{
	CNullDriver::endScene();

	Presenter->present(BackBuffer);

	return true;
}




//! sets a render target
bool CSoftwareDriver2::setRenderTarget(video::ITexture* texture, bool clearBackBuffer, 
								 bool clearZBuffer, SColor color)
{
	if (texture && texture->getDriverType() != EDT_SOFTWARE2)
	{
		os::Printer::log("Fatal Error: Tried to set a texture not owned by this driver.", ELL_ERROR);
		return false;
	}

	if (RenderTargetTexture)
		RenderTargetTexture->drop();

	RenderTargetTexture = texture;

	if (RenderTargetTexture)
	{
		RenderTargetTexture->grab();
		setRenderTarget(((CSoftwareTexture2*)RenderTargetTexture)->getTexture());
	}
	else
	{
		setRenderTarget(BackBuffer);
		//setRenderTarget((video::CImage*)0);
	}

	if (RenderTargetSurface && (clearBackBuffer || clearZBuffer))
	{
		if (clearZBuffer)
			ZBuffer->clear();

		if (clearBackBuffer)
			((video::CImage*)RenderTargetSurface)->fill(color.toA1R5G5B5());
	}

	return true;
}


//! sets a render target
void CSoftwareDriver2::setRenderTarget(video::CImage* image)
{
	if (RenderTargetSurface)
		RenderTargetSurface->drop();

	RenderTargetSurface = image;
	RenderTargetSize.Width = 0;
	RenderTargetSize.Height = 0;
	Render2DTranslation.X = 0;
	Render2DTranslation.Y = 0;

	if (RenderTargetSurface)
	{
		RenderTargetSurface->grab();
		RenderTargetSize = RenderTargetSurface->getDimension();
	}

	setViewPort(core::rect<s32>(0,0,RenderTargetSize.Width,RenderTargetSize.Height));

	if (ZBuffer)
		ZBuffer->setSize(RenderTargetSize);
}



//! sets a viewport
void CSoftwareDriver2::setViewPort(const core::rect<s32>& area)
{
	ViewPort = area;


	//TODO: the clipping is not correct, because the projection is affected.
	// to correct this, ViewPortSize and Render2DTranslation will have to be corrected.
	core::rect<s32> rendert(0,0,RenderTargetSize.Width,RenderTargetSize.Height);
	ViewPort.clipAgainst(rendert);

	TransformationMatrix [ ETS_CLIPSCALE ].buildNDCToDCMatrix ( ViewPort, 1 );


	ViewPortSize.Width = ViewPort.getWidth();
	ViewPortSize.Height = ViewPort.getHeight();
	Render2DTranslation.X = (ViewPortSize.Width / 2) + ViewPort.UpperLeftCorner.X;
	Render2DTranslation.Y = ViewPort.UpperLeftCorner.Y + ViewPortSize.Height - (ViewPortSize.Height / 2);// + ViewPort.UpperLeftCorner.Y;

	if (CurrentTriangleRenderer)
		CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
}

/*
	generic plane clipping in homogenous coordinates
	special case ndc frustum <-w,w>,<-w,w>,<-w,w>
	can be rewritten with compares e.q near plane, a.z < -a.w and b.z < -b.w
*/

const sVec4 NDCPlane[6] =
{
	sVec4(  0.f,  0.f,  1.f, -1.f ),	// near
	sVec4(  1.f,  0.f,  0.f, -1.f ),	// left
	sVec4( -1.f,  0.f,  0.f, -1.f ),	// right
	sVec4(  0.f,  1.f,  0.f, -1.f ),	// bottom
	sVec4(  0.f, -1.f,  0.f, -1.f ),	// top
	sVec4(  0.f,  0.f, -1.f, -1.f )		// far
};

u32 clipToHyperPlane ( s4DVertex * dest, const s4DVertex * source, u32 inCount, const sVec4 &plane )
{
	u32 outCount;
	s4DVertex * out;

	const s4DVertex * a;
	const s4DVertex * b;

	f32 aDotPlane;
	f32 bDotPlane;

	out = dest;
	outCount = 0;
	b = source;
	bDotPlane = b->Pos.dotProduct ( plane );

	for( u32 i = 1; i < inCount + 1; ++i)
	{
		a = &source[i%inCount];
		aDotPlane = a->Pos.dotProduct ( plane );

		// current point inside
		if ( aDotPlane <= 0.f )
		{
			// last point outside
			if ( bDotPlane > 0.f )
			{
				// intersect line segment with plane
				out->interpolate ( *b, *a, bDotPlane / (b->Pos - a->Pos).dotProduct ( plane ) );
				out += 1;
				outCount += 1;
			}
			// copy current to out
			*out = *a;
			b = out;

			out += 1;
			outCount += 1;
		}
		else
		{
			// current point outside

			if ( bDotPlane <= 0.f )
			{
				// previous was inside
				// intersect line segment with plane
				out->interpolate ( *b, *a, bDotPlane / (b->Pos - a->Pos).dotProduct ( plane ) );

				out += 1;
				outCount += 1;
			}
			b = a;
		}

		bDotPlane = b->Pos.dotProduct ( plane );

	}

	return outCount;
}


//! draws an indexed triangle list
void CSoftwareDriver2::drawIndexedTriangleList(const S3DVertex* vertices, s32 vertexCount,
											 const u16* indexList, s32 triangleCount)
{
	if ( 0 == CurrentTriangleRenderer )
		return;

	s4DVertex *v0;
	s4DVertex *v1;

	// triangle face
	const S3DVertex * a;
	const S3DVertex * b;
	const S3DVertex * c;

	int i,g;

	triangleCount *= 3;
	for ( i = 0; i!= triangleCount; i += 3 )
	{
		// select face
		a = &vertices [ indexList [ i + 0 ] ];
		b = &vertices [ indexList [ i + 1 ] ];
		c = &vertices [ indexList [ i + 2 ] ];

		//	- transform Model * World * Camera * Projection * NDCSpace matrix
		//	- store homogenous
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[0].Pos.x, a->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[1].Pos.x, b->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[2].Pos.x, c->Pos );

		// apply lighting model ;-)
#ifdef SOFTWARE_DRIVER_2_VERTEX_COLOR
		CurrentOut[0].Color.setA8R8G8B8 ( a->Color.color );
		CurrentOut[1].Color.setA8R8G8B8 ( b->Color.color );
		CurrentOut[2].Color.setA8R8G8B8 ( c->Color.color );
#endif

		// apply TexCoords
		CurrentOut[0].TCoords0 = a->TCoords;
		CurrentOut[1].TCoords0 = b->TCoords;
		CurrentOut[2].TCoords0 = c->TCoords;

		// vertices count per triangle
		int vOut = 3;

		// Clip in NDC Space to Frustum
		// Clips a primitive at the viewing frustrum in hyper space
		// (<-w,w>, <-w,w>, <-w,w>)
		{
			v0 = CurrentOut;
			v1 = Temp;

			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[0] );		// near
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[1] );		// left
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[2] );		// right
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[3] );		// bottom
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[4] );		// top
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[5] );		// far
		}

		if ( vOut < 3 )
			continue;

		// to device coordinates
		{
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g,++v0 )
			{
				v0->Pos.x = v0->Pos.x * TransformationMatrix [ ETS_CLIPSCALE ].M[ 0] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[12];
				v0->Pos.y = v0->Pos.y * TransformationMatrix [ ETS_CLIPSCALE ].M[ 5] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[13];
			}
		}

		// project homogenous vertex
		// store 1/w
		{
			f32 rhw;
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g, ++v0 )
			{
				rhw = inverse32 ( v0->Pos.w );

				v0->Pos.x *= rhw;
				v0->Pos.y *= rhw;
				v0->Pos.z *= rhw;
				v0->Pos.w = rhw;

				// for perspective correction interpolation
#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
				v0->Color *= rhw;
				v0->TCoords0 *= rhw;
#endif
			}
		}

		// check 2d backface culling
		if ( Material.BackfaceCulling )
		{
			v0 = CurrentOut;

			f32 x0,y0, x1,y1, z;

			x0 = v0[1].Pos.x - v0[0].Pos.x;
			y0 = v0[1].Pos.y - v0[0].Pos.y;
			x1 = v0[2].Pos.x - v0[0].Pos.x;
			y1 = v0[2].Pos.y - v0[0].Pos.y;

			z = x0*y1 - y0*x1;

			if ( z < 0.f )
				continue;
		}

		// re-tesselate ( triangle-fan, 0-1-2,0-2-3.. )
		for ( g = 0; g <= vOut - 3; ++g )
		{
			// rasterize
			CurrentTriangleRenderer->drawTriangle ( CurrentOut, &CurrentOut[g + 1], &CurrentOut[g + 2] );
		}
	}
}


//! Draws an indexed triangle list.
void CSoftwareDriver2::drawIndexedTriangleList(const S3DVertexTangents* vertices,
	s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	if ( 0 == CurrentTriangleRenderer )
		return;

	s4DVertex *v0;
	s4DVertex *v1;

	// triangle face
	const S3DVertexTangents * a;
	const S3DVertexTangents * b;
	const S3DVertexTangents * c;

	int i,g;

	triangleCount *= 3;
	for ( i = 0; i!= triangleCount; i += 3 )
	{
		// select face
		a = &vertices [ indexList [ i + 0 ] ];
		b = &vertices [ indexList [ i + 1 ] ];
		c = &vertices [ indexList [ i + 2 ] ];

		//	- transform Model * World * Camera * Projection * NDCSpace matrix
		//	- store homogenous
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[0].Pos.x, a->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[1].Pos.x, b->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[2].Pos.x, c->Pos );

		// apply lighting model ;-)
#ifdef SOFTWARE_DRIVER_2_VERTEX_COLOR
		CurrentOut[0].Color.setA8R8G8B8 ( a->Color.color );
		CurrentOut[1].Color.setA8R8G8B8 ( b->Color.color );
		CurrentOut[2].Color.setA8R8G8B8 ( c->Color.color );
#endif
		// apply TexCoords
		CurrentOut[0].TCoords0 = a->TCoords;
		CurrentOut[1].TCoords0 = b->TCoords;
		CurrentOut[2].TCoords0 = c->TCoords;


		// vertices count per triangle
		int vOut = 3;

		// Clip in NDC Space to Frustum
		// Clips a primitive at the viewing frustrum in hyper space
		// (<-w,w>, <-w,w>, <-w,w>)
		{
			v0 = CurrentOut;
			v1 = Temp;

			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[0] );		// near
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[1] );		// left
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[2] );		// right
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[3] );		// bottom
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[4] );		// top
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[5] );		// far
		}

		if ( vOut < 3 )
			continue;

		// to device coordinates
		{
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g,++v0 )
			{
				v0->Pos.x = v0->Pos.x * TransformationMatrix [ ETS_CLIPSCALE ].M[ 0] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[12];
				v0->Pos.y = v0->Pos.y * TransformationMatrix [ ETS_CLIPSCALE ].M[ 5] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[13];
			}
		}

		// project homogenous vertex
		// store 1/w
		{
			f32 rhw;
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g, ++v0 )
			{
				rhw = inverse32 ( v0->Pos.w );

				v0->Pos.x *= rhw;
				v0->Pos.y *= rhw;
				v0->Pos.z *= rhw;
				v0->Pos.w = rhw;

				// for perspective correction interpolation
#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
				v0->Color *= rhw;
				v0->TCoords0 *= rhw;
#endif
			}
		}

		// check 2d backface culling
		if ( Material.BackfaceCulling )
		{
			v0 = CurrentOut;

			f32 x0,y0, x1,y1, z;

			x0 = v0[1].Pos.x - v0[0].Pos.x;
			y0 = v0[1].Pos.y - v0[0].Pos.y;
			x1 = v0[2].Pos.x - v0[0].Pos.x;
			y1 = v0[2].Pos.y - v0[0].Pos.y;

			z = x0*y1 - y0*x1;

			if ( z < 0.f )
				continue;
		}

		// re-tesselate ( triangle-fan, 0-1-2,0-2-3.. )
		for ( g = 0; g <= vOut - 3; ++g )
		{
			// rasterize
			CurrentTriangleRenderer->drawTriangle ( CurrentOut, &CurrentOut[g + 1], &CurrentOut[g + 2] );
		}
	}


}


//! draws an indexed triangle list
void CSoftwareDriver2::drawIndexedTriangleList(const S3DVertex2TCoords* vertices, s32 vertexCount,
											 const u16* indexList, s32 triangleCount)
{
	if ( 0 == CurrentTriangleRenderer )
		return;

	s4DVertex *v0;
	s4DVertex *v1;

	// triangle face
	const S3DVertex2TCoords * a;
	const S3DVertex2TCoords * b;
	const S3DVertex2TCoords * c;

	int i,g;

	triangleCount *= 3;
	for ( i = 0; i!= triangleCount; i += 3 )
	{
		// select face
		a = &vertices [ indexList [ i + 0 ] ];
		b = &vertices [ indexList [ i + 1 ] ];
		c = &vertices [ indexList [ i + 2 ] ];

		//	- transform Model * World * Camera * Projection * NDCSpace matrix
		//	- store homogenous
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[0].Pos.x, a->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[1].Pos.x, b->Pos );
		TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[2].Pos.x, c->Pos );

		// apply lighting model ;-)
#ifdef SOFTWARE_DRIVER_2_VERTEX_COLOR
		CurrentOut[0].Color.setA8R8G8B8 ( a->Color.color );
		CurrentOut[1].Color.setA8R8G8B8 ( b->Color.color );
		CurrentOut[2].Color.setA8R8G8B8 ( c->Color.color );
#endif
		// apply TexCoords
		CurrentOut[0].TCoords0 = a->TCoords;
		CurrentOut[1].TCoords0 = b->TCoords;
		CurrentOut[2].TCoords0 = c->TCoords;

		CurrentOut[0].TCoords1 = a->TCoords2;
		CurrentOut[1].TCoords1 = b->TCoords2;
		CurrentOut[2].TCoords1 = c->TCoords2;

		// vertices count per triangle
		int vOut = 3;

		// Clip in NDC Space to Frustum
		// Clips a primitive at the viewing frustrum in hyper space
		// (<-w,w>, <-w,w>, <-w,w>)
		{
			v0 = CurrentOut;
			v1 = Temp;

			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[0] );		// near
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[1] );		// left
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[2] );		// right
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[3] );		// bottom
			vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[4] );		// top
			vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[5] );		// far
		}

		if ( vOut < 3 )
			continue;

		// to device coordinates
		{
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g,++v0 )
			{
				v0->Pos.x = v0->Pos.x * TransformationMatrix [ ETS_CLIPSCALE ].M[ 0] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[12];
				v0->Pos.y = v0->Pos.y * TransformationMatrix [ ETS_CLIPSCALE ].M[ 5] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[13];
			}
		}

		// project homogenous vertex
		// store 1/w
		{
			f32 rhw;
			v0 = CurrentOut;
			for ( g = 0; g != vOut; ++g, ++v0 )
			{
				rhw = inverse32 ( v0->Pos.w );

				v0->Pos.x *= rhw;
				v0->Pos.y *= rhw;
				v0->Pos.z *= rhw;
				v0->Pos.w = rhw;


				// for perspective correction interpolation
#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
				v0->Color *= rhw;
				v0->TCoords0 *= rhw;
				v0->TCoords1 *= rhw;
#endif
			}
		}

		// check 2d backface culling
		if ( Material.BackfaceCulling )
		{
			v0 = CurrentOut;

			f32 x0,y0, x1,y1, z;

			x0 = v0[1].Pos.x - v0[0].Pos.x;
			y0 = v0[1].Pos.y - v0[0].Pos.y;
			x1 = v0[2].Pos.x - v0[0].Pos.x;
			y1 = v0[2].Pos.y - v0[0].Pos.y;

			z = x0*y1 - y0*x1;

			if ( z < 0.f )
				continue;
		}

		// re-tesselate ( triangle-fan, 0-1-2,0-2-3.. )
		for ( g = 0; g <= vOut - 3; ++g )
		{
			// rasterize
			CurrentTriangleRenderer->drawTriangle ( CurrentOut, &CurrentOut[g + 1], &CurrentOut[g + 2] );
		}
	}


}


//! draws an indexed triangle fan
void CSoftwareDriver2::drawIndexedTriangleFan(const S3DVertex* vertices, 
											s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	// TODO: don't convert fan to list

	core::array<irr::u16> newBuffer;

	for( s32 t=0; t<triangleCount; t++ )
	{
		newBuffer.push_back(indexList[0]);
		newBuffer.push_back(indexList[t+1]);
		newBuffer.push_back(indexList[t+2]);
	}

	drawIndexedTriangleList( vertices, vertexCount, newBuffer.pointer(), triangleCount );
}



//! draws an indexed triangle fan
void CSoftwareDriver2::drawIndexedTriangleFan(const S3DVertex2TCoords* vertices, s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	// TODO: don't convert fan to list

	core::array<irr::u16> newBuffer;

	for( s32 t=0; t<triangleCount; t++ )
	{
		newBuffer.push_back(indexList[0]);
		newBuffer.push_back(indexList[t+1]);
		newBuffer.push_back(indexList[t+2]);
	}

	drawIndexedTriangleList( vertices, vertexCount, newBuffer.pointer(), triangleCount );
}



//! draws an 2d image
void CSoftwareDriver2::draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos)
{
	if (texture)
	{
		if (texture->getDriverType() != EDT_SOFTWARE2)
		{
			os::Printer::log("Fatal Error: Tried to copy from a surface not owned by this driver.", ELL_ERROR);
			return;
		}

		((CSoftwareTexture2*)texture)->getImage()->copyTo(BackBuffer, destPos);
	}
}


//! Draws a 2d line. 
void CSoftwareDriver2::draw2DLine(const core::position2d<s32>& start,
								const core::position2d<s32>& end, 
								SColor color)
{
	((CImage*)BackBuffer)->drawLine(start, end, color.toA1R5G5B5());
}




//! draws an 2d image, using a color (if color is other then Color(255,255,255,255)) and the alpha channel of the texture if wanted.
void CSoftwareDriver2::draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos,
								 const core::rect<s32>& sourceRect, 
								 const core::rect<s32>* clipRect, SColor color, 
								 bool useAlphaChannelOfTexture)
{
	if (texture)
	{
		if (texture->getDriverType() != EDT_SOFTWARE2)
		{
			os::Printer::log("Fatal Error: Tried to copy from a surface not owned by this driver.", ELL_ERROR);
			return;
		}

		if (useAlphaChannelOfTexture)
			((CSoftwareTexture2*)texture)->getImage()->copyToWithAlpha(
				BackBuffer, destPos, sourceRect, color.toA1R5G5B5(), clipRect);
		else
			((CSoftwareTexture2*)texture)->getImage()->copyTo(
				BackBuffer, destPos, sourceRect, clipRect);
	}
}



//! draw an 2d rectangle
void CSoftwareDriver2::draw2DRectangle(SColor color, const core::rect<s32>& pos,
									 const core::rect<s32>* clip)
{
	if (clip)
	{
		core::rect<s32> p(pos);

		p.clipAgainst(*clip);

		if(!p.isValid())  
			return;

		BackBuffer->drawRectangle(p, color);
	}
	else
	{
		if(!pos.isValid())
			return;

		BackBuffer->drawRectangle(pos, color);
	}
}


//!Draws an 2d rectangle with a gradient.
void CSoftwareDriver2::draw2DRectangle(const core::rect<s32>& pos,
	SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
	const core::rect<s32>* clip)
{
	// TODO: implement
	draw2DRectangle(colorLeftUp, pos, clip);
}


//! \return Returns the name of the video driver. Example: In case of the DirectX8
//! driver, it would return "Direct3D8.1".
const wchar_t* CSoftwareDriver2::getName()
{
	return L"Apfelbaum Software Renderer 1.0";
}

//! Returns type of video driver
E_DRIVER_TYPE CSoftwareDriver2::getDriverType()
{
	return EDT_SOFTWARE2;
}

//! Returns the transformation set by setTransform
core::matrix4 CSoftwareDriver2::getTransform(E_TRANSFORMATION_STATE state)
{
	return TransformationMatrix[state];
}

//! Creates a render target texture.
ITexture* CSoftwareDriver2::createRenderTargetTexture(core::dimension2d<s32> size)
{
	CImage* img = new CImage(video::ECF_A1R5G5B5, size);
	ITexture* tex = new CSoftwareTexture2(img);
	img->drop();
	return tex;	
}


//! Clears the ZBuffer. 
void CSoftwareDriver2::clearZBuffer()
{
	if (ZBuffer)
		ZBuffer->clear();
}

//! returns a device dependent texture from a software surface (IImage)
//! THIS METHOD HAS TO BE OVERRIDDEN BY DERIVED DRIVERS WITH OWN TEXTURES
ITexture* CSoftwareDriver2::createDeviceDependentTexture(IImage* surface)
{
	return new CSoftwareTexture2(surface);
}

//! Draws a 3d line.
void CSoftwareDriver2::draw3DLine(const core::vector3df& start, 
					const core::vector3df& end, SColor color)
{
	// TODO: implement
}


//! creates a video driver
IVideoDriver* createSoftwareDriver2(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter)
{
	return new CSoftwareDriver2(windowSize, fullscreen, io, presenter);
}


} // end namespace video
} // end namespace irr
