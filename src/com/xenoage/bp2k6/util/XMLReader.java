/**
 * Bolzplatz
 * Copyright (C) 2006-2007 by Xenoage Software
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

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 * This class contains some helper functions
 * to read values out of XML files with JAXP.
 *
 * @author Andreas Wenger
 */
public class XMLReader
{

  
  /**
   * Reads and returns the XML document at the given path.
   */
  public static Document readFile(String file)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(file);
  }
  
  
  /**
   * Reads and returns the XML document at the given input stream.
   */
  public static Document readFile(InputStream inputStream)
    throws ParserConfigurationException, SAXException, IOException
  {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(inputStream);
  }
  

  public static String readAttributeValue(Node element, String name)
  {
    if (element == null) return "";
    NamedNodeMap attributes = element.getAttributes();
    if (attributes == null) return "";
    Node value = attributes.getNamedItem(name);
    if (value == null) return "";
    return value.getTextContent();
  }
  
  
  public static String readTextTrim(Node parentElement, String element)
  {
    if (parentElement == null) return "";
    return readTextTrim(getChildElement(parentElement, element));
  }
  
  
  public static String readTextTrim(Node element)
  {
    if (element == null) return "";
    String ret = element.getTextContent();
    return ret != null ? ret.trim() : "";
  }
  
  
  public static int readAttributeValueInt(Node element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Integer.parseInt(val);
    else
      return 0;
  }
  
  
  public static int readAttributeValueIntHex(Node element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
       return Integer.parseInt(val, 16);
    else
      return 0;
  }

  
  public static float readAttributeValueFloat(Node element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Float.parseFloat(val);
    else
      return 0;
  }
  
  
  public static boolean readAttributeValueBoolean(Node element, String name)
  {
    String val = readAttributeValue(element, name);
    if (val.length() > 0)
      return Boolean.parseBoolean(val);
    else
      return false;
  }
  
  
  public static float readElementValueFloat(Node element)
  {
    if (element != null)
      return Float.parseFloat(element.getTextContent());
    else
      return 0;
  }
  
  
  /**
   * Gets the first child element of the given node with
   * the given name, or null if not found.
   */
  public static Node getChildElement(Node parent, String name)
  {
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      if (children.item(i).getNodeName().equals(name))
        return children.item(i);
    }
    return null;
  }
  
  
  /**
   * Gets the a list of all child elements of the given node with
   * the given name. If no such elements are found, the returned
   * list is empty. 
   */
  public static List<Element> elements(Node parent, String name)
  {
    ArrayList<Element> ret = new ArrayList<Element>();
    NodeList children = parent.getChildNodes();
    for (int i = 0; i < children.getLength(); i++)
    {
      if (children.item(i).getNodeName().equals(name))
        ret.add((Element) children.item(i));
    }
    return ret;
  }


}
