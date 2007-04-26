/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.GameEngine;

import net.sf.jirr.ICameraSceneNode;
import net.sf.jirr.vector3df;


/**
 * Camera for the stadium editor.
 *
 * @author Andi
 */
public class Camera
{
  private ICameraSceneNode cam = null;
  private vector3df nativeFrom = new vector3df();
  private vector3df nativeAt = new vector3df();
  
  private Vector3f at = new Vector3f();
  
  private float angleY;
  private float angleV;
  private float distance;

  
  /**
   * Constructor
   * @param cam Existing Irrlicht camera this class will use.
   */
  public Camera(ICameraSceneNode cam)
  {
    this.cam = cam;
    this.cam.setNearValue(1 * GameEngine.UNITS_GAME_TO_IRRLICHT);
    this.cam.setFarValue(500 * GameEngine.UNITS_GAME_TO_IRRLICHT);
    //OBSOLETE: this.cam.setFOV(0.015f);
    setDefaultPosition();
  }
  
  
  /**
   * Set camera back to the default position.
   */
  public void setDefaultPosition()
  {
    angleY = -90;
    angleV = 45;
    distance = 60;
    at.set(0, 1, 0);
    updatePosition();
  }
  
  
  /**
   * Zoom camera in.
   */
  public void zoomIn()
  {
    distance -= 5; 
    if (distance < 2)
      distance = 2;
    updatePosition();
  }
  
  
  /**
   * Zoom camera out.
   */
  public void zoomOut()
  {
    distance += 5; 
    if (distance > 200)
      distance = 200;
    updatePosition();
  }
  
  
  /**
   * camera left.
   */
  public void left()
  {
    angleY = (angleY - 5 + 360) % 360; 
    updatePosition();
  }
  
  
  /**
   * camera right.
   */
  public void right()
  {
    angleY = (angleY + 5) % 360; 
    updatePosition();
  }
  
  
  /**
   * camera up.
   */
  public void up()
  {
    angleV += 5; 
    if (angleV > 89)
      angleV = 89;
    updatePosition();
  }
  
  
  /**
   * camera down.
   */
  public void down()
  {
    angleV -= 5; 
    if (angleV < 0)
      angleV = 0;
    updatePosition();
  }
  
  
  /**
   * Updates the Irrlicht camera with the values
   * of angleY, angleV and distance.
   */
  private void updatePosition()
  {
    Vector3f from = new Vector3f();
    from.set((float) (Math.cos(Math.toRadians(angleY))) *
          (float) (Math.cos(Math.toRadians(angleV))),
       (float) (Math.sin(Math.toRadians(angleV))),
       (float) (Math.sin(Math.toRadians(angleY))) *
         (float) (Math.cos(Math.toRadians(angleV))));
    from.scale(distance);
    from.y += 1;
    
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    nativeFrom.set(from.x * u, from.y * u, from.z * u);
    nativeAt.set(at.x * u, at.y * u, at.z * u);
    cam.setPosition(nativeFrom);
    cam.setTarget(nativeAt);
    //no idea why we need the following line, but it works.
    //without it the camera is still on the old position
    //when rendered again (some time later it's right, but
    //when we render directly after this method, it's wrong).
    cam.updateAbsolutePosition();
  }
  
}
