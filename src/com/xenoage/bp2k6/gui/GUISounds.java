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
package com.xenoage.bp2k6.gui;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.audio.SoundEffect;
import com.xenoage.bp2k6.audio.SoundEngine;
import com.xenoage.bp2k6.util.Logging;
import com.xenoage.bp2k6.util.XMLReader;

import java.util.List;

import org.w3c.dom.*;


/**
 * This class contains the GUI sounds
 * and a method to play them.
 * 
 * @author Andreas Wenger
 */
public class GUISounds
{

  public enum GUISound
  {
    Focus,
    Select,
    Switch,
    Accept,
    Abort,
    Alert,
    Move;
  };

  private SoundEffect soundFocus = null;
  private SoundEffect soundSelect = null;
  private SoundEffect soundSwitch = null;
  private SoundEffect soundAccept = null;
  private SoundEffect soundAbort = null;
  private SoundEffect soundAlert = null;
  private SoundEffect soundMove = null;

  
  /**
   * Constructor.
   * Loads all GUI sounds.
   */
  public GUISounds()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Creating GUISounds class...");
    //create gui sounds
    SoundEngine soundEngine = GameEngine.getSoundEngine();
    String soundDir = "data/sound/gui/";
    //GOON: Java Sound: wav works, ogg not always (when file too small...)
    soundFocus = soundEngine.createSoundEffect(soundDir + "focus.wav");
    soundSelect = soundEngine.createSoundEffect(soundDir + "select.ogg");
    soundSwitch = soundEngine.createSoundEffect(soundDir + "switch.ogg");
    soundAccept = soundEngine.createSoundEffect(soundDir + "accept.ogg");
    soundAbort = soundEngine.createSoundEffect(soundDir + "abort.ogg");
    soundAlert = soundEngine.createSoundEffect(soundDir + "alert.ogg");
    soundMove = soundEngine.createSoundEffect(soundDir + "move.ogg");
    //load volume information from data/config/guisounds.xml
    try
    {
      Document doc = XMLReader.readFile("data/config/guisounds.xml");
      Node root = doc.getFirstChild();
      //list of guisounds
      List<Element> listGUISounds = XMLReader.elements(root, "guisound");
      for (int i = 0; i < listGUISounds.size(); i++)
      {
        Node e = listGUISounds.get(i);
        String id = XMLReader.readAttributeValue(e, "id");
        float volume = XMLReader.readAttributeValueInt(e, "volume") * 0.01f;
        if (id.equals("focus"))
          soundFocus.setVolume(volume);
        else if (id.equals("select"))
          soundSelect.setVolume(volume);
        else if (id.equals("switch"))
          soundSwitch.setVolume(volume);
        else if (id.equals("accept"))
          soundAccept.setVolume(volume);
        else if (id.equals("abort"))
          soundAbort.setVolume(volume);
        else if (id.equals("alert"))
          soundAlert.setVolume(volume);
        else if (id.equals("move"))
          soundMove.setVolume(volume);
        else
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "config/guisounds.xml: Unknown GUISound id \"" + id + "\"");
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "config/guisounds.xml could not be parsed! Details:");
      Main.fatalError(this, ex);
    }
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "GUISounds created.");
  }
  
  
  /**
   * Plays the given GUI sound.
   */
  public void playSound(GUISound sound)
  {
    if (sound == GUISound.Focus)
      soundFocus.play();
    else if (sound == GUISound.Select)
      soundSelect.play();
    else if (sound == GUISound.Switch)
      soundSwitch.play();
    else if (sound == GUISound.Accept)
      soundAccept.play();
    else if (sound == GUISound.Abort)
      soundAbort.play();
    else if (sound == GUISound.Alert)
      soundAlert.play();
    else if (sound == GUISound.Move)
      soundMove.play();
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Playing GUISound \"" + sound + "\"");
  }
  
  
}
