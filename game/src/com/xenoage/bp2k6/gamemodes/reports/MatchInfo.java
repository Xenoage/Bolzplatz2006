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
import com.xenoage.bp2k6.match.TeamInfo;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.math.NormalDistribution;


/**
 * Class, that contains basic information
 * about a match: the IDs of the teams,
 * if a draw is allowed (else: overtime
 * and penalty shootout if needed),
 * the list of all goals,
 * and the score (90 min score, halftime score,
 * if needed overtime and penalty score).
 * 
 * @author awr
 */
public class MatchInfo
{
  private boolean played = false;
  private String teamHomeID;
  private String teamVisitorID;
  private boolean drawAllowed;
  private ArrayList<Goal> goals = new ArrayList<Goal>();
  
  
  /**
   * Constructor.
   */
  public MatchInfo(String teamHomeID, String teamVisitorID,
    boolean drawAllowed)
  {
    this.teamHomeID = teamHomeID;
    this.teamVisitorID = teamVisitorID;
    this.drawAllowed = drawAllowed;
  }
  
  
  /**
   * Constructor.
   * Creates a MatchInfo from the given XML element.
   */
  public MatchInfo(Node eMatchInfo)
  {
    //parse match element
    played = XMLReader.readAttributeValueBoolean(eMatchInfo, "played");
    teamHomeID = XMLReader.readAttributeValue(eMatchInfo, "home");
    teamVisitorID = XMLReader.readAttributeValue(eMatchInfo, "visitor");
    drawAllowed = XMLReader.readAttributeValueBoolean(eMatchInfo, "drawallowed");
    //list of all goals
    Node eGoals = XMLReader.getChildElement(eMatchInfo, "goals");
    if (eGoals != null)
    {
      List<Element> listGoals = XMLReader.elements(eGoals, "goal");
      for (int i = 0; i < listGoals.size(); i++)
      {
        Goal goal = new Goal(listGoals.get(i));
        goals.add(goal);
      }
    }
  }
  
  
  /**
   * Adds a new goal to the list.
   */
  public void addGoal(Goal goal)
  {
    goals.add(goal);
  }
  
  
  /**
   * Computes the results for this match.
   * If this match should not be computed
   * (for example because it was the match with
   * the user), the <code>alreadyComputedResult</code>
   * parameter can be used to report the result. 
   * 
   */
  public void computeResult(MatchInfo alreadyComputedResult)
  {
    played = true;
    //compute match or take the given result?
    if (alreadyComputedResult != null &&
      alreadyComputedResult.getTeamHomeID().equals(teamHomeID) &&
      alreadyComputedResult.getTeamVisitorID().equals(teamVisitorID))
    {
      goals = alreadyComputedResult.getGoals();
    }
    else
    {
      //compute the match
      TeamInfo teams[] = new TeamInfo[2];
      teams[0] = GameEngine.getMatchManager().getTeamInfo(teamHomeID);
      teams[1] = GameEngine.getMatchManager().getTeamInfo(teamVisitorID);
      
      //goals
      int goals[] = new int[2];

      
      for (int iTeam = 0; iTeam < 2; iTeam++)
      {
        goals[iTeam] = (int) (0.3f * (1.4f * teams[iTeam].getAverageAttack() +
          0.5f * teams[iTeam].getAverageSpeed() -
          teams[1 - iTeam].getAverageDefense() -
          0.5f * teams[1 - iTeam].getAverageStamina()));
        if (goals[iTeam] < 0) goals[iTeam] = 0; 
        goals[iTeam] = NormalDistribution.randomizeGoals(goals[iTeam]);
        for (int iGoals = 0; iGoals < goals[iTeam]; iGoals++)
        {
          addGoal(new Goal(iTeam, 0, 0, 0, 0));
        }
      }  
      
      //if draw, but not allowed: goal to random team
      if (goals[0] == goals[1] && !drawAllowed)
      {
        int team = MathTools.randomInt(2);
        goals[team]++;
        addGoal(new Goal(team, 0, 0, 0, 0));
      }
        
    }
    
  }
  
  
  /**
   * Gets the end score of team 0 and 1.
   */
  public int[] getScore()
  {
    int ret[] = new int[2];
    for (int i = 0; i < goals.size(); i++)
    {
      ret[goals.get(i).getTeam()]++;
    }
    return ret;
  }
  
  
  /**
   * Gets the halftime score (45 min) as
   * a string in the format "0:0".
   */
  public String getScore45()
  {
    int homeGoals = 0;
    int visitorGoals = 0;
    for (int i = 0; i < goals.size(); i++)
    {
      Goal goal = goals.get(i);
      if (goal.getTimePhase() == Goal.TIMEPHASE_FIRSTHALF)
        if (goal.getTeam() == 0)
          homeGoals++;
        else
          visitorGoals++;
    }
    return homeGoals + ":" + visitorGoals;
  }
  
  
  /**
   * Gets the 90 min score as
   * a string in the format "0:0".
   */
  public String getScore90()
  {
    int homeGoals = 0;
    int visitorGoals = 0;
    for (int i = 0; i < goals.size(); i++)
    {
      Goal goal = goals.get(i);
      int timePhase = goal.getTimePhase();
      if (timePhase == Goal.TIMEPHASE_FIRSTHALF ||
        timePhase == Goal.TIMEPHASE_SECONDHALF)
        if (goal.getTeam() == 0)
          homeGoals++;
        else
          visitorGoals++;
    }
    return homeGoals + ":" + visitorGoals;
  }
  
  
  /**
   * Gets the ID of the winner team of this match.
   * If the match is drawn, the visitor team is returned.
   */
  public String getWinnerTeamID()
  {
    int score[] = getScore();
    if (score[0] > score[1])
      return teamHomeID;
    else
      return teamVisitorID;
  }
  
  
  /**
   * Gets the ID of the loser team of this match.
   * If the match is drawn, the home team is returned.
   */
  public String getLoserTeamID()
  {
    int score[] = getScore();
    if (score[0] > score[1])
      return teamVisitorID;
    else
      return teamHomeID;
  }
  
  
  /**
   * Gets the ID of the opponent team of the given team.
   * If the given team does not take part in this match,
   * the visitor team is returned.
   */
  public String getOpponentID(String teamID)
  {
    if (teamHomeID.equals(teamID))
      return teamVisitorID;
    else
      return teamHomeID;
  }
  
  
  /**
   * Save this match within the given
   * XML element. A new child element "match"
   * with all information about the match
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create match element
    Node eMatch = XMLWriter.addElement("match", parentElement);
    XMLWriter.addAttribute(eMatch, "played", String.valueOf(played));
    XMLWriter.addAttribute(eMatch, "home", teamHomeID);
    XMLWriter.addAttribute(eMatch, "visitor", teamVisitorID);
    XMLWriter.addAttribute(eMatch, "drawallowed", String.valueOf(drawAllowed));
    //list of all goals
    Node eGoals = XMLWriter.addElement("goals", eMatch);
    for (int i = 0; i < goals.size(); i++)
    {
      Goal goal = goals.get(i);
      goal.saveToXML(eGoals);
    }
  }
  

  //Getters...
  
  public boolean isPlayed()
  {
    return played;
  }
  
  public boolean isDrawAllowed()
  {
    return drawAllowed;
  }
  
  public ArrayList<Goal> getGoals()
  {
    return goals;
  }
  
  public String getTeamHomeID()
  {
    return teamHomeID;
  }
  
  public String getTeamVisitorID()
  {
    return teamVisitorID;
  }

  
}
