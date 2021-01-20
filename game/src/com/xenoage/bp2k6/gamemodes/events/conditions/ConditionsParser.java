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

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class parses a set of conditions.
 * 
 * @author Andreas Wenger
 */
public class ConditionsParser
{

  /**
   * Returns the set of conditions within the
   * given XML element, or an empty array if
   * nothing was found.
   */
  public static ArrayList<Condition> parse(Node eConditions)
  {
    ArrayList<Condition> ret = new ArrayList<Condition>();
    NodeList eConditionsList = eConditions.getChildNodes();
    for (int i = 0; i < eConditionsList.getLength(); i++)
    {
      Node e = eConditionsList.item(i);
      Condition c = null;
      if (e.getNodeName().equals("money"))
        c = new MoneyCondition(e);
      else if (e.getNodeName().equals("league"))
        c = new LeagueCondition(e);
      else if (e.getNodeName().equals("matchresult"))
        c = new MatchResultCondition(e);
      else if (e.getNodeName().equals("lawn"))
        c = new LawnCondition(e);
      if (c != null)
        ret.add(c);
    }
    return ret;
  }
  
}
