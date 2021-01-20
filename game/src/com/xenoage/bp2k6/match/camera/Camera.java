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
package com.xenoage.bp2k6.match.camera;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.stadium.Stadium;


/**
 * Interface for match and gui cameras.
 *
 * @author Andreas Wenger
 */
public interface Camera
{

  /**
   * Sets the point the camera should view at.
   */
  public void setTarget(Vector3f target);

  
  /**
   * When this method is called, the current position
   * and direction is applied to the camera of the
   * <code>GameEngine</code>.
   * If this camera is active, call this method
   * in each frame.
   */
  public void update();
  
  
  /**
   * Sets a flight for the camera and starts it
   * immediately (changes the current CameraView
   * to Flight), if not <code>null<code>.
   * If <code>null</code>, the CameraView changes
   * to Match.
   * @param stadium   The current stadium, or <code>null</code>
   */
  public void setFlight(Flight flight, Stadium stadium);
  
  
  /**
   * Sets the CameraView and applies it at once.
   */
  public void setCameraView(CameraView cameraView);
  
  
  /**
   * Sets the soccer match this camera should show,
   * or <code>null</code> if there is no match.
   */
  public void setMatch(Match match);
  

}
