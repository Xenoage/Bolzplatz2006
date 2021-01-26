// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_TRIANGLE_2_RENDERER_GOURAUD_H_INCLUDED__
#define __C_TRIANGLE_2_RENDERER_GOURAUD_H_INCLUDED__

#include "ITriangleRenderer2.h"
#include "rect.h"

namespace irr
{
namespace video
{

	class CTRGouraud2 : public ITriangleRenderer2
	{
	public:

		//! constructor
		CTRGouraud2(IZBuffer2* zbuffer);
	
		//! destructor
		virtual ~CTRGouraud2();

		//! sets a render target
		virtual void setRenderTarget(video::IImage* surface, const core::rect<s32>& viewPort);

		//! draws an indexed triangle list
		virtual void drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c );

		//! en or disables the backface culling
		virtual void setBackfaceCulling(bool enabled = true);

		//! sets the Texture
		virtual void setTexture(video::IImage* texture);

	protected:


		video::IImage* RenderTarget;
		core::rect<s32> ViewPortRect;

		IZBuffer2* ZBuffer;

		s32 SurfaceWidth;
		s32 SurfaceHeight;
		bool BackFaceCullingEnabled;
		TZBufferType2* lockedZBuffer;
		u16* lockedSurface;

//		u16* lockedTexture;
//		s32 lockedTextureWidth;
//		s32 textureXMask, textureYMask;
//		video::IImage* Texture;

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

			f32 w[2];			// w coordinate
			f32 slopeW[2];		// w slope along edges

			sVec4 c[2];			// color
			sVec4 slopeC[2];	// color slope along edges

//			core::vector2d<f32> t[2];			// texture
//			core::vector2d<f32> slopeT[2];	// texture slope along edges

		};

		// passed to scan Line
		struct sScanLineData
		{
			s32 y;			// y position of scanline
			f32 x[2];			// x start, x end of scanline
			f32 z[2];			// z start, z end of scanline
			f32 w[2];			// w start, w end of scanline

			sVec4 c[2];			// color start, color end of scanline
//			core::vector2d<f32> t[2];			// texture start, texture end of scanline
		};

		void scanline_point_sample ( sScanLineData * data ) const;

//		u32 textureXMask2;
//		u32 textureYMask2;
	};

} // end namespace video
} // end namespace irr

#endif

