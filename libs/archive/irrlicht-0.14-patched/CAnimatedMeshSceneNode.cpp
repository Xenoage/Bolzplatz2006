// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CAnimatedMeshSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "S3DVertex.h"
#include "os.h"
#include "CShadowVolumeSceneNode.h"
#include "SViewFrustrum.h"
#include "ICameraSceneNode.h"
#include "IAnimatedMeshMS3D.h"
#include "IAnimatedMeshX.h"
#include "IDummyTransformationSceneNode.h"
#include "IMaterialRenderer.h"

namespace irr
{
namespace scene
{



//! constructor
CAnimatedMeshSceneNode::CAnimatedMeshSceneNode(IAnimatedMesh* mesh, ISceneNode* parent, ISceneManager* mgr, s32 id,
			const core::vector3df& position, const core::vector3df& rotation,	const core::vector3df& scale)
: IAnimatedMeshSceneNode(parent, mgr, id, position, rotation, scale), Mesh(mesh), 
	BeginFrameTime(0), StartFrame(0), EndFrame(0), FramesPerSecond(100),
	Shadow(0), Looping(true), LoopCallBack(0)
{
	#ifdef _DEBUG
	setDebugName("CAnimatedMeshSceneNode");
	#endif

	BeginFrameTime = os::Timer::getTime();

	if (Mesh)
	{
		// get materials.
		Box = Mesh->getBoundingBox();

		IMesh* m = Mesh->getMesh(0,0);
		if (m)
		{
			video::SMaterial mat;
			for (s32 i=0; i<m->getMeshBufferCount(); ++i)
			{
				IMeshBuffer* mb = m->getMeshBuffer(i);
				if (mb)
					mat = mb->getMaterial();

				Materials.push_back(mat);
			}
		}

		// get start and begin time

		StartFrame = 0;
		EndFrame = Mesh->getFrameCount();

		// grab the mesh

		Mesh->grab();
	}
}



//! destructor
CAnimatedMeshSceneNode::~CAnimatedMeshSceneNode()
{
	if (Mesh)
		Mesh->drop();

	if (Shadow)
		Shadow->drop();

	for (s32 i=0; i<(s32)JointChildSceneNodes.size(); ++i)
		if (JointChildSceneNodes[i])
			JointChildSceneNodes[i]->drop();

	if (LoopCallBack)
		LoopCallBack->drop();
}



//! sets the current frame. from now on the animation is played from this frame.
void CAnimatedMeshSceneNode::setCurrentFrame(s32 frame)
{
}


//! frame
void CAnimatedMeshSceneNode::OnPreRender()
{
	if (IsVisible)
	{
		// because this node supports rendering of mixed mode meshes consisting of 
		// transparent and solid material at the same time, we need to go through all 
		// materials, check of what type they are and register this node for the right
		// render pass according to that.

		video::IVideoDriver* driver = SceneManager->getVideoDriver();

		PassCount = 0;
		int transparentCount = 0;
		int solidCount = 0;

		// count transparent and solid materials in this scene node
		for (u32 i=0; i<Materials.size(); ++i)
		{
			video::IMaterialRenderer* rnd = 
				driver->getMaterialRenderer(Materials[i].MaterialType);

			if (rnd && rnd->isTransparent())
				++transparentCount;
			else
				++solidCount;

			if (solidCount && transparentCount)
				break;
		}	

		// register according to material types counted

		if (solidCount)
			SceneManager->registerNodeForRendering(this, scene::ESNRP_SOLID);

		if (transparentCount)
			SceneManager->registerNodeForRendering(this, scene::ESNRP_TRANSPARENT);
	}

	ISceneNode::OnPreRender();

	if (IsVisible)
		for (s32 i=0; i<(s32)JointChildSceneNodes.size(); ++i)
			if (JointChildSceneNodes[i])
				JointChildSceneNodes[i]->OnPreRender();
}


inline s32 CAnimatedMeshSceneNode::getFrameNr()
{
	s32 frame = 0;
	s32 len = EndFrame - StartFrame;

	if (!len)
		return StartFrame;

	if (Looping)
	{
		// play animation looped
		frame = StartFrame + ((s32)((os::Timer::getTime() - BeginFrameTime)
			* (FramesPerSecond/1000.0f)) % len);
	}
	else
	{
		// play animation non looped
		frame = StartFrame + ((s32)((os::Timer::getTime() - BeginFrameTime)
			* (FramesPerSecond/1000.0f)));

		if (frame > EndFrame)
		{
			frame = EndFrame;
			StartFrame = EndFrame;
			if (LoopCallBack)
				LoopCallBack->OnAnimationEnd(this);
		}
	}

	return frame;
}



//! OnPostRender() is called just after rendering the whole scene.
void CAnimatedMeshSceneNode::OnPostRender(u32 timeMs)
{
	s32 frameNr = getFrameNr();

	if (IsVisible)
	{
		// animate this node with all animators

		core::list<ISceneNodeAnimator*>::Iterator ait = Animators.begin();
		for (; ait != Animators.end(); ++ait)
			(*ait)->animateNode(this, timeMs);

		// update absolute position
		updateAbsolutePosition();

		// update all dummy transformation nodes
		if (!JointChildSceneNodes.empty() && Mesh && 
			(Mesh->getMeshType() == EAMT_MS3D || Mesh->getMeshType() == EAMT_X))
		{
			IAnimatedMeshMS3D* amm = (IAnimatedMeshMS3D*)Mesh;
			core::matrix4* mat;

			for (s32 i=0; i<(s32)JointChildSceneNodes.size(); ++i)
				if (JointChildSceneNodes[i])
				{
					mat = amm->getMatrixOfJoint(i, frameNr);
					if (mat)
						JointChildSceneNodes[i]->getRelativeTransformationMatrix() = *mat;
				}
		}
		
		core::list<ISceneNode*>::Iterator it = Children.begin();
		for (; it != Children.end(); ++it)
			(*it)->OnPostRender(timeMs);
	}
}


//! renders the node.
void CAnimatedMeshSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();

	if (!Mesh || !driver)
		return;

	bool isTransparentPass = 
		SceneManager->getSceneNodeRenderPass() == scene::ESNRP_TRANSPARENT;

	++PassCount;

	driver->setTransform(video::ETS_WORLD, AbsoluteTransformation);

	s32 frame = getFrameNr();

	scene::IMesh* m = Mesh->getMesh(frame, 255, StartFrame, EndFrame);

	if (m)
	{
		Box = m->getBoundingBox();

		// for debug purposes only:
		if (DebugDataVisible && PassCount==1)
		{
			video::SMaterial m;
			m.Lighting = false;
			driver->setMaterial(m);
			driver->draw3DBox(Box, video::SColor(0,255,255,255));
			
			if (Mesh->getMeshType() == EAMT_X)
			{
				// draw skeleton
				const core::array<core::vector3df>* ds = 
					((IAnimatedMeshX*)Mesh)->getDrawableSkeleton(frame);

				for (s32 s=0; s<(s32)ds->size(); s+=2)
					driver->draw3DLine((*ds)[s], (*ds)[s+1],  video::SColor(0,255,255,255));
			}			
		}

		if (Shadow && PassCount==1)
			Shadow->setMeshToRenderFrom(m);

		for (s32 i=0; i<m->getMeshBufferCount(); ++i)
		{
			video::IMaterialRenderer* rnd = driver->getMaterialRenderer(Materials[i].MaterialType);
			bool transparent = (rnd && rnd->isTransparent());

			// only render transparent buffer if this is the transparent render pass
			// and solid only in solid pass
			if (transparent == isTransparentPass) 
			{
				scene::IMeshBuffer* mb = m->getMeshBuffer(i);
				driver->setMaterial(Materials[i]);
				driver->drawMeshBuffer(mb);
			}
		}			
	}
	#ifdef _DEBUG
	else
		os::Printer::log("Animated Mesh returned no mesh to render.", Mesh->getDebugName(), ELL_WARNING);
	#endif
}



//! sets the frames between the animation is looped.
//! the default is 0 - MaximalFrameCount of the mesh.
bool CAnimatedMeshSceneNode::setFrameLoop(s32 begin, s32 end)
{
	if (!Mesh)
		return false;

	s32 frameCount = Mesh->getFrameCount();

	if (!(begin <= end && begin < frameCount && end < frameCount))
		return false;

	StartFrame = begin;
	EndFrame = end;
	BeginFrameTime = os::Timer::getTime();

	return true;
}



//! sets the speed with witch the animation is played
void CAnimatedMeshSceneNode::setAnimationSpeed(s32 framesPerSecond)
{
	FramesPerSecond = framesPerSecond;
}




//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CAnimatedMeshSceneNode::getBoundingBox() const
{
	return Box;//Mesh ? Mesh->getBoundingBox() : Box;
}


//! returns the material based on the zero based index i. To get the amount
//! of materials used by this scene node, use getMaterialCount().
//! This function is needed for inserting the node into the scene hirachy on a
//! optimal position for minimizing renderstate changes, but can also be used
//! to directly modify the material of a scene node.
video::SMaterial& CAnimatedMeshSceneNode::getMaterial(s32 i)
{
	if (i < 0 || i >= (s32)Materials.size())
		return ISceneNode::getMaterial(i);

	return Materials[i];
}



//! returns amount of materials used by this scene node.
s32 CAnimatedMeshSceneNode::getMaterialCount()
{
	return Materials.size();
}


//! Creates shadow volume scene node as child of this node
//! and returns a pointer to it. 
IShadowVolumeSceneNode* CAnimatedMeshSceneNode::addShadowVolumeSceneNode(s32 id,
																		 bool zfailmethod,
																		 f32 infinity)
{
	if (!SceneManager->getVideoDriver()->queryFeature(video::EVDF_STENCIL_BUFFER))
		return 0;

	if (Shadow)
	{
		os::Printer::log("This node already has a shadow.", ELL_WARNING);
		return 0;
	}

	Shadow = new CShadowVolumeSceneNode(this, SceneManager, -1,  zfailmethod, infinity);
	return Shadow;
}



//! Returns a pointer to a child node, wich has the same transformation as 
//! the corrsesponding joint, if the mesh in this scene node is a ms3d mesh.
ISceneNode* CAnimatedMeshSceneNode::getMS3DJointNode(const c8* jointName)
{
	if (!Mesh || Mesh->getMeshType() != EAMT_MS3D)
		return 0;

	IAnimatedMeshMS3D* amm = (IAnimatedMeshMS3D*)Mesh;
	s32 jointCount = amm->getJointCount();
	s32 number = amm->getJointNumber(jointName);

	if (number == -1)
	{
		os::Printer::log("Joint with specified name not found in ms3d mesh.", jointName, ELL_WARNING);
		return 0;
	}

	if (JointChildSceneNodes.empty())
	{
		// allocate joints for the first time.
		JointChildSceneNodes.set_used(jointCount);
		for (s32 i=0; i<jointCount; ++i)
			JointChildSceneNodes[i] = 0;
	}

	if (JointChildSceneNodes[number] == 0)
	{
		JointChildSceneNodes[number] = 
			SceneManager->addDummyTransformationSceneNode(this);
		JointChildSceneNodes[number]->grab();
	}

	return JointChildSceneNodes[number];
}


//! Returns a pointer to a child node, wich has the same transformation as 
//! the corrsesponding joint, if the mesh in this scene node is a ms3d mesh.
ISceneNode* CAnimatedMeshSceneNode::getXJointNode(const c8* jointName)
{
	if (!Mesh || Mesh->getMeshType() != EAMT_X)
		return 0;

	IAnimatedMeshX* amm = (IAnimatedMeshX*)Mesh;
	s32 jointCount = amm->getJointCount();
	s32 number = amm->getJointNumber(jointName);

	if (number == -1)
	{
		os::Printer::log("Joint with specified name not found in x mesh.", jointName, ELL_WARNING);
		return 0;
	}

	if (JointChildSceneNodes.empty())
	{
		// allocate joints for the first time.
		JointChildSceneNodes.set_used(jointCount);
		for (s32 i=0; i<jointCount; ++i)
			JointChildSceneNodes[i] = 0;
	}

	if (JointChildSceneNodes[number] == 0)
	{
		JointChildSceneNodes[number] = 
			SceneManager->addDummyTransformationSceneNode(this);
		JointChildSceneNodes[number]->grab();
	}

	return JointChildSceneNodes[number];
}



//! Removes a child from this scene node.
//! Implemented here, to be able to remove the shadow properly, if there is one,
//! or to remove attached childs.
bool CAnimatedMeshSceneNode::removeChild(ISceneNode* child)
{
	if (child && Shadow == child)
	{
		Shadow->drop();
		Shadow = 0;
		return true;
	}

	if (ISceneNode::removeChild(child))
	{
		for (s32 i=0; i<(s32)JointChildSceneNodes.size(); ++i)
		if (JointChildSceneNodes[i] == child)
		{
			JointChildSceneNodes[i]->drop();
			JointChildSceneNodes[i] = 0;
			return true;
		}

		return true;
	}

	return false;
}


//! Starts a MD2 animation. 
bool CAnimatedMeshSceneNode::setMD2Animation(EMD2_ANIMATION_TYPE anim)
{
	if (!Mesh || Mesh->getMeshType() != EAMT_MD2)
		return false;

	IAnimatedMeshMD2* m = (IAnimatedMeshMD2*)Mesh;

	s32 begin, end, speed;
	m->getFrameLoop(anim, begin, end, speed);

	setAnimationSpeed(speed);
	setFrameLoop(begin, end);
	return true;
}


//! Starts a special MD2 animation. 
bool CAnimatedMeshSceneNode::setMD2Animation(const c8* animationName)
{
	if (!Mesh || Mesh->getMeshType() != EAMT_MD2)
		return false;

	IAnimatedMeshMD2* m = (IAnimatedMeshMD2*)Mesh;

	s32 begin, end, speed;
	if (!m->getFrameLoop(animationName, begin, end, speed))
		return false;

	setAnimationSpeed(speed);
	setFrameLoop(begin, end);
	return true;
}


//! Sets looping mode which is on by default. If set to false,
//! animations will not be looped.
void CAnimatedMeshSceneNode::setLoopMode(bool playAnimationLooped)
{
	Looping = playAnimationLooped;
}

//! Sets a callback interface which will be called if an animation
//! playback has ended. Set this to 0 to disable the callback again.
void CAnimatedMeshSceneNode::setAnimationEndCallback(IAnimationEndCallBack* callback)
{
	if (LoopCallBack)
		LoopCallBack->drop();

	LoopCallBack = callback;

	if (LoopCallBack)
		LoopCallBack->grab();
}


} // end namespace scene
} // end namespace irr

