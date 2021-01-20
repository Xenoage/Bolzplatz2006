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
import com.xenoage.bp2k6.util.Cast;
import com.xenoage.bp2k6.util.Rect2i;
import java.awt.Color;
import net.sf.jirr.*;
import com.xenoage.bp2k6.util.Logging;


/**
 * Class for Irrlicht fonts.
 *
 * @author   Andi
 */
public class IrrlichtFont
  extends Font
{

  //the Irrlicht font, suitable to the current screen resolution
  private IGUIFont font;

  //native Irrlicht objects
  private dimension2di nativeDimension2di = new dimension2di();


  /**
   * Constructor.
   */
  public IrrlichtFont(String id)
  {
    super(id);
    //try to load the version of the font, that fits to the
    //current screen resolution
    dimension2di d = GameEngine.getNativeScreenDimension();
    String fontSize = "";
    String fontFormat = ".png";
    if (d.getWidth() == 1024)
      fontSize = "1024";
    else if (d.getWidth() == 800)
      fontSize = "800";
    else
    {
      fontSize = "800";
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "No fonts for resolution " + d.getWidth() + "x" + d.getHeight() +
        "registered. Using default font size.");
    }
    //load font
    String fontFile = id + "-" + fontSize + fontFormat;
    font = GameEngine.getGUIEnvironment().getFont("data/fonts/" + fontFile);
    if (font == null)
    {
      GameEngine.fatalError(this,
        new Exception("Irrlicht font \"" + fontFile + "\" could not be loaded!"));
    }
    else
    {
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Irrlicht font \"" + fontFile + "\" loaded.");
    }
  }


  /**
   * Draws a text with this Irrlicht font.
   * The text is vertically centered, and the right font size
   * is automatically selected (dependent on the screen resolution).
   */
  @Override public void drawText(String text, Rect2i rect, Color color, boolean center)
  {
    if (font != null)
    {
      recti rectIrr = Cast.Rect2iToIrrlichtRecti(rect);
      SColor colorIrr = Cast.ColorToIrrlichtSColor(color);
      font.draw(text, rectIrr, colorIrr, center, true);
    }
  }


  /**
   * Gets the width of the given text in pixels.
   */
  @Override public int getTextWidth(String text, int fontHeight)
  {
    nativeDimension2di = font.getDimension(text);
    return nativeDimension2di.getWidth();
  }
  
  
  /**
   * Not supported.
   */
  @Override public void drawTextMultiLine(
    String text, Rect2i rect, Color color, float lineHeight)
  {
  }


}
