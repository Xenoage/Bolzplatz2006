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
package com.xenoage.bp2k6.match;


/**
 * Possible controllers for a team:
 * AI, player 1 or player 2.
 * 
 * @author Andreas Wenger
 */
public enum TeamControl
{
  Player1,
  Player2,
  AI;
  
  public int getPlayerIndex()
  {
    if (this == Player1)
      return 0;
    else if (this == Player2)
      return 1;
    else
      return -1;
  }
};