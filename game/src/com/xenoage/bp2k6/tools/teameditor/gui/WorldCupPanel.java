/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.tools.teameditor.ctrl.WorldCupCtrl;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * Class for the world cup tab page.
 * 
 * @author awr
 */
public class WorldCupPanel
  extends JPanel
  implements ActionListener
{
  private MainFrame mainFrame;
  private WorldCupCtrl controller; //the controller for this page
  
  //main panels
  JPanel panelLeft, panelMiddle, panelRight;
  
  //remaining teams list
  JList listRemainingTeams;
  DefaultListModel listRemainingTeamsModel;
  JButton btnRemainingTeamsAddSelectedTeams;
  
  //worldcup teams list
  JList listWorldCupTeams;
  DefaultListModel listWorldCupTeamsModel;
  JButton btnWorldCupTeamsMoveUp;
  JButton btnWorldCupTeamsMoveDown;
  JButton btnWorldCupTeamsRemoveSelectedTeams;
  JButton btnWorldCupTeamsReal;
  
  //groups
  JLabel lblGroups[] = new JLabel[8];
  JTextArea txtGroups[] = new JTextArea[8];
  
  //warning if less than 32 teams
  JTextArea txtWarning32Teams;
  
  
  /**
   * Constructor.
   */
  public WorldCupPanel(MainFrame mainFrame)
  {
    this.mainFrame = mainFrame; 
    this.setLayout(null);
    
    //left panel
    panelLeft = createPanelRemainingTeamsList();
    this.add(panelLeft);
    
    //vertical seperator
    JLabel seperator = new JLabel();
    seperator.setBorder(new EtchedBorder());
    seperator.setSize(2, mainFrame.getWindowHeight() - 20);
    seperator.setLocation(250, 10);
    this.add(seperator);
    
    //middle panel
    panelMiddle = createPanelWorldCupTeamsList();
    this.add(panelMiddle);
    
    //right panel
    panelRight = createPanelWorldCupGroups();
    this.add(panelRight);

  }
  
  
  /**
   * Sets the controller for this page.
   * Must be called after the constructor.
   */
  public void setController(WorldCupCtrl controller)
  {
    this.controller = controller;
  }
  
  
  /**
   * Create the left bar: Remaining teams list
   */
  private JPanel createPanelRemainingTeamsList()
  {
    JPanel ret = new JPanel(null);
    ret.setSize(220, mainFrame.getWindowHeight() - 20);
    ret.setLocation(10, 10);
    
    //title
    JLabel lblListRemainingTeams = new JLabel(Language.get("remainingteams") + ":");
    lblListRemainingTeams.setSize(ret.getWidth(), 20);
    ret.add(lblListRemainingTeams);
    
    //list of teams
    listRemainingTeamsModel = new DefaultListModel();
    listRemainingTeams = new JList(listRemainingTeamsModel);
    JScrollPane scroll = new JScrollPane(listRemainingTeams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 70);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnRemainingTeamsAddSelectedTeams = new JButton(Language.get("addteams"));
    btnRemainingTeamsAddSelectedTeams.setSize(ret.getWidth(), 24);
    btnRemainingTeamsAddSelectedTeams.setLocation(0, ret.getHeight() - 30);
    ret.add(btnRemainingTeamsAddSelectedTeams);
    btnRemainingTeamsAddSelectedTeams.addActionListener(this);
    
    
    return ret;
  }
  
  
  /**
   * Create the middle bar: WorldCup teams list
   */
  private JPanel createPanelWorldCupTeamsList()
  {
    JPanel ret = new JPanel(null);
    ret.setSize(220, mainFrame.getWindowHeight() - 20);
    ret.setLocation(270, 10);
    
    //title
    JLabel lblListWorldCupTeams = new JLabel(Language.get("worldcupteams") + ":");
    lblListWorldCupTeams.setSize(ret.getWidth(), 20);
    ret.add(lblListWorldCupTeams);
    
    //list of teams
    listWorldCupTeamsModel = new DefaultListModel();
    listWorldCupTeams = new JList(listWorldCupTeamsModel);
    listWorldCupTeams.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        //if (!updating)
        //  controller.teamSelected((String) listTeams.getSelectedValue());
      }
    });
    JScrollPane scroll = new JScrollPane(listWorldCupTeams);
    scroll.setSize(ret.getWidth(), ret.getHeight() - 160);
    scroll.setLocation(0, 30);
    ret.add(scroll);
    
    //create buttons
    btnWorldCupTeamsMoveUp = new JButton(Language.get("moveup"));
    btnWorldCupTeamsMoveUp.setSize(ret.getWidth(), 24);
    btnWorldCupTeamsMoveUp.setLocation(0, ret.getHeight() - 120);
    ret.add(btnWorldCupTeamsMoveUp);
    btnWorldCupTeamsMoveUp.addActionListener(this);
    
    btnWorldCupTeamsMoveDown = new JButton(Language.get("movedown"));
    btnWorldCupTeamsMoveDown.setSize(ret.getWidth(), 24);
    btnWorldCupTeamsMoveDown.setLocation(0, ret.getHeight() - 90);
    ret.add(btnWorldCupTeamsMoveDown);
    btnWorldCupTeamsMoveDown.addActionListener(this);
    
    btnWorldCupTeamsRemoveSelectedTeams = new JButton(Language.get("removeteams"));
    btnWorldCupTeamsRemoveSelectedTeams.setSize(ret.getWidth(), 24);
    btnWorldCupTeamsRemoveSelectedTeams.setLocation(0, ret.getHeight() - 60);
    ret.add(btnWorldCupTeamsRemoveSelectedTeams);
    btnWorldCupTeamsRemoveSelectedTeams.addActionListener(this);
    
    btnWorldCupTeamsReal = new JButton(Language.get("realworldcupteams"));
    btnWorldCupTeamsReal.setSize(ret.getWidth(), 24);
    btnWorldCupTeamsReal.setLocation(0, ret.getHeight() - 30);
    ret.add(btnWorldCupTeamsReal);
    btnWorldCupTeamsReal.addActionListener(this);
    
    
    return ret;
  }
  
  /**
   * Create the right bar: World Cup groups
   */
  private JPanel createPanelWorldCupGroups()
  {
    JPanel ret = new JPanel(null);
    ret.setSize(450, mainFrame.getWindowHeight() - 20);
    ret.setLocation(530, 10);
    
    //groups
    for (int iy = 0; iy < 4; iy++)
    {
      for (int ix = 0; ix < 2; ix++)
      {
        int group = iy * 2 + ix;
        char groupNumber;
        switch (group)
        {
          case 0: groupNumber = 'A'; break;
          case 1: groupNumber = 'B'; break;
          case 2: groupNumber = 'C'; break;
          case 3: groupNumber = 'D'; break;
          case 4: groupNumber = 'E'; break;
          case 5: groupNumber = 'F'; break;
          case 6: groupNumber = 'G'; break;
          default: groupNumber = 'H'; break;
        }
        lblGroups[group] = new JLabel(Language.get("group") + " " + groupNumber);
        lblGroups[group].setSize(ret.getWidth() / 2, 20);
        lblGroups[group].setLocation(ix * ret.getWidth() / 2, iy * 150 + 30);
        ret.add(lblGroups[group]);
        txtGroups[group] = new JTextArea();
        txtGroups[group].setSize(ret.getWidth() / 2, 80);
        txtGroups[group].setLocation(ix * ret.getWidth() / 2, iy * 150 + 50);
        txtGroups[group].setOpaque(false);
        txtGroups[group].setEditable(false);
        ret.add(txtGroups[group]);
      }
    }
    
    txtWarning32Teams = new JTextArea(Language.get("warning_32teams"));
    txtWarning32Teams.setForeground(Color.red);
    txtWarning32Teams.setOpaque(false);
    txtWarning32Teams.setEditable(false);
    txtWarning32Teams.setWrapStyleWord(true);
    txtWarning32Teams.setLineWrap(true);
    txtWarning32Teams.setSize(ret.getWidth(), 40);
    txtWarning32Teams.setLocation(0, ret.getHeight() - 50);
    ret.add(txtWarning32Teams);
    
    
    return ret;
  }
  
  
  /**
   * Fills the teams list with the given team ids.
   * The remaining teams list should be sorted alphabetically.
   */
  public void updateTeamsList(ArrayList<String> remainingTeams,
    ArrayList<String> worldCupTeams)
  {
    listRemainingTeamsModel.clear();
    for (int i = 0; i < remainingTeams.size(); i++)
      listRemainingTeamsModel.addElement(remainingTeams.get(i));
    listWorldCupTeamsModel.clear();
    for (int i = 0; i < worldCupTeams.size(); i++)
      listWorldCupTeamsModel.addElement(worldCupTeams.get(i));
    updateGroups();
    //show warning if less than 32 teams
    txtWarning32Teams.setVisible(worldCupTeams.size() < 32);
  }


  /**
   * This method is called when the user clicks
   * a button.
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == btnRemainingTeamsAddSelectedTeams)
    {
      //add selected teams to world cup
      controller.addWorldCupTeams(
        listRemainingTeams.getSelectedValues());
    }
    else if (e.getSource() == btnWorldCupTeamsMoveUp)
    {
      //move selected teams one step up
      int[] selectedIndices = listWorldCupTeams.getSelectedIndices();
      controller.moveWorldCupTeamsUp(
        listWorldCupTeams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] > 0)
          selectedIndices[i]--;
      listWorldCupTeams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnWorldCupTeamsMoveDown)
    {
      //move selected teams one step down
      int[] selectedIndices = listWorldCupTeams.getSelectedIndices();
      controller.moveWorldCupTeamsDown(
        listWorldCupTeams.getSelectedValues());
      //still select the same teams
      for (int i = 0; i < selectedIndices.length; i++)
        if (selectedIndices[i] < listWorldCupTeamsModel.size() - 1)
          selectedIndices[i]++;
      listWorldCupTeams.setSelectedIndices(selectedIndices);
    }
    else if (e.getSource() == btnWorldCupTeamsRemoveSelectedTeams)
    {
      //remove selected teams from world cup
      controller.removeWorldCupTeams(
        listWorldCupTeams.getSelectedValues());
    }
    else if (e.getSource() == btnWorldCupTeamsReal)
    {
      //load real World Cup 2006 teams
      controller.loadRealWorldCupTeams();
    }
  }
  
  
  /**
   * Updates the groups.
   */
  private void updateGroups()
  {
    for (int iy = 0; iy < 4; iy++)
    {
      for (int ix = 0; ix < 2; ix++)
      {
        int group = iy * 2 + ix;
        String text = "";
        for (int iTeam = 0; iTeam < 4; iTeam++)
          if (listWorldCupTeamsModel.size() > group * 4 + iTeam)
            text += listWorldCupTeamsModel.getElementAt(group * 4 + iTeam) + "\n";
        txtGroups[group].setText(text);
      }
    }
  }
  
  
  
  
  

  
}