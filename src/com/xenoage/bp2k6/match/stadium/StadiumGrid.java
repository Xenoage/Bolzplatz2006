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
package com.xenoage.bp2k6.match.stadium;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.UniqueIDCreator;

import net.sf.jirr.*;


/**
 * Plane mesh with a grid on it, that
 * shows the default positions of
 * stadium stands.
 * 
 * @author Andreas Wenger
 */
public class StadiumGrid
{
  private ISceneNode node;
  
  public StadiumGrid(ISceneManager smgr, IVideoDriver videoDriver)
  {
    videoDriver.setTextureCreationFlag(E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, false);
    
    String path = "data/meshes/stadiumgrid.x";
    IAnimatedMesh mesh = smgr.getMesh(path);

    if (mesh == null)
      GameEngine.fatalError(this,
        new Exception("StadiumGrid mesh could not be loaded: \"" + path + "\""));

    node = SceneNodeFactory.createAnimatedMeshSceneNode(
      smgr, mesh, UniqueIDCreator.BITMASK_NONE,
      GameEngine.UNITS_GAME_TO_IRRLICHT, false, false);
    node.setPosition(new vector3df(0, 0.1f * GameEngine.UNITS_GAME_TO_IRRLICHT, 0));
  }
  
  
  public void setVisible(boolean b)
  {
    node.setVisible(b);
  }
  
}
