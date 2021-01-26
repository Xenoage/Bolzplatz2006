VERSION = 0.12.0
# Irrlicht Engine 0.12.0
# Makefile for Linux, created by N.Gebhardt.
# 
# To use, just run:
#
# make
#
# This will compile Irrlicht and create a static lib (libIrrlicht.a) and copy it
# into the subdirectory /lib/Linux. That's all.
#
# If you want Irrlicht to be compiled as shared lib (libIrrlicht.so.versionnumber), then run:
#
# make sharedlib
# make install
#
# Please note that Irrlicht as shared lib is just experimental and  
# probably not tested.
#

CPP  = g++ 
LINKOBJ  = CGUIButton.o CGUICheckBox.o CGUIComboBox.o CGUIContextMenu.o CGUIEditBox.o CGUIEnvironment.o CGUIFileOpenDialog.o CGUIFont.o CGUIImage.o CGUIInOutFader.o CGUIListBox.o CGUIMenu.o CGUIMeshViewer.o CGUIMessageBox.o CGUIModalScreen.o CGUIScrollBar.o CGUISkin.o CGUIStaticText.o CGUITabControl.o CGUIToolBar.o CGUIWindow.o CSoftwareDriver.o CSoftwareTexture.o CTRFlat.o CTRFlatWire.o CTRGouraud.o CTRGouraudWire.o CTRTextureFlat.o CTRTextureFlatWire.o CTRTextureGouraud.o CTRTextureGouraudAdd.o CTRTextureGouraudNoZ.o CTRTextureGouraudWire.o CZBuffer.o COpenGLDriver.o COpenGLNormalMapRenderer.o COpenGLParallaxMapRenderer.o COpenGLShaderMaterialRenderer.o COpenGLTexture.o CD3D8Driver.o CD3D8NormalMapRenderer.o CD3D8ParallaxMapRenderer.o CD3D8ShaderMaterialRenderer.o CD3D8Texture.o CColorConverter.o CFPSCounter.o CImage.o CImageLoaderBmp.o CImageLoaderJPG.o CImageLoaderPCX.o CImageLoaderPNG.o CImageLoaderPSD.o CImageLoaderTGA.o CNullDriver.o CD3D9Driver.o CD3D9HLSLMaterialRenderer.o CD3D9NormalMapRenderer.o CD3D9ParallaxMapRenderer.o CD3D9ShaderMaterialRenderer.o CD3D9Texture.o CVideoModeList.o C3DSMeshFileLoader.o CAnimatedMeshMD2.o CAnimatedMeshMS3D.o CAnimatedMeshSceneNode.o CBillboardSceneNode.o CCameraFPSSceneNode.o CCameraMayaSceneNode.o CCameraSceneNode.o CColladaFileLoader.o CCSMLoader.o CDefaultMeshFormatLoader.o CDMFLoader.o CDummyTransformationSceneNode.o CEmptySceneNode.o CGeometryCreator.o CLightSceneNode.o CLMTSMeshFileLoader.o CMeshManipulator.o CMeshSceneNode.o CMetaTriangleSelector.o CMY3DMeshFileLoader.o COCTLoader.o COctTreeSceneNode.o COctTreeTriangleSelector.o CParticleBoxEmitter.o CParticleFadeOutAffector.o CParticleGravityAffector.o CParticlePointEmitter.o CParticleSystemSceneNode.o CQ3LevelMesh.o CSceneCollisionManager.o CSceneManager.o CSceneNodeAnimatorCollisionResponse.o CSceneNodeAnimatorDelete.o CSceneNodeAnimatorFlyCircle.o CSceneNodeAnimatorFlyStraight.o CSceneNodeAnimatorFollowSpline.o CSceneNodeAnimatorRotation.o CSceneNodeAnimatorTexture.o CShadowVolumeSceneNode.o CSkyBoxSceneNode.o CStaticMeshOBJ.o CTerrainSceneNode.o CTerrainTriangleSelector.o CTestSceneNode.o CTextSceneNode.o CTriangleBBSelector.o CTriangleSelector.o CWaterSurfaceSceneNode.o CXAnimationPlayer.o CXFileReader.o CXMeshFileLoader.o CFileList.o CFileSystem.o CLimitReadFile.o CMemoryReadFile.o CReadFile.o CWriteFile.o CXMLReader.o CXMLWriter.o CZipReader.o irrXML.o zlib/adler32.o zlib/compress.o zlib/crc32.o zlib/deflate.o zlib/inffast.o zlib/inflate.o zlib/inftrees.o zlib/trees.o zlib/uncompr.o zlib/zutil.o jpeglib/jcapimin.o jpeglib/jcapistd.o jpeglib/jccoefct.o jpeglib/jccolor.o jpeglib/jcdctmgr.o jpeglib/jchuff.o jpeglib/jcinit.o jpeglib/jcmainct.o jpeglib/jcmarker.o jpeglib/jcmaster.o jpeglib/jcomapi.o jpeglib/jcparam.o jpeglib/jcphuff.o jpeglib/jcprepct.o jpeglib/jcsample.o jpeglib/jctrans.o jpeglib/jdapimin.o jpeglib/jdapistd.o jpeglib/jdatadst.o jpeglib/jdatasrc.o jpeglib/jdcoefct.o jpeglib/jdcolor.o jpeglib/jddctmgr.o jpeglib/jdhuff.o jpeglib/jdinput.o jpeglib/jdmainct.o jpeglib/jdmarker.o jpeglib/jdmaster.o jpeglib/jdmerge.o jpeglib/jdphuff.o jpeglib/jdpostct.o jpeglib/jdsample.o jpeglib/jdtrans.o jpeglib/jerror.o jpeglib/jfdctflt.o jpeglib/jfdctfst.o jpeglib/jfdctint.o jpeglib/jidctflt.o jpeglib/jidctfst.o jpeglib/jidctint.o jpeglib/jidctred.o jpeglib/jmemmgr.o jpeglib/jmemnobs.o jpeglib/jquant1.o jpeglib/jquant2.o jpeglib/jutils.o jpeglib/rdbmp.o jpeglib/rdcolmap.o jpeglib/rdgif.o jpeglib/rdppm.o jpeglib/rdrle.o jpeglib/rdswitch.o jpeglib/rdtarga.o jpeglib/transupp.o jpeglib/wrbmp.o jpeglib/wrgif.o jpeglib/wrppm.o jpeglib/wrrle.o jpeglib/wrtarga.o libpng/png.o libpng/pngerror.o libpng/pngget.o libpng/pngmem.o libpng/pngpread.o libpng/pngread.o libpng/pngrio.o libpng/pngrtran.o libpng/pngrutil.o libpng/pngset.o libpng/pngtrans.o libpng/pngwio.o libpng/pngwrite.o libpng/pngwtran.o libpng/pngwutil.o CIrrDeviceLinux.o CIrrDeviceStub.o CIrrDeviceWin32.o CLogger.o COSOperator.o CStringParameters.o Irrlicht.o os.o CMeshCache.o COpenGLSLMaterialRenderer.o CZBuffer2.o CTRTextureGouraudAlpha2.o CTRTextureGouraudNoZ2.o CTRTextureLightMap2_M2.o CTRTextureLightMap2_M4.o CTRTextureLightMap2_M1.o CSoftwareDriver2.o CSoftwareTexture2.o CTRTextureGouraud2.o CCrowdSceneNode.o $(RES)
LIBS =  -L"/usr/X11R6/lib" -L"jpeglib" -L"zlib" --no-export-all-symbols --add-stdcall-alias -lGL -lGLU -lXxf86vm
CXXINCS = -I"include/" -I"zlib/"
STATIC_LIB  = libIrrlicht.a
SHARED_LIB = libIrrlicht.so
CXXFLAGS = $(CXXINCS) -DIRRLICHT_EXPORTS=1
INSTALL_DIR = /usr/local/lib

# All target, builds Irrlicht as static lib (libIrrlicht.a) and copies it into /lib/Linux
all: $(LINKOBJ) staticlib

# Cleans all temporary files and compilation results. 
clean:
	rm -f $(LINKOBJ) $(SHARED_LIB).$(VERSION) $(STATIC_LIB)

# Builds Irrlicht as shared lib (libIrrlicht.so.versionNumber) and copies it into /lib/Linux
sharedlib: $(LINKOBJ)
	$(CPP) -shared -fPIC -o $(SHARED_LIB).$(VERSION) $(LINKOBJ) $(LIBS)
	cp $(SHARED_LIB).$(VERSION) lib/Linux/
	
# Builds Irrlicht as static lib (libIrrlicht.a) and copies it into /lib/Linux
staticlib: $(LINKOBJ)
	ar r $(STATIC_LIB) $(LINKOBJ)
	ranlib $(STATIC_LIB)
	cp libIrrlicht.a lib/Linux/
	
# Installs Irrlicht if it was created as shared lib
install:
	cp lib/Linux/$(SHARED_LIB).$(VERSION) $(INSTALL_DIR)
	ln -s $(INSTALL_DIR)/libIrrlicht.so.$(VERSION) $(INSTALL_DIR)/$(SHARED_LIB)
	ldconfig -n $(INSTALL_DIR)

CGUIButton.o: CGUIButton.cpp
	$(CPP) -c CGUIButton.cpp -o CGUIButton.o $(CXXFLAGS)

CGUICheckBox.o: CGUICheckBox.cpp
	$(CPP) -c CGUICheckBox.cpp -o CGUICheckBox.o $(CXXFLAGS)

CGUIComboBox.o: CGUIComboBox.cpp
	$(CPP) -c CGUIComboBox.cpp -o CGUIComboBox.o $(CXXFLAGS)

CGUIContextMenu.o: CGUIContextMenu.cpp
	$(CPP) -c CGUIContextMenu.cpp -o CGUIContextMenu.o $(CXXFLAGS)

CGUIEditBox.o: CGUIEditBox.cpp
	$(CPP) -c CGUIEditBox.cpp -o CGUIEditBox.o $(CXXFLAGS)

CGUIEnvironment.o: CGUIEnvironment.cpp
	$(CPP) -c CGUIEnvironment.cpp -o CGUIEnvironment.o $(CXXFLAGS)

CGUIFileOpenDialog.o: CGUIFileOpenDialog.cpp
	$(CPP) -c CGUIFileOpenDialog.cpp -o CGUIFileOpenDialog.o $(CXXFLAGS)

CGUIFont.o: CGUIFont.cpp
	$(CPP) -c CGUIFont.cpp -o CGUIFont.o $(CXXFLAGS)

CGUIImage.o: CGUIImage.cpp
	$(CPP) -c CGUIImage.cpp -o CGUIImage.o $(CXXFLAGS)

CGUIInOutFader.o: CGUIInOutFader.cpp
	$(CPP) -c CGUIInOutFader.cpp -o CGUIInOutFader.o $(CXXFLAGS)

CGUIListBox.o: CGUIListBox.cpp
	$(CPP) -c CGUIListBox.cpp -o CGUIListBox.o $(CXXFLAGS)

CGUIMenu.o: CGUIMenu.cpp
	$(CPP) -c CGUIMenu.cpp -o CGUIMenu.o $(CXXFLAGS)

CGUIMeshViewer.o: CGUIMeshViewer.cpp
	$(CPP) -c CGUIMeshViewer.cpp -o CGUIMeshViewer.o $(CXXFLAGS)

CGUIMessageBox.o: CGUIMessageBox.cpp
	$(CPP) -c CGUIMessageBox.cpp -o CGUIMessageBox.o $(CXXFLAGS)

CGUIModalScreen.o: CGUIModalScreen.cpp
	$(CPP) -c CGUIModalScreen.cpp -o CGUIModalScreen.o $(CXXFLAGS)

CGUIScrollBar.o: CGUIScrollBar.cpp
	$(CPP) -c CGUIScrollBar.cpp -o CGUIScrollBar.o $(CXXFLAGS)

CGUISkin.o: CGUISkin.cpp
	$(CPP) -c CGUISkin.cpp -o CGUISkin.o $(CXXFLAGS)

CGUIStaticText.o: CGUIStaticText.cpp
	$(CPP) -c CGUIStaticText.cpp -o CGUIStaticText.o $(CXXFLAGS)

CGUITabControl.o: CGUITabControl.cpp
	$(CPP) -c CGUITabControl.cpp -o CGUITabControl.o $(CXXFLAGS)

CGUIToolBar.o: CGUIToolBar.cpp
	$(CPP) -c CGUIToolBar.cpp -o CGUIToolBar.o $(CXXFLAGS)

CGUIWindow.o: CGUIWindow.cpp
	$(CPP) -c CGUIWindow.cpp -o CGUIWindow.o $(CXXFLAGS)

CSoftwareDriver.o: CSoftwareDriver.cpp
	$(CPP) -c CSoftwareDriver.cpp -o CSoftwareDriver.o $(CXXFLAGS)

CSoftwareTexture.o: CSoftwareTexture.cpp
	$(CPP) -c CSoftwareTexture.cpp -o CSoftwareTexture.o $(CXXFLAGS)

CTRFlat.o: CTRFlat.cpp
	$(CPP) -c CTRFlat.cpp -o CTRFlat.o $(CXXFLAGS)

CTRFlatWire.o: CTRFlatWire.cpp
	$(CPP) -c CTRFlatWire.cpp -o CTRFlatWire.o $(CXXFLAGS)

CTRGouraud.o: CTRGouraud.cpp
	$(CPP) -c CTRGouraud.cpp -o CTRGouraud.o $(CXXFLAGS)

CTRGouraudWire.o: CTRGouraudWire.cpp
	$(CPP) -c CTRGouraudWire.cpp -o CTRGouraudWire.o $(CXXFLAGS)

CTRTextureFlat.o: CTRTextureFlat.cpp
	$(CPP) -c CTRTextureFlat.cpp -o CTRTextureFlat.o $(CXXFLAGS)

CTRTextureFlatWire.o: CTRTextureFlatWire.cpp
	$(CPP) -c CTRTextureFlatWire.cpp -o CTRTextureFlatWire.o $(CXXFLAGS)

CTRTextureGouraud.o: CTRTextureGouraud.cpp
	$(CPP) -c CTRTextureGouraud.cpp -o CTRTextureGouraud.o $(CXXFLAGS)

CTRTextureGouraudAdd.o: CTRTextureGouraudAdd.cpp
	$(CPP) -c CTRTextureGouraudAdd.cpp -o CTRTextureGouraudAdd.o $(CXXFLAGS)

CTRTextureGouraudNoZ.o: CTRTextureGouraudNoZ.cpp
	$(CPP) -c CTRTextureGouraudNoZ.cpp -o CTRTextureGouraudNoZ.o $(CXXFLAGS)

CTRTextureGouraudWire.o: CTRTextureGouraudWire.cpp
	$(CPP) -c CTRTextureGouraudWire.cpp -o CTRTextureGouraudWire.o $(CXXFLAGS)

CZBuffer.o: CZBuffer.cpp
	$(CPP) -c CZBuffer.cpp -o CZBuffer.o $(CXXFLAGS)

COpenGLDriver.o: COpenGLDriver.cpp
	$(CPP) -c COpenGLDriver.cpp -o COpenGLDriver.o $(CXXFLAGS)

COpenGLNormalMapRenderer.o: COpenGLNormalMapRenderer.cpp
	$(CPP) -c COpenGLNormalMapRenderer.cpp -o COpenGLNormalMapRenderer.o $(CXXFLAGS)

COpenGLParallaxMapRenderer.o: COpenGLParallaxMapRenderer.cpp
	$(CPP) -c COpenGLParallaxMapRenderer.cpp -o COpenGLParallaxMapRenderer.o $(CXXFLAGS)

COpenGLShaderMaterialRenderer.o: COpenGLShaderMaterialRenderer.cpp
	$(CPP) -c COpenGLShaderMaterialRenderer.cpp -o COpenGLShaderMaterialRenderer.o $(CXXFLAGS)

COpenGLTexture.o: COpenGLTexture.cpp
	$(CPP) -c COpenGLTexture.cpp -o COpenGLTexture.o $(CXXFLAGS)

CD3D8Driver.o: CD3D8Driver.cpp
	$(CPP) -c CD3D8Driver.cpp -o CD3D8Driver.o $(CXXFLAGS)

CD3D8NormalMapRenderer.o: CD3D8NormalMapRenderer.cpp
	$(CPP) -c CD3D8NormalMapRenderer.cpp -o CD3D8NormalMapRenderer.o $(CXXFLAGS)

CD3D8ParallaxMapRenderer.o: CD3D8ParallaxMapRenderer.cpp
	$(CPP) -c CD3D8ParallaxMapRenderer.cpp -o CD3D8ParallaxMapRenderer.o $(CXXFLAGS)

CD3D8ShaderMaterialRenderer.o: CD3D8ShaderMaterialRenderer.cpp
	$(CPP) -c CD3D8ShaderMaterialRenderer.cpp -o CD3D8ShaderMaterialRenderer.o $(CXXFLAGS)

CD3D8Texture.o: CD3D8Texture.cpp
	$(CPP) -c CD3D8Texture.cpp -o CD3D8Texture.o $(CXXFLAGS)

CColorConverter.o: CColorConverter.cpp
	$(CPP) -c CColorConverter.cpp -o CColorConverter.o $(CXXFLAGS)

CFPSCounter.o: CFPSCounter.cpp
	$(CPP) -c CFPSCounter.cpp -o CFPSCounter.o $(CXXFLAGS)

CImage.o: CImage.cpp
	$(CPP) -c CImage.cpp -o CImage.o $(CXXFLAGS)

CImageLoaderBmp.o: CImageLoaderBmp.cpp
	$(CPP) -c CImageLoaderBmp.cpp -o CImageLoaderBmp.o $(CXXFLAGS)

CImageLoaderJPG.o: CImageLoaderJPG.cpp
	$(CPP) -c CImageLoaderJPG.cpp -o CImageLoaderJPG.o $(CXXFLAGS)

CImageLoaderPCX.o: CImageLoaderPCX.cpp
	$(CPP) -c CImageLoaderPCX.cpp -o CImageLoaderPCX.o $(CXXFLAGS)

CImageLoaderPNG.o: CImageLoaderPNG.cpp
	$(CPP) -c CImageLoaderPNG.cpp -o CImageLoaderPNG.o $(CXXFLAGS)

CImageLoaderPSD.o: CImageLoaderPSD.cpp
	$(CPP) -c CImageLoaderPSD.cpp -o CImageLoaderPSD.o $(CXXFLAGS)

CImageLoaderTGA.o: CImageLoaderTGA.cpp
	$(CPP) -c CImageLoaderTGA.cpp -o CImageLoaderTGA.o $(CXXFLAGS)

CNullDriver.o: CNullDriver.cpp
	$(CPP) -c CNullDriver.cpp -o CNullDriver.o $(CXXFLAGS)

CD3D9Driver.o: CD3D9Driver.cpp
	$(CPP) -c CD3D9Driver.cpp -o CD3D9Driver.o $(CXXFLAGS)

CD3D9HLSLMaterialRenderer.o: CD3D9HLSLMaterialRenderer.cpp
	$(CPP) -c CD3D9HLSLMaterialRenderer.cpp -o CD3D9HLSLMaterialRenderer.o $(CXXFLAGS)

CD3D9NormalMapRenderer.o: CD3D9NormalMapRenderer.cpp
	$(CPP) -c CD3D9NormalMapRenderer.cpp -o CD3D9NormalMapRenderer.o $(CXXFLAGS)

CD3D9ParallaxMapRenderer.o: CD3D9ParallaxMapRenderer.cpp
	$(CPP) -c CD3D9ParallaxMapRenderer.cpp -o CD3D9ParallaxMapRenderer.o $(CXXFLAGS)

CD3D9ShaderMaterialRenderer.o: CD3D9ShaderMaterialRenderer.cpp
	$(CPP) -c CD3D9ShaderMaterialRenderer.cpp -o CD3D9ShaderMaterialRenderer.o $(CXXFLAGS)

CD3D9Texture.o: CD3D9Texture.cpp
	$(CPP) -c CD3D9Texture.cpp -o CD3D9Texture.o $(CXXFLAGS)

CVideoModeList.o: CVideoModeList.cpp
	$(CPP) -c CVideoModeList.cpp -o CVideoModeList.o $(CXXFLAGS)

C3DSMeshFileLoader.o: C3DSMeshFileLoader.cpp
	$(CPP) -c C3DSMeshFileLoader.cpp -o C3DSMeshFileLoader.o $(CXXFLAGS)

CAnimatedMeshMD2.o: CAnimatedMeshMD2.cpp
	$(CPP) -c CAnimatedMeshMD2.cpp -o CAnimatedMeshMD2.o $(CXXFLAGS)

CAnimatedMeshMS3D.o: CAnimatedMeshMS3D.cpp
	$(CPP) -c CAnimatedMeshMS3D.cpp -o CAnimatedMeshMS3D.o $(CXXFLAGS)

CAnimatedMeshSceneNode.o: CAnimatedMeshSceneNode.cpp
	$(CPP) -c CAnimatedMeshSceneNode.cpp -o CAnimatedMeshSceneNode.o $(CXXFLAGS)

CBillboardSceneNode.o: CBillboardSceneNode.cpp
	$(CPP) -c CBillboardSceneNode.cpp -o CBillboardSceneNode.o $(CXXFLAGS)

CCameraFPSSceneNode.o: CCameraFPSSceneNode.cpp
	$(CPP) -c CCameraFPSSceneNode.cpp -o CCameraFPSSceneNode.o $(CXXFLAGS)

CCameraMayaSceneNode.o: CCameraMayaSceneNode.cpp
	$(CPP) -c CCameraMayaSceneNode.cpp -o CCameraMayaSceneNode.o $(CXXFLAGS)

CCameraSceneNode.o: CCameraSceneNode.cpp
	$(CPP) -c CCameraSceneNode.cpp -o CCameraSceneNode.o $(CXXFLAGS)

CColladaFileLoader.o: CColladaFileLoader.cpp
	$(CPP) -c CColladaFileLoader.cpp -o CColladaFileLoader.o $(CXXFLAGS)

CCSMLoader.o: CCSMLoader.cpp
	$(CPP) -c CCSMLoader.cpp -o CCSMLoader.o $(CXXFLAGS)

CDefaultMeshFormatLoader.o: CDefaultMeshFormatLoader.cpp
	$(CPP) -c CDefaultMeshFormatLoader.cpp -o CDefaultMeshFormatLoader.o $(CXXFLAGS)

CDMFLoader.o: CDMFLoader.cpp
	$(CPP) -c CDMFLoader.cpp -o CDMFLoader.o $(CXXFLAGS)

CDummyTransformationSceneNode.o: CDummyTransformationSceneNode.cpp
	$(CPP) -c CDummyTransformationSceneNode.cpp -o CDummyTransformationSceneNode.o $(CXXFLAGS)

CEmptySceneNode.o: CEmptySceneNode.cpp
	$(CPP) -c CEmptySceneNode.cpp -o CEmptySceneNode.o $(CXXFLAGS)

CGeometryCreator.o: CGeometryCreator.cpp
	$(CPP) -c CGeometryCreator.cpp -o CGeometryCreator.o $(CXXFLAGS)

CLightSceneNode.o: CLightSceneNode.cpp
	$(CPP) -c CLightSceneNode.cpp -o CLightSceneNode.o $(CXXFLAGS)

CLMTSMeshFileLoader.o: CLMTSMeshFileLoader.cpp
	$(CPP) -c CLMTSMeshFileLoader.cpp -o CLMTSMeshFileLoader.o $(CXXFLAGS)

CMeshManipulator.o: CMeshManipulator.cpp
	$(CPP) -c CMeshManipulator.cpp -o CMeshManipulator.o $(CXXFLAGS)

CMeshSceneNode.o: CMeshSceneNode.cpp
	$(CPP) -c CMeshSceneNode.cpp -o CMeshSceneNode.o $(CXXFLAGS)

CMetaTriangleSelector.o: CMetaTriangleSelector.cpp
	$(CPP) -c CMetaTriangleSelector.cpp -o CMetaTriangleSelector.o $(CXXFLAGS)

CMY3DMeshFileLoader.o: CMY3DMeshFileLoader.cpp
	$(CPP) -c CMY3DMeshFileLoader.cpp -o CMY3DMeshFileLoader.o $(CXXFLAGS)

COCTLoader.o: COCTLoader.cpp
	$(CPP) -c COCTLoader.cpp -o COCTLoader.o $(CXXFLAGS)

COctTreeSceneNode.o: COctTreeSceneNode.cpp
	$(CPP) -c COctTreeSceneNode.cpp -o COctTreeSceneNode.o $(CXXFLAGS)

COctTreeTriangleSelector.o: COctTreeTriangleSelector.cpp
	$(CPP) -c COctTreeTriangleSelector.cpp -o COctTreeTriangleSelector.o $(CXXFLAGS)

CParticleBoxEmitter.o: CParticleBoxEmitter.cpp
	$(CPP) -c CParticleBoxEmitter.cpp -o CParticleBoxEmitter.o $(CXXFLAGS)

CParticleFadeOutAffector.o: CParticleFadeOutAffector.cpp
	$(CPP) -c CParticleFadeOutAffector.cpp -o CParticleFadeOutAffector.o $(CXXFLAGS)

CParticleGravityAffector.o: CParticleGravityAffector.cpp
	$(CPP) -c CParticleGravityAffector.cpp -o CParticleGravityAffector.o $(CXXFLAGS)

CParticlePointEmitter.o: CParticlePointEmitter.cpp
	$(CPP) -c CParticlePointEmitter.cpp -o CParticlePointEmitter.o $(CXXFLAGS)

CParticleSystemSceneNode.o: CParticleSystemSceneNode.cpp
	$(CPP) -c CParticleSystemSceneNode.cpp -o CParticleSystemSceneNode.o $(CXXFLAGS)

CQ3LevelMesh.o: CQ3LevelMesh.cpp
	$(CPP) -c CQ3LevelMesh.cpp -o CQ3LevelMesh.o $(CXXFLAGS)

CSceneCollisionManager.o: CSceneCollisionManager.cpp
	$(CPP) -c CSceneCollisionManager.cpp -o CSceneCollisionManager.o $(CXXFLAGS)

CSceneManager.o: CSceneManager.cpp
	$(CPP) -c CSceneManager.cpp -o CSceneManager.o $(CXXFLAGS)

CSceneNodeAnimatorCollisionResponse.o: CSceneNodeAnimatorCollisionResponse.cpp
	$(CPP) -c CSceneNodeAnimatorCollisionResponse.cpp -o CSceneNodeAnimatorCollisionResponse.o $(CXXFLAGS)

CSceneNodeAnimatorDelete.o: CSceneNodeAnimatorDelete.cpp
	$(CPP) -c CSceneNodeAnimatorDelete.cpp -o CSceneNodeAnimatorDelete.o $(CXXFLAGS)

CSceneNodeAnimatorFlyCircle.o: CSceneNodeAnimatorFlyCircle.cpp
	$(CPP) -c CSceneNodeAnimatorFlyCircle.cpp -o CSceneNodeAnimatorFlyCircle.o $(CXXFLAGS)

CSceneNodeAnimatorFlyStraight.o: CSceneNodeAnimatorFlyStraight.cpp
	$(CPP) -c CSceneNodeAnimatorFlyStraight.cpp -o CSceneNodeAnimatorFlyStraight.o $(CXXFLAGS)

CSceneNodeAnimatorFollowSpline.o: CSceneNodeAnimatorFollowSpline.cpp
	$(CPP) -c CSceneNodeAnimatorFollowSpline.cpp -o CSceneNodeAnimatorFollowSpline.o $(CXXFLAGS)

CSceneNodeAnimatorRotation.o: CSceneNodeAnimatorRotation.cpp
	$(CPP) -c CSceneNodeAnimatorRotation.cpp -o CSceneNodeAnimatorRotation.o $(CXXFLAGS)

CSceneNodeAnimatorTexture.o: CSceneNodeAnimatorTexture.cpp
	$(CPP) -c CSceneNodeAnimatorTexture.cpp -o CSceneNodeAnimatorTexture.o $(CXXFLAGS)

CShadowVolumeSceneNode.o: CShadowVolumeSceneNode.cpp
	$(CPP) -c CShadowVolumeSceneNode.cpp -o CShadowVolumeSceneNode.o $(CXXFLAGS)

CSkyBoxSceneNode.o: CSkyBoxSceneNode.cpp
	$(CPP) -c CSkyBoxSceneNode.cpp -o CSkyBoxSceneNode.o $(CXXFLAGS)

CStaticMeshOBJ.o: CStaticMeshOBJ.cpp
	$(CPP) -c CStaticMeshOBJ.cpp -o CStaticMeshOBJ.o $(CXXFLAGS)

CTerrainSceneNode.o: CTerrainSceneNode.cpp
	$(CPP) -c CTerrainSceneNode.cpp -o CTerrainSceneNode.o $(CXXFLAGS)

CTerrainTriangleSelector.o: CTerrainTriangleSelector.cpp
	$(CPP) -c CTerrainTriangleSelector.cpp -o CTerrainTriangleSelector.o $(CXXFLAGS)

CTestSceneNode.o: CTestSceneNode.cpp
	$(CPP) -c CTestSceneNode.cpp -o CTestSceneNode.o $(CXXFLAGS)

CTextSceneNode.o: CTextSceneNode.cpp
	$(CPP) -c CTextSceneNode.cpp -o CTextSceneNode.o $(CXXFLAGS)

CTriangleBBSelector.o: CTriangleBBSelector.cpp
	$(CPP) -c CTriangleBBSelector.cpp -o CTriangleBBSelector.o $(CXXFLAGS)

CTriangleSelector.o: CTriangleSelector.cpp
	$(CPP) -c CTriangleSelector.cpp -o CTriangleSelector.o $(CXXFLAGS)

CWaterSurfaceSceneNode.o: CWaterSurfaceSceneNode.cpp
	$(CPP) -c CWaterSurfaceSceneNode.cpp -o CWaterSurfaceSceneNode.o $(CXXFLAGS)

CXAnimationPlayer.o: CXAnimationPlayer.cpp
	$(CPP) -c CXAnimationPlayer.cpp -o CXAnimationPlayer.o $(CXXFLAGS)

CXFileReader.o: CXFileReader.cpp
	$(CPP) -c CXFileReader.cpp -o CXFileReader.o $(CXXFLAGS)

CXMeshFileLoader.o: CXMeshFileLoader.cpp
	$(CPP) -c CXMeshFileLoader.cpp -o CXMeshFileLoader.o $(CXXFLAGS)

CFileList.o: CFileList.cpp
	$(CPP) -c CFileList.cpp -o CFileList.o $(CXXFLAGS)

CFileSystem.o: CFileSystem.cpp
	$(CPP) -c CFileSystem.cpp -o CFileSystem.o $(CXXFLAGS)

CLimitReadFile.o: CLimitReadFile.cpp
	$(CPP) -c CLimitReadFile.cpp -o CLimitReadFile.o $(CXXFLAGS)

CMemoryReadFile.o: CMemoryReadFile.cpp
	$(CPP) -c CMemoryReadFile.cpp -o CMemoryReadFile.o $(CXXFLAGS)

CReadFile.o: CReadFile.cpp
	$(CPP) -c CReadFile.cpp -o CReadFile.o $(CXXFLAGS)

CWriteFile.o: CWriteFile.cpp
	$(CPP) -c CWriteFile.cpp -o CWriteFile.o $(CXXFLAGS)

CXMLReader.o: CXMLReader.cpp
	$(CPP) -c CXMLReader.cpp -o CXMLReader.o $(CXXFLAGS)

CXMLWriter.o: CXMLWriter.cpp
	$(CPP) -c CXMLWriter.cpp -o CXMLWriter.o $(CXXFLAGS)

CZipReader.o: CZipReader.cpp
	$(CPP) -c CZipReader.cpp -o CZipReader.o $(CXXFLAGS)

irrXML.o: irrXML.cpp
	$(CPP) -c irrXML.cpp -o irrXML.o $(CXXFLAGS)

zlib/adler32.o: zlib/adler32.c
	$(CC) -c zlib/adler32.c -o zlib/adler32.o $(CFLAGS)

zlib/compress.o: zlib/compress.c
	$(CC) -c zlib/compress.c -o zlib/compress.o $(CFLAGS)

zlib/crc32.o: zlib/crc32.c
	$(CC) -c zlib/crc32.c -o zlib/crc32.o $(CFLAGS)

zlib/deflate.o: zlib/deflate.c
	$(CC) -c zlib/deflate.c -o zlib/deflate.o $(CFLAGS)

zlib/inffast.o: zlib/inffast.c
	$(CC) -c zlib/inffast.c -o zlib/inffast.o $(CFLAGS)

zlib/inflate.o: zlib/inflate.c
	$(CC) -c zlib/inflate.c -o zlib/inflate.o $(CFLAGS)

zlib/inftrees.o: zlib/inftrees.c
	$(CC) -c zlib/inftrees.c -o zlib/inftrees.o $(CFLAGS)

zlib/trees.o: zlib/trees.c
	$(CC) -c zlib/trees.c -o zlib/trees.o $(CFLAGS)

zlib/uncompr.o: zlib/uncompr.c
	$(CC) -c zlib/uncompr.c -o zlib/uncompr.o $(CFLAGS)

zlib/zutil.o: zlib/zutil.c
	$(CC) -c zlib/zutil.c -o zlib/zutil.o $(CFLAGS)

jpeglib/jcapimin.o: jpeglib/jcapimin.c
	$(CC) -c jpeglib/jcapimin.c -o jpeglib/jcapimin.o $(CFLAGS)

jpeglib/jcapistd.o: jpeglib/jcapistd.c
	$(CC) -c jpeglib/jcapistd.c -o jpeglib/jcapistd.o $(CFLAGS)

jpeglib/jccoefct.o: jpeglib/jccoefct.c
	$(CC) -c jpeglib/jccoefct.c -o jpeglib/jccoefct.o $(CFLAGS)

jpeglib/jccolor.o: jpeglib/jccolor.c
	$(CC) -c jpeglib/jccolor.c -o jpeglib/jccolor.o $(CFLAGS)

jpeglib/jcdctmgr.o: jpeglib/jcdctmgr.c
	$(CC) -c jpeglib/jcdctmgr.c -o jpeglib/jcdctmgr.o $(CFLAGS)

jpeglib/jchuff.o: jpeglib/jchuff.c
	$(CC) -c jpeglib/jchuff.c -o jpeglib/jchuff.o $(CFLAGS)

jpeglib/jcinit.o: jpeglib/jcinit.c
	$(CC) -c jpeglib/jcinit.c -o jpeglib/jcinit.o $(CFLAGS)

jpeglib/jcmainct.o: jpeglib/jcmainct.c
	$(CC) -c jpeglib/jcmainct.c -o jpeglib/jcmainct.o $(CFLAGS)

jpeglib/jcmarker.o: jpeglib/jcmarker.c
	$(CC) -c jpeglib/jcmarker.c -o jpeglib/jcmarker.o $(CFLAGS)

jpeglib/jcmaster.o: jpeglib/jcmaster.c
	$(CC) -c jpeglib/jcmaster.c -o jpeglib/jcmaster.o $(CFLAGS)

jpeglib/jcomapi.o: jpeglib/jcomapi.c
	$(CC) -c jpeglib/jcomapi.c -o jpeglib/jcomapi.o $(CFLAGS)

jpeglib/jcparam.o: jpeglib/jcparam.c
	$(CC) -c jpeglib/jcparam.c -o jpeglib/jcparam.o $(CFLAGS)

jpeglib/jcphuff.o: jpeglib/jcphuff.c
	$(CC) -c jpeglib/jcphuff.c -o jpeglib/jcphuff.o $(CFLAGS)

jpeglib/jcprepct.o: jpeglib/jcprepct.c
	$(CC) -c jpeglib/jcprepct.c -o jpeglib/jcprepct.o $(CFLAGS)

jpeglib/jcsample.o: jpeglib/jcsample.c
	$(CC) -c jpeglib/jcsample.c -o jpeglib/jcsample.o $(CFLAGS)

jpeglib/jctrans.o: jpeglib/jctrans.c
	$(CC) -c jpeglib/jctrans.c -o jpeglib/jctrans.o $(CFLAGS)

jpeglib/jdapimin.o: jpeglib/jdapimin.c
	$(CC) -c jpeglib/jdapimin.c -o jpeglib/jdapimin.o $(CFLAGS)

jpeglib/jdapistd.o: jpeglib/jdapistd.c
	$(CC) -c jpeglib/jdapistd.c -o jpeglib/jdapistd.o $(CFLAGS)

jpeglib/jdatadst.o: jpeglib/jdatadst.c
	$(CC) -c jpeglib/jdatadst.c -o jpeglib/jdatadst.o $(CFLAGS)

jpeglib/jdatasrc.o: jpeglib/jdatasrc.c
	$(CC) -c jpeglib/jdatasrc.c -o jpeglib/jdatasrc.o $(CFLAGS)

jpeglib/jdcoefct.o: jpeglib/jdcoefct.c
	$(CC) -c jpeglib/jdcoefct.c -o jpeglib/jdcoefct.o $(CFLAGS)

jpeglib/jdcolor.o: jpeglib/jdcolor.c
	$(CC) -c jpeglib/jdcolor.c -o jpeglib/jdcolor.o $(CFLAGS)

jpeglib/jddctmgr.o: jpeglib/jddctmgr.c
	$(CC) -c jpeglib/jddctmgr.c -o jpeglib/jddctmgr.o $(CFLAGS)

jpeglib/jdhuff.o: jpeglib/jdhuff.c
	$(CC) -c jpeglib/jdhuff.c -o jpeglib/jdhuff.o $(CFLAGS)

jpeglib/jdinput.o: jpeglib/jdinput.c
	$(CC) -c jpeglib/jdinput.c -o jpeglib/jdinput.o $(CFLAGS)

jpeglib/jdmainct.o: jpeglib/jdmainct.c
	$(CC) -c jpeglib/jdmainct.c -o jpeglib/jdmainct.o $(CFLAGS)

jpeglib/jdmarker.o: jpeglib/jdmarker.c
	$(CC) -c jpeglib/jdmarker.c -o jpeglib/jdmarker.o $(CFLAGS)

jpeglib/jdmaster.o: jpeglib/jdmaster.c
	$(CC) -c jpeglib/jdmaster.c -o jpeglib/jdmaster.o $(CFLAGS)

jpeglib/jdmerge.o: jpeglib/jdmerge.c
	$(CC) -c jpeglib/jdmerge.c -o jpeglib/jdmerge.o $(CFLAGS)

jpeglib/jdphuff.o: jpeglib/jdphuff.c
	$(CC) -c jpeglib/jdphuff.c -o jpeglib/jdphuff.o $(CFLAGS)

jpeglib/jdpostct.o: jpeglib/jdpostct.c
	$(CC) -c jpeglib/jdpostct.c -o jpeglib/jdpostct.o $(CFLAGS)

jpeglib/jdsample.o: jpeglib/jdsample.c
	$(CC) -c jpeglib/jdsample.c -o jpeglib/jdsample.o $(CFLAGS)

jpeglib/jdtrans.o: jpeglib/jdtrans.c
	$(CC) -c jpeglib/jdtrans.c -o jpeglib/jdtrans.o $(CFLAGS)

jpeglib/jerror.o: jpeglib/jerror.c
	$(CC) -c jpeglib/jerror.c -o jpeglib/jerror.o $(CFLAGS)

jpeglib/jfdctflt.o: jpeglib/jfdctflt.c
	$(CC) -c jpeglib/jfdctflt.c -o jpeglib/jfdctflt.o $(CFLAGS)

jpeglib/jfdctfst.o: jpeglib/jfdctfst.c
	$(CC) -c jpeglib/jfdctfst.c -o jpeglib/jfdctfst.o $(CFLAGS)

jpeglib/jfdctint.o: jpeglib/jfdctint.c
	$(CC) -c jpeglib/jfdctint.c -o jpeglib/jfdctint.o $(CFLAGS)

jpeglib/jidctflt.o: jpeglib/jidctflt.c
	$(CC) -c jpeglib/jidctflt.c -o jpeglib/jidctflt.o $(CFLAGS)

jpeglib/jidctfst.o: jpeglib/jidctfst.c
	$(CC) -c jpeglib/jidctfst.c -o jpeglib/jidctfst.o $(CFLAGS)

jpeglib/jidctint.o: jpeglib/jidctint.c
	$(CC) -c jpeglib/jidctint.c -o jpeglib/jidctint.o $(CFLAGS)

jpeglib/jidctred.o: jpeglib/jidctred.c
	$(CC) -c jpeglib/jidctred.c -o jpeglib/jidctred.o $(CFLAGS)

jpeglib/jmemmgr.o: jpeglib/jmemmgr.c
	$(CC) -c jpeglib/jmemmgr.c -o jpeglib/jmemmgr.o $(CFLAGS)

jpeglib/jmemnobs.o: jpeglib/jmemnobs.c
	$(CC) -c jpeglib/jmemnobs.c -o jpeglib/jmemnobs.o $(CFLAGS)

jpeglib/jquant1.o: jpeglib/jquant1.c
	$(CC) -c jpeglib/jquant1.c -o jpeglib/jquant1.o $(CFLAGS)

jpeglib/jquant2.o: jpeglib/jquant2.c
	$(CC) -c jpeglib/jquant2.c -o jpeglib/jquant2.o $(CFLAGS)

jpeglib/jutils.o: jpeglib/jutils.c
	$(CC) -c jpeglib/jutils.c -o jpeglib/jutils.o $(CFLAGS)

jpeglib/rdbmp.o: jpeglib/rdbmp.c
	$(CC) -c jpeglib/rdbmp.c -o jpeglib/rdbmp.o $(CFLAGS)

jpeglib/rdcolmap.o: jpeglib/rdcolmap.c
	$(CC) -c jpeglib/rdcolmap.c -o jpeglib/rdcolmap.o $(CFLAGS)

jpeglib/rdgif.o: jpeglib/rdgif.c
	$(CC) -c jpeglib/rdgif.c -o jpeglib/rdgif.o $(CFLAGS)

jpeglib/rdppm.o: jpeglib/rdppm.c
	$(CC) -c jpeglib/rdppm.c -o jpeglib/rdppm.o $(CFLAGS)

jpeglib/rdrle.o: jpeglib/rdrle.c
	$(CC) -c jpeglib/rdrle.c -o jpeglib/rdrle.o $(CFLAGS)

jpeglib/rdswitch.o: jpeglib/rdswitch.c
	$(CC) -c jpeglib/rdswitch.c -o jpeglib/rdswitch.o $(CFLAGS)

jpeglib/rdtarga.o: jpeglib/rdtarga.c
	$(CC) -c jpeglib/rdtarga.c -o jpeglib/rdtarga.o $(CFLAGS)

jpeglib/transupp.o: jpeglib/transupp.c
	$(CC) -c jpeglib/transupp.c -o jpeglib/transupp.o $(CFLAGS)

jpeglib/wrbmp.o: jpeglib/wrbmp.c
	$(CC) -c jpeglib/wrbmp.c -o jpeglib/wrbmp.o $(CFLAGS)

jpeglib/wrgif.o: jpeglib/wrgif.c
	$(CC) -c jpeglib/wrgif.c -o jpeglib/wrgif.o $(CFLAGS)

jpeglib/wrppm.o: jpeglib/wrppm.c
	$(CC) -c jpeglib/wrppm.c -o jpeglib/wrppm.o $(CFLAGS)

jpeglib/wrrle.o: jpeglib/wrrle.c
	$(CC) -c jpeglib/wrrle.c -o jpeglib/wrrle.o $(CFLAGS)

jpeglib/wrtarga.o: jpeglib/wrtarga.c
	$(CC) -c jpeglib/wrtarga.c -o jpeglib/wrtarga.o $(CFLAGS)

libpng/png.o: libpng/png.c
	$(CC) -c libpng/png.c -o libpng/png.o $(CFLAGS)

libpng/pngerror.o: libpng/pngerror.c
	$(CC) -c libpng/pngerror.c -o libpng/pngerror.o $(CFLAGS)

libpng/pngget.o: libpng/pngget.c
	$(CC) -c libpng/pngget.c -o libpng/pngget.o $(CFLAGS)

libpng/pngmem.o: libpng/pngmem.c
	$(CC) -c libpng/pngmem.c -o libpng/pngmem.o $(CFLAGS)

libpng/pngpread.o: libpng/pngpread.c
	$(CC) -c libpng/pngpread.c -o libpng/pngpread.o $(CFLAGS)

libpng/pngread.o: libpng/pngread.c
	$(CC) -c libpng/pngread.c -o libpng/pngread.o $(CFLAGS)

libpng/pngrio.o: libpng/pngrio.c
	$(CC) -c libpng/pngrio.c -o libpng/pngrio.o $(CFLAGS)

libpng/pngrtran.o: libpng/pngrtran.c
	$(CC) -c libpng/pngrtran.c -o libpng/pngrtran.o $(CFLAGS)

libpng/pngrutil.o: libpng/pngrutil.c
	$(CC) -c libpng/pngrutil.c -o libpng/pngrutil.o $(CFLAGS)

libpng/pngset.o: libpng/pngset.c
	$(CC) -c libpng/pngset.c -o libpng/pngset.o $(CFLAGS)

libpng/pngtrans.o: libpng/pngtrans.c
	$(CC) -c libpng/pngtrans.c -o libpng/pngtrans.o $(CFLAGS)

libpng/pngwio.o: libpng/pngwio.c
	$(CC) -c libpng/pngwio.c -o libpng/pngwio.o $(CFLAGS)

libpng/pngwrite.o: libpng/pngwrite.c
	$(CC) -c libpng/pngwrite.c -o libpng/pngwrite.o $(CFLAGS)

libpng/pngwtran.o: libpng/pngwtran.c
	$(CC) -c libpng/pngwtran.c -o libpng/pngwtran.o $(CFLAGS)

libpng/pngwutil.o: libpng/pngwutil.c
	$(CC) -c libpng/pngwutil.c -o libpng/pngwutil.o $(CFLAGS)

CIrrDeviceLinux.o: CIrrDeviceLinux.cpp
	$(CPP) -c CIrrDeviceLinux.cpp -o CIrrDeviceLinux.o $(CXXFLAGS)

CIrrDeviceStub.o: CIrrDeviceStub.cpp
	$(CPP) -c CIrrDeviceStub.cpp -o CIrrDeviceStub.o $(CXXFLAGS)

CIrrDeviceWin32.o: CIrrDeviceWin32.cpp
	$(CPP) -c CIrrDeviceWin32.cpp -o CIrrDeviceWin32.o $(CXXFLAGS)

CLogger.o: CLogger.cpp
	$(CPP) -c CLogger.cpp -o CLogger.o $(CXXFLAGS)

COSOperator.o: COSOperator.cpp
	$(CPP) -c COSOperator.cpp -o COSOperator.o $(CXXFLAGS)

CStringParameters.o: CStringParameters.cpp
	$(CPP) -c CStringParameters.cpp -o CStringParameters.o $(CXXFLAGS)

Irrlicht.o: Irrlicht.cpp
	$(CPP) -c Irrlicht.cpp -o Irrlicht.o $(CXXFLAGS)

os.o: os.cpp
	$(CPP) -c os.cpp -o os.o $(CXXFLAGS)

CMeshCache.o: CMeshCache.cpp
	$(CPP) -c CMeshCache.cpp -o CMeshCache.o $(CXXFLAGS)

COpenGLSLMaterialRenderer.o: COpenGLSLMaterialRenderer.cpp
	$(CPP) -c COpenGLSLMaterialRenderer.cpp -o COpenGLSLMaterialRenderer.o $(CXXFLAGS)

CZBuffer2.o: CZBuffer2.cpp
	$(CPP) -c CZBuffer2.cpp -o CZBuffer2.o $(CXXFLAGS)

CTRTextureGouraudAlpha2.o: CTRTextureGouraudAlpha2.cpp
	$(CPP) -c CTRTextureGouraudAlpha2.cpp -o CTRTextureGouraudAlpha2.o $(CXXFLAGS)

CTRTextureGouraudNoZ2.o: CTRTextureGouraudNoZ2.cpp
	$(CPP) -c CTRTextureGouraudNoZ2.cpp -o CTRTextureGouraudNoZ2.o $(CXXFLAGS)

CTRTextureLightMap2_M2.o: CTRTextureLightMap2_M2.cpp
	$(CPP) -c CTRTextureLightMap2_M2.cpp -o CTRTextureLightMap2_M2.o $(CXXFLAGS)

CTRTextureLightMap2_M4.o: CTRTextureLightMap2_M4.cpp
	$(CPP) -c CTRTextureLightMap2_M4.cpp -o CTRTextureLightMap2_M4.o $(CXXFLAGS)

CTRTextureLightMap2_M1.o: CTRTextureLightMap2_M1.cpp
	$(CPP) -c CTRTextureLightMap2_M1.cpp -o CTRTextureLightMap2_M1.o $(CXXFLAGS)

CSoftwareDriver2.o: CSoftwareDriver2.cpp
	$(CPP) -c CSoftwareDriver2.cpp -o CSoftwareDriver2.o $(CXXFLAGS)

CSoftwareTexture2.o: CSoftwareTexture2.cpp
	$(CPP) -c CSoftwareTexture2.cpp -o CSoftwareTexture2.o $(CXXFLAGS)

CTRTextureGouraud2.o: CTRTextureGouraud2.cpp
	$(CPP) -c CTRTextureGouraud2.cpp -o CTRTextureGouraud2.o $(CXXFLAGS)

CCrowdSceneNode.o: CCrowdSceneNode.cpp
	$(CPP) -c CCrowdSceneNode.cpp -o CCrowdSceneNode.o $(CXXFLAGS)



