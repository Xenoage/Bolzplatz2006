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
package com.xenoage.bp2k6.gui.loader.parser;

import net.sf.jirr.*;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.util.*;

import org.w3c.dom.Node;


/**
 * @author Andreas Wenger
 */
public class ImageParser
{

  public static Image parseImage(Node e,
    Screen parentScreen, Group parentGroup)
  {
    Image c = new Image();
    //base control
    BaseParser.parseBaseControl(e, c, parentScreen, parentGroup);
    //transparency
    String transparency = XMLReader.readAttributeValue(e, "transparency");
    if (transparency.length() > 0)
    {
      if (transparency.equals("firstpixel"))
        c.setTransparency(Image.ImageTransparency.FirstPixel);
      else if (transparency.equals("magenta"))
        c.setTransparency(Image.ImageTransparency.Magenta);
    }
    //source image
    String src = XMLReader.readAttributeValue(e, "src");
    ITexture tex = null;
    if (src.length() > 0)
    {
      tex = MediaLoader.loadTexture(src);
      if (tex == null)
        Logging.log(Logging.LEVEL_WARNINGS, new ImageParser(),
          "Image could not be loaded: \"" + src + "\"");
      c.setImage(tex);
    }
    //source image size
    int srcX = XMLReader.readAttributeValueInt(e, "srcx");
    int srcY = XMLReader.readAttributeValueInt(e, "srcy");
    c.setSrcPos(new Point2i(srcX, srcY));
    int srcWidth = XMLReader.readAttributeValueInt(e, "srcwidth");
    int srcHeight = XMLReader.readAttributeValueInt(e, "srcheight");
    c.setSrcSize(new Point2i(srcWidth, srcHeight));
    //rotation
    float rotation = XMLReader.readAttributeValueFloat(e, "rotation");
    if (rotation != 0)
      rotation *= MathTools.DEG_TO_RAD;
    c.setRotation(rotation);
    //images are not enabled by default
    c.setEnabled(false);
    return c;
  }

}
