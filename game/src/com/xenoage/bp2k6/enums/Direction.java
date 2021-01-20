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
package com.xenoage.bp2k6.enums;

import net.sf.jirr.vector3df;

/**
 * Enumeration for direction.
 * 
 * @author Andreas Wenger
 */
public enum Direction
{
  NONE(0),
  UP(1),
  RIGHT(2),
  DOWN(4),
  LEFT(8),
  UP_RIGHT(UP.dir | RIGHT.dir),
  DOWN_RIGHT(DOWN.dir | RIGHT.dir),
  UP_LEFT(UP.dir | LEFT.dir),
  DOWN_LEFT(DOWN.dir | LEFT.dir);

  private final int dir;

  private final float halfsqrt2 = 0.70710678118654752440084436210485f;

  Direction(int dir)
  {
    this.dir = dir;
  }

  public int dir() { return this.dir; }

  public static Direction fromInt(int dir)
  {
    if (dir == UP.dir) return UP;
    else if (dir == UP_RIGHT.dir) return UP_RIGHT;
    else if (dir == RIGHT.dir) return RIGHT;
    else if (dir == DOWN_RIGHT.dir) return DOWN_RIGHT;
    else if (dir == DOWN.dir) return DOWN;
    else if (dir == DOWN_LEFT.dir) return DOWN_LEFT;
    else if (dir == LEFT.dir) return LEFT;
    else if (dir == UP_LEFT.dir) return UP_LEFT;
    return NONE;
  }

  public vector3df getvector3df()
  {
    if (dir == UP.dir) return new vector3df(0, 0, 1);
    else if (dir == UP_RIGHT.dir) return new vector3df(halfsqrt2, 0, halfsqrt2);
    else if (dir == RIGHT.dir) return new vector3df(1, 0, 0);
    else if (dir == DOWN_RIGHT.dir) return new vector3df(halfsqrt2, 0, -halfsqrt2);
    else if (dir == DOWN.dir) return new vector3df(0, 0, -1);
    else if (dir == DOWN_LEFT.dir) return new vector3df(-halfsqrt2, 0, -halfsqrt2);
    else if (dir == LEFT.dir) return new vector3df(-1, 0, 0);
    else if (dir == UP_LEFT.dir) return new vector3df(-halfsqrt2, 0, halfsqrt2);
    return new vector3df(0, 0, 0);
  }

  public float getDegrees()
  {
    if (dir == UP.dir) return 0;
    else if (dir == UP_RIGHT.dir) return 45;
    else if (dir == RIGHT.dir) return 90;
    else if (dir == DOWN_RIGHT.dir) return 135;
    else if (dir == DOWN.dir) return 180;
    else if (dir == DOWN_LEFT.dir) return 225;
    else if (dir == LEFT.dir) return 270;
    else if (dir == UP_LEFT.dir) return 315;
    return 0;
  }


}
