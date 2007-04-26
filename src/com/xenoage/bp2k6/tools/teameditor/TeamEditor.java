/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.tools.teameditor.ctrl.*;
import com.xenoage.bp2k6.tools.teameditor.gui.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This is the main class of the
 * team editor (developer mode).
 * 
 * @author Andreas Wenger
 */
public class TeamEditor
{
  
  public static final String version = "1.1";

  //main window
  MainFrame mainFrame;
  
  //controllers
  TeamDataCtrl teamDataCtrl;
  CareerCtrl careerCtrl;
  WorldCupCtrl worldCupCtrl;
  StatisticsCtrl statisticsCtrl;
  
  //last selected tab page
  int lastTabIndex = 0;
  
  
  /**
   * Constructor.
   * Starts the team editor.
   */
  public TeamEditor()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Starting the team editor...");
    mainFrame = new MainFrame(this);
 
    //create controllers
    teamDataCtrl = new TeamDataCtrl(mainFrame.getTeamDataPanel());
    careerCtrl = new CareerCtrl(mainFrame.getCareerPanel());
    worldCupCtrl = new WorldCupCtrl(mainFrame.getWorldCupPanel());
    statisticsCtrl = new StatisticsCtrl(mainFrame.getStatisticsPanel());
    
    //open first page
    teamDataCtrl.pageActivated();
    
    //show window
    mainFrame.setVisible(true);
  }
  
  
  /**
   * Starts the team editor standalone application.
   */
  public static void main(String[] args)
  {
    //clear the temp folder
    FileUtils.clearTempFolder();
    
    Main.setFatalErrorMsgBoxEnabled(true);
    Language.loadLanguage(Language.getSelectedLanguageID());
    
    //load Irrlicht library
    Main.loadIrrlichtLibrary();
    
    new TeamEditor();
  }
  
  
  /**
   * Closes the team editor
   */
  public void close()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Team editor is closing...");
    Logging.close();
    System.exit(0);
  }
  
  
  /**
   * This method is called when another tab page is
   * selected.
   */
  public void pageSelected(int index)
  {
    //close old tab
    if (lastTabIndex == 0)
      teamDataCtrl.pageClosed();
    else if (lastTabIndex == 1)
      careerCtrl.pageClosed();
    else if (lastTabIndex == 2)
      worldCupCtrl.pageClosed();
    else if (lastTabIndex == 3)
      statisticsCtrl.pageClosed();
    //open new tab
    int newTabIndex = index;
    if (newTabIndex == 0)
      teamDataCtrl.pageActivated();
    else if (newTabIndex == 1)
      careerCtrl.pageActivated();
    else if (newTabIndex == 2)
      worldCupCtrl.pageActivated();
    else if (newTabIndex == 3)
      statisticsCtrl.pageActivated();
    lastTabIndex = newTabIndex;
  }
  
}