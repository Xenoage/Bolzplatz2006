/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.match.PlayerColors;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EtchedBorder;


/**
 * This class contains a panel with
 * controls to edit the colors of a team.
 * 
 * @author awr
 */
public class TeamColorPanel
  extends JPanel
{
  
  private PlayerColors playerColors;
  private boolean updating = true;
  
  JLabel lblPattern;
  JComboBox cmbPattern;
  ArrayList<String> cmbPatternPatterns = new ArrayList<String>();
  
  JLabel lblSkin;
  JTextField txtSkin;
  JLabel lblSkinPreview;
  
  JLabel lblShirt;
  JTextField txtShirt;
  JLabel lblShirtPreview;
  
  JLabel lblShirt2;
  JTextField txtShirt2;
  JLabel lblShirt2Preview;
  
  JLabel lblPants;
  JTextField txtPants;
  JLabel lblPantsPreview;
  
  JLabel lblShoes;
  JTextField txtShoes;
  JLabel lblShoesPreview;
  
  

  public TeamColorPanel()
  {
    this.setLayout(null);
    
    //shirt pattern
    lblPattern = new JLabel(Language.get("pattern") + ":");
    lblPattern.setSize(90, 20);
    lblPattern.setLocation(10, 10);
    this.add(lblPattern);
    cmbPattern = new JComboBox();
    cmbPattern.setSize(140, 20);
    cmbPattern.setLocation(100, 10);
    cmbPattern.addItem(Language.get("pattern_plain"));
    cmbPatternPatterns.add("");
    cmbPattern.addItem(Language.get("pattern_vertical"));
    cmbPatternPatterns.add("vertical");
    cmbPattern.addItem(Language.get("pattern_horizontal"));
    cmbPatternPatterns.add("horizontal");
    cmbPattern.addItem(Language.get("pattern_rednose"));
    cmbPatternPatterns.add("rednose");
    cmbPattern.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        updatePattern();
      }
    });
    this.add(cmbPattern);
    
    //skin
    lblSkin = new JLabel(Language.get("skin") + ":");
    lblSkin.setSize(90, 20);
    lblSkin.setLocation(10, 40);
    this.add(lblSkin);
    txtSkin = new JTextField("FFFFFF");
    txtSkin.setSize(100, 20);
    txtSkin.setLocation(100, 40);
    this.add(txtSkin);
    lblSkinPreview = new JLabel();
    lblSkinPreview.setOpaque(true);
    lblSkinPreview.setBackground(Color.white);
    lblSkinPreview.setBorder(new EtchedBorder());
    lblSkinPreview.setSize(30, 20);
    lblSkinPreview.setLocation(210, 40);
    this.add(lblSkinPreview);
    txtSkin.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        if (!updating)
          updateColor(txtSkin, lblSkinPreview);
      }
    });
    lblSkinPreview.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        showColorChooser(txtSkin, lblSkinPreview);
      }
    });
    
    //shirt
    lblShirt = new JLabel(Language.get("shirt") + ":");
    lblShirt.setSize(90, 20);
    lblShirt.setLocation(10, 70);
    this.add(lblShirt);
    txtShirt = new JTextField("FFFFFF");
    txtShirt.setSize(100, 20);
    txtShirt.setLocation(100, 70);
    this.add(txtShirt);
    lblShirtPreview = new JLabel();
    lblShirtPreview.setOpaque(true);
    lblShirtPreview.setBackground(Color.white);
    lblShirtPreview.setBorder(new EtchedBorder());
    lblShirtPreview.setSize(30, 20);
    lblShirtPreview.setLocation(210, 70);
    this.add(lblShirtPreview);
    txtShirt.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        if (!updating)
          updateColor(txtShirt, lblShirtPreview);
      }
    });
    lblShirtPreview.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        showColorChooser(txtShirt, lblShirtPreview);
      }
    });
    
    //shirt 2
    lblShirt2 = new JLabel(Language.get("shirt2") + ":");
    lblShirt2.setSize(90, 20);
    lblShirt2.setLocation(10, 100);
    this.add(lblShirt2);
    txtShirt2 = new JTextField("FFFFFF");
    txtShirt2.setSize(100, 20);
    txtShirt2.setLocation(100, 100);
    this.add(txtShirt2);
    lblShirt2Preview = new JLabel();
    lblShirt2Preview.setOpaque(true);
    lblShirt2Preview.setBackground(Color.white);
    lblShirt2Preview.setBorder(new EtchedBorder());
    lblShirt2Preview.setSize(30, 20);
    lblShirt2Preview.setLocation(210, 100);
    this.add(lblShirt2Preview);
    txtShirt2.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        if (!updating)
          updateColor(txtShirt2, lblShirt2Preview);
      }
    });
    lblShirt2Preview.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        showColorChooser(txtShirt2, lblShirt2Preview);
      }
    });
    
    //pants
    lblPants = new JLabel(Language.get("pants") + ":");
    lblPants.setSize(90, 20);
    lblPants.setLocation(10, 130);
    this.add(lblPants);
    txtPants = new JTextField("FFFFFF");
    txtPants.setSize(100, 20);
    txtPants.setLocation(100, 130);
    this.add(txtPants);
    lblPantsPreview = new JLabel();
    lblPantsPreview.setOpaque(true);
    lblPantsPreview.setBackground(Color.white);
    lblPantsPreview.setBorder(new EtchedBorder());
    lblPantsPreview.setSize(30, 20);
    lblPantsPreview.setLocation(210, 130);
    this.add(lblPantsPreview);
    txtPants.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        if (!updating)
          updateColor(txtPants, lblPantsPreview);
      }
    });
    lblPantsPreview.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        showColorChooser(txtPants, lblPantsPreview);
      }
    });
    
    //shoes
    lblShoes = new JLabel(Language.get("shoes") + ":");
    lblShoes.setSize(90, 20);
    lblShoes.setLocation(10, 160);
    this.add(lblShoes);
    txtShoes = new JTextField("FFFFFF");
    txtShoes.setSize(100, 20);
    txtShoes.setLocation(100, 160);
    this.add(txtShoes);
    lblShoesPreview = new JLabel();
    lblShoesPreview.setOpaque(true);
    lblShoesPreview.setBackground(Color.white);
    lblShoesPreview.setBorder(new EtchedBorder());
    lblShoesPreview.setSize(30, 20);
    lblShoesPreview.setLocation(210, 160);
    this.add(lblShoesPreview);
    txtShoes.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        updateColor(txtShoes, lblShoesPreview);
      }
    });
    lblShoesPreview.addMouseListener(new MouseAdapter()
    {
      @Override public void mouseClicked(MouseEvent e)
      {
        showColorChooser(txtShoes, lblShoesPreview);
      }
    });
    
    
  }
  
  
  /**
   * Shows the given player colors.
   * The given PlayerColors object is also used
   * to store modified values.
   */
  public void update(PlayerColors playerColors)
  {
    updating = true; //no change events now
    this.playerColors = playerColors;
    selectPattern(playerColors.getPattern());
    txtSkin.setText(ColorTools.getHex(
      playerColors.getColor(PlayerColors.COLOR_SKIN)));
    updateColor(txtSkin, lblSkinPreview);
    txtShirt.setText(ColorTools.getHex(
      playerColors.getColor(PlayerColors.COLOR_SHIRT)));
    updateColor(txtShirt, lblShirtPreview);
    txtShirt2.setText(ColorTools.getHex(
      playerColors.getColor(PlayerColors.COLOR_SHIRT2)));
    updateColor(txtShirt2, lblShirt2Preview);
    txtPants.setText(ColorTools.getHex(
      playerColors.getColor(PlayerColors.COLOR_PANTS)));
    updateColor(txtPants, lblPantsPreview);
    txtShoes.setText(ColorTools.getHex(
      playerColors.getColor(PlayerColors.COLOR_SHOES)));
    updateColor(txtShoes, lblShoesPreview);
    updating = false;
  }
  
  
  /**
   * Selects the item of the patterns list with
   * the given pattern. If not found, the first item
   * (meaning: "plain color") is selected.
   */
  private void selectPattern(String pattern)
  {
    if (pattern != null)
    {
      for (int i = 0; i < cmbPatternPatterns.size(); i++)
      {
        if (cmbPatternPatterns.get(i).equals(pattern))
        {
          cmbPattern.setSelectedIndex(i);
          updatePattern();
          return;
        }
      }
    }
    cmbPattern.setSelectedIndex(0);
    updatePattern();
  }
  
  
  /**
   * Updates the pattern and the
   * enabled status of the shirt2 controls
   * (dependent on the pattern).
   */
  private void updatePattern()
  {
    String pattern;
    if (!updating)
    {
      pattern = cmbPatternPatterns.get(cmbPattern.getSelectedIndex());
      playerColors.setPattern(pattern);
    }
    else
    {
      pattern = playerColors.getPattern();
    }
    boolean shirt2Enabled = (pattern != null &&
      !pattern.equals("") && !pattern.equals("rednose"));
    txtShirt2.setEnabled(shirt2Enabled);
    lblShirt2Preview.setVisible(shirt2Enabled);
  }
  
  
  /**
   * Shows a color choose dialog for the given
   * color textfield and preview box.
   */
  private void showColorChooser(JTextField txt, JLabel preview)
  {
    Color newColor = JColorChooser.showDialog(
      this.getParent().getParent().getParent(),
      Language.get("choosecolor"),
      preview.getBackground());
    if (newColor != null)
    {
      txt.setText(ColorTools.getHex(newColor));
      updateColor(txt, preview);
    }
  }
  
  
  /**
   * Updates the given color preview label with
   * the value from the given text field
   * and updates the color in the teamInfo
   * belonging to this PlayerColors object.
   */
  private void updateColor(JTextField txt, JLabel preview)
  {
    Color c = ColorTools.getColor(txt.getText());
    preview.setBackground(c);
    if (txt == txtSkin)
      playerColors.setColor(PlayerColors.COLOR_SKIN, c);
    else if (txt == txtShirt)
     playerColors.setColor(PlayerColors.COLOR_SHIRT, c);
    else if (txt == txtShirt2)
      playerColors.setColor(PlayerColors.COLOR_SHIRT2, c);
    else if (txt == txtPants)
      playerColors.setColor(PlayerColors.COLOR_PANTS, c);
    else if (txt == txtShoes)
      playerColors.setColor(PlayerColors.COLOR_SHOES, c);
  }
  
}