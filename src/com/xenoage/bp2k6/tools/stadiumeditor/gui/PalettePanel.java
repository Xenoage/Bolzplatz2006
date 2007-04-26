/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor.gui;

import com.xenoage.bp2k6.match.stadium.*;
import com.xenoage.bp2k6.tools.stadiumeditor.StadiumEditor;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.border.*;


/**
 * This class contains the palette bar
 * of the stadium editor.
 * 
 * @author Andreas Wenger
 */
public class PalettePanel
  extends JPanel
{
  
  //the main frame and the editor main class
  MainFrame mainFrame = null;
  StadiumEditor editor = null;
  
  //the parent frame
  JFrame parentFrame;
  
  //panels
  JTabbedPane tabPaneModus;
  JPanel tabStadium;
  JTextField txtStadiumName;
  JLabel lblSpectatorsCountValue;
  JPanel tabStandsContentPanel;
  JLabel lblTerrainIDValue;
  JButton lstProperties;
  JPanel tabGoals;
  JPanel tabLawn;
  JPanel tabScoreboard;
  JPanel tabFloodlight;
  JPanel tabAttractions;
  JPanel tabObjects;
  
  //gaps between components
  int gapSmall = 5;
  int gapLarge = 10;
  
  
  /**
   * Constructor.
   */
  public PalettePanel(MainFrame mainFrame, StadiumEditor editor, JFrame parentFrame)
  {
    this.mainFrame = mainFrame;
    this.editor = editor;
    this.parentFrame = parentFrame;
    
    BorderLayout panelLeftLayout = new BorderLayout(5, 5);
    this.setLayout(panelLeftLayout);
    this.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.setSize(200, 200);
    //create modus pane and add tabs
    tabPaneModus = new JTabbedPane();
    tabPaneModus.addTab(Language.get("stadium"), createTabStadium());
    tabPaneModus.addTab(Language.get("stands"), createTabStands());
    tabPaneModus.addTab(Language.get("adboards"), createTabAdboards());
    tabPaneModus.addTab(Language.get("goals"), createTabGoals());
    tabPaneModus.addTab(Language.get("lawn"), createTabLawn());
    tabPaneModus.addTab(Language.get("scoreboard"), createTabScoreboard());
    tabPaneModus.addTab(Language.get("floodlight"), createTabFloodlight());
    tabPaneModus.addTab(Language.get("attractions"), createTabAttractions());
    tabPaneModus.addTab(Language.get("objects"), createTabObjects());
    tabPaneModus.addTab(Language.get("terrain"), createTabTerrain());
    tabPaneModus.setSelectedIndex(1);
    this.add(tabPaneModus, BorderLayout.CENTER);
  }
  
  
  /**
   * Creates the tab page for general stadium information.
   */
  private JPanel createTabStadium()
  {
    JPanel ret = new JPanel();
    JPanel content = new JPanel();
    
    BoxLayout boxLayout = new BoxLayout(content, BoxLayout.Y_AXIS);
    content.setLayout(boxLayout);
    
    JLabel lblStadiumName = new JLabel(Language.get("stadiumname"));
    content.add(lblStadiumName);
    content.add(Box.createRigidArea(new Dimension(0, gapSmall)));
    
    txtStadiumName = new JTextField(Language.get("unnamedstadium"));
    content.add(txtStadiumName);
    content.add(Box.createRigidArea(new Dimension(0, gapLarge)));
    
    JLabel lblSpectatorsCount = new JLabel(Language.get("numberofspectators"));
    content.add(lblSpectatorsCount);
    content.add(Box.createRigidArea(new Dimension(0, gapSmall)));
    
    lblSpectatorsCountValue = new JLabel("0");
    content.add(lblSpectatorsCountValue);
    content.add(Box.createRigidArea(new Dimension(0, gapLarge)));
    
    ret.add(content);
    return ret;
  }
  
  
  /**
   * Creates the tab page for adboards.
   */
  private JPanel createTabAdboards()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    JPanel content = new JPanel();
    ret.add(content);
    content.setLayout(new GridLayout(4, 1, 0, 5));
    content.add(createAdboardButton(
      "adboard1", "Vereinslogo-Bande"));
    content.add(createAdboardButton(
      "adboard2", "Werbung 1"));
    content.add(createAdboardButton(
      "adboard3", "Werbung 2"));
    content.add(createAdboardButton(
      "adboard4", "Werbung 3"));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for other stadium objects.
   */
  private JPanel createTabObjects()
  {
    JPanel ret = new JPanel();
    ret.setLayout(new BorderLayout());
    JPanel content = new JPanel();
    content.setBorder(new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    content.setLayout(new GridLayout(1, 1, 0, gapSmall));
    
    JButton btnAddObject = new JButton(Language.get("addobject..."));
    btnAddObject.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JFileChooser dlgOpen = new JFileChooser();
        dlgOpen.setCurrentDirectory(new java.io.File("data/meshes/"));
        dlgOpen.addChoosableFileFilter(FileUtils.get3DObjectFilter());
        int ret = dlgOpen.showOpenDialog(parentFrame);
        if (ret == JFileChooser.APPROVE_OPTION)
        {
          String id = dlgOpen.getSelectedFile().getName();
          if (new File("data/meshes/" + id).exists())
          {
            editor.addNewObject(id);
            if (!editor.isOSWindows())
              mainFrame.requestFocus(); //focus the main frame for keyboard input
          }
          else
            showMessageBox(
              Language.get("3dobjectmustbeinmeshesdirectory"), true);
        }
      }
    });
    content.add(btnAddObject);

    ret.add(content, BorderLayout.NORTH);
    return ret;
  }
  
  
  /**
   * Creates the tab page for general stadium information.
   */
  private JPanel createTabTerrain()
  {
    JPanel ret = new JPanel();
    ret.setLayout(new BorderLayout());
    JPanel content = new JPanel();
    content.setBorder(new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    content.setLayout(new GridLayout(4, 1, 0, gapSmall));
    
    JLabel lblTerrainID = new JLabel("Aktuelles Terrain:");
    content.add(lblTerrainID);
    
    lblTerrainIDValue = new JLabel("Kein Terrain");
    content.add(lblTerrainIDValue);
    
    JButton btnTerrainIDLoad = new JButton("Terrain laden...");
    btnTerrainIDLoad.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        JFileChooser dlgOpen = new JFileChooser();
        dlgOpen.setCurrentDirectory(new java.io.File("data/terrains/"));
        dlgOpen.addChoosableFileFilter(FileUtils.getTerrainFilter());
        int ret = dlgOpen.showOpenDialog(parentFrame);
        if (ret == JFileChooser.APPROVE_OPTION)
        {
          String id = FileUtils.getNameWithoutExtension(dlgOpen.getSelectedFile());
          if (new java.io.File("data/terrains/" + id + ".xml").exists())
          {
            editor.setTerrain(id);
            if (!editor.isOSWindows())
              mainFrame.requestFocus(); //focus the main frame for keyboard input
          }
          else
          {
            showMessageBox(
              "Invalid file or directory.\nUse only the given terrain directory!", true);
          }
        }
      }
    });
    content.add(btnTerrainIDLoad);
    
    JButton btnTerrainIDDelete = new JButton("Kein Terrain");
    btnTerrainIDDelete.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.setTerrain(null);
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    content.add(btnTerrainIDDelete);
    
    ret.add(content, BorderLayout.NORTH);
    return ret;
  }
  
  
  /**
   * Creates the tab page for stands.
   */
  private JPanel createTabStands()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    //3 panels for the 3 stand stages
    tabStandsContentPanel = new JPanel(new CardLayout());
    JPanel cardStage1 = new JPanel();
    JPanel cardStage2 = new JPanel();
    JPanel cardStage3 = new JPanel();
    tabStandsContentPanel.add(cardStage1, "Stufe 1");
    tabStandsContentPanel.add(cardStage2, "Stufe 2");
    tabStandsContentPanel.add(cardStage3, "Stufe 3");
    ret.add(tabStandsContentPanel, BorderLayout.CENTER);
    
    //create card layout functionality
    JPanel comboBoxPane = new JPanel(new GridLayout(1, 1, 0, 0));
    comboBoxPane.setBorder(new EmptyBorder(0, 0, gapLarge, 0));
    String comboBoxItems[] = { "Stufe 1", "Stufe 2", "Stufe 3" };
    JComboBox cb = new JComboBox(comboBoxItems);
    cb.setEditable(false);
    cb.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent evt)
      {
        CardLayout cl = (CardLayout)(tabStandsContentPanel.getLayout());
        cl.show(tabStandsContentPanel, (String)evt.getItem());
      }
    });
    comboBoxPane.add(cb);
    ret.add(comboBoxPane, BorderLayout.NORTH);
    
    //fill stage 1 panel
    cardStage1.setLayout(new GridLayout(3, 1, 0, 5));
    cardStage1.add(createStandButton(
      "stage1_normal", "Gerade (20x10m)"));
    cardStage1.add(createStandButton(
      "stage1_curve", "Kurve (10x10m)"));
    cardStage1.add(createStandButton(
      "stage1_completion", "Abschluss (10x20m)"));
    
    //fill stage 2 panel
    cardStage2.setLayout(new GridLayout(3, 1, 0, 5));
    cardStage2.add(createStandButton(
      "stage2_normal", "Gerade (20x10m)"));
    cardStage2.add(createStandButton(
      "stage2_curve", "Kurve (20x20m)"));
    cardStage2.add(createStandButton(
      "stage2_completion", "Abschluss (10x20m)"));

    //fill stage 3 panel
    cardStage3.setLayout(new GridLayout(3, 1, 0, 5));
    cardStage3.add(createStandButton(
      "stage3_normal", "Gerade (20x10m)"));
    cardStage3.add(createStandButton(
      "stage3_curve", "Kurve (30x30m)"));
    cardStage3.add(createStandButton(
      "stage3_completion", "Abschluss (10x30m)"));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for goals.
   */
  private JPanel createTabGoals()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
   
    ret.setLayout(new GridLayout(3, 1, 0, 5));
    ret.add(createGoalsButton(
      "1", Language.get("goal1")));
    ret.add(createGoalsButton(
      "2", Language.get("goal2")));
    ret.add(createGoalsButton(
      "3", Language.get("goal3")));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for lawn.
   */
  private JPanel createTabLawn()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
   
    ret.setLayout(new GridLayout(4, 1, 0, 5));
    ret.add(createLawnButton(
      "lawn1", Language.get("lawn_mudfield")));
    ret.add(createLawnButton(
      "lawn2", Language.get("lawn_grassland")));
    ret.add(createLawnButton(
      "lawn3", Language.get("lawn_tendedlawn")));
    ret.add(createLawnButton(
      "lawn4", Language.get("lawn_sod")));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for scoreboards
   */
  private JPanel createTabScoreboard()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    JPanel content = new JPanel();
    ret.add(content);
    content.setLayout(new GridLayout(3, 1, 0, 5));
    content.add(createScoreboardButton(
      "1", Language.get("scoreboard1")));
    content.add(createScoreboardButton(
      "2", Language.get("scoreboard2")));
    content.add(createScoreboardButton(
      "3", Language.get("scoreboard3")));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for floodlight towers.
   */
  private JPanel createTabFloodlight()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    JPanel content = new JPanel();
    ret.add(content);
    content.setLayout(new GridLayout(3, 1, 0, 5));
    content.add(createFloodlightButton(
      "1", Language.get("floodlight1")));
    content.add(createFloodlightButton(
      "2", Language.get("floodlight2")));
    content.add(createFloodlightButton(
      "3", Language.get("floodlight3")));
    
    return ret;
  }
  
  
  /**
   * Creates the tab page for attractions.
   */
  private JPanel createTabAttractions()
  {
    JPanel ret = new JPanel(new BorderLayout());
    ret.setBorder(
      new EmptyBorder(gapLarge, gapLarge, gapLarge, gapLarge));
    
    JPanel content = new JPanel();
    ret.add(content);
    content.setLayout(new GridLayout(5, 1, 0, 5));
    content.add(createAttractionButton(
      "hotdogstand", Language.get("hotdogstand")));
    content.add(createAttractionButton(
      "bettingoffice", Language.get("bettingoffice")));
    content.add(createAttractionButton(
      "beerbar", Language.get("beerbar")));
    content.add(createAttractionButton(
      "track", Language.get("track")));
    content.add(createAttractionButton(
      "highjump", Language.get("highjump")));
    
    return ret;
  }
  
  
  /**
   * Creates a button for a stand
   */
  private JButton createStandButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.addNewStand(e.getActionCommand());
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for an adboard
   */
  private JButton createAdboardButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.addNewAdboard(e.getActionCommand());
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for goals.
   */
  private JButton createGoalsButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/goal" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.setGoals(Integer.parseInt(e.getActionCommand()));
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for a lawn
   */
  private JButton createLawnButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.setLawn(e.getActionCommand());
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for a scoreboard.
   */
  private JButton createScoreboardButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/scoreboard" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.setScoreboard(Integer.parseInt(e.getActionCommand()));
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for a floodlight tower.
   */
  private JButton createFloodlightButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/floodlight" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.addNewFloodlight(Integer.parseInt(e.getActionCommand()));
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Creates a button for an attraction.
   */
  private JButton createAttractionButton(String id, String description)
  {
    JButton ret = new JButton(description,
      new ImageIcon("data/images/stadiumeditor/" + id + ".png"));
    ret.setVerticalTextPosition(AbstractButton.BOTTOM);
    ret.setHorizontalTextPosition(AbstractButton.CENTER);
    ret.setActionCommand(id);
    ret.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        editor.addNewAttraction(e.getActionCommand());
        if (!editor.isOSWindows())
          mainFrame.requestFocus(); //focus the main frame for keyboard input
      }
    });
    return ret;
  }
  
  
  /**
   * Call this method to fill all controls and
   * the workspace with the information of the
   * current stadium object.
   */
  public void updateStadiumData()
  {
    Stadium stadium = editor.getStadium();
    //terrain
    if (stadium.getTerrain() != null)
      lblTerrainIDValue.setText(stadium.getTerrain().getID());
    else
      lblTerrainIDValue.setText("Kein Terrain"); 
    //stadium info
    txtStadiumName.setText(stadium.getStadiumInfo().getName());
    lblSpectatorsCountValue.setText(String.valueOf(
      stadium.getStadiumInfo().getMaxSpectators()));
  }
  
  
  private void showMessageBox(String text, boolean warning)
  {
    JOptionPane.showMessageDialog(this, text,
      Language.get("stadiumeditor_title"),
      (warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE));
  }
  
  
  /**
   * Gets the name of the stadium.
   */
  public String getStadiumName()
  {
    return txtStadiumName.getText();
  }

  
  
}
