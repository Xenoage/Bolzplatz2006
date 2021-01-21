// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CParticleFadeOutAffector.h"
#include "os.h"

namespace irr
{
namespace scene
{

//! constructor
CParticleFadeOutAffector::CParticleFadeOutAffector(
	video::SColor targetColor, u32 fadeOutTime)
	: TargetColor(targetColor)
{
	FadeOutTime = fadeOutTime ? (f32)fadeOutTime : 1.0f;
}


//! Affects an array of particles.
void CParticleFadeOutAffector::affect(u32 now, SParticle* particlearray, u32 count)
{
	f32 d;

	for (u32 i=0; i<count; ++i)
		if (particlearray[i].endTime - now < FadeOutTime)
		{
			d = (particlearray[i].endTime - now) / FadeOutTime;
			particlearray[i].color = particlearray[i].startColor.getInterpolated(
				TargetColor, d);
		}
}


} // end namespace scene
} // end namespace irr

