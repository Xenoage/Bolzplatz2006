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


/**
 * This action lets a Character tumble
 * in the viewing/running direction.
 * 
 * @author Andreas Wenger
 *
 */
public class TumbleAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  
  
  /**
   * This action lets a Character tumble
   * in the viewing/running direction.
   */
  public TumbleAction(com.xenoage.bp2k6.match.characters.Character character)
  {
    this.character = character;
  }
  

  /**
   * Returns <code>false</code>, because this
   * action can not be interrupted immediately.
   */
  public boolean isInterruptible()
  {
    return false;
  }
  
  
  /**
   * Performs the tumble.
   */
  public void perform()
  {
    character.tumble();
  }
  
  
  /**
   * Returns <code>true</code>, when the tumble is over.
   */
  public boolean isDone()
  {
    return !character.getAnimationStage().isTumbling();
  }

}
