package net.sf.jirr.tests;

//import static net.sf.jirr.E_DRIVER_TYPE.*;
import net.sf.jirr.*;

// Java port (c) 2004 by S.Dingfelder

/*
 * This Tutorial shows how to set up the IDE for using the Irrlicht Engine and
 * how to write a simple HelloWorld program with it. The program will show how
 * to use the basics of the VideoDriver, the GUIEnvironment and the
 * SceneManager. Microsoft Visual C++ 6.0 is used as example IDE, but you will
 * also be able to understand everything if you are using a different one.
 * 
 * To use the engine, we will have to include the header file irrlicht.h, which
 * can be found in the Irrlicht Engine SDK directory \include. To let the
 * compiler find this header file, the directory where it is located should be
 * specified somewhere. This is different for every IDE and compiler you use. I
 * explain shortly how to do this in Microsift Visual C++ 6.0. Select the Menu
 * Extras -> Options. Select the directories tab, and select the 'Include' Item
 * in the combo box. Add the \include directory to the list of directories. Now
 * the compiler will find the Irrlicht.h header file. We also need the
 * irrlicht.lib to be found, so stay in that dialog, select 'Libraries' in the
 * combo box and add the \lib\VisualStudio directory. That's it, with your IDE
 * set up like this, you will now be able to develop applications with the
 * Irrlicht Engine.
 */
//#include <irrlicht.h>

/*
 * In the Irrlicht Engine, everything can be found in the namespace 'irr'. So if
 * you want to use a class of the engine, you have to write an irr:: before the
 * name of the class. For example to use the IrrlichtDevice write:
 * irr::IrrlichtDevice. To get rid of the irr:: in front of the name of every
 * class, we tell the compiler that we use that namespace from now on, and we
 * will not have to write that 'irr::'.
 */
//using namespace irr;

/*
 * There are 5 sub namespaces in the Irrlicht Engine. Take a look at them, you
 * can read a detailed description of them in the documentation by clicking on
 * the top menu item 'Namespace List' or using this link:
 * http://irrlicht.sourceforge.net/docu/namespaces.html. Like the irr Namespace,
 * we do not want these 5 sub namespaces now, to keep this example simple. Hence
 * we tell the compiler again that we do not want always to write their names:
 */
//using namespace core;
//using namespace scene;
//using namespace video;
//using namespace io;
//using namespace gui;

public class TestHelloWorld
{

/*
 * To be able to use the Irrlicht.DLL file, we need to link with the
 * Irrlicht.lib. We could set this option in the project settings, but to make
 * it easy, we use a pragma comment lib:
 */
//#pragma comment(lib, "Irrlicht.lib")

static
{
   System.loadLibrary("jirr");
}

/*
 * This is the main method. We can use void main() on every platform. On Windows
 * platforms, we could also use the WinMain method if we would want to get rid
 * of the console window, which pops up when starting a program with main(), but
 * to keep this example simple, we use main().
 */

//int main()
public static void main(String argv[])
{
	/*
	 * The most important function of the engine is the 'createDevice' function.
	 * The Irrlicht Device can be created with it, which is the root object for
	 * doing everything with the engine. createDevice() has 6 paramters:
	 * deviceType: Type of the device. This can currently be the Null-device,
	 * the Software device, DirectX8, DirectX9, or OpenGL. In this example we
	 * use EDT_SOFTWARE, but to try out, you might want to change it to
	 * EDT_NULL, EDT_DIRECTX8 , EDT_DIRECTX9, or EDT_OPENGL. windowSize: Size of
	 * the Window or FullscreenMode to be created. In this example we use
	 * 640x480. bits: Amount of bits per pixel when in fullscreen mode. This
	 * should be 16 or 32. This parameter is ignored when running in windowed
	 * mode. fullscreen: Specifies if we want the device to run in fullscreen
	 * mode or not. stencilbuffer: Specifies if we want to use the stencil
	 * buffer for drawing shadows. eventReceiver: An object to receive events.
	 * We do not want to use this parameter here, and set it to 0.
	 */

//// IrrlichtDevice *device =
//// createDevice(EDT_SOFTWARE, dimension2d<s32>(512, 384), 16, false, false,
// 0);

	//IrrlichtDevice device = Jirr.createDeviceJava(E_DRIVER_TYPE.swigToEnum(4), 640, 480, 16, false);
	//IEventReceiver receiver = new IEventReceiver(0,false);
	//SWIGTYPE_p_irr__core__vector3dTfloat_t t = Jirr.createVector3df32((float)1.0,(float)1.0,(float)1.0);
	
	MyEventReciever myreceiver = new MyEventReciever();
	IrrlichtDevice device = Jirr.createDeviceJava(E_DRIVER_TYPE.EDT_OPENGL, 640, 480, 16, false, false, false, myreceiver);
	//IrrlichtDevice device = Jirr.createDeviceJava(E_DRIVER_TYPE.swigToEnum(4), 640, 480, 16, false, false, false, null);

	/*
	 * Set the caption of the window to some nice text. Note that there is a 'L'
	 * in front of the string. The Irrlicht Engine uses wide character strings
	 * when displaying text.
	 */
	//SWIGTYPE_p_long windowCaption;// = Jirr.createWchar("Java@Hello World! -
	// Irrlicht Engine Demo");
	device.setWindowCaption(Jirr.createWchar("Kmedjen"));

	/*
	 * Get a pointer to the video driver, the SceneManager and the graphical
	 * user interface environment, so that we do not always have to write
	 * device->getVideoDriver(), device->getSceneManager() and
	 * device->getGUIEnvironment().
	 */
	IVideoDriver driver = device.getVideoDriver();
	ISceneManager smgr = device.getSceneManager();
	IGUIEnvironment guienv = device.getGUIEnvironment();

	/*
	 * We add a hello world label to the window, using the GUI environment.
	 */
	//SWIGTYPE_p_wchar_t text = Jirr.createWchar("Hello World! This is the
	// Irrlicht Software engine for Java!");
	//SWIGTYPE_p_irr__core__rectTint_t rect = Jirr.createRectS32(10, 10, 280, 30);
	// new IGUIElement() <=> IGUIElement * = 0 in Cpp;
	//IGUIStaticText staticText = guienv.addStaticText(text, rect, true, true,
	// new IGUIElement(), Jirr.createS32(-1));
	
	/*
	 * To display something interesting, we load a Quake 2 model and display it.
	 * We only have to get the Mesh from the Scene Manager (getMesh()) and add a
	 * SceneNode to display the mesh. (addAnimatedMeshSceneNode()). Instead of
	 * writing the filename sydney.md2, it would also be possible to load a Maya
	 * object file (.obj), a complete Quake3 map (.bsp) or a Milshape file
	 * (.ms3d). By the way, that cool Quake 2 model called sydney was modelled
	 * by Brian Collins.
	 */
	//IAnimatedMesh mesh = smgr.getMesh(Jirr.createC8("../media/sydney.md2"));
	//IAnimatedMeshSceneNode node = smgr.addAnimatedMeshSceneNode(mesh, new
	// ISceneNode(), Jirr.createS32(-1), Jirr.createVector3dF32(0,0,0),
	// Jirr.createVector3dF32(0,0,0), Jirr.createVector3dF32(1.0f,1.0f,1.0f));

	/*
	 * To let the mesh look a little bit nicer, we change its material a little
	 * bit: We disable lighting because we do not have a dynamic light in here,
	 * and the mesh would be totally black. Then we set the frame loop, so that
	 * the animation is looped between the frames 0 and 310. And at last, we
	 * apply a texture to the mesh. Without it the mesh would be drawn using
	 * only a color. if (node != null) {
	 * Jirr.superISceneNode(node).setMaterialFlag(Jirr.EMF_LIGHTING, false);
	 * node.setFrameLoop(Jirr.createS32(0), Jirr.createS32(99999));
	 * Jirr.superISceneNode(node).setMaterialTexture(Jirr.createS32(0),
	 * driver.getTexture(Jirr.createC8("../media/sydney.bmp"))); }
	 */

	/*
	 * To look at the mesh, we place a camera into 3d space at the position
	 *  (0, 10, -40). The camera looks from there to (0,0,0).
	 */
	//smgr.addCameraSceneNode(new ISceneNode(), Jirr.createVector3dF32(0,10,-40), Jirr.createVector3dF32(0,0,0), -1);

	/*
	 * Ok, now we have set up the scene, lets draw everything: We run the device
	 * in a while() loop, until the device does not want to run any more. This
	 * would be when the user closed the window or pressed ALT+F4 in windows.
	 */
	
	final int step = 100;
	int counter = 0;
	double diff = 0;
	long timer1 = System.currentTimeMillis();
	long timer2 = 0;
	while(device.run())
	{
		/*
		 * Anything can be drawn between a beginScene() and an endScene() call.
		 * The beginScene clears the screen with a color and also the depth
		 * buffer if wanted. Then we let the Scene Manager and the GUI
		 * Environment draw their content. With the endScene() call everything
		 * is presented on the screen. SWIGTYPE_p_s32 a = Jirr.createS32(0);
		 * SWIGTYPE_p_s32 r = Jirr.createS32(100); SWIGTYPE_p_s32 g =
		 * Jirr.createS32(100); SWIGTYPE_p_s32 b = Jirr.createS32(100);
		*/ 
		driver.beginScene(true, true, new SColor(100, 100, 100, 100));
		 
		smgr.drawAll(); 
		guienv.drawAll();
		 
		counter++;
		if (counter >= step)
		{
			timer2 = System.currentTimeMillis(); diff = 1000.0 / ((timer2 - timer1) /(double) step);
			counter = 0;
			timer1 = System.currentTimeMillis();
			//text = Jirr.createWchar("This is the Irrlicht Software engine for Java! FPS: " + diff); 
			//staticText.setText(text);
			System.out.println("This is the Irrlicht Software engine for Java! FPS: " + diff);
		}
		driver.endScene();
	}

	/*
	 * After we are finished, we have to delete the Irrlicht Device created
	 * before with createDevice(). In the Irrlicht Engine, you will have to
	 * delete all objects you created with a method or function which starts
	 * with 'create'. The object is simply deleted by calling ->drop(). See the
	 * documentation at
	 * http://irrlicht.sourceforge.net//docu/classirr_1_1IUnknown.html#a3 for
	 * more information.
	 */
	device.closeDevice();
}
}
