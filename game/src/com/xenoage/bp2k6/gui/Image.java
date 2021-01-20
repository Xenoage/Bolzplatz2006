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
import com.xenoage.bp2k6.util.Point2i;
import com.xenoage.bp2k6.util.Rect2i;

import net.sf.jirr.*;


/**
 * Class for a GUI image.
 *
 * @author   Andi
 */
public class Image
  extends Control
{

  //the picture to display
  private ITexture image = null;
  //the x and y start positiom of the source image
  private Point2i srcPos = new Point2i();
  //the width and height of the source image
  private Point2i srcSize = new Point2i();
  //transparency of the image
  public enum ImageTransparency { SourceFile, FirstPixel, Magenta };
  private ImageTransparency transparency = ImageTransparency.SourceFile;
  //color to draw the image
  private SColor color = new SColor(255, 255, 255, 255);
  private SColor[] colorArray = new SColor[]{color, color, color, color};
  //source and destination rect
  private recti rectSrc = new recti();
  private recti rectDest = new recti();
  //center of rotation (or null)
  private Point2i pivot = null;
  private position2di nativePivot = new position2di();
  //rotation of the image
  private float rotation = 0;

  public Image()
  {
  }


  /**
   * Gets the image texture.
   */
  public ITexture getImage()
  {
    return this.image;
  }


  /**
   * Sets the image texture.
   */
  public void setImage(ITexture image)
  {
    this.image = image;
    if (transparency == ImageTransparency.FirstPixel)
    {
      GameEngine.getVideoDriver().makeColorKeyTexture(
        this.image, new position2di(0, 0));
    }
    else if (transparency == ImageTransparency.Magenta)
    {
      GameEngine.getVideoDriver().makeColorKeyTexture(
        this.image, new SColor(255, 255, 0, 255));
    }
    
  }


  /**
   * Gets the size of the image texture.
   */
  public Point2i getSrcSize()
  {
    return this.srcSize;
  }
  
  
  /**
   * Gets the source position of the image texture.
   */
  public Point2i getSrcPos()
  {
    return this.srcPos;
  }


  /**
   * Sets the source position of the image texture.
   */
  public void setSrcPos(Point2i srcPos)
  {
    if (srcPos != null)
      this.srcPos.set(srcPos);
  }


  /**
   * Sets the size of the image texture.
   */
  public void setSrcSize(Point2i srcSize)
  {
    if (srcSize != null)
      this.srcSize.set(srcSize);
  }


  /**
   * Sets the transpareny of the image
   */
  public void setTransparency(ImageTransparency transparency)
  {
    if (transparency == null)
      this.transparency = ImageTransparency.SourceFile;
    else
      this.transparency = transparency;
  }


  /**
   * Draws the <code>Image</code>.
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
      //before: srcPos.x + 1 and srcPos.y + 1
      //but this seems to work better:
      rectSrc.setRect(r.x1, r.y1, r.x2, r.y2);
        rectDest.setRect(srcPos.x, srcPos.y,
        srcPos.x + srcSize.x, srcPos.y + srcSize.y);
      
      if (rotation == 0f)
      {
        //no rotation
        driver.draw2DImage(image, rectSrc, rectDest,
          null, colorArray, true);
      }
      else if (pivot == null)
      {
        //rotation around the center
        driver.draw2DImageRotation(image, rectSrc, rectDest,
          rotation, color, true);
      }
      else
      {
        //rotation around the given pivot
        nativePivot.setX(pivot.x);
        nativePivot.setY(pivot.y);
        driver.draw2DImageRotation(image, rectSrc, rectDest,
          nativePivot, rotation, color, true);
      }
    }
  }
  

  
  /**
   * Sets the center of rotation, or <code>null</code>.
   * If <code>null</code>, the image is rotated around
   * its center.
   */
  public void setPivot(Point2i pivot)
  {
    this.pivot = pivot;
  }


  /**
   * Sets the clockwise rotation of the image
   * in radians.
   */
  public void setRotation(float rotation)
  {
    this.rotation = rotation;
  }


}
