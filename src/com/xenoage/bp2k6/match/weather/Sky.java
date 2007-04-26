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
package com.xenoage.bp2k6.match.weather;

import net.sf.jirr.*;


/**
 * The sky, created by a skybox
 * (only a visual effect).
 *
 * @author Andi
 */
public class Sky
{
  
  private ISceneNode node;
  private ITexture[] textures = new ITexture[6];

  
  public Sky(String id, ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    videoDriver.setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, false);
    videoDriver.setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_ALWAYS_32_BIT, true);
    String format = ".tga";
    textures[0] = videoDriver.getTexture("data/skies/" + id + "-top" + format);
    textures[1] = videoDriver.getTexture("data/skies/" + id + "-bottom" + format);
    textures[2] = videoDriver.getTexture("data/skies/" + id + "-left" + format);
    textures[3] = videoDriver.getTexture("data/skies/" + id + "-right" + format);
    textures[4] = videoDriver.getTexture("data/skies/" + id + "-front" + format);
    textures[5] = videoDriver.getTexture("data/skies/" + id + "-back" + format);
    node = sceneManager.addSkyBoxSceneNode(textures[0], textures[1], textures[2],
      textures[3], textures[4], textures[5]);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_ZBUFFER, false);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_BILINEAR_FILTER, true);
    node.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, false);
    //node.getMaterial(0).setDiffuseColor(new SColor(255, 0, 0, 255));
    //Scaling does not work with skyboxes.
    //Irrlicht must be patched: The skybox should have 1000x1000x1000 units.
    //Otherwise it can not be seen with a near clipping plane of 100 units.
    //node.setScale(new vector3df(100f, 100f, 100f));
  }
  
  
  /**
   * Removes this sky from the scene.
   */
  public void removeFromScene()
  {
    node.remove();
    node = null;
  }
  
  
  /**
   * Set visibility on or off.
   */
  public void setVisible(boolean visible)
  {
    if (node != null)
      node.setVisible(visible);
  }

}
