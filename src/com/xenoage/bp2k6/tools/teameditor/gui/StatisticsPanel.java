/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.tools.teameditor.ctrl.StatisticsCtrl;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.*;


/**
 * Class for the team data tab page.
 * 
 * @author awr
 */
public class StatisticsPanel
  extends JPanel
  implements ActionListener
{
  private StatisticsCtrl controller; //the controller for this page
  JCheckBox chkCareer;
  JCheckBox chkWorldCup;
  JScrollPane scrollPane;
  
  JTable table;
  DefaultTableModel tableModel;
  TableColumnModel tableColumnModel;
  
  /**
   * Constructor.
   */
  public StatisticsPanel(MainFrame mainFrame)
  {
    this.setLayout(null);
    
    //show only career teams
    chkCareer = new JCheckBox(Language.get("showcareerteams"));
    chkCareer.setSize(200, 25);
    chkCareer.setLocation(10, 10);
    chkCareer.setSelected(true);
    this.add(chkCareer);
    chkCareer.addActionListener(this);
    
    //show only worldcup teams
    chkWorldCup = new JCheckBox(Language.get("showworldcupteams"));
    chkWorldCup.setSize(200, 25);
    chkWorldCup.setLocation(220, 10);
    chkWorldCup.setSelected(true);
    this.add(chkWorldCup);
    chkWorldCup.addActionListener(this);
    
    tableModel = new DefaultTableModel();
    tableModel.addColumn(Language.get("ranking"));
    tableModel.addColumn(Language.get("teamname"));
    tableModel.addColumn(Language.get("attack"));
    tableModel.addColumn(Language.get("defense"));
    tableModel.addColumn(Language.get("speed"));
    tableModel.addColumn(Language.get("stamina"));
    tableModel.addColumn(Language.get("total"));
    tableModel.addColumn("");
    
    table = new JTable(tableModel);
    table.setBorder(new EtchedBorder());
    table.setRowHeight(20);
    table.getColumnModel().getColumn(0).setPreferredWidth(50);
    table.getColumnModel().getColumn(1).setPreferredWidth(250);
    table.getColumnModel().getColumn(2).setPreferredWidth(50);
    table.getColumnModel().getColumn(3).setPreferredWidth(50);
    table.getColumnModel().getColumn(4).setPreferredWidth(50);
    table.getColumnModel().getColumn(5).setPreferredWidth(50);
    table.getColumnModel().getColumn(6).setPreferredWidth(50);
    table.getColumnModel().getColumn(7).setPreferredWidth(200);
    table.setEnabled(false);
    
    JTableHeader tableHeader = table.getTableHeader();
    tableHeader.setReorderingAllowed(false);
    this.add(tableHeader, BorderLayout.NORTH);
    tableHeader.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        int i = table.columnAtPoint(e.getPoint());
        if (i == 1)
          controller.sortByID();
        else if (i == 2)
          controller.sortByAttack();
        else if (i == 3)
          controller.sortByDefense();
        else if (i == 4)
          controller.sortBySpeed();
        else if (i == 5)
          controller.sortByStamina();
        else if (i == 6)
          controller.sortByTotal();
      }
    });
    
    scrollPane = new JScrollPane(table);
    scrollPane.setSize(
      mainFrame.getWindowWidth() - 20, mainFrame.getWindowHeight() - 50);
    scrollPane.setLocation(10, 45);
    
    this.add(scrollPane);

  }
  
  
  /**
   * Sets the controller for this page.
   * Must be called after the constructor.
   */
  public void setController(StatisticsCtrl controller)
  {
    this.controller = controller;
  }
  
  
  /**
   * Updates the list with the given teams
   * in the given order.
   */
  public void updateList(ArrayList<StatisticsCtrl.TeamRanking> teamsList)
  {
    while (tableModel.getRowCount() > 0)
      tableModel.removeRow(0);
    for (int i = 0; i < teamsList.size(); i++)
      tableModel.addRow(new Object[]
      {
        i + 1,
        teamsList.get(i).id,
        String.valueOf(((int) (teamsList.get(i).attack * 100)) / 100f),
        String.valueOf(((int) (teamsList.get(i).defense * 100)) / 100f),
        String.valueOf(((int) (teamsList.get(i).speed * 100)) / 100f),
        String.valueOf(((int) (teamsList.get(i).stamina * 100)) / 100f),
        String.valueOf(((int) (teamsList.get(i).total * 100)) / 100f)
      });
  }


  /**
   * This method is called when the user
   * clicks a button.
   */
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == chkCareer || e.getSource() == chkWorldCup)
      controller.filterTeams(chkCareer.isSelected(), chkWorldCup.isSelected());
  }
 
  
}
  