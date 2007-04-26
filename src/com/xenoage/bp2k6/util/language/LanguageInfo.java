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

import com.xenoage.bp2k6.util.ConfigFile;


/**
 * Contains the most important information
 * about a language pack
 *
 * @author Andreas Wenger
 */
public class LanguageInfo
{
  private String id;
  private String localName;
  private String internationalName;
  
  
  /**
   * Loads information from the given
   * language pack information file.
   */
  public LanguageInfo(String filePath) throws Exception
  {
    ConfigFile langInfo = new ConfigFile(filePath);
    this.id = langInfo.getValue("id");
    this.localName = langInfo.getValue("localname");
    this.internationalName = langInfo.getValue("intname");
  }


  /**
   * Gets the ID of this language pack.
   */
  public String getID()
  {
    return id;
  }


  /**
   * Gets the international name of this language file,
   * e.g. "German" or "French".
   */
  public String getInternationalName()
  {
    return internationalName;
  }


  /**
   * Gets the local name of this language file,
   * e.g. "Deutsch" or "Français".
   */
  public String getLocalName()
  {
    return localName;
  }
  
  
  
  
}
