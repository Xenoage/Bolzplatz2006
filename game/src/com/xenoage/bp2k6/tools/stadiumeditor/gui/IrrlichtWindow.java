/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import java.awt.event.*;
import java.util.List;

import org.w3c.dom.*;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.input.KeyboardEvent;
import com.xenoage.bp2k6.match.stadium.StadiumGrid;
import com.xenoage.bp2k6.tools.stadiumeditor.*;
import com.xenoage.bp2k6.util.*;
import net.sf.jirr.*;


/**
 * This is the window the stadium editor
 * uses for the 3d view (work space)
 * in Linux.
 * 
 * @author Andreas Wenger
 */
public class IrrlichtWindow
  implements IrrlichtInterface
{
  
  private MainFrame mainFrame;
  
  private boolean deviceCreated = false;
  
  private IrrlichtDevice irrlichtDevice = null;
  private IVideoDriver videoDriver = null;
  private ISceneManager sceneManager = null;
  private ICameraSceneNode irrlichtCamera = null;
  
  private SColor backgroundColor;
  
  private Camera camera;
  private StadiumGrid stadiumGrid;
  
  boolean locked = false;
  
  
  public IrrlichtWindow(MainFrame mainFrame)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Create Irrlicht window...");
    this.mainFrame = mainFrame;
    //load irrlicht library
    Main.loadIrrlichtLibrary();
  }
  
  
  /**
   * Creates the Irrlicht device.
   */
  public void createIrrlichtDevice(int width, int height)
  {
    if (!deviceCreated)
    {
      //create irrlicht device
      Logging.log(Logging.LEVEL_MESSAGES, this, "Create Irrlicht device...");
      IEventReceiver myreceiver = new MyEventReciever();
      irrlichtDevice = Jirr.createDevice(E_DRIVER_TYPE.EDT_OPENGL,
        new dimension2di(width, height), 32, false, false, false, myreceiver);
      
      //show main frame
      //(show it after creating the Irrlicht window, otherwise
      //the windows have the wrong position)
      mainFrame.setVisible(true);
      
      //get other irrlicht objects
      videoDriver = irrlichtDevice.getVideoDriver();
      sceneManager = irrlichtDevice.getSceneManager();
      backgroundColor = new SColor(255, 200, 200, 200);
      Logging.log(Logging.LEVEL_MESSAGES, this, "Irrlicht created.");
      
      //init texture factory
      TextureFactory.init(videoDriver);
      
      //no fog
      videoDriver.setFog(new SColor(), true,
        100000, 200000); //is this really "fog off"?
      
      //create camera
      irrlichtCamera = sceneManager.addCameraSceneNode();
      camera = new Camera(irrlichtCamera);
      
      //create light
      sceneManager.setAmbientLight(new SColorf(0.8f, 0.8f, 0.8f, 1f));

      //create light
      sceneManager.addLightSceneNode(null,
        new vector3df(0, 200, 0), new SColorf(1f, 1f, 1f, 1f), 100000.0f);
      
      //create stadium grid
      stadiumGrid = new StadiumGrid(sceneManager, videoDriver);
      
      //preload important textures
      //(otherwise they are not shown, no idea why...)
      try
      {
        Document doc = XMLReader.readFile("data/meshes/stadiumeditor-preload.xml");
        Node root = doc.getFirstChild();
        List<Element> listTextures = XMLReader.elements(root, "texture");
        for (int i = 0; i < listTextures.size(); i++)
        {
          Node eTexture = listTextures.get(i);
          videoDriver.getTexture("data/meshes/" + eTexture.getTextContent());
        }
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Could not load the preload list!");
      }
      
      
      deviceCreated = true;  
      
      //in Linux: mainframe is loaded now
      if (!mainFrame.getEditor().isOSWindows())
      {
        mainFrame.irrlichtLoaded();
      }
      
      while (irrlichtDevice.run())
      {
        paint();
      }
      
      /* //Irrlicht thread
      irrlichtThread = new Thread()
      {
        @Override public void run()
        {
          Logging.log(Logging.LEVEL_MESSAGES, null,
            "Thread for Irrlicht started.");
          while (irrlichtDevice.run())
          {
            try
            {
              paint();
              sleep(500);
            }
            catch (InterruptedException ex)
            {
            }
          }
          Logging.log(Logging.LEVEL_MESSAGES, null,
            "Thread for Irrlicht ended.");
        }
      };
      irrlichtThread.start(); */
    }
  }
  
  
  synchronized public void paint()
  {
    if (!locked && deviceCreated && irrlichtDevice.run())
    {
      videoDriver.beginScene(true, true, backgroundColor);
      sceneManager.drawAll();
      videoDriver.endScene();
    }
  }
  
  
  /**
   * Call this method when the stadium will be modified.
   * This ensures that the Irrlicht window is not repainted
   * until endEdit() is called, which may cause a crash.
   */
  synchronized public void startEdit()
  {
    locked = true;
  }
  
  
  /**
   * Call this method when the modification of the
   * stadium is done. Then the Irrlicht window
   * may be repainted again.
   */
  synchronized public void endEdit()
  {
    locked = false;
  }
  
  
  public Camera getCamera()
  {
    return camera;
  }
  
  
  public void setStadiumGridVisible(boolean b)
  {
    stadiumGrid.setVisible(b);
  }
  
  
  /**
   * Gets the Irrlicht scene manager.
   */
  public ISceneManager getSceneManager()
  {
    return sceneManager;
  }
  
  
  /**
   * Gets the Irrlicht video driver.
   */
  public IVideoDriver getVideoDriver()
  {
    return videoDriver;
  }
  
  
  /**
   * Irrlicht event.
   */
  private class MyEventReciever extends IEventReceiver
  {
    @Override public boolean OnEvent(SEvent event)
    {
      if (event.getEventType() == EEVENT_TYPE.EET_MOUSE_INPUT_EVENT)
      {
        EMOUSE_INPUT_EVENT mouseEvent = event.getMouseInputEvent();
        if (mouseEvent.equals(EMOUSE_INPUT_EVENT.EMIE_MOUSE_WHEEL))
        {
          //mouse: wheel
          if (event.getMouseInputWheel() > 0)
            camera.zoomIn();
          else
            camera.zoomOut();
        }
        else if (mouseEvent.equals(EMOUSE_INPUT_EVENT.EMIE_LMOUSE_LEFT_UP))
        {
          //mouse: left click
          ISceneCollisionManager coll = sceneManager.getSceneCollisionManager();
          ISceneNode node = coll.getSceneNodeFromScreenCoordinatesBB(
            new position2di(event.getMouseInputX(), event.getMouseInputY()),
            UniqueIDCreator.BITMASK_STADIUMOBJECTS);
          mainFrame.getEditor().selectNode(node);
        }
      }
      else if (event.getEventType() == EEVENT_TYPE.EET_KEY_INPUT_EVENT)
      {
        //key event
        if (event.isKeyInputPressedDown())
        {
          int keyCode = event.getKeyInputChar(); //TODO: this is a KeyEvent.-constant
          if (keyCode == KeyboardEvent.KEY_DELETE)
            mainFrame.getEditor().keyPressed(KeyEvent.VK_DELETE);
          else
            mainFrame.getEditor().keyPressed(keyCode);
        }
      }
      return true;
    }
  }
  
}
