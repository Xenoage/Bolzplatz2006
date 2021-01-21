// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_PARTICLE_SYSTEM_SCENE_NODE_H_INCLUDED__
#define __C_PARTICLE_SYSTEM_SCENE_NODE_H_INCLUDED__

#include "IParticleSystemSceneNode.h"
#include "irrArray.h"
#include "irrList.h"
#include "S3DVertex.h"

namespace irr
{
namespace scene
{

//! A particle system scene node.
/** A scene node controlling a particle System. The behavior of the particles
can be controlling by setting the right particle emitters and effectors.
*/
class CParticleSystemSceneNode : public IParticleSystemSceneNode
{
public:

	//! constructor
	CParticleSystemSceneNode(bool createDefaultEmitter,
		ISceneNode* parent, ISceneManager* mgr, s32 id, 
		const core::vector3df& position,
		const core::vector3df& rotation,
		const core::vector3df& scale);

	//! destructor
	virtual ~CParticleSystemSceneNode();

	//! Sets the particle emitter, which creates the particles.
	virtual void setEmitter(IParticleEmitter* emitter);

	//! Adds new particle effector to the particle system.
	virtual void addAffector(IParticleAffector* affector);

	//! Removes all particle affectors in the particle system.
	virtual void removeAllAffectors();

	//! Returns the material based on the zero based index i.
	virtual video::SMaterial& getMaterial(s32 i);

	//! Returns amount of materials used by this scene node.
	virtual s32 getMaterialCount();

	//! pre render event
	virtual void OnPreRender();

	//! render
	virtual void render();

	//! returns the axis aligned bounding box of this node
	virtual const core::aabbox3d<f32>& getBoundingBox() const;

	//! Creates a point particle emitter.
	virtual IParticleEmitter* createPointEmitter(
		core::vector3df direction = core::vector3df(0.0f,0.03f,0.0f), 
		u32 minParticlesPerSecond = 5,
		u32 maxParticlePerSecond = 10,
		video::SColor minStartColor = video::SColor(255,0,0,0),
		video::SColor maxStartColor = video::SColor(255,255,255,255),
		u32 lifeTimeMin=2000, u32 lifeTimeMax=4000,
		s32 maxAngleDegrees=0);

	//! Creates a box particle emitter.
	virtual IParticleEmitter* createBoxEmitter(
		core::aabbox3d<f32> box = core::aabbox3d<f32>(-10,28,-10,10,30,10),
		core::vector3df direction = core::vector3df(0.0f,0.03f,0.0f), 
		u32 minParticlesPerSecond = 5,
		u32 maxParticlePerSecond = 10,
		video::SColor minStartColor = video::SColor(255,0,0,0),
		video::SColor maxStartColor = video::SColor(255,255,255,255),
		u32 lifeTimeMin=2000, u32 lifeTimeMax=4000,
		s32 maxAngleDegrees=0);

	//! Creates a fade out particle affector.
	virtual IParticleAffector* createFadeOutParticleAffector(
		video::SColor targetColor = video::SColor(0,0,0,0),
		u32 timeNeededToFadeOut = 1000);

	//! Creates a gravity affector.
	virtual IParticleAffector* createGravityAffector(
		core::vector3df gravity = core::vector3df(0.0f,-0.03f,0.0f),
		u32 timeForceLost = 1000);

	//! Sets the size of all particles.
	virtual void setParticleSize(
		const core::dimension2d<f32> &size = core::dimension2d<f32>(5.0f, 5.0f));

	//! Sets if the particles should be global. If it is, the particles are affected by 
	//! the movement of the particle system scene node too, otherwise they completely 
	//! ignore it. Default is true.
	virtual void setParticlesAreGlobal(bool global);

private:

	void doParticleSystem(u32 time);
	void reallocateBuffers();

	core::list<IParticleAffector*> AffectorList;
	IParticleEmitter* Emitter;
	core::array<SParticle> Particles;
	video::SMaterial Material;
	core::aabbox3d<f32> Box;
	u32 lastEmitTime;
	core::dimension2d<f32> ParticleSize;

	core::array<video::S3DVertex> Vertices; // 4 vertices per particle
	core::array<u16> Indices; // 6 indices per particle

	bool ParticlesAreGlobal;
};

} // end namespace scene
} // end namespace irr


#endif

