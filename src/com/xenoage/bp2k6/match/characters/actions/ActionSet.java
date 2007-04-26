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
 * This interface is for classes that
 * contain a set of actions.
 * These can be for example sets for
 * kickoff, free kick or goal elation, or
 * just simple Actions like waiting or running
 * to a certain position.
 * 
 * @author Andreas Wenger
 */
public interface ActionSet
{
  public static final int PRIORITY_LOW = 0;
  public static final int PRIORITY_NORMAL = 1;
  public static final int PRIORITY_HIGH = 2;
  public static final int PRIORITY_VERYHIGH = 2;
  
  /**
   * Returns the action with the given index.
   */
  public Action getAction(int index);
  
  /**
   * Returns the number of actions of this set.
   */
  public int getActionCount();
  
  /**
   * Returns the priority of this action set.
   */
  public int getPriority();
  
}
