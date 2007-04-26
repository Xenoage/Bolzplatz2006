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
package com.xenoage.bp2k6.match.camera;


/**
 * This enum lists the possible camera views.
 * 
 * @author Andreas Wenger
 */
public enum CameraView
{
  Match(1, 100), //normal match view
  Flight(1, 500), //camera flight
  GoalKick(1, 100), //view from goal kick position
  CornerKick(1, 100), //view from the corner
  PenaltyKickFront(1, 100), //penalty kick view from the forward player
  FreeKick(1, 100); //free kick view (behind the free kick shooter)
  
  public float nearValue;
  public float farValue;
  
  private CameraView(float nearValue, float farValue)
  {
    this.nearValue = nearValue;
    this.farValue = farValue;
  }
  
}
