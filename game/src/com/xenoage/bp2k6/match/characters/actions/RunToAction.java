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

import com.xenoage.bp2k6.match.characters.Character;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;


/**
 * This action lets a Character run
 * to the given position.
 * Only use it for sequences like
 * goal emotions (e.g. run somewhere, jubilate,
 * run somewhere else).
 * Since it is an action, it can only be interrupted
 * by actions with a higher priority. Use
 * Character.runTo() for "normal" moves.
 * 
 * @author Andreas Wenger
 *
 */
public class RunToAction
  implements Action
{
  
  private Character character;
  private Vector3f position;
  private Speed speed;
  
  
  /**
   * Sets a new target and speed to the given
   * character.
   */
  public RunToAction(Character character, Vector3f position, Speed speed)
  {
    this.character = character;
    this.position = position;
    this.speed = speed;
  }
  

  /**
   * Returns <code>true</code>, because this
   * action can be interrupted immediately. 
   */
  public boolean isInterruptible()
  {
    return true;
  }
  
  
  /**
   * Sets a new target and speed to the given
   * character.
   */
  public void perform()
  {
    character.setTarget(position, speed);
  }
  
  
  /**
   * Returns <code>true</code>, as soon as the character
   * has reached his target (tolerance: 50 cm).
   */
  public boolean isDone()
  {
    return (MathTools.distanceSquared(
      position, character.getPosition()) < 0.5f * 0.5f);
  }

}
