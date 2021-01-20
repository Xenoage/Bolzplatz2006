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
package com.xenoage.bp2k6.match.terrain;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.util.*;

import java.util.*;

import org.w3c.dom.*;

import net.sf.jirr.*;


/**
 * Class for a terrain (map around the stadium)
 * with a skybox and periphery objects
 * (like housesn or trees).
 *
 * @author Andi
 */
public class Terrain
{
  private String id;
  private ISceneNode node;
  private ArrayList<ISceneNode> nodesPeriphery = new ArrayList<ISceneNode>();
  private ITexture textureTerrain;
  private ITexture textureDetailmap;
  
  
  public Terrain(String id, ISceneManager sceneManager, IVideoDriver videoDriver)
  {

    //read terrain information from XML file
    try
    {
      Document doc = XMLReader.readFile("data/terrains/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      //create terrain mesh
      //first alternative: when a mesh is given, load it
      String meshSrc = XMLReader.readAttributeValue(root, "mesh");
      String heightmapSrc = XMLReader.readAttributeValue(root, "heightmap");
      if (meshSrc.length() > 0)
      {
        //use mesh
        IAnimatedMesh meshTerrain =
          sceneManager.getMesh("data/meshes/" + meshSrc);
        node = SceneNodeFactory.createAnimatedMeshSceneNode(
          sceneManager, meshTerrain, UniqueIDCreator.BITMASK_NONE,
          GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
      }
      else if (heightmapSrc.length() > 0)
      {
        //use heightmap
        float heightmapSize = XMLReader.readAttributeValueFloat(
          root, "heightmapsize"); //size of the heightmap in pixel
        float terrainSize = XMLReader.readAttributeValueFloat(
          root, "terrainsize"); //size of the terrain in meters
        float terrainY = XMLReader.readAttributeValueFloat(
          root, "terrainy"); //y offset of the heightmap in meters
        float terrainHeight = XMLReader.readAttributeValueFloat(
          root, "terrainheight"); //max height of the terrain in meters
        float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
        node = sceneManager.addTerrainSceneNode(
          "data/meshes/" + heightmapSrc, null, UniqueIDCreator.BITMASK_NONE,
          new vector3df(-terrainSize / 2 * u,
            terrainY * u, -terrainSize / 2 * u), //position
          new vector3df(), //rotation - not implemented in irrlicht yet
          new vector3df(terrainSize / heightmapSize * u,
            terrainHeight * 0.01f * u, terrainSize / heightmapSize * u), //scale
          new SColor(255, 255, 255, 255) //vertex color
          );
        //node.setScale(new vector3df(500f / 128f * 100f, 100f, 500f / 128f * 100f));
        node.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
        node.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      }
      else
      {
        throw new Exception("Terrain must have a mesh or a heightmap!");
      }
      //load textures
      Node textures = XMLReader.getChildElement(root, "textures");
      if (textures != null)
      {
        List<Element> listTextures = XMLReader.elements(textures, "texture");
        for (int i = 0; i < listTextures.size(); i++)
        {
          Node t = listTextures.get(i);
          String textureType = XMLReader.readAttributeValue(t, "type");
          String src = XMLReader.readAttributeValue(t, "src");
          if (textureType.equals("level1"))
          {
            //normal texture
            if (src.length() > 0)
            {
              textureTerrain = videoDriver.getTexture("data/meshes/" + src);
              node.getMaterial(0).setTexture(0, textureTerrain);
            }
          }
          else if (textureType.equals("detailmap"))
          {
            //detailmap
            if (src.length() > 0)
            {
              textureDetailmap = videoDriver.getTexture("data/meshes/" + src);
              node.setMaterialTexture(1, textureDetailmap);
              node.setMaterialType(E_MATERIAL_TYPE.EMT_DETAIL_MAP);
              float tiles = XMLReader.readAttributeValueFloat(t, "tiles");
              if (tiles > 0 && node instanceof ITerrainSceneNode)
                ((ITerrainSceneNode) node).scaleTexture(1.0f, tiles);
            }
          }
        }
      }
      //read periphery objects (houses, trees, ...)
      Node ePeriphery = XMLReader.getChildElement(root, "periphery");
      if (ePeriphery != null)
      {
        List<Element> listMeshes = XMLReader.elements(ePeriphery, "mesh");
        for (int i = 0; i < listMeshes.size(); i++)
        {
          Node t = listMeshes.get(i);
          String src = XMLReader.readAttributeValue(t, "src");
          String texture = XMLReader.readAttributeValue(t, "texture");
          float x = XMLReader.readAttributeValueFloat(t, "x");
          float y = XMLReader.readAttributeValueFloat(t, "y");
          float z = XMLReader.readAttributeValueFloat(t, "z");
          float rot = XMLReader.readAttributeValueFloat(t, "rot");
          if (src.length() > 0)
          {
            IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + src);
            IAnimatedMeshSceneNode nodePeri =
              SceneNodeFactory.createAnimatedMeshSceneNode(
              sceneManager, mesh, UniqueIDCreator.BITMASK_NONE,
              2.54f, true, false);
            nodePeri.setRotation(new vector3df(0, rot, 0));
            nodePeri.setPosition(new vector3df(x * GameEngine.UNITS_GAME_TO_IRRLICHT,
              y * GameEngine.UNITS_GAME_TO_IRRLICHT, z * GameEngine.UNITS_GAME_TO_IRRLICHT));
            if (texture.length() > 0)
            {
              ITexture tex = videoDriver.getTexture("data/meshes/" + texture);
              nodePeri.setMaterialTexture(0, tex);
            }
            nodesPeriphery.add(nodePeri);
          }
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Terrain \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  public Terrain(String id)
  {
    this.id = id;
  }

  
  /**
   * Removes this terrain from the scene.
   */
  public void removeFromScene()
  {
    for (int i = 0; i < nodesPeriphery.size(); i++)
      nodesPeriphery.get(i).remove();
    node.remove();
  }
  
  
  /**
   * Set visibility on or off.
   */
  public void setVisible(boolean visible)
  {
    for (int i = 0; i < nodesPeriphery.size(); i++)
      nodesPeriphery.get(i).setVisible(visible);
    node.setVisible(visible);
  }


  /**
   * Gets the ID of this terrain.
   */
  public String getID()
  {
    return id;
  }
  
  
  /**
   * Gets the terrain scene node.
   */
  public ISceneNode getTerrainNode()
  {
    return node;
  }

}
