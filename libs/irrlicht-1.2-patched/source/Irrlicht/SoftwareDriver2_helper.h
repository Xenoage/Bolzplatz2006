// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

/*
	History:
	- changed behaviour for log2 textures ( replaced multiplys by shift )
*/

#ifndef __S_VIDEO_2_SOFTWARE_HELPER_H_INCLUDED__
#define __S_VIDEO_2_SOFTWARE_HELPER_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include "irrMath.h"
#include "CSoftwareTexture2.h"


namespace irr
{


//! used as zBuffer Type
typedef float TZBufferType2;

// supporting different packed pixel needs many defines...

#ifdef SOFTWARE_DRIVER_2_32BIT
	typedef u32	tVideoSample;

	#define	MASK_A	0xFF000000
	#define	MASK_R	0x00FF0000
	#define	MASK_G	0x0000FF00
	#define	MASK_B	0x000000FF

	#define	SHIFT_A	24
	#define	SHIFT_R	16
	#define	SHIFT_G	8
	#define	SHIFT_B	0

	#define	COLOR_MAX			0xFF
	#define	COLOR_MAX_INVERSE	0.00392157f

	#define VIDEO_SAMPLE_GRANULARITY	2
	#define	ECF_SOFTWARE2	ECF_A8R8G8B8

#else
	typedef u16	tVideoSample;

	#define	MASK_A	0x8000
	#define	MASK_R	0x7C00
	#define	MASK_G	0x03E0
	#define	MASK_B	0x001F

	#define	SHIFT_A	15
	#define	SHIFT_R	10
	#define	SHIFT_G	5
	#define	SHIFT_B	0

	#define	COLOR_MAX		0x1F
	#define	COLOR_MAX_INVERSE	0.0322581f
	#define VIDEO_SAMPLE_GRANULARITY	1
	#define	ECF_SOFTWARE2	ECF_A1R5G5B5

#endif




// ----------------------- Generic ----------------------------------

//! a more useful memset for pixel
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


// only for showing it's here.. precision lack, can't use
#if 0
inline f32 inverse32 ( const f32 p )
	{
		f32 r;
		u32 _i = 0x7F000000 - *(u32 *)&p;
		r = *(f32 *)&_i;
		r = r * (2.0f - p * r);
		return r;
	}
#else
	inline f32 inverse32 ( const f32 x )
	{
		return 1.f / x;
	}
#endif




// integer log2 of a float ieee 754. TODO: non ieee floating point
static inline s32 s32_log2_f32( f32 x)
{
	u32 ix = (u32&)x;
	u32 exp = (ix >> 23) & 0xFF;
	s32 log2 = s32(exp) - 127;

	return log2;
}

static inline s32 s32_log2(u32 x)
{
	return s32_log2_f32( (f32) x);
}

static inline s32 s32_abs(s32 x)
{
	s32 b = x >> 31;
	return (x ^ b ) - b;
}

// swap integer with xor
inline void swap_xor ( s32 &a, s32 &b )
{
	a ^= b;
	b ^= a;
	a ^= b;
}


inline s32 s32_min ( s32 a, s32 b)
{
	s32 mask = (a - b) >> 31;
	return (a & mask) | (b & ~mask);
}

inline s32 s32_max ( s32 a, s32 b)
{
	s32 mask = (a - b) >> 31;
	return (b & mask) | (a & ~mask);
}

inline s32 s32_clamp (s32 value, s32 low, s32 high) 
{
	return s32_min (s32_max(value,low), high);
}


#ifndef max
#define max(a,b)            (((a) > (b)) ? (a) : (b))
#endif

#ifndef min
#define min(a,b)            (((a) < (b)) ? (a) : (b))
#endif

template <class T>
inline const T clamp (const T value, const T low, const T high) 
{
	return min (max(value,low), high);
}

//! conditional set based on mask and arithemtic shift
inline u32 if_c_a_else_b ( s32 condition, u32 a, u32 b )
{
	return ( ( -condition >> 31 ) & ( a ^ b ) ) ^ b;
}


// ------------------ Video---------------------------------------
/*!
	Pixel = dest * ( 1 - alpha ) + source * alpha
	alpha [0;256]
*/
inline u32 PixelBlend32 ( const u32 c2, const u32 c1, u32 alpha )
{
	u32 srcRB = c1 & 0x00FF00FF;
	u32 srcXG = c1 & 0x0000FF00;

	u32 dstRB = c2 & 0x00FF00FF;
	u32 dstXG = c2 & 0x0000FF00;


	u32 rb = srcRB - dstRB;
	u32 xg = srcXG - dstXG;

	rb *= alpha;
	xg *= alpha;
	rb >>= 8;
	xg >>= 8;

	rb += dstRB;
	xg += dstXG;

	rb &= 0x00FF00FF;
	xg &= 0x0000FF00;

	return rb | xg; 
}

/*!
	Pixel = dest * ( 1 - alpha ) + source * alpha
	alpha [0;32]
*/
inline u16 PixelBlend16 ( const u16 c2, const u32 c1, const u32 alpha )
{
	u32 srcRB = c1 & 0x7C1F;
	u32 srcXG = c1 & 0x03E0;

	u32 dstRB = c2 & 0x7C1F;
	u32 dstXG = c2 & 0x03E0;


	u32 rb = srcRB - dstRB;
	u32 xg = srcXG - dstXG;

	rb *= alpha;
	xg *= alpha;
	rb >>= 5;
	xg >>= 5;

	rb += dstRB;
	xg += dstXG;

	rb &= 0x7C1F;
	xg &= 0x03E0;

	return rb | xg; 
}

/*!
	alpha 0 - 256
*/
inline u32 PixelLerp32 ( const u32 source, const u32 alpha )
{
	u32 srcRB = source & 0x00FF00FF;
	u32 srcXG = (source & 0xFF00FF00) >> 8;

	srcRB *= alpha;
	srcXG *= alpha;

	srcRB >>= 8;
	//srcXG >>= 8;

	srcRB &= 0x00FF00FF;
	srcXG &= 0xFF00FF00;

	return srcRB | srcXG; 
}

/*
	return alpha in [0;256] Granularity
	 add highbit alpha ( alpha > 127 ? + 1 )
*/
inline u32 extractAlpha ( u32 c )
{
	return ( c >> 24 ) + ( c >> 31 );
}

/*
	Pixel = c0 * (c1/31). c0 Alpha retain
*/
inline u16 PixelMul16 ( u16 c0, u16 c1)
{
	return	( ( (c0 & 0x7C00) * (c1 & 0x7C00) ) & 0x3E000000 ) >> 15 |
			( ( (c0 & 0x03E0) * (c1 & 0x03E0) ) & 0x000F8000 ) >> 10 |
			( ( (c0 & 0x001F) * (c1 & 0x001F) ) & 0x000003E0 ) >> 5  |
			c0 & 0x8000;
}

/*
	Pixel = c0 * (c1/31). 
*/
inline u16 PixelMul16_2 ( u16 c0, u16 c1)
{
	return	( ( (c0 & 0x7C00) * (c1 & 0x7C00) ) & 0x3E000000 ) >> 15 |
			( ( (c0 & 0x03E0) * (c1 & 0x03E0) ) & 0x000F8000 ) >> 10 |
			( ( (c0 & 0x001F) * (c1 & 0x001F) ) & 0x000003E0 ) >> 5  |
			( c0 & c1 & 0x8000);
}

/*
	Pixel = c0 * (c1/255). c0 Alpha Retain
*/
inline u32 PixelMul32 ( u32 c0, u32 c1)
{
	return	c0 & 0xFF000000 |
			( ( (c0 & 0x00FF0000) >> 12 ) * ( (c1 & 0x00FF0000) >> 12 ) ) & 0x00FF0000 |
			( ( (c0 & 0x0000FF00) * (c1 & 0x0000FF00) ) >> 16 ) & 0x0000FF00 |
			( ( (c0 & 0x000000FF) * (c1 & 0x000000FF) ) >> 8  ) & 0x000000FF;
}

/*
	Pixel = c0 * (c1/255). 
*/
inline u32 PixelMul32_2 ( u32 c0, u32 c1)
{
	return	( ( (c0 & 0xFF000000) >> 16 ) * ( (c1 & 0xFF000000) >> 16 ) ) & 0xFF000000 |
			( ( (c0 & 0x00FF0000) >> 12 ) * ( (c1 & 0x00FF0000) >> 12 ) ) & 0x00FF0000 |
			( ( (c0 & 0x0000FF00) * (c1 & 0x0000FF00) ) >> 16 ) & 0x0000FF00 |
			( ( (c0 & 0x000000FF) * (c1 & 0x000000FF) ) >> 8  ) & 0x000000FF;
}


// 1 - Bit Alpha Blending
inline u16 PixelBlend16 ( const u16 c2, const u16 c1 )
{
	u16 c = c1 & 0x8000;
	
	c >>= 15;
	c += 0x7fff;
	
	c &= c2;
	c |= c1;
	
	return c;
}

// 1 - Bit Alpha Blending 16Bit SIMD
inline u32 PixelBlend16_simd ( const u32 c2, const u32 c1 )
{
	u32 c = c1 & 0x80008000;
	
	c >>= 15;
	c += 0x7fff7fff;
	
	c &= c2;
	c |= c1;
	
	return c;
}


/*!
	Pixel = dest * ( 1 - SourceAlpha ) + source * SourceAlpha
*/
inline u32 PixelBlend32 ( const u32 c2, const u32 c1 )
{
	// alpha test
	u32 alpha = c1 & 0xFF000000;

	if ( 0 == alpha )
		return c2;

	if ( 0xFF000000 == alpha )
	{
		return c1;
	}

	alpha >>= 24;

	// add highbit alpha, if ( alpha > 127 ) alpha += 1;
	alpha += ( alpha >> 7);

	u32 srcRB = c1 & 0x00FF00FF;
	u32 srcXG = c1 & 0x0000FF00;

	u32 dstRB = c2 & 0x00FF00FF;
	u32 dstXG = c2 & 0x0000FF00;


	u32 rb = srcRB - dstRB;
	u32 xg = srcXG - dstXG;

	rb *= alpha;
	xg *= alpha;
	rb >>= 8;
	xg >>= 8;

	rb += dstRB;
	xg += dstXG;

	rb &= 0x00FF00FF;
	xg &= 0x0000FF00;

	return rb | xg; 
}



// ------------------ Fix Point ----------------------------------

typedef s32 tFixPoint;
typedef u32 tFixPointu;

#if 0
	#define FIX_POINT_PRE			7
	#define FIX_POINT_FRACT_MASK	0x7F
	#define FIX_POINT_SIGNED_MASK	0xFFFFFF80
	#define FIX_POINT_UNSIGNED_MASK	0x7FFFFF80
	#define FIX_POINT_ONE			0x80
	#define FIX_POINT_ZERO_DOT_FIVE	0x40
	#define FIX_POINT_F32_MUL		128.f
	#define FIX_POINT_F32_MUL_INVERSE		0.0078125f
#else
	#define FIX_POINT_PRE			9
	#define FIX_POINT_FRACT_MASK	0x1FF
	#define FIX_POINT_SIGNED_MASK	0xFFFFFE00
	#define FIX_POINT_UNSIGNED_MASK	0x7FFFFE00
	#define FIX_POINT_ONE			0x200
	#define FIX_POINT_ZERO_DOT_FIVE	0x100
	#define FIX_POINT_F32_MUL		512.f
	#define FIX_POINT_F32_MUL_INVERSE		0.001953125f

#endif

#define	FIXPOINT_COLOR_MAX		( COLOR_MAX << FIX_POINT_PRE )


/*
	convert signed integer to fixpoint
*/
inline tFixPoint s32_to_fixPoint (const s32 x)
{
	return x << FIX_POINT_PRE;
}

inline tFixPointu u32_to_fixPoint (const u32 x)
{
	return x << FIX_POINT_PRE;
}


// 1/x * FIX_POINT
inline f32 fix_inverse32 ( const f32 x )
{
	return FIX_POINT_F32_MUL / x;
}


/*
	convert float to fixpoint
	fast convert (fistp on x86) HAS to be used..
	hints: compileflag /QIfist for msvc7. msvc 8.0 has smth different
	others should use their favourite assembler.. 
*/
static inline int f_round2(float f)
{
	f += (3<<22);
	return *((int*)&f) - 0x4b400000;
}

/*
	convert f32 to Fix Point.
	multiply is needed anyway, so scale mulby
*/
inline tFixPoint f32_to_fixPoint (const f32 x, const f32 mulby = FIX_POINT_F32_MUL )
{
	return (tFixPoint) (x * mulby);
}

/*
	Fix Point , Fix Point Multiply
*/
inline tFixPoint imulFix(const tFixPoint x, const tFixPoint y)
{
	return (x * y) >> FIX_POINT_PRE;
}

/*
	Multiply x * y * 2
*/
inline tFixPoint imulFix2(const tFixPoint x, const tFixPoint y)
{
	return ( x * y) >> ( FIX_POINT_PRE + ( VIDEO_SAMPLE_GRANULARITY * 4 ) );
}

/*
	Multiply x * y * 4
*/
inline tFixPoint imulFix3(const tFixPoint x, const tFixPoint y)
{
#ifdef SOFTWARE_DRIVER_2_32BIT
	return ( ( x >> 2 ) * ( y >> 2 ) ) >> ( FIX_POINT_PRE + 2 );
#else
	return ( x * y) >> ( FIX_POINT_PRE + ( VIDEO_SAMPLE_GRANULARITY * 3 ) );
#endif
}

/*!
	clamp FixPoint to maxcolor in FixPoint, min(a,31)
*/
inline tFixPoint clampfix_maxcolor ( const tFixPoint a)
{
	tFixPoint c = (a - FIXPOINT_COLOR_MAX) >> 31;	// shift is maxInt
	return (a & c) + (FIXPOINT_COLOR_MAX & ~c);
}

/*!
	clamp FixPoint to 0 in FixPoint, max(a,0)
*/
inline tFixPoint clampfix_mincolor ( const tFixPoint a)
{
	return a - ( a & ( a >> 31 ) );
}


// rount fixpoint to int
inline s32 roundFix ( const tFixPoint x )
{
	return ( x + FIX_POINT_ZERO_DOT_FIVE ) >> FIX_POINT_PRE;
}



// x in [0;1[
inline s32 f32_to_23Bits(const f32 x)
{
    f32 y = x + 1.f;
    return ((u32&)y) & 0x7FFFFF;	// last 23 bits
}

/*!
	return VideoSample from fixpoint
*/
inline tVideoSample fix_to_color ( const tFixPoint r, const tFixPoint g, const tFixPoint b )
{
#ifdef __BIG_ENDIAN__
	return	( r & FIXPOINT_COLOR_MAX) >> ( FIX_POINT_PRE - 8) |
			( g & FIXPOINT_COLOR_MAX) << ( 16 - FIX_POINT_PRE ) |
			//( g & FIXPOINT_COLOR_MAX) << ( SHIFT_G - FIX_POINT_PRE ) |
			( b & FIXPOINT_COLOR_MAX) << ( 24 - FIX_POINT_PRE );
#else
	return	( r & FIXPOINT_COLOR_MAX) << ( SHIFT_R - FIX_POINT_PRE ) |
			( g & FIXPOINT_COLOR_MAX) >> ( FIX_POINT_PRE - SHIFT_G ) |
			//( g & FIXPOINT_COLOR_MAX) << ( SHIFT_G - FIX_POINT_PRE ) |
			( b & FIXPOINT_COLOR_MAX) >> ( FIX_POINT_PRE - SHIFT_B );
#endif
}


/*!
	return fixpoint from VideoSample
*/
inline void color_to_fix ( tFixPoint &r, tFixPoint &g, tFixPoint &b, const tVideoSample t00 )
{
	r	 =	(t00 & MASK_R) >> ( SHIFT_R - FIX_POINT_PRE );
	//g	 =	(t00 & MASK_G) >> ( SHIFT_G - FIX_POINT_PRE );
	g	 =	(t00 & MASK_G) << ( FIX_POINT_PRE - SHIFT_G );
	b	 =	(t00 & MASK_B) << ( FIX_POINT_PRE - SHIFT_B );
}



// ------------------------ Internal Texture -----------------------------

struct sInternalTexture
{
	u32 textureXMask, textureYMask;

	u32 pitch;
	u32 pitchlog2;
	void *data;

	video::CSoftwareTexture2 *Texture;
	//video::IImage* Texture;
};


// get sample linear
inline void getSample_linear ( tFixPointu &r, tFixPointu &g, tFixPointu &b, 
								const sInternalTexture * t, const tFixPointu tx, const tFixPointu ty
								)
{
	u32 ofs;

	//TODO: remember what you doin;-)
/*
	tx &= t->textureXMask;
	ty &= t->textureYMask;

	ofs = ( (ty & t->textureYMask ) >> FIX_POINT_PRE ) * t->pitch;
	ofs |= ( ( tx & t->textureXMask) & FIX_POINT_UNSIGNED_MASK ) >> ( FIX_POINT_PRE - VIDEO_SAMPLE_GRANULARITY );
*/
	ofs = ( ( ty & t->textureYMask ) >> FIX_POINT_PRE ) << t->pitchlog2;
	ofs |= ( tx & t->textureXMask ) >> ( FIX_POINT_PRE - VIDEO_SAMPLE_GRANULARITY );

	// texel
	tVideoSample t00;
	t00 = *((tVideoSample*)( (u8*) t->data + ofs ));

	r	 =	(t00 & MASK_R) >> SHIFT_R;
	g	 =	(t00 & MASK_G) >> SHIFT_G;
	b	 =	(t00 & MASK_B);

}

// get video sample plain
inline tVideoSample getSample_plain ( const sInternalTexture * t, const tFixPointu tx, const tFixPointu ty )
{
	u32 ofs;

	ofs = ( ( ty & t->textureYMask ) >> FIX_POINT_PRE ) << t->pitchlog2;
	ofs |= ( tx & t->textureXMask ) >> ( FIX_POINT_PRE - VIDEO_SAMPLE_GRANULARITY );

	// texel
	return *((tVideoSample*)( (u8*) t->data + ofs ));
}

/*
	load a sample from internal texture at position tx,ty to fixpoint
*/
#ifndef SOFTWARE_DRIVER_2_BILINEAR

// get Sample linear
inline void getSample_texture ( tFixPoint &r, tFixPoint &g, tFixPoint &b, 
								const sInternalTexture * t, const tFixPointu tx, const tFixPointu ty
								)
{
	u32 ofs;

	ofs = ( ( ty & t->textureYMask ) >> FIX_POINT_PRE ) << t->pitchlog2;
	ofs |= ( tx & t->textureXMask ) >> ( FIX_POINT_PRE - VIDEO_SAMPLE_GRANULARITY );

	// texel
	tVideoSample t00;
	t00 = *((tVideoSample*)( (u8*) t->data + ofs ));

	r	 =	(t00 & MASK_R) >> ( SHIFT_R - FIX_POINT_PRE);
	g	 =	(t00 & MASK_G) << ( FIX_POINT_PRE - SHIFT_G );
	b	 =	(t00 & MASK_B) << ( FIX_POINT_PRE - SHIFT_B );

}


#else

// get Sample bilinear
inline void getSample_texture ( tFixPoint &r, tFixPoint &g, tFixPoint &b, 
								const sInternalTexture * t, const tFixPointu tx, const tFixPointu ty
								)
{

	tFixPointu r00,g00,b00;
	tFixPointu r01,g01,b01;
	tFixPointu r10,g10,b10;
	tFixPointu r11,g11,b11;

	getSample_linear ( r00, g00, b00, t, tx,ty );
	getSample_linear ( r10, g10, b10, t, tx + FIX_POINT_ONE,ty );
	getSample_linear ( r01, g01, b01, t, tx,ty + FIX_POINT_ONE );
	getSample_linear ( r11, g11, b11, t, tx + FIX_POINT_ONE,ty + FIX_POINT_ONE );

	const tFixPointu txFract = tx & FIX_POINT_FRACT_MASK;
	const tFixPointu txFractInv = FIX_POINT_ONE - txFract;

	const tFixPointu tyFract = ty & FIX_POINT_FRACT_MASK;
	const tFixPointu tyFractInv = FIX_POINT_ONE - tyFract;

	const tFixPoint w00 = imulFix ( txFractInv, tyFractInv );
	const tFixPoint w10 = imulFix ( txFract	, tyFractInv );
	const tFixPoint w01 = imulFix ( txFractInv, tyFract );
	const tFixPoint w11 = imulFix ( txFract	, tyFract );

	r =		(r00 * w00 ) +
			(r01 * w01 ) +
			(r10 * w10 ) +
			(r11 * w11 );

	g =		(g00 * w00 ) +
			(g01 * w01 ) +
			(g10 * w10 ) +
			(g11 * w11 );

	b =		(b00 * w00 ) +
			(b01 * w01 ) +
			(b10 * w10 ) +
			(b11 * w11 );

}

#endif

}


#endif 

