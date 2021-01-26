// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h


#ifndef __S_4D_VERTEX_H_INCLUDED__
#define __S_4D_VERTEX_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include "SoftwareDriver2_helper.h"

namespace irr
{

namespace video
{

struct sVec2
{
	f32 x;
	f32 y;

	sVec2 () {}

	sVec2 ( f32 _x, f32 _y )
		: x ( _x ), y ( _y ) {}

	void set ( f32 _x, f32 _y )
	{
		x = _x;
		y = _y;
	}

	// f = a * t + b * ( 1 - t )
	void interpolate(const sVec2& a, const sVec2& b, const f32 t)
	{
		x = b.x + ( ( a.x - b.x ) * t );
		y = b.y + ( ( a.y - b.y ) * t );
	}

	sVec2 operator-(const sVec2& other) const
	{
		return sVec2(x - other.x, y - other.y);
	}

	sVec2 operator+(const sVec2& other) const
	{
		return sVec2(x + other.x, y + other.y);
	}

	void operator+=(const sVec2& other)
	{
		x += other.x;
		y += other.y;
	}

	sVec2 operator*(const f32 s) const
	{
		return sVec2(x * s , y * s);
	}

	void operator*=( const f32 s)
	{
		x *= s;
		y *= s;
	}

	void operator=(const sVec2& other)
	{
		x = other.x;
		y = other.y;
	}

};

inline f32 clamp(f32 a, f32 mn, f32 mx)
{
	if ( a < mn ) return mn;
	if ( a > mx ) return mx;
	return a;
}

struct sVec4
{
	f32 x, y, z, w;

	sVec4 () {}

	sVec4 ( f32 _x, f32 _y, f32 _z, f32 _w )
		: x ( _x ), y ( _y ), z( _z ), w ( _w ){}

/*
	sVec4( u32 argb )
	{
		x = ( ( argb & 0xFF000000 ) >> 24 ) / 255.f;
		y = ( ( argb & 0x00FF0000 ) >> 16 ) / 255.f;
		z = ( ( argb & 0x0000FF00 ) >>  8 ) / 255.f;
		w = ( ( argb & 0x000000FF )       ) / 255.f;
	}
*/

	void set ( f32 _x, f32 _y, f32 _z, f32 _w )
	{
		x = _x;
		y = _y;
		z = _z;
		w = _w;
	}

	void setA8R8G8B8 ( u32 argb )
	{
		x = ( ( argb & 0xFF000000 ) >> 24 ) * ( 1.f / 255.f );
		y = ( ( argb & 0x00FF0000 ) >> 16 ) * ( 1.f / 255.f );
		z = ( ( argb & 0x0000FF00 ) >>  8 ) * ( 1.f / 255.f );
		w = ( ( argb & 0x000000FF )       ) * ( 1.f / 255.f );
	}

	void setColorf ( const irr::video::SColorf & color )
	{
		x = color.a;
		y = color.r;
		z = color.g;
		w = color.b;
	}

	void clampToOne ()
	{
		x = clamp ( x, 0.f, 1.f );
		y = clamp ( y, 0.f, 1.f );
		z = clamp ( z, 0.f, 1.f );
		w = clamp ( w, 0.f, 1.f );
	}


	// f = a * t + b * ( 1 - t )
	void interpolate(const sVec4& a, const sVec4& b, const f32 t)
	{
		x = b.x + ( ( a.x - b.x ) * t );
		y = b.y + ( ( a.y - b.y ) * t );
		z = b.z + ( ( a.z - b.z ) * t );
		w = b.w + ( ( a.w - b.w ) * t );
	}


	f32 dotProduct(const sVec4& other) const
	{
		return x*other.x + y*other.y + z*other.z + w*other.w;
	}

	sVec4 operator-(const sVec4& other) const
	{
		return sVec4(x - other.x, y - other.y, z - other.z,w - other.w);
	}

	sVec4 operator+(const sVec4& other) const
	{
		return sVec4(x + other.x, y + other.y, z + other.z,w + other.w);
	}

	void operator+=(const sVec4& other)
	{
		x += other.x;
		y += other.y;
		z += other.z;
		w += other.w;
	}

	sVec4 operator*(f32 s) const
	{
		return sVec4(x * s , y * s, z * s,w * s);
	}

	sVec4 operator*(const sVec4 &other) const
	{
		return sVec4(x * other.x , y * other.y, z * other.z,w * other.w);
	}

	void operator*=(f32 s)
	{
		x *= s;
		y *= s;
		z *= s;
		w *= s;
	}

	void operator*=(const sVec4 &other)
	{
		x *= other.x;
		y *= other.y;
		z *= other.z;
		w *= other.w;
	}

	void operator=(const sVec4& other)
	{
		x = other.x;
		y = other.y;
		z = other.z;
		w = other.w;
	}

};


struct s4DVertex
{
	sVec4 Pos;
	sVec4 Color;

	sVec2 Tex[2];

	// f = a * t + b * ( 1 - t )
	void interpolate(const s4DVertex& b, const s4DVertex& a, const f32 t)
	{
		Pos.interpolate ( a.Pos, b.Pos, t );
		Color.interpolate ( a.Color, b.Color, t );
		Tex[0].interpolate ( a.Tex[0], b.Tex[0], t );
		Tex[1].interpolate ( a.Tex[1], b.Tex[1], t );
	}
};


inline void swapVertices(const s4DVertex** v1, const s4DVertex** v2)
{
	const s4DVertex* b = *v1;
	*v1 = *v2;
	*v2 = b;
}


// ------------------------ Internal Scanline Rasterizer -----------------------------


// internal scan convert
struct sScanConvertData
{
	s32 left;			// major edge left/right
	s32 right;		// !left

	f32 invDeltaY[3];	// inverse edge delta y

	f32 x[2];			// x coordinate
	f32 slopeX[2];		// x slope along edges

#if defined ( SOFTWARE_DRIVER_2_USE_WBUFFER ) || defined ( SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT )
	f32 w[2];			// w coordinate
	f32 slopeW[2];		// w slope along edges
#else
	f32 z[2];			// z coordinate
	f32 slopeZ[2];		// z slope along edges
#endif

	sVec4 c[2];			// color
	sVec4 slopeC[2];	// color slope along edges

	sVec2 t0[2];			// texture
	sVec2 slopeT0[2];	// texture slope along edges

	sVec2 t1[2];			// texture
	sVec2 slopeT1[2];	// texture slope along edges

};

// passed to scan Line
struct sScanLineData
{
	s32 y;			// y position of scanline
	f32 x[2];			// x start, x end of scanline

#if defined ( SOFTWARE_DRIVER_2_USE_WBUFFER ) || defined ( SOFTWARE_DRIVER_2_PERSPECTIVE_CORRECT )
	f32 w[2];			// w start, w end of scanline
#else
	f32 z[2];			// z start, z end of scanline
#endif

	sVec4 c[2];		// color start, color end of scanline
	sVec2 t0[2];		// texture start, texture end of scanline
	sVec2 t1[2];		// texture start, texture end of scanline
};


/*
	load a color value
*/
inline void getSample_plain (	tFixPoint &r, tFixPoint &g, tFixPoint &b, 
							const sVec4 &v
							)
{
	r = f32_to_fixPoint ( v.y );
	g = f32_to_fixPoint ( v.z );
	b = f32_to_fixPoint ( v.w );
}

/*
	load a color value
*/
inline void getSample_color (	tFixPoint &a, tFixPoint &r, tFixPoint &g, tFixPoint &b, 
							const sVec4 &v
							)
{
	a = f32_to_fixPoint ( v.x );
	r = f32_to_fixPoint ( v.y, COLOR_MAX * FIX_POINT_F32_MUL);
	g = f32_to_fixPoint ( v.z, COLOR_MAX * FIX_POINT_F32_MUL);
	b = f32_to_fixPoint ( v.w, COLOR_MAX * FIX_POINT_F32_MUL);
}

/*
	load a color value
*/
inline void getSample_color (	tFixPoint &r, tFixPoint &g, tFixPoint &b, 
							const sVec4 &v
							)
{
	r = f32_to_fixPoint ( v.y, COLOR_MAX * FIX_POINT_F32_MUL);
	g = f32_to_fixPoint ( v.z, COLOR_MAX * FIX_POINT_F32_MUL);
	b = f32_to_fixPoint ( v.w, COLOR_MAX * FIX_POINT_F32_MUL);
}



}

}

#endif 

