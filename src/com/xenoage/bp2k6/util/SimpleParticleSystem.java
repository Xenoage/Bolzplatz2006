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
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.GameEngine;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;


/**
 * Class for a simple particle system.
 * It emits particles with the given texture,
 * then it is dead.
 * 
 * @author Andreas Wenger
 */
public class SimpleParticleSystem
{
  private IParticleSystemSceneNode node;
  
  private float startTime;
  private boolean emitStopped = false;
  
  //irrlicht objects
  ISceneManager sceneManager = null;
  IVideoDriver videoDriver = null;
  
  
  /**
   * Constructor.
   */
  public SimpleParticleSystem(Vector3f position, String textureFile,
    ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    node = sceneManager.addParticleSystemSceneNode(false, null);
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    node.setParticleSize(new dimension2df(0.6f * u, 0.6f * u));
    IParticleAffector pgaf = node.createGravityAffector(
      new vector3df(0f, -0.3f, 0f), 800);
    node.addAffector(pgaf);
    pgaf.drop();
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
    node.setMaterialTexture(0, videoDriver.getTexture(textureFile));
    node.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
    
    IParticleEmitter emt = node.createBoxEmitter(
      new aabbox3df(-0.5f * u, -0.5f * u, -0.5f * u,
        0.5f * u, 0.5f * u, 0.5f * u),
      new vector3df(0, 0.8f, 0),
        10, 20, new SColor(220, 220, 220, 220),
      new SColor(255, 255, 255, 255), 2000, 3000, 180);
    node.setEmitter(emt);
    emt.drop();
    
    node.setPosition(new vector3df(
      position.x * u, position.y * u, position.z * u));
    
    startTime = GameEngine.getFrameTime(false);
  }
  
  
  /**
   * Call this method about 5 times per second.
   * It may be called more often, but this is not
   * necessary.
   * Returns true when it is dead.
   */
  public boolean step()
  {
    float timeNow = GameEngine.getFrameTime(false);
    if (!emitStopped && timeNow - startTime > 0.2f)
    {
      //stop emitting particles after 0.2 s
      emitStopped = true;
      node.setEmitter(null);
    }
    if (timeNow - startTime > 5f)
    {
      return true;
    }
    return false;
  }
  
  
  /**
   * Removes this object from the scene.
   */
  public void removeFromScene()
  {
    if (node != null)
      node.remove();
    node = null;
  }
  
  
}
