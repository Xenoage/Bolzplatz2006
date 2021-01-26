// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CCameraFPSSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "os.h"
#include "Keycodes.h"
#include "stdio.h"
#include "quaternion.h"

namespace irr
{
namespace scene
{

const f32 MAX_VERTICAL_ANGLE = 89.0f;

//! constructor
CCameraFPSSceneNode::CCameraFPSSceneNode(ISceneNode* parent, ISceneManager* mgr,
		gui::ICursorControl* cursorControl, s32 id, f32 rotateSpeed , f32 moveSpeed,
		SKeyMap* keyMapArray, s32 keyMapSize, bool noVerticalMovement)
: CCameraSceneNode(parent, mgr, id), CursorControl(cursorControl),
	MoveSpeed(moveSpeed), RotateSpeed(rotateSpeed), firstUpdate(true),
	NoVerticalMovement(noVerticalMovement)
{
	#ifdef _DEBUG
	setDebugName("CCameraFPSSceneNode");
	#endif

	if (CursorControl)
		CursorControl->grab();

	MoveSpeed /= 1000.0f;

	recalculateViewArea();

	allKeysUp();

	// create key map
	if (!keyMapArray || !keyMapSize)
	{
		// create default key map
		KeyMap.push_back(SCamKeyMap(0, irr::KEY_UP));
		KeyMap.push_back(SCamKeyMap(1, irr::KEY_DOWN));
		KeyMap.push_back(SCamKeyMap(2, irr::KEY_LEFT));
		KeyMap.push_back(SCamKeyMap(3, irr::KEY_RIGHT));
	}
	else
	{
		// create custom key map

		for (s32 i=0; i<keyMapSize; ++i)
		{
			switch(keyMapArray[i].Action)
			{
			case EKA_MOVE_FORWARD: KeyMap.push_back(SCamKeyMap(0, keyMapArray[i].KeyCode));
				break;
			case EKA_MOVE_BACKWARD: KeyMap.push_back(SCamKeyMap(1, keyMapArray[i].KeyCode));
				break;
			case EKA_STRAFE_LEFT: KeyMap.push_back(SCamKeyMap(2, keyMapArray[i].KeyCode));
				break;
			case EKA_STRAFE_RIGHT: KeyMap.push_back(SCamKeyMap(3, keyMapArray[i].KeyCode));
				break;
			default:
				break;
			} // end switch
		} // end for
	}// end if
}


//! destructor
CCameraFPSSceneNode::~CCameraFPSSceneNode()
{
	if (CursorControl)
		CursorControl->drop();
}


//! It is possible to send mouse and key events to the camera. Most cameras
//! may ignore this input, but camera scene nodes which are created for 
//! example with scene::ISceneManager::addMayaCameraSceneNode or
//! scene::ISceneManager::addFPSCameraSceneNode, may want to get this input
//! for changing their position, look at target or whatever. 
bool CCameraFPSSceneNode::OnEvent(SEvent event)
{
	if (!InputReceiverEnabled)
		return false;

	if (event.EventType == EET_KEY_INPUT_EVENT)
	{
		s32 cnt = (s32)KeyMap.size();
		for (s32 i=0; i<cnt; ++i)
			if (KeyMap[i].keycode == event.KeyInput.Key)
			{
				CursorKeys[KeyMap[i].action] = event.KeyInput.PressedDown; 
				return true;
			}
	}

	return false;
}



//! onPostRender is called just after rendering the whole scene.
//! nodes may calculate or store animations here, and may do other useful things,
//! dependent on what they are.
void CCameraFPSSceneNode::OnPostRender(u32 timeMs)
{
	animate();

	core::list<ISceneNodeAnimator*>::Iterator ait = Animators.begin();
				for (; ait != Animators.end(); ++ait)
					(*ait)->animateNode(this, timeMs);

	updateAbsolutePosition();
	Target = getPosition() + TargetVector;

	core::list<ISceneNode*>::Iterator it = Children.begin();
				for (; it != Children.end(); ++it)
					(*it)->OnPostRender(timeMs);
}



void CCameraFPSSceneNode::animate()
{
	if (SceneManager->getActiveCamera() != this)
		return;

	if (firstUpdate)
	{
		if (CursorControl)
			CursorControl->setPosition(0.5f, 0.5f);
		CenterCursor = CursorControl->getRelativePosition();

		LastAnimationTime = os::Timer::getTime();

		firstUpdate = false;
		return;
	}

	// get time

	s32 now = os::Timer::getTime();
	s32 timeDiff = now - LastAnimationTime;
	LastAnimationTime = now;

	// Update rotation

	Target.set(0,0,1);

	if (!CursorControl)
		return;

	RelativeRotation.X *= -1.0f;
	RelativeRotation.Y *= -1.0f;

	if (InputReceiverEnabled)
	{
		core::position2d<f32> cursorpos = CursorControl->getRelativePosition();

		if (!core::equals(cursorpos.X, CenterCursor.X) ||
			!core::equals(cursorpos.Y, CenterCursor.Y))
		{
			RelativeRotation.Y += (0.5f - cursorpos.X) * RotateSpeed;
			RelativeRotation.X += (0.5f - cursorpos.Y) * RotateSpeed;
			CursorControl->setPosition(0.5f, 0.5f);
			CenterCursor = CursorControl->getRelativePosition();

			if (RelativeRotation.X > MAX_VERTICAL_ANGLE)
				RelativeRotation.X = MAX_VERTICAL_ANGLE;
			if (RelativeRotation.X < -MAX_VERTICAL_ANGLE)
				RelativeRotation.X = -MAX_VERTICAL_ANGLE;
		}
	}

	// set target

	core::matrix4 mat;
	mat.setRotationDegrees(core::vector3df(-RelativeRotation.X, -RelativeRotation.Y, 0));
	mat.transformVect(Target);

	// update position

	core::vector3df pos = getPosition();	

	core::vector3df movedir = Target;

	if (NoVerticalMovement)
		movedir.Y = 0.0f;

	movedir.normalize();

	if (CursorKeys[0])
		pos += movedir * (f32)timeDiff * MoveSpeed;

	if (CursorKeys[1])
		pos -= movedir * (f32)timeDiff * MoveSpeed;

	// strafing

	core::vector3df strafevect = Target;
	strafevect = strafevect.crossProduct(UpVector);

	if (NoVerticalMovement)
		strafevect.Y = 0.0f;

	strafevect.normalize();

	if (CursorKeys[2])
		pos += strafevect * (f32)timeDiff * MoveSpeed;

	if (CursorKeys[3])
		pos -= strafevect * (f32)timeDiff * MoveSpeed;

	// write translation

	setPosition(pos);

	// write right target

	TargetVector = Target;
	Target += pos;

	RelativeRotation.X *= -1.0f;
	RelativeRotation.Y *= -1.0f;
}


void CCameraFPSSceneNode::allKeysUp()
{
	for (s32 i=0; i<4; ++i)
		CursorKeys[i] = false;
}


//! sets the look at target of the camera
//! \param pos: Look at target of the camera.
void CCameraFPSSceneNode::setTarget(const core::vector3df& tgt)
{
	updateAbsolutePosition();
	core::vector3df vect = tgt - getAbsolutePosition();
	vect = vect.getHorizontalAngle();
	RelativeRotation.X = vect.X;
	RelativeRotation.Y = vect.Y;

	if (RelativeRotation.X > MAX_VERTICAL_ANGLE)
		 RelativeRotation.X -= 360.0f;
}


} // end namespace
} // end namespace

