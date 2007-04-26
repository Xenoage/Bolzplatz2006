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

import java.util.ArrayList;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * This class parses a set of actions.
 * 
 * @author Andreas Wenger
 */
public class ActionsParser
{

  /**
   * Returns the set of action within the
   * given XML element, or an empty array if
   * nothing was found.
   */
  public static ArrayList<Action> parse(Node eActions)
  {
    ArrayList<Action> ret = new ArrayList<Action>();
    NodeList eActionsList = eActions.getChildNodes();
    for (int i = 0; i < eActionsList.getLength(); i++)
    {
      Node e = eActionsList.item(i);
      Action a = null;
      if (e.getNodeName().equals("money"))
        a = new MoneyAction(e);
      if (e.getNodeName().equals("teamvalues"))
        a = new TeamValuesAction(e);
      if (a != null)
        ret.add(a);
    }
    return ret;
  }
  
}
