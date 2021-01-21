// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CParticleBoxEmitter.h"
#include "os.h"
#include <math.h>

namespace irr
{
namespace scene
{

//! constructor
CParticleBoxEmitter::CParticleBoxEmitter(
	core::aabbox3d<f32> box,
	core::vector3df direction, 	u32 minParticlesPerSecond,
	u32 maxParticlePerSecond,	video::SColor minStartColor,
	video::SColor maxStartColor, u32 lifeTimeMin, u32 lifeTimeMax,
	s32 maxAngleDegrees)
 : Box(box), Direction(direction), MinParticlesPerSecond(minParticlesPerSecond),
	MaxParticlesPerSecond(maxParticlePerSecond), 
	MinStartColor(minStartColor), MaxStartColor(maxStartColor),
	MinLifeTime(lifeTimeMin), MaxLifeTime(lifeTimeMax), Time(0), Emitted(0),
	MaxAngleDegrees(maxAngleDegrees)
{

}



//! Prepares an array with new particles to emitt into the system
//! and returns how much new particles there are.
s32 CParticleBoxEmitter::emitt(u32 now, u32 timeSinceLastCall, SParticle*& outArray)
{
	Time += timeSinceLastCall;

	u32 pps = (MaxParticlesPerSecond - MinParticlesPerSecond);
	f32 perSecond = pps ? (f32)MinParticlesPerSecond + (os::Randomizer::rand() % pps) : MinParticlesPerSecond;
	f32 everyWhatMillisecond = 1000.0f / perSecond;

	if (Time > everyWhatMillisecond)
	{
		Particles.set_used(0);
		s32 amount = (s32)((Time / everyWhatMillisecond) + 0.5f);
        Time = 0;
		SParticle p;
		core::vector3df extend = Box.getExtent();

		if (amount > (s32)MaxParticlesPerSecond*2)
			amount = MaxParticlesPerSecond * 2;

		f64 len = Direction.getLength();

		for (s32 i=0; i<amount; ++i)
		{
			p.pos.X = Box.MinEdge.X + fmodf((f32)os::Randomizer::rand(), extend.X);
			p.pos.Y = Box.MinEdge.Y + fmodf((f32)os::Randomizer::rand(), extend.Y);
			p.pos.Z = Box.MinEdge.Z + fmodf((f32)os::Randomizer::rand(), extend.Z);

			p.startTime = now;
			p.vector = Direction;

			if (MaxAngleDegrees)
			{
				core::vector3df tgt = Direction;
				tgt.rotateXYBy((os::Randomizer::rand()%(MaxAngleDegrees*2)) - MaxAngleDegrees, core::vector3df(0,0,0));
				tgt.rotateYZBy((os::Randomizer::rand()%(MaxAngleDegrees*2)) - MaxAngleDegrees, core::vector3df(0,0,0));
				p.vector = tgt;
			}

			if (MaxLifeTime - MinLifeTime == 0)
				p.endTime = now + MinLifeTime;
			else
				p.endTime = now + MinLifeTime + (os::Randomizer::rand() % (MaxLifeTime - MinLifeTime));

			p.color = MinStartColor.getInterpolated(
				MaxStartColor, (os::Randomizer::rand() % 100) / 100.0f);

			p.startColor = p.color;
			p.startVector = p.vector;

			Particles.push_back(p);
		}

		outArray = Particles.pointer();

		return Particles.size();
	}

	return 0;
}

} // end namespace scene
} // end namespace irr

