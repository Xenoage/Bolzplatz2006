// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_BUTTON_H_INCLUDED__
#define __C_GUI_BUTTON_H_INCLUDED__

#include "IGUIButton.h"

namespace irr
{
namespace gui
{

	class CGUIButton : public IGUIButton
	{
	public:

		//! constructor
		CGUIButton(IGUIEnvironment* environment, IGUIElement* parent,
			s32 id, core::rect<s32> rectangle, bool noclip=false);

		//! destructor
		~CGUIButton();

		//! called if an event happened.
		virtual bool OnEvent(SEvent event);

		//! draws the element and its children
		virtual void draw();

		//! sets another skin independent font. if this is set to zero, the button uses the font of the skin.
		virtual void setOverrideFont(IGUIFont* font=0);

		//! Sets an image which should be displayed on the button when it is in normal state. 
		virtual void setImage(video::ITexture* image);

		//! Sets an image which should be displayed on the button when it is in normal state. 
		virtual void setImage(video::ITexture* image, const core::rect<s32>& pos);

		//! Sets an image which should be displayed on the button when it is in pressed state. 
		virtual void setPressedImage(video::ITexture* image);

		//! Sets an image which should be displayed on the button when it is in pressed state. 
		virtual void setPressedImage(video::ITexture* image, const core::rect<s32>& pos);

		//! Sets if the button should behave like a push button. Which means it
		//! can be in two states: Normal or Pressed. With a click on the button,
		//! the user can change the state of the button.
		virtual void setIsPushButton(bool isPushButton);

		//! Returns if the button is currently pressed
		virtual bool isPressed();

		//! Sets the pressed state of the button if this is a pushbutton
		virtual void setPressed(bool pressed);
		
		//! Sets if the alpha channel should be used for drawing images on the button (default is false)
		virtual void setUseAlphaChannel(bool useAlphaChannel);

		//! Returns if the alpha channel should be used for drawing images on the button
		virtual bool getUseAlphaChannel();

	private:

		bool Pressed;
		bool NoClip;
		bool IsPushButton;
		bool UseAlphaChannel;

		IGUIFont* OverrideFont;
		video::ITexture* Image;
		video::ITexture* PressedImage;
		core::rect<s32> ImageRect;
		core::rect<s32> PressedImageRect;
	};

} // end namespace gui
} // end namespace irr

#endif

