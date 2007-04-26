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
import com.xenoage.bp2k6.Settings;
import com.xenoage.bp2k6.audio.SoundTrack;
import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.util.*;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import net.sf.jirr.*;


/**
 * This class contain all the spectators in
 * the stadium and coordinates their
 * emotion and special actions
 * (like an audience wave).
 * 
 * @author Andreas Wenger
 */
public class Crowd
{
  
  private float attendance;
  private ArrayList<StandCrowd> crowds = new ArrayList<StandCrowd>();
  
  private int spectatorsCount = 0;
  
  private float lastStepTime = 0;
  private float lastCrowdStepTime = 0;
  private float lastMoodStepTime = 0;
  private float lastBengalLightsStepTime = 0;
  
  private ArrayList<BengalLight> bengalLights = new ArrayList<BengalLight>();
  
  //mood
  private CrowdMood mood;
  
  //ambience
  private SoundTrack ambience[] = new SoundTrack[7];
  private boolean ambienceNeedStop[] = new boolean[7];
  private float ambienceStopTime[] = new float[7];
  private int currentAmbience = 0;
  private float fadeOffset = 0;
  
  
  //irrlicht objects
  ISceneManager sceneManager = null;
  IVideoDriver videoDriver = null;
  
  
  
  /**
   * Creates a (empty) crowd.
   */
  public Crowd(float attendance, int team,
    ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    this.attendance = attendance;
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    mood = new CrowdMood(team);
    
  }
  
  
  /**
   * Adds a new stand crowd.
   */
  public void add(StandCrowd standCrowd)
  {
    crowds.add(standCrowd);
    spectatorsCount += standCrowd.getSpectatorsCount();
  }
  
  
  /**
   * Create the ambience sound, dependent on the number
   * of spectators.
   */
  public void createAmbienceSound()
  {
    int ambienceID = 0;
    try
    {
      ConfigFile crowdConf = new ConfigFile("data/config/crowd.xml");
      String val = crowdConf.getValue("ambience");
      if (val.length() > 0)
      {
        //for debugging: read ambience value from crowd.xml
        ambienceID = Integer.parseInt(val);
      }
      else
      {
        //compute ambience ID
        if (spectatorsCount < 10)
          ambienceID = 0;
        else if (spectatorsCount < 30)
          ambienceID = 1;
        else if (spectatorsCount < 100)
          ambienceID = 2;
        else if (spectatorsCount < 1000)
          ambienceID = 3;
        else
          ambienceID = 4;
      }
    }
    catch (Exception ex)
    {
    }
    
    //crowd settings
    float volume = 1;
    float fadeInTime = 0;
    float fadeOutTime = 0;
    try
    {
      ConfigFile conf = new ConfigFile("data/config/crowd.xml");
      volume = conf.getValueFloat("volume");
      fadeInTime = conf.getValueFloat("fadein");
      fadeOutTime = conf.getValueFloat("fadeout");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_WARNINGS, this, "Could not load crowd config!");
    }
    
    //sound tracks
    for (int i = 0; i < 7; i++)
    {
      ambience[i] = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_STADIUMAMBIENCE_0 + i);
      ambience[i].setNextSound(
        "sound/ambience/crowd-" + ambienceID +
        "-" + i + ".ogg",
        (ambienceID == 0 ? 0 : fadeInTime), //ambience 0: no fade in
        fadeOutTime);
      ambience[i].loadNextSound();
      ambience[i].setVolume(volume);
      //do not loop crowd-0
      if (ambienceID == 0)
        ambience[i].setRepeatMode(false);
      else
        ambience[i].setRepeatMode(true);
      ambienceNeedStop[i] = true;
    }
    currentAmbience = 0;
    fadeOffset = (fadeInTime + fadeOutTime) / 4f;
  }
  
  
  /**
   * Computes the crowd's actions.
   * Call this function in every frame.
   * @param match  The current match, or null.
   */
  public void step(Match match)
  {
    //compute time
    float deltaTime = 0;
    if (lastStepTime > 0)
    {
      float timeNow = GameEngine.getFrameTime(false);
        //true does not work with gui scene
      
      //animate crowd
      deltaTime = timeNow - lastCrowdStepTime;
      if (deltaTime > 0.02f)
      {
        lastCrowdStepTime = timeNow;
        for (int i = 0; i < crowds.size(); i++)
        {
          crowds.get(i).step20ms(mood);
        }
      }
      
      //bengal lights
      deltaTime = timeNow - lastBengalLightsStepTime;
      if (deltaTime > 0.5f && !Settings.isVideoLowQuality())
      {
        lastBengalLightsStepTime = timeNow;
        
        //compute bengal lights
        for (int i = 0; i < bengalLights.size(); i++)
        {
          bengalLights.get(i).step();
          if (bengalLights.get(i).isDead())
          {
            bengalLights.get(i).removeFromScene();
            bengalLights.remove(i);
            i--;
          }
        }
        
        //new bengal light? dependent on number of
        //spectators and the spectators' mood
        if (spectatorsCount > 500 &&
          Math.abs(mood.getMoodValue()) > 7) //only when emotions are high
        {
          if (MathTools.random() < 10.01f && bengalLights.size() < 2 &&
            crowds.size() > 0)
          {
            //90% in the fan curve
            int i = 0;
            if (MathTools.random() < 0.9f)
              i = match.getStadium().getRandomFanCurveStandIndex();
            else
              i = MathTools.randomInt(crowds.size());
            StandCrowd crowd = crowds.get(i);
            if (MathTools.random() < 0.98f)
            {
              if (crowd.getBengalLightPosition() != null &&
                crowd.getBengalLight() == null)
              {
                BengalLight bl = new BengalLight(BengalLight.TYPE_BENGALLIGHT, crowd);
                addBengalLight(bl);
              }
            }
            else
            {
              //smoke bomb
              BengalLight bl = new BengalLight(BengalLight.TYPE_SMOKEBOMB,
                new Vector3f(MathTools.randomCenter(25), 0.3f,
                  MathTools.randomCenter(15)));
              addBengalLight(bl);
            }
          }
        }
      }
      
      //crowd ambience
      deltaTime = timeNow - lastMoodStepTime;
      if (deltaTime > 0.2f)
      {
        lastMoodStepTime = timeNow;
        mood.step(this);
        int newEmotion = mood.getAmbienceIndex();
        //emotion changed and not already in other stream?
        if (newEmotion != currentAmbience)
        {
          //stop the current ambience
          ambienceNeedStop[currentAmbience] = true;
          ambienceStopTime[currentAmbience] = timeNow + fadeOffset;
          //start new emotion
          currentAmbience = newEmotion;
          ambience[currentAmbience].fadeIn();
          ambienceNeedStop[currentAmbience] = false;
        }
        //fade other ambiences out
        for (int i = 0; i < 7; i++)
        {
          if (ambienceNeedStop[i] && timeNow > ambienceStopTime[i])
          {
            ambience[i].fadeOut();
            ambienceNeedStop[i] = false;
          }
        }
      }
      
    }
    lastStepTime = GameEngine.getFrameTime(true);
  }
  
  
  /**
   * Gets the number of spectators.
   */
  public int getSpectatorsCount()
  {
    return spectatorsCount;
  }
  
  
  /**
   * Adds a bengal light.
   */
  public void addBengalLight(BengalLight bengalLight)
  {
    bengalLights.add(bengalLight);
    bengalLight.addToScene(sceneManager, videoDriver);
  }
  
  
  /**
   * Removes this crowd from the scene.
   */
  public void removeFromScene()
  {
    for (int i = 0; i < bengalLights.size(); i++)
    {
      bengalLights.get(i).removeFromScene();
    }
    //fade out crowd ambience
    for (int i = 0; i < 7; i++)
    {
      GameEngine.getSoundTrack(
        GameEngine.SOUNDTRACK_STADIUMAMBIENCE_0 + i).fadeOut();
    }
  }


  /**
   * Gets the mood of the crowd.
   */
  public CrowdMood getMood()
  {
    return mood;
  }


  
  public float getAttendance()
  {
    return attendance;
  }
  
  
  /**
   * Sets the adboard texture (needed for flags).
   */
  public void setAdboardsTexture(ITexture texFlag1, ITexture texFlag2)
  {
    for (int i = 0; i < crowds.size(); i++)
      crowds.get(i).setAdboardsTexture(texFlag1, texFlag2);
  }
  
  
}
