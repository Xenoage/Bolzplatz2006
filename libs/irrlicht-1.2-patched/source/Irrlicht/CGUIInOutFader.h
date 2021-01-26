// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_IN_OUT_FADER_H_INCLUDED__
#define __C_GUI_IN_OUT_FADER_H_INCLUDED__

#include "IGUIInOutFader.h"

namespace irr
{
namespace gui
{

	class CGUIInOutFader : public IGUIInOutFader
	{
	public:

		//! constructor
		CGUIInOutFader(IGUIEnvironment* environment, IGUIElement* parent,
			s32 id, core::rect<s32> rectangle);

		//! destructor
		~CGUIInOutFader();

		//! draws the element and its children
		virtual void draw();

		//! Gets the color to fade out to or to fade in from.
		virtual video::SColor getColor() const;

		//! Sets the color to fade out to or to fade in from.
		virtual void setColor(video::SColor color);

		//! Starts the fade in process.
		virtual void fadeIn(u32 time);

		//! Starts the fade out process.
		virtual void fadeOut(u32 time);

		//! Returns if the fade in or out process is done.
		virtual bool isReady() const;

	private:

		enum EFadeAction
		{
			EFA_NOTHING = 0,
			EFA_FADE_IN,
			EFA_FADE_OUT
		};

		u32 StartTime;
		u32 EndTime;
		EFadeAction Action;		

		video::SColor Color;
		video::SColor FullColor;
		video::SColor TransColor;

	};

} // end namespace gui
} // end namespace irr

#endif

