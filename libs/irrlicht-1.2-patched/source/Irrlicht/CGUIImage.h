// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_IMAGE_H_INCLUDED__
#define __C_GUI_IMAGE_H_INCLUDED__

#include "IGUIImage.h"

namespace irr
{
namespace gui
{

	class CGUIImage : public IGUIImage
	{
	public:

		//! constructor
		CGUIImage(IGUIEnvironment* environment, IGUIElement* parent, s32 id, core::rect<s32> rectangle);

		//! destructor
		~CGUIImage();

		//! sets an image
		virtual void setImage(video::ITexture* image);

		//! draws the element and its children
		virtual void draw();

		//! sets if the image should use its alpha channel to draw itself
		virtual void setUseAlphaChannel(bool use);

	private:

		video::ITexture* Texture;
		bool UseAlphaChannel;

	};


} // end namespace gui
} // end namespace irr

#endif

