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

public class Point2i
{
  public int x;
  public int y;

  public Point2i()
  {
    this.x = 0;
    this.y = 0;
  }

  public Point2i(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public Point2i(Point2i p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public void add(Point2i p)
  {
    this.x += p.x;
    this.y += p.y;
  }
  
  
  public void set(Point2i p)
  {
    this.x = p.x;
    this.y = p.y;
  }

}
