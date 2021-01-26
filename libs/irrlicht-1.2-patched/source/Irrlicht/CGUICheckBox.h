// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_CHECKBOX_H_INCLUDED__
#define __C_GUI_CHECKBOX_H_INCLUDED__

#include "IGUICheckBox.h"

namespace irr
{
namespace gui
{

	class CGUICheckBox : public IGUICheckBox
	{
	public:

		//! constructor
		CGUICheckBox(bool checked, IGUIEnvironment* environment, IGUIElement* parent, s32 id, core::rect<s32> rectangle);

		//! destructor
		~CGUICheckBox();

		//! set if box is checked
		virtual void setChecked(bool checked);

		//! returns if box is checked
		virtual bool isChecked();

		//! called if an event happened.
		virtual bool OnEvent(SEvent event);

		//! draws the element and its children
		virtual void draw();


	private:

		bool Pressed;
		bool Checked;
	};

} // end namespace gui
} // end namespace irr

#endif

