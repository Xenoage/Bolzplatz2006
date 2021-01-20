/**
 * Bolzplatz
 * Copyright (C) 2007 by Xenoage Software
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

import java.io.*;
import java.lang.reflect.Method;


/**
 * This class contains a method to open
 * data files at the user's home directory
 * or the game directory as an input stream.
 * 
 * @author Andreas Wenger
 */
public class IO
{
  
  private static String gameSubDir = null;
  
  
  /**
   * Opens and returns an input stream for the data file with
   * the given relative path.
   * It is tried to open the files in the user's home folder first.
   * If this file does not exist, it is opened from the game
   * folder.
   * @param filepath  the relative path, e.g. "data/images/gui.png".
   */
  public static InputStream openDataFile(String filepath)
    throws IOException
  {
    String homeFile = getGameHomeDir() + filepath;
    if (new File(homeFile).exists())
    {
      //open from home directory
      Logging.log(Logging.LEVEL_MESSAGES,
        "\"" + filepath + "\" found in home directory.");
      return new FileInputStream(new File(homeFile));
    }
    else if (new File(filepath).exists())
    {
      //open from game directory
      Logging.log(Logging.LEVEL_MESSAGES,
        "\"" + filepath + "\" found in game directory.");
      return new FileInputStream(new File(filepath));
    }
    else
    {
      throw new FileNotFoundException("\"" + filepath + "\" not found!");
    }
  }
  
  
  /**
   * Opens and returns a file output stream for the given data file.
   * If the directory does not exist, it is created.
   * @param filepath  the relative path, e.g. "data/images/gui.png".
   * @param homedir   true, if the file should be in the home directory
   *                  instead of the game directory.
   */
  public static OutputStream createOutputStream(String filepath, boolean homedir)
    throws IOException
  {
    String file = "" + filepath;
    if (homedir)
      file = getGameHomeDir() + file;
    File dir = new File(file).getParentFile();
    if (dir != null && !dir.exists())
      dir.mkdirs();
    return new FileOutputStream(new File(file));
  }
  
  
  /**
   * Gets the user's home directory.
   */
  public static String getHomeDir()
  {
    return System.getProperty("user.home");
  }
  
  
  /**
   * Gets the name of the game's home directory.
   * It will return "Bolzplatz" on Windows and
   * ".bolzplatz" on other systems.
   */
  public static String getGameSubDir()
  {
    if (gameSubDir == null)
    {
      String osName = System.getProperty("os.name");
      if (osName.startsWith("Windows"))
        gameSubDir = "Bolzplatz";
      else
        gameSubDir = ".bolzplatz";
    }
    return gameSubDir;
  }
  
  
  /**
   * Gets the game's home directory with "/" at the end.
   */
  public static String getGameHomeDir()
  {
    return System.getProperty("user.home") + "/" + getGameSubDir() + "/";
  }

}
