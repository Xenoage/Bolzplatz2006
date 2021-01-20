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

import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.util.Logging;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;


/**
 * Implementation of a sound effect
 * based on LWJGL/OpenAL with Ogg Vorbis support
 * (by Phillip M. Jordan).
 *
 * This class holds a reference to a sound buffer, its
 * position, velocity and volume. A sound effect is not
 * streamed but completely stored in an OpenAL buffer.
 *
 * @author Andreas Wenger
 */
public class LWJGLSoundEffect
  implements SoundEffect
{

  //sound engine playing this sound
  private LWJGLSoundEngine soundEngine;
  //buffer for sound data
  private LWJGLSoundBuffer soundBuffer;
  
  //position of this sound
  private FloatBuffer sourcePos = BufferUtils.createFloatBuffer(4).put(
    new float[] { 0.0f, 0.0f, 0.0f });
  //velocity of this sound
  private FloatBuffer sourceVel = BufferUtils.createFloatBuffer(4).put(
    new float[] { 0.0f, 0.0f, 0.0f });
  //some constants
  private final int alTrue = AL10.AL_TRUE;
  private final int alFalse = AL10.AL_FALSE;
  
  //current source of this sound effect
  private boolean hasSource = false;
  private int currentSource;
  
  //sound effect properties
  private boolean loop = false;
  private float volume = 1;
  

  //friend-constructor: creation is only possible with SoundEngine
  LWJGLSoundEffect(LWJGLSoundBuffer soundBuffer, LWJGLSoundEngine soundEngine)
  {
    this.soundBuffer = soundBuffer;
    this.soundEngine = soundEngine;
  }

 
  /**
   * Gets the position of the sound.
   */
  public FloatBuffer getPosition()
  {
    return sourcePos;
  }
  

  /**
   * Gets the velocity of this sound.
   */
  public FloatBuffer getVelocity()
  {
    return sourceVel;
  }
  
  
  /**
   * Plays the given sound effect.
   */
  public void play()
  {
    //get free source, if we have not already one
    int sourceIndex = -1;
    if (!hasSource)
    {
      sourceIndex = soundEngine.requestFreeSoundEffectSourceIndex(this);
      if (sourceIndex == -1)
      {
        //all sources in use
        return;
      }
      try
      {
        currentSource = soundEngine.getSoundEffectSource(sourceIndex);
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this, ex);
        return;
      }
      AL10.alSourcei(currentSource, AL10.AL_BUFFER, soundBuffer.getBuffer());
    }
    if (hasSource || sourceIndex != -1)
    {
      hasSource = true;
      //apply settings
      applyLoop();
      applyPosition();
      applyVolume();
      AL10.alSourcePlay(currentSource);
    }
    else
    {
      //no source free at the moment. can not play sound.
      hasSource = false;
    }  
  }
  
  
  /**
   * Stops this sound effect.
   */
  public void stop()
  {
    if (hasSource)
      AL10.alSourceStop(currentSource);
  }
  

  /**
   * Sets the repeat mode on/off.
   */
  public void setLoop(boolean loop)
  {
    this.loop = loop;
    applyLoop();
  }
  
  
  /**
   * Applies the current repeat mode
   * to the OpenAL source.
   */
  private void applyLoop()
  {
    if (hasSource)
      AL10.alSourcei(currentSource, AL10.AL_LOOPING, loop ? alTrue : alFalse);
  }
  

  /**
   * Sets the position of the sound effect. Must be called
   * before calling <code>play()</code>, otherwise it
   * has no effect.
   */
  public void setPosition(float x, float y, float z)
  {
    sourcePos.put(0, x);
    sourcePos.put(1, y);
    sourcePos.put(2, z);
    applyPosition();
  }
  
  
  /**
   * Applies the current position to the OpenAL source.
   */
  private void applyPosition()
  {
    if (hasSource)
      AL10.alSource(currentSource, AL10.AL_POSITION, sourcePos);
  }
  

  /**
   * Sets the volume of the sound effect. Must be called
   * before calling <code>play()</code>, otherwise it
   * has no effect.
   * @param volume   value between 0.0f (silence) and 1.0f (max)
   */
  public void setVolume(float volume)
  {
    this.volume = volume;
    applyVolume();
  }
  
  
  /**
   * Applies the current volume to the OpenAL source.
   */
  public void applyVolume()
  {
    if (hasSource)
      AL10.alSourcef(currentSource, AL10.AL_GAIN, volume);
  }
  
  
  /**
   * Call this method from the sound engine, when
   * this sound effect may not longer use the current
   * source.
   */
  void forgetSource()
  {
    if (hasSource)
      AL10.alSourceStop(currentSource);
    hasSource = false;
  }
  

  /**
   * Clears this sound effect. This method
   * must be called before the class instance is destroyed!
   */
  public void clear()
  {
    //stop the source, so it can be removed in
    //the sound engine
    if (hasSource)
      AL10.alSourceStop(currentSource);
  }
  

  @Override protected void finalize()
  {
    try { clear(); }
    catch (Exception ex) {}
  }


}