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

/**
 * Interface for a sound engine.
 * Creates <code>SoundEffect</code> objects from
 * implementation dependent audio files,
 * manages the sound effect sources, and stores
 * the position, velocity and orientation of the listener.
 *
 * @author   Andreas Wenger
 */
public interface SoundEngine
{
  
  
  /**
   * Creates a <code>SoundEffect</code> from an already
   * loaded <code>SoundBuffer</code>.
   */
  public SoundEffect createSoundEffect(SoundBuffer soundBuffer);
  

  /**
   * Creates a <code>SoundEffect</code> from an audio file
   * (implementation dependent).
   * The sound is completely loaded into a buffer (not streamed),
   * but if it is already open the existing buffer is used.
   * @param filePath The path of the file, for example "data/sound/soundfx/1.wav"
   * @return         A new <code>SoundEffect</code> instance containing the sound
   */
  public SoundEffect createSoundEffect(String id);
  
  
  /**
   * Creates a <code>SoundBuffer</code> from an audio file
   * (implementation dependent).
   * The sound is completely loaded into a buffer (not streamed),
   * but if it is already open the existing buffer is used.
   * @param path   The path of the file, for example "data/sound/soundfx/1.wav"
   * @return       A <code>SoundBuffer</code> instance containing the sound
   */
  public SoundBuffer createSoundBuffer(String path);
  
  
  /**
   * Creates a new <code>SoundTrack</code> object
   * for streaming.
   */
  public SoundTrack createSoundTrack();
  
  
  /**
   * Sets the position of the listener.
   */
  public void setListenerPosition(float x, float y, float z);
  
}
