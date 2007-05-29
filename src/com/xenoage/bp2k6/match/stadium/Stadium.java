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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.*;

import org.w3c.dom.*;

import net.sf.jirr.*;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.physics.PhysicsObject;
import com.xenoage.bp2k6.match.physics.Wall;
import com.xenoage.bp2k6.match.stadium.attractions.*;
import com.xenoage.bp2k6.match.stadium.crowd.*;
import com.xenoage.bp2k6.match.stadium.floodlight.Floodlight;
import com.xenoage.bp2k6.match.stadium.goal.*;
import com.xenoage.bp2k6.match.stadium.objects.SimpleStadiumObject;
import com.xenoage.bp2k6.match.stadium.scoreboard.Scoreboard;
import com.xenoage.bp2k6.match.terrain.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;
import com.xenoage.bp2k6.util.math.Rectangle;


/**
 * Class for a stadium with a field,
 * goals, stands, traps, terrain
 * and so on.
 *
 * @author Andreas Wenger
 */
public class Stadium
{
  
  //most important information about the stadium
  private StadiumInfo stadiumInfo;
  //the terrain around this stadium
  private Terrain terrain = null;
  //the stands of this stadium
  private ArrayList<Stand> stands = new ArrayList<Stand>();
  //the crowd (audience)
  private Crowd crowd = null;
  //the adboards of this stadium
  private ArrayList<Adboard> adboards = new ArrayList<Adboard>();
  //the soccer field
  private Field field;
  //the goals
  Goal goals[] = new Goal[2];
  //the scoreboard
  private Scoreboard scoreboard = null;
  //the floodlights of this stadium
  private ArrayList<Floodlight> floodlights = new ArrayList<Floodlight>();
  //the attractions
  private HotDogStand hotDogStand = null;
  private BettingOffice bettingOffice = null;
  private BeerBar beerBar = null;
  private Track track = null;
  private HighJump highJump = null;
  //list of other stadium objects
  private ArrayList<SimpleStadiumObject> objects = new ArrayList<SimpleStadiumObject>();
  //the position the players come from (at the beginning)
  //and run to (at halftime)
  private Vector3f entrancePositon;
  
  //only for career stadiums: built stand blocks and adboard blocks
  public static final int BLOCK_ADBOARDS = 0;
  public static final int BLOCK_STAGE_1 = 1;
  public static final int BLOCK_STAGE_2 = 2;
  public static final int BLOCK_STAGE_3 = 3;
  public static final int BLOCK_ALIGNMENT_NORTH = 1;
  public static final int BLOCK_ALIGNMENT_NORTHEAST = 2;
  public static final int BLOCK_ALIGNMENT_EAST = 3;
  public static final int BLOCK_ALIGNMENT_SOUTHEAST = 4;
  public static final int BLOCK_ALIGNMENT_SOUTH = 5;
  public static final int BLOCK_ALIGNMENT_SOUTHWEST = 6;
  public static final int BLOCK_ALIGNMENT_WEST = 7;
  public static final int BLOCK_ALIGNMENT_NORTHWEST = 8;
  private boolean[][] builtCareerStandBlocks = new boolean[3][8];
  private boolean[] builtCareerAdboardBlocks = new boolean[8];
  
  //fancurve alignment (BLOCK_ALIGNMENT_)
  private int fanCurveAlignment = BLOCK_ALIGNMENT_SOUTHEAST;
  
  
  //irrlicht objects
  private ISceneManager sceneManager;
  private IVideoDriver videoDriver;
  private PhysicsManager physicsManager;
  
  
  /**
   * Constructor for an existing stadium (load from XML file).
   */
  public Stadium(String id)
  {
    
    stadiumInfo = new StadiumInfo(id);
    
    //read stadium information from XML file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading stadium \"" + id + "\"...");
    try
    {
      Document doc;
      //first look in config/newstadium folder; if not found,
      //look in temp folder if not found,
      //look in stadiums folder
      if (new java.io.File("data/config/newstadium/" + id + ".xml").exists())
        doc = XMLReader.readFile("data/config/newstadium/" + id + ".xml");
      else if (new java.io.File("data/temp/" + id + ".xml").exists())
        doc = XMLReader.readFile("data/temp/" + id + ".xml");
      else
        doc = XMLReader.readFile("data/stadiums/" + id + ".xml");
      Node root = doc.getFirstChild();
      
      //terrain
      Node eTerrain = XMLReader.getChildElement(root, "terrain");
      if (eTerrain != null)
      {
        String terrainID = XMLReader.readAttributeValue(eTerrain, "id");
        if (terrainID.length() > 0)
          terrain = new Terrain(terrainID);
      }
      if (terrain == null)
      {
        //if no terrain was set, use the default one
        terrain = new Terrain("terrain-default");
      }
      
      //field and lawn
      int lawnQuality = 0;
      Node eLawn = XMLReader.getChildElement(root, "lawn");
      if (eLawn != null)
      {
        lawnQuality = XMLReader.readAttributeValueInt(eLawn, "quality");
      }
      field = new Field(lawnQuality);
      
      //career stand blocks
      Node eCareerStandBlocks = XMLReader.getChildElement(root, "careerstandblocks");
      if (eCareerStandBlocks != null)
      {
        List<Element> listBlocks =
          XMLReader.elements(eCareerStandBlocks, "block");
        for (int i = 0; i < listBlocks.size(); i++)
        {
          Node eBlock = listBlocks.get(i);
          int stage = XMLReader.readAttributeValueInt(eBlock, "stage") - 1;
          int alignment = XMLReader.readAttributeValueInt(eBlock, "alignment") - 1;
          builtCareerStandBlocks[stage][alignment] = true;
        }
      }
      
      //career adboard blocks
      Node eAdboardStandBlocks =
        XMLReader.getChildElement(root, "careeradboardblocks");
      if (eAdboardStandBlocks != null)
      {
        List<Element> listBlocks =
          XMLReader.elements(eAdboardStandBlocks, "block");
        for (int i = 0; i < listBlocks.size(); i++)
        {
          Node eBlock = listBlocks.get(i);
          int alignment = XMLReader.readAttributeValueInt(eBlock, "alignment") - 1;
          builtCareerAdboardBlocks[alignment] = true;
        }
      }
      
      //stands
      Node eStands = XMLReader.getChildElement(root, "stands");
      if (eStands != null)
      {
        List<Element> listStands = XMLReader.elements(eStands, "stand");
        int i = 0;
        stadiumInfo.setMaxSpectators(0);
        for (i = 0; i < listStands.size(); i++)
        {
          Node eStand = listStands.get(i);
          String standID = XMLReader.readAttributeValue(eStand, "id");
          String standModification =
            XMLReader.readAttributeValue(eStand, "modification");
          Stand stand = new Stand(standID, standModification);
          stand.setPositionAndRotationFromXMLElement(eStand);
          int stage = XMLReader.readAttributeValueInt(eStand, "stage");
          int alignment = XMLReader.readAttributeValueInt(eStand, "alignment");
          if (stage > 0 && alignment > 0)
          {
            stand.setCareerBlock(stage, alignment);
          }
          addStand(stand);
        }
        Logging.log(Logging.LEVEL_MESSAGES, this,
          i + " stands loaded.");
      }
      
      //adboards
      Node eAdboards = XMLReader.getChildElement(root, "adboards");
      if (eAdboards != null)
      {
        List<Element> listAdboards = XMLReader.elements(eAdboards, "adboard");
        int i = 0;
        for (i = 0; i < listAdboards.size(); i++)
        {
          Node eAdboard = listAdboards.get(i);
          String adboardID = XMLReader.readAttributeValue(eAdboard, "id");
          Adboard adboard = new Adboard(adboardID);
          adboard.setPositionAndRotationFromXMLElement(eAdboard);
          int alignment = XMLReader.readAttributeValueInt(eAdboard, "alignment");
          if (alignment > 0)
          {
            adboard.setCareerBlock(alignment);
          }
          adboards.add(adboard);
        }
        Logging.log(Logging.LEVEL_MESSAGES, this,
          i + " stands loaded.");
      } 
      
      //load goals
      StadiumInfo.Goals goalsType = stadiumInfo.getGoals();
      if (goalsType == StadiumInfo.Goals.GarbageCans)
      {
        goals[0] = new GarbageCansGoal(new Vector3f(20f, 0, 0), 90);
        goals[1] = new GarbageCansGoal(new Vector3f(-20f, 0, 0), -90);
      }
      else if (goalsType == StadiumInfo.Goals.WoodenGoals)
      {
        goals[0] = new WoodenGoal(new Vector3f(20f, 0, 0), 90);
        goals[1] = new WoodenGoal(new Vector3f(-20f, 0, 0), -90);
      }
      else
      {
        goals[0] = new AluminumGoal(new Vector3f(20f, 0, 0), 90);
        goals[1] = new AluminumGoal(new Vector3f(-20f, 0, 0), -90);
      }
      
      //scoreboard
      Node eScoreboard = XMLReader.getChildElement(root, "scoreboard");
      if (eScoreboard != null)
      {
        int stage = XMLReader.readAttributeValueInt(eScoreboard, "stage");
        scoreboard = new Scoreboard(stage);
        scoreboard.setPositionAndRotationFromXMLElement(eScoreboard);
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Scoreboard " + stage + " loaded.");
      } 
      
      //floodlights
      Node eFloodlights = XMLReader.getChildElement(root, "floodlights");
      if (eFloodlights != null)
      {
        List<Element> listFloodlights =
          XMLReader.elements(eFloodlights, "floodlight");
        int i = 0;
        for (i = 0; i < listFloodlights.size(); i++)
        {
          Node eFloodlight = listFloodlights.get(i);
          int stage = XMLReader.readAttributeValueInt(eFloodlight, "stage");
          int careerposition =
            XMLReader.readAttributeValueInt(eFloodlight, "careerposition");
          Floodlight floodlight = new Floodlight(stage, careerposition);
          floodlight.setPositionAndRotationFromXMLElement(eFloodlight);
          floodlights.add(floodlight);
        }
        Logging.log(Logging.LEVEL_MESSAGES, this,
          i + " floodlights loaded.");
      } 
      
      //attractions
      Node eAttractions = XMLReader.getChildElement(root, "attractions");
      if (eAttractions != null)
      {
        //hot dog stand
        Node eHotDogStand = XMLReader.getChildElement(eAttractions, "hotdogstand");
        if (eHotDogStand != null)
        {
          hotDogStand = new HotDogStand();
          hotDogStand.setPositionAndRotationFromXMLElement(eHotDogStand);
        }
        //betting office
        Node eBettingOffice =
          XMLReader.getChildElement(eAttractions, "bettingoffice");
        if (eBettingOffice != null)
        {
          bettingOffice = new BettingOffice();
          bettingOffice.setPositionAndRotationFromXMLElement(eBettingOffice);
        }
        //beer bar
        Node eBeerBar =
          XMLReader.getChildElement(eAttractions, "beerbar");
        if (eBeerBar != null)
        {
          beerBar = new BeerBar();
          beerBar.setPositionAndRotationFromXMLElement(eBeerBar);
        }
        //track
        Node eTrack = XMLReader.getChildElement(eAttractions, "track");
        if (eTrack != null)
        {
          track = new Track();
          track.setPositionAndRotationFromXMLElement(eTrack);
        }
        //high jump
        Node eHighJump = XMLReader.getChildElement(eAttractions, "highjump");
        if (eHighJump != null)
        {
          highJump = new HighJump();
          highJump.setPositionAndRotationFromXMLElement(eHighJump);
        }
      }
      
      //objects
      Node eObjects = XMLReader.getChildElement(root, "objects");
      if (eObjects != null)
      {
        List<Element> listObjects = XMLReader.elements(eObjects, "object");
        int i = 0;
        for (i = 0; i < listObjects.size(); i++)
        {
          Node eObject = listObjects.get(i);
          String oid = XMLReader.readAttributeValue(eObject, "id");
          SimpleStadiumObject object = new SimpleStadiumObject(oid);
          object.setPositionAndRotationFromXMLElement(eObject);
          objects.add(object);
        }
        Logging.log(Logging.LEVEL_MESSAGES, this,
          i + " objects loaded.");
      }
      
      //compute the alignment of the fancurve: where
      //the most fences are, otherwise south east
      int fences[] = new int[8];
      int max = 0, maxDir = -1;
      for (int i = 0; i < objects.size(); i++)
      {
        SimpleStadiumObject o = objects.get(i);
        if (o.getID().equals("fence.x"))
        {
          if (o.getPosition().x < 0)
            if (o.getPosition().z < 0)
              fences[BLOCK_ALIGNMENT_SOUTHWEST-1]++;
            else
              fences[BLOCK_ALIGNMENT_NORTHWEST-1]++;
          else
            if (o.getPosition().z < 0)
              fences[BLOCK_ALIGNMENT_SOUTHEAST-1]++;
            else
              fences[BLOCK_ALIGNMENT_NORTHEAST-1]++;
        }
      }
      for (int i = 0; i < 8; i++)
      {
        if (fences[i] > max)
        {
          max = fences[i];
          maxDir = i;
        }
      }
      if (maxDir > -1)
      {
        fanCurveAlignment = maxDir + 1;
      }
        
      entrancePositon = new Vector3f(0, 0, field.getWidth() / 2 + 30);
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Stadium successfully loaded.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Stadium \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
    
  }
  
  
  /**
   * Adds this stadium to the scene.
   * Call this method only once!
   */
  public void addToScene(ISceneManager sceneManager, IVideoDriver videoDriver,
    PhysicsManager physicsManager)
  {
  
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    //terrain
    terrain = new Terrain(terrain.getID(), sceneManager, videoDriver);
    //stands
    for (int i = 0; i < stands.size(); i++)
    {
      stands.get(i).addToScene(sceneManager, videoDriver);
    }
    //adboards
    for (int i = 0; i < adboards.size(); i++)
    {
      adboards.get(i).addToScene(sceneManager, videoDriver, physicsManager);
    }
    //field
    field.addToScene(sceneManager, videoDriver, terrain.getTerrainNode());
    //goals
    for (int i = 0; i < 2; i++)
    {
      goals[i].addToScene(sceneManager, videoDriver, physicsManager);
    }
    //scoreboard
    if (scoreboard != null)
    {
      scoreboard.addToScene(sceneManager, videoDriver, physicsManager);
    }
    //floodlights
    for (int i = 0; i < floodlights.size(); i++)
    {
      floodlights.get(i).addToScene(sceneManager, videoDriver, physicsManager);
    }
    //attractions
    if (hotDogStand != null)
      this.hotDogStand.addToScene(sceneManager, videoDriver, physicsManager);
    if (bettingOffice != null)
      this.bettingOffice.addToScene(sceneManager, videoDriver, physicsManager);
    if (beerBar != null)
      this.beerBar.addToScene(sceneManager, videoDriver, physicsManager);
    if (track != null)
      this.track.addToScene(sceneManager, videoDriver, physicsManager);
    if (highJump != null)
      this.highJump.addToScene(sceneManager, videoDriver, physicsManager);
    //objects
    for (int i = 0; i < objects.size(); i++)
    {
      objects.get(i).addToScene(sceneManager, videoDriver, physicsManager);
    }
    
    //invisible stadium walls
    if (physicsManager != null)
    {
      Rectangle rect;
      float bounce = 0.1f;

      //north
      rect = new Rectangle(new Vector3f[] {
        new Vector3f(-30, 0, 20),
        new Vector3f(-30, 10, 20),
        new Vector3f(30, 10, 20),
        new Vector3f(30, 0, 20)});
      physicsManager.addPhysicsObject(
        new Wall(rect, bounce, PhysicsObject.TYPE_INVISIBLESTADIUMWALL));
      
      //east
      rect = new Rectangle(new Vector3f[] {
        new Vector3f(30, 0, 20),
        new Vector3f(30, 10, 20),
        new Vector3f(30, 10, -20),
        new Vector3f(30, 0, -20)});
      physicsManager.addPhysicsObject(
        new Wall(rect, bounce, PhysicsObject.TYPE_INVISIBLESTADIUMWALL));
      
      //south
      rect = new Rectangle(new Vector3f[] {
        new Vector3f(30, 0, -20),
        new Vector3f(30, 10, -20),
        new Vector3f(-30, 10, -20),
        new Vector3f(-30, 0, -20)});
      physicsManager.addPhysicsObject(
        new Wall(rect, bounce, PhysicsObject.TYPE_INVISIBLESTADIUMWALL));
      
      //west
      rect = new Rectangle(new Vector3f[] {
        new Vector3f(-30, 0, -20),
        new Vector3f(-30, 10, -20),
        new Vector3f(-30, 10, 20),
        new Vector3f(-30, 0, 20)});
      physicsManager.addPhysicsObject(
        new Wall(rect, bounce, PhysicsObject.TYPE_INVISIBLESTADIUMWALL));
    }

  }
  
  
  /**
   * Gets the terrain around this stadium,
   * or <code>null</code>.
   */
  public Terrain getTerrain()
  {
    return terrain;
  }
  
  
  /**
   * Sets the terrain around this stadium
   * by its ID.
   */
  public void setTerrain(String id, ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    if (this.terrain != null)
    {
      terrain.removeFromScene();
    }
    if (id == null || id.length() == 0)
    {
      this.terrain = null;
    }
    else
    {
      this.terrain = new Terrain(id, sceneManager, videoDriver);
    }
  }
  
  
  /**
   * Adds a stand to this stadium.
   */
  public Stand addStand(String id,
    Vector3f position, Vector3f rotation,
    ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    Stand stand = new Stand(id, null);
    if (sceneManager != null)
    {
      stand.addToScene(sceneManager, videoDriver);
    }
    addStand(stand);
    return stand;
  }
  
  
  /**
   * Adds a stand to this stadium and updates
   * the maximum number of spectators.
   */
  public void addStand(Stand stand)
  {
    stands.add(stand);
    stadiumInfo.setMaxSpectators(
      stadiumInfo.getMaxSpectators() + stand.getMaxSpectators());
  }
  
  
  /**
   * Adds an adboard to this stadium.
   */
  public Adboard addAdboard(String id,
    Vector3f position, Vector3f rotation)
  {
    Adboard adboard = new Adboard(id);
    if (sceneManager != null)
    {
      adboard.addToScene(sceneManager, videoDriver, physicsManager);
    }
    //stand.setPosition(position);
    //stand.setRotation(rotation);
    adboards.add(adboard);
    return adboard;
  }
  
  
  /**
   * Sets the scoreboard of this stadium
   * (if there is already one, it is replaced at
   * the same position).
   */
  public Scoreboard setScoreboard(int stage)
  {
    Vector3f pos = new Vector3f();
    Vector3f rot = new Vector3f();
    if (scoreboard != null)
    {
      pos = scoreboard.getPosition();
      rot = scoreboard.getRotation();
      scoreboard.removeFromScene();
    }
    scoreboard = new Scoreboard(stage);
    if (sceneManager != null)
    {
      scoreboard.addToScene(sceneManager, videoDriver, physicsManager);
    }
    scoreboard.setPosition(pos);
    scoreboard.setRotation(rot);
    return scoreboard;
  }
  
  
  /**
   * Adds a floodlight to this stadium.
   */
  public Floodlight addFloodlight(int stage, int careerPosition)
  {
    Floodlight floodlight = new Floodlight(stage, careerPosition);
    if (sceneManager != null)
    {
      floodlight.addToScene(sceneManager, videoDriver, physicsManager);
    }
    floodlights.add(floodlight);
    return floodlight;
  }
  
  
  /**
   * Adds a 3d object to this stadium.
   */
  public SimpleStadiumObject addSimpleObject(String id)
  {
    SimpleStadiumObject object = new SimpleStadiumObject(id);
    if (sceneManager != null)
    {
      object.addToScene(sceneManager, videoDriver, physicsManager);
    }
    objects.add(object);
    return object;
  }
  
  
  /**
   * Removes this stadium from the scene.
   */
  public void removeFromScene()
  {
    if (terrain != null)
      terrain.removeFromScene(); 
    if (field != null)
      field.removeFromScene();
    for (int i = 0; i < 2; i++)
    {
      if (goals[i] != null)
        goals[i].removeFromScene();
    }
    for (int i = 0; i < stands.size(); i++)
      stands.get(i).removeFromScene();
    for (int i = 0; i < adboards.size(); i++)
      adboards.get(i).removeFromScene();
    if (crowd != null)
      crowd.removeFromScene();
    //scoreboard
    if (scoreboard != null)
      scoreboard.removeFromScene();
    //floodlights
    for (int i = 0; i < floodlights.size(); i++)
      floodlights.get(i).removeFromScene();
    //attractions
    if (hotDogStand != null)
      hotDogStand.removeFromScene();
    if (bettingOffice != null)
      bettingOffice.removeFromScene();
    if (beerBar != null)
      beerBar.removeFromScene();
    if (track != null)
      track.removeFromScene();
    if (highJump != null)
      highJump.removeFromScene();
    //objects
    for (int i = 0; i < objects.size(); i++)
      objects.get(i).removeFromScene();
  }
  
  
  /**
   * Gets the Stand that belongs to the
   * given ISceneNode, or <code>null</code>
   * if not found.
   */
  public Stand findStand(ISceneNode node)
  {
    for (int i = 0; i < stands.size(); i++)
      if (node != null)
        if (stands.get(i).getNode().getID() == node.getID())
          return stands.get(i);
    return null;
  }
  
  
  /**
   * Gets the Adboard that belongs to the
   * given ISceneNode, or <code>null</code>
   * if not found.
   */
  public Adboard findAdboard(ISceneNode node)
  {
    if (node != null)
    {
      for (int i = 0; i < adboards.size(); i++)
      {
        if (adboards.get(i).getNode().getID() == node.getID())
          return adboards.get(i);
      }
    }
    return null;
  }
  
  
  /**
   * Gets the FloodlightTower that belongs to the
   * given ISceneNode, or <code>null</code>
   * if not found.
   */
  public Floodlight findFloodlight(ISceneNode node)
  {
    if (node != null)
    {
      for (int i = 0; i < floodlights.size(); i++)
      {
        if (floodlights.get(i).getNode().getID() == node.getID())
          return floodlights.get(i);
      }
    }
    return null;
  }
  
  
  /**
   * Gets the 3d object that belongs to the
   * given ISceneNode, or <code>null</code>
   * if not found.
   */
  public SimpleStadiumObject findSimpleObject(ISceneNode node)
  {
    if (node != null)
    {
      for (int i = 0; i < objects.size(); i++)
      {
        if (objects.get(i).getNode().getID() == node.getID())
          return objects.get(i);
      }
    }
    return null;
  }
  
  
  /**
   * Gets the stand, adboard, floodlight, attraction or
   * other object that belongs to the
   * given ISceneNode, or <code>null</code>
   * if not found.
   */
  public StadiumObject findStadiumObject(ISceneNode node)
  {
    if (node == null)
      return null;
    StadiumObject ret = null;
    //stands
    ret = findStand(node);
    if (ret != null)
      return ret;
    //adboards
    ret = findAdboard(node);
    if (ret != null)
      return ret;
    //scoreboard
    if (scoreboard != null && scoreboard.getNodeID() == node.getID())
      return scoreboard;
    //floodlights
    ret = findFloodlight(node);
    if (ret != null)
      return ret;
    //attractions
    if (hotDogStand != null && hotDogStand.getNodeID() == node.getID())
      return hotDogStand;
    if (bettingOffice != null && bettingOffice.getNodeID() == node.getID())
      return bettingOffice;
    if (beerBar != null && beerBar.getNodeID() == node.getID())
      return beerBar;
    if (track != null && track.getNodeID() == node.getID())
      return track;
    if (highJump != null && highJump.getNodeID() == node.getID())
      return highJump;
    //3d objects
    ret = findSimpleObject(node);
    if (ret != null)
      return ret;
    return null;
  }
  
  
  /**
   * Unselects all stands, adboards, and other objects.
   */
  public void unselectAll()
  {
    for (int i = 0; i < stands.size(); i++)
      stands.get(i).setSelected(false);
    for (int i = 0; i < adboards.size(); i++)
      adboards.get(i).setSelected(false);
    if (scoreboard != null)
      scoreboard.setSelected(false);
    for (int i = 0; i < floodlights.size(); i++)
      floodlights.get(i).setSelected(false);
    if (hotDogStand != null)
      hotDogStand.setSelected(false);
    if (bettingOffice != null)
      bettingOffice.setSelected(false);
    if (beerBar != null)
      beerBar.setSelected(false);
    if (track != null)
      track.setSelected(false);
    if (highJump != null)
      highJump.setSelected(false);
    for (int i = 0; i < objects.size(); i++)
      objects.get(i).setSelected(false);
  }
  
  
  /**
   * Saves this stadium in the file
   * "data/stadiums/" + stadiumInfo.getID() + ".xml" (if tempFolder is false),
   * otherwise in "data/temp/" + stadiumInfo.getID()+ ".xml".
   */
  public void saveToXMLFile(boolean tempFolder)
    throws Exception
  {
    saveToXMLFile(stadiumInfo.getID(), tempFolder);
  }
  
  
  /**
   * Saves this stadium in the file
   * "data/stadiums/" + id + ".xml" (if tempFolder is false),
   * otherwise in "data/temp/" + id + ".xml".
   */
  public void saveToXMLFile(String id, boolean tempFolder)
    throws Exception
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Saving stadium \"" + stadiumInfo.getID() + "\"...");
    
    Document document = XMLWriter.createEmptyDocument();
    //root element
    Node root = XMLWriter.addElement("stadium", document);
    XMLWriter.addAttribute(root, "id", id);
    XMLWriter.addAttribute(root, "name", stadiumInfo.getName());
    
    //terrain
    if (terrain != null)
    {
      Node eTerrain = XMLWriter.addElement("terrain", root);
      XMLWriter.addAttribute(eTerrain, "id", terrain.getID());
    }
    
    //lawn
    Node eLawn = XMLWriter.addElement("lawn", root);
    if (field != null)
      XMLWriter.addAttribute(eLawn, "quality", String.valueOf(field.getLawnQuality()));
    else
      XMLWriter.addAttribute(eLawn, "quality", "12");
    
    //goals
    Node eGoals = XMLWriter.addElement("goals", root);
    String goalstype;
    if (stadiumInfo.getGoals() == StadiumInfo.Goals.GarbageCans)
      goalstype = "garbagecans";
    else if (stadiumInfo.getGoals() == StadiumInfo.Goals.WoodenGoals)
      goalstype = "woodengoals";
    else
      goalstype = "aluminumgoals";
    XMLWriter.addAttribute(eGoals, "id", goalstype);
    if (stadiumInfo.isWoodenGoalsBought())
      XMLWriter.addAttribute(eGoals, "wooden", "true");
    if (stadiumInfo.isAluminumGoalsBought())
      XMLWriter.addAttribute(eGoals, "aluminum", "true");
    
    //career stand blocks
    Node eCareerStandBlocks = XMLWriter.addElement("careerstandblocks", root);
    for (int iStage = 0; iStage < 3; iStage++)
    {
      for (int iAlignment = 0; iAlignment < 8; iAlignment++)
      {
        if (builtCareerStandBlocks[iStage][iAlignment])
        {
          Node eBlock = XMLWriter.addElement("block", eCareerStandBlocks);
          XMLWriter.addAttribute(eBlock, "stage", String.valueOf(iStage + 1));
          XMLWriter.addAttribute(eBlock, "alignment", String.valueOf(iAlignment + 1));
        }
      }
    }
    
    //career adboard blocks
    Node eCareerAdboardBlocks = XMLWriter.addElement("careeradboardblocks", root);
    for (int iAlignment = 0; iAlignment < 8; iAlignment++)
    {
      if (builtCareerAdboardBlocks[iAlignment])
      {
        Node eBlock = XMLWriter.addElement("block", eCareerAdboardBlocks);
        XMLWriter.addAttribute(eBlock, "alignment", String.valueOf(iAlignment + 1));
      }
    }
    
    //stands
    Node eStands = XMLWriter.addElement("stands", root);
    XMLWriter.addAttribute(eStands, "maxspectators", String.valueOf(
      stadiumInfo.getMaxSpectators()));
    for (int i = 0; i < stands.size(); i++)
    {
      Stand stand = stands.get(i);
      Node eStand = XMLWriter.addElement("stand", eStands);
      XMLWriter.addAttribute(eStand, "id", stand.getID());
      XMLWriter.addAttribute(eStand, "modification", stand.getModification());
      stand.addPositionAndRotationToXMLElement(eStand);
      
      int stage = stand.getCareerBlockStage();
      int alignment = stand.getCareerBlockAlignment();
      if (stage > 0 && alignment > 0)
      {
        XMLWriter.addAttribute(eStand, "stage", String.valueOf(stage));
        XMLWriter.addAttribute(eStand, "alignment", String.valueOf(alignment));
      }
    }
    
    //adboards
    Node eAdboards = XMLWriter.addElement("adboards", root);
    for (int i = 0; i < adboards.size(); i++)
    {
      Adboard adboard = adboards.get(i);
      Node eAdboard = XMLWriter.addElement("adboard", eAdboards);
      XMLWriter.addAttribute(eAdboard, "id", adboard.getID());
      adboard.addPositionAndRotationToXMLElement(eAdboard);
      
      int alignment = adboard.getCareerBlockAlignment();
      if (alignment > 0)
      {
        XMLWriter.addAttribute(eAdboard, "alignment", String.valueOf(alignment));
      }
    }
    
    //scoreboard
    if (scoreboard != null)
    {
      Node eScoreboard = XMLWriter.addElement("scoreboard", root);
      XMLWriter.addAttribute(eScoreboard, "stage", String.valueOf(scoreboard.getStage()));
      scoreboard.addPositionAndRotationToXMLElement(eScoreboard);
    }
    
    //floodlights
    Node eFloodlights = XMLWriter.addElement("floodlights", root);
    for (int i = 0; i < floodlights.size(); i++)
    {
      Floodlight floodlight = floodlights.get(i);
      Node eFloodlight = XMLWriter.addElement("floodlight", eFloodlights);
      XMLWriter.addAttribute(eFloodlight, "stage", String.valueOf(floodlight.getStage()));
      if (floodlight.getCareerPosition() != 0)
        XMLWriter.addAttribute(eFloodlight, "careerposition",
          String.valueOf(floodlight.getCareerPosition()));
      floodlight.addPositionAndRotationToXMLElement(eFloodlight);
    }
    
    //attractions
    Node eAttractions = XMLWriter.addElement("attractions", root);
    if (hotDogStand != null)
    {
      Node eHotDogStand = XMLWriter.addElement("hotdogstand", eAttractions);
      hotDogStand.addPositionAndRotationToXMLElement(eHotDogStand);
    }
    if (bettingOffice != null)
    {
      Node eBettingOffice = XMLWriter.addElement("bettingoffice", eAttractions);
      bettingOffice.addPositionAndRotationToXMLElement(eBettingOffice);
    }
    if (beerBar != null)
    {
      Node eBeerBar = XMLWriter.addElement("beerbar", eAttractions);
      beerBar.addPositionAndRotationToXMLElement(eBeerBar);
    }
    if (track != null)
    {
      Node eTrack = XMLWriter.addElement("track", eAttractions);
      track.addPositionAndRotationToXMLElement(eTrack);
    }
    if (highJump != null)
    {
      Node eHighJump = XMLWriter.addElement("highjump", eAttractions);
      highJump.addPositionAndRotationToXMLElement(eHighJump);
    }
    
    //objects
    Node eObjects = XMLWriter.addElement("objects", root);
    for (int i = 0; i < objects.size(); i++)
    {
      SimpleStadiumObject object = objects.get(i);
      Node eObject = XMLWriter.addElement("object", eObjects);
      XMLWriter.addAttribute(eObject, "id", object.getID());
      object.addPositionAndRotationToXMLElement(eObject);
    }

    String out;
    if (!tempFolder)
      out = "data/stadiums/" + id + ".xml";
    else
      out = "data/temp/" + id + ".xml";
    XMLWriter.writeFile(document, out);
    
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Stadium \"" + stadiumInfo.getID() + "\" saved.");

  }


  
  public StadiumInfo getStadiumInfo()
  {
    return stadiumInfo;
  }
  
  
  /**
   * Remove the given stand and updates the
   * maximum number of spectators.
   */
  public void removeStand(Stand stand)
  {
    stand.removeFromScene();
    stands.remove(stand);
    stadiumInfo.setMaxSpectators(
      stadiumInfo.getMaxSpectators() - stand.getMaxSpectators());
  }
  
  
  /**
   * Remove the given adboard.
   */
  public void removeAdboard(Adboard adboard)
  {
    adboard.removeFromScene();
    adboards.remove(adboard);
  }
  
  
  /**
   * Remove the scoreboard.
   */
  public void removeScoreboard()
  {
    if (scoreboard != null)
      scoreboard.removeFromScene();
    scoreboard = null;
  }
  
  
  /**
   * Remove the given floodlight.
   */
  public void removeFloodlight(Floodlight floodlight)
  {
    floodlight.removeFromScene();
    floodlights.remove(floodlight);
  }
  
  
  /**
   * Remove the given attraction.
   */
  public void removeAttraction(StadiumObject attraction)
  {
    if (attraction == null)
      return;
    else if (attraction == hotDogStand)
    {
      hotDogStand.removeFromScene();
      hotDogStand = null;
    }
    else if (attraction == bettingOffice)
    {
      bettingOffice.removeFromScene();
      bettingOffice = null;
    }
    else if (attraction == beerBar)
    {
      beerBar.removeFromScene();
      beerBar = null;
    }
    else if (attraction == track)
    {
      track.removeFromScene();
      track = null;
    }
    else if (attraction == highJump)
    {
      highJump.removeFromScene();
      highJump = null;
    }
  }
  
  
  /**
   * Remove the given 3d object.
   */
  public void removeSimpleObject(SimpleStadiumObject object)
  {
    object.removeFromScene();
    objects.remove(object);
  }
  
  
  /**
   * Gets the number of stands.
   */
  public int getStandsCount()
  {
    return stands.size();
  }

  
  /**
   * @return Returns the field.
   */
  public Field getField()
  {
    return field;
  }
  
  
  /**
   * Gets the entrance position of this stadium
   * (the position the players come from at the beginning
   * and run to at halftime).
   */
  public Vector3f getEntrancePosition()
  {
    return entrancePositon;
  }
  
  
  /**
   * Gets the field border position of this stadium
   * (the position between the center of the field and
   * the entrance position).
   */
  public Vector3f getFieldBorderPosition()
  {
    return new Vector3f(entrancePositon.x, 0,
      (entrancePositon.z > 0 ? 1 : -1) * 9);
  }
  
  
  /**
   * Replaces the adboard texture of this stadium
   * with the texture with the given filename
   * (usually the adboards texture of the home team).
   */
  public void setAdboardsTexture(String filename, Color col1)
  {
    ITexture newTex = TextureFactory.loadTexture(filename, true);
    if (newTex != null)
    {
      for (int i = 0; i < adboards.size(); i++)
      {
        adboards.get(i).setTexture(newTex);
      }
      if (crowd != null)
      {
        ITexture texFlag = createFlagTexture(col1);
        crowd.setAdboardsTexture(texFlag, newTex);
      }
      for (int i = 0; i < objects.size(); i++)
      {
        SimpleStadiumObject object = objects.get(i);
        if (object.isTeamAdsTexture())
          object.setTexture(newTex);
      }
    }
  }
  
  
  /**
   * Adds crowd to this stadium. Call this method only one
   * time for each instance of this class, and not before
   * calling addToScene().
   * @param attendance          0 = empty stadium, ..., 1 = full stadium
   * @param team                0 = supports home team, 1 = supports visitor team
   * @param homeTeamShirtColor  Color of the shirt of the home team,
   *                            or <code>null</code> if unknown
   * @param homeTeamPantsColor  Color of the pants of the home team,
   *                            or <code>null</code> if unknown
   */
  public Crowd createCrowd(float attendance, int team,
    Color homeTeamShirtColor, Color homeTeamPantsColor, boolean celebration)
  {
    Logging.log(Logging.LEVEL_MESSAGES, "Create crowd with " +
      (int) (attendance * 100) + "% attendance");
    
    //create crowd colors
    //fan colors
    CrowdColors crowdColors = new CrowdColors();
    if (homeTeamShirtColor == null)
      homeTeamShirtColor = new Color(230, 230, 230);
    if (homeTeamPantsColor == null)
      homeTeamPantsColor = new Color(30, 30, 30);
    //other (random) colors
    Color otherColorShirt[] = new Color[2];
    Color otherColorPants[] = new Color[2];
    try
    {
      ConfigFile colorsConf = new ConfigFile("data/config/crowd.xml");
      for (int i = 0; i < 2; i++)
      {
        int index = MathTools.randomInt(10) + 1;
        otherColorShirt[i] = colorsConf.getValueColor("color" + index + "shirt");
        otherColorPants[i] = colorsConf.getValueColor("color" + index + "pants");
      }
    }
    catch (Exception ex)
    {
      GameEngine.fatalError(this, new Exception(
        "Could not load crowd colors from crowd.xml!"));
    }
    
    crowdColors.setColor(0, CrowdColors.COLOR_SHIRT, homeTeamShirtColor);
    crowdColors.setColor(0, CrowdColors.COLOR_PANTS, homeTeamPantsColor);
    crowdColors.setColor(1, CrowdColors.COLOR_SHIRT, otherColorShirt[0]);
    crowdColors.setColor(1, CrowdColors.COLOR_PANTS, otherColorPants[0]);
    crowdColors.setColor(2, CrowdColors.COLOR_SHIRT, homeTeamPantsColor);
    crowdColors.setColor(2, CrowdColors.COLOR_PANTS, homeTeamShirtColor);
    crowdColors.setColor(3, CrowdColors.COLOR_SHIRT, otherColorShirt[1]);
    crowdColors.setColor(3, CrowdColors.COLOR_PANTS, otherColorPants[1]);
    
    //create crowd texture
    String dest = "data/temp/crowdcolor.png";
    ITexture crowdTexture = null;
    if (crowdColors.createTexture(dest))
    {
      ITexture texture = videoDriver.getTexture(dest);
      if (texture != null)
        videoDriver.removeTexture(texture);
      crowdTexture = GameEngine.getVideoDriver().getTexture(dest);
      new java.io.File(dest).delete();
    }
    
    //create crowd
    Crowd crowd = new Crowd(attendance, team, sceneManager, videoDriver);
    for (int i = 0; i < stands.size(); i++)
    {
      crowd.add(new StandCrowd(stands.get(i), attendance,
        stands.get(i).isFanblock(this), //fanblock
          sceneManager, videoDriver, crowdTexture, celebration));
    }
    crowd.createAmbienceSound();
    
    this.crowd = crowd;
    return crowd;
  }


  /**
   * Gets the crowd (audience).
   */
  public Crowd getCrowd()
  {
    return crowd;
  }
  
  
  /**
   * Gets the cost of the given block.
   */
  public int getCareerBlockCost(int stage, int alignment)
  {
    alignment--; //0 based here
    if (stage == 0)
    {
      //adboards
      if (alignment % 4 == 0) //horizontal
        return 1600;
      else if (alignment % 2 == 0) //vertical
        return 800;
      else //curve
        return 400;
    }
    else if (stage == 1)
    {
      //stands, stage 1
      if (alignment % 4 == 0) //horizontal
        return 2000;
      else if (alignment % 2 == 0) //vertical
        return 1500;
      else
        return 600;
    }
    else if (stage == 2)
    {
      //stands, stage 2
      if (alignment % 4 == 0) //horizontal
        return 4000;
      else if (alignment % 2 == 0) //vertical
        return 3000;
      else
        return 1800;
    }
    else if (stage == 3)
    {
      //stands, stage 3
      if (alignment % 4 == 0) //horizontal
        return 8000;
      else if (alignment % 2 == 0) //vertical
        return 6000;
      else
        return 3000;
    }
    return 0;
  }


  /**
   * Returns <code>true</code>, if the given adboard
   * block is already built.
   */
  public boolean isCareerAdboardBlockBuilt(int alignment)
  {
    return builtCareerAdboardBlocks[alignment - 1];
  }


  /**
   * Returns <code>true</code>, if the given stand
   * block is already built.
   */
  public boolean isCareerStandBlockBuilt(int stage, int alignment)
  {
    return builtCareerStandBlocks[stage - 1][alignment - 1];
  }
  
  
  /**
   * Build the given aboard block.
   */
  public void buildCareerAdboardBlock(int alignment)
  {
    if (isCareerAdboardBlockBuilt(alignment))
      return;
    builtCareerAdboardBlocks[alignment - 1] = true;
    if (alignment == BLOCK_ALIGNMENT_NORTH)
    {
      Adboard adboard1 = new Adboard("adboard1");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(-8, 0, 15));
      adboard1.setRotation(new Vector3f(0, 0, 0));
      adboards.add(adboard1);
      Adboard adboard2 = new Adboard("adboard3");
      adboard2.setCareerBlock(alignment);
      adboard2.setPosition(new Vector3f(-16, 0, 15));
      adboard2.setRotation(new Vector3f(0, 0, 0));
      adboards.add(adboard2);
      Adboard adboard3 = new Adboard("adboard3");
      adboard3.setCareerBlock(alignment);
      adboard3.setPosition(new Vector3f(16, 0, 15));
      adboard3.setRotation(new Vector3f(0, 0, 0));
      adboards.add(adboard3);
      Adboard adboard4 = new Adboard("adboard4");
      adboard4.setCareerBlock(alignment);
      adboard4.setPosition(new Vector3f(8, 0, 15));
      adboard4.setRotation(new Vector3f(0, 0, 0));
      adboards.add(adboard4);
    }
    else if (alignment == BLOCK_ALIGNMENT_NORTHEAST)
    {
      Adboard adboard1 = new Adboard("adboard1");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(23, 0, 13));
      adboard1.setRotation(new Vector3f(0, 45, 0));
      adboards.add(adboard1);
    }
    else if (alignment == BLOCK_ALIGNMENT_EAST)
    {
      Adboard adboard1 = new Adboard("adboard2");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(25, 0, 6));
      adboard1.setRotation(new Vector3f(0, 90, 0));
      adboards.add(adboard1);
      Adboard adboard2 = new Adboard("adboard3");
      adboard2.setCareerBlock(alignment);
      adboard2.setPosition(new Vector3f(25, 0, -6));
      adboard2.setRotation(new Vector3f(0, 90, 0));
      adboards.add(adboard2);
    }
    else if (alignment == BLOCK_ALIGNMENT_SOUTHEAST)
    {
      Adboard adboard1 = new Adboard("adboard2");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(23, 0, -13));
      adboard1.setRotation(new Vector3f(0, 135, 0));
      adboards.add(adboard1);
    }
    else if (alignment == BLOCK_ALIGNMENT_SOUTH)
    {
      Adboard adboard1 = new Adboard("adboard1");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(-16, 0, -15));
      adboard1.setRotation(new Vector3f(0, 180, 0));
      adboards.add(adboard1);
      Adboard adboard2 = new Adboard("adboard2");
      adboard2.setCareerBlock(alignment);
      adboard2.setPosition(new Vector3f(-8, 0, -15));
      adboard2.setRotation(new Vector3f(0, 180, 0));
      adboards.add(adboard2);
      Adboard adboard3 = new Adboard("adboard3");
      adboard3.setCareerBlock(alignment);
      adboard3.setPosition(new Vector3f(0, 0, -15));
      adboard3.setRotation(new Vector3f(0, 180, 0));
      adboards.add(adboard3);
      Adboard adboard4 = new Adboard("adboard4");
      adboard4.setCareerBlock(alignment);
      adboard4.setPosition(new Vector3f(8, 0, -15));
      adboard4.setRotation(new Vector3f(0, 180, 0));
      adboards.add(adboard4);
      /* highjump
      Adboard adboard5 = new Adboard("adboard1");
      adboard5.setCareerBlock(alignment);
      adboard5.setPosition(new Vector3f(16, 0, -15));
      adboard5.setRotation(new Vector3f(0, 180, 0));
      adboards.add(adboard5); */
    }
    else if (alignment == BLOCK_ALIGNMENT_SOUTHWEST)
    {
      Adboard adboard1 = new Adboard("adboard3");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(-23, 0, -13));
      adboard1.setRotation(new Vector3f(0, -135, 0));
      adboards.add(adboard1);
    }
    else if (alignment == BLOCK_ALIGNMENT_WEST)
    {
      Adboard adboard1 = new Adboard("adboard2");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(-25, 0, -6));
      adboard1.setRotation(new Vector3f(0, -90, 0));
      adboards.add(adboard1);
      Adboard adboard2 = new Adboard("adboard4");
      adboard2.setCareerBlock(alignment);
      adboard2.setPosition(new Vector3f(-25, 0, 6));
      adboard2.setRotation(new Vector3f(0, -90, 0));
      adboards.add(adboard2);
    }
    else if (alignment == BLOCK_ALIGNMENT_NORTHWEST)
    {
      Adboard adboard1 = new Adboard("adboard2");
      adboard1.setCareerBlock(alignment);
      adboard1.setPosition(new Vector3f(-23, 0, 13));
      adboard1.setRotation(new Vector3f(0, -45, 0));
      adboards.add(adboard1);
    }
    
  }
  
  
  /**
   * Build the given stand block.
   */
  public void buildCareerStandBlock(int stage, int alignment)
  {
    if (isCareerStandBlockBuilt(stage, alignment))
      return;
    builtCareerStandBlocks[stage - 1][alignment - 1] = true;
    if (stage == BLOCK_STAGE_1)
    {
      //stage 1
      if (alignment == BLOCK_ALIGNMENT_NORTH)
      {
        Stand stand1 = new Stand("stage1_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, 25));
        stand1.setRotation(new Vector3f(0, 0, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage1_normal", "stage1_normal_entrance1");
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, 25));
        stand2.setRotation(new Vector3f(0, 0, 0));
        addStand(stand2);
        Stand stand3 = new Stand("stage1_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, 25));
        stand3.setRotation(new Vector3f(0, 0, 0));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHEAST)
      {
        Stand stand1 = new Stand("stage1_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(35, 0, 25));
        stand1.setRotation(new Vector3f(0, 0, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_EAST)
      {
        Stand stand1 = new Stand("stage1_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(35, 0, 10));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage1_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(35, 0, -10));
        stand2.setRotation(new Vector3f(0, 90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHEAST)
      {
        Stand stand1 = new Stand("stage1_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(35, 0, -25));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        //fan curve: add fence and banners
        SimpleStadiumObject o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(23, 0, -19));
        o.setRotation(new Vector3f(0, 180, 0));
        o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(29, 0, -13));
        o.setRotation(new Vector3f(0, 90, 0));
        o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(27, 0, -17));
        o.setRotation(new Vector3f(0, 135, 0));
        o = addSimpleObject("banner.x");
        o.setPosition(new Vector3f(23, 2, -19));
        o.setRotation(new Vector3f(0, 180, 0));
        o = addSimpleObject("banner.x");
        o.setPosition(new Vector3f(29, 0, -13));
        o.setRotation(new Vector3f(0, 90, 0));
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTH)
      {
        Stand stand1 = new Stand("stage1_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, -25));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage1_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, -25));
        stand2.setRotation(new Vector3f(0, 180, 0));
        addStand(stand2);
        Stand stand3 = new Stand("stage1_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, -25));
        stand3.setRotation(new Vector3f(0, 180, 0));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHWEST)
      {
        Stand stand1 = new Stand("stage1_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-35, 0, -25));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_WEST)
      {
        Stand stand1 = new Stand("stage1_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-35, 0, -10));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage1_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(-35, 0, 10));
        stand2.setRotation(new Vector3f(0, -90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHWEST)
      {
        Stand stand1 = new Stand("stage1_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-35, 0, 25));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
      }
    }
    else if (stage == BLOCK_STAGE_2)
    {
      //stage 2
      if (alignment == BLOCK_ALIGNMENT_NORTH)
      {
        Stand stand1 = new Stand("stage2_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, 35));
        addStand(stand1);
        Stand stand2 = new Stand("stage2_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, 35));
        addStand(stand2);
        Stand stand3 = new Stand("stage2_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, 35));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHEAST)
      {
        Stand stand1 = new Stand("stage2_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(40, 0, 30));
        stand1.setRotation(new Vector3f(0, 0, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_EAST)
      {
        Stand stand1 = new Stand("stage2_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(45, 0, 10));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage2_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(45, 0, -10));
        stand2.setRotation(new Vector3f(0, 90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHEAST)
      {
        Stand stand1 = new Stand("stage2_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(40, 0, -30));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        //fan curve: add fence and banners
        SimpleStadiumObject o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(18, 0, -19));
        o.setRotation(new Vector3f(0, 180, 0));
        o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(29, 0, -8));
        o.setRotation(new Vector3f(0, 90, 0));
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTH)
      {
        Stand stand1 = new Stand("stage2_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, -35));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage2_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, -35));
        stand2.setRotation(new Vector3f(0, 180, 0));
        addStand(stand2);
        Stand stand3 = new Stand("stage2_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, -35));
        stand3.setRotation(new Vector3f(0, 180, 0));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHWEST)
      {
        Stand stand1 = new Stand("stage2_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-40, 0, -30));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_WEST)
      {
        Stand stand1 = new Stand("stage2_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-45, 0, -10));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage2_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(-45, 0, 10));
        stand2.setRotation(new Vector3f(0, -90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHWEST)
      {
        Stand stand1 = new Stand("stage2_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-40, 0, 30));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
      }
    }
    else if (stage == BLOCK_STAGE_3)
    {
      //stage 3
      if (alignment == BLOCK_ALIGNMENT_NORTH)
      {
        Stand stand1 = new Stand("stage3_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, 45));
        addStand(stand1);
        Stand stand2 = new Stand("stage3_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, 45));
        addStand(stand2);
        Stand stand3 = new Stand("stage3_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, 45));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHEAST)
      {
        Stand stand1 = new Stand("stage3_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(45, 0, 35));
        stand1.setRotation(new Vector3f(0, 0, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_EAST)
      {
        Stand stand1 = new Stand("stage3_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(55, 0, 10));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage3_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(55, 0, -10));
        stand2.setRotation(new Vector3f(0, 90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHEAST)
      {
        Stand stand1 = new Stand("stage3_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(45, 0, -35));
        stand1.setRotation(new Vector3f(0, 90, 0));
        addStand(stand1);
        //fan curve: add fence and banners
        SimpleStadiumObject o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(13, 0, -19));
        o.setRotation(new Vector3f(0, 180, 0));
        o = addSimpleObject("fence.x");
        o.setPosition(new Vector3f(29, 0, -3));
        o.setRotation(new Vector3f(0, 90, 0));
        o = addSimpleObject("banner.x");
        o.setPosition(new Vector3f(13, 2, -19));
        o.setRotation(new Vector3f(0, 180, 0));
        o = addSimpleObject("banner.x");
        o.setPosition(new Vector3f(29, 2, -3));
        o.setRotation(new Vector3f(0, 90, 0));
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTH)
      {
        Stand stand1 = new Stand("stage3_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-20, 0, -45));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage3_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(0, 0, -45));
        stand2.setRotation(new Vector3f(0, 180, 0));
        addStand(stand2);
        Stand stand3 = new Stand("stage3_normal", null);
        stand3.setCareerBlock(stage, alignment);
        stand3.setPosition(new Vector3f(20, 0, -45));
        stand3.setRotation(new Vector3f(0, 180, 0));
        addStand(stand3);
      }
      else if (alignment == BLOCK_ALIGNMENT_SOUTHWEST)
      {
        Stand stand1 = new Stand("stage3_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-45, 0, -35));
        stand1.setRotation(new Vector3f(0, 180, 0));
        addStand(stand1);
      }
      else if (alignment == BLOCK_ALIGNMENT_WEST)
      {
        Stand stand1 = new Stand("stage3_normal", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-55, 0, -10));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
        Stand stand2 = new Stand("stage3_normal", null);
        stand2.setCareerBlock(stage, alignment);
        stand2.setPosition(new Vector3f(-55, 0, 10));
        stand2.setRotation(new Vector3f(0, -90, 0));
        addStand(stand2);
      }
      else if (alignment == BLOCK_ALIGNMENT_NORTHWEST)
      {
        Stand stand1 = new Stand("stage3_curve", null);
        stand1.setCareerBlock(stage, alignment);
        stand1.setPosition(new Vector3f(-45, 0, 35));
        stand1.setRotation(new Vector3f(0, -90, 0));
        addStand(stand1);
      }
    }
  }
  
  
  /**
   * Gets the number of adboards in a career stadium.
   */
  public int getCareerAdboardsCount()
  {
    int ret = 0;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_NORTH - 1]) ret += 4;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_NORTHEAST - 1]) ret += 1;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_EAST - 1]) ret += 2;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_SOUTHEAST - 1]) ret += 1;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_SOUTH - 1]) ret += 5;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_SOUTHWEST - 1]) ret += 1;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_WEST - 1]) ret += 2;
    if (builtCareerAdboardBlocks[BLOCK_ALIGNMENT_NORTHWEST - 1]) ret += 1;
    return ret;
  }
  
   
  /**
   * Gets the relative number of adboards in a career stadium
   * (0 = no adboards, 1 = maximum number)
   */
  public float getCareerAdboardsRelativeNumber()
  {
    return (getCareerAdboardsCount() / 17f);
  }


  /**
   * Gets the hot dog stand, or <code>null</code> if not built.
   */
  public HotDogStand getHotDogStand()
  {
    return hotDogStand;
  }


  /**
   * Sets the hot dog stand.
   */
  public void setHotDogStand(HotDogStand hotDogStand)
  {
    if (this.hotDogStand != null)
      this.hotDogStand.removeFromScene();
    this.hotDogStand = hotDogStand;
    if (this.hotDogStand != null && sceneManager != null)
      this.hotDogStand.addToScene(sceneManager, videoDriver, physicsManager);
  }
  
  
  /**
   * Gets the betting office, or <code>null</code> if not built.
   */
  public BettingOffice getBettingOffice()
  {
    return bettingOffice;
  }


  /**
   * Sets the betting office.
   */
  public void setBettingOffice(BettingOffice bettingOffice)
  {
    if (this.bettingOffice != null)
      this.bettingOffice.removeFromScene();
    this.bettingOffice = bettingOffice;
    if (this.bettingOffice != null && sceneManager != null)
      this.bettingOffice.addToScene(sceneManager, videoDriver, physicsManager);
  }
  
  
  /**
   * Gets the beer bar, or <code>null</code> if not built.
   */
  public BeerBar getBeerBar()
  {
    return beerBar;
  }


  /**
   * Sets the beer bar.
   */
  public void setBeerBar(BeerBar beerBar)
  {
    if (this.beerBar != null)
      this.beerBar.removeFromScene();
    this.beerBar = beerBar;
    if (this.beerBar != null && sceneManager != null)
      this.beerBar.addToScene(sceneManager, videoDriver, physicsManager);
  }
  
  
  /**
   * Gets the track, or <code>null</code> if not built.
   */
  public Track getTrack()
  {
    return track;
  }


  /**
   * Sets the track.
   */
  public void setTrack(Track track)
  {
    if (this.track != null)
      this.track.removeFromScene();
    this.track = track;
    if (this.track != null && sceneManager != null)
      this.track.addToScene(sceneManager, videoDriver, physicsManager);
  }
  
  
  /**
   * Gets the high jump, or <code>null</code> if not built.
   */
  public HighJump getHighJump()
  {
    return highJump;
  }


  /**
   * Sets the high jump.
   */
  public void setHighJump(HighJump highJump)
  {
    if (this.highJump != null)
      this.highJump.removeFromScene();
    this.highJump = highJump;
    if (this.highJump != null && sceneManager != null)
      this.highJump.addToScene(sceneManager, videoDriver, physicsManager);
  }
  
  
  /**
   * Activates the floodlights.
   */
  public void setFloodlightsOn()
  {
    for (int i = 0; i < floodlights.size(); i++)
    {
      floodlights.get(i).setLightOn(sceneManager, videoDriver, null);
    }
  }
  
  
  /**
   * Gets the light value of the stadium
   * (0 = no light, ..., 1 = perfectly lit).
   */
  public float getLightValue()
  {
    int max = 12;
    int val = 0;
    for (int i = 0; i < floodlights.size(); i++)
    {
      val += floodlights.get(i).getStage();
    }
    return MathTools.clamp(1f * val / max, 0, 1);
  }
  
  
  /**
   * Gets the scoreboard of this stadium, or null.
   */
  public Scoreboard getScoreboard()
  {
    return scoreboard;
  }
  
  
  /**
   * Gets the quality of the lighting as
   * a local String.
   */
  public String getLightingQuality()
  {
    int points = 0;
    for (int i = 0; i < floodlights.size(); i++)
    {
      int stage = floodlights.get(i).getStage();
      points += stage;
    }
    if (points == 0)
      return Language.get("lighting_moonlit");
    else if (points <= 3)
      return Language.get("lighting_dusky");
    else if (points <= 7)
      return Language.get("lighting_dimlylit");
    else if (points <= 11)
      return Language.get("lighting_bright");
    else
      return Language.get("lighting_perfect");
  }
  
  
  /**
   * Gets the local name of the scoreboard.
   */
  public String getScoreBoardName()
  {
    if (scoreboard == null)
      return Language.get("scoreboard_none");
    else
      return Language.get("scoreboard" + scoreboard.getStage());
  }
  
  
  /**
   * Gets the quality of the attractions as a local string, e.g.
   * "Gähnende Langeweile", "Erlebnispark".
   */
  public String getAttractionsQualityLocalString()
  {
    int points = 0;
    points += (hotDogStand != null ? 1 : 0);
    points += (bettingOffice != null ? 1 : 0);
    points += (beerBar != null ? 1 : 0);
    points += (track != null ? 1 : 0);
    points += (highJump != null ? 1 : 0);
    return Language.get("attractions" + points);
  }
  
  
  /**
   * Gets the light on
   * the given position (Floodlight.POSITION_-constant)
   * in a career stadium, or null if not built.
   */
  public Floodlight getCareerLight(int position)
  {
    for (int i = 0; i < floodlights.size(); i++)
    {
      if (floodlights.get(i).getCareerPosition() == position)
        return floodlights.get(i);
    }
    return null;
  }
  
  
  /**
   * Gets the local description of the light on
   * the given position (Floodlight.POSITION_-constant)
   * in a career stadium.
   */
  public String getCareerLightLocalString(int position)
  {
    Floodlight light = getCareerLight(position);
    if (light != null)
      return light.getLocalName();
    else
      return Language.get("floodlight0");
  }


  /**
   * Gets the alignment of the fan curve.
   */
  public int getFanCurveAlignment()
  {
    return fanCurveAlignment;
  }
  
  
  /**
   * Gets the index of a random fan curve stand,
   * or null if no fan curve stand was found.
   */
  public int getRandomFanCurveStandIndex()
  {
    if (stands.size() == 0)
      return 0;
    int start = MathTools.randomInt(stands.size());
    for (int i = 0; i < stands.size(); i++)
    {
      if (stands.get((i + start) % stands.size()).isFanblock(this))
        return i;
    }
    return 0;
  }
  
  
  /**
   * Create flag texture with the given color
   * and returns it.
   */
  private ITexture createFlagTexture(Color col1)
  {
    int size = 16;
    //little bit darker
    Color colNew = new Color(
      MathTools.clamp(col1.getRed(), 0, 255),
      MathTools.clamp(col1.getGreen(), 0, 255),
      MathTools.clamp(col1.getBlue(), 0, 255));
    //create BufferedImage
    BufferedImage img = new BufferedImage(size, size,
      BufferedImage.TYPE_INT_ARGB);
    //draw color
    for (int y = 0; y < size; y++)
    {
      for (int x = 0; x < size; x++)
      {
        Color c = new Color(
          (int) MathTools.clamp(colNew.getRed() * (1 + x * 0.2f), 0, 255),
          (int) MathTools.clamp(colNew.getGreen() * (1 + x * 0.2f), 0, 255),
          (int) MathTools.clamp(colNew.getBlue() * (1 + x * 0.2f), 0, 255));
        img.setRGB(x, y, c.getRGB());
      }
    }
    //save new texture
    java.io.File file = new java.io.File("data/temp/flag.png");
    try
    {
      javax.imageio.ImageIO.write(img, "png", file);
      ITexture texture = videoDriver.getTexture("data/temp/flag.png");
      if (texture != null)
        videoDriver.removeTexture(texture);
      return videoDriver.getTexture("data/temp/flag.png");
    }
    catch (Exception ex)
    {
      return null;
    }
  }
  
  
  /**
   * Gets the crowd factor (attendance) for the lighting
   * of this stadium (if no light is needed, 100% is returned).
   */
  public float getLightingCrowdAttendance(boolean lightingNeeded)
  {
    if (!lightingNeeded)
      return 1;
    else
      return getLightValue();
  }
  
  
  /**
   * Gets the crowd factor (attendance) for the attractions.
   */
  public float getAttractionsCrowdAttendance()
  {
    float ret = 0.5f;
    ret += (hotDogStand != null ? 0.1f : 0);
    ret += (bettingOffice != null ? 0.1f : 0);
    ret += (beerBar != null ? 0.1f : 0);
    ret += (track != null ? 0.1f : 0);
    ret += (highJump != null ? 0.1f : 0);
    return ret;
  }
  
  
  /**
   * Gets the crowd factor (attendance) for the scoreboard.
   */
  public float getScoreboardCrowdAttendance()
  {
    if (scoreboard == null)
      return 0.6f;
    else
      return scoreboard.getCrowdAttendance();
  }
  
}
