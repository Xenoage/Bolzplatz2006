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

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.Match.MatchStatus;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.util.*;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;


/**
 * Match camera 1.
 * This camera does not "jump" when the interesting
 * point changes to another position, but it
 * goes "smoothly" (but with constant speed)
 * to the new position.
 *
 * @author Andreas Wenger
 */
public class Camera1
  implements Camera
{
  private ICameraSceneNode camera;
  
  private CameraView cameraView = CameraView.Match;
  private Vector3f cornerKickPos;
  private Vector3f goalKickPos;
  private Vector3f penaltyKickPos;
  
  private Vector3f freeKickPos;
  
  private Vector3f from = new Vector3f();
  private Vector3f at = new Vector3f();
  private Vector3f targetAt = new Vector3f();
  private Vector3f targetFrom = new Vector3f();

  private float speed = 15; //15 m/s

  private float lastUpdateTime = 0;
  
  private Flight flight;
  
  public float distanceZ = 8;
  public float distanceY = 6;
  
  private Match match = null;
  

  
  /**
   * Constructor.
   */
  public Camera1(ISceneManager sceneManager, ICameraSceneNode camera)
  {
    this.camera = camera;
    setCameraView(CameraView.Match);
  }
  

  /**
   * Sets the point the camera should view at.
   */
  public void setTarget(Vector3f target)
  {
    if (target != null)
      this.targetAt.set(target);
  }


  /**
   * When this method is called, the current position
   * and direction is applied to the camera of the
   * <code>GameEngine</code>.
   * If this camera is active, call this method
   * in each frame.
   */
  public void update()
  {
    
    //Flight?
    if (cameraView == CameraView.Flight && flight != null)
    {
      //flight (no smoothing here, because built-in)
      from.set(flight.getFromPointAtTime(GameEngine.getFrameTime(false)));
      at.set(flight.getAtPointAtTime(GameEngine.getFrameTime(false)));
      CameraUtils.setCameraPosition(camera, from, at);
    }
    else if (lastUpdateTime > 0)
    {
      //other view (with smoothing)
      float deltaTime = GameEngine.getFrameTime(false) - lastUpdateTime;
      
      //Goal Kick
      if (cameraView == CameraView.GoalKick &&
        match.getMatchStatus() == MatchStatus.GOALKICK)
      {
        targetFrom.set(goalKickPos);
        float angle = match.getActiveTeam().getPlayerController().getGoalKickAngle();
        Vector3f angleV = MathTools.computeVectorFromYRotation(angle);
        angleV.scale(2);
        targetFrom.sub(angleV);
        targetFrom.y = 1.5f;
        targetAt.set(goalKickPos);
        targetAt.y = 1.5f;
      }
      
      //Corner Kick
      else if (cameraView == CameraView.CornerKick &&
        match.getMatchStatus() == MatchStatus.CORNERKICK)
      {
        targetFrom.set(cornerKickPos);
        float angle = match.getActiveTeam().getPlayerController().getCornerKickAngle();
        Vector3f angleV = MathTools.computeVectorFromYRotation(angle);
        angleV.scale(2);
        targetFrom.sub(angleV);
        targetFrom.y = 1.5f;
        targetAt.set(cornerKickPos);
        targetAt.y = 1.5f;
      }
      
      //Penalty Kick (front view)
      else if (cameraView == CameraView.PenaltyKickFront)
      {
        targetFrom.set(penaltyKickPos);
        targetFrom.x *= 0.8f;
        targetFrom.y = 2.5f;
        targetAt.set(penaltyKickPos);
        targetAt.y = 2f;
      }
      
      //Free Kick (front view)
      else if (cameraView == CameraView.FreeKick &&
        match.getMatchStatus() == MatchStatus.FREEKICK)
      {
        targetFrom.set(freeKickPos);
        float angle = match.getActiveTeam().getPlayerController().getFreeKickAngle();
        Vector3f angleV = MathTools.computeVectorFromYRotation(angle);
        angleV.scale(2);
        targetFrom.sub(angleV);
        targetFrom.y = 1.5f;
        targetAt.set(freeKickPos);
        targetAt.y = 1.5f;
      }
      
      //Match or unknown CameraView
      else if (match != null)
      {
        //if there is a match running, compute interesting position
        computeMatchTarget();
        
        if (match.getMatchStatus() == MatchStatus.GOALEMOTIONS)
          targetFrom.set(targetAt.x, 4, targetAt.z - 6);
        else
          targetFrom.set(targetAt.x, distanceY, targetAt.z - distanceZ); //8, 12 //6, 8
      }
      
      //compute new at
      float deltaAtSquared = MathTools.distanceSquaredWithoutY(targetAt, at);
      if (deltaAtSquared < speed * speed * deltaTime * deltaTime)
      {
        //new position is ok
        at.set(targetAt);
      }
      else
      {
        //too far away. move with <speed> m/s towards it
        Vector3f move = new Vector3f();
        move.sub(targetAt, at);
        MathTools.normalize(move);
        move.scale(speed * deltaTime);
        at.add(move);
      } 
      
      //compute new from
      float deltaFromSquared = MathTools.distanceSquaredWithoutY(targetFrom, from);
      if (deltaFromSquared < speed * speed * deltaTime * deltaTime)
      {
        //new position is ok
        from.set(targetFrom);
      }
      else
      {
        //too far away. move with <speed> m/s towards it
        Vector3f move = new Vector3f();
        move.sub(targetFrom, from);
        MathTools.normalize(move);
        move.scale(speed * deltaTime);
        from.add(move);
      } 
      
      //from must be within 50x30 m in match view
      if (cameraView == CameraView.Match)
      {
        if (from.x > 25)
          from.x = 25;
        else if (from.x < -25)
          from.x = -25;
        if (from.z > 15 - distanceZ)
          from.z = 15 - distanceZ;
        else if (from.z < -15 - distanceZ)
          from.z = -15 - distanceZ;
      }
      
      CameraUtils.setCameraPosition(camera, from, at);
    }
    
    
    lastUpdateTime = GameEngine.getFrameTime(false);
  }
  
  
  /**
   * Sets a flight for the camera and starts it
   * immediately (changes the current CameraView
   * to Flight), if not <code>null<code> and if
   * this flight is not running already.
   * If <code>null</code>, the CameraView changes
   * to Match.
   * @param stadium   The current stadium, or <code>null</code>
   */
  public void setFlight(Flight flight, Stadium stadium)
  {
    if (flight != null)
    {
      if (this.flight == null || !this.flight.getID().equals(flight.getID()))
      {
        this.flight = flight;
        this.flight.replaceFlagPoints(from, at, stadium);
        this.flight.setStartTime(GameEngine.getFrameTime(false));
        cameraView = CameraView.Flight;
      }
    }
    else
    {
      this.flight = null;
      cameraView = CameraView.Match;
    }
  }
  
  
  /**
   * Zoom in.
   */
  public void zoomIn()
  {
    float distance = (float)
      Math.sqrt(distanceY * distanceY + distanceZ * distanceZ); 
    if (distance > 3)
    {
      distanceY -= 0.6;
      distanceZ -= 0.8;
    }
  }
  
  
  /**
   * Zoom out.
   */
  public void zoomOut()
  {
    float distance = (float)
      Math.sqrt(distanceY * distanceY + distanceZ * distanceZ); 
    if (distance < 40)
    {
      distanceY += 0.6;
      distanceZ += 0.8;
    }
  }

  
  /**
   * Computes the interesting point of a soccer match
   * and stores the result in target.
   */
  public void computeMatchTarget()
  {
    //normal situation: show the ball
    Vector3f at = new Vector3f(
      match.getBall().getPosition());
    //special situations:
    float t = GameEngine.getFrameTime(false);
    if (match.getMatchStatus() == MatchStatus.GOALEMOTIONS &&
      t - match.getMatchStatusChangeTime() > 1)
    {
      //goal elations: show the shooter
      at.set(match.getGoalPlayer().getPosition());
    }
    at.y = 0;
    targetAt = at;
  }
  
  
  /**
   * Sets the CameraView and applies it at once.
   */
  public void setCameraView(CameraView cameraView)
  {
    this.cameraView = cameraView;
    //camera.setNearValue(cameraView.nearValue);
    //camera.setNearValue(cameraView.farValue);
    //camera.setFOV(0.1f);
    if (cameraView == CameraView.CornerKick)
      cornerKickPos = new Vector3f(match.getMatchStatusBallPosition());
    else if (cameraView == CameraView.GoalKick)
      goalKickPos = new Vector3f(match.getMatchStatusBallPosition());
    else if (cameraView == CameraView.PenaltyKickFront)
      penaltyKickPos = new Vector3f(match.getMatchStatusBallPosition());
    else if (cameraView == CameraView.FreeKick)
      freeKickPos = new Vector3f(match.getMatchStatusBallPosition());
  }
  
  
  /**
   * Sets the soccer match this camera should show,
   * or <code>null</code> if there is no match.
   */
  public void setMatch(Match match)
  {
    this.match = match;
  }

}
