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
package com.xenoage.bp2k6.gui.loader.parser;

import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.gui.GUISounds.GUISound;
import com.xenoage.bp2k6.util.*;

import org.w3c.dom.Node;

/**
 * @author Johannes Loepelmann
 * @author Andreas Wenger
 */
public class ActionParser
{

  public static Action parseAction(Node e)
  {
    String eventparam = XMLReader.readAttributeValue(e, "eventparam");
    String param1 = XMLReader.readAttributeValue(e, "param1");
    String param2 = XMLReader.readAttributeValue(e, "param2");
    String param3 = XMLReader.readAttributeValue(e, "param3");
    String param4 = XMLReader.readAttributeValue(e, "param4");
    String type = XMLReader.readAttributeValue(e, "type");
    String sGUISound = XMLReader.readAttributeValue(e, "guisound");
    String sound = XMLReader.readAttributeValue(e, "sound");
    Action a = null;
    
    //action type
    Action.ActionType at = Action.ActionType.None;
    if (type.equals(""))
      at = Action.ActionType.None;
    else if (type.equals("activategroup"))
      at = Action.ActionType.ActivateGroup;
    else if (type.equals("cameraflight"))
      at = Action.ActionType.CameraFlight;
    else if (type.equals("cameraflightrandom"))
      at = Action.ActionType.CameraFlightRandom;
    else if (type.equals("changegamemode"))
      at = Action.ActionType.ChangeGameMode;
    else if (type.equals("changeimage"))
      at = Action.ActionType.ChangeImage;
    else if (type.equals("changeimagecontrol"))
      at = Action.ActionType.ChangeImageControl;
    else if (type.equals("changesystemlistcontrol"))
      at = Action.ActionType.ChangeSystemListControl;
    else if (type.equals("corruptreferee"))
      at = Action.ActionType.CorruptReferee;
    else if (type.equals("editstadium"))
      at = Action.ActionType.EditStadium;
    else if (type.equals("exitprogram"))
      at = Action.ActionType.ExitProgram;
    else if (type.equals("halloffame"))
      at = Action.ActionType.HallOfFame;
    else if (type.equals("inputgamemode"))
      at = Action.ActionType.InputGameMode;
    else if (type.equals("leavegamemode"))
      at = Action.ActionType.LeaveGameMode;
    else if (type.equals("loadcareer"))
      at = Action.ActionType.LoadCareer;
    else if (type.equals("loadworldcup"))
      at = Action.ActionType.LoadWorldCup;
    else if (type.equals("matchevent"))
      at = Action.ActionType.MatchEvent;
    else if (type.equals("matchover"))
      at = Action.ActionType.MatchOver;
    else if (type.equals("openscreen"))
      at = Action.ActionType.OpenScreen;
    else if (type.equals("openwebsite"))
      at = Action.ActionType.OpenWebsite;
    else if (type.equals("performactions"))
      at = Action.ActionType.PerformActions;
    else if (type.equals("restart"))
      at = Action.ActionType.Restart;
    else if (type.equals("savecareer"))
      at = Action.ActionType.SaveCareer;
    else if (type.equals("savesetting"))
      at = Action.ActionType.SaveSetting;
    else if (type.equals("savesettingcontrol"))
      at = Action.ActionType.SaveSettingControl;
    else if (type.equals("saveworldcup"))
      at = Action.ActionType.SaveWorldCup;
    else if (type.equals("setactivejoystick"))
      at = Action.ActionType.SetActiveJoystick;
    else if (type.equals("setcaption"))
      at = Action.ActionType.SetCaption;
    else if (type.equals("setfocus"))
      at = Action.ActionType.SetFocus;
    else if (type.equals("setlabelcolor"))
      at = Action.ActionType.SetLabelColor;
    else if (type.equals("setoption"))
      at = Action.ActionType.SetOption;
    else if (type.equals("setpause"))
      at = Action.ActionType.SetPause;
    else if (type.equals("setselection"))
      at = Action.ActionType.SetSelection;
    else if (type.equals("setvalue"))
      at = Action.ActionType.SetValue;
    else if (type.equals("startmatchday"))
      at = Action.ActionType.StartMatchDay;
    else
      Logging.log(Logging.LEVEL_WARNINGS, new ActionParser(),
        "Unknown action type: \"" + type + "\"");
    
    //gui sound
    GUISound guiSound = null;
    if (sGUISound.equals(""))
      guiSound = null;
    else if (sGUISound.equals("focus"))
      guiSound = GUISound.Focus;
    else if (sGUISound.equals("select"))
      guiSound = GUISound.Select;
    else if (sGUISound.equals("switch"))
      guiSound = GUISound.Switch;
    else if (sGUISound.equals("accept"))
      guiSound = GUISound.Accept;
    else if (sGUISound.equals("abort"))
      guiSound = GUISound.Abort;
    else if (sGUISound.equals("alert"))
      guiSound = GUISound.Alert;
    else if (sGUISound.equals("move"))
      guiSound = GUISound.Move;
    else
      Logging.log(Logging.LEVEL_WARNINGS, new ActionParser(),
        "Unknown GUI sound: \"" + sGUISound + "\"");
    
    a = new Action(at, param1, param2, param3, param4,
      eventparam, guiSound, sound);
    return a;
  }
}
