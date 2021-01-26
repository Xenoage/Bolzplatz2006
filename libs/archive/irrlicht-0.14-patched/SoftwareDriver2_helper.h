// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __S_VIDEO_2_SOFTWARE_HELPER_H_INCLUDED__
#define __S_VIDEO_2_SOFTWARE_HELPER_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include <math.h>
#include "IImage.h"


namespace irr
{


//! used as zBuffer Type
typedef float TZBufferType2;


// ----------------------- Generic ----------------------------------

//! a more useful memset
inline void memset32 ( void * dest, const u32 value, u32 bytesize )
{
	u32 * d = (u32*) dest;

	u32 i;
	
	i = bytesize >> ( 2 + 3 );
	while( i )
	{
		d[0] = value;
		d[1] = value;
		d[2] = value;
		d[3] = value;

		d[4] = value;
		d[5] = value;
		d[6] = value;
		d[7] = value;

		d += 8;
		i -= 1;
	}

	i = (bytesize >> 2 ) & 7;
	while( i )
	{
		d[0] = value;
		d += 1;
		i -= 1;
	}

}

inline s32 ceil32 ( f32 x )
{
	return (s32) ceilf ( x );
}

inline f32 inverse32 ( f32 x )
{
	return 1.f / x;
}

// ------------------ Fix Point ----------------------------------

typedef int tFixPoint;
#define FIX_POINT_PRE			9
#define FIX_POINT_FRACT_MASK	0x1FF
#define FIX_POINT_SIGNED_MASK	0xFFFFFE00
#define FIX_POINT_UNSIGNED_MASK	0x7FFFFE00
#define FIX_POINT_ONE			0x200

/*
	convert signed integer to fixpoint
*/
inline tFixPoint s32_to_fixPoint (s32 x)
{
	return x << FIX_POINT_PRE;
}

/*
	convert float to fixpoint
*/
inline tFixPoint f32_to_fixPoint (f32 x)
{
	return (tFixPoint) (x * 512.f);
}


/*
	Fix Point Multiply
*/
inline tFixPoint imulFix(tFixPoint x, tFixPoint y)
{
	return (x * y) >> FIX_POINT_PRE;
}

/*
	Multiply x * y * 2
*/
inline tFixPoint imulFix2(tFixPoint x, tFixPoint y)
{
	return ( x * y) >> ( FIX_POINT_PRE + 4 );
}

/*
	Multiply x * y * 4
*/
inline tFixPoint imulFix3(tFixPoint x, tFixPoint y)
{
	return ( x * y) >> ( FIX_POINT_PRE + 3 );
}

/*!
	clamp FixPoint to 0-31 in FixPoint
*/
inline tFixPoint clampFix31 ( tFixPoint a)
{
	tFixPoint c = (a - 0x3E00) >> 31;
	return (a & c) + (0x3E00 & ~c);
}

/*
	x	FixPoint 1.22.9
	y	(TextureWidth - 1) must be 2^n
	used for texture wrapping
*/
inline tFixPoint imodFix(tFixPoint x, u32 y)
{
	return ( x & ( y << FIX_POINT_PRE ) ) | (x & FIX_POINT_FRACT_MASK);
}

/*!
	return A1R5G5B5 from fixpoint
*/
inline u16 getA1R5G5B5 ( tFixPoint r, tFixPoint g, tFixPoint b )
{
	return	( r & 0x3E00) << ( 10 - FIX_POINT_PRE) |
			( g & 0x3E00) >> (FIX_POINT_PRE  - 5 ) |
			( b & 0x3E00) >> FIX_POINT_PRE;
}


/*!
	return fixpoint from A1R5G5B5
*/
inline void getFixPoint_A1R5G5B5 ( tFixPoint &r, tFixPoint &g, tFixPoint &b, u32 t00 )
{
	r	 =	(t00 & 0x7C00) >> ( 10 - FIX_POINT_PRE);
	g	 =	(t00 & 0x03E0) << ( FIX_POINT_PRE - 5 );
	b	 =	(t00 & 0x001F) << ( FIX_POINT_PRE - 0 );
}



// ------------------------ Internal Texture -----------------------------

struct sInternalTexture
{
	u16* lockedTexture;
	s32 lockedTextureWidth;
	s32 textureXMask, textureYMask;

	video::IImage* Texture;
};


/*
	load a sample from internal texture at position tx,ty to fixpoint
*/
#ifndef SOFTWARE_DRIVER_2_BILINEAR

// get sample linear
inline void getSample_texture ( tFixPoint &r, tFixPoint &g, tFixPoint &b, 
									const sInternalTexture * t, tFixPoint tx, tFixPoint ty
									)
{
	s32 ix, iy;
	ix = tx >> FIX_POINT_PRE;
	iy = ty >> FIX_POINT_PRE;

	const u16* t0 = t->lockedTexture + (t->lockedTextureWidth * iy ) + ix;

	// texel
	u16 t00;
	t00 = t0[0];

	r	 =	(t00 & 0x7C00) >> ( 10 - FIX_POINT_PRE);
	g	 =	(t00 & 0x03E0) << ( FIX_POINT_PRE - 5 );
	b	 =	(t00 & 0x001F) << ( FIX_POINT_PRE - 0 );

}

#else

// get Samle bilinear
inline void getSample_texture ( tFixPoint &r, tFixPoint &g, tFixPoint &b, 
										const sInternalTexture * t, tFixPoint tx, tFixPoint ty
								)
{
	tFixPoint txFract = tx & FIX_POINT_FRACT_MASK;
	tFixPoint txFractInv = FIX_POINT_ONE - txFract;

	tFixPoint tyFract = ty & FIX_POINT_FRACT_MASK;
	tFixPoint tyFractInv = FIX_POINT_ONE - tyFract;

	/*  weight values for
		( x + 0, y + 0 )
		( x + 1, y + 0 )
		( x + 0, y + 1 )
		( x + 1, y + 1 )
	*/
	
	tFixPoint w00 = imulFix ( txFractInv, tyFractInv );
	tFixPoint w10 = imulFix ( txFract	, tyFractInv );
	tFixPoint w01 = imulFix ( txFractInv, tyFract );
	tFixPoint w11 = imulFix ( txFract	, tyFract );

	/* texel samples
		( x + 0, y + 0 )
		( x + 1, y + 0 )
		( x + 0, y + 1 )
		( x + 1, y + 1 )
	*/

	s32 ix, iy;

	ix = tx >> FIX_POINT_PRE;
	iy = ty >> FIX_POINT_PRE;

	const u16* t0 = t->lockedTexture + (t->lockedTextureWidth * iy ) + ix;
	const u16* t1 = t0 + t->lockedTextureWidth;

	// texel
	u16 t00;
	u16 t01;
	u16 t10;
	u16 t11;

	t00 = t0[0];
	t01 = t0[1];
	t10 = t1[0];
	t11 = t1[1];

	r	 =	(((t00 & 0x7C00) >> 10) * w00) + 
			(((t01 & 0x7C00) >> 10) * w10) + 
			(((t10 & 0x7C00) >> 10) * w01) + 
			(((t11 & 0x7C00) >> 10) * w11);

	g	 =	(((t00 & 0x03E0) >> 5) * w00) + 
			(((t01 & 0x03E0) >> 5) * w10) + 
			(((t10 & 0x03E0) >> 5) * w01) + 
			(((t11 & 0x03E0) >> 5) * w11);

	b	 =	(((t00 & 0x001F)     ) * w00) + 
			(((t01 & 0x001F)     ) * w10) + 
			(((t10 & 0x001F)     ) * w01) + 
			(((t11 & 0x001F)     ) * w11);

}

#endif



}


#endif 

