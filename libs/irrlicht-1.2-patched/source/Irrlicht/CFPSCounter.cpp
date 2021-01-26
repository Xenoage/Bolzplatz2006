// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CFPSCounter.h"

namespace irr
{
namespace video  
{


CFPSCounter::CFPSCounter()
: fps(0), startTime(0), framesCounted(100)
{

}



//! returns current fps
s32 CFPSCounter::getFPS()
{
	return fps;
}



//! to be called every frame
void CFPSCounter::registerFrame(u32 now)
{
	framesCounted++;

	u32 milliseconds = now - startTime;

	if (milliseconds > 2000)
	{
		fps = (s32)((f32)framesCounted / ((f32)milliseconds / 1000.0f));

		startTime = now;
		framesCounted = 0;
	}
}


} // end namespace video
} // end namespace irr

