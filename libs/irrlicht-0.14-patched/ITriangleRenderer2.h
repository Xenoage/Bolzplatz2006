// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __I_TRIANGLE_2_RENDERER_H_INCLUDED__
#define __I_TRIANGLE_2_RENDERER_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include "IUnknown.h"
#include "IImage.h"
#include "S2DVertex.h"
#include "rect.h"
#include "IZBuffer2.h"
#include "S4DVertex.h"



namespace irr
{


namespace video
{

	enum ETriangleRenderer2
	{
		ETR_FLAT = 0,
		ETR_FLAT_WIRE,
		ETR_GOURAUD,
		ETR_GOURAUD_WIRE,
		ETR_TEXTURE_FLAT,
		ETR_TEXTURE_FLAT_WIRE,
		ETR_TEXTURE_GOURAUD,
		ETR_TEXTURE_GOURAUD_WIRE,
		ETR_TEXTURE_GOURAUD_NOZ,
		ETR_TEXTURE_GOURAUD_ADD,

		ETR_TEXTURE_GOURAUD_LIGHTMAP,
		ETR_TEXTURE_GOURAUD_LIGHTMAP_M2,
		ETR_TEXTURE_GOURAUD_LIGHTMAP_M4,

		ETR_TEXTURE_GOURAUD_ALPHA,

		ETR2_COUNT
	};

	class ITriangleRenderer2 : public IUnknown
	{
	public:
	
		//! destructor
		virtual ~ITriangleRenderer2() {};

		//! sets a render target
		virtual void setRenderTarget(video::IImage* surface, const core::rect<s32>& viewPort) = 0;


		//! sets the Texture
		virtual void setTexture( u32 stage, video::IImage* texture) = 0;

		virtual void drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c ) = 0;
	};


	ITriangleRenderer2* createTriangleRendererTextureGouraud2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureLightMap2_M1(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureLightMap2_M2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureLightMap2_M4(IZBuffer2* zbuffer);

	ITriangleRenderer2* createTriangleRendererTextureGouraudAlpha2(IZBuffer2* zbuffer);

	ITriangleRenderer2* createTriangleRendererTextureGouraudWire2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererGouraud2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererGouraudWire2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureFlat2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureFlatWire2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererFlat2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererFlatWire2(IZBuffer2* zbuffer);
	ITriangleRenderer2* createTriangleRendererTextureGouraudNoZ2();
	ITriangleRenderer2* createTriangleRendererTextureGouraudAdd2(IZBuffer2* zbuffer);


} // end namespace video
} // end namespace irr

#endif

