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

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * Class for a season in career mode.
 * 
 * @author Andreas Wenger
 */
public class Season
{
  //the career this season belongs to
  private Career career;
  //the year of the season
  private int year;
  //the three leagues [0]: 1. league, ..., [2]: 3. league
  private League leagues[] = new League[3];
  //the ÜFA Allstars League
  private AllstarsLeague allstarsLeague;
  
  //types of leagues
  public static final int LEAGUE_ALLSTARS = 0;
  public static final int LEAGUE_1 = 1;
  public static final int LEAGUE_2 = 2;
  public static final int LEAGUE_3 = 3;
  
  //type of the current matchday:
  //league, allstars, winter break
  //or end of season?
  public static final int MATCHDAY_ALLSTARS = 0;
  public static final int MATCHDAY_LEAGUE = 1;
  public static final int MATCHDAY_WINTERBREAK = 2;
  public static final int MATCHDAY_ENDOFSEASON = 3;
  
  //number of league matchdays
  private static final int LEAGUE_MATCHDAYS = 18;
  
  //the list of matchdays
  private static int matchDayTypes[] = new int[] {
    MATCHDAY_LEAGUE,      //01. league matchday
    MATCHDAY_LEAGUE,      //02. league matchday
    MATCHDAY_LEAGUE,      //03. league matchday
    MATCHDAY_ALLSTARS,    //01. allstars matchday
    MATCHDAY_LEAGUE,      //04. league matchday
    MATCHDAY_LEAGUE,      //05. league matchday
    MATCHDAY_ALLSTARS,    //02. allstars matchday
    MATCHDAY_LEAGUE,      //06. league matchday
    MATCHDAY_LEAGUE,      //07. league matchday
    MATCHDAY_ALLSTARS,    //03. allstars matchday
    MATCHDAY_LEAGUE,      //08. league matchday
    MATCHDAY_LEAGUE,      //09. league matchday
    MATCHDAY_WINTERBREAK, //    winter break
    MATCHDAY_LEAGUE,      //10. league matchday
    MATCHDAY_ALLSTARS,    //04. allstars matchday
    MATCHDAY_LEAGUE,      //11. league matchday
    MATCHDAY_ALLSTARS,    //05. allstars matchday
    MATCHDAY_LEAGUE,      //12. league matchday
    MATCHDAY_ALLSTARS,    //06. allstars matchday
    MATCHDAY_LEAGUE,      //13. league matchday
    MATCHDAY_LEAGUE,      //14. league matchday
    MATCHDAY_ALLSTARS,    //    allstars quarter finals
    MATCHDAY_LEAGUE,      //15. league matchday
    MATCHDAY_LEAGUE,      //16. league matchday
    MATCHDAY_ALLSTARS,    //    allstars semi finals
    MATCHDAY_LEAGUE,      //17. league matchday
    MATCHDAY_ALLSTARS,    //    allstars 3rd-place-match
    MATCHDAY_ALLSTARS,    //    allstars final
    MATCHDAY_LEAGUE,      //18. league matchday
    MATCHDAY_ENDOFSEASON  //    end of season
  };
  private int currentMatchDay = 1;
  
  
  /**
   * Creates a new season.
   * @param career                the career this season belongs to
   * @param year                  the year of the season (e.g. 2006)
   * @param league1Teams          the 10 team ids of the 1. league
   * @param league2Teams          the 10 team ids of the 2. league
   * @param league3Teams          the 10 team ids of the 3. league
   * @param allstarsLeagueTeams   the 16 team ids of the ÜFA Allstars League
   */
  public Season(
    Career career, int year,
    ArrayList<String> league1Teams,
    ArrayList<String> league2Teams,
    ArrayList<String> league3Teams,
    ArrayList<String> allstarsLeagueTeams)
  {
    this.year = year;
    //create 1. league
    leagues[0] = new League(this, League.LEAGUE_1, league1Teams);
    //create 2. league
    leagues[1] = new League(this, League.LEAGUE_2, league2Teams);
    //create 3. league
    leagues[2] = new League(this, League.LEAGUE_3, league3Teams);
    //create ÜFA Allstars League
    allstarsLeague = new AllstarsLeague(this, allstarsLeagueTeams);
  }
  
  
  /**
   * Constructor.
   * Creates a Season from the given XML element.
   */
  public Season(Node eSeason)
  {
    //parse season element
    year = XMLReader.readAttributeValueInt(eSeason, "year");
    currentMatchDay = XMLReader.readAttributeValueInt(
      eSeason, "currentmatchday");
    //parse the three leagues and the ÜFA Allstars League
    Node eLeagues = XMLReader.getChildElement(eSeason, "leagues");
    if (eLeagues != null)
    {
      List<Element> listLeagues = XMLReader.elements(eLeagues, "league");
      for (int i = 0; i < listLeagues.size(); i++)
      {
        Node eLeague = listLeagues.get(i);
        int type = XMLReader.readAttributeValueInt(eLeague, "type");
        if (type == League.LEAGUE_1)
          leagues[0] = new League(eLeague);
        else if (type == League.LEAGUE_2)
          leagues[1] = new League(eLeague);
        else if (type == League.LEAGUE_3)
          leagues[2] = new League(eLeague);
      }
      Node eAllstarsLeague = XMLReader.getChildElement(eLeagues, "allstarsleague");
      if (eAllstarsLeague != null)
        //TODO: allstarsLeague = new AllstarsLeague(eAllstarsLeague);
      if (leagues[0] == null || leagues[1] == null ||
        leagues[2] == null || allstarsLeague == null)
        GameEngine.fatalError(this,
          new Exception("Savegame: Season has not all 4 leagues!"));
    }
    else
    {
      GameEngine.fatalError(this,
        new Exception("Savegame: Season has no leagues element!"));
    }
  }
  
  
  /**
   * Computes the next season.
   * Call this method at the end of this season to get
   * the season for the next year with the following teams:
   * <ul>
   *   <li>Allstars League: 13 international teams and
   *       the best 3 league-1-teams</li>
   *   <li>League 1: The 7 best league-1-teams and the
   *       3 best league-2-teams</li>
   *   <li>League 2: The 3 worst league-1-teams, the
   *       4-7-placed league2-teams and the
   *       best 3 league-3-teams</li>
   *   <li>League 3: The 3 worst league-2-teams and
   *       the 7 worst league-3-teams</li>
   * </ul>
   */
  public Season computeNextSeason()
  {
    int lastMatchDay = LEAGUE_MATCHDAYS;
    
    //get final tables
    Table leagueTable[] = new Table[3];
    for (int i = 0; i < 3; i++)
      leagueTable[i] = leagues[i].getMatchDay(lastMatchDay).getTable();
    
    //allstars league
    ArrayList<String> allstars = new ArrayList<String>();
    Career.getTeamIDs(null, null, null, allstars);
    if (allstars.size() < 13)
    {
      GameEngine.fatalError(this, new Exception(
        "There must be at least 13 international teams!"));
    }
    else
    {
      while (allstars.size() > 13)
      {
        allstars.remove((int) (Math.random() * allstars.size()));
      }
    }
    for (int i = 1; i <= 3; i++)
      allstars.add(leagues[0].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    
    //1. league
    ArrayList<String> teams1 = new ArrayList<String>();
    for (int i = 1; i <= 7; i++)
      teams1.add(leagues[0].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    for (int i = 1; i <= 3; i++)
      teams1.add(leagues[1].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    
    //2. league
    ArrayList<String> teams2 = new ArrayList<String>();
    for (int i = 8; i <= 10; i++)
      teams2.add(leagues[0].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    for (int i = 4; i <= 7; i++)
      teams2.add(leagues[1].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    for (int i = 1; i <= 3; i++)
      teams2.add(leagues[2].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    
    //3. league
    ArrayList<String> teams3 = new ArrayList<String>();
    for (int i = 8; i <= 10; i++)
      teams3.add(leagues[1].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    for (int i = 4; i <= 10; i++)
      teams3.add(leagues[2].getMatchDay(
        lastMatchDay).getTable().getTableEntry(i).getTeamID());
    
    //create season
    return new Season(career, year + 1, teams1, teams2, teams3, allstars);
  }
  
  
  /**
   * Computes the results for all matches of the
   * current matchday in all leagues and
   * switches to the next matchday.
   * If a certain match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result. 
   */
  public void computeResults(MatchInfo alreadyComputedResult, String userTeamID)
  {
    int currentMatchDayType = getCurrentMatchDayType();
    if (currentMatchDayType == MATCHDAY_ALLSTARS)
    {
      allstarsLeague.computeResults(alreadyComputedResult, userTeamID);
    }
    else if (currentMatchDayType == MATCHDAY_LEAGUE)
    {
      for (int i = 0; i < 3; i++)
        leagues[i].computeResults(alreadyComputedResult, userTeamID);
    }
    currentMatchDay++;
  }
  
  
  /**
   * Gets the allstars league of this season.
   */
  public AllstarsLeague getAllstarsLeague()
  {
    return allstarsLeague;
  }
  
  
  /**
   * Gets the number of the current allstars league matchday.
   */
  public int getCurrentAllstarsMatchDayNumber()
  {
    return allstarsLeague.getCurrentMatchDayNumber();
  }
  
  
  /**
   * Gets the match with the given team at
   * the current matchday, or <code>null</code>
   * if this match was not found.
   */
  public MatchInfo getCurrentMatch(String teamID)
  {
    MatchInfo ret = null;
    int currentMatchDayType = getCurrentMatchDayType();
    if (currentMatchDayType == MATCHDAY_ALLSTARS)
    {
      return allstarsLeague.getCurrentMatch(teamID);
    }
    else if (currentMatchDayType == MATCHDAY_LEAGUE)
    {
      for (int i = 0; i < 3; i++)
        if (ret == null)
          ret = leagues[i].getCurrentMatch(teamID);
    }
    return ret;
  }
  
  
  /**
   * Gets the number of the current league matchday.
   */
  public int getCurrentLeagueMatchDayNumber()
  {
    return leagues[0].getCurrentMatchDayNumber();
  }
  
  
  /**
   * Gets the type of the current matchday
   * (league or allstars league).
   */
  public int getCurrentMatchDayType()
  {
    return matchDayTypes[currentMatchDay - 1];
  }
  
  
  /**
   * Gets the type of the given matchday
   * (league or allstars league).
   */
  public static int getMatchDayType(int matchDayNumber)
  {
    return matchDayTypes[matchDayNumber - 1];
  }
  
  
  /**
   * Gets the league of the given team
   * (LEAGUE_-constant), or -1 if not found.
   * If the team is in the allstars league and in
   * the league1, LEAGUE_ALLSTARS is returned.
   */
  public int getLeagueOfTeam(String teamID)
  {
    if (allstarsLeague.hasTeam(teamID))
      return LEAGUE_ALLSTARS;
    else if (leagues[0].hasTeam(teamID))
      return LEAGUE_1;
    else if (leagues[1].hasTeam(teamID))
      return LEAGUE_2;
    else if (leagues[2].hasTeam(teamID))
      return LEAGUE_3;
    else
      return -1;
  }
  
  
  /**
   * Gets the given league (LEAGUE_1, LEAGUE_2 or LEAGUE_3).
   */
  public League getLeague(int league)
  {
    return leagues[league - 1];
  }
  
  
  /**
   * Gets the given matchday of the given league.
   * If it does not exist, <code>null</code> is returned.
   * <code>group</code> is needed for Allstars League group matchdays.
   * The matchday of the given group (0, 1, 2 or 3) is returned.
   */
  public MatchDay getMatchDay(int league, int matchDay, int group)
  {
    if (league == LEAGUE_ALLSTARS)
      return allstarsLeague.getMatchDay(matchDay, group);
    else if (league == LEAGUE_1)
      return leagues[0].getMatchDay(matchDay);
    else if (league == LEAGUE_2)
      return leagues[1].getMatchDay(matchDay);
    else if (league == LEAGUE_3)
      return leagues[2].getMatchDay(matchDay);
    else
      return null;
  }
  
  
  /**
   * Save this season within the given
   * XML element. A new child element "season"
   * with all information about the season
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create season element
    Node eLeague = XMLWriter.addElement("season", parentElement);
    XMLWriter.addAttribute(eLeague, "currentmatchday",
      String.valueOf(currentMatchDay));
    //year of the season
    XMLWriter.addAttribute(eLeague, "year", String.valueOf(year));
    //save the three leagues and the ÜFA Allstars League
    Node eLeagues = XMLWriter.addElement("leagues", eLeague);
    leagues[0].saveToXML(eLeagues);
    leagues[1].saveToXML(eLeagues);
    leagues[2].saveToXML(eLeagues);
    allstarsLeague.saveToXML(eLeagues);
  }


  /**
   * Gets the number of the current season matchday
   * (may be a league or a allstars matchday).
   */
  public int getCurrentMatchDay()
  {
    return currentMatchDay;
  }
  
}