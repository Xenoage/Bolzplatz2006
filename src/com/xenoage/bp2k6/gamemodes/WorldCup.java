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
package com.xenoage.bp2k6.gamemodes;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.gamemodes.reports.*;
import com.xenoage.bp2k6.match.MatchManager;
import com.xenoage.bp2k6.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;


/**
 * Class for a World Cup competition.
 * Rules: see design document.
 * 
 * It contains a list of all teams,
 * a list of all 8 groups and the
 * 8 eighth finals, the 4 quarter finals,
 * the 2 semi-finals, 1 final and 1 third-place match.
 * 
 * @author Andreas Wenger
 */
public class WorldCup
{

  //list of team ids for this World Cup
  private ArrayList<String> teamIDs = new ArrayList<String>();
  //id of the user-controlled team
  private String userTeamID;
  
  //the number of the current (unplayed) matchday, 1-based
  private int matchDayNumber = 1;
  
  //8 groups
  League groups[] = new League[8];
  private static int groupMatchDays = 3; //each team against each other, one time
  //final matches
  public static final int FINAL_EIGHTH1 = 0;
  public static final int FINAL_EIGHTH2 = 1;
  public static final int FINAL_EIGHTH3 = 2;
  public static final int FINAL_EIGHTH4 = 3;
  public static final int FINAL_EIGHTH5 = 4;
  public static final int FINAL_EIGHTH6 = 5;
  public static final int FINAL_EIGHTH7 = 6;
  public static final int FINAL_EIGHTH8 = 7;
  public static final int FINAL_QUARTER1 = 8;
  public static final int FINAL_QUARTER2 = 9;
  public static final int FINAL_QUARTER3 = 10;
  public static final int FINAL_QUARTER4 = 11;
  public static final int FINAL_SEMI1 = 12;
  public static final int FINAL_SEMI2 = 13;
  public static final int FINAL_THIRDPLACE = 14;
  public static final int FINAL_FINAL = 15;
  MatchInfo finalMatches[] = new MatchInfo[16];
  
  //next referee
  private String nextRefereeID;
  
  
  /**
   * Creates a new World Cup.
   * The teams for the competition are loaded
   * from "data/gamemodes/worldcup.xml".
   * @param userTeamID   the id of the user-controlled team
   */
  public WorldCup(String userTeamID)
  {
    //set id of the user-controlled team
    this.userTeamID = userTeamID;
    //load the 32 custom teams
    teamIDs = getTeamIDs(false);
    //there must be 32 teams.
    if (teamIDs.size() != 32)
    {
      Main.fatalError(this,
        new Exception("There must be exactly 32 World Cup teams!"));
    }
    //create 8 groups
    for (int iGroup = 0; iGroup < 8; iGroup++)
    {
      ArrayList<String> group = new ArrayList<String>();
      for (int i = 0; i < 4; i++)
      {
        group.add(teamIDs.get(iGroup * 4 + i));
      }
      groups[iGroup] = new League(null, League.LEAGUE_WORLDCUP, group);
    }  
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
  }
  
  
  /**
   * Constructor.
   * Loads a worldcup from the given savegame worldcup slot
   * (number between 1 and 5).
   */
  public WorldCup(int id) throws TeamNotFoundException
  {
    //read career from XML file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading world cup \"" + id + "\"...");
    try
    {
      Document doc = XMLReader.readFile(
        "data/savegames/worldcup/worldcup" + id + ".xml");
      
      //root element
      Node root = doc.getFirstChild();
      userTeamID = XMLReader.readAttributeValue(root, "userteamid");
      matchDayNumber = XMLReader.readAttributeValueInt(root, "matchdaynumber");
      
      //load groups
      Node eGroups = XMLReader.getChildElement(root, "groups");
      if (eGroups != null)
      {
        List<Element> listGroups = XMLReader.elements(eGroups, "league");
        if (listGroups.size() != 8)
        {
          GameEngine.fatalError(this, new Exception("World cup must have 8 groups!"));
        }
        for (int i = 0; i < listGroups.size(); i++)
        {
          groups[i] = new League(listGroups.get(i));
        }
      }
      else
      {
        GameEngine.fatalError(this, new Exception("World cup has no groups!"));
      }
      
      //load finals
      Node eFinals = XMLReader.getChildElement(root, "finals");
      if (eFinals != null)
      {
        List<Element> listMatches = XMLReader.elements(eFinals, "match");
        for (int i = 0; i < listMatches.size(); i++)
        {
          finalMatches[i] = new MatchInfo(listMatches.get(i));
        }
      }
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "World cup successfully loaded.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "World cup \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
    
    //check if all teams are existing, otherwise show error and cancel
    MatchManager matchManager = GameEngine.getMatchManager();
    boolean error = false;
    String missingTeamID = null;
    for (int iGroup = 0; iGroup < groups.length; iGroup++)
    {
      Table table = groups[iGroup].getEmptyTable();
      for (int iTeam = 0; iTeam < table.getTableEntriesCount(); iTeam++)
      {
        if (matchManager.getTeamInfo(
          table.getTableEntry(iTeam+1).getTeamID()) == null)
        {
          missingTeamID = table.getTableEntry(iTeam+1).getTeamID();
          error = true;
          break;
        }
      }
    }
    if (error)
    {
      SaveGames.setMissingTeamID(missingTeamID);
      throw new TeamNotFoundException();
    }
    
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
  }
  
  
  /**
   * Gets a list of the 32 World Cup teams
   * from the file "data/gamemodes/worldcup.xml".
   * @param realTeams   if <code>false</code>, the "custom"
   *                    teams are loaded, otherwise the
   *                    "real" World Cup 2006 teams are loaded.
   */
  public static ArrayList<String> getTeamIDs(boolean realTeams)
  {
    //load teams from xml file
    String xmlFile = "data/gamemodes/worldcup.xml";
    Logging.log(Logging.LEVEL_MESSAGES, null,
      "Load list of World Cup teams from \"" + xmlFile + "\"...");
    try
    {
      Document doc = XMLReader.readFile(xmlFile);
      Node root = doc.getFirstChild();
      //find element <teams id="custom">
      ArrayList<String> teams = new ArrayList<String>();
      List<Element> listListTeams = XMLReader.elements(root, "teams");
      for (int iListTeams = 0; iListTeams < listListTeams.size(); iListTeams++)
      {
        Node eTeams = listListTeams.get(iListTeams);
        if ((!realTeams && 
          XMLReader.readAttributeValue(eTeams, "id").equals("custom")) ||
          (realTeams &&
          XMLReader.readAttributeValue(eTeams, "id").equals("real")))
        {
          //load list of teams
          List<Element> listTeams = XMLReader.elements(eTeams, "team");
          for (int iTeams = 0; iTeams < listTeams.size(); iTeams++)
          {
            Node eTeam = listTeams.get(iTeams);
            teams.add(XMLReader.readAttributeValue(eTeam, "id"));
          }
          break;
        }
      }
      return teams;
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, null,
        "Error while loading list of World Cup teams. Details:");
      Main.fatalError(null, ex);
      return null;
    }
  }
  
  
  /**
   * Gets the ID of the user's team.
   */
  public String getUserTeamID()
  {
    return userTeamID;
  }
  
  
  /**
   * Gets the number of the current matchday.
   */
  public int getMatchDayNumber()
  {
    return matchDayNumber;
  }
  
  
  /**
   * Gets the given group (between 0 and 7).
   */
  public League getGroup(int group)
  {
    return groups[group];
  }
  
  
  /**
   * Gets the given final round match
   * (FINAL_ constant).
   */
  public MatchInfo getFinalMatch(int matchIndex)
  {
    return finalMatches[matchIndex];
  }
  
  
  /**
   * Gets the index of the user's group,
   * or -1 if not found.
   */
  public int getUserGroupIndex()
  {
    for (int i = 0; i < 8; i++)
    {
      if (groups[i].hasTeam(userTeamID))
        return i;
    }
    return -1;
  }
  
  
  /**
   * Gets the current match with the user's team,
   * or <code>null</code>, if not found.
   */
  public MatchInfo getUserMatch()
  {
    if (matchDayNumber <= groupMatchDays)
    {
      //group matchdays
      return groups[getUserGroupIndex()].getMatchDay(
        matchDayNumber).getMatch(userTeamID);
    }
    else if (matchDayNumber == groupMatchDays + 1)
    {
      //eighth finals
      for (int i = FINAL_EIGHTH1; i <= FINAL_EIGHTH8; i++)
      {
        if (finalMatches[i].getTeamHomeID().equals(userTeamID) ||
          finalMatches[i].getTeamVisitorID().equals(userTeamID))
          return finalMatches[i];
      }
    }
    else if (matchDayNumber == groupMatchDays + 2)
    {
      //quarter finals
      for (int i = FINAL_QUARTER1; i <= FINAL_QUARTER4; i++)
      {
        if (finalMatches[i].getTeamHomeID().equals(userTeamID) ||
          finalMatches[i].getTeamVisitorID().equals(userTeamID))
          return finalMatches[i];
      }
    }
    else if (matchDayNumber == groupMatchDays + 3)
    {
      //semi finals
      for (int i = FINAL_SEMI1; i <= FINAL_SEMI2; i++)
      {
        if (finalMatches[i].getTeamHomeID().equals(userTeamID) ||
          finalMatches[i].getTeamVisitorID().equals(userTeamID))
          return finalMatches[i];
      }
    }
    else if (matchDayNumber == groupMatchDays + 4)
    {
      //third place match
      if (finalMatches[FINAL_THIRDPLACE].getTeamHomeID().equals(userTeamID) ||
        finalMatches[FINAL_THIRDPLACE].getTeamVisitorID().equals(userTeamID))
        return finalMatches[FINAL_THIRDPLACE];
    }
    else if (matchDayNumber == groupMatchDays + 5)
    {
      //final
      if (finalMatches[FINAL_FINAL].getTeamHomeID().equals(userTeamID) ||
        finalMatches[FINAL_FINAL].getTeamVisitorID().equals(userTeamID))
        return finalMatches[FINAL_FINAL];
    }
    return null;
  }
   
  
  /**
   * Starts the current matchday.
   * The screen <code>matchScreen</code> is opened.
   * The results of the other matches are computed after
   * the user's match, when <code>matchOver()</code> is called.
   */
  public void startMatchDay(String matchScreen)
  {
    MatchInfo userMatch = getUserMatch();
    if (userMatch != null)
    {
      MatchManager matchManager = GameEngine.getMatchManager();
      //random worldcup stadium
      matchManager.setStadium("%randomwcstadium%");
      //referee
      matchManager.setReferee(nextRefereeID);
      matchManager.startCareerWorldCupMatch(
        userTeamID, userMatch.getTeamHomeID(),
        userMatch.getTeamVisitorID(), matchScreen);
    }
    else
    {
      //should never happen, because matchdays without
      //the user's team are already computed before
      GameEngine.fatalError(this,
        new Exception("Current user match not found!"));
    }
  }
  
  
  /**
   * Call this method when the user's match is over.
   * The other results for this matchday are computed,
   * and the next matchday is activated.
   */
  public void matchOver(MatchInfo matchResult)
  {
    //play this matchday
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Play current matchday...");
    computeResults(matchResult);
    //matchday is played, and it is already
    //switched to the next matchday.
    //if the user does not play on this matchday, play it now
    while (matchDayNumber <= groupMatchDays + 5 &&
      getUserMatch() == null)
    {
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Compute matchday results (without userteam)...");
      computeResults(null);
    }
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
    //if it is the end of the worldcup,
    //show the end of worldcup screen
    if (matchDayNumber > groupMatchDays + 5)
    {
      //show worldcup result
      if (finalMatches[FINAL_FINAL].getWinnerTeamID().equals(userTeamID))
        GameEngine.getUIManager().openScreen("championworldcup", null);
      else
        GameEngine.getUIManager().openScreen("worldcupexit", null);
    }
    else
    {
      //open worldcup overview screen
      Logging.log(Logging.LEVEL_MESSAGES, this,
         "Switch to worldcup overview screen...");
      GameEngine.getUIManager().openScreen("worldcup", null);
    }
  }
  
  
  /**
   * Computes the results for all matches of the
   * current matchday and switches to the next matchday.
   * If a certain match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result. 
   */
  public void computeResults(MatchInfo alreadyComputedResult)
  {
    if (matchDayNumber <= groupMatchDays)
    {
      //play group games
      for (int i = 0; i < 8; i++)
        groups[i].computeResults(alreadyComputedResult, userTeamID);
      //if this was the last group game, compute the
      //quarter finals
      if (matchDayNumber == groupMatchDays)
      {
        //a1:b2; c1:d2; e1:f2; g1:h2;
        //b1:a2; d1:c2; f1:e2; h1:g2;
        for (int i = 0; i < 4; i++)
        {
          finalMatches[i] = new MatchInfo(
            groups[i * 2].getMatchDay(3).getTable().getTableEntry(1).getTeamID(),
            groups[i * 2 + 1].getMatchDay(3).getTable().getTableEntry(2).getTeamID(),
            false);
        }
        for (int i = 0; i < 4; i++)
        {
          finalMatches[4 + i] = new MatchInfo(
            groups[i * 2 + 1].getMatchDay(3).getTable().getTableEntry(1).getTeamID(),
            groups[i * 2].getMatchDay(3).getTable().getTableEntry(2).getTeamID(),
            false);
        }
      }
    }
    else if (matchDayNumber == groupMatchDays + 1)
    {
      //play eighth finals
      for (int i = FINAL_EIGHTH1; i <= FINAL_EIGHTH8; i++)
      {
        finalMatches[i].computeResult(alreadyComputedResult);
      }
      //compute the teams for the quarter finals
      for (int i = 0; i < 4; i++)
      {
        finalMatches[FINAL_QUARTER1 + i] = new MatchInfo(
          finalMatches[FINAL_EIGHTH1 + i * 2].getWinnerTeamID(),
          finalMatches[FINAL_EIGHTH2 + i * 2].getWinnerTeamID(),
          false);
      }
    }
    else if (matchDayNumber == groupMatchDays + 2)
    {
      //play quarter finals
      for (int i = FINAL_QUARTER1; i <= FINAL_QUARTER4; i++)
      {
        finalMatches[i].computeResult(alreadyComputedResult);
      }
      //compute the teams for the semi finals
      finalMatches[FINAL_SEMI1] = new MatchInfo(
        finalMatches[FINAL_QUARTER1].getWinnerTeamID(),
        finalMatches[FINAL_QUARTER2].getWinnerTeamID(),
        false);
      finalMatches[FINAL_SEMI2] = new MatchInfo(
        finalMatches[FINAL_QUARTER3].getWinnerTeamID(),
        finalMatches[FINAL_QUARTER4].getWinnerTeamID(),
        false);
    }
    else if (matchDayNumber == groupMatchDays + 3)
    {
      //play semi finals
      finalMatches[FINAL_SEMI1].computeResult(alreadyComputedResult);
      finalMatches[FINAL_SEMI2].computeResult(alreadyComputedResult);
      //3rd place match
      finalMatches[FINAL_THIRDPLACE] = new MatchInfo(
        finalMatches[FINAL_SEMI1].getLoserTeamID(),
        finalMatches[FINAL_SEMI2].getLoserTeamID(),
        false);
      //final
      finalMatches[FINAL_FINAL] = new MatchInfo(
        finalMatches[FINAL_SEMI1].getWinnerTeamID(),
        finalMatches[FINAL_SEMI2].getWinnerTeamID(),
        false);
    }
    else if (matchDayNumber == groupMatchDays + 4)
    {
      //play 3rd place match
      finalMatches[FINAL_THIRDPLACE].computeResult(alreadyComputedResult);
    }
    else if (matchDayNumber == groupMatchDays + 5)
    {
      //play final
      finalMatches[FINAL_FINAL].computeResult(alreadyComputedResult);
    }
    matchDayNumber++;
  }
  
  
  /**
   * Saves this worldcup in the file
   * <code>"data/savegames/worldcup/worldcup" + id + ".xml"</code>.
   */
  public void saveToXMLFile(int id)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving worldcup \"" + id + "\"...");
    try
    {
    
      Document document = XMLWriter.createEmptyDocument();
      //root element
      Node root = document.createElement("savegame");
      document.appendChild(root);
      XMLWriter.addAttribute(root, "id", String.valueOf(id));
      XMLWriter.addAttribute(root, "gamemode", "worldcup");
      //user team id
      XMLWriter.addAttribute(root, "userteamid", userTeamID);
      //current matchday
      XMLWriter.addAttribute(root, "matchdaynumber", String.valueOf(matchDayNumber));
    
      /* //create worldcup element
      Element eWorldCup = root.addElement("worldcup");
      //list of all teams
      Element eTeams = eWorldCup.addElement("teams");
      for (int i = 0; i < teamIDs.size(); i++)
      {
        String teamID = teamIDs.get(i);
        Element eTeam = eTeams.addElement("team");
        eTeam.addAttribute("id", teamID);
      } */
      
      //save the 8 groups
      Node eGroups = XMLWriter.addElement("groups", root);
      for (int i = 0; i < 8; i++)
      {
        groups[i].saveToXML(eGroups);
      }
      //save the final matches, if already played
      Node eFinals = XMLWriter.addElement("finals", root);
      for (int i = 0; i < 16; i++)
      {
        if (finalMatches[i] != null)
          finalMatches[i].saveToXML(eFinals);
        else
          break;
      }
      
      String file = "data/savegames/worldcup/worldcup" + id + ".xml";
      XMLWriter.writeFile(document, file);
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Worldcup \"" + id + "\" saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while saving worldcup \"" + id + "\":");
      Logging.log(this, ex);
    }

  }


  /**
   * Gets the ID of the referee of the user's next match.
   */
  public String getNextRefereeID()
  {
    return nextRefereeID;
  }

  
  /**
   * Returns <code>true</code> if the given worldcup savegame slot
   * is used.
   */
  public static boolean isSlotUsed(int slot)
  {
    return new File("data/savegames/worldcup/worldcup" + slot + ".xml").exists();
  }
  
  
  /**
   * Returns true, if the current matchday is still
   * a group matchday.
   */
  public boolean isCurrentMatchDayGroupMatchDay()
  {
    return (matchDayNumber <= groupMatchDays);
  }


  /**
   * Gets the number of group matchdays.
   */
  public static int getGroupMatchDays()
  {
    return groupMatchDays;
  }

  
}
