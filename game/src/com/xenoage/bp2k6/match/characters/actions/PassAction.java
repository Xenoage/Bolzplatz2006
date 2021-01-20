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

import com.xenoage.bp2k6.match.Ball;
import com.xenoage.bp2k6.match.characters.enums.*;

import javax.vecmath.Vector3f;


/**
 * This action lets a Character pass
 * the given ball in the given direction
 * with the given power
 * or to the given player.
 * It does not stop the movement of the
 * character.
 * 
 * @author Andreas Wenger
 *
 */
public class PassAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  private Ball ball;
  
  //1. possibility: pass to the given direction with the given power
  private Vector3f direction;
  private float power;
  
  //2. possibility: pass to the given character
  private com.xenoage.bp2k6.match.characters.Character targetCharacter;
  
  private boolean force;
  
  
  /**
   * Lets a character pass the given ball in the
   * given direction (normalized) with the given power
   * between 0 (no power) and 1 (full power).
   * If <code>force</code> is <code>true</code>, the
   * pass is even performed if the character is not in ball
   * possession (otherwise he does not kick it). 
   */
  public PassAction(com.xenoage.bp2k6.match.characters.Character character,
    Ball ball, Vector3f direction, float power, boolean force)
  {
    this.character = character;
    this.ball = ball;
    this.direction = direction;
    this.power = power;
    this.force = force;
  }
  
  
  /**
   * Lets a character pass the given ball
   * to the given character.
   * If <code>force</code> is <code>true</code>, the
   * pass is even performed if the character is not in ball
   * possession (otherwise he does not kick it). 
   */
  public PassAction(com.xenoage.bp2k6.match.characters.Character character,
    Ball ball, com.xenoage.bp2k6.match.characters.Character targetCharacter,
    boolean force)
  {
    this.character = character;
    this.ball = ball;
    this.targetCharacter = targetCharacter;
    this.force = force;
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
   * Performs the pass.
   */
  public void perform()
  {
    if (targetCharacter != null)
    {
      //pass to the given character
      character.pass(ball, targetCharacter, force);
    }
    else
    {
      //pass into the given direction
      character.pass(ball, direction, power, force);
    }
  }
  
  
  /**
   * Returns <code>true</code>, when the pass is over.
   * When running, this applies immediately,
   * if standing, this applies when the kick animation
   * is over.
   */
  public boolean isDone()
  {
    return (character.getAnimationStage() != AnimationStage.ShootStandLeft &&
      character.getAnimationStage() != AnimationStage.ShootStandRight);
  }

}