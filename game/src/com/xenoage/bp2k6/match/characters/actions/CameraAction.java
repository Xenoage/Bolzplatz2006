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
package com.xenoage.bp2k6.match.characters.actions;

import com.xenoage.bp2k6.match.camera.*;


/**
 * This action changes the properties
 * of the given camera.
 * 
 * @author Andreas Wenger
 */
public class CameraAction
  implements Action
{
  private Camera camera;
  private CameraView cameraView;
  
  
  /**
   * Changes the properties of the given camera.
   * @param camera      The camera.
   * @param cameraView  The new cameraView, or <code>null</code> if
   *                    it should not be changed
   */
  public CameraAction(Camera camera, CameraView cameraView)
  {
    this.camera = camera;
    this.cameraView = cameraView;
  }
  

  /**
   * Returns <code>true</code>, because the action
   * needs no time anyway. 
   */
  public boolean isInterruptible()
  {
    return true;
  }
  
  
  /**
   * Changes the camera properties.
   */
  public void perform()
  {
    if (cameraView != null)
      camera.setCameraView(cameraView);
  }
  
  
  /**
   * Returns <code>true</code>, because the action
   * needs no time anyway. 
   */
  public boolean isDone()
  {
    return true;
  }
  
}