/**
 * Bolzplatz 2006
 * Copyright (C) 2006 / 2021 by Xenoage Software
 * 
 * This file is part of the Gold-Edition
 * of Bolzplatz 2006 and was originally not released under the GPL.
 * 2021-01-15 the Gold-Edition was released under the GPL like
 * the other code of this game.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import com.xenoage.bp2k6.tools.stadiumeditor.*;

import net.sf.jirr.*;


/**
 * This is the interface for the
 * Irrlicht window and panel.
 * 
 * @author Andreas Wenger
 */
public interface IrrlichtInterface
{

  
  public void createIrrlichtDevice(int width, int height);
  
  public void paint();
  
  public Camera getCamera();
  
  public void setStadiumGridVisible(boolean b);
  
  public ISceneManager getSceneManager();
  
  public IVideoDriver getVideoDriver();
  
}
