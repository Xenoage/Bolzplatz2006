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
package com.xenoage.bp2k6.gamemodes.events.actions;

import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * This action increases or decreases
 * the user team's values.
 * 
 * @author Andreas Wenger
 */
public class TeamValuesAction
  implements Action
{
  protected int attack;
  protected int defense;
  protected int speed;
  protected int stamina;

  
  /**
   * Constructor.
   */
  public TeamValuesAction(int attack, int defense, int speed, int stamina)
  {
    this.attack = attack;
    this.defense = defense;
    this.speed = speed;
    this.stamina = stamina;
  }
  
  
  /**
   * Constructor.
   * Creates a LeagueCondition from the given
   * XML element.
   */
  public TeamValuesAction(Node e)
  {
    this.attack = XMLReader.readAttributeValueInt(e, "attack");
    this.defense = XMLReader.readAttributeValueInt(e, "defense");
    this.speed = XMLReader.readAttributeValueInt(e, "speed");
    this.stamina = XMLReader.readAttributeValueInt(e, "stamina");
  }
  

  public void perform(Career career)
  {
    career.setUserTeamValuesNextGame(attack, defense, speed, stamina);
  }
}