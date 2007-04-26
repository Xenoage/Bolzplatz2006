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

import com.xenoage.bp2k6.util.*;

import net.sf.jirr.*;


/**
 * Class for an energy bar.
 *
 * @author Andreas Wenger
 */
public class EnergyBar
  extends Control
{

  //value (number between 0 and 10)
  private int value = 5;
  //the picture (left half: full element, right half: empty element)
  private ITexture image = null;
  private int imageWidth = 0;
  private int imageHeight = 0;
  //color to draw the energy bar
  private SColor color = new SColor(255, 255, 255, 255);
  private SColor[] colorArray = new SColor[]{color, color, color, color};
  //native source and destination rects
  private recti rectSrc = new recti();
  private recti rectDest = new recti();

  
  /**
   * Constructor.
   */
  public EnergyBar()
  {
    //load the image
    image = TextureFactory.loadTexture("data/images/gui/energybar.png", false);
    imageWidth = image.getSize().getWidth();
    imageHeight = image.getSize().getHeight();
  }
  
  
  /**
   * Sets the value of the bar
   * (number between 0 and 10).
   */
  public void setValue(int value)
  {
    if (value < 1)
      this.value = 1;
    else if (value > 10)
      this.value = 10;
    else
      this.value = value;
  }


  /**
   * Draws the <code>EnergyBar</code>.
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
    Rect2i r = new Rect2i(
      (int) (drawPos.x * scaleX),
      (int) (drawPos.y * scaleY),
      (int) ((drawPos.x + size.x) * scaleX),
      (int) ((drawPos.y + size.y) * scaleY));

    if (image != null)
    {
      
      for (int i = 0; i < 10; i++)
      {
        if (value > i)
          rectSrc.setRect(1, 1, imageWidth / 2 - 1, imageHeight - 1);
        else
          rectSrc.setRect(imageWidth / 2 + 1, 1, imageWidth - 1, imageHeight - 1);
        rectDest.setRect(r.x1 + i * r.getWidth() / 10 , r.y1,
          r.x1 + (i + 1) * r.getWidth() / 10, r.y2);
        driver.draw2DImage(image, rectDest, rectSrc, null,
          colorArray, true);
      }

        
    }
  }


}
