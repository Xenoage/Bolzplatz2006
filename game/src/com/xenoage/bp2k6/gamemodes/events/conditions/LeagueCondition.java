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
package com.xenoage.bp2k6.gamemodes.events.conditions;

import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * This condition is met, when the user is
 * in one of the given leagues.
 * 
 * @author Andreas Wenger
 */
public class LeagueCondition
  implements Condition
{
  protected boolean allstars;
  protected boolean league1;
  protected boolean league2;
  protected boolean league3;

  
  /**
   * Constructor.
   */
  public LeagueCondition(boolean allstars, boolean league1, boolean league2, boolean league3)
  {
    this.allstars = allstars;
    this.league1 = league1;
    this.league2 = league2;
    this.league3 = league3;
  }
  
  
  /**
   * Constructor.
   * Creates a LeagueCondition from the given
   * XML element.
   */
  public LeagueCondition(Node e)
  {
    this.allstars = XMLReader.readAttributeValueBoolean(e, "allstars");
    this.league1 = XMLReader.readAttributeValueBoolean(e, "league1");
    this.league2 = XMLReader.readAttributeValueBoolean(e, "league2");
    this.league3 = XMLReader.readAttributeValueBoolean(e, "league3");
  }
  

  public boolean isConditionMet(Career career)
  {
    return (
      (allstars && career.getCurrentUserLeague() == Career.LEAGUE_ALLSTARS) ||
      (league1 && career.getCurrentUserLeague() == Career.LEAGUE_1) ||
      (league2 && career.getCurrentUserLeague() == Career.LEAGUE_2) ||
      (league3 && career.getCurrentUserLeague() == Career.LEAGUE_3));
  }

}