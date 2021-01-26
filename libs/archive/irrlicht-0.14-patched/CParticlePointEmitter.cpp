// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CParticlePointEmitter.h"
#include "os.h"

namespace irr
{
namespace scene
{

//! constructor
CParticlePointEmitter::CParticlePointEmitter(
	core::vector3df direction, 	u32 minParticlesPerSecond,
	u32 maxParticlePerSecond,	video::SColor minStartColor,
	video::SColor maxStartColor, u32 lifeTimeMin, u32 lifeTimeMax,
	s32 maxAngleDegrees)
 : Direction(direction), MinParticlesPerSecond(minParticlesPerSecond),
	MaxParticlesPerSecond(maxParticlePerSecond), 
	MinStartColor(minStartColor), MaxStartColor(maxStartColor),
	MinLifeTime(lifeTimeMin), MaxLifeTime(lifeTimeMax), Time(0), Emitted(0),
	MaxAngleDegrees(maxAngleDegrees)
{

}



//! Prepares an array with new particles to emitt into the system
//! and returns how much new particles there are.
s32 CParticlePointEmitter::emitt(u32 now, u32 timeSinceLastCall, SParticle*& outArray)
{
	Time += timeSinceLastCall;

	u32 pps = (MaxParticlesPerSecond - MinParticlesPerSecond);
	f32 perSecond = pps ? (f32)MinParticlesPerSecond + (os::Randomizer::rand() % pps) : MinParticlesPerSecond;
	f32 everyWhatMillisecond = 1000.0f / perSecond;

	if (Time > everyWhatMillisecond)
	{
		Time = 0;
		Particle.startTime = now;
		Particle.vector = Direction;

		if (MaxAngleDegrees)
		{
			core::vector3df tgt = Direction;
			tgt.rotateXYBy((os::Randomizer::rand()%(MaxAngleDegrees*2)) - MaxAngleDegrees, core::vector3df(0,0,0));
			tgt.rotateYZBy((os::Randomizer::rand()%(MaxAngleDegrees*2)) - MaxAngleDegrees, core::vector3df(0,0,0));
			Particle.vector = tgt;
		}

		if (MaxLifeTime - MinLifeTime == 0)
			Particle.endTime = now + MinLifeTime;
		else
			Particle.endTime = now + MinLifeTime + (os::Randomizer::rand() % (MaxLifeTime - MinLifeTime));

		Particle.color = MinStartColor.getInterpolated(
			MaxStartColor, (os::Randomizer::rand() % 100) / 100.0f);

		Particle.startColor = Particle.color;
		Particle.startVector = Particle.vector;
		outArray = &Particle;
		return 1;
	}

	return 0;
}

} // end namespace scene
} // end namespace irr

