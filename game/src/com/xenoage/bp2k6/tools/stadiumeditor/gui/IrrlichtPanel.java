/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.stadium.StadiumGrid;
import com.xenoage.bp2k6.tools.stadiumeditor.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.UniqueIDCreator;
import com.xenoage.bp2k6.util.language.Language;
import com.xenoage.util.AWTGetHWnd;
import net.sf.jirr.*;


/**
 * This is the Panel the stadium editor
 * uses for the 3d view (work space)
 * in Windows.
 * 
 * @author Andreas Wenger
 */
public class IrrlichtPanel
  extends Panel
  implements MouseListener, MouseWheelListener, KeyListener, IrrlichtInterface
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
  
  
  public IrrlichtPanel(MainFrame mainFrame)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Create Irrlicht window...");
    this.mainFrame = mainFrame;
    //load irrlicht library
    Main.loadIrrlichtLibrary();
    //add mouse listener
    this.addMouseListener(this);
    //add mouse wheel listener
    this.addMouseWheelListener(this);
    //add key listener
    this.addKeyListener(this);
  }
  
  
  /**
   * Creates the Irrlicht device. Call this method
   * after creating an instance of this class, but not
   * before this component is completely created
   * (use the WindowOpened-event of the parent JFrame).
   */
  public void createIrrlichtDevice(int width, int height)
  {
    if (!deviceCreated)
    {
      
      //get HWnd of window
      Logging.log(Logging.LEVEL_MESSAGES, this, "Get Irrlicht window HWnd...");
      AWTGetHWnd frameHWnd = new AWTGetHWnd();
      int hwnd = frameHWnd.getHWnd(this);
      
      //without this it does not work. Perhaps the system needs the
      //time to create the component...
      JOptionPane.showMessageDialog(this,
        Language.get("stadiumeditor_title") + "\n\n" +
        "(c) 2006 by Andreas Wenger, Xenoage Software",
        Language.get("stadiumeditor_title"),
        JOptionPane.INFORMATION_MESSAGE);
      
      //create irrlicht device
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Create Irrlicht device in window " + hwnd + "...");
      SIrrlichtCreationParameters irrConfig = new SIrrlichtCreationParameters();
      irrConfig.setDriverType(E_DRIVER_TYPE.EDT_OPENGL);
      irrConfig.setWindowId(hwnd);
      irrlichtDevice = Jirr.createDeviceEx(irrConfig);
      
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
      
      deviceCreated = true;  
      repaint();
    }
  }
  
  
  /**
   * Mouse wheel was moved.
   */
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    if (e.getWheelRotation() < 0)
      camera.zoomIn();
    else
      camera.zoomOut();
    repaint();
  }
  
  
  public void paint()
  {
    if (deviceCreated)
    {
      irrlichtDevice.run();
      videoDriver.beginScene(true, true, backgroundColor);
      sceneManager.drawAll();
      videoDriver.endScene();
    }
  }
  
  @Override public void paint(Graphics g)
  {
    paint();
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


  public void mouseClicked(MouseEvent e)
  {
    ISceneCollisionManager coll = sceneManager.getSceneCollisionManager();
    ISceneNode node = coll.getSceneNodeFromScreenCoordinatesBB(
      new position2di(e.getX(), e.getY()), UniqueIDCreator.BITMASK_STADIUMOBJECTS);
    
    mainFrame.getEditor().selectNode(node);
  }


  public void mousePressed(MouseEvent e)
  {
  }


  public void mouseReleased(MouseEvent e)
  {
  }


  public void mouseEntered(MouseEvent e)
  { 
  }


  public void mouseExited(MouseEvent e)
  {
  }
  
  
  public void keyTyped(KeyEvent e)
  { 
  }


 
  public void keyPressed(KeyEvent e)
  {
  }


  public void keyReleased(KeyEvent e)
  {
  }
  
}
