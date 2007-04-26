/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import com.xenoage.bp2k6.tools.stadiumeditor.StadiumEditor;

import javax.swing.*;


/**
 * This class is used in Linux
 * to display the palette bar in
 * a window.
 * 
 * @author Andreas Wenger
 */
public class PaletteWindow
  extends JFrame
{
  private PalettePanel palettePanel;
  
  
  /**
   * Constructor.
   */
  public PaletteWindow(MainFrame mainFrame, StadiumEditor editor)
  {
    this.setTitle("");
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    palettePanel = new PalettePanel(mainFrame, editor, this);
    this.add(palettePanel);
;
    this.pack();
  }
  
  
  /**
   * Returns the palette panel.
   */
  public PalettePanel getPalettePanel()
  {
    return palettePanel;
  }

  
}