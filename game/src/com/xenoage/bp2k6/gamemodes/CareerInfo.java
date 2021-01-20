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
import com.xenoage.bp2k6.gamemodes.reports.*;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.language.Language;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class contains the most important
 * information about a career (useful
 * for career preview).
 * 
 * @author Andreas Wenger
 */
public class CareerInfo
{
  
  //career information
  private String userTeamID;
  private int userMoney;
  private int currentYear;
  private int currentLeague;
  private int currentMatchDay;
  private int currentAllstarsLeagueMatchDay;
  private int currentLeagueMatchDay;
  
  //types of leagues
  public static final int LEAGUE_ALLSTARS = 0;
  public static final int LEAGUE_1 = 1;
  public static final int LEAGUE_2 = 2;
  public static final int LEAGUE_3 = 3;
  
  
  
  /**
   * Constructor. Loads the career with the
   * given index (between 1 and 5).
   */
  public CareerInfo(int id)
  { 
    //read career information from XML file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading CareerInfo \"" + id + "\"...");
    try
    {
      Document doc = XMLReader.readFile("data/savegames/career/career" + id + ".xml");
      Node root = doc.getFirstChild();
      //load information
      userTeamID = XMLReader.readAttributeValue(root, "userteamid");
      userMoney = XMLReader.readAttributeValueInt(root, "usermoney");
      currentYear = XMLReader.readAttributeValueInt(root, "currentyear");
      currentLeague = XMLReader.readAttributeValueInt(root, "currentleague");
      currentAllstarsLeagueMatchDay = XMLReader.readAttributeValueInt(root,
        "currentallstarsleaguematchday");
      currentLeagueMatchDay = XMLReader.readAttributeValueInt(root,
        "currentleaguematchday");
      currentMatchDay = XMLReader.readAttributeValueInt(root,
        "currentmatchday");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "CareerInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Gets the name of the current league.
   * If the user is in allstars league, the result
   * is dependent on the type of the current matchday.
   */
  public String getCurrentLeagueName(boolean shortName)
  {
    if (currentLeague == LEAGUE_ALLSTARS)
      if (Season.getMatchDayType(currentMatchDay) == Season.MATCHDAY_ALLSTARS)
        if (shortName)
          return Language.get("leagueallstarsshort");
        else
          return Language.get("leagueallstars");
      else
        return Language.get("league1");
    else if (currentLeague == LEAGUE_1)
      return Language.get("league1");
    else if (currentLeague == LEAGUE_2)
      return Language.get("league2");
    else
      return Language.get("league3");
  }

  
  public int getCurrentLeague()
  {
    return currentLeague;
  }
  
  
  /**
   * Gets the matchday of the current league or allstars league,
   * dependent on the user's league and the current matchday type.
   */
  public int getCurrentLeagueOrAllstarsMatchDay()
  {
    if (currentLeague == LEAGUE_ALLSTARS &&
      Season.getMatchDayType(currentMatchDay) == Season.MATCHDAY_ALLSTARS)
      return getCurrentAllstarsLeagueMatchDay();
    else
      return getCurrentLeagueMatchDay();
  }
  
  
  public int getCurrentAllstarsLeagueMatchDay()
  {
    return currentAllstarsLeagueMatchDay;
  }

  
  public int getCurrentLeagueMatchDay()
  {
    return currentLeagueMatchDay;
  }

  
  public int getCurrentYear()
  {
    return currentYear;
  }

  
  public int getUserMoney()
  {
    return userMoney;
  }

 
  public String getUserTeamID()
  {
    return userTeamID;
  }
  
}
