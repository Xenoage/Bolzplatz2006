/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.ctrl;

import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.tools.teameditor.gui.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * This class is the controller of the
 * player data page.
 * 
 * @author Andreas Wenger
 */
public class TeamDataCtrl
{
  
  TeamDataPanel panel;
  TeamInfo currentTeamInfoBackup = null;
  TeamInfo currentTeamInfo = null;
  

  /**
   * Constructor.
   */
  public TeamDataCtrl(TeamDataPanel panel)
  {
    this.panel = panel;
    panel.setController(this);
  }
  
  
  /**
   * Call this method when the player data page
   * is opened.
   */
  public void pageActivated()
  {
    currentTeamInfo = null;
    //updates the teams list
    panel.updateTeamsList(getAllTeamIDs());
    //updates the stadium list
    ArrayList<String> stadiumIDs = getAllStadiumIDs();
    stadiumIDs.add(0, Language.get("nostadium")); //add "no stadium"
    panel.updateStadiumsList(stadiumIDs);
    //loads the first team
    panel.selectFirstTeam();
  }
  
  
  /**
   * Call this method when the player data page
   * is closed.
   */
  public void pageClosed()
  {
    //old team changed?
    closeCurrentTeam();
  }
  
  
  /**
   * Gets a list of all team ids, alphabetically sorted.
   */
  public ArrayList<String> getAllTeamIDs()
  {
    ArrayList<String> ret = new ArrayList<String>();
    String[] teamFiles = new File("data/teams/").list(FileUtils.getXMLFilter());
    ArrayUtils.sortStringArrayAlphabetically(teamFiles);
    for (int i = 0; i < teamFiles.length; i++)
      ret.add(teamFiles[i].substring(0, teamFiles[i].length() - 4));
    return ret;
  }
  
  
  /**
   * Gets a list of all stadium ids, alphabetically sorted.
   */
  public ArrayList<String> getAllStadiumIDs()
  {
    ArrayList<String> ret = new ArrayList<String>();
    String[] teamFiles = new File("data/stadiums/").list(FileUtils.getXMLFilter());
    ArrayUtils.sortStringArrayAlphabetically(teamFiles);
    for (int i = 0; i < teamFiles.length; i++)
      ret.add(teamFiles[i].substring(0, teamFiles[i].length() - 4));
    return ret;
  }
  
  
  /**
   * Call this method when the user selected a team
   * from the teams list.
   */
  public void teamSelected(String teamID)
  {
    //old team changed?
    closeCurrentTeam();
    //select new team
    currentTeamInfo = new TeamInfo(teamID);
    currentTeamInfoBackup = new TeamInfo(teamID);
    panel.showTeam(currentTeamInfo);
  }
  
  
  /**
   * New team logo was selected.
   */
  public void teamLogoSelected(File file)
  {
    String logo = file.toString();
    ImageIcon img = new ImageIcon(logo);
    int size = 512;
    if (img != null && img.getIconWidth() == size &&
      img.getIconHeight() == size)
    {
      //copy logo into data/teams/<id>-logo.png
      File fileDest = new File("data/teams/" +
        currentTeamInfo.getID() + "-logo.png");
      if (!file.equals(fileDest))
      {
        if (fileDest.exists())
          fileDest.delete();
        try
        {
          FileUtils.copy(file, fileDest);
        }
        catch (Exception ex)
        {
          String text = Language.get("teamlogoerror");
          text = text.replace("%size%", String.valueOf(size));
          JOptionPane.showMessageDialog(panel.getParent(),
            text, Language.get("error"),
            JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else
    {
      String text = Language.get("invalidteamlogo");
      text = text.replace("%size%", String.valueOf(size));
      JOptionPane.showMessageDialog(panel.getParent(),
        text, Language.get("attention"),
        JOptionPane.WARNING_MESSAGE);
    }
  }
  
  
  /**
   * New team ads was selected.
   */
  public void teamAdsSelected(File file)
  {
    String ads = file.toString();
    ImageIcon img = new ImageIcon(ads);
    int size = 512;
    if (img != null && img.getIconWidth() == size &&
      img.getIconHeight() == size)
    {
      //copy logo into data/teams/<id>-ads.png
      File fileDest = new File("data/teams/" +
        currentTeamInfo.getID() + "-ads.png");
      if (!file.equals(fileDest))
      {
        if (fileDest.exists())
          fileDest.delete();
        try
        {
          FileUtils.copy(file, fileDest);
        }
        catch (Exception ex)
        {
          String text = Language.get("teamadserror");
          text = text.replace("%size%", String.valueOf(size));
          JOptionPane.showMessageDialog(panel.getParent(),
            text, Language.get("error"),
            JOptionPane.ERROR_MESSAGE);
        }
      }
    }
    else
    {
      String text = Language.get("invalidteamads");
      text = text.replace("%size%", String.valueOf(size));
      JOptionPane.showMessageDialog(panel.getParent(),
        text, Language.get("attention"),
        JOptionPane.WARNING_MESSAGE);
    }
  }
  
  
  /**
   * This method is called when the teams list has changed
   * (e.g. new team was created). Update this list.
   */
  public void teamsListChanged()
  {
    //updates the teams list
    panel.updateTeamsList(getAllTeamIDs());
  }
  
  
  /**
   * Creates a new team and opens it.
   */
  public void createNewTeam(String newID)
  {
    if (newID != null && newID.length() > 0)
    {
      File newFile = new File("data/teams/" + newID + ".xml");
      if (!newFile.exists())
      {
        try
        {
          FileUtils.copy(new File("data/config/newteam.xml"), newFile);
          teamsListChanged();
          panel.selectTeam(newID);
        }
        catch (Exception ex)
        {
          JOptionPane.showMessageDialog(panel.getParent(),
            Language.get("teamlogoerror"), Language.get("error"),
            JOptionPane.ERROR_MESSAGE);
        }
      }
      else
      {
        JOptionPane.showMessageDialog(panel.getParent(),
          Language.get("teamalreadyexists"), Language.get("attention"),
          JOptionPane.WARNING_MESSAGE);
      }
    }
  }
  
  
  /**
   * Deletes the current team.
   */
  public void deleteCurrentTeam()
  {
    String question = Language.get("question_deleteteam");
    question = question.replaceAll("%teamid%", currentTeamInfo.getID());
    if (JOptionPane.showConfirmDialog(panel, question, 
      Language.get("deleteteam"), JOptionPane.YES_NO_OPTION,
      JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
    {
      File file = new File("data/teams/" + currentTeamInfo.getID() + ".xml");
      if (file.exists())
        file.delete();
      file = new File("data/teams/" + currentTeamInfo.getID() + "-logo.png");
      if (file.exists())
        file.delete();
      //updates the teams list
      panel.updateTeamsList(getAllTeamIDs());
      //loads the first team
      panel.selectFirstTeam();
    }
  }
  
  
  /**
   * Close current team and ask if changes should be
   * saved (if needed).
   */
  private void closeCurrentTeam()
  {
    if (currentTeamInfo != null && currentTeamInfoBackup != null)
    {
      if (!currentTeamInfo.equals(currentTeamInfoBackup))
      {
        //save this team?
        String question = Language.get("question_saveteam");
        question = question.replaceAll("%teamid%", currentTeamInfo.getID());
        if (JOptionPane.showConfirmDialog(panel, question, 
          Language.get("teammodified"), JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
        {
          currentTeamInfo.saveToXMLFile();
        }
      }
    }
  }
  
}
