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
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.GameEngine;
import javax.vecmath.Vector3f;
import net.sf.jirr.*;


/**
 * Some useful functions for
 * Irrlich cameras.
 */
public class CameraUtils
{

  private static vector3df cameraFrom = new vector3df();
  private static vector3df cameraAt = new vector3df();
  
  
  /**
   * Sets the position of the camera.
   */
  public static void setCameraPosition(
    ICameraSceneNode camera, Vector3f from, Vector3f at)
  {
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    cameraFrom.set(from.x * u, from.y * u, from.z * u);
    cameraAt.set(at.x * u, at.y * u, at.z * u);
    camera.setPosition(cameraFrom);
    camera.setTarget(cameraAt);
    //this makes the camera
    //better when FPS are low. no idea why. (ask forum?)
    //(at the stadium editor this method made the camera
    //update immediately)
    camera.updateAbsolutePosition();
  }
  
}
