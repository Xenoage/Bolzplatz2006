// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_TRIANGLE_2_RENDERER_TEXTURE_LIGHT_H_INCLUDED__
#define __C_TRIANGLE_2_RENDERER_TEXTURE_LIGHT_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include "ITriangleRenderer2.h"
#include "rect.h"


namespace irr
{
namespace video
{

	class CTRTextureLightMap2_M1 : public ITriangleRenderer2
	{
	public:

		//! constructor
		CTRTextureLightMap2_M1(IZBuffer2* zbuffer);
	
		//! destructor
		virtual ~CTRTextureLightMap2_M1();

		//! sets a render target
		virtual void setRenderTarget(video::IImage* surface, const core::rect<s32>& viewPort);

		//! draws an indexed triangle list
		virtual void drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c );

		//! sets the Texture
		virtual void setTexture(u32 stage, video::IImage* texture);

	protected:


		video::IImage* RenderTarget;
		core::rect<s32> ViewPortRect;

		IZBuffer2* ZBuffer;

		s32 SurfaceWidth;
		s32 SurfaceHeight;
		TZBufferType2* lockedZBuffer;
		u16* lockedSurface;

		sInternalTexture IT[2];

		//------------- Draw ScanLine -------

		// internal scan convert
		struct sScanConvertData
		{
			s32 left;			// major edge left/right
			s32 right;		// !left

			f32 invDeltaY[3];	// inverse edge delta y

			f32 x[2];			// x coordinate
			f32 slopeX[2];		// x slope along edges

			f32 z[2];			// z coordinate
			f32 slopeZ[2];		// z slope along edges

#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT	
			f32 w[2];			// w coordinate
			f32 slopeW[2];		// w slope along edges
#endif

#ifdef SOFTWARE_DRIVER_2_VERTEX_COLOR
			sVec4 c[2];			// color
			sVec4 slopeC[2];	// color slope along edges
#endif

			core::vector2d<f32> t0[2];			// texture
			core::vector2d<f32> slopeT0[2];	// texture slope along edges

			core::vector2d<f32> t1[2];			// texture
			core::vector2d<f32> slopeT1[2];	// texture slope along edges

		};

		// passed to scan Line
		struct sScanLineData
		{
			s32 y;			// y position of scanline
			f32 x[2];			// x start, x end of scanline
			f32 z[2];			// z start, z end of scanline

#ifdef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT	
			f32 w[2];			// w start, w end of scanline
#endif

#ifdef SOFTWARE_DRIVER_2_VERTEX_COLOR
			sVec4 c[2];						// color start, color end of scanline
#endif
			core::vector2d<f32> t0[2];		// texture start, texture end of scanline
			core::vector2d<f32> t1[2];		// texture start, texture end of scanline
		};

	private:
		void scanline_bilinear ( sScanLineData * data ) const;

	};

} // end namespace video
} // end namespace irr

#endif

