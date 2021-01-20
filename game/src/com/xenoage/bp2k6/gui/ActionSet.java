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
package com.xenoage.bp2k6.gui;

import java.util.ArrayList;


/**
 * This class contains a set of actions.
 * It can be used to make the GUI XML files easier
 * by grouping actions that are called from
 * more than only one event.
 * With the action "performactions" the complete
 * set of actions is performed.
 * 
 * @author Andreas Wenger
 */
public class ActionSet
{
  private String id;
  private ArrayList<Action> actions = new ArrayList<Action>();
  
  
  /**
   * Constructor.
   */
  public ActionSet(String id)
  {
    this.id = id;
  }
  
  
  /**
   * Gets the ID of this ActionSet.
   */
  public String getID()
  {
    return id;
  }
  
  
  /**
   * Adds a new action to this ActionSet.
   */
  public void addAction(Action action)
  {
    actions.add(action);
  }
  
  
  /**
   * Performs all actions of this ActionSet.
   * @param group   The group or the parent group of the control
   *                that wants to perform this ActionSet.
   * @param event   The event that caused these actions.
   */
  public void performAll(Group group, String event)
  {
    for (int i = 0; i < actions.size(); i++)
      actions.get(i).perform(group, event);
  }
  
  
  /*-*
   * Performs all actions of this ActionSet.
   * @param group The control that wants to perform this ActionSet.
   * @param event The event that caused these actions.
   *-/
  public void performAll(Control control, String event)
  {
    for (int i = 0; i < actions.size(); i++)
      actions.get(i).perform(control, event);
  } */
  
}
