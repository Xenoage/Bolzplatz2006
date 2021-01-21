%module(directors="1") Jirr
%include "arrays_java.i"
%include "p_arrays.i"
%include "wchar_t.i"
%include "typemaps.i"
%include "pointer_ref.i"
%include "vector3df_vector.i"
%include "carrays.i"
%include "temp_arrays.i"

JAVA_P_ARRAYSOFCLASSES(S3DVERTEXARRAY, void, irr::video::S3DVertex, S3DVertex, getVertexCount ())
JAVA_P_ARRAYS_TYPEMAPS(INDEXARRAY, unsigned short, jint, jintArray, int, Ushort, "[I", getIndexCount ())
TEMPJAVA_P_ARRAYSOFCLASSES(S3DVERTEXARRAY2, void, irr::video::S3DVertex, S3DVertex)

POINTER_REF_TYPEMAP(irr::video::S3DVertex *, S3DVertex)
POINTER_REF_TYPEMAP(irr::scene::ISceneNode *, ISceneNode)
POINTER_REF_TYPEMAP(irr::gui::IGUIElement *, IGUIElement)
POINTER_REF_TYPEMAP(irr::scene::IMesh *, IMesh)
POINTER_REF_TYPEMAP(irr::video::ITexture *, ITexture)
POINTER_REF_TYPEMAP(irr::video::IGPUProgrammingServices *, IGPUProgrammingServices)
POINTER_REF_TYPEMAP(irr::video::IMaterialRenderer *, IMaterialRenderer)

%{
#include <irrlicht.h>
#include <IMeshLoader.h>
#include <IGPUProgrammingServices.h>
#include "jirr.h"
using namespace irr;
using namespace irr::video;
using namespace irr::scene;
using namespace irr::gui;
using namespace irr::core;
using namespace irr::io;

%}

%ignore irr::gui::createSkin;
%ignore irr::io::createWriteFile;
%ignore irr::io::createReadFile;
%ignore irr::io::createLimitReadFile;
%ignore irr::io::createMemoryReadFile;

// 1.0
%ignore irr::io::createIrrXMLReader;
%ignore irr::io::createIrrXMLReaderUTF16;
%ignore irr::io::createIrrXMLReaderUTF32;

//%ignore (IGPUProgrammingServices::addShaderMaterialFromFiles(irr::io::IReadFile*,irr::io::IReadFile*,irr::video::IShaderConstantSetCallBack*,irr::video::E_MATERIAL_TYPE));
//%rename (irr::video::IGPUProgrammingServices::addShaderMaterialFromFiles(irr::io::IReadFile*,irr::io::IReadFile*,irr::video::IShaderConstantSetCallBack*,irr::video::E_MATERIAL_TYPE)) addShaderMaterialFromFilesIoRead;

%include "irrTypes.h"
//%feature("director") irr::scene::ISceneNodeAnimator::animateNode;

// RENAMING OF OPERATORS
%rename(subtractOperator) operator-;
%rename(addOperator) operator+;
%rename(timesOperator) operator*;
%rename(dividedOperator) operator/;

%rename(equalsOperator) operator==;
%rename(notEqualsOperator) operator!=;
%rename(lessThanOrEqualOperator) operator<=;
%rename(lessThanOperator) operator<;
%rename(moreThanOrEqualOperator) operator>=;
%rename(moreThanOperator) operator>;

%ignore operator[];
%ignore operator();
//%rename(parenthesisOperator) operator();

//%ignore operator=;
%rename(assignOperator) operator=;
%rename(assingTimesOperator) operator*=;
%rename(assingMinusOperator) operator+=;
%rename(assingPlusOperator) operator-=;
%rename(assingDivideOperator) operator/=;

%rename(assingIncrement) operator++;
%rename(assingDecrement) operator--;


// RENAMING OF CONST-VARIANTS (and ignoring fields that would generate conflicting getters)
%rename(getMaterialConst) getMaterial() const;
%ignore(*::Material); // Will generate getter (cause duplicate method)
%rename(getBoundingBoxConst) getBoundingBox() const;
%ignore(*::BoundingBox); // Will generate getter (cause duplicate method)
%rename(getVerticesConst) getVertices() const;
%ignore(*::Vertices); // Will generate getter (cause duplicate method)
%rename(getIndicesConst) getIndices() const;
%ignore(*::Indices); // Will generate getter (cause duplicate method)
%rename(getLastConst) getLast() const;
%ignore(*::log(const c8*, const c8*, ELOG_LEVEL));
%ignore(*::log(const c8*, ELOG_LEVEL));
%ignore(*::log(const c8*, const c8*));
%ignore(*::log(const c8*));


// OAKHACK (just make stuff WORK)
//%ignore irr::core::list::begin();


// The include of irrlicht header-files
%include "irrMath.h"
%include "irrArray.h" 
%include "vector2d.h"
%include "vector3d.h"
%include "plane3d.h"
%include "line3d.h"
%include "aabbox3d.h"
%include "heapsort.h"
%include "dimension2d.h"
%include "irrString.h"
%include "line2d.h"
%include "irrList.h"
%include "matrix4.h"
%include "triangle3d.h"
%include "position2d.h"
%include "quaternion.h"
%include "rect.h"
%include "Keycodes.h"
%include "SColor.h"
%include "SLight.h"
%include "SKeyMap.h"
%include "S3DVertex.h"
%include "EDriverTypes.h"
%include "IUnknown.h"
%include "IImage.h"
%include "ITexture.h"
%include "SMaterial.h"

%include "irrXML.h"
%include "IXMLReader.h"
%include "IXMLWriter.h"

%apply void *S3DVERTEXARRAY { void * };
%apply unsigned short *INDEXARRAY { unsigned short * };
%include "IMeshBuffer.h" // IUnknown.h SMaterial.h array.h aabbox3d.h S3DVertex.h
%clear void *;
%clear unsigned short *;
%include "IMesh.h" // IUnknown.h IMeshBuffer.h
//%apply irr::scene::IMesh *& INPUT {irr::scene::IMesh *&};
//%apply irr::scene::IMesh *& OUTPUT {irr::scene::IMesh *&};
//%clear irr::scene::IMesh *&;
%include "IAnimatedMesh.h" // IUnknown.h IMesh.h matrix4.h
%include "IAnimatedMeshMD2.h" // IAnimatedMesh.h
%include "IAnimatedMeshMS3D.h" // IAnimatedMesh.h
%include "IAnimatedMeshX.h" // IAnimatedMesh.h
%include "ISceneNodeAnimator.h" // IUnknown.h vector3d.h
%include "ITriangleSelector.h" // IUnknown triangle3d.h aabbox3d.h matrix4.h

%feature("director") irr::scene::ISceneNode;
%feature("director") irr::scene::ISceneNode::OnPreRender;
%feature("director") irr::scene::ISceneNode::OnPostRender;
%feature("director") irr::scene::ISceneNode::render;
%feature("director") irr::scene::ISceneNode::getBoundingBox;
%feature("director") irr::scene::ISceneNode::getMaterialCount;
%feature("director") irr::scene::ISceneNode::getMaterial;
%feature("nodirector") irr::scene::ISceneNode::setName;
%include "ISceneNode.h" // IUnknown.h ISceneNodeAnimator.h ITriangleSelector.h SMaterial.h irrString.h aabbox3d.h matrix4.h irrList.h

//%apply irr::scene::ISceneNode *& INPUT {irr::scene::ISceneNode *&};
//%clear irr::scene::ISceneNode *&;
%include "IShadowVolumeSceneNode.h"; // ISceneNode.h IMesh.h
%include "IAnimatedMeshSceneNode.h"; // ISceneNode.h IAnimatedMeshMD2.h IShadowVolumeSceneNode.h
//%include "ITextSceneNode.h" 
//%include "IBspTreeSceneNode.h" // ISceneNode.h IMesh.h
%include "ILogger.h" // IUnknown.h

%feature("director") irr::IEventReceiver; 
%feature("director") irr::IEventReceiver::OnEvent; 
%include "IEventReceiver.h" // ILogger.h position2d.h Keycodes.h

%include "SViewFrustrum.h" // plane3d.h vector3d.h aabbox3d.h matrix4.h
%include "ICameraSceneNode.h" // SViewFrustrum.h ISceneNode.h IEventReceiver.h
%include "IDummyTransformationSceneNode.h" // ISceneNode.h
%include "IFileList.h" // IUnknown.h
%include "IFileSystem.h" // IUnknown.h
%include "IGUIElement.h" // IUnknown.h irrList.h rect.h irrString.h IEventReceiver.h
//%apply irr::gui::IGUIElement *& INPUT {irr::gui::IGUIElement *&};

//%clear irr::gui::IGUIElement *&;
%include "IGUIButton.h" // IGUIElement.h
%include "IGUICheckBox.h" // IGUIElement.h
%include "IGUIContextMenu.h" // IGUIElement.h
%include "IGUIEditBox.h" // IGUIElement.h SColor.h
%include "IGUIWindow.h" // IGUIButton.h
%include "IGUISkin.h" // IUnknown.h SColor.h IGUISkin.h
%include "IGUIEnvironment.h" // rect.h IUnknown.h IEventReceiver.h irrTypes.h IGUIWindow.h
%include "IGUIFileOpenDialog.h" // IGUIElement.h
%include "IGUIFont.h" // IUnknown.h irrTypes.h SColor.h
%include "IGUIImage.h" // IGUIElement.h ITexture.h
%include "IGUIInOutFader.h" // IGUIElement.h SColor.h
%include "IGUIListBox.h" // IGUIElement.h irrTypes.h
%include "IGUIMeshViewer.h" // IGUIElement.h
%include "IGUIScrollBar.h" // IGUIElement.h
%include "IGUIStaticText.h" // IGUIElement.h SColor.h
%include "IGUITabControl.h" // IGUIElement.h SColor.h
%include "IGUIComboBox.h"
%include "IGUIToolbar.h"
%include "ILightSceneNode.h" // ISceneNode.h SLight.h
%include "IMeshManipulator.h" // IUnknown.h vector3d.h IMeshBuffer.h aabbox3d.h
%include "IMetaTriangleSelector.h" // ITriangleSelector.h
%include "IReadFile.h" // IUnknown.h
%include "IImageLoader.h" // IUnknown.h IReadFile.h IImage.h


%feature("director") irr::video::IShaderConstantSetCallBack;
%feature("director") irr::video::IShaderConstantSetCallBack::OnSetConstants;

%include "IGPUProgrammingServices.h"

%apply float[] {float *};
%include "IMaterialRenderer.h" // 0.9
%clear float *;

//%apply void *S3DVERTEXARRAY2 { S3DVertex * };
%apply unsigned short[] {unsigned short *};
//%apply irr::video::S3DVertex[] {irr::video::S3DVertex *};
//%apply void * irr::video::S3DVertex { void * };
//virtual void irr::video::IVideoDriver::drawIndexedTriangleList(irr::video::S3DVertex *INPUT[], s32 INPUT, const u16 *INPUT, s32 INPUT);
%include "IVideoDriver.h" // rect.h SColor.h ITexture.h matrix4.h dimension2d.h position2d.h IReadFile.h SMaterial.h SLight.h IImageLoader.h triangle3d.h

//%clear irr::video::S3DVertex *;
%clear unsigned short *;
//%clear S3DVertex *;
//%extend irr::SEvent {
//   int getKeyInputChar() {return self->KeyInput.Char;};
//   bool isKeyInputPressedDown() {return self->KeyInput.PressedDown;};
//   EMOUSE_INPUT_EVENT getMouseInputEvent() {return self->MouseInput.Event;};
//};
%include "IMeshLoader.h" // IUnknown.h IReadFile.h IAnimatedMesh.h
%include "ISceneManager.h" // array.h IUnknown.h vector3d.h dimension2d.h SColor.h SMaterial.h IEventReceiver.h
%include "ICursorControl.h" // position2d.h IUnknown.h irrTypes.h
%include "IVideoModeList.h" // IUnknown.h dimension2d.h
%include "ITimer.h" // IUnknown.h
%include "IOSOperator.h" // IUnknown.h
%include "IrrlichtDevice.h" // irrTypes.h IUnknown.h dimension2d.h IFileSystem.h IVideoDriver.h EDriverTypes.h IGUIEnvironment.h IEventReceiver.h ISceneManager.h ICursorControl.h IVideoModeList.h ITimer.h ILogger.h IOSOperator.h
%include "ISceneCollisionManager.h" // IUnknown.h vector3d.h triangle3d.h position2d.h line3d.h
%include "ISceneNodeAnimatorCollisionResponse.h" // ISceneNode.h
%include "SParticle.h" // vector3d.h SColor.h
%include "IParticleEmitter.h" // IUnknown.h SParticle.h
%include "IParticleAffector.h" // IUnknown.h SParticle.h
%include "IParticleSystemSceneNode.h" // ISceneNode.h IParticleEmitter.h IParticleAffector.h dimension2d.h
%include "ITerrainSceneNode.h" // ISceneNode.h vector2d.h
%include "ITextSceneNode.h" // 1.0 (0.9)
%include "IBillboardSceneNode.h" // ISceneNode.h
%include "SAnimatedMesh.h" // IAnimtedMesh.h
%include "SMesh.h" // IMeshBuffer.h IMesh.h
%include "IWriteFile.h"

//%feature("director") irr::SEventQueue; 
%include "jirr.h"

%apply void *S3DVERTEXARRAY { void * };
%apply unsigned short *INDEXARRAY { unsigned short * };
%include "SMeshBuffer.h" // array.h IMeshBuffer.h
%include "SMeshBufferLightMap.h" // array.h IMeshBuffer.h
%include "SMeshBufferTangents.h" // array.h IMeshBuffer.h // 0.9
%clear void *;
%clear unsigned short *;

%include "SIrrCreationParameters.h" // 1.0

%include "IShaderConstantSetCallBack.h" // 0.9
%include "IStringParameters.h" // 0.9


//OAK INSERTED THIS
%include "irrlicht.h"



/*
namespace irr
{
	// IRRLICHT_API
	IrrlichtDevice* createDevice(
		video::EDriverType deviceType = video::EDT_SOFTWARE, 
		const core::dimension2d<s32>& windowSize = core::dimension2d<s32>(640,480),
		u32 bits = 16,
		bool fullscreen = false,
		bool stencilbuffer=false,
		IEventReceiver* receiver = 0,
		const wchar_t* sdk_version_do_not_use = IRRLICHT_SDK_VERSION);
}
*/

// Templates (to make sure we get often used classes)
%template(vector2df) irr::core::vector2d<f32>;
%template(vector2di) irr::core::vector2d<s32>;
//%template(vector2dfarray) irr::core::array<irr::core::vector2df>;
%template(vector3df) irr::core::vector3d<f32>;
//%template(vector3di) irr::core::vector3d<s32>;
%template(plane3df) irr::core::plane3d<f32>;
//%template(plane3di) irr::core::plane3d<s32>;
%template(line3df) irr::core::line3d<f32>;
//%template(line3di) irr::core::line3d<s32>;
%template(aabbox3df) irr::core::aabbox3d<f32>;
//%template(aabbox3di) irr::core::aabbox3d<s32>;

//template(vector3df) bool operator<(vector3df other) {return X<other.X && Y<other.Y && Z<other.Z;}
/*%header %{
irr::core::vector3d<T>
{
	bool operator< (const irr::core::vector3d<T> &other) const { return X<other.X && Y<other.Y && Z<other.Z;};
}
%}*/
/*%include %{
irr::core::vector3df
{
	bool operator< (const vector3df &other) const { return X<other.X && Y<other.Y && Z<other.Z;};
}
%}*/
/*%inline %{
vector3df
{
	bool operator< (const vector3df &other) const { return X<other.X && Y<other.Y && Z<other.Z;};
}
%}*/
%template(vector3dfarray) irr::core::array<vector3df>;

//%template(vector3dfarray) irr::core::array<irr::core::vector3df>;
%template(dimension2df) irr::core::dimension2d<f32>;
%template(dimension2di) irr::core::dimension2d<s32>;
//%template(line2df) irr::core::line2d<f32>;
//%template(line2di) irr::core::line2d<s32>;
%template(triangle3df) irr::core::triangle3d<f32>;
//%template(triangle3di) irr::core::triangle3d<s32>;
//%template(position2df) irr::core::position2d<f32>;
%template(position2di) irr::core::position2d<s32>;
//%template(rectf) irr::core::rect<f32>;
%template(recti) irr::core::rect<s32>;
%template(ITextureArray) irr::core::array<irr::video::ITexture *>;
//%template(ISceneNodeList) irr::core::list<irr::scene::ISceneNode *>;
%template(IMeshArray) irr::core::array<irr::scene::IMesh *>;
//%template(IGUIElementList) irr::core::list<irr::gui::IGUIElement *>;
//%template(IGUIElementListIterator) irr::core::Iterator<irr::gui::IGUIElement *>;
//%template(ISceneNodeListIterator) irr::core::Iterator<irr::scene::ISceneNode *>;

//typedef std::list<T> irr::core::list<T>

%template(IXMLReader) irr::io::IIrrXMLReader<wchar_t, irr::IUnknown>;



// Code extensions

%extend irr::video::SMaterial {
   bool isWireframe() {return self->Wireframe;};
   bool isFlag(int material_flag) {return self->Flags[material_flag];};
   void setFlag(int material_flag, bool value) {self->Flags[material_flag] = value;};
};

%extend irr::SEvent {
   int getKeyInputChar() {return self->KeyInput.Char;};
   //ANDI EKEY_CODE getKeyInputKey() {return self->KeyInput.Key;};
   int getKeyInputKey() {return self->KeyInput.Key;};
   bool isKeyInputPressedDown() {return self->KeyInput.PressedDown;};
   bool isKeyInputShift() {return self->KeyInput.Shift;};
   bool isKeyInputCtrl() {return self->KeyInput.Control;};
   
   s32 getMouseInputX() {return self->MouseInput.X;};
   s32 getMouseInputY() {return self->MouseInput.Y;};   
   f32 getMouseInputWheel() {return self->MouseInput.Wheel;};   
   EMOUSE_INPUT_EVENT getMouseInputEvent() {return self->MouseInput.Event;};
   
   irr::gui::IGUIElement* getGUIEventCaller() {return self->GUIEvent.Caller;};
   //irr::gui::IGUIScrollBar* getGUIEventCallerScrollBar() {return (irr::gui::IGUIScrollBar*)(self->GUIEvent.Caller);};
   gui::EGUI_EVENT_TYPE getGUIEventType() {return self->GUIEvent.EventType;};   
   
	const c8* getLogEventText() {return self->LogEvent.Text;};
	ELOG_LEVEL getLogEventLevel() {return self->LogEvent.Level;};

	s32 getUserEventData1() {return self->UserEvent.UserData1;};
	s32 getUserEventData2() {return self->UserEvent.UserData2;};
	f32 getUserEventData3() {return self->UserEvent.UserData3;};
};

%extend irr::video::IVideoDriver {
	//nosuccess
	void drawIndexedTriangleList(bool a, S3DVertex vertices[],
		s32 vertexCount, const u16* indexList, s32 triangleCount)
			{self->drawIndexedTriangleList(vertices, vertexCount,indexList,triangleCount);};

	// silly example
	void drawIndexedTriangleList(int a, S3DVertex vertices[],
		s32 vertexCount, const u16* indexList, s32 triangleCount)
	{
		S3DVertex Vertices[4];
		
		Vertices[0] = video::S3DVertex(0,0,10, 1,1,0, video::SColor(255,0,255,255), 0, 1);
		Vertices[1] = video::S3DVertex(10,0,-10, 1,0,0, video::SColor(255,255,0,255), 1, 1);
		Vertices[2] = video::S3DVertex(0,20,0, 0,1,1, video::SColor(255,255,255,0), 1, 0);
		Vertices[3] = video::S3DVertex(-10,0,-10, 0,0,1, video::SColor(255,0,255,0), 0, 0);
		self->drawIndexedTriangleList(Vertices, vertexCount, indexList, triangleCount);
	};

	void drawIndexedTriangleList(float pos[], float normal[], int colors[], float tcoords[],
		s32 vertexCount, const u16* indexList, s32 triangleCount)
	{
		S3DVertex Vertices[vertexCount];
		
		for (int i = 0; i < vertexCount; i++)
		{
			Vertices[i] = video::S3DVertex(pos[i*3], pos[i*3+1], pos[i*3+2],
													 normal[i*3], normal[i*3+1], normal[i*3+2],
													 video::SColor(colors[i*4], colors[i*4+1], colors[i*4+2], colors[i*4+3]),
													 tcoords[i*3], tcoords[i*3+1]);
		}
		
		self->drawIndexedTriangleList(Vertices, vertexCount, indexList, triangleCount);
	};

	//nosuccess
	S3DVertex* createVertices(int length)
	{
		S3DVertex *array = new S3DVertex[length];
		return array;
	};
	//nosuccess
	void addToVertices(S3DVertex *vertices, S3DVertex *vertex, int pos)
	{
		S3DVertex *array = vertices;
      *(void **)&array[pos] = vertex;
	};
};

%extend irr::scene::ISceneManager {
	//nosuccess
	SKeyMap* createKeyMaps(int length)
	{
		SKeyMap *keyMaps = new SKeyMap[length];
		//return (SKeyMap*)&keyMaps[0];
		return keyMaps;
	};
	//nosuccess
	void addToKeyMaps(SKeyMap *maps, SKeyMap *keyMap, int pos)
	{
		((SKeyMap)maps[pos]).Action = keyMap->Action;
		((SKeyMap)maps[pos]).KeyCode = keyMap->KeyCode;
		//maps[pos] = keyMap;
		//SKeyMap **myArray = *(void**)maps;
		//myArray[pos] = keyMap;
		
		//*(SKeyMap **)(array[pos]) = keyMap;
		//SKeyMap *array = maps;
	   //*(void **)&array[pos] = keyMap;
	};
	//nosuccess
	SKeyMap * getCustomDefaultKeymap()
	{
		SKeyMap *maps = new SKeyMap[8];
		((SKeyMap)maps[0]).Action = EKA_MOVE_FORWARD;
		((SKeyMap)maps[0]).KeyCode = KEY_UP;
		((SKeyMap)maps[1]).Action = EKA_MOVE_FORWARD;
		((SKeyMap)maps[1]).KeyCode = KEY_KEY_W;
        
		((SKeyMap)maps[2]).Action = EKA_MOVE_BACKWARD;
		((SKeyMap)maps[2]).KeyCode = KEY_DOWN;
		((SKeyMap)maps[3]).Action = EKA_MOVE_BACKWARD;
		((SKeyMap)maps[3]).KeyCode = KEY_KEY_S;
        
		((SKeyMap)maps[4]).Action = EKA_STRAFE_LEFT;
		((SKeyMap)maps[4]).KeyCode = KEY_LEFT;
		((SKeyMap)maps[5]).Action = EKA_STRAFE_LEFT;
		((SKeyMap)maps[5]).KeyCode = KEY_KEY_A;
        
		((SKeyMap)maps[6]).Action = EKA_STRAFE_RIGHT;
		((SKeyMap)maps[6]).KeyCode = KEY_RIGHT;
		((SKeyMap)maps[7]).Action = EKA_STRAFE_RIGHT;
		((SKeyMap)maps[7]).KeyCode = KEY_KEY_D;
		
		return (SKeyMap *)&maps[0];
	};


			
			
	ICameraSceneNode* addCameraSceneNodeFPS(ISceneNode* parent,
														 f32 rotateSpeed,
														 f32 moveSpeed,
														 s32 id,
														 int actions[], int keyCodes[], s32 keyMapSize)
	{
		SKeyMap maps[keyMapSize];
		for (int i = 0; i < keyMapSize; i++)
		{
			maps[i].Action = (irr::EKEY_ACTION)actions[i];
			maps[i].KeyCode = (irr::EKEY_CODE)keyCodes[i];
		}	
			
		return self->addCameraSceneNodeFPS(parent, rotateSpeed, moveSpeed, id, maps, keyMapSize);
	};	
}

%extend irr::scene::ISceneNode {
	void setMaterialTypeByInt(s32 type)
	{
		self->setMaterialType((E_MATERIAL_TYPE)type);
	}
	
	void OnPreRenderFromJava()
	{
		// no directors for this method
		self->ISceneNode::OnPreRender();
	};
	
	//nosuccess
	irr::video::S3DVertex * getDefaultVertices()
	{
		S3DVertex Vertices[4];
		
		Vertices[0] = video::S3DVertex(0,0,10, 1,1,0, video::SColor(255,0,255,255), 0, 1);
		Vertices[1] = video::S3DVertex(10,0,-10, 1,0,0, video::SColor(255,255,0,255), 1, 1);
		Vertices[2] = video::S3DVertex(0,20,0, 0,1,1, video::SColor(255,255,255,0), 1, 0);
		Vertices[3] = video::S3DVertex(-10,0,-10, 0,0,1, video::SColor(255,0,255,0), 0, 0);
		
		return Vertices;
	}
}
		
%extend irr::core::matrix4 {
	irr::core::matrix4  multiply(irr::core::matrix4 *matrix)
	{
		return ((irr::core::matrix4 const *)self)->operator *((irr::core::matrix4 const &)*matrix);
	};	
}
	