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
package com.xenoage.util;

import java.awt.Component;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.Logging;


public class AWTGetHWnd
{
  
  public AWTGetHWnd()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Load AWTGetHWnd library...");
    try
    {
      System.loadLibrary("awtgethwnd");
      Logging.log(Logging.LEVEL_MESSAGES, this, "AWTGetHWnd was loaded successfully.");
    }
    catch (UnsatisfiedLinkError err)
    {
      Main.fatalError(this, new Exception(
        "AWTGetHWnd could not be loaded. Installation is corrupt."));
    }
  }

  public native int getHWnd(Component c);

}
