// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CGUIFont.h"
#include "os.h"
#include "CImage.h"

namespace irr
{
namespace gui
{

//! constructor
CGUIFont::CGUIFont(video::IVideoDriver* driver)
: Driver(driver), Positions(382), Texture(0), WrongCharacter(0)
{
	#ifdef _DEBUG
	setDebugName("CGUIFont");
	#endif

	if (Driver)
		Driver->grab();
}



//! destructor
CGUIFont::~CGUIFont()
{
	if (Driver)
		Driver->drop();

	if (Texture)
		Texture->drop();
}


//! loads a font file, native file needed, for texture parsing
bool CGUIFont::load(io::IReadFile* file)
{
	return loadTexture ( Driver->createImageFromFile ( file ),
						file->getFileName()
							);
}

//! loads a font file, native file needed, for texture parsing
bool CGUIFont::load(const c8* filename)
{
	return loadTexture (	Driver->createImageFromFile ( filename ),
							filename
							);
}


/*

//! loads a font file
bool CGUIFont::load(io::IReadFile* file)
{
	if (!Driver)
		return false;

	// turn mip-maps off
	bool mipmap = Driver->getTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS);
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, false);
	Driver->setTextureCreationFlag(video::ETCF_FILTER_TEXTURE, false);

	// get a pointer to the texture
	video::ITexture* tex = Driver->getTexture(file);

	// set previous mip-map state
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, mipmap);
	Driver->setTextureCreationFlag(video::ETCF_FILTER_TEXTURE, true);

	// load the texture
	return loadTexture(tex);
}


//! loads a font file
bool CGUIFont::load(const c8* filename)
{
	Environment->getFileSystem ();
	FileSystem->createAndOpenFile(filename);

	if (!Driver)
		return false;

	// turn mip-maps off
	bool mipmap = Driver->getTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS);
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, false);
	Driver->setTextureCreationFlag(video::ETCF_FILTER_TEXTURE, false);

	// get a pointer to the texture
	video::ITexture* tex = Driver->getTexture(filename);

	// set previous mip-map state
	Driver->setTextureCreationFlag(video::ETCF_CREATE_MIP_MAPS, mipmap);
	Driver->setTextureCreationFlag(video::ETCF_FILTER_TEXTURE, true);

	// load the texture
	return loadTexture(tex);
}
*/


//! load & prepare font from ITexture
bool CGUIFont::loadTexture(video::IImage* image, const c8 * name)
{
	if (!image)
		return false;

	s32 lowerRightPositions = 0;

	video::IImage* tmpImage=image;
	bool deleteTmpImage=false;
	switch(image->getColorFormat())
	{
	case video::ECF_R5G6B5:
		tmpImage =  new video::CImage(video::ECF_A1R5G5B5,image);
		deleteTmpImage=true;
	case video::ECF_A1R5G5B5:
		readPositions16bit(tmpImage, lowerRightPositions);
		break;
	case video::ECF_R8G8B8:
		tmpImage = new video::CImage(video::ECF_A8R8G8B8,image);
		deleteTmpImage=true;
	case video::ECF_A8R8G8B8:
		readPositions32bit (tmpImage, lowerRightPositions);
		break;
	}

	if (Positions.size() > 127)
		WrongCharacter = 127;

	// output warnings
	if (!lowerRightPositions || !Positions.size())
		os::Printer::log("The amount of upper corner pixels or lower corner pixels is == 0, font file may be corrupted.", ELL_ERROR);
	else
	if (lowerRightPositions != (s32)Positions.size())
		os::Printer::log("The amount of upper corner pixels and the lower corner pixels is not equal, font file may be corrupted.", ELL_ERROR);

	bool ret = ( !Positions.empty() && lowerRightPositions );


	if ( ret )
	{
		Texture = Driver->addTexture ( name, tmpImage );
		Texture->grab ();
	}
	if (deleteTmpImage)
		tmpImage->drop();
	image->drop ();

	return ret;
}



void CGUIFont::readPositions32bit(video::IImage* image, s32& lowerRightPositions)
{
	const core::dimension2d<s32>& size = image->getDimension();

	s32* p = (s32*)image->lock();
	if (!p)
	{
		os::Printer::log("Could not lock texture while preparing texture for a font.", ELL_ERROR);
		return;
	}

	// TODO: Hack till it's getting better...
	// Pixel(0,0) == half opacity assume font with alpha..

	s32 truealphaFont = ( (p[0] & 0xFF000000) == 0x7f000000 );
	p[0] |= 0xFF000000;

	s32 colorTopLeft = p[0];;
	s32 colorLowerRight = *(p+1);
	s32 colorBackGround = *(p+2);
	s32 colorBackGroundTransparent = 0x00FFFFFF & colorBackGround;
	s32 colorFont = 0xFFFFFFFF;

	*(p+1) = colorBackGround;

	// start parsing

	core::position2d<s32> pos(0,0);
	for (pos.Y=0; pos.Y<size.Height; ++pos.Y)
	{
		for (pos.X=0; pos.X<size.Width; ++pos.X)
		{
			if ( *p == colorTopLeft)
			{
				*p = colorBackGroundTransparent;
				Positions.push_back(core::rect<s32>(pos, pos));
			}
			else
			if (*p == colorLowerRight)
			{
				if (Positions.size()<=(u32)lowerRightPositions)
				{
					image->unlock();
					lowerRightPositions = 0;
					return;
				}

				*p = colorBackGroundTransparent;
				Positions[lowerRightPositions].LowerRightCorner = pos;
				++lowerRightPositions;
			}
			else
			if (*p == colorBackGround)
			{
				*p = colorBackGroundTransparent;
			}
			else
			if ( 0 == truealphaFont )
			{
				*p = colorFont;
			}
			++p;
		}
	}

	// Positions parsed.

	image->unlock();
}




void CGUIFont::readPositions16bit(video::IImage* image, s32& lowerRightPositions)
{
	core::dimension2d<s32> size = image->getDimension();

	s16* p = (s16*)image->lock();
	if (!p)
	{
		os::Printer::log("Could not lock texture while preparing texture for a font.", ELL_ERROR);
		return;
	}

	s16 truealphaFont = ( (p[0] & 0x8000) == 0x8000 );
	p[0] |= 0x8000;

	s16 colorTopLeft = p[0];;
	s16 colorLowerRight = *(p+1);
	s16 colorBackGround = *(p+2);
	s16 colorBackGroundTransparent = 0x7FFF & colorBackGround;
	u16 colorFont = 0xFFFF;

	*(p+1) = colorBackGround;

	// start parsing

	core::position2d<s32> pos(0,0);
	for (pos.Y=0; pos.Y<size.Height; ++pos.Y)
	{
		for (pos.X=0; pos.X<size.Width; ++pos.X)
		{
			if (*p == colorTopLeft)
			{
				*p = colorBackGroundTransparent;
				Positions.push_back(core::rect<s32>(pos, pos));
			}
			else
			if (*p == colorLowerRight)
			{
				// too many lower right points
				if (Positions.size()<=(u32)lowerRightPositions)
				{
					image->unlock();
					lowerRightPositions = 0;
					return;
				}

				*p = colorBackGroundTransparent;
				Positions[lowerRightPositions].LowerRightCorner = pos;
				++lowerRightPositions;
			}
			else
			if (*p == colorBackGround)
				*p = colorBackGroundTransparent;
			else
			if ( 0 == truealphaFont )
			{
				*p = colorFont;
			}
			++p;
		}
	}

	// Positions parsed.

	image->unlock();
}



//! returns the dimension of a text
core::dimension2d<s32> CGUIFont::getDimension(const wchar_t* text)
{
	core::dimension2d<s32> dim(0, Positions[0].getHeight());

	u32 n;

	for(const wchar_t* p = text; *p; ++p)
	{
		n = (*p) - 32;
		if (n > Positions.size())
			n = WrongCharacter;

		dim.Width += Positions[n].getWidth();
	}

	return dim;
}



inline s32 CGUIFont::getWidthFromCharacter(wchar_t c)
{
	u32 n = c - 32;
	if (n > Positions.size())
		n = WrongCharacter;

	return Positions[n].getWidth();
}


//! draws an text and clips it to the specified rectangle if wanted
void CGUIFont::draw(const wchar_t* text, const core::rect<s32>& position, video::SColor color, bool hcenter, bool vcenter, const core::rect<s32>* clip)
{
	if (!Driver)
		return;

	core::dimension2d<s32> textDimension;
	core::position2d<s32> offset = position.UpperLeftCorner;

	if (hcenter || vcenter)
	{
		textDimension = getDimension(text);

		if (hcenter)
			offset.X = ((position.getWidth() - textDimension.Width)>>1) + offset.X;

		if (vcenter)
			offset.Y = ((position.getHeight() - textDimension.Height)>>1) + offset.Y;
	}

	core::array<s32> indices;
	indices.reallocate(core::stringw(text).size());
	u32 n;
	while(*text)
	{
		n = (*text) - 32;
		if ( n > Positions.size())
			n = WrongCharacter;
		indices.push_back(n);
		++text;
	}
	Driver->draw2DImage(Texture, offset, Positions, indices, clip, color, true);
}


//! Calculates the index of the character in the text which is on a specific position.
s32 CGUIFont::getCharacterFromPos(const wchar_t* text, s32 pixel_x)
{
	s32 x = 0;
	s32 idx = 0;

	while (text[idx])
	{
		x += getWidthFromCharacter(text[idx]);

		if (x >= pixel_x)
			return idx;

		++idx;
	}

	return -1;
}


} // end namespace gui
} // end namespace irr
