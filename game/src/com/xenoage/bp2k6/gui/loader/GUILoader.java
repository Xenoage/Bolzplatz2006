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
package com.xenoage.bp2k6.gui.loader;

import net.sf.jirr.SColor;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.gui.loader.parser.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.util.List;

import org.w3c.dom.*;


/**
 * @author Johannes Loepelmann
 * @author Andreas Wenger
 */
public class GUILoader
{


  //id: name of screen, without ".xml"
  public static Screen loadScreen(String id)
  {
    String lang = Language.getCurrentLanguageID();
    Logging.log(Logging.LEVEL_MESSAGES, new GUILoader(),
      "Loading screen \"" + lang + "/"+ id + "\"");
    Screen screen = new Screen(GameEngine.getUIManager());
    try
    {
      //open xml file with screen definition
      Document doc = XMLReader.readFile("data/gui/" + lang + "/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      screen.setID(XMLReader.readAttributeValue(root, "id"));
      //program
      screen.setProgram(XMLReader.readTextTrim(root, "program"));
      //background image
      String backgroundImage = XMLReader.readTextTrim(root, "background");
      if (backgroundImage.length() > 0)
        screen.setBackgroundImage(
          TextureFactory.loadTexture("data/" + backgroundImage, false));
      //gui scene
      Node eScene = XMLReader.getChildElement(root, "scene");
      if (eScene != null)
      {
        screen.setGUIScene(XMLReader.readAttributeValue(eScene, "systemid"));
      }
      //fading
      Node fading = XMLReader.getChildElement(root, "fading");
      if (fading != null)
      {
        screen.setFadeInColor(new SColor(XMLReader.readAttributeValueIntHex(fading, "incolor")));
        screen.setFadeOutColor(new SColor(XMLReader.readAttributeValueIntHex(fading, "outcolor")));
        screen.setFadeInTime(XMLReader.readAttributeValueFloat(fading, "intime"));
        screen.setFadeOutTime(XMLReader.readAttributeValueFloat(fading, "outtime"));
        if (XMLReader.readAttributeValue(fading, "usefadeout") == "false")
          screen.setUseFadeOut(false);
      }
      //music
      Node music = XMLReader.getChildElement(root, "music");
      if (music != null)
      {
        screen.setMusicFadeInTime(XMLReader.readAttributeValueFloat(music, "fadein"));
        screen.setMusicFadeOutTime(XMLReader.readAttributeValueFloat(music, "fadeout"));
        screen.setMusicContinue(XMLReader.readAttributeValueBoolean(music, "continue"));
        screen.setMusic(Variables.replaceVariables(
          XMLReader.readTextTrim(music)));
      }
      //following screen
      Node follow = XMLReader.getChildElement(root, "follow");
      if (follow != null)
      {
        screen.setNextScreenClose(XMLReader.readAttributeValue(follow, "nextid"));
        screen.setAutomaticCloseTime(XMLReader.readAttributeValueFloat(follow, "time"));
      }
      //groups
      Node eGroups = XMLReader.getChildElement(root, "groups");
      //start group
      screen.setStartGroupID(
        XMLReader.readAttributeValue(eGroups, "start"));
      //list of groups
      if (eGroups != null)
      {
        List<Element> listGroups = XMLReader.elements(eGroups, "group");
        for (int i = 0; i < listGroups.size(); i++)
        {
          Node el = listGroups.get(i);
          Group group = GroupParser.parseGroup(el, screen);
          screen.addGroup(group);
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new GUILoader(),
        "Screen \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(new GUILoader(), ex);
      return null;
    }
    return screen;
  }

  
  /**
   * Parse a list of controls and add it to
   * the given target container (Group or Panel)
   */
  public static void parseControls(Node controls, Container target,
    Screen parentScreen, Group parentGroup)
  {
    if (controls != null)
    {
      NodeList listControls = controls.getChildNodes();
      for (int i = 0; i < listControls.getLength(); i++)
      {
        Control c = null;
        Node elemControl = listControls.item(i);
        if (elemControl.getNodeName().equals("button"))
        {
          c = ButtonParser.parseButton(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("energybar"))
        {
          c = EnergyBarParser.parseEnergyBar(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("label"))
        {
          c = LabelParser.parseLabel(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("image"))
        {
          c = ImageParser.parseImage(elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("optionbutton"))
        {
          c = OptionButtonParser.parseOptionButton(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("imageswitch"))
        {
          c = ImageSwitchParser.parseImageSwitch(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("panel"))
        {
          c = PanelParser.parsePanel(
            elemControl, parentScreen, parentGroup);
        }
        else if (elemControl.getNodeName().equals("keychangebutton"))
        {
          c = KeyChangeButtonParser.parseKeyChangeButton(
            elemControl, parentScreen, parentGroup);
        }
        if (c != null)
          target.addControl(c, parentGroup, parentScreen);
        //ignore actions and actionsets here
        else if (!elemControl.getNodeName().equals("action")
          && !elemControl.getNodeName().equals("actionset")) 
          Logging.log(Logging.LEVEL_WARNINGS, new GUILoader(), "Unknown control type \"" +
            elemControl.getNodeName() + "\" in screen \"" + parentScreen.getID() + "\"!");
      }
    }
  }


}
