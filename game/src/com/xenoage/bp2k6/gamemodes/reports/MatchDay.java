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

import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;


/**
 * Class for a matchday. A matchday
 * contains a list of all matches and their
 * results and the table after these
 * matches, if needed.
 * 
 * @author Andreas Wenger
 */
public class MatchDay
{
  //the number of the matchday
  private int number;
  //matchday already played?
  private boolean matchDayPlayed = false;
  //list of all matches of this day
  private ArrayList<MatchInfo> matches = new ArrayList<MatchInfo>();
  //table after this matchday (may be null if this is the current matchday)
  private Table table;
  
  
  /**
   * Constructor.
   * @param number    Number of the matchday, beginning with 1.
   * @param matches   List of matches of this matchday.
   * @param played    <code>true</code>, if matchday was already played
   */
  public MatchDay(int number, ArrayList<MatchInfo> matches, boolean played)
  {
    this.number = number;
    this.matches = matches;
    this.matchDayPlayed = played;
  }
  
  
  /**
   * Constructor.
   * Creates a MatchDay from the given XML element.
   */
  public MatchDay(Node eMatchDay)
  {
    //parse matchday element
    number = XMLReader.readAttributeValueInt(eMatchDay, "number");
    matchDayPlayed = XMLReader.readAttributeValueBoolean(eMatchDay, "matchdayplayed");
    //list of all matches
    Node eMatches = XMLReader.getChildElement(eMatchDay, "matches");
    if (eMatches != null)
    {
      List<Element> listMatches = XMLReader.elements(eMatches, "match");
      for (int i = 0; i < listMatches.size(); i++)
      {
        MatchInfo match = new MatchInfo(listMatches.get(i));
        matches.add(match);
      }
    }
    //table after the matchday, if available
    Node eTable = XMLReader.getChildElement(eMatchDay, "table");
    if (eTable != null)
    {
      table = new Table(eTable);
    }
  }
  
  
  /**
   * Computes the results for all matches of
   * this matchday and updates the table.
   * If a certain match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result. 
   * The <code>oldTable</code> parameter is the
   * table after the last matchday, or <code>null</code>
   * if no table is needed for this matchday
   * (for example because it is a Allstars League
   * final matchday).
   */
  public void computeResults(MatchInfo alreadyComputedResult,
    Table oldTable, String userTeamID)
  {
    //compute results
    for (int i = 0; i < matches.size(); i++)
    {
      MatchInfo match = matches.get(i);
      match.computeResult(alreadyComputedResult);
    }
    //updates the table
    if (oldTable != null)
    {
      table = new Table(oldTable); //copy the old table
      for (int i = 0; i < matches.size(); i++)
      {
        MatchInfo match = matches.get(i);
        int score[] = match.getScore();
        //home team
        TableEntry homeTeam = table.getTableEntry(match.getTeamHomeID());
        if (score[0] > score[1])
        {
          homeTeam.setGamesWon(homeTeam.getGamesWon() + 1);
          homeTeam.setPoints(homeTeam.getPoints() + 3);
        }
        else if (score[0] == score[1])
        {
          homeTeam.setGamesDrawn(homeTeam.getGamesDrawn() + 1);
          homeTeam.setPoints(homeTeam.getPoints() + 1);
        }
        else
        {
          homeTeam.setGamesLost(homeTeam.getGamesLost() + 1);
        }
        homeTeam.setGoalsFor(homeTeam.getGoalsFor() + score[0]);
        homeTeam.setGoalsAgainst(homeTeam.getGoalsAgainst() + score[1]);
        //visitor team
        TableEntry visitorTeam = table.getTableEntry(match.getTeamVisitorID());
        if (score[1] > score[0])
        {
          visitorTeam.setGamesWon(visitorTeam.getGamesWon() + 1);
          visitorTeam.setPoints(visitorTeam.getPoints() + 3);
        }
        else if (score[1] == score[0])
        {
          visitorTeam.setGamesDrawn(visitorTeam.getGamesDrawn() + 1);
          visitorTeam.setPoints(visitorTeam.getPoints() + 1);
        }
        else
        {
          visitorTeam.setGamesLost(visitorTeam.getGamesLost() + 1);
        }
        visitorTeam.setGoalsFor(visitorTeam.getGoalsFor() + score[1]);
        visitorTeam.setGoalsAgainst(visitorTeam.getGoalsAgainst() + score[0]);
      }
      table.sortByRanking(userTeamID);
    }
    matchDayPlayed = true;
  }
  
  
  /**
   * Gets the table after this matchday.
   * This may also be <code>null</code> if this is the
   * current matchday or no table was generated
   * after the matchday.
   */
  public Table getTable()
  {
    return table;
  }
  
  
  /**
   * Gets the number of the matchday.
   */
  public int getNumber()
  {
    return number;
  }
  
  
  /**
   * Gets the match with the given index
   * (beginning at 0).
   */
  public MatchInfo getMatch(int index)
  {
    return matches.get(index);
  }
  
  
  /**
   * Gets the number of matches of this matchday.
   */
  public int getMatchCount()
  {
    return matches.size();
  }
  
  
  /**
   * Gets the match with the given team,
   * or <code>null</code>
   * if this match was not found.
   */
  public MatchInfo getMatch(String teamID)
  {
    for (int i = 0; i < matches.size(); i++)
    {
      MatchInfo match = matches.get(i);
      if (match.getTeamHomeID().equals(teamID) ||
        match.getTeamVisitorID().equals(teamID))
        return match;
    }
    return null;
  }
  
  
  /**
   * Save this matchday within the given
   * XML element. A new child element "matchday"
   * with all information about the matchday
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create matchday element
    Node eMatchDay = XMLWriter.addElement("matchday", parentElement);
    //number of the matchday
    XMLWriter.addAttribute(eMatchDay, "number", String.valueOf(number));
    //matchday already played?
    XMLWriter.addAttribute(eMatchDay,
      "matchdayplayed", String.valueOf(matchDayPlayed));
    //list of all matches
    Node eMatches = XMLWriter.addElement("matches", eMatchDay);
    for (int i = 0; i < matches.size(); i++)
    {
      MatchInfo match = matches.get(i);
      match.saveToXML(eMatches);
    }
    //table after the matchday, if available
    if (table != null)
    {
      table.saveToXML(eMatchDay);
    }
  }


  /**
   * Returns <code>true</code> if this matchday was already
   * played, otherwise <code>false</code>.
   */
  public boolean isMatchDayPlayed()
  {
    return matchDayPlayed;
  }
  
}
