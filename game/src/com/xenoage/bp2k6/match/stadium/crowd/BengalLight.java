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
package com.xenoage.bp2k6.match.stadium.crowd;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;


/**
 * Class for a bengal light
 * or smoke bomb.
 * 
 * @author Andreas Wenger
 */
public class BengalLight
{
  
  public static final int TYPE_BENGALLIGHT = 0;
  public static final int TYPE_SMOKEBOMB = 1;
  private int type = TYPE_BENGALLIGHT;

  private StandCrowd parentCrowd = null;
  private Vector3f position = new Vector3f();

  private IParticleSystemSceneNode fireNode;
  private IParticleSystemSceneNode smokeNode;
  private ILightSceneNode lightNode;

  private static final int STATUS_ALIVE = 0; //fire with smoke starts
  private static final int STATUS_DYING = 1; //fire put out, smoke becomes thinner
  private static final int STATUS_SMOKE = 2; //some more time for the smoke
  private static final int STATUS_DEAD = 3; //no more fire or smoke visible
  private int status = STATUS_ALIVE;
  private float statusTime = 0;
  private float lifeTime = 20;
  private float dyingTime = 10;
  private float smokeTime = 25;

  //irrlicht objects
  ISceneManager sceneManager = null;
  IVideoDriver videoDriver = null;


  /**
   * Constructor.
   * @param parentCrowd   Parent stand crowd, or null.
   */
  public BengalLight(int type, StandCrowd parentCrowd)
  {
    this.type = type;
    this.parentCrowd = parentCrowd;
    setPosition(parentCrowd.getBengalLightPosition());
  }
  
  
  /**
   * Constructor.
   * @param position   Position of the bengal fire.
   */
  public BengalLight(int type, Vector3f position)
  {
    this.type = type;
    setPosition(position);
  }


  /**
   * Adds this bengal light to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    if (parentCrowd != null)
      parentCrowd.setBengalLight(this);
    
    ISceneNode parentNode =
      (parentCrowd != null ? parentCrowd.getStand().getNode() : null);
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    float u2 = (parentCrowd != null ? 1 : u);
    
    if (type == TYPE_SMOKEBOMB)
      smokeTime = 25;
    else
      smokeTime = 5;

    //fire node
    fireNode = sceneManager.addParticleSystemSceneNode(false, parentNode);

    fireNode.setParticleSize(new dimension2df(0.6f * u, 0.4f * u));

    IParticleEmitter fireEmitter = fireNode.createBoxEmitter(new aabbox3df(-0.3f * u2,
      -0.3f * u2, -0.3f * u2, 0.3f * u2, 0.3f * u2, 0.3f * u2),
      new vector3df(0, 0.08f, 0),
      40, 80, new SColor(0, 255, 100, 80), new SColor(0, 255, 100, 80), 800, 3000, 20);
    fireNode.setEmitter(fireEmitter);
    fireEmitter.drop();

    IParticleAffector paf1 = fireNode.createFadeOutParticleAffector();
    fireNode.addAffector(paf1);
    paf1.drop();

    fireNode.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, false);
    fireNode.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, false);
    fireNode
      .setMaterialTexture(0, videoDriver.getTexture("data/meshes/bengallight1.png"));
    fireNode.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);


    //smoke node
    smokeNode = sceneManager.addParticleSystemSceneNode(false, parentNode);

    smokeNode.setParticleSize(new dimension2df(0.8f * u, 0.8f * u));
    setSmokeEmitter(1f);

    IParticleAffector paf2 = smokeNode.createFadeOutParticleAffector();
    smokeNode.addAffector(paf2);
    paf2.drop();

    smokeNode.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, false);
    smokeNode.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, false);
    smokeNode.setMaterialTexture(0, videoDriver
      .getTexture("data/meshes/bengallight2.png"));
    smokeNode.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);


    //light node
    lightNode = sceneManager.addLightSceneNode(parentNode, new vector3df(), new SColorf(1f,
      0.4f, 0.3f, 1f), 5f * u);

    setPosition(position);

    //set status
    status = STATUS_ALIVE;
    statusTime = GameEngine.getFrameTime(true);
    lifeTime += MathTools.random(10);

  }


  /**
   * Call this method about 2 times per second.
   * It may be called more often, but this is not
   * necessary.
   */
  public void step()
  {
    float frameTime = GameEngine.getFrameTime(true);
    float statusDur = frameTime - statusTime;
    if (status == STATUS_ALIVE)
    {
      //begal light dies after lifeTime seconds
      if (statusDur > lifeTime)
      {
        status = STATUS_DYING;
        statusTime = frameTime;
        fireNode.setEmitter(null); //stop fire
      }
    }
    else if (status == STATUS_DYING)
    {
      //smoke is dying
      float smokeDepth = 1 - (1.0f * statusDur / dyingTime);
      setSmokeEmitter(smokeDepth);
      //light is dying
      lightNode.getLightData().setDiffuseColor(
        new SColorf(1f * smokeDepth, 0.4f * smokeDepth, 0.3f * smokeDepth));
      //smoke dies after dyingTime seconds
      if (statusDur > dyingTime)
      {
        status = STATUS_SMOKE;
        statusTime = frameTime;
        smokeNode.setEmitter(null); //stop smoke
      }
    }
    else if (status == STATUS_SMOKE)
    {
      //smoke has smokeTime seconds to disappear
      if (statusDur > smokeTime)
      {
        status = STATUS_DEAD;
      }
    }
  }


  /**
   * Returns <code>true</code>, if this bengal light
   * is dead and can be removed from scene.
   */
  public boolean isDead()
  {
    return (status == STATUS_DEAD);
  }


  /**
   * Sets the position of this bengal fire.
   */
  public void setPosition(Vector3f position)
  {
    this.position.set(position);
    if (fireNode != null)
    {
      float u = 1;
      if (parentCrowd == null)
       u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      fireNode.setPosition(new vector3df(position.x * u, position.y * u, position.z * u));
      fireNode.updateAbsolutePosition();
      smokeNode
        .setPosition(new vector3df(position.x * u, position.y * u, position.z * u));
      smokeNode.updateAbsolutePosition();
      lightNode
        .setPosition(new vector3df(position.x * u, position.y * u, position.z * u));
      lightNode.updateAbsolutePosition();
    }
  }


  /**
   * Removes this object from the scene.
   */
  public void removeFromScene()
  {
    if (parentCrowd != null)
    {
      parentCrowd.setBengalLight(null);
    }
    else
    {
      if (fireNode != null)
        fireNode.remove();
      if (smokeNode != null)
        smokeNode.remove();
      if (lightNode != null)
        lightNode.remove();
    }
  }


  /**
   * Sets the smoke emitter of the bengal light.
   * @param depth Value between 0 (no smoke) and 1 (full smoke)
   */
  private void setSmokeEmitter(float depth)
  {
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    float u2 = (parentCrowd != null ? 1 : u);
    IParticleEmitter smokeEmitter;
    if (type == TYPE_BENGALLIGHT)
    {
      smokeEmitter = smokeNode.createBoxEmitter(new aabbox3df(-0.01f * u2,
        0.5f * u2, -0.01f * u2, 0.01f * u2, 1.5f * u2, 0.01f * u2), new vector3df(0, 0.1f, 0),
        (int) (50 * depth), (int) (100 * depth), new SColor(0, 100, 80, 80),
        new SColor(0, 100, 80, 80), 500, 5000, 20);
    }
    else //(type == TYPE_SMOKEBOMB)
    {
      smokeEmitter = smokeNode.createBoxEmitter(new aabbox3df(-0.5f * u2,
        0.5f * u2, -0.5f * u2, 0.5f * u2, 2 * u2, 0.5f * u2), new vector3df(0, 0.1f, 0),
        (int) (200 * depth), (int) (300 * depth), new SColor(0, 100, 80, 80),
        new SColor(0, 100, 80, 80), 15000, 25000, (position.z > 2 ? 135 : 100));
    }
    smokeNode.setEmitter(smokeEmitter);
    smokeEmitter.drop();
  }

}
