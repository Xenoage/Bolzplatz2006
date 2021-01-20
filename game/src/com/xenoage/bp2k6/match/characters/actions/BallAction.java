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

import javax.vecmath.Vector3f;


/**
 * This action sets the ball to the given
 * position and velocity and enables or
 * disables the physics.
 * 
 * @author Andreas Wenger
 */
public class BallAction
  implements Action
{
  private Ball ball;
  private Vector3f position;
  private Vector3f velocity;
  private boolean physicsEnabled;
  
  
  /**
   * Creates the ball action.
   * @param ball     The ball.
   * @param position The new position, or <code>null</code> if
   *                 it should not be changed
   * @param velocity The new velocity, or <code>null</code> if
   *                 it should not be changed
   * @param physicsEnabled Physics on or off
   */
  public BallAction(Ball ball,
    Vector3f position, Vector3f velocity, boolean physicsEnabled)
  {
    this.ball = ball;
    this.position = position;
    this.velocity = velocity;
    this.physicsEnabled = physicsEnabled;
  }
  

  /**
   * Returns <code>true</code>, because the action
   * needs no time anyway. 
   */
  public boolean isInterruptible()
  {
    return true;
  }
  
  
  /**
   * Sets the ball to the given position and velocity.
   */
  public void perform()
  {
    if (position != null)
      ball.setPosition(position);
    if (velocity != null)
      ball.setVelocity(velocity);
    ball.setPhysicsEnabled(physicsEnabled);
  }
  
  
  /**
   * Returns <code>true</code>, because the action
   * needs no time anyway. 
   */
  public boolean isDone()
  {
    return true;
  }
  
}