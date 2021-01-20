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
package com.xenoage.bp2k6.gui;

import net.sf.jirr.*;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.gui.font.*;
import com.xenoage.bp2k6.gui.loader.GUILoader;
import com.xenoage.bp2k6.input.*;
import com.xenoage.bp2k6.match.camera.Flight;
import com.xenoage.bp2k6.util.*;
import java.util.ArrayList;


/**
 * The <code>UIManager</code> class organizes the display
 * an actions of the GUI.
 *
 * @author   Andi
 */
public class UIManager
{
  
  public static final SColor COLOR_WHITE = new SColor(255, 255, 255, 255);
  public static final SColor[] COLOR_WHITE_ARRAY4 = new SColor[]{
    COLOR_WHITE, COLOR_WHITE, COLOR_WHITE, COLOR_WHITE
  };
  public static final String FONT_DEFAULT = "font1";
  
  
  //scaling factor (screen resolution dependent)
  private float scaleX;
  private float scaleY;

  //the current screen
  private Screen currentScreen = null;
  //the next screen, which is activated when the current screen is closed,
  //or null, if the program should be closed after the current screen
  private Screen nextScreen = null;
  //the id of the start group of the next screen, or null if
  //the default start group should be shown
  private String nextScreenStartGroup = null;
  
  //the current gui scene
  private GUIScene currentGUIScene = null;
  //the gui scene of the next screen
  private String nextGUISceneID = null;
  
  //the current background music (or null)
  private String backgroundMusic;

  //list of all GUI fonts
  private ArrayList<Font> fonts = new ArrayList<Font>();

  //image with templates for GUI elements (buttons, ...)
  private ITexture imageGUI;
  
  //gui sounds
  private GUISounds guiSounds;


  public UIManager()
  {
    
    //compute scaling factor
    dimension2di dimScreen = GameEngine.getNativeScreenDimension();
    scaleX = dimScreen.getWidth() / 1024f;
    scaleY = dimScreen.getHeight() / 768f;

    //load fonts
    IVideoDriver videoDriver = GameEngine.getVideoDriver();
    fonts.add(new GameFont("timescore", videoDriver)); //time- and score-font
    fonts.add(new GameFont("font1", videoDriver)); //main font

    //load GUI templates image
    String imageGUIFile = "gui.png";
    imageGUI = GameEngine.getVideoDriver().getTexture("data/images/" + imageGUIFile);
    if (imageGUI == null)
    {
      GameEngine.fatalError(this,
        new Exception("GUI templates image \"" + imageGUIFile +
          "\" could not be loaded."));
    }
    else
    {
      //Main.getVideoDriver().makeColorKeyTexture(imageGUI, new position2di(0, 0));
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "GUI templates image \"" + imageGUIFile + "\" loaded.");
    }
    
    //load gui sounds
    guiSounds = new GUISounds();

  }


  /**
   * Gets the currently displayed screen
   * @return   the current screen
   */
  public Screen getCurrentScreen()
  {
    return this.currentScreen;
  }


  /**
   * Sets the currently displayed screen. This function should
   * only be called at the very beginning, because a screen should
   * notbe replaced without a transition effect (for example fading).
   * Use the <code>setNextScreenID</code> method instead.
   * @param img   the new current screen
   */
  public void setCurrentScreen(Screen screen)
  {
    this.currentScreen = screen;
  }


 /**
  * Gets the screen, that will be shown when the
  * current screen is closed. Can also be <code>null</code>, which means
  * that the program will be exited when the current screen is closed.
  * @return   the screen that will be shown next, or <code>null</code>
  */
 public Screen getNextScreen()
 {
   return this.nextScreen;
 }


 /**
  * Closes the current screen and opens the given one.
  * Then the group with the given groupID is activated.
  * @param screenID   The ID of the screen to open
  * @param groupID    The ID of the group to show, or <code>null</code>
  *                   if the default start group should be shown.
  */
  public void openScreen(String screenID, String groupID)
  {
    if (screenID == null)
      this.nextScreen = null;
    else
    {
      Screen s = GUILoader.loadScreen(screenID);
      this.nextScreen = s;
      this.nextScreenStartGroup = groupID;
    }
    currentScreen.close();
  }
  
  
  /**
   * Closes the current screen and closes the program.
   */
   public void closeScreenAndExit()
   {
     this.nextScreen = null;
     currentScreen.close();
   }


  /**
   * draws the current screen and process user input
   * call this function in the main loop
   */
  public void run()
  {
    if (currentScreen == null)
    {
      //no screen to display. should not happen. exit program
      GameEngine.fatalError(this, new Exception("No screen to display!"));
    }
    else if (currentScreen.getStatus() != Screen.STATUS_CLOSED)
    {
      //draw gui scene
      if (currentGUIScene != null)
        currentGUIScene.run();
      //draw the current screen
      currentScreen.draw();
    }
    else
    {
      //screen closed. load another screen?
      if (nextScreen != null)
      {
        openScreen(nextScreen, nextScreenStartGroup);
        nextScreen = null;
        //draw the current screen
        currentScreen.draw();
      }
      else
      {
        //no screen follows. regular end of the program.
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Next screen is null. Regular end of the program.");
        GameEngine.exit();
      }
    }
  }

  /**
   * This method processes the input actions from the user.
   * Call it, whenever a new input event is received.
   * @param event   The input event
   */
  public void input(InputEvent event)
  {
    if (event instanceof KeyboardEvent)
    {
      KeyboardEvent kbEvent = (KeyboardEvent) event;
      if (!kbEvent.pressed) return; //ignore keyup events
      currentScreen.input(kbEvent);
    }
    else if (event instanceof MouseEvent)
    {
      currentScreen.input((MouseEvent) event);
    }
  }


  /**
   * Opens the specified screen and shows it immediately.
   * Only call this method in special situations like
   * the program start.
   * @param id   the id of the screen to display now
   */
  public void openScreenNow(String id)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Opening screen \"" + id + "\"");
    Screen s = GUILoader.loadScreen(id);
    if (s != null)
    {
      openScreen(s, null);
    }
    else
    {
      GameEngine.fatalError(this, new Exception("Screen \"" + id + "\" could not be loaded!"));
    }
  }


  /**
   * Opens the specified screen and shows it immediately.
   * Only call this method in special situations like
   * the program start.
   * It is also called by <code>run()</code> when it is
   * time to display the next screen.
   * @param screen   the next screen to display now
   * @param group    the ID of the start group, or <code>null</code>
   *                 if the default start group should be shown
   */
  public void openScreen(Screen screen, String group)
  {
    if (screen != null)
    {
      
      //remove old gui scene from irrlicht scene
      if (currentGUIScene != null)
      {
        currentGUIScene.removeFromScene();
      }
      //if it was the match screen, remove all match objects
      if (currentScreen != null &&
        currentScreen.getProgram() == Screen.Program.MATCH)
      {
        GameEngine.getMatchManager().clearMatch();
      }
      
      //new gui scene?
      if (nextGUISceneID != null && 
        (currentGUIScene == null || !currentGUIScene.getID().equals(nextGUISceneID)))
      {
        //create new gui scene
        currentGUIScene = new GUIScene(nextGUISceneID,
          GameEngine.getSceneManager(), GameEngine.getVideoDriver());
        nextGUISceneID = null;
      }
      else if (nextGUISceneID == null)
      {
        currentGUIScene = null;
      }
      //start flight
      if (screen.getGUISceneStartFlightID() != null && currentGUIScene != null)
      {
        currentGUIScene.setCameraFlightByID(screen.getGUISceneStartFlightID());
      }
      
      //when program is "match", start a new match
      if (screen.getProgram() == Screen.Program.MATCH)
        GameEngine.getMatchManager().startMatch();
      
      backgroundMusic = screen.getMusic();
      screen.open(group);
      this.setCurrentScreen(screen);


    }
    else
    {
      this.setCurrentScreen(null);
    }
  }


  /**
   * Gets the GUI font with the given id,
   * or <code>null</code> if not found.
   * If an empty string or null is given,
   * the default font is returned.
   */
  public Font getFont(String id)
  {
    String fontid = id;
    if (id == null || id.length() == 0)
      fontid = FONT_DEFAULT;
    for (int i = 0; i < fonts.size(); i++)
    {
      if (fonts.get(i).getID().equals(fontid))
        return fonts.get(i);
    }
    return null;
  }


  /**
   * Gets the GUI templates image
   */
  public ITexture getTemplatesImage()
  {
    return imageGUI;
  }


  /**
   * Gets the current background music id (or null)
   */
  public String getBackgroundMusic()
  {
    return backgroundMusic;
  }
  
  
  /**
   * Called by the <code>Screen</code> class when it
   * is loaded and the ID of a <code>GUIScene</code> is set.
   */
  public void setNextGUIScene(String id)
  {
    nextGUISceneID = id;
  }
  
  
  /**
   * Starts the given camera flight in the
   * current scene.
   */
  public void startCameraFlight(String id)
  {
    if (currentGUIScene != null)
    {
      currentGUIScene.setCameraFlightByID(id);
    }
  }
  
  
  /**
   * Gets a camera flight by the given id.
   */
  public Flight getCameraFlight(String id)
  {
    if (currentGUIScene != null)
    {
      return currentGUIScene.getCameraFlightByID(id);
    }
    return null;
  }
  
  
  /**
   * Gets the GUISounds manager.
   */
  public GUISounds getGUISounds()
  {
    return guiSounds;
  }


  /**
   * Gets the screen resolution dependent
   * horizontal scaling factor.
   */
  public float getScaleX()
  {
    if (GameEngine.isRenderingToTexture())
      return Fade.getTextureSize() / 1024f;
    else
      return scaleX;
  }


  /**
   * Gets the screen resolution dependent
   * vertical scaling factor.
   */
  public float getScaleY()
  {
    if (GameEngine.isRenderingToTexture())
      return Fade.getTextureSize() / 768f;
    else
      return scaleY;
  }
  
  
  /**
   * Run the current gui program.
   */
  public void runProgram()
  {
    currentScreen.runProgram();
  }


  /**
   * Gets the current GUI scene.
   */
  public GUIScene getCurrentGUIScene()
  {
    return currentGUIScene;
  }
  

}
