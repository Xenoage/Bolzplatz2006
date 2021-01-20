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
package com.xenoage.bp2k6.match.stadium.crowd;

import com.xenoage.bp2k6.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;


/**
 * This class contains the colors and
 * of the crowd (4 different characters)
 * and has a method that creates a texture with
 * these colors.
 *
 * @author Andreas Wenger
 */
public class CrowdColors
{

  private final int characterCount = 4;
  private final int colorCount = 2;
  public static final int COLOR_SHIRT = 0;
  public static final int COLOR_PANTS = 1;

  //lowest possible base colors
  private Color[] baseLow = new Color[colorCount];
  //highest possible base colors
  private Color[] baseHigh = new Color[colorCount];
  //most dominant colors (must be within the low and high borders)
  private Color[] baseMain = new Color[colorCount];
  //colors the base colors will be replaced by
  private Color[][] newColors = new Color[characterCount][colorCount];
  
  
  /**
   * Constructor.
   */
  public CrowdColors()
  {
    baseLow[COLOR_SHIRT] = new Color(10, 30, 50);
    baseHigh[COLOR_SHIRT] = new Color(120, 150, 180);
    baseMain[COLOR_SHIRT] = new Color(30, 80, 140);
    baseLow[COLOR_PANTS] = new Color(80, 0, 0);
    baseHigh[COLOR_PANTS] = new Color(210, 5, 5);
    baseMain[COLOR_PANTS] = new Color(150, 0, 0);
  }

  
  /**
   * Sets the given color of the given character.
   */
  public void setColor(int character, int part, Color color)
  {
    newColors[character][part] = color;
  }
  
  
  /**
   * Gets the given color of the given character.
   */
  public Color getColor(int character, int part)
  {
    return newColors[character][part];
  }
  

  /**
   * Creates a texture with the crowd colors, based
   * on the given texture. Each character must have his
   * own row with 128 pixel height.
   * @param   destTexturePath  Path of the created texture
   * @return  <code>true</code>, if the method succeeded,
   *          otherwise <code>false</code>
   */
  public boolean createTexture(String destTexturePath)
  {
    
    //find base texture
    String baseTexturePath = "data/meshes/crowd1.png";
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Load and modify crowd texture: \"" + baseTexturePath + "\"");
    
    //create target image and copy base texture in it
    ImageIcon icon = new ImageIcon(baseTexturePath);
    BufferedImage target = new BufferedImage(
      icon.getImage().getWidth(null),
      icon.getImage().getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    Graphics g = target.getGraphics();
    g.drawImage(icon.getImage(), 0, 0, null);
    
    //change colors
    replaceColors(target, COLOR_SHIRT, "data/meshes/crowd1_shirtmask.png");
    replaceColors(target, COLOR_PANTS, "data/meshes/crowd1_pantsmask.png");
    
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
  
  
  private void replaceColors(BufferedImage target, 
    int part, String maskImagePath)
  {
    //load mask image into a BufferedImage
    ImageIcon icon = new ImageIcon(maskImagePath);
    BufferedImage mask = new BufferedImage(
      icon.getImage().getWidth(null),
      icon.getImage().getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    Graphics g = mask.getGraphics();
    g.drawImage(icon.getImage(), 0, 0, null);
    //replace colors
    int w = target.getWidth();
    int h = 128;
    for (int c = 0; c < characterCount; c++)
    {
      for (int y = 0; y < h; y++)
      {
        for (int x = 0; x < w; x++)
        {
          //change only pixels, that are black in the mask image
          boolean changePixel = (((mask.getRGB(x, c * h + y) >> 16) & 0xFF) < 100);
          if (changePixel)
          {
            int argb = target.getRGB(x, c * h + y);
            Color col = new Color((argb >> 16) & 0xFF, (argb >> 8) & 0xFF,
              (argb) & 0xFF, (argb >> 24) & 0xFF);
            target.setRGB(x, c * h + y,
              ColorTools.computeBrightnessColor(
                baseMain[part], col, newColors[c][part]).getRGB());;
          }
        }
      }
    }
  }
  
}