// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CIrrDeviceStub.h"
#include "IEventReceiver.h"
#include "irrList.h"
#include "os.h"
#include "IrrCompileConfig.h"
#include "CTimer.h"
#include "CLogger.h"
#include "irrString.h"
#include <string.h>
#include "irrlicht.h"

namespace irr
{

//! constructor
CIrrDeviceStub::CIrrDeviceStub(const char* version, irr::IEventReceiver* resv)
: Logger(0), Operator(0), VideoDriver(0)
{
	UserReceiver = resv;

	Logger = new CLogger(UserReceiver);
	os::Printer::Logger = Logger;

	core::stringc s = "Irrlicht Engine version ";
	s.append(getVersion());
	os::Printer::log(s.c_str(), ELL_NONE);

	checkVersion(version);

	// create timer
	Timer = new irr::CTimer();

	// create filesystem
	FileSystem = io::createFileSystem();
}


CIrrDeviceStub::~CIrrDeviceStub()
{
	FileSystem->drop();

	if (GUIEnvironment)
		GUIEnvironment->drop();

	if (VideoDriver)
		VideoDriver->drop();

	if (SceneManager)
		SceneManager->drop();

	if (CursorControl)
		CursorControl->drop();

	if (Operator)
		Operator->drop();

	CursorControl = 0;

	Timer->drop();

	Logger->drop();
}


void CIrrDeviceStub::createGUIAndScene()
{
	// create gui environment
	GUIEnvironment = gui::createGUIEnvironment(FileSystem, VideoDriver, Operator);

	// create Scene manager
	SceneManager = scene::createSceneManager(VideoDriver, FileSystem, CursorControl);

	setEventReceiver(UserReceiver);
}


//! returns the video driver
video::IVideoDriver* CIrrDeviceStub::getVideoDriver()
{
	return VideoDriver;
}



//! return file system
io::IFileSystem* CIrrDeviceStub::getFileSystem()
{
	return FileSystem;
}



//! returns the gui environment
gui::IGUIEnvironment* CIrrDeviceStub::getGUIEnvironment()
{
	return GUIEnvironment;
}



//! returns the scene manager
scene::ISceneManager* CIrrDeviceStub::getSceneManager()
{
	return SceneManager;
}


//! \return Returns a pointer to the ITimer object. With it the
//! current Time can be received.
ITimer* CIrrDeviceStub::getTimer()
{
	return Timer;
}


//! Returns the version of the engine. 
const char* CIrrDeviceStub::getVersion()
{
	return IRRLICHT_SDK_VERSION;
}

//! \return Returns a pointer to the mouse cursor control interface.
gui::ICursorControl* CIrrDeviceStub::getCursorControl()
{
	return CursorControl;
}


//! \return Returns a pointer to a list with all video modes supported
//! by the gfx adapter.
video::IVideoModeList* CIrrDeviceStub::getVideoModeList()
{
	return &VideoModeList;
}


//! checks version of sdk and prints warning if there might be a problem
bool CIrrDeviceStub::checkVersion(const char* version)
{
	if (strcmp(getVersion(), version))
	{
		core::stringc w;
		w = "Warning: The library version of the Irrlicht Engine (";
		w += getVersion();
		w += ") does not match the version the application was compiled with (";
		w += version;
		w += "). This may cause problems.";
		os::Printer::log(w.c_str(), ELL_WARNING);
		return false;
	}

	return true;
}


//! send the event to the right receiver
void CIrrDeviceStub::postEventFromUser(SEvent event)
{
	bool absorbed = false;

	if (UserReceiver)
		absorbed = UserReceiver->OnEvent(event);

	if (!absorbed && GUIEnvironment)
		absorbed = GUIEnvironment->postEventFromUser(event);

	if (!absorbed && SceneManager)
		absorbed = SceneManager->postEventFromUser(event);
}


//! Sets a new event receiver to receive events
void CIrrDeviceStub::setEventReceiver(IEventReceiver* receiver)
{
	UserReceiver = receiver;
	Logger->setReceiver(receiver);
	if (GUIEnvironment)
		GUIEnvironment->setUserEventReceiver(receiver);
}


//! Returns poinhter to the current event receiver. Returns 0 if there is none.
IEventReceiver* CIrrDeviceStub::getEventReceiver()
{
	return UserReceiver;
}


//! \return Returns a pointer to the logger.
ILogger* CIrrDeviceStub::getLogger()
{
	return Logger;
}


//! Returns the operation system opertator object.
IOSOperator* CIrrDeviceStub::getOSOperator()
{
	return Operator;
}


//! Sets if the window should be resizeable in windowed mode.
void CIrrDeviceStub::setResizeAble(bool resize)
{

}


} // end namespace irr
