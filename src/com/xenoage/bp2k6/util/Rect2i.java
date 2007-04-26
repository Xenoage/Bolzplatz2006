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
 * A class for a 2d rectangle (yet missing in vecmath.jar)
 *
 * @author   Andi
 */
public class Rect2i
{
  public int x1;
  public int y1;
  public int x2;
  public int y2;

  public Rect2i()
  {
    set(0, 0, 0, 0);
  }

  public Rect2i(int x1, int y1, int x2, int y2)
  {
    set(x1, y1, x2, y2);
  }

  public int getHeight()
  {
    return y2 - y1;
  }

  public int getWidth()
  {
    return x2 - x1;
  }

  public void set(int x1, int y1, int x2, int y2)
  {
    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
  }


}
