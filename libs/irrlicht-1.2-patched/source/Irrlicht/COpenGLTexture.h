// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_OPEN_GL_TEXTURE_H_INCLUDED__
#define __C_OPEN_GL_TEXTURE_H_INCLUDED__

#include "ITexture.h"
#include "IImage.h"

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_OPENGL_

#ifdef _IRR_WINDOWS_
	// include windows headers for HWND
	#define WIN32_LEAN_AND_MEAN
	#include <windows.h>
	#include <GL/gl.h>
	#include <GL/glu.h>
	#include "glext.h"
	#pragma comment(lib, "OpenGL32.lib")
	#pragma comment(lib, "GLu32.lib")
#elif defined(MACOSX)
	#include <OpenGL/gl.h>
	#include <OpenGL/glu.h>
	#include <OpenGL/glext.h>
#else
	#if defined(_IRR_OPENGL_USE_EXTPOINTER_)
		#define GL_GLEXT_LEGACY 1
	#endif
	#include <GL/gl.h>
	#if defined(_IRR_OPENGL_USE_EXTPOINTER_)
		#include "glext.h"
	#endif
	#include <GL/glu.h>
#endif


namespace irr
{
namespace video
{

class COpenGLDriver;
//! OpenGL texture.
class COpenGLTexture : public ITexture
{
public:

	//! constructor
	COpenGLTexture(IImage* surface, bool generateMipLevels, const char* name, COpenGLDriver* driver=0);
    //! FrameBufferObject constructor
	COpenGLTexture(const core::dimension2d<s32>& size, bool extPackedDepthStencilSupported, const char* name, COpenGLDriver* driver=0);

	//! destructor
	virtual ~COpenGLTexture();

	//! lock function
	virtual void* lock();

	//! unlock function
	virtual void unlock();

	//! Returns original size of the texture.
	virtual const core::dimension2d<s32>& getOriginalSize();

	//! Returns size of the texture.
	virtual const core::dimension2d<s32>& getSize();

	//! returns driver type of texture (=the driver, who created the texture)
	virtual E_DRIVER_TYPE getDriverType();

	//! returns color format of texture
	virtual ECOLOR_FORMAT getColorFormat() const;

	//! returns pitch of texture (in bytes)
	virtual s32 getPitch();

	//! return open gl texture name
	GLuint getOpenGLTextureName();

	//! return whether this texture has mipmaps
	virtual bool hasMipMaps();

	//! Regenerates the mip map levels of the texture. Useful after
	//! locking and modifying the texture
	virtual void regenerateMipMapLevels();

	//! Is it a FrameBufferObject?
	bool isFrameBufferObject();

	//! Bind FrameBufferObject (valid only if isFrameBufferObject() returns true).
	void bindFrameBufferObject();

	//! Unbind FrameBufferObject (valid only if isFrameBufferObject() returns true).
	void unbindFrameBufferObject();

private:

	void getImageData(IImage* image);

	//! copies the the texture into an open gl texture.
	//! \param: newTexture is true if method is called from a newly created texture for the first time. Otherwise call with false to improve memory handling.
	void copyTexture(bool newTexture=true);

	//! returns the size of a texture which would be the optimize size for rendering it
	inline s32 getTextureSizeFromSurfaceSize(s32 size);

	core::dimension2d<s32> ImageSize;
	core::dimension2d<s32> OriginalSize;
	s32 Pitch;
	bool SurfaceHasSameSize; // true if Surface has the same dimension as texture.
	u8* ImageData;
	ECOLOR_FORMAT ColorFormat;
	COpenGLDriver* Driver;

	GLuint TextureName;
	GLint InternalFormat;
	GLenum PixelFormat;
	GLenum PixelType;
	bool HasMipMaps;
	bool AutomaticMipmapUpdate;

	GLuint ColorFrameBuffer; // for FBO path
	GLuint DepthRenderBuffer; // for FBO path
	GLuint StencilRenderBuffer; // for FBO path
};


} // end namespace video
} // end namespace irr

#endif
#endif // _IRR_COMPILE_WITH_OPENGL_

