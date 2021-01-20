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

import java.io.File;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This class contains information about the
 * stored savegames for career and worldcup mode.
 * 
 * @author Andreas Wenger
 */
public class SaveGames
{
  
  //id of the missing team (when loading fails)
  private static String missingTeamID = "";
  
  
  /**
   * Gets a description String about the given
   * career savegame slot (value from 1 to 5).
   */
  public static String getCareerSlotInfo(int index)
  {
    //does the savegame exist?
    File slotFile = new File("data/savegames/career/career" + index + ".xml");
    if (slotFile.exists())
    {
      try
      {
        //load the most important information about this savegame
        CareerInfo career = new CareerInfo(index);
        String teamName = GameEngine.getMatchManager().getTeamInfo(
          career.getUserTeamID()).getName();
        //cut teamname after 12 chars, if it is longer than 14 chars
        if (teamName.length() > 14)
        {
          teamName = teamName.substring(0, 11) + ".";
        }
        return /* Language.get("career") + ": " + */
          teamName + ", " + career.getCurrentYear() + ", " +
          career.getCurrentLeagueName(true) + ", " +
          career.getCurrentLeagueOrAllstarsMatchDay() + ". " + Language.get("matchday");
      }
      catch (Exception ex)
      {
        return Language.get("invalidslot");
      }
    }
    else
    {
      //savegame does not exists
      return Language.get("emptyslot");
    }
  }
  
  
  
  /**
   * Gets a description String about the given
   * worldcup savegame slot (value from 1 to 5).
   */
  public static String getWorldCupSlotInfo(int index)
  {
    //does the savegame exist?
    File slotFile = new File("data/savegames/worldcup/worldcup" + index + ".xml");
    if (slotFile.exists())
    {
      try
      {
        //load the most important information about this savegame
        WorldCupInfo worldCupInfo = new WorldCupInfo(index);
        String teamName = GameEngine.getMatchManager().getTeamInfo(
          worldCupInfo.getUserTeamID()).getName();
        //cut teamname after 12 chars, if it is longer than 14 chars
        if (teamName.length() > 14)
        {
          teamName = teamName.substring(0, 11) + ".";
        }
        return /* Language.get("career") + ": " + */
          teamName + ", " + worldCupInfo.getMatchDayName();
      }
      catch (Exception ex)
      {
        return Language.get("invalidslot");
      }
    }
    else
    {
      //savegame does not exists
      return Language.get("emptyslot");
    }
  }



  
  public static String getMissingTeamID()
  {
    return missingTeamID;
  }



  
  public static void setMissingTeamID(String missingID)
  {
    missingTeamID = missingID;
  }
  
  
}
