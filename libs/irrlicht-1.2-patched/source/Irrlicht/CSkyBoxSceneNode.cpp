// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSkyBoxSceneNode.h"
#include "IVideoDriver.h"
#include "ISceneManager.h"
#include "ICameraSceneNode.h"
#include "S3DVertex.h"
#include "os.h"

namespace irr
{
namespace scene
{

//! constructor
CSkyBoxSceneNode::CSkyBoxSceneNode(video::ITexture* top, video::ITexture* bottom, video::ITexture* left,
			video::ITexture* right, video::ITexture* front, video::ITexture* back, ISceneNode* parent, ISceneManager* mgr, s32 id)
: ISceneNode(parent, mgr, id)
{
	#ifdef _DEBUG
	setDebugName("CSkyBoxSceneNode");
	#endif

	setAutomaticCulling(false);
	Box.MaxEdge.set(0,0,0);
	Box.MinEdge.set(0,0,0);

	// create indices

	Indices[0] = 0;
	Indices[1] = 1;
	Indices[2] = 2;
	Indices[3] = 3;

	// create material

	video::SMaterial mat;
	mat.Lighting = false;
	mat.ZBuffer = false;
	mat.ZWriteEnable = false;

	/* Hey, I am no artist, but look at that
	   cool ASCII art I made! ;)

       -111         111
          /6--------/5        y
         /  |      / |        ^  z
        /   |   11-1 |        | /
  -11-1 3---------2  |        |/
        |   7- - -| -4 1-11    *---->x
        | -1-11   |  /       3-------|2
        |/        | /         |    //|
        0---------1/          |  //  |
     -1-1-1     1-1-1         |//    |
	                         0--------1
	*/

	f32 onepixel = 0.0f;

	video::ITexture* tex = front;
	if (!tex) tex = left;
	if (!tex) tex = back;
	if (!tex) tex = right;
	if (!tex) tex = top;
	if (!tex) tex = bottom;

	if (tex)
	{
		core::dimension2d<s32> dim = tex->getSize();
		onepixel = 1.0f / (dim.Width * 1.5f);
	}

    //BP2K6: skybox is now 1000 units long (instead of 10)
	f32 l = 1000.0f; //10.0f

	f32 t = 1.0f - onepixel;
	f32 o = 0.0f + onepixel;

	Material[0] = mat;
	Material[0].Texture1 = front;
	Vertices[0] = video::S3DVertex(-l,-l,-l, 0,0,1, video::SColor(255,255,255,255), o, t);
	Vertices[1] = video::S3DVertex( l,-l,-l, 0,0,1, video::SColor(255,255,255,255), t, t);
	Vertices[2] = video::S3DVertex( l, l,-l, 0,0,1, video::SColor(255,255,255,255), t, o);
	Vertices[3] = video::S3DVertex(-l, l,-l, 0,0,1, video::SColor(255,255,255,255), o, o);

	// create left side

	Material[1] = mat;
	Material[1].Texture1 = left;
	Vertices[4] = video::S3DVertex( l,-l,-l, -1,0,0, video::SColor(255,255,255,255), o, t);
	Vertices[5] = video::S3DVertex( l,-l, l, -1,0,0, video::SColor(255,255,255,255), t, t);
	Vertices[6] = video::S3DVertex( l, l, l, -1,0,0, video::SColor(255,255,255,255), t, o);
	Vertices[7] = video::S3DVertex( l, l,-l, -1,0,0, video::SColor(255,255,255,255), o, o);

	// create back side

	Material[2] = mat;
	Material[2].Texture1 = back;
	Vertices[8]  = video::S3DVertex( l,-l, l, 0,0,-1, video::SColor(255,255,255,255), o, t);
	Vertices[9]  = video::S3DVertex(-l,-l, l, 0,0,-1, video::SColor(255,255,255,255), t, t);
	Vertices[10] = video::S3DVertex(-l, l, l, 0,0,-1, video::SColor(255,255,255,255), t, o);
	Vertices[11] = video::S3DVertex( l, l, l, 0,0,-1, video::SColor(255,255,255,255), o, o);

	// create right side

	Material[3] = mat;
	Material[3].Texture1 = right;
	Vertices[12] = video::S3DVertex(-l,-l, l, 1,0,0, video::SColor(255,255,255,255), o, t);
	Vertices[13] = video::S3DVertex(-l,-l,-l, 1,0,0, video::SColor(255,255,255,255), t, t);
	Vertices[14] = video::S3DVertex(-l, l,-l, 1,0,0, video::SColor(255,255,255,255), t, o);
	Vertices[15] = video::S3DVertex(-l, l, l, 1,0,0, video::SColor(255,255,255,255), o, o);

	// create top side

	Material[4] = mat;
	Material[4].Texture1 = top;
	Vertices[16] = video::S3DVertex( l, l, l, 0,-1,0, video::SColor(255,255,255,255), o, o);
	Vertices[17] = video::S3DVertex(-l, l, l, 0,-1,0, video::SColor(255,255,255,255), o, t);
	Vertices[18] = video::S3DVertex(-l, l,-l, 0,-1,0, video::SColor(255,255,255,255), t, t);
	Vertices[19] = video::S3DVertex( l, l,-l, 0,-1,0, video::SColor(255,255,255,255), t, o);

	// create bottom side

	Material[5] = mat;
	Material[5].Texture1 = bottom;
	Vertices[20] = video::S3DVertex(-l,-l, l, 0,1,0, video::SColor(255,255,255,255), o, o);
	Vertices[21] = video::S3DVertex( l,-l, l, 0,1,0, video::SColor(255,255,255,255), o, t);
	Vertices[22] = video::S3DVertex( l,-l,-l, 0,1,0, video::SColor(255,255,255,255), t, t);
	Vertices[23] = video::S3DVertex(-l,-l,-l, 0,1,0, video::SColor(255,255,255,255), t, o);
}



//! destructor
CSkyBoxSceneNode::~CSkyBoxSceneNode()
{
}


//! renders the node.
void CSkyBoxSceneNode::render()
{
	video::IVideoDriver* driver = SceneManager->getVideoDriver();
	scene::ICameraSceneNode* camera = SceneManager->getActiveCamera();

	if (!camera || !driver)
		return;

	if ( !camera->isOrthogonal() )
	{
		// draw perspective skybox

		core::matrix4 mat(AbsoluteTransformation);
		mat.setTranslation(camera->getAbsolutePosition());

		driver->setTransform(video::ETS_WORLD, mat);

		for (s32 i=0; i<6; ++i)
		{
			driver->setMaterial(Material[i]);
			driver->drawIndexedTriangleFan(&Vertices[i*4], 4, Indices, 2);
		}
	}
	else
	{
		// draw orthogonal skybox,
		// simply choose one texture and draw it as 2d picture.
		// there could be better ways to do this, but currently I think this is ok.

		core::vector3df lookVect = camera->getTarget() - camera->getAbsolutePosition();
		lookVect.normalize();
		core::vector3df absVect( core::abs_(lookVect.X),
					 core::abs_(lookVect.Y),
					 core::abs_(lookVect.Z));

		int idx = 0;

		if ( absVect.X >= absVect.Y && absVect.X >= absVect.Z )
		{
			// x direction
			idx = lookVect.X > 0 ? 0 : 2;
		}
		else
		if ( absVect.Y >= absVect.X && absVect.Y >= absVect.Z )
		{
			// y direction
			idx = lookVect.Y > 0 ? 4 : 5;
		}
		else
		if ( absVect.Z >= absVect.X && absVect.Z >= absVect.Y )
		{
			// z direction
			idx = lookVect.Z > 0 ? 1 : 3;
		}

		video::ITexture* tex = Material[idx].Texture1;

		if ( tex )
		{
			core::rect<s32> rctDest(core::position2d<s32>(-1,0), driver->getCurrentRenderTargetSize());
			core::rect<s32> rctSrc(core::position2d<s32>(0,0), tex->getSize());

			driver->draw2DImage(tex, rctDest, rctSrc);
		}
	}
}



//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CSkyBoxSceneNode::getBoundingBox() const
{
	return Box;
}


void CSkyBoxSceneNode::OnPreRender()
{
	if (IsVisible)
	{
		SceneManager->registerNodeForRendering(this, ESNRP_SKY_BOX);
		ISceneNode::OnPreRender();
	}
}


//! returns the material based on the zero based index i. To get the amount
//! of materials used by this scene node, use getMaterialCount().
//! This function is needed for inserting the node into the scene hirachy on a
//! optimal position for minimizing renderstate changes, but can also be used
//! to directly modify the material of a scene node.
video::SMaterial& CSkyBoxSceneNode::getMaterial(s32 i)
{
	return Material[i];
}


//! returns amount of materials used by this scene node.
s32 CSkyBoxSceneNode::getMaterialCount()
{
	return 6;
}


} // end namespace scene
} // end namespace irr

