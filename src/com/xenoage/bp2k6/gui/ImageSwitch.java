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

import com.xenoage.bp2k6.gui.imageswitch.Option;
import com.xenoage.bp2k6.util.*;
import java.util.ArrayList;
import net.sf.jirr.*;


/**
 * Class for an imageswitch-Control.
 * This is a clickable image, that
 * changes its picture when clicked,
 * and each picture represents another
 * value.
 * It can e.g. also be used to display
 * values, like different hearts representing
 * the energy of a player. In this case its
 * enabled-property should be set to false,
 * so that the user can not change the
 * picture but only view it.
 *
 * @author   Andi
 */
public class ImageSwitch
  extends Control
{
  //picture that contains all option-images
  private ITexture image = null;
  //list of options (and pictures)
  private ArrayList<Option> options = new ArrayList<Option>();
  //index of the active option
  private int selectedIndex = 0;
  //actions when user switches the option
  private ArrayList<Action> actionsSwitch = new ArrayList<Action>();


  /**
   * Constructor.
   */
  public ImageSwitch()
  {
    //an imageswitch is enabled by default
    this.setEnabled(true);
  }


  /**
   * Gets the picture of this <code>ImageSwitch</code>
   * @return   the texture of this <code>ImageSwitch</code>
   */
  public ITexture getImage()
  {
    return this.image;
  }


  /**
   * Sets the picture of this <code>ImageSwitch</code>
   * @param img   the texture of this <code>ImageSwitch</code>
   */
  public void setImage(ITexture img)
  {
    this.image = img;
  }


  /**
   * Gets an option.
   */
  public Option getOption(int index)
  {
    try
    {
      return this.options.get(index);
    }
    catch (Exception Ex)
    {
      return null;
    }
  }


  /**
   * Adds a new option to the <code>ImageSwitch</code>.
   */
  public void addOption(Option option)
  {
    try
    {
      this.options.add(option);
    }
    catch (Exception Ex)
    {
    }
  }


  /**
   * Draws this <code>ImageSwitch</code>.
   * @see <code>Control.draw</code>
   */
  @Override void draw(IVideoDriver driver, Point2i offset)
  {
    
    if (!this.visible)
      return;

    Point2i drawPos = new Point2i(position);

    //compute current position
    drawPos = computeCurrentPosition();

    //add offset
    drawPos.add(offset);

    //compute resolution-dependent position
    float scaleX = parentScreen.getUIManager().getScaleX();
    float scaleY = parentScreen.getUIManager().getScaleY();
    Rect2i rDest = new Rect2i(
      (int) (drawPos.x * scaleX),
      (int) (drawPos.y * scaleY),
      (int) ((drawPos.x + size.x) * scaleX),
      (int) ((drawPos.y + size.y) * scaleY));

    if (image != null)
    {
      Rect2i rSrc = options.get(selectedIndex).getSrcPos();
      driver.draw2DImage(image, Cast.Rect2iToIrrlichtRecti(rDest),
        Cast.Rect2iToIrrlichtRecti(rSrc), null, UIManager.COLOR_WHITE_ARRAY4, true);
    }
  }


  /**
   * Sets the option to be selected by its index.
   */
  public void setSelectedIndex(int selectedIndex)
  {
    if (selectedIndex > -1 && selectedIndex < options.size())
      this.selectedIndex = selectedIndex;
  }


  /**
   * Adds an action connected with a switch on this imageswitch
   */
  public void addActionSwitch(Action action)
  {
    this.actionsSwitch.add(action);
  }


  /**
   * Switches the current option (for example 1 means: step to
   * the next option, -2 means: step 2 options back)
   */
  public void switchOption(int steps)
  {
    selectedIndex += steps;
    while (selectedIndex < 0)
      selectedIndex += options.size();
    selectedIndex %= options.size();
    for (int i = 0; i < actionsSwitch.size(); i++)
      actionsSwitch.get(i).perform(this, "switch");
  }


  /**
   * Gets the value of the option button.
   */
  @Override public String getValue()
  {
    return options.get(selectedIndex).getValue();
  }




}
