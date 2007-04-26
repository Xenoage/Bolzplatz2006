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
package com.xenoage.bp2k6.audio.nulldevice;

import com.xenoage.bp2k6.audio.*;


/**
 * Null device implementation of a sound effect
 * (does nothing)
 *
 * @author   Andreas Wenger
 */
public class NullDeviceSoundEffect
  implements SoundEffect
{

  public void play()
  {
  }
  
  public void stop()
  {
  }

  public void setLoop(boolean loop)
  {
  }

  public void setPosition(float x, float y, float z)
  {
  }

  public void setVolume(float volume)
  {
  }

  public void clear()
  {
  }

}
