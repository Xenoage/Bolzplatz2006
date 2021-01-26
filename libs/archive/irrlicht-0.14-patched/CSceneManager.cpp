// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CSceneManager.h"
#include "IVideoDriver.h"
#include "IFileSystem.h"
#include "IAnimatedMesh.h"
#include "CMeshCache.h"

#include "os.h"
#include <wchar.h>

#include "CGeometryCreator.h"

#include "CDefaultMeshFormatLoader.h"
#include "C3DSMeshFileLoader.h"
#include "CXMeshFileLoader.h"
#include "COCTLoader.h"
#include "CCSMLoader.h"
#include "CLMTSMeshFileLoader.h"
#include "CMY3DMeshFileLoader.h"
#include "CColladaFileLoader.h"
#include "CDMFLoader.h"

#include "CTestSceneNode.h"
#include "CAnimatedMeshSceneNode.h"
#include "COctTreeSceneNode.h"
#include "CCameraSceneNode.h"
#include "CCameraMayaSceneNode.h"
#include "CCameraFPSSceneNode.h"
#include "CLightSceneNode.h"
#include "CBillboardSceneNode.h"
#include "CMeshSceneNode.h"
#include "CSkyBoxSceneNode.h"
#include "CParticleSystemSceneNode.h"
#include "CDummyTransformationSceneNode.h"
#include "CWaterSurfaceSceneNode.h"
#include "CTerrainSceneNode.h"
#include "CEmptySceneNode.h"
#include "CTextSceneNode.h"

#include "CSceneCollisionManager.h"
#include "CMeshManipulator.h"
#include "CTriangleSelector.h"
#include "COctTreeTriangleSelector.h"
#include "CTriangleBBSelector.h"
#include "CMetaTriangleSelector.h"
#include "CTerrainTriangleSelector.h"

#include "CSceneNodeAnimatorRotation.h"
#include "CSceneNodeAnimatorFlyCircle.h"
#include "CSceneNodeAnimatorFlyStraight.h"
#include "CSceneNodeAnimatorTexture.h"
#include "CSceneNodeAnimatorCollisionResponse.h"
#include "CSceneNodeAnimatorDelete.h"
#include "CSceneNodeAnimatorFollowSpline.h"

#include "CCrowdSceneNode.h"


namespace irr
{
namespace scene
{


//! constructor
CSceneManager::CSceneManager(video::IVideoDriver* driver, io::IFileSystem* fs,
							 gui::ICursorControl* cursorControl, CMeshCache* cache)
: ISceneNode(0, 0), Driver(driver), FileSystem(fs), ActiveCamera(0),
	CursorControl(cursorControl), CollisionManager(0),
	ShadowColor(150,0,0,0), MeshManipulator(0), CurrentRendertime(ESNRP_COUNT),
	MeshCache(cache)
{
	#ifdef _DEBUG
	ISceneManager::setDebugName("CSceneManager ISceneManager");
	ISceneNode::setDebugName("CSceneManager ISceneNode");
	#endif

	if (Driver)
		Driver->grab();

	if (FileSystem)
		FileSystem->grab();

	if (CursorControl)
		CursorControl->grab();

	// create mesh cache if not there already
	if (!MeshCache)
		MeshCache = new CMeshCache();
	else
		MeshCache->grab();

    // create collision manager
	CollisionManager = new CSceneCollisionManager(this, Driver);

	// create manipulator
	MeshManipulator = new CMeshManipulator();

	// add default format loader

	MeshLoaderList.push_back(new CDefaultMeshFormatLoader(FileSystem, Driver));
	MeshLoaderList.push_back(new C3DSMeshFileLoader(FileSystem, Driver));
	MeshLoaderList.push_back(new CXMeshFileLoader(MeshManipulator, Driver));
	MeshLoaderList.push_back(new COCTLoader(Driver));
	MeshLoaderList.push_back(new CCSMLoader(this, FileSystem));
	MeshLoaderList.push_back(new CLMTSMeshFileLoader(FileSystem, Driver, &Parameters));
	MeshLoaderList.push_back(new CMY3DMeshFileLoader(FileSystem, Driver, this));
	MeshLoaderList.push_back(new CColladaFileLoader(Driver, this, FileSystem));
	MeshLoaderList.push_back(new CDMFLoader(Driver, this));	
}



//! destructor
CSceneManager::~CSceneManager()
{
	clearDeletionList();

	if (Driver)
		Driver->drop();

	if (FileSystem)
		FileSystem->drop();

	if (CursorControl)
		CursorControl->drop();

	if (CollisionManager)
		CollisionManager->drop();

	if (MeshManipulator)
		MeshManipulator->drop();

	for (u32 i=0; i<MeshLoaderList.size(); ++i)
		MeshLoaderList[i]->drop();

	if (ActiveCamera)
		ActiveCamera->drop();

	if (MeshCache)
		MeshCache->drop();
}


//! gets an animateable mesh. loads it if needed. returned pointer must not be dropped.
IAnimatedMesh* CSceneManager::getMesh(const c8* filename)
{
	IAnimatedMesh* msh = 0;

	core::stringc name = filename;
	name.make_lower();

	msh = MeshCache->findMesh(name.c_str());
	if (msh)
		return msh;

	io::IReadFile* file = FileSystem->createAndOpenFile(filename);
	if (!file)
	{
		os::Printer::log("Could not load mesh, because file could not be opened.", filename, ELL_ERROR);
		return 0;
	}

	s32 count = MeshLoaderList.size();
	for (s32 i=count-1; i>=0; --i)
		if (MeshLoaderList[i]->isALoadableFileExtension(name.c_str()))
		{
			msh = MeshLoaderList[i]->createMesh(file);
			if (msh)
			{
				MeshCache->addMesh(filename, msh);
				msh->drop();
				break;
			}
		}

	file->drop();

	if (!msh)
		os::Printer::log("Could not load mesh, file format seems to be unsupported", filename, ELL_ERROR);
	else
		os::Printer::log("Loaded mesh", filename, ELL_INFORMATION);

	return msh;
}


//! returns the video driver
video::IVideoDriver* CSceneManager::getVideoDriver()
{
	return Driver;
}

//! Adds a text scene node, which is able to display 
//! 2d text at a position in three dimensional space
ITextSceneNode* CSceneManager::addTextSceneNode(gui::IGUIFont* font, const wchar_t* text,
	video::SColor color, 
	ISceneNode* parent,	const core::vector3df& position,
	s32 id)
{
	if (!font)
		return 0;

	if (!parent)
		parent = this;

	ITextSceneNode* t = new CTextSceneNode(parent, this, id, font, 
		getSceneCollisionManager(), position, text, color);
	t->drop();

	return t;
}	


//! adds a test scene node for test purposes to the scene. It is a simple cube of (1,1,1) size. 
//! the returned pointer must not be dropped.
ISceneNode* CSceneManager::addTestSceneNode(f32 size, ISceneNode* parent, s32 id,
	const core::vector3df& position, const core::vector3df& rotation, const core::vector3df& scale)
{
	if (!parent)
		parent = this;

	ISceneNode* node = new CTestSceneNode(size, parent, this, id, position, rotation, scale);
	node->drop();

	return node;	
}


ICrowdSceneNode* CSceneManager::addCrowdSceneNode(ISceneNode* parent, s32 id,
  s32 characterCount,
	const core::vector3df& position, const core::vector3df& rotation, const core::vector3df& scale)
{
	if (!parent)
		parent = this;

	ICrowdSceneNode* node = new CCrowdSceneNode(parent,
    this, id, characterCount, position, rotation, scale);
	node->drop();

	return node;	
}


//! adds a scene node for rendering a static mesh
//! the returned pointer must not be dropped.
ISceneNode* CSceneManager::addMeshSceneNode(IMesh* mesh, ISceneNode* parent, s32 id,
	const core::vector3df& position, const core::vector3df& rotation,
	const core::vector3df& scale)
{
	if (!mesh)
		return 0;

	if (!parent)
		parent = this;

	ISceneNode* node = new CMeshSceneNode(mesh, parent, this, id, position, rotation, scale);
	node->drop();

	return node;
}


//! Adds a scene node for rendering a animated water surface mesh.
ISceneNode* CSceneManager::addWaterSurfaceSceneNode(IMesh* mesh, f32 waveHeight, f32 waveSpeed, f32 waveLenght, 
	ISceneNode* parent, s32 id, const core::vector3df& position,
	const core::vector3df& rotation, const core::vector3df& scale)
{
	if (!mesh)
		return 0;

	if (!parent)
		parent = this;

	ISceneNode* node = new CWaterSurfaceSceneNode(waveHeight, waveSpeed, waveLenght, 
		mesh, parent, this, id, position, rotation, scale);

	node->drop();

	return node;
}


	
//! adds a scene node for rendering an animated mesh model
IAnimatedMeshSceneNode* CSceneManager::addAnimatedMeshSceneNode(IAnimatedMesh* mesh, ISceneNode* parent, s32 id,
	const core::vector3df& position, const core::vector3df& rotation , const core::vector3df& scale)
{
	if (!mesh)
		return 0;

	if (!parent)
		parent = this;

	IAnimatedMeshSceneNode* node = 
		new CAnimatedMeshSceneNode(mesh, parent, this, id, position, rotation, scale);
	node->drop();

	return node;	
}


//! Adds a scene node for rendering using a octtree to the scene graph. This a good method for rendering 
//! scenes with lots of geometry. The Octree is built on the fly from the mesh, much
//! faster then a bsp tree.
ISceneNode* CSceneManager::addOctTreeSceneNode(IAnimatedMesh* mesh, ISceneNode* parent, 
			s32 id, s32 minimalPolysPerNode)
{
	if (!mesh || !mesh->getFrameCount())
		return 0;

	return addOctTreeSceneNode(mesh->getMesh(0), parent, id, minimalPolysPerNode);
}



//! Adss a scene node for rendering using a octtree. This a good method for rendering 
//! scenes with lots of geometry. The Octree is built on the fly from the mesh, much
//! faster then a bsp tree.
ISceneNode* CSceneManager::addOctTreeSceneNode(IMesh* mesh, ISceneNode* parent,
											   s32 id, s32 minimalPolysPerNode)
{
	if (!mesh)
		return 0;

	if (!parent)
		parent = this;

	COctTreeSceneNode* node = new COctTreeSceneNode(parent, this, id, minimalPolysPerNode);
	node->createTree(mesh);
	node->drop();

    return node;
}



//! Adds a camera scene node to the tree and sets it as active camera.
//! \param position: Position of the space relative to its parent where the camera will be placed.
//! \param lookat: Position where the camera will look at. Also known as target.
//! \param parent: Parent scene node of the camera. Can be null. If the parent moves,
//! the camera will move too.
//! \return Returns pointer to interface to camera
ICameraSceneNode* CSceneManager::addCameraSceneNode(ISceneNode* parent,
	const core::vector3df& position, const core::vector3df& lookat, s32 id)
{
	if (!parent)
		parent = this;

	ICameraSceneNode* node = new CCameraSceneNode(parent, this, id, position, lookat);
	node->drop();

	setActiveCamera(node);

	return node;
}



//! Adds a camera scene node which is able to be controlle with the mouse similar
//! like in the 3D Software Maya by Alias Wavefront.
//! The returned pointer must not be dropped.
ICameraSceneNode* CSceneManager::addCameraSceneNodeMaya(ISceneNode* parent, 
	f32 rotateSpeed, f32 zoomSpeed, f32 translationSpeed, s32 id)
{
	if (!parent)
		parent = this;

	ICameraSceneNode* node = new CCameraMayaSceneNode(parent, this, id, rotateSpeed,
		zoomSpeed, translationSpeed);
	node->drop();

	setActiveCamera(node);

	return node;
}



//! Adds a camera scene node which is able to be controled with the mouse and keys
//! like in most first person shooters (FPS): 
ICameraSceneNode* CSceneManager::addCameraSceneNodeFPS(ISceneNode* parent,
	f32 rotateSpeed, f32 moveSpeed, s32 id,
	SKeyMap* keyMapArray, s32 keyMapSize, bool noVerticalMovement)
{
	if (!parent)
		parent = this;

	ICameraSceneNode* node = new CCameraFPSSceneNode(parent, this, CursorControl, 
		id, rotateSpeed, moveSpeed, keyMapArray, keyMapSize, noVerticalMovement);
	node->drop();

	setActiveCamera(node);

	return node;
}



//! Adds a dynamic light scene node. The light will cast dynamic light on all
//! other scene nodes in the scene, which have the material flag video::MTF_LIGHTING
//! turned on. (This is the default setting in most scene nodes).
ILightSceneNode* CSceneManager::addLightSceneNode(ISceneNode* parent,
	const core::vector3df& position, video::SColorf color, f32 range, s32 id)
{
	if (!parent)
		parent = this;

	ILightSceneNode* node = new CLightSceneNode(parent, this, id, position, color, range);
	node->drop();

	return node;
}



//! Adds a billboard scene node to the scene. A billboard is like a 3d sprite: A 2d element,
//! which always looks to the camera. It is usually used for things like explosions, fire,
//! lensflares and things like that.
IBillboardSceneNode* CSceneManager::addBillboardSceneNode(ISceneNode* parent,
	const core::dimension2d<f32>& size, const core::vector3df& position, s32 id)
{
	if (!parent)
		parent = this;

	IBillboardSceneNode* node = new CBillboardSceneNode(parent, this, id, position, size);
	node->drop();

	return node;
}



//! Adds a skybox scene node. A skybox is a big cube with 6 textures on it and
//! is drawed around the camera position. 
ISceneNode* CSceneManager::addSkyBoxSceneNode(video::ITexture* top, video::ITexture* bottom,
	video::ITexture* left, video::ITexture* right, video::ITexture* front,
	video::ITexture* back, ISceneNode* parent, s32 id)
{
	if (!parent)
		parent = this;

	ISceneNode* node = new CSkyBoxSceneNode(top, bottom, left, right, 
			front, back, parent, this, id);

	node->drop();
	return node;
}


//! Adds a particle system scene node. 
IParticleSystemSceneNode* CSceneManager::addParticleSystemSceneNode(
	bool withDefaultEmitter, ISceneNode* parent, s32 id,
	const core::vector3df& position, const core::vector3df& rotation,
	const core::vector3df& scale)
{
	if (!parent)
		parent = this;

	IParticleSystemSceneNode* node = new CParticleSystemSceneNode(withDefaultEmitter,
		parent, this, id, position, rotation, scale);
	node->drop();

	return node;
}


//! Adds a terrain scene node to the scene graph.
ITerrainSceneNode* CSceneManager::addTerrainSceneNode(
	const char* heightMapFileName, 
	ISceneNode* parent, s32 id, 
	const core::vector3df& position,
	const core::vector3df& rotation,
	const core::vector3df& scale,
	video::SColor vertexColor,
	s32 maxLOD, E_TERRAIN_PATCH_SIZE patchSize)
{
	io::IReadFile* file = FileSystem->createAndOpenFile(heightMapFileName);
	if (!file)
	{
		os::Printer::log("Could not load terrain, because file could not be opened.", 
			heightMapFileName, ELL_ERROR);
		return 0;
	}

	ITerrainSceneNode* terrain = addTerrainSceneNode(file, parent, id,
		position, rotation, scale, vertexColor, maxLOD, patchSize);
	file->drop();

	return terrain;
}

//! Adds a terrain scene node to the scene graph.
ITerrainSceneNode* CSceneManager::addTerrainSceneNode(
	io::IReadFile* heightMapFile, 
	ISceneNode* parent, s32 id, 
	const core::vector3df& position,
	const core::vector3df& rotation,
	const core::vector3df& scale,
	video::SColor vertexColor,
	s32 maxLOD, E_TERRAIN_PATCH_SIZE patchSize)
{
	if (!parent)
		parent = this;

	CTerrainSceneNode* node = new CTerrainSceneNode(parent, this, id,
		maxLOD, patchSize, position, rotation, scale);

	if (!node->loadHeightMap(heightMapFile, vertexColor))
	{
		node->remove();
		node->drop();
		return 0;
	}

	node->drop();
	return node;
}


//! Adds an empty scene node.
ISceneNode* CSceneManager::addEmptySceneNode(ISceneNode* parent, s32 id)
{
	if (!parent)
		parent = this;

	ISceneNode* node = new CEmptySceneNode(parent, this, id);
	node->drop();

	return node;
}


//! Adds a dummy transformation scene node to the scene graph.
IDummyTransformationSceneNode* CSceneManager::addDummyTransformationSceneNode(
	ISceneNode* parent, s32 id)
{
	if (!parent)
		parent = this;

	IDummyTransformationSceneNode* node = new CDummyTransformationSceneNode(
		parent, this, id);
	node->drop();

	return node;
}

//! Adds a Hill Plane mesh to the mesh pool. The mesh is generated on the fly
//! and looks like a plane with some hills on it. It is uses mostly for quick
//! tests of the engine only. You can specify how many hills there should be 
//! on the plane and how high they should be. Also you must specify a name for
//! the mesh, because the mesh is added to the mesh pool, and can be retieved
//! again using ISceneManager::getMesh with the name as parameter.
IAnimatedMesh* CSceneManager::addHillPlaneMesh(const c8* name,
	const core::dimension2d<f32>& tileSize, const core::dimension2d<s32>& tileCount,
	video::SMaterial* material,	f32 hillHeight, const core::dimension2d<f32>& countHills,
	const core::dimension2d<f32>& textureRepeatCount)
{
	if (!name || MeshCache->isMeshLoaded(name))
		return 0;

	IAnimatedMesh* animatedMesh = CGeometryCreator::createHillPlaneMesh(tileSize, 
		tileCount, material, hillHeight, countHills, textureRepeatCount);

	MeshCache->addMesh(name, animatedMesh);

	animatedMesh->drop();
    
	return animatedMesh;
}


//! Adds a terrain mesh to the mesh pool.
IAnimatedMesh* CSceneManager::addTerrainMesh(const c8* name,
	video::IImage* texture, video::IImage* heightmap,
	const core::dimension2d<f32>& stretchSize,
	f32 maxHeight,
	const core::dimension2d<s32>& defaultVertexBlockSize)
{
	if (!name || MeshCache->isMeshLoaded(name))
		return 0;

	IAnimatedMesh* animatedMesh = CGeometryCreator::createTerrainMesh(texture,
		heightmap, stretchSize, maxHeight, getVideoDriver(), defaultVertexBlockSize);

	if (!animatedMesh)
		return 0;

	MeshCache->addMesh(name, animatedMesh);

	animatedMesh->drop();
    
	return animatedMesh;
}



//! Returns the root scene node. This is the scene node wich is parent 
//! of all scene nodes. The root scene node is a special scene node which
//! only exists to manage all scene nodes. It is not rendered and cannot
//! be removed from the scene.
//! \return Returns a pointer to the root scene node.
ISceneNode* CSceneManager::getRootSceneNode()
{
	return this;
}



//! Returns the current active camera.
//! \return The active camera is returned. Note that this can be NULL, if there
//! was no camera created yet.
ICameraSceneNode* CSceneManager::getActiveCamera()
{
	return ActiveCamera;
}



//! Sets the active camera. The previous active camera will be deactivated.
//! \param camera: The new camera which should be active.
void CSceneManager::setActiveCamera(ICameraSceneNode* camera)
{
	if (ActiveCamera)
		ActiveCamera->drop();

	ActiveCamera = camera;

	if (ActiveCamera)
		ActiveCamera->grab();
}



//! renders the node.
void CSceneManager::render()
{
}


//! returns the axis aligned bounding box of this node
const core::aabbox3d<f32>& CSceneManager::getBoundingBox() const
{
	_IRR_DEBUG_BREAK_IF(true) // Bounding Box of Scene Manager wanted.

	// should never be used.
	return *((core::aabbox3d<f32>*)0);
}



//! returns if node is culled
bool CSceneManager::isCulled(ISceneNode* node)
{
	if (!node->getAutomaticCulling())
		return false;

	ICameraSceneNode* cam = getActiveCamera();
	if (!cam)
		return false;

    core::aabbox3d<f32> tbox = node->getBoundingBox();
	node->getAbsoluteTransformation().transformBox(tbox);
	return !(tbox.intersectsWithBox(cam->getViewFrustrum()->boundingBox));

	// This is a slower but more exact version:
	//
	// SViewFrustrum f = *cam->getViewFrustrum();
	// core::matrix4 invTrans;
	// AbsoluteTransformation.getInverse(invTrans);
	// f.transform(invTrans);
	// culled = !(f.boundingBox.intersectsWithBox(Mesh->getBoundingBox()));
}



//! registers a node for rendering it at a specific time.
void CSceneManager::registerNodeForRendering(ISceneNode* node, E_SCENE_NODE_RENDER_PASS time)
{
	switch(time)
	{
	case ESNRP_LIGHT_AND_CAMERA:
		LightAndCameraList.push_back(node);
		break;
	case ESNRP_SKY_BOX:
		SkyBoxList.push_back(node);
		break;
	case ESNRP_SOLID:
		if (!isCulled(node))
			SolidNodeList.push_back(node);
		break;
	case ESNRP_TRANSPARENT:
		if (!isCulled(node))
			TransparentNodeList.push_back(TransparentNodeEntry(node, camTransPos));
		break;
	case ESNRP_AUTOMATIC:
		if (!isCulled(node))
		{
			s32 count = node->getMaterialCount();

			for (s32 i=0; i<count; ++i)
			{
				video::IMaterialRenderer* rnd = 
					Driver->getMaterialRenderer(node->getMaterial(i).MaterialType);
				if (rnd && rnd->isTransparent())
				{
					// register as transparent node
					TransparentNodeEntry e(node, camTransPos);
					TransparentNodeList.push_back(e);
					return;
				}
			}
			
			// not transparent, register as solid
			SolidNodeList.push_back(node);
		}
		break;
	case ESNRP_SHADOW:
		if (!isCulled(node))
			ShadowNodeList.push_back(node);
		break;
	}
}


//! This method is called just before the rendering process of the whole scene.
//! draws all scene nodes
void CSceneManager::drawAll()
{
	if (!Driver)
		return;

	// calculate camera pos.
	camTransPos.set(0,0,0);
	if (ActiveCamera)
		camTransPos = ActiveCamera->getAbsolutePosition();

	// let all nodes register themselfes
	OnPreRender();

	//render lights and cameras

	CurrentRendertime = ESNRP_LIGHT_AND_CAMERA;

	Driver->deleteAllDynamicLights();
	
	u32 i; // new ISO for scoping problem in some compilers

	for (i=0; i<LightAndCameraList.size(); ++i)
		LightAndCameraList[i]->render();

	LightAndCameraList.clear();

	// render skyboxes

	CurrentRendertime = ESNRP_SKY_BOX;

	for (i=0; i<SkyBoxList.size(); ++i)
		SkyBoxList[i]->render();

	SkyBoxList.clear();


	// render default objects

	CurrentRendertime = ESNRP_SOLID;

	SolidNodeList.sort(); // sort by textures

	for (i=0; i<SolidNodeList.size(); ++i)
		SolidNodeList[i].node->render();

	SolidNodeList.clear();

	// render shadows

	CurrentRendertime = ESNRP_SHADOW;

	for (i=0; i<ShadowNodeList.size(); ++i)
		ShadowNodeList[i]->render();

	if (!ShadowNodeList.empty())
		Driver->drawStencilShadow(true,ShadowColor, ShadowColor,
			ShadowColor, ShadowColor);

	ShadowNodeList.clear();

	// render transparent objects.

	CurrentRendertime = ESNRP_TRANSPARENT;

	TransparentNodeList.sort(); // sort by distance from camera

	for (i=0; i<TransparentNodeList.size(); ++i)
		TransparentNodeList[i].node->render();

	TransparentNodeList.clear();

	// do animations and other stuff.
	OnPostRender(os::Timer::getTime());

	clearDeletionList();

	CurrentRendertime = ESNRP_COUNT;
}


//! Sets the color of stencil buffers shadows drawed by the scene manager.
void CSceneManager::setShadowColor(video::SColor color)
{
	ShadowColor = color;
}


//! Returns the current color of shadows.
video::SColor CSceneManager::getShadowColor() const
{
	return ShadowColor;
}



//! creates a rotation animator, which rotates the attached scene node around itself.
ISceneNodeAnimator* CSceneManager::createRotationAnimator(const core::vector3df& rotationPerSecond)
{
	ISceneNodeAnimator* anim = new CSceneNodeAnimatorRotation(os::Timer::getTime(),
		rotationPerSecond);

	return anim;
}



//! creates a fly circle animator, which lets the attached scene node fly around a center.
ISceneNodeAnimator* CSceneManager::createFlyCircleAnimator(const core::vector3df& normal,
														   f32 radius, f32 speed)
{
	ISceneNodeAnimator* anim = new CSceneNodeAnimatorFlyCircle(os::Timer::getTime(), normal,
			radius, speed);
	return anim;
}


//! Creates a fly straight animator, which lets the attached scene node
//! fly or move along a line between two points.
ISceneNodeAnimator* CSceneManager::createFlyStraightAnimator(const core::vector3df& startPoint,
					const core::vector3df& endPoint, u32 timeForWay, bool loop)
{
	ISceneNodeAnimator* anim = new CSceneNodeAnimatorFlyStraight(startPoint,
		endPoint, timeForWay, loop, os::Timer::getTime());

	return anim;
}


//! Creates a texture animator, which switches the textures of the target scene
//! node based on a list of textures.
ISceneNodeAnimator* CSceneManager::createTextureAnimator(const core::array<video::ITexture*>& textures,
	s32 timePerFrame, bool loop)
{
	ISceneNodeAnimator* anim = new CSceneNodeAnimatorTexture(textures,
		timePerFrame, loop, os::Timer::getTime());

	return anim;
}


//! Creates a scene node animator, which deletes the scene node after
//! some time automaticly.
ISceneNodeAnimator* CSceneManager::createDeleteAnimator(u32 when)
{
	return new CSceneNodeAnimatorDelete(this, os::Timer::getTime() + when);
}




//! Creates a special scene node animator for doing automatic collision detection 
//! and response.
ISceneNodeAnimatorCollisionResponse* CSceneManager::createCollisionResponseAnimator(
	ITriangleSelector* world, ISceneNode* sceneNode, const core::vector3df& ellipsoidRadius,
	const core::vector3df& gravityPerSecond,
	const core::vector3df& ellipsoidTranslation, f32 slidingValue)
{
	ISceneNodeAnimatorCollisionResponse* anim = new
		CSceneNodeAnimatorCollisionResponse(this, world, sceneNode, 
			ellipsoidRadius, gravityPerSecond, 
			ellipsoidTranslation, slidingValue);

	return anim;
}


//! Creates a follow spline animator.
ISceneNodeAnimator* CSceneManager::createFollowSplineAnimator(s32 startTime,
	const core::array< core::vector3df >& points,
	f32 speed, f32 tightness)
{
	ISceneNodeAnimator* a = new CSceneNodeAnimatorFollowSpline(startTime, points,
		speed, tightness);
	return a;
}



//! Adds an external mesh loader.
void CSceneManager::addExternalMeshLoader(IMeshLoader* externalLoader)
{
	if (!externalLoader)
		return;

	externalLoader->grab();
	MeshLoaderList.push_back(externalLoader);
}



//! Returns a pointer to the scene collision manager.
ISceneCollisionManager* CSceneManager::getSceneCollisionManager()
{
	return CollisionManager;
}


//! Returns a pointer to the mesh manipulator.
IMeshManipulator* CSceneManager::getMeshManipulator()
{
	return MeshManipulator;
}


//! Creates a simple ITriangleSelector, based on a mesh.
ITriangleSelector* CSceneManager::createTriangleSelector(IMesh* mesh, ISceneNode* node)
{
	if (!mesh || !node)
		return 0;

	return new CTriangleSelector(mesh, node);
}


//! Creates a simple dynamic ITriangleSelector, based on a axis aligned bounding box.
ITriangleSelector* CSceneManager::createTriangleSelectorFromBoundingBox(ISceneNode* node)
{
	if (!node)
		return 0;

	return new CTriangleBBSelector(node);
}


//! Creates a simple ITriangleSelector, based on a mesh.
ITriangleSelector* CSceneManager::createOctTreeTriangleSelector(IMesh* mesh,
																ISceneNode* node,
																s32 minimalPolysPerNode)
{
	if (!mesh || !node)
		return 0;

	return new COctTreeTriangleSelector(mesh, node, minimalPolysPerNode);
}



//! Creates a meta triangle selector.
IMetaTriangleSelector* CSceneManager::createMetaTriangleSelector()
{
	return new CMetaTriangleSelector();
}



//! Creates a triangle selector which can select triangles from a terrain scene node
ITriangleSelector* CSceneManager::createTerrainTriangleSelector(
	ITerrainSceneNode* node, s32 LOD)
{
	return new CTerrainTriangleSelector(node, LOD);
}



//! Adds a scene node to the deletion queue.
void CSceneManager::addToDeletionQueue(ISceneNode* node)
{
	if (!node)
		return;

	node->grab();
	DeletionList.push_back(node);
}


//! clears the deletion list
void CSceneManager::clearDeletionList()
{
	if (DeletionList.empty())
		return;

	for (s32 i=0; i<(s32)DeletionList.size(); ++i)
	{
		DeletionList[i]->remove();
		DeletionList[i]->drop();
	}

	DeletionList.clear();
}	


//! Returns the first scene node with the specified name.
ISceneNode* CSceneManager::getSceneNodeFromName(const wchar_t* name, ISceneNode* start)
{
	if (start == 0)
		start = getRootSceneNode();

	if (!wcscmp(start->getName(),name))
		return start;

	ISceneNode* node = 0;

	const core::list<ISceneNode*>& list = start->getChildren();
	core::list<ISceneNode*>::Iterator it = list.begin();
	for (; it!=list.end(); ++it)
	{
		node = getSceneNodeFromName(name, *it);
		if (node)
			return node;
	}

	return 0;
}


//! Returns the first scene node with the specified id.
ISceneNode* CSceneManager::getSceneNodeFromId(s32 id, ISceneNode* start)
{
	if (start == 0)
		start = getRootSceneNode();

	if (start->getID() == id)
		return start;

	ISceneNode* node = 0;

	const core::list<ISceneNode*>& list = start->getChildren();
	core::list<ISceneNode*>::Iterator it = list.begin();
	for (; it!=list.end(); ++it)
	{
		node = getSceneNodeFromId(id, *it);
		if (node)
			return node;
	}

	return 0;
}


//! Posts an input event to the environment. Usually you do not have to
//! use this method, it is used by the internal engine.
bool CSceneManager::postEventFromUser(SEvent event)
{
	bool ret = false;
	ICameraSceneNode* cam = getActiveCamera();
	if (cam)
		ret = cam->OnEvent(event);

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return ret;
}


//! Removes all children of this scene node
void CSceneManager::removeAll()
{
	ISceneNode::removeAll();
	setActiveCamera(0);
}


//! Clears the whole scene. All scene nodes are removed. 
void CSceneManager::clear()
{
	removeAll();
}


//! Returns interface to the parameters set in this scene.
IStringParameters* CSceneManager::getParameters()
{
	return &Parameters;
}


//! Returns current render pass. 
E_SCENE_NODE_RENDER_PASS CSceneManager::getSceneNodeRenderPass()
{
	return CurrentRendertime;
}



//! Returns an interface to the mesh cache which is shared beween all existing scene managers.
IMeshCache* CSceneManager::getMeshCache()
{
	return MeshCache;
}


//! Creates a new scene manager. 
ISceneManager* CSceneManager::createNewSceneManager()
{
	return new CSceneManager(Driver, FileSystem, CursorControl, MeshCache);
}



// creates a scenemanager
ISceneManager* createSceneManager(video::IVideoDriver* driver, io::IFileSystem* fs,
								  gui::ICursorControl* cursorcontrol)
{
	return new CSceneManager(driver, fs, cursorcontrol);
}



} // end namespace scene
} // end namespace irr

