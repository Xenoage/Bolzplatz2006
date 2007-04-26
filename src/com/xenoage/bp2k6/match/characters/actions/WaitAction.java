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

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.characters.Character;


/**
 * This action lets a Character stop
 * and wait.
 * 
 * @author Andreas Wenger
 */
public class WaitAction
  implements Action
{
  private Character character;
  private float minTime;
  private float startTime;
  
  
  /**
   * Lets the given character wait.
   */
  public WaitAction(Character character)
  {
    this.character = character;
    this.minTime = 0;
  }
  
  
  /**
   * Lets the given character wait at least
   * the given amount of time
   */
  public WaitAction(Character character, float minTime)
  {
    this.character = character;
    this.minTime = minTime;
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
   * Stops the player and let him wait.
   */
  public void perform()
  {
    character.setTarget(null, null);
    startTime = GameEngine.getFrameTime(true);
  }
  
  
  /**
   * If the minimum waiting time is not set,
   * this method returns <code>true</code>, because
   * waiting is always over if there is a new action.
   * If the minimum waiting time is set,
   * <code>true</code> is returned after the waiting
   * time is over.
   */
  public boolean isDone()
  {
    if (minTime == 0f)
      return true;
    else
      return (GameEngine.getFrameTime(true) - startTime >= minTime);
  }
  
}
