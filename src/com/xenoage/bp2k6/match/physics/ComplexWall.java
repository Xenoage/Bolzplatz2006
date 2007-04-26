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
package com.xenoage.bp2k6.match.physics;

import com.xenoage.bp2k6.util.math.Rectangle;


/**
 * A complex wall is a anmoveable set of rectangles that is used
 * for collision detection.
 * Examples for complex walls: posts of a goal, training goal ("Torwand")
 * 
 * @author Andreas Wenger
 */
public class ComplexWall
  implements PhysicsObject
{
  public Rectangle rects[];
  protected float bounce;
  protected int physicsObjectType;
  protected boolean physicsEnabled = true;

  public ComplexWall(Rectangle rects[], float bounce, int physicsObjectType)
  {
    this.rects = rects;
    this.bounce = bounce;
    this.physicsObjectType = physicsObjectType;
  }

  public void setRectangles(Rectangle rects[])
  {
    this.rects = rects;
  }

  public int getRectangleCount()
  {
    return rects.length;
  }

  public Rectangle getRectangle(int i)
  {
    return rects[i];
  }

  public boolean isMoveable()
  {
    return false;
  }


  public boolean cullBackface()
  {
    return false;
  }


  public float getBounce()
  {
    return bounce;
  }

  public int getObjectTypeID()
  {
    return physicsObjectType;
  }


  public void setPhysicsEnabled(boolean enabled)
  {
    this.physicsEnabled = enabled;
  }
  
  
  public boolean isPhysicsEnabled()
  {
    return physicsEnabled;
  }



}
