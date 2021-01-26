// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CGUIInOutFader.h"
#include "IGUIEnvironment.h"
#include "IVideoDriver.h"
#include "os.h"

namespace irr
{
namespace gui
{


//! constructor
CGUIInOutFader::CGUIInOutFader(IGUIEnvironment* environment, IGUIElement* parent, s32 id, core::rect<s32> rectangle)
: IGUIInOutFader(environment, parent, id, rectangle)
{
	#ifdef _DEBUG
	setDebugName("CGUIInOutFader");
	#endif

	Action = EFA_NOTHING;
	StartTime = 0;
	EndTime = 0;

	setColor(video::SColor(0,0,0,0));
}



//! destructor
CGUIInOutFader::~CGUIInOutFader()
{
}



//! draws the element and its children
void CGUIInOutFader::draw()
{
	if (!IsVisible || !Action)
		return;

	u32 now = os::Timer::getTime();
	if (now > EndTime && Action == EFA_FADE_IN)
	{
		Action = EFA_NOTHING;
		return;
	}

	irr::video::IVideoDriver* driver = Environment->getVideoDriver();

	if (driver)
	{
		f32 d;

		if (now > EndTime)
			d = 0.0f;
		else
			d = (EndTime - now) / (f32)(EndTime - StartTime);

		video::SColor newCol = FullColor.getInterpolated(TransColor, d);
		driver->draw2DRectangle(newCol, AbsoluteRect, &AbsoluteClippingRect);
	}

	IGUIElement::draw();
}



//! Gets the color to fade out to or to fade in from.
video::SColor CGUIInOutFader::getColor() const
{
	return Color;
}



//! Sets the color to fade out to or to fade in from.
void CGUIInOutFader::setColor(video::SColor color)
{
	Color = color;
	FullColor = Color;
	TransColor = Color;

	if (Action == EFA_FADE_OUT)
	{
		FullColor.setAlpha(0);	
		TransColor.setAlpha(255);
	}
	else
	if (Action == EFA_FADE_IN)
	{
		FullColor.setAlpha(255);	
		TransColor.setAlpha(0);
	}
}


//! Returns if the fade in or out process is done.
bool CGUIInOutFader::isReady() const 
{
	u32 now = os::Timer::getTime();
	bool ret = (now > EndTime);
	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return ret;
}



//! Starts the fade in process.
void CGUIInOutFader::fadeIn(u32 time)
{
	StartTime = os::Timer::getTime();
	EndTime = StartTime + time;
	Action = EFA_FADE_IN;
	setColor(Color);
}


//! Starts the fade out process.
void CGUIInOutFader::fadeOut(u32 time)
{
	StartTime = os::Timer::getTime();
	EndTime = StartTime + time;
	Action = EFA_FADE_OUT;
	setColor(Color);
}



} // end namespace gui
} // end namespace irr
