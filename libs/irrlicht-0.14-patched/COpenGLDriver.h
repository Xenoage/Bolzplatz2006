// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in Irrlicht.h

#ifndef __C_VIDEO_OPEN_GL_H_INCLUDED__
#define __C_VIDEO_OPEN_GL_H_INCLUDED__

#include "CNullDriver.h"

#ifdef WIN32
// include windows headers for HWND
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#pragma comment(lib, "OpenGL32.lib")
#pragma comment(lib, "GLu32.lib")
#endif

#include "IrrCompileConfig.h"

#ifdef LINUX
#define GL_GLEXT_LEGACY 1
#include <X11/Xlib.h>
#endif // LINUX

#ifdef _IRR_COMPILE_WITH_OPENGL_
#include <GL/gl.h>
#include "glext.h"

namespace irr
{
namespace video
{
	class COpenGLTexture;
	
	class COpenGLDriver : public CNullDriver, IMaterialRendererServices
	{
	public:

		#ifdef _IRR_WINDOWS_
		//! win32 constructor
		COpenGLDriver(const core::dimension2d<s32>& screenSize, HWND window, bool fullscreen,
			bool stencilBuffer, io::IFileSystem* io);

		//! inits the open gl driver
		bool initDriver(const core::dimension2d<s32>& screenSize, HWND window,
			bool fullscreen, bool vsync);
		#endif

		#ifdef LINUX
		COpenGLDriver(const core::dimension2d<s32>& screenSize, bool fullscreen, bool doublebuffer, 
			bool stencilBuffer, Window window, Display* display, io::IFileSystem* io);
		#endif		

		//! destructor
		virtual ~COpenGLDriver();

		//! presents the rendered scene on the screen, returns false if failed
		virtual bool endScene();

		//! clears the zbuffer
		virtual bool beginScene(bool backBuffer, bool zBuffer, SColor color);

		//! sets transformation
		virtual void setTransform(E_TRANSFORMATION_STATE state, const core::matrix4& mat);

		//! draws an indexed triangle list
		virtual void drawIndexedTriangleList(const S3DVertex* vertices,
			s32 vertexCount, const u16* indexList, s32 triangleCount);

		//! draws an indexed triangle list
		virtual void drawIndexedTriangleList(const S3DVertex2TCoords* vertices,
			s32 vertexCount, const u16* indexList, s32 triangleCount);

		//! Draws an indexed triangle list.
		virtual void drawIndexedTriangleList(const S3DVertexTangents* vertices,
			s32 vertexCount, const u16* indexList, s32 triangleCount);

		//! Draws an indexed triangle fan.
		virtual void drawIndexedTriangleFan(const S3DVertex* vertices,
			s32 vertexCount, const u16* indexList, s32 triangleCount);

		//! Draws an indexed triangle fan.
		virtual void drawIndexedTriangleFan(const S3DVertex2TCoords* vertices,
			s32 vertexCount, const u16* indexList, s32 triangleCount);

		//! queries the features of the driver, returns true if feature is available
		bool queryFeature(E_VIDEO_DRIVER_FEATURE feature);

		//! Sets a material. All 3d drawing functions draw geometry now
		//! using this material.
		//! \param material: Material to be used from now on.
		virtual void setMaterial(const SMaterial& material);

		//! draws an 2d image
		virtual void draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos);

		//! draws an 2d image, using a color (if color is other then Color(255,255,255,255)) and the alpha channel of the texture if wanted.
		virtual void draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos,
			const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect = 0, 
			SColor color=SColor(255,255,255,255), bool useAlphaChannelOfTexture=false);

		//! Draws a part of the texture into the rectangle.
		virtual void draw2DImage(video::ITexture* texture, const core::rect<s32>& destRect,
			const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect = 0,
			SColor color=SColor(255,255,255,255), bool useAlphaChannelOfTexture=false);
			
		//! Draws a part of the texture into the rotated rectangle.
		virtual void draw2DImageRotation(video::ITexture* texture, const core::rect<s32>& destRect,
			const core::rect<s32>& sourceRect,
			const f32 rotation = 0.0f,
			SColor color=SColor(255,255,255,255), bool useAlphaChannelOfTexture=false);
			
		//! Draws a part of the texture into the rotated rectangle.
		virtual void draw2DImageRotation(video::ITexture* texture, const core::rect<s32>& destRect,
			const core::rect<s32>& sourceRect,
			const core::position2d<s32>& rotationPos, const f32 rotation, 
			SColor color=SColor(255,255,255,255), bool useAlphaChannelOfTexture=false);

		//! draw an 2d rectangle
		virtual void draw2DRectangle(SColor color, const core::rect<s32>& pos,
			const core::rect<s32>* clip = 0);

		//!Draws an 2d rectangle with a gradient.
		virtual void draw2DRectangle(const core::rect<s32>& pos,
			SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
			const core::rect<s32>* clip = 0);

		//! \return Returns the name of the video driver. Example: In case of the Direct3D8
		//! driver, it would return "Direct3D8.1".
		virtual const wchar_t* getName();

		//! deletes all dynamic lights there are
		virtual void deleteAllDynamicLights();

		//! adds a dynamic light
		virtual void addDynamicLight(const SLight& light);

		//! returns the maximal amount of dynamic lights the device can handle
		virtual s32 getMaximalDynamicLightAmount();

		//! Sets the dynamic ambient light color. The default color is
		//! (0,0,0,0) which means it is dark.
		//! \param color: New color of the ambient light.
		virtual void setAmbientLight(const SColorf& color);

		//! Draws a shadow volume into the stencil buffer. To draw a stencil shadow, do
		//! this: Frist, draw all geometry. Then use this method, to draw the shadow
		//! volume. Then, use IVideoDriver::drawStencilShadow() to visualize the shadow.
		virtual void drawStencilShadowVolume(const core::vector3df* triangles, s32 count, bool zfail);

		//! Fills the stencil shadow with color. After the shadow volume has been drawn
		//! into the stencil buffer using IVideoDriver::drawStencilShadowVolume(), use this
		//! to draw the color of the shadow. 
		virtual void drawStencilShadow(bool clearStencilBuffer=false, 
			video::SColor leftUpEdge = video::SColor(0,0,0,0), 
			video::SColor rightUpEdge = video::SColor(0,0,0,0),
			video::SColor leftDownEdge = video::SColor(0,0,0,0),
			video::SColor rightDownEdge = video::SColor(0,0,0,0));

		//! Draws a 3d line.
		virtual void draw3DLine(const core::vector3df& start,
			const core::vector3df& end, SColor color = SColor(255,255,255,255));

		//! sets a viewport //added by klems@sdi-syndrom.de
		virtual void setViewPort(const core::rect<s32>& area);

		//! Sets the fog mode.
		virtual void setFog(SColor color, bool linearFog, f32 start, 
			f32 end, f32 density, bool pixelFog, bool rangeFog);

		//! Only used by the internal engine. Used to notify the driver that
		//! the window was resized.
		virtual void OnResize(const core::dimension2d<s32>& size);

		//! Draws a 2d line. 
		virtual void draw2DLine(const core::position2d<s32>& start,
								const core::position2d<s32>& end, 
								SColor color=SColor(255,255,255,255));

		//! Returns type of video driver
		virtual E_DRIVER_TYPE getDriverType();

		//! Returns the transformation set by setTransform
		virtual core::matrix4 getTransform(E_TRANSFORMATION_STATE state);
		
		// public access to the (loaded) extensions. 
		void extGlActiveTextureARB(GLenum texture);
		void extGlClientActiveTextureARB(GLenum texture);
		void extGlGenProgramsARB(GLsizei n, GLuint *programs);
		void extGlBindProgramARB(GLenum target, GLuint program);
		void extGlProgramStringARB(GLenum target, GLenum format, GLsizei len, const GLvoid *string);
		void extGlDeleteProgramsARB(GLsizei n, const GLuint *programs);
		void extGlProgramLocalParameter4fvARB(GLenum, GLuint, const GLfloat *);
		
		GLhandleARB extGlCreateShaderObjectARB(GLenum shaderType); 
		void extGlShaderSourceARB(GLhandleARB shader, int numOfStrings, const char **strings, int *lenOfStrings); 
		void extGlCompileShaderARB(GLhandleARB shader); 
		GLhandleARB extGlCreateProgramObjectARB(void); 
		void extGlAttachObjectARB(GLhandleARB program, GLhandleARB shader); 
		void extGlLinkProgramARB(GLhandleARB program); 
		void extGlUseProgramObjectARB(GLhandleARB prog); 
		void extGlDeleteObjectARB(GLhandleARB object); 
		void extGlGetObjectParameterivARB(GLhandleARB object, GLenum type, int *param); 
		GLint extGlGetUniformLocationARB(GLhandleARB program, const char *name); 
		void extGlUniform4fvARB(GLint location, GLsizei count, const GLfloat *v); 
		
		void extGlUniform1fvARB (GLint loc, GLsizei count, const GLfloat *v);
		void extGlUniform2fvARB (GLint loc, GLsizei count, const GLfloat *v);
		void extGlUniform3fvARB (GLint loc, GLsizei count, const GLfloat *v);
		void extGlUniformMatrix2fvARB (GLint loc, GLsizei count, GLboolean transpose, const GLfloat *v);
		void extGlUniformMatrix3fvARB (GLint loc, GLsizei count, GLboolean transpose, const GLfloat *v);
		void extGlUniformMatrix4fvARB (GLint loc, GLsizei count, GLboolean transpose, const GLfloat *v);
		void extGlGetActiveUniformARB (GLhandleARB program, GLuint index, GLsizei maxlength, GLsizei *length, GLint *size, GLenum *type, GLcharARB *name);

		bool hasMultiTextureExtension();

		//! Can be called by an IMaterialRenderer to make its work easier.
		void setBasicRenderStates(const SMaterial& material, const SMaterial& lastmaterial,
			bool resetAllRenderstates);

		//! Sets a vertex shader constant.
		virtual void setVertexShaderConstant(f32* data, s32 startRegister, s32 constantAmount=1);

		//! Sets a pixel shader constant.
		virtual void setPixelShaderConstant(f32* data, s32 startRegister, s32 constantAmount=1);

		//! Sets a constant for the vertex shader based on a name. 
		virtual bool setVertexShaderConstant(const c8* name, const f32* floats, int count);

		//! Sets a constant for the pixel shader based on a name.
		virtual bool setPixelShaderConstant(const c8* name, const f32* floats, int count);

		//! sets the current Texture
		void setTexture(s32 stage, video::ITexture* texture);

		//! Adds a new material renderer to the VideoDriver, usingextGLGetObjectParameterivARB(shaderHandle, GL_OBJECT_COMPILE_STATUS_ARB, &status) pixel and/or 
		//! vertex shaders to render geometry.
		s32 addShaderMaterial(const c8* vertexShaderProgram, const c8* pixelShaderProgram,
			IShaderConstantSetCallBack* callback, E_MATERIAL_TYPE baseMaterial, s32 userData);
		
		//! Adds a new material renderer to the VideoDriver, using GLSL to render geometry.
		s32 addHighLevelShaderMaterial(const c8* vertexShaderProgram, const c8* vertexShaderEntryPointName,
			E_VERTEX_SHADER_TYPE vsCompileTarget, const c8* pixelShaderProgram, const c8* pixelShaderEntryPointName,
			E_PIXEL_SHADER_TYPE psCompileTarget, IShaderConstantSetCallBack* callback, E_MATERIAL_TYPE baseMaterial,
			s32 userData);
			
		//! Returns pointer to the IGPUProgrammingServices interface.
		IGPUProgrammingServices* getGPUProgrammingServices();

		//! Returns a pointer to the IVideoDriver interface. (Implementation for
		//! IMaterialRendererServices)
		virtual IVideoDriver* getVideoDriver();
				
		ITexture* createRenderTargetTexture(core::dimension2d<s32> size);
		
		bool setRenderTarget(video::ITexture* texture, bool clearBackBuffer, 
								 bool clearZBuffer, SColor color);

		//! Clears the ZBuffer. 
		virtual void clearZBuffer();

	private:

		//! returns a device dependent texture from a software surface (IImage)
		//! THIS METHOD HAS TO BE OVERRIDDEN BY DERIVED DRIVERS WITH OWN TEXTURES
		virtual video::ITexture* createDeviceDependentTexture(IImage* surface);

		inline void createGLMatrix(GLfloat gl_matrix[16], const core::matrix4& m)
		{
			s32 i = 0;
			for (s32 r=0; r<4; ++r)
				for (s32 c=0; c<4; ++c)
					gl_matrix[i++] = m(c,r);
		}

		//! sets the needed renderstates
		void setRenderStates3DMode();

		//! sets the needed renderstates
		void setRenderStates2DMode(bool alpha, bool texture, bool alphaChannel);

		//! prints error if an error happened.
		void printGLError();

		// returns the current size of the screen or rendertarget
		core::dimension2d<s32> getCurrentRenderTargetSize();

		void loadExtensions();
		void createMaterialRenderers();

		core::matrix4 Matrizes[ETS_COUNT];
		core::array<s32> ColorBuffer;

		#ifdef _IRR_WINDOWS_		
		HDC HDc; // Private GDI Device Context
		HWND Window;
		HGLRC HRc; // Permanent Rendering Context
		#endif

		// enumeration for rendering modes such as 2d and 3d for minizing the switching of renderStates.
		enum E_RENDER_MODE
		{
			ERM_NONE = 0,	// no render state has been set yet.
			ERM_2D,			// 2d drawing rendermode
			ERM_3D,			// 3d rendering mode
		};

		E_RENDER_MODE CurrentRenderMode;
		bool ResetRenderStates; // bool to make all renderstates be reseted if set.
		bool Transformation3DChanged;
		bool MultiTextureExtension;
		bool StencilBuffer;
		bool ARBVertexProgramExtension; //GL_ARB_vertex_program
		bool ARBFragmentProgramExtension; //GL_ARB_fragment_program
		bool ARBShadingLanguage100Extension; 
		bool AnisotropyExtension;
		
		SMaterial Material, LastMaterial;
		COpenGLTexture* RenderTargetTexture;
		s32 LastSetLight;	
		f32 MaxAnisotropy;
		f32 AnisotropyToUse;

		GLint MaxTextureUnits;

		core::dimension2d<s32> CurrentRendertargetSize;

	#ifdef _IRR_WINDOWS_
		PFNGLACTIVETEXTUREARBPROC pGlActiveTextureARB;
		PFNGLCLIENTACTIVETEXTUREARBPROC	pGlClientActiveTextureARB;

		typedef BOOL (APIENTRY *PFNWGLSWAPINTERVALFARPROC)(int);
		PFNWGLSWAPINTERVALFARPROC wglSwapIntervalEXT;

		PFNGLGENPROGRAMSARBPROC pGlGenProgramsARB;
		PFNGLBINDPROGRAMARBPROC pGlBindProgramARB;
		PFNGLPROGRAMSTRINGARBPROC pGlProgramStringARB;
		PFNGLDELETEPROGRAMSNVPROC pGlDeleteProgramsARB;
		PFNGLPROGRAMLOCALPARAMETER4FVARBPROC pGlProgramLocalParameter4fvARB;
	#endif
		PFNGLCREATESHADEROBJECTARBPROC pGlCreateShaderObjectARB; 
		PFNGLSHADERSOURCEARBPROC pGlShaderSourceARB; 
		PFNGLCOMPILESHADERARBPROC pGlCompileShaderARB; 
		PFNGLCREATEPROGRAMOBJECTARBPROC pGlCreateProgramObjectARB; 
		PFNGLATTACHOBJECTARBPROC pGlAttachObjectARB; 
		PFNGLLINKPROGRAMARBPROC pGlLinkProgramARB; 
		PFNGLUSEPROGRAMOBJECTARBPROC pGlUseProgramObjectARB; 
		PFNGLDELETEOBJECTARBPROC pGlDeleteObjectARB; 
		PFNGLGETOBJECTPARAMETERIVARBPROC pGlGetObjectParameterivARB; 
		PFNGLGETUNIFORMLOCATIONARBPROC pGlGetUniformLocationARB; 
		PFNGLUNIFORM4FVARBPROC pGlUniform4fvARB; 
		PFNGLUNIFORM1FVARBPROC pGlUniform1fvARB;
		PFNGLUNIFORM2FVARBPROC pGlUniform2fvARB;
		PFNGLUNIFORM3FVARBPROC pGlUniform3fvARB;
		PFNGLUNIFORMMATRIX2FVARBPROC pGlUniformMatrix2fvARB;
		PFNGLUNIFORMMATRIX3FVARBPROC pGlUniformMatrix3fvARB;
		PFNGLUNIFORMMATRIX4FVARBPROC pGlUniformMatrix4fvARB;
		PFNGLGETACTIVEUNIFORMARBPROC pGlGetActiveUniformARB;
		
	#ifdef LINUX
		bool DoubleBuffered;
		Window XWindow;
		Display* XDisplay;
	#endif
	};

} // end namespace video
} // end namespace irr


#endif // _IRR_COMPILE_WITH_OPENGL_
#endif

