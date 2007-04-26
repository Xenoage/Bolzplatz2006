/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6;

import javax.swing.JOptionPane;

import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This is the start class of Bolzplatz 2006,
 * which contains also the initialization of the libraries.
 *
 * @author   Andi
 */
public class Main
{
  
  private static Main mainObject;
  private static boolean fatalErrorMsgBoxEnabled = true;
  

  public void run()
  {

    //Logging: Init logging and log everything at the moment
    Logging.init();
    Logging.setLoggingLevel(Logging.LEVEL_ALL);
    //Catch all uncaught exceptions
    try
    {
      //load language pack
      String lang = Language.getSelectedLanguageID();
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Selected language is \"" + lang + "\"");
      Language.loadLanguage(lang);
      //Try to load Jirr/Irrlicht library
      loadIrrlichtLibrary();
      //Initialize game engine
      GameEngine.init();
      //Main loop
      GameEngine.mainloop();
      //Close logging
      Logging.close();
    }
    catch (Exception ex)
    {
      //An uncaught exception occurred. Program must be closed.
      fatalError(this, ex);
    }
  }
  
  
  public void kill()
  {
    GameEngine.exit();
  }
  

  public static void main(String[] args)
  {
    mainObject = new Main();
    mainObject.run();
  }


  /**
   * Log a fatal error and close
   */
  public static void fatalError(Object sender, Exception ex)
  {
    Logging.log(Logging.LEVEL_ERRORS, sender,
      "Fatal error. Program must be closed. Details:");
    Logging.log(sender, ex);
    if (fatalErrorMsgBoxEnabled)
    {
      String errMsg = Language.getWithNull("fatalerrormessage");
      if (errMsg == null)
      { 
        errMsg = "Fatal error. Program must be closed.\n" +
          "See log.txt for details.";
      }
      JOptionPane.showMessageDialog(null, errMsg,
        GameEngine.PROJECT_NAME_VERSION, JOptionPane.ERROR_MESSAGE);
    }
    exit();
  }
  
  
  /**
   * Show message box or not, if a fatal error happens.
   */
  public static void setFatalErrorMsgBoxEnabled(boolean b)
  {
    fatalErrorMsgBoxEnabled = b;
  }


  /**
   * Close the program immediately
   */
  public static void exit()
  {
    //clean ressources
    //not done... works anyway.
    Logging.log(Logging.LEVEL_MESSAGES, "Main.exit()");
    //Try to close game engine
    GameEngine.exit();
    Logging.close();
    System.exit(0);
  }
  
  
  /**
   * Loads the Irrlicht/Jirr library.
   */
  public static void loadIrrlichtLibrary()
  {
    Logging.log(Logging.LEVEL_MESSAGES, null, "Load Jirr library...");
    try
    {
      System.loadLibrary("irrlicht_wrap");
      Logging.log(Logging.LEVEL_MESSAGES, null,
        "Jirr was loaded successfully.");
    }
    catch (UnsatisfiedLinkError err)
    {
      //Jirr is a must-have. Without it we can not start.
      fatalError(null,
        new Exception("Jirr could not be loaded. Installation is corrupt."));
    }
  }

}
