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
package com.xenoage.bp2k6.gui;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.gui.font.Font;
import com.xenoage.bp2k6.util.Point2i;
import com.xenoage.bp2k6.util.Rect2i;
import java.awt.Color;
import net.sf.jirr.IVideoDriver;


/**
 * Class for a GUI label.
 *
 * @author   Andi
 */
public class Label
  extends Control
{

  //caption of the label
  protected String caption = "";
  //font of the label
  private Font font;
  //is text (both horizontally and verically) centered?
  private boolean center = true;

  //original color of the label
  private Color color = Color.WHITE; //new Color(190, 230, 125);
  //displayed color of the label
  private Color diplayedColor = Color.WHITE;
  
  //multiline and line height
  private boolean multiLine = false;
  private float lineHeight = 50;
  
  //font height
  public static final int FONTSIZE_16PXSMALLER = 0;
  public static final int FONTSIZE_FULL = 1;
  private int fontSize = FONTSIZE_16PXSMALLER;


  public Label(String fontID)
  {
    setFontByID(fontID);
  }


  /**
   * Gets the caption of this label.
   */
  public String getCaption()
  {
    return this.caption;
  }


  /**
   * Sets the caption of this label.
   */
  public void setCaption(String caption)
  {
    if (caption == null)
      this.caption = "";
    else
      this.caption = caption;
  }
  
  
  /**
   * Sets the color of this label.
   */
  public void setColor(Color color)
  {
    this.color = color;
    diplayedColor = new Color(color.getRGB());
  }
  
  
  /**
   * Sets the displayed color of this label,
   * or <code>null</code> to use the original color.
   */
  public void setDisplayedColor(Color color)
  {
    if (color != null)
      diplayedColor = new Color(color.getRGB());
    else
      diplayedColor = new Color(this.color.getRGB());
  }


  /**
   * Gets the font of this label.
   */
  public Font getFont()
  {
    return this.font;
  }


  /**
   * Sets the font of this label.
   */
  public void setFont(Font font)
  {
    this.font = font;
  }


  /**
   * Sets the font of this label,
   * or empty string for the default font.
   */
  public void setFontByID(String fontID)
  {
    this.font = GameEngine.getUIManager().getFont(fontID);
  }


  /**
   * Gets the center-alignment of this label.
   */
  public boolean getAlignmentCenter()
  {
    return this.center;
  }


  /**
   * Sets the center-alignment of this label.
   */
  public void setAlignmentCenter(boolean center)
  {
    this.center = center;
  }


  /**
   * Draws the <code>Label</code>.
   * @see <code>Control.draw</code>
   */
  @Override void draw(IVideoDriver driver, Point2i offset)
  {
    if (!this.visible)
      return;
    
    if (position == null || font == null)
      return;

    Point2i drawPos = new Point2i(position);

    //compute current position
    drawPos = computeCurrentPosition();

    //add offset
    drawPos.add(offset);

    //compute resolution-dependent position
    float scaleX = parentScreen.getUIManager().getScaleX();
    float scaleY = parentScreen.getUIManager().getScaleY();
    int drawPosXScaled = (int) (drawPos.x * scaleX);
    int drawPosYScaled = (int) (drawPos.y * scaleY);
    Rect2i r = new Rect2i(
      drawPosXScaled, drawPosYScaled,
      drawPosXScaled + (int) (size.x * scaleX),
      drawPosYScaled + (int) (size.y * scaleY));
    //if (r.getHeight() < 35) System.out.println(r.getHeight() + "       " +
    //  drawPos.y + " " + scaleY + " " + size.y);
    
    //make font 16 px smaller than the label
    if (fontSize != FONTSIZE_FULL)
    {
      r.y1 += 8 * scaleY; 
      r.y2 -= 8 * scaleY;
    }
    
    if (!multiLine)
      font.drawText(caption, r, diplayedColor, center);
    else
      font.drawTextMultiLine(caption, r, diplayedColor,
        lineHeight * scaleY);

  }


  @Override public String getValue()
  {
    return caption;
  }


  /**
   * Gets the font size setting for this label
   * (FONTHEIGHT_ constant)
   */
  public int getFontSize()
  {
    return fontSize;
  }


  /**
   * Sets the font size setting for this label
   * (FONTHEIGHT_ constant).
   * Default value is FONTSIZE_16PXSMALLER, which
   * means that in 1024x768 the font height is
   * 16 pixels smaller than the label height.
   */
  public void setFontSize(int fontSize)
  {
    this.fontSize = fontSize;
  }


  /**
   * Gets the height of this multiline label in pixels.
   */
  public float getLineHeight()
  {
    return lineHeight;
  }


  /**
   * Sets the height of this multiline label in pixels.
   */
  public void setLineHeight(float lineHeight)
  {
    this.lineHeight = lineHeight;
  }


  /**
   * Returns <code>true</code>, if this is a multiline
   * label.
   */
  public boolean isMultiLine()
  {
    return multiLine;
  }


  /**
   * Enables or disables the use of multiple
   * lines for this label.
   */
  public void setMultiLine(boolean multiLine)
  {
    this.multiLine = multiLine;
  }



}
