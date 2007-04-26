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
package com.xenoage.bp2k6.util.language;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.*;

import java.io.File;
import java.util.*;

import org.w3c.dom.*;


/**
 * Class for a language pack.
 * When a language is loaded, all .xml files from
 * the given directory are read and all values are
 * stored. After that the entries of language pack can be
 * retrieved with <code>getEntry()</code>.
 *
 * @author Andreas Wenger
 */
public class Language
{
  
  private static Language thisObject = new Language();
  private static String currentLanguageID = null;
  private static Hashtable<String, String> entries = null;
  
  
  /**
   * Gets a list of the available languages.
   */
  public static ArrayList<LanguageInfo> getAvailableLanguages()
  {
    ArrayList<LanguageInfo> languagesInfo = new ArrayList<LanguageInfo>();
    String[] langFiles = new File("data/languages/").list(FileUtils.getXMLFilter());
    if (langFiles == null)
    {
      GameEngine.fatalError(thisObject, new Exception(
        "Languages directory does not exist!"));
    }
    else if (langFiles.length < 1)
    {
      GameEngine.fatalError(thisObject, new Exception(
        "No language pack installed!"));
    }
    else
    {
      for (int i = 0; i < langFiles.length; i++)
      {
        try
        {
          languagesInfo.add(new LanguageInfo("data/languages/" + langFiles[i]));
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_ERRORS, thisObject,
            "Error while loading language pack information from \"" +
            langFiles[i] + "\":");
          Logging.log(Logging.LEVEL_ERRORS, thisObject, ex);
        }
      }
    }
    return languagesInfo;
  }
  
  
  /**
   * Loads a language pack from the directory
   * <code>"data/languages/" + id</code>. All .xml
   * files in this directory are loaded.
   * If the method fails, the program is closed
   * with a fatal error.
   */
  public static void loadLanguage(String id)
  {
    loadLanguageFromPath(id, "data/languages/" + id);
  }
  
  
  /**
   * Loads a language pack from the directory
   * <code>"data/languages/" + id</code>. Use the
   * given path.
   */
  public static void loadLanguageFromPath(String id, String path)
  {
    currentLanguageID = id;
    String currentFile = "";
    try
    {
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "Loading language pack \"" + id + "\"...");
      String[] langFiles =
        new File(path).list(FileUtils.getXMLFilter());
      if (langFiles == null)
      {
        //if language was not found, use "en".
        //if it was already "en", raise a fatal error.
        if (id.equals("en"))
        {
          Main.fatalError(new Language(), new Exception(
            "Language pack \"" + id + "\" not found!"));
        }
        else
        {
          Logging.log(Logging.LEVEL_ERRORS, thisObject,
            "Language pack \"" + id + "\" not found! Trying to load \"en\"...");
          loadLanguage("en");
        }
      }
      else
      {
        entries = new Hashtable<String, String>(100);
        int entriesCount = 0;
        int entriesOverwrittenCount = 0;
        for (int iFiles = 0; iFiles < langFiles.length; iFiles++)
        {
          currentFile = new File(langFiles[iFiles]).getName();
          Logging.log(Logging.LEVEL_MESSAGES, thisObject,
            "Reading language file \"" + currentFile + "\"...");
          Document doc = XMLReader.readFile(path + "/" + langFiles[iFiles]);
          Node root = doc.getFirstChild();
          List<Element> eEntries = XMLReader.elements(root, "entry");
          for (int i = 0; i < eEntries.size(); i++)
          {
            Node e = eEntries.get(i);
            String eID = XMLReader.readAttributeValue(e, "id");
            String eValue = XMLReader.readAttributeValue(e, "value");
            String oldValue = entries.put(eID, eValue);
            if (oldValue == null)
              entriesCount++;
            else
            {
              Logging.log(Logging.LEVEL_WARNINGS, thisObject,
                "Overwritten entry: " + eID);
              entriesOverwrittenCount++;
            }
          }
          Logging.log(Logging.LEVEL_MESSAGES, thisObject,
            "Language pack loaded. Entries: " + entriesCount + 
              ". Overwritten entries: " + entriesOverwrittenCount);
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, thisObject,
        "Language pack \"" + id + "\" could not be loaded! " +
        "Reading file \"" + currentFile + "\" failed! Details:");
      Main.fatalError(thisObject, ex);
    }
  }
  
  
  /**
   * Gets a entry from the language pack.
   * If no value is found, the id is given back
   * (because it's more useful for the user
   * than an empty string).
   */
  public static String get(String id)
  {
    String ret = getWithNull(id);
    if (ret == null)
      return id;
    else
      return ret;
  }
  
  
  /**
   * Gets a entry from the language pack.
   * If no value is found, <code>null</code> is given back.
   */
  public static String getWithNull(String id)
  {
    String ret = entries.get(id);
    if (ret == null)
      return null;
    else
    {
      ret = ret.replace("\\n", "\n");
      return ret;
    }
  }


  /**
   * Gets the ID of the current language.
   */
  public static String getCurrentLanguageID()
  {
    return currentLanguageID;
  }
  
  
  /**
   * Gets the ID of the selected language
   * from "data/config/language.xml".
   */
  public static String getSelectedLanguageID()
  {
    try
    {
      ConfigFile langConfig = new ConfigFile("data/config/language.xml");
      return langConfig.getValue("id");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, thisObject, "Language config is invalid! Details:");
      Logging.log(Logging.LEVEL_ERRORS, thisObject, ex);
      return "en";
    }
  }
  
  
}
