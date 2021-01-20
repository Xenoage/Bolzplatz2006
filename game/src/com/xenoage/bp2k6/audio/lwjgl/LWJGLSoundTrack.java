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
package com.xenoage.bp2k6.audio.lwjgl;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.MathTools;

import org.lwjgl.audio.*;
import org.lwjgl.audio.vorbis.*;


/**
 * Implementation of a sound track (streamed sound)
 * based on LWJGL/OpenAL with Ogg Vorbis support
 * (by Phillip M. Jordan).
 *
 * This class stores a sound track, that is is a
 * audio stream that can play one sound effect
 * (always streaming).
 * It is also possible to fade the sound in and out.
 *
 * @author Andreas Wenger
 */
public class LWJGLSoundTrack
  implements SoundTrack
{
  //the audio stream
  private StreamPlaylist playlist;
  
  //volume of the stream
  private float volume = 1;

  //id of the current sound
  private String playlistCurrentID;
  //duration (in seconds) of the fade-in
  private float playlistFadeIn;
  //duration (in seconds) of the fade-out
  private float playlistFadeOut;
  //time when sound effect began to fade in
  private float playlistStartTime;
  //time when sound effect began to fade out
  private float playlistEndTime;

  //id of the next sound, that will be played when playNextSound() is called
  private String playlistNextID;
  //duration (in seconds) of the fade-in of the next sound
  private float playlistNextFadeIn;
  //duration (in seconds) of the fade-out of the next sound
  private float playlistNextFadeOut;
  //true, when next sound should automatically be started when
  //the current one was faded out
  private boolean playlistNextStartAfterFadeOut = false;

  //last time update() was called
  private float lastUpdateTime = 0;
  
  //status of the current sound
  private static final int STATUS_NOSOUNDLOADED = -1;
  private static final int STATUS_FADEIN = 0;
  private static final int STATUS_PLAYING = 1;
  private static final int STATUS_FADEOUT = 2;
  private static final int STATUS_PAUSED = 3;
  private static final int STATUS_STOPPED = 4;
  private int status = STATUS_NOSOUNDLOADED;


  LWJGLSoundTrack()
  {
    playlist = new StreamPlaylist();
    playlist.setGain(0);
    playlist.enableRepeatMode(false);
  }


  /**
   * Sets the next sound, without playing it now.
   */
  public void setNextSound(String soundID, float fadeInTime, float fadeOutTime)
  {
    this.playlistNextID = soundID;
    this.playlistNextFadeIn = fadeInTime;
    this.playlistNextFadeOut = fadeOutTime;
  }
  
  
  /**
   * Plays the current sound with fade-in.
   */
  public void fadeIn()
  {
    playlistStartTime = GameEngine.getIrrlichtTime();
    status = STATUS_FADEIN;
    playlist.play();
  }


  /**
   * Fades the current sound out. The next sound (if set)
   * is not automatically played after that (for this
   * use playNextSound()). After fade out the sound
   * will be paused.
   */
  public void fadeOut()
  {
    playlistEndTime = GameEngine.getIrrlichtTime();
    status = STATUS_FADEOUT;
    playlistNextStartAfterFadeOut = false;
  }


  /**
   * Sets the fade-out time of the current sound.
   */
  public void setFadeOutTime(float fadeOutTime)
  {
    this.playlistFadeOut = fadeOutTime;
  }
  
  
  /**
   * Sets the volume (value between 0 and 1).
   */
  public void setVolume(float volume)
  {
    this.volume = volume;
  }



  /**
   * Begin to play the next sound, but fade out the current one,
   * if necessary. If currently no sound is played or its
   * volume is 0, the next sound begins immediately (with fade-in,
   * if set).
   */
  public void playNextSound()
  {
    fadeOut();
    playlistNextStartAfterFadeOut = true;
    run();
  }


  /**
   * Call this method as often as possible
   * (for example in the main loop)
   */
  public synchronized void run()
  {
    //update the streams every 500 ms
    float timeNow = GameEngine.getIrrlichtTime();
    if (timeNow - lastUpdateTime > 0.5f)
    {
      update();
      lastUpdateTime = timeNow;
    }
    float timeRunning = timeNow - playlistStartTime;
    
    //fade-in time?
    if (status == STATUS_FADEIN)
    {
      if (playlistFadeIn > 0 && timeRunning < playlistFadeIn)
      {
        float fadeFactor = MathTools.clamp(timeRunning / playlistFadeIn, 0, 1);
        playlist.setGain(fadeFactor * volume);
      }
      else
      {
        playlist.setGain(volume);
        status = STATUS_PLAYING;
      }
    }
    
    //fade-out time or no sound loaded?
    else if (status == STATUS_FADEOUT || status == STATUS_NOSOUNDLOADED)
    {
      if (status == STATUS_FADEOUT &&
        playlistFadeOut > 0 && timeNow < playlistEndTime + playlistFadeOut)
      {
        float fadeFactor = MathTools.clamp(1f -
          (timeNow - playlistEndTime) / playlistFadeOut, 0, 1);
        //current volume may not be louder than the last one
        if (fadeFactor <= playlist.getGain())
          playlist.setGain(fadeFactor * volume);
      }
      else
      {
        playlist.setGain(0);
        playlist.stop(); //pause();
        status = STATUS_PAUSED;
        //play next sound?
        if (playlistNextStartAfterFadeOut)
        {
          loadNextSound();
          if (playlistCurrentID != null)
          {
            playlist.play();
          }
        }
      }
    }
    
    //playing sound at end?
    if (status == STATUS_PLAYING &&
      playlist.getStatus() == StreamPlaylist.PLAYSTATUS_STOPPED)
    {
      status = STATUS_NOSOUNDLOADED;
      loadNextSound();
      if (playlistCurrentID != null)
        playlist.play();
    }
    
  }


  /**
   * Plays the sound track. If none is loaded, the next
   * sound track is played, if set.
   */
  public boolean play()
  {
    if (status == STATUS_STOPPED || status == STATUS_PAUSED)
      status = STATUS_PLAYING;
    return playlist.play();
  }


  /**
   * Stops the sound track.
   */
  public void stop()
  {
    status = STATUS_STOPPED;
    playlist.stop();
  }


  /**
   * Pauses the sound track.
   */
  public boolean pause()
  {
    status = STATUS_PAUSED;
    return playlist.pause();
  }


  /**
   * Updates the streaming buffer.
   * This method is called about every 500 ms by run().
   */
  private void update()
  {
    try
    {
      playlist.update();
    }
    catch (Exception Ex)
    {
    }
  }


  /**
   * Gets the id of the currently played sound track.
   */
  public String getCurrentSoundID()
  {
    return playlistCurrentID;
  }


  /**
   * Gets the id of the next sound track, or <code>null</code>,
   * if not set.
   */
  public String getNextSoundID()
  {
    return playlistNextID;
  }


  /**
   * Returns <code>true</code>, if the SoundTrack is
   * playing at the moment, otherwise <code>false</code>
   */
  public boolean isPlaying()
  {
    return playlist.getStatus() == playlist.PLAYSTATUS_PLAYING;
  }

  
  /**
   * Sets the repeat mode of this sound track.
   * If <code>true</code>, the playlist will be looped,
   * otherwise not (default).
   */
  public void setRepeatMode(boolean b)
  {
    playlist.enableRepeatMode(b);
  }
  
  
  /**
   * Loads and activates the next sound immediately
   * without playing it.
   */
  public void loadNextSound()
  {
    if (playlistNextID != null)
    {
      float timeNow = GameEngine.getIrrlichtTime();
      playlistCurrentID = playlistNextID;
      playlistNextID = null;
      
      Logging.log(Logging.LEVEL_MESSAGES, this, "Stream next sound: \"" +
        playlistCurrentID + "\"");
  
      playlist.stop();
      playlist.clearPlayList();
      IDataStream sound = Util.CreateStreamFromFile("data/" + playlistCurrentID);
      if (sound == null)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Could not open audio stream: \"data/" + playlistCurrentID + "\"");
        playlistCurrentID = null;
        return;
      }
      playlist.addDataStream(sound);
  
      playlistFadeIn = playlistNextFadeIn;
      playlistNextFadeIn = 0;
      playlistFadeOut = playlistNextFadeOut;
      playlistNextFadeOut = 0;
      if (playlistFadeIn > 0)
      {
        status = STATUS_FADEIN;
      }
      else
      {
        playlist.setGain(volume);
        status = STATUS_PLAYING;
      }
  
      playlistStartTime = timeNow;
      playlistEndTime = 0;
  
    }
    else
    {
      playlistCurrentID = null;
      playlistNextID = null;
    }
  }
  
  
  public void close()
  {
    stop();
  }

}
