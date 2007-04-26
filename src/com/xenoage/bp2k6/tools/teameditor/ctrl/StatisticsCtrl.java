/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.ctrl;

import java.io.File;
import java.util.ArrayList;

import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.tools.teameditor.gui.*;
import com.xenoage.bp2k6.util.*;


/**
 * This class is the controller of the
 * statistics page.
 * 
 * @author Andreas Wenger
 */
public class StatisticsCtrl
{
  
  StatisticsPanel panel;
  boolean showCareerTeams = true;
  boolean showWorldCupTeams = true;
  
  public class TeamRanking
  {
    public String id;
    public float attack;
    public float defense;
    public float speed;
    public float stamina;
    public float total;
  }
  private ArrayList<TeamRanking> teams;
  

  /**
   * Constructor.
   */
  public StatisticsCtrl(StatisticsPanel panel)
  {
    this.panel = panel;
    panel.setController(this);
    teams = new ArrayList<TeamRanking>();
  }
  
  
  /**
   * Call this method when the player data page
   * is opened.
   */
  public void pageActivated()
  {
    //parse all teams to the TeamRanking ArrayList
    loadAllTeams();
    //sort by total points and display teams
    sortByTotal();
  }
  
  
  /**
   * Call this method when the player data page
   * is closed.
   */
  public void pageClosed()
  {
    
  }
  
  
  /**
   * Parses all teams to the TeamRanking ArrayList
   */
  public void loadAllTeams()
  {
    teams.clear();
    String[] teamFiles = new File("data/teams/").list(FileUtils.getXMLFilter());
    for (int i = 0; i < teamFiles.length; i++)
    {
      TeamInfo teamInfo = new TeamInfo(
        teamFiles[i].substring(0, teamFiles[i].length() - 4));
      TeamRanking teamRanking = new TeamRanking();
      teamRanking.id = new String(teamInfo.getID());
      teamRanking.attack = teamInfo.getAverageAttack();
      teamRanking.defense = teamInfo.getAverageDefense();
      teamRanking.speed = teamInfo.getAverageSpeed();
      teamRanking.stamina = teamInfo.getAverageStamina();
      teamRanking.total = teamInfo.getAverageAll();
      teams.add(teamRanking);
    }
  }
  
  
  /**
   * Sort teams list by total points.
   */
  public void sortByTotal()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).total < teams.get(i2 + 1).total)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * Sort teams list alphabetically by ID.
   */
  public void sortByID()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).id.compareTo(teams.get(i2 + 1).id) > 0)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * Sort teams list by attack points.
   */
  public void sortByAttack()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).attack < teams.get(i2 + 1).attack)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * Sort teams list by defense points.
   */
  public void sortByDefense()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).defense < teams.get(i2 + 1).defense)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * Sort teams list by speed points.
   */
  public void sortBySpeed()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).speed < teams.get(i2 + 1).speed)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * Sort teams list by stamina points.
   */
  public void sortByStamina()
  {
    TeamRanking temp;
    for(int i = 0; i < teams.size() - 1; i++)
    {
      for (int i2 = 0; i2 < teams.size() - i - 1; i2++)
      {
        if(teams.get(i2).stamina < teams.get(i2 + 1).stamina)
        {
           temp = teams.remove(i2 + 1);
           teams.add(i2, temp);
        }
      }
    }
    updateList();
  }
  
  
  /**
   * This method is called when the user wants to filter
   * the shown teams.
   */
  public void filterTeams(boolean showCareerTeams, boolean showWorldCupTeams)
  {
    this.showCareerTeams = showCareerTeams;
    this.showWorldCupTeams = showWorldCupTeams;
    updateList();
  }
  
  
  /**
   * Updates the teams list on the panel.
   */
  public void updateList()
  {
    //filter teams
    ArrayList<TeamRanking> filteredTeams = new ArrayList<TeamRanking>(teams);
    if (!showCareerTeams)
    {
      ArrayList<String> careerTeams = Career.getTeamIDs();
      for (int i = 0; i < careerTeams.size(); i++)
      {
        remove(filteredTeams, careerTeams.get(i));
      }
    }
    if (!showWorldCupTeams)
    {
      ArrayList<String> worldCupTeams = WorldCup.getTeamIDs(false);
      for (int i = 0; i < worldCupTeams.size(); i++)
      {
        remove(filteredTeams, worldCupTeams.get(i));
      }
    }
    panel.updateList(filteredTeams);
  }
  
  
  /**
   * If the TeamRanking with the given ID is found in the given ArrayList,
   * it is removed and returned.
   */
  private TeamRanking remove(ArrayList<TeamRanking> array, String teamID)
  {
    for (int i = 0; i < array.size(); i++)
      if (array.get(i).id.equals(teamID))
        return array.remove(i);
    return null;
  }
  
}
  