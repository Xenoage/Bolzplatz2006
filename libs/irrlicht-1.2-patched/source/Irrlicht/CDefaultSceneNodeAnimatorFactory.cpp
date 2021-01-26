// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CDefaultSceneNodeAnimatorFactory.h"
#include "ISceneNodeAnimatorCollisionResponse.h"
#include "ISceneManager.h"
#include <string.h>

namespace irr
{
namespace scene
{

//! Names for scene node types
const c8* const SceneNodeAnimatorTypeNames[] =
{
	"flyCircle",
	"flyStraight",
	"followSpline",
	"rotation",
	"texture",
	"deletion",
	"collisionResponse",
	0
};


CDefaultSceneNodeAnimatorFactory::CDefaultSceneNodeAnimatorFactory(ISceneManager* mgr)
: Manager(mgr)
{
	// don't grab the scene manager here to prevent cyclic references
}


CDefaultSceneNodeAnimatorFactory::~CDefaultSceneNodeAnimatorFactory()
{
}


//! creates a scene node animator based on its type id
ISceneNodeAnimator* CDefaultSceneNodeAnimatorFactory::createSceneNodeAnimator(ESCENE_NODE_ANIMATOR_TYPE type, ISceneNode* target)
{
	scene::ISceneNodeAnimator* anim = 0;

	switch(type)
	{
	case ESNAT_FLY_CIRCLE:
		anim = Manager->createFlyCircleAnimator(core::vector3df(0,0,0), 10);
		break;
	case ESNAT_FLY_STRAIGHT:
		anim = Manager->createFlyStraightAnimator(core::vector3df(0,0,0), core::vector3df(100,100,100), 10000, true );
		break;
	case ESNAT_FOLLOW_SPLINE:
		{
			core::array<core::vector3df> points;
			points.push_back(core::vector3df(0,0,0));
			points.push_back(core::vector3df(10,5,10));
			anim = Manager->createFollowSplineAnimator(0, points);
		}
		break;
	case ESNAT_ROTATION:
		anim = Manager->createRotationAnimator(core::vector3df(0.3f,0,0));
		break;
	case ESNAT_TEXTURE:
		{
			core::array<video::ITexture*> textures;
			anim = Manager->createTextureAnimator(textures, 250);
		}
		break;
	case ESNAT_DELETION:
		anim = Manager->createDeleteAnimator(5000);
		break;
	case ESNAT_COLLISION_RESPONSE:
		anim = Manager->createCollisionResponseAnimator(0, target);
		break;
	}

	if (anim && target)
		target->addAnimator(anim);

	return anim;
}


//! creates a scene node animator based on its type name
ISceneNodeAnimator* CDefaultSceneNodeAnimatorFactory::createSceneNodeAnimator(const c8* typeName, ISceneNode* target)
{
	return createSceneNodeAnimator( getTypeFromName(typeName), target );
}


//! returns amount of scene node animator types this factory is able to create
s32 CDefaultSceneNodeAnimatorFactory::getCreatableSceneNodeAnimatorTypeCount()
{
	return ESNAT_COUNT;
}


//! returns type of a createable scene node animator type
ESCENE_NODE_ANIMATOR_TYPE CDefaultSceneNodeAnimatorFactory::getCreateableSceneNodeAnimatorType(s32 idx)
{
	if (idx>=0 && idx<ESNAT_COUNT)
		return (ESCENE_NODE_ANIMATOR_TYPE)idx;

	return ESNAT_UNKNOWN;
}


//! returns type name of a createable scene node animator type 
const c8* CDefaultSceneNodeAnimatorFactory::getCreateableSceneNodeAnimatorTypeName(s32 idx)
{
	if (idx>=0 && idx<ESNAT_COUNT)
		return SceneNodeAnimatorTypeNames[idx];

	return 0;
}

//! returns type name of a createable scene node animator type 
const c8* CDefaultSceneNodeAnimatorFactory::getCreateableSceneNodeAnimatorTypeName(ESCENE_NODE_ANIMATOR_TYPE type)
{
	// for this factory: index == type

	if (type>=0 && type<ESNAT_COUNT)
		return SceneNodeAnimatorTypeNames[type];

	return 0;
}

ESCENE_NODE_ANIMATOR_TYPE CDefaultSceneNodeAnimatorFactory::getTypeFromName(const c8* name)
{
	for ( int i=0; SceneNodeAnimatorTypeNames[i]; ++i)
		if (!strcmp(name, SceneNodeAnimatorTypeNames[i]) )
			return (ESCENE_NODE_ANIMATOR_TYPE)i;

	return ESNAT_UNKNOWN;
}


} // end namespace scene
} // end namespace irr

