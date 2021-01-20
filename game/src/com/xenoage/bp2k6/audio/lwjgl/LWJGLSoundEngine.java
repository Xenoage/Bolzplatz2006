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

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.audio.vorbis.Util;


/**
 * Implementation of a sound engine
 * based on LWJGL/OpenAL with Ogg Vorbis support
 * (by Phillip M. Jordan).
 *
 * This class initializes the complete audio engine
 * (based on OpenAL/LWJGL),
 * creates <code>SoundEffect</code> objects from .wav
 * and .ogg files, manages
 * the sound effect sources, and stores
 * the position, velocity and orientation of the listener.
 *
 * @author Andreas Wenger
 */
public class LWJGLSoundEngine
  implements SoundEngine
{

  //position of the listener
  FloatBuffer listenerPos = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

  //velocity of the listener
  FloatBuffer listenerVel = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });

  //orientation of the listener (first 3 elements are "at", second 3 are "up")
  FloatBuffer listenerOri = BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, 1.0f,  0.0f, 1.0f, 0.0f });

  //list of the loaded OpenAL buffers
  ArrayList<LWJGLSoundBuffer> buffers = new ArrayList<LWJGLSoundBuffer>();
  
  //the 5 sound effect sources and the effects that use
  //them at the moment
  private final int sourcesCount = 6; 
  private IntBuffer sources = BufferUtils.createIntBuffer(sourcesCount);
  private LWJGLSoundEffect sourcesEffects[] = new LWJGLSoundEffect[sourcesCount];


  /**
   * Create the sound engine.
   * One sound engine is enough for the whole game.
   */
  public LWJGLSoundEngine()
  {

    Logging.log(Logging.LEVEL_MESSAGES, this, "Creating SoundEngine...");

    //flip buffers to establish their positions and limits
    listenerPos.flip();
    listenerVel.flip();
    listenerOri.flip();

    //create OpenAL
    try {
      //AL.create(null, 15, 22050, true);
      //AL.create(null, 44100, 15, true);
      AL.create();
    }
    catch (LWJGLException ex) {
      Logging.log(Logging.LEVEL_ERRORS, this, "AL.create() failed!");
      Logging.log(this, ex);
      return;
    }
    AL10.alGetError(); //clear error bit

    //set default listener position, velocity and orientation
    AL10.alListener(AL10.AL_POSITION,    listenerPos);
    AL10.alListener(AL10.AL_VELOCITY,    listenerVel);
    AL10.alListener(AL10.AL_ORIENTATION, listenerOri);
    
    //create the 5 sound effect sources
    try
    {
      AL10.alGenSources(sources);
      if (AL10.alGetError() != AL10.AL_NO_ERROR)
        throw new Exception("OpenAL Error: " + AL10.alGetError());
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Sources could not be created: OpenAL error! Details:");
      GameEngine.fatalError(this, ex);
    }

    Logging.log(Logging.LEVEL_MESSAGES, this, "SoundEngine successfully created.");
  }

  
  /**
   * @return   the <code>SoundBuffer</code> with the given id,
   *           or <code>null</code> if no buffer with this id is open
   */
  private LWJGLSoundBuffer searchBuffer(String id)
  {
    for (int i = 0; i < buffers.size(); i++)
      if (buffers.get(i).getID().equals(id))
      {
        Logging.log(Logging.LEVEL_MESSAGES, this, "SoundBuffer \"" + id + "\" already loaded.");
        return buffers.get(i);
      }
    return null;
  }
  
  
  /**
   * Creates a <code>SoundEffect</code> from an already
   * loaded <code>SoundBuffer</code>.
   */
  public LWJGLSoundEffect createSoundEffect(LWJGLSoundBuffer soundBuffer)
  {
    if (soundBuffer == null)
      return null;
    
    LWJGLSoundEffect ret = new LWJGLSoundEffect(soundBuffer, this);
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "SoundEffect with buffer \"" + soundBuffer.getID() + "\" created.");
    return ret;
  }
  

  /**
   * Creates a <code>SoundEffect</code> from a .wav or .ogg file.
   * The sound is completely loaded into a buffer (not streamed),
   * but if it is already open the existing buffer is used.
   * @param id       The relative path of the file, for example "sound/voices/1.wav"
   * @return         A new <code>SoundEffect</code> instance containing the sound
   */
  public LWJGLSoundEffect createSoundEffect(String path)
  {
    LWJGLSoundBuffer soundBuffer = createSoundBuffer(path);
    return createSoundEffect(soundBuffer);
  }
  
  
  /**
   * Creates a <code>SoundBuffer</code> from a .wav or .ogg file.
   * The sound is completely loaded into a buffer (not streamed),
   * but if it is already open the existing buffer is used.
   * @param path     The path of the file, for example "data/sound/soundfx/1.wav"
   * @return         A <code>SoundBuffer</code> instance containing the sound
   */
  public LWJGLSoundBuffer createSoundBuffer(String path)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Open SoundBuffer \"" + path + "\"...");

    //if buffer is not already loaded, open the sound now
    LWJGLSoundBuffer ret = searchBuffer(path);
    if (ret == null)
    {
      int bufferID;
      if (path.toLowerCase().endsWith(".ogg"))
      {
        //open as an Ogg Vorbis file (load complete file into a buffer)
        try
        {
          bufferID = Util.CreateALBufferFromFile(path, 16);
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Could not load ogg file \"" + path + "\"!");
          return null;
        }
      }
      else
      {
        //open as a Wave file
        WaveData waveFile;
        waveFile = WaveData.create(path);
        if (waveFile == null)
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Could not load wave file \"" + path + "\"!");
          return null;
        }
        IntBuffer ib = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(ib);
        bufferID = ib.get(0);
        Logging.log(Logging.LEVEL_MESSAGES, this,
                    "Buffer created. Index: " + bufferID);
        AL10.alBufferData(bufferID, waveFile.format, waveFile.data,
                          waveFile.samplerate);
        waveFile.dispose();
      }
      //create a new SoundBuffer
      ret = new LWJGLSoundBuffer(path, bufferID);
      buffers.add(ret);
      //check if everything worked fine
      if(AL10.alGetError() != AL10.AL_NO_ERROR)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "OpenAL error while creating Soundbuffer: " + AL10.alGetError());
        return null;
      }
      Logging.log(Logging.LEVEL_MESSAGES, this, "SoundBuffer successfully created.");
    }
    return ret;
  }
  
  
  /**
   * Gets the index of a free sound effect source,
   * that means, a source that is not playing
   * at the moment. This source can then be used
   * for the given sound effect.
   * If no source is available,
   * <code>-1</code> is returned.
   */
  int requestFreeSoundEffectSourceIndex(LWJGLSoundEffect soundEffect)
  {
    for (int i = 0; i < sourcesCount; i++)
    {
      if (AL10.alGetSourcei(sources.get(i),
        AL10.AL_SOURCE_STATE) != AL10.AL_PLAYING)
      {
        //remove old sound effect
        if (sourcesEffects[i] != null)
          sourcesEffects[i].forgetSource();
        sourcesEffects[i] = soundEffect;
        return i;
      }
    }
    return -1;
  }
  
  
  /**
   * Gets the sound effect source with the given index.
   * Use only indices returned by
   * <code>getFreeSoundEffectSourceIndex()</code>.
   * find another solution
   */
  int getSoundEffectSource(int index) throws Exception
  {
    return sources.get(index);
  }


  public SoundEffect createSoundEffect(SoundBuffer soundBuffer)
  {
    return null;
  }


  /**
   * Creates a new <code>SoundTrack</code> object
   * for streaming.
   */
  public LWJGLSoundTrack createSoundTrack()
  {
    return new LWJGLSoundTrack();
  }
  
  
  /**
   * Sets the position of the listener.
   */
  public void setListenerPosition(float x, float y, float z)
  {
    listenerPos.put(0, x);
    listenerPos.put(0, y);
    listenerPos.put(0, z);
    AL10.alListener(AL10.AL_POSITION, listenerPos);
  }
  
}
