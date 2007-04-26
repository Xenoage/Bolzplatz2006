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

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;


/**
 * Implementation of a sound buffer
 * based on LWJGL/OpenAL with Ogg Vorbis support
 * (by Phillip M. Jordan).
 *
 * @author   Andreas Wenger
 */
public class LWJGLSoundBuffer
  implements SoundBuffer
{

  private String id;
  private IntBuffer buffer = BufferUtils.createIntBuffer(1);

  
  /**
   * @param id       ID of the file (relative path)
   * @param buffer   ID of the OpenAL buffer
   */
  LWJGLSoundBuffer(String id, int buffer)
  {
    this.id = id;
    this.buffer.put(0, buffer);
  }

  
  /**
   * @return   the file ID of the sound
   */
  public String getID()
  {
    return id;
  }

  
  /**
   * @return   the ID of the buffer
   */
  public int getBuffer()
  {
    return buffer.get(0);
  }
  
  
  /**
   * Clears this sound buffer. This method
   * must be called before the class instance is destroyed!
   */
  public void clear()
  {
    AL10.alDeleteBuffers(buffer);
  }

  @Override protected void finalize()
  {
    try { clear(); }
    catch (Exception ex) {}
  }

}
