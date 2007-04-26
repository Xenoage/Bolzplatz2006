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
import com.xenoage.bp2k6.gamemodes.reports.MatchInfo;
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * This condition is met, when the user's last match
 * had the given result.
 * 
 * @author Andreas Wenger
 */
public class MatchResultCondition
  implements Condition
{
  
  public enum MatchResultType { Lost, Drawn, Won };
  protected MatchResultType type;

  
  /**
   * Constructor.
   */
  public MatchResultCondition(MatchResultType type)
  {
    this.type = type;
  }
  
  
  /**
   * Constructor.
   * Creates a MatchResultCondition from the given
   * XML element.
   */
  public MatchResultCondition(Node e)
  {
    String type = XMLReader.readAttributeValue(e, "type");
    if (type.equals("lost"))
      this.type = MatchResultType.Lost;
    else if (type.equals("drawn"))
      this.type = MatchResultType.Drawn;
    else
      this.type = MatchResultType.Won;
  }

  
  public boolean isConditionMet(Career career)
  {
    MatchInfo result = career.getCurrentMatchResult();
    if (result != null)
    {
      int side = (result.getTeamHomeID().equals(career.getUserTeamID()) ? 1 : -1);
      if (result.getScore()[0] * side > result.getScore()[1] * side )
        return this.type == MatchResultType.Won;
      else if (result.getScore()[0] == result.getScore()[1])
        return this.type == MatchResultType.Drawn;
      else
        return this.type == MatchResultType.Lost;
    }
    return false;
  }
  
}
