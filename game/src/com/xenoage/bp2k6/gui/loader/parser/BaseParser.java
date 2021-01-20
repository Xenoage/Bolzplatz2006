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
import com.xenoage.bp2k6.util.*;

import java.util.List;

import org.w3c.dom.*;

import net.sf.jirr.*;


/**
 * @author Johannes Loepelmann
 * @author Andreas Wenger
 */
public class BaseParser {

  public static void parseBaseControl(Node e, Control c,
    Screen parentScreen, Group parentGroup)
  {
    //id
    c.setID(XMLReader.readAttributeValue(e, "id"));
    //focus
    if (XMLReader.getChildElement(e, "focus") != null && parentGroup != null)
      parentGroup.setFocus(c);
    //enabled
    c.setEnabled(XMLReader.readAttributeValue(
      e, "enabled").equals("false") ? false : true);
    //visible
    String visible = XMLReader.readAttributeValue(e, "visible");
    visible = Variables.replaceVariables(visible);
    c.setVisible(visible.equals("false") ? false : true);
    //position
    Node pos = XMLReader.getChildElement(e, "pos");
    if (pos == null)
    {
      Logging.log(Logging.LEVEL_WARNINGS, new BaseParser(), "Control with id \"" +
        c.getID() + "\" has no position!");
    }
    else
    {
      int x = XMLReader.readAttributeValueInt(pos, "x");
      int y = XMLReader.readAttributeValueInt(pos, "y");
      int width = XMLReader.readAttributeValueInt(pos, "width");
      int height = XMLReader.readAttributeValueInt(pos, "height");
      c.setPosition(new Point2i(x, y));
      c.setSize(new Point2i(width, height));
    }
    //velocity
    Node vel = XMLReader.getChildElement(e, "movement");
    if (vel != null)
    {
      float moveX = XMLReader.readAttributeValueFloat(vel, "x");
      float moveY = XMLReader.readAttributeValueFloat(vel, "y");
      c.setVelocity(new vector2df(moveX, moveY));
    }
    //fly-in
    Node flyin = XMLReader.getChildElement(e, "flyin");
    if (flyin != null)
    {
      int x = XMLReader.readAttributeValueInt(flyin, "x");
      int y = XMLReader.readAttributeValueInt(flyin, "y");
      float start = XMLReader.readAttributeValueFloat(flyin, "starttime");
      float duration = XMLReader.readAttributeValueFloat(flyin, "duration");
      c.setPositionFlyIn(new Point2i(x, y));
      c.setTimeStartFlyIn(start);
      c.setDurationFlyIn(duration);
    }
    //fly-out
    Node flyout = XMLReader.getChildElement(e, "flyout");
    if (flyout != null)
    {
      int x = XMLReader.readAttributeValueInt(flyout, "x");
      int y = XMLReader.readAttributeValueInt(flyout, "y");
      float start = XMLReader.readAttributeValueFloat(flyout, "starttime");
      float duration = XMLReader.readAttributeValueFloat(flyout, "duration");
      c.setPositionFlyOut(new Point2i(x, y));
      c.setTimeStartFlyOut(start);
      c.setDurationFlyOut(duration);
    }
    //next controls
    Node n = XMLReader.getChildElement(e, "next");
    if (n != null)
    {
      List<Element> nodeList = XMLReader.elements(n, "next_control");
      for (int i = 0; i < nodeList.size(); i++)
      {
        Element m = nodeList.get(i);
        c.setNextControl(readDirection(
          XMLReader.readAttributeValue(m, "dir")),
          m.getTextContent());
      }
    }
    //actions
    List<Element> listActions = XMLReader.elements(e, "action");
    for (int i = 0; i < listActions.size(); i++)
    {
      Node el = listActions.get(i);
      Action a = ActionParser.parseAction(el);
      String attrVal = XMLReader.readAttributeValue(el, "event");
      if (attrVal.equals("load"))
        c.addActionLoad(a);
      else if (attrVal.equals("click"))
        c.addActionClick(a);
      else if (attrVal.equals("focus"))
        c.addActionFocus(a);
    }
  }

  //todo: constants
  private static int readDirection(String dir)
  {
    if (dir.equals("up"))
      return 0;
    else if (dir.equals("right"))
      return 1;
    else if (dir.equals("down"))
      return 2;
    else if (dir.equals("left"))
      return 3;
    else
      return 0;
  }
}
