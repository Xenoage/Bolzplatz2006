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

import com.xenoage.bp2k6.util.Rect2i;
import java.awt.Color;


/**
 * Abstract base class for a GUI font.
 * A GUI font can be an Irrlicht font,
 * a bp2k6 font, or any other class
 * that extends this class.
 *
 * @author   Andi
 */
public abstract class Font
{

  //id of this font
  protected String id;

  /**
   * Constructor.
   */
  public Font(String id)
  {
    this.id = id;
  }

  /**
   * Gets the id of this font
   */
  public String getID()
  {
    return id;
  }

  /**
   * Draws a text with this font on the screen
   */
  public abstract void drawText(String text, Rect2i rect, Color color, boolean center);
  
  /**
   * Draws a multiline text with this font on the screen
   */
  public abstract void drawTextMultiLine(
    String text, Rect2i rect, Color color, float lineHeight);

  /**
   * Gets the width of the given text in pixels.
   */
  public abstract int getTextWidth(String text, int fontHeight);



}
