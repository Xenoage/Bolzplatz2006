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

import com.xenoage.bp2k6.gamemodes.WorldCup;
import com.xenoage.bp2k6.tools.teameditor.gui.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This class is the controller of the
 * World Cup page.
 * 
 * @author Andreas Wenger
 */
public class WorldCupCtrl
{
  
  WorldCupPanel panel;
  
  private ArrayList<String> remainingTeams;
  private ArrayList<String> worldCupTeams;
  

  /**
   * Constructor.
   */
  public WorldCupCtrl(WorldCupPanel panel)
  {
    this.panel = panel;
    panel.setController(this);
    remainingTeams = new ArrayList<String>();
    worldCupTeams = new ArrayList<String>();
  }
  
  
  /**
   * Call this method when the world cup page
   * is opened.
   */
  public void pageActivated()
  {
    //loads the list of remaining and custom world cup teams
    loadAllTeams(false);
    //updates the panel
    panel.updateTeamsList(remainingTeams, worldCupTeams);
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
   * Loads all team IDs.
   * @param realTeams   if <code>false</code>, the "custom"
   *                    teams are loaded, otherwise the
   *                    "real" World Cup 2006 teams are loaded.
   */
  public void loadAllTeams(boolean realTeams)
  {
    //remaining teams (load all teams first)
    remainingTeams.clear();
    String[] teamFiles = new File("data/teams/").list(FileUtils.getXMLFilter());
    ArrayUtils.sortStringArrayAlphabetically(teamFiles);
    for (int i = 0; i < teamFiles.length; i++)
    {
      remainingTeams.add(teamFiles[i].substring(0, teamFiles[i].length() - 4));
    }
    //World Cup teams (remove this teams from the remaining teams list)
    worldCupTeams = WorldCup.getTeamIDs(realTeams);
    for (int i = 0; i < worldCupTeams.size(); i++)
    {
      int index = ArrayUtils.contains(remainingTeams, worldCupTeams.get(i));
      if (index == -1)
      {
        //remove teams that do not exist
        worldCupTeams.remove(i);
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
   * teams to the world cup.
   */
  public void addWorldCupTeams(Object teamIDs[])
  {
    if (worldCupTeams.size() + teamIDs.length > 32)
    {
      //world cup can only have 32 teams
      JOptionPane.showMessageDialog(panel.getParent(),
        Language.get("worldcup32teamsallowed"),
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
        worldCupTeams.add(teamID);
      }
      panel.updateTeamsList(remainingTeams, worldCupTeams);
    }
  }
  
  
  /**
   * Call this method when the user wants to remove
   * teams from the world cup.
   */
  public void removeWorldCupTeams(Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      ArrayUtils.remove(worldCupTeams, teamID);
      if (ArrayUtils.contains(remainingTeams, teamID) == -1)
        remainingTeams.add(teamID);
    }
    ArrayUtils.sortStringArrayAlphabetically(remainingTeams);
    panel.updateTeamsList(remainingTeams, worldCupTeams);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected world cup teams one step upwards.
   */
  public void moveWorldCupTeamsUp(Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      int index = ArrayUtils.contains(worldCupTeams, teamID);
      if (index > 0) //first team can not be moved further upwards
      {
        worldCupTeams.set(index, worldCupTeams.get(index - 1));
        worldCupTeams.set(index - 1, teamID);
      }
    }
    panel.updateTeamsList(remainingTeams, worldCupTeams);
  }
  
  
  /**
   * Call this method when the user wants to move
   * the selected world cup teams one step downwards.
   */
  public void moveWorldCupTeamsDown(Object teamIDs[])
  {
    for (int i = 0; i < teamIDs.length; i++)
    {
      String teamID = (String) teamIDs[i];
      int index = ArrayUtils.contains(worldCupTeams, teamID);
      if (index < worldCupTeams.size() - 1)
        //last team can not be moved further downwards
      {
        worldCupTeams.set(index, worldCupTeams.get(index + 1));
        worldCupTeams.set(index + 1, teamID);
      }
    }
    panel.updateTeamsList(remainingTeams, worldCupTeams);
  }
  
  
  /**
   * Call this method when the user wants to load
   * the real World Cup 2006 teams.
   */
  public void loadRealWorldCupTeams()
  {
    if (JOptionPane.showConfirmDialog(panel,
      Language.get("loadrealworldcupteams"), 
      Language.get("realworldcupteams"), JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
    {
      loadAllTeams(true);
      panel.updateTeamsList(remainingTeams, worldCupTeams);
    }
  }
  
  
  /**
   * Saves the current World Cup teams in the file
   * <code>"data/gamemodes/worldcup.xml"</code>
   * as the "custom" teams.
   * The "real" teams are copied from the old
   * xml file.
   */
  public void saveToXMLFile()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving World Cup teams...");
    
    try
    {
      Document document = XMLWriter.createEmptyDocument();
      //root element
      Node root = XMLWriter.addElement("gamemode", document);
      XMLWriter.addAttribute(root, "id", "worldcup");
      //copy real teams from old file
      ArrayList<String> realTeams = WorldCup.getTeamIDs(true);
      Node eRealTeams = XMLWriter.addElement("teams", root);
      XMLWriter.addAttribute(eRealTeams, "id", "real");
      for (int i = 0; i < realTeams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eRealTeams);
        XMLWriter.addAttribute(eTeam, "id", realTeams.get(i));
      }
      //custom teams
      Node eCustomTeams = XMLWriter.addElement("teams", root);
      XMLWriter.addAttribute(eCustomTeams, "id", "custom");
      for (int i = 0; i < worldCupTeams.size(); i++)
      {
        Node eTeam = XMLWriter.addElement("team", eCustomTeams);
        XMLWriter.addAttribute(eTeam, "id", worldCupTeams.get(i));
      }
 
      XMLWriter.writeFile(document, "data/gamemodes/worldcup.xml");
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "World Cup teams saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while saving World Cup teams:");
      Logging.log(this, ex);
    }

  }
  
  
}
  