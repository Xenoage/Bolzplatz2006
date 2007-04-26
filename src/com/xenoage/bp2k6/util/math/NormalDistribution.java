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
package com.xenoage.bp2k6.util.math;


/**
 * This class helps to compute more realistic
 * random match results and other events.
 * 
 * @author Andreas Wenger
 */
public class NormalDistribution
{

  private static int goalNormality[] = new int[] {
   -6,
   -5,
   -4, -4,
   -3, -3, -3, -3, -3,
   -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
    1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,
    2,  2,  2,  2,  2,  2,  2,  2,  2,  2,
    3,  3,  3,  3,  3,
    4,  4,
    5,
    6
  };
  
  
  private static int normality[] = new int[] {
    0,
    1, 1,
    2, 2, 2, 2,
    3, 3, 3, 3, 3, 3, 3, 3,
    4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
    5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
    6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
    7, 7, 7, 7, 7, 7, 7, 7,
    8, 8, 8, 8,
    9, 9,
    10
   };
  
  
  
  /**
   * Applies a random modification (based on normal distribution)
   * on the given score and returns the modified score.
   */
  public static int randomizeGoals(int score)
  {
    int ret = score + goalNormality[(int) (Math.random() * goalNormality.length)];
    if (ret > 0)
      return ret;
    else
      return 0;
  }
  
  
  /**
   * Gets a random number (based on normal distribution)
   * between 0 and 10.
   */
  public static int random10()
  {
    return normality[(int) (Math.random() * normality.length)];
  }
  
  
  /**
   * Gets a random number (based on normal distribution)
   * between 0 and 1.
   */
  public static float random()
  {
    return random10() * 0.1f;
  }
  
}