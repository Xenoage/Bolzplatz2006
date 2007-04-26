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

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;


/**
 * Class for a league for one season.
 * This can be the 1., 2. or 3. league
 * or the group round of a World Cup or
 * the ÜFA Allstars League.
 * It contains a list of all teams, and a list
 * of all matchdays.
 * 
 * @author Andreas Wenger
 */
public class League
{
  
  //type of the league
  public static final int LEAGUE_ALLSTARS = 0;
  public static final int LEAGUE_1 = 1;
  public static final int LEAGUE_2 = 2;
  public static final int LEAGUE_3 = 3;
  public static final int LEAGUE_WORLDCUP = 4;
  private int type;
  
  //list of team ids for this league
  private ArrayList<String> teamIDs;
  
  //the number of the current (unplayed) matchday, 1-based
  private int currentMatchDayNumber = 1;
  
  //list of all matchdays
  private ArrayList<MatchDay> matchDays = new ArrayList<MatchDay>();
  
  //empty table, only with the names of the teams
  Table emptyTable;
 
  
  /**
   * Constructor.
   * Creates a new season with the given teams and
   * allots the matches.
   * @param season    the season this league belongs to.
   * @param type      the ID of the league, use <code>LEAGUE</code>-constants.
   * @param teamIDs   the list of IDs of all teams in this league.
   */
  public League(Season season, int type, ArrayList<String> teamIDs)
  {
    this.type = type;
    this.teamIDs = teamIDs;
    int teamsCount = teamIDs.size();
    
    if ((type == LEAGUE_ALLSTARS || type == LEAGUE_WORLDCUP) &&
      teamsCount != 4)
    {
      Main.fatalError(this, new Exception(
        "The ÜFA Allstars League or the World Cup must " +
        "have 4 teams in each group!"));
    }
    else if (!(type == LEAGUE_ALLSTARS || type == LEAGUE_WORLDCUP) &&
      teamsCount != 10)
    {
      Main.fatalError(this, new Exception(
        "The 1., 2. or 3. league must have 10 teams!"));
    }
    
    //shuffle teams (not in World Cup)
    ArrayList<String> teamIDsShuffled = new ArrayList<String>(teamIDs);
    if (type != LEAGUE_WORLDCUP)
    {
      for (int i = 0; i < teamsCount * 2; i++)
      {
        //remove a team from somewhere
        String teamID = teamIDsShuffled.remove(
          (int) (Math.random() * teamsCount));
        //add it to the end
        teamIDsShuffled.add(teamID);
      }
    }
    
    //create matchdays
    int matchDaysTable[][][];
    if (teamsCount == 4)
      matchDaysTable = matchDays4Teams;
    else
      matchDaysTable = matchDays10Teams;
    
    //first round and second round
    for (int iRound = 0; iRound < 2; iRound++)
    {
      //all matchdays for this round
      for (int iMatchDay = 0; iMatchDay < teamsCount - 1; iMatchDay++)
      {
        //collect matches for this matchday
        ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
        for (int iMatch = 0; iMatch < teamsCount / 2; iMatch++)
        {
          matches.add(new MatchInfo(
            teamIDsShuffled.get(
              matchDaysTable[iMatchDay][iMatch][iRound == 0 ? 0 : 1]), //home
            teamIDsShuffled.get(
              matchDaysTable[iMatchDay][iMatch][iRound == 0 ? 1 : 0]), //visitor
            true //draw is allowed
            ));
        }
        
        //create matchday
        matchDays.add(new MatchDay(
          iMatchDay + 1 + iRound * (teamsCount - 1), //number of the matchday
          matches, //list of matches
          false));
      }
    }
    //create empty table (for start of season)
    emptyTable = new Table(teamIDs);
  }
  
  
  /**
   * Constructor.
   * Creates a League from the given XML element.
   */
  public League(Node eLeague)
  {
    //parse league element
    type = XMLReader.readAttributeValueInt(eLeague, "type");
    currentMatchDayNumber = XMLReader.readAttributeValueInt(eLeague, "currentmatchday");
    //list of all teams
    Node eTeams = XMLReader.getChildElement(eLeague, "teams");
    if (eTeams != null)
    {
      teamIDs = new ArrayList<String>();
      List<Element> listTeams = XMLReader.elements(eTeams, "team");
      for (int i = 0; i < listTeams.size(); i++)
      {
        Node eTeam = listTeams.get(i);
        teamIDs.add(XMLReader.readAttributeValue(eTeam, "id"));
      }
    }
    //list of all matchdays
    Node eMatchDays = XMLReader.getChildElement(eLeague, "matchdays");
    if (eMatchDays != null)
    {
      List<Element> listMatchDays = XMLReader.elements(eMatchDays, "matchday");
      for (int i = 0; i < listMatchDays.size(); i++)
      {
        MatchDay matchDay = new MatchDay(listMatchDays.get(i));
        matchDays.add(matchDay);
      }
    }
    //create empty table (for start of season)
    emptyTable = new Table(teamIDs);
  }
  
  
  /**
   * Computes the results for all matches of the
   * current matchday in this league and
   * switches to the next matchday.
   * If a certain match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result. 
   */
  public void computeResults(MatchInfo alreadyComputedResult, String userTeamID)
  {
    MatchDay matchDay = matchDays.get(currentMatchDayNumber - 1);
    Table oldTable = null;
    if (currentMatchDayNumber == 1)
      oldTable = getEmptyTable();
    else
      oldTable = getMatchDay(currentMatchDayNumber - 1).getTable();
    matchDay.computeResults(alreadyComputedResult, oldTable, userTeamID);
    currentMatchDayNumber++;
  }
  
  
  /**
   * Gets the match with the given team at
   * the current matchday, or <code>null</code>
   * if this match was not found.
   */
  public MatchInfo getCurrentMatch(String teamID)
  {
    MatchDay matchDay = matchDays.get(currentMatchDayNumber - 1);
    return matchDay.getMatch(teamID);
  }
  
  
  /**
   * Gets the number of the current (unplayed) matchday.
   * //If the season is completely, the last matchday is
   * //returned.
   */
  public int getCurrentMatchDayNumber()
  {
    //if (currentMatchDayNumber > matchDays.size())
    //  return matchDays.size();
    //else
      return currentMatchDayNumber;
  }
  
  
  /**
   * Gets the given matchday (beginning at 1).
   * If it does not exist, <code>null</code> is returned.
   */
  public MatchDay getMatchDay(int matchDay)
  {
    if (matchDay > 0 && matchDay < matchDays.size() + 1)
      return matchDays.get(matchDay - 1);
    else
      return null;
  }
  
  
  /**
   * Gets the last matchday.
   * If it does not exist, <code>null</code> is returned.
   */
  public MatchDay getLastMatchDay()
  {
    return matchDays.get(matchDays.size() - 1);
  }
  
  
  /**
   * Gets the empty table (only with
   * the names of the teams).
   */
  public Table getEmptyTable()
  {
    return emptyTable;
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
   * Save this league within the given
   * XML element. A new child element "league"
   * with all information about the league
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create league element
    Node eLeague = XMLWriter.addElement("league", parentElement);
    XMLWriter.addAttribute(eLeague, "type", String.valueOf(type));
    XMLWriter.addAttribute(eLeague,
      "currentmatchday", String.valueOf(currentMatchDayNumber));
    //list of all teams
    Node eTeams = XMLWriter.addElement("teams", eLeague);
    for (int i = 0; i < teamIDs.size(); i++)
    {
      String teamID = teamIDs.get(i);
      Node eTeam = XMLWriter.addElement("team", eTeams);
      XMLWriter.addAttribute(eTeam, "id", teamID);
    }
    //list of all matchdays
    Node eMatchDays = XMLWriter.addElement("matchdays", eLeague);
    XMLWriter.addAttribute(eMatchDays,
      "currentmatchday", String.valueOf(currentMatchDayNumber));
    for (int i = 0; i < matchDays.size(); i++)
    {
      MatchDay matchDay = matchDays.get(i);
      matchDay.saveToXML(eMatchDays);
    }
  }
  
 
  
  //matchdays for a 10-team-league
  //(only first round, in the second round the
  //home/visitor teams are just flipped)
  private int matchDays10Teams[][][] = new int[][][]{
    { //1. matchday
      {0, 8},
      {6, 1},
      {2, 5},
      {4, 3},
      {9, 7}
    },
    { //2. matchday
      {5, 9},
      {1, 4},
      {3, 2},
      {8, 6},
      {7, 0}
    },
    { //3. matchday
      {9, 3},
      {2, 1},
      {7, 5},
      {0, 6},
      {4, 8}
    },
    { //4. matchday
      {1, 9},
      {6, 4},
      {5, 0},
      {3, 7},
      {8, 2}
    },
    { //5. matchday
      {5, 3},
      {0, 4},
      {2, 6},
      {7, 1},
      {9, 8}
    },
    { //6. matchday
      {3, 0},
      {1, 5},
      {6, 9},
      {8, 7},
      {4, 2}
    },
    { //7. matchday
      {9, 4},
      {5, 8},
      {7, 6},
      {3, 1},
      {0, 2}
    },
    { //8. matchday
      {4, 7},
      {6, 5},
      {2, 9},
      {1, 0},
      {8, 3}
    },
    //9. matchday
    {
      {5, 4},
      {1, 8},
      {0, 9},
      {7, 2},
      {3, 6}
    }
  };
  
  
  //matchdays for a 4-team-league
  //(only first round, in the second round (if needed) the
  //home/visitor teams are just flipped)
  //the matches are in the same order as the
  //group matches in the real World Cup 2006.
  private int matchDays4Teams[][][] = new int[][][]{
    { //1. matchday
      {0, 1},
      {2, 3}
    },
    { //2. matchday
      {0, 2},
      {3, 1}
    },
    { //3. matchday
      {3, 0},
      {1, 2}
    }
  };
  
}
