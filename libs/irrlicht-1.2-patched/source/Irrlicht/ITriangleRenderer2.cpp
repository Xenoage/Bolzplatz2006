// Copyright (C) 2002-2006 Nikolaus Gebhardt/Alten Thomas
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "SoftwareDriver2_compile_config.h"
#include "ITriangleRenderer2.h"

namespace irr
{

namespace video
{

	ITriangleRenderer2::ITriangleRenderer2(IZBuffer2* zbuffer)
		:RenderTarget(0),ZBuffer(zbuffer)
	{
		IT[0].Texture = 0;
		IT[1].Texture = 0;

		#ifdef _DEBUG
		setDebugName("CTRTextureLightMap2_M1");
		#endif

		if (ZBuffer)
			zbuffer->grab();
	}


	//! destructor
	ITriangleRenderer2::~ITriangleRenderer2()
	{
		if (RenderTarget)
			RenderTarget->drop();

		if (ZBuffer)
			ZBuffer->drop();

		if ( IT[0].Texture )
			IT[0].Texture->drop();

		if ( IT[1].Texture )
			IT[1].Texture->drop();
	};

	//! sets a render target
	void ITriangleRenderer2::setRenderTarget(video::IImage* surface, const core::rect<s32>& viewPort)
	{
		if (RenderTarget)
			RenderTarget->drop();

		RenderTarget = surface;

		if (RenderTarget)
		{
			SurfaceWidth = RenderTarget->getDimension().Width;
			RenderTarget->grab();
		}
	}


	//! sets the Texture
	void ITriangleRenderer2::setTexture( u32 stage, video::CSoftwareTexture2* texture)
	{
		sInternalTexture *it = &IT[stage];

		if ( it->Texture)
			it->Texture->drop();

		it->Texture = texture;

		if ( it->Texture)
		{
			it->Texture->grab();

			// prepare for optimal fixpoint
			it->pitch = it->Texture->getPitch();
			it->pitchlog2 = s32_log2 ( it->pitch );

			it->textureXMask = s32_to_fixPoint ( it->Texture->getSize().Width - 1 );
			it->textureYMask = s32_to_fixPoint ( it->Texture->getSize().Height - 1 );

			//it->textureXMask |= FIX_POINT_FRACT_MASK;
			//it->textureYMask |= FIX_POINT_FRACT_MASK;

			it->textureXMask &= FIX_POINT_UNSIGNED_MASK;
			it->textureYMask &= FIX_POINT_UNSIGNED_MASK;
		}
	}


} // end namespace video
} // end namespace irr


