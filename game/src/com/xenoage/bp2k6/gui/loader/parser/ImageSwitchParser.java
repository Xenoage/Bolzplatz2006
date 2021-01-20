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
import com.xenoage.bp2k6.gui.imageswitch.Option;
import com.xenoage.bp2k6.util.*;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import net.sf.jirr.ITexture;


/**
 * @author Andreas Wenger
 */
public class ImageSwitchParser
{

  public static ImageSwitch parseImageSwitch(Node e,
    Screen parentScreen, Group parentGroup)
  {
    ImageSwitch c = new ImageSwitch();
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //transparency
    String transparency = XMLReader.readAttributeValue(e, "transparency");
    if (transparency.length() > 0)
    {
      //add transparency for imageswitch: not needed at the moment
    }
    //source image
    String src = XMLReader.readAttributeValue(e, "src");
    ITexture tex = null;
    if (src.length() > 0)
    {
      tex = MediaLoader.loadTexture(src);
      c.setImage(tex);
    }
    //options
    Node optionsContainer = XMLReader.getChildElement(e, "options");
    if (optionsContainer != null)
    {
      List<Element> listOptions =
        XMLReader.elements(optionsContainer, "option");
      for (int i = 0; i < listOptions.size(); i++)
      {
        Node el = listOptions.get(i);
        String value = XMLReader.readAttributeValue(el, "value");
        int srcx = XMLReader.readAttributeValueInt(el, "srcx");
        int srcy = XMLReader.readAttributeValueInt(el, "srcy");
        int srcwidth = XMLReader.readAttributeValueInt(el, "srcwidth");
        int srcheight = XMLReader.readAttributeValueInt(el, "srcheight");
        Option option = new Option(value,
          new Rect2i(srcx, srcy, srcx + srcwidth, srcy + srcheight));
        c.addOption(option);
      }
      //default value?
      String defaultOption = XMLReader.readAttributeValue(
        optionsContainer, "default");
      if (defaultOption.length() > 0)
      {
        c.setSelectedIndex(Integer.parseInt(defaultOption));
      }
    }

    //actions
    List<Element> listActions = XMLReader.elements(e, "action");
    for (int i = 0; i < listActions.size(); i++)
    {
      Node el = listActions.get(i);
      Action a = ActionParser.parseAction(el);
      if (XMLReader.readAttributeValue(el, "event").equals("switch"))
        c.addActionSwitch(a);
    }

    return c;
  }


}
