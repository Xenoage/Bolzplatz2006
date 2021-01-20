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
import com.xenoage.bp2k6.util.XMLReader;

import org.w3c.dom.Node;


/**
 * @author Johannes Loepelmann
 * @author Andreas Wenger
 */
public class KeyChangeButtonParser
{

  public static KeyChangeButton parseKeyChangeButton(Node e,
    Screen parentScreen, Group parentGroup)
  {
    KeyChangeButton c = new KeyChangeButton("");
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //bp2k6 key
    int bp2k6key = XMLReader.readAttributeValueInt(e, "bp2k6key");
    c.setBp2k6Key(bp2k6key);
    //user
    int user = XMLReader.readAttributeValueInt(e, "user");
    c.setUser(user);
    //font
    String fontID = XMLReader.readAttributeValue(e, "font");
    c.setFontByID(fontID);
    return c;
  }

}