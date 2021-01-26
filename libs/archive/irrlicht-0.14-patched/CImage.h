// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_IMAGE_H_INCLUDED__
#define __C_IMAGE_H_INCLUDED__

#include "IImage.h"

namespace irr
{
namespace video  
{

//! IImage implementation with a lot of special image operations for
//! 16 bit A1R5G5B5 images, which are used by the SoftwareDevice.
class CImage : public IImage
{
public:

	//! constructor 
	CImage(ECOLOR_FORMAT format, IImage* imageToCopy);

	//! constructor 
	//! \param useForeignMemory: If true, the image will use the data pointer
	//! directly and own it from now on, which means it will also try to delete [] the
	//! data when the image will be destructed. If false, the memory will by copied.
	CImage(ECOLOR_FORMAT format, const core::dimension2d<s32>& size,
		void* data, bool ownForeignMemory=true);

	//! constructor
	CImage(ECOLOR_FORMAT format, const core::dimension2d<s32>& size);

	//! constructor
	CImage(IImage* imageToCopy,
		const core::position2d<s32>& pos, const core::dimension2d<s32>& size);

	//! destructor
	virtual ~CImage();

	//! Lock function.
	virtual void* lock();

	//! Unlock function.
	virtual void unlock();

	//! Returns width and height of image data.
	virtual const core::dimension2d<s32>& getDimension();

	//! Returns bits per pixel. 
	virtual s32 getBitsPerPixel();

	//! Returns bytes per pixel
	virtual s32 getBytesPerPixel();

	//! Returns image data size in bytes
	virtual s32 getImageDataSizeInBytes();

	//! Returns image data size in pixels
	virtual s32 getImageDataSizeInPixels();

	//! returns mask for red value of a pixel
	virtual s32 getRedMask();

	//! returns mask for green value of a pixel
	virtual s32 getGreenMask();

	//! returns mask for blue value of a pixel
	virtual s32 getBlueMask();

	//! returns mask for alpha value of a pixel
	virtual s32 getAlphaMask();

	//! returns a pixel
	virtual SColor getPixel(s32 x, s32 y);

	//! returns the color format
	virtual ECOLOR_FORMAT getColorFormat();

	//! draws a rectangle
	void drawRectangle(const core::rect<s32>& rect, SColor color);

	//! draws a rectangle
	void drawRectangle(s32 x, s32 y, s32 x2, s32 y2, SColor color);

	//! copies this surface into another
	void copyTo(CImage* target, s32 x, s32 y);

	//! copies this surface into another
	void copyTo(CImage* target, const core::position2d<s32>& pos);

	//! copies this surface into another
	void copyTo(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect=0);

	//! copies this surface into another, using the alpha mask
	void copyToWithAlpha(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect);

	//! copies this surface into another, using the alpha mask, an cliprect and a color to add with
	void copyToWithAlpha(CImage* target, const core::position2d<s32>& pos, const core::rect<s32>& sourceRect, s16 color, const core::rect<s32>* clipRect = 0);

	//! copies this surface into another, scaling it to fit it.
	void copyToScaling(CImage* target);

	//! draws a line from to
	void drawLine(const core::position2d<s32>& from, const core::position2d<s32>& to, s16 color);
	
	//! resizes the surface to a new size
	void resizeTo(const core::dimension2d<s32>& size);

	//! fills the surface with black or white
	void fill(s16 color);
	
private:

	//! assumes format and size has been set and creates the rest
	void initData();

	//! clips a x coordinate into the screen
	inline void clipX(s32 &x);

	//! clips a y coordinate into the screen
	inline void clipY(s32 &y);

	//! exchanges two ints
	inline void exchange(s32& a, s32& b);

	//! clips coordinates into the rect. returns false if the rects is completely outside
	//! and does not need to be drawn.
	inline bool clipRect(const s32 targetWidth, const s32 targetHeight, s32& targetX, s32& targetY, s32& xInSource, s32& yInSource, s32& sourceWidth, s32& soureHeight);

	//! sets a pixel very fast and inline
	inline void setPixelFast(s32 x, s32 y, s16 color);

	s32 getBitsPerPixelFromFormat();

	void* Data;
	core::dimension2d<s32> Size;
	s32 BitsPerPixel;
	s32 BytesPerPixel;
	ECOLOR_FORMAT Format;

	s32 RedMask;
	s32 GreenMask;
	s32 BlueMask;
	s32 AlphaMask;
};

} // end namespace video
} // end namespace irr


#endif

