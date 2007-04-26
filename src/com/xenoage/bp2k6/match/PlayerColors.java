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

import com.xenoage.bp2k6.Settings;
import com.xenoage.bp2k6.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

import org.w3c.dom.Node;


/**
 * This class contains the colors and
 * the pattern (texture id) of a player
 * and has a method that creates a texture with
 * these colors.
 *
 * @author   Andi
 */
public class PlayerColors
{

  private final int colorCount = 5;
  public static final int COLOR_SKIN = 0;
  public static final int COLOR_SHIRT = 1;
  public static final int COLOR_SHIRT2 = 2;
  public static final int COLOR_PANTS = 3;
  public static final int COLOR_SHOES = 4;

  //lowest possible base colors
  private Color[] baseLow = new Color[colorCount];
  //highest possible base colors
  private Color[] baseHigh = new Color[colorCount];
  //most dominant colors (must be within the low and high borders)
  private Color[] baseMain = new Color[colorCount];
  //colors the base colors will be replaced by
  private Color[] newColors = new Color[colorCount];
  //pattern (part of texture-id)
  private String pattern;
  

  public PlayerColors()
  {
    baseLow[COLOR_SKIN] = new Color(130, 105, 70);
    baseHigh[COLOR_SKIN] = new Color(255, 240, 160);
    baseMain[COLOR_SKIN] = new Color(255, 200, 135);
    baseLow[COLOR_SHIRT] = new Color(20, 60, 110); //(0, 0, 0);
    baseHigh[COLOR_SHIRT] = new Color(115, 153, 197); //(45, 105, 175);
    baseMain[COLOR_SHIRT] = new Color(40, 100, 165);
    baseLow[COLOR_SHIRT2] = new Color(25, 100, 25);
    baseHigh[COLOR_SHIRT2] = new Color(44, 182, 44); 
    baseMain[COLOR_SHIRT2] = new Color(38, 164, 38);
    baseLow[COLOR_PANTS] = new Color(150, 0, 0);
    baseHigh[COLOR_PANTS] = new Color(235, 85, 85);
    baseMain[COLOR_PANTS] = new Color(200, 0, 0);
    baseLow[COLOR_SHOES] = new Color(120, 120, 120);
    baseHigh[COLOR_SHOES] = new Color(210, 210, 210);
    baseMain[COLOR_SHOES] = new Color(200, 200, 200);
    this.setColor(COLOR_SKIN, new Color(153, 120, 90));
    this.setColor(COLOR_SHIRT, new Color(30, 100, 160));
    this.setColor(COLOR_SHIRT2, new Color(30, 100, 160));
    this.setColor(COLOR_PANTS, new Color(190, 0, 0));
    this.setColor(COLOR_SHOES, new Color(160, 160, 160));
  }

  public void setColor(int part, Color color)
  {
    newColors[part] = color;
  }
  
  public Color getColor(int part)
  {
    return newColors[part];
  }
  

  /**
   * Creates a texture with the player colors, based
   * on the given texture.
   * @param   playerType       "outfieldplayer", "goalkeeper" or "playercap"
   * @param   usePattern       use pattern or ignore it
   * @param   destTexturePath  Path of the created texture
   * @return  <code>true</code>, if the method succeeded,
   *          otherwise <code>false</code>
   */
  public boolean createTexture(String playerType,
    boolean usePattern, String destTexturePath)
  {
    //find base texture
    String baseTexturePath;
    if (!usePattern || pattern == null || pattern.length() == 0)
      baseTexturePath = "data/meshes/" + playerType;
    else
      baseTexturePath = "data/meshes/" + playerType + "-" + pattern;
    if (Settings.isVideoLowQuality())
      baseTexturePath += "-lowquality";
    baseTexturePath += ".png";
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Load and modify player texture: \"" + baseTexturePath + "\"");
    //load base texture into a BufferedImage
    ImageIcon icon = new ImageIcon(baseTexturePath);
    BufferedImage base = new BufferedImage(
      icon.getImage().getWidth(null),
      icon.getImage().getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    Graphics g = base.getGraphics();
    g.drawImage(icon.getImage(), 0, 0, null);
    //create target image
    BufferedImage target = new BufferedImage(
      icon.getImage().getWidth(null),
      icon.getImage().getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    //replace colors
    int w = base.getWidth();
    int h = base.getHeight();
    for (int y = 0; y < h; y++)
    {
      for (int x = 0; x < w; x++)
      {
        Color c = new Color(base.getRGB(x, y) & 0xFFFFFF);
        boolean found = false;
        for (int i = 0; i < colorCount; i++)
        {
          if (ColorTools.isBrighter(c, baseLow[i]))
            if (ColorTools.isDarker(c, baseHigh[i]))
            {
              target.setRGB(x, y,
                          ColorTools.computeBrightnessColor(
                            baseMain[i], c, newColors[i]).getRGB());
              found = true;
              break;
            }
        }
        if (!found)
          target.setRGB(x, y, c.getRGB());
      }
    }
    //save new texture
    java.io.File file = new java.io.File(destTexturePath);
    try
    {
      javax.imageio.ImageIO.write(target, "png", file);
      return true;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  
  public String getPattern()
  {
    return pattern;
  }

  /**
   * Sets the pattern (part of texture id), always a ".png" file.
   * For example the pattern for the texture
   * "images/meshes/outfieldplayer-rednose.png" would be "rednose".
   * If no pattern is set, "images/meshes/outfieldplayer.png"
   * would be used.
   */
  public void setPattern(String pattern)
  {
    this.pattern = pattern;
  }
  
  
  /**
   * Compares this PlayerColors object with another one and
   * returns <code>true</code> if their newColors and
   * pattern are completely equal, otherwise <code>false</code>.
   */
  public boolean equals(PlayerColors other)
  {
    for (int i = 0; i < colorCount; i++)
      if (!newColors[i].equals(other.getColor(i)))
        return false;
    if (pattern == null || other.getPattern() == null)
      return pattern == other.getPattern();
    else
      return (pattern.equals(other.getPattern()));
  }
  
  
  /**
   * Save this PlayerColors object within the given
   * XML element. A new child element "colors"
   * with all information about the goal
   * is created.
   * @param id  If not <code>null</code>, a "id"-attribute
   *            with the given value is also created.
   */
  public void saveToXML(Node parentElement, String id)
  {
    //create goal element
    Node eColors = XMLWriter.addElement("colors", parentElement);
    if (id != null)
      XMLWriter.addAttribute(eColors, "id", id);
    XMLWriter.addAttribute(eColors,
      "pattern", (pattern != null ? pattern : ""));
    XMLWriter.addAttribute(eColors,
      "skin", ColorTools.getHex(newColors[COLOR_SKIN]));
    XMLWriter.addAttribute(eColors,
      "shirt", ColorTools.getHex(newColors[COLOR_SHIRT]));
    XMLWriter.addAttribute(eColors,
      "shirt2", ColorTools.getHex(newColors[COLOR_SHIRT2]));
    XMLWriter.addAttribute(eColors,
      "pants", ColorTools.getHex(newColors[COLOR_PANTS]));
    XMLWriter.addAttribute(eColors,
      "shoes", ColorTools.getHex(newColors[COLOR_SHOES]));
  }
  
  
  /**
   * Returns true, if the given shirt color 1
   * is similar to this shirt color 1.
   */
  public boolean isColorSimilarTo(PlayerColors col)
  {
    Color c1 = newColors[COLOR_SHIRT];
    Color c2 = col.getColor(COLOR_SHIRT);
    return (Math.abs(c1.getRed() - c2.getRed()) < 120 &&
      Math.abs(c1.getGreen() - c2.getGreen()) < 120 &&
      Math.abs(c1.getBlue() - c2.getBlue()) < 120);
  }
  
  
  /**
   * Returns the inverse colors (only shirtis reversed)
   */
  public PlayerColors getInverseColors()
  {
    PlayerColors ret = new PlayerColors();
    ret.setColor(COLOR_SKIN, newColors[COLOR_SKIN]);
    ret.setColor(COLOR_SHIRT, ColorTools.getInverseColor(newColors[COLOR_SHIRT]));
    ret.setColor(COLOR_SHIRT2, ColorTools.getInverseColor(newColors[COLOR_SHIRT2]));
    ret.setColor(COLOR_PANTS, newColors[COLOR_PANTS]);
    ret.setColor(COLOR_SHOES, newColors[COLOR_SHOES]);
    return ret;
  }

}
