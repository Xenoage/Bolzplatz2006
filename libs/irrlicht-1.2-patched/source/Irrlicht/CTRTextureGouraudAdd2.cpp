// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "ITriangleRenderer2.h"

// compile flag for this file
#undef USE_Z
#undef IPOL_Z
#undef CMP_Z
#undef WRITE_Z

#undef IPOL_W
#undef CMP_W
#undef WRITE_W

#undef SUBTEXEL
#undef INVERSE_W

#undef IPOL_C
#undef IPOL_T0
#undef IPOL_T1

// define render case
#define SUBTEXEL
#define INVERSE_W

#define USE_Z
#define IPOL_W
#define CMP_W
#define WRITE_W

#define IPOL_C
#define IPOL_T0
//#define IPOL_T1

// apply global override
#ifndef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
	#undef INVERSE_W
#endif

#ifndef SOFTWARE_DRIVER_2_SUBTEXEL
	#undef SUBTEXEL
#endif

#if !defined ( SOFTWARE_DRIVER_2_USE_WBUFFER ) && defined ( USE_Z )
	#ifndef SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT
		#undef IPOL_W
	#endif
	#define IPOL_Z

	#ifdef CMP_W
		#undef CMP_W
		#define CMP_Z
	#endif

	#ifdef WRITE_W
		#undef WRITE_W
		#define WRITE_Z
	#endif

#endif



namespace irr
{

namespace video
{

class CTRTextureGouraudAdd2 : public ITriangleRenderer2
{
public:

	//! constructor
	CTRTextureGouraudAdd2(IZBuffer2* zbuffer);

	//! draws an indexed triangle list
	virtual void drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c );


private:
	void scanline_bilinear ( sScanLineData * data ) const;

};

//! constructor
CTRTextureGouraudAdd2::CTRTextureGouraudAdd2(IZBuffer2* zbuffer)
: ITriangleRenderer2(zbuffer)
{
	#ifdef _DEBUG
	setDebugName("CTRTextureGouraudAdd2");
	#endif
}



/*!
*/
void CTRTextureGouraudAdd2::scanline_bilinear ( sScanLineData * data ) const
{
	tVideoSample *dst;

#ifdef USE_Z
	TZBufferType2 *z;
#endif

	s32 xStart;
	s32 xEnd;
	s32 dx;

	f32 invDeltaX;

#ifdef SUBTEXEL
	f32 subPixel;
#endif

#ifdef IPOL_Z
	f32 slopeZ;
#endif
#ifdef IPOL_W
	f32 slopeW;
#endif
#ifdef IPOL_C
	sVec4 slopeC;
#endif
#ifdef IPOL_T0
	sVec2 slopeT0;
#endif
#ifdef IPOL_T1
	sVec2 slopeT1;
#endif

	// apply top-left fill-convention, left
	xStart = irr::core::ceil32( data->x[0] );
	xEnd = irr::core::ceil32( data->x[1] ) - 1;

	dx = xEnd - xStart;

	if ( dx < 0 )
		return;

	// slopes
	invDeltaX = data->x[1] - data->x[0];
	invDeltaX = inverse32 ( invDeltaX );

#ifdef IPOL_Z
	slopeZ = (data->z[1] - data->z[0]) * invDeltaX;
#endif
#ifdef IPOL_W
	slopeW = (data->w[1] - data->w[0]) * invDeltaX;
#endif
#ifdef IPOL_C
	slopeC = (data->c[1] - data->c[0]) * invDeltaX;
#endif
#ifdef IPOL_T0
	slopeT0 = (data->t0[1] - data->t0[0]) * invDeltaX;
#endif
#ifdef IPOL_T1
	slopeT1 = (data->t1[1] - data->t1[0]) * invDeltaX;
#endif

#ifdef SUBTEXEL
	subPixel = ( (f32) xStart ) - data->x[0];
#ifdef IPOL_Z
	data->z[0] += slopeZ * subPixel;
#endif
#ifdef IPOL_W
	data->w[0] += slopeW * subPixel;
#endif
#ifdef IPOL_C
	data->c[0] += slopeC * subPixel;
#endif
#ifdef IPOL_T0
	data->t0[0] += slopeT0 * subPixel;
#endif
#ifdef IPOL_T1
	data->t1[0] += slopeT1 * subPixel;
#endif
#endif

	dst = lockedSurface + ( data->y * SurfaceWidth ) + xStart;

#ifdef USE_Z
	z = lockedZBuffer + ( data->y * SurfaceWidth ) + xStart;
#endif


#ifdef INVERSE_W
	f32 inversew;
#endif

	tFixPoint tx0;
	tFixPoint ty0;

	tFixPoint r0, g0, b0;
	tFixPoint r1, g1, b1;
	tFixPoint r2, g2, b2;


	for ( s32 i = 0; i <= dx; ++i )
	{
#ifdef CMP_Z
		if ( data->z[0] < z[i] )
#endif
#ifdef CMP_W
		if ( data->w[0] > z[i] )
#endif

		{
#ifdef INVERSE_W
			inversew = fix_inverse32 ( data->w[0] );

			tx0 = f32_to_fixPoint ( data->t0[0].x,inversew);
			ty0 = f32_to_fixPoint ( data->t0[0].y,inversew);
#else
			tx0 = f32_to_fixPoint ( data->t0[0].x );
			ty0 = f32_to_fixPoint ( data->t0[0].y );
#endif

			getSample_texture ( r0, g0, b0, &IT[0], tx0,ty0 );

			color_to_fix ( r1, g1, b1, dst[i] );

			r2 = clampfix_maxcolor ( r1 + r0 );
			g2 = clampfix_maxcolor ( g1 + g0 );
			b2 = clampfix_maxcolor ( b1 + b0 );


			dst[i] = fix_to_color ( r2, g2, b2 );

#ifdef WRITE_Z
			z[i] = data->z[0];
#endif
#ifdef WRITE_W
			z[i] = data->w[0];
#endif

		}

#ifdef IPOL_Z
		data->z[0] += slopeZ;
#endif
#ifdef IPOL_W
		data->w[0] += slopeW;
#endif
#ifdef IPOL_C
		data->c[0] += slopeC;
#endif
#ifdef IPOL_T0
		data->t0[0] += slopeT0;
#endif
#ifdef IPOL_T1
		data->t1[0] += slopeT1;
#endif
	}

}

void CTRTextureGouraudAdd2::drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c )
{
	sScanConvertData scan;
	sScanLineData line;


	// sort on height, y
	if ( a->Pos.y > b->Pos.y ) swapVertices(&a, &b);
	if ( a->Pos.y > c->Pos.y ) swapVertices(&a, &c);
	if ( b->Pos.y > c->Pos.y ) swapVertices(&b, &c);


	// calculate delta y of the edges
	scan.invDeltaY[0] = c->Pos.y - a->Pos.y;
	scan.invDeltaY[1] = b->Pos.y - a->Pos.y;
	scan.invDeltaY[2] = c->Pos.y - b->Pos.y;

	scan.invDeltaY[0] = inverse32 ( scan.invDeltaY[0] );
	scan.invDeltaY[1] = inverse32 ( scan.invDeltaY[1] );
	scan.invDeltaY[2] = inverse32 ( scan.invDeltaY[2] );

	if ( (f32) 0.0 == scan.invDeltaY[0] )
		return;

	// find if the major edge is left or right aligned
	f32 temp[4];

	temp[0] = a->Pos.x - c->Pos.x;
	temp[1] = a->Pos.y - c->Pos.y;
	temp[2] = b->Pos.x - a->Pos.x;
	temp[3] = b->Pos.y - a->Pos.y;

	scan.left = ( temp[0] * temp[3] - temp[1] * temp[2] ) > (f32) 0.0 ? 0 : 1;
	scan.right = 1 - scan.left;

	// calculate slopes for the major edge
	scan.slopeX[0] = (c->Pos.x - a->Pos.x) * scan.invDeltaY[0];
	scan.x[0] = a->Pos.x;

#ifdef IPOL_Z
	scan.slopeZ[0] = (c->Pos.z - a->Pos.z) * scan.invDeltaY[0];
	scan.z[0] = a->Pos.z;
#endif

#ifdef IPOL_W
	scan.slopeW[0] = (c->Pos.w - a->Pos.w) * scan.invDeltaY[0];
	scan.w[0] = a->Pos.w;
#endif

#ifdef IPOL_C
	scan.slopeC[0] = (c->Color - a->Color) * scan.invDeltaY[0];
	scan.c[0] = a->Color;
#endif

#ifdef IPOL_T0
	scan.slopeT0[0] = (c->Tex[0] - a->Tex[0]) * scan.invDeltaY[0];
	scan.t0[0] = a->Tex[0];
#endif

#ifdef IPOL_T1
	scan.slopeT1[0] = (c->Tex[1] - a->Tex[1]) * scan.invDeltaY[0];
	scan.t1[0] = a->Tex[1];
#endif

	// top left fill convention y run
	s32 yStart;
	s32 yEnd;
	s32 y;

#ifdef SUBTEXEL
	f32 subPixel;
#endif

	lockedSurface = (tVideoSample*)RenderTarget->lock();

#ifdef USE_Z
	lockedZBuffer = ZBuffer->lock();
#endif

#ifdef IPOL_T0
	IT[0].data = (tVideoSample*)IT[0].Texture->lock();
#endif

#ifdef IPOL_T1
	IT[1].data = (tVideoSample*)IT[1].Texture->lock();
#endif

	// rasterize upper sub-triangle
	if ( (f32) 0.0 != scan.invDeltaY[1]  )
	{
		// calculate slopes for top edge
		scan.slopeX[1] = (b->Pos.x - a->Pos.x) * scan.invDeltaY[1];
		scan.x[1] = a->Pos.x;

#ifdef IPOL_Z
		scan.slopeZ[1] = (b->Pos.z - a->Pos.z) * scan.invDeltaY[1];
		scan.z[1] = a->Pos.z;
#endif

#ifdef IPOL_W
		scan.slopeW[1] = (b->Pos.w - a->Pos.w) * scan.invDeltaY[1];
		scan.w[1] = a->Pos.w;
#endif

#ifdef IPOL_C
		scan.slopeC[1] = (b->Color - a->Color) * scan.invDeltaY[1];
		scan.c[1] = a->Color;
#endif

#ifdef IPOL_T0
		scan.slopeT0[1] = (b->Tex[0] - a->Tex[0]) * scan.invDeltaY[1];
		scan.t0[1] = a->Tex[0];
#endif

#ifdef IPOL_T1
		scan.slopeT1[1] = (b->Tex[1] - a->Tex[1]) * scan.invDeltaY[1];
		scan.t1[1] = a->Tex[1];
#endif

		// apply top-left fill convention, top part
		yStart = irr::core::ceil32( a->Pos.y );
		yEnd = irr::core::ceil32( b->Pos.y ) - 1;

#ifdef SUBTEXEL
		subPixel = ( (f32) yStart ) - a->Pos.y;

		// correct to pixel center
		scan.x[0] += scan.slopeX[0] * subPixel;
		scan.x[1] += scan.slopeX[1] * subPixel;		

#ifdef IPOL_Z
		scan.z[0] += scan.slopeZ[0] * subPixel;
		scan.z[1] += scan.slopeZ[1] * subPixel;		
#endif

#ifdef IPOL_W
		scan.w[0] += scan.slopeW[0] * subPixel;
		scan.w[1] += scan.slopeW[1] * subPixel;		
#endif

#ifdef IPOL_C
		scan.c[0] += scan.slopeC[0] * subPixel;
		scan.c[1] += scan.slopeC[1] * subPixel;		
#endif

#ifdef IPOL_T0
		scan.t0[0] += scan.slopeT0[0] * subPixel;
		scan.t0[1] += scan.slopeT0[1] * subPixel;		
#endif

#ifdef IPOL_T1
		scan.t1[0] += scan.slopeT1[0] * subPixel;
		scan.t1[1] += scan.slopeT1[1] * subPixel;		
#endif

#endif

		// rasterize the edge scanlines
		for( y = yStart; y <= yEnd; ++y)
		{
			line.y = y;

			line.x[scan.left] = scan.x[0];
			line.x[scan.right] = scan.x[1];

#ifdef IPOL_Z
			line.z[scan.left] = scan.z[0];
			line.z[scan.right] = scan.z[1];
#endif

#ifdef IPOL_W
			line.w[scan.left] = scan.w[0];
			line.w[scan.right] = scan.w[1];
#endif

#ifdef IPOL_C
			line.c[scan.left] = scan.c[0];
			line.c[scan.right] = scan.c[1];
#endif

#ifdef IPOL_T0
			line.t0[scan.left] = scan.t0[0];
			line.t0[scan.right] = scan.t0[1];
#endif

#ifdef IPOL_T1
			line.t1[scan.left] = scan.t1[0];
			line.t1[scan.right] = scan.t1[1];
#endif

			// render a scanline
			scanline_bilinear ( &line );

			scan.x[0] += scan.slopeX[0];
			scan.x[1] += scan.slopeX[1];

#ifdef IPOL_Z
			scan.z[0] += scan.slopeZ[0];
			scan.z[1] += scan.slopeZ[1];
#endif

#ifdef IPOL_W
			scan.w[0] += scan.slopeW[0];
			scan.w[1] += scan.slopeW[1];
#endif

#ifdef IPOL_C
			scan.c[0] += scan.slopeC[0];
			scan.c[1] += scan.slopeC[1];
#endif

#ifdef IPOL_T0
			scan.t0[0] += scan.slopeT0[0];
			scan.t0[1] += scan.slopeT0[1];
#endif

#ifdef IPOL_T1
			scan.t1[0] += scan.slopeT1[0];
			scan.t1[1] += scan.slopeT1[1];
#endif

		}
	}

	// rasterize lower sub-triangle
	if ( (f32) 0.0 != scan.invDeltaY[2] )
	{
		// advance to middle point
		if( (f32) 0.0 != scan.invDeltaY[1] )
		{
			temp[0] = b->Pos.y - a->Pos.y;	// dy

			scan.x[0] = a->Pos.x + scan.slopeX[0] * temp[0];
#ifdef IPOL_Z
			scan.z[0] = a->Pos.z + scan.slopeZ[0] * temp[0];
#endif
#ifdef IPOL_W
			scan.w[0] = a->Pos.w + scan.slopeW[0] * temp[0];
#endif
#ifdef IPOL_C
			scan.c[0] = a->Color + scan.slopeC[0] * temp[0];
#endif
#ifdef IPOL_T0
			scan.t0[0] = a->Tex[0] + scan.slopeT0[0] * temp[0];
#endif
#ifdef IPOL_T1
			scan.t1[0] = a->Tex[1] + scan.slopeT1[0] * temp[0];
#endif

		}

		// calculate slopes for bottom edge
		scan.slopeX[1] = (c->Pos.x - b->Pos.x) * scan.invDeltaY[2];
		scan.x[1] = b->Pos.x;

#ifdef IPOL_Z
		scan.slopeZ[1] = (c->Pos.z - b->Pos.z) * scan.invDeltaY[2];
		scan.z[1] = b->Pos.z;
#endif

#ifdef IPOL_W
		scan.slopeW[1] = (c->Pos.w - b->Pos.w) * scan.invDeltaY[2];
		scan.w[1] = b->Pos.w;
#endif

#ifdef IPOL_C
		scan.slopeC[1] = (c->Color - b->Color) * scan.invDeltaY[2];
		scan.c[1] = b->Color;
#endif

#ifdef IPOL_T0
		scan.slopeT0[1] = (c->Tex[0] - b->Tex[0]) * scan.invDeltaY[2];
		scan.t0[1] = b->Tex[0];
#endif

#ifdef IPOL_T1
		scan.slopeT1[1] = (c->Tex[1] - b->Tex[1]) * scan.invDeltaY[2];
		scan.t1[1] = b->Tex[1];
#endif

		// apply top-left fill convention, top part
		yStart = irr::core::ceil32( b->Pos.y );
		yEnd = irr::core::ceil32( c->Pos.y ) - 1;

#ifdef SUBTEXEL

		subPixel = ( (f32) yStart ) - b->Pos.y;

		// correct to pixel center
		scan.x[0] += scan.slopeX[0] * subPixel;
		scan.x[1] += scan.slopeX[1] * subPixel;		

#ifdef IPOL_Z
		scan.z[0] += scan.slopeZ[0] * subPixel;
		scan.z[1] += scan.slopeZ[1] * subPixel;		
#endif

#ifdef IPOL_W
		scan.w[0] += scan.slopeW[0] * subPixel;
		scan.w[1] += scan.slopeW[1] * subPixel;		
#endif

#ifdef IPOL_C
		scan.c[0] += scan.slopeC[0] * subPixel;
		scan.c[1] += scan.slopeC[1] * subPixel;		
#endif

#ifdef IPOL_T0
		scan.t0[0] += scan.slopeT0[0] * subPixel;
		scan.t0[1] += scan.slopeT0[1] * subPixel;		
#endif

#ifdef IPOL_T1
		scan.t1[0] += scan.slopeT1[0] * subPixel;
		scan.t1[1] += scan.slopeT1[1] * subPixel;		
#endif

#endif

		// rasterize the edge scanlines
		for( y = yStart; y <= yEnd; ++y)
		{
			line.y = y;
			line.x[scan.left] = scan.x[0];
			line.x[scan.right] = scan.x[1];

#ifdef IPOL_Z
			line.z[scan.left] = scan.z[0];
			line.z[scan.right] = scan.z[1];
#endif

#ifdef IPOL_W
			line.w[scan.left] = scan.w[0];
			line.w[scan.right] = scan.w[1];
#endif

#ifdef IPOL_C
			line.c[scan.left] = scan.c[0];
			line.c[scan.right] = scan.c[1];
#endif

#ifdef IPOL_T0
			line.t0[scan.left] = scan.t0[0];
			line.t0[scan.right] = scan.t0[1];
#endif

#ifdef IPOL_T1
			line.t1[scan.left] = scan.t1[0];
			line.t1[scan.right] = scan.t1[1];
#endif

			// render a scanline
			scanline_bilinear ( &line );

			scan.x[0] += scan.slopeX[0];
			scan.x[1] += scan.slopeX[1];

#ifdef IPOL_Z
			scan.z[0] += scan.slopeZ[0];
			scan.z[1] += scan.slopeZ[1];
#endif

#ifdef IPOL_W
			scan.w[0] += scan.slopeW[0];
			scan.w[1] += scan.slopeW[1];
#endif

#ifdef IPOL_C
			scan.c[0] += scan.slopeC[0];
			scan.c[1] += scan.slopeC[1];
#endif

#ifdef IPOL_T0
			scan.t0[0] += scan.slopeT0[0];
			scan.t0[1] += scan.slopeT0[1];
#endif

#ifdef IPOL_T1
			scan.t1[0] += scan.slopeT1[0];
			scan.t1[1] += scan.slopeT1[1];
#endif

		}
	}

	RenderTarget->unlock();

#ifdef USE_Z
	ZBuffer->unlock();
#endif

#ifdef IPOL_T0
	IT[0].Texture->unlock();
#endif

#ifdef IPOL_T1
	IT[1].Texture->unlock();
#endif

}






//! creates a flat triangle renderer
ITriangleRenderer2* createTRTextureGouraudAdd2(IZBuffer2* zbuffer)
{
	return new CTRTextureGouraudAdd2(zbuffer);
}


} // end namespace video
} // end namespace irr



