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

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.audio.SoundTrack;
import com.xenoage.bp2k6.gui.programs.WorldCupLogo;
import com.xenoage.bp2k6.input.*;
import com.xenoage.bp2k6.util.Logging;

import java.util.ArrayList;

import net.sf.jirr.*;


/**
 * Class for a GUI screen.
 *
 * @author   Andi
 */
public class Screen
{

  /** The status of this screen is closed. Do not call the <code>draw</code> method. */
  public static int STATUS_CLOSED = 0;
  /** The status of this screen is open. Call the <code>draw</code> method in the main loop. */
  public static int STATUS_OPEN = 1;
  /** The status of this screen is closing, that means it is faded out.
   * Call the <code>draw</code> method in the main loop. */
  public static int STATUS_CLOSING = 2;

  //UIManager that manages this screen
  private UIManager ui = null;
  //id of the screen
  private String id;
  //status: open, closing or closed
  private int status = STATUS_CLOSED;
  //background picture or null
  private ITexture backgroundImage = null;
  private recti backgroundImageRect = null;
  //alternative to background picture: 3d gui scene
  //it is managed by the UIManager, here only its id is stored
  private String guiScene = null;
  //id of the camera start flight of this screen
  private String guiSceneStartFlightID = null;
  //fade-in time in seconds
  private float fadeInTime = 0;
  //fade-in color
  private SColor fadeInColor = new SColor(255, 0, 0, 0);
  //fade-out time in seconds
  private float fadeOutTime = 0;
  //fade-out color
  private SColor fadeOutColor = new SColor(255, 0, 0, 0);
  //use fade out
  private boolean useFadeOut = true;
  //background music or null
  private String music = null;
  //background music fade-in time in seconds
  private float musicFadeInTime = 0;
  //background music fade-out time in seconds
  private float musicFadeOutTime = 0;
  //continue background music, if this screen has the same one as the last screen
  private boolean musicContinue = true;
  
  //time after which screen is closed automatically
  private float automaticCloseTime = 0;
  //screen which follows after closing
  private String nextScreenClose = null;
  
  //list of groups
  private ArrayList<Group> groups = new ArrayList<Group>();
  //the currently active group
  private Group activeGroup = null;
  //the id of the start group
  private String startGroupID;
  
  //special "program" controlled by the game engine (for example the match)
  private Program program = Program.NONE;
  public enum Program { NONE, MATCH, WORLDCUPLOGO };

  //time when the screen was opened (first painted)
  private float timeStart = 0;
  //time when closing command was sent (begin of fade-out)
  private float timeClose = 0;


  public Screen(UIManager ui)
  {
    this.ui = ui;
  }

  /**
   * @return  the ID of this screen
   */
  public String getID()
  {
    return this.id;
  }

  /**
   * @param id    the ID of this screen
   */
  public void setID(String id)
  {
    this.id = id;
  }


  /**
   * Gets the <code>UIManager</code> of this screen.
   * It can for example be used to close this screen and open another one.
   * @return  the UIManager that manages this screen
   */
  public UIManager getUIManager()
  {
    return this.ui;
  }


  /**
   * Gets the background picture of this screen
   * @return   the background texture, or <code>null</code>
   */
  public ITexture getBackgroundImage()
  {
    return this.backgroundImage;
  }

  /**
   * Sets the background picture of this screen
   * @param img   the background texture, or <code>null</code>
   */
  public void setBackgroundImage(ITexture img)
  {
    this.backgroundImage = img;
    if (img != null)
    {
      backgroundImageRect = new recti(0, 0,
        img.getSize().getWidth(), img.getSize().getHeight());
    }
  }

  /**
   * Get the fade-in time of the screen (in seconds). This is the
   * time it takes to fade from complete black to the normal screen colors
   * when the screen is opening.
   * @return   fade-in time in seconds
   */
  public float getFadeInTime()
  {
    return this.fadeInTime;
  }

  /**
   * Set the fade-in time of the screen (in seconds). This is the
   * time it takes to fade from complete black to the normal screen colors
   * when the screen is opening.
   * @param time   fade-in time in seconds
   */
  public void setFadeInTime(float time)
  {
    this.fadeInTime = time;
  }


  /**
   * @return   the fade-in color of this screen
   */
  public SColor getFadeInColor()
  {
    return this.fadeInColor;
  }

  /**
   * @param color   the fade-in color of this screen
   */
  public void setFadeInColor(SColor color)
  {
    this.fadeInColor = color;
  }


  /**
   * Get the fade-out time of the screen (in seconds). This is the
   * time it takes to fade from the normal screen colors to complete black
   * when the screen is closing.
   * @return   fade-out time in seconds
   */
  public float getFadeOutTime()
  {
    return this.fadeOutTime;
  }

  /**
   * Set the fade-out time of the screen (in seconds). This is the
   * time it takes to fade from the normal screen colors to complete black
   * when the screen is closing.
   * @param time   fade-out time in seconds
   */
  public void setFadeOutTime(float time)
  {
    this.fadeOutTime = time;
  }


  /**
   * @return   the fade-out color of this screen
   */
  public SColor getFadeOutColor()
  {
    return this.fadeOutColor;
  }

  /**
   * @param color   the fade-out color of this screen
   */
  public void setFadeOutColor(SColor color)
  {
    this.fadeOutColor = color;
  }


  /**
   * @return   the id of the background music, or <code>null</code>
   */
  public String getMusic()
  {
    return this.music;
  }

  /**
   * Sets the id of the background music. When this screen is opened
   * and it is not already played, this music will be loaded and
   * put into the Music-<code>SoundTrack</code>.
   * Set the fade-in and fade-out time before you call this method!
   * @param music   the id of the background music, or <code>null</code>
   */
  public void setMusic(String music)
  {
    if (music == null || music.length() == 0)
      this.music = null;
    else
      this.music = music;
  }


  /**
   * @return   the fade-in time for the background music in seconds
   */
  public float getMusicFadeInTime()
  {
    return this.musicFadeInTime;
  }

  
  /**
   * @param time   the fade-in time for the background music in seconds
   */
  public void setMusicFadeInTime(float time)
  {
    this.musicFadeInTime = time;
  }


  /**
   * @return   the fade-out time for the background music in seconds
   */
  public float getMusicFadeOutTime()
  {
    return this.musicFadeOutTime;
  }

  /**
   * @param time   the fade-out time for the background music in seconds
   */
  public void setMusicFadeOutTime(float time)
  {
    this.musicFadeOutTime = time;
  }


  /**
   * @return   <code>true</code> (default), if the background music is not restarted,
   *           but played on, if the last screen has the same music as
   *           this screen, otherwise <code>false</code>
   */
  public boolean getMusicContinue()
  {
    return this.musicContinue;
  }

  /**
   * @param musicContinue   <code>true</code> (default), if the
   *                        background music should not be restarted,
   *                        but played on, if the last screen has the same music as
   *                        this screen, otherwise <code>false</code>
   */
  public void setMusicContinue(boolean musicContinue)
  {
    this.musicContinue = musicContinue;
  }


  /**
   * Set the time (in seconds) after which the screen will
   * automatically be closed.
   * @return   automatic closetime in seconds
   */
  public float getAutomaticCloseTime()
  {
    return this.automaticCloseTime;
  }

  /**
   * Set the time (in seconds) after which the screen will
   * automatically be closed.
   * @param time   automatic closetime in seconds
   */
  public void setAutomaticCloseTime(float time)
  {
    this.automaticCloseTime = time;
  }

  /**
   * Get the ID of the screen that follows, when the screen is closed.
   * @return   ID of the next screen
   */
  public String getNextScreenClose()
  {
    return this.nextScreenClose;
  }

  /**
   * Set the ID of the screen that follows, when the screen is closed.
   * @param id   ID of the next screen
   */
  public void setNextScreenClose(String id)
  {
    this.nextScreenClose = id;
  }


  /**
   * Gets the special "program" of this screen, that is
   * controlled by the game engine (for example the match)
   */
  public Program getProgram()
  {
    return program;
  }

  /**
   * Sets the special "program" of this screen, that is
   * controlled by the game engine (for example the match)
   */
  public void setProgram(String program)
  {
    if (program == null)
      this.program = Program.NONE;
    else if (program.equals("match"))
      this.program = Program.MATCH;
    else if (program.equals("worldcuplogo"))
      this.program = Program.WORLDCUPLOGO;
    else
      this.program = Program.NONE;
  }



  /**
   * Draws the screen. Call this function in the main loop
   * between <code>driver.beginScene()</code> and
   * <code>driver.endScene()</code>.
   */
  public void draw()
  {
    //how long is this screen active already?
    float timeScreenActive = GameEngine.getFrameTime(false) - timeStart;
    //get screen rect
    recti rectScreen = GameEngine.getNativeScreenRect();
    //draw background
    IVideoDriver driver = GameEngine.getVideoDriver();
    if (this.backgroundImage != null)
    {
      driver.draw2DImage(backgroundImage, rectScreen,
        backgroundImageRect, null, UIManager.COLOR_WHITE_ARRAY4, false);
    }
    //draw all groups
    for (int i = 0; i < groups.size(); i++)
      groups.get(i).draw(driver);
    //fade-in time?
    if (fadeInTime > 0)
    {
      if (timeScreenActive <= fadeInTime)
      {
        float fadeFactor = 1f - timeScreenActive / fadeInTime;
        if (fadeFactor > 1)
          fadeFactor = 1;
        else if (fadeFactor < 0)
          fadeFactor = 0;
        driver.draw2DRectangle(new SColor((int) (255 * fadeFactor), 0, 0, 0),
          rectScreen);
      }
    }
    //fade-out time?
    if (useFadeOut && status == STATUS_CLOSING && fadeOutTime >= 0)
    {
      float fadeFactor = (GameEngine.getFrameTime(false) - timeClose) / fadeOutTime;
      if (fadeFactor > 1)
        fadeFactor = 1;
      else if (fadeFactor < 0)
        fadeFactor = 0;
      driver.draw2DRectangle(new SColor((int) (255 * fadeFactor), 0, 0, 0),
        rectScreen);

    }
    //is it time to close the screen now?
    if (status == STATUS_OPEN && automaticCloseTime > 0 && timeClose == 0)
    {
      if (timeScreenActive > automaticCloseTime)
      {
        GameEngine.getUIManager().openScreen(nextScreenClose, null);
      }
    }
    //closing time over?
    if (status == STATUS_CLOSING && GameEngine.getFrameTime(false) > timeClose + fadeOutTime)
    {
      status = STATUS_CLOSED;
    }
  }


  /**
   * Sets the focus to the {@link Control} in the current group,
   * that belongs to the specified id.
   * @param control  the {@link Control} that gets the focus, or <code>null</code>
   */
  public void setFocus(String id)
  {
    if (activeGroup != null)
      activeGroup.setFocus(id);
  }

  
  /**
   * Opens this screen.
   * @param group   The ID of the start group, or <code>null</code>,
   *                if the default start group should be shown.
   */
  public void open(String group)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Opening screen \"" + id + "\"");

    //raise load events
    for (int i = 0; i < groups.size(); i++)
      groups.get(i).loadEvent();
    
    //open new music, if set, and if not to continue
    SoundTrack st = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_MUSIC);
    if (music != null)
    {
      if (musicContinue && music.equals(st.getCurrentSoundID()))
      {
        //the current music will be continued. only change the
        //fade-out time to the new value.
        st.setFadeOutTime(musicFadeOutTime);
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Current background music is continued.");
      }
      else
      {
        //open the new music
        st.setNextSound(music, musicFadeInTime, musicFadeOutTime);
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Start new background music: \"" + music + "\"");
        st.playNextSound();
      }
    }
    else
    {
      st.setNextSound(null, 0, 0);
      st.playNextSound();
    }

    status = STATUS_OPEN;
    timeStart = GameEngine.getTime(false);
    
    //open start group
    if (group != null)
      activateGroup(group);
    else
      activateGroup(startGroupID);
    
  }

  /**
   * Closes this screen. If a fade-out time is set, the screen will
   * not be closed until this time is over.
   */
  public void close()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Closing screen.");
    if (status == STATUS_OPEN && fadeOutTime > 0)
    {
      status = STATUS_CLOSING;
      timeClose = GameEngine.getFrameTime(false);
    }
    else
    {
      status = STATUS_CLOSED;
    }
    //fade out, but not if the next screen has the same music and
    //continue is true
    if (music != null)
    {
      if (ui.getNextScreen() != null &&
          ui.getNextScreen().getMusic() != null &&
          music.equals(ui.getNextScreen().getMusic()) &&
          ui.getNextScreen().getMusicContinue())
      {
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Continue background music.");
      }
      else
      {
        GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_MUSIC).fadeOut();
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Fade out background music.");

      }
    }

  }


  /**
   * Gets the status of the screen.
   */
  public int getStatus()
  {
    return status;
  }

  
  /**
   * Keyboard Input.
   */
  public void input(KeyboardEvent kbEvent)
  {
    if (status == STATUS_CLOSING) return;
    
    //send input event to active group
    if (activeGroup != null)
      activeGroup.input(kbEvent);
  }
  
  
  /**
   * Sends a mouse event to the Screen
   */
  public void input(MouseEvent event)
  {
    if (status == STATUS_CLOSING) return;
    
    //send input event to active group
    if (activeGroup != null)
      activeGroup.input(event);
  }


  /**
   * Returns the time duration this screen is open already.
   * If it is not open, 0 is returned.
   */
  public float getScreenTime()
  {
    if (status == STATUS_CLOSED)
      return 0;
    else
      return GameEngine.getFrameTime(false) - timeStart;
  }

  
  /**
   * Gets the ID of the gui scene of this screen.
   */
  public String getGUIScene()
  {
    return guiScene;
  }

  
  /**
   * Sets the gui scene of this screen by its ID.
   */
  public void setGUIScene(String guiScene)
  {
    this.guiScene = guiScene;
    ui.setNextGUIScene(guiScene);
  }

  
  /**
   * Gets the ID of the start camera flight of this screen.
   */
  public String getGUISceneStartFlightID()
  {
    return guiSceneStartFlightID;
  }

  
  /**
   * Sets the id of the start camera flight of this screen.
   */
  public void setGUISceneStartFlightID(String id)
  {
    this.guiSceneStartFlightID = id;
  }
  
  
  /**
   * @param id   the ID of the <code>Control</code>
   * @return     the <code>Control</code> on this screen with the
   *             specified ID, or <code>null</code> if not found.
   */
  public Control findControl(String id)
  {
    for (int i = 0; i < groups.size(); i++)
    {
      Control c = groups.get(i).findControl(id);
      if (c != null)
        return c;
    }
    return null;
  }
  
  
  /**
   * Gets the control of the active group,
   * that has the focus, or <code>null</code>
   */
  public Control getFocusControl()
  {
    if (activeGroup != null)
      return activeGroup.getFocusControl();
    else
      return null;
  }

  
  /**
   * Adds a {@link Group} to this screen.
   */
  public void addGroup(Group group)
  {
    if (!groups.contains(group))
    {
      groups.add(group);
    }
  }
  
  
  /**
   * Returns the group that belongs to
   * the given id, or <code>null</code>
   * if it is not loaded.
   */
  public Group findGroup(String groupID)
  {
    for (int i = 0; i < groups.size(); i++)
      if (groups.get(i).getID().equals(groupID))
        return groups.get(i);
    return null;
  }
  
  
  /**
   * Activates a group and deactivates
   * the currently active group, if set.
   * If he group is already active, nothing
   * happens.
   * If the given group is <code>null</code>,
   * the currently active group is deactivated
   * and no other one is activated.
   * It may also be one the following variables:
   * <li>"%gamemode.overview%": The overview page for the
   *   selected game mode is opened</li>
   */
  public void activateGroup(String groupID)
  {
    if (groupID == null || groupID.length() == 0)
    {
      if (activeGroup != null) activeGroup.hide();
      activeGroup = null;
    }
    else
    {
      String g = new String(groupID);
      //variable? then replace it
      if (g.equals("%gamemode.overview%"))
      {
        GameEngine.GameMode gameMode = GameEngine.getGameMode();
        if (gameMode == GameEngine.GameMode.FriendlyGame)
          g = "friendlygameoverview";
      }
      //find group
      Group group = findGroup(g);
      if (group != null && activeGroup != group)
      {
        float groupWait = 0;
        if (activeGroup != null)
        {
          activeGroup.hide();
          groupWait = activeGroup.getDurationNextGroupWait();
        }
        activeGroup = group;
        activeGroup.setDurationThisGroupWait(groupWait);
        activeGroup.show();
      }
      else if (group == null)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Group not found: \"" + g + "\"");
      }
    }
  }

  
  /**
   * Gets the ID of the start group of this screen.
   */
  public String getStartGroupID()
  {
    return startGroupID;
  }

  
  /**
   * Sets the ID of the start group of this screen.
   */
  public void setStartGroupID(String startGroupID)
  {
    this.startGroupID = startGroupID;
  }
  
  
  /**
   * Run the current gui program.
   */
  public void runProgram()
  {
    if (program == Screen.Program.MATCH)
    {
      GameEngine.getMatchManager().run();
    }
    else if (program == Screen.Program.WORLDCUPLOGO)
    {
      WorldCupLogo.step(this);
    }
  }

  
  /**
   * Gets the active group.
   */
  public Group getActiveGroup()
  {
    return activeGroup;
  }

  
  /**
   * Fade-out used?
   */
  public boolean isUseFadeOut()
  {
    return useFadeOut;
  }

  
  /**
   * Set usage of fade-out.
   */
  public void setUseFadeOut(boolean useFadeOut)
  {
    this.useFadeOut = useFadeOut;
  }
  
  
}
