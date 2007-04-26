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


/**
 * This action lets a Character shoot
 * the given ball into the given goal
 * with the given power.
 * It does not stop the movement of the
 * character.
 * 
 * @author Andreas Wenger
 *
 */
public class ShootAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  private Ball ball;
  private ShootDirection shootDirection;
  private float power;
  private int goal;
  private boolean force;
  
  
  /**
   * Lets a character shoot the given ball into the
   * given goal with the given power
   * between 0 (no power) and 1 (full power).
   * If <code>force</code> is <code>true</code>, the
   * shot is even performed if the character is not in ball
   * possession (otherwise he does not kick it).
   * @param goal       -1: right goal (left team), 1: left goal (right team)
   */
  public ShootAction(com.xenoage.bp2k6.match.characters.Character character,
    Ball ball, ShootDirection shootDirection, float power,
    int goal, boolean force)
  {
    this.character = character;
    this.ball = ball;
    this.shootDirection = shootDirection;
    this.goal = goal;
    this.power = power;
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
   * Performs the shot.
   */
  public void perform()
  {
    character.shoot(ball, shootDirection, power, goal, force);
  }
  
  
  /**
   * Returns <code>true</code>, when the shot is over.
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