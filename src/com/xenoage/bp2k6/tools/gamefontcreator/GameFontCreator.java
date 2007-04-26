/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.gamefontcreator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import javax.swing.ImageIcon;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.xenoage.bp2k6.gui.font.GameFont;
import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;


/**
 * This program creates a bp2k6 font definition
 * file (.xml) from a given texture with
 * magenta points, that define the width of
 * the letters.
 * The letters are:
 * <code>
 * A B C D E F G H I J
 * K L M N O P Q R S T
 * U V W X Y Z Ä Ö Ü ©
 * a b c d e f g h i j
 * k l m n o p q r s t
 * u v w x y z ä ö ü ß
 * 0 1 2 3 4 5 6 7 8 9
 * . , ; : _ ! ? " ' &
 * + - * / = € $ § @ %
 * ( ) { } [ ] < > \  
 * </code>
 * All letters must have the same height,
 * so they must be aligned in a row.
 * View the design document for details.
 *
 * @author Andreas Wenger
 */
public class GameFontCreator
{
  
  /**
   * Start the program with these parameters:
   * filename of texture, height of a row in pixels,
   * scaling factor for all values and id of the font
   */
  public static void main(String args[])
    throws Exception
  {
    System.out.println("\nBP2K6 FONT CREATOR");
    System.out.println("******************");
    System.out.println("\n(c) 2006 by Andreas Wenger, Xenoage Software\n");
    
    //4 arguments?
    if (args.length != 4)
    {
      System.out.println("4 parameters must be given:");
      System.out.println("- filename of the texture        (e.g. \"font1-raster.png\")");
      System.out.println("- id of the font                 (e.g. \"font1\" results in \"font1.xml\")");
      System.out.println("- height of a row in pixels      (e.g. 200)");
      System.out.println("- scaling factor for all values  (e.g. 0.5)");
      
      return;
    }
    String texturePath = args[0];
    String fontID = args[1];
    int rowHeight = Integer.parseInt(args[2]);
    float scaleFactor = Float.parseFloat(args[3]);
    
    //read texture
    ImageIcon icon = new ImageIcon(texturePath);
    if (icon == null || icon.getImage().getWidth(null) < 0)
    {
      System.out.println("Error: Texture \"" + texturePath + "\" not found or not readable.");
      return;
    }
    BufferedImage tex = new BufferedImage(
      icon.getImage().getWidth(null),
      icon.getImage().getHeight(null),
      BufferedImage.TYPE_INT_ARGB);
    Graphics g = tex.getGraphics();
    g.drawImage(icon.getImage(), 0, 0, null);
    
    //read the 100 letters
    int points[] = new int[10 * 10 * 2];
    int pointsPos = 0;
    int rowPointsCount = 0;
    int w = tex.getWidth();
    int h = tex.getHeight();
    if (h < rowHeight * 10)
    {
      System.out.println("Error: Texture is not high enough for 10 rows!");
      return;
    }
    Color colorPoint = new Color(255, 0, 255);
    for (int y = 0; y < 10; y++)
    {
      rowPointsCount = 0;
      for (int x = 0; x < w; x++)
      {
        Color c = new Color(tex.getRGB(x, y * rowHeight) & 0xFFFFFF);
        if (c.equals(colorPoint))
        {
          points[pointsPos++] = (int) (x * scaleFactor);
          if (pointsPos > 200)
          {
            System.out.println("Error: Too much points!");
          }
          rowPointsCount++;
        }
      }
      if (rowPointsCount != 20)
      {
        System.out.println("Error: Row " + y + " has " + rowPointsCount +
          " points, but must have 10!");
        return;
      }
    }
    
    //Everything worked fine so far. Now write the XML file
    char[] letters = GameFont.getAllLetters();
    Document document = XMLWriter.createEmptyDocument();
    Node root = XMLWriter.addElement("font", document);
    XMLWriter.addAttribute(root, "id", fontID);
    XMLWriter.addAttribute(root,
      "h", String.valueOf((int) (rowHeight * scaleFactor)));
    pointsPos = 0;
    for (int i = 0; i < 100; i++)
    {
      Node eLetter = XMLWriter.addElement("letter", root);
      XMLWriter.addAttribute(eLetter, "name", String.valueOf(letters[i]));
      int x1 = points[pointsPos++];
      int x2 = points[pointsPos++];
      XMLWriter.addAttribute(eLetter, "x", String.valueOf(x1));
      XMLWriter.addAttribute(eLetter, "y", String.valueOf(
        (int) (i / 10 * rowHeight * scaleFactor)));
      XMLWriter.addAttribute(eLetter, "w", String.valueOf(x2 - x1));
    }
    
    try
    {
      XMLWriter.writeFile(document, fontID + ".xml");
    }
    catch (Exception ex)
    {
      System.out.println("Error creating file: " + ex.getMessage());
      return;
    }
    
    System.out.println("Font definition file successfully created!");
    
  }
  
}
