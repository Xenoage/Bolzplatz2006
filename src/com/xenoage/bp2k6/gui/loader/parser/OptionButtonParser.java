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
package com.xenoage.bp2k6.gui.loader.parser;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.match.stadium.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * @author Andreas Wenger
 */
public class OptionButtonParser
{

  public static OptionButton parseOptionButton(Node e,
    Screen parentScreen, Group parentGroup)
  {
    OptionButton c = new OptionButton("");
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //caption
    String caption = XMLReader.readAttributeValue(e, "caption");
    caption = Variables.replaceVariables(caption);
    c.setCaption(caption);
    //font
    String fontID = XMLReader.readAttributeValue(e, "font");
    c.setFontByID(fontID);

    //options
    List<Element> listOptionContainers = XMLReader.elements(e, "options");
    if (listOptionContainers.size() > 0)
    {
      Node optionsContainer = listOptionContainers.get(0);
      List<Element> listOptions = XMLReader.elements(optionsContainer, "option");
      String systemList = XMLReader.readAttributeValue(
        optionsContainer, "systemlist");

      //first possibility: options from XML file
      if (listOptions.size() > 0)
      {
        ArrayList<String> optionsCaption = new ArrayList<String>();
        ArrayList<String> optionsValue = new ArrayList<String>();
        for (int i = 0; i < listOptions.size(); i++)
        {
          Node el = listOptions.get(i);
          optionsCaption.add(XMLReader.readTextTrim(el));
          String val = XMLReader.readAttributeValue(el, "value");
          if (val.length() == 0)
            val = XMLReader.readTextTrim(el); //if no value is set, use caption
          optionsValue.add(val);
        }
        c.setOptions(Cast.StringArrayListToStringArray(optionsCaption),
          Cast.StringArrayListToStringArray(optionsValue));
      }

      //second possibility: system dependent options
      else if (systemList.length() > 0)
      {
        loadSystemList(c, systemList);
      }

      //default value?
      String defaultOption = XMLReader.readAttributeValue(
        optionsContainer, "default");
      if (defaultOption.length() > 0)
      {
        //if value starts with "%", this is a system value
        if (defaultOption.startsWith("%"))
        {
          if (defaultOption.equals("%sys_fullscreen%"))
          {
            c.setSelectedIndex(GameEngine.getScreenFullscreen() ? 0 : 1);
          }
          else
          {
            try
            {
              int index = Integer.valueOf(Variables.replaceVariables(defaultOption));
              c.setSelectedIndex(index);
            }
            catch (Exception ex)
            {
              //unknown system value
              Logging.log(Logging.LEVEL_WARNINGS, new OptionButtonParser(),
                "Unknown optionbutton default system value: \"" +
                  defaultOption + "\"");
            }
          }
        }
        //else it is a index
        else
        {
          c.setSelectedIndex(Integer.parseInt(defaultOption));
        }
      }

    }

    //actions
    List<Element> listActions = XMLReader.elements(e, "action");
    for (int i = 0; i < listActions.size(); i++)
    {
      Node el = listActions.get(i);
      Action a = ActionParser.parseAction(el);
      if (XMLReader.readAttributeValue(el, "event").equals("switch"))
        c.addActionSwitch(a);
    }
    return c;
  }
  
  
  /**
   * Loads a system list into the given control.
   */
  public static void loadSystemList(OptionButton c, String systemList)
  {
    //VIDEO MODES - not used any more
    //***********
    if (systemList.equals("screenres"))
    {
      //list available video modes
      Resolution resolution = new Resolution();
      int[][] screenres = resolution.getAvailableVideoModes();
      ArrayList<String> options = new ArrayList<String>();
      int selectedIndex = 0;
      for (int i = 0; i < screenres.length; i++)
      {
        //not only 16... use >= 16
        if (screenres[i][2] == 16)
        {
          options.add(screenres[i][0] + " x " + screenres[i][1]); // + " x " + screenres[i][2];
          //preselect current setting
          if (screenres[i][0] == GameEngine.getScreenDimension().x
            && screenres[i][1] == GameEngine.getScreenDimension().y)
            //depth!?
            selectedIndex = options.size() - 1;
        }
      }
      String[] optionsArray = Cast.StringArrayListToStringArray(options);
      c.setOptions(optionsArray, optionsArray); //caption and value is the same here
      c.setSelectedIndex(selectedIndex);
    }

    //LIST OF ALL TEAMS
    //*****************
    else if (systemList.equals("teams.all"))
    {
      //list available teams
      ArrayList<TeamInfo> teams = GameEngine.getMatchManager().getAvailableTeams();
      String[] teamsName = new String[teams.size()];
      String[] teamsID = new String[teams.size()];
      for (int i = 0; i < teams.size(); i++)
      {
        teamsName[i] = teams.get(i).getName();
        teamsID[i] = teams.get(i).getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST LEAGUE 1 TEAMS
    //*******************
    else if (systemList.equals("teams.league1"))
    {
      //list available teams
      ArrayList<String> league1 = new ArrayList<String>();
      Career.getTeamIDs(league1, null, null, null);
      String teamsName[] = new String[league1.size()];
      String teamsID[] = new String[league1.size()];
      for (int i = 0; i < league1.size(); i++)
      {
        TeamInfo teamInfo = new TeamInfo(league1.get(i));
        teamsName[i] = teamInfo.getName();
        teamsID[i] = teamInfo.getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST LEAGUE 2 TEAMS
    //*******************
    else if (systemList.equals("teams.league2"))
    {
      //list available teams
      ArrayList<String> league2 = new ArrayList<String>();
      Career.getTeamIDs(null, league2, null, null);
      String teamsName[] = new String[league2.size()];
      String teamsID[] = new String[league2.size()];
      for (int i = 0; i < league2.size(); i++)
      {
        TeamInfo teamInfo = new TeamInfo(league2.get(i));
        teamsName[i] = teamInfo.getName();
        teamsID[i] = teamInfo.getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST LEAGUE 3 TEAMS
    //*******************
    else if (systemList.equals("teams.league3"))
    {
      //list available teams
      ArrayList<String> league3 = new ArrayList<String>();
      Career.getTeamIDs(null, null, league3, null);
      String teamsName[] = new String[league3.size()];
      String teamsID[] = new String[league3.size()];
      for (int i = 0; i < league3.size(); i++)
      {
        TeamInfo teamInfo = new TeamInfo(league3.get(i));
        teamsName[i] = teamInfo.getName();
        teamsID[i] = teamInfo.getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST INTERNATIONAL TEAMS
    //************************
    else if (systemList.equals("teams.international"))
    {
      //list available teams
      ArrayList<String> international = new ArrayList<String>();
      Career.getTeamIDs(null, null, null, international);
      String teamsName[] = new String[international.size()];
      String teamsID[] = new String[international.size()];
      for (int i = 0; i < international.size(); i++)
      {
        TeamInfo teamInfo = new TeamInfo(international.get(i));
        teamsName[i] = teamInfo.getName();
        teamsID[i] = teamInfo.getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST WORLDCUP TEAMS
    //*******************
    else if (systemList.equals("teams.worldcup"))
    {
      //list available teams
      ArrayList<String> worldcup = WorldCup.getTeamIDs(false);
      String teamsName[] = new String[worldcup.size()];
      String teamsID[] = new String[worldcup.size()];
      for (int i = 0; i < worldcup.size(); i++)
      {
        TeamInfo teamInfo = new TeamInfo(worldcup.get(i));
        teamsName[i] = teamInfo.getName();
        teamsID[i] = teamInfo.getID();
      }
      c.setOptions(teamsName, teamsID);
    }
    
    //LIST OF STADIUMS
    //****************
    else if (systemList.equals("stadiums"))
    {
      //list available stadiums
      ArrayList<StadiumInfo> stadiums =
        GameEngine.getMatchManager().getAvailableStadiums();
      String[] stadiumsName = new String[stadiums.size()];
      String[] stadiumsID = new String[stadiums.size()];
      for (int i = 0; i < stadiums.size(); i++)
      {
        stadiumsName[i] = stadiums.get(i).getName();
        stadiumsID[i] = stadiums.get(i).getID();
      }
      c.setOptions(stadiumsName, stadiumsID);
    }
    
    //LIST OF WEATHERS
    //****************
    else if (systemList.equals("weathers"))
    {
      //list available weathers
      String[] weathersID = Cast.StringArrayListToStringArray(
        GameEngine.getMatchManager().getAvailableWeathers());
      String[] weathersName = new String[weathersID.length];
      for (int i = 0; i < weathersID.length; i++)
      {
        weathersName[i] = Language.get(weathersID[i]);
      }
      c.setOptions(weathersName, weathersID);
    }
    
    
    //LIST OF REFEREES
    //****************
    else if (systemList.equals("referees"))
    {
      //list available referees
      ArrayList<RefereeInfo> referees =
        GameEngine.getMatchManager().getAvailableReferees();
      String[] refereesName = new String[referees.size()];
      String[] refereesID = new String[referees.size()];
      for (int i = 0; i < referees.size(); i++)
      {
        refereesName[i] = referees.get(i).getName();
        refereesID[i] = referees.get(i).getID();
      }
      c.setOptions(refereesName, refereesID);
    }
    
    
    //LIST OF ALREADY PLAYED MATCHDAYS
    //********************************
    else if (systemList.equals("career.playedmatchdays"))
    {
      if (GameEngine.getGameMode() == GameEngine.GameMode.Career)
      {
        Career career = GameEngine.getCareer();
        int lastPlayedMatchDay = career.getCurrentLeagueMatchDayNumber() - 1;
        String[] ids = new String[lastPlayedMatchDay + 1];
        String[] names = new String[lastPlayedMatchDay + 1];
        ids[0] = "0";
        names[0] = Language.get("startofseason");
        for (int i = 0; i < lastPlayedMatchDay; i++)
        {
          ids[i + 1] = String.valueOf(i + 1);
          names[i + 1] = String.valueOf(i + 1) + ". " +
            Language.get("matchday");
        }
        c.setOptions(names, ids);
      }
      else
      {
        GameEngine.fatalError(new OptionButtonParser(), new Exception(
          "Can not read system list \"" + systemList +
          "\" in screen \"" + c.getParentScreen().getID() +
          "\", because the game is not in career mode!"));
      }
    }
    
    
    //LIST OF ALL MATCHDAYS
    //********************************
    else if (systemList.equals("career.allmatchdays"))
    {
      String[] ids = new String[18];
      String[] names = new String[18];
      for (int i = 0; i < 18; i++)
      {
        ids[i] = String.valueOf(i + 1);
        names[i] = String.valueOf(i + 1) + ". " +
          Language.get("matchday");
      }
      c.setOptions(names, ids);
    }
    
    
    //LIST OF ALL ALLSTARS LEAGUE GROUP MATCHDAYS
    //*******************************************
    else if (systemList.equals("career.allallstarsgroupmatchdays"))
    {
      String[] ids = new String[6];
      String[] names = new String[6];
      for (int i = 0; i < 6; i++)
      {
        ids[i] = String.valueOf(i + 1);
        names[i] = String.valueOf(i + 1) + ". " +
          Language.get("matchday");
      }
      c.setOptions(names, ids);
    }
    
    
    //LIST OF ALL WORLDCUP GROUP MATCHDAYS
    //*******************************************
    else if (systemList.equals("worldcup.allgroupmatchdays"))
    {
      String[] ids = new String[3];
      String[] names = new String[3];
      for (int i = 0; i < 3; i++)
      {
        ids[i] = String.valueOf(i + 1);
        names[i] = String.valueOf(i + 1) + ". " +
          Language.get("matchday");
      }
      c.setOptions(names, ids);
    }
    

    //unknown system list
    else
    {
      GameEngine.fatalError(new OptionButtonParser(), new Exception(
        "Unknown system list \"" + systemList +
        "\" in screen \"" + c.getParentScreen().getID() + "\""));
    }
  }


}
