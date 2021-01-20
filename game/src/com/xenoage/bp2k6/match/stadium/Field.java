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

import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.SceneNodeFactory;
import com.xenoage.bp2k6.util.TextureFactory;
import com.xenoage.bp2k6.util.language.Language;


/**
 * Class for a soccer field.
 *
 * @author Andi
 */
public class Field
{

  private ISceneNode nodeTerrain;

  //the length and width of the field (distance between goals) in meters
  private float length;
  private float width;
  
  //the length and width of the penalty area in meters
  private float penaltyLength;
  private float penaltyWidth;
  
  //distance of the penalty spot to the goalline
  private float penaltySpotDistance;
  
  //quality of the lawn
  public enum Lawn
  {
    MudField(1, 0, 4, "lawn_mudfield", 500, 0.4f),
    Grassland(2, 5, 9, "lawn_grassland", 1000, 0.6f),
    TendedLawn(3, 10, 14, "lawn_tendedlawn", 3000, 0.8f),
    Sod(4, 15, -1, "lawn_sod", 3000, 1f);
    
    public int textureID;
    public int minQuality;
    public int maxQuality;
    public String langID;
    public int lawnCareCost;
    public float crowdAttendance;
    
    Lawn(int textureID, int minQuality,
      int maxQuality, String langID, int lawnCareCost, float crowdAttendance)
    {
      this.textureID = textureID;
      this.minQuality = minQuality;
      this.maxQuality = maxQuality;
      this.langID = langID;
      this.lawnCareCost = lawnCareCost;
      this.crowdAttendance = crowdAttendance;
    }
    
    /**
     * Gets the lawn from the given quality points.
     */
    public static Lawn fromQuality(int quality)
    {
      for (int i = 0; i < values().length; i++)
      {
        if (values()[i].minQuality <= quality &&
          (values()[i].maxQuality == -1 ||
            values()[i].maxQuality >= quality))
          return values()[i];
      }
      return MudField; //default
    }
    
    /**
     * Gets the local name of the lawn.
     */
    public String getLocalName()
    {
      return Language.get(langID);
    }
    
    
  }
  private Lawn lawn;
  private int lawnQuality; //quality points (see design document)
  
  //center mat and team mats
  private String teamMatIDs[] = new String[2];
  private ISceneNode nodeCenterMat;
  private ISceneNode nodeTeamMats[] = new ISceneNode[4];
  private ITexture textureTeamMats[] = new ITexture[2];
  private int teamMatsSide = 0;
  
  //corner flags
  private ISceneNode nodeCornerFlags[] = new ISceneNode[4];
  
  //irrlicht objects
  ISceneManager sceneManager = null;
  IVideoDriver videoDriver = null;
  

  /**
   * Constructor.
   */
  public Field(int lawnQuality)
  {
    setLawnQuality(lawnQuality); 
    
    length = 40;
    width = 20;
    
    penaltyLength = 7;
    penaltyWidth = 11.3f;
    penaltySpotDistance = 5.2f;
  }
  
  
  /**
   * Set mats. addToScene() must be called before!
   */
  public void setMats(String centerMatID, String team1MatID, String team2MatID)
  {
    this.teamMatIDs[0] = team1MatID;
    this.teamMatIDs[1] = team2MatID;
    
    //add center mat to scene, if set
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    if (centerMatID != null)
    {
      IAnimatedMesh centerMat = sceneManager.getMesh("data/meshes/centermat.x");
      if (centerMat != null)
      {
        nodeCenterMat = SceneNodeFactory.createAnimatedMeshSceneNode(
          sceneManager, centerMat, 0, 0.4f * u, true, true);
        nodeCenterMat.setPosition(new vector3df(0, 0.05f * u, 0));
        nodeCenterMat.setRotation(new vector3df(0, 180, 0));
        ITexture textureCenterMat = videoDriver.getTexture(
          "data/meshes/centermat_" + centerMatID + ".png");
        nodeCenterMat.setMaterialTexture(0, textureCenterMat);
      }
    }
    
    //add team mats to scene, if set
    for (int iTeam = 0; iTeam < 2; iTeam++)
    {
      if (teamMatIDs[iTeam] != null)
      {
        IAnimatedMesh teamMat = sceneManager.getMesh("data/meshes/teammat.x");
        if (teamMat != null)
        {
          textureTeamMats[iTeam] = videoDriver.getTexture(
            "data/teams/" + teamMatIDs[iTeam] + "-ads.png");
          for (int iMat = 0; iMat < 2; iMat++)
          {
            nodeTeamMats[iTeam * 2 + iMat] =
              SceneNodeFactory.createAnimatedMeshSceneNode(
                sceneManager, teamMat, 0, u, true, true);
            nodeTeamMats[iTeam * 2 + iMat].setRotation(
              new vector3df(0, (iTeam * 2 - 1) * 90, 0));
            nodeTeamMats[iTeam * 2 + iMat].setPosition(
              new vector3df((-22 + iTeam * 44) * u, 0.05f * u, (-4 + iMat * 8) * u));
            nodeTeamMats[iTeam * 2 + iMat].setMaterialTexture(
              0, textureTeamMats[iTeam]);
          }
        }
      }
    }
  }
  
  
  /**
   * Adds this field to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    ISceneNode nodeTerrain)
  {
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    if (lawnQuality >= Lawn.TendedLawn.minQuality)
    {
      //use flags
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      IAnimatedMesh flagMesh = sceneManager.getMesh("data/meshes/cornerflag.x");
      for (int i = 0; i < 4; i++)
      {
        nodeCornerFlags[i] = SceneNodeFactory.createAnimatedMeshSceneNode(
          sceneManager, flagMesh, 0, 1.2f * u, true, true);
        nodeCornerFlags[i].setPosition(new vector3df(
          (i < 2 ? -1 : 1) * u * length / 2, 0,
          (i % 2 == 0 ? -1 : 1) * u * width / 2));
        nodeCornerFlags[i].updateAbsolutePosition();
      }
    }
    
    this.nodeTerrain = nodeTerrain;
    
    updateTexture();
    
  }

  public float getLength()
  {
    return length;
  }

  public float getWidth()
  {
    return width;
  }
  
  public float getPenaltyLength()
  {
    return penaltyLength;
  }

  public float getPenaltyWidth()
  {
    return penaltyWidth;
  }
  
  public float getPenaltySpotDistance()
  {
    return penaltySpotDistance;
  }
  
  /**
   * Removes this field from the scene.
   */
  public void removeFromScene()
  {
    //do not remove terrain node here.
    if (nodeCenterMat != null)
      nodeCenterMat.remove();
    if (nodeTeamMats[0] != null)
      nodeTeamMats[0].remove();
    if (nodeTeamMats[1] != null)
      nodeTeamMats[1].remove();
    for (int i = 0; i < 4; i++)
    {
      if (nodeCornerFlags[i] != null)
        nodeCornerFlags[i].remove();
    }
  }


  /**
   * Gets the lawn of the field.
   */
  public Lawn getLawn()
  {
    return lawn;
  }
  
  
  /**
   * Gets the quality of the lawn.
   */
  public int getLawnQuality()
  {
    return this.lawnQuality;
  }
  
  
  /**
   * Sets the quality of the lawn.
   */
  public void setLawnQuality(int lawnQuality)
  {
    this.lawnQuality = lawnQuality;
    if (this.lawnQuality < 0)
      this.lawnQuality = 0;
    this.lawn = Lawn.fromQuality(this.lawnQuality);
  }
  
  
  /**
   * Increases the quality of the lawn by 5 points.
   */
  public void lawnCare()
  {
    lawnQuality += 5;
    lawn = Lawn.fromQuality(lawnQuality);
    updateTexture();
  }
  
  
  /**
   * Decreases the quality of the lawn by 1 point.
   */
  public void lawnWear()
  {
    lawnQuality -= 1;
    if (lawnQuality < 0)
      lawnQuality = 0;
    lawn = Lawn.fromQuality(lawnQuality);
    updateTexture();
  }
  
  
  /**
   * Updates the lawn texture.
   */
  private void updateTexture()
  {
    if (nodeTerrain != null)
    {
      //environment map (no mip maps)
      ITexture texEnvi = TextureFactory.loadTexture(
        "data/meshes/envi" + lawn.textureID + ".png", false);
      nodeTerrain.getMaterial(1).setTexture(0, texEnvi);
      //field (no mip maps)
      ITexture texField = TextureFactory.loadTexture(
        "data/meshes/soccerfield" + lawn.textureID + ".png", false);
      nodeTerrain.getMaterial(2).setTexture(0, texField);
    }
  }
  
  
  /**
   * Sets the opacity of the center mat
   * to the given value between 0 and 1.
   */
  public void setCenterMatOpacity(float opacity)
  {
    if (nodeCenterMat != null)
    {
      float scale = 0.4f * GameEngine.UNITS_GAME_TO_IRRLICHT * opacity;
      nodeCenterMat.setScale(new vector3df(scale, scale, scale));
    }
  }
  
  
  /**
   * Removes the center mat from the scene.
   */
  public void removeCenterMat()
  {
    if (nodeCenterMat != null)
    {
      nodeCenterMat.remove();
      nodeCenterMat = null;
    }
  }
  
  
  /**
   * Exchange the team mats.
   */
  public void exchangeTeamMats()
  {
    if (nodeTeamMats[0] != null)
    {
      teamMatsSide = 1 - teamMatsSide;
      for (int i = 0; i < 2; i++)
      {
        nodeTeamMats[i + 0 * 2].setMaterialTexture(0, textureTeamMats[teamMatsSide]);
        nodeTeamMats[i + 1 * 2].setMaterialTexture(0, textureTeamMats[1 - teamMatsSide]);
      }
    }
  }
  
}
