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

import org.w3c.dom.Node;


/**
 * @author Andreas Wenger
 */
public class EnergyBarParser
{

  public static EnergyBar parseEnergyBar(Node e,
    Screen parentScreen, Group parentGroup)
  {
    EnergyBar c = new EnergyBar();
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //energy bars never get the focus
    c.setEnabled(false);
    return c;
  }

}