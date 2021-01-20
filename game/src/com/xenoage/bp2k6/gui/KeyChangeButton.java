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
import com.xenoage.bp2k6.input.KeyboardEvent;
import com.xenoage.bp2k6.input.KeysAssignment;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.awt.Color;
import net.sf.jirr.*;


/**
 * Class for a GUI button for key assignment change.
 * It shows the name of the current key for a given
 * bp2k6 button (like pass, shoot, run) for a given user.
 * When the button is clicked, it begins to blink
 * and the user can press another key he wants to
 * assign. After that the button does not blink
 * any more and the user can navigate in the GUI
 * again.
 *
 * @author Andreas Wenger
 */
public class KeyChangeButton
  extends Control
{

  //font of the button
  private Font font;
  //bp2k6 key of the button (KeysAssignment.KEY_ constant)
  private int bp2k6Key;
  //user index of the button (0 or 1)
  private int user;
  //status of the button: normal or awaiting key press?
  private boolean awaitingKey;
  //selected key and its caption (displayed name of the key)
  private int key;
  private String caption = "";
  
  //coordinates of the background image for the button (activated state)
  //[0]: left part, [1]: middle scaleable part, [2]: right part
  private recti imagePositionsFocus[] = new recti[] {
    new recti(0, 140, 25, 206),
    new recti(25, 140, 289, 206),
    new recti(290, 140, 320, 206)};
  //coordinates of the background image for the button (non-activated state)
  //[0]: left part, [1]: middle scaleable part, [2]: right part
  private recti imagePositionsNoFocus[] = new recti[] {
    new recti(0, 210, 25, 276),
    new recti(26, 210, 289, 276),
    new recti(290, 210, 320, 276)};
  //text colors
  private Color colorTextFocus = new Color(247, 227, 93);
  private Color colorTextNoFocus = new Color(250, 234, 128);


  /**
   * Constructor.
   */
  public KeyChangeButton(String fontID)
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
   * Gets the bp2k6 key of this button.
   */
  public int getBp2k6Key()
  {
    return this.bp2k6Key;
  }
  
  
  /**
   * Sets the bp2k6 key of this button.
   */
  public void setBp2k6Key(int bp2k6key)
  {
    this.bp2k6Key = bp2k6key;
    this.key = KeysAssignment.getKeyboardKey(this.bp2k6Key, this.user);
    this.caption = KeysAssignment.getKeyboardKeyName(this.key);
    if (this.caption == null)
      this.caption = Language.get("unknown");
  }
  
  
  /**
   * Gets the user of this button.
   */
  public int getUser()
  {
    return this.user;
  }
  
  
  /**
   * Sets the user of this button.
   */
  public void setUser(int user)
  {
    this.user = user;
    this.caption = KeysAssignment.getKeyboardKeyName(
      KeysAssignment.getKeyboardKey(this.bp2k6Key, this.user));
    if (this.caption == null)
      this.caption = Language.get("unknown");
  }


  /**
   * Draws the <code>KeyChangeButton</code>.
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
      //if awaiting key, blink, otherwise draw caption always
      if (!awaitingKey || GameEngine.getFrameTime(false) % 0.5f < 0.3f)
      {
        //draw text
        font.drawText(caption, rectButtonCenter, col, false);
      }
    }
  }


  /**
   * Returns <code>true</code>, when this button waits
   * for the user to press a key.
   */
  public boolean isAwaitingKey()
  {
    return awaitingKey;
  }
  
  
  /**
   * Call this method when the control was clicked.
   */
  @Override public void click()
  {
    super.click();
    awaitingKey = true;
  }
  
  
  /**
   * Call this method when <code>isAwaitingKey</code>
   * returns <code>true</code> and the user pressed
   * a button.
   */
  public void input(KeyboardEvent kbEvent)
  {
    if (kbEvent.bp2k6Key)
      return;
    this.key = kbEvent.keyCode;
    caption = KeysAssignment.getKeyboardKeyName(key);
    if (caption == null)
      caption = Language.get("key") + " " + key;
    awaitingKey = false;
  }


  /**
   * Gets the user and key of this KeyChangeButton
   * in the format "user#bp2k6key#keycode", e.g. "0#1#220".
   */
  @Override public String getValue()
  {
    return user + "#" + bp2k6Key + "#" + key;
  }
  
}
