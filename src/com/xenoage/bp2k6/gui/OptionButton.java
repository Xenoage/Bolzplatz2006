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
import com.xenoage.bp2k6.util.Cast;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.Point2i;
import com.xenoage.bp2k6.util.Rect2i;
import net.sf.jirr.*;
import java.awt.Color;
import java.util.ArrayList;


/**
 * Class for a GUI option button.
 *
 * @author   Andi
 */
public class OptionButton
  extends Control
{

  //font of the optionbutton
  private Font font;
  //caption of the button (followed by ": <Option>")
  private String caption = "";
  //list of the captions of the available options
  private String[] optionsCaption = new String[0];
  //list of the values of the available options
  private String[] optionsValue = new String[0];
  //index of the currently selected option
  private int selectedIndex = -1;

  //actions when user switches the option
  private ArrayList<Action> actionsSwitch = new ArrayList<Action>();

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


  public OptionButton(String fontID)
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
   * Gets the caption of this option button.
   */
  public String getCaption()
  {
    return this.caption;
  }


  /**
   * Gets the caption of this option button.
   */
  public void setCaption(String caption)
  {
    if (caption == null)
      this.caption = "";
    else
      this.caption = caption;
  }


  /**
   * Gets the list of the captions of the available options.
   */
  public String[] getOptionsCaption()
  {
    return this.optionsCaption;
  }


  /**
   * Sets the list of available options.
   * The selected index is set back to 0, but no
   * switch event is raised.
   */
  public void setOptions(String[] captions, String[] values)
  {
    selectedIndex = 0;
    if (captions != null && values != null)
    {
      this.optionsCaption = captions;
      this.optionsValue = values;
    }
    //switch event, but without sound
    for (int i = 0; i < actionsSwitch.size(); i++)
      actionsSwitch.get(i).perform(this, "switch", false);
  }


  /**
   * Gets the index of the selected option.
   */
  public int getSelectedIndex()
  {
    return this.selectedIndex;
  }


  /**
   * Sets the option to be selected by its index.
   */
  public void setSelectedIndex(int selectedIndex)
  {
    if (selectedIndex >= 0 && selectedIndex < optionsCaption.length)
    {
      this.selectedIndex = selectedIndex;
      //switch event, but without sound
      for (int i = 0; i < actionsSwitch.size(); i++)
        actionsSwitch.get(i).perform(this, "switch", false);
    }
    else
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "Invalid selected index \"" + selectedIndex +
        "\" for optionbutton \"" + id + "\"!");
  }


  /**
   * Gets the list of actions connected with a switch on this button
   */
  public ArrayList<Action> getActionSwitch()
  {
    return this.actionsSwitch;
  }

  /**
   * Adds an action connected with a switch on this optionbutton
   */
  public void addActionSwitch(Action action)
  {
    this.actionsSwitch.add(action);
  }


  /**
   * Draws the <code>OptionButton</code>.
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

      //make font 16 px smaller than button
      rectButtonCenter.y1 += 8 * scaleY; 
      rectButtonCenter.y2 -= 8 * scaleY;
      
      //caption-dependent alignment
      if (caption.length() > 0)
      {
        //there is a caption, draw "caption:   value"
        //draw caption
        font.drawText(caption + (optionsCaption.length > 0 ? ":" : ""),
          rectButtonCenter, focus ? colorTextFocus : colorTextNoFocus, false);
        //draw selected option
        if (selectedIndex > -1)
        {
          int textWidth = font.getTextWidth(optionsCaption[selectedIndex],
            rectButtonCenter.getHeight());
          Rect2i rectOption = new Rect2i(
            r.x2 - buttonOuterPartWidth - textWidth, r.y1,
            r.x2 - buttonOuterPartWidth, r.y2);
          
          //make font 16 px smaller than button
          rectOption.y1 += 8 * scaleY; 
          rectOption.y2 -= 8 * scaleY;
          
          font.drawText(optionsCaption[selectedIndex],
            rectOption, focus ? colorTextFocus : colorTextNoFocus, false);
        }
      }
      else
      {
        //there is no caption, draw just "value"
        if (selectedIndex > -1)
        {
          font.drawText(optionsCaption[selectedIndex],
            rectButtonCenter, focus ? colorTextFocus : colorTextNoFocus, false);
        }
      }
    }
  }


  /**
   * Switches the current option (for example 1 means: step to
   * the next option, -2 means: step 2 options back).
   */
  public void switchOption(int steps)
  {
    if (selectedIndex == -1)
      return;
    selectedIndex += steps;
    while (selectedIndex < 0)
      selectedIndex += optionsCaption.length;
    selectedIndex %= optionsCaption.length;
    for (int i = 0; i < actionsSwitch.size(); i++)
      actionsSwitch.get(i).perform(this, "switch");
  }


  /**
   * Gets the value of the option button.
   */
  @Override public String getValue()
  {
    if (selectedIndex == -1)
      return "";
    else
      return optionsValue[selectedIndex];
  }

}
