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
 * the user's money.
 * 
 * @author Andreas Wenger
 */
public class MoneyAction
  implements Action
{
  protected int value;

  
  /**
   * Constructor.
   */
  public MoneyAction(int value)
  {
    this.value = value; 
  }
  
  
  /**
   * Constructor.
   * Creates a LeagueCondition from the given
   * XML element.
   */
  public MoneyAction(Node e)
  {
    this.value = XMLReader.readAttributeValueInt(e, "value");
  }
  

  public void perform(Career career)
  {
    career.setUserMoney(career.getUserMoney() + value);
  }
}
