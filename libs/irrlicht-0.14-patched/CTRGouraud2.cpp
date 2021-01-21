// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CTRGouraud2.h"
#include "os.h"
#include "SColor.h"


namespace irr
{

namespace video
{

//! constructor
CTRGouraud2::CTRGouraud2(IZBuffer2* zbuffer)
:	RenderTarget(0),	BackFaceCullingEnabled(true), SurfaceHeight(0), SurfaceWidth(0)
//	,Texture(0)
	,ZBuffer(zbuffer)
{
	#ifdef _DEBUG
	setDebugName("CTRGouraud2");
	#endif

	if (ZBuffer)
		zbuffer->grab();
}



//! destructor
CTRGouraud2::~CTRGouraud2()
{
	if (RenderTarget)
		RenderTarget->drop();

	if (ZBuffer)
		ZBuffer->drop();

//	if (Texture)
//		Texture->drop();
}



//! sets the Texture
void CTRGouraud2::setTexture(video::IImage* texture)
{
/*
	if (Texture)
		Texture->drop();

	Texture = texture;

	if (Texture)
	{
		Texture->grab();
		lockedTextureWidth = Texture->getDimension().Width;

		textureXMask = lockedTextureWidth-1;
		textureYMask = Texture->getDimension().Height-1;

		textureXMask2 = s32_to_fixPoint ( textureXMask );
		textureYMask2 = s32_to_fixPoint ( textureYMask );
	}
*/
}




//! en or disables the backface culling
void CTRGouraud2::setBackfaceCulling(bool enabled)
{
	BackFaceCullingEnabled = enabled;
}



//! sets a render target
void CTRGouraud2::setRenderTarget(video::IImage* surface, const core::rect<s32>& viewPort)
{
	if (RenderTarget)
		RenderTarget->drop();

	RenderTarget = surface;

	if (RenderTarget)
	{
		SurfaceWidth = RenderTarget->getDimension().Width;
		SurfaceHeight = RenderTarget->getDimension().Height;
		RenderTarget->grab();
		ViewPortRect = viewPort;
	}		
}




/*!
*/
inline void CTRGouraud2::scanline_point_sample ( sScanLineData * data ) const
{
	u16 *dst;
	TZBufferType2 *z;

	s32 xStart;
	s32 xEnd;
	s32 dx;

	f32 invDeltaX;

	f32 subPixel;

	f32 slopeZ;
	f32 slopeW;
	sVec4 slopeC;
	//core::vector2d<f32> slopeT;

	// apply top-left fill-convention, left
	xStart = ceil32( data->x[0] );
	xEnd = ceil32( data->x[1] ) - 1;

	dx = xEnd - xStart;

	if ( dx < 0 )
		return;

	// slopes
	invDeltaX = data->x[1] - data->x[0];
	invDeltaX = inverse32 ( invDeltaX );

	slopeZ = (data->z[1] - data->z[0]) * invDeltaX;
	slopeW = (data->w[1] - data->w[0]) * invDeltaX;
	slopeC = (data->c[1] - data->c[0]) * invDeltaX;
	//slopeT = (data->t[1] - data->t[0]) * invDeltaX;

	subPixel = ( (f32) xStart ) - data->x[0];
	data->z[0] += slopeZ * subPixel;
	data->w[0] += slopeW * subPixel;
	data->c[0] += slopeC * subPixel;
	//data->t[0] += slopeT * subPixel;


	dst = lockedSurface + ( data->y * SurfaceWidth ) + xStart;
	z = lockedZBuffer + ( data->y * SurfaceWidth ) + xStart;


	f32 inversew;
	sVec4 c_perspective;

	for ( s32 i = 0; i <= dx; ++i )
	{

		if ( data->z[0] > z[i] )
		{
			inversew = inverse32 ( data->w[0] );

			c_perspective = data->c[0] * inversew;
			dst[i] = c_perspective.getA1R5G6B5 ();

			z[i] = data->z[0];
		}

		data->z[0] += slopeZ;
		data->w[0] += slopeW;
		data->c[0] += slopeC;
		//data->t[0] += slopeT;
	}

}

void CTRGouraud2::drawTriangle ( const s4DVertex *a,const s4DVertex *b,const s4DVertex *c )
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

	scan.slopeZ[0] = (c->Pos.z - a->Pos.z) * scan.invDeltaY[0];
	scan.z[0] = a->Pos.z;

	scan.slopeW[0] = (c->Pos.w - a->Pos.w) * scan.invDeltaY[0];
	scan.w[0] = a->Pos.w;

	scan.slopeC[0] = (c->Color - a->Color) * scan.invDeltaY[0];
	scan.c[0] = a->Color;

	//scan.slopeT[0] = (c->TCoords - a->TCoords) * scan.invDeltaY[0];
	//scan.t[0] = a->TCoords;

	// top left fill convention y run
	s32 yStart;
	s32 yEnd;
	s32 y;

	f32 subPixel;

	lockedSurface = (u16*)RenderTarget->lock();
	lockedZBuffer = ZBuffer->lock();
	//lockedTexture = (u16*)Texture->lock();

	// rasterize upper sub-triangle
	if ( (f32) 0.0 != scan.invDeltaY[1]  )
	{
		// calculate slopes for top edge
		scan.slopeX[1] = (b->Pos.x - a->Pos.x) * scan.invDeltaY[1];
		scan.x[1] = a->Pos.x;

		scan.slopeZ[1] = (b->Pos.z - a->Pos.z) * scan.invDeltaY[1];
		scan.z[1] = a->Pos.z;

		scan.slopeW[1] = (b->Pos.w - a->Pos.w) * scan.invDeltaY[1];
		scan.w[1] = a->Pos.w;

		scan.slopeC[1] = (b->Color - a->Color) * scan.invDeltaY[1];
		scan.c[1] = a->Color;

		//scan.slopeT[1] = (b->TCoords - a->TCoords) * scan.invDeltaY[1];
		//scan.t[1] = a->TCoords;

		// apply top-left fill convention, top part
		yStart = ceil32( a->Pos.y );
		yEnd = ceil32( b->Pos.y ) - 1;

		subPixel = ( (f32) yStart ) - a->Pos.y;

		// correct to pixel center
		scan.x[0] += scan.slopeX[0] * subPixel;
		scan.x[1] += scan.slopeX[1] * subPixel;		

		scan.z[0] += scan.slopeZ[0] * subPixel;
		scan.z[1] += scan.slopeZ[1] * subPixel;		

		scan.w[0] += scan.slopeW[0] * subPixel;
		scan.w[1] += scan.slopeW[1] * subPixel;		

		scan.c[0] += scan.slopeC[0] * subPixel;
		scan.c[1] += scan.slopeC[1] * subPixel;		

		//scan.t[0] += scan.slopeT[0] * subPixel;
		//scan.t[1] += scan.slopeT[1] * subPixel;		

		// rasterize the edge scanlines
		for( y = yStart; y <= yEnd; ++y)
		{
			line.y = y;

			line.x[scan.left] = scan.x[0];
			line.x[scan.right] = scan.x[1];

			line.z[scan.left] = scan.z[0];
			line.z[scan.right] = scan.z[1];

			line.w[scan.left] = scan.w[0];
			line.w[scan.right] = scan.w[1];

			line.c[scan.left] = scan.c[0];
			line.c[scan.right] = scan.c[1];

			//line.t[scan.left] = scan.t[0];
			//line.t[scan.right] = scan.t[1];

			// render a scanline
			scanline_point_sample ( &line );

			scan.x[0] += scan.slopeX[0];
			scan.x[1] += scan.slopeX[1];

			scan.z[0] += scan.slopeZ[0];
			scan.z[1] += scan.slopeZ[1];

			scan.w[0] += scan.slopeW[0];
			scan.w[1] += scan.slopeW[1];

			scan.c[0] += scan.slopeC[0];
			scan.c[1] += scan.slopeC[1];

			//scan.t[0] += scan.slopeT[0];
			//scan.t[1] += scan.slopeT[1];

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
			scan.z[0] = a->Pos.z + scan.slopeZ[0] * temp[0];
			scan.w[0] = a->Pos.w + scan.slopeW[0] * temp[0];
			scan.c[0] = a->Color + scan.slopeC[0] * temp[0];
			//scan.t[0] = a->TCoords + scan.slopeT[0] * temp[0];

		}

		// calculate slopes for bottom edge
		scan.slopeX[1] = (c->Pos.x - b->Pos.x) * scan.invDeltaY[2];
		scan.x[1] = b->Pos.x;

		scan.slopeZ[1] = (c->Pos.z - b->Pos.z) * scan.invDeltaY[2];
		scan.z[1] = b->Pos.z;

		scan.slopeW[1] = (c->Pos.w - b->Pos.w) * scan.invDeltaY[2];
		scan.w[1] = b->Pos.w;

		scan.slopeC[1] = (c->Color - b->Color) * scan.invDeltaY[2];
		scan.c[1] = b->Color;

		//scan.slopeT[1] = (c->TCoords - b->TCoords) * scan.invDeltaY[2];
		//scan.t[1] = b->TCoords;

		// apply top-left fill convention, top part
		yStart = ceil32( b->Pos.y );
		yEnd = ceil32( c->Pos.y ) - 1;

		subPixel = ( (f32) yStart ) - b->Pos.y;

		// correct to pixel center
		scan.x[0] += scan.slopeX[0] * subPixel;
		scan.x[1] += scan.slopeX[1] * subPixel;		

		scan.z[0] += scan.slopeZ[0] * subPixel;
		scan.z[1] += scan.slopeZ[1] * subPixel;		

		scan.w[0] += scan.slopeW[0] * subPixel;
		scan.w[1] += scan.slopeW[1] * subPixel;		

		scan.c[0] += scan.slopeC[0] * subPixel;
		scan.c[1] += scan.slopeC[1] * subPixel;		

		//scan.t[0] += scan.slopeT[0] * subPixel;
		//scan.t[1] += scan.slopeT[1] * subPixel;		

		// rasterize the edge scanlines
		for( y = yStart; y <= yEnd; ++y)
		{
			line.y = y;
			line.x[scan.left] = scan.x[0];
			line.x[scan.right] = scan.x[1];

			line.z[scan.left] = scan.z[0];
			line.z[scan.right] = scan.z[1];

			line.w[scan.left] = scan.w[0];
			line.w[scan.right] = scan.w[1];

			line.c[scan.left] = scan.c[0];
			line.c[scan.right] = scan.c[1];

			//line.t[scan.left] = scan.t[0];
			//line.t[scan.right] = scan.t[1];

			// render a scanline
			scanline_point_sample ( &line );

			scan.x[0] += scan.slopeX[0];
			scan.x[1] += scan.slopeX[1];

			scan.z[0] += scan.slopeZ[0];
			scan.z[1] += scan.slopeZ[1];

			scan.w[0] += scan.slopeW[0];
			scan.w[1] += scan.slopeW[1];

			scan.c[0] += scan.slopeC[0];
			scan.c[1] += scan.slopeC[1];

			//scan.t[0] += scan.slopeT[0];
			//scan.t[1] += scan.slopeT[1];

		}
	}

	RenderTarget->unlock();
	ZBuffer->unlock();
	//Texture->unlock();

}





//! creates a flat triangle renderer
ITriangleRenderer2* createTriangleRendererGouraud2(IZBuffer2* zbuffer)
{
	return new CTRGouraud2(zbuffer);
}


} // end namespace video
} // end namespace irr



