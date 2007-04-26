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
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.tools.teameditor.gui.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This class is the controller of the
 * career page.
 * 
 * @author Andreas Wenger
 */
public class CareerCtrl
{
  
  CareerPanel panel;
  
  private ArrayList<String> remainingTeams;
  private ArrayList<String> league1Teams;
  private ArrayList<String> league2Teams;
  private ArrayList<String> league3Teams;
  private ArrayList<String> internationalTeams;
  

  /**
   * Constructor.
   */
  public CareerCtrl(CareerPanel panel)
  {
    this.panel = panel;
    panel.setController(this);
    remainingTeams = new ArrayList<String>();
    league1Teams = new ArrayList<String>();
    league2Teams = new ArrayList<String>();
    league3Teams = new ArrayList<String>();
    internationalTeams = new ArrayList<String>();
  }
  
  
  /**
   * Call this method when the career page
   * is opened.
   */
  public void pageActivated()
  {
    //loads the list of remaining and custom world cup teams
    loadAllTeams();
    //updates the panel
    panel.updateTeamsList(remainingTeams, league1Teams,
      league2Teams, league3Teams, internationalTeams);
  }
  
  
  /**
   * Call this method when the player data page
   * is closed.
   */
  public void pageClosed()
  {
    saveToXMLFile();
  }
  
  
  /**
   * Loads all team IDs into the corresponding leagues.
   */
  public void loadAllTeams()
  {
    //remaining teams (load all teams first)
    remainingTeams.clear();
    String[] teamFiles = new File("data/teams/").list(FileUtils.getXMLFilter());
    ArrayUtils.sortStringArrayAlphabetically(teamFiles);
    for (int i = 0; i < teamFiles.length; i++)
    {
      remainingTeams.add(teamFiles[i].substring(0, teamFiles[i].length() - 4));
    }
    //load leagues
    league1Teams.clear();
    league2Teams.clear();
    league3Teams.clear();
    internationalTeams.clear();
    Career.getTeamIDs(league1Teams, league2Teams, league3Teams,
      internationalTeams);
    //process 1. league
    for (int i = 0; i < league1Teams.size(); i++)
    {
      int index = ArrayUtils.contains(remainingTeams, league1Teams.get(i));
      if (index == -1)
      {
        //remove teams that do not exist
        league1Teams.remove(i);
        i--;
      }
      else
      {
        //remove world cup team from remaining teams
        remainingTeams.remove(index);
      }
    }
    //process 2. league
    for (int i = 0; i < league2Teams.size(); i++)
    {
      int index = ArrayUtils.contains(remainingTeams, league2Teams.get(i));
      if (index == -1)
      {
        //remove teams that do not exist
        league2Teams.remove(i);
        i--;
      }
      else
      {
        //remove world cup team from remaining teams
        remainingTeams.remove(index);
      }
    }
    //process 3. league
    for (int i = 0; i < league3Teams.size(); i++)
    {
      int index = ArrayUtils.contains(remainingTeams, league3Teams.get(i));
      if (index == -1)
      {
        //remove teams that do not exist
        league3Teams.remove(i);
        i--;
      }
      else
      {
        //remove world cup team from remaining teams
        remainingTeams.remove(index);
      }
    }
    //process international teams
    for (int i = 0; i < internationalTeams.size(); i++)
    {
      int index = ArrayUtils.contains(remainingTeams, internationalTeams.get(i));
      if (index == -1)
      {
        //remove teams that do not exist
        internationalTeams.remove(i);
        i--;
      }
      else
      {
        //remove world cup team from remaining teams
        remainingTeams.remove(index);
      }
    }
  }
  
  
  /**
   * Call this method when the user wants to add
   * teams to the 1. league.
   */
  public void addLeague1Teams(Object teamIDs[])
  {
    addLeagueTeams(league1Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to add
   * teams to the 2. league.
   */
  public void addLeague2Teams(Object teamIDs[])
  {
    addLeagueTeams(league2Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to add
   * teams to the 3. league.
   */
  public void addLeague3Teams(Object teamIDs[])
  {
    addLeagueTeams(league3Teams, teamIDs);
  }
  
  
  /**
   * Add teams to league1, league2 or league3.
   */
  private void addLeagueTeams(ArrayList<String> league, Object teamIDs[])
  {
    if (league.size() + teamIDs.length > 10)
    {
      //league can only have 10 teams
      JOptionPane.showMessageDialog(panel.getParent(),
        Language.get("league10teamsallowed"),
        Language.get("attention"),
        JOptionPane.WARNING_MESSAGE);
    }
    else
    {
      //add teams
      for (int i = 0; i < teamIDs.length; i++)
      {
        String teamID = (String) teamIDs[i];
        ArrayUtils.remove(remainingTeams, teamID);
        league.add(teamID);
      }
      panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
        league3Teams, internationalTeams);
    }
  }
  
  
  /**
   * Call this method when the user wants to add
   * teams to the international teams.
   */
  public void addInternationalTeams(Object teamIDs[])
  {
    //add teams
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      ArrayUtils.remove(remainingTeams, teamID);
      internationalTeams.add(teamID);
    }
    ArrayUtils.sortStringArrayAlphabetically(internationalTeams);
    panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
      league3Teams, internationalTeams);
  }
  
  
  /**
   * Call this method when the user wants to remove
   * teams from the 1. league.
   */
  public void removeLeague1Teams(Object teamIDs[])
  {
    removeLeagueTeams(league1Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to remove
   * teams from the 2. league.
   */
  public void removeLeague2Teams(Object teamIDs[])
  {
    removeLeagueTeams(league2Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to remove
   * teams from the 3. league.
   */
  public void removeLeague3Teams(Object teamIDs[])
  {
    removeLeagueTeams(league3Teams, teamIDs);
  }
  
  
  /**
   * Remove teams from league1, league2 or league3.
   */
  private void removeLeagueTeams(ArrayList<String> league, Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      ArrayUtils.remove(league, teamID);
      if (ArrayUtils.contains(remainingTeams, teamID) == -1)
        remainingTeams.add(teamID);
    }
    ArrayUtils.sortStringArrayAlphabetically(remainingTeams);
    panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
      league3Teams, internationalTeams);
  }
  
  
  /**
   * Call this method when the user wants to remove
   * teams from the international teams.
   */
  public void removeInternationalTeams(Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      ArrayUtils.remove(internationalTeams, teamID);
      if (ArrayUtils.contains(remainingTeams, teamID) == -1)
        remainingTeams.add(teamID);
    }
    ArrayUtils.sortStringArrayAlphabetically(remainingTeams);
    panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
      league3Teams, internationalTeams);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 2. league teams one step upwards.
   */
  public void moveLeague2TeamsUp(Object teamIDs[])
  {
    moveLeagueTeamsUp(league2Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 3. league teams one step upwards.
   */
  public void moveLeague3TeamsUp(Object teamIDs[])
  {
    moveLeagueTeamsUp(league3Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 1. league teams one step upwards.
   */
  public void moveLeague1TeamsUp(Object teamIDs[])
  {
    moveLeagueTeamsUp(league1Teams, teamIDs);
  }
  
  
  /**
   * Move teams from league1, league2 or league3 one step up.
   */
  private void moveLeagueTeamsUp(ArrayList<String> league, Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      int index = ArrayUtils.contains(league, teamID);
      if (index > 0) //first team can not be moved further upwards
      {
        league.set(index, league.get(index - 1));
        league.set(index - 1, teamID);
      }
    }
    panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
      league3Teams, internationalTeams);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 1. league teams one step downwards.
   */
  public void moveLeague1TeamsDown(Object teamIDs[])
  {
    moveLeagueTeamsDown(league1Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 2. league teams one step downwards.
   */
  public void moveLeague2TeamsDown(Object teamIDs[])
  {
    moveLeagueTeamsDown(league2Teams, teamIDs);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected 3. league teams one step downwards.
   */
  public void moveLeague3TeamsDown(Object teamIDs[])
  {
    moveLeagueTeamsDown(league3Teams, teamIDs);
  }
  
  
  /**
   * Move teams from league1, league2 or league3 one step down.
   */
  private void moveLeagueTeamsDown(ArrayList<String> league, Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      int index = ArrayUtils.contains(league, teamID);
      if (index < league.size() - 1)
        //last team can not be moved further downwards
      {
        league.set(index, league.get(index + 1));
        league.set(index + 1, teamID);
      }
    }
    panel.updateTeamsList(remainingTeams, league1Teams, league2Teams,
      league3Teams, internationalTeams);
  }
  
  
  
  
  /**
   * Saves the current career teams in the file
   * <code>"data/gamemodes/career.xml"</code>.
   */
  public void saveToXMLFile()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving career teams...");
    
    try
    {
      Document document = XMLWriter.createEmptyDocument();
      //root element
      Node root = XMLWriter.addElement("gamemode", document);
      XMLWriter.addAttribute(root, "id", "career");
      //leagues
      Node eLeagues = XMLWriter.addElement("leagues", root);
      //1. league
      Node eLeague1 = XMLWriter.addElement("league", eLeagues);
      XMLWriter.addAttribute(eLeague1, "type", "1");
      for (int i = 0; i < league1Teams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eLeague1);
        XMLWriter.addAttribute(eTeam, "id", league1Teams.get(i));
      }
      //2. league
      Node eLeague2 = XMLWriter.addElement("league", eLeagues);
      XMLWriter.addAttribute(eLeague2, "type", "2");
      for (int i = 0; i < league2Teams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eLeague2);
        XMLWriter.addAttribute(eTeam, "id", league2Teams.get(i));
      }
      //3. league
      Node eLeague3 = XMLWriter.addElement("league", eLeagues);
      XMLWriter.addAttribute(eLeague3, "type", "3");
      for (int i = 0; i < league3Teams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eLeague3);
        XMLWriter.addAttribute(eTeam, "id", league3Teams.get(i));
      }
      //international teams
      Node eLeague0 = XMLWriter.addElement("league", eLeagues);
      XMLWriter.addAttribute(eLeague0, "type", "0");
      for (int i = 0; i < internationalTeams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eLeague0);
        XMLWriter.addAttribute(eTeam, "id", internationalTeams.get(i));
      }
 
      XMLWriter.writeFile(document, "data/gamemodes/career.xml");
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Career teams saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while saving career teams:");
      Logging.log(this, ex);
    }
  }
  
  
}
