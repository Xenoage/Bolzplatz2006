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
package com.xenoage.bp2k6.gamemodes.events.conditions;

import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * Condition for a given amount of money
 * on the user's account.
 * 
 * @author Andreas Wenger
 */
public class MoneyCondition
  implements Condition
{
  protected int money;
  public enum Type { Less, More };
  protected Type type;

  
  /**
   * Constructor.
   */
  public MoneyCondition(int money, Type type)
  {
    this.money = money;
    this.type = type;
  }
  
  
  /**
   * Constructor.
   * Creates a MoneyCondition from the given
   * XML element.
   */
  public MoneyCondition(Node e)
  {
    this.money = XMLReader.readAttributeValueInt(e, "value");
    String type = XMLReader.readAttributeValue(e, "type");
    if (type.equals("less"))
      this.type = Type.Less;
    else
      this.type = Type.More;
  }
  

  public boolean isConditionMet(Career career)
  {
    if (type == Type.Less)
      return (career.getUserMoney() < money);
    else
      return (career.getUserMoney() > money);
  }

}
