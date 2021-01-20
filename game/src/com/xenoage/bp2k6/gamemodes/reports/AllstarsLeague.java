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
package com.xenoage.bp2k6.gamemodes.reports;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;
import com.xenoage.bp2k6.util.language.Language;

/**
 * Class for the ÜFA Allstars League for one season.
 * It contains a list of all teams, a list
 * of all 4 groups and the 4 quarter finals,
 * 2 semi-finals, 1 final and 1 third-place match.
 * 
 * @author Andreas Wenger
 */
public class AllstarsLeague
{
  //the season this Allstars League belongs to
  //list of team ids for this Allstar League
  private ArrayList<String> teamIDs;
  //4 groups
  private static final int groupsCount = 4;
  private League groups[] = new League[groupsCount];
  //final matches
  public static final int FINAL_QUARTER1 = 0;
  public static final int FINAL_QUARTER2 = 1;
  public static final int FINAL_QUARTER3 = 2;
  public static final int FINAL_QUARTER4 = 3;
  public static final int FINAL_SEMI1 = 4;
  public static final int FINAL_SEMI2 = 5;
  public static final int FINAL_THIRDPLACE = 6;
  public static final int FINAL_FINAL = 7;
  private MatchInfo finalMatches[] = new MatchInfo[8];
  //matchdays
  public static final int MATCHDAY_GROUP1 = 1;
  public static final int MATCHDAY_GROUP2 = 2;
  public static final int MATCHDAY_GROUP3 = 3;
  public static final int MATCHDAY_GROUP4 = 4;
  public static final int MATCHDAY_GROUP5 = 5;
  public static final int MATCHDAY_GROUP6 = 6;
  public static final int MATCHDAY_QUARTER = 7;
  public static final int MATCHDAY_SEMI = 8;
  public static final int MATCHDAY_THIRDPLACE = 9;
  public static final int MATCHDAY_FINAL = 10;
  public static final int MATCHDAY_END = 11; //allstars league is over
  private int currentMatchDayNumber = MATCHDAY_GROUP1;
  
  
  
  
  /**
   * Constructor.
   * Creates a new Allstars League with the given teams and
   * allots the groups.
   * @param season    the season this Allstars League belongs to.
   * @param teamIDs   the list of IDs of all teams in this league.
   */
  public AllstarsLeague(Season season, ArrayList<String> teamIDs)
  {
    //NOTNEEDED this.season = season;
    this.teamIDs = teamIDs;
    int teamsCount = teamIDs.size();
    
    if (teamsCount != 16)
    {
      GameEngine.fatalError(this, new Exception(
        "The ÜFA Allstars League must have 16 teams!"));
    }
    
    //shuffle teams
    ArrayList<String> teamIDsShuffled = new ArrayList<String>(teamIDs);
    for (int i = 0; i < teamsCount * 2; i++)
    {
      //remove a team from somewhere
      String teamID = teamIDsShuffled.remove(
        (int) (Math.random() * teamsCount));
      //add it to the end
      teamIDsShuffled.add(teamID);
    }
    
    //create the groups
    for (int iGroup = 0; iGroup < groupsCount; iGroup++)
    {
      ArrayList<String> group = new ArrayList<String>();
      for (int i = 0; i < groupsCount; i++)
      {
        group.add(teamIDsShuffled.get(iGroup * groupsCount + i));
      }
      groups[iGroup] = new League(season, League.LEAGUE_ALLSTARS, group);
    }
  }
  
  
  /**
   * Constructor.
   * Creates an AllstarsLeague from the given XML element.
   */
  public AllstarsLeague(Node eAllstarsLeague)
  {
    //parse allstars league element
    currentMatchDayNumber = XMLReader.readAttributeValueInt(
      eAllstarsLeague, "currentmatchday");
    //list of all teams
    teamIDs = new ArrayList<String>();
    Node eTeams = XMLReader.getChildElement(eAllstarsLeague, "teams");
    if (eTeams != null)
    {
      List<Element> listTeams = XMLReader.elements(eTeams, "team");
      for (int i = 0; i < listTeams.size(); i++)
      {
        String teamID = XMLReader.readAttributeValue(
          listTeams.get(i), "id");
        teamIDs.add(teamID);
      }
    }
    else
    {
      GameEngine.fatalError(this,
        new Exception("AllstarsLeague has no teams list!"));
    }
    //load the groups
    Node eGroups = XMLReader.getChildElement(eAllstarsLeague, "groups");
    if (eGroups != null)
    {
      List<Element> listGroups = XMLReader.elements(eGroups, "league");
      if (listGroups.size() != groupsCount)
        GameEngine.fatalError(this,
          new Exception("AllstarsLeague has not " + groupsCount + " groups!"));
      for (int i = 0; i < groupsCount; i++)
      {
        groups[i] = new League(listGroups.get(i));
      }
    }
    else
    {
      GameEngine.fatalError(this,
        new Exception("AllstarsLeague has no groups element!"));
    }
    //load the final matches, if already played
    Node eFinals = XMLReader.getChildElement(eAllstarsLeague, "finals");
    if (eFinals != null)
    {
      List<Element> listFinals = XMLReader.elements(eFinals, "match");
      for (int i = 0; i < listFinals.size(); i++)
      {
        finalMatches[i] = new MatchInfo(listFinals.get(i));
      }
    }
  }
  
  
  /**
   * Computes the results for all matches of the
   * current matchday in this allstars league and
   * switches to the next matchday.
   * If a certain match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result.
   */
  public void computeResults(MatchInfo alreadyComputedResult, String userTeamID)
  {
    if (currentMatchDayNumber >= MATCHDAY_GROUP1 &&
      currentMatchDayNumber <= MATCHDAY_GROUP6)
    {
      //play group games
      for (int i = 0; i < groupsCount; i++)
        groups[i].computeResults(alreadyComputedResult, userTeamID);
      //if this was the last group game, compute the
      //quarter finals
      if (currentMatchDayNumber == MATCHDAY_GROUP6)
      {
        for (int i = 0; i < 4; i++)
        {
          //1. team of group a,b,c,d against 2. team of group b,c,d,a
          finalMatches[i] = new MatchInfo(
            groups[i].getMatchDay(6).getTable().getTableEntry(1).getTeamID(),
            groups[(i + 1) % 4].getMatchDay(6).getTable().getTableEntry(2).getTeamID(),
            false);
        }
      }
    }
    else if (currentMatchDayNumber == MATCHDAY_QUARTER)
    {
      //play quarter finals
      for (int i = 0; i < 4; i++)
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
    else if (currentMatchDayNumber == MATCHDAY_SEMI)
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
    else if (currentMatchDayNumber == MATCHDAY_THIRDPLACE)
    {
      //play 3rd place match
      finalMatches[FINAL_THIRDPLACE].computeResult(alreadyComputedResult);
    }
    else if (currentMatchDayNumber == MATCHDAY_FINAL)
    {
      //play final
      finalMatches[FINAL_FINAL].computeResult(alreadyComputedResult);
    }
    currentMatchDayNumber++;
  }
  
  
  /**
   * Gets the current match with the given team at
   * the current matchday, or <code>null</code>
   * if this match was not found.
   */
  public MatchInfo getCurrentMatch(String teamID)
  {
    if (currentMatchDayNumber >= MATCHDAY_GROUP1 &&
      currentMatchDayNumber <= MATCHDAY_GROUP6)
    {
      //group matchday
      for (int i = 0; i < groupsCount; i++)
      {
        MatchInfo match = groups[i].getCurrentMatch(teamID);
        if (match != null)
          return match;
      }
    }
    else if (currentMatchDayNumber == MATCHDAY_QUARTER)
    {
      //quarter finals
      for (int i = FINAL_QUARTER1; i <= FINAL_QUARTER4; i++)
      {
        if (finalMatches[i].getTeamHomeID().equals(teamID) ||
          finalMatches[i].getTeamVisitorID().equals(teamID))
          return finalMatches[i];
      }
    }
    else if (currentMatchDayNumber == MATCHDAY_SEMI)
    {
      //semi finals
      for (int i = FINAL_SEMI1; i <= FINAL_SEMI2; i++)
      {
        if (finalMatches[i].getTeamHomeID().equals(teamID) ||
          finalMatches[i].getTeamVisitorID().equals(teamID))
          return finalMatches[i];
      }
    }
    else if (currentMatchDayNumber == MATCHDAY_THIRDPLACE)
    {
      //3rd place match
      if (finalMatches[FINAL_THIRDPLACE].getTeamHomeID().equals(teamID) ||
        finalMatches[FINAL_THIRDPLACE].getTeamVisitorID().equals(teamID))
        return finalMatches[FINAL_THIRDPLACE];
    }
    else if (currentMatchDayNumber == MATCHDAY_FINAL)
    {
      //final
      if (finalMatches[FINAL_FINAL].getTeamHomeID().equals(teamID) ||
        finalMatches[FINAL_FINAL].getTeamVisitorID().equals(teamID))
        return finalMatches[FINAL_FINAL];
    }
    return null;
  }
  
  
  /**
   * Gets the number of the current matchday of this
   * Allstars League. If the returned value is
   * between 0 and 5, it is a group matchday,
   * otherwise it is a MATCHDAY_-constant matchday.
   */
  public int getCurrentMatchDayNumber()
  {
    return currentMatchDayNumber;
  }
  
  
  /**
   * Gets the empty table (only with
   * the names of the teams) of the given group.
   */
  public Table getEmptyTable(int group)
  {
    return groups[group].getEmptyTable();
  }
  
  
  /**
   * Gets the number of the group (0, 1, 2, 3)
   * of the team with the given ID, or
   * -1 if not found.
   */
  public int getGroup(String teamID)
  {
    for (int i = 0; i < groupsCount; i++)
      if (groups[i].hasTeam(teamID))
        return i;
    return -1;
  }
  
  
  /**
   * Gets the name of the given group (0, 1, 2, 3).
   */
  public String getGroupName(int group)
  {
    if (group == 0)
      return Language.get("groupa");
    else if (group == 1)
      return Language.get("groupb");
    else if (group == 2)
      return Language.get("groupc");
    else
      return Language.get("groupd");
  }
  
  
  /**
   * Gets the given matchday. Use the
   * MATCHDAY_-constants as parameter.
   * If it is a group matchday, also use
   * the parameter group (0, 1, 2 or 3).
   * If it does not exist, <code>null</code>
   * is returned.
   */
  public MatchDay getMatchDay(int number, int group)
  {
    if (number >= MATCHDAY_GROUP1 && number <= MATCHDAY_GROUP6)
      return groups[group].getMatchDay(number);
    else if (number == MATCHDAY_QUARTER)
    {
      //fill matchday info with matches
      ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
      matches.add(finalMatches[FINAL_QUARTER1]);
      matches.add(finalMatches[FINAL_QUARTER2]);
      matches.add(finalMatches[FINAL_QUARTER3]);
      matches.add(finalMatches[FINAL_QUARTER4]);
      return new MatchDay(MATCHDAY_QUARTER, matches,
        currentMatchDayNumber > MATCHDAY_QUARTER);
    }
    else if (number == MATCHDAY_SEMI)
    {
      //fill matchday info with matches
      ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
      matches.add(finalMatches[FINAL_SEMI1]);
      matches.add(finalMatches[FINAL_SEMI2]);
      return new MatchDay(MATCHDAY_SEMI, matches,
        currentMatchDayNumber > MATCHDAY_SEMI);
    }
    else if (number == MATCHDAY_THIRDPLACE)
    {
      //fill matchday info with matches
      ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
      matches.add(finalMatches[FINAL_THIRDPLACE]);
      return new MatchDay(MATCHDAY_THIRDPLACE, matches,
        currentMatchDayNumber > MATCHDAY_THIRDPLACE);
    }
    else if (number == MATCHDAY_FINAL)
    {
      //fill matchday info with matches
      ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
      matches.add(finalMatches[FINAL_FINAL]);
      return new MatchDay(MATCHDAY_FINAL, matches,
        currentMatchDayNumber > MATCHDAY_FINAL);
    }
    return null;
  }
  
  
  /**
   * Returns <code>true</code> if the given team
   * is in this league, otherwise <code>false</code>.
   */
  public boolean hasTeam(String teamID)
  {
    for (int i = 0; i < teamIDs.size(); i++)
      if (teamIDs.get(i).equals(teamID))
        return true;
    return false;
  }
  
  
  /**
   * Save this ÜFA Allstars League within the given
   * XML element. A new child element "allstarsleague"
   * with all information about the ÜFA Allstars League
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create allstarsleague element
    Node eAllstarsLeague = XMLWriter.addElement("allstarsleague", parentElement);
    //add current matchday
    XMLWriter.addAttribute(eAllstarsLeague, "currentmatchday",
      String.valueOf(currentMatchDayNumber));
    //list of all teams
    Node eTeams = XMLWriter.addElement("teams", eAllstarsLeague);
    for (int i = 0; i < teamIDs.size(); i++)
    {
      String teamID = teamIDs.get(i);
      Node eTeam = XMLWriter.addElement("team", eTeams);
      XMLWriter.addAttribute(eTeam, "id", teamID);
    }
    //save the groups
    Node eGroups = XMLWriter.addElement("groups", eAllstarsLeague);
    for (int i = 0; i < groupsCount; i++)
    {
      groups[i].saveToXML(eGroups);
    }
    //save the final matches, if already played
    Node eFinals = XMLWriter.addElement("finals", eAllstarsLeague);
    for (int i = 0; i < 8; i++)
    {
      if (finalMatches[i] != null)
        finalMatches[i].saveToXML(eFinals);
      else
        break;
    }
  }


  
  public static int getGroupsCount()
  {
    return groupsCount;
  }
  
  
  /**
   * Returns true, if the given team is qualified for
   * the finals. Call this method not before the
   * group matches are played.
   */
  public boolean isTeamQualifiedForFinals(String teamID)
  {
    if (currentMatchDayNumber >= MATCHDAY_QUARTER)
    {
      for (int i = 0; i < 4; i++)
      {
        if (groups[i].getMatchDay(6).getTable().getTableEntry(
          1).getTeamID().equals(teamID) ||
          groups[i].getMatchDay(6).getTable().getTableEntry(
          2).getTeamID().equals(teamID))
          return true;
      }
    }
    return false;
  }
  
  
}
