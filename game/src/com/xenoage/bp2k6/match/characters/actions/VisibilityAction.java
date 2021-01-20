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


/**
 * This is an action which makes
 * a character visible or invisible.
 * 
 * @author Andreas Wenger
 */
public class VisibilityAction
  implements Action
{
  private Character character;
  private boolean visible;
  
  
  /**
   * Makes the given character visible or invisible.
   */
  public VisibilityAction(Character character, boolean visible)
  {
    this.character = character;
    this.visible = visible;
  }
  
  
  public boolean isInterruptible()
  {
    return false;
  }
  
  
  public void perform()
  {
    character.setVisible(visible);
  }
  
  
  /**
   * Returns <code>true</code>, because this action is over at once.
   */
  public boolean isDone()
  {
    return true;
  }
  
}