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

import com.xenoage.bp2k6.input.Input;


/**
 * Goal shoot direction: Not exact coordinates,
 * but general information where and how to
 * shoot the ball, e.g. "left side, lifted".
 * 
 * @author Andreas Wenger
 */
public enum ShootDirection
{
  None,
  Left, CenterLeft, Center, CenterRight, Right,
  LiftLeft, LiftCenter, LiftRight;


  /**
   * Creates a <code>ShootDirection</code> dependent on the
   * given arrow keys combination (see Input.java) and <code>PlayerNr</code>.
   * @param currentSide -1: player team on the left side,
   *                     1: player team on the right side
   */
  public static ShootDirection fromArrowKeys(
    int arrowKeys, int currentSide)
  {
    boolean up = (arrowKeys & Input.up) != 0;
    boolean down = (arrowKeys & Input.down) != 0;
    boolean left = (arrowKeys & Input.left) != 0;
    boolean right = (arrowKeys & Input.right) != 0;
    
    if (currentSide == -1)
    {
      //player team on the left side
      if (left)
      {
        //lift the ball
        if (up)
          return ShootDirection.LiftLeft;
        else if (down)
          return ShootDirection.LiftRight;
        else
          return ShootDirection.LiftCenter;
      }
      else
      {
        //normal shoot
        if (up && right)
          return ShootDirection.CenterLeft;
        else if (down && right)
          return ShootDirection.CenterRight;
        else if (up)
          return ShootDirection.Left;
        else if (down)
          return ShootDirection.Right;
        else
          return ShootDirection.Center;
      }
    }
    else
    {
      //player team on the right side
      if (right)
      {
        //lift the ball
        if (up)
          return ShootDirection.LiftRight;
        else if (down)
          return ShootDirection.LiftLeft;
        else
          return ShootDirection.LiftCenter;
      }
      else
      {
        //normal shoot
        if (up && left)
          return ShootDirection.CenterRight;
        else if (down && right)
          return ShootDirection.CenterLeft;
        else if (up)
          return ShootDirection.Right;
        else if (down)
          return ShootDirection.Left;
        else
          return ShootDirection.Center;
      }
    }
    
  }

  /**
   * Gets a random shoot direction.
   */
  public static ShootDirection getRandomShootDirection()
  {
    double r = Math.random();
    if (r < 0.3)
      return ShootDirection.Center;
    else if (r < 0.5)
      return ShootDirection.CenterLeft;
    else if (r < 0.7)
      return ShootDirection.CenterRight;
    else if (r < 0.8)
      return ShootDirection.Left;
    else if (r < 0.9)
      return ShootDirection.Right;
    else if (r < 0.94)
      return ShootDirection.LiftCenter;
    else if (r < 0.97)
      return ShootDirection.LiftLeft;
    else //(r < 1.0)
      return ShootDirection.LiftRight;
  }


}
