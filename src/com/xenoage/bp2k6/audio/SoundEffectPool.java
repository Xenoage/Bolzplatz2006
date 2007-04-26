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
package com.xenoage.bp2k6.audio;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.*;

import java.io.File;
import java.util.ArrayList;


/**
 * Pool for sound effects.
 * 
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM, LOM_2)
 */
public class SoundEffectPool
{
  public enum Sound
  {
    BallLawn("ball_lawn", "ogg"),
    BallMud("ball_mud", "ogg"),
    BallPass("ball_pass", "ogg"),
    BallShoot("ball_shoot", "ogg"),
    BallBoard("ball_board", "ogg"),
    BallNet("ball_net", "ogg"),
    BallAluminum("ball_aluminum", "ogg"),
    BallWood("ball_wood", "ogg"),
    BallGarbageCan("ball_garbagecan", "ogg"),
    BallKeeper("ball_keeper", "ogg"),
    
    WhistleShort("whistle_short", "ogg"),
    WhistleLong("whistle_long", "ogg"),
    Whistle2Short1Long("whistle_2short1long", "ogg"),
    Whistle5Short("whistle_5short", "ogg"),
    WhistleCongested("whistle_congested", "ogg"),
    
    PlayerTackle("player_tackle", "ogg"),
    PlayerTackleHit("player_tacklehit", "ogg"),
    PlayerWheeze("player_gasp", "ogg"),
    PlayerCheer("player_cheer", "ogg"),
    PlayerRail("player_rail", "ogg"),
    PlayerCry("player_cry", "ogg"),
    
    CrowdDrumming("crowd_drumming", "ogg"),
    CrowdFanfare("crowd_fanfare", "ogg"),
    CrowdHorn("crowd_horn", "ogg"),
  
    SpecialBeer("special_beer", "ogg"),
    SpecialMeow("special_meow", "ogg"),
    
    WeatherThunder("weather_thunder", "ogg"),
    
    FireworksExplosion("fireworks_explosion", "ogg"),
    FireworksRocket("fireworks_rocket", "ogg");
    
    private String path = "data/sound/soundfx/";
    private String id;
    private String extension;
    private ArrayList<SoundEffect> soundEffects = new ArrayList<SoundEffect>();
    
    private float volume = 1; 
    private boolean loaded = false;
    
    
    
    /**
     * Constructor.
     */
    Sound(String id, String extension)
    {
      this.id = id;
      this.extension = extension;
    }
    
    
    /**
     * Initializes and loads this sound.
     * @param config   Config file with volume information, or null.
     */
    public void init(ConfigFile config)
    {
      soundEffects.clear();
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Load sound effect(s) \"" + this + "\" from \"" +
        path + id + "\", extension \"" + extension + "\"..." );
      //volume
      if (config != null)
      {
        String sVolume = config.getValue(id, "volume");
        if (sVolume.length() > 0)
          volume = Float.parseFloat(sVolume) * 0.01f;
      }
      load();
    }
    
    
    /**
     * Loads this sound effect. There may be more than
     * one sound for this effect, and all of them
     * are loaded.
     */
    private void load()
    {
      //more than 1 sound for this effect?
      if (new File(path + id + "1." + extension).exists())
      {
        int i = 1;
        while (new File(path + id + i + "." + extension).exists())
        {
          SoundEffect effect = load(i);
          if (effect != null)
          {
            effect.setVolume(volume);
            soundEffects.add(effect);
          }
          i++;
        }
      }
      else
      {
        //single file
        SoundEffect effect = load(0);
        if (effect != null)
        {
          effect.setVolume(volume);
          soundEffects.add(effect);
        }
      }
      loaded = true;
    }
    
    
    /**
     * Loads the given sound file.
     */
    private SoundEffect load(int index)
    {
      String file;
      if (index == 0)
        file = path + id + "." + extension;
      else
        file = path + id + index + "." + extension;
      SoundEffect effect = GameEngine.getSoundEngine().createSoundEffect(file);
      if (file == null)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Sound effect \"" + file + "\" could not be loaded!");
      }
      return effect;
    }
    
    
    /**
     * Plays this sound.
     */
    public void play()
    {
      if (!loaded)
        load(); //load the sound buffer(s) now
      SoundEffect s = null;
      if (soundEffects.size() > 1)
        s = soundEffects.get(MathTools.randomInt(soundEffects.size()));
      else
        s = soundEffects.get(0);
      s.play();
    }
    
  }
  
  public static final int JINGLE_GOAL_LEAGUE1 = 0;
  public static final int JINGLE_GOAL_LEAGUE2 = 1;
  public static final int JINGLE_GOAL_LEAGUE3 = 2;
  public static final int JINGLE_HALFTIME = 3;
  public static final int JINGLE_VICTORY = 4;
  public static final int JINGLE_DRAW = 5;
  public static final int JINGLE_DEFEAT = 6;
  public static final int JINGLE_ENTRANCE = 7; //LOM_2 patch
  public static final int JINGLE_GOAL_RANDOM = 100;
  
  private static final int jingleTypesCount = 8; //LOM_2 patch: 7 -> 8
  private static ArrayList<String> jingles[] = new ArrayList[jingleTypesCount];
  
  private static float jinglesVolume = 1;
  
  
  /**
   * Load all sound effects.
   */
  public static void init()
  {
    //volume config
    ConfigFile soundfxConfig = null;
    try
    {
      soundfxConfig = new ConfigFile("data/config/soundfx.xml");
    }
    catch (Exception ex)
    {
    }
    //sound effects
    for (int i = 0; i < Sound.values().length; i++)
    {
      Sound.values()[i].init(soundfxConfig);
    }
    
    //load lists of all available jingles
    for (int iJingleType = 0; iJingleType < jingleTypesCount; iJingleType++)
    {
      jingles[iJingleType] = new ArrayList<String>();
      String dir = "";
      if (iJingleType == JINGLE_GOAL_LEAGUE1)
        dir = "goal1";
      else if (iJingleType == JINGLE_GOAL_LEAGUE2)
        dir = "goal2";
      else if (iJingleType == JINGLE_GOAL_LEAGUE3)
        dir = "goal3";
      else if (iJingleType == JINGLE_HALFTIME)
        dir = "halftime";
      else if (iJingleType == JINGLE_VICTORY)
        dir = "victory";
      else if (iJingleType == JINGLE_DRAW)
        dir = "draw";
      else if (iJingleType == JINGLE_DEFEAT)
        dir = "defeat";
      
      //LOM_2 patch
      else if (iJingleType == JINGLE_ENTRANCE)
        dir = "entrance/default";
      
      String[] jingleFiles = new File(
        "data/sound/jingles/" + dir).list(FileUtils.getOGGFilter());
      if (jingleFiles != null)
      {
        for (int i = 0; i < jingleFiles.length; i++)
        {
          jingles[iJingleType].add(
            "data/sound/jingles/" + dir + "/" + jingleFiles[i]);
        }
      }
    }
    
    //jingles volume config
    try
    {
      ConfigFile jinglesConfig = new ConfigFile("data/config/jingles.xml");
      jinglesVolume = jinglesConfig.getValueFloat("volume");
    }
    catch (Exception ex)
    {
    }
    
  }
  
  
  /**
   * Gets a random jingle of the given type.
   */
  public static SoundEffect getRandomJingle(int type)
  {
    String jingle = getRandomJingleID(type);
    if (jingle != null)
    {
      SoundEffect ret = GameEngine.getSoundEngine().createSoundEffect(jingle);
      ret.setVolume(jinglesVolume);
      return ret;
    }
    else
    {
      return null;
    }
  }
  
  
  /**
   * Gets the ID of a random jingle of the given type.
   */
  private static String getRandomJingleID(int type)
  {
    if (type == JINGLE_GOAL_RANDOM)
    {
      //random: use goal2 or goal3
      if (MathTools.random() < 0.5)
        type = JINGLE_GOAL_LEAGUE2;
      else
        type = JINGLE_GOAL_LEAGUE3;
    }
    if (jingles[type].size() > 0)
      return (String)
        jingles[type].get(MathTools.randomInt(jingles[type].size()));
    else
      return null;
  }
  
  
  /**
   * LOM patch: Gets the entrance jingle for the given team.
   * 
   * @author Malte Löpmann
   */
  public static SoundEffect getEntranceJingleByTeam(String team)
  {
    String entranceDir = "data/sound/jingles/entrance/";
    String jingle = entranceDir +team +".ogg";    
    
    if(!new File(jingle).exists())
    {
      jingle = getRandomJingleID(JINGLE_ENTRANCE);
    }
    
    if (jingle != null)
    {
      SoundEffect ret = GameEngine.getSoundEngine().createSoundEffect(jingle);
      ret.setVolume(jinglesVolume);
      return ret;
    }
    else
    {
      return null;
    }
  }
  
    
}
