/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.tools.teameditor.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * This is the main frame of the
 * team editor (developer mode).
 * 
 * @author Andreas Wenger
 */
public class MainFrame
  extends JFrame
  implements WindowListener, ComponentListener
{
  //the editor main class
  TeamEditor editor;
  
  //this JFrame: allow "this" for inner classes
  JFrame thisFrame;
  
  //the main tab control
  JTabbedPane tabMain; 
  int windowWidth = 1000;
  int windowHeight = 700;
  
  //tab pages
  TeamDataPanel teamDataPanel;
  CareerPanel careerPanel;
  WorldCupPanel worldCupPanel;
  StatisticsPanel statisticsPanel;
  
  
  /**
   * Constructor.
   */
  public MainFrame(TeamEditor editor)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Loading team editor window...");
    
    this.editor = editor;
    thisFrame = this;
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    //title of stadium editor
    this.setTitle(Language.get("teameditor_title") +
      " - " + Language.get("version") + " " + editor.version);
    
    //general settings
    this.setIconImage(new ImageIcon("data/images/icons/teameditor.png").getImage());
    this.setSize(1024, 768);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.addWindowListener(this);
    this.addComponentListener(this);
    this.setLayout(null);
    
    //create the four pages
    tabMain = new JTabbedPane();
    tabMain.setSize(windowWidth, windowHeight);
    teamDataPanel = new TeamDataPanel(this);
    tabMain.addTab(Language.get("teamdata"), teamDataPanel);
    careerPanel = new CareerPanel(this);
    tabMain.addTab(Language.get("career"), careerPanel);
    worldCupPanel = new WorldCupPanel(this);
    tabMain.addTab(Language.get("worldcup"), worldCupPanel);
    statisticsPanel = new StatisticsPanel(this);
    tabMain.addTab(Language.get("statistics"), statisticsPanel);
    this.add(tabMain);
    tabMain.addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent e)
      {
        pageSelected(tabMain.getSelectedIndex());
      }
    });
    
    Logging.log(Logging.LEVEL_MESSAGES, this, "Team editor loaded.");
  }
  
  
  /**
   * This method is called when the user selected another page.
   */
  public void pageSelected(int index)
  {
    editor.pageSelected(tabMain.getSelectedIndex());
  }


  public void keyPressed(KeyEvent e)
  {
    //editor.keyPressed(e);
  }


  public void windowOpened(WindowEvent e)
  {
  }


  public void windowClosing(WindowEvent e)
  {
    pageSelected(-1); //close current tab
    editor.close();
  }


  public void windowClosed(WindowEvent e)
  {
  }


  public void windowIconified(WindowEvent e)
  {
  }


  public void windowDeiconified(WindowEvent e)
  {
  }


  public void windowActivated(WindowEvent e)
  {
  }


  public void windowDeactivated(WindowEvent e)
  {
  }


  public void componentResized(ComponentEvent e)
  {
    int x = (this.getWidth() - tabMain.getWidth()) / 2;
    int y = (this.getHeight() - tabMain.getHeight()) / 2 - 20;
    if (x < 0) x = 0;
    if (y < 0) y = 0;
    tabMain.setLocation(x, y);
  }


  public void componentMoved(ComponentEvent e)
  { 
  }


  public void componentShown(ComponentEvent e)
  {
  }


  public void componentHidden(ComponentEvent e)
  {
  }


  
  public int getWindowHeight()
  {
    return windowHeight - 30;
  }


  
  public int getWindowWidth()
  {
    return windowWidth;
  }


  /**
   * Gets the team data page.
   */
  public TeamDataPanel getTeamDataPanel()
  {
    return teamDataPanel;
  }
  
  
  /**
   * Gets the career page.
   */
  public CareerPanel getCareerPanel()
  {
    return careerPanel;
  }
  
  
  /**
   * Gets the World Cup page.
   */
  public WorldCupPanel getWorldCupPanel()
  {
    return worldCupPanel;
  }
  
  
  /**
   * Gets the statistics page.
   */
  public StatisticsPanel getStatisticsPanel()
  {
    return statisticsPanel;
  }
  
  
}