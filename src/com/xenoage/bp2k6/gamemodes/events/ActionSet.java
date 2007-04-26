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
package com.xenoage.bp2k6.gamemodes.events;

import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.gamemodes.events.actions.Action;
import com.xenoage.bp2k6.gamemodes.events.actions.ActionsParser;
import com.xenoage.bp2k6.gamemodes.events.conditions.Condition;
import com.xenoage.bp2k6.gamemodes.events.conditions.ConditionsParser;
import com.xenoage.bp2k6.util.XMLReader;

import java.util.ArrayList;

import org.w3c.dom.Node;


/**
 * A set of conditions and actions, that
 * are performed when the conditions
 * are met.
 * 
 * @author Andreas Wenger
 */
public class ActionSet
{
  protected ArrayList<Condition> conditions;
  protected ArrayList<Action> actions;
  
  
  /**
   * Constructor.
   * Loads an action set from the given "beforematch" or
   * "aftermatch" XML element.
   */
  public ActionSet(Node eActionSet)
  {
    //read conditions
    Node eConditions = XMLReader.getChildElement(eActionSet, "conditions");
    if (eConditions != null)
      conditions = ConditionsParser.parse(eConditions);
    //read actions
    Node eActions = XMLReader.getChildElement(eActionSet, "actions");
    if (eActions != null)
      actions = ActionsParser.parse(eActions);
  }
  
  
  /**
   * Gets the conditions of this action set. May be an empty array.
   */
  public ArrayList<Condition> getConditions()
  {
    return conditions;
  }
  
  
  /**
   * Gets the actions of this action set. May be an empty array.
   */
  public ArrayList<Action> getActions()
  {
    return actions;
  }
  
  
  /**
   * Returns <code>true</code>, when this action set
   * is possible for the given career
   * (conditions must be met)
   */
  public boolean isPossible(Career career)
  {
    if (conditions != null)
    {
      for (int i = 0; i < conditions.size(); i++)
      {
        if (!conditions.get(i).isConditionMet(career))
          return false;
      }
    }
    return true;
  }
  
  
  /**
   * Performs the actions and returns <code>true</code>
   * if the conditions are met, otherwise nothing
   * is done and <code>false</code> is returned.
   */
  public boolean perform(Career career)
  {
    if (conditions != null)
    {
      for (int i = 0; i < conditions.size(); i++)
      {
        if (!conditions.get(i).isConditionMet(career))
          return false;
      }
    }
    if (actions != null)
    {
      for (int i = 0; i < actions.size(); i++)
        actions.get(i).perform(career);
    }
    return true;
  }
  
}
