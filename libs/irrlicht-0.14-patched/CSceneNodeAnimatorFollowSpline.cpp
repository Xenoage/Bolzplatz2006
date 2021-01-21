// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSceneNodeAnimatorFollowSpline.h"

namespace irr
{
namespace scene
{

//! constructor
CSceneNodeAnimatorFollowSpline::CSceneNodeAnimatorFollowSpline(u32 time,
	const core::array< core::vector3df >& points, f32 speed,
	f32 tightness)
: Points(points), Speed(speed), StartTime(time), Tightness(tightness)
{
	NumPoints = Points.size();

	#ifdef _DEBUG
	setDebugName("CSceneNodeAnimatorFollowSpline");
	#endif
}



//! destructor
CSceneNodeAnimatorFollowSpline::~CSceneNodeAnimatorFollowSpline()
{
}



inline s32 CSceneNodeAnimatorFollowSpline::clamp(s32 idx, s32 size)
{
	return ( idx<0 ? size+idx : ( idx>=size ? idx-size : idx ) );
}


//! animates a scene node
void CSceneNodeAnimatorFollowSpline::animateNode(ISceneNode* node, u32 timeMs)
{
	core::vector3df p, p0, p1, p2, p3;
	core::vector3df t1, t2;

	f32 dt = ( (timeMs-StartTime) * Speed );
	s32 idx = static_cast< s32 >( 0.001f * dt ) % NumPoints;
	f32 u = 0.001f * fmodf( dt, 1000.0f );
    
	p0 = Points[ clamp( idx - 1, NumPoints ) ];
	p1 = Points[ clamp( idx, NumPoints ) ];
	p2 = Points[ clamp( idx + 1, NumPoints ) ];
	p3 = Points[ clamp( idx + 2, NumPoints ) ];

    // hermite polynomials
    f32 h1 = 2.0f * u * u * u - 3.0f * u * u + 1.0f;
    f32 h2 = -2.0f * u * u * u + 3.0f * u * u;
    f32 h3 = u * u * u - 2.0f * u * u + u;
    f32 h4 = u * u * u - u * u;

    // tangents
	t1 = ( p2 - p0 ) * Tightness;
	t2 = ( p3 - p1 ) * Tightness;

    // interpolated point
	p = p1 * h1 + p2 * h2 + t1 * h3 + t2 * h4;
		
	node->setPosition(p);
}


} // end namespace scene
} // end namespace irr

