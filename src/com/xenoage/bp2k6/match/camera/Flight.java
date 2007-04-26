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

import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.util.Vector3fFlags;
import com.xenoage.bp2k6.util.math.HermiteSpline;

import java.util.ArrayList;
import javax.vecmath.Vector3f;


/**
 * This class contains the waypoints
 * of a camera flight along splines.
 * These are waypoints for the camera position (from)
 * and the camera target (at).
 * 
 * @author Andreas Wenger
 */
public class Flight
{
  
  private String id;
  private ArrayList<Vector3fFlags> pointsFrom = new ArrayList<Vector3fFlags>();
  private ArrayList<Vector3fFlags> pointsAt = new ArrayList<Vector3fFlags>();
  private HermiteSpline splineFrom;
  private HermiteSpline splineAt;
  
  
  /**
   * Constructor.
   */
  public Flight()
  {
    //create "empty" spline
    splineFrom = new HermiteSpline(pointsFrom, 0, 1);
    splineAt = new HermiteSpline(pointsAt, 0, 1);
  }
  
  
  /**
   * Adds a new point to the camera position route.
   */
  public void addFromPoint(Vector3fFlags point)
  {
    pointsFrom.add(point);
  }
  
  
  /**
   * Adds a new point to the camera target route.
   */
  public void addAtPoint(Vector3fFlags point)
  {
    pointsAt.add(point);
  }
  
  
  /**
   * Replaces all points with flags
   * by the "normal" points without flags.
   * @param stadium   The current stadium, or <code>null</code>
   */
  public void replaceFlagPoints(Vector3f currentFrom, Vector3f currentAt,
    Stadium stadium)
  {
    Vector3fFlags pFrom = new Vector3fFlags(currentFrom);
    Vector3fFlags pAt = new Vector3fFlags(currentAt);
    for (int iSpline = 0; iSpline < 2; iSpline++)
    {
      ArrayList<Vector3fFlags> points = (iSpline == 0 ? pointsFrom : pointsAt);
      for (int i = 0; i < points.size(); i++)
      {
        if (points.get(i).flags == Vector3fFlags.FLAG_CURRENTPOS)
        {
          if (points == pointsFrom)
            points.set(i, pFrom);
          else
            points.set(i, pAt);
          //keep in mind that this was a "current position"
          points.get(i).flags = Vector3fFlags.FLAG_CURRENTPOS;
        }
        else if (points.get(i).flags == Vector3fFlags.FLAG_SCOREBOARD)
        {
          if (stadium != null && stadium.getScoreboard() != null)
          {
            //8 m in front of scoreboard
            Vector3fFlags p = new Vector3fFlags(stadium.getScoreboard().getPosition());
            p.y += 3;
            p.scale((p.length() - 8) / p.length());
            points.set(i, p);
          }
          else
          {
            points.set(i, new Vector3fFlags(0, 20, -5));
          }
          //keep in mind that this was a special position
          points.get(i).flags = Vector3fFlags.FLAG_SCOREBOARD;
        }
        else if (points.get(i).flags == Vector3fFlags.FLAG_BEHINDSCOREBOARD)
        {
          if (stadium != null && stadium.getScoreboard() != null)
          {
            //5 m behind of scoreboard
            Vector3fFlags p = new Vector3fFlags(stadium.getScoreboard().getPosition());
            p.y += 3;
            p.scale((p.length() + 5) / p.length());
            points.set(i, p);
          }
          else
          {
            points.set(i, new Vector3fFlags(0, 0, 5));
          }
          //keep in mind that this was a special position
          points.get(i).flags = Vector3fFlags.FLAG_BEHINDSCOREBOARD;
        }
      }
    }
  }
  
  
  /**
   * Sets the start time of the flight in milliseconds.
   */
  public void setStartTime(float startTime)
  {
    splineFrom.setStartTime(startTime);
    splineAt.setStartTime(startTime);
  }
  
  
  /**
   * Gets the camera position point at the given time.
   */
  public Vector3f getFromPointAtTime(float time)
  {
    return splineFrom.getPointAtTime(time);
  }
  
  
  /**
   * Gets the camera target point at the given time.
   */
  public Vector3f getAtPointAtTime(float time)
  {
    return splineAt.getPointAtTime(time);
  }

  
  /**
   * Returns the ID of this flight.
   */
  public String getID()
  {
    return id;
  }

  
  /**
   * Sets the ID of this flight.
   */
  public void setID(String id)
  {
    this.id = id;
  }
  
  
  /**
   * Sets the speed of this flight
   * in points/second.
   */
  public void setSpeed(float speed)
  {
    splineFrom.setSpeed(speed);
    splineAt.setSpeed(speed);
  }
  
  
  /**
   * Sets the loop on or off.
   */
  public void setLoop(boolean loop)
  {
    splineFrom.setLoop(loop);
    splineAt.setLoop(loop);
  }
  
  
}
