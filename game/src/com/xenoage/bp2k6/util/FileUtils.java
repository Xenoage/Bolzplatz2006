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

import com.xenoage.bp2k6.util.language.Language;

import java.io.*;
import javax.swing.filechooser.FileFilter;


/**
 * Contains some useful methods for files.
 *
 * @author Andreas Wenger
 */
public class FileUtils
{

  /**
   * Returns a filename filter for files with ".xml" ending.
   */
  public static FilenameFilter getXMLFilter()
  {
    FilenameFilter ret = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
          return name.toLowerCase().endsWith(".xml");
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filename filter for files with ".ogg" ending.
   */
  public static FilenameFilter getOGGFilter()
  {
    FilenameFilter ret = new FilenameFilter()
    {
      public boolean accept(File dir, String name)
      {
          return name.toLowerCase().endsWith(".ogg");
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filechooser filter for stadium files with ".xml" ending.
   */
  public static FileFilter getStadiumFilter()
  {
    FileFilter ret = new FileFilter()
    {
      @Override public String getDescription()
      {
        return "Stadien (.xml)";
      }
      @Override public boolean accept(File file)
      {
          return file.getName().toLowerCase().endsWith(".xml");
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filechooser filter for 3d object files
   * (*.x and *.ms3d)
   */
  public static FileFilter get3DObjectFilter()
  {
    FileFilter ret = new FileFilter()
    {
      @Override public String getDescription()
      {
        return Language.get("filefilter_3dobjects");
      }
      @Override public boolean accept(File file)
      {
        return file.getName().toLowerCase().endsWith(".x") ||
          file.getName().toLowerCase().endsWith(".ms3d");
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filechooser filter for terrain files with ".xml" ending.
   */
  public static FileFilter getTerrainFilter()
  {
    FileFilter ret = new FileFilter()
    {
      @Override public String getDescription()
      {
        return Language.get("filefilter_terrains");
      }
      @Override public boolean accept(File file)
      {
          return file.getName().toLowerCase().endsWith(".xml");
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filechooser filter for team logo files with ".png" ending.
   */
  public static FileFilter getTeamLogoFilter()
  {
    FileFilter ret = new FileFilter()
    {
      @Override public String getDescription()
      {
        return Language.get("teamlogofilter");
      }
      @Override public boolean accept(File file)
      {
          return file.getName().toLowerCase().endsWith(".png") ||
            file.isDirectory();
      }
    };
    return ret;
  }
  
  
  /**
   * Returns a filechooser filter for team logo files with ".png" ending.
   */
  public static FileFilter getTeamAdsFilter()
  {
    FileFilter ret = new FileFilter()
    {
      @Override public String getDescription()
      {
        return Language.get("teamadsfilter");
      }
      @Override public boolean accept(File file)
      {
          return file.getName().toLowerCase().endsWith(".png") ||
            file.isDirectory();
      }
    };
    return ret;
  }


  /**
   * Returns the name without extension of
   * the given file.
   */
  public static String getNameWithoutExtension(File f)
  {
    String ret = f.getName();
    int i = ret.lastIndexOf('.');
    if (i > 0 && i < ret.length() - 1)
      ret = ret.substring(0, i);
    return ret;
  }
  
  
  /**
   * Copies a file.
   */
  public static void copy(File src, File dst)
    throws Exception
  {
    InputStream in = new FileInputStream(src);
    OutputStream out = new FileOutputStream(dst);

    // Transfer bytes from in to out
    byte[] buf = new byte[1024];
    int len;
    while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
    }
    in.close();
    out.close();
  }
  
  
  /**
   * Clears the folder "data/temp/".
   */
  public static void clearTempFolder()
  {
    try
    {
      String files[] = new File("data/temp").list();
      for (int i = 0; i < files.length; i++)
      {
        new File("data/temp/" + files[i]).delete();
      }
      Logging.log(Logging.LEVEL_MESSAGES, null,
        "Deleted " + files.length + " temp files.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_WARNINGS, null,
        "Could not clear temp folder!");
    }
  }


}
