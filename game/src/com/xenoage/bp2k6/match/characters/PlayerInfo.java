/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */
package com.xenoage.bp2k6.match.characters;

import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.XMLWriter;

import org.w3c.dom.Node;


/**
 * This class stores information about a player
 * (name, values, ...)
 * The values of the player are explained in
 * the design doc section "Werte der Spieler".
 *
 * @author Andi
 */
public class PlayerInfo
{
  private String name = "";
  private int attack;
  private int defense;
  private int speed;
  private int stamina;
  private String headdress;
  private String glasses;
  private float energy = 1f;

  public enum EnergyEvent
  {
    SPRINT(-1f), //special algorithm, see energyEvent()
    TACKLE(-0.02f), //tackle
    KNOCKEDDOWN(-0.1f), //player gets knocked down by another one
    STUMBLE(-0.1f), //player stumbled, e.g. over a molehill
    HALFTIME(0.3f); //recovery in half-time break
    private float energy;
    EnergyEvent(float energy)
    {
      this.energy = energy;
    }
    public float getEnergy()
    {
      return energy;
    }
  }
  
  private static final float energyLossFactor = 0.7f;


  /**
   * Constructor. Set name and start values.
   */
  public PlayerInfo(String name, int attack, int defense,
    int speed, int stamina, String headdress, String glasses)
  {
    this.name = name;
    this.attack = attack;
    this.defense = defense;
    this.speed = speed;
    this.stamina = stamina;
    this.headdress = headdress;
    this.glasses = glasses;
  }

  /**
   * Gets the name of the player.
   */
  public String getName()
  {
    return name;
  }
  
  /**
   * Sets the name of the player.
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Gets the start attack value of the player.
   */
  public int getAttackStart()
  {
    return attack;
  }
  
  /**
   * Sets the start attack value of the player.
   */
  public void setAttackStart(int attack)
  {
    this.attack = MathTools.clamp(attack, 0, 10);
  }
  
  /**
   * Gets the current energy-dependent attack
   * value of the player.
   */
  public float getAttack()
  {
    return attack * computeEnergyFactor();
  }

  /**
   * Gets the start defense value of the player.
   */
  public int getDefenseStart()
  {
    return defense;
  }
  
  /**
   * Sets the start defense value of the player.
   */
  public void setDefenseStart(int defense)
  {
    this.defense = MathTools.clamp(defense, 0, 10);
  }
  
  /**
   * Gets the current energy-dependent defense
   * value of the player.
   */
  public float getDefense()
  {
    return defense * computeEnergyFactor();
  }

  /**
   * Gets the start speed value of the player.
   */
  public int getSpeedStart()
  {
    return speed;
  }
  
  /**
   * Sets the start speed value of the player.
   */
  public void setSpeedStart(int speed)
  {
    this.speed = MathTools.clamp(speed, 0, 10);
  }
  
  /**
   * Gets the stamina value of the player.
   */
  public int getStamina()
  {
    return stamina;
  }
  
  /**
   * Sets the stamina value of the player.
   */
  public void setStamina(int stamina)
  {
    this.stamina = MathTools.clamp(stamina, 0, 10);
  }
  
  /**
   * Gets the hairdress of the player,
   * or <code>""</code>.
   */
  public String getHeaddress()
  {
    return headdress;
  }
  
  /**
   * Sets the hairdress of the player,
   * or <code>""</code>.
   */
  public void setHeaddress(String hairdress)
  {
    if (hairdress == null || hairdress.length() == 0)
      this.headdress = "";
    else
      this.headdress = hairdress;
  }
  
  /**
   * Gets the glasses of the player,
   * or <code>""</code>.
   */
  public String getGlasses()
  {
    return glasses;
  }
  
  /**
   * Sets the glasses of the player,
   * or <code>null</code>.
   */
  public void setGlasses(String glasses)
  {
    if (glasses == null || glasses.length() == 0)
      this.glasses = "";
    else
      this.glasses = glasses;
  }

  /**
   * Gets the current energy value of the player.
   */
  public float getEnergy()
  {
    return energy;
  }

  /**
   * Gets the current energy as a integer value
   * between 0 and 10.
   */
  public int getEnergy0to10()
  {
    if (energy < 0.01f)
      return 0;
    else
      return (int) (energy * 10 + 0.4f);
  }


  /**
   * Call this method when any event happens that is connected
   * with a loss of energy.
   * The <code>time</code>-parameter is only used for
   * sprinting. It stands for the time the player sprinted
   * (in relative team, that means sprint duration / match duration)
   */
  public void energyEvent(EnergyEvent event, float time)
  {
    float e = 0;
    if (event == EnergyEvent.SPRINT)
    {
      //special formula for sprint
      e = -time * (100 / (20 + this.stamina * 6));
    }
    else
    {
      //stamina-dependent energy value
      e = event.getEnergy() * (1.5f - this.stamina * 0.1f);
    }
    if (e < 0)
      decreaseEnergy(-e);
    else
      increaseEnergy(e);
  }

  /**
   * Decreases the energy value of the player. Call this method,
   * when a player is involved in an action that cost energy
   * (e.g. when he is sprinting or when he is knocked down).
   * The given value must be already stamina dependent.
   */
  private void decreaseEnergy(float energy)
  {
    this.energy -= energy * energyLossFactor; 
    if (this.energy < 0f) this.energy = 0f;
  }

  /**
   * Increase the energy value of the player, e.g.
   * in the half-time break.
   * The given value is not stamina-dependent.
   * Attention: Use energyEvent() whenever possible, do not
   * modify the energy directly.
   */
  private void increaseEnergy(float energy)
  {
    this.energy += energy * energyLossFactor;
    if (this.energy > 1f) this.energy = 1f;
  }

  /**
   * Computes the energy-dependent factor between
   * 0.5 and 1, that must be multiplied to
   * the attack, defense and speed values to get
   * the current values.
   */
  private float computeEnergyFactor()
  {
    return (0.6f + energy * 0.4f);
  }


  /**
   * Computes the current speed of the player, based
   * on the values speed and energy (class internal)
   * and the given value of a player's normal speed
   * in m/s.
   */
  public float computeSpeed(float normalSpeed)
  {
    return normalSpeed * (speed * 0.04f + 0.80f) * computeEnergyFactor();
  }
  
  
  /**
   * Compares this PlayerInfo object with another one and
   * returns <code>true</code> if their contents are
   * completely equal, otherwise <code>false</code>.
   */
  public boolean equals(PlayerInfo other)
  {
    return
      name.equals(other.getName()) &&
      attack == other.getAttackStart() &&
      defense == other.getDefenseStart() &&
      speed == other.getSpeedStart() &&
      stamina == other.getStamina() &&
      headdress.equals(other.getHeaddress()) &&
      glasses.equals(other.getGlasses()) &&
      energy == other.getEnergy();
  }
  
  
  /**
   * Save this PlayerInfo object within the given
   * XML element. A new child element "player"
   * with all information about the player
   * is created (but without the attribute "energy").
   */
  public void saveToXML(Node parentElement)
  {
    //create goal element
    Node ePlayer = XMLWriter.addElement("player", parentElement);
    XMLWriter.addAttribute(ePlayer, "name", name);
    XMLWriter.addAttribute(ePlayer, "attack", String.valueOf(attack));
    XMLWriter.addAttribute(ePlayer, "defense", String.valueOf(defense));
    XMLWriter.addAttribute(ePlayer, "speed", String.valueOf(speed));
    XMLWriter.addAttribute(ePlayer, "stamina", String.valueOf(stamina));
    XMLWriter.addAttribute(ePlayer, "headdress", headdress);
    XMLWriter.addAttribute(ePlayer, "glasses", glasses);
  }


}
