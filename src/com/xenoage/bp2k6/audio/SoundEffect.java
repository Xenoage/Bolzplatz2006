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
 * Interface for a class that holds a reference
 * to a sound buffer, its
 * position, velocity and volume. A sound effect is not
 * streamed but completely stored in a memory buffer.
 *
 * @author   Andi
 */
public interface SoundEffect
{
  
  
  /**
   * Plays this sound effect.
   */
  public void play();
  
  
  /**
   * Stops this sound effect.
   */
  public void stop();
  

  /**
   * Sets the repeat mode on/off.
   */
  public void setLoop(boolean loop);
   

  /**
   * Sets the position of the sound effect. Must be called
   * before calling <code>play()</code>, otherwise it
   * has no effect.
   */
  public void setPosition(float x, float y, float z);
  
  
  /**
   * Sets the volume of the sound effect. Must be called
   * before calling <code>play()</code>, otherwise it
   * has no effect.
   * @param volume   value between 0.0f (silence) and 1.0f (max)
   */
  public void setVolume(float volume);
   
  
  /**
   * Clears this sound effect. This method
   * must be called before the class instance is destroyed!
   */
  public void clear();


}
