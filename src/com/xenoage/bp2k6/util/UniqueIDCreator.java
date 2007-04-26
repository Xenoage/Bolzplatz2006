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

/**
 * Creates unique IDs for ISceneNode objects.
 * This is needed for object picking bit masks
 * (e.g. only pick stadium stands) and for
 * comparing ISceneNode objects ("equals" or "=="
 * does not work for JNI objects).
 * (Works for about 65000 nodes, that
 * should be enough ;-))
 *
 * @author Andi
 */
public class UniqueIDCreator
{
  public static final int BITMASK_NONE = 0x0000;
  public static final int BITMASK_STADIUMOBJECTS = 0x1000;
  private static int idcounter = 1;
  
  public static int createUniqueID(int bitmask)
  {
    int ret = bitmask + idcounter;
    idcounter++;
    return ret;
  }
  
}
