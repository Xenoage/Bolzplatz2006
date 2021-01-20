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
package com.xenoage.bp2k6.match.characters;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.match.characters.enums.AnimationStage;

import net.sf.jirr.*;


/**
 * Interface for classes that store a character mesh and information
 * about it (for example the meaning of its keyframes).
 *
 * @author Andreas Wenger
 */
public interface CharacterMesh
{

  /**
   * Loads a character mesh that conforms with the rules
   * in the design document.
   */
  public void loadMesh(String filename);

  /**
   * Gets the animation speed of the mesh in ?*frames/s.
   */
  public int getAnimationSpeed();

  /**
   * Gets the scale factor of the mesh, that has to be
   * applied so that the player has the right size.
   */
  public Vector3f getScale();

  /**
   * Gets the rotation of the mesh, that has to be applied
   * so that the player looks to the right side.
   */
  public Vector3f getRotation();

  /**
   * Gets the irrlicht mesh or <code>null</code> if no mesh is loaded.
   */
  public IAnimatedMesh getMesh();

  /**
   * Gets the start and end frame of the given animation stage.
   */
  public abstract int[] getFrames(AnimationStage animationStage);
  
  /**
   * Gets the speed factor of the given animation stage.
   * If it is not supported, 0 is returned.
   */
  public abstract float getSpeedFactor(AnimationStage animationStage);
  
}
