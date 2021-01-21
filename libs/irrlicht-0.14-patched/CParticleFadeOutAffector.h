// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_PARTICLE_FADE_OUT_AFFECTOR_H_INCLUDED__
#define __C_PARTICLE_FADE_OUT_AFFECTOR_H_INCLUDED__

#include "IParticleAffector.h"
#include "SColor.h"

namespace irr
{
namespace scene
{

//! Particle Affector for fading out a color
class CParticleFadeOutAffector : public IParticleAffector
{
public:

	CParticleFadeOutAffector(video::SColor targetColor, u32 fadeOutTime);

	//! Affects a particle.
	virtual void affect(u32 now, SParticle* particlearray, u32 count);

private:

	video::SColor TargetColor;
	f32 FadeOutTime;
};

} // end namespace scene
} // end namespace irr


#endif

