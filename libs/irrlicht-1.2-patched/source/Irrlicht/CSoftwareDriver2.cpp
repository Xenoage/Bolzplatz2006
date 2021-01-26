// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSoftwareDriver2.h"
#include "CSoftwareTexture2.h"
#include "CSoftware2MaterialRenderer.h"
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
	Texmap[0].Texture = 0;
	Texmap[1].Texture = 0;

	// create backbuffer
	BackBuffer = new CImage(ECF_SOFTWARE2, windowSize);
	BackBuffer->fill(SColor(0));
	
	// get presenter

	Presenter = presenter;

	// create z buffer

	ZBuffer = irr::video::createZBuffer2(BackBuffer->getDimension());

	// create triangle renderers

	memset32 ( TriangleRenderer, 0, sizeof ( TriangleRenderer ) );
	//TriangleRenderer[ETR_FLAT] = createTRFlat2(ZBuffer);
	//TriangleRenderer[ETR_FLAT_WIRE] = createTRFlatWire2(ZBuffer);
	TriangleRenderer[ETR_GOURAUD] = createTriangleRendererGouraud2(ZBuffer);
	TriangleRenderer[ETR_GOURAUD_ALPHA] = createTriangleRendererGouraudAlpha2(ZBuffer );
	TriangleRenderer[ETR_GOURAUD_ALPHA_NOZ] = createTriangleRendererGouraudAlphaNoZ2(ZBuffer );
	//TriangleRenderer[ETR_GOURAUD_WIRE] = createTriangleRendererGouraudWire2(ZBuffer);
	//TriangleRenderer[ETR_TEXTURE_FLAT] = createTriangleRendererTextureFlat2(ZBuffer);
	//TriangleRenderer[ETR_TEXTURE_FLAT_WIRE] = createTriangleRendererTextureFlatWire2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD] = createTriangleRendererTextureGouraud2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_LIGHTMAP] = createTriangleRendererTextureLightMap2_M1(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_LIGHTMAP_M2] = createTriangleRendererTextureLightMap2_M2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_LIGHTMAP_M4] = createTriangleRendererGTextureLightMap2_M4(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_LIGHTMAP_M4] = createTriangleRendererTextureLightMap2_M4(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_LIGHTMAP_ADD] = createTriangleRendererTextureLightMap2_Add(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_DETAIL_MAP] = createTriangleRendererTextureDetailMap2(ZBuffer);

	TriangleRenderer[ETR_TEXTURE_GOURAUD_WIRE] = createTriangleRendererTextureGouraudWire2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_NOZ] = createTRTextureGouraudNoZ2();
	TriangleRenderer[ETR_TEXTURE_GOURAUD_ADD] = createTRTextureGouraudAdd2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_ADD_NO_Z] = createTRTextureGouraudAddNoZ2(ZBuffer);
	TriangleRenderer[ETR_TEXTURE_GOURAUD_VERTEX_ALPHA] = createTriangleRendererTextureVertexAlpha2 ( ZBuffer );


	// add the same renderer for all solid types
	CSoftware2MaterialRenderer_SOLID* smr = new CSoftware2MaterialRenderer_SOLID( this);
	CSoftware2MaterialRenderer_TRANSPARENT_ADD_COLOR* tmr = new CSoftware2MaterialRenderer_TRANSPARENT_ADD_COLOR( this);
	CSoftware2MaterialRenderer_UNSUPPORTED * umr = new CSoftware2MaterialRenderer_UNSUPPORTED ( this );

	//!TODO: addMaterialRenderer depends on pushing order....
	addMaterialRenderer ( smr ); // EMT_SOLID
	addMaterialRenderer ( smr ); // EMT_SOLID_2_LAYER,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP,
	addMaterialRenderer ( tmr ); // EMT_LIGHTMAP_ADD,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP_M2,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP_M4,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP_LIGHTING,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP_LIGHTING_M2,
	addMaterialRenderer ( smr ); // EMT_LIGHTMAP_LIGHTING_M4,
	addMaterialRenderer ( smr ); // EMT_DETAIL_MAP,
	addMaterialRenderer ( umr ); // EMT_SPHERE_MAP,
	addMaterialRenderer ( smr ); // EMT_REFLECTION_2_LAYER,
	addMaterialRenderer ( tmr ); // EMT_TRANSPARENT_ADD_COLOR,
	addMaterialRenderer ( tmr ); // EMT_TRANSPARENT_ALPHA_CHANNEL,	
	addMaterialRenderer ( tmr ); // EMT_TRANSPARENT_ALPHA_CHANNEL_REF,	
	addMaterialRenderer ( tmr ); // EMT_TRANSPARENT_VERTEX_ALPHA,
	addMaterialRenderer ( smr ); // EMT_TRANSPARENT_REFLECTION_2_LAYER,
	addMaterialRenderer ( umr ); // EMT_NORMAL_MAP_SOLID,
	addMaterialRenderer ( umr ); // EMT_NORMAL_MAP_TRANSPARENT_ADD_COLOR,
	addMaterialRenderer ( umr ); // EMT_NORMAL_MAP_TRANSPARENT_VERTEX_ALPHA,
	addMaterialRenderer ( umr ); // EMT_PARALLAX_MAP_SOLID,
	addMaterialRenderer ( umr ); // EMT_PARALLAX_MAP_TRANSPARENT_ADD_COLOR,
	addMaterialRenderer ( umr ); // EMT_PARALLAX_MAP_TRANSPARENT_VERTEX_ALPHA,

	smr->drop ();
	tmr->drop ();
	umr->drop ();


	// select render target

	setRenderTarget(BackBuffer);


	Global_AmbientLight.set ( 0.f, 0.f, 0.f, 0.f );

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
		if (TriangleRenderer[i])
			TriangleRenderer[i]->drop();

	// delete zbuffer

	if (ZBuffer)
		ZBuffer->drop();

	// delete current texture

	if ( Texture[0] )
		Texture[0]->drop();

	if ( Texture[1] )
		Texture[1]->drop();

	if (RenderTargetTexture)
		RenderTargetTexture->drop();

	if (RenderTargetSurface)
		RenderTargetSurface->drop();
}



//! void selects the right triangle renderer based on the render states.
void CSoftwareDriver2::selectRightTriangleRenderer()
{
	ETriangleRenderer2 renderer = ETR_TEXTURE_GOURAUD;

	bool zTest = true;
	switch ( Material.org.MaterialType )
	{

		case EMT_TRANSPARENT_ALPHA_CHANNEL:
		case EMT_TRANSPARENT_ADD_COLOR:
			if ( Material.org.ZBuffer )
			{
				renderer = ETR_TEXTURE_GOURAUD_ADD;
			}
			else
			{
				renderer = ETR_TEXTURE_GOURAUD_ADD_NO_Z;
			}
			zTest = false;
			break;

		case EMT_TRANSPARENT_VERTEX_ALPHA:
			renderer = ETR_TEXTURE_GOURAUD_VERTEX_ALPHA;
			break;

		case EMT_LIGHTMAP:
		case EMT_LIGHTMAP_LIGHTING:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP;
			break;

		case EMT_LIGHTMAP_M2:
		case EMT_LIGHTMAP_LIGHTING_M2:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP_M2;
			break;

		case EMT_LIGHTMAP_LIGHTING_M4:
			renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP_M4;
			break;
		case EMT_LIGHTMAP_M4:
			renderer = ETR_TEXTURE_LIGHTMAP_M4;
			break;

		case EMT_LIGHTMAP_ADD:
			if ( Material.org.Texture2 )
				renderer = ETR_TEXTURE_GOURAUD_LIGHTMAP_ADD;
			break;

		case EMT_DETAIL_MAP:
			renderer = ETR_TEXTURE_GOURAUD_DETAIL_MAP;
			break;

		default:
			break;

	}

	if ( zTest && !Material.org.ZBuffer && !Material.org.ZWriteEnable)
	{
		renderer = ETR_TEXTURE_GOURAUD_NOZ;
	}

	if ( 0 == Material.org.Texture1 )
	{
		renderer = ETR_GOURAUD;
	}

	if ( Material.org.Wireframe )
	{
		renderer = ETR_TEXTURE_GOURAUD_WIRE;
	}

	// switchToTriangleRenderer
	CurrentTriangleRenderer = TriangleRenderer[renderer];
	if ( CurrentTriangleRenderer )
	{
		//CurrentTriangleRenderer->setTexture(0, Texmap[0].Texture);
		//CurrentTriangleRenderer->setTexture(1, Texmap[1].Texture);
		CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
	}

}



//! queries the features of the driver, returns true if feature is available
bool CSoftwareDriver2::queryFeature(E_VIDEO_DRIVER_FEATURE feature)
{
	switch (feature)
	{
#ifdef SOFTWARE_DRIVER_2_BILINEAR   
	case EVDF_BILINEAR_FILTER:
		return true;
#endif
#ifdef SOFTWARE_DRIVER_2_MIPMAPPING
	case EVDF_MIP_MAP:
		return true;
#endif

	case EVDF_RENDER_TO_TARGET:
	case EVDF_MULTITEXTURE:
		return true;

	default:
		return false;
	};
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
bool CSoftwareDriver2::setTexture(u32 stage, video::ITexture* texture)
{
	if (texture && texture->getDriverType() != EDT_SOFTWARE2)
	{
		os::Printer::log("Fatal Error: Tried to set a texture not owned by this driver.", ELL_ERROR);
		return false;
	}

	if (Texture[stage])
		Texture[stage]->drop();

	Texture[stage] = texture;

	if (Texture[stage])
		Texture[stage]->grab();

	if ( Texture[stage] )
	{
		Texmap[stage].Texture = (video::CSoftwareTexture2*) Texture[stage];
	}

	selectRightTriangleRenderer();
	return true;
}



//! sets a material
void CSoftwareDriver2::setMaterial(const SMaterial& material)
{
	Material.org = material;

	Material.AmbientColor.setA8R8G8B8 ( Material.org.AmbientColor.color );
	Material.DiffuseColor.setA8R8G8B8 ( Material.org.DiffuseColor.color );
	Material.EmissiveColor.setA8R8G8B8 ( Material.org.EmissiveColor.color );
	Material.SpecularColor.setA8R8G8B8 ( Material.org.SpecularColor.color );
	setTexture( 0, Material.org.Texture1 );
	setTexture( 1, Material.org.Texture2 );
}



//! clears the zbuffer
bool CSoftwareDriver2::beginScene(bool backBuffer, bool zBuffer, SColor color)
{

	CNullDriver::beginScene(backBuffer, zBuffer, color);

	if (backBuffer)
		BackBuffer->fill( color );

	if (ZBuffer && zBuffer)
		ZBuffer->clear();

	return true;
}

//! presents the rendered scene on the screen, returns false if failed
bool CSoftwareDriver2::endScene( s32 windowId, core::rect<s32>* sourceRect )
{
	CNullDriver::endScene();

	Presenter->present(BackBuffer, windowId, sourceRect );

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
			((video::CImage*)RenderTargetSurface)->fill( color );
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


	if (CurrentTriangleRenderer)
		CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
}

/*
	generic plane clipping in homogenous coordinates
	special case ndc frustum <-w,w>,<-w,w>,<-w,w>
	can be rewritten with compares e.q near plane, a.z < -a.w and b.z < -b.w
*/

static const sVec4 NDCPlane[6] =
{
	sVec4(  0.f,  0.f,  1.f, -1.f ),	// near
	sVec4(  1.f,  0.f,  0.f, -1.f ),	// left
	sVec4( -1.f,  0.f,  0.f, -1.f ),	// right
	sVec4(  0.f,  1.f,  0.f, -1.f ),	// bottom
	sVec4(  0.f, -1.f,  0.f, -1.f ),	// top
	sVec4(  0.f,  0.f, -1.f, -1.f )		// far
};


static u32 clipToHyperPlane ( s4DVertex * dest, const s4DVertex * source, u32 inCount, const sVec4 &plane )
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
		//a = &source[ i%inCount];

		const s32 condition = i - inCount;
		const s32 index = ( ( condition >> 31 ) & ( i ^ condition ) ) ^ condition;

		a = &source[ index ];

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


u32 CSoftwareDriver2::clipToFrustrum_NoStat ( s4DVertex *v0, s4DVertex * v1, u32 vIn )
{
	u32 vOut;

	vOut = vIn;

	vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[0] );		// near
	if ( vOut < vIn )
		return vOut;

	vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[5] );		// far
	if ( vOut < vIn )
		return vOut;

	vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[2] );		// right
	if ( vOut < vIn )
		return vOut;

	vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[1] );		// left
	if ( vOut < vIn )
		return vOut;

	vOut = clipToHyperPlane ( v1, v0, vOut, NDCPlane[4] );		// top
	if ( vOut < vIn )
		return vOut;

	vOut = clipToHyperPlane ( v0, v1, vOut, NDCPlane[3] );		// bottom


	return vOut;
}

/*!
 Part I:
	apply Clip Scale matrix
	From Normalized Device Coordiante ( NDC ) Space to Device Coordinate Space ( DC )

 Part II:
	Project homogeneous vector
	homogeneous to non-homogenous coordinates ( dividebyW )

	Incoming: ( xw, yw, zw, w, u, v, 1, R, G, B, A )
	Outgoing: ( xw/w, yw/w, zw/w, w/w, u/w, v/w, 1/w, R/w, G/w, B/w, A/w )


	replace w/w by 1/w
*/
inline void CSoftwareDriver2::ndc_2_dc_and_project ( s4DVertex *source, u32 vIn ) const
{
	s4DVertex *v0;
	u32 g;
	f32 rhw;


	v0 = source;

	for ( g = 0; g != vIn; ++g,++v0 )
	{
		// to device coordinates
		v0->Pos.x = v0->Pos.x * TransformationMatrix [ ETS_CLIPSCALE ].M[ 0] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[12];
		v0->Pos.y = v0->Pos.y * TransformationMatrix [ ETS_CLIPSCALE ].M[ 5] + v0->Pos.w * TransformationMatrix [ ETS_CLIPSCALE ].M[13];

		// project homogenous vertex, store 1/w
		rhw = inverse32 ( v0->Pos.w );

		v0->Pos.x *= rhw;
		v0->Pos.y *= rhw;
		v0->Pos.z *= rhw;
		v0->Pos.w = rhw;

#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
		v0->Color *= rhw;
		//v0->Tex[0] *= rhw;
		//v0->Tex[1] *= rhw;
#endif

	}

}


/*!
	crossproduct in projcteded 2D
*/
inline f32 CSoftwareDriver2::backface ( const s4DVertex *v0 ) const
{
	return	( ( v0[1].Pos.x - v0[0].Pos.x ) * ( v0[2].Pos.y - v0[0].Pos.y ) ) -
			( ( v0[1].Pos.y - v0[0].Pos.y ) * ( v0[2].Pos.x - v0[0].Pos.x ) );
}

/*!
*/
inline f32 CSoftwareDriver2::texelarea ( const s4DVertex *v0, int tex ) const
{
	f32 x0,y0, x1,y1, z;

	x0 = v0[1].Tex[tex].x - v0[0].Tex[tex].x;
	y0 = v0[1].Tex[tex].y - v0[0].Tex[tex].y;
	x1 = v0[2].Tex[tex].x - v0[0].Tex[tex].x;
	y1 = v0[2].Tex[tex].y - v0[0].Tex[tex].y;

	z = x0*y1 - x1*y0;

	const core::dimension2d<s32> &d = Texmap[tex].Texture->getOriginalSize();
	z *= d.Height;
	z *= d.Width;
	return z;
}

//! Sets the dynamic ambient light color. The default color is
//! (0,0,0,0) which means it is dark.
//! \param color: New color of the ambient light.
void CSoftwareDriver2::setAmbientLight(const SColorf& color)
{
	Global_AmbientLight.setColorf ( color );
}


//! adds a dynamic light
void CSoftwareDriver2::addDynamicLight(const SLight& dl)
{
	SInternalLight l;

	l.org = dl;

	l.AmbientColor.setColorf ( l.org.AmbientColor );
	l.DiffuseColor.setColorf ( l.org.DiffuseColor );

	Light.push_back ( l );
}

//! deletes all dynamic lights there are
void CSoftwareDriver2::deleteAllDynamicLights()
{
	Light.set_used ( 0 );
}

//! returns the maximal amount of dynamic lights the device can handle
s32 CSoftwareDriver2::getMaximalDynamicLightAmount()
{
	return 4096;	// i'm dreaming;-)
}



/*!
*/
void CSoftwareDriver2::transform_and_lighting ( s4DVertex *dest, const S3DVertex ** face )
{
	//	- transform Model * World * Camera * Projection * NDCSpace matrix
	//	- store homogenous
	TransformationMatrix [ ETS_CURRENT].transformVect ( &dest[0].Pos.x, face[0]->Pos );
	TransformationMatrix [ ETS_CURRENT].transformVect ( &dest[1].Pos.x, face[1]->Pos );
	TransformationMatrix [ ETS_CURRENT].transformVect ( &dest[2].Pos.x, face[2]->Pos );

#ifndef SOFTWARE_DRIVER_2_LIGHTING
	dest[0].Color.setA8R8G8B8 ( face[0]->Color.color );
	dest[1].Color.setA8R8G8B8 ( face[1]->Color.color );
	dest[2].Color.setA8R8G8B8 ( face[2]->Color.color );
#else

	// apply lighting model
	if ( false == Material.org.Lighting )
	{
		// should use the DiffuseColor but using pre-lit vertex color
		dest[0].Color.setA8R8G8B8 ( face[0]->Color.color );
		dest[1].Color.setA8R8G8B8 ( face[1]->Color.color );
		dest[2].Color.setA8R8G8B8 ( face[2]->Color.color );
		return;
	}

	u32 i,v;

	// mhmm. i need to study some lighting

	irr::core::vector3df n[3];

	// TODO: don't transform normals. Inverse Transform light
	for ( v = 0; v!= 3; ++v )
	{
		n[v] = face[v]->Normal;
		TransformationMatrix[ETS_WORLD].rotateVect ( n[v] );
		n[v].normalize ();

	}

	irr::core::vector3df vpos[3];
	bool needPos = false;
	for ( i = 0; i!= Light.size (); ++i )
	{
		if ( Light[i].org.Type == video::ELT_POINT )
		{
			needPos = true;
			break;
		}
	}

	if ( needPos )
	{
		TransformationMatrix[ETS_WORLD].transformVect ( face[0]->Pos,vpos[0] );
		TransformationMatrix[ETS_WORLD].transformVect ( face[1]->Pos,vpos[1] );
		TransformationMatrix[ETS_WORLD].transformVect ( face[2]->Pos,vpos[2] );
	}


	sVec4 ambient;
	sVec4 diffuse;
	sVec4 specular;


	f32 dot;
	core::vector3df light;
	for ( v = 0; v!= 3; ++v )
	{
		ambient = Global_AmbientLight;

		// the universe started in darkness..
		diffuse.set ( 0.f, 0.f, 0.f, 0.f );
		specular.set ( 0.f, 0.f, 0.f, 0.f );
		f32 attenuation = 0;

		for ( i = 0; i!= Light.size (); ++i )
		{
			switch ( Light[i].org.Type )
			{
				case video::ELT_POINT:
				{
					core::vector3df d;

					d = vpos[v] - Light[i].org.Position;

					f32 dist = (f32) d.getLength();

					attenuation = 1.f / ( ( 1.f / Light[i].org.Radius ) * dist );

					light = d;
					light.normalize ();
				} break;

				case video::ELT_DIRECTIONAL:
				{
					attenuation = 1.f;
					light = Light[i].org.Position;
					light.normalize ();
				} break;
			}

			// accumulate ambient
			ambient += Light[i].AmbientColor * attenuation;

			// build diffuse reflection
			dot = n[v].dotProduct ( light );
			if ( dot < 0.f )
			{
				diffuse += Light[i].DiffuseColor * ( -dot * attenuation );
			}

			// specular


		}

		sVec4 final;

		final = Material.EmissiveColor;
		final += ambient * Material.AmbientColor;
		final += diffuse * Material.DiffuseColor;
		final += specular * Material.SpecularColor;

		final.clampToOne ();
		dest[v].Color = final;
	}

#endif
}


/*!
*/
inline void CSoftwareDriver2::select_polygon_mipmap ( s4DVertex *source, u32 vIn, s32 tex )
{
	s4DVertex *v0;
	u32 g;


	v0 = source;

	core::dimension2d<f32> d;

	d.Width = (f32) Texmap[tex].Texture->getSize().Width;
	d.Height = (f32) Texmap[tex].Texture->getSize().Height;

	for ( g = 0; g != vIn; ++g,++v0 )
	{
		v0->Tex[tex].x *= v0->Pos.w * d.Width;
		v0->Tex[tex].y *= v0->Pos.w * d.Height;
	}

}

//! draws a vertex primitive list
void CSoftwareDriver2::drawVertexPrimitiveList(const void* vertices, s32 vertexCount, const u16* indexList, s32 primitiveCount, E_VERTEX_TYPE vType, scene::E_PRIMITIVE_TYPE pType)
{
	if (!checkPrimitiveCount(primitiveCount))
		return;

	CNullDriver::drawVertexPrimitiveList(vertices, vertexCount, indexList, primitiveCount, vType, pType);

	if ( 0 == CurrentTriangleRenderer )
		return;

	s32 stride=0;
	switch (vType)
	{
		case EVT_STANDARD:
			stride=sizeof(S3DVertex);
			break;
		case EVT_2TCOORDS:
			stride=sizeof(S3DVertex2TCoords);
			break;
		case EVT_TANGENTS:
			stride=sizeof(S3DVertexTangents);
			break;
	}

	// triangle face
	const S3DVertex * face[3];
	const S3DVertex2TCoords * face2[3];
	const S3DVertexTangents * faceT[3];

	if (pType == scene::EPT_TRIANGLE_FAN)
	{
		// select root vertex
		switch (vType)
		{
			case EVT_STANDARD:
				face[0] = &((S3DVertex*)vertices)[ indexList [0] ];
				break;
			case EVT_2TCOORDS:
				face2[0] = &((S3DVertex2TCoords*)vertices)[ indexList [0] ];
				break;
			case EVT_TANGENTS:
				// select face
				faceT[0] = &((S3DVertexTangents*)vertices)[ indexList [0] ];
				break;
		}
	}

	s32 pitch = 1;
	switch (pType)
	{
		case scene::EPT_TRIANGLES:
			primitiveCount = ( primitiveCount << 1 ) + primitiveCount;
			pitch=3;
			break;
	}

	u32 g;
	for ( s32 i = 0; i!= primitiveCount; i+=pitch )
	{
		switch (vType)
		{
			case EVT_STANDARD:
				// select face
				if (pType != scene::EPT_TRIANGLE_FAN)
					face[0] = &((S3DVertex*)vertices)[ indexList [ i + 0 ] ];
				face[1] = &((S3DVertex*)vertices)[ indexList [ i + 1 ] ];
				face[2] = &((S3DVertex*)vertices)[ indexList [ i + 2 ] ];

				transform_and_lighting ( CurrentOut, face );

				CurrentOut[0].Tex[0].set ( face[0]->TCoords.X, face[0]->TCoords.Y );
				CurrentOut[1].Tex[0].set ( face[1]->TCoords.X, face[1]->TCoords.Y );
				CurrentOut[2].Tex[0].set ( face[2]->TCoords.X, face[2]->TCoords.Y );

				break;
			case EVT_2TCOORDS:
				// select face
				if (pType != scene::EPT_TRIANGLE_FAN)
					face2[0] = &((S3DVertex2TCoords*)vertices)[ indexList [ i + 0 ] ];
				face2[1] = &((S3DVertex2TCoords*)vertices)[ indexList [ i + 1 ] ];
				face2[2] = &((S3DVertex2TCoords*)vertices)[ indexList [ i + 2 ] ];

				transform_and_lighting ( CurrentOut, (const S3DVertex**) face2 );

				CurrentOut[0].Tex[0].set ( face2[0]->TCoords.X, face2[0]->TCoords.Y );
				CurrentOut[1].Tex[0].set ( face2[1]->TCoords.X, face2[1]->TCoords.Y );
				CurrentOut[2].Tex[0].set ( face2[2]->TCoords.X, face2[2]->TCoords.Y );

				CurrentOut[0].Tex[1].set ( face2[0]->TCoords2.X, face2[0]->TCoords2.Y );
				CurrentOut[1].Tex[1].set ( face2[1]->TCoords2.X, face2[1]->TCoords2.Y );
				CurrentOut[2].Tex[1].set ( face2[2]->TCoords2.X, face2[2]->TCoords2.Y );

				break;
			case EVT_TANGENTS:
				// select face
				if (pType != scene::EPT_TRIANGLE_FAN)
					faceT[0] = &((S3DVertexTangents*)vertices)[ indexList [ i + 0 ] ];
				faceT[1] = &((S3DVertexTangents*)vertices)[ indexList [ i + 1 ] ];
				faceT[2] = &((S3DVertexTangents*)vertices)[ indexList [ i + 2 ] ];

				transform_and_lighting ( CurrentOut, (const S3DVertex**) faceT );
				CurrentOut[0].Tex[0].set ( faceT[0]->TCoords.X, faceT[0]->TCoords.Y );
				CurrentOut[1].Tex[0].set ( faceT[1]->TCoords.X, faceT[1]->TCoords.Y );
				CurrentOut[2].Tex[0].set ( faceT[2]->TCoords.X, faceT[2]->TCoords.Y );
		}

		// vertices count per triangle
		u32 vOut = clipToFrustrum_NoStat ( CurrentOut, Temp, 3 );
		if ( vOut < 3 )
			continue;

		// to DC Space, project homogenous vertex
		ndc_2_dc_and_project ( CurrentOut, vOut );

		// check 2d backface culling on first
		f32 cross = backface ( CurrentOut );

		if ( Material.org.BackfaceCulling && cross <= 0.f )
			continue;

		// select mipmap

		f32 texarea;
		s32 lodLevel;

		for ( g = 0; g != 2; ++g )
		{
			if ( Texmap[g].Texture )
			{
				texarea = texelarea ( CurrentOut, g );
				lodLevel = s32_log2_f32 ( texarea / cross );
				lodLevel = s32_clamp ( lodLevel, 0, SOFTWARE_DRIVER_2_MIPMAPPING_MAX - 1 );

				Texmap[g].Texture->setCurrentMipMapLOD ( lodLevel );
				select_polygon_mipmap ( CurrentOut, vOut, g );
			}
			CurrentTriangleRenderer->setTexture(g, Texmap[g].Texture);
		}

		// re-tesselate ( triangle-fan, 0-1-2,0-2-3.. )
		for ( g = 0; g <= vOut - 3; ++g )
		{
			// rasterize
			CurrentTriangleRenderer->drawTriangle ( CurrentOut, &CurrentOut[g + 1], &CurrentOut[g + 2] );
		}
	}
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
				BackBuffer, destPos, sourceRect, color, clipRect);
		else
			((CSoftwareTexture2*)texture)->getImage()->copyTo(
				BackBuffer, destPos, sourceRect, clipRect);
	}
}



//! Draws a 2d line. 
void CSoftwareDriver2::draw2DLine(const core::position2d<s32>& start,
					const core::position2d<s32>& end,
					SColor color)
{
	((CImage*)BackBuffer)->drawLine(start, end, color );
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
void CSoftwareDriver2::draw2DRectangle(const core::rect<s32>& position,
	SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
	const core::rect<s32>* clip)
{
	//draw2DRectangle(colorLeftUp, position, clip);
	//return;

	core::rect<s32> pos = position;

	if (clip)
		pos.clipAgainst(*clip);

	if (!pos.isValid())
		return;

	const core::dimension2d<s32> renderTargetSize ( ViewPort.getSize() );

	s32 xPlus = -(renderTargetSize.Width>>1);
	f32 xFact = 1.0f / (renderTargetSize.Width>>1);

	s32 yPlus = renderTargetSize.Height-(renderTargetSize.Height>>1);
	f32 yFact = 1.0f / (renderTargetSize.Height>>1);

	s4DVertex v[4];

	v[0].Pos.set ( (f32)(pos.UpperLeftCorner.X+xPlus) * xFact, (f32)(yPlus-pos.UpperLeftCorner.Y) * yFact, 0.f, 1.f );
	v[1].Pos.set ( (f32)(pos.LowerRightCorner.X+xPlus) * xFact, (f32)(yPlus- pos.UpperLeftCorner.Y) * yFact, 0.f, 1.f );
	v[2].Pos.set ( (f32)(pos.LowerRightCorner.X+xPlus) * xFact, (f32)(yPlus-pos.LowerRightCorner.Y) * yFact, 0.f ,1.f );
	v[3].Pos.set ( (f32)(pos.UpperLeftCorner.X+xPlus) * xFact, (f32)(yPlus-pos.LowerRightCorner.Y) * yFact, 0.f, 1.f );


	v[0].Color.setA8R8G8B8 ( colorLeftUp.color );
	v[1].Color.setA8R8G8B8 ( colorRightUp.color );
	v[2].Color.setA8R8G8B8 ( colorRightDown.color );
	v[3].Color.setA8R8G8B8 ( colorLeftDown.color );


	ITriangleRenderer2 * render;

	render = TriangleRenderer [ ETR_GOURAUD_ALPHA_NOZ ];
	render->setRenderTarget(RenderTargetSurface, ViewPort);

	s16 indices[6] = {0,1,2,0,2,3};

	s32 i;
	u32 g;
	for ( i = 0; i!= 6; i += 3 )
	{
		// select face
		CurrentOut[0] = v[ indices [ i + 0 ] ];
		CurrentOut[1] = v[ indices [ i + 1 ] ];
		CurrentOut[2] = v[ indices [ i + 2 ] ];

		// vertices count per triangle
		u32 vOut = clipToFrustrum_NoStat ( CurrentOut, Temp, 3 );
		if ( vOut < 3 )
			continue;

		// to DC Space, project homogenous vertex
		ndc_2_dc_and_project ( CurrentOut, vOut );

		// re-tesselate ( triangle-fan, 0-1-2,0-2-3.. )
		for ( g = 0; g <= vOut - 3; ++g )
		{
			render->drawTriangle ( CurrentOut, &CurrentOut[g + 1], &CurrentOut[g + 2] );
		}
	}
}



//! Draws a 3d line.
void CSoftwareDriver2::draw3DLine(const core::vector3df& start,
	const core::vector3df& end, SColor color)
{
	TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[0].Pos.x, start );
	TransformationMatrix [ ETS_CURRENT].transformVect ( &CurrentOut[1].Pos.x, end );

	u32 g;
	u32 vOut;

	// don't lite
	CurrentOut[0].Color.setA8R8G8B8 ( color.color );
	CurrentOut[1].Color.setA8R8G8B8 ( color.color );

	// vertices count per line
	vOut = clipToFrustrum_NoStat ( CurrentOut, Temp, 2 );
	if ( vOut >= 2 )
	{
		ITriangleRenderer2 * line;
		line = TriangleRenderer [ ETR_TEXTURE_GOURAUD_WIRE ];
		line->setRenderTarget(RenderTargetSurface, ViewPort);

		// to DC Space, project homogenous vertex
		ndc_2_dc_and_project ( CurrentOut, vOut );

		// don't lite
		for ( g = 0; g != vOut; ++g )
		{
			CurrentOut[g].Color.setA8R8G8B8 ( color.color );
		}

		for ( g = 0; g <= vOut - 2; ++g )
		{
			// rasterize
			line->drawLine ( CurrentOut, CurrentOut + g + 1 );
		}
	}
}



//! \return Returns the name of the video driver. Example: In case of the DirectX8
//! driver, it would return "Direct3D8.1".
const wchar_t* CSoftwareDriver2::getName()
{
#ifdef SOFTWARE_DRIVER_2_32BIT
	return L"burning's video 0.2 32Bit";
#else
	return L"burning's video 0.2 15Bit";
#endif

}

//! Returns type of video driver
E_DRIVER_TYPE CSoftwareDriver2::getDriverType()
{
	return EDT_SOFTWARE2;
}

//! Returns the transformation set by setTransform
const core::matrix4& CSoftwareDriver2::getTransform(E_TRANSFORMATION_STATE state)
{
	return TransformationMatrix[state];
}

//! Creates a render target texture.
ITexture* CSoftwareDriver2::createRenderTargetTexture(const core::dimension2d<s32>& size)
{
	CImage* img = new CImage(ECF_SOFTWARE2, size);

	ITexture* tex = new CSoftwareTexture2(img, 0, false);
	img->drop();
	return tex;	
}


//! Clears the ZBuffer. 
void CSoftwareDriver2::clearZBuffer()
{
	if (ZBuffer)
		ZBuffer->clear();
}


//! Returns an image created from the last rendered frame.
IImage* CSoftwareDriver2::createScreenShot()
{
	return new CImage(BackBuffer->getColorFormat(), BackBuffer);
}

//! Enables or disables a texture creation flag.
void CSoftwareDriver2::setTextureCreationFlag(E_TEXTURE_CREATION_FLAG flag, bool enabled)
{
	CNullDriver::setTextureCreationFlag(flag,enabled);
}


//! returns a device dependent texture from a software surface (IImage)
//! THIS METHOD HAS TO BE OVERRIDDEN BY DERIVED DRIVERS WITH OWN TEXTURES
ITexture* CSoftwareDriver2::createDeviceDependentTexture(IImage* surface, const char* name)
{
	return new CSoftwareTexture2(surface, name, getTextureCreationFlag(ETCF_CREATE_MIP_MAPS)
					);

}


//! creates a video driver
IVideoDriver* createSoftwareDriver2(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter)
{
	return new CSoftwareDriver2(windowSize, fullscreen, io, presenter);
}



} // end namespace video
} // end namespace irr
