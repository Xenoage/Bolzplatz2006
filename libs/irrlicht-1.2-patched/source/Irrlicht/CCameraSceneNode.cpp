// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CCameraSceneNode.h"
#include "ISceneManager.h"
#include "IVideoDriver.h"
#include "os.h"

namespace irr
{
namespace scene
{


//! constructor
CCameraSceneNode::CCameraSceneNode(ISceneNode* parent, ISceneManager* mgr, s32 id, 
	const core::vector3df& position, const core::vector3df& lookat)
	: ICameraSceneNode(parent, mgr, id, position, core::vector3df(0.0f, 0.0f, 0.0f),
			core::vector3df(1.0f, 1.0f, 1.0f)), InputReceiverEnabled(true)
{
	#ifdef _DEBUG
	setDebugName("CCameraSceneNode");
	#endif

	BBox.reset(0,0,0);

	// set default view

	UpVector.set(0.0f, 1.0f, 0.0f);
	Target.set(lookat);

	// set default projection

	Fovy = core::PI / 2.5f;	// Field of view, in radians. 
	Aspect = 4.0f / 3.0f;	// Aspect ratio. 
	ZNear = 1.0f;		// value of the near view-plane. 
	ZFar = 3000.0f;		// Z-value of the far view-plane. 

	video::IVideoDriver* d = mgr->getVideoDriver();
	if (d)
	{
		screenDim.Width = (f32)d->getCurrentRenderTargetSize().Width;
		screenDim.Height = (f32)d->getCurrentRenderTargetSize().Height;
		Aspect = screenDim.Width / screenDim.Height;
	}

	recalculateProjectionMatrix();
	recalculateViewArea();
}



//! destructor
CCameraSceneNode::~CCameraSceneNode()
{
}


//! Disables or enables the camera to get key or mouse inputs.
void CCameraSceneNode::setInputReceiverEnabled(bool enabled)
{
	InputReceiverEnabled = enabled;
}


//! Returns if the input receiver of the camera is currently enabled.
bool CCameraSceneNode::isInputReceiverEnabled()
{
	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return InputReceiverEnabled;
}


//! Sets the projection matrix of the camera. The core::matrix4 class has some methods
//! to build a projection matrix. e.g: core::matrix4::buildProjectionMatrixPerspectiveFovLH
//! \param projection: The new projection matrix of the camera. 
void CCameraSceneNode::setProjectionMatrix(const core::matrix4& projection)
{
	Projection = projection;
}



//! Gets the current projection matrix of the camera
//! \return Returns the current projection matrix of the camera.
const core::matrix4& CCameraSceneNode::getProjectionMatrix()
{
	return Projection;
}



//! Gets the current view matrix of the camera
//! \return Returns the current view matrix of the camera.
const core::matrix4& CCameraSceneNode::getViewMatrix()
{
	return View;
}



//! It is possible to send mouse and key events to the camera. Most cameras
//! may ignore this input, but camera scene nodes which are created for 
//! example with scene::ISceneManager::addMayaCameraSceneNode or
//! scene::ISceneManager::addFPSCameraSceneNode, may want to get this input
//! for changing their position, look at target or whatever. 
bool CCameraSceneNode::OnEvent(SEvent event)
{
	return false;
}



//! sets the look at target of the camera
//! \param pos: Look at target of the camera.
void CCameraSceneNode::setTarget(const core::vector3df& pos)
{
	Target = pos;
}



//! Gets the current look at target of the camera
//! \return Returns the current look at target of the camera
core::vector3df CCameraSceneNode::getTarget() const
{
	return Target;
}



//! sets the up vector of the camera
//! \param pos: New upvector of the camera.
void CCameraSceneNode::setUpVector(const core::vector3df& pos)
{
	UpVector = pos;
}



//! Gets the up vector of the camera.
//! \return Returns the up vector of the camera.
core::vector3df CCameraSceneNode::getUpVector() const
{
	return UpVector;
}


f32 CCameraSceneNode::getNearValue()
{
	return ZNear;
}

f32 CCameraSceneNode::getFarValue()
{
	return ZFar;
}

f32 CCameraSceneNode::getAspectRatio()
{
	return Aspect;
}

f32 CCameraSceneNode::getFOV()
{
	return Fovy;
}

void CCameraSceneNode::setNearValue(f32 f)
{
	ZNear = f;
	recalculateProjectionMatrix();
}

void CCameraSceneNode::setFarValue(f32 f)
{
	ZFar = f;
	recalculateProjectionMatrix();
}

void CCameraSceneNode::setAspectRatio(f32 f)
{
	Aspect = f;
	recalculateProjectionMatrix();
}

void CCameraSceneNode::setFOV(f32 f)
{
	Fovy = f;
	recalculateProjectionMatrix();
}

void CCameraSceneNode::recalculateProjectionMatrix()
{
	Projection.buildProjectionMatrixPerspectiveFovLH(Fovy, Aspect, ZNear, ZFar);
	//recalculateViewArea();
}


//! prerender
void CCameraSceneNode::OnPreRender()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	if (!driver)
		return;

	if (SceneManager->getActiveCamera() == this)
	{
		screenDim.Width = (f32)driver->getCurrentRenderTargetSize().Width;
		screenDim.Height = (f32)driver->getCurrentRenderTargetSize().Height;

		driver->setTransform(video::ETS_PROJECTION, Projection);

		// if upvector and vector to the target are the same, we have a
		// problem. so solve this problem:

		core::vector3df pos = getAbsolutePosition();
		core::vector3df tgtv = Target - pos;
		tgtv.normalize();

		core::vector3df up = UpVector;
		up.normalize();

		f32 dp = tgtv.dotProduct(up);
		if ((dp > -1.0001f && dp < -0.9999f) ||
			(dp < 1.0001f && dp > 0.9999f))
			up.X += 1.0f;

		View.buildCameraLookAtMatrixLH(pos, Target, up);
		recalculateViewArea();

		SceneManager->registerNodeForRendering(this, ESNRP_CAMERA);
	}

	if (IsVisible)
		ISceneNode::OnPreRender();
}


//! render
void CCameraSceneNode::render()
{	
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	if (!driver)
		return;

	driver->setTransform(video::ETS_VIEW, View);
}


//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CCameraSceneNode::getBoundingBox() const
{
	return BBox;
}



//! returns the view frustrum. needed sometimes by bsp or lod render nodes.
const SViewFrustrum* CCameraSceneNode::getViewFrustrum()
{
	return &ViewArea;
}


void CCameraSceneNode::recalculateViewArea()
{
	core::matrix4 mat = Projection * View;
	ViewArea = SViewFrustrum(mat);

	ViewArea.cameraPosition = getAbsolutePosition();
	ViewArea.recalculateBoundingBox();
}


//! Writes attributes of the scene node.
void CCameraSceneNode::serializeAttributes(io::IAttributes* out, io::SAttributeReadWriteOptions* options)
{
	ISceneNode::serializeAttributes(out, options);

	out->addVector3d("Target", Target);
	out->addVector3d("UpVector", UpVector);
	out->addFloat("Fovy", Fovy);
	out->addFloat("Aspect", Aspect);
	out->addFloat("ZNear", ZNear);
	out->addFloat("ZFar", ZFar);
}


//! Reads attributes of the scene node.
void CCameraSceneNode::deserializeAttributes(io::IAttributes* in, io::SAttributeReadWriteOptions* options)
{
	ISceneNode::deserializeAttributes(in, options);

	Target = in->getAttributeAsVector3d("Target");
	UpVector = in->getAttributeAsVector3d("UpVector");
	Fovy = in->getAttributeAsFloat("Fovy");
	Aspect = in->getAttributeAsFloat("Aspect");
	ZNear = in->getAttributeAsFloat("ZNear");
	ZFar = in->getAttributeAsFloat("ZFar");

	recalculateProjectionMatrix();
	recalculateViewArea();	
}


} // end namespace
} // end namespace

