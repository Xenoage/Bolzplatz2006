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
package com.xenoage.bp2k6.gamemodes;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class contains the most important
 * information about a world cup (useful
 * for world cup preview).
 * 
 * @author Andreas Wenger
 */
public class WorldCupInfo
{
  public static final int groupMatchDays = 3;
  
  //world cup information
  private String userTeamID;
  private int matchDayNumber;
  
  
  /**
   * Constructor. Loads the world cup with the
   * given index (between 1 and 5).
   */
  public WorldCupInfo(int id)
  { 
    //read world cup information from XML file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading WorldCupInfo \"" + id + "\"...");
    try
    {
      Document doc = XMLReader.readFile("data/savegames/worldcup/worldcup" + id + ".xml");
      Node root = doc.getFirstChild();
      //load information
      userTeamID = XMLReader.readAttributeValue(root, "userteamid");
      matchDayNumber = XMLReader.readAttributeValueInt(root, "matchdaynumber");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "WorldCupInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Gets the name of the given matchday.
   */
  public static String getMatchDayName(int number)
  {
    if (number <= groupMatchDays)
      return number + ". " + Language.get("groupmatchday");
    else if (number == groupMatchDays + 1)
      return Language.get("eighthfinal");
    else if (number == groupMatchDays + 2)
      return Language.get("quarterfinal");
    else if (number == groupMatchDays + 3)
      return Language.get("semifinal");
    else if (number == groupMatchDays + 4)
      return Language.get("thirdplacematch");
    else
      return Language.get("final");
  }
  
  
  /**
   * Gets the name of the given group
   * (0, 1, 2, 3, 4, 5, 6, 7).
   */
  public static String getGroupName(int group)
  {
    if (group == 0)
      return Language.get("groupa");
    else if (group == 1)
      return Language.get("groupb");
    else if (group == 2)
      return Language.get("groupc");
    else if (group == 3)
      return Language.get("groupd");
    else if (group == 4)
      return Language.get("groupe");
    else if (group == 5)
      return Language.get("groupf");
    else if (group == 6)
      return Language.get("groupg");
    else
      return Language.get("grouph");
  }
  
  
  public String getUserTeamID()
  {
    return userTeamID;
  }
  
  
  public String getMatchDayName()
  {
    return getMatchDayName(matchDayNumber);
  }
  
}