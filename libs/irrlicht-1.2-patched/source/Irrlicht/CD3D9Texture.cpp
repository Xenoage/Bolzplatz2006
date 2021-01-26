// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_DIRECT3D_9_

#define _IRR_DONT_DO_MEMORY_DEBUGGING_HERE
#include "CD3D9Texture.h"
#include "CD3D9Driver.h"
#include "os.h"

#include <stdio.h>
#include <d3dx9tex.h>

#ifndef _IRR_COMPILE_WITH_DIRECT3D_8_
// The D3DXFilterTexture function seems to get linked wrong when
// compiling with both D3D8 and 9, causing it not to work in the D3D9 device.
// So mipmapgeneration is replaced with my own bad generation in d3d 8 when
// compiling with both D3D 8 and 9.
// #define _IRR_USE_D3DXFilterTexture_
#endif // _IRR_COMPILE_WITH_DIRECT3D_8_

#ifdef _IRR_USE_D3DXFilterTexture_
#pragma comment(lib, "d3dx9.lib")
#endif

namespace irr
{
namespace video
{

//! rendertarget constructor
CD3D9Texture::CD3D9Texture(CD3D9Driver* driver, core::dimension2d<s32> size, const char* name)
: ITexture(name), Image(0), Driver(driver), TextureSize(size),
	Texture(0), Pitch(0), ImageSize(size), HasMipMaps(0), HardwareMipMaps(0),
	IsRenderTarget(true), RTTSurface(0)
{
	#ifdef _DEBUG
	setDebugName("CD3D9Texture");
	#endif

	Device=driver->getExposedVideoData().D3D9.D3DDev9;
	if (Device)
		Device->AddRef();

	createRenderTarget();
}


//! constructor
CD3D9Texture::CD3D9Texture(IImage* image, CD3D9Driver* driver,
					   u32 flags, const char* name)
: ITexture(name), Image(image), Driver(driver), TextureSize(0,0),
Texture(0), Pitch(0), ImageSize(0,0), HasMipMaps(false), HardwareMipMaps(false),
IsRenderTarget(false), RTTSurface(0)
{
	#ifdef _DEBUG
	setDebugName("CD3D9Texture");
	#endif

	bool generateMipLevels = (flags & video::ETCF_CREATE_MIP_MAPS) != 0;

	Device=driver->getExposedVideoData().D3D9.D3DDev9;
	if (Device)
		Device->AddRef();

	if (Image)
	{
		Image->grab();

		createTexture(flags);

		if (Texture)
		{
			if (copyTexture() && generateMipLevels)
			{
				// create mip maps.
				#ifdef _IRR_USE_D3DXFilterTexture_
					// The D3DXFilterTexture function seems to get linked wrong when
					// compiling with both D3D8 and 9, causing it not to work in the D3D9 device.
					// So mipmapgeneration is replaced with my own bad generation in d3d 8 when
					// compiling with both D3D 8 and 9.
					HRESULT hr  = D3DXFilterTexture(Texture, NULL, D3DX_DEFAULT, D3DX_DEFAULT);
					if (FAILED(hr))
						os::Printer::log("Could not create direct3d mip map levels.", ELL_WARNING);
					else
						HasMipMaps = true;
				#else
					createMipMaps();
					HasMipMaps = true;
				#endif
			}
		}
		else
			os::Printer::log("Could not create DIRECT3D9 Texture.", ELL_WARNING);
	}
}


void CD3D9Texture::createRenderTarget()
{
	TextureSize.Width = getTextureSizeFromImageSize(TextureSize.Width);
	TextureSize.Height = getTextureSizeFromImageSize(TextureSize.Height);

	// get backbuffer format to create the render target in the
	// same format

	IDirect3DSurface9* bb;
	D3DFORMAT d3DFormat = D3DFMT_A8R8G8B8;

	if (!FAILED(Device->GetBackBuffer(0, 0, D3DBACKBUFFER_TYPE_MONO, &bb)))
	{
		D3DSURFACE_DESC desc;
		bb->GetDesc(&desc);
		d3DFormat = desc.Format;

		if (d3DFormat == D3DFMT_X8R8G8B8)
			d3DFormat = D3DFMT_A8R8G8B8;

		bb->Release();
	}
	else
	{
		os::Printer::log("Could not create RenderTarget texture: could not get BackBuffer.",
			ELL_WARNING);
		return;
	}

	// create texture
	HRESULT hr;

	hr = Device->CreateTexture(
		TextureSize.Width,
		TextureSize.Height,
		1, // mip map level count, we don't want mipmaps here
		D3DUSAGE_RENDERTARGET,
		d3DFormat,
		D3DPOOL_DEFAULT,
		&Texture,
		NULL);

	// get irrlicht format from D3D format

	switch(d3DFormat)
	{
	case D3DFMT_X1R5G5B5:
	case D3DFMT_A1R5G5B5:
		ColorFormat = ECF_A1R5G5B5;
		break;
	case D3DFMT_A8B8G8R8:
	case D3DFMT_A8R8G8B8:
	case D3DFMT_X8R8G8B8:
		ColorFormat = ECF_A8R8G8B8;
		break;
	case D3DFMT_R5G6B5:
		ColorFormat = ECF_R5G6B5;
		break;
	default:
		ColorFormat = (ECOLOR_FORMAT)-1;
	};

	if (FAILED(hr))
		os::Printer::log("Could not create render target texture");
}


bool CD3D9Texture::createMipMaps(s32 level)
{
	if (HardwareMipMaps && Texture)
	{
		// generate mipmaps in hardware

		Texture->GenerateMipSubLevels();
		return true;
	}
	//os::Printer::log("manual mipmap");

	// The D3DXFilterTexture function seems to get linked wrong when
	// compiling with both D3D8 and 9, causing it not to work in the D3D9 device.
	// So mipmapgeneration is replaced with my own bad generation here when
	// compiling with both D3D 8 and 9.

	IDirect3DSurface9* upperSurface = 0;
	IDirect3DSurface9* lowerSurface = 0;

	// get upper level
	HRESULT hr = Texture->GetSurfaceLevel(level-1, &upperSurface);
	if (FAILED(hr) || !upperSurface)
	{
		os::Printer::log("Could get upper surface level for mip map generation", ELL_WARNING);
		return false;
	}

	// get lower level
	hr = Texture->GetSurfaceLevel(level, &lowerSurface);
	if (FAILED(hr) || !lowerSurface)
	{
		os::Printer::log("Could get lower surface level for mip map generation", ELL_WARNING);
		return false;
	}

	D3DSURFACE_DESC upperDesc, lowerDesc;
	upperSurface->GetDesc(&upperDesc);
	lowerSurface->GetDesc(&lowerDesc);


	D3DLOCKED_RECT upperlr;
	D3DLOCKED_RECT lowerlr;

	// lock upper surface
	if (FAILED(upperSurface->LockRect(&upperlr, NULL, 0)))
	{
		os::Printer::log("Could not lock upper texture for mip map generation", ELL_WARNING);
		return false;
	}

	// lock lower surface
	if (FAILED(lowerSurface->LockRect(&lowerlr, NULL, 0)))
	{
		os::Printer::log("Could not lock lower texture for mip map generation", ELL_WARNING);
		return false;
	}

	if (upperDesc.Format != lowerDesc.Format)
	{
		os::Printer::log("Cannot copy mip maps with different formats.", ELL_WARNING);
	}
	else
	{
		if (upperDesc.Format == D3DFMT_A1R5G5B5)
			copy16BitMipMap((char*)upperlr.pBits, (char*)lowerlr.pBits,
					lowerDesc.Width, lowerDesc.Height,
					upperlr.Pitch, lowerlr.Pitch);
		else
		if (upperDesc.Format == D3DFMT_A8R8G8B8)
			copy32BitMipMap((char*)upperlr.pBits, (char*)lowerlr.pBits,
					lowerDesc.Width, lowerDesc.Height,
					upperlr.Pitch, lowerlr.Pitch);
		else
			os::Printer::log("Unsupported mipmap format, cannot copy.", ELL_WARNING);
	}

	// unlock
	if (FAILED(upperSurface->UnlockRect()))
		return false;
	if (FAILED(lowerSurface->UnlockRect()))
		return false;

	// release
	upperSurface->Release();
	lowerSurface->Release();

	if (upperDesc.Width <= 2 || upperDesc.Height <= 2)
		return true; // stop generating levels

	// generate next level
	return createMipMaps(level+1);
}


//! creates the hardware texture
void CD3D9Texture::createTexture(u32 flags)
{
	core::dimension2d<s32> optSize;
	ImageSize = Image->getDimension();

	if (Driver->queryFeature(EVDF_TEXTURE_NPOT))
		optSize=ImageSize;
	else
	{
		optSize.Width = getTextureSizeFromImageSize(ImageSize.Width);
		optSize.Height = getTextureSizeFromImageSize(ImageSize.Height);
	}

	HRESULT hr;
	D3DFORMAT format = D3DFMT_A1R5G5B5;

	switch(getTextureFormatFromFlags(flags))
	{
	case ETCF_ALWAYS_16_BIT:
		format = D3DFMT_A1R5G5B5; break;
	case ETCF_ALWAYS_32_BIT:
		format = D3DFMT_A8R8G8B8; break;
	case ETCF_OPTIMIZED_FOR_QUALITY:
		{
			switch(Image->getColorFormat())
			{
			case ECF_R8G8B8:
			case ECF_A8R8G8B8:
				format = D3DFMT_A8R8G8B8; break;
			case ECF_A1R5G5B5:
			case ECF_R5G6B5:
				format = D3DFMT_A1R5G5B5; break;
			}
		}
		break;
	case ETCF_OPTIMIZED_FOR_SPEED:
		format = D3DFMT_A1R5G5B5; break;
	}

	bool mipmaps = (flags & video::ETCF_CREATE_MIP_MAPS) != 0;

	DWORD usage = 0;

	// This enables hardware mip map generation. Disabled because
	// some cards or drivers seem to have problems with this.
	// D3DCAPS9 caps;
	// Device->GetDeviceCaps(&caps);
	// if (caps.Caps2 & D3DCAPS2_CANAUTOGENMIPMAP)
	// {
	//	usage = D3DUSAGE_AUTOGENMIPMAP;
	//	HardwareMipMaps = true;
	//}

	hr = Device->CreateTexture(optSize.Width, optSize.Height,
		mipmaps ? 0 : 1, // number of mipmaplevels (0 = automatic all)
		usage, // usage
		format, D3DPOOL_MANAGED , &Texture, NULL);

	if (FAILED(hr) && format == D3DFMT_A8R8G8B8)
	{
		// try brute force 16 bit

		format = D3DFMT_A1R5G5B5;

		hr = Device->CreateTexture(optSize.Width, optSize.Height,
			(flags & ETCF_CREATE_MIP_MAPS) ? 0 : 1, // number of mipmaplevels (0 = automatic all)
			0, D3DFMT_A1R5G5B5, D3DPOOL_MANAGED, &Texture, NULL);
	}

	ColorFormat = (format == D3DFMT_A1R5G5B5) ? ECF_A1R5G5B5 : ECF_A8R8G8B8;
}



//! copies the image to the texture
bool CD3D9Texture::copyTexture()
{
	if (Texture && Image)
	{
		D3DSURFACE_DESC desc;
		Texture->GetLevelDesc(0, &desc);

		TextureSize.Width = desc.Width;
		TextureSize.Height = desc.Height;

		SurfaceHasSameSize = (TextureSize == ImageSize);

		if (desc.Format == D3DFMT_A1R5G5B5)
			return copyTo16BitTexture();
		else
		if (desc.Format == D3DFMT_A8R8G8B8)
			return copyTo32BitTexture();
		else
			os::Printer::log("CD3D9Texture: Unsupported D3D9 hardware texture format", ELL_ERROR);
	}

	return true;
}


//! copies texture to 32 bit hardware texture
bool CD3D9Texture::copyTo32BitTexture()
{
	D3DLOCKED_RECT rect;
	HRESULT hr = Texture->LockRect(0, &rect, 0, 0);
	if (FAILED(hr))
	{
		os::Printer::log("Could not lock DIRECT3D9 32 bit Texture.", ELL_ERROR);
		return false;
	}

	s32* dest = (s32*)rect.pBits;
	s32* source = (s32*)Image->lock();
	Pitch = rect.Pitch;
	s32 pitch = rect.Pitch / 4;

	if (SurfaceHasSameSize)
	{
		if (Image->getColorFormat() == ECF_A8R8G8B8)
		{
			// direct copy, fast

			for (s32 y=0; y<ImageSize.Height; ++y)
				for (s32 x=0; x<ImageSize.Width; ++x)
					dest[x + y*pitch] = source[x + y * ImageSize.Width];
		}
		else
		{
			// slow convert

			for (s32 y=0; y<ImageSize.Height; ++y)
				for (s32 x=0; x<ImageSize.Width; ++x)
					dest[x + y*pitch] = Image->getPixel(x,y).color;
		}
	}
	else
	{
		// scale texture

		f32 sourceXStep = (f32)ImageSize.Width / (f32)TextureSize.Width;
		f32 sourceYStep = (f32)ImageSize.Height / (f32)TextureSize.Height;
		f32 sy;

		if (Image->getColorFormat() == ECF_A8R8G8B8)
		{
			// copy texture scaling

			for (s32 x=0; x<TextureSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<TextureSize.Height; ++y)
				{
					dest[(s32)(y*pitch + x)] = source[(s32)(((s32)sy)*ImageSize.Width + x*sourceXStep)];
					sy+=sourceYStep;
				}
			}
		}
		else
		{
			// convert texture scaling, slow
			for (s32 x=0; x<TextureSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<TextureSize.Height; ++y)
				{
					dest[(s32)(y*pitch + x)] =
						Image->getPixel((s32)(x*sourceXStep), (s32)sy).color;

					sy+=sourceYStep;
				}
			}
		}
	}

	Image->unlock();

	hr = Texture->UnlockRect(0);
	if (FAILED(hr))
	{
		os::Printer::log("Could not unlock DIRECT3D9 Texture.", ELL_ERROR);
		return false;
	}

	return true;

}


//! optimized for 16 bit to 16 copy.
bool CD3D9Texture::copyTo16BitTexture()
{
	D3DLOCKED_RECT rect;
	HRESULT hr = Texture->LockRect(0, &rect, 0, 0);
	if (FAILED(hr))
	{
		os::Printer::log("Could not lock DIRECT3D9 16 bit Texture.", ELL_ERROR);
		return false;
	}

	s16* dest = (s16*)rect.pBits;
	s16* source = (s16*)Image->lock();
	Pitch = rect.Pitch;
	s32 pitch = rect.Pitch/2;

	if (SurfaceHasSameSize)
	{
		// copy texture

		if (Image->getColorFormat() == ECF_A1R5G5B5)
		{
			// direct copy, fast

			for (s32 y=0; y<ImageSize.Height; ++y)
				for (s32 x=0; x<ImageSize.Width; ++x)
					dest[x + y*pitch] = source[x + y * ImageSize.Width];
		}
		else
		{
			// slow convert

			for (s32 y=0; y<ImageSize.Height; ++y)
				for (s32 x=0; x<ImageSize.Width; ++x)
					dest[x + y*pitch] = Image->getPixel(x,y).toA1R5G5B5();
		}
	}
	else
	{
		// scale texture

		f32 sourceXStep = (f32)ImageSize.Width / (f32)TextureSize.Width;
		f32 sourceYStep = (f32)ImageSize.Height / (f32)TextureSize.Height;
		f32 sy;

		if (Image->getColorFormat() == ECF_A1R5G5B5)
		{
			// copy texture scaling

			for (s32 x=0; x<TextureSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<TextureSize.Height; ++y)
				{
					dest[(s32)(y*pitch + x)] = source[(s32)(((s32)sy)*ImageSize.Width + x*sourceXStep)];
					sy+=sourceYStep;
				}
			}
		}
		else
		{
			// convert texture scaling, slow
			for (s32 x=0; x<TextureSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<TextureSize.Height; ++y)
				{
					dest[(s32)(y*pitch + x)] =
						Image->getPixel((s32)(x*sourceXStep), (s32)sy).toA1R5G5B5();

					sy+=sourceYStep;
				}
			}
		}
	}

	Image->unlock();

	hr = Texture->UnlockRect(0);
	if (FAILED(hr))
	{
		os::Printer::log("Could not unlock DIRECT3D9 16 bit Texture.", ELL_ERROR);
		return false;
	}

	return true;
}



//! destructor
CD3D9Texture::~CD3D9Texture()
{
	if (Device)
		Device->Release();

	if (Image)
		Image->drop();

	if (Texture)
		Texture->Release();

	if (RTTSurface)
		RTTSurface->Release();
}



//! lock function
void* CD3D9Texture::lock()
{
	if (!Texture)
		return 0;

	HRESULT hr;
	D3DLOCKED_RECT rect;
	if(!IsRenderTarget)
	{
		hr = Texture->LockRect(0, &rect, 0, 0);
	}
	else
	{
		D3DSURFACE_DESC desc;
		Texture->GetLevelDesc(0, &desc);
		if (!RTTSurface)
		{
			hr = Device->CreateOffscreenPlainSurface(desc.Width, desc.Height, desc.Format, D3DPOOL_SYSTEMMEM, &RTTSurface, NULL);
			if (FAILED(hr))
			{
				os::Printer::log("Could not lock DIRECT3D9 Texture.", ELL_ERROR);
				return 0;
			}
		}

		IDirect3DSurface9 *surface = NULL;
		hr = Texture->GetSurfaceLevel(0, &surface);
		if (FAILED(hr))
		{
			os::Printer::log("Could not lock DIRECT3D9 Texture.", ELL_ERROR);
			return 0;
		}
		hr = Device->GetRenderTargetData(surface, RTTSurface);
		if(FAILED(hr))
		{
			os::Printer::log("Could not lock DIRECT3D9 Texture.", ELL_ERROR);
			return 0;
		}
		hr = RTTSurface->LockRect(&rect, NULL, 0);
		if(FAILED(hr))
		{
			os::Printer::log("Could not lock DIRECT3D9 Texture.", ELL_ERROR);
			return 0;
		}
		return rect.pBits;
	}
	if (FAILED(hr))
	{
		os::Printer::log("Could not lock DIRECT3D9 Texture.", ELL_ERROR);
		return 0;
	}

	return rect.pBits;
}



//! unlock function
void CD3D9Texture::unlock()
{
	if (!Texture)
		return;

	if (!IsRenderTarget)
		Texture->UnlockRect(0);
	else if (RTTSurface)
		RTTSurface->UnlockRect();
}


//! Returns original size of the texture.
const core::dimension2d<s32>& CD3D9Texture::getOriginalSize()
{
	return ImageSize;
}


//! Returns (=size) of the texture.
const core::dimension2d<s32>& CD3D9Texture::getSize()
{
	return TextureSize;
}


//! returns the size of a texture which would be the optimize size for rendering it
inline s32 CD3D9Texture::getTextureSizeFromImageSize(s32 size)
{
	s32 ts = 0x01;

	while(ts < size)
		ts <<= 1;

	if (ts > size && ts > 64)
		ts >>= 1;

	return ts;
}



//! returns driver type of texture (=the driver, who created the texture)
E_DRIVER_TYPE CD3D9Texture::getDriverType()
{
	return EDT_DIRECT3D9;
}



//! returns color format of texture
ECOLOR_FORMAT CD3D9Texture::getColorFormat() const
{
	return ColorFormat;
}



//! returns pitch of texture (in bytes)
s32 CD3D9Texture::getPitch()
{
	return Pitch;
}



//! returns the DIRECT3D9 Texture
IDirect3DTexture9* CD3D9Texture::getDX9Texture()
{
	return Texture;
}


//! returns if texture has mipmap levels
bool CD3D9Texture::hasMipMaps()
{
	return HasMipMaps;
}


void CD3D9Texture::copy16BitMipMap(char* src, char* tgt,
				   s32 width, s32 height,
				   s32 pitchsrc, s32 pitchtgt)
{
	u16 c;

	for (int x=0; x<width; ++x)
		for (int y=0; y<height; ++y)
		{
			s32 a=0, r=0, g=0, b=0;

			for (int dx=0; dx<2; ++dx)
				for (int dy=0; dy<2; ++dy)
				{
					int tgx = (x*2)+dx;
					int tgy = (y*2)+dy;

					c = *(u16*)((void*)&src[(tgx*2)+(tgy*pitchsrc)]);

					a += getAlpha(c);
					r += getRed(c);
					g += getGreen(c);
					b += getBlue(c);
				}

			a /= 4;
			r /= 4;
			g /= 4;
			b /= 4;

			c = ((a & 0x1) <<15) | ((r & 0x1F)<<10) | ((g & 0x1F)<<5) | (b & 0x1F);
			*(u16*)((void*)&tgt[(x*2)+(y*pitchtgt)]) = c;
		}
}


void CD3D9Texture::copy32BitMipMap(char* src, char* tgt,
				   s32 width, s32 height,
				   s32 pitchsrc, s32 pitchtgt)
{
	SColor c;

	for (int x=0; x<width; ++x)
	{
		for (int y=0; y<height; ++y)
		{
			s32 a=0, r=0, g=0, b=0;

			for (int dx=0; dx<2; ++dx)
			{
				for (int dy=0; dy<2; ++dy)
				{
					int tgx = (x*2)+dx;
					int tgy = (y*2)+dy;

					c = *(u32*)((void*)&src[(tgx<<2)+(tgy*pitchsrc)]);

					a += c.getAlpha();
					r += c.getRed();
					g += c.getGreen();
					b += c.getBlue();
				}
			}

			a >>= 2;
			r >>= 2;
			g >>= 2;
			b >>= 2;

			c = ((a & 0xff)<<24) | ((r & 0xff)<<16) | ((g & 0xff)<<8) | (b & 0xff);
			*(u32*)((void*)&tgt[(x*4)+(y*pitchtgt)]) = c.color;
		}
	}
}


//! Regenerates the mip map levels of the texture. Useful after locking and
//! modifying the texture
void CD3D9Texture::regenerateMipMapLevels()
{
	if (HasMipMaps)
		createMipMaps();
}


//! returns if it is a render target
bool CD3D9Texture::isRenderTarget()
{
	return IsRenderTarget;
}

//! Returns pointer to the render target surface
IDirect3DSurface9* CD3D9Texture::getRenderTargetSurface()
{
	if (!IsRenderTarget)
		return 0;

	IDirect3DSurface9 *pRTTSurface = 0;
	if (Texture)
		Texture->GetSurfaceLevel(0, &pRTTSurface);

	if (pRTTSurface)
		pRTTSurface->Release();

	return pRTTSurface;
}



} // end namespace video
} // end namespace irr

#endif // _IRR_COMPILE_WITH_DIRECT3D_9_
