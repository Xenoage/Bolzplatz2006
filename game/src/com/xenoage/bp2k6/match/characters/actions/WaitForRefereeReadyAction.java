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
import com.xenoage.bp2k6.match.characters.Referee;


/**
 * This action lets a Character stop
 * and wait until the referee is ready
 * for the next action he decided,
 * like a free kick or penalty kick.
 * 
 * @author Andreas Wenger
 */
public class WaitForRefereeReadyAction
  implements Action
{
  private Character character;
  private Referee referee;
  
  
  /**
   * This action lets a Player stop
   * and wait until the referee is ready for
   * the next action he decided (like a penalty kick)
   */
  public WaitForRefereeReadyAction(Character character, Referee referee)
  {
    this.character = character;
    this.referee = referee;
  }
  
  
  /**
   * Returns <code>false</code>, because this
   * action can not be interrupted. 
   */
  public boolean isInterruptible()
  {
    return false;
  }
  
  
  /**
   * Stops the character and let him wait.
   */
  public void perform()
  {
    character.setTarget(null, null);
  }
  
  
  /**
   * Returns <code>true</code> after the referee is ready.
   */
  public boolean isDone()
  {
    return referee.getAI().isReadyToProceed();
  }
  
}
