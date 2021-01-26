// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_DIRECTX8_TEXTURE_H_INCLUDED__
#define __C_DIRECTX8_TEXTURE_H_INCLUDED__

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_DIRECT3D_8_

#include "ITexture.h"
#include "IImage.h"
#include <d3d8.h>

namespace irr
{
namespace video
{

class CD3D8Driver;

/*!
	interface for a Video Driver dependent Texture.
*/
class CD3D8Texture : public ITexture
{
public:

	//! constructor
	CD3D8Texture(IImage* image, CD3D8Driver* driver,
		u32 flags, const char* name);

	//! rendertarget constructor
	CD3D8Texture(CD3D8Driver* driver, core::dimension2d<s32> size, const char* name);

	//! destructor
	virtual ~CD3D8Texture();

	//! lock function
	virtual void* lock();

	//! unlock function
	virtual void unlock();

	//! Returns original size of the texture.
	virtual const core::dimension2d<s32>& getOriginalSize();

	//! Returns (=size) of the texture.
	virtual const core::dimension2d<s32>& getSize();

	//! returns driver type of texture (=the driver, who created the texture)
	virtual E_DRIVER_TYPE getDriverType();

	//! returns color format of texture
	virtual ECOLOR_FORMAT getColorFormat() const;

	//! returns pitch of texture (in bytes)
	virtual s32 getPitch();

	//! returns the DIRECT3D8 Texture
	IDirect3DTexture8* getDX8Texture();

	//! returns if texture has mipmap levels
	bool hasMipMaps();

	//! Regenerates the mip map levels of the texture. Useful after locking and
	//! modifying the texture
	virtual void regenerateMipMapLevels();

	//! returns if it is a render target
	bool isRenderTarget();

	//! Returns pointer to the render target surface
	IDirect3DSurface8* getRenderTargetSurface();

private:

	void createRenderTarget();

	//! returns the size of a texture which would be the optimize size for rendering it
	inline s32 getTextureSizeFromImageSize(s32 size);

	//! creates the hardware texture
	void createTexture(u32 flags);

	//! copies the image to the texture
	bool copyTexture();

	//! optimized for 16 bit to 16 copy.
	bool copyTo16BitTexture();

	//! copies texture to 32 bit hardware texture
	bool copyTo32BitTexture();

	bool createMipMaps(s32 level=1);

	void copy16BitMipMap(char* src, char* tgt,
		s32 width, s32 height, s32 pitchsrc, s32 pitchtgt);

	void copy32BitMipMap(char* src, char* tgt,
		s32 width, s32 height, s32 pitchsrc, s32 pitchtgt);

	IImage* Image;
	IDirect3DDevice8* Device;
	IDirect3DTexture8* Texture;
	IDirect3DSurface8* RTTSurface;
	CD3D8Driver* Driver;
	core::dimension2d<s32> TextureSize;
	core::dimension2d<s32> ImageSize;
	s32 Pitch;
	ECOLOR_FORMAT ColorFormat;
	bool SurfaceHasSameSize; // true if image has the same dimension as texture.
	bool HasMipMaps;
	bool IsRenderTarget;
};


} // end namespace video
} // end namespace irr

#endif // _IRR_COMPILE_WITH_DIRECT3D_8_

#endif // __C_DIRECTX8_TEXTURE_H_INCLUDED__

