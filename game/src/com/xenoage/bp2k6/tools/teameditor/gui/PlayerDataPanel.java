/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.teameditor.gui;

import com.xenoage.bp2k6.match.characters.PlayerInfo;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * This class contains a panel with
 * controls to edit the data
 * of a player.
 * 
 * @author awr
 */
public class PlayerDataPanel
  extends JPanel
  implements ChangeListener
{
  
  private PlayerInfo playerInfo;
  private boolean updating = true;
  
  JLabel lblName;
  JTextField txtName;
  JLabel lblAttack;
  JSlider slideAttack;
  JLabel lblDefense;
  JSlider slideDefense;
  JLabel lblSpeed;
  JSlider slideSpeed;
  JLabel lblStamina;
  JSlider slideStamina;
  JLabel lblHeaddress;
  JComboBox cmbHeaddress;
  ArrayList<String> cmbHeaddressIDs = new ArrayList<String>();
  JCheckBox chkGlasses;

  public PlayerDataPanel()
  {
    this.setLayout(null);
    
    //name
    lblName = new JLabel(Language.get("playername") + ":");
    lblName.setSize(150, 20);
    lblName.setLocation(10, 10);
    this.add(lblName);
    txtName = new JTextField("?");
    txtName.setSize(240, 20);
    txtName.setLocation(150, 10);
    this.add(txtName);
    txtName.addKeyListener(new KeyAdapter()
    {
      @Override public void keyReleased(KeyEvent e)
      {
        if (!updating)
          playerInfo.setName(txtName.getText());
      }
    });
    
    //attack
    lblAttack = new JLabel(Language.get("attack") + ":");
    lblAttack.setSize(150, 20);
    lblAttack.setLocation(10, 50);
    this.add(lblAttack);
    slideAttack = new JSlider(0, 10, 0);
    slideAttack.setMajorTickSpacing(2);
    slideAttack.setMinorTickSpacing(1);
    slideAttack.setPaintTicks(true);
    slideAttack.setPaintLabels(false);
    slideAttack.setSize(240, 50);
    slideAttack.setLocation(150, 30);
    this.add(slideAttack);
    slideAttack.addChangeListener(this);
    
    //defense
    lblDefense = new JLabel(Language.get("defense") + ":");
    lblDefense.setSize(150, 20);
    lblDefense.setLocation(10, 90);
    this.add(lblDefense);
    slideDefense = new JSlider(0, 10, 0);
    slideDefense.setMajorTickSpacing(2);
    slideDefense.setMinorTickSpacing(1);
    slideDefense.setPaintTicks(true);
    slideDefense.setPaintLabels(false);
    slideDefense.setSize(240, 50);
    slideDefense.setLocation(150, 70);
    this.add(slideDefense);
    slideDefense.addChangeListener(this);
    
    //speed
    lblSpeed = new JLabel(Language.get("speed") + ":");
    lblSpeed.setSize(150, 20);
    lblSpeed.setLocation(10, 130);
    this.add(lblSpeed);
    slideSpeed = new JSlider(0, 10, 0);
    slideSpeed.setMajorTickSpacing(2);
    slideSpeed.setMinorTickSpacing(1);
    slideSpeed.setPaintTicks(true);
    slideSpeed.setPaintLabels(false);
    slideSpeed.setSize(240, 50);
    slideSpeed.setLocation(150, 110);
    this.add(slideSpeed);
    slideSpeed.addChangeListener(this);
    
    //stamina
    lblStamina = new JLabel(Language.get("stamina") + ":");
    lblStamina.setSize(150, 20);
    lblStamina.setLocation(10, 170);
    this.add(lblStamina);
    slideStamina = new JSlider(0, 10, 0);
    slideStamina.setMajorTickSpacing(2);
    slideStamina.setMinorTickSpacing(1);
    slideStamina.setPaintTicks(true);
    slideStamina.setPaintLabels(true);
    slideStamina.setSize(240, 50);
    slideStamina.setLocation(150, 155);
    this.add(slideStamina);
    slideStamina.addChangeListener(this);
    
    //Headdress
    lblHeaddress = new JLabel(Language.get("Headdress") + ":");
    lblHeaddress.setSize(150, 20);
    lblHeaddress.setLocation(10, 210);
    this.add(lblHeaddress);
    cmbHeaddress = new JComboBox();
    cmbHeaddress.setSize(120, 20);
    cmbHeaddress.setLocation(150, 210);
    cmbHeaddress.addItem(Language.get("noHeaddress"));
    cmbHeaddressIDs.add("none");
    cmbHeaddress.addItem(Language.get("cap"));
    cmbHeaddressIDs.add("cap");
    cmbHeaddress.addItem(Language.get("bobblecap"));
    cmbHeaddressIDs.add("bobblecap");
    cmbHeaddress.addItem(Language.get("ted"));
    cmbHeaddressIDs.add("ted");
    cmbHeaddress.addItem(Language.get("iro"));
    cmbHeaddressIDs.add("iro");
    cmbHeaddress.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          updateHeaddress();
        }
      });
    this.add(cmbHeaddress);
    
    //glasses
    chkGlasses = new JCheckBox(Language.get("glasses"));
    chkGlasses.setSize(100, 20);
    chkGlasses.setLocation(280, 210);
    chkGlasses.addChangeListener(new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          if (!updating)
          {
            if (chkGlasses.isSelected())
              playerInfo.setGlasses("glasses");
            else
              playerInfo.setGlasses(null);
          }
        }
      });
    this.add(chkGlasses);

  }
  
  
  /**
   * Shows the given player.
   * The given PlayerInfo object is also used
   * to store modified values.
   */
  public void update(PlayerInfo playerInfo)
  {
    this.playerInfo = playerInfo;
    updating = true; //no change events now
    txtName.setText(playerInfo.getName());
    slideAttack.setValue(playerInfo.getAttackStart());
    slideDefense.setValue(playerInfo.getDefenseStart());
    slideSpeed.setValue(playerInfo.getSpeedStart());
    slideStamina.setValue(playerInfo.getStamina());
    if (playerInfo.getHeaddress().length() > 0)
      selectHeaddress(playerInfo.getHeaddress());
    else
      selectHeaddress("cap");
    chkGlasses.setSelected(
      playerInfo.getGlasses() != null &&
      playerInfo.getGlasses().equals("glasses"));
    updating = false;
  }


  /**
   * This method is called when a slider
   * changes its value.
   */
  public void stateChanged(ChangeEvent e)
  {
    if (!updating)
    {
      if (e.getSource() == slideAttack)
        playerInfo.setAttackStart(slideAttack.getValue());
      else if (e.getSource() == slideDefense)
        playerInfo.setDefenseStart(slideDefense.getValue());
      else if (e.getSource() == slideSpeed)
        playerInfo.setSpeedStart(slideSpeed.getValue());
      else if (e.getSource() == slideStamina)
        playerInfo.setStamina(slideStamina.getValue());
    }
  }
  
  
  /**
   * Selects the item of the Headdress list with
   * the given Headdress. If not found, the first item
   * (meaning: "no heardress") is selected.
   */
  private void selectHeaddress(String Headdress)
  {
    if (Headdress != null)
    {
      for (int i = 0; i < cmbHeaddressIDs.size(); i++)
      {
        if (cmbHeaddressIDs.get(i).equals(Headdress))
        {
          cmbHeaddress.setSelectedIndex(i);
          updateHeaddress();
          return;
        }
      }
    }
    cmbHeaddress.setSelectedIndex(0);
    updateHeaddress();
  }
  
  
  /**
   * Updates the Headdress.
   */
  private void updateHeaddress()
  {
    if (!updating)
    {
      playerInfo.setHeaddress(
        cmbHeaddressIDs.get(cmbHeaddress.getSelectedIndex()));
    }
  }
  
}
