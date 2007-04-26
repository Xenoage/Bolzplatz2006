/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.match.TeamInfo;
import com.xenoage.bp2k6.tools.teameditor.ctrl.TeamDataCtrl;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Class for the team data tab page.
 * 
 * @author awr
 */
public class TeamDataPanel
  extends JPanel
  implements KeyListener, ActionListener
{
  private MainFrame mainFrame;
  private TeamDataCtrl controller; //the controller for this page
  
  private TeamInfo teamInfo; //the current TeamInfo object
  
  //main panels
  JPanel panelLeft, panelRight;
  
  //team list
  JList listTeams;
  DefaultListModel listTeamsModel;
  JButton btnTeamNew;
  JButton btnTeamDelete;
  
  //team data
  private boolean updating = true;
  JLabel lblTeamIDValue;
  JTextField txtTeamName;
  JTextArea txtTeamDescription;
  JLabel imgTeamLogo;
  JButton btnTeamLogo;
  JLabel lblTeamGroup;
  JComboBox cmbTeamGroup;
  JLabel lblTeamStadium;
  JComboBox cmbTeamStadium;
  JLabel imgTeamAds;
  JButton btnTeamAds;
  JComboBox cmbTeamAds;
  PlayerDataPanel playerDataPanels[] = new PlayerDataPanel[5];
  TeamColorPanel teamColorPanels[] = new TeamColorPanel[3];
  

  
  /**
   * Constructor.
   */
  public TeamDataPanel(MainFrame mainFrame)
  {
    this.mainFrame = mainFrame;
    
    this.setLayout(null);
    //this.setBackground(Color.blue);
    
    //left panel
    panelLeft = createPanelTeamList();
    this.add(panelLeft);
    
    //vertical seperator
    JLabel seperator = new JLabel();
    seperator.setBorder(new EtchedBorder());
    seperator.setSize(2, mainFrame.getWindowHeight() - 20);
    seperator.setLocation(250, 10);
    this.add(seperator);
    
    //right panel
    panelRight = createPanelTeamData();
    this.add(panelRight);

  }
  
  
  /**
   * Sets the controller for this page.
   * Must be called after the constructor.
   */
  public void setController(TeamDataCtrl controller)
  {
    this.controller = controller;
  }
  
  
  /**
   * Create the left bar: Team list
   */
  private JPanel createPanelTeamList()
  {
    JPanel ret = new JPanel(null);
    ret.setSize(220, mainFrame.getWindowHeight() - 20);
    ret.setLocation(10, 10);
    
    //title
    JLabel lblListTeams = new JLabel(Language.get("selectteamhere") + ":");
    lblListTeams.setSize(ret.getWidth(), 20);
    ret.add(lblListTeams);
    
    //list of teams
    listTeamsModel = new DefaultListModel();
    listTeams = new JList(listTeamsModel);
    listTeams.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listTeams.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        if (!updating)
          controller.teamSelected((String) listTeams.getSelectedValue());
      }
    });
    JScrollPane scroll = new JScrollPane(listTeams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 100);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnTeamNew = new JButton(Language.get("newteam"));
    btnTeamNew.setSize(ret.getWidth(), 24);
    btnTeamNew.setLocation(0, ret.getHeight() - 60);
    ret.add(btnTeamNew);
    btnTeamNew.addActionListener(this);
    btnTeamDelete = new JButton(Language.get("deleteteam"));
    btnTeamDelete.setSize(ret.getWidth(), 24);
    btnTeamDelete.setLocation(0, ret.getHeight() - 30);
    ret.add(btnTeamDelete);
    btnTeamDelete.addActionListener(this);
    
    return ret;
  }
  
  
  /**
   * Create the right bar: Team data
   */
  private JPanel createPanelTeamData()
  {
    JPanel ret = new JPanel(null);
    ret.setSize(mainFrame.getWindowWidth() - 270,
      mainFrame.getWindowHeight() - 20);
    ret.setLocation(260, 10);
    
    //id of the team
    JLabel lblTeamID = new JLabel(Language.get("teamid") + ":");
    lblTeamID.setSize(150, 20);
    lblTeamID.setLocation(10, 0);
    ret.add(lblTeamID);
    lblTeamIDValue = new JLabel("?");
    lblTeamIDValue.setSize(250, 20);
    lblTeamIDValue.setLocation(160, 0);
    ret.add(lblTeamIDValue);
    
    //name of the team
    JLabel lblTeamName = new JLabel(Language.get("teamname") + ":");
    lblTeamName.setSize(150, 20);
    lblTeamName.setLocation(10, 30);
    ret.add(lblTeamName);
    txtTeamName = new JTextField("?");
    txtTeamName.setSize(250, 20);
    txtTeamName.setLocation(160, 30);
    ret.add(txtTeamName);
    txtTeamName.addKeyListener(this);
    
    //description of the team
    JLabel lblTeamDescription = new JLabel(Language.get("teamdescription") + ":");
    lblTeamDescription.setSize(150, 20);
    lblTeamDescription.setLocation(10, 60);
    ret.add(lblTeamDescription);
    txtTeamDescription = new JTextArea("?");
    txtTeamDescription.setLineWrap(true);
    txtTeamDescription.setWrapStyleWord(true);
    txtTeamDescription.setBorder(new EtchedBorder());
    txtTeamDescription.setSize(250, 70);
    txtTeamDescription.setLocation(160, 60);
    ret.add(txtTeamDescription);
    txtTeamDescription.addKeyListener(this);
    
    //logo of the team
    JLabel lblTeamLogo = new JLabel(Language.get("teamlogo") + ":");
    lblTeamLogo.setSize(256, 20);
    lblTeamLogo.setLocation(450, 0);
    ret.add(lblTeamLogo);
    imgTeamLogo = new JLabel();
    //imgTeamLogo.setBorder(new EtchedBorder());
    imgTeamLogo.setSize(256, 256);
    imgTeamLogo.setLocation(450, 30);
    ret.add(imgTeamLogo);
    updateTeamLogo();
    btnTeamLogo = new JButton(Language.get("selectteamlogo"));
    btnTeamLogo.setSize(256, 25);
    btnTeamLogo.setLocation(450, 296);
    ret.add(btnTeamLogo);
    btnTeamLogo.addActionListener(this);
    
    //group of the team
    lblTeamGroup = new JLabel(Language.get("teamgroup") + ":");
    lblTeamGroup.setSize(150, 20);
    lblTeamGroup.setLocation(10, 140);
    ret.add(lblTeamGroup);
    cmbTeamGroup = new JComboBox();
    cmbTeamGroup.setSize(250, 20);
    cmbTeamGroup.setLocation(160, 140);
    cmbTeamGroup.addItem(Language.get("nogroup"));
    String[] groups = TeamInfo.getAvailableGroups();
    for (String group : groups)
      cmbTeamGroup.addItem(Language.get(group));
    ret.add(cmbTeamGroup);
    cmbTeamGroup.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        if (!updating)
        {
          if (cmbTeamGroup.getSelectedIndex() == 0)
            teamInfo.setGroup(null);
          else
          {
            teamInfo.setGroup(TeamInfo.getAvailableGroups()
              [cmbTeamGroup.getSelectedIndex()-1]);
          }
        }
      }
    });
    
    //stadium of the team
    lblTeamStadium = new JLabel(Language.get("teamstadium") + ":");
    lblTeamStadium.setSize(150, 20);
    lblTeamStadium.setLocation(10, 170);
    ret.add(lblTeamStadium);
    cmbTeamStadium = new JComboBox();
    cmbTeamStadium.setSize(250, 20);
    cmbTeamStadium.setLocation(160, 170);
    cmbTeamStadium.addItem(Language.get("nostadium"));
    ret.add(cmbTeamStadium);
    cmbTeamStadium.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        if (!updating)
        {
          if (cmbTeamStadium.getSelectedIndex() == 0)
            teamInfo.setStadium("");
          else
            teamInfo.setStadium((String) cmbTeamStadium.getSelectedItem());
        }
      }
    });
    
    //ads of the team
    JLabel lblTeamAds = new JLabel(Language.get("teamads") + ":");
    lblTeamAds.setSize(400, 20);
    lblTeamAds.setLocation(10, 200);
    ret.add(lblTeamAds);
    
    imgTeamAds = new JLabel();
    imgTeamAds.setSize(4000, 640);
    imgTeamAds.setLocation(10, 226);
    imgTeamAds.setHorizontalAlignment(JLabel.LEFT);
    imgTeamAds.setVerticalAlignment(JLabel.TOP);
    ret.add(imgTeamAds);
    
    btnTeamAds = new JButton(Language.get("chooseteamads"));
    btnTeamAds.setSize(195, 25);
    btnTeamAds.setLocation(10, 296);
    ret.add(btnTeamAds);
    btnTeamAds.addActionListener(this);
    
    cmbTeamAds = new JComboBox(new String[] {
      Language.get("teamlogo"), Language.get("ad") + " 1",
      Language.get("ad") + " 2", Language.get("ad") + " 3",
      Language.get("fanbanners"), Language.get("backside")});
    cmbTeamAds.setSize(195, 25);
    cmbTeamAds.setLocation(215, 296);
    ret.add(cmbTeamAds);
    cmbTeamAds.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent arg0)
      {
        updateTeamAds();
      }
    });
    updateTeamAds();
    
    //values of the players
    JTabbedPane tabPlayers = new JTabbedPane();
    tabPlayers.setSize(400, 300);
    tabPlayers.setLocation(10, 340);
    for (int i = 0; i < 5; i++)
      playerDataPanels[i] = new PlayerDataPanel();
    tabPlayers.addTab(Language.get("goalkeeper"), playerDataPanels[0]);
    tabPlayers.addTab(Language.get("defenseleft"), playerDataPanels[1]);
    tabPlayers.addTab(Language.get("defenseright"), playerDataPanels[2]);
    tabPlayers.addTab(Language.get("forwardleft"), playerDataPanels[3]);
    tabPlayers.addTab(Language.get("forwardright"), playerDataPanels[4]);
    ret.add(tabPlayers);
    
    //team colors
    JTabbedPane tabColors = new JTabbedPane();
    tabColors.setSize(256, 300);
    tabColors.setLocation(450, 340);
    for (int i = 0; i < 3; i++)
      teamColorPanels[i] = new TeamColorPanel();
    tabColors.addTab(Language.get("homecolors"), teamColorPanels[0]);
    tabColors.addTab(Language.get("awaycolors"), teamColorPanels[1]);
    tabColors.addTab(Language.get("goalkeepercolors"), teamColorPanels[2]);
    ret.add(tabColors);
    
    return ret;
  }
  
  
  /**
   * Fills the teams list with the given team ids.
   * The list should be sorted alphabetically.
   */
  public void updateTeamsList(ArrayList<String> teamIDs)
  {
    updating = true;
    listTeamsModel.clear();
    for (int i = 0; i < teamIDs.size(); i++)
      listTeamsModel.addElement(teamIDs.get(i));
    updating = false;
  }
  
  
  /**
   * Fills the stadiums list with the given stadium ids.
   * The list should be sorted alphabetically.
   * The first item must be no stadium, but a description
   * for "no stadium".
   */
  public void updateStadiumsList(ArrayList<String> stadiumIDs)
  {
    updating = true;
    cmbTeamStadium.removeAllItems();
    for (int i = 0; i < stadiumIDs.size(); i++)
      cmbTeamStadium.addItem(stadiumIDs.get(i));
    updating = false;
  }
  
  
  /**
   * Selects the first team in the list
   * (if list has at least one item)
   */
  public void selectFirstTeam()
  {
    if (listTeamsModel.size() > 0)
      listTeams.setSelectedIndex(0);
  }
  
  
  /**
   * Selects the team with the given ID
   * or don't change selection of not found.
   */
  public void selectTeam(String id)
  {
    for (int i = 0; i < listTeamsModel.size(); i++)
      if (listTeamsModel.get(i).equals(id))
        listTeams.setSelectedIndex(i);
  }
  
  
  
  /**
   * Shows the given team.
   */
  public void showTeam(TeamInfo teamInfo)
  {
    this.teamInfo = teamInfo;
    updating = true; //no change events now
    //general information
    lblTeamIDValue.setText(teamInfo.getID());
    txtTeamName.setText(teamInfo.getName());
    txtTeamDescription.setText(teamInfo.getDescription());
    selectGroup(teamInfo.getGroup());
    selectStadium(teamInfo.getStadium());
    //team logo
    updateTeamLogo();
    //team ads
    updateTeamAds();
    //player values
    for (int i = 0; i < teamInfo.getPlayerInfo().length; i++)
      playerDataPanels[i].update(teamInfo.getPlayerInfo()[i]);
    //player colors
    teamColorPanels[0].update(teamInfo.getPlayerColorsHome());
    teamColorPanels[1].update(teamInfo.getPlayerColorsAway());
    teamColorPanels[2].update(teamInfo.getPlayerColorsGoalkeeper());
    updating = false;
  }
  
  
  /**
   * Selects the item of the groups list with
   * the given id. If not found, the first item
   * (meaning: "no group") is selected.
   */
  private void selectGroup(String groupID)
  {
    for (int i = 0; i < cmbTeamGroup.getItemCount(); i++)
    {
      if (cmbTeamGroup.getItemAt(i).equals(groupID))
      {
        cmbTeamGroup.setSelectedIndex(i);
        return;
      }
    }
    cmbTeamGroup.setSelectedIndex(0);
  }
  
  
  /**
   * Selects the item of the stadiums list with
   * the given id. If not found, the first item
   * (meaning: "no stadium") is selected.
   */
  private void selectStadium(String stadiumID)
  {
    for (int i = 0; i < cmbTeamStadium.getItemCount(); i++)
    {
      if (cmbTeamStadium.getItemAt(i).equals(stadiumID))
      {
        cmbTeamStadium.setSelectedIndex(i);
        return;
      }
    }
    cmbTeamStadium.setSelectedIndex(0);
  }


  public void keyTyped(KeyEvent e)
  { 
  }


  public void keyPressed(KeyEvent e)
  {
    
  }


  /**
   * This method is called when the user pressed
   * a key in a text field.
   */
  public void keyReleased(KeyEvent e)
  {
    if (!updating)
    {
      if (e.getSource() == txtTeamName)
        teamInfo.setName(txtTeamName.getText());
      else if (e.getSource() == txtTeamDescription)
        teamInfo.setDescription(txtTeamDescription.getText());
    }
  }


  /**
   * This method is called when the user e.g.
   * presses a button.
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == btnTeamNew)
    {
      //create a new team
      String newID = JOptionPane.showInputDialog(mainFrame,
        Language.get("newteamid"), Language.get("newteam"),
        JOptionPane.QUESTION_MESSAGE);
      controller.createNewTeam(newID);
    }
    else if (e.getSource() == btnTeamDelete)
    {
      controller.deleteCurrentTeam();
    }
    else if (e.getSource() == btnTeamLogo)
    {
      //choose team logo
      JFileChooser dlgLogo = new JFileChooser();
      dlgLogo.setCurrentDirectory(new java.io.File("."));
      dlgLogo.addChoosableFileFilter(FileUtils.getTeamLogoFilter());
      int ret = dlgLogo.showOpenDialog(mainFrame);
      if (ret == JFileChooser.APPROVE_OPTION)
      {
        controller.teamLogoSelected(dlgLogo.getSelectedFile());
      }
      updateTeamLogo();
    }
    else if (e.getSource() == btnTeamAds)
    {
      //choose team ads
      JFileChooser dlgAds = new JFileChooser();
      dlgAds.setCurrentDirectory(new java.io.File("."));
      dlgAds.addChoosableFileFilter(FileUtils.getTeamAdsFilter());
      int ret = dlgAds.showOpenDialog(mainFrame);
      if (ret == JFileChooser.APPROVE_OPTION)
      {
        controller.teamAdsSelected(dlgAds.getSelectedFile());
      }
      updateTeamAds();
    }
  }
  
  
  /**
   * Updates the team logos.
   */
  public void updateTeamLogo()
  {
    if (teamInfo != null)
    {
      //ImageIcon caches images... if we overwrite
      //the team logo file, creating a new ImageIcon has no effect...
      //Use a temporary file now
      File oldTempLogo = new File("data/temp/teamlogo" + tempLogoNr + ".png");
      if (oldTempLogo.exists())
        oldTempLogo.delete();
      tempLogoNr++;
      File newTempLogo = new File("data/temp/teamlogo" + tempLogoNr + ".png");
      try
      {
        FileUtils.copy(new File(teamInfo.getTeamLogoPath()), newTempLogo);
        ImageIcon icon = new ImageIcon(newTempLogo.toString());
        icon.setImage(icon.getImage().getScaledInstance(
          256, 256, Image.SCALE_FAST));
        imgTeamLogo.setIcon(icon);
        newTempLogo.deleteOnExit();
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Could not preview team logo:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
      }
    }
  }
  private int tempLogoNr = 0;
  
  
  
  /**
   * Updates the team ads.
   */
  public void updateTeamAds()
  {
    if (teamInfo != null)
    {
      //Problem: ImageIcon caches images... if we overwrite
      //the team ads file, creating a new ImageIcon has no effect...
      //Use a temporary file now
      File oldTempLogo = new File("data/temp/teamads" + tempAdsNr + ".png");
      if (oldTempLogo.exists())
        oldTempLogo.delete();
      tempAdsNr++;
      File newTempLogo = new File("data/temp/teamads" + tempAdsNr + ".png");
      try
      {
        FileUtils.copy(new File(teamInfo.getTeamAdsPath()), newTempLogo);
        ImageIcon iconAds = new ImageIcon(newTempLogo.toString());
        BufferedImage imgThisAd = new BufferedImage(
          500, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) imgThisAd.getGraphics();
        int sourcePosY = 82 * cmbTeamAds.getSelectedIndex() + 1;
        g2d.drawImage(iconAds.getImage(), 0, 0, 400, 64,
           1, sourcePosY, 501, sourcePosY + 80, null);
        ImageIcon iconThisAd = new ImageIcon(imgThisAd);
        imgTeamAds.setIcon(iconThisAd);
        newTempLogo.deleteOnExit();
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Could not preview team logo:");
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
      }
    }
  }
  private int tempAdsNr = 0;
  

  
}
