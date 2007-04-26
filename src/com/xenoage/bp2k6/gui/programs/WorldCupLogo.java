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
package com.xenoage.bp2k6.gui.programs;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffect;
import com.xenoage.bp2k6.gui.Image;
import com.xenoage.bp2k6.gui.Screen;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.Point2i;


/**
 * This gui program animates the
 * worldcup-logo screen.
 * 
 * @author Andreas Wenger
 */
public class WorldCupLogo
{
  private static float startTime = 0;
  
  private static boolean hooliganAnimation = true;
  private static boolean bubblesAnimation = true;
  
  private static Image hooligan;
  private static Image beaten;
  private static Image bubbles;
  
  private static SoundEffect soundPunch = null;
  private static boolean soundPunchPlayed = false;
  private static SoundEffect soundBurp = null;
  private static boolean soundBurpPlayed = false;
  
  
  /**
   * Call this method in every frame
   * when this program is active.
   */
  public static void step(Screen screen)
  {
    
    
    if (startTime == 0)
    {
      startTime = GameEngine.getFrameTime(false);
      
      hooliganAnimation = (MathTools.random() > 0.5f);
      bubblesAnimation = (MathTools.random() > 0.5f);
      
      hooligan = (Image) screen.findControl("worldcuplogo.hooligan");
      beaten = (Image) screen.findControl("worldcuplogo.beaten");
      bubbles = (Image) screen.findControl("worldcuplogo.bubbles");
      
      float scaleX = GameEngine.getUIManager().getScaleX();
      float scaleY = GameEngine.getUIManager().getScaleY();
      hooligan.setPivot(new Point2i((int) ((177 + 256) * scaleX),
          (int) ((211 + 128) * scaleY)));
      
      soundPunch = GameEngine.getSoundEngine().createSoundEffect(
        "data/sound/soundfx/intro_punch.wav");
      soundBurp = GameEngine.getSoundEngine().createSoundEffect(
        "data/sound/soundfx/intro_burp.wav");
      
    }
    else
    {
      float t = GameEngine.getFrameTime(false) - startTime;
      
      //hooligan-animation
      if (hooliganAnimation)
      {
        if (t > 2f && t < 2.1f)
        {
          //hooligan: rotate right
          hooligan.setRotation(MathTools.DEG_TO_RAD * 90 * (t - 2) * 10);
        }
        else if (t > 2.1f && t < 3f)
        {
          //beaten: cry
          beaten.setVisible(true);
          if (!soundPunchPlayed)
          {
            soundPunchPlayed = true;
            soundPunch.play();
          }
        }
        else if (t > 3f && t < 3.2f)
        {
          //hooligan: rotate left
          hooligan.setRotation(MathTools.DEG_TO_RAD * (90 - 90 * (t - 3) * 5));
        }
      }
      
      
      //bubbles-animation
      if (bubblesAnimation)
      {
        if (t > 4f)
        {
          if (!soundBurpPlayed)
          {
            soundBurpPlayed = true;
            soundBurp.play();
          }
          //show bubbles image
          bubbles.setVisible(true);
          Point2i p = new Point2i(bubbles.getSrcPos());
          if (t < 4.1f)
          {
            //show bubble
            p.x = 48;
          }
          else if (t < 4.2f)
          {
            //show next bubble
            p.x = 88;
          }
          else if (t < 4.3f)
          {
            //show next bubble
            p.x = 128;
          }
          else if (t < 4.4f)
          {
            //show next bubble
            p.x = 168;
          }
          else
          {
            //show next bubble
            p.x = 208;
          }
          bubbles.setSrcPos(p);
        }
      }
      
    }
  }
  
  
}
