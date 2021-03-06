// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSoftwareDriver.h"
#include "CSoftwareTexture.h"
#include "os.h"
#include "S3DVertex.h"

namespace irr
{
namespace video
{


//! constructor
CSoftwareDriver::CSoftwareDriver(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter)
: CNullDriver(io, windowSize), CurrentTriangleRenderer(0), Texture(0),
	ZBuffer(0), RenderTargetTexture(0), RenderTargetSurface(0)
{
	#ifdef _DEBUG
	setDebugName("CSoftwareDriver");
	#endif

	// create backbuffer

	BackBuffer = new CImage(ECF_A1R5G5B5, windowSize);
	BackBuffer->fill(SColor(0));

	// get presenter

	Presenter = presenter;

	// create z buffer

	ZBuffer = irr::video::createZBuffer(BackBuffer->getDimension());

	// create triangle renderers

	TriangleRenderers[ETR_FLAT] = createTriangleRendererFlat(ZBuffer);
	TriangleRenderers[ETR_FLAT_WIRE] = createTriangleRendererFlatWire(ZBuffer);
	TriangleRenderers[ETR_GOURAUD] = createTriangleRendererGouraud(ZBuffer);
	TriangleRenderers[ETR_GOURAUD_WIRE] = createTriangleRendererGouraudWire(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_FLAT] = createTriangleRendererTextureFlat(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_FLAT_WIRE] = createTriangleRendererTextureFlatWire(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD] = createTriangleRendererTextureGouraud(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_WIRE] = createTriangleRendererTextureGouraudWire(ZBuffer);
	TriangleRenderers[ETR_TEXTURE_GOURAUD_NOZ] = createTriangleRendererTextureGouraudNoZ();
	TriangleRenderers[ETR_TEXTURE_GOURAUD_ADD] = createTriangleRendererTextureGouraudAdd(ZBuffer);

	// select render target

	setRenderTarget(BackBuffer);

	// select the right renderer

	selectRightTriangleRenderer();
}



//! destructor
CSoftwareDriver::~CSoftwareDriver()
{
	// delete Backbuffer
	BackBuffer->drop();

	// delete triangle renderers

	for (s32 i=0; i<ETR_COUNT; ++i)
		if (TriangleRenderers[i])
			TriangleRenderers[i]->drop();

	// delete zbuffer

	if (ZBuffer)
		ZBuffer->drop();

	// delete current texture

	if (Texture)
		Texture->drop();

	if (RenderTargetTexture)
		RenderTargetTexture->drop();

	if (RenderTargetSurface)
		RenderTargetSurface->drop();
}



//! switches to a triangle renderer
void CSoftwareDriver::switchToTriangleRenderer(ETriangleRenderer renderer)
{
	video::IImage* s = 0;
	if (Texture)
		s = ((CSoftwareTexture*)Texture)->getTexture();

	CurrentTriangleRenderer = TriangleRenderers[renderer];
	CurrentTriangleRenderer->setBackfaceCulling(Material.BackfaceCulling == true);
	CurrentTriangleRenderer->setTexture(s);
	CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
}


//! void selects the right triangle renderer based on the render states.
void CSoftwareDriver::selectRightTriangleRenderer()
{

	ETriangleRenderer renderer = ETR_FLAT;

	if (Texture)
	{
		if (!Material.GouraudShading)
			renderer = (!Material.Wireframe) ? ETR_TEXTURE_FLAT : ETR_TEXTURE_FLAT_WIRE;
		else
		{
			if (Material.Wireframe)
				renderer = ETR_TEXTURE_GOURAUD_WIRE;
			else
			{
				if (Material.MaterialType == EMT_TRANSPARENT_ADD_COLOR ||
						Material.MaterialType == EMT_TRANSPARENT_ALPHA_CHANNEL ||
						Material.MaterialType == EMT_TRANSPARENT_VERTEX_ALPHA)
				{
					// simply draw all transparent stuff with the same renderer. at
					// least it is transparent then.
					renderer = ETR_TEXTURE_GOURAUD_ADD;
				}
				else
				if (!Material.ZBuffer && !Material.ZWriteEnable)
					renderer = ETR_TEXTURE_GOURAUD_NOZ;
				else
				{
					renderer = ETR_TEXTURE_GOURAUD;
				}
			}
		}
	}
	else
	{
		if (!Material.GouraudShading)
			renderer = (!Material.Wireframe) ? ETR_FLAT : ETR_FLAT_WIRE;
		else
			renderer = (!Material.Wireframe) ? ETR_GOURAUD : ETR_GOURAUD_WIRE;
	}

	switchToTriangleRenderer(renderer);
}




//! presents the rendered scene on the screen, returns false if failed
bool CSoftwareDriver::endScene( s32 windowId, core::rect<s32>* sourceRect )
{
	CNullDriver::endScene();

	Presenter->present(BackBuffer, windowId, sourceRect );
	return true;
}



//! queries the features of the driver, returns true if feature is available
bool CSoftwareDriver::queryFeature(E_VIDEO_DRIVER_FEATURE feature)
{
	switch (feature)
	{
	case EVDF_RENDER_TO_TARGET:
		return true;
	default:
		return false;
	};
}



//! sets transformation
void CSoftwareDriver::setTransform(E_TRANSFORMATION_STATE state, const core::matrix4& mat)
{
	TransformationMatrix[state] = mat;
}




//! sets the current Texture
bool CSoftwareDriver::setTexture(video::ITexture* texture)
{
	if (texture && texture->getDriverType() != EDT_SOFTWARE)
	{
		os::Printer::log("Fatal Error: Tried to set a texture not owned by this driver.", ELL_ERROR);
		return false;
	}

	if (Texture)
		Texture->drop();

	Texture = texture;

	if (Texture)
		Texture->grab();

	selectRightTriangleRenderer();
	return true;
}



//! sets a material
void CSoftwareDriver::setMaterial(const SMaterial& material)
{
	Material = material;
	setTexture(Material.Texture1);
}


//! clears the zbuffer
bool CSoftwareDriver::beginScene(bool backBuffer, bool zBuffer, SColor color)
{
	CNullDriver::beginScene(backBuffer, zBuffer, color);

	if (backBuffer)
		BackBuffer->fill( color );

	if (ZBuffer && zBuffer)
		ZBuffer->clear();

	return true;
}


//! sets a render target
bool CSoftwareDriver::setRenderTarget(video::ITexture* texture, bool clearBackBuffer,
								bool clearZBuffer, SColor color)
{
	if (texture && texture->getDriverType() != EDT_SOFTWARE)
	{
		os::Printer::log("Fatal Error: Tried to set a texture not owned by this driver.", ELL_ERROR);
		return false;
	}

	if (RenderTargetTexture)
		RenderTargetTexture->drop();

	RenderTargetTexture = texture;

	if (RenderTargetTexture)
	{
		RenderTargetTexture->grab();
		setRenderTarget(((CSoftwareTexture*)RenderTargetTexture)->getTexture());
	}
	else
	{
		setRenderTarget(BackBuffer);
	}

	if (RenderTargetSurface && (clearBackBuffer || clearZBuffer))
	{
		if (clearZBuffer)
			ZBuffer->clear();

		if (clearBackBuffer)
			((video::CImage*)RenderTargetSurface)->fill( color );
	}

	return true;
}


//! sets a render target
void CSoftwareDriver::setRenderTarget(video::CImage* image)
{
	if (RenderTargetSurface)
		RenderTargetSurface->drop();

	RenderTargetSurface = image;
	RenderTargetSize.Width = 0;
	RenderTargetSize.Height = 0;
	Render2DTranslation.X = 0;
	Render2DTranslation.Y = 0;

	if (RenderTargetSurface)
	{
		RenderTargetSurface->grab();
		RenderTargetSize = RenderTargetSurface->getDimension();
	}

	setViewPort(core::rect<s32>(0,0,RenderTargetSize.Width,RenderTargetSize.Height));

	if (ZBuffer)
		ZBuffer->setSize(RenderTargetSize);
}



//! sets a viewport
void CSoftwareDriver::setViewPort(const core::rect<s32>& area)
{
	ViewPort = area;

	//TODO: the clipping is not correct, because the projection is affected.
	// to correct this, ViewPortSize and Render2DTranslation will have to be corrected.
	core::rect<s32> rendert(0,0,RenderTargetSize.Width,RenderTargetSize.Height);
	ViewPort.clipAgainst(rendert);

	ViewPortSize = ViewPort.getSize();
	Render2DTranslation.X = (ViewPortSize.Width / 2) + ViewPort.UpperLeftCorner.X;
	Render2DTranslation.Y = ViewPort.UpperLeftCorner.Y + ViewPortSize.Height - (ViewPortSize.Height / 2);// + ViewPort.UpperLeftCorner.Y;

	if (CurrentTriangleRenderer)
		CurrentTriangleRenderer->setRenderTarget(RenderTargetSurface, ViewPort);
}


//! draws a vertex primitive list
void CSoftwareDriver::drawVertexPrimitiveList(const void* vertices, s32 vertexCount, const u16* indexList, s32 primitiveCount, E_VERTEX_TYPE vType, scene::E_PRIMITIVE_TYPE pType)
{
	const u16* indexPointer=0;
	core::array<u16> newBuffer;
	switch (pType)
	{
		case scene::EPT_TRIANGLE_FAN:
			{
				// TODO: don't convert fan to list
				newBuffer.reallocate(primitiveCount*3);
				for( s32 t=0; t<primitiveCount; ++t )
				{
					newBuffer.push_back(indexList[0]);
					newBuffer.push_back(indexList[t+1]);
					newBuffer.push_back(indexList[t+2]);
				}

				indexPointer = newBuffer.pointer();
			}
			break;
		case scene::EPT_TRIANGLES:
			indexPointer=indexList;
	}
	switch (vType)
	{
		case EVT_STANDARD:
			drawClippedIndexedTriangleListT((S3DVertex*)vertices, vertexCount, indexPointer, primitiveCount);
			break;
		case EVT_2TCOORDS:
			drawClippedIndexedTriangleListT((S3DVertex2TCoords*)vertices, vertexCount, indexPointer, primitiveCount);
			break;
		case EVT_TANGENTS:
			drawClippedIndexedTriangleListT((S3DVertexTangents*)vertices, vertexCount, indexPointer, primitiveCount);
			break;
	}
}



template<class VERTEXTYPE>
void CSoftwareDriver::drawClippedIndexedTriangleListT(const VERTEXTYPE* vertices,
	s32 vertexCount, const u16* indexList, s32 triangleCount)
{
	if (!RenderTargetSurface || !ZBuffer || !triangleCount)
		return;

	if (!checkPrimitiveCount(triangleCount))
		return;

	// arrays for storing clipped vertices
	core::array<VERTEXTYPE> clippedVertices;
	core::array<u16> clippedIndices;

	// calculate inverse world transformation
	core::matrix4 worldinv(TransformationMatrix[ETS_WORLD]);
	worldinv.makeInverse();

	// calculate view frustrum planes
	scene::SViewFrustrum frustrum(TransformationMatrix[ETS_PROJECTION] * TransformationMatrix[ETS_VIEW]);

	// copy and transform clipping planes ignoring far plane
	core::plane3df planes[5]; // ordered by near, left, right, bottom, top
	for (int p=0; p<5; ++p)
		worldinv.transformPlane(frustrum.planes[p+1], planes[p]);

	core::EIntersectionRelation3D inout[3]; // is point in front or back of plane?

	// temporary buffer for vertices to be clipped by all planes
	core::array<VERTEXTYPE> tClpBuf;
	int t;

	int i;
	for (i=0; i<triangleCount; ++i) // for all input triangles
	{
		// add next triangle to tempClipBuffer
		for (t=0; t<3; ++t)
			tClpBuf.push_back(vertices[indexList[(i*3)+t]]);

		for (int p=0; p<5; ++p) // for all clip planes
		for (int v=0; v<(int)tClpBuf.size(); v+=3) // for all vertices in temp clip buffer
		{
			int inside = 0;
			int outside = 0;

			// test intersection relation of the current vertices
			for (t=0; t<3; ++t)
			{
				inout[t] = planes[p].classifyPointRelation(tClpBuf[v+t].Pos);
				if (inout[t] == core::ISREL3D_FRONT)
					++inside;
				else
				if (inout[t] == core::ISREL3D_BACK)
					++outside;
			}

			if (!outside)
			{
				// add all vertices to new buffer, this triangle needs no clipping.
				// so simply don't change this part of the temporary triangle buffer
				continue;
			}

			if (!inside)
			{
				// all vertices are outside, don't add this triangle, so erase this
				// triangle from the tClpBuf
				tClpBuf.erase(v,3);
				v -= 3;
				continue;
			}

			// this vertex has to be clipped by this clipping plane.

			// The following lines represent my try to implement some real clipping.
			// There is a bug somewhere, and after some time I've given up.
			// So now it is commented out, resulting that triangles which would need clipping
			// are simply taken out (in the next two lines).
#ifndef __SOFTWARE_CLIPPING_PROBLEM__
			tClpBuf.erase(v,3);
			v -= 3;
#endif

			/*
			// my idea is the following:
			// current vertex to next vertex relation:
			// out - out : add nothing
			// out -  in : add middle point
			// in -  out : add first and middle point
			// in -   in : add both


			// now based on the number of intersections, create new vertices
			// into tClpBuf (at the front for not letting them be clipped again)

			int added = 0;
			int prev = v+2;
			for (int index=v; index<v+3; ++index)
			{
				if (inout[prev] == core::ISREL3D_BACK)
				{
					if (inout[index] != core::ISREL3D_BACK)
					{
						VERTEXTYPE& vt1 = tClpBuf[prev];
						VERTEXTYPE& vt2 = tClpBuf[index];

						f32 fact = planes[p].getKnownIntersectionWithLine(vt1.Pos, vt2.Pos);
						VERTEXTYPE nvt;
						nvt.Pos = vt1.Pos.getInterpolated(vt2.Pos, fact);
						nvt.Color = vt1.Color.getInterpolated(vt2.Color, fact);
						nvt.TCoords = vt1.TCoords.getInterpolated(vt2.TCoords, fact);

						tClpBuf.push_front(nvt); ++index; ++prev; ++v;
						++added;
					}
				}
				else
				{
					if (inout[index] != core::ISREL3D_BACK)
					{
						VERTEXTYPE vt1 = tClpBuf[index];
						VERTEXTYPE vt2 = tClpBuf[prev];
						tClpBuf.push_front(vt1); ++index; ++prev; ++v;
						tClpBuf.push_front(vt2); ++index; ++prev; ++v;
						added+= 2;
					}
					else
					{
						// same as above, but other way round.
						VERTEXTYPE vt1 = tClpBuf[index];
						VERTEXTYPE vt2 = tClpBuf[prev];

						f32 fact = planes[p].getKnownIntersectionWithLine(vt1.Pos, vt2.Pos);
						VERTEXTYPE nvt;
						nvt.Pos = vt1.Pos.getInterpolated(vt2.Pos, fact);
						nvt.Color = vt1.Color.getInterpolated(vt2.Color, fact);
						nvt.TCoords = vt1.TCoords.getInterpolated(vt2.TCoords, fact);

						tClpBuf.push_front(vt2); ++index; ++prev; ++v;
						tClpBuf.push_front(nvt); ++index; ++prev; ++v;
						added += 2;
					}
				}

				prev = index;
			}

			// erase original vertices
			tClpBuf.erase(v,3);
			v -= 3;
			*/


		} // end for all clip planes

		// now add all remaining triangles in tempClipBuffer to clippedIndices
		// and clippedVertices array.
		if (clippedIndices.size() + tClpBuf.size() < 65535)
		for (t=0; t<(int)tClpBuf.size(); ++t)
		{
			clippedIndices.push_back(clippedVertices.size());
			clippedVertices.push_back(tClpBuf[t]);
		}
		tClpBuf.clear();

	} // end for all input triangles


	// draw newly created triangles.

	// -----------------------------------------------------------
	// here all triangles are being drawn. I put this in a separate
	// method, but the visual studio 6 compiler has great problems
	// with templates and didn't accept two template methods in this
	// class.

	// draw triangles

	CNullDriver::drawVertexPrimitiveList(clippedVertices.pointer(), clippedVertices.size(),
		clippedIndices.pointer(), clippedIndices.size()/3, EVT_STANDARD, scene::EPT_TRIANGLES);

	if (TransformedPoints.size() < clippedVertices.size())
		TransformedPoints.set_used(clippedVertices.size());

	if (TransformedPoints.empty())
		return;

	const VERTEXTYPE* currentVertex = clippedVertices.pointer();
	S2DVertex* tp = &TransformedPoints[0];

	core::dimension2d<s32> textureSize(0,0);
	f32 zDiv;

	if (Texture)
		textureSize = ((CSoftwareTexture*)Texture)->getTexture()->getDimension();

	f32 transformedPos[4]; // transform all points in the list

	core::matrix4 matrix(TransformationMatrix[ETS_PROJECTION]);
	matrix *= TransformationMatrix[ETS_VIEW];
	matrix *= TransformationMatrix[ETS_WORLD];

	s32 ViewTransformWidth = (ViewPortSize.Width>>1);
	s32 ViewTransformHeight = (ViewPortSize.Height>>1);

	for (i=0; i<(int)clippedVertices.size(); ++i)
	{
		transformedPos[0] = currentVertex->Pos.X;
		transformedPos[1] = currentVertex->Pos.Y;
		transformedPos[2] = currentVertex->Pos.Z;
		transformedPos[3] = 1.0f;

		matrix.multiplyWith1x4Matrix(transformedPos);
		zDiv = transformedPos[3] == 0.0f ? 1.0f : (1.0f / transformedPos[3]);

		tp->Pos.X = (s32)(ViewTransformWidth * (transformedPos[0] * zDiv) + (Render2DTranslation.X));
		tp->Pos.Y = (Render2DTranslation.Y - (s32)(ViewTransformHeight * (transformedPos[1] * zDiv)));
		tp->Color = currentVertex->Color.toA1R5G5B5();
		tp->ZValue = (TZBufferType)(32767.0f * zDiv);

		tp->TCoords.X = (s32)(currentVertex->TCoords.X * textureSize.Width);
		tp->TCoords.X <<= 8;
		tp->TCoords.Y = (s32)(currentVertex->TCoords.Y * textureSize.Height);
		tp->TCoords.Y <<= 8;

		++currentVertex;
		++tp;
	}

	// draw all transformed points from the index list
	CurrentTriangleRenderer->drawIndexedTriangleList(&TransformedPoints[0],
		clippedVertices.size(), clippedIndices.pointer(), clippedIndices.size()/3);
}



//! Draws a 3d line.
void CSoftwareDriver::draw3DLine(const core::vector3df& start,
				const core::vector3df& end, SColor color)
{
	core::vector3df vect = start.crossProduct(end);
	vect.normalize();
	vect *= Material.Thickness;

	S3DVertex vtx[4];

	vtx[0].Color = color;
	vtx[1].Color = color;
	vtx[2].Color = color;
	vtx[3].Color = color;

	vtx[0].Pos = start;
	vtx[1].Pos = end;

	vtx[2].Pos = start + vect;
	vtx[3].Pos = end + vect;

	u16 idx[12] = {0,1,2, 0,2,1, 0,1,3, 0,3,1};

	drawIndexedTriangleList(vtx, 4, idx, 4);
}



//! clips a triangle against the viewing frustrum
void CSoftwareDriver::clipTriangle(f32* transformedPos)
{
}



//! creates the clipping planes from the matrix
void CSoftwareDriver::createPlanes(const core::matrix4& mat)
{
	Frustrum = scene::SViewFrustrum(mat);
}



//! draws an 2d image, using a color (if color is other then Color(255,255,255,255)) and the alpha channel of the texture if wanted.
void CSoftwareDriver::draw2DImage(video::ITexture* texture, const core::position2d<s32>& destPos,
					const core::rect<s32>& sourceRect,
					const core::rect<s32>* clipRect, SColor color,
					bool useAlphaChannelOfTexture)
{
	if (texture)
	{
		if (texture->getDriverType() != EDT_SOFTWARE)
		{
			os::Printer::log("Fatal Error: Tried to copy from a surface not owned by this driver.", ELL_ERROR);
			return;
		}

		if (useAlphaChannelOfTexture)
			((CSoftwareTexture*)texture)->getImage()->copyToWithAlpha(
				((CImage*)RenderTargetSurface), destPos, sourceRect, color, clipRect);
		else
			((CSoftwareTexture*)texture)->getImage()->copyTo(
				((CImage*)RenderTargetSurface), destPos, sourceRect, clipRect);
	}
}



//! Draws a 2d line.
void CSoftwareDriver::draw2DLine(const core::position2d<s32>& start,
								const core::position2d<s32>& end,
								SColor color)
{
	((CImage*)RenderTargetSurface)->drawLine(start, end, color );
}



//! draw a 2d rectangle
void CSoftwareDriver::draw2DRectangle(SColor color, const core::rect<s32>& pos,
					const core::rect<s32>* clip)
{
	if (clip)
	{
		core::rect<s32> p(pos);

		p.clipAgainst(*clip);

		if(!p.isValid())
			return;

		((CImage*)RenderTargetSurface)->drawRectangle(p, color);
	}
	else
	{
		if(!pos.isValid())
			return;

		((CImage*)RenderTargetSurface)->drawRectangle(pos, color);
	}
}


//!Draws an 2d rectangle with a gradient.
void CSoftwareDriver::draw2DRectangle(const core::rect<s32>& pos,
	SColor colorLeftUp, SColor colorRightUp, SColor colorLeftDown, SColor colorRightDown,
	const core::rect<s32>* clip)
{
	// TODO: implement
	draw2DRectangle(colorLeftUp, pos, clip);
}


//! \return Returns the name of the video driver. Example: In case of the Direct3D8
//! driver, it would return "Direct3D8.1".
const wchar_t* CSoftwareDriver::getName()
{
	return L"Irrlicht Software Device 1.0";
}

//! Returns type of video driver
E_DRIVER_TYPE CSoftwareDriver::getDriverType()
{
	return EDT_SOFTWARE;
}

//! Returns the transformation set by setTransform
const core::matrix4& CSoftwareDriver::getTransform(E_TRANSFORMATION_STATE state)
{
	return TransformationMatrix[state];
}

//! Creates a render target texture.
ITexture* CSoftwareDriver::createRenderTargetTexture(const core::dimension2d<s32>& size)
{
	CImage* img = new CImage(video::ECF_A1R5G5B5, size);
	ITexture* tex = new CSoftwareTexture(img, 0);
	img->drop();
	return tex;
}


//! Clears the ZBuffer.
void CSoftwareDriver::clearZBuffer()
{
	if (ZBuffer)
		ZBuffer->clear();
}


//! Returns an image created from the last rendered frame.
IImage* CSoftwareDriver::createScreenShot()
{
	return new CImage(BackBuffer->getColorFormat(), BackBuffer);
}


//! creates a video driver
IVideoDriver* createSoftwareDriver(const core::dimension2d<s32>& windowSize, bool fullscreen, io::IFileSystem* io, video::IImagePresenter* presenter)
{
	return new CSoftwareDriver(windowSize, fullscreen, io, presenter);
}


} // end namespace video
} // end namespace irr
