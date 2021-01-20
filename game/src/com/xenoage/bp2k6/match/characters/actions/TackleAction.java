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

import com.xenoage.bp2k6.match.characters.enums.*;


/**
 * This action lets a Character tackle
 * in the viewing/running direction
 * with the given speed.
 * 
 * @author Andreas Wenger
 *
 */
public class TackleAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  private Speed speed;
  
  
  /**
   * This action lets a Character tackle
   * in the viewing/running direction with the given speed.
   */
  public TackleAction(com.xenoage.bp2k6.match.characters.Character character,
    Speed speed)
  {
    this.character = character;
    this.speed = speed;
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
   * Performs the tackle.
   */
  public void perform()
  {
    //forget target, and use the current viewing direction for tackling
    character.setTarget(null, null);
    character.tackle(speed);
  }
  
  
  /**
   * Returns <code>true</code>, when the tackle is over.
   */
  public boolean isDone()
  {
    return (character.getAnimationStage() != AnimationStage.Tackle);
  }

}