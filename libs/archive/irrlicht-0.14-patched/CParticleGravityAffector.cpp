// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CParticleGravityAffector.h"
#include "os.h"

namespace irr
{
namespace scene
{

//! constructor
CParticleGravityAffector::CParticleGravityAffector(
	core::vector3df gravity,
		u32 timeForceLost)
	: Gravity(gravity), TimeForceLost((f32)timeForceLost)
{
}


//! Affects an array of particles.
void CParticleGravityAffector::affect(u32 now, SParticle* particlearray, u32 count)
{
	f32 d;

	for (u32 i=0; i<count; ++i)
	{
		d = (now - particlearray[i].startTime) / TimeForceLost;
		if (d > 1.0f) d = 1.0f;
		if (d < 0.0f) d = 0.0f;
		d = 1.0f - d;

		particlearray[i].vector = particlearray[i].startVector.getInterpolated(Gravity, d);
	}
}


} // end namespace scene
} // end namespace irr

