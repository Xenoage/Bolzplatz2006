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

import java.util.List;
import javax.vecmath.*;

import org.w3c.dom.*;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.util.*;

import net.sf.jirr.*;


/**
 * Class for a stadium stand.
 *
 * @author Andreas Wenger
 */
public class Stand
  extends StadiumObject
{

  
  //modification of the stand
  private String modification;
  
  //next available modification
  //(if it was the last one, this is the first
  //modification of the file)
  private String nextModification = "";
  
  //mesh file name of the modification
  private String meshFileName;
  
  //size of the stand in meters
  private Point2f size;
  
  //maximum number of spectators
  private int maxSpectators;
  
  //only for career stadiums: to which block does this stand belong to?
  private int careerBlockStage;
  private int careerBlockAlignment;
  
  
  /**
   * Loads a stand from a xml file.
   * @param id The id of the stand
   * @param modification The modification to load, or <code>null</code>
   * to use the default (=first) modification from the file.
   */
  public Stand(String id, String modification)
  {
    super(id);
    try
    {
      Document doc = XMLReader.readFile("data/stands/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      //size
      size = new Point2f();
      size.x = XMLReader.readAttributeValueFloat(root, "sizex");
      size.y = XMLReader.readAttributeValueFloat(root, "sizez");
      //modification
      Node eModificationList = XMLReader.getChildElement(root, "modifications");
      if (eModificationList == null)
        throw new Exception("No modifications list found!");
      List<Element> eModifications =
        XMLReader.elements(eModificationList, "modification");
      for (int i = 0; i < eModifications.size(); i++)
      {
        Node e = eModifications.get(i);
        if (this.modification != null || i == 0)
        {
          nextModification = XMLReader.readAttributeValue(e, "id");
          if (i != 0) break;
        }
        if (modification == null ||
          XMLReader.readAttributeValue(e, "id").equals(modification))
        {
          this.modification = XMLReader.readAttributeValue(e, "id");
          this.meshFileName = XMLReader.readAttributeValue(e, "src");
          this.maxSpectators = XMLReader.readAttributeValueInt(e, "maxspectators");
        }
      }
      if (this.modification == null)
        this.modification = modification;
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Stand \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Adds this stand to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    //load mesh
    try
    {
      String modNotNull = (modification == null) ? "" : new String(modification);
      if (meshFileName != null)
      {
        IAnimatedMesh mesh = sceneManager.getMesh("data/meshes/" + meshFileName);
        if (mesh == null)
          throw new Exception("Stand mesh \"" +
            meshFileName + "\" could not be loaded!");
        node = SceneNodeFactory.createAnimatedMeshSceneNode(
          sceneManager, mesh, UniqueIDCreator.BITMASK_STADIUMOBJECTS,
          GameEngine.UNITS_GAME_TO_IRRLICHT, true, true);
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Loaded stand \"" + id + "\", modification \"" + modNotNull + "\"");
        if (Settings.isVideoLowQuality())
        {
          //low video quality mode: replace concrete3.png to concrete6.png
          //by concrete 2!
          //not done... viewer showed no fps difference...
        }
      }
      else
      {
        throw new Exception("Modification \"" + modNotNull + "\" not found!");
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Stand \"" + id + "\" could not be added to scene! Details:");
      Main.fatalError(this, ex);
    }
    setPosition(position);
    setRotation(rotation);
  }
  
  
  /**
   * Gets the size in meters of the base of
   * this stand.
   */
  public Point2f getSize()
  {
    return size;
  }


  
  public String getModification()
  {
    return modification;
  }
  
  
  /**
   * Sets this stand to a block (only for career stadiums).
   */
  public void setCareerBlock(int stage, int alignment)
  {
    this.careerBlockStage = stage;
    this.careerBlockAlignment = alignment;
  }
  
  
  /**
   * Gets the stage of the block, to which this stand belongs to,
   * or 0 if it does not belong to a career block.
   */
  public int getCareerBlockStage()
  {
    return this.careerBlockStage;
  }
  
  
  /**
   * Gets the alignment of the block, to which this stand belongs to,
   * or 0 if it does not belong to a career block.
   */
  public int getCareerBlockAlignment()
  {
    return this.careerBlockAlignment;
  }


  /**
   * Gets the maximum number of spectators of this stand.
   */
  public int getMaxSpectators()
  {
    return maxSpectators;
  }
  
  
  /**
   * Gets <code>true</code>, if this stand is a fanblock.
   */
  public boolean isFanblock(Stadium stadium)
  {
    int fanBlock = stadium.getFanCurveAlignment();
    float x = position.x;
    float z = position.z;
    if (fanBlock == Stadium.BLOCK_ALIGNMENT_NORTHEAST)
      return x > 0 && z > 0;
    else if (fanBlock == Stadium.BLOCK_ALIGNMENT_SOUTHEAST)
      return x > 0 && z < 0;  
    else if (fanBlock == Stadium.BLOCK_ALIGNMENT_SOUTHWEST)
      return x < 0 && z < 0;
      else if (fanBlock == Stadium.BLOCK_ALIGNMENT_NORTHWEST)
      return x < 0 && z > 0;
    return false;
  }
  
  
  /**
   * Gets the next possible stand modification,
   * with the same position and rotation as
   * this stand.
   * To use it, remove this stand and add the returned one
   * to the scene.
   */
  public Stand getNextModification()
  {
    Stand ret = new Stand(id, nextModification);
    ret.setPosition(position);
    ret.setRotation(rotation);
    return ret;
  }
  
}
