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
package com.xenoage.bp2k6.match.characters.actions;

import java.util.ArrayList;


/**
 * This is a normal ActionSet which
 * can contain a set of any number of actions.
 * 
 * @author Andreas Wenger
 */
public class GeneralActionSet
  implements ActionSet
{
  private int priority;
  private ArrayList<Action> actions;
  
  
  /**
   * Constructor.
   */
  public GeneralActionSet(ArrayList<Action> actions, int priority)
  {
    this.actions = actions;
    this.priority = priority;
  }
  
  
  /**
   * Returns the action with the given index.
   */
  public Action getAction(int index)
  {
    return actions.get(index);
  }

  
  /**
   * Returns the number of actions of this set.
   */
  public int getActionCount()
  {
    return actions.size();
  }


  /**
   * Returns the priority of this action set.
   */
  public int getPriority()
  {
    return priority;
  }
  
}
