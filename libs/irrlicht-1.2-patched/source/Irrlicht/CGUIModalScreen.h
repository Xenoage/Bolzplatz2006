// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_MODAL_SCREEN_H_INCLUDED__
#define __C_GUI_MODAL_SCREEN_H_INCLUDED__

#include "IGUIElement.h"

namespace irr
{
namespace gui
{

	class CGUIModalScreen : public IGUIElement
	{
	public:

		//! constructor
		CGUIModalScreen(IGUIEnvironment* environment, IGUIElement* parent, s32 id);

		//! destructor
		~CGUIModalScreen();

		//! called if an event happened.
		virtual bool OnEvent(SEvent event);

		//! Removes a child.
		virtual void removeChild(IGUIElement* child);

		//! draws the element and its children
		virtual void draw();

		//! Updates the absolute position.
		virtual void updateAbsolutePosition();

	private:

		u32 MouseDownTime;
	};


} // end namespace gui
} // end namespace irr

#endif

