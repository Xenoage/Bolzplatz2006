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
 * This interface is for Action classes
 * like running to a certain position,
 * waiting, emotions, and so on.
 * 
 * @author Andreas Wenger
 */
public interface Action
{
  
  /**
   * Returns <code>true</code>, if this action can be
   * immediately interrupted by another one (for example
   * waiting), or <code>false</code> if it must be
   * performed completely (for example tumbling).
   */
  public boolean isInterruptible();
  
  
  /**
   * Call this method when this action should be
   * performed.
   */
  public void perform();
  
  
  /**
   * Returns <code>true</code>, when the action
   * is completely performed.
   */
  public boolean isDone();
  
}
