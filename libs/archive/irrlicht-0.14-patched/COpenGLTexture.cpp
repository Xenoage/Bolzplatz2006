// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "irrTypes.h"
#include "COpenGLTexture.h"
#include "os.h"
#include "CColorConverter.h"

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_OPENGL_

#include "glext.h"

namespace irr
{
namespace video  
{

//! constructor
COpenGLTexture::COpenGLTexture(IImage* image, bool generateMipLevels)
: Pitch(0), ImageSize(0,0), hasMipMaps(generateMipLevels),
ImageData(0), TextureName(0)
{
	#ifdef _DEBUG
    setDebugName("COpenGLTexture");
	#endif

	#ifdef DISABLE_MIPMAPPING
		hasMipMaps = false;
	#endif

	if (image)
	{
		getImageData(image);

		if (ImageData)
		{
			glGenTextures(1, &TextureName);
			copyTexture();
		}		
	}
}


//! destructor
COpenGLTexture::~COpenGLTexture()
{
	delete [] ImageData;
	glDeleteTextures(1, &TextureName);
}


void COpenGLTexture::getImageData(IImage* image)
{
	ImageSize = image->getDimension();
	OriginalSize = ImageSize;

	core::dimension2d<s32> nImageSize;
	nImageSize.Width = getTextureSizeFromSurfaceSize(ImageSize.Width);
	nImageSize.Height = getTextureSizeFromSurfaceSize(ImageSize.Height);

	if (!nImageSize.Width || !nImageSize.Height ||
		!ImageSize.Width || !ImageSize.Height)
	{
		os::Printer::log("Could not create OpenGL Texture.", ELL_ERROR);
		return;
	}

	ImageData = new s32[nImageSize.Width * nImageSize.Height];

	if (nImageSize == ImageSize)
	{
		if (image->getColorFormat() == ECF_A8R8G8B8)
		{
			s32 s = nImageSize.Width * nImageSize.Height;
			s32 *t = (s32*)image->lock();
			for (s32 i=0; i<s; ++i)
				ImageData[i] = t[i];
			image->unlock();
		}
		else
		{
			// slow converting
			for (s32 x=0; x<ImageSize.Width; ++x)
				for (s32 y=0; y<ImageSize.Height; ++y)
					ImageData[y*nImageSize.Width + x] = image->getPixel(x,y).color;
		}
	}
	else
	{
		// scale texture

		s32* source = (s32*)image->lock();

		f32 sourceXStep = (f32)ImageSize.Width / (f32)nImageSize.Width;
		f32 sourceYStep = (f32)ImageSize.Height / (f32)nImageSize.Height;
		f32 sy;

		if (image->getColorFormat() == ECF_A8R8G8B8)
		{
			// copy texture scaling
			for (s32 x=0; x<nImageSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<nImageSize.Height; ++y)
				{
					ImageData[(s32)(y*nImageSize.Width + x)] = source[(s32)(((s32)sy)*ImageSize.Width + x*sourceXStep)];
					sy+=sourceYStep;
				}
			}
		}
		else
		{
			// convert texture scaling, slow
			for (s32 x=0; x<nImageSize.Width; ++x)
			{
				sy = 0.0f;

				for (s32 y=0; y<nImageSize.Height; ++y)
				{
					ImageData[(s32)(y*nImageSize.Width + x)] = 
						image->getPixel((s32)(x*sourceXStep), (s32)sy).color;

					sy+=sourceYStep;
				}
			}
		}
	}		

	ImageSize = nImageSize;
}


//! test if an error occurred, prints the problem, and returns 
//! true if an error happened
inline bool COpenGLTexture::testError()
{
	#ifdef _DEBUG
	GLenum g = glGetError();
	switch(g)
	{
	case GL_NO_ERROR:
		return false;
	case GL_INVALID_ENUM:
		os::Printer::log("GL_INVALID_ENUM", ELL_ERROR); break;
	case GL_INVALID_VALUE:
		os::Printer::log("GL_INVALID_VALUE", ELL_ERROR); break;
	case GL_INVALID_OPERATION:
		os::Printer::log("GL_INVALID_OPERATION", ELL_ERROR); break;
	case GL_STACK_OVERFLOW:
		os::Printer::log("GL_STACK_OVERFLOW", ELL_ERROR); break;
	case GL_STACK_UNDERFLOW:
		os::Printer::log("GL_STACK_UNDERFLOW", ELL_ERROR); break;
	case GL_OUT_OF_MEMORY:
		os::Printer::log("GL_OUT_OF_MEMORY", ELL_ERROR); break;
	};

	return true;
#endif
	return false;	
}


//! copies the the texture into an open gl texture.
void COpenGLTexture::copyTexture()
{
	glBindTexture(GL_TEXTURE_2D, TextureName);
	if (testError())
		os::Printer::log("Could not bind Texture", ELL_ERROR);

	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, ImageSize.Width, 
		ImageSize.Height, 0, GL_BGRA_EXT , GL_UNSIGNED_BYTE, ImageData);
	if (testError())
		os::Printer::log("Could not glTexImage2D", ELL_ERROR);

	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

	if (hasMipMaps)
	{
		s32 ret = 0;
		
		#ifndef DISABLE_MIPMAPPING
		ret = gluBuild2DMipmaps(GL_TEXTURE_2D, 4, ImageSize.Width, ImageSize.Height,
 		            GL_BGRA_EXT, GL_UNSIGNED_BYTE, ImageData);
		#endif

		if (ret)
		{
			#ifndef DISABLE_MIPMAPPING
			os::Printer::log("Could not create OpenGL texture mip maps.",
				(c8*)gluErrorString(ret), ELL_ERROR);
			#else
			os::Printer::log("Did not create OpenGL texture mip maps.", ELL_ERROR);
			#endif
		}
		else
		{
			glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST );
			glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
		}
	}	
}


//! returns the size of a texture which would be the optimize size for rendering it
inline s32 COpenGLTexture::getTextureSizeFromSurfaceSize(s32 size)
{
	s32 ts = 0x01;
	while(ts < size)
		ts <<= 1;

	return ts;
}


//! lock function
void* COpenGLTexture::lock()
{
	return ImageData;
}



//! unlock function
void COpenGLTexture::unlock()
{
	copyTexture();
}


//! Sets a random pixel on the texture to white (snow) (ANDI)

void COpenGLTexture::snowPixel(s32 count)
{
/*
  os::Randomizer r;
  s32 w = ImageSize.Width;
  s32 h = ImageSize.Height;
  for (s32 i = 0; i < count; i++)
  {
    s32 x = r.rand() % w;
    s32 y = r.rand() % h;
    s32 c = 220 + r.rand() % 30;
    ImageData[x * w + y] = RGB(c, c, c);
  }
  copyTexture();
*/
}


//! Returns original size of the texture.
const core::dimension2d<s32>& COpenGLTexture::getOriginalSize()
{
	return OriginalSize;
}


//! Returns (=size) of the texture.
const core::dimension2d<s32>& COpenGLTexture::getSize()
{
	return ImageSize;
}


//! returns driver type of texture (=the driver, who created the texture)
E_DRIVER_TYPE COpenGLTexture::getDriverType()
{
	return EDT_OPENGL;
}



//! returns color format of texture
ECOLOR_FORMAT COpenGLTexture::getColorFormat()
{
	return ECF_A8R8G8B8;
}



//! returns pitch of texture (in bytes)
s32 COpenGLTexture::getPitch()
{
	return ImageSize.Width * 4;
}



//! return open gl texture name
GLuint COpenGLTexture::getOpenGLTextureName()
{
	return TextureName;
}


//! Regenerates the mip map levels of the texture. Useful after locking and 
//! modifying the texture
void COpenGLTexture::regenerateMipMapLevels()
{
	if (hasMipMaps)
		copyTexture();
}


} // end namespace video
} // end namespace irr

#endif // _IRR_COMPILE_WITH_OPENGL_
