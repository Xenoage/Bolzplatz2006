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
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * Class for a career event. A career event may be occur
 * after a match (but not after the last match of a season).
 * The text of the event is shown in a special screen.
 * For further information see the design document.
 * 
 * @author Andreas Wenger
 */
public class CareerEvent
{
  
  protected String title;
  protected String text;

  protected ActionSet beforeMatchActionSet;
  protected ActionSet afterMatchActionSet;
 
  
  /**
   * Constructor.
   * Loads a career event from the given "event" XML element.
   */
  public CareerEvent(Node eEvent)
  {
    //read before match action set
    Node eBeforeMatch = XMLReader.getChildElement(eEvent, "beforematch");
    if (eBeforeMatch != null)
      this.beforeMatchActionSet = new ActionSet(eBeforeMatch);
    //read after match action set
    Node eAfterMatch = XMLReader.getChildElement(eEvent, "aftermatch");
    if (eAfterMatch != null)
      this.afterMatchActionSet = new ActionSet(eAfterMatch);
    //read title and text
    Node eTitle = XMLReader.getChildElement(eEvent, "title");
    if (eTitle != null)
      this.title = eTitle.getTextContent();
    Node eText = XMLReader.getChildElement(eEvent, "text");
    if (eText != null)
    {
      this.text = eText.getTextContent();
      this.text = this.text.replace("\\n", "\n");
    }
  }


  /**
   * Gets the title of this event.
   */
  public String getTitle()
  {
    return title;
  }


  /**
   * Sets the title of this event.
   */
  public void setTitle(String title)
  {
    this.title = title;
  }


  /**
   * Gets the text of this event.
   */
  public String getText()
  {
    return text;
  }


  /**
   * Sets the text of this event.
   */
  public void setText(String text)
  {
    this.text = text;
  }

  
  /**
   * Returns <code>true</code>, when this event
   * is possible for the given career
   * (before match conditions must be met).
   */
  public boolean isPossible(Career career)
  {
    if (beforeMatchActionSet != null)
      return beforeMatchActionSet.isPossible(career);
    else
      return true;
  }
  

  /**
   * Raises an event and returns <code>true</code>
   * if the conditions are met, otherwise nothing
   * is done and <code>false</code> is returned.
   * Call this method before and after the match.
   */
  public boolean perform(Career career, boolean afterMatch)
  {
    if (afterMatch && afterMatchActionSet != null)
    {
      afterMatchActionSet.perform(career);
    }
    else if (!afterMatch && beforeMatchActionSet != null)
    {
      beforeMatchActionSet.perform(career);
    }
    return true;
  }

}