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

import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.util.Logging;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.*;


/**
 * Implementation of a sound engine
 * based on Java Sound with Vorbis SPI
 * (http://www.javazoom.net/vorbisspi/vorbisspi.html)
 * for Ogg/Vorbis support.
 *
 * This class initializes the complete audio engine,
 * creates <code>SoundEffect</code> objects from .wav
 * and .ogg files, manages
 * the sound effect sources, and stores
 * the position, velocity and orientation of the listener.
 * (TODO)
 *
 * @author Andreas Wenger
 */
public class JavaSoundEngine
  implements SoundEngine
{

  //list of the loaded OpenAL buffers
  ArrayList<JavaSoundBuffer> buffers = new ArrayList<JavaSoundBuffer>();


  /**
   * Create the sound engine.
   * One sound engine is enough for the whole game.
   */
  public JavaSoundEngine()
  {

    Logging.log(Logging.LEVEL_MESSAGES, this, "Creating SoundEngine...");
    Logging.log(Logging.LEVEL_MESSAGES, this, "SoundEngine successfully created.");
  }

  
  /**
   * @return   the <code>SoundBuffer</code> with the given id,
   *           or <code>null</code> if no buffer with this id is open
   */
  private JavaSoundBuffer searchBuffer(String id)
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
  public JavaSoundEffect createSoundEffect(JavaSoundBuffer soundBuffer)
  {
    if (soundBuffer == null)
      return null;
    
    JavaSoundEffect ret = new JavaSoundEffect(soundBuffer);
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
  public JavaSoundEffect createSoundEffect(String path)
  {
    JavaSoundBuffer soundBuffer = createSoundBuffer(path);
    return createSoundEffect(soundBuffer);
  }
  
  
  /**
   * Creates a <code>SoundBuffer</code> from a .wav or .ogg file.
   * The sound is completely loaded into a buffer (not streamed),
   * but if it is already open the existing buffer is used.
   * @param path     The path of the file, for example "data/sound/soundfx/1.wav"
   * @return         A <code>SoundBuffer</code> instance containing the sound
   */
  public JavaSoundBuffer createSoundBuffer(String path)
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Open SoundBuffer \"" + path + "\"...");

    //if buffer is not already loaded, open the sound now
    JavaSoundBuffer ret = searchBuffer(path);
    if (ret == null)
    {
      try
      {
        File file = new File(path);
        //get AudioInputStream from given file.  
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
        AudioInputStream ais = AudioSystem.getAudioInputStream(
          decodedFormat, baseAIS);
        DataLine.Info info = new DataLine.Info(Clip.class,
          ais.getFormat(), ((int) ais.getFrameLength() *
            decodedFormat.getFrameSize()));
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(ais);
        //clip.start(); //TEST
        if (clip.getMicrosecondLength() == 0)
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Java Sound reports length 0: \"" + path + "\", file length is " +
            file.length() + " bytes");
        //create a new SoundBuffer
        ret = new JavaSoundBuffer(path, clip);
        buffers.add(ret);
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "SoundBuffer successfully created.");
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Could not load sound file \"" + path + "\":");
        Logging.log(Logging.LEVEL_WARNINGS, this, ex);
        return null;
      }
    }
    return ret;
  }


  public SoundEffect createSoundEffect(SoundBuffer soundBuffer)
  {
    return null;
  }


  /**
   * Creates a new <code>SoundTrack</code> object
   * for streaming.
   */
  public JavaSoundTrack createSoundTrack()
  {
    return new JavaSoundTrack();
  }
  
  
  /**
   * Sets the position of the listener.
   */
  public void setListenerPosition(float x, float y, float z)
  {
    //TODO
  }
  
}
