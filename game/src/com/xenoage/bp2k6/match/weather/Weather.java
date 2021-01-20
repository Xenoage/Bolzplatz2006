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
package com.xenoage.bp2k6.match.weather;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.audio.SoundTrack;
import com.xenoage.bp2k6.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.sf.jirr.*;


/**
 * Class for weather, skybox and lighting.
 * Possible effects:
 * <ul>
 *   <li>precipitation: rain or snow</li>
 *   <li>fog with any color and density</li>
 *   <li>skybox</li>
 *   <li>ambient and directional light (dynamic for thunderstorm)</li>
 * </ul>
 * There can be two light and skybox situations
 * at the beginning and at the end:
 * If set, a fading is done within a given time.
 * 
 * @author Andreas Wenger
 */
public class Weather
{
  
  //skybox
  private Sky sky = null;
  
  //precipitation
  private enum PrecipitationType { Rain, Snow, None };
  private PrecipitationType precipitation = PrecipitationType.None;
  private int precipitationMinPPS;
  private int precipitationMaxPPS;

  //fog
  private SColor fogColor = null;
  private float fogMinDistance;
  private float fogMaxDistance;
  
  //lights
  private SColorf ambientColor = new SColorf(0.8f, 0.8f, 0.8f, 1f);
  private SColorf directionalColor = new SColorf(1f, 1f, 1f, 1f);
  private ILightSceneNode directionalNode;
  
  //lightnings and thunder
  private SColorf lightingColor = new SColorf(1f, 1f, 1f, 1f);
  private float lightningsPerSecond;
  private boolean lightningShown = false;
  private float lightningStartTime = 0;
  private float lightningDuration = 0;
  private float nextThunderTime = 0;
  private boolean nextThunderPlayed = false;
  
  //stadium lighting
  private boolean stadiumLighting = false;
  
  //ambience
  private String ambienceSound = null;
  private SoundTrack ambience = null;
  
  //crowd factor
  private float crowdFactor = 1;
  
  private float lastStepTime;
  
  private IParticleSystemSceneNode particleSystem;
  private IVideoDriver videoDriver;
  private ISceneManager sceneManager;
  
  
  /**
   * Create weather from xml file.
   */
  public Weather(String id,
    IVideoDriver videoDriver, ISceneManager sceneManager)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading weather \"" + id + "\"...");
    this.videoDriver = videoDriver;
    this.sceneManager = sceneManager;
    try
    {
      Document doc = XMLReader.readFile("data/weather/" + id + ".xml");
      Node root = doc.getFirstChild();
      //sky
      Node eSky = XMLReader.getChildElement(root, "sky");
      if (eSky != null)
      {
        String skyID = XMLReader.readAttributeValue(eSky, "id");
        sky = new Sky(skyID, sceneManager, videoDriver);
      }
      //precipitation
      Node ePrecipitation = XMLReader.getChildElement(root, "precipitation");
      if (ePrecipitation != null)
      {
        String type = XMLReader.readAttributeValue(ePrecipitation, "type");
        if (type.equals("snow"))
          precipitation = PrecipitationType.Snow;
        else if (type.equals("rain"))
          precipitation = PrecipitationType.Rain;
        else
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Unknown precipitation \"" + type + "\"!");
        //not used: precipitationParticlesCount =
        //  XMLReader.readAttributeValueInt(ePrecipitation, "particlescount");
        precipitationMinPPS = XMLReader.readAttributeValueInt(ePrecipitation, "minpps");
        precipitationMaxPPS = XMLReader.readAttributeValueInt(ePrecipitation, "maxpps");
        if (precipitation != PrecipitationType.None)
          createPrecipitation(precipitation,
            precipitationMinPPS, precipitationMaxPPS);
      }
      //fog
      Node eFog = XMLReader.getChildElement(root, "fog");
      if (eFog != null)
      {
        fogColor = new SColor(255,
          XMLReader.readAttributeValueInt(eFog, "red"),
          XMLReader.readAttributeValueInt(eFog, "green"),
          XMLReader.readAttributeValueInt(eFog, "blue"));
        fogMinDistance = XMLReader.readAttributeValueFloat(eFog, "min");
        fogMaxDistance = XMLReader.readAttributeValueFloat(eFog, "max");
        setFog(fogColor, fogMinDistance, fogMaxDistance);
        GameEngine.setBackgroundColor(fogColor);
      }
      else
      {
        GameEngine.getVideoDriver().setFog(
          new SColor(), true, 100000, 200000); //is this really "fog off"?
      }
      //lightning
      Node eLightnings = XMLReader.getChildElement(root, "lightnings");
      if (eLightnings != null)
      {
        int red = XMLReader.readAttributeValueInt(eLightnings, "red");
        int green = XMLReader.readAttributeValueInt(eLightnings, "green");
        int blue = XMLReader.readAttributeValueInt(eLightnings, "blue");
        lightingColor = new SColorf(red / 256f, green / 256f, blue / 256f, 1f);
        lightningsPerSecond = XMLReader.readAttributeValueFloat(eLightnings, "persecond");
      }
      //ambient light
      Node eAmbientLight = XMLReader.getChildElement(root, "ambientlight");
      if (eAmbientLight != null)
      {
        int red = XMLReader.readAttributeValueInt(eAmbientLight, "red");
        int green = XMLReader.readAttributeValueInt(eAmbientLight, "green");
        int blue = XMLReader.readAttributeValueInt(eAmbientLight, "blue");
        ambientColor = new SColorf(red / 256f, green / 256f, blue / 256f, 1f);
        sceneManager.setAmbientLight(ambientColor);
      }
      //directional light
      Node eDirectionalLight = XMLReader.getChildElement(root, "directionallight");
      if (eDirectionalLight != null)
      {
        int red = XMLReader.readAttributeValueInt(eDirectionalLight, "red");
        int green = XMLReader.readAttributeValueInt(eDirectionalLight, "green");
        int blue = XMLReader.readAttributeValueInt(eDirectionalLight, "blue");
        directionalColor = new SColorf(red / 256f, green / 256f, blue / 256f);
        /* TODO: irrlicht says: "Invalid Directional Light Direction". reason: ?
        float x = XMLReader.readAttributeValueFloat(eDirectionalLight, "x");
        float y = XMLReader.readAttributeValueFloat(eDirectionalLight, "y");
        float z = XMLReader.readAttributeValueFloat(eDirectionalLight, "z");
        directionalNode = sceneManager.addLightSceneNode(
          null, new vector3df(0, 0, 0), directionalColor);
        directionalNode.getLightData().setPosition(new vector3df(x, y, z));
        directionalNode.getLightData().setType(E_LIGHT_TYPE.ELT_DIRECTIONAL);
        */
      }
      //stadium lighting
      Node eStadiumLighting = XMLReader.getChildElement(root, "stadiumlighting");
      if (eStadiumLighting != null)
      {
        stadiumLighting = (eStadiumLighting.getTextContent().equals("on"));
      }
      //ambience sound
      Node eAmbienceSound = XMLReader.getChildElement(root, "ambiencesound");
      if (eAmbienceSound != null)
      {
        ambienceSound = eAmbienceSound.getTextContent();
      }
      //crowd factor
      Node eCrowdFactor = XMLReader.getChildElement(root, "crowdfactor");
      if (eCrowdFactor != null)
      {
        crowdFactor = Float.parseFloat(eCrowdFactor.getTextContent());
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "StadiumInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Create the ambience sound, dependent on the weather
   * and the number of spectators.
   */
  public void createAmbienceSound(int spectatorsCount)
  {
    if (ambienceSound != null && ambienceSound.equals("ambience_village.ogg") &&
      spectatorsCount > 30)
    {
      //no village sound if there are more then 30 spectators
    }
    else if (ambienceSound != null)
    {
      float volume = 1;
      float fadeInTime = 0;
      float fadeOutTime = 0;
      try
      {
        ConfigFile conf = new ConfigFile("data/config/weather.xml");
        volume = conf.getValueFloat("volume");
        fadeInTime = conf.getValueFloat("fadein");
        fadeOutTime = conf.getValueFloat("fadeout");
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this, "Could not load weather config!");
      }
      
      //sound track
      ambience = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_WEATHERAMBIENCE);
      ambience.setNextSound(
          "sound/ambience/" + ambienceSound, fadeInTime, fadeOutTime);
      ambience.setRepeatMode(true);
      ambience.setVolume(volume);
      ambience.playNextSound();
    }
  }
  
  
  /**
   * Creates the given precipitation.
   */
  private void createPrecipitation(
    PrecipitationType precipitationType, int minPPS, int maxPPS)
  {
    if (Settings.isVideoLowQuality())
    {
      //low video quality: no precipitation!
    }
    else if (precipitationType == PrecipitationType.Snow)
    {
      //snow
      particleSystem = sceneManager.addParticleSystemSceneNode(false);
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      particleSystem.setParticleSize(new dimension2df(0.2f * u, 0.2f * u));
      float radius = 30;
      IParticleEmitter snowEmitter = particleSystem.createBoxEmitter(
        new aabbox3df(-radius * u, 20 * u, -radius * u,
          radius * u, 30 * u, radius * u),
        new vector3df(0f, -0.003f * u, 0.0f),
        minPPS, maxPPS,
        new SColor(0,255,255,255), new SColor(0,255,255,255),
        12000, 12000, 20);
      particleSystem.setEmitter(snowEmitter);
      snowEmitter.drop();

      particleSystem.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      ITexture t = videoDriver.getTexture("data/meshes/snowflake.png");
      particleSystem.setMaterialTexture(0, t);
      particleSystem.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
      particleSystem.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, false);
    }
    else if (precipitationType == PrecipitationType.Rain)
    {
      //rain
      particleSystem = sceneManager.addParticleSystemSceneNode(false);
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      particleSystem.setParticleSize(new dimension2df(0.05f * u, 0.2f * u));
      float radius = 30;
      IParticleEmitter rainEmitter = particleSystem.createBoxEmitter(
        new aabbox3df(-radius * u, 20 * u, -radius * u,
          radius * u, 30 * u, radius * u),
        new vector3df(-0.001f, -0.008f * u, -0.001f),
        minPPS, maxPPS,
        new SColor(0,255,255,255), new SColor(0,255,255,255),
        3000, 3000, 5);
      particleSystem.setEmitter(rainEmitter);
      rainEmitter.drop();

      particleSystem.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      ITexture t = videoDriver.getTexture("data/meshes/raindrop.png");
      particleSystem.setMaterialTexture(0, t);
      particleSystem.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
      particleSystem.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
    }
  }
  
  
  /**
   * Sets the fog.
   */
  private void setFog(SColor color, float minDistance, float maxDistance)
  {
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    videoDriver.setFog(color, true, u * minDistance, u * maxDistance,
      0, false, false);
    GameEngine.setBackgroundColor(color);
  }
  
  
  
  /**
   * Removes this weather from the scene
   * (removes the directional light scene node,
   * the skybox and the particle system scene node).
   */
  public void removeFromScene()
  {
    if (particleSystem != null)
      particleSystem.remove();
    if (sky != null)
      sky.removeFromScene();
    if (directionalNode != null)
      directionalNode.remove();
    if (ambience != null)
      ambience.fadeOut();
  }
  
  
  /**
   * Call this method in every frame.
   */
  public void step()
  {
    float timeNow = GameEngine.getFrameTime(false);
    if (lastStepTime > 0)
    {
      float deltaTime = timeNow - lastStepTime;
      if (lightningsPerSecond > 0)
      {
        //lightnings
        //hide lightning after 0.2 s
        if (lightningShown && timeNow - lightningStartTime > lightningDuration)
        {
          sceneManager.setAmbientLight(ambientColor);
          lightningShown = false;
        }
        //create new lighting
        if (!lightningShown)
        {
          if (MathTools.random() < deltaTime * lightningsPerSecond)
          {
            sceneManager.setAmbientLight(lightingColor);
            lightningShown = true;
            lightningStartTime = timeNow;
            lightningDuration = MathTools.random(0.2f) + 0.1f;
            if (nextThunderPlayed)
            {
              nextThunderTime = timeNow + 2 - lightningDuration * 5;
              nextThunderPlayed = false;
            }
          }
        }
        //thunder
        if (!nextThunderPlayed && timeNow > nextThunderTime)
        {
          nextThunderPlayed = true;
          SoundEffectPool.Sound.WeatherThunder.play();
        }
      }
    }
    lastStepTime = timeNow;
  }


  /**
   * Returns true if the stadium should be lit.
   */
  public boolean isStadiumLighting()
  {
    return stadiumLighting;
  }
  
  
  /**
   * Activates the stadium lighting. Value between
   * 0 (no light) and 1 (perfectly lit).
   */
  public void activateStadiumLighting(float lightValue)
  {
    float red = ambientColor.getR() + (lightValue *
      (0.95f - ambientColor.getR()));
    float green = ambientColor.getG() + (lightValue *
      (0.95f - ambientColor.getG()));
    float blue = ambientColor.getB() + (lightValue *
      (0.85f - ambientColor.getB()));
    ambientColor = new SColorf(red, green, blue);
    red = lightingColor.getR() + (lightValue *
      (1 - lightingColor.getR()));
    green = lightingColor.getG() + (lightValue *
      (1 - lightingColor.getG()));
    blue = lightingColor.getB() + (lightValue *
      (1 - lightingColor.getB()));
    lightingColor = new SColorf(red, green, blue);
    sceneManager.setAmbientLight(ambientColor);
  }


  /**
   * Gets the crowd factor of this weather.
   */
  public float getCrowdFactor()
  {
    return crowdFactor;
  }
  
  
}
