// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CImage.h"
#include "os.h"
#include <memory.h>

namespace irr
{
namespace video  
{


//! constructor
CImage::CImage(ECOLOR_FORMAT format, const core::dimension2d<s32>& size)
: Format(format), Size(size), Data(0)
{
	initData();	
}


//! constructor
CImage::CImage(ECOLOR_FORMAT format, const core::dimension2d<s32>& size, void* data,
			   bool ownForeignMemory)
: Format(format), Size(size), Data(0)
{
	if (ownForeignMemory)
	{
		Data = (void*)0xbadf00d;
		initData();	
		Data = data;
	}
	else
	{
		Data = 0;
		initData();
		memcpy(Data, data, Size.Height * Size.Width * BytesPerPixel);
	}
}




//! constructor 
CImage::CImage(ECOLOR_FORMAT format, IImage* imageToCopy)
: Format(format), Data(0)
{
	if (!imageToCopy)
		return;

	Size = imageToCopy->getDimension();
	initData();

	// now copy data from other image

	if (Format == imageToCopy->getColorFormat())
	{
		memcpy(Data, imageToCopy->lock(), imageToCopy->getImageDataSizeInBytes()); 
		imageToCopy->unlock();
	}
	else
	if (Format == video::ECF_A1R5G5B5)
		for (s32 x=0; x<(s32)Size.Width; ++x)
			for (s32 y=0; y<(s32)Size.Height; ++y)
				((s16*)Data)[y*Size.Width + x] = imageToCopy->getPixel(x,y).toA1R5G5B5();
	else
		os::Printer::log("CImage: Unsupported format to copy from.", ELL_ERROR);
}



//! constructor
CImage::CImage(IImage* imageToCopy,	const core::position2d<s32>& pos,
			   const core::dimension2d<s32>& size)
 : Data(0), Size(0,0)
{
	if (!imageToCopy)
		return;

	Format = imageToCopy->getColorFormat();
	Size = size;

	initData();

	// copy image part
	c8* data = (c8*)Data;
	c8* source = (c8*)imageToCopy->lock();
	s32 bytes = imageToCopy->getBytesPerPixel();
	s32 srcpitch = imageToCopy->getDimension().Width * bytes;
	s32 tgtpitch = Size.Width*bytes;

	for (s32 x=0; x<size.Width; ++x)
		for (s32 y=0; y<size.Height; ++y)
		{
			for (s32 b=0; b<bytes; ++b)
				data[y*tgtpitch + (x*bytes) + b] =
					source[(y+pos.Y)*srcpitch + ((pos.X+x)*bytes) + b];
		}

	imageToCopy->unlock();
}




//! assumes format and size has been set and creates the rest
void CImage::initData()
{
	BitsPerPixel = getBitsPerPixelFromFormat();
	BytesPerPixel = BitsPerPixel / 8;

	if (!Data)
		Data = new s8[Size.Height * Size.Width * BytesPerPixel];
}


//! destructor
CImage::~CImage()
{
	delete [] (s8*)Data;
}



//! Lock function.
void* CImage::lock()
{
	return Data;
}



//! Unlock function.
void CImage::unlock()
{
}



//! Returns width and height of image data.
const core::dimension2d<s32>& CImage::getDimension()
{
	return Size;
}



//! Returns bits per pixel. 
s32 CImage::getBitsPerPixel()
{
	return BitsPerPixel;
}


//! Returns bytes per pixel
s32 CImage::getBytesPerPixel()
{
	return BytesPerPixel;
}



//! Returns image data size in bytes
s32 CImage::getImageDataSizeInBytes()
{
	return BytesPerPixel * Size.Width * Size.Height;
}



//! Returns image data size in pixels
s32 CImage::getImageDataSizeInPixels()
{
	return Size.Width * Size.Height;
}



//! returns mask for red value of a pixel
s32 CImage::getRedMask()
{
	return RedMask;
}



//! returns mask for green value of a pixel
s32 CImage::getGreenMask()
{
	return GreenMask;
}



//! returns mask for blue value of a pixel
s32 CImage::getBlueMask()
{
	return BlueMask;
}



//! returns mask for alpha value of a pixel
s32 CImage::getAlphaMask()
{
	return AlphaMask;
}


s32 CImage::getBitsPerPixelFromFormat()
{
	switch(Format)
	{
	case ECF_A1R5G5B5:
		AlphaMask = 0x1<<15;
		RedMask = 0x1F<<10;
		GreenMask = 0x1F<<5;
		BlueMask = 0x1F;
		return 16;
	case ECF_R5G6B5:
		AlphaMask = 0x0;
		RedMask = 0x1F<<11;
		GreenMask = 0x3F<<5;
		BlueMask = 0x1F;
		return 16;
	case ECF_R8G8B8:
		AlphaMask = 0x0;
		RedMask = 0xFF<<16;
		GreenMask = 0xFF<<8;
		BlueMask = 0xFF;
		return 24;
	case ECF_A8R8G8B8:
		AlphaMask = 0xFF<<24;
		RedMask = 0xFF<<16;
		GreenMask = 0xFF<<8;
		BlueMask = 0xFF;
		return 32;
	}

	os::Printer::log("CImage: Unknown color format.", ELL_ERROR);
	return 0;
}


//! returns a pixel
SColor CImage::getPixel(s32 x, s32 y)
{
	if (x < 0 || y < 0 || x >= Size.Width || y >= Size.Height)
		return SColor(0);

	switch(Format)
	{
	case ECF_A1R5G5B5:
		return A1R5G5B5toA8R8G8B8(((s16*)Data)[y*Size.Width + x]);
	case ECF_R5G6B5:
		return R5G6B5toA8R8G8B8(((s16*)Data)[y*Size.Width + x]);
	case ECF_A8R8G8B8:
		return ((s32*)Data)[y*Size.Width + x];
	case ECF_R8G8B8:
		{
			u8* p = &((u8*)Data)[(y*3)*Size.Width + (x*3)];
			return SColor(255,p[0],p[1],p[2]);
		}
	}

	return SColor(0);
}


//! returns the color format
ECOLOR_FORMAT CImage::getColorFormat()
{
	return Format;
}


//! clips a x coordinate into the screen
inline void CImage::clipX(s32 &x)
{
	if (x<0)
		x = 0;
	else
		if (x > Size.Width)
			x = Size.Width;
}



//! clips a y coordinate into the screen
inline void CImage::clipY(s32 &y)
{
	if (y<0)
		y = 0;
	else
		if (y > Size.Height)
			y = Size.Height;
}



//! exchanges two ints
inline void CImage::exchange(s32& a, s32& b)
{
	s32 tmp;
	tmp = a;
	a = b;
	b = tmp;
}


//! draws a rectangle
void CImage::drawRectangle(const core::rect<s32>& rect, SColor color)
{
	drawRectangle(	rect.UpperLeftCorner.X, rect.UpperLeftCorner.Y, 
					rect.LowerRightCorner.X, rect.LowerRightCorner.Y, color);
}


//! draws a rectangle
void CImage::drawRectangle(s32 x, s32 y, s32 x2, s32 y2, SColor color)
{
	if (Format != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	// clip

	clipX(x); clipX(x2);
	clipY(y); clipY(y2);

	// switch x and x2 if neccecary

	if (x > x2)
		exchange(x,x2);

	if (y > y2)
		exchange(y,y2);

	// draw

	s32 l=y*Size.Width;
	s32 ix;

	if (color.getAlpha()==255)
	{
		// quickly draw without alpha.

		s16 c = color.toA1R5G5B5();

		for (s32 iy=y; iy<y2; ++iy)
		{
			for (ix=x; ix<x2; ++ix)
				((s16*)Data)[l + ix] = c;

			l += Size.Width;
		}
	}
	else
	{
		// draw with alpha

		s32 ia = color.getAlpha();
		s32 a = 255-ia;

		s32 r = getRed(color.toA1R5G5B5()) * ia;
		s32 g = getGreen(color.toA1R5G5B5()) * ia;
		s32 b = getBlue(color.toA1R5G5B5()) * ia;

		s16 *src;

		for (s32 iy=y; iy<y2; ++iy)
		{
			for (ix=x; ix<x2; ++ix)
			{
				src = &((s16*)Data)[l + ix];
				*src =	video::RGB16(	
					(video::getRed(*src)*a + r)>>5,
					(video::getGreen(*src)*a + g)>>5,
					(video::getBlue(*src)*a + b)>>5);
			}

			l += Size.Width;
		}
	}
}


//! clips coordinates into the rect. returns false if the rects is completely outside
//! and does not need to be drawn.
inline bool CImage::clipRect(const s32 targetWidth, const s32 targetHeight,
							 s32& targetX, s32& targetY, s32& xInSource,
							 s32& yInSource, s32& sourceWidth, s32& soureHeight)
{
	if (targetX < 0)
	{
		sourceWidth += targetX;
		if (sourceWidth <= 0)
			return false;

		xInSource -= targetX;
		targetX = 0;
	}
	
	if (targetX+sourceWidth > targetWidth)
	{
		sourceWidth -= (targetX + sourceWidth) - targetWidth;
		if (sourceWidth <= 0)
			return false;
	}

	if (targetY < 0)
	{
		soureHeight += targetY;
		if (soureHeight <= 0)
			return false;

		yInSource -= targetY;
		targetY = 0;
	}
	
	if (targetY + soureHeight > targetHeight)
	{
		soureHeight -= (targetY + soureHeight) - targetHeight;
		if (soureHeight <= 0)
			return false;
	}

	return true;
}



//! copies this surface into another
void CImage::copyTo(CImage* target, const core::position2d<s32>& pos)
{
	copyTo(target, pos.X, pos.Y);
}



//! copies this surface into another
void CImage::copyTo(CImage* target, s32 x, s32 y)
{
	if (Format != ECF_A1R5G5B5 ||
		target->getColorFormat() != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	s16* data = (s16*)target->lock();
	core::dimension2d<s32> size = target->getDimension();

	// clip

	s32 ownWidth = Size.Width;
	s32 ownHeight = Size.Height;
	s32 ownX = 0;
	s32 ownY = 0;

	if (!clipRect(size.Width, size.Height, x, y, ownX, ownY, ownWidth, ownHeight))
		return;

	// copy

	s32 ltarget=y*size.Width + x;
	s32 lown=ownY*Size.Width + ownX;
	size_t dataRowSizeInBytes = ownWidth*sizeof(s16);

	for (s32 iy=0; iy<ownHeight; ++iy)
	{
		memcpy(&data[ltarget], &((s16*)Data)[lown], dataRowSizeInBytes); 

		lown += Size.Width;
		ltarget += size.Width;
	}

	target->unlock();
}



//! copies this surface into another
void CImage::copyTo(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect)
{
	if (Format != ECF_A1R5G5B5 ||
		target->getColorFormat() != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	if (sourceRect.UpperLeftCorner.X >= sourceRect.LowerRightCorner.X ||
		sourceRect.UpperLeftCorner.Y >= sourceRect.LowerRightCorner.Y)
		return;

	core::position2d<s32> targetPos = pos;
	core::position2d<s32> sourcePos = sourceRect.UpperLeftCorner;
	core::dimension2d<s32> sourceSize(sourceRect.getWidth(), sourceRect.getHeight());
	const core::dimension2d<s32> targetSurfaceSize = target->getDimension();

	if (clipRect)
	{
		if (targetPos.X < clipRect->UpperLeftCorner.X)
		{
			sourceSize.Width += targetPos.X - clipRect->UpperLeftCorner.X;
			if (sourceSize.Width <= 0)
				return;

			sourcePos.X -= targetPos.X - clipRect->UpperLeftCorner.X;
			targetPos.X = clipRect->UpperLeftCorner.X;
		}
		
		if (targetPos.X + sourceSize.Width > clipRect->LowerRightCorner.X)
		{
			sourceSize.Width -= (targetPos.X + sourceSize.Width) - clipRect->LowerRightCorner.X;
			if (sourceSize.Width <= 0)
				return;
		}

		if (targetPos.Y < clipRect->UpperLeftCorner.Y)
		{
			sourceSize.Height += targetPos.Y - clipRect->UpperLeftCorner.Y;
			if (sourceSize.Height <= 0)
				return;

			sourcePos.Y -= targetPos.Y - clipRect->UpperLeftCorner.Y;
			targetPos.Y = clipRect->UpperLeftCorner.Y;
		}
		
		if (targetPos.Y + sourceSize.Height > clipRect->LowerRightCorner.Y)
		{
			sourceSize.Height -= (targetPos.Y + sourceSize.Height) - clipRect->LowerRightCorner.Y;
			if (sourceSize.Height <= 0)
				return;
		}
	}

	// clip these coordinates

	if (targetPos.X<0)
	{
		sourceSize.Width += targetPos.X;
		if (sourceSize.Width <= 0)
			return;

		sourcePos.X -= targetPos.X;
		targetPos.X = 0;
	}
	
	if (targetPos.X + sourceSize.Width > targetSurfaceSize.Width)
	{
		sourceSize.Width -= (targetPos.X + sourceSize.Width) - targetSurfaceSize.Width;
		if (sourceSize.Width <= 0)
			return;
	}

	if (targetPos.Y<0)
	{
		sourceSize.Height += targetPos.Y;
		if (sourceSize.Height <= 0)
			return;

		sourcePos.Y -= targetPos.Y;
		targetPos.Y = 0;
	}
	
	if (targetPos.Y + sourceSize.Height > targetSurfaceSize.Height)
	{
		sourceSize.Height -= (targetPos.Y + sourceSize.Height) - targetSurfaceSize.Height;
		if (sourceSize.Height <= 0)
			return;
	}

	// draw everything

	s16* targetData = (s16*)target->lock();
	s32 ltarget = targetPos.Y * targetSurfaceSize.Width + targetPos.X;
	s32 lsource = sourcePos.Y * Size.Width + sourcePos.X;
	size_t dataRowSizeInBytes = sourceSize.Width * sizeof(s16);

	for (s32 iy=0; iy<sourceSize.Height; ++iy)
	{
		memcpy(&targetData[ltarget], &((s16*)Data)[lsource], dataRowSizeInBytes); 
		lsource += Size.Width;
		ltarget += targetSurfaceSize.Width;
	}
}



//! copies this surface into another, using the alpha mask
void CImage::copyToWithAlpha(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect)
{
	if (Format != ECF_A1R5G5B5 ||
		target->getColorFormat() != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}


	if (sourceRect.UpperLeftCorner.X >= sourceRect.LowerRightCorner.X ||
		sourceRect.UpperLeftCorner.Y >= sourceRect.LowerRightCorner.Y)
		return;

	core::position2d<s32> targetPos = pos;
	core::position2d<s32> sourcePos = sourceRect.UpperLeftCorner;
	core::dimension2d<s32> sourceSize(sourceRect.getWidth(), sourceRect.getHeight());
	const core::dimension2d<s32> targetSurfaceSize = target->getDimension();

	// clip these coordinates

	if (targetPos.X<0)
	{
		sourceSize.Width += targetPos.X;
		if (sourceSize.Width <= 0)
			return;

		sourcePos.X -= targetPos.X;
		targetPos.X = 0;
	}
	
	if (targetPos.X + sourceSize.Width > targetSurfaceSize.Width)
	{
		sourceSize.Width -= (targetPos.X + sourceSize.Width) - targetSurfaceSize.Width;
		if (sourceSize.Width <= 0)
			return;
	}

	if (targetPos.Y<0)
	{
		sourceSize.Height += targetPos.Y;
		if (sourceSize.Height <= 0)
			return;

		sourcePos.Y -= targetPos.Y;
		targetPos.Y = 0;
	}
	
	if (targetPos.Y + sourceSize.Height > targetSurfaceSize.Height)
	{
		sourceSize.Height -= (targetPos.Y + sourceSize.Height) - targetSurfaceSize.Height;
		if (sourceSize.Height <= 0)
			return;
	}

	// draw everything

	s16* targetData = (s16*)target->lock();
	s32 ltarget = targetPos.Y * targetSurfaceSize.Width + targetPos.X;
	s32 lsource = sourcePos.Y * Size.Width + sourcePos.X;
	size_t dataRowSizeInBytes = sourceSize.Width * sizeof(s16);

	s16 alphaMask = getAlphaMask();
	s16* p;
	s16* end;
	s16* t;

	for (s32 iy=0; iy<sourceSize.Height; ++iy)
	{
		p = &((s16*)Data)[lsource];
		end = p + (dataRowSizeInBytes>>1);
		t = &targetData[ltarget];

		while(p != end)
		{
			if (*p & alphaMask)
				*t = *p;

			++t;
			++p;
		}
		lsource += Size.Width;
		ltarget += targetSurfaceSize.Width;
	}

}




//! copies this surface into another, using the alpha mask, an cliprect and a color to add with
void CImage::copyToWithAlpha(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect, s16 color, const core::rect<s32>* clipRect)
{
	if (Format != ECF_A1R5G5B5 ||
		target->getColorFormat() != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	if (sourceRect.UpperLeftCorner.X >= sourceRect.LowerRightCorner.X ||
		sourceRect.UpperLeftCorner.Y >= sourceRect.LowerRightCorner.Y)
		return;

	core::position2d<s32> targetPos = pos;
	core::position2d<s32> sourcePos = sourceRect.UpperLeftCorner;
	core::dimension2d<s32> sourceSize(sourceRect.getWidth(), sourceRect.getHeight());
	const core::dimension2d<s32> targetSurfaceSize = target->getDimension();

	// clip to cliprect if there is one
	if (clipRect)
	{
		if (targetPos.X < clipRect->UpperLeftCorner.X)
		{
			sourceSize.Width += targetPos.X - clipRect->UpperLeftCorner.X;
			if (sourceSize.Width <= 0)
				return;

			sourcePos.X -= targetPos.X - clipRect->UpperLeftCorner.X;
			targetPos.X = clipRect->UpperLeftCorner.X;
		}
		
		if (targetPos.X + sourceSize.Width > clipRect->LowerRightCorner.X)
		{
			sourceSize.Width -= (targetPos.X + sourceSize.Width) - clipRect->LowerRightCorner.X;
			if (sourceSize.Width <= 0)
				return;
		}

		if (targetPos.Y < clipRect->UpperLeftCorner.Y)
		{
			sourceSize.Height += targetPos.Y - clipRect->UpperLeftCorner.Y;
			if (sourceSize.Height <= 0)
				return;

			sourcePos.Y -= targetPos.Y - clipRect->UpperLeftCorner.Y;
			targetPos.Y = clipRect->UpperLeftCorner.Y;
		}
		
		if (targetPos.Y + sourceSize.Height > clipRect->LowerRightCorner.Y)
		{
			sourceSize.Height -= (targetPos.Y + sourceSize.Height) - clipRect->LowerRightCorner.Y;
			if (sourceSize.Height <= 0)
				return;
		}
	}

	// clip these coordinates to screen

	if (targetPos.X<0)
	{
		sourceSize.Width += targetPos.X;
		if (sourceSize.Width <= 0)
			return;

		sourcePos.X -= targetPos.X;
		targetPos.X = 0;
	}
	
	if (targetPos.X + sourceSize.Width > targetSurfaceSize.Width)
	{
		sourceSize.Width -= (targetPos.X + sourceSize.Width) - targetSurfaceSize.Width;
		if (sourceSize.Width <= 0)
			return;
	}

	if (targetPos.Y<0)
	{
		sourceSize.Height += targetPos.Y;
		if (sourceSize.Height <= 0)
			return;

		sourcePos.Y -= targetPos.Y;
		targetPos.Y = 0;
	}
	
	if (targetPos.Y + sourceSize.Height > targetSurfaceSize.Height)
	{
		sourceSize.Height -= (targetPos.Y + sourceSize.Height) - targetSurfaceSize.Height;
		if (sourceSize.Height <= 0)
			return;
	}

	// draw everything

	s16* targetData = (s16*)target->lock();
	s32 ltarget = targetPos.Y * targetSurfaceSize.Width + targetPos.X;
	s32 lsource = sourcePos.Y * Size.Width + sourcePos.X;
	size_t dataRowSizeInBytes = sourceSize.Width * sizeof(s16);

	s16 alphaMask = getAlphaMask();
	s16* p;
	s16* end;
	s16* t;

	s32 r = getRed(color);
	s32 g = getGreen(color);
	s32 b = getBlue(color);

	for (s32 iy=0; iy<sourceSize.Height; ++iy)
	{
		p = &((s16*)Data)[lsource];
		end = p + (dataRowSizeInBytes>>1);
		t = &targetData[ltarget];

		while(p != end)
		{
			if (*p & alphaMask)
				*t = video::RGB16(video::getRed(*p) * (r) >>2, video::getGreen(*p) * (g) >>2, video::getBlue(*p) * (b) >>2);

			++t;
			++p;
		}
		lsource += Size.Width;
		ltarget += targetSurfaceSize.Width;
	}
}



//! draws a line from to
void CImage::drawLine(const core::position2d<s32>& from, const core::position2d<s32>& to, s16 color)
{
	if (Format != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	s32 x = from.X;
    s32 y = from.Y;

	s32 deltax = to.X - from.X;
	s32 deltay = to.Y - from.Y;
 
	s32 stepx = deltax < 0 ? -1 : 1;
	s32 stepy = deltay < 0 ? -1 : 1;
 
	deltax *= stepx; // wie  deltax = abs(deltax);
	deltay *= stepy; // wie  deltay = abs(deltay);
 
	if (deltax > deltay)
	{
		 s32 d = deltax >> 1;
 
		 while (x != to.X)
		 {
			if (x>=0 && y>=0 && x<Size.Width && y<Size.Height)
				setPixelFast(x, y, color);

			d -= deltay;

			if (d < 0)
			{
				y += stepy;
				d += deltax;
			}

			x += stepx;       
		 }
	}
	else
	{
		 s32 d = deltay >> 1;
 
		 while (y != to.Y)
		 {
			if (x>=0 && y>=0 && x<Size.Width && y<Size.Height)
				setPixelFast(x, y, color);

			d -= deltax;
			if (d < 0)
			{
				x += stepx;
				d += deltay;
			}

			y += stepy;
		 }
	}
}


//! sets a pixel very fast and inline
inline void CImage::setPixelFast(s32 x, s32 y, s16 color)
{
	((s16*)Data)[y*Size.Width + x] = color;
}


//! resizes the surface to a new size
void CImage::resizeTo(const core::dimension2d<s32>& size)
{
	if (Format != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	// note: this is very very slow. (i didn't want to write a fast version.
	// but hopefully, nobody wants to scale surfaces every frame.

	if (!size.Width || !size.Height)
		return;

	s32 nDataSize = size.Width * size.Height;
	s32 nDataSizeInBytes = nDataSize * sizeof(s16);
	s16* nData = new s16[nDataSize];

	f32 sourceXStep = (f32)Size.Width / (f32)size.Width;
	f32 sourceYStep = (f32)Size.Height / (f32)size.Height;
	f32 sy;

    for (s32 x=0; x<size.Width; ++x)
	{
		sy = 0.0f;

		for (s32 y=0; y<size.Height; ++y)
		{
			nData[(s32)(y*size.Width + x)] = ((s16*)Data)[(s32)(((s32)sy)*Size.Width + x*sourceXStep)];
			sy+=sourceYStep;
		}
	}

	delete [] (s8*)Data;
    //DataSize = nDataSize;
	//DataSizeInBytes = nDataSizeInBytes;
	Size = size;
	Data = nData;
}



//! copies this surface into another, scaling it to fit it.
void CImage::copyToScaling(CImage* target)
{
	if (Format != ECF_A1R5G5B5 ||
		target->getColorFormat() != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}

	// note: this is very very slow. (i didn't want to write a fast version.
	// but hopefully, nobody wants to scale surfaces every frame.

	core::dimension2d<s32> size = target->getDimension();

	s32 nDataSize = size.Width * size.Height;
	s32 nDataSizeInBytes = nDataSize * sizeof(s16);

	if (!size.Width || !size.Height)
		return;

	s16* nData = (s16*)target->lock();

	f32 sourceXStep = (f32)Size.Width / (f32)size.Width;
	f32 sourceYStep = (f32)Size.Height / (f32)size.Height;
	f32 sy;

    for (s32 x=0; x<size.Width; ++x)
	{
		sy = 0.0f;

		for (s32 y=0; y<size.Height; ++y)
		{
			nData[(s32)(y*size.Width + x)] = ((s16*)Data)[(s32)(((s32)sy)*Size.Width + x*sourceXStep)];
			sy+=sourceYStep;
		}
	}

	target->unlock();
}


//! fills the surface with black or white
void CImage::fill(s16 color)
{
	if (Format != ECF_A1R5G5B5)
	{
		os::Printer::log("CImage is not A1R5G5B5", ELL_ERROR);
		return;
	}


	s32* p = (s32*)Data;
	s32* bufferEnd = p + ((Size.Width * Size.Height)>>1);
	s32 c = ((color & 0x0000ffff)<<16) | (color & 0x0000ffff);
	while(p != bufferEnd)
	{
		*p = c;
		++p;
	}
}



} // end namespace video
} // end namespace irr
