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
 * Interface for a sound track, that is is a
 * audio stream that can play one sound effect
 * (always streaming).
 * It is also possible to fade the sound in and out.
 *
 * @author Andreas Wenger
 */
public interface SoundTrack
{


  /**
   * Sets the next sound, without playing it now.
   */
  public void setNextSound(String soundID, float fadeInTime, float fadeOutTime);

  
  /**
   * Plays the current sound with fade-in.
   */
  public void fadeIn();
  

  /**
   * Fades the current sound out. The next sound (if set)
   * is not automatically played after that (for this
   * use playNextSound()).
   */
  public void fadeOut();


  /**
   * Sets the fade-out time of the current sound.
   */
  public void setFadeOutTime(float fadeOutTime);


  /**
   * Begin to play the next sound, but fade out the current one,
   * if necessary. If currently no sound is played or its
   * volume is 0, the next sound begins immediately (with fade-in,
   * if set).
   */
  public void playNextSound();


  /**
   * Call this method as often as possible
   * (for example in the main loop). It should be
   * implemented as <code>synchronized</code> when
   * called in a separate audio thread.
   */
  public void run();


  /**
   * Plays the sound track.
   */
  public boolean play();


  /**
   * Stops the sound track.
   */
  public void stop();


  /**
   * Pauses the sound track.
   */
  public boolean pause();


  /**
   * Gets the id of the currently played sound track.
   */
  public String getCurrentSoundID();


  /**
   * Gets the id of the next sound track, or <code>null</code>,
   * if not set.
   */
  public String getNextSoundID();


  /**
   * Returns <code>true</code>, if the SoundTrack is
   * playing at the moment, otherwise <code>false</code>
   */
  public boolean isPlaying();
  

  /**
   * Sets the repeat mode of this sound track.
   * If <code>true</code>, the playlist will be looped,
   * otherwise not (default).
   */
  public void setRepeatMode(boolean b);
  
  
  /**
   * Sets the volume (value between 0 and 1).
   */
  public void setVolume(float volume);
  
  
  /**
   * Loads and activates the next sound immediately
   * without playing it.
   */
  public void loadNextSound();
  
  
  /**
   * Call this method to close the track forever.
   * This may be necessary to stop running threads.
   */
  public void close();

}
