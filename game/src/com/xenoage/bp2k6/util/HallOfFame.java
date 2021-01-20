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
package com.xenoage.bp2k6.util;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.GameEngine.GameMode;
import com.xenoage.bp2k6.util.language.Language;

import java.lang.reflect.Method;
import java.util.Date;


/**
 * This class opens the hall-of-fame
 * entry website in the default browser,
 * with the correct parameters for
 * the current gamemode and team.
 * 
 * @author Andreas Wenger
 */
public class HallOfFame
{
  

  /**
   * Opens the entry website.
   */
  public static void openWebsite()
  {
    //gamemode and team ID
    GameMode gameMode = GameEngine.getGameMode();
    String sGameMode = null;
    String teamID = null;
    if (gameMode == GameMode.Career)
    {
      sGameMode = "career";
      teamID = GameEngine.getCareer().getUserTeamID();
    }
    else if (gameMode == GameMode.WorldCup)
    {
      sGameMode = "worldcup";
      teamID = GameEngine.getWorldCup().getUserTeamID();
    }
    else
    {
      return;
    }
    //code
    long code = (new Date().getTime()) / 1000;
    if (gameMode == GameMode.Career)
      code *= 3;
    else if (gameMode == GameMode.WorldCup)
      code *= 5;
    for (int i = 0; i < teamID.length(); i++)
    {
      int ascii = (char) teamID.charAt(i);
      code += ((long) 5479) * (i + 9973) * ascii;
    }
    String sCode = String.valueOf(code);
    //open website
    String url =
      "http://www.xenoage.com/bp2k6/myhof.php?gamemode=" +
      sGameMode + "&team=" + teamID + "&code=" + sCode +
      "&lang=" + Language.getCurrentLanguageID(); 
    openURL(url);
  }
  
  
  /**
   * Opens the given URL in a webbrowser.
   * Supports: Mac OS X, GNU/Linux, Unix, Windows XP
   * From: http://www.centerkey.com/java/browser/
   */
  public static void openURL(String url)
  {
    String osName = System.getProperty("os.name");
    try
    {
      if (osName.startsWith("Mac OS"))
      {
        Class fileMgr = Class.forName("com.apple.eio.FileManager");
        Method openURL = fileMgr.getDeclaredMethod(
          "openURL", new Class[] {String.class});
        openURL.invoke(null, new Object[] {url});
      }
      else if (osName.startsWith("Windows"))
      {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
      }
      else
      {
        //assume Unix or Linux
        String[] browsers = { "firefox", "opera", "konqueror",
          "epiphany", "mozilla", "netscape" };
        String browser = null;
        for (int count = 0; count < browsers.length && browser == null; count++)
        {
          if (Runtime.getRuntime().exec(
            new String[] {"which", browsers[count]}).waitFor() == 0)
          {
            browser = browsers[count];
          }
          if (browser != null)
          {
            Runtime.getRuntime().exec(new String[] {browser, url});
          }
          else
          {
            throw new Exception("Linux/Unix: No browser found!");
          }
        }
      }
      Logging.log(Logging.LEVEL_MESSAGES, new HallOfFame(),
        "Website \"" + url + "\" opened.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new HallOfFame(),
        "Could not open website \"" + url + "\":");
      Logging.log(Logging.LEVEL_ERRORS, new HallOfFame(), ex);
    }
  }

  }
