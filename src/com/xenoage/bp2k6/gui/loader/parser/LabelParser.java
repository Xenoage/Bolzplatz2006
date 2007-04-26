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

import java.awt.Color;

import org.w3c.dom.Node;

import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.util.*;


/**
 * @author Andreas Wenger
 */
public class LabelParser
{

  public static Label parseLabel(Node e,
    Screen parentScreen, Group parentGroup)
  {
    Label c = new Label("");
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //caption
    String caption = XMLReader.readAttributeValue(e, "caption");
    if (caption.indexOf("\\n") > -1)
      caption = caption.replace("\\n", "\n");
    caption = Variables.replaceVariables(caption);
    c.setCaption(caption);
    //color
    String color = XMLReader.readAttributeValue(e, "color");
    if (color.length() == 6)
    {
      Color col = ColorTools.getColor(color);
      c.setColor(col);
    }
    //font
    String fontID = XMLReader.readAttributeValue(e, "font");
    c.setFontByID(fontID);
    //font size
    String fontSize = XMLReader.readAttributeValue(e, "fontsize");
    if (fontSize.equals("full"))
      c.setFontSize(Label.FONTSIZE_FULL);
    //center-alignment
    if (XMLReader.readAttributeValue(e, "center") != null)
    {
      boolean center = XMLReader.readAttributeValueBoolean(e, "center");
      c.setAlignmentCenter(center);
    }
    //multiline
    boolean multiLine = XMLReader.readAttributeValueBoolean(e, "multiline");
    c.setMultiLine(multiLine);
    if (multiLine)
    {
      //line height
      float lineHeight = XMLReader.readAttributeValueFloat(e, "lineheight");
      c.setLineHeight(lineHeight);
    }
    //labels never get the focus
    c.setEnabled(false);
    return c;
  }

}
