// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_D3D8_MATERIAL_RENDERER_H_INCLUDED__
#define __C_D3D8_MATERIAL_RENDERER_H_INCLUDED__

#include "IrrCompileConfig.h"
#ifdef _IRR_WINDOWS_

#ifdef _IRR_COMPILE_WITH_DIRECT3D_8_
#include <d3d8.h>

#include "IMaterialRenderer.h"

namespace irr
{
namespace video  
{

D3DMATRIX UnitMatrixD3D8;
D3DMATRIX SphereMapMatrixD3D8;

//! Base class for all internal D3D8 material renderers
class CD3D8MaterialRenderer : public IMaterialRenderer
{
public:

	//! Constructor
	CD3D8MaterialRenderer(IDirect3DDevice8* d3ddev, video::IVideoDriver* driver)
		: pID3DDevice(d3ddev), Driver(driver)
	{
	}

protected:

	IDirect3DDevice8* pID3DDevice;
	video::IVideoDriver* Driver;
};


//! Solid material renderer
class CD3D8MaterialRenderer_SOLID : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_SOLID(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);
		}

		pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);
		pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}
};


//! Solid 2 layer material renderer
class CD3D8MaterialRenderer_SOLID_2_LAYER : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_SOLID_2_LAYER(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
			pID3DDevice->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState(0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXCOORDINDEX, 0);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_BLENDDIFFUSEALPHA);

			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);		
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}
};


//! Transparent add color material renderer
class CD3D8MaterialRenderer_TRANSPARENT_ADD_COLOR : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_TRANSPARENT_ADD_COLOR(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
			pID3DDevice->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_ONE);
			pID3DDevice->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCCOLOR);
		}

		material.ZWriteEnable = false;
		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	//! Returns if the material is transparent. 
	virtual bool isTransparent() 
	{
		return true; 
	}
};


//! Transparent vertex alpha material renderer
class CD3D8MaterialRenderer_TRANSPARENT_VERTEX_ALPHA : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_TRANSPARENT_VERTEX_ALPHA(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_SELECTARG1);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAARG1,  D3DTA_DIFFUSE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
			pID3DDevice->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_ONE);
			pID3DDevice->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);
		}

		material.ZWriteEnable = false;
		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	//! Returns if the material is transparent. 
	virtual bool isTransparent() 
	{
		return true; 
	}
};


//! Transparent alpha channel material renderer
class CD3D8MaterialRenderer_TRANSPARENT_ALPHA_CHANNEL : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_TRANSPARENT_ALPHA_CHANNEL(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates
			|| material.MaterialTypeParam != lastMaterial.MaterialTypeParam )
		{
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLOROP,   D3DTOP_MODULATE ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLORARG1, D3DTA_TEXTURE ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLORARG2, D3DTA_CURRENT ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_ALPHAOP,   D3DTOP_SELECTARG1 ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE ); 

			pID3DDevice->SetTextureStageState( 1, D3DTSS_COLOROP,   D3DTOP_DISABLE ); 
			pID3DDevice->SetTextureStageState( 1, D3DTSS_ALPHAOP,   D3DTOP_DISABLE ); 

			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE); 
			pID3DDevice->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA); 
			pID3DDevice->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA ); 

			s32 refValue = (s32)(material.MaterialTypeParam * 255);
			if ( !refValue ) 
				refValue = 127; // default value

			pID3DDevice->SetRenderState(D3DRS_ALPHAREF, refValue);            
            pID3DDevice->SetRenderState(D3DRS_ALPHAFUNC, D3DCMP_GREATEREQUAL);
			pID3DDevice->SetRenderState(D3DRS_ALPHATESTENABLE, TRUE); 
		}

		//material.ZWriteEnable = false;
		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	virtual void OnUnsetMaterial() 
	{ 
		pID3DDevice->SetRenderState(D3DRS_ALPHATESTENABLE, FALSE); 
	}  

	//! Returns if the material is transparent. 
	virtual bool isTransparent() 
	{
		return true; 
	}
};


//! Transparent alpha channel material renderer
class CD3D8MaterialRenderer_TRANSPARENT_ALPHA_CHANNEL_REF : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_TRANSPARENT_ALPHA_CHANNEL_REF(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLOROP,   D3DTOP_MODULATE ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLORARG1, D3DTA_TEXTURE ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_COLORARG2, D3DTA_CURRENT ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_ALPHAOP,   D3DTOP_SELECTARG1 ); 
			pID3DDevice->SetTextureStageState( 0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE ); 

			pID3DDevice->SetTextureStageState( 1, D3DTSS_COLOROP,   D3DTOP_DISABLE ); 
			pID3DDevice->SetTextureStageState( 1, D3DTSS_ALPHAOP,   D3DTOP_DISABLE ); 

			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE); 
			
			pID3DDevice->SetRenderState(D3DRS_ALPHAREF,127);            
			pID3DDevice->SetRenderState(D3DRS_ALPHAFUNC, D3DCMP_GREATEREQUAL);
			pID3DDevice->SetRenderState(D3DRS_ALPHATESTENABLE, TRUE); 
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	virtual void OnUnsetMaterial() 
	{ 
		pID3DDevice->SetRenderState(D3DRS_ALPHATESTENABLE, FALSE); 
	}   

	//! Returns if the material is transparent. The scene managment needs to know this
	//! for being able to sort the materials by opaque and transparent.
	virtual bool isTransparent() 
	{ 
		return false; // this material is not really transparent because it does no blending.
	}
};



//! material renderer for all kinds of linghtmaps 
class CD3D8MaterialRenderer_LIGHTMAP : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_LIGHTMAP(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			if (material.MaterialType >= EMT_LIGHTMAP_LIGHTING)
			{
				// with lighting
				pID3DDevice->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_MODULATE);
				pID3DDevice->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
				pID3DDevice->SetTextureStageState(0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			}
			else
			{
				pID3DDevice->SetTextureStageState(0, D3DTSS_COLOROP, D3DTOP_SELECTARG1);
				pID3DDevice->SetTextureStageState(0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			}

			pID3DDevice->SetTextureStageState(0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_TEXCOORDINDEX, 1);

			if (material.MaterialType == EMT_LIGHTMAP_ADD)
				pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_ADD);
			else
			if (material.MaterialType == EMT_LIGHTMAP_M4)
				pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_MODULATE4X);
			else
			if (material.MaterialType == EMT_LIGHTMAP_M2)
				pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_MODULATE2X);
			else
				pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_MODULATE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG2, D3DTA_CURRENT);
			pID3DDevice->SetTextureStageState (1, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);		
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}
};

//! material renderer for detail maps
class CD3D8MaterialRenderer_DETAIL_MAP : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_DETAIL_MAP(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState (1, D3DTSS_COLOROP, D3DTOP_ADDSIGNED);
			pID3DDevice->SetTextureStageState (1, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (1, D3DTSS_COLORARG2, D3DTA_CURRENT);
			pID3DDevice->SetTextureStageState (1, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_TEXCOORDINDEX, 1);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);		
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}
};


//! sphere map material renderer
class CD3D8MaterialRenderer_SPHERE_MAP : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_SPHERE_MAP(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_DISABLE);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);

			pID3DDevice->SetTransform( D3DTS_TEXTURE0, &SphereMapMatrixD3D8 );
			pID3DDevice->SetTextureStageState( 0, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_COUNT2 );
			pID3DDevice->SetTextureStageState( 0, D3DTSS_TEXCOORDINDEX, D3DTSS_TCI_CAMERASPACENORMAL );
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	virtual void OnUnsetMaterial()
	{
		pID3DDevice->SetTextureStageState( 0, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_DISABLE );
		pID3DDevice->SetTextureStageState( 0, D3DTSS_TEXCOORDINDEX, 0);
		pID3DDevice->SetTransform( D3DTS_TEXTURE0, &UnitMatrixD3D8 );
	}
};


//! reflection 2 layer material renderer
class CD3D8MaterialRenderer_REFLECTION_2_LAYER : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_REFLECTION_2_LAYER(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG2, D3DTA_CURRENT);

			pID3DDevice->SetTransform( D3DTS_TEXTURE1, &SphereMapMatrixD3D8 );
			pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_COUNT2 );
			pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXCOORDINDEX, D3DTSS_TCI_CAMERASPACEREFLECTIONVECTOR);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);
		}

		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	virtual void OnUnsetMaterial()
	{
		pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_DISABLE );
		pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXCOORDINDEX, 1);
		pID3DDevice->SetTransform( D3DTS_TEXTURE1, &UnitMatrixD3D8 );
	}
};


//! reflection 2 layer material renderer
class CD3D8MaterialRenderer_TRANSPARENT_REFLECTION_2_LAYER : public CD3D8MaterialRenderer
{
public:

	CD3D8MaterialRenderer_TRANSPARENT_REFLECTION_2_LAYER(IDirect3DDevice8* p, video::IVideoDriver* d)
		: CD3D8MaterialRenderer(p, d) {}

	virtual void OnSetMaterial(SMaterial& material, const SMaterial& lastMaterial,
		bool resetAllRenderstates, IMaterialRendererServices* services) 
	{
		if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
		{
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_COLORARG2, D3DTA_DIFFUSE);
			pID3DDevice->SetTextureStageState (0, D3DTSS_ALPHAOP,  D3DTOP_DISABLE);

			pID3DDevice->SetTextureStageState(1, D3DTSS_COLOROP, D3DTOP_MODULATE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG1, D3DTA_TEXTURE);
			pID3DDevice->SetTextureStageState(1, D3DTSS_COLORARG2, D3DTA_CURRENT);

			pID3DDevice->SetTransform( D3DTS_TEXTURE1, &SphereMapMatrixD3D8 );
			pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_COUNT2 );
			pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXCOORDINDEX, D3DTSS_TCI_CAMERASPACEREFLECTIONVECTOR);
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, FALSE);
	
			pID3DDevice->SetRenderState(D3DRS_ALPHABLENDENABLE, TRUE);
			pID3DDevice->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);
			pID3DDevice->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA );
		}

		material.ZWriteEnable = false;
		services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
	}

	virtual void OnUnsetMaterial()
	{
		pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXTURETRANSFORMFLAGS, D3DTTFF_DISABLE );
		pID3DDevice->SetTextureStageState( 1, D3DTSS_TEXCOORDINDEX, 1);
		pID3DDevice->SetTransform( D3DTS_TEXTURE1, &UnitMatrixD3D8 );
	}

	//! Returns if the material is transparent. 
	virtual bool isTransparent() 
	{
		return true; 
	}
};

} // end namespace video
} // end namespace irr

#endif
#endif
#endif

