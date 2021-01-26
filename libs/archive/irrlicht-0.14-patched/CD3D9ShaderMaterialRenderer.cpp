// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "IrrCompileConfig.h"
#ifdef _IRR_COMPILE_WITH_DIRECT3D_9_

#include "CD3D9ShaderMaterialRenderer.h"
#include "IShaderConstantSetCallBack.h"
#include "IVideoDriver.h"
#include "os.h"

#ifndef _IRR_D3D_NO_SHADER_DEBUGGING
#include <stdio.h>
#endif


namespace irr
{
namespace video  
{

//! Public constructor
CD3D9ShaderMaterialRenderer::CD3D9ShaderMaterialRenderer(IDirect3DDevice9* d3ddev, video::IVideoDriver* driver, 
		s32& outMaterialTypeNr, const c8* vertexShaderProgram, const c8* pixelShaderProgram,
		IShaderConstantSetCallBack* callback, IMaterialRenderer* baseMaterial, s32 userData)
: pID3DDevice(d3ddev), Driver(driver), BaseMaterial(baseMaterial), CallBack(callback),
	VertexShader(0), PixelShader(0), UserData(userData)
{
	if (BaseMaterial)
		BaseMaterial->grab();

	if (CallBack)
		CallBack->grab();

	init(outMaterialTypeNr, vertexShaderProgram, pixelShaderProgram);
}



//! constructor only for use by derived classes who want to
//! create a fall back material for example.
CD3D9ShaderMaterialRenderer::CD3D9ShaderMaterialRenderer(IDirect3DDevice9* d3ddev,
														 video::IVideoDriver* driver, 
														 IShaderConstantSetCallBack* callback,
														 IMaterialRenderer* baseMaterial,
														 s32 userData)
: pID3DDevice(d3ddev), Driver(driver), BaseMaterial(baseMaterial), CallBack(callback),
	VertexShader(0), PixelShader(0), UserData(userData)
{
	if (BaseMaterial)
		BaseMaterial->grab();

	if (CallBack)
		CallBack->grab();
}


void CD3D9ShaderMaterialRenderer::init(s32& outMaterialTypeNr, const c8* vertexShaderProgram,
									   const c8* pixelShaderProgram)
{
	outMaterialTypeNr = -1;

	// create vertex shader
	if (!createVertexShader(vertexShaderProgram))
		return;

	// create pixel shader
	if (!createPixelShader(pixelShaderProgram))
		return;

	// register myself as new material
	outMaterialTypeNr = Driver->addMaterialRenderer(this);
}



//! Destructor
CD3D9ShaderMaterialRenderer::~CD3D9ShaderMaterialRenderer()
{
	if (CallBack)
		CallBack->drop();

	if (VertexShader)
		VertexShader->Release();

	if (PixelShader)
		PixelShader->Release();

	if (BaseMaterial)
		BaseMaterial->drop ();
}

bool CD3D9ShaderMaterialRenderer::OnRender(IMaterialRendererServices* service, E_VERTEX_TYPE vtxtype)
{
	// call callback to set shader constants
	if (CallBack && (VertexShader || PixelShader))
		CallBack->OnSetConstants(service, UserData);

	return true;
}

void CD3D9ShaderMaterialRenderer::OnSetMaterial(video::SMaterial& material, const video::SMaterial& lastMaterial,
	bool resetAllRenderstates, video::IMaterialRendererServices* services) 
{
	if (material.MaterialType != lastMaterial.MaterialType || resetAllRenderstates)
	{		
		if (VertexShader)
		{
			// save old vertex shader
			pID3DDevice->GetVertexShader(&OldVertexShader);
			
			// set new vertex shader
			if (FAILED(pID3DDevice->SetVertexShader(VertexShader)))
				os::Printer::log("Could not set vertex shader.");
		}

		// set new pixel shader
		if (PixelShader)
		{
			if (FAILED(pID3DDevice->SetPixelShader(PixelShader)))
				os::Printer::log("Could not set pixel shader.");
		}

		if (BaseMaterial)
			BaseMaterial->OnSetMaterial(material, material, true, services);
	}

	services->setBasicRenderStates(material, lastMaterial, resetAllRenderstates);
}

void CD3D9ShaderMaterialRenderer::OnUnsetMaterial() 
{
	if (VertexShader)
		pID3DDevice->SetVertexShader(OldVertexShader);

	if (PixelShader)
		pID3DDevice->SetPixelShader(0);

	if (BaseMaterial)
		BaseMaterial->OnUnsetMaterial();
}

	
//! Returns if the material is transparent. The scene managment needs to know this
//! for being able to sort the materials by opaque and transparent.
bool CD3D9ShaderMaterialRenderer::isTransparent() 
{
	return BaseMaterial ? BaseMaterial->isTransparent() : false; 
}

bool CD3D9ShaderMaterialRenderer::createPixelShader(const c8* pxsh)
{
	if (!pxsh)
		return true;

	// compile shader

	LPD3DXBUFFER code = 0;
	LPD3DXBUFFER errors = 0;

	#ifdef _IRR_D3D_NO_SHADER_DEBUGGING

		// compile shader without debug info
		D3DXAssembleShader(pxsh, strlen(pxsh), 0, 0, 0, &code, &errors);
	#else

		// compile shader and emitt some debug informations to
		// make it possible to debug the shader in visual studio

		static int irr_dbg_file_nr = 0; 
		++irr_dbg_file_nr;
		char tmp[32];
		sprintf(tmp, "irr_d3d9_dbg_shader_%d.psh", irr_dbg_file_nr);

		FILE* f = fopen(tmp, "wb");
		fwrite(pxsh, strlen(pxsh), 1, f);
		fflush(f);
		fclose(f);

		D3DXAssembleShaderFromFile(tmp, 0, 0, D3DXSHADER_DEBUG, &code, &errors);

	#endif


	if (errors)
	{
		// print out compilation errors.
		os::Printer::log("Pixel shader compilation failed:");
		os::Printer::log((c8*)errors->GetBufferPointer());			

		errors->Release();
		return false;
	}

	if (FAILED(pID3DDevice->CreatePixelShader((DWORD*)code->GetBufferPointer(), &PixelShader)))
	{
		os::Printer::log("Could not create pixel shader.");
		code->Release();
		return false;
	}

	code->Release();
	return true;
}



bool CD3D9ShaderMaterialRenderer::createVertexShader(const char* vtxsh)
{
	if (!vtxsh)
		return true;

	// compile shader

	LPD3DXBUFFER code = 0;
	LPD3DXBUFFER errors = 0;

	#ifdef _IRR_D3D_NO_SHADER_DEBUGGING

		// compile shader without debug info
		D3DXAssembleShader(vtxsh, strlen(vtxsh), 0, 0, 0, &code, &errors);

	#else

		// compile shader and emitt some debug informations to
		// make it possible to debug the shader in visual studio

		static int irr_dbg_file_nr = 0; 
		++irr_dbg_file_nr;
		char tmp[32];
		sprintf(tmp, "irr_d3d9_dbg_shader_%d.vsh", irr_dbg_file_nr);

		FILE* f = fopen(tmp, "wb");
		fwrite(vtxsh, strlen(vtxsh), 1, f);
		fflush(f);
		fclose(f);

        D3DXAssembleShaderFromFile(tmp, 0, 0, D3DXSHADER_DEBUG, &code, &errors);

	#endif
	

	if (errors)
	{
		// print out compilation errors.
		os::Printer::log("Vertex shader compilation failed:");
		os::Printer::log((c8*)errors->GetBufferPointer());

		errors->Release();
		return false;
	}

	if (FAILED(pID3DDevice->CreateVertexShader((DWORD*)code->GetBufferPointer(), &VertexShader)))
	{
		os::Printer::log("Could not create vertex shader.");
		code->Release();
		return false;
	}

	code->Release();
	return true;
}



} // end namespace video
} // end namespace irr

#endif

