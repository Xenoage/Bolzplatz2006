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
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;


/**
 * Pool for career events.
 * 
 * @author Andreas Wenger
 */
public class CareerEventPool
{
  protected ArrayList<CareerEvent> events = new ArrayList<CareerEvent>();
  
  
  /**
   * Load all available career events.
   */
  public CareerEventPool()
  {
    String lang = Language.getCurrentLanguageID();
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading all \"" + lang + "\"career events into the pool...");
    String[] files = new File(
      "data/careerevents/" + lang + "/").list(FileUtils.getXMLFilter());
    if (files == null)
    {
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "No career event files found!");
    }
    else
    {
      for (int i = 0; i < files.length; i++)
      {
        //load events from XML file
        try
        {
          Document doc = XMLReader.readFile(
            "data/careerevents/" + lang + "/" + files[i]);
          Node root = doc.getFirstChild();
          //list of events
          List<Element> listEvents = XMLReader.elements(root, "event");
          for (int iE = 0; iE < listEvents.size(); iE++)
          {
            Node e = listEvents.get(iE);
            events.add(new CareerEvent(e));
          }
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, this,
            "Error while loading career events:");
          Logging.log(Logging.LEVEL_ERRORS, this, ex);
        }
      }
      Logging.log(Logging.LEVEL_MESSAGES, this,
        events.size() + " career event loaded.");
    }
  }
  
  
  /**
   * Gets a random career event, that is ok for the given
   * career, or <code>null</code>.
   */
  public CareerEvent getRandomEvent(Career career)
  {
    if (events.size() > 0 && Math.random() < 0.2) //20% events
    {
      //raise event
      int i = 0;
      while(i < 10) //10 attempts to find an event
      {
        i++;
        CareerEvent e = events.get((int) (Math.random() * events.size()));
        if (e.isPossible(career))
          return e;
      }
    }
    return null;
  }
  
}
