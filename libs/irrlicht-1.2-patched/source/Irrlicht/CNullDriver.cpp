// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include <stdio.h>
#include <string.h>
#include "CNullDriver.h"
#include "CSoftwareTexture.h"
#include "os.h"
#include "CImage.h"
#include "CAttributes.h"
#include "IWriteFile.h"

namespace irr
{
namespace video
{

//! creates a loader which is able to load windows bitmaps
IImageLoader* createImageLoaderBMP();

//! creates a loader which is able to load jpeg images
IImageLoader* createImageLoaderJPG();

//! creates a loader which is able to load targa images
IImageLoader* createImageLoaderTGA();

//! creates a loader which is able to load psd images
IImageLoader* createImageLoaderPSD();

//! creates a loader which is able to load pcx images
IImageLoader* createImageLoaderPCX();

//! creates a loader which is able to load png images
IImageLoader* createImageLoaderPNG();


//! creates a loader which is able to load bmp images
IImageWriter* createImageWriterBMP();

//! creates a loader which is able to load jpg images
IImageWriter* createImageWriterJPG();

//! creates a loader which is able to load tga images
IImageWriter* createImageWriterTGA();

//! creates a loader which is able to load psd images
IImageWriter* createImageWriterPSD();

//! creates a loader which is able to load pcx images
IImageWriter* createImageWriterPCX();

//! creates a loader which is able to load png images
IImageWriter* createImageWriterPNG();

//! creates a loader which is able to load ppm images
IImageWriter* createImageWriterPPM();



//! Array holding the built in material type names
const char* const sBuiltInMaterialTypeNames[] =
{
	"solid",
	"solid_2layer",
	"lightmap",
	"lightmap_add",
	"lightmap_m2",
	"lightmap_m4",
	"lightmap_light",
	"lightmap_light_m2",
	"lightmap_light_m4",
	"detail_map",
	"sphere_map",
	"reflection_2layer",
	"trans_add",
	"trans_alphach",
	"trans_alphach_ref",
	"trans_vertex_alpha",
	"trans_reflection_2layer",
	"normalmap_solid",
	"normalmap_trans_add",
	"normalmap_trans_vertexalpha",
	"parallaxmap_solid",
	"parallaxmap_trans_add",
	"parallaxmap_trans_vertexalpha"
};


//! constructor
CNullDriver::CNullDriver(io::IFileSystem* io, const core::dimension2d<s32>& screenSize)
: ScreenSize(screenSize), ViewPort(0,0,0,0),
 FileSystem(io), PrimitivesDrawn(0), TextureCreationFlags(0)
{
	#ifdef _DEBUG
	setDebugName("CNullDriver");
	#endif

	setFog();

	setTextureCreationFlag(ETCF_ALWAYS_32_BIT, true);
	setTextureCreationFlag(ETCF_CREATE_MIP_MAPS, true);

	ViewPort = core::rect<s32>(core::position2d<s32>(0,0), screenSize);

	if (FileSystem)
		FileSystem->grab();

	// create surface loader

	SurfaceLoader.push_back(video::createImageLoaderBMP());
	SurfaceLoader.push_back(video::createImageLoaderJPG());
	SurfaceLoader.push_back(video::createImageLoaderTGA());
	SurfaceLoader.push_back(video::createImageLoaderPSD());
	SurfaceLoader.push_back(video::createImageLoaderPCX());
	SurfaceLoader.push_back(video::createImageLoaderPNG());

	SurfaceWriter.push_back(video::createImageWriterBMP());
	SurfaceWriter.push_back(video::createImageWriterJPG());
	SurfaceWriter.push_back(video::createImageWriterTGA());
	SurfaceWriter.push_back(video::createImageWriterPSD());
	SurfaceWriter.push_back(video::createImageWriterPCX());
	SurfaceWriter.push_back(video::createImageWriterPNG());
	SurfaceWriter.push_back(video::createImageWriterPPM());

	// set ExposedData to 0
	memset(&ExposedData, 0, sizeof(ExposedData));
}



//! destructor
CNullDriver::~CNullDriver()
{
	// delete file system

	if (FileSystem)
		FileSystem->drop();

	// delete textures

	deleteAllTextures();

	// delete surface loader

	u32 i;
	for (i=0; i<SurfaceLoader.size(); ++i)
		SurfaceLoader[i]->drop();

	// delete surface writer

	for (i=0; i<SurfaceWriter.size(); ++i)
		SurfaceWriter[i]->drop();

	deleteMaterialRenders();
}


//! Adds an external surface loader to the engine.
void CNullDriver::addExternalImageLoader(IImageLoader* loader)
{
	if (!loader)
		return;

	loader->grab();
	SurfaceLoader.push_back(loader);
}


//! Adds an external surface writer to the engine.
void CNullDriver::addExternalImageWriter(IImageWriter* writer)
{
	if (!writer)
		return;

	writer->grab();
	SurfaceWriter.push_back(writer);
}


//! deletes all textures
void CNullDriver::deleteAllTextures()
{
	for (u32 i=0; i<Textures.size(); ++i)
		Textures[i].Surface->drop();

	Textures.clear();
}



//! applications must call this method before performing any rendering. returns false if failed.
bool CNullDriver::beginScene(bool backBuffer, bool zBuffer, SColor color)
{
	PrimitivesDrawn = 0;
	return true;
}



//! applications must call this method after performing any rendering. returns false if failed.
bool CNullDriver::endScene( s32 windowId, core::rect<s32>* sourceRect )
{
	FPSCounter.registerFrame(os::Timer::getRealTime());
	return true;
}



//! queries the features of the driver, returns true if feature is available
bool CNullDriver::queryFeature(E_VIDEO_DRIVER_FEATURE feature)
{
	return false;
}



//! sets transformation
void CNullDriver::setTransform(E_TRANSFORMATION_STATE state, const core::matrix4& mat)
{
}


//! Returns the transformation set by setTransform
const core::matrix4& CNullDriver::getTransform(E_TRANSFORMATION_STATE state)
{
	return TransformationMatrix;
}



//! sets a material
void CNullDriver::setMaterial(const SMaterial& material)
{
}



//! Removes a texture from the texture cache and deletes it, freeing lot of
//! memory.
void CNullDriver::removeTexture(ITexture* texture)
{
	for (u32 i=0; i<Textures.size(); ++i)
		if (Textures[i].Surface == texture)
		{
			texture->drop();
			Textures.erase(i);
		}
}


//! Removes all texture from the texture cache and deletes them, freeing lot of
//! memory.
void CNullDriver::removeAllTextures()
{
	deleteAllTextures();
}


//! Returns a texture by index
ITexture* CNullDriver::getTextureByIndex(s32 i)
{
	if (i>=0 && i<(int)Textures.size())
		return Textures[i].Surface;

	return 0;
}


//! Returns amount of textures currently loaded
s32 CNullDriver::getTextureCount()
{
	return Textures.size();
}


//! loads a Texture
ITexture* CNullDriver::getTexture(const c8* filename)
{
	ITexture* texture = findTexture(filename);

	if (texture)
		return texture;

	io::IReadFile* file = FileSystem->createAndOpenFile(filename);
	bool errorReported = false;

	if (file)
	{
		texture = loadTextureFromFile(file);
		file->drop();

		if (texture)
		{
			addTexture(texture);
			texture->drop(); // drop it becaus we created it, one grab to much
		}
	}
	else
	{
		errorReported = true;
		os::Printer::log("Could not open file of texture", filename, ELL_ERROR);
	}

	if (!texture && !errorReported)
		os::Printer::log("Could not load texture", filename, ELL_ERROR);

	return texture;
}



//! loads a Texture
ITexture* CNullDriver::getTexture(io::IReadFile* file)
{
	ITexture* texture = 0;

	if (file)
	{
		texture = findTexture(file->getFileName());

		if (texture)
			return texture;

		texture = loadTextureFromFile(file);

		if (texture)
		{
			addTexture(texture);
			texture->drop(); // drop it because we created it, one grab to much
		}
	}

	if (!texture)
		os::Printer::log("Could not load texture", file->getFileName(), ELL_ERROR);

	return texture;
}



//! opens the file and loads it into the surface
video::ITexture* CNullDriver::loadTextureFromFile(io::IReadFile* file)
{
	ITexture* texture = 0;
	IImage* image = createImageFromFile(file);

	if (image)
	{
		// create texture from surface
		texture = createDeviceDependentTexture(image, file->getFileName());
		os::Printer::log("Loaded texture", file->getFileName());
		image->drop();
	}

	return texture;
}



//! adds a surface, not loaded or created by the Irrlicht Engine
void CNullDriver::addTexture(video::ITexture* texture)
{
	if (texture)
	{
		SSurface s;
		s.Surface = texture;
		texture->grab();

		Textures.push_back(s);

		// the new texture is now at the end of the texture list. when searching for
		// the next new texture, the texture array will be sorted and the index of this texture
		// will be changed. to let the order be more consistent to the user, sort
		// the textures now already although this isn't necessary:

		Textures.sort();
	}
}



//! looks if the image is already loaded
video::ITexture* CNullDriver::findTexture(const c8* filename)
{
	if (!filename)
		filename = "";

	SSurface s;
	SDummyTexture dummy(filename);
	s.Surface = &dummy;

	s32 index = Textures.binary_search(s);
	if (index != -1)
		return Textures[index].Surface;

	return 0;
}



//! Creates a texture from a loaded IImage.
ITexture* CNullDriver::addTexture(const c8* name, IImage* image)
{
	if (!name || !image)
		return 0;

	ITexture* t = createDeviceDependentTexture(image, name);
	addTexture(t);
	t->drop();
	return t;
}



//! creates a Texture
ITexture* CNullDriver::addTexture(const core::dimension2d<s32>& size,
				 const c8* name, ECOLOR_FORMAT format)
{
	if (!name)
		return 0;

	IImage* image = new CImage(format, size);
	ITexture* t = createDeviceDependentTexture(image, name);
	image->drop();
	addTexture(t);

	if (t)
		t->drop();

	return t;
}



//! returns a device dependent texture from a software surface (IImage)
//! THIS METHOD HAS TO BE OVERRIDDEN BY DERIVED DRIVERS WITH OWN TEXTURES
ITexture* CNullDriver::createDeviceDependentTexture(IImage* surface, const char* name)
{
	return new CSoftwareTexture(surface, name);
}



//! sets a render target
bool CNullDriver::setRenderTarget(video::ITexture* texture, bool clearBackBuffer,
					 bool clearZBuffer, SColor color)
{
	return false;
}



//! sets a viewport
void CNullDriver::setViewPort(const core::rect<s32>& area)
{
}



//! gets the area of the current viewport
const core::rect<s32>& CNullDriver::getViewPort() const
{
	return ViewPort;
}



//! draws a vertex primitive list
void CNullDriver::drawVertexPrimitiveList(const void* vertices, s32 vertexCount, const u16* indexList, s32 primitiveCount, E_VERTEX_TYPE vType, scene::E_PRIMITIVE_TYPE pType)
{
	PrimitivesDrawn += primitiveCount;
}



//! draws an indexed triangle list
inline void CNullDriver::drawIndexedTriangleList(const S3DVertex* vertices, s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	drawVertexPrimitiveList(vertices, vertexCount, indexList, triangleCount, EVT_STANDARD, scene::EPT_TRIANGLES);
}



//! draws an indexed triangle list
inline void CNullDriver::drawIndexedTriangleList(const S3DVertex2TCoords* vertices, s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	drawVertexPrimitiveList(vertices, vertexCount, indexList, triangleCount, EVT_2TCOORDS, scene::EPT_TRIANGLES);
}


//! Draws an indexed triangle list.
inline void CNullDriver::drawIndexedTriangleList(const S3DVertexTangents* vertices,
	s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	drawVertexPrimitiveList(vertices, vertexCount, indexList, triangleCount, EVT_TANGENTS, scene::EPT_TRIANGLES);
}



//! Draws an indexed triangle fan.
inline void CNullDriver::drawIndexedTriangleFan(const S3DVertex* vertices,
	s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	drawVertexPrimitiveList(vertices, vertexCount, indexList, triangleCount, EVT_STANDARD, scene::EPT_TRIANGLE_FAN);
}



//! Draws an indexed triangle fan.
inline void CNullDriver::drawIndexedTriangleFan(const S3DVertex2TCoords* vertices,
	s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	drawVertexPrimitiveList(vertices, vertexCount, indexList, triangleCount, EVT_2TCOORDS, scene::EPT_TRIANGLE_FAN);
}



//! Draws a 3d line.
void CNullDriver::draw3DLine(const core::vector3df& start,
				const core::vector3df& end, SColor color)
{
}



//! Draws a 3d triangle.
void CNullDriver::draw3DTriangle(const core::triangle3df& triangle, SColor color)
{
	draw3DLine(triangle.pointA, triangle.pointB, color);
	draw3DLine(triangle.pointB, triangle.pointC, color);
	draw3DLine(triangle.pointC, triangle.pointA, color);
}



//! Draws a 3d axis aligned box.
void CNullDriver::draw3DBox(const core::aabbox3d<f32> box, SColor color)
{
	core::vector3df edges[8];
	box.getEdges(edges);

	// TODO: optimize into one big drawIndexPrimitive call.

	draw3DLine(edges[5], edges[1], color);
	draw3DLine(edges[1], edges[3], color);
	draw3DLine(edges[3], edges[7], color);
	draw3DLine(edges[7], edges[5], color);
	draw3DLine(edges[0], edges[2], color);
	draw3DLine(edges[2], edges[6], color);
	draw3DLine(edges[6], edges[4], color);
	draw3DLine(edges[4], edges[0], color);
	draw3DLine(edges[1], edges[0], color);
	draw3DLine(edges[3], edges[2], color);
	draw3DLine(edges[7], edges[6], color);
	draw3DLine(edges[5], edges[4], color);
}



//! draws an 2d image
void CNullDriver::draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos)
{
	if (!texture)
		return;

	draw2DImage(texture,destPos, core::rect<s32>(core::position2d<s32>(0,0), texture->getOriginalSize()));
}



//! draws a set of 2d images, using a color and the alpha channel of the
//! texture if desired. The images are drawn beginning at pos and concatenated
//! in one line. All drawings are clipped against clipRect (if != 0).
//! The subtextures are defined by the array of sourceRects and are chosen
//! by the indices given.
void CNullDriver::draw2DImage(video::ITexture* texture,
				const core::position2d<s32>& pos,
				const core::array<core::rect<s32> >& sourceRects,
				const core::array<s32>& indices,
				const core::rect<s32>* clipRect, SColor color,
				bool useAlphaChannelOfTexture)
{
	core::position2d<s32> target(pos);

	for (u32 i=0; i<indices.size(); ++i)
	{
		draw2DImage(texture, target, sourceRects[indices[i]],
				clipRect, color, useAlphaChannelOfTexture);
		target.X += sourceRects[indices[i]].getWidth();
	}
}



//! Draws a part of the texture into the rectangle.
void CNullDriver::draw2DImage(video::ITexture* texture, const core::rect<s32>& destRect,
	const core::rect<s32>& sourceRect, const core::rect<s32>* clipRect,
	/* BP2K6: */ SColor color /* video::SColor* colors*/,
	bool useAlphaChannelOfTexture)
{
}



//! draws an 2d image, using a color (if color is other then Color(255,255,255,255)) and the alpha channel of the texture if wanted.
void CNullDriver::draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos,
				 const core::rect<s32>& sourceRect,
				 const core::rect<s32>* clipRect, SColor color,
				 bool useAlphaChannelOfTexture)
{
}



//! draw an 2d rectangle
void CNullDriver::draw2DRectangle(SColor color, const core::rect<s32>& pos, const core::rect<s32>* clip)
{
	draw2DRectangle(pos, color, color, color, color, clip);
}



//!Draws an 2d rectangle with a gradient.
void CNullDriver::draw2DRectangle(const core::rect<s32>& pos,
	SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
	const core::rect<s32>* clip)
{
}



//! Draws a 2d line.
void CNullDriver::draw2DLine(const core::position2d<s32>& start,
				const core::position2d<s32>& end, SColor color)
{
}



//! Draws a non filled concyclic regular 2d polyon.
void CNullDriver::draw2DPolygon(core::position2d<s32> center,
	f32 radius, video::SColor color, s32 count)
{
	if (count < 2)
		return;

	core::position2d<s32> first;
	core::position2d<s32> a,b;

	for (s32 j=0; j<count; ++j)
	{
		b = a;

		f32 p = j / (f32)count * (core::PI*2);
		a = center + core::position2d<s32>((s32)(sin(p)*radius), (s32)(cos(p)*radius));

		if (j==0)
			first = a;
		else
			draw2DLine(a, b, color);
	}

	draw2DLine(a, first, color);
}




//! returns screen size
core::dimension2d<s32> CNullDriver::getScreenSize()
{
	return ScreenSize;
}

//! returns the current render target size,
//! or the screen size if render targets are not implemented
core::dimension2d<s32> CNullDriver::getCurrentRenderTargetSize()
{
	return ScreenSize;
}


// returns current frames per second value
s32 CNullDriver::getFPS()
{
	return FPSCounter.getFPS();
}



//! returns amount of primitives (mostly triangles) were drawn in the last frame.
//! very useful method for statistics.
u32 CNullDriver::getPrimitiveCountDrawn()
{
	return PrimitivesDrawn;
}



//! Sets the dynamic ambient light color. The default color is
//! (0,0,0,0) which means it is dark.
//! \param color: New color of the ambient light.
void CNullDriver::setAmbientLight(const SColorf& color)
{
}



//! \return Returns the name of the video driver. Example: In case of the DIRECT3D8
//! driver, it would return "Direct3D8".
const wchar_t* CNullDriver::getName()
{
	return L"Irrlicht NullDevice";
}



//! Draws a shadow volume into the stencil buffer. To draw a stencil shadow, do
//! this: Frist, draw all geometry. Then use this method, to draw the shadow
//! volume. Then, use IVideoDriver::drawStencilShadow() to visualize the shadow.
void CNullDriver::drawStencilShadowVolume(const core::vector3df* triangles, s32 count, bool zfail)
{
}


//! Fills the stencil shadow with color. After the shadow volume has been drawn
//! into the stencil buffer using IVideoDriver::drawStencilShadowVolume(), use this
//! to draw the color of the shadow.
void CNullDriver::drawStencilShadow(bool clearStencilBuffer, video::SColor leftUpEdge,
	video::SColor rightUpEdge,	video::SColor leftDownEdge,	video::SColor rightDownEdge)
{
}


//! deletes all dynamic lights there are
void CNullDriver::deleteAllDynamicLights()
{
	Lights.set_used(0);
}



//! adds a dynamic light
void CNullDriver::addDynamicLight(const SLight& light)
{
	Lights.push_back(light);
}



//! returns the maximal amount of dynamic lights the device can handle
s32 CNullDriver::getMaximalDynamicLightAmount()
{
	return 0;
}

//! Returns current amount of dynamic lights set
//! \return Current amount of dynamic lights set
s32 CNullDriver::getDynamicLightCount()
{
	return Lights.size();
}

//! Returns light data which was previously set with IVideDriver::addDynamicLight().
//! \param idx: Zero based index of the light. Must be greater than 0 and smaller
//! than IVideoDriver()::getDynamicLightCount.
//! \return Light data.
const SLight& CNullDriver::getDynamicLight(s32 idx)
{
	if (idx<0 || idx>=(s32)Lights.size())
		return *((SLight*)0);

	return Lights[idx];
}


//! Creates an 1bit alpha channel of the texture based of an color key.
void CNullDriver::makeColorKeyTexture(video::ITexture* texture, video::SColor color)
{
	if (!texture)
		return;

	if (texture->getColorFormat() != ECF_A1R5G5B5 &&
		texture->getColorFormat() != ECF_A8R8G8B8 )
	{
		os::Printer::log("Error: Unsupported texture color format for making color key channel.", ELL_ERROR);
		return;
	}

	if (texture->getColorFormat() == ECF_A1R5G5B5)
	{
		s16 *p = (s16*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making color key channel.", ELL_ERROR);
			return;
		}

		core::dimension2d<s32> dim = texture->getSize();
		s32 pitch = texture->getPitch() / 2;

		// color with alpha enabled (color opaque)
		s16 ref = (0x1<<15) | (0x7fff & color.toA1R5G5B5());

		for (s32 y=0; y<dim.Height; ++y)
		{
			for (s32 x=0; x<pitch; ++x)
			{
				s16 c = (0x1<<15) | (0x7fff & p[y*pitch + x]);
				p[y*pitch + x] = (c == ref) ? 0 : c;
			}
		}

		texture->unlock();
	}
	else
	{
		s32 *p = (s32*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making color key channel.", ELL_ERROR);
			return;
		}

		core::dimension2d<s32> dim = texture->getSize();
		s32 pitch = texture->getPitch() / 4;

		// color with alpha enabled (color opaque)
		s32 ref = (0xff<<24) | (0x00ffffff & color.color);

		for (s32 y=0; y<dim.Height; ++y)
		{
			for (s32 x=0; x<pitch; ++x)
			{
				s32 c = (0xff<<24) | (0x00ffffff & p[y*pitch + x]);
				p[y*pitch + x] = (c == ref) ? 0 : c;
			}
		}

		texture->unlock();
	}
}



//! Creates an 1bit alpha channel of the texture based of an color key position.
void CNullDriver::makeColorKeyTexture(video::ITexture* texture,
					 core::position2d<s32> colorKeyPixelPos)
{
	if (!texture)
		return;

	if (texture->getColorFormat() != ECF_A1R5G5B5 &&
		texture->getColorFormat() != ECF_A8R8G8B8 )
	{
		os::Printer::log("Error: Unsupported texture color format for making color key channel.", ELL_ERROR);
		return;
	}

	if (texture->getColorFormat() == ECF_A1R5G5B5)
	{
		s16 *p = (s16*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making color key channel.", ELL_ERROR);
			return;
		}

		core::dimension2d<s32> dim = texture->getSize();
		s32 pitch = texture->getPitch() / 2;

		s16 ref = (0x1<<15) | (0x7fff & p[colorKeyPixelPos.Y*dim.Width + colorKeyPixelPos.X]);

		for (s32 y=0; y<dim.Height; ++y)
		{
			for (s32 x=0; x<pitch; ++x)
			{
				s16 c = (0x1<<15) | (0x7fff & p[y*pitch + x]);
				p[y*pitch + x] = (c == ref) ? 0 : c;
			}
		}

		texture->unlock();
	}
	else
	{
		s32 *p = (s32*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making color key channel.", ELL_ERROR);
			return;
		}

		core::dimension2d<s32> dim = texture->getSize();
		s32 pitch = texture->getPitch() / 4;

		s32 ref = (0xff<<24) | (0x00ffffff & p[colorKeyPixelPos.Y*dim.Width + colorKeyPixelPos.X]);

		for (s32 y=0; y<dim.Height; ++y)
		{
			for (s32 x=0; x<pitch; ++x)
			{
				s32 c = (0xff<<24) | (0x00ffffff & p[y*pitch + x]);
				p[y*pitch + x] = (c == ref) ? 0 : c;
			}
		}

		texture->unlock();
	}
}



//! Creates a normal map from a height map texture.
//! \param amplitude: Constant value by which the height information is multiplied.
void CNullDriver::makeNormalMapTexture(video::ITexture* texture, f32 amplitude)
{
	if (!texture)
		return;

	if (texture->getColorFormat() != ECF_A1R5G5B5 &&
		texture->getColorFormat() != ECF_A8R8G8B8 )
	{
		os::Printer::log("Error: Unsupported texture color format for making normal map.", ELL_ERROR);
		return;
	}

	core::dimension2d<s32> dim = texture->getSize();
	amplitude = amplitude / 255.0f;
	f32 vh = dim.Height / (f32)dim.Width;
	f32 hh = dim.Width / (f32)dim.Height;

	if (texture->getColorFormat() == ECF_A8R8G8B8)
	{
		// ECF_A8R8G8B8 version

		s32 *p = (s32*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making normal map.", ELL_ERROR);
			return;
		}

		// copy texture

		s32 pitch = texture->getPitch() / 4;

		s32* in = new s32[dim.Height * pitch];
		memcpy(in, p, dim.Height * pitch * 4);

		for (s32 x=0; x<pitch; ++x)
			for (s32 y=0; y<dim.Height; ++y)
			{
				// TODO: this could be optimized really a lot

				core::vector3df h1((x-1)*hh, nml32(x-1, y, pitch, dim.Height, in)*amplitude, y*vh);
				core::vector3df h2((x+1)*hh, nml32(x+1, y, pitch, dim.Height, in)*amplitude, y*vh);
				//core::vector3df v1(x*hh, nml32(x, y-1, pitch, dim.Height, in)*amplitude, (y-1)*vh);
				//core::vector3df v2(x*hh, nml32(x, y+1, pitch, dim.Height, in)*amplitude, (y+1)*vh);
				core::vector3df v1(x*hh, nml32(x, y+1, pitch, dim.Height, in)*amplitude, (y-1)*vh);
				core::vector3df v2(x*hh, nml32(x, y-1, pitch, dim.Height, in)*amplitude, (y+1)*vh);

				core::vector3df v = v1-v2;
				core::vector3df h = h1-h2;

				core::vector3df n = v.crossProduct(h);
				n.normalize();
				n *= 0.5f;
				n += core::vector3df(0.5f,0.5f,0.5f); // now between 0 and 1
				n *= 255.0f;

				s32 height = (s32)nml32(x, y, pitch, dim.Height, in);
				p[y*pitch + x] = video::SColor(
					height, // store height in alpha
					(s32)n.X, (s32)n.Z, (s32)n.Y).color;
			}

		delete [] in;
		texture->unlock();
	}
	else
	{
		// ECF_A1R5G5B5 version

		s16 *p = (s16*)texture->lock();

		if (!p)
		{
			os::Printer::log("Could not lock texture for making normal map.", ELL_ERROR);
			return;
		}

		s32 pitch = texture->getPitch() / 2;

		// copy texture

		s16* in = new s16[dim.Height * pitch];
		memcpy(in, p, dim.Height * pitch * 2);

		for (s32 x=0; x<pitch; ++x)
			for (s32 y=0; y<dim.Height; ++y)
			{
				// TODO: this could be optimized really a lot

				core::vector3df h1((x-1)*hh, nml16(x-1, y, pitch, dim.Height, in)*amplitude, y*vh);
				core::vector3df h2((x+1)*hh, nml16(x+1, y, pitch, dim.Height, in)*amplitude, y*vh);
				core::vector3df v1(x*hh, nml16(x, y-1, pitch, dim.Height, in)*amplitude, (y-1)*vh);
				core::vector3df v2(x*hh, nml16(x, y+1, pitch, dim.Height, in)*amplitude, (y+1)*vh);

				core::vector3df v = v1-v2;
				core::vector3df h = h1-h2;

				core::vector3df n = v.crossProduct(h);
				n.normalize();
				n *= 0.5f;
				n += core::vector3df(0.5f,0.5f,0.5f); // now between 0 and 1
				n *= 255.0f;

				p[y*pitch + x] = video::RGB16((s32)n.X, (s32)n.Z, (s32)n.Y);
			}

		delete [] in;
		texture->unlock();
	}

	texture->regenerateMipMapLevels();
}


//! Returns the maximum amount of primitives (mostly vertices) which
//! the device is able to render with one drawIndexedTriangleList
//! call.
s32 CNullDriver::getMaximalPrimitiveCount()
{
	return (1<<30)-1;
}


//! checks triangle count and print warning if wrong
bool CNullDriver::checkPrimitiveCount(s32 prmCount)
{
	s32 m = getMaximalPrimitiveCount();
	if ((prmCount-1) > m)
	{
		char tmp[1024];
		sprintf(tmp,"Could not draw triangles, too many primitives(%d), maxium is %d.", prmCount, m);
		os::Printer::log(tmp, ELL_ERROR);
		return false;
	}

	return true;
}

//! Enables or disables a texture creation flag.
void CNullDriver::setTextureCreationFlag(E_TEXTURE_CREATION_FLAG flag, bool enabled)
{
	if (enabled && ((flag == ETCF_ALWAYS_16_BIT) || (flag == ETCF_ALWAYS_32_BIT)
		|| (flag == ETCF_OPTIMIZED_FOR_QUALITY) || (flag == ETCF_OPTIMIZED_FOR_SPEED)))
	{
		// disable other formats
		setTextureCreationFlag(ETCF_ALWAYS_16_BIT, false);
		setTextureCreationFlag(ETCF_ALWAYS_32_BIT, false);
		setTextureCreationFlag(ETCF_OPTIMIZED_FOR_QUALITY, false);
		setTextureCreationFlag(ETCF_OPTIMIZED_FOR_SPEED, false);
	}

	// set flag
	TextureCreationFlags = (TextureCreationFlags & (~flag)) |
		((((u32)!enabled)-1) & flag);
}


//! Returns if a texture creation flag is enabled or disabled.
bool CNullDriver::getTextureCreationFlag(E_TEXTURE_CREATION_FLAG flag)
{
	return (TextureCreationFlags & flag)!=0;
}


//! Creates a software image from a file.
IImage* CNullDriver::createImageFromFile(const char* filename)
{
	IImage* image = 0;
	io::IReadFile* file = FileSystem->createAndOpenFile(filename);

	if (file)
	{
		image = createImageFromFile(file);
		file->drop();
	}
	else
		os::Printer::log("Could not open file of image", filename, ELL_ERROR);

	return image;
}


//! Creates a software image from a file.
IImage* CNullDriver::createImageFromFile(io::IReadFile* file)
{
	IImage* image = 0;

	u32 i;

	// try to load file based on file extension
	for (i=0; i<SurfaceLoader.size(); ++i)
	{
		if (SurfaceLoader[i]->isALoadableFileExtension(file->getFileName()))
		{
			// reset file position which might have changed due to previous loadImage calls
			file->seek(0);
			image = SurfaceLoader[i]->loadImage(file);
			if (image)
				return image;
		}
	}

	// try to load file based on what is in it
	for (i=0; i<SurfaceLoader.size(); ++i)
	{
		// dito
		file->seek(0);
		if (SurfaceLoader[i]->isALoadableFileFormat(file))
		{
			file->seek(0);
			image = SurfaceLoader[i]->loadImage(file);
			if (image)
				return image;
		}
	}

	return 0; // failed to load
}



//! Writes the provided image to disk file
bool CNullDriver::writeImageToFile(IImage* image, const char* filename)
{
	for (u32 i=0; i<SurfaceWriter.size(); ++i)
	{
		if (SurfaceWriter[i]->isAWriteableFileExtension(filename))
		{
			io::IWriteFile* file = FileSystem->createAndWriteFile(filename);
			if (file)
			{
				bool written = SurfaceWriter[i]->writeImage(file, image);
				file->drop();
				if (written)
					return true;
			}
		}
	}
	return false; // failed to write
}



//! Creates a software image from a byte array.
IImage* CNullDriver::createImageFromData(ECOLOR_FORMAT format,
										const core::dimension2d<s32>& size, void *data,
										bool ownForeignMemory)
{
	return new CImage(format, size, data, ownForeignMemory);
}


//! Sets the fog mode.
void CNullDriver::setFog(SColor color, bool linearFog, f32 start, f32 end, f32 density,
						bool pixelFog, bool rangeFog)
{
	FogColor = color;
	LinearFog = linearFog;
	FogStart = start;
	FogEnd = end;
	FogDensity = density;
	PixelFog = pixelFog;
	RangeFog = rangeFog;
}


//! Draws a mesh buffer
void CNullDriver::drawMeshBuffer(scene::IMeshBuffer* mb)
{
	if (!mb)
		return;

	drawVertexPrimitiveList(mb->getVertices(), mb->getVertexCount(), mb->getIndices(), mb->getIndexCount()/3, mb->getVertexType(), scene::EPT_TRIANGLES);
}



//! Only used by the internal engine. Used to notify the driver that
//! the window was resized.
void CNullDriver::OnResize(const core::dimension2d<s32>& size)
{
	if (ViewPort.getWidth() == ScreenSize.Width &&
		ViewPort.getHeight() == ScreenSize.Height)
		ViewPort = core::rect<s32>(core::position2d<s32>(0,0), size);

	ScreenSize = size;
}

// adds a material renderer and drops it afterwards. To be used for internal creation
s32 CNullDriver::addAndDropMaterialRenderer(IMaterialRenderer* m)
{
	s32 i = addMaterialRenderer(m);

	if (m)
		m->drop();

	return i;
}


//! Adds a new material renderer to the video device.
s32 CNullDriver::addMaterialRenderer(IMaterialRenderer* renderer, const char* name)
{
	if (!renderer)
		return -1;

	SMaterialRenderer r;
	r.Renderer = renderer;
	r.Name = name;

	if (name == 0 && (MaterialRenderers.size() < sizeof(sBuiltInMaterialTypeNames) / sizeof(char*)))
	{
		// set name of built in renderer so that we don't have to implement name
		// setting in all 5 available renderers.
		r.Name = sBuiltInMaterialTypeNames[MaterialRenderers.size()];
	}

	MaterialRenderers.push_back(r);
	renderer->grab();

	return MaterialRenderers.size()-1;
}


//! Sets the name of a material renderer.
void CNullDriver::setMaterialRendererName(s32 idx, const char* name)
{
	if (idx < s32(sizeof(sBuiltInMaterialTypeNames) / sizeof(char*)) ||
		idx >= (s32)MaterialRenderers.size())
		return;

	MaterialRenderers[idx].Name = name;
}


//! Creates material attributes list from a material, usable for serialization and more.
io::IAttributes* CNullDriver::createAttributesFromMaterial(video::SMaterial& material)
{
	io::CAttributes* attr = new io::CAttributes(this);

	const char** materialNames = new const char*[MaterialRenderers.size()+1];
	for (int i=0; i<(int)MaterialRenderers.size(); ++i)
		materialNames[i] = MaterialRenderers[i].Name.c_str();

	materialNames[MaterialRenderers.size()] = 0;

	attr->addEnum("Type", material.MaterialType, materialNames);

	delete [] materialNames;

	attr->addColorf("Ambient", material.AmbientColor);
	attr->addColorf("Diffuse", material.DiffuseColor);
	attr->addColorf("Emissive", material.EmissiveColor);
	attr->addColorf("Specular", material.SpecularColor);

	attr->addFloat("Shininess", material.Shininess);
	attr->addFloat("Param1", material.MaterialTypeParam);
	attr->addFloat("Param2", material.MaterialTypeParam2);

	attr->addTexture("Texture1", material.Texture1);
	attr->addTexture("Texture2", material.Texture2);
	attr->addTexture("Texture3", material.Texture3);
	attr->addTexture("Texture4", material.Texture4);

	attr->addBool("Wireframe", material.Wireframe);
	attr->addBool("GouraudShading", material.GouraudShading);
	attr->addBool("Lighting", material.Lighting);
	attr->addBool("ZBuffer", material.ZBuffer);
	attr->addBool("ZWriteEnable", material.ZWriteEnable);
	attr->addBool("BackfaceCulling", material.BackfaceCulling);
	attr->addBool("BilinearFilter", material.BilinearFilter);
	attr->addBool("TrilinearFilter", material.TrilinearFilter);
	attr->addBool("AnisotropicFilter", material.AnisotropicFilter);
	attr->addBool("FogEnable", material.FogEnable);
	attr->addBool("NormalizeNormals", material.NormalizeNormals);

	return attr;
}


//! Fills an SMaterial structure from attributes.
void CNullDriver::fillMaterialStructureFromAttributes(video::SMaterial& outMaterial, io::IAttributes* attr)
{
	outMaterial.MaterialType = video::EMT_SOLID;

	core::stringc name = attr->getAttributeAsString("Type");

	for (int i=0; i<(int)MaterialRenderers.size(); ++i)
		if ( name == MaterialRenderers[i].Name )
		{
			outMaterial.MaterialType = (video::E_MATERIAL_TYPE)i;
			break;
		}

	outMaterial.AmbientColor = attr->getAttributeAsColor("Ambient");
	outMaterial.DiffuseColor = attr->getAttributeAsColor("Diffuse");
	outMaterial.EmissiveColor = attr->getAttributeAsColor("Emissive");
	outMaterial.SpecularColor = attr->getAttributeAsColor("Specular");

	outMaterial.Shininess = attr->getAttributeAsFloat("Shininess");
	outMaterial.MaterialTypeParam = attr->getAttributeAsFloat("Param1");
	outMaterial.MaterialTypeParam2 = attr->getAttributeAsFloat("Param2");

	outMaterial.Texture1 = attr->getAttributeAsTexture("Texture1");
	outMaterial.Texture2 = attr->getAttributeAsTexture("Texture2");
	outMaterial.Texture3 = attr->getAttributeAsTexture("Texture3");
	outMaterial.Texture4 = attr->getAttributeAsTexture("Texture4");

	outMaterial.Wireframe = attr->getAttributeAsBool("Wireframe");
	outMaterial.GouraudShading = attr->getAttributeAsBool("GouraudShading");
	outMaterial.Lighting = attr->getAttributeAsBool("Lighting");
	outMaterial.ZBuffer = attr->getAttributeAsBool("ZBuffer");
	outMaterial.ZWriteEnable = attr->getAttributeAsBool("ZWriteEnable");
	outMaterial.BackfaceCulling = attr->getAttributeAsBool("BackfaceCulling");
	outMaterial.BilinearFilter = attr->getAttributeAsBool("BilinearFilter");
	outMaterial.TrilinearFilter = attr->getAttributeAsBool("TrilinearFilter");
	outMaterial.AnisotropicFilter = attr->getAttributeAsBool("AnisotropicFilter");
	outMaterial.FogEnable = attr->getAttributeAsBool("FogEnable");
	outMaterial.NormalizeNormals = attr->getAttributeAsBool("NormalizeNormals");
}


//! Returns driver and operating system specific data about the IVideoDriver.
SExposedVideoData CNullDriver::getExposedVideoData()
{
	return ExposedData;
}


//! Returns type of video driver
E_DRIVER_TYPE CNullDriver::getDriverType()
{
	return EDT_NULL;
}

//! deletes all material renderers
void CNullDriver::deleteMaterialRenders()
{
	// delete material renderers
	for (int i=0; i<(int)MaterialRenderers.size(); ++i)
		if (MaterialRenderers[i].Renderer)
			MaterialRenderers[i].Renderer->drop();

	MaterialRenderers.clear();
}

//! Returns pointer to material renderer or null
IMaterialRenderer* CNullDriver::getMaterialRenderer(s32 idx)
{
	if (idx < 0 || idx >= (s32)MaterialRenderers.size())
		return 0;

	return MaterialRenderers[idx].Renderer;
}



//! Returns amount of currently available material renderers.
s32 CNullDriver::getMaterialRendererCount()
{
	return MaterialRenderers.size();
}


//! Returns name of the material renderer
const char* CNullDriver::getMaterialRendererName(s32 idx)
{
	if (idx < 0 || idx >= (s32)MaterialRenderers.size())
		return 0;

	return MaterialRenderers[idx].Name.c_str();
}


//! Returns pointer to the IGPUProgrammingServices interface.
IGPUProgrammingServices* CNullDriver::getGPUProgrammingServices()
{
	return 0;
}

//! Adds a new material renderer to the VideoDriver, based on a high level shading
//! language. Currently only HLSL in D3D9 is supported.
s32 CNullDriver::addHighLevelShaderMaterial(
	const c8* vertexShaderProgram,
	const c8* vertexShaderEntryPointName,
	E_VERTEX_SHADER_TYPE vsCompileTarget,
	const c8* pixelShaderProgram,
	const c8* pixelShaderEntryPointName,
	E_PIXEL_SHADER_TYPE psCompileTarget,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	os::Printer::log("High level shader materials not available (yet) in this driver, sorry");
	return -1;
}

//! Like IGPUProgrammingServices::addShaderMaterial() (look there for a detailed description),
//! but tries to load the programs from files.
s32 CNullDriver::addHighLevelShaderMaterialFromFiles(
	const c8* vertexShaderProgram,
	const c8* vertexShaderEntryPointName,
	E_VERTEX_SHADER_TYPE vsCompileTarget,
	const c8* pixelShaderProgram,
	const c8* pixelShaderEntryPointName,
	E_PIXEL_SHADER_TYPE psCompileTarget,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	io::IReadFile* vsfile = 0;
	io::IReadFile* psfile = 0;

	if (vertexShaderProgram)
	{
		vsfile = FileSystem->createAndOpenFile(vertexShaderProgram);
		if (!vsfile)
		{
			os::Printer::log("Could not open vertex shader program file",
				vertexShaderProgram, ELL_WARNING);
			return -1;
		}
	}

	if (pixelShaderProgram)
	{
		psfile = FileSystem->createAndOpenFile(pixelShaderProgram);
		if (!psfile)
		{
			os::Printer::log("Could not open pixel shader program file",
				pixelShaderProgram, ELL_WARNING);
			if (vsfile)
				vsfile->drop();
			return -1;
		}
	}

	s32 result = addHighLevelShaderMaterialFromFiles(
		vsfile, vertexShaderEntryPointName, vsCompileTarget,
		psfile, pixelShaderEntryPointName, psCompileTarget,
		callback, baseMaterial, userData);

	if (psfile)
		psfile->drop();

	if (vsfile)
		vsfile->drop();

	return result;
}

//! Like IGPUProgrammingServices::addShaderMaterial() (look there for a detailed description),
//! but tries to load the programs from files.
s32 CNullDriver::addHighLevelShaderMaterialFromFiles(
	io::IReadFile* vertexShaderProgram,
	const c8* vertexShaderEntryPointName,
	E_VERTEX_SHADER_TYPE vsCompileTarget,
	io::IReadFile* pixelShaderProgram,
	const c8* pixelShaderEntryPointName,
	E_PIXEL_SHADER_TYPE psCompileTarget,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	c8* vs = 0;
	c8* ps = 0;

	if (vertexShaderProgram)
	{
		s32 size = vertexShaderProgram->getSize();
		if (size)
		{
			vs = new c8[size+1];
			vertexShaderProgram->read(vs, size);
			vs[size] = 0;
		}
	}

	if (pixelShaderProgram)
	{
		s32 size = pixelShaderProgram->getSize();
		if (size)
		{
			ps = new c8[size+1];
			pixelShaderProgram->read(ps, size);
			ps[size] = 0;
		}
	}

	s32 result = this->addHighLevelShaderMaterial(
		vs, vertexShaderEntryPointName, vsCompileTarget,
		ps, pixelShaderEntryPointName, psCompileTarget,
		callback, baseMaterial, userData);

	delete [] vs;
	delete [] ps;

	return result;
}

//! Adds a new material renderer to the VideoDriver, using pixel and/or
//! vertex shaders to render geometry.
s32 CNullDriver::addShaderMaterial(const c8* vertexShaderProgram,
	const c8* pixelShaderProgram,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	os::Printer::log("Shader materials not implemented yet in this driver, sorry.");
	return -1;
}

//! Like IGPUProgrammingServices::addShaderMaterial(), but tries to load the
//! programs from files.
s32 CNullDriver::addShaderMaterialFromFiles(io::IReadFile* vertexShaderProgram,
	io::IReadFile* pixelShaderProgram,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	c8* vs = 0;
	c8* ps = 0;

	if (vertexShaderProgram)
	{
		s32 size = vertexShaderProgram->getSize();
		if (size)
		{
			vs = new c8[size+1];
			vertexShaderProgram->read(vs, size);
			vs[size] = 0;
		}
	}

	if (pixelShaderProgram)
	{
		s32 size = pixelShaderProgram->getSize();
		if (size)
		{
			ps = new c8[size+1];
			pixelShaderProgram->read(ps, size);
			ps[size] = 0;
		}
	}

	s32 result = addShaderMaterial(vs, ps, callback, baseMaterial, userData);

	delete [] vs;
	delete [] ps;

	return result;
}



//! Like IGPUProgrammingServices::addShaderMaterial(), but tries to load the
//! programs from files.
s32 CNullDriver::addShaderMaterialFromFiles(const c8* vertexShaderProgramFileName,
	const c8* pixelShaderProgramFileName,
	IShaderConstantSetCallBack* callback,
	E_MATERIAL_TYPE baseMaterial,
	s32 userData)
{
	io::IReadFile* vsfile = 0;
	io::IReadFile* psfile = 0;

	if (vertexShaderProgramFileName)
	{
		vsfile = FileSystem->createAndOpenFile(vertexShaderProgramFileName);
		if (!vsfile)
		{
			os::Printer::log("Could not open vertex shader program file",
				vertexShaderProgramFileName, ELL_WARNING);
			return -1;
		}
	}

	if (pixelShaderProgramFileName)
	{
		psfile = FileSystem->createAndOpenFile(pixelShaderProgramFileName);
		if (!psfile)
		{
			os::Printer::log("Could not open pixel shader program file",
				pixelShaderProgramFileName, ELL_WARNING);
			if (vsfile)
				vsfile->drop();
			return -1;
		}
	}

	s32 result = addShaderMaterialFromFiles(vsfile, psfile, callback,
		baseMaterial, userData);

	if (psfile)
		psfile->drop();

	if (vsfile)
		vsfile->drop();

	return result;
}

//! Creates a render target texture.
ITexture* CNullDriver::createRenderTargetTexture(const core::dimension2d<s32>& size)
{
	return 0;
}

//! Clears the ZBuffer.
void CNullDriver::clearZBuffer()
{
}

//! Returns an image created from the last rendered frame.
IImage* CNullDriver::createScreenShot()
{
	return 0;
}

// prints renderer version
void CNullDriver::printVersion()
{
	core::stringw namePrint = L"Using renderer: ";
	namePrint += getName();
	os::Printer::log(namePrint.c_str(), ELL_INFORMATION);
}


//! creates a video driver
IVideoDriver* createNullDriver(io::IFileSystem* io, const core::dimension2d<s32>& screenSize)
{
	return new CNullDriver(io, screenSize);
}


//! BP2K6: Draws a part of the texture into the rotated rectangle.
void CNullDriver::draw2DImageRotation(video::ITexture* texture, const core::rect<s32>& destRect,
	const core::rect<s32>& sourceRect,
	const f32 rotation, SColor color, bool useAlphaChannelOfTexture)
{
    //Do nothing (not implemented).
}


//! BP2K6: Draws a part of the texture into the rotated rectangle.
void CNullDriver::draw2DImageRotation(video::ITexture* texture, const core::rect<s32>& destRect,
	const core::rect<s32>& sourceRect, const core::position2d<s32>& rotationPos,
	const f32 rotation, SColor color, bool useAlphaChannelOfTexture)
{
    //Do nothing (not implemented).
}


} // end namespace
} // end namespace

