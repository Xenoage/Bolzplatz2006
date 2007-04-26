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
import com.xenoage.bp2k6.GameEngine.GameMode;
import com.xenoage.bp2k6.audio.SoundEffect;
import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.gamemodes.WorldCup;
import com.xenoage.bp2k6.gui.GUISounds.GUISound;
import com.xenoage.bp2k6.gui.loader.parser.OptionButtonParser;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.match.stadium.StadiumInfo;
import com.xenoage.bp2k6.match.stadium.attractions.*;
import com.xenoage.bp2k6.match.stadium.floodlight.Floodlight;
import com.xenoage.bp2k6.match.stadium.scoreboard.Scoreboard;
import com.xenoage.bp2k6.util.*;

import net.sf.jirr.ITexture;


/**
 * Actions determine what should be done after the user
 * presses a button, for example load another screen
 * or exit the program.
 *
 * @author   Andi
 */
public class Action
{

  public enum ActionType
  {
    None,               //no action
    ActivateGroup,      //starts the fly-in of the controls of a group and activated it
    CameraFlight,       //starts a camera flight from a gui scene
    ChangeGameMode,     //changes the gamemode
    ChangeImage,        //change the image of a image or imageswitch control
    ChangeImageControl, //change the image of a image or imageswitch control,
                        //and take the variable from another control
    ChangeSystemListControl, //changes the list of the given control to the
                        //system list with the name of the value of the given control
    CorruptReferee,     //corrupt the referee
    EditStadium,        //edit the user's stadium in career mode
    ExitProgram,        //exit game
    HallOfFame,         //open hall of fame entry website
    InputGameMode,      //sets the game mode of the input engine on or off
    LeaveGameMode,      //leaves the current gamemode
    LoadCareer,         //loads the given career slot
    LoadWorldCup,       //loads the given worldcup slot
    MatchEvent,         //a match event like "secondhalf" to begin the second half
    MatchOver,          //tells the GameEngine that the current match is over
    OpenScreen,         //open a screen
    OpenWebsite,        //opens the given website
    PerformActions,     //performs all actions of a ActionSet
    Restart,            //restarts the program
    SaveCareer,         //saves the current career
    SaveSetting,        //save a setting, like screen resolution
    SaveSettingControl, //save a setting, like screen resolution, from a control
    SaveWorldCup,       //saves the current worldcup
    SetActiveJoystick,  //sets the active joystick for gamemode=false
    SetCaption,         //sets the caption of the given label to the given value
    SetFocus,           //sets the focus to the given control
    SetLabelColor,      //sets the color of the given label
    SetOption,          //sets the single option of the given optionbutton
    SetPause,           //sets pause on or off
    SetSelection,       //sets the selection of the given optionbutton to the given index
    SetValue,           //sets the value of the given energybar
    StartMatchDay;      //starts the next matchday in a career or worldcup
  };

  public ActionType type = ActionType.None;
  public String eventparam = null;
  public String param1 = null;
  public String param2 = null;
  public String param3 = null;
  public String param4 = null;
  public String sound = null; //sound with filename
  public GUISound guiSound = null; //gui sound
  public SoundEffect soundEffect = null;
  
  public boolean alreadyPerformed = false;
  

  public Action(ActionType type, String param1,
    String param2, String param3, String param4, String eventparam,
    GUISound guiSound, String sound)
  {
    this.type = type;
    this.param1 = param1;
    this.param2 = param2;
    this.param3 = param3;
    this.param4 = param4;
    this.eventparam = eventparam;
    this.guiSound = guiSound;
    this.sound = sound;
    if (sound != null && sound.length() > 0)
    {
      soundEffect =
        GameEngine.getSoundEngine().createSoundEffect(sound);
    }
  }
  
  
  /**
   * Performs the action of a control.
   * @param control   The control that caused the action.
   * @param event     The name of the event, e.g. "focus" or "click".
   *                  May also be null. Only needed for logging.
   */
  public void perform(Control control, String event)
  {
    perform(control, event, true);
  }
  

  /**
   * Performs the action of a control.
   * @param control   The control that caused the action.
   * @param event     The name of the event, e.g. "focus" or "click".
   *                  May also be null. Only needed for logging.
   * @param playSound Play sound, if <code>true</code> and sound set.
   */
  public void perform(Control control, String event, boolean playSound)
  {
    String e = event;
    if (e == null) e = "";
    Logging.log(Logging.LEVEL_MESSAGES, this, "Event \"" + e +
      "\" on control (eventparam: \"" + eventparam + "\") action: " +
      type.toString() + " ( \"" + param1 + "\", \"" + param2 +
        "\", \"" + param3 + "\", \"" + sound + "\" )");
    //get UIManager of the control
    Screen screen = control.getParentScreen();
    UIManager ui = null;
    if (screen != null)
    {
      ui = screen.getUIManager();
      if (ui == null) return;
    }
    
    //perform action
    boolean soundPlayed = false;
    if (type == ActionType.ChangeImage ||
      type == ActionType.ChangeImageControl)
    {
      //change the picture of a image control
      Control c = control.getParentScreen().findControl(param1);
      
      //replacement for "%value%"
      String tokenValue = "";
      if (type == ActionType.ChangeImage)
        tokenValue = new String(control.getValue());
      else
      {
        Control cToken = control.getParentScreen().findControl(param4);
        if (cToken != null)
        {
          tokenValue = new String(cToken.getValue());
        }
      }
      
      if (c != null && c instanceof Image)
      {
        Image img = (Image) c;
        String imgPath = new String(param2);
        //replace %value%
        imgPath = imgPath.replaceAll("%value%", tokenValue);
        //replace other variables
        imgPath = Variables.replaceVariables(imgPath);
        ITexture tex = MediaLoader.loadTexture(imgPath);
        if (tex != null)
        {
          img.setImage(tex);
        }
        else if (param3 != null)
        {
          //use alternative image, if set
          tex = MediaLoader.loadTexture(param3);
          if (tex != null)
            img.setImage(tex);
          else
            Logging.log(Logging.LEVEL_WARNINGS, this,
              "Can neither find image file \"" + imgPath + "\" nor " +
              "alternative image file \"" + param3 + "\"");
        }
        else
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Can not find image file \"" + imgPath + "\"");
        }
      }
      else
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Can not find image control \"" + param1 + "\"");
    }
    else
    {
      //perform general (not control-specific) action
      perform(screen, control.getParentGroup(), event, playSound);
      soundPlayed = true;
    } 
    
    //if it was a control-specific action, play sound
    //and set action to alreadyPerformed = true.
    if (!soundPlayed && playSound)
    {
      //play gui sound
      if (guiSound != null)
        ui.getGUISounds().playSound(guiSound);
      //play sound
      if (soundEffect != null)
        soundEffect.play();
    }
    alreadyPerformed = true;
    
  }
  
  
  /**
   * Performs the action of a group.
   * @param screen    The group that caused the action.
   * @param event     The name of the event, e.g. "cameraflight".
   *                  May also be null. Only needed for logging.
   */
  public void perform(Group group, String event)
  {
    perform(group, event, true);
  }
  
  
  /**
   * Performs the action of a group.
   * @param screen    The group that caused the action.
   * @param event     The name of the event, e.g. "cameraflight".
   *                  May also be null. Only needed for logging.
   * @param playSound Play sound, if <code>true</code> and sound set.
   */
  public void perform(Group group, String event, boolean playSound)
  {
    String e = event;
    if (e == null) e = "";
    Logging.log(Logging.LEVEL_MESSAGES, this, "Event \"" + e +
      "\" on group (eventparam: \"" + eventparam + "\") action: " +
      type.toString() + " ( \"" + param1 + "\", \"" + param2 +
        "\", \"" + param3 + "\", \"" + sound + "\" )");
    //perfom action
    perform(group.getParentScreen(), group, event, playSound);
  }
  
  
  /**
   * Performs a action.
   * @param screen    The parent screen of the control or group
   *                  that caused the action.
   * @param group     The group or the parent group of the control
   *                  that caused the action.
   * @param event     The name of the event, e.g. "focus" or "click".
   *                  May also be null. Only needed for logging.
   * @param playSound Play sound, if <code>true</code> and sound set.                 
   */
  private void perform(Screen screen, Group group, String event, boolean playSound)
  {
    UIManager ui = null;
    if (screen != null)
    {
      ui = screen.getUIManager();
      if (ui == null) return;
    }
    //play gui sound
    if (playSound && guiSound != null)
      ui.getGUISounds().playSound(guiSound);
    //play sound
    if (playSound && soundEffect != null)
      soundEffect.play();
    //perform action
    if (type == ActionType.ActivateGroup)
    {
      //start fly-in of the controls of a screen
      String p = Variables.replaceVariables(param1);
      screen.activateGroup(p);
    }
    else if (type == ActionType.CameraFlight)
    {
      //start camera flight with id <argument1>
      ui.startCameraFlight(param1);
    }
    else if (type == ActionType.ChangeGameMode)
    {
      //changes the gamemode
      if (param1.equals("newcareer"))
      {
        //starts a new career with the team, whose id is
        //in the value of control <param2>
        Control c = screen.findControl(param2);
        if (c != null)
        {
          String teamID = c.getValue();
          GameEngine.startCareer(teamID);
        }
        else
        {
          GameEngine.fatalError(this, 
            new Exception("New career: No user-team is specified in param2!"));
        }
      }
      else if (param1.equals("newworldcup"))
      {
        //starts a new worldcup with the team, whose id is
        //in the value of control <param2>
        Control c = screen.findControl(param2);
        if (c != null)
        {
          String teamID = c.getValue();
          GameEngine.startWorldCup(teamID);
        }
        else
        {
          GameEngine.fatalError(this, 
            new Exception("New career: No user-team is specified in param2!"));
        }
      }
      else if (param1.equals("friendlygame"))
      {
        //starts a friendly game
        GameEngine.setGameMode("friendlygame");
      }
      else
      {
        GameEngine.fatalError(this, 
          new Exception("Unknown game mode \"" + param1 + "\"!"));
      }
    }
    else if (type == ActionType.ChangeSystemListControl)
    {
      //change the list of the given control to the
      //system list with the name of the value of the given control
      Control c1 = screen.findControl(param1);
      Control c2 = screen.findControl(param2);
      if (c1 != null && c2 != null)
      {
        if (c1 instanceof OptionButton)
        {
          OptionButtonParser.loadSystemList((OptionButton) c1, c2.getValue());
          if (param3.length() > 0)
            ((OptionButton) c1).setSelectedIndex(Integer.valueOf(param3));
        }
        else
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Control \"" + param1 + "\" is no OptionButton!");
      }
      else if (c1 == null)
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Control \"" + param1 + "\" does not exist!");
      else if (c2 == null)
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Control \"" + param2 + "\" does not exist!");  
    }
    else if (type == ActionType.CorruptReferee)
    {
      //corrupt the referee
      corruptReferee(screen, Integer.parseInt(param1));
    }
    else if (type == ActionType.EditStadium)
    {
      //edit the stadium
      editStadium(param1, param2);
    }
    else if (type == ActionType.ExitProgram)
    {
      //close the screen and close the program
      ui.closeScreenAndExit();
    }
    else if (type == ActionType.HallOfFame)
    {
      //show hall of fame entry website
      HallOfFame.openWebsite();
      screen.activateGroup("browseropened");
    }
    else if (type == ActionType.InputGameMode)
    {
      //set game mode of the input engine on or off
      boolean gamemode = (param1.equals("true"));
      GameEngine.getInput().setGameMode(gamemode);
    }
    else if (type == ActionType.LeaveGameMode)
    {
      //if not already saves, show warning
      if (Settings.isGamemodeGameNotSafed())
        screen.activateGroup("leavegamemode");
      else
        ui.openScreen("mainmenu", "startnologo");
    }
    else if (type == ActionType.LoadCareer)
    {
      //load the career slot param1
      int slot = Integer.parseInt(param1);
      if (Career.isSlotUsed(slot))
        GameEngine.loadCareer(slot);
      else
        screen.activateGroup("newcareer");
    }
    else if (type == ActionType.LoadWorldCup)
    {
      //load the worldcup slot param1
      int slot = Integer.parseInt(param1);
      if (WorldCup.isSlotUsed(slot))
        GameEngine.loadWorldCup(slot);
      else
        screen.activateGroup("newworldcup");
    }
    else if (type == ActionType.MatchEvent)
    {
      //match event for the current match
      Match match = GameEngine.getMatchManager().getMatch();
      if (match != null)
        match.matchEvent(param1);
    }
    else if (type == ActionType.MatchOver)
    {
      if (param1.equals("exitmatch"))
      {
        //user aborted the match
        ui.openScreen("mainmenu", "startnologo");
      }
      else
      {
        //tell the GameEngine that the match is over and
        //give details about the match in the parameter
        GameEngine.matchOver(GameEngine.getMatchManager().getMatchResult());
      }
    }
    else if (type == ActionType.OpenScreen)
    {
      //opens a new screen
      ui.openScreen(param1, null);
    }
    else if (type == ActionType.OpenWebsite)
    {
      //opens the given website
      HallOfFame.openURL(param1);
    }
    else if (type == ActionType.PerformActions)
    {
      //performs all actions of the given ActionSet
      ActionSet actionSet = group.findActionSet(param1);
      if (actionSet != null)
        actionSet.performAll(group, event);
    }
    else if (type == ActionType.Restart)
    {
      //restarts the program
      //does not work at the moment!
      //Main.restart();
    }
    else if (type == ActionType.SaveSetting ||
      type == ActionType.SaveSettingControl)
    {
      //save a value in the in the variable with the name <param1>
      
      String value = null;
      if (type == ActionType.SaveSetting)
      {
        //save the value of <param2>
        value = new String(param2);
      }
      else if (ui.getCurrentScreen() != null)
      {
        //save the value of the control with id <param2>
        Control c = ui.getCurrentScreen().findControl(param2);
        if (c != null)
          value = c.getValue();
      }
      
      if (value != null)
      {
        value = Variables.replaceVariables(value); //replace variables
        Settings.saveSetting(param1, value, param3, screen);
      }
      else
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "SaveSetting does not work: Control \"" + param2 +
            "\" not found!");
      }
    }
    else if (type == ActionType.SaveCareer)
    {
      //save the current carrer in the slot param1
      if (GameEngine.getGameMode() == GameEngine.GameMode.Career)
      {
        Career career = GameEngine.getCareer();
        career.saveToXMLFile(Integer.parseInt(param1));
      }
      else
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Game is currently not in career mode!");
      }
    }
    else if (type == ActionType.SaveWorldCup)
    {
      //save the current worldcup in the slot param1
      if (GameEngine.getGameMode() == GameEngine.GameMode.WorldCup)
      {
        WorldCup worldCup = GameEngine.getWorldCup();
        worldCup.saveToXMLFile(Integer.parseInt(param1));
      }
      else
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Game is currently not in worldcup mode!");
      }
    }
    else if (type == ActionType.SetActiveJoystick)
    {
      //sets the active joystick for gamemode=false
      //if param2==true, it must be a user vs user match
      if (Boolean.parseBoolean(param2) == false ||
        GameEngine.getMatchManager().isNextMatchUserVsUser())
      {
        GameEngine.getInput().setActiveJoystick(Integer.parseInt(param1));
      }
      else
      {
        GameEngine.getInput().setActiveJoystick(0);
      }
    }
    else if (type == ActionType.SetCaption)
    {
      //sets the text of label param1 to param2
      Control c = screen.findControl(param1);
      if (c == null || (!(c instanceof Label) && !(c instanceof Button)))
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Label/Button \"" + param1 + "\" not found!");
      }
      else if (c instanceof Label)
      {
        Label lbl = (Label) c;
        String caption = Variables.replaceVariables(param2);
        if (caption.indexOf("\\n") > -1)
          caption = caption.replace("\\n", "\n");
        lbl.setCaption(caption);
      }
      else if (c instanceof Button)
      {
        Button btn = (Button) c;
        btn.setCaption(Variables.replaceVariables(param2));
      }
    }
    else if (type == ActionType.SetFocus)
    {
      //set the focus to another control
      screen.setFocus(param1);
    }
    else if (type == ActionType.SetLabelColor)
    {
      //sets the color of label param1
      if (param1.equals("*"))
      {
        //for all labels on the current group
        for (int i = 0; i < group.getControls().size(); i++)
        {
          Control c = group.getControls().get(i);
          if (c instanceof Label)
            setLabelColor((Label) c, param2, param3);
        }
      }
      else
      {
        //for the given label
        Control c = screen.findControl(param1);
        if (c == null || (!(c instanceof Label)))
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Label \"" + param1 + "\" not found!");
        }
        else
        {
          setLabelColor((Label) c, param2, param3);
        }
      }
    }
    else if (type == ActionType.SetOption)
    {
      //sets the single option of optionbutton param1 to param2
      Control c = screen.findControl(param1);
      if (c == null || (!(c instanceof OptionButton)))
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "OptionButton \"" + param1 + "\" not found!");
      }
      else
      {
        OptionButton btn = (OptionButton) c;
        btn.setOptions(new String[]{Variables.replaceVariables(param2)},
          new String[]{""});
      }
    }
    else if (type == ActionType.SetPause)
    {
      //sets pause on or off
      boolean pause = Boolean.parseBoolean(param1);
      GameEngine.setPause(pause);
    }
    else if (type == ActionType.SetSelection)
    {
      //sets the selection of the given optionbutton to the given index
      Control c = screen.findControl(param1);
      if (c == null || (!(c instanceof OptionButton)))
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "OptionButton \"" + param1 + "\" not found!");
      }
      else
      {
        OptionButton ob = (OptionButton) c;
        ob.setSelectedIndex(Integer.parseInt(
          Variables.replaceVariables(param2)));
      }
    }
    else if (type == ActionType.SetValue)
    {
      //sets the value of the given energybar
      Control c = screen.findControl(param1);
      if (c == null || (!(c instanceof EnergyBar)))
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "EnergyBar \"" + param1 + "\" not found!");
      }
      else
      {
        EnergyBar eb = (EnergyBar) c;
        try
        {
          eb.setValue(Integer.parseInt(
            Variables.replaceVariables(param2)));
        }
        catch (Exception ex)
        {
        }
      }
    }
    else if (type == ActionType.StartMatchDay)
    {
      //starts the next matchday in the current career or worldcup
      if (GameEngine.getGameMode() == GameEngine.GameMode.Career)
      {
        Career career = GameEngine.getCareer();
        boolean showEvent = false;
        if (param2.length() > 0)
          showEvent = Boolean.parseBoolean(param2);
        career.startMatchDay(param1, showEvent);
      }
      else if (GameEngine.getGameMode() == GameEngine.GameMode.WorldCup)
      {
        WorldCup worldCup = GameEngine.getWorldCup();
        worldCup.startMatchDay(param1);
      }
      else
      {
        GameEngine.fatalError(this,
          new Exception("Game is currently not in career or worldcup mode!"));
      }
    }
    alreadyPerformed = true;
  }
  

  
  /**
   * Returns <code>true</code>, if this action was already
   * performed, otherwise <code>false</code>.
   */
  public boolean isAlreadyPerformed()
  {
    return alreadyPerformed;
  }

  
  /**
   * Edit the stadium.
   */
  private void editStadium(String param1, String param2)
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.Career)
    {
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "Stadium can only be edited in career mode!");
      return;
    }
    Career career = GameEngine.getCareer();
    Stadium stadium = career.getUserStadium();
    StadiumInfo stadiumInfo = stadium.getStadiumInfo();
    UIManager ui = GameEngine.getUIManager();
    
    if (param1.equals("setgoals"))
    {
      //set the goals
      StadiumInfo.Goals goals;
      if (param2.equals("2"))
        goals = StadiumInfo.Goals.WoodenGoals;
      else if (param2.equals("3"))
        goals = StadiumInfo.Goals.AluminumGoals;
      else
        goals = StadiumInfo.Goals.GarbageCans;
      //buy goal
      boolean ok = false;
      if (goals == StadiumInfo.Goals.GarbageCans)
      {
        //garbage cans are for free
        ok = true;
      }
      else if ((goals == StadiumInfo.Goals.WoodenGoals &&
        stadiumInfo.isWoodenGoalsBought()) ||
        (goals == StadiumInfo.Goals.AluminumGoals &&
          stadiumInfo.isAluminumGoalsBought()))
      {
        //goals already bought
        ok = true;
      }
      else
      {
        int cost = stadiumInfo.getGoalTypeCost(goals);
        if (cost > career.getUserMoney())
        {
          //not enough money. error sound and screen.
          ui.getGUISounds().playSound(guiSound.Abort);
          ui.getCurrentScreen().activateGroup("notenoughmoney");
        }
        else
        {
          //buy it
          career.setUserMoney(career.getUserMoney() - cost);
          ok = true;
        }
      }
      if (ok)
      {
        //set the goals, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        stadiumInfo.setGoals(goals);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    
    else if (param1.equals("lawncare"))
    {
      //lawn care
      int cost = stadium.getField().getLawn().lawnCareCost;
      if (cost <= career.getUserMoney())
      {
        //tend the lawn, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        career.setUserMoney(career.getUserMoney() - cost);
        stadium.getField().lawnCare();
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
      else
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
    }
    
    else if (param1.equals("buildblock"))
    {
      //build block (adboards or stands)
      int stage = Settings.getCareerStadiumBuildBlock();
      int alignment = Integer.parseInt(param2);
      //block already built?
      if (stage == 0)
      {
        if (stadium.isCareerAdboardBlockBuilt(alignment))
          return; //do nothing
      }
      else
      {
        if (stadium.isCareerStandBlockBuilt(stage, alignment))
          return; //do nothing
      }
      //enough money?
      int cost = stadium.getCareerBlockCost(stage, alignment);
      if (cost <= career.getUserMoney())
      {
        //build the block save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        career.setUserMoney(career.getUserMoney() - cost);
        if (stage == 0)
          stadium.buildCareerAdboardBlock(alignment);
        else
          stadium.buildCareerStandBlock(stage, alignment);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        if (stage == 0)
          ui.getCurrentScreen().activateGroup("stadiumeditor");
        else
          ui.getCurrentScreen().activateGroup("stadiumstands");
      }
      else
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
    }
    
    
    else if (param1.equals("buildlight"))
    {
      //build light
      int position = Settings.getCareerStadiumBuildLight();
      int stage = Integer.parseInt(param2);
      Floodlight oldLight = stadium.getCareerLight(position);
      //the same light already built there?
      if (oldLight != null && oldLight.getStage() == stage)
        return; //do nothing
      //enough money?
      int cost = Floodlight.getCost(stage);
      if (cost <= career.getUserMoney())
      {
        //remove the old light, build the light,
        //save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        career.setUserMoney(career.getUserMoney() - cost);
        if (oldLight != null)
          stadium.removeFloodlight(oldLight);
        stadium.addFloodlight(stage, position);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumlighting");
      }
      else
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
    }
    
    else if (param1.equals("setscoreboard"))
    {
      //set the scoreboard
      int stage = Integer.parseInt(param2);
      Scoreboard oldScoreboard = stadium.getScoreboard();
      int cost = Scoreboard.getCost(stage);
      //the same scoreboard already there?
      if (oldScoreboard != null && oldScoreboard.getStage() == stage)
        return; //do nothing
      //stage 2 needs northern stands stage 1,
      //stage 3 needs northern stands stage 3.
      if ((stage == 2 && !stadium.isCareerStandBlockBuilt(
        Stadium.BLOCK_STAGE_1, Stadium.BLOCK_ALIGNMENT_NORTH)) ||
        (stage == 3 && !stadium.isCareerStandBlockBuilt(
          Stadium.BLOCK_STAGE_3, Stadium.BLOCK_ALIGNMENT_NORTH)))
      {
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("northstandsmissing");
      }
      else if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        stadium.setScoreboard(stage);
        stadium.getScoreboard().moveToCareerPosition();
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    
    else if (param1.equals("buildattraction") &&
      param2.equals("hotdogstand"))
    {
      //build a hotdog stand
      //already there?
      if (stadium.getHotDogStand() != null)
        return; //do nothing
      int cost = HotDogStand.getCost();
      if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        HotDogStand hotDogStand = new HotDogStand();
        hotDogStand.moveToCareerPosition();
        stadium.setHotDogStand(hotDogStand);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    else if (param1.equals("buildattraction") &&
      param2.equals("bettingoffice"))
    {
      //build a betting office
      //already there?
      if (stadium.getBettingOffice() != null)
        return; //do nothing
      int cost = BettingOffice.getCost();
      if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        BettingOffice bettingOffice = new BettingOffice();
        bettingOffice.moveToCareerPosition();
        stadium.setBettingOffice(bettingOffice);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    
    else if (param1.equals("buildattraction") &&
      param2.equals("beerbar"))
    {
      //build a beer bar
      //already there?
      if (stadium.getBeerBar() != null)
        return; //do nothing
      int cost = BeerBar.getCost();
      if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        BeerBar beerBar = new BeerBar();
        beerBar.moveToCareerPosition();
        stadium.setBeerBar(beerBar);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    
    else if (param1.equals("buildattraction") &&
      param2.equals("track"))
    {
      //build a track
      //already there?
      if (stadium.getTrack() != null)
        return; //do nothing
      int cost = Track.getCost();
      if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        Track track = new Track();
        track.moveToCareerPosition();
        stadium.setTrack(track);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
    
    else if (param1.equals("buildattraction") &&
      param2.equals("highjump"))
    {
      //build a highjump
      //already there?
      if (stadium.getHighJump() != null)
        return; //do nothing
      int cost = HighJump.getCost();
      if (cost > career.getUserMoney())
      {
        //not enough money. error sound and screen.
        ui.getGUISounds().playSound(guiSound.Abort);
        ui.getCurrentScreen().activateGroup("notenoughmoney");
      }
      else
      {
        //buy it
        career.setUserMoney(career.getUserMoney() - cost);
        //set the scoreboard, save the stadium, and reload it
        ui.getGUISounds().playSound(guiSound.Accept);
        HighJump highJump = new HighJump();
        highJump.moveToCareerPosition();
        stadium.setHighJump(highJump);
        try
        {
          stadium.saveToXMLFile(true);
          GUIScene guiScene = ui.getCurrentGUIScene();
          if (guiScene != null)
            guiScene.reloadStadium();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Could not save career stadium:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
        ui.getCurrentScreen().activateGroup("stadiumeditor");
      }
    }
    
  }

  
  /**
   * Sets the color of the given label.
   * param2: "reset" sets the label back to the original
   * color, "userteam" sets the color to param3, if
   * it contains the name of the current career user team.
   */
  private void setLabelColor(Label label, String param2, String param3)
  {
    if (param2.equals("reset"))
    {
      label.setDisplayedColor(null);
    }
    else if (param2.equals("userteam"))
    {
      if (GameEngine.getGameMode() == GameMode.Career)
      {
        Career career = GameEngine.getCareer();
        if (label.getCaption().equals(
          GameEngine.getMatchManager().getTeamInfo(
            career.getUserTeamID()).getName()))
        {
          label.setDisplayedColor(ColorTools.getColor(param3));
        }
      }
      else if (GameEngine.getGameMode() == GameMode.WorldCup)
      {
        WorldCup worldCup = GameEngine.getWorldCup();
        if (label.getCaption().equals(
          GameEngine.getMatchManager().getTeamInfo(
            worldCup.getUserTeamID()).getName()))
        {
          label.setDisplayedColor(ColorTools.getColor(param3));
        }
      }
    }
  }
  
  
  /**
   * Corrupts the referee.
   */
  public void corruptReferee(Screen screen, int stage)
  {
    if (GameEngine.getGameMode() == GameMode.Career)
    {
      Career career = GameEngine.getCareer();
      int cost = RefereeInfo.getCorruptionCost(
        career.getCurrentMatchDayLeague(), stage);
      if (career.getUserMoney() < cost)
      {
        //not enough money
        screen.activateGroup("corruptionnomoney");
      }
      else
      {
        if (career.corruptReferee(stage))
        {
          //corruption done!
          career.setUserMoney(career.getUserMoney() - cost);
          screen.activateGroup("corruptiondone");
        }
        else
        {
          //corruption failed!
          career.setUserMoney(career.getUserMoney() - cost / 2);
          screen.activateGroup("corruptionfailed");
        }
      }
    }
  }
  
  
}
