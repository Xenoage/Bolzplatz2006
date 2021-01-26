// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h


#ifndef __S_4D_VERTEX_H_INCLUDED__
#define __S_4D_VERTEX_H_INCLUDED__

#include "SoftwareDriver2_helper.h"
#include "S3DVertex.h"

namespace irr
{

namespace video
{

struct sVec4
{
	f32 x, y, z, w;

	sVec4 () {}


	sVec4 ( f32 _x, f32 _y, f32 _z, f32 _w )
		: x ( _x ), y ( _y ), z( _z ), w ( _w ){}

	void set ( f32 _x, f32 _y, f32 _z, f32 _w )
	{
		x = _x;
		y = _y;
		z = _z;
		w = _w;
	}

	void setA8R8G8B8 ( u32 argb )
	{
		x = ( ( argb & 0xFF000000 ) >> 24 ) / 255.f;
		y = ( ( argb & 0x00FF0000 ) >> 16 ) / 255.f;
		z = ( ( argb & 0x0000FF00 ) >>  8 ) / 255.f;
		w = ( ( argb & 0x000000FF )       ) / 255.f;
	}

	u16 getA1R5G5B5 () const
	{
		return	( (u16) (x * 1.f) ) << 15 |
				( (u16) (y * 32.f) ) << 10 |
				( (u16) (z * 32.f) ) << 5 |
				( (u16) (w * 32.f) );
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
	core::vector2d<f32> TCoords0;	
	core::vector2d<f32> TCoords1;	

	// f = a * t + b * ( 1 - t )
	void interpolate(const s4DVertex& b, const s4DVertex& a, const f32 t)
	{
		Pos.interpolate ( a.Pos, b.Pos, t );
		Color.interpolate ( a.Color, b.Color, t );
		TCoords0.interpolate ( a.TCoords0, b.TCoords0, t );
		TCoords1.interpolate ( a.TCoords1, b.TCoords1, t );
	}
};


inline void swapVertices(const s4DVertex** v1, const s4DVertex** v2)
{
	const s4DVertex* b = *v1;
	*v1 = *v2;
	*v2 = b;
}

}

}

#endif 

