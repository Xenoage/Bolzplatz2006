/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.tools.teameditor.ctrl.CareerCtrl;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * Class for the career tab page.
 * 
 * @author awr
 */
public class CareerPanel
  extends JPanel
  implements ActionListener
{
  private MainFrame mainFrame;
  private CareerCtrl controller; //the controller for this page
  
  //main panels
  JPanel panelRemaining,
    panelLeague1, panelLeague2, panelLeague3, panelInternational;
  
  //remaining teams list
  JList listRemainingTeams;
  DefaultListModel listRemainingTeamsModel;
  JButton btnRemainingTeamsAddLeague1;
  JButton btnRemainingTeamsAddLeague2;
  JButton btnRemainingTeamsAddLeague3;
  JButton btnRemainingTeamsAddInternational;
  
  //1. league teams list
  JList listLeague1Teams;
  DefaultListModel listLeague1TeamsModel;
  JButton btnLeague1TeamsRemove;
  JButton btnLeague1TeamsMoveUp;
  JButton btnLeague1TeamsMoveDown;
  
  //2. league teams list
  JList listLeague2Teams;
  DefaultListModel listLeague2TeamsModel;
  JButton btnLeague2TeamsRemove;
  JButton btnLeague2TeamsMoveUp;
  JButton btnLeague2TeamsMoveDown;
  
  //3. league teams list
  JList listLeague3Teams;
  DefaultListModel listLeague3TeamsModel;
  JButton btnLeague3TeamsRemove;
  JButton btnLeague3TeamsMoveUp;
  JButton btnLeague3TeamsMoveDown;
  
  //international teams list
  JList listInternationalTeams;
  DefaultListModel listInternationalTeamsModel;
  JButton btnInternationalTeamsRemove;
  
  //warning if leagues are not complete
  JTextArea txtWarningLeagues;
  
  
  /**
   * Constructor.
   */
  public CareerPanel(MainFrame mainFrame)
  {
    this.mainFrame = mainFrame; 
    this.setLayout(null);
    
    int marginleft = 10;
    int colwidth = 1000 / 5;
    
    panelRemaining = createPanelRemainingTeamsList(
      marginleft, colwidth - 20);
    this.add(panelRemaining);
    addVerticalSeperator(
      marginleft + colwidth - 10, mainFrame.getWindowHeight() - 20);
    
    panelLeague1 = createPanelLeague1TeamsList(
      marginleft + colwidth, colwidth - 20);
    this.add(panelLeague1);
    addVerticalSeperator(marginleft + colwidth * 2 - 10,
      mainFrame.getWindowHeight() - 60);
    
    panelLeague2 = createPanelLeague2TeamsList(
      marginleft + colwidth * 2, colwidth - 20);
    this.add(panelLeague2);
    addVerticalSeperator(
      marginleft + colwidth * 3 - 10, mainFrame.getWindowHeight() - 60);
    
    panelLeague3 = createPanelLeague3TeamsList(
      marginleft + colwidth * 3, colwidth - 20);
    this.add(panelLeague3);
    addVerticalSeperator(
      marginleft + colwidth * 4 - 10, mainFrame.getWindowHeight() - 60);
    
    panelInternational = createPanelInternationalTeamsList(
      marginleft + colwidth * 4, colwidth - 20);
    this.add(panelInternational);
    
    //hint/warning label
    txtWarningLeagues = new JTextArea(Language.get("warning_leagues"));
    txtWarningLeagues.setForeground(Color.red);
    txtWarningLeagues.setOpaque(false);
    txtWarningLeagues.setEditable(false);
    txtWarningLeagues.setWrapStyleWord(true);
    txtWarningLeagues.setLineWrap(true);
    txtWarningLeagues.setSize(colwidth * 4, 40);
    txtWarningLeagues.setLocation(
      colwidth + 10, mainFrame.getWindowHeight() - 50);
    this.add(txtWarningLeagues);

  }
  
  
  /**
   * Sets the controller for this page.
   * Must be called after the constructor.
   */
  public void setController(CareerCtrl controller)
  {
    this.controller = controller;
  }
  
  
  /**
   * Adds a vertical seperator at the given
   * position.
   */
  private void addVerticalSeperator(int x, int height)
  {
    JLabel seperator = new JLabel();
    seperator.setBorder(new EtchedBorder());
    seperator.setSize(2, height);
    seperator.setLocation(x, 10);
    this.add(seperator);
  }
  
  
  /**
   * Create the remaining teams list
   */
  private JPanel createPanelRemainingTeamsList(int x, int width)
  {
    JPanel ret = new JPanel(null);
    ret.setSize(width, mainFrame.getWindowHeight() - 20);
    ret.setLocation(x, 10);
    
    //title
    JLabel lblListRemainingTeams = new JLabel(Language.get("remainingteams") + ":");
    lblListRemainingTeams.setSize(ret.getWidth(), 20);
    ret.add(lblListRemainingTeams);
    
    //list of teams
    listRemainingTeamsModel = new DefaultListModel();
    listRemainingTeams = new JList(listRemainingTeamsModel);
    JScrollPane scroll = new JScrollPane(listRemainingTeams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 160);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnRemainingTeamsAddLeague1 = new JButton(Language.get("league1"));
    btnRemainingTeamsAddLeague1.setSize(ret.getWidth(), 24);
    btnRemainingTeamsAddLeague1.setLocation(0, ret.getHeight() - 120);
    ret.add(btnRemainingTeamsAddLeague1);
    btnRemainingTeamsAddLeague1.addActionListener(this);
    
    btnRemainingTeamsAddLeague2 = new JButton(Language.get("league2"));
    btnRemainingTeamsAddLeague2.setSize(ret.getWidth(), 24);
    btnRemainingTeamsAddLeague2.setLocation(0, ret.getHeight() - 90);
    ret.add(btnRemainingTeamsAddLeague2);
    btnRemainingTeamsAddLeague2.addActionListener(this);
    
    btnRemainingTeamsAddLeague3 = new JButton(Language.get("league3"));
    btnRemainingTeamsAddLeague3.setSize(ret.getWidth(), 24);
    btnRemainingTeamsAddLeague3.setLocation(0, ret.getHeight() - 60);
    ret.add(btnRemainingTeamsAddLeague3);
    btnRemainingTeamsAddLeague3.addActionListener(this);
    
    btnRemainingTeamsAddInternational = new JButton(Language.get("international"));
    btnRemainingTeamsAddInternational.setSize(ret.getWidth(), 24);
    btnRemainingTeamsAddInternational.setLocation(0, ret.getHeight() - 30);
    ret.add(btnRemainingTeamsAddInternational);
    btnRemainingTeamsAddInternational.addActionListener(this);
    
    
    return ret;
  }
  
  
  /**
   * Create the 1. league teams list
   */
  private JPanel createPanelLeague1TeamsList(int x, int width)
  {
    JPanel ret = new JPanel(null);
    ret.setSize(width, mainFrame.getWindowHeight() - 60);
    ret.setLocation(x, 10);
    
    //title
    JLabel lblTitle = new JLabel(Language.get("league1") + ":");
    lblTitle.setSize(ret.getWidth(), 20);
    ret.add(lblTitle);
    
    //list of teams
    listLeague1TeamsModel = new DefaultListModel();
    listLeague1Teams = new JList(listLeague1TeamsModel);
    JScrollPane scroll = new JScrollPane(listLeague1Teams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 130);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnLeague1TeamsMoveUp = new JButton(Language.get("moveup"));
    btnLeague1TeamsMoveUp.setSize(ret.getWidth(), 24);
    btnLeague1TeamsMoveUp.setLocation(0, ret.getHeight() - 90);
    ret.add(btnLeague1TeamsMoveUp);
    btnLeague1TeamsMoveUp.addActionListener(this);
  
    btnLeague1TeamsMoveDown = new JButton(Language.get("movedown"));
    btnLeague1TeamsMoveDown.setSize(ret.getWidth(), 24);
    btnLeague1TeamsMoveDown.setLocation(0, ret.getHeight() - 60);
    ret.add(btnLeague1TeamsMoveDown);
    btnLeague1TeamsMoveDown.addActionListener(this);
    
    btnLeague1TeamsRemove = new JButton(Language.get("removeteams"));
    btnLeague1TeamsRemove.setSize(ret.getWidth(), 24);
    btnLeague1TeamsRemove.setLocation(0, ret.getHeight() - 30);
    ret.add(btnLeague1TeamsRemove);
    btnLeague1TeamsRemove.addActionListener(this);
    
    return ret;
  }
  
  
  /**
   * Create the 2. league teams list
   */
  private JPanel createPanelLeague2TeamsList(int x, int width)
  {
    JPanel ret = new JPanel(null);
    ret.setSize(width, mainFrame.getWindowHeight() - 60);
    ret.setLocation(x, 10);
    
    //title
    JLabel lblTitle = new JLabel(Language.get("league2") + ":");
    lblTitle.setSize(ret.getWidth(), 20);
    ret.add(lblTitle);
    
    //list of teams
    listLeague2TeamsModel = new DefaultListModel();
    listLeague2Teams = new JList(listLeague2TeamsModel);
    JScrollPane scroll = new JScrollPane(listLeague2Teams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 130);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnLeague2TeamsMoveUp = new JButton(Language.get("moveup"));
    btnLeague2TeamsMoveUp.setSize(ret.getWidth(), 24);
    btnLeague2TeamsMoveUp.setLocation(0, ret.getHeight() - 90);
    ret.add(btnLeague2TeamsMoveUp);
    btnLeague2TeamsMoveUp.addActionListener(this);
  
    btnLeague2TeamsMoveDown = new JButton(Language.get("movedown"));
    btnLeague2TeamsMoveDown.setSize(ret.getWidth(), 24);
    btnLeague2TeamsMoveDown.setLocation(0, ret.getHeight() - 60);
    ret.add(btnLeague2TeamsMoveDown);
    btnLeague2TeamsMoveDown.addActionListener(this);
    
    btnLeague2TeamsRemove = new JButton(Language.get("removeteams"));
    btnLeague2TeamsRemove.setSize(ret.getWidth(), 24);
    btnLeague2TeamsRemove.setLocation(0, ret.getHeight() - 30);
    ret.add(btnLeague2TeamsRemove);
    btnLeague2TeamsRemove.addActionListener(this);
    
    return ret;
  }
  
  
  /**
   * Create the 3. league teams list
   */
  private JPanel createPanelLeague3TeamsList(int x, int width)
  {
    JPanel ret = new JPanel(null);
    ret.setSize(width, mainFrame.getWindowHeight() - 60);
    ret.setLocation(x, 10);
    
    //title
    JLabel lblTitle = new JLabel(Language.get("league3") + ":");
    lblTitle.setSize(ret.getWidth(), 20);
    ret.add(lblTitle);
    
    //list of teams
    listLeague3TeamsModel = new DefaultListModel();
    listLeague3Teams = new JList(listLeague3TeamsModel);
    JScrollPane scroll = new JScrollPane(listLeague3Teams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 130);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnLeague3TeamsMoveUp = new JButton(Language.get("moveup"));
    btnLeague3TeamsMoveUp.setSize(ret.getWidth(), 24);
    btnLeague3TeamsMoveUp.setLocation(0, ret.getHeight() - 90);
    ret.add(btnLeague3TeamsMoveUp);
    btnLeague3TeamsMoveUp.addActionListener(this);
  
    btnLeague3TeamsMoveDown = new JButton(Language.get("movedown"));
    btnLeague3TeamsMoveDown.setSize(ret.getWidth(), 24);
    btnLeague3TeamsMoveDown.setLocation(0, ret.getHeight() - 60);
    ret.add(btnLeague3TeamsMoveDown);
    btnLeague3TeamsMoveDown.addActionListener(this);
    
    btnLeague3TeamsRemove = new JButton(Language.get("removeteams"));
    btnLeague3TeamsRemove.setSize(ret.getWidth(), 24);
    btnLeague3TeamsRemove.setLocation(0, ret.getHeight() - 30);
    ret.add(btnLeague3TeamsRemove);
    btnLeague3TeamsRemove.addActionListener(this);
    
    return ret;
  }
  
  
  /**
   * Create the international teams list
   */
  private JPanel createPanelInternationalTeamsList(int x, int width)
  {
    JPanel ret = new JPanel(null);
    ret.setSize(width, mainFrame.getWindowHeight() - 60);
    ret.setLocation(x, 10);
    
    //title
    JLabel lblTitle = new JLabel(Language.get("international") + ":");
    lblTitle.setSize(ret.getWidth(), 20);
    ret.add(lblTitle);
    
    //list of teams
    listInternationalTeamsModel = new DefaultListModel();
    listInternationalTeams = new JList(listInternationalTeamsModel);
    JScrollPane scroll = new JScrollPane(listInternationalTeams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 70);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnInternationalTeamsRemove = new JButton(Language.get("removeteams"));
    btnInternationalTeamsRemove.setSize(ret.getWidth(), 24);
    btnInternationalTeamsRemove.setLocation(0, ret.getHeight() - 30);
    ret.add(btnInternationalTeamsRemove);
    btnInternationalTeamsRemove.addActionListener(this);
    
    return ret;
  }
  
  
  /**
   * Fills the teams list with the given team ids.
   * The remaining teams list and the international
   * teams list should be sorted alphabetically.
   */
  public void updateTeamsList(ArrayList<String> remainingTeams,
    ArrayList<String> league1Teams, ArrayList<String> league2Teams,
    ArrayList<String> league3Teams, ArrayList<String> internationalTeams)
  {
    listRemainingTeamsModel.clear();
    for (int i = 0; i < remainingTeams.size(); i++)
      listRemainingTeamsModel.addElement(remainingTeams.get(i));
    listLeague1TeamsModel.clear();
    for (int i = 0; i < league1Teams.size(); i++)
      listLeague1TeamsModel.addElement(league1Teams.get(i));
    listLeague2TeamsModel.clear();
    for (int i = 0; i < league2Teams.size(); i++)
      listLeague2TeamsModel.addElement(league2Teams.get(i));
    listLeague3TeamsModel.clear();
    for (int i = 0; i < league3Teams.size(); i++)
      listLeague3TeamsModel.addElement(league3Teams.get(i));
    listInternationalTeamsModel.clear();
    for (int i = 0; i < internationalTeams.size(); i++)
      listInternationalTeamsModel.addElement(internationalTeams.get(i));
    //show warning if a league has less than 10 teams
    //or if there are less than 13 international teams
    if (league1Teams.size() != 10 || league2Teams.size() != 10 ||
      league3Teams.size() != 10 || internationalTeams.size() < 13)
    {
      txtWarningLeagues.setText(Language.get("warning_leagues"));
      txtWarningLeagues.setForeground(Color.red);
    }
    else
    {
      //show names of national Allstars League participants
      txtWarningLeagues.setText(Language.get("hint_leagues") +
        " " + listLeague1TeamsModel.get(0) + ", " +
        listLeague1TeamsModel.get(1) + ", " +
        listLeague1TeamsModel.get(2));
      txtWarningLeagues.setForeground(Color.black);
    }
  }


  /**
   * This method is called when the user clicks
   * a button.
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == btnRemainingTeamsAddLeague1)
    {
      //add selected teams to 1. league
      controller.addLeague1Teams(
        listRemainingTeams.getSelectedValues());
    }
    else if (e.getSource() == btnRemainingTeamsAddLeague2)
    {
      //add selected teams to 2. league
      controller.addLeague2Teams(
        listRemainingTeams.getSelectedValues());
    }
    else if (e.getSource() == btnRemainingTeamsAddLeague3)
    {
      //add selected teams to 3. league
      controller.addLeague3Teams(
        listRemainingTeams.getSelectedValues());
    }
    else if (e.getSource() == btnRemainingTeamsAddInternational)
    {
      //add selected teams to international teams
      controller.addInternationalTeams(
        listRemainingTeams.getSelectedValues());
    }
    else if (e.getSource() == btnLeague1TeamsMoveUp)
    {
      //move selected 1. league teams one step up
      int[] selectedIndices = listLeague1Teams.getSelectedIndices();
      controller.moveLeague1TeamsUp(
        listLeague1Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] > 0)
          selectedIndices[i]--;
      listLeague1Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague1TeamsMoveDown)
    {
      //move selected 1. league teams one step down
      int[] selectedIndices = listLeague1Teams.getSelectedIndices();
      controller.moveLeague1TeamsDown(
        listLeague1Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] < listLeague1TeamsModel.size() - 1)
          selectedIndices[i]++;
      listLeague1Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague1TeamsRemove)
    {
      //remove selected teams from 1. league
      controller.removeLeague1Teams(
        listLeague1Teams.getSelectedValues());
    }
    else if (e.getSource() == btnLeague2TeamsMoveUp)
    {
      //move selected 2. league teams one step up
      int[] selectedIndices = listLeague2Teams.getSelectedIndices();
      controller.moveLeague2TeamsUp(
        listLeague2Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] > 0)
          selectedIndices[i]--;
      listLeague2Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague2TeamsMoveDown)
    {
      //move selected 2. league teams one step down
      int[] selectedIndices = listLeague2Teams.getSelectedIndices();
      controller.moveLeague2TeamsDown(
        listLeague2Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] < listLeague2TeamsModel.size() - 1)
          selectedIndices[i]++;
      listLeague2Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague2TeamsRemove)
    {
      //remove selected teams from 2. league
      controller.removeLeague2Teams(
        listLeague2Teams.getSelectedValues());
    }
    else if (e.getSource() == btnLeague3TeamsMoveUp)
    {
      //move selected 3. league teams one step up
      int[] selectedIndices = listLeague3Teams.getSelectedIndices();
      controller.moveLeague3TeamsUp(
        listLeague3Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] > 0)
          selectedIndices[i]--;
      listLeague3Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague3TeamsMoveDown)
    {
      //move selected 3. league teams one step down
      int[] selectedIndices = listLeague3Teams.getSelectedIndices();
      controller.moveLeague3TeamsDown(
        listLeague3Teams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] < listLeague3TeamsModel.size() - 1)
          selectedIndices[i]++;
      listLeague3Teams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnLeague3TeamsRemove)
    {
      //remove selected teams from 3. league
      controller.removeLeague3Teams(
        listLeague3Teams.getSelectedValues());
    }
    else if (e.getSource() == btnInternationalTeamsRemove)
    {
      //remove selected teams from international teams
      controller.removeInternationalTeams(
        listInternationalTeams.getSelectedValues());
    }

  }

  
}