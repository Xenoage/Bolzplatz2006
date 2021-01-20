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

import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.input.KeysAssignment;
import com.xenoage.bp2k6.util.*;


/**
 * This game manages the game variables,
 * for example the team IDs of the next match.
 * 
 * @author Andreas Wenger
 */
public class Settings
{

  private static Settings thisObject = new Settings();
  
  //teaminfo
  private static String teamInfoTeam = "";
  
  //career
  private static String careerViewedTableLeague = "";
  private static String careerViewedTableMatchDay = "0";
  private static String careerViewedFixturesMatchDay = "1";
  private static String careerViewedAllstarsMatchDay = "1";
  private static String careerViewedAllstarsGroup = "0";
  private static String careerStadiumBuildBlock = "0";
  private static String careerStadiumBuildLight = "1";
  
  //worldcup
  private static String worldcupViewMatchday = "1";
  private static String worldcupViewGroup = "0";
  
  //gamemode
  private static boolean gamemodeGameNotSafed = true;
  
  //video quality
  private static boolean videoQualitySettingsRead = false;
  private static boolean videoLowQuality = false;
  
  
  /**
   * Saves a setting with the given value.
   * Some settings are only saved in class internal variables,
   * others are saved in other classes and some
   * are saved in XML files.
   * Some settings needs the additional parameters
   * <code>value2</code> and <code>screen</code>,
   * for the others their values may be <code>null</code>.
   */
  public static void saveSetting(String setting, String value,
    String value2, Screen screen)
  {
    
    //screen resolution - not used at the moment
    if (setting.equals("screenres"))
    {
      //save screen resolution
      String[] val = value.split("x");
      if (val.length == 2)
      {
        int w = Integer.parseInt(val[0].trim());
        int h = Integer.parseInt(val[1].trim());
        int d = 32; //allow not only 32 bit later...
        //fullscreen?
        boolean fullscreen;
        Control c2 = screen.findControl(value2);
        if (c2 != null && c2 instanceof OptionButton)
        {
          OptionButton ob = (OptionButton) c2;
          fullscreen = (ob.getValue().equals("fullscreen"));
        }
        else
        {
          Logging.log(Logging.LEVEL_WARNINGS, thisObject,
            "SaveSetting can not read fullscreen mode: Control \"" + value2 +
              "\" not found! Using default value.");
          fullscreen = false;
        }
        Resolution resolution = new Resolution();
        int resolutionIndex = resolution.getVideoModeIndex(w, h, d);
        if (resolutionIndex > -1)
        {
          try
          {
            resolution.saveSelectedVideoMode(resolutionIndex, fullscreen);
            Logging.log(Logging.LEVEL_MESSAGES, thisObject,
              "SaveSetting: Video mode: " + w + " x " + h + " x" + d);
          }
          catch (Exception ex)
          {
            Logging.log(Logging.LEVEL_ERRORS, thisObject,
              "Can not save video mode. Details:");
            Logging.log(thisObject, ex);
          }
        }
        else
        {
          Logging.log(Logging.LEVEL_ERRORS, thisObject,
            "Can not save video mode. Mode not available: " +
              w + " x " + h + " x " + d);
        }
      }
    }
    

    //controller 1 or 2
    else if (setting.equals("match.controller1") ||
      setting.equals("match.controller2"))
    {
      int i = setting.endsWith("1") ? 0 : 1;
      GameEngine.getMatchManager().setController(i, value);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: Conroller " + i + ": " + value);
    }

    //team 1 or 2
    else if (setting.equals("match.team1") ||
      setting.equals("match.team2"))
    {
      int i = setting.endsWith("1") ? 0 : 1;
      GameEngine.getMatchManager().setTeam(i, value);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: Team " + i + ": " + value);
    }
    
    //stadium
    else if (setting.equals("match.stadium"))
    {
      GameEngine.getMatchManager().setStadium(value);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: Stadium: " + value);
    }
    
    //weather
    else if (setting.equals("match.weather"))
    {
      GameEngine.getMatchManager().setWeather(value, false);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: Weather: " + value);
    }
    
    //referee
    else if (setting.equals("match.referee"))
    {
      GameEngine.getMatchManager().setReferee(value);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: Referee: " + value);
    }
    
    //time factor
    else if (setting.equals("match.timefactor"))
    {
      try
      {
        ConfigFile matchConfig = new ConfigFile("data/config/match.xml");
        matchConfig.setValue("timefactor", value);
        matchConfig.saveToXML();
        Logging.log(Logging.LEVEL_MESSAGES, thisObject,
          "SaveSetting: timefactor: " + value);
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, thisObject,
          "Error while SaveSetting: timefactor: " + value + ":");
        Logging.log(Logging.LEVEL_ERRORS, thisObject, ex);
      }
    }
    
    //league of viewed table
    else if (setting.equals("career.viewedtable.league"))
    {
      careerViewedTableLeague = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //matchday of viewed table
    else if (setting.equals("career.viewedtable.matchday"))
    {
      careerViewedTableMatchDay = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //matchday of viewed fixtures
    else if (setting.equals("career.viewedfixtures.matchday"))
    {
      careerViewedFixturesMatchDay = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //matchday of viewed allstars league
    else if (setting.equals("career.viewedallstars.matchday"))
    {
      careerViewedAllstarsMatchDay = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //group of viewed allstars league
    else if (setting.equals("career.viewedallstars.group"))
    {
      careerViewedAllstarsGroup = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //stadium editor: build block (adboards, stage1, stage2, stage3)
    else if (setting.equals("career.stadium.buildblock"))
    {
      careerStadiumBuildBlock = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //stadium editor: build light
    else if (setting.equals("career.stadium.buildlight"))
    {
      careerStadiumBuildLight = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //worldcup: viewed matchday
    else if (setting.equals("worldcup.view.matchday"))
    {
      worldcupViewMatchday = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //worldcup: viewed group
    else if (setting.equals("worldcup.view.group"))
    {
      worldcupViewGroup = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    
    //keys assignment
    else if (setting.equals("keysassignment"))
    {
      String[] keyArray = value.split("#");
      KeysAssignment.saveKeyAssignment(
        Integer.parseInt(keyArray[0]),
        Integer.parseInt(keyArray[1]),
        Integer.parseInt(keyArray[2]));
      KeysAssignment.setAssignment(GameEngine.getInput());
    }
    
    //new woldcup/career team
    else if (setting.equals("teaminfo"))
    {
      teamInfoTeam = value;
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }
    
    //gamemode not already saved
    else if (setting.equals("gamenotsafed"))
    {
      gamemodeGameNotSafed = Boolean.parseBoolean(value);
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "SaveSetting: " + setting + ": " + value);
    }

    //unknown system variable
    else
    {
      Logging.log(Logging.LEVEL_WARNINGS, thisObject,
        "SaveSetting does not work: System variable \"" + value +
          "\" is unknown!");
    }
  }
  
  
  /**
   * Gets the league of the currently viewed table in career
   * mode (should be "league1", "league2",
   * "league3" or "" if not set).
   */
  public static String getCareerViewedTableLeague()
  {
    return careerViewedTableLeague;
  }
  
  
  /**
   * Gets the number of the matchday
   * of the currently viewed table in career mode.
   */
  public static int getCareerViewedTableMatchDay()
  {
    return Integer.parseInt(careerViewedTableMatchDay);
  }
  
  
  /**
   * Gets the number of the matchday
   * of the currently viewed fixtures in career mode.
   */
  public static int getCareerViewedFixturesMatchDay()
  {
    return Integer.parseInt(careerViewedFixturesMatchDay);
  }


  /**
   * Gets the index of the currently viewed
   * allstars group.
   */
  public static int getCareerViewedAllstarsGroup()
  {
    return Integer.parseInt(careerViewedAllstarsGroup);
  }


  /**
   * Gets the number of the currently viewed
   * allstars matchday.
   */
  public static int getCareerViewedAllstarsMatchDay()
  {
    return Integer.parseInt(careerViewedAllstarsMatchDay);
  }
  
  
  /**
   * Gets the number of the currently viewed
   * worldcup matchday.
   */
  public static int getWorldcupViewMatchday()
  {
    return Integer.parseInt(worldcupViewMatchday);
  }
  
  
  /**
   * Gets the index of the currently viewed
   * worldcup group.
   */
  public static int getWorldcupViewGroup()
  {
    return Integer.parseInt(worldcupViewGroup);
  }


  /**
   * Gets the currently edited stadium stage.
   */
  public static int getCareerStadiumBuildBlock()
  {
    return Integer.parseInt(careerStadiumBuildBlock);
  }
  
  
  /**
   * Gets the currently edited stadium light.
   */
  public static int getCareerStadiumBuildLight()
  {
    return Integer.parseInt(careerStadiumBuildLight);
  }


  /**
   * Gets the currently selected team
   * for retrieving team information.
   */
  public static String getTeamInfoTeam()
  {
    return teamInfoTeam;
  }


  /**
   * Returns true, if the current gamemode was not
   * saved already.
   */
  public static boolean isGamemodeGameNotSafed()
  {
    return gamemodeGameNotSafed;
  }


  /**
   * Sets, if the current gamemode was not
   * saved already.
   */
  public static void setGamemodeGameNotSafed(boolean gamemodeGameNotSafed)
  {
    Settings.gamemodeGameNotSafed = gamemodeGameNotSafed;
  }
  
  
  /**
   * Returns true, if low video quality is selected.
   */
  public static boolean isVideoLowQuality()
  {
    if (!videoQualitySettingsRead)
    {
      videoQualitySettingsRead = true;
      videoLowQuality = ConfigFile.getValueFromFileBoolean(
        "data/config/videoquality.xml", "lowquality");
    }
    return videoLowQuality;
  }
  
}
