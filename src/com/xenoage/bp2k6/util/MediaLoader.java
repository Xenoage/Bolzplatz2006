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

import net.sf.jirr.*;
import com.xenoage.bp2k6.GameEngine;
import java.io.File;


/**
 * Loads media files (textures, sounds, ...) into
 * the memory so that bp2k6 can use them.
 *
 * @author Andreas Wenger
 */
public class MediaLoader
{

  
  /**
   * Loads a texture.
   */
  public static ITexture loadTexture(String id)
  {
    ITexture ret = null;
    if (new File("data/" + id).exists())
      ret = TextureFactory.loadTexture("data/" + id, true);
    return ret;
  }

  
  /**
   * Loads a texture with a color key at position (0/0).
   */
  public static ITexture loadTextureColorKey(String id)
  {
    ITexture ret = loadTexture(id);
    GameEngine.getVideoDriver().makeColorKeyTexture(ret, new position2di(0, 0));
    return ret;
  }



}
