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
package com.xenoage.bp2k6.gui.scene;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;

/**
 * Class for a fireworks show.
 * 
 * @author Andreas Wenger
 */
public class Fireworks
{
  private Vector3f position = new Vector3f();
  
  private int nodesCount = 8;
  private IParticleSystemSceneNode nodes[] =
    new IParticleSystemSceneNode[nodesCount];
  private Vector3f nodesPosition[] = new Vector3f[nodesCount];
  private float nodesStartTime[] = new float[nodesCount];
  private float nodesLifeTime[] = new float[nodesCount];
  private boolean nodesRocketSoundPlayed[] = new boolean[nodesCount];
  
  //irrlicht objects
  ISceneManager sceneManager = null;
  IVideoDriver videoDriver = null;
  
  
  /**
   * Constructor.
   */
  public Fireworks(Vector3f position)
  {
    setPosition(position);
  }
  
  
  /**
   * Adds this fireworks show to the scene.
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    for (int i = 0; i < nodesCount; i++)
    {
      nodes[i] = sceneManager.addParticleSystemSceneNode(false, null);
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      nodes[i].setParticleSize(new dimension2df(0.4f * u, 0.4f * u));
      IParticleAffector paf = nodes[i].createFadeOutParticleAffector();
      nodes[i].addAffector(paf);
      paf.drop();
      IParticleAffector pgaf = nodes[i].createGravityAffector(
        new vector3df(0f, -0.03f, 0f), 1000);
      nodes[i].addAffector(pgaf);
      pgaf.drop();
      nodes[i].setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, false);
      nodes[i].setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, false);
      nodes[i].setMaterialTexture(0, videoDriver.getTexture(
        "data/meshes/fireworks1.png"));
      nodes[i].setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
      nodesStartTime[i] = 0;
      nodesLifeTime[i] = 0;
      nodesPosition[i] = new Vector3f();
    }
    
  }
  
  
  /**
   * Sets the position of this fireworks show.
   */
  public void setPosition(Vector3f position)
  {
    this.position.set(position);
  }
  
  
  /**
   * Call this method about 2 times per second.
   * It may be called more often, but this is not
   * necessary.
   */
  public void step()
  {
    float frameTime = GameEngine.getFrameTime(true);
    for (int i = 0; i < nodesCount; i++)
    {
      if (frameTime - nodesStartTime[i] > nodesLifeTime[i])
      {
        //create new explosion
        nodesStartTime[i] = frameTime;
        nodesLifeTime[i] = 3 + MathTools.random(2);
        nodesPosition[i] = new Vector3f(
          MathTools.randomCenter(5),
          MathTools.randomCenter(5),
          MathTools.randomCenter(5));
        createExplosion(i);
        float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
        nodes[i].setPosition(new vector3df(
          (position.x + nodesPosition[i].x) * u,
          (position.y + nodesPosition[i].y) * u,
          (position.z + nodesPosition[i].z) * u));
        nodes[i].updateAbsolutePosition();
        SoundEffectPool.Sound.FireworksExplosion.play();
        nodesRocketSoundPlayed[i] = false;
      }
      else if (!nodesRocketSoundPlayed[i] &&
        frameTime - nodesStartTime[i] > 0.4f)
      {
        //rocket sound
        SoundEffectPool.Sound.FireworksRocket.play();
        nodesRocketSoundPlayed[i] = true;
      }
      else if (frameTime - nodesStartTime[i] > 0.2f)
      {
        //stop emitting particles after 0.2 s
        nodes[i].setEmitter(null);
      }
    }
  }
  
  
  /**
   * Removes this object from the scene.
   */
  public void removeFromScene()
  {
    for (int i = 0; i < nodesCount; i++)
    {
      if (nodes[i] != null)
        nodes[i].remove();
    }
  }
  
  
  /**
   * Creates an explosion.
   */
  private void createExplosion(int nodeIndex)
  {
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    
    int r = 50 + MathTools.randomInt(150);
    int g = 50 + MathTools.randomInt(150);
    int b = 50 + MathTools.randomInt(150);
    if (r < 150 && g < 150 && b < 150)
    {
      r = 200;
      g = 200;
      b = 200;
    }
    IParticleEmitter emt = nodes[nodeIndex].createBoxEmitter(
      new aabbox3df(-0.05f * u, -0.05f * u, -0.05f * u,
        0.05f * u, 0.05f * u, 0.05f * u),
      new vector3df(0, 0.4f, 0),
        300, 600, new SColor(0, r, g, b),
      new SColor(0, r + 50, g + 50, b + 50),
      (int) (1000 * nodesLifeTime[nodeIndex] - 2000),
      (int) (1000 * nodesLifeTime[nodeIndex]),
      360);
    nodes[nodeIndex].setEmitter(emt);
    emt.drop();
  }
  
  
}
