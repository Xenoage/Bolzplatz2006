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
package com.xenoage.bp2k6.input;

import java.util.List;

import org.w3c.dom.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.input.KeyboardEvent;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;


/**
 * The user can arbitrarily assign the keys.
 * This class is the "interface" between
 * "virtual keys" (like "arrow left" or "shoot button")
 * and "real keys" (Java/Irrlicht key codes).
 *
 * @author Andi
 */
public class KeysAssignment
{

  private static final int KEY_ARROWLEFT = 0;
  private static final int KEY_ARROWUP = 1;
  private static final int KEY_ARROWRIGHT = 2;
  private static final int KEY_ARROWDOWN = 3;
  private static final int KEY_PASS = 4; //pass, tackle
  private static final int KEY_SHOOT = 5; //shoot, player switch
  private static final int KEY_SPRINT = 6; //sprint

  private static int keyboard[][] = new int[2][7]; //keyboard assignments [user][button]
  private static int gamepad[][] = new int[2][7]; //gamepad assignments [user][button]

  private static KeysAssignment thisObject = new KeysAssignment();
  

  /**
   * Sets the keys assignment for the given input engine.
   * The settings are read from "data/config/input.xml".
   */
  public static void setAssignment(Input input)
  {
    //read keys assignment information from XML file
    Logging.log(Logging.LEVEL_MESSAGES, new KeysAssignment(),
      "Loading keys assignment from \"data/config/input.xml\"...");
    try
    {
      Document doc = XMLReader.readFile("data/config/input.xml");
      Node root = doc.getFirstChild();
      List<Element> listUsers = XMLReader.elements(root, "user");
      if (listUsers.size() != 2)
        GameEngine.fatalError(new KeysAssignment(), new Exception(
          "There must be 2 users."));
      for (int iUser = 0; iUser < listUsers.size(); iUser++)
      {
        Node eUser = listUsers.get(iUser);
        //id: 1 or 2
        int id = XMLReader.readAttributeValueInt(eUser, "id") - 1;
        if (id != 0 && id != 1)
          GameEngine.fatalError(new KeysAssignment(), new Exception("Unknown user \""
            + (id + 1) + "\""));
        else
        {
          //keyboard
          Node eKeyboard = XMLReader.getChildElement(eUser, "keyboard");
          if (eKeyboard == null)
            GameEngine.fatalError(new KeysAssignment(), new Exception(
              "No keyboard settings for user \"" + (id + 1) + "\""));
          else
          {
            List<Element> listKeys = XMLReader.elements(eKeyboard, "key");
            for (int iKey = 0; iKey < listKeys.size(); iKey++)
            {
              Node eKey = listKeys.get(iKey);
              keyboard[id][getKeyConstant(XMLReader.readAttributeValue(eKey, "id"))] = XMLReader
                .readAttributeValueInt(eKey, "value");
            }
          }
          //gamepad
          Node eGamepad = XMLReader.getChildElement(eUser, "gamepad");
          if (eGamepad == null)
            GameEngine.fatalError(new KeysAssignment(), new Exception(
              "No gamepad settings for user \"" + (id + 1) + "\""));
          else
          {
            List<Element> listKeys = XMLReader.elements(eGamepad, "key");
            for (int iKey = 0; iKey < listKeys.size(); iKey++)
            {
              Node eKey = listKeys.get(iKey);
              int bp2k6Key = getKeyConstant(XMLReader.readAttributeValue(eKey, "id"));
              if (bp2k6Key > -1)
              {
                gamepad[id][bp2k6Key] = XMLReader.readAttributeValueInt(eKey, "value");
              }
            }
          }
        }
        //apply settings
        input.setKeyboardKeys(id, keyboard[id]);
        input.setJoystickKeys(id, new int[] { gamepad[id][KEY_PASS],
          gamepad[id][KEY_SHOOT], gamepad[id][KEY_SPRINT] });
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new KeysAssignment(),
        "Keys assignment could not be loaded! Details:");
      Main.fatalError(new KeysAssignment(), ex);
    }
  }
  
  
  /**
   * Saves a new key assignment in "data/config/input.xml".
   * The settings are not applied! Call <code>setAssignment</code>
   * after this method to do so.
   */
  public static void saveKeyAssignment(int user, int bp2k6Key, int key)
  {
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Saving key assignment: user: " + user + ", bp2k6Key: " +
      bp2k6Key + ", key: " + key);
    try
    {
      ConfigFile inputConfig = new ConfigFile("data/config/input.xml");
      Node root = inputConfig.getRootElement();
      List<Element> userList = XMLReader.elements(root, "user");
      boolean userFound = false;
      for (int iUser = 0; iUser < userList.size(); iUser++)
      {
        Node eUser = userList.get(iUser);
        if (XMLReader.readAttributeValueInt(eUser, "id") == user + 1)
        {
          userFound = true;
          Node eKeyboard = XMLReader.getChildElement(eUser, "keyboard");
          if (eKeyboard == null)
          {
            throw new Exception("keyboard element not found!");
          }
          List<Element> keyList = XMLReader.elements(eKeyboard, "key");
          boolean keyFound = false;
          for (int iKey = 0; iKey < keyList.size(); iKey++)
          {
            Node eKey = keyList.get(iKey);
            if (getKeyConstant(XMLReader.readAttributeValue(
              eKey, "id")) == bp2k6Key)
            {
              keyFound = true;
              XMLWriter.addAttribute(eKey, "value", String.valueOf(key));
              inputConfig.saveToXML();
              break;
            }
          }
          if (!keyFound)
          {
            throw new Exception("key element \"" + bp2k6Key + "\" not found!");
          }
          break;
        }
      }
      if (!userFound)
      {
        throw new Exception("user element \"" + user + "\" not found!");
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, thisObject,
        "Error while saving keys assignment:");
      Logging.log(Logging.LEVEL_ERRORS, thisObject, ex);
    }
  }


  /**
   * Gets the KEY_-constant (bp2k6Key) from the given String.
   */
  private static int getKeyConstant(String s)
  {
    if (s.equals("arrowleft"))
      return KEY_ARROWLEFT;
    else if (s.equals("arrowup"))
      return KEY_ARROWUP;
    else if (s.equals("arrowright"))
      return KEY_ARROWRIGHT;
    else if (s.equals("arrowdown"))
      return KEY_ARROWDOWN;
    else if (s.equals("pass"))
      return KEY_PASS;
    else if (s.equals("shoot"))
      return KEY_SHOOT;
    else if (s.equals("sprint"))
      return KEY_SPRINT;
    Logging.log(Logging.LEVEL_WARNINGS, new KeysAssignment(), "Unknown key constant: \""
      + s + "\"");
    return -1;
  }


  /**
   * Gets the keyboard key assigned to the given bp2k6 key and
   * the given user.
   */
  public static int getKeyboardKey(int bp2k6Key, int user)
  {
    return keyboard[user][bp2k6Key];
  }
  
  
  /**
   * Returns the name of the given keyboard key, or
   * <code>null</code> if the given key is not allowed
   * for keys assignment.
   */
  public static String getKeyboardKeyName(int key)
  {
    switch (key)
    {
      case KeyboardEvent.KEY_BACK : return Language.get("key_backspace");
      case KeyboardEvent.KEY_TAB : return Language.get("key_tab");
      //case KEY_CLEAR : return "0x0C; // CLEAR key 
      case KeyboardEvent.KEY_RETURN : return Language.get("key_enter");
      case KeyboardEvent.KEY_SHIFT : return Language.get("key_shift");
      case KeyboardEvent.KEY_CONTROL : return Language.get("key_ctrl");
      case KeyboardEvent.KEY_MENU : return Language.get("key_menu");
      case KeyboardEvent.KEY_PAUSE : return Language.get("key_pause");
      case KeyboardEvent.KEY_KANA : return Language.get("key_kana");
      //case KeyboardEvent.KEY_HANGUL : return Language.get("key_hangul");
      case KeyboardEvent.KEY_JUNJA : return Language.get("key_junja");
      case KeyboardEvent.KEY_FINAL : return Language.get("key_final"); 
      case KeyboardEvent.KEY_HANJA : return Language.get("key_hanja");
      //case KeyboardEvent.KEY_KANJI : return Language.get("key_kanji");
      case KeyboardEvent.KEY_ESCAPE : return Language.get("key_escape");
      case KeyboardEvent.KEY_CONVERT : return Language.get("key_convert");
      case KeyboardEvent.KEY_NONCONVERT : return Language.get("key_nonconvert");
      case KeyboardEvent.KEY_ACCEPT : return Language.get("key_accept");
      case KeyboardEvent.KEY_MODECHANGE : return Language.get("key_modechange");
      case KeyboardEvent.KEY_SPACE : return Language.get("key_space");
      case KeyboardEvent.KEY_PRIOR : return Language.get("key_prior");
      case KeyboardEvent.KEY_NEXT : return Language.get("key_next");
      case KeyboardEvent.KEY_END : return Language.get("key_end");
      case KeyboardEvent.KEY_HOME : return Language.get("key_home");
      case KeyboardEvent.KEY_LEFT : return Language.get("key_left"); 
      case KeyboardEvent.KEY_UP : return Language.get("key_up");
      case KeyboardEvent.KEY_RIGHT : return Language.get("key_right");
      case KeyboardEvent.KEY_DOWN : return Language.get("key_down");
      case KeyboardEvent.KEY_SELECT : return Language.get("key_select"); 
      case KeyboardEvent.KEY_PRINT : return Language.get("key_print");
      case KeyboardEvent.KEY_EXECUT : return Language.get("key_execute");
      case KeyboardEvent.KEY_SNAPSHOT : return Language.get("key_snapshot"); 
      case KeyboardEvent.KEY_INSERT : return Language.get("key_insert");
      case KeyboardEvent.KEY_DELETE : return Language.get("key_delete");
      case KeyboardEvent.KEY_HELP : return Language.get("key_help");
      case KeyboardEvent.KEY_KEY_0 : return "0";
      case KeyboardEvent.KEY_KEY_1 : return "1";
      case KeyboardEvent.KEY_KEY_2 : return "2";
      case KeyboardEvent.KEY_KEY_3 : return "3";
      case KeyboardEvent.KEY_KEY_4 : return "4"; 
      case KeyboardEvent.KEY_KEY_5 : return "5";
      case KeyboardEvent.KEY_KEY_6 : return "6";
      case KeyboardEvent.KEY_KEY_7 : return "7"; 
      case KeyboardEvent.KEY_KEY_8 : return "8"; 
      case KeyboardEvent.KEY_KEY_9 : return "9"; 
      case KeyboardEvent.KEY_KEY_A : return "A"; 
      case KeyboardEvent.KEY_KEY_B : return "B";
      case KeyboardEvent.KEY_KEY_C : return "C";
      case KeyboardEvent.KEY_KEY_D : return "D"; 
      case KeyboardEvent.KEY_KEY_E : return "E"; 
      case KeyboardEvent.KEY_KEY_F : return "F";
      case KeyboardEvent.KEY_KEY_G : return "G"; 
      case KeyboardEvent.KEY_KEY_H : return "H"; 
      case KeyboardEvent.KEY_KEY_I : return "I"; 
      case KeyboardEvent.KEY_KEY_J : return "J";
      case KeyboardEvent.KEY_KEY_K : return "K";
      case KeyboardEvent.KEY_KEY_L : return "L";
      case KeyboardEvent.KEY_KEY_M : return "M"; 
      case KeyboardEvent.KEY_KEY_N : return "N"; 
      case KeyboardEvent.KEY_KEY_O : return "O";
      case KeyboardEvent.KEY_KEY_P : return "P"; 
      case KeyboardEvent.KEY_KEY_Q : return "Q";
      case KeyboardEvent.KEY_KEY_R : return "R";
      case KeyboardEvent.KEY_KEY_S : return "S"; 
      case KeyboardEvent.KEY_KEY_T : return "T";
      case KeyboardEvent.KEY_KEY_U : return "U";
      case KeyboardEvent.KEY_KEY_V : return "V";
      case KeyboardEvent.KEY_KEY_W : return "W";
      case KeyboardEvent.KEY_KEY_X : return "X";
      case KeyboardEvent.KEY_KEY_Y : return "Y"; 
      case KeyboardEvent.KEY_KEY_Z : return "Z";
      case KeyboardEvent.KEY_LWIN : return Language.get("key_lwin");
      case KeyboardEvent.KEY_RWIN : return Language.get("key_rwin"); 
      case KeyboardEvent.KEY_APPS : return Language.get("key_apps");
      case KeyboardEvent.KEY_SLEEP : return Language.get("key_sleep");
      case KeyboardEvent.KEY_NUMPAD0 : return Language.get("key_numpad") + " 0";
      case KeyboardEvent.KEY_NUMPAD1 : return Language.get("key_numpad") + " 1";
      case KeyboardEvent.KEY_NUMPAD2 : return Language.get("key_numpad") + " 2";
      case KeyboardEvent.KEY_NUMPAD3 : return Language.get("key_numpad") + " 3";
      case KeyboardEvent.KEY_NUMPAD4 : return Language.get("key_numpad") + " 4";
      case KeyboardEvent.KEY_NUMPAD5 : return Language.get("key_numpad") + " 5";
      case KeyboardEvent.KEY_NUMPAD6 : return Language.get("key_numpad") + " 6";
      case KeyboardEvent.KEY_NUMPAD7 : return Language.get("key_numpad") + " 7";
      case KeyboardEvent.KEY_NUMPAD8 : return Language.get("key_numpad") + " 8";
      case KeyboardEvent.KEY_NUMPAD9 : return Language.get("key_numpad") + " 9";
      case KeyboardEvent.KEY_MULTIPLY : return Language.get("key_multiply");
      case KeyboardEvent.KEY_ADD : return Language.get("key_add");
      case KeyboardEvent.KEY_SEPARATOR : return Language.get("key_separator"); 
      case KeyboardEvent.KEY_SUBTRACT : return Language.get("key_subtract");
      case KeyboardEvent.KEY_DECIMAL : return Language.get("key_decimal");
      case KeyboardEvent.KEY_DIVIDE : return Language.get("key_divide");
      case KeyboardEvent.KEY_F1 : return "F1"; 
      case KeyboardEvent.KEY_F2 : return "F2"; 
      case KeyboardEvent.KEY_F3 : return "F3"; 
      case KeyboardEvent.KEY_F4 : return "F4";  
      case KeyboardEvent.KEY_F5 : return "F5"; 
      case KeyboardEvent.KEY_F6 : return "F6"; 
      case KeyboardEvent.KEY_F7 : return "F7"; 
      case KeyboardEvent.KEY_F8 : return "F8"; 
      case KeyboardEvent.KEY_F9 : return "F9"; 
      case KeyboardEvent.KEY_F10 : return "F10"; 
      case KeyboardEvent.KEY_F11 : return "F11"; 
      case KeyboardEvent.KEY_F12 : return "F12"; 
      case KeyboardEvent.KEY_F13 : return "F13"; 
      case KeyboardEvent.KEY_F14 : return "F14"; 
      case KeyboardEvent.KEY_F15 : return "F15"; 
      case KeyboardEvent.KEY_F16 : return "F16"; 
      case KeyboardEvent.KEY_F17 : return "F17"; 
      case KeyboardEvent.KEY_F18 : return "F18"; 
      case KeyboardEvent.KEY_F19 : return "F19"; 
      case KeyboardEvent.KEY_F20 : return "F20"; 
      case KeyboardEvent.KEY_F21 : return "F21"; 
      case KeyboardEvent.KEY_F22 : return "F22"; 
      case KeyboardEvent.KEY_F23 : return "F23"; 
      case KeyboardEvent.KEY_F24 : return "F24"; 
      case KeyboardEvent.KEY_NUMLOCK : return Language.get("key_numlock");
      case KeyboardEvent.KEY_SCROLL : return Language.get("key_scroll");
      case KeyboardEvent.KEY_LSHIFT : return Language.get("key_lshift");
      case KeyboardEvent.KEY_RSHIFT : return Language.get("key_rshift");
      case KeyboardEvent.KEY_LCONTROL : return Language.get("key_lcontrol");
      case KeyboardEvent.KEY_RCONTROL : return Language.get("key_rcontrol");
      case KeyboardEvent.KEY_LMENU : return Language.get("key_lmenu");
      case KeyboardEvent.KEY_RMENU : return Language.get("key_rmenu");
      case KeyboardEvent.KEY_COMMA : return ",";
      case KeyboardEvent.KEY_PLUS : return "+";
      case KeyboardEvent.KEY_MINUS : return "-";
      case KeyboardEvent.KEY_PERIOD : return ".";
      case KeyboardEvent.KEY_ATTN : return Language.get("key_attn");
      case KeyboardEvent.KEY_CRSEL : return Language.get("key_crsel");
      case KeyboardEvent.KEY_EXSEL : return Language.get("key_exsel");
      case KeyboardEvent.KEY_EREOF : return Language.get("key_ereof");
      case KeyboardEvent.KEY_PLAY : return Language.get("key_play");
      case KeyboardEvent.KEY_ZOOM : return Language.get("key_zoom");
      case KeyboardEvent.KEY_PA1 : return Language.get("key_pa1");
      case KeyboardEvent.KEY_OEM_CLEAR : return Language.get("key_oemclear");
      //other keys (without constant?)
      case 186 : return "Ü";
      case 191 : return "Raute";
      case 192 : return "Ö";
      case 222 : return "Ä";
      case 219 : return "ß";
      case 226 : return "<";
      case 220 : return "'";
    }
    return null;
  }


}
