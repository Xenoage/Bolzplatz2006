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

import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;

import org.w3c.dom.Node;


/**
 * Entry for a table
 * (contains one team, the number of won, drawn
 * and lost games, the number of for- and against-goals
 * and the points)
 */
public class TableEntry
{
  private String teamID;
  private int gamesWon;
  private int gamesDrawn;
  private int gamesLost;
  private int goalsFor;
  private int goalsAgainst;
  private int points;
  
  
  /**
   * Constructor.
   */
  public TableEntry(String teamID)
  {
    this.teamID = teamID;
  }
  
  
  /**
   * Constructor.
   * Creates a copy of the given TableEntry.
   */
  public TableEntry(TableEntry tableEntry)
  {
    this.teamID = tableEntry.getTeamID();
    this.gamesWon = tableEntry.getGamesWon();
    this.gamesDrawn = tableEntry.getGamesDrawn();
    this.gamesLost = tableEntry.getGamesLost();
    this.goalsFor = tableEntry.getGoalsFor();
    this.goalsAgainst = tableEntry.getGoalsAgainst();
    this.points = tableEntry.getPoints();
  }
  
  
  /**
   * Constructor.
   * Creates a TableEntry from the given XML element.
   */
  public TableEntry(Node eTableEntry)
  {
    //parse entry element
    teamID = XMLReader.readAttributeValue(eTableEntry, "teamid");
    gamesWon = XMLReader.readAttributeValueInt(eTableEntry, "gameswon");
    gamesDrawn = XMLReader.readAttributeValueInt(eTableEntry, "gamesdrawn");
    gamesLost = XMLReader.readAttributeValueInt(eTableEntry, "gameslost");
    goalsFor = XMLReader.readAttributeValueInt(eTableEntry, "goalsfor");
    goalsAgainst = XMLReader.readAttributeValueInt(eTableEntry, "goalsagainst");
    points = XMLReader.readAttributeValueInt(eTableEntry, "points");
  }
  
  
  /**
   * Gets the goal difference
   * (= goalsFor - goalsAgainst)
   */
  public int getGoalDifference()
  {
    return goalsFor - goalsAgainst;
  }
  
  
  /**
   * Gets the number of played games.
   * (= gamesWon + gamesDrawn + gamesLost)
   */
  public int getGamesCount()
  {
    return gamesWon + gamesDrawn + gamesLost;
  }
  
  
  /**
   * Returns <code>true</code>, if the given team
   * has a better ranking than this team.
   * First the higher number of points is important.
   * If the two teams have the same points,
   * the better goal difference is important.
   * If the two teams have still the same ranking,
   * the higher number of for-goals is important.
   * If the two teams have still the same ranking,
   * a user-controlled team is always ranked higher,
   * otherwise it is decided at random.
   * @param userTeamID  id of the user controlled team,
   *                    or <code>null</code>
   */
  public boolean hasBetterRankingThan(
    TableEntry otherTeam, String userTeamID)
  {
    if (points > otherTeam.getPoints())
      return true;
    else if (points < otherTeam.getPoints())
      return false;
    else //points == otherTeam.getPoints()
    {
      if (getGoalDifference() > otherTeam.getGoalDifference())
        return true;
      else if (getGoalDifference() < otherTeam.getGoalDifference())
        return false;
      else //getGoalDifference() == otherTeam.getGoalDifference()
      {
        if (goalsFor > otherTeam.getGoalsFor())
          return true;
        else if (goalsFor < otherTeam.getGoalsFor())
          return false;
        else //goalsFor == otherTeam.getGoalsFor()
        {
          if (userTeamID != null && teamID.equals(userTeamID))
            return true;
          else if (userTeamID != null && otherTeam.getTeamID().equals(userTeamID))
            return false;
          else //no user team
          {
            return (Math.random() < 0.5);
          }
        }
      }
    }
  }
  
  
  /**
   * Save this table entry within the given
   * XML element. A new child element "entry"
   * with all information about the table entry
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create entry element
    Node eEntry = XMLWriter.addElement("entry", parentElement);
    XMLWriter.addAttribute(eEntry, "teamid", String.valueOf(teamID));
    XMLWriter.addAttribute(eEntry, "gameswon", String.valueOf(gamesWon));
    XMLWriter.addAttribute(eEntry, "gamesdrawn", String.valueOf(gamesDrawn));
    XMLWriter.addAttribute(eEntry, "gameslost", String.valueOf(gamesLost));
    XMLWriter.addAttribute(eEntry, "goalsfor", String.valueOf(goalsFor));
    XMLWriter.addAttribute(eEntry, "goalsagainst", String.valueOf(goalsAgainst));
    XMLWriter.addAttribute(eEntry, "points", String.valueOf(points));
  }
  
  
  //Getters and setters...
  
  public int getGamesDrawn()
  {
    return gamesDrawn;
  }
  
  public void setGamesDrawn(int gamesDrawn)
  {
    this.gamesDrawn = gamesDrawn;
  }
  
  public int getGamesLost()
  {
    return gamesLost;
  }
  
  public void setGamesLost(int gamesLost)
  {
    this.gamesLost = gamesLost;
  }
  
  public int getGamesWon()
  {
    return gamesWon;
  }
  
  public void setGamesWon(int gamesWon)
  {
    this.gamesWon = gamesWon;
  }
  
  public int getGoalsAgainst()
  {
    return goalsAgainst;
  }
  
  public void setGoalsAgainst(int goalsAgainst)
  {
    this.goalsAgainst = goalsAgainst;
  }
  
  public int getGoalsFor()
  {
    return goalsFor;
  }
  
  public void setGoalsFor(int goalsFor)
  {
    this.goalsFor = goalsFor;
  }
  
  public int getPoints()
  {
    return points;
  }
  
  public void setPoints(int points)
  {
    this.points = points;
  }
  
  public String getTeamID()
  {
    return teamID;
  }
  
  public void setTeamID(String teamID)
  {
    this.teamID = teamID;
  }
  
    
  
}
