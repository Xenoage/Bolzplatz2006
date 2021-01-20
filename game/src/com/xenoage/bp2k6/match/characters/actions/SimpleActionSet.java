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


/**
 * This is the simplest form of a ActionSet:
 * It contains only one element. It is used instead
 * of GeneralActionSet for one-element-sets
 * because it is faster and needs less memory
 * (needs no ArrayList).
 * 
 * @author Andreas Wenger
 */
public class SimpleActionSet
  implements ActionSet
{
  private int priority;
  private Action action;
  
  
  /**
   * Constructor.
   */
  public SimpleActionSet(Action action, int priority)
  {
    this.action = action;
    this.priority = priority;
  }
  
  
  /**
   * Returns the only element.
   */
  public Action getAction(int index)
  {
    return action;
  }

  
  /**
   * Returns the number of elements: Of course 1.
   */
  public int getActionCount()
  {
    return 1;
  }


  /**
   * Returns the priority of this action set.
   */
  public int getPriority()
  {
    return priority;
  }
  
}
