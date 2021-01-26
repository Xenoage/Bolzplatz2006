%module(directors="1") Jirr
%include "arrays_java.i"
%include "p_arrays.i"
%include "wchar_t.i"
%include "typemaps.i"
%include "pointer_ref.i"
%include "vector3df_vector.i"
%include "carrays.i"
%include "temp_arrays.i"

//%include "swigwarnings.swg"
%define SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG    "470:Thread/reentrant unsafe wrapping, consider returning by value instead." %enddef

// java 1.5 enums - otherwise patching cpp will be like ....
%include "enums.swg"
%javaconst(1);
%javaconstvalue("0x80000000l") F32_SIGN_BIT;
%javaconstvalue("0x7FFFFFFFl") F32_EXPON_MANTISSA;
%javaconstvalue("E_TRANSFORMATION_STATE.ETS_PROJECTION.swigValue()+1") ETS_VIEW_PROJECTION_3;
%javaconstvalue("EDS_BBOX.swigValue()|EDS_NORMALS.swigValue()|EDS_SKELETON.swigValue()|EDS_MESH_WIRE_OVERLAY.swigValue()") EDS_FULL;
//%javaconstvalue("{0.f,0.1f,0.01f,0.001f,0.0001f,0.00001f,0.000001f,0.0000001f,0.00000001f,0.000000001f,0.0000000001f,0.00000000001f,0.000000000001f,0.0000000000001f,0.00000000000001f,0.000000000000001f};") float fast_atof_table[];

JAVA_P_ARRAYSOFCLASSES(S3DVERTEXARRAY, void, irr::video::S3DVertex, S3DVertex, getVertexCount ())
//JAVA_P_ARRAYSOFCLASSES(S3DVERTEXIRRARRAY, void, irr::video::S3DVertex, S3DVertex, getVertexCount ())
JAVA_P_ARRAYS_TYPEMAPS(INDEXARRAY, unsigned short, jint, jintArray, int, Ushort, "[I", getIndexCount ())
//TEMPJAVA_P_ARRAYSOFCLASSES(S3DVERTEXARRAY2, void, irr::video::S3DVertex, S3DVertex)
JAVA_ARRAYSOFCLASSES(irr::video::S3DVertex)
JAVA_ARRAYSOFCLASSES(irr::video::S3DVertex2TCoords)
JAVA_ARRAYSOFCLASSES(irr::video::S3DVertexTangents)
JAVA_ARRAYSOFCLASSES(irr::video::SColor)
JAVA_ARRAYSOFCLASSES(irr::SKeyMap)
//JAVA_ARRAYSOFCLASSES(irr::core::matrix4)

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
#include "IGUIElementFactory.h"
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

%rename(assignOperator) operator=;
%rename(assignTimesOperator) operator*=;
%rename(assignMinusOperator) operator-=;
%rename(assignPlusOperator) operator+=;
%rename(assignDivideOperator) operator/=;

%rename(assignIncrement) operator++;
%rename(assignDecrement) operator--;

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
%rename(getF32PointerConst) pointer() const; // 1.3
%rename(getTextureMatrixConst) getTextureMatrix(u32 i) const; // 1.3
%rename(getCreateableGUIElementTypeNameByType) getCreateableGUIElementTypeName(irr::gui::EGUI_ELEMENT_TYPE type); // 1.3
%rename(getCreateableSceneNodeTypeNameByType)  getCreateableSceneNodeTypeName(irr::scene::ESCENE_NODE_TYPE type) const; // 1.3
%rename(addString_C8_WChar)  addString(const c8* attributeName, const wchar_t* value); // 1.3
%rename(setAttribute_C8_WChar)  setAttribute(const c8* attributeName, const wchar_t* value); // 1.3
%rename(setAttribute_C8_WChar)  setAttribute(const c8* attributeName, const wchar_t* value); // 1.3
%rename(setAttribute_S32_WChar) setAttribute(s32 index, const wchar_t* value); // 1.3
%rename(setAttribute_S32_C8) setAttribute(s32 index, const c8* value); // 1.3

// ignore 1.3 enums
%ignore(*::IdentityMatrix);
// 1.3 missing references
%ignore irr::video::SColorHSL::setfromRGB;
%ignore irr::core::matrix4::operator=(const f32 scalar);

// The include of irrlicht header-files
%include "irrTypes.h"

//todo: breaks enums in plane3d and matrix4//
//%include "IrrMap.h" // 1.3

%include "EGUIElementTypes.h" // 1.3
%include "IrrCompileConfig.h" // 1.2
%include "irrMath.h"

%typemap(out) float []
%{$result = SWIG_JavaArrayOutFloat(jenv, $1, 16); %}
%include "fast_atof.h"
%typemap(out) float []
%{$result = SWIG_JavaArrayOutFloat(jenv, $1, FillMeInAsSizeCannotBeDeterminedAutomatically); %}

%include "irrArray.h" 
%include "vector3d.h"
%include "vector2d.h"

%include "plane3d.h"

%include "line3d.h"
%include "aabbox3d.h"
%include "heapsort.h"
%include "dimension2d.h"
%include "irrString.h"
%include "coreutil.h" // 1.3
%include "line2d.h"
%include "irrList.h"
%include "triangle3d.h"
%include "position2d.h"
%include "rect.h"

//%apply float[] { float * };
//%apply irr::core::matrix4[] {irr::core::matrix4 *};
%typemap(out) float []
%{$result = SWIG_JavaArrayOutFloat(jenv, $1, 16); %}
%apply irr::f32[] {irr::f32 *};
%include "matrix4.h"
%clear irr::f32 *;
%typemap(out) float []
%{$result = SWIG_JavaArrayOutFloat(jenv, $1, FillMeInAsSizeCannotBeDeterminedAutomatically); %}
//clear irr::core::matrix4 *;
//clear float *;

%include "quaternion.h"
%include "Keycodes.h"
%include "SColor.h"
%include "SLight.h"
%include "SKeyMap.h"
%include "S3DVertex.h"
%include "EDriverTypes.h"
%include "IUnknown.h"
%include "ILogger.h" // IUnknown.h
%include "IImage.h"
%include "ITexture.h"
%include "SMaterial.h"
%include "SceneParameters.h"
%include "IGUIElementFactory.h" // 1.3

%include "irrXML.h"
%include "IXMLReader.h"
%include "IXMLWriter.h"

%apply void *S3DVERTEXARRAY { void * };
%apply unsigned short *INDEXARRAY { unsigned short * };
%include "IMeshBuffer.h" // IUnknown.h SMaterial.h array.h aabbox3d.h S3DVertex.h
%clear unsigned short *;
%clear void *;

%include "IMesh.h" // IUnknown.h IMeshBuffer.h

// 1.2
%include "ETerrainElements.h"
%include "ESceneNodeTypes.h"

// 1.1
%include "IMeshCache.h"
// 1.1
%include "IAttributes.h"
%include "IAttributeExchangingObject.h"

%include "IAnimatedMesh.h" // IUnknown.h IMesh.h matrix4.h
%include "IAnimatedMeshB3d.h" // IAnimatedMesh.h
%include "IAnimatedMeshMD2.h" // IAnimatedMesh.h
%include "IAnimatedMeshMS3D.h" // IAnimatedMesh.h
%include "ISceneNodeAnimator.h" // IUnknown.h vector3d.h
%include "ITriangleSelector.h" // IUnknown triangle3d.h aabbox3d.h matrix4.h

%feature("director") irr::IEventReceiver; 
%feature("director") irr::IEventReceiver::OnEvent; 
%include "IEventReceiver.h" // ILogger.h position2d.h Keycodes.h

%feature("director") irr::scene::ISceneNode;
%feature("director") irr::scene::ISceneNode::OnRegisterSceneNode;
%feature("director") irr::scene::ISceneNode::OnAnimate;
%feature("director") irr::scene::ISceneNode::render;
%feature("director") irr::scene::ISceneNode::getBoundingBox;
%feature("director") irr::scene::ISceneNode::getMaterialCount;

%feature("director") irr::scene::ISceneNode::OnReadUserData;
%feature("director") irr::scene::ISceneNode::createUserData;
%feature("nodirector") irr::scene::ISceneNode::setName;
%feature("nodirector") irr::scene::ISceneNode::getName;

%feature("nodirector") irr::scene::ISceneNode::getMaterial;
%feature("nodirector") irr::scene::ISceneNode::getTriangleSelector;
%apply irr::SKeyMap[] { irr::SKeyMap * };
%include "ISceneManager.h" //irrArray.h, IUnknown.h vector3d.h dimension2d.h SColor.h  SMaterial.h IEventReceiver.h ETerrainElements.h ESceneNodeTypes.h SceneParameters.h
%clear irr::SKeyMap *;
%include "ISceneNode.h" // IUnknown.h ISceneNodeAnimator.h ITriangleSelector.h SMaterial.h irrString.h aabbox3d.h matrix4.h irrList.h
%include "IAnimatedMeshX.h" // IAnimatedMesh.h

// 1.1
%include "IMeshSceneNode.h"

%include "IShadowVolumeSceneNode.h"; // ISceneNode.h IMesh.h
%include "IAnimatedMeshSceneNode.h"; // ISceneNode.h IAnimatedMeshMD2.h IShadowVolumeSceneNode.h

%include "SViewFrustum.h" // plane3d.h vector3d.h aabbox3d.h matrix4.h
%include "ICameraSceneNode.h" // SViewFrustrum.h ISceneNode.h IEventReceiver.h
%include "IDummyTransformationSceneNode.h" // ISceneNode.h
%include "IFileList.h" // IUnknown.h
%include "IFileSystem.h" // IUnknown.h
%include "IGUIElement.h" // IUnknown.h irrList.h rect.h irrString.h IEventReceiver.h

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
%include "IGUIColorSelectDialog.h"
%include "IGUISpriteBank.h" // 1.3
%include "IGUIFontBitmap.h" // 1.3

%include "ILightSceneNode.h" // ISceneNode.h SLight.h
%include "IMeshManipulator.h" // IUnknown.h vector3d.h IMeshBuffer.h aabbox3d.h
%include "IMetaTriangleSelector.h" // ITriangleSelector.h
%include "IReadFile.h" // IUnknown.h
%include "IImageLoader.h" // IUnknown.h IReadFile.h IImage.h

%feature("director") irr::video::IShaderConstantSetCallBack;
%feature("director") irr::video::IShaderConstantSetCallBack::OnSetConstants;

%include "IGPUProgrammingServices.h"

%apply float[] {float *};
//1.1
%include "IMaterialRendererServices.h"
%include "IMaterialRenderer.h" // 0.9
%clear float *;

%apply unsigned short[] {unsigned short *};
%apply irr::video::S3DVertexTangents[] {irr::video::S3DVertexTangents *};
%apply irr::video::S3DVertex2TCoords[] {irr::video::S3DVertex2TCoords *};
%apply irr::video::S3DVertex[] {irr::video::S3DVertex *};
%apply irr::video::SColor[] {irr::video::SColor *};
%include "IVideoDriver.h" // rect.h SColor.h ITexture.h matrix4.h dimension2d.h position2d.h IReadFile.h SMaterial.h SLight.h IImageLoader.h triangle3d.h
%clear irr::video::SColor *;
%clear const irr::video::S3DVertex *;
%clear const irr::video::S3DVertex2TCoords *;
%clear const irr::video::S3DVertexTangents *;
%clear unsigned short *;

%include "IMeshLoader.h" // IUnknown.h IReadFile.h IAnimatedMesh.h
%include "ISceneManager.h" // array.h IUnknown.h vector3d.h dimension2d.h SColor.h SMaterial.h IEventReceiver.h
%include "ICursorControl.h" // position2d.h position2f.h IUnknown.h irrTypes.h
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

// 1.1
%include "ISceneNodeFactory.h"
%include "ISceneNodeAnimatorFactory.h"

// 1.3
%include "IQ3Shader.h"
%include "IAnimatedMeshMD3.h"

//// 

%typemap(out) signed char []
%{$result = SWIG_JavaArrayOutSchar(jenv, $1, arg2.Height*arg2.Width); %}
%typemap(out) unsigned int []
%{$result = SWIG_JavaArrayOutUint(jenv, $1, arg2.Height*arg2.Width); %}
%typemap(out) int []
%{$result = SWIG_JavaArrayOutInt(jenv, $1, arg2.Height*arg2.Width); %}
%apply irr::video::SColor[] {irr::video::SColor *};
%apply signed char[] {signed char *};
%apply unsigned int[] {unsigned int *};
%apply int[] {int *};
%include "jirr.h"
%clear signed char*;
%clear unsigned int*;
%clear  int*;
%clear irr::video::SColor *;
%typemap(out) signed char []
%{$result = SWIG_JavaArrayOutSchar(jenv, $1, FillMeInAsSizeCannotBeDeterminedAutomatically); %}
%typemap(out) unsigned int []
%{$result = SWIG_JavaArrayOutUint(jenv, $1, FillMeInAsSizeCannotBeDeterminedAutomatically); %}
%typemap(out) int []
%{$result = SWIG_JavaArrayOutInt(jenv, $1, FillMeInAsSizeCannotBeDeterminedAutomatically); %}

%apply void *S3DVERTEXARRAY { void * };
%apply unsigned short *INDEXARRAY { unsigned short * };
%include "SMeshBuffer.h" // array.h IMeshBuffer.h
%include "SMeshBufferLightMap.h" // array.h IMeshBuffer.h
%include "SMeshBufferTangents.h" // array.h IMeshBuffer.h // 0.9
%clear unsigned short *;
%clear void *;

%include "SIrrCreationParameters.h" // 1.0

%include "IShaderConstantSetCallBack.h" // 0.9

// 1.1
%feature("director") irr::video::ISceneUserDataSerializer;
%feature("director") irr::video::ISceneUserDataSerializer::OnReadUserData;
%feature("director") irr::video::ISceneUserDataSerializer::createUserData;


//OAK INSERTED THIS
%include "irrlicht.h"


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

%template(vector3dfarray) irr::core::array<vector3df>;

//%template(EIntersectionRelation3DMap) irr::core::map<EIntersectionRelation3D, EIntersectionRelation3D>; // 1.3

%template(dimension2df) irr::core::dimension2d<f32>;
%template(dimension2di) irr::core::dimension2d<s32>;
//%template(line2df) irr::core::line2d<f32>;
//%template(line2di) irr::core::line2d<s32>;
%template(triangle3df) irr::core::triangle3d<f32>;
//%template(triangle3di) irr::core::triangle3d<s32>;
%template(position2df) irr::core::position2d<f32>;
%template(position2di) irr::core::position2d<s32>;
//%template(rectf) irr::core::rect<f32>;
%template(recti) irr::core::rect<s32>;
%template(ITextureArray) irr::core::array<irr::video::ITexture *>;
//%template(ISceneNodeList) irr::core::list<irr::scene::ISceneNode *>;
%template(IMeshArray) irr::core::array<irr::scene::IMesh *>;
//%template(S3DVertexCoreArray2) irr::core::array<irr::video::S3DVertex *>;
%ignore irr::core::array<irr::video::S3DVertex>::binary_search;
%ignore irr::core::array<irr::video::S3DVertex>::binary_search_const;
%ignore irr::core::array<irr::video::S3DVertex>::linear_search;
%ignore irr::core::array<irr::video::S3DVertex>::sort;
%template(S3DVertexCoreArray) irr::core::array<irr::video::S3DVertex>;
%template(u16Array) irr::core::array<u16>;
//%template(IGUIElementList) irr::core::list<irr::gui::IGUIElement *>;
//%template(IGUIElementListIterator) irr::core::Iterator<irr::gui::IGUIElement *>;
//%template(ISceneNodeListIterator) irr::core::Iterator<irr::scene::ISceneNode *>;

%template(IXMLReader) irr::io::IIrrXMLReader<wchar_t, irr::IUnknown>;


// Code extensions

%extend irr::video::S3DVertex {
	bool operator < (const S3DVertex& other) const
	{
		return (self->Pos < other.Pos);
	}
}

%extend irr::video::SMaterial {
   bool isWireframe() {return self->Wireframe;};
   //bool isFlag(int material_flag) {return self->Flags[material_flag];};
   //void setFlag(int material_flag, bool value) {self->Flags[material_flag] = value;};
   void setTexture(int index, ITexture* texture) {self->Textures[index] = texture;};
}

%extend irr::SEvent {
   int getKeyInputChar() {return self->KeyInput.Char;};
   EKEY_CODE getKeyInputKey() {return self->KeyInput.Key;};
	 int getKeyInputKeyInt() {return self->KeyInput.Key;}; //BP2K6
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
}

%extend irr::scene::SMeshBuffer {

   void setMaterial(video::SMaterial& newMaterial)
   {
      self->Material = newMaterial;
   }
   void setIndices(core::array<u16> &newIndices)
   {
      self->Indices = newIndices;
   }
//   void setVertices2(irr::core::array<irr::video::S3DVertex *> &newVertices )
//   {
//      self->Vertices = newVertices;
//   }
   void setVertices(irr::core::array<irr::video::S3DVertex> &newVertices )
   {
      self->Vertices = newVertices;
   }
//   void setVertices3(S3DVertex[] newVertices)
//   {
//	  int size = sizeof(newVertices)/sizeof(S3DVertex)
//      irr::core::array<irr::video::S3DVertex> s3dVertexArray = irr::core::array<irr::video::S3DVertex>(size);
//	  for (int i = 0; i < size; i++)
//	  {
//		s3dVertexArray.push_back(newVertices[i]);
//	  }
//	  
//      self->Vertices = &s3dVertexArray;
//   }
}

%extend irr::video::IVideoDriver {
}

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

	void OnRegisterSceneNodeJava()
	{
		// no directors for this method
		self->ISceneNode::OnRegisterSceneNode();
	}
	
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
	}
}

%extend irr::core::rect<s32> {
	//! Sets all 4 values of the rectangle (ANDI)
	void setRect(s32 x1, s32 y1, s32 x2, s32 y2)
	{		
		self->UpperLeftCorner = position2d<s32>(x1, y1);
		self->LowerRightCorner = position2d<s32>(x2, y2);
	}
}

%extend irr::core::vector3d <class T> {
//	bool operator<(const vector3d<T>&other) const { return self.X<other.X && self.Y<other.Y && selfZ<other.Z;};
}



//BP2k6 additions

%include "ICrowdSceneNode.h"

%extend irr::core::matrix4 {
   
    void setMatrixData(f32 data[16])
    {
        for (int i = 0; i < 16; i++))
            self->M[i] = data[i];
        definitelyIdentityMatrix = false;
    }
}