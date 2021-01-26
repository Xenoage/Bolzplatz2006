// Copyright (C) 2002-2006 Nikolaus Gebhardt
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
	s32 idx = irr::core::floor32( 0.001f * dt ) % Points.size();
	f32 u = 0.001f * fmodf( dt, 1000.0f );

	p0 = Points[ clamp( idx - 1, Points.size() ) ];
	p1 = Points[ clamp( idx, Points.size() ) ];
	p2 = Points[ clamp( idx + 1, Points.size() ) ];
	p3 = Points[ clamp( idx + 2, Points.size() ) ];

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


//! Writes attributes of the scene node animator.
void CSceneNodeAnimatorFollowSpline::serializeAttributes(io::IAttributes* out, io::SAttributeReadWriteOptions* options)
{
	out->addFloat("Speed", Speed);
	out->addFloat("Tightness", Tightness);

	u32 count = Points.size();

	if ( options && (options->Flags & io::EARWF_FOR_EDITOR))
	{
		// add one point in addition when serializing for editors
		// to make it easier to add points quickly
		count += 1;
	}

	for (u32 i=0; i<count; ++i)
	{
		core::stringc tname = "Point";
		tname += (int)(i+1);

		out->addVector3d(tname.c_str(), i<Points.size() ? Points[i] : core::vector3df(0,0,0) );
	}
}

//! Reads attributes of the scene node animator.
void CSceneNodeAnimatorFollowSpline::deserializeAttributes(io::IAttributes* in, io::SAttributeReadWriteOptions* options)
{
	Speed = in->getAttributeAsFloat("Speed");
	Tightness = in->getAttributeAsFloat("Tightness");
	Points.clear();

	for(u32 i=1; true; ++i)
	{
		core::stringc pname = "Point";
		pname += (int)i;

		if (in->existsAttribute(pname.c_str()))
		{
			core::vector3df pos = in->getAttributeAsVector3d(pname.c_str());
			Points.push_back(pos);
		}
		else
			break;
	}

	// remove last point if double entry from editor
	if ( options && (options->Flags & io::EARWF_FOR_EDITOR) &&
		Points.size() > 2 && Points.getLast() == core::vector3df(0,0,0))
	{
		Points.erase(Points.size()-1);

		if (Points.size() > 2 && Points.getLast() == core::vector3df(0,0,0))
			Points.erase(Points.size()-1);
	}
}


} // end namespace scene
} // end namespace irr

