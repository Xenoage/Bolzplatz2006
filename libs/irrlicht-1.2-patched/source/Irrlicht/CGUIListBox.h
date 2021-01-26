// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_LIST_BOX_BAR_H_INCLUDED__
#define __C_GUI_LIST_BOX_BAR_H_INCLUDED__

#include "IGUIListBox.h"
#include "irrArray.h"

namespace irr
{
namespace gui
{

	class IGUIFont;
	class IGUIScrollBar;

	class CGUIListBox : public IGUIListBox
	{
	public:

		//! constructor
		CGUIListBox(IGUIEnvironment* environment, IGUIElement* parent, 
			s32 id, core::rect<s32> rectangle, bool clip=true,
			bool drawBack=false, bool moveOverSelect=false);

		//! destructor
		~CGUIListBox();

		//! returns amount of list items
		virtual s32 getItemCount();

		//! returns string of a list item. the may be a value from 0 to itemCount-1
		virtual const wchar_t* getListItem(s32 id);

		//! adds an list item, returns id of item
		virtual s32 addItem(const wchar_t* text);

		//! clears the list
		virtual void clear();

		//! returns id of selected item. returns -1 if no item is selected.
		virtual s32 getSelected();

		//! sets the selected item. Set this to -1 if no item should be selected
		virtual void setSelected(s32 id);

		//! called if an event happened.
		virtual bool OnEvent(SEvent event);

		//! draws the element and its children
		virtual void draw();

		//! adds an list item with an icon
		//! \param text Text of list entry
		//! \param icon Text of the Icon. This text can be for example one of the texts defined in
		//!  GUIIcons.h. 
		//! \return
		//! returns the id of the new created item
		virtual s32 addItem(const wchar_t* text, const wchar_t* icon);

		//! Sets the font which should be used as icon font. This font is set to the Irrlicht engine
		//! built-in-font by default. Icons can be displayed in front of every list item.
		//! An icon is a string, displayed with the icon font. When using the build-in-font of the
		//! Irrlicht engine as icon font, the icon strings defined in GUIIcons.h can be used.
		virtual void setIconFont(IGUIFont* font);
		
		//! Update the position and size of the listbox, and update the scrollbar
		virtual void updateAbsolutePosition();

	private:

		struct ListItem
		{
			core::stringw text;
			core::stringw icon;
		};

		void recalculateItemHeight();
		void selectNew(s32 ypos, bool onlyHover=false);

		core::array< ListItem > Items;
		s32 Selected;
		s32 ItemHeight;
		s32 TotalItemHeight;
		s32 ItemsIconWidth;
		gui::IGUIFont* Font;
		gui::IGUIFont* IconFont;
		bool Selecting;
		gui::IGUIScrollBar* ScrollBar;
		bool Clip;
		bool DrawBack;
		bool MoveOverSelect;
	};


} // end namespace gui
} // end namespace irr

#endif

