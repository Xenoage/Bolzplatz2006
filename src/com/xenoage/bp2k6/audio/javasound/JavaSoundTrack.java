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
package com.xenoage.bp2k6.audio.javasound;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.MathTools;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;


/**
 * Implementation of a sound track (streamed sound)
 * based on Java Sound with Vorbis SPI.
 *
 * This class stores a sound track, that is is a
 * audio stream that can play one sound effect
 * (always streaming).
 * It is also possible to fade the sound in and out.
 *
 * @author Andreas Wenger
 */
public class JavaSoundTrack
  implements SoundTrack, LineListener
{

  //the audio stream
  private int BUFFER_SIZE = 2048 * 200; //200 kB
  private AudioInputStream ais;
  private byte[] buffer = new byte[BUFFER_SIZE];
  private SourceDataLine line;
  private boolean linePlaying = false;
  
  //volume of the stream
  private float volume = 1;
  
  //repeat mode: on or off
  private boolean repeatMode = false;

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
  
  //status of the current sound
  private static final int STATUS_NOSOUNDLOADED = -1;
  private static final int STATUS_FADEIN = 0;
  private static final int STATUS_PLAYING = 1;
  private static final int STATUS_FADEOUT = 2;
  private static final int STATUS_PAUSED = 3;
  private static final int STATUS_STOPPED = 4;
  private int status = STATUS_NOSOUNDLOADED;
  
  //current volume of the line (value between 0 and 1)
  private float lineVolume = 1;
  
  //track finalized? then also close the update-thread
  private boolean finalized = false;


  JavaSoundTrack()
  {
    volume = 1;
    new UpdateThread().start();
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
    play();
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
  public void run()
  {
    float timeNow = GameEngine.getIrrlichtTime();
    float timeRunning = timeNow - playlistStartTime;
    
    //fade-in time?
    if (status == STATUS_FADEIN)
    {
      if (playlistFadeIn > 0 && timeRunning < playlistFadeIn)
      {
        float fadeFactor = MathTools.clamp(timeRunning / playlistFadeIn, 0, 1);
        setLineVolume(fadeFactor * volume);
        //TEST
        //Logging.log(Logging.LEVEL_MESSAGES, timeRunning + "  " + fadeFactor * volume);
      }
      else
      {
        setLineVolume(volume);
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
        float currentVolume = getLineVolume();
        if (currentVolume != -1 && fadeFactor <= currentVolume)
          setLineVolume(fadeFactor * volume);
        //TEST
        //Logging.log(Logging.LEVEL_MESSAGES, "SOUND: " + timeRunning + "  " + fadeFactor * volume);
      }
      else
      {
        setLineVolume(0);
        stop();
        //play next sound?
        if (playlistNextStartAfterFadeOut)
        {
          loadNextSound();
          
          if (playlistFadeIn > 0)
          {
            status = STATUS_FADEIN;
          }
          else
          {
            setLineVolume(volume);
            status = STATUS_PLAYING;
          }
      
          playlistStartTime = timeNow;
          playlistEndTime = 0;
          
          play();
        }
      }
    }
    
    //TEST
    //System.out.println("runtime: " + (GameEngine.getIrrlichtTime() - timeNow));
    //Logging.log(Logging.LEVEL_MESSAGES, "SOUND: " + status);
    
  }


  /**
   * Plays the sound track. If none is loaded, the next
   * sound track is played, if set.
   */
  public boolean play()
  {
    //TEST
    //Logging.log(Logging.LEVEL_MESSAGES, "SOUND: play()");
    
    if (line == null)
      return false;
    if (status == STATUS_STOPPED || status == STATUS_PAUSED)
      status = STATUS_FADEIN;
    line.start();
    return true;
  }


  /**
   * Stops the sound track.
   */
  public void stop()
  {
    //TEST
    //Logging.log(Logging.LEVEL_MESSAGES, "SOUND: stop()");
    
    if (line == null)
      return;
    status = STATUS_STOPPED;
    line.stop(); //setting back is not possible. just stop.
    
    //TEST
    //Logging.log(Logging.LEVEL_MESSAGES, "SOUND: end stop()");
  }


  /**
   * Pauses the sound track.
   */
  public boolean pause()
  {
    if (line == null)
      return false;
    status = STATUS_PAUSED;
    line.stop();
    return true;
  }


  /**
   * Thread, that updates the streaming buffer.
   */
  private class UpdateThread extends Thread
  {
    
    @Override public void run()
    {
      while (!finalized)
      {
        if (line != null && ais != null)
        {
          try
          {
            //TEST
            //System.out.println(GameEngine.getIrrlichtTime() +
            //  ": Line " + playlistCurrentID + " trying to refill...");
            int readBytes = ais.read(buffer, 0, buffer.length);
            if (readBytes >= 0)
            {
              line.write(buffer, 0, readBytes);
              //TEST
              //System.out.println(GameEngine.getIrrlichtTime() +
              //  ": Line " + playlistCurrentID + " refilled");
            }
            else
            {
              //we reached the end of the sound.
              if (repeatMode && line.isActive())
              {
                //repeat it.
                openInputStream();
                readBytes = ais.read(buffer, 0, buffer.length);
                if (readBytes >= 0)
                  line.write(buffer, 0, readBytes);
              }
            }
          }
          catch (Exception ex)
          {
            //this happens e.g. when the thread is interrupted
            //and the line or ais becomes null. since this
            //will not happen very often, a try-catch-block
            //may be easier than using a monitor.
          }
        }
        try
        {
          sleep(50); //update all 50 ms
        }
        catch (InterruptedException ex)
        {
        }
      }
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
    if (line == null)
      return false;
    else
      return linePlaying;
  }

  
  /**
   * Sets the repeat mode of this sound track.
   * If <code>true</code>, the playlist will be looped,
   * otherwise not (default).
   */
  public void setRepeatMode(boolean b)
  {
    repeatMode = b;
  }
  
  
  /**
   * Loads and activates the next sound immediately
   * without playing it.
   */
  public void loadNextSound()
  {
    if (line != null)
    {
      line.stop();
      line.close();
      line = null;
    }
    status = STATUS_NOSOUNDLOADED;
    
    if (playlistNextID != null)
    {
      
      playlistCurrentID = playlistNextID;
      playlistNextID = null;
      
      Logging.log(Logging.LEVEL_MESSAGES, this, "Stream next sound: \"" +
        playlistCurrentID + "\"");
      
      if (!loadCurrentFile())
        return;
  
      playlistFadeIn = playlistNextFadeIn;
      playlistNextFadeIn = 0;
      playlistFadeOut = playlistNextFadeOut;
      playlistNextFadeOut = 0;
  
    }
    else
    {
      playlistCurrentID = null;
      playlistNextID = null;
    }
    
  }
  
  
  private void setLineVolume(float volume)
  {
    if (line != null)
    {
      if (volume < 0.0002f)
        volume = 0.0002f;
      else if (volume > 1)
        volume = 1;
      lineVolume = volume;
      FloatControl gainControl = (FloatControl)
        line.getControl(FloatControl.Type.MASTER_GAIN);
      gainControl.setValue(20 * (float) Math.log10(lineVolume));
    }
  }
  
  
  private float getLineVolume()
  {
    return lineVolume;
  }
  
  
  /**
   * Loads and starts the current sound
   * stored in playlistCurrentID.
   * Returns true, if everything worked fine,
   * otherwise false.
   */
  private boolean loadCurrentFile()
  {
    if (playlistCurrentID == null)
      return false;
    try
    {
      AudioFormat decodedFormat = openInputStream();
      DataLine.Info info = new DataLine.Info(SourceDataLine.class,
        ais.getFormat(), 1024 * 500); //500 kb should be enough for the buffer
        //((int) ais.getFrameLength() * decodedFormat.getFrameSize()));
      line = (SourceDataLine)AudioSystem.getLine(info);
      line.open(decodedFormat); //TODO: or baseFormat?
      line.addLineListener(this);
      return true;
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "Could not open audio stream: \"data/" + playlistCurrentID + "\":");
      Logging.log(Logging.LEVEL_WARNINGS, this, ex);
      line = null;
      playlistCurrentID = null;
      return false;
    }
  }
  
  
  /**
   * Opens an AudioInputStream for the current sound
   * and puts it into the variable ais.
   * The decoded format is returned.
   */
  private AudioFormat openInputStream()
    throws IOException, UnsupportedAudioFileException
  {
    File file = new File("data/" + playlistCurrentID);
    AudioInputStream baseAIS = AudioSystem.getAudioInputStream(file);
    AudioFormat baseFormat = baseAIS.getFormat();
    AudioFormat decodedFormat = new AudioFormat(
      AudioFormat.Encoding.PCM_SIGNED,
      baseFormat.getSampleRate(),
      16,
      baseFormat.getChannels(),
      baseFormat.getChannels() * 2,
      baseFormat.getSampleRate(),
      false);
    ais = AudioSystem.getAudioInputStream(
      decodedFormat, baseAIS);
    return decodedFormat;
  }
  
  
  public void close()
  {
    setVolume(0);
    if (line != null)
    {
      line.stop();
      line.close();
    }
    finalized = true;
  }


  /**
   * This method is called by the line
   * (because of the LineListener)
   * when it has been started, stopped, opened or closed.
   */
  public void update(LineEvent event)
  {
    if (event.getType().equals(LineEvent.Type.START))
      linePlaying = false;
    else if (event.getType().equals(LineEvent.Type.STOP))
      linePlaying = true;
  }

}
