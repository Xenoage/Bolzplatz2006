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
package com.xenoage.bp2k6.match.characters.actions;

import com.xenoage.bp2k6.match.characters.enums.*;


/**
 * This action lets a Character show the
 * given emotion.
 * 
 * @author Andreas Wenger
 *
 */
public class EmotionAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  private Emotion emotion;
  private boolean sound;
  
  
  /**
   * Lets a character show the given emotion.
   * This may also be boredom. Boredom can be shown also in
   * a "normal" waiting animation stage, but if it is created
   * as an emotion, is is performed completely.
   */
  public EmotionAction(com.xenoage.bp2k6.match.characters.Character character,
    Emotion emotion, boolean sound)
  {
    this.character = character;
    this.emotion = emotion;
    this.sound = sound;
  }
  

  /**
   * Returns <code>false</code>, because
   * emotions are shown completely.
   */
  public boolean isInterruptible()
  {
    return false;
  }
  
  
  /**
   * Performs the emotion.
   */
  public void perform()
  {
    character.setEmotion(emotion, sound);
  }
  
  
  /**
   * Returns <code>true</code>, when the emotion is over.
   */
  public boolean isDone()
  {
    return !character.getAnimationStage().isEmotion();
  }

}