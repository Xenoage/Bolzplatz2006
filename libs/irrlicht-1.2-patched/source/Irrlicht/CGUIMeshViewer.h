// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_GUI_MESH_VIEWER_H_INCLUDED__
#define __C_GUI_MESH_VIEWER_H_INCLUDED__

#include "IGUIMeshViewer.h"
#include "SMaterial.h"
#include "vector3d.h"

namespace irr
{

namespace gui
{

	class CGUIMeshViewer : public IGUIMeshViewer
	{
	public:

		//! constructor
		CGUIMeshViewer(IGUIEnvironment* environment, IGUIElement* parent, s32 id, core::rect<s32> rectangle);

		//! destructor
		~CGUIMeshViewer();

		//! sets the mesh to be shown
		virtual void setMesh(scene::IAnimatedMesh* mesh);

		//! sets the material
		virtual void setMaterial(const video::SMaterial& material);

		//! gets the material
		virtual const video::SMaterial& getMaterial();

		//! called if an event happened.
		virtual bool OnEvent(SEvent event);

		//! draws the element and its children
		virtual void draw();


	private:

		video::SMaterial Material;
		scene::IAnimatedMesh* Mesh;
	};


} // end namespace gui
} // end namespace irr

#endif

