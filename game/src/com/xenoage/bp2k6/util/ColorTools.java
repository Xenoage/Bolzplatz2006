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

import java.awt.Color;

/**
 * This class has some useful methods that work with colors.
 *
 * @author   Andi
 */
public class ColorTools
{

  /**
   * @return <code>true</code>, if <code>c</code> is equal or brighter than
   *         <code>than</code>, otherwise <code>false</code>
   */
  public static boolean isBrighter(Color c, Color than)
  {
    if (c.getRed() >= than.getRed())
      if (c.getGreen() >= than.getGreen())
        if (c.getBlue() >= than.getBlue())
          return true;
    return false;
  }

  /**
   * @return <code>true</code>, if <code>c</code> is equal or darker than
   *         <code>than</code>, otherwise <code>false</code>
   */
  public static boolean isDarker(Color c, Color than)
  {
    if (c.getRed() <= than.getRed())
      if (c.getGreen() <= than.getGreen())
        if (c.getBlue() <= than.getBlue())
          return true;
    return false;
  }

  /**
   * This function creates a color, that is based on a given color
   * but with modified brightness. The amount of brightness-change
   * is defined by the brightness-difference of two
   * other given colors.
   * @param templateMainColor   The dominant template color (for example #444444)
   * @param templateColor       The current template color (for example #666666)
   * @param baseColor Color     The base color that will be modified (for example #008800)
   * @return                    The brightness-changed color (here #00CC00)
   */
  public static Color computeBrightnessColor(
    Color templateMainColor, Color templateColor, Color baseColor)
  {
    float hsbBase[] = Color.RGBtoHSB(baseColor.getRed(),
      baseColor.getGreen(), baseColor.getBlue(), null);
    float hsb1[] = Color.RGBtoHSB(templateMainColor.getRed(),
      templateMainColor.getGreen(), templateMainColor.getBlue(), null);
    float hsb2[] = Color.RGBtoHSB(templateColor.getRed(),
      templateColor.getGreen(), templateColor.getBlue(), null);
    float b = hsbBase[2] + hsb2[2] - hsb1[2];
    if (b < 0)
      b = 0;
    else if (b > 1)
      b = 1;
    return Color.getHSBColor(hsbBase[0], hsbBase[1], b);
  }
  
  
  /**
   * Gets the hex representation of the given color.
   */
  public static String getHex(Color c)
  {
    String r = Integer.toHexString(c.getRed());
    if (r.length() == 1) r = "0" + r;
    String g = Integer.toHexString(c.getGreen());
    if (g.length() == 1) g = "0" + g;
    String b = Integer.toHexString(c.getBlue());
    if (b.length() == 1) b = "0" + b;
    return (r + g + b).toUpperCase();
  }
  
  
  /**
   * Gets a color from its hex representation.
   */
  public static Color getColor(String hex)
  {
    try
    {
      return new Color(Integer.parseInt(hex, 16));
    }
    catch (Exception ex)
    {
      return Color.black;
    }
  }
  
  
  /**
   * Gets the inverse color.
   */
  public static Color getInverseColor(Color color)
  {
    return new Color(255 - color.getRed(),
      255 - color.getGreen(), 255 - color.getBlue());
  }


}
