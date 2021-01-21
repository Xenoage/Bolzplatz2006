// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_PARTICLE_GRAVITY_AFFECTOR_H_INCLUDED__
#define __C_PARTICLE_GRAVITY_AFFECTOR_H_INCLUDED__

#include "IParticleAffector.h"
#include "SColor.h"

namespace irr
{
namespace scene
{

//! Particle Affector for affecting direction of particle
class CParticleGravityAffector : public IParticleAffector
{
public:

	CParticleGravityAffector(
		core::vector3df gravity = core::vector3df(0.0f,-0.03f,0.0f),
		u32 timeForceLost = 1000);

	//! Affects a particle.
	virtual void affect(u32 now, SParticle* particlearray, u32 count);

private:

	f32 TimeForceLost;
	core::vector3df Gravity;
};

} // end namespace scene
} // end namespace irr


#endif

