// Copyright (C) 2002-2005 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_VIDEO_2_SOFTWARE_H_INCLUDED__
#define __C_VIDEO_2_SOFTWARE_H_INCLUDED__

#include "SoftwareDriver2_compile_config.h"
#include "ITriangleRenderer2.h"
#include "CNullDriver.h"
#include "CImage.h"

namespace irr
{
namespace video
{
	class CSoftwareDriver2 : public CNullDriver
	{
	public:

		//! constructor
		CSoftwareDriver2(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter);

		//! destructor
		virtual ~CSoftwareDriver2();

		//! presents the rendered scene on the screen, returns false if failed
		virtual bool endScene();

		//! queries the features of the driver, returns true if feature is available
		virtual bool queryFeature(E_VIDEO_DRIVER_FEATURE feature);

		//! sets transformation
		virtual void setTransform(E_TRANSFORMATION_STATE state, const core::matrix4& mat);

		//! sets a material
		virtual void setMaterial(const SMaterial& material);

		virtual bool setRenderTarget(video::ITexture* texture, bool clearBackBuffer, 
								 bool clearZBuffer, SColor color);

		//! sets a viewport
		virtual void setViewPort(const core::rect<s32>& area);

		//! clears the zbuffer
		virtual bool beginScene(bool backBuffer, bool zBuffer, SColor color);

		//! draws an indexed triangle list
		virtual void drawIndexedTriangleList(const S3DVertex* vertices, s32 vertexCount,
			const u16* indexList, s32 triangleCount);

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

		//! draws an 2d image
		virtual void draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos);

		//! draws an 2d image, using a color (if color is other then Color(255,255,255,255)) and the alpha channel of the texture if wanted.
		virtual void draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos,
			const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect = 0, 
			SColor color=SColor(255,255,255,255), bool useAlphaChannelOfTexture=false);

		//! draw an 2d rectangle
		virtual void draw2DRectangle(SColor color, const core::rect<s32>& pos, 
			const core::rect<s32>* clip = 0);

		//!Draws an 2d rectangle with a gradient.
		virtual void draw2DRectangle(const core::rect<s32>& pos,
			SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
			const core::rect<s32>* clip = 0);

		//! Draws a 2d line. 
		virtual void draw2DLine(const core::position2d<s32>& start,
								const core::position2d<s32>& end, 
								SColor color=SColor(255,255,255,255));

		//! Draws a 3d line.
		virtual void draw3DLine(const core::vector3df& start, 
							const core::vector3df& end, SColor color);

		//! \return Returns the name of the video driver. Example: In case of the DirectX8
		//! driver, it would return "Direct3D8.1".
		virtual const wchar_t* getName();

		//! Returns type of video driver
		virtual E_DRIVER_TYPE getDriverType();

		//! Returns the transformation set by setTransform
		virtual core::matrix4 getTransform(E_TRANSFORMATION_STATE state);

		//! Creates a render target texture.
		virtual ITexture* createRenderTargetTexture(core::dimension2d<s32> size);
	
		//! Clears the ZBuffer. 
		virtual void clearZBuffer();

	protected:

		//! sets a render target
		void setRenderTarget(video::CImage* image);

		//! sets the current Texture
		void setTexture(u32 stage, video::ITexture* texture);

		//! returns a device dependent texture from a software surface (IImage)
		//! THIS METHOD HAS TO BE OVERRIDDEN BY DERIVED DRIVERS WITH OWN TEXTURES
		virtual video::ITexture* createDeviceDependentTexture(IImage* surface);

		video::CImage* BackBuffer;
		video::IImagePresenter* Presenter;

		//! switches to a triangle renderer
		void switchToTriangleRenderer(ETriangleRenderer2 renderer);

		//! void selects the right triangle renderer based on the render states.
		void selectRightTriangleRenderer();


		video::ITexture* RenderTargetTexture;	
		video::IImage* RenderTargetSurface;	
		core::position2d<s32> Render2DTranslation;
		core::dimension2d<s32> RenderTargetSize;
		core::dimension2d<s32> ViewPortSize;

		ITriangleRenderer2* CurrentTriangleRenderer;
		ITriangleRenderer2* TriangleRenderers[ETR2_COUNT];
		ETriangleRenderer2 CurrentRenderer;

		IZBuffer2* ZBuffer;

		video::ITexture* Texture[2];
		
		SMaterial Material;


		/*
			extend Matrix Stack
			-> combined CameraProjection
			-> combined CameraProjectionWorld
			-> ClipScale from NDC to DC Space
		*/
		enum E_TRANSFORMATION_STATE_2
		{
			ETS_VIEW_PROJECTION = ETS_COUNT,
			ETS_CURRENT,
			ETS_CLIPSCALE,

			ETS2_COUNT
		};

		core::matrix4 TransformationMatrix[ETS2_COUNT];

		// holds transformed, clipped vertices
		s4DVertex CurrentOut[10];
		s4DVertex Temp[10];
	};

} // end namespace video
} // end namespace irr


#endif

