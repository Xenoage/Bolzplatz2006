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

import com.xenoage.bp2k6.GameEngine;

import net.sf.jirr.*;


/**
 * Helper functions for loading textures.
 * 
 * @author Andreas Wenger
 */
public class TextureFactory
{
  
  private static IVideoDriver videoDriver;
  
  
  /**
   * Sets the video driver. Must be called before
   * loadTexture() can be used.
   */
  public static void init(IVideoDriver driver)
  {
    videoDriver = driver;
  }
  
  
  /**
   * Loads the given texture with or without mipmapping.
   */
  public static ITexture loadTexture(String path, boolean mipmaps)
  {
    if (videoDriver == null)
      return null;
    videoDriver.setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, mipmaps);
    ITexture ret = videoDriver.getTexture(path);
    videoDriver.setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, GameEngine.VIDEO_MIPMAPS);
    return ret;
  }
  
  
}
