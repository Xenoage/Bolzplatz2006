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
import com.xenoage.bp2k6.util.*;
import java.awt.Color;
import net.sf.jirr.*;


/**
 * Class for a GUI button.
 *
 * @author Andreas Wenger
 */
public class Button
  extends Control
{

  //ID of the screen that follows, if this button is clicked
  private String nextScreen = "";
  //font of the button
  private Font font;
  //caption of the button
  private String caption = "";

  //coordinates of the background image for the button (activated state)
  //[0]: left part, [1]: middle scaleable part, [2]: right part
  private recti imagePositionsFocus[] = new recti[] {
    new recti(0, 0, 25, 66),
    new recti(25, 0, 289, 66),
    new recti(290, 0, 320, 66)};
  //coordinates of the background image for the button (non-activated state)
  //[0]: left part, [1]: middle scaleable part, [2]: right part
  private recti imagePositionsNoFocus[] = new recti[] {
    new recti(0, 70, 25, 136),
    new recti(26, 70, 289, 136),
    new recti(290, 70, 320, 136)};
  
  //text colors
  //private Color colorTextFocus = new Color(220, 0, 0);
  //private Color colorTextNoFocus = new Color(100, 0, 0);
  private Color colorTextFocus = new Color(240, 0, 0);
  private Color colorTextNoFocus = new Color(180, 0, 0);



  public Button(String fontID)
  {
    //a button is enabled by default
    this.setEnabled(true);
    //set font
    setFontByID(fontID);
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
   * Gets the caption of this button.
   */
  public String getCaption()
  {
    return this.caption;
  }


  /**
   * Gets the caption of this button.
   */
  public void setCaption(String caption)
  {
    if (caption == null)
      this.caption = "";
    else
      this.caption = caption;
  }


  /**
   * Gets the ID of the screen that follows, if this button is clicked.
   * @return   ID of the next screen
   */
  public String getNextScreen()
  {
    return this.nextScreen;
  }

  /**
   * Sets the ID of the screen that follows, if this button is clicked
   * @param id   ID of the next screen
   */
  public void setNextScreen(String id)
  {
    this.nextScreen = id;
  }


  /**
   * Draws the <code>Button</code>.
   * @see <code>Control.draw</code>
   */
  @Override void draw(IVideoDriver driver, Point2i offset)
  {
    
    if (!this.visible)
      return;

    boolean focus = false;
    if (parentScreen != null && parentScreen.getFocusControl() == this)
      focus = true; //control has the focus

    Point2i drawPos = new Point2i(position);

    //compute current position
    drawPos = computeCurrentPosition();

    //add offset
    drawPos.add(offset);

    //compute resolution-dependent position
    float scaleX = parentScreen.getUIManager().getScaleX();
    float scaleY = parentScreen.getUIManager().getScaleY();
    Rect2i r = new Rect2i(
      (int) (drawPos.x * scaleX),
      (int) (drawPos.y * scaleY),
      (int) ((drawPos.x + size.x) * scaleX),
      (int) ((drawPos.y + size.y) * scaleY));
    int buttonOuterPartWidth =
      (int) (imagePositionsFocus[0].getWidth() * scaleX);
    
    //animate it when it has the focus
    int animmovex = 0;
    if (focus)
    {
      animmovex = (int) (3 * MathTools.sin(GameEngine.getFrameTime(false) * 10));
      if (animmovex < 0) animmovex = -animmovex;
      r.x1 = r.x1 - animmovex;
      r.y1 = r.y1 - animmovex;
      r.x2 = r.x2 + animmovex;
      r.y2 = r.y2 + animmovex;
    }

    ITexture image = GameEngine.getUIManager().getTemplatesImage();

    if (image != null)
    {
      recti imagePositions[] =
        focus ? imagePositionsFocus : imagePositionsNoFocus;
      //draw left part of the button
      Rect2i rectButtonLeft = new Rect2i(r.x1, r.y1, r.x1 + buttonOuterPartWidth, r.y2);
      driver.draw2DImage(image, Cast.Rect2iToIrrlichtRecti(rectButtonLeft),
        imagePositions[0], null, UIManager.COLOR_WHITE_ARRAY4, true);
      //draw center part of the button
      Rect2i rectButtonCenter = new Rect2i(r.x1 + buttonOuterPartWidth, r.y1,
        r.x2 - buttonOuterPartWidth, r.y2);
      driver.draw2DImage(image, Cast.Rect2iToIrrlichtRecti(rectButtonCenter),
        imagePositions[1], null, UIManager.COLOR_WHITE_ARRAY4, true);
      //draw right part of the button
      Rect2i rectButtonRight = new Rect2i(r.x2 - buttonOuterPartWidth, r.y1, r.x2, r.y2);
      driver.draw2DImage(image, Cast.Rect2iToIrrlichtRecti(rectButtonRight),
        imagePositions[2], null, UIManager.COLOR_WHITE_ARRAY4, true);
      
      //do not animate text position
      rectButtonCenter.x1 += animmovex; 
      //make font 16 px smaller than button
      rectButtonCenter.y1 += 8 * scaleY + animmovex; 
      rectButtonCenter.y2 -= 8 * scaleY + animmovex;
      //color
      Color col = colorTextNoFocus;
      if (focus)
      {
        int colanim = animmovex * 20;
        int red = colorTextFocus.getRed() + colanim;
        if (red > 255) red = 255;
        int green = colorTextFocus.getGreen() + colanim;
        if (green > 255) green = 255;
        int blue = colorTextFocus.getBlue() + colanim;
        if (blue > 255) blue = 255;
        col = new Color(red, green, blue);
      }
      //draw text
      
      font.drawText(caption, rectButtonCenter, col, false);
    }
  }


}
