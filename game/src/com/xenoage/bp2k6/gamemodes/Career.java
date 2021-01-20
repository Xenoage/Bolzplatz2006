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
import com.xenoage.bp2k6.gamemodes.events.CareerEvent;
import com.xenoage.bp2k6.gamemodes.events.CareerEventPool;
import com.xenoage.bp2k6.gamemodes.reports.*;
import com.xenoage.bp2k6.match.MatchManager;
import com.xenoage.bp2k6.match.TeamInfo;
import com.xenoage.bp2k6.match.characters.PlayerInfo;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;


/**
 * Class for a career.
 * Details: see design document.
 * 
 * @author Andreas Wenger
 */
public class Career
{
  //the user's team
  private String userTeamID;
  
  //current season, year and the user's league
  private Season currentSeason;
  private int currentYear;
  private int currentLeague;
  
  //types of leagues
  public static final int LEAGUE_ALLSTARS = 0;
  public static final int LEAGUE_1 = 1;
  public static final int LEAGUE_2 = 2;
  public static final int LEAGUE_3 = 3;
  
  //the current bank balance of the user
  private int userMoney;
  
  //the stadium of the user
  private Stadium userStadium;
  
  //types of matchdays
  public static final int MATCHDAY_ALLSTARS = 0;
  public static final int MATCHDAY_LEAGUE = 1;
  
  //the current event
  private CareerEvent currentEvent;
  
  //the current match result with the user's team
  private MatchInfo currentMatchResult;
  
  //next referee
  private String nextRefereeID;
  private float nextRefereePrejudice = 0;
  private boolean nextRefereeCorruptionAttempted = false;
  
  //next user team's value changes for the next match
  private int nextAttackModification = 0;
  private int nextDefenseModification = 0;
  private int nextSpeedModification = 0;
  private int nextStaminaModification = 0;
  
  
  /**
   * Creates a new career.
   * The teams for the 1., 2. and 3. league are loaded
   * from "data/gamemodes/career.xml", the teams of the
   * ÜFA Allstars League are the 3 best 1.-league-teams
   * and 13 random international teams from the file.
   */
  public Career(String userTeamID, int startYear)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Create a new career with user team \"" + userTeamID +
      "\" in the year " + startYear + "...");
    this.userTeamID = userTeamID;
    this.currentYear = startYear;
    //load leagues from xml file
    try
    {
      //leagues
      ArrayList<String> league1 = new ArrayList<String>();
      ArrayList<String> league2 = new ArrayList<String>();
      ArrayList<String> league3 = new ArrayList<String>();
      ArrayList<String> international = new ArrayList<String>();
      getTeamIDs(league1, league2, league3, international);
      //1., 2. and 3. league must have 10 teams
      if (league1.size() != 10 || league1.size() != 10 || league1.size() != 10)
      {
        throw new Exception("1., 2. and 3. league must have 10 teams!");
      }
      //there must be 13 international teams. if there
      //are more, remove the rest at random.
      //then add the 3 best national teams
      if (international.size() < 13)
      {
        throw new Exception("There must be at least 13 international teams!");
      }
      else
      {
        while (international.size() > 13)
        {
          international.remove((int) (Math.random() * international.size()));
        }
      }
      international.add(league1.get(0));
      international.add(league1.get(1));
      international.add(league1.get(2));
      //search user team in the leagues
      String userTeamFound = removeAndReturnString(league1, userTeamID);
      if (userTeamFound != null)
      {
        //change last team of 3. league with user team of 1. league
        league1.add(league3.remove(league3.size()-1));
        league3.add(userTeamID);
      }
      else
      {
        userTeamFound = removeAndReturnString(league2, userTeamID);
        if (userTeamFound != null)
        {
          //change last team of 3. league with user team of 2. league
          league2.add(league3.remove(league3.size()-1));
          league3.add(userTeamID);
        }
        else
        {
          userTeamFound = removeAndReturnString(league3, userTeamID);
          if (userTeamFound != null)
          {
            //move user team to the end of the list
            league3.add(userTeamID);
          }
          else
          {
            //user team is not in default leagues.
            //replace last 3. league team with user team
            league3.remove(league3.size()-1);
            league3.add(userTeamID);
          }
        }
      }
      //create season
      currentSeason = new Season(this, currentYear,
        league1, league2, league3, international);
      currentLeague = LEAGUE_3;
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while creating career. Details:");
      Main.fatalError(this, ex);
    }
    //copy stadium template into data/temp/ and load info
    File fileStadium = new File(
      "data/gamemodes/career-stadium.xml");
    if (fileStadium.exists())
    {
      try
      {
        FileUtils.copy(fileStadium, new File("data/temp/career-stadium.xml"));
        userStadium = new Stadium("career-stadium");
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Error while copying stadium template file:");
        Main.fatalError(this, ex);
      }
    }
    else
    {
      Main.fatalError(this, new Exception(
        "Could not find stadium template!"));
    }
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
    
    String sUserMoney = ConfigFile.getValueFromFile(
      "data/config/cost.xml", "careerstart");
    if (sUserMoney.length() > 0)
      userMoney = Integer.parseInt(sUserMoney);
  }
  
  
  /**
   * Constructor.
   * Loads a career from the given savegame career slot
   * (number between 1 and 5).
   */
  public Career(int id) throws TeamNotFoundException
  {
    //read career from XML file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading career \"" + id + "\"...");
    try
    {
      Document doc = XMLReader.readFile(
        "data/savegames/career/career" + id + ".xml");
      
      //root element
      Node root = doc.getFirstChild();
      userTeamID = XMLReader.readAttributeValue(root, "userteamid");
      userMoney = XMLReader.readAttributeValueInt(root, "usermoney");
      currentYear = XMLReader.readAttributeValueInt(root, "currentyear");
      currentLeague = XMLReader.readAttributeValueInt(root, "currentleague");
      //list of seasons
      Node eSeasons = XMLReader.getChildElement(root, "seasons");
      if (eSeasons != null)
      {
        currentSeason = new Season(XMLReader.getChildElement(eSeasons, "season"));
      }
      else
      {
        GameEngine.fatalError(this, new Exception("Career has no seasons!"));
      }
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Career successfully loaded.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Career \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
    
    //check if all teams are existing, otherwise show error and cancel
    MatchManager matchManager = GameEngine.getMatchManager();
    boolean error = false;
    String missingTeamID = null;
    for (int iLeague = LEAGUE_1; iLeague <= LEAGUE_3; iLeague++)
    {
      Table table = currentSeason.getLeague(iLeague).getEmptyTable();
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
    if (!error)
    {
      AllstarsLeague allstarsLeague = currentSeason.getAllstarsLeague();
      for (int iGroup = 0; iGroup < allstarsLeague.getGroupsCount(); iGroup++)
      {
        Table table = allstarsLeague.getEmptyTable(iGroup);
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
    }
    if (error)
    {
      SaveGames.setMissingTeamID(missingTeamID);
      throw new TeamNotFoundException();
    }
    
    //copy stadium into data/temp/ and load info
    File fileStadium = new File(
      "data/savegames/career/career" + id + "-stadium.xml");
    if (fileStadium.exists())
    {
      try
      {
        FileUtils.copy(fileStadium, new File("data/temp/career-stadium.xml"));
        userStadium = new Stadium("career-stadium");
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Error while copying stadium file:");
        Main.fatalError(this, ex);
      }
    }
    else
    {
      Main.fatalError(this, new Exception(
        "Invalid career savegame: Stadium not found!"));
    }
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
  }
  
  
  /**
   * Loads the team IDs into the corresponding leagues.
   * Everything is loaded from "data/gamemodes/career.xml".
   * If a certain league is not needed, its parameter may
   * be <code>null</code>.
   */
  public static void getTeamIDs(ArrayList<String> league1,
    ArrayList<String> league2, ArrayList<String> league3,
    ArrayList<String> international)
  {
    //load leagues from xml file
    String xmlFile = "data/gamemodes/career.xml";
    Logging.log(Logging.LEVEL_MESSAGES, null,
      "Load career teams from \"" + xmlFile + "\"...");
    try
    {
      Document doc = XMLReader.readFile(xmlFile);
      Node root = doc.getFirstChild();
      //leagues
      Node eLeagues = XMLReader.getChildElement(root, "leagues");
      if (eLeagues != null)
      {
        List<Element> listLeagues = XMLReader.elements(eLeagues, "league");
        for (int iLeague = 0; iLeague < listLeagues.size(); iLeague++)
        {
          Node eLeague = listLeagues.get(iLeague);
          int type = XMLReader.readAttributeValueInt(eLeague, "type");
          List<Element> listTeams = XMLReader.elements(eLeague, "team");
          for (int iTeam = 0; iTeam < listTeams.size(); iTeam++)
          {
            Node eTeam = listTeams.get(iTeam);
            String teamID = XMLReader.readAttributeValue(eTeam, "id");
            if (type == League.LEAGUE_1 && league1 != null)
              league1.add(teamID);
            else if (type == League.LEAGUE_2 && league2 != null)
              league2.add(teamID);
            else if (type == League.LEAGUE_3 && league3 != null)
              league3.add(teamID);
            if (type == League.LEAGUE_ALLSTARS && international != null)
              international.add(teamID);
          }
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, null,
        "Error while loading career teams. Details:");
      Main.fatalError(null, ex);
    }
  }
  
  
  /**
   * Gets the id of the default stadium for
   * the given league (data/config/stadiums.xml)
   */
  private static String getDefaultStadiumID(int league)
  {
    try
    {
      ConfigFile config = new ConfigFile("data/config/stadiums.xml");
      if (league == LEAGUE_ALLSTARS)
        return config.getValue("leagueallstars");
      if (league == LEAGUE_1)
        return config.getValue("league1");
      if (league == LEAGUE_2)
        return config.getValue("league2");
      else
        return config.getValue("league3");
    }
    catch (Exception ex)
    {
      return "";
    }
  }
  
  
  /**
   * Loads the career team IDs and returns them without
   * regarding their corresponding leagues.
   */
  public static ArrayList<String> getTeamIDs()
  {
    ArrayList<String> arr1 = new ArrayList<String>();
    ArrayList<String> arr2 = new ArrayList<String>();
    ArrayList<String> arr3 = new ArrayList<String>();
    ArrayList<String> arr4 = new ArrayList<String>();
    getTeamIDs(arr1, arr2, arr3, arr4);
    ArrayList<String> ret = new ArrayList<String>();
    ret.addAll(arr1);
    ret.addAll(arr2);
    ret.addAll(arr3);
    ret.addAll(arr4);
    return ret;
  }
  
  
  /**
   * Searches for a String in the given ArrayList.
   * If found, it is removed and returned, else
   * null is returned.
   */
  private String removeAndReturnString(ArrayList<String> arr, String str)
  {
    for (int i = 0; i < arr.size(); i++)
      if (arr.get(i).equals(str))
        return arr.remove(i);
    return null;
  }
  
  
  /**
   * Gets the ID of the user's team.
   */
  public String getUserTeamID()
  {
    return userTeamID;
  }
  
  
  /**
   * Gets the type of the current matchday:
   * <code>MATCHDAY_ALLSTARS</code> or <code>MATCHDAY_LEAGUE</code>.
   */
  public int getCurrentMatchDayType()
  {
    return currentSeason.getCurrentMatchDayType();
  }
  
  
  /**
   * Gets the league of the current matchday.
   * This is always the user's normal league or
   * LEAGUE_ALLSTARS if the user team is in the
   * Allstars League and now there is an Allstars matchday.
   */
  public int getCurrentMatchDayLeague()
  {
    if (currentLeague == LEAGUE_ALLSTARS)
      if (currentSeason.getCurrentMatchDayType() == Season.MATCHDAY_ALLSTARS)
        return LEAGUE_ALLSTARS;
      else
        return LEAGUE_1;
    else
      return currentLeague;
  }
  
  
  /**
   * Gets the current league (constants starting with LEAGUE_)
   * of the user's team.
   */
  public int getCurrentUserLeague()
  {
    return currentLeague;
  }
  
  
  /**
   * Gets the current match with the userteam,
   * or <code>null</code>, if not found.
   */
  public MatchInfo getCurrentUserMatch()
  {
    return currentSeason.getCurrentMatch(userTeamID);
  }
  
  
  /**
   * Gets the current money of the user.
   */
  public int getUserMoney()
  {
    return userMoney;
  }
  
  
  /**
   * Sets the current money of the user.
   */
  public void setUserMoney(int userMoney)
  {
    this.userMoney = userMoney;
    if (this.userMoney < 0)
      this.userMoney = 0;
  }
  
  
  /**
   * Gets the current year.
   */
  public int getCurrentYear()
  {
    return currentYear;
  }
  
  
  /**
   * Gets the number of the current allstars league matchday.
   */
  public int getCurrentAllstarsMatchDayNumber()
  {
    return currentSeason.getCurrentAllstarsMatchDayNumber();
  }
  
  
  /**
   * Gets the allstars league.
   */
  public AllstarsLeague getAllstars()
  {
    return currentSeason.getAllstarsLeague();
  }
  
  
  /**
   * Gets the number of the current league matchday.
   */
  public int getCurrentLeagueMatchDayNumber()
  {
    return currentSeason.getCurrentLeagueMatchDayNumber();
  }
  
  
  /**
   * Gets the given league (LEAGUE_1, LEAGUE_2 or LEAGUE_3).
   * If the user is in Allstars League, LEAGUE_1 is returned.
   */
  public League getLeague(int league)
  {
    int l = league == LEAGUE_ALLSTARS ? LEAGUE_1 : league;
    return currentSeason.getLeague(l);
  }
  
  
  /**
   * Gets the id of the given league, e.g.
   * "leagueallstars" or "league2".
   */
  public String getLeagueID(int league)
  {
    if (league == LEAGUE_ALLSTARS)
      return "leagueallstars";
    else if (league == LEAGUE_1)
      return "league1";
    else if (league == LEAGUE_2)
      return "league2";
    else
      return "league3";
  }
  
  
  /**
   * Gets the long name of the given league.
   */
  public String getLeagueName(int league)
  {
    if (league == LEAGUE_ALLSTARS)
      return Language.get("leagueallstarslong");
    else if (league == LEAGUE_1)
      return Language.get("league1long");
    else if (league == LEAGUE_2)
      return Language.get("league2long");
    else
      return Language.get("league3long");
  }
  
  
  /**
   * Gets the name of the given matchday in the
   * given league.
   */
  public String getMatchDayName(int number, int league)
  {
    if (league == LEAGUE_ALLSTARS)
    {
      if (number <= AllstarsLeague.MATCHDAY_GROUP6)
        return number + ". " + Language.get("groupmatchday");
      else if (number == AllstarsLeague.MATCHDAY_QUARTER)
        return Language.get("quarterfinal");
      else if (number == AllstarsLeague.MATCHDAY_SEMI)
        return Language.get("semifinal");
      else if (number == AllstarsLeague.MATCHDAY_THIRDPLACE)
        return Language.get("thirdplacematch");
      else
        return Language.get("final");
    }
    else
    {
      String ret = number + ". " + Language.get("matchday");
      if (number == 9)
        ret += " (" + Language.get("winterbreak") + ")";
      else if (number == 18)
        ret += " (" + Language.get("seasonfinal") + ")";
      return ret;
    }
  }
  
  
  /**
   * Call this method when the user's match is over.
   * The other results for this matchday are computed,
   * and the next matchday is activated.
   */
  public void matchOver(MatchInfo matchResult)
  {
    //if there is a current event, perform after-match-actions
    currentMatchResult = matchResult;
    if (currentEvent != null)
      currentEvent.perform(this, true);
    currentEvent = null;
    //get money for the match
    userMoney += getMatchResultIncome(matchResult);
    //if this was a allstars matchday with the user's team
    //show result if needed
    boolean screenShown = false;
    boolean wasAllstars = false;
    if (currentLeague == LEAGUE_ALLSTARS &&
      currentSeason.getCurrentMatchDayType() == Season.MATCHDAY_ALLSTARS)
    {
      wasAllstars = true;
      AllstarsLeague allstarsLeague = currentSeason.getAllstarsLeague();
      if (matchResult.getWinnerTeamID().equals(userTeamID) &&
        allstarsLeague.getCurrentMatchDayNumber() == AllstarsLeague.MATCHDAY_FINAL)
      {
        //user won the final!
        userMoney += 20000; //award
        GameEngine.getUIManager().openScreen("championallstars", null);
        screenShown = true;
      }
      else if (((allstarsLeague.getCurrentMatchDayNumber() ==
        AllstarsLeague.MATCHDAY_QUARTER ||
        allstarsLeague.getCurrentMatchDayNumber() ==
          AllstarsLeague.MATCHDAY_FINAL) &&
        matchResult.getLoserTeamID().equals(userTeamID)) || //lost quarter or final
        allstarsLeague.getCurrentMatchDayNumber() ==
          AllstarsLeague.MATCHDAY_THIRDPLACE) //after third place match
      {
        //final matches: lost! user's team exited
        GameEngine.getUIManager().openScreen("allstarsexit", null);
        screenShown = true;
      }
    }
    //play this matchday
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Play current matchday...");
    //compute other results
    currentSeason.computeResults(matchResult, userTeamID);
    //matchday is played, and it is already
    //switched to the next matchday.
    if (wasAllstars)
    {
      AllstarsLeague allstarsLeague = currentSeason.getAllstarsLeague();
      if (allstarsLeague.getCurrentMatchDayNumber() ==
        AllstarsLeague.MATCHDAY_QUARTER &&
        !allstarsLeague.isTeamQualifiedForFinals(userTeamID))
      {
        //user team exited in the group phase
        GameEngine.getUIManager().openScreen("allstarsexit", null);
        screenShown = true;
      }
    }
    //if this one is a allstars league matchday,
    //but the user's team is not in the allstars
    //league, or the user does not play on this matchday play it now
    while (
      currentSeason.getCurrentMatchDayType() == Season.MATCHDAY_ALLSTARS &&
      (currentLeague != LEAGUE_ALLSTARS || //user is not in allstars league
       currentSeason.getAllstarsLeague().getCurrentMatch(
         userTeamID) == null)) //user does not play on this matchday
    {
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Compute Allstars League results (without userteam)...");
      currentSeason.computeResults(null, userTeamID);
    }
    //get next referee
    nextRefereeID = GameEngine.getMatchManager().getRandomReferee().getID();
    nextRefereePrejudice = 0;
    nextRefereeCorruptionAttempted = false;
    //if it is winter break now, switch to the next matchday
    //(also done with computeResults()) and show the winter break group
    if (currentSeason.getCurrentMatchDayType() ==
      Season.MATCHDAY_WINTERBREAK)
    {
      Logging.log(Logging.LEVEL_MESSAGES, this, "Winter break.");
      currentSeason.computeResults(null, userTeamID);
      GameEngine.getUIManager().openScreen("winterbreak", null);
    }
    //if it is the end of the season, create the new season
    //and show the end of season group
    else if (currentSeason.getCurrentMatchDayType() ==
      Season.MATCHDAY_ENDOFSEASON)
    {
      //find end of session screen
      int league = MathTools.clamp(currentLeague, LEAGUE_1, LEAGUE_3);
      int position = currentSeason.getLeague(
        league).getLastMatchDay().getTable().getPosition(userTeamID);
      String screenID = "endofseason";
      if (league == LEAGUE_1)
      {
        if (position == 1)
          screenID = "champion1";
        else if (position <= 3)
          screenID = "allstarspromotion";
        else if (position >= 8)
          screenID = "relegation";
        //award
        if (position == 1)
          userMoney += 8000;
        else if (position == 2)
          userMoney += 4000;
        if (position == 3)
          userMoney += 2400;
      }
      else if (league == LEAGUE_2)
      {
        if (position == 1)
          screenID = "champion2";
        else if (position <= 3)
          screenID = "promotion";
        else if (position >= 8)
          screenID = "relegation";
        //award
        if (position == 1)
          userMoney += 4000;
        else if (position == 2)
          userMoney += 2000;
        if (position == 3)
          userMoney += 1200;
      }
      else if (league == LEAGUE_3)
      {
        if (position == 1)
          screenID = "champion3";
        else if (position <= 3)
          screenID = "promotion";
        else if (position >= 8)
          screenID = "relegation";
        //award
        if (position == 1)
          userMoney += 2000;
        else if (position == 2)
          userMoney += 1000;
        if (position == 3)
          userMoney += 600;
      }
      //create new season
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "End of season. Create new season...");
      currentSeason = currentSeason.computeNextSeason();
      currentYear++;
      currentLeague = currentSeason.getLeagueOfTeam(userTeamID);
      //show season result
      GameEngine.getUIManager().openScreen(screenID, null);
    }
    //if no other screen is shown: open the career overview window
    else if (!screenShown)
    {
      //open career overview screen
      Logging.log(Logging.LEVEL_MESSAGES, this,
         "Switch to career overview screen...");
      GameEngine.getUIManager().openScreen("career", null);
    }
  }
  
  
  /**
   * Saves this career in the file
   * <code>"data/savegames/career/career" + id + ".xml"</code>
   * and the stadium in the file
   * <code>"data/savegames/career/career" + id + "-stadium.xml"</code>
   */
  public void saveToXMLFile(int id)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving career \"" + id + "\"...");
    try
    {
    
      Document document = XMLWriter.createEmptyDocument();
      //root element
      Node root = XMLWriter.addElement("savegame", document);
      XMLWriter.addAttribute(root, "id", String.valueOf(id));
      XMLWriter.addAttribute(root, "gamemode", "career");
      //user team id
      XMLWriter.addAttribute(root, "userteamid", userTeamID);
      //the user's money
      XMLWriter.addAttribute(root, "usermoney", String.valueOf(userMoney));
      //current season (year)
      XMLWriter.addAttribute(root, "currentyear", String.valueOf(currentYear));
      //current league
      XMLWriter.addAttribute(root, "currentleague", String.valueOf(currentLeague));
      //current season matchday (needed for fast loading of career information)
      XMLWriter.addAttribute(root, "currentmatchday",
        String.valueOf(currentSeason.getCurrentMatchDay()));
      //current league matchday (needed for fast loading of career information)
      XMLWriter.addAttribute(root, "currentleaguematchday",
        String.valueOf(getCurrentLeagueMatchDayNumber()));
      //current allstars league matchday (needed for fast loading of career information)
      XMLWriter.addAttribute(root, "currentallstarsleaguematchday",
        String.valueOf(getCurrentAllstarsMatchDayNumber()));
      //list of seasons
      Node eSeasons = XMLWriter.addElement("seasons", root);
      currentSeason.saveToXML(eSeasons);
      
      XMLWriter.writeFile(document, "data/savegames/career/career" + id + ".xml");
      
      //save stadium into data/temp/ and copy it the the savegame folder
      File stadiumSrcPath = new File(
        "data/temp/career" + id + "-stadium.xml");
      File stadiumDestFile = new File(
        "data/savegames/career/career" + id + "-stadium.xml");
      userStadium.saveToXMLFile("career" + id + "-stadium", true);
      if (stadiumSrcPath.exists())
      {
        try
        {
          FileUtils.copy(stadiumSrcPath, stadiumDestFile);
          stadiumSrcPath.delete();
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Error while copying saved stadium:");
          Main.fatalError(this, ex);
        }
      }
      else
      {
        Main.fatalError(this, new Exception(
          "Could not find stadium \"career" + id + "-stadium\" after saving!"));
      }
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Career \"" + id + "\" saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while saving career \"" + id + "\":");
      Main.fatalError(this, ex);
    }

  }
  
  
  /**
   * Starts the current matchday. Possibly a career
   * event is raised instead, but only if
   * <code>careerEvent</code> is true.
   * The screen <code>matchScreen</code> is opened.
   * The results of the other matches are computed after
   * the user's match, when <code>matchOver()</code> is called.
   */
  public void startMatchDay(String matchScreen, boolean careerEvent)
  {
    MatchManager matchManager = GameEngine.getMatchManager();
    //reset value modifications
    nextAttackModification = 0;
    nextDefenseModification = 0;
    nextSpeedModification = 0;
    nextStaminaModification = 0;
    //raise career event?
    CareerEventPool eventPool = GameEngine.getCareerEventPool();
    CareerEvent event = null;
    if (careerEvent)
      event = eventPool.getRandomEvent(this);
    if (event != null)
    {
      //show event
      currentEvent = event;
      currentEvent.perform(this, false);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("careerevent");
    }
    else
    {
      //no event. start match.
      MatchInfo userMatch = currentSeason.getCurrentMatch(userTeamID);
      if (userMatch != null)
      {
        //stadium
        String stadiumID;
        if (userTeamID.equals(userMatch.getTeamHomeID()))
        {
          //home stadium
          stadiumID = "career-stadium";
        }
        else
        {
          //away stadium
          stadiumID =
            matchManager.getTeamInfo(userMatch.getTeamHomeID()).getStadium();
          if (stadiumID == null || stadiumID.length() == 0 ||
            (!new File("data/stadiums/" + stadiumID + ".xml").exists()))
          {
            //use default stadium for this league
            stadiumID = getDefaultStadiumID(getCurrentMatchDayLeague());
          }
        }
        matchManager.setStadium(stadiumID);
        //referee
        matchManager.setReferee(nextRefereeID);
        matchManager.setRefereePrejudice((int) (nextRefereePrejudice));
        //start match
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
  }
  
  
  /**
   * Gets the current career event, or <code>null</code>.
   */
  public CareerEvent getCurrentEvent()
  {
    return currentEvent;
  }
  
  
  /**
   * Gets the current match result with the user's team.
   * May also be <code>null</code> before the first match.
   */
  public MatchInfo getCurrentMatchResult()
  {
    return currentMatchResult;
  }


  /**
   * Gets the user's stadium.
   */
  public Stadium getUserStadium()
  {
    return userStadium;
  }
  
  
  /**
   * Gets the money for a played match.
   */
  private int getMatchResultIncome(MatchInfo match)
  {
    int ret = 0;
    int league = getCurrentMatchDayLeague();
    
    //match result
    if (match.getScore()[0] == match.getScore()[1])
    {
      //draw
      if (league == LEAGUE_3)
        ret += 200;
      else if (league == LEAGUE_2)
        ret += 300;
      else if (league == LEAGUE_1)
        ret += 500;
      if (league == LEAGUE_ALLSTARS)
        ret += 700;
    }
    else if (match.getWinnerTeamID().equals(userTeamID))
    {
      //win
      if (league == LEAGUE_3)
        ret += 500;
      else if (league == LEAGUE_2)
        ret += 800;
      else if (league == LEAGUE_1)
        ret += 1200;
      if (league == LEAGUE_ALLSTARS)
        ret += 1500;
    }
    else
    {
      //lose
      if (league == LEAGUE_3)
        ret += 100;
      else if (league == LEAGUE_2)
        ret += 150;
      else if (league == LEAGUE_1)
        ret += 250;
      if (league == LEAGUE_ALLSTARS)
        ret += 400;
    }
    
    //adboards
    if (match.getTeamHomeID().equals(userTeamID))
    {
      int adboardsCount = userStadium.getCareerAdboardsCount();
      if (league == LEAGUE_3)
        ret += 100 * adboardsCount;
      else if (league == LEAGUE_2)
        ret += 150 * adboardsCount;
      else if (league == LEAGUE_1)
        ret += 200 * adboardsCount;
      if (league == LEAGUE_ALLSTARS)
        ret += 250 * adboardsCount;
    }
    
    //spectators' entrance fee is already computed
    //in Match class.
    
    return ret;
  }
  
  
  /**
   * Gets the ID of the referee of the user's next match.
   */
  public String getNextRefereeID()
  {
    return nextRefereeID;
  }
  
  
  /**
   * Gets information about the next referee.
   */
  public RefereeInfo getNextRefereeInfo()
  {
    return GameEngine.getMatchManager().getRefereeInfo(nextRefereeID);
  }


  /**
   * Returns <code>true</code>, if the user already tried
   * to corrupt the referee.
   */
  public boolean isNextRefereeCorruptionAttempted()
  {
    return nextRefereeCorruptionAttempted;
  }


  /**
   * Sets the prejudice value of the referee.
   */
  public void setNextRefereePrejudice(float nextRefereePrejudice)
  {
    this.nextRefereePrejudice = nextRefereePrejudice;
    this.nextRefereeCorruptionAttempted = true;
  }
  
  
  /**
   * Returns <code>true</code> if the given career savegame slot
   * is used.
   */
  public static boolean isSlotUsed(int slot)
  {
    return new File("data/savegames/career/career" + slot + ".xml").exists();
  }
  
  
  /**
   * Tries to corrupt the referee by the given stage.
   * When successfull, true is returned, otherwise false.
   */
  public boolean corruptReferee(int stage)
  {
    nextRefereeCorruptionAttempted = true;
    RefereeInfo ref = GameEngine.getMatchManager().getRefereeInfo(nextRefereeID);
    float level = 0;
    if (stage == 1)
      level = 0.2f;
    else if (stage == 2)
      level = 0.3f;
    else if (stage == 3)
      level = 0.4f;
    if ((0.1 + MathTools.random()) * ref.getLoyality() / 10f < level)
    {
      MatchInfo matchInfo = getCurrentUserMatch();
      int side = (matchInfo.getTeamHomeID().equals(userTeamID) ? -1 : 1);
      if (stage == 1)
        level = 4f;
      else if (stage == 2)
        level = 7f;
      else if (stage == 3)
        level = 10f;
      nextRefereePrejudice = side * level;
      return true;
    }
    else
    {
      return false;
    }
  }
  
  
  /**
   * Modify the user team's values for the next match.
   */
  public void setUserTeamValuesNextGame(int attackModification,
    int defenseModification, int speedModification, int staminaModification)
  {
    this.nextAttackModification = attackModification;
    this.nextDefenseModification = defenseModification;
    this.nextSpeedModification = speedModification;
    this.nextStaminaModification = staminaModification;
  }
  
  
  /**
   * Apply the modifications to the given team info,
   * if the team id is the user team id.
   */
  public void applyUserTeamValues(TeamInfo teamInfo)
  {
    if (teamInfo.getID().equals(userTeamID))
    {
      for (int i = 0; i < teamInfo.getPlayerInfo().length; i++)
      {
        PlayerInfo player = teamInfo.getPlayerInfo()[i];
        player.setAttackStart(player.getAttackStart() + nextAttackModification);
        player.setDefenseStart(player.getDefenseStart() + nextDefenseModification);
        player.setSpeedStart(player.getSpeedStart() + nextSpeedModification);
        player.setStamina(player.getStamina() + nextStaminaModification);
      }
    }
  }
  
}
