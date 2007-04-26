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

import java.awt.Color;
import java.io.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class parses a simple XML file
 * with key/value pairs (like a .ini file).
 * 
 * @author Andreas Wenger
 */
public class ConfigFile
{
  
  private String filename;
  private Document document;
  private Node root;
  
  
  /**
   * Parses the given config XML file.
   */
  public ConfigFile(String filename)
    throws Exception
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Parsing config file \"" + filename + "\"...");
    this.filename = filename;
    document = XMLReader.readFile(filename);
    root = document.getFirstChild();
    Logging.log(Logging.LEVEL_MESSAGES, this, "Config file parsed.");
  }
  
  
  /**
   * Gets the string value of the property
   * with the given key.
   */
  public String getValue(String key)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e != null)
      return e.getTextContent();
    else
      return "";
  }
  
  
  /**
   * Gets the int value of the property
   * with the given key.
   */
  public int getValueInt(String key)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e != null)
      return Integer.parseInt(e.getTextContent());
    else
      return 0;
  }
  
  
  /**
   * Gets the float value of the property
   * with the given key.
   */
  public float getValueFloat(String key)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e != null)
      return Float.parseFloat(e.getTextContent());
    else
      return 0;
  }
  
  
  /**
   * Gets the color value of the property
   * with the given key.
   */
  public Color getValueColor(String key)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e != null)
      return ColorTools.getColor(e.getTextContent());
    else
      return new Color(0);
  }
  
  
  /**
   * Gets the string value of the given attribute
   * of the element with the given key.
   */
  public String getValue(String key, String attribute)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e != null)
      return XMLReader.readAttributeValue(e, attribute);
    else
      return "";
  }
  
  
  /**
   * Gets the root element of the config file.
   * It can be read and modified, and then saved
   * again by calling <code>saveToXML</code>.
   */
  public Node getRootElement()
  {
    return root;
  }
  
   
  /**
   * Sets the value of the property with the given key.
   * If the key does not exists, it is created.
   */
  public void setValue(String key, String value)
  {
    Node e = XMLReader.getChildElement(root, key);
    if (e == null)
      e = XMLWriter.addElement(key, root);
    e.setTextContent(value);
  }
  
  
  /**
   * Saves this config XML file.
   */
  public void saveToXML() throws IOException
  {
    try
    {
      XMLWriter.writeFile(document, filename);
    }
    catch (Exception ex)
    {
      Logging.log(this, ex);
    }
  }
  
  
  /**
   * Gets the string value of the property
   * with the given key from the given config file,
   * or "" if an error occurred or the value was not found.
   */
  public static String getValueFromFile(String configFile, String key)
  {
    try
    {
      ConfigFile conf = new ConfigFile(configFile);
      return conf.getValue(key);
    }
    catch (Exception ex)
    {
      return "";
    }
  }
  
  
  /**
   * Gets the boolean value of the property
   * with the given key from the given config file,
   * or false if an error occurred or the value was not found.
   */
  public static boolean getValueFromFileBoolean(String configFile, String key)
  {
    String s = getValueFromFile(configFile, key);
    try
    {
      return Boolean.parseBoolean(s);
    }
    catch (Exception ex)
    {
      return false;
    }
  }
  
  
  /**
   * Gets the boolean float of the property
   * with the given key from the given config file,
   * or 0 if an error occurred or the value was not found.
   */
  public static float getValueFromFileFloat(String configFile, String key)
  {
    String s = getValueFromFile(configFile, key);
    try
    {
      return Float.parseFloat(s);
    }
    catch (Exception ex)
    {
      return 0;
    }
  }
  
}
