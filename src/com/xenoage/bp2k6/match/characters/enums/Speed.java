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
package com.xenoage.bp2k6.match.characters.enums;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.match.characters.PlayerSettings;


/**
 * Possible speeds of a character.
 * 
 * @author Andreas Wenger
 */
public enum Speed
{
  None, Walk, Run;
  
  
  /**
   * Scales the given speed vector (m/s)
   * by a factor fitting to this speed.
   */
  public void scale(Vector3f ret)
  {
    if (this == Speed.Walk)
      ret.scale(1);
    else if (this == Speed.Run)
      ret.scale(PlayerSettings.getPlayerSprintFactor());
    else
      ret.scale(0);
  }
  
}
