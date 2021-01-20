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

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;


/**
 * This class contains some helper functions
 * to write XML files, using Java's
 * built-in JAXP.
 *
 * @author Andreas Wenger
 */
public class XMLWriter
{
  
  
  /**
   * Creates an empty DOM document.
   */
  public static Document createEmptyDocument()
    throws Exception
  {
    try
    {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      return builder.newDocument();
    }
    catch (Exception ex)
    {
      throw new Exception("Creating a new DOM document failed: " +
        ex.toString());
    }
  }
  

  /**
   * Writes the given XML document into a file
   * at the given path.
   */
  public static void writeFile(Document doc, String path)
    throws IOException
  {
    File f = new File(path);
    FileOutputStream fos = new FileOutputStream(f);
    writeStream(doc, fos);
  }
  
  
  /**
   * Writes the given XML document into the given output stream.
   */
  public static void writeStream(Document doc, OutputStream outputStream)
    throws IOException
  {
    try
    {
      TransformerFactory tFactory =
        TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(
        "{http://xml.apache.org/xslt}indent-amount", "2");
      
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(outputStream);
      
      transformer.transform(source, result);
    }
    catch (Exception ex)
    {
      throw new IOException("Problem with XML-Transformer: " +
        ex.toString());
    }
  }
  
  
  /**
   * Adds and returns a new element with the given
   * name as a child of the given element.
   */
  public static Node addElement(String name, Node parentElement)
  {
    //get the parent document.
    //parentElement may be the document itself.
    Document parentDoc = parentElement.getOwnerDocument();
    Document doc = (parentDoc != null ? parentDoc : (Document) parentElement);
    //add element
    Node ret = doc.createElement(name);
    parentElement.appendChild(ret);
    return ret;
  }
  
  
  /**
   * Adds a new attribute with the given
   * name and value to the given element.
   */
  public static void addAttribute(Node element, String name, String value)
  {
    ((Element) element).setAttribute(name, value);
  }
  
  
}
