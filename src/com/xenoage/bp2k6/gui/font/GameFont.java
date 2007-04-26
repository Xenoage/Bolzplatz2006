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
package com.xenoage.bp2k6.gui.font;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import net.sf.jirr.ITexture;
import net.sf.jirr.IVideoDriver;
import net.sf.jirr.SColor;

import java.util.List;
import java.util.Hashtable;
import java.awt.Color;

import org.w3c.dom.*;


/**
 * Class for bp2k6 fonts.
 * These fonts are not based on Irrlicht fonts, but use
 * the 2d drawing functions from Irrlicht. They can
 * use an alpha channel (e.g. for antialiasing) and
 * may have a stroke, shadow, and so on.
 * But the color can not be changed.
 * For each font two files are needed: <id>.png for
 * the images and <id>.xml for the letter positions.
 *
 * @author   Andi
 */
public class GameFont
  extends Font
{
  //font texture
  private ITexture textureFont;

  //letter positions
  private Hashtable<Character, Rect2i> letters = new Hashtable<Character, Rect2i>();

  //hight of the font
  private int height = 0;
  
  //native color variable
  private SColor nativeColor = new SColor();
  SColor[] nativeColorArray = new SColor[4];
  
  private IVideoDriver videoDriver;


  /**
   * Constructor.
   */
  public GameFont(String id, IVideoDriver videoDriver)
  {
    super(id);
    this.videoDriver = videoDriver;
    String lang = Language.getCurrentLanguageID();

    //load letter positions from xml file
    try
    {
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Loading game font \"data/fonts/" + lang + "/" + id + ".xml\"...");
      Document doc = XMLReader.readFile("data/fonts/" + lang + "/" + id + ".xml");
      Node root = doc.getFirstChild();
      height = XMLReader.readAttributeValueInt(root, "h");
      if (height == 0) height = 1;
      //letters
      List<Element> listLetters = XMLReader.elements(root, "letter");
      for (int i = 0; i < listLetters.size(); i++)
      {
        Node e = listLetters.get(i);
        char c = XMLReader.readAttributeValue(e, "name").charAt(0);
        int x1 = XMLReader.readAttributeValueInt(e, "x");
        int y1 = XMLReader.readAttributeValueInt(e, "y");
        int x2 = x1 + XMLReader.readAttributeValueInt(e, "w");
        int y2 = y1 + height;
        Rect2i r = new Rect2i(x1, y1 + 1, x2, y2 - 1);
        letters.put(c, r);
      }
    }
    catch (Exception e)
    {
      GameEngine.fatalError(this,
        new Exception("Game font letters from \"" + id + ".xml \" could not be loaded!"));
    }

    //load texture
    String fileName = id + ".png";
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading game font texture \"data/fonts/" + lang + "/" + fileName + "\"...");
    textureFont = TextureFactory.loadTexture(
      "data/fonts/" + lang + "/" + fileName, false);
    if (textureFont == null)
    {
      GameEngine.fatalError(this,
        new Exception("Game font texture \"" + fileName + "\" could not be loaded!"));
    }
    /* //temp: use colorkey for 1024 textures (does not accept alpha channel?!?)
    if (textureFont.getOriginalSize().getWidth() == 1024)
      GameEngine.getVideoDriver().makeColorKeyTexture(textureFont,
        new position2di(0, 0)); */
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Game font \"" + id + "\" loaded.");
  }


  /**
   * Draws a text with this font on the screen.
   * The font is scaled to the height of the given rect.
   */
  @Override public void drawText(String text, Rect2i rect, Color color, boolean center)
  {
    //compute scale factor
    float scale = (float) rect.getHeight() / height;
    //compute x-coordinate where to draw the text
    int posX = 0;
    if (center)
    {
      int textWidth = getTextWidth(text, rect.getHeight());
      posX = rect.x1 + (rect.getWidth() - textWidth) / 2;
    }
    else
    {
      posX = rect.x1;
    }
    //draw all letters
    Rect2i rDest = new Rect2i();
    Rect2i rSrc;
    nativeColor.set(255, color.getRed(), color.getGreen(), color.getBlue());
    for (int i = 0; i < 4; i++)
      nativeColorArray[i] = nativeColor;
    for (int i = 0; i < text.length(); i++)
    {
      rSrc = getLetterRect(text.charAt(i));
      if (rSrc != null)
      {
        rDest.set(posX, rect.y1, posX + (int) (rSrc.getWidth() * scale), rect.y2);
        videoDriver.draw2DImage(textureFont,
          Cast.Rect2iToIrrlichtRecti(rDest), Cast.Rect2iToIrrlichtRecti(rSrc),
          null, nativeColorArray, true);
        posX += (int) (rSrc.getWidth() * scale);
      }
    }
  }
  
  
  /**
   * Draws a multiline text with this font on the screen.
   * The font is scaled to the height of the given rect.
   */
  @Override public void drawTextMultiLine(
    String text, Rect2i rect, Color color, float lineHeight)
  {
    //compute scale factor
    float scale = (float) rect.getHeight() / height;
    //compute x-coordinate where to draw the text
    int posX = rect.x1;
    //compute x-coordinate where to draw the text
    int posY = rect.y1;
    //compute width for all characters
    int w[] = new int[text.length()];
    Rect2i rSrc;
    for (int i = 0; i < w.length; i++)
    {
      rSrc = getLetterRect(text.charAt(i));
      if (rSrc != null)
      {
        w[i] = (int) (rSrc.getWidth() * scale);
      }
    }
    //draw all letters
    Rect2i rDest = new Rect2i();
    boolean firstCharInLine = true;
    nativeColor.set(255, color.getRed(), color.getGreen(), color.getBlue());
    for (int i = 0; i < 4; i++)
      nativeColorArray[i] = nativeColor;
    for (int i = 0; i < text.length(); i++)
    {
      if (firstCharInLine && text.charAt(i) == ' ')
      {
        //ignore first space at the beginning of a line
        firstCharInLine = false;
      }
      else
      {
        //draw character
        rSrc = getLetterRect(text.charAt(i));
        if (rSrc != null)
        {
          rDest.set(posX, posY, posX + w[i], posY + rect.y2 - rect.y1);
          videoDriver.draw2DImage(textureFont,
            Cast.Rect2iToIrrlichtRecti(rDest), Cast.Rect2iToIrrlichtRecti(rSrc),
            null, nativeColorArray, true);
          posX += w[i];
          //if this was a space char ' ', find the next one to
          //get the whole word and find out
          //if there is enough space for it
          if (text.charAt(i) == ' ')
          {
            int i2 = i + 1;
            int wordWidth = 0;
            while (i2 < text.length() && text.charAt(i2) != ' ' &&
              text.charAt(i2) != '\n')
            {
              wordWidth += w[i2];
              i2++;
            }
            if (posX + wordWidth > rect.x2)
            {
              //next line
              posX = rect.x1;
              posY += lineHeight;
              firstCharInLine = true;
            }
          }
          else
          {
            firstCharInLine = false;
          }
        }
        //if this was a newline char ('\n'), new line
        if (text.charAt(i) == '\n')
        {
          posX = rect.x1;
          posY += lineHeight;
          firstCharInLine = true;
        }
      }
    }
  }


  /**
   * Gets the width of the given text in pixels.
   */
  @Override public int getTextWidth(String text, int fontHeight)
  {
    int ret = 0;
    for (int i = 0; i < text.length(); i++)
    {
      Rect2i r = getLetterRect(text.charAt(i));
      if (r != null)
        ret += r.getWidth();
    }
    return (int) (ret * ((float) fontHeight / height));
  }

  
  /**
   * Gets the Rect2i of the given letter
   */
  private Rect2i getLetterRect(char c)
  {
    Rect2i ret = letters.get(c);
    if (ret == null)
    {
      if (c == '\n')
        return null;
      else if (c == 'é' || c == 'è')
        c = 'e';
      else if (c == 'É' || c == 'È')
        c = 'E';
      else if (c == 'Ç')
        c = 'C';
      else if (c == 'ç')
        c = 'c';
      else if (c == 'ô')
        c = 'o';
      else if (c == 'î')
        c = 'i';
      else if (c == 'á' || c == 'à' || c == 'â')
        c = 'a';
      else if (c == 'ä')
        c = 'a';
      else if (c == 'ö')
        c = 'o';
      else if (c == 'ü')
        c = 'u';
      else if (c == 'Ä')
        c = 'A';
      else if (c == 'Ö')
        c = 'O';
      else if (c == 'Ü')
        c = 'U';
      else if (c == 'ß')
        c = 's';
      else if (c == '´' || c == '´')
        c = '\'';
      else
        c = '?';
      ret = letters.get(c);
    }
    return ret;
  }
  
  
  /**
   * Gets a char array with all letters
   * of a bp2k6 font, in the order of the
   * placement in the texture
   */
  public static char[] getAllLetters()
  {
    return new char[] {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
      'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
      'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ü', '©',
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
      'u', 'v', 'w', 'x', 'y', 'z', 'ä', 'ö', 'ü', 'ß',
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
      '.', ',', ';', ':', '_', '!', '?', '"', '\'', '&',
      '+', '-', '*', '/', '=', '€', '$', '§', '@', '%',
      '(', ')', '{', '}', '[', ']', '<', '>', '\\', ' '
    };
  }


}
