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

import com.xenoage.bp2k6.Main;

import java.io.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class contains some functions to find out usefull resolutions
 *
 * @author   Markus Madeja
 * @author   Andreas Wenger
 *
 * Example:
   Resolution resolution = new Resolution(device);
   int[][] aufl = resolution.getAvailableVideoModes();
   int k=-1;
 //Alle Auflösungen anzeigen
 for (int i = 0; i < aufl.length; i++) {
     System.out.print(aufl[i][0] + " " + aufl[i][1] + " " + aufl[i][2]+"\n"  );
   }
  try
  {
    k = resolution.getSelectedVideoMode();
  }
  catch (Exception e)
  {
    k=resolution.getDefaultVideoMode();
  }
//ausgewählte auflösung loggen...
  Logging.log(0,""+aufl[k][0]+" "+aufl[k][1]+" "+aufl[k][2]+" ");
//speichern
  try
  {
    resolution.saveSelectedVideoMode(k);
  }
  catch (IOException ex1)
  {
  }
 *
 */


public class Resolution
{
  private int[][] reso;
  private int resoanz;
 

  /**
   * Initialisation, sucht und findet die Auflösungen
   */
  public Resolution()
  {
    try
    {
      DisplayMode[] modes = Display.getAvailableDisplayModes();
      int resomax = modes.length;
      int[][] res = new int[resomax][3];
  
      //read video modes
      for (int i = 0; i < modes.length; i++)
      {
        if (modes[i].getWidth() >= 640 && modes[i].getBitsPerPixel() >= 16)
        {
          //add it, if not already in list
          boolean found = false;
          for (int i2 = 0; i2 < resoanz; i2++)
          {
            if (res[i2][0] == modes[i].getWidth() &&
              res[i2][1] == modes[i].getHeight() &&
              res[i2][2] == modes[i].getBitsPerPixel())
            {
              found = true;
              break;
            }
          }
          if (!found)
          {
            res[resoanz][0] = modes[i].getWidth();
            res[resoanz][1] = modes[i].getHeight();
            res[resoanz][2] = modes[i].getBitsPerPixel();
            resoanz++;
          }
        }
      }
      
      //copy video modes
      reso = new int[resoanz][3];
      for (int i = 0; i < resoanz; i++)
      {
        reso[i][0] = res[i][0];
        reso[i][1] = res[i][1];
        reso[i][2] = res[i][2];
      }
      
      //sort video modes
      int temp;
      for(int i = 0; i < resoanz - 1; i++)
      {
        for (int i2 = 0; i2 < resoanz - i - 1; i2++)
        {
          if(reso[i2][0] > reso[i2 + 1][0])
          {
            for(int iTemp = 0; iTemp < 3; iTemp++)
            {
              temp = reso[i2 + 1][iTemp];
              reso[i2 + 1][iTemp] = reso[i2][iTemp];
              reso[i2][iTemp] = temp; 
            }
          }
        }
      }
      
     }
    catch (LWJGLException e)
    {     
      Main.fatalError(this, new Exception("Unable to determine display modes!"));
    }
  }


  /**
   * Liefert das Ergebnis der Suche
   */

  public int[][] getAvailableVideoModes () {return reso;}

  /**
   * Stürzt ab wenn width, height und depth nicht da sind aber die datei schon
   */
  public int getSelectedVideoMode() throws Exception
  {
    Document doc = XMLReader.readFile("data/config/video.xml");
    Node root = doc.getFirstChild();
    Node eWidth = XMLReader.getChildElement(root, "width");
    Node eHeight = XMLReader.getChildElement(root, "height");
    Node eDepth = XMLReader.getChildElement(root, "depth");
    if (eWidth == null || eHeight == null || eDepth == null)
      throw new Exception("Could not read selected video mode.");
    Integer w = new Integer(eWidth.getTextContent());
    Integer h = new Integer(eHeight.getTextContent());
    Integer d = new Integer(eDepth.getTextContent());
    for(int i = 0; i < resoanz; i++) if ((reso[i][0]==w.intValue())&&(reso[i][1]==h.intValue())&&(reso[i][2]==d.intValue())) return i;
      throw new Exception("Screen settings not avaible.");
  }

  
  /**
   * saves
   * @param res the number of the selected resolution of the array,
   *            or -1 to save the resolution 640 x 480 x 16
   */
  public void saveSelectedVideoMode(int res, boolean fullscreen) throws IOException
  {
    try
    {
      Document doc = XMLWriter.createEmptyDocument();
      Node root = doc.appendChild(doc.createElement("resolution"));
      Node eWidth = doc.createElement("width");
      root.appendChild(eWidth);
      Node eHeight = doc.createElement("height");
      root.appendChild(eHeight);
      Node eDepth = doc.createElement("depth");
      root.appendChild(eDepth);
      if (res > -1)
      {
        eWidth.setTextContent(""+reso[res][0]);
        eHeight.setTextContent(""+reso[res][1]);
        eDepth.setTextContent(""+reso[res][2]);
      }
      else
      {
        eWidth.setTextContent("640");
        eHeight.setTextContent("480");
        eDepth.setTextContent("16");
      }
      Node eFullscreen = doc.createElement("fullscreen");
      root.appendChild(eFullscreen);
      eFullscreen.setTextContent(""+fullscreen);
  
      XMLWriter.writeFile(doc, "data/config/video.xml");
  
  //   Output auf den Screen:
  //    OutputFormat format = OutputFormat.createPrettyPrint();
  //    writer = new XMLWriter( System.out, format );
  //    writer.write( document );
    }
    catch (Exception ex)
    {
      Logging.log(this, ex);
    }
  }

  /**
   * Return the default VideoMode
   * @return int
   */
  public int getDefaultVideoMode() {
    for(int i = 0; i < resoanz; i++) if ((reso[i][0]==1024)&&(reso[i][1]==768)&&(reso[i][2]==32)) return i;
    for(int i = 0; i < resoanz; i++) if ((reso[i][0]==1024)&&(reso[i][1]==768)&&(reso[i][2]==24)) return i;
    return -1;
  }


  /**
   * Returns the video mode at the given index.
   */
  public int[] getVideoModeByIndex(int index)
  {
    int[] ret = new int[3];
    for (int i = 0; i < 3; i++)
      ret[i] = reso[index][i];
    return ret;
  }
  
  
  /**
   * Gets a minimal video mode (for example the list of
   * available video modes is empty).
   */
  public int[] getMinimalVideoMode()
  {
    return new int[] {640, 480, 16};
  }


  /**
   * Reads fullscreen-setting from XML file
   */
  public boolean getSelectedFullscreenMode() throws Exception
  {
    Document doc = XMLReader.readFile("data/config/video.xml");
    Node eFullscreen = XMLReader.getChildElement(
      doc.getFirstChild(), "fullscreen");
    return Boolean.parseBoolean(eFullscreen.getTextContent());
  }

  /**
   * Find the index of the video mode with the given width,
   * height and depth, or -1, if it is not available.
   */
  public int getVideoModeIndex(int width, int height, int depth)
  {
    for (int i = 0; i < resoanz; i++)
      if (reso[i][0] == width && reso[i][1] == height && reso[i][2] == depth)
        return i;
    return -1;
  }



}
