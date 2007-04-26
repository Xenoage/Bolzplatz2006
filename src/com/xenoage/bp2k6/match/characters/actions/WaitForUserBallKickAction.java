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
import com.xenoage.bp2k6.match.characters.Player;


/**
 * This action lets a Player stop
 * and wait until the user kicked the ball
 * (used for user controlled active team when
 * match status is goal kick, corner kick,
 * throw in, free kick or penalty kick).
 * 
 * @author Andreas Wenger
 */
public class WaitForUserBallKickAction
  implements Action
{
  private Player player;
  
  
  /**
   * This action lets a Player stop
   * and wait until the user kicked the ball.
   */
  public WaitForUserBallKickAction(Character character)
  {
    if (character instanceof Player &&
      ((Player)character).getTeam().getPlayerController() != null)
      this.player = (Player) character;
    else
      this.player = null;
  }
  
  
  /**
   * Returns <code>false</code>, because this
   * action can not be interrupted. 
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
    player.setTarget(null, null);
  }
  
  
  /**
   * Returns <code>true</code> after the user kicked
   * the ball (or to say it more technically: when
   * PlayerController.isMatchStatusBallKicked()
   * returns <code>true</code>.
   * If the character is no player or the team is
   * not user-controlled, always <code>true</code> is returned.
   */
  public boolean isDone()
  {
    if (player == null)
      return true;
    else
    {
      return player.getTeam().getPlayerController().isMatchStatusBallKicked();
    }
  }
  
}
