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


/**
 * This class contains methods that help to create
 * Irrlicht scene nodes. Use it whenevery possible.
 * It's easier than creating nodes directly, and you
 * can not forget important things like setting
 * lighting or IDs.
 * 
 * @author Andreas Wenger
 */
public class SceneNodeFactory
{

  /**
   * Creates an IAnimatedMeshSceneNode instance.
   * Bilinear filtering is on.
   */
  public static IAnimatedMeshSceneNode createAnimatedMeshSceneNode(
    ISceneManager sceneManager, IAnimatedMesh mesh,
    int idBitmask, float scale,
    boolean lighting, boolean fog)
  {
    IAnimatedMeshSceneNode ret =
      sceneManager.addAnimatedMeshSceneNode(mesh);
    ret.setMaterialFlag(E_MATERIAL_FLAG.EMF_BILINEAR_FILTER, true);
    ret.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, lighting);
    ret.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, fog);
    ret.setScale(new vector3df(scale, scale, scale));
    ret.setID(UniqueIDCreator.createUniqueID(idBitmask));
    ret.updateAbsolutePosition();
    return ret;
  }
  
}
