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
 * Condition for the lawn of the
 * user's stadium.
 * 
 * @author Andreas Wenger
 */
public class LawnCondition
  implements Condition
{
  protected int quality;
  public enum Type { Less, More };
  protected Type type;

  
  /**
   * Constructor.
   */
  public LawnCondition(int quality, Type type)
  {
    this.quality = quality;
    this.type = type;
  }
  
  
  /**
   * Constructor.
   * Creates a LawnCondition from the given
   * XML element.
   */
  public LawnCondition(Node e)
  {
    this.quality = XMLReader.readAttributeValueInt(e, "quality");
    String type = XMLReader.readAttributeValue(e, "type");
    if (type.equals("less"))
      this.type = Type.Less;
    else
      this.type = Type.More;
  }
  

  public boolean isConditionMet(Career career)
  {
    if (type == Type.Less)
      return (career.getUserStadium().getField().getLawnQuality() < quality);
    else
      return (career.getUserStadium().getField().getLawnQuality() > quality);
  }

}