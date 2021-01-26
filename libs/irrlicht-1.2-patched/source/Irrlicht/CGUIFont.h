// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_FONT_H_INCLUDED__
#define __C_GUI_FONT_H_INCLUDED__

#include "IGUIFont.h"
#include "irrString.h"
#include "IVideoDriver.h"
#include "irrArray.h"

namespace irr
{
namespace gui
{

class CGUIFont : public IGUIFont
{
public:

	//! constructor
	CGUIFont(video::IVideoDriver* Driver);

	//! destructor
	virtual ~CGUIFont();

	//! loads a font file
	bool load(const c8* filename);

	//! loads a font file
	bool load(io::IReadFile* file);

	//! draws an text and clips it to the specified rectangle if wanted
	virtual void draw(const wchar_t* text, const core::rect<s32>& position, video::SColor color, bool hcenter=false, bool vcenter=false, const core::rect<s32>* clip=0);

	//! returns the dimension of a text
	virtual core::dimension2d<s32> getDimension(const wchar_t* text);

	//! Calculates the index of the character in the text which is on a specific position.
	virtual s32 getCharacterFromPos(const wchar_t* text, s32 pixel_x);

private:

	//! load & prepare font from ITexture
	bool loadTexture(video::IImage * image, const c8* name);

	void readPositions16bit(video::IImage* texture, s32& lowerRightPositions);
	void readPositions32bit(video::IImage* texture, s32& lowerRightPositions);

	inline s32 getWidthFromCharacter(wchar_t c);

	video::IVideoDriver* Driver;
	core::array< core::rect<s32> > Positions;
	video::ITexture* Texture;
	s32 WrongCharacter;
};

} // end namespace gui
} // end namespace irr

#endif

