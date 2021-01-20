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
package com.xenoage.bp2k6.util;

import javax.vecmath.Vector3f;


/**
 * This class extends the Vector3f class
 * by flags like "currentPosition".
 * 
 * @author Andreas Wenger
 */
public class Vector3fFlags
  extends Vector3f
{
  public int flags = FLAG_NOFLAG;
  
  public final static int FLAG_NOFLAG = 0; //normal point
  public final static int FLAG_CURRENTPOS = 1; //current camera position
  public final static int FLAG_SCOREBOARD = 2; //5 m in front of scoreboard
  public final static int FLAG_BEHINDSCOREBOARD = 3; //5 m behind the scoreboard
  
  
  /**
   * Constructor. Creates a copy of
   * the given Vector3fFlags object.
   */
  public Vector3fFlags(Vector3fFlags v)
  {
    this.x = v.x;
    this.y = v.y;
    this.z = v.z;
    this.flags = v.flags;
  }
  
  
  /**
   * Constructor. Creates a copy of
   * the given Vector3f object.
   */
  public Vector3fFlags(Vector3f v)
  {
    this.x = v.x;
    this.y = v.y;
    this.z = v.z;
  }
  
  
  /**
   * Constructor. Creates a new Vector3fFlags object.
   */
  public Vector3fFlags(float x, float y, float z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  
  /**
   * Constructor. Creates a new Vector3fFlags object.
   */
  public Vector3fFlags()
  {
  }
  
}
