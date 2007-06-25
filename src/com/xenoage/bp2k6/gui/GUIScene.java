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
package com.xenoage.bp2k6.gui;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.w3c.dom.*;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.gui.scene.Fireworks;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.camera.*;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.enums.AnimationStage;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.match.stadium.StadiumObject;
import com.xenoage.bp2k6.match.stadium.crowd.BengalLight;
import com.xenoage.bp2k6.match.stadium.objects.SimpleStadiumObject;
import com.xenoage.bp2k6.match.weather.Weather;
import com.xenoage.bp2k6.util.*;

import net.sf.jirr.*;


/**
 * A gui scene is a 3d scene that is used in the
 * gui as a dynamic background. Parts of this scene
 * may be a stadium, 3d objects or a camera with
 * a flight sequence.
 * It is an own class and not part of a certain screen,
 * so that multiple screens can use it one after another.
 */
public class GUIScene
{
  private String id;
  
  private ICameraSceneNode nativeCamera;
  
  private String stadiumID;
  private Stadium stadium;
  private Camera camera;
  private ArrayList<Flight> flights = new ArrayList<Flight>();
  private Weather weather;
  private boolean weatherSound = false;
  
  private ISceneManager sceneManager;
  private IVideoDriver videoDriver;
  
  private StadiumObject cup = null;
  private Team team = null;
  private int playerLastFrame[] = new int[5];
  
  private Fireworks fireworks = null;
  
  
  /**
   * Constructor that creates a gui scene from a xml file.
   */
  public GUIScene(String id, ISceneManager sceneManager, IVideoDriver videoDriver)
  {
    this.sceneManager = sceneManager;
    this.videoDriver = videoDriver;
    
    //read gui scene information from xml file
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading GUI scene \"" + id + "\"...");
    try
    {
      Document doc = XMLReader.readFile("data/scenes/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      //weather
      Node eWeather = XMLReader.getChildElement(root, "weather");
      if (eWeather != null)
      {
        String weatherID = XMLReader.readAttributeValue(eWeather, "id");
        GameEngine.getMatchManager().setWeather(weatherID, true);
        weather = GameEngine.getMatchManager().getWeather();
        if (XMLReader.readAttributeValueBoolean(eWeather, "sound"))
        {
          weatherSound = true;
          weather.createAmbienceSound(0);
        }
      }
      //stadium
      Node eStadium = XMLReader.getChildElement(root, "stadium");
      if (eStadium != null)
      {
        stadiumID = XMLReader.readAttributeValue(eStadium, "id");
        if (stadiumID.equals("%random%"))
          stadiumID = GameEngine.getMatchManager().getRandomStadiumID();
        else
          stadiumID = Variables.replaceVariables(stadiumID);
        if (stadiumID.length() > 0)
        {
          stadium = new Stadium(stadiumID);
          stadium.addToScene(sceneManager, videoDriver,
            GameEngine.getMatchManager().getPhysicsManager());
          //stadium lighting
          if (weather != null)
          {
            String light = XMLReader.readAttributeValue(eStadium, "light");
            boolean lightsOn = Boolean.parseBoolean(light);
            //also lightend, if value is "weatherdependent" and the weather
            //requests light
            if (light.equals("weatherdependent") && weather.isStadiumLighting())
              lightsOn = true;
            if (lightsOn)
              stadium.setFloodlightsOn();
          }
          if (XMLReader.readAttributeValueBoolean(eStadium, "usecareerads"))
          {
            Career career = GameEngine.getCareer();
            if (career != null)
            {
              TeamInfo teamInfo =
                GameEngine.getMatchManager().getTeamInfo(career.getUserTeamID());
              String teamAds =
                "data/teams/" + teamInfo.getID() + "-ads.png";
              if (new File(teamAds).exists())
                stadium.setAdboardsTexture(teamAds, Color.RED);
            }
          }
        }
      }
      //program
      Node eProgram = XMLReader.getChildElement(root, "program");
      if (eProgram != null)
      {
        String program = XMLReader.readAttributeValue(eProgram, "id");
        if (program.equals("champion3"))
          createSceneChampion3();
        else if (program.equals("champion2"))
          createSceneChampion2();
        else if (program.equals("champion1"))
          createSceneChampion1();
        else if (program.equals("championallstars"))
          createSceneChampionAllstars();
        else if (program.equals("championworldcup"))
          createSceneChampionWorldCup();
      }
      //create camera
      nativeCamera = sceneManager.addCameraSceneNode();
      //System.out.println(nativeCamera.getFOV());
      nativeCamera.setFarValue(600f * GameEngine.UNITS_GAME_TO_IRRLICHT);
      nativeCamera.setNearValue(1f * GameEngine.UNITS_GAME_TO_IRRLICHT);
      //OBSOLETE: nativeCamera.setFOV(0.015f);
      sceneManager.setAmbientLight(new SColorf(1, 1, 1, 1));
      camera = new Camera1(sceneManager, nativeCamera);
      //nativeCamera.setPosition(new vector3df(0, 1000, -2000));
      //load camera flights
      Node eFlights = XMLReader.getChildElement(root, "cameraflights");
      if (eFlights != null)
      {
        List<Element> eFlightList =
          XMLReader.elements(eFlights, "cameraflight");
        for (int iFlight = 0; iFlight < eFlightList.size(); iFlight++)
        {
          Node eFlight = eFlightList.get(iFlight);
          Flight flight = new Flight();
          flight.setID(XMLReader.readAttributeValue(eFlight, "id"));
          float speed = XMLReader.readAttributeValueFloat(eFlight, "speed");
          if (speed > 0)
          {
            flight.setSpeed(speed);
          }
          flight.setLoop(XMLReader.readAttributeValueBoolean(eFlight, "loop"));
          //camera position points (from)
          Node eFrom = XMLReader.getChildElement(eFlight, "from");
          if (eFrom != null)
          {
            List<Element> ePointsList = XMLReader.elements(eFrom, "point");
            for (int i = 0; i < ePointsList.size(); i++)
            {
              Node ePoint = ePointsList.get(i);
              Vector3fFlags point = readPoint(ePoint);
              int times = XMLReader.readAttributeValueInt(ePoint, "times");
              if (times == 0) times = 1;
              for (int iTimes = 0; iTimes < times; iTimes++)
                flight.addFromPoint(point);
            }
          }
          else
          {
            throw new Exception(
              "Flight \"" + flight.getID() + "\" has no from-points!");
          }
          //camera position points (at), optional
          Node eAt = XMLReader.getChildElement(eFlight, "at");
          if (eAt != null)
          {
            List<Element> ePointsList = XMLReader.elements(eAt, "point");
            for (int i = 0; i < ePointsList.size(); i++)
            {
              Node ePoint = ePointsList.get(i);
              Vector3fFlags point = readPoint(ePoint);
              int times = XMLReader.readAttributeValueInt(ePoint, "times");
              if (times == 0) times = 1;
              for (int iTimes = 0; iTimes < times; iTimes++)
                flight.addAtPoint(point);
            }
          }
          else
          {
            //no camera target spline. Use two points (0,0,0)
            flight.addAtPoint(new Vector3fFlags());
            flight.addAtPoint(new Vector3fFlags());
          }
          flights.add(flight);
        }
      }
      //finished
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "GUI scene \"" + id + "\" successfully loaded.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "GUI scene \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Reads a Vector3fFlags point from the given XML element.
   */
  private Vector3fFlags readPoint(Node ePoint)
  {
    Vector3fFlags ret = new Vector3fFlags();
    String pos = XMLReader.readAttributeValue(ePoint, "pos");
    if (pos.equals("current"))
      ret.flags = Vector3fFlags.FLAG_CURRENTPOS;
    else if (pos.equals("scoreboard"))
      ret.flags = Vector3fFlags.FLAG_SCOREBOARD;
    else if (pos.equals("behindscoreboard"))
      ret.flags = Vector3fFlags.FLAG_BEHINDSCOREBOARD;
    else
    {
      ret.x = XMLReader.readAttributeValueFloat(ePoint, "x");
      ret.y = XMLReader.readAttributeValueFloat(ePoint, "y");
      ret.z = XMLReader.readAttributeValueFloat(ePoint, "z");
    }
    return ret;
  }
  
  
  /**
   * Sets a camera flight by its ID.
   */
  public void setCameraFlightByID(String id)
  {
    Flight f = getCameraFlightByID(id);
    if (id != null)
      camera.setFlight(f, stadium);
  }
  
  
  /**
   * Gets a camera flight by its ID.
   */
  public Flight getCameraFlightByID(String id)
  {
    for (int i = 0; i < flights.size(); i++)
    {
      if (flights.get(i).getID().equals(id))
      {
        return flights.get(i);
      }
    }
    return null;
  }
  
  
  /**
   * Removes the nodes belonging to this gui scene 
   * from Irrlicht's scene manager.
   */
  public void removeFromScene()
  {
    if (stadium != null)
      stadium.removeFromScene();
    if (nativeCamera != null)
      nativeCamera.remove();
    if (weather != null)
    {
      weather.removeFromScene();
      if (weatherSound)
        GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_WEATHERAMBIENCE).fadeOut();
    }
    if (cup != null)
      cup.removeFromScene();
    if (team != null)
      team.removeFromScene();
    if (fireworks != null)
      fireworks.removeFromScene();
  }

  
  /**
   * Gets the ID of this gui scene.
   */
  public String getID()
  {
    return id;
  }
  
  
  /**
   * Call this method every frame this scene is shown.
   */
  public void run()
  {
    camera.update();
    if (stadium != null && stadium.getCrowd() != null)
      stadium.getCrowd().step(null);
    if (team != null)
    {
      //find new animation stages for players
      //(the goalkeeper does not change his animation)
      for (int i = 1; i < 5; i++)
      {
        Player player = team.getPlayers()[i];
        if (player.getNode().getFrameNr() < playerLastFrame[i])
        {
          player.setAnimationStage(AnimationStage.getRandomElation());
        }
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    if (fireworks != null)
      fireworks.step();
  }
  
  
  /**
   * Reload the stadium.
   */
  public void reloadStadium()
  {
    if (stadiumID != null && stadiumID.length() > 0)
    {
      stadium.removeFromScene();
      stadium = new Stadium(stadiumID);
      stadium.addToScene(sceneManager, videoDriver,
        GameEngine.getMatchManager().getPhysicsManager());
    }
  }
  
  
  /**
   * Creates the championship celebration
   * of league 3.
   */
  private void createSceneChampion3()
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.Career)
      return;
    String teamID = GameEngine.getCareer().getUserTeamID();
    team = new Team(teamID, null, false, null);
    //align team around center
    for (int i = 0; i < 5; i++)
    {
      Player player = team.getPlayers()[i];
      Vector3f pos = new Vector3f(
        (i + 2) % 5 - 2, 0,
        -2 + Math.abs((i + 2) % 5 - 2));
      pos.x += MathTools.randomCenter(0.3f);
      pos.z += MathTools.randomCenter(0.3f);
      player.setPosition(pos);
      player.getNode().setRotation(new vector3df(0,
        (MathTools.randomCenter(10) - 50) * ((i + 2) % 5 - 2), 0));
      if (i == 0)
        player.setAnimationStage(AnimationStage.CupInHands);
      else
      {
        player.setAnimationStage(AnimationStage.getRandomElation());
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    //cup
    SimpleStadiumObject cup = new SimpleStadiumObject("cup_league3.x");
    cup.addToScene(sceneManager, videoDriver, null);
    cup.setScale(0.5f);
    cup.setRotation(new Vector3f(0, 0, -90));
    cup.setPosition(new Vector3f(0, 0.15f, 0));
    ISceneNode playerHand =
      team.getPlayers()[0].getNode().getMS3DJointNode("Bip01 R Finger1");
    playerHand.addChild(cup.getNode());
    //fill stands
    PlayerColors col = team.getTeamInfo().getPlayerColorsHome();
    stadium.createCrowd(1, 0,
      col.getColor(PlayerColors.COLOR_SHIRT), col.getColor(PlayerColors.COLOR_PANTS),
      true);
    stadium.getCrowd().createAmbienceSound();
    stadium.setAdboardsTexture(team.getTeamInfo().getAdboardsFilename(),
      col.getColor(PlayerColors.COLOR_SHIRT));
  }
  
  
  /**
   * Creates the championship celebration
   * of league 2.
   */
  private void createSceneChampion2()
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.Career)
      return;
    String teamID = GameEngine.getCareer().getUserTeamID();
    team = new Team(teamID, null, false, null);
    //align team around fan curve
    for (int i = 0; i < 5; i++)
    {
      Player player = team.getPlayers()[i];
      Vector3f pos = new Vector3f(
        (i + 2) % 5 - 2, 0,
        -Math.abs((i + 2) % 5 - 2));
      pos.x += MathTools.randomCenter(0.3f);
      pos.z += MathTools.randomCenter(0.3f);
      pos.x += 17f;
      pos.z -= 8f;
      player.setPosition(pos);
      player.getNode().setRotation(new vector3df(0,
        180 + (MathTools.randomCenter(10) + 50) * ((i + 2) % 5 - 2), 0));
      if (i == 0)
        player.setAnimationStage(AnimationStage.CupInHands);
      else
      {
        player.setAnimationStage(AnimationStage.getRandomElation());
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    //cup
    SimpleStadiumObject cup = new SimpleStadiumObject("cup_league2.x");
    cup.addToScene(sceneManager, videoDriver, null);
    cup.setScale(0.5f);
    cup.setRotation(new Vector3f(30, 0, -90));
    cup.setPosition(new Vector3f(0.1f, 0.15f, 0));
    ISceneNode playerHand =
      team.getPlayers()[0].getNode().getMS3DJointNode("Bip01 R Finger1");
    playerHand.addChild(cup.getNode());
    //fill stands
    PlayerColors col = team.getTeamInfo().getPlayerColorsHome();
    stadium.createCrowd(1, 0,
      col.getColor(PlayerColors.COLOR_SHIRT), col.getColor(PlayerColors.COLOR_PANTS),
      true);
    stadium.getCrowd().createAmbienceSound();
    stadium.setAdboardsTexture(team.getTeamInfo().getAdboardsFilename(),
      col.getColor(PlayerColors.COLOR_SHIRT));
  }
  
  
  /**
   * Creates the championship celebration
   * of league 1.
   */
  private void createSceneChampion1()
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.Career)
      return;
    String teamID = GameEngine.getCareer().getUserTeamID();
    team = new Team(teamID, null, false, null);
    //align team around fan curve
    for (int i = 0; i < 5; i++)
    {
      Player player = team.getPlayers()[i];
      Vector3f pos = new Vector3f(
        (i + 2) % 5 - 2, 0,
        -Math.abs((i + 2) % 5 - 2));
      pos.x += MathTools.randomCenter(0.3f);
      pos.z += MathTools.randomCenter(0.3f);
      pos.x += 20f;
      pos.z -= 9f;
      player.setPosition(pos);
      player.getNode().setRotation(new vector3df(0,
        180 + (MathTools.randomCenter(10) + 50) * ((i + 2) % 5 - 2), 0));
      if (i == 0)
        player.setAnimationStage(AnimationStage.CupInHands);
      else
      {
        player.setAnimationStage(AnimationStage.getRandomElation());
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    //cup
    SimpleStadiumObject cup = new SimpleStadiumObject("cup_league1.x");
    cup.addToScene(sceneManager, videoDriver, null);
    cup.setScale(0.5f);
    cup.setRotation(new Vector3f(30, 0, -90));
    cup.setPosition(new Vector3f(0.1f, 0.15f, 0));
    ISceneNode playerHand =
      team.getPlayers()[0].getNode().getMS3DJointNode("Bip01 R Finger1");
    playerHand.addChild(cup.getNode());
    //fill stands
    PlayerColors col = team.getTeamInfo().getPlayerColorsHome();
    stadium.createCrowd(1, 0,
      col.getColor(PlayerColors.COLOR_SHIRT), col.getColor(PlayerColors.COLOR_PANTS),
      true);
    stadium.getCrowd().createAmbienceSound();
    stadium.setAdboardsTexture(team.getTeamInfo().getAdboardsFilename(),
      col.getColor(PlayerColors.COLOR_SHIRT));
    //smoke bomb
    BengalLight bengalLight = new BengalLight(
      BengalLight.TYPE_SMOKEBOMB, new Vector3f(15, 0, -5));
    stadium.getCrowd().addBengalLight(bengalLight);
  }
  
   
  /**
   * Creates the championship celebration
   * of the Allstars League.
   */
  private void createSceneChampionAllstars()
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.Career)
      return;
    String teamID = GameEngine.getCareer().getUserTeamID();
    team = new Team(teamID, null, false, null);
    //align team around the side line
    for (int i = 0; i < 5; i++)
    {
      Player player = team.getPlayers()[i];
      Vector3f pos = new Vector3f(
        (i + 2) % 5 - 2, 0,
        -0.5f * Math.abs((i + 2) % 5 - 2));
      pos.x += MathTools.randomCenter(0.3f);
      pos.z += MathTools.randomCenter(0.3f);
      pos.x += 0f;
      pos.z -= 9f;
      player.setPosition(pos);
      player.getNode().setRotation(new vector3df(0,
        180 + (MathTools.randomCenter(10) + 50) * ((i + 2) % 5 - 2), 0));
      if (i == 0)
        player.setAnimationStage(AnimationStage.CupInHands);
      else
      {
        player.setAnimationStage(AnimationStage.getRandomElation());
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    //cup
    SimpleStadiumObject cup = new SimpleStadiumObject("cup_allstars.x");
    cup.addToScene(sceneManager, videoDriver, null);
    cup.setScale(0.5f);
    cup.setRotation(new Vector3f(30, 0, -90));
    cup.setPosition(new Vector3f(0.1f, 0.15f, 0));
    ISceneNode playerHand =
      team.getPlayers()[0].getNode().getMS3DJointNode("Bip01 R Finger1");
    playerHand.addChild(cup.getNode());
    //fill stands
    PlayerColors col = team.getTeamInfo().getPlayerColorsHome();
    stadium.createCrowd(1, 0,
      col.getColor(PlayerColors.COLOR_SHIRT), col.getColor(PlayerColors.COLOR_PANTS),
      true);
    stadium.getCrowd().createAmbienceSound();
    stadium.setAdboardsTexture(team.getTeamInfo().getAdboardsFilename(),
      col.getColor(PlayerColors.COLOR_SHIRT));
    //smoke bomb
    BengalLight bengalLight = new BengalLight(
      BengalLight.TYPE_SMOKEBOMB, new Vector3f(10, 0, 5));
    stadium.getCrowd().addBengalLight(bengalLight);
    //fireworks
    fireworks = new Fireworks(new Vector3f(0, 15, 0));
    fireworks.addToScene(sceneManager, videoDriver);
  }
  
  
  /**
   * Creates the championship celebration
   * of the World Cup.
   */
  private void createSceneChampionWorldCup()
  {
    if (GameEngine.getGameMode() != GameEngine.GameMode.WorldCup)
      return;
    String teamID = GameEngine.getWorldCup().getUserTeamID();
    team = new Team(teamID, null, false, null);
    //align team around fan curve
    for (int i = 0; i < 5; i++)
    {
      Player player = team.getPlayers()[i];
      Vector3f pos = new Vector3f(
        (i + 2) % 5 - 2, 0,
        -Math.abs((i + 2) % 5 - 2));
      pos.x += MathTools.randomCenter(0.3f);
      pos.z += MathTools.randomCenter(0.3f);
      pos.x += 20f;
      pos.z -= 9f;
      player.setPosition(pos);
      player.getNode().setRotation(new vector3df(0,
        180 + (MathTools.randomCenter(10) + 50) * ((i + 2) % 5 - 2), 0));
      if (i == 0)
        player.setAnimationStage(AnimationStage.CupInHands);
      else
      {
        player.setAnimationStage(AnimationStage.getRandomElation());
        playerLastFrame[i] = player.getNode().getFrameNr();
      }
    }
    //cup
    SimpleStadiumObject cup = new SimpleStadiumObject("cup_worldcup.x");
    cup.addToScene(sceneManager, videoDriver, null);
    cup.setScale(0.5f);
    cup.setRotation(new Vector3f(30, 0, -90));
    cup.setPosition(new Vector3f(0.1f, 0.15f, 0));
    ISceneNode playerHand =
      team.getPlayers()[0].getNode().getMS3DJointNode("Bip01 R Finger1");
    playerHand.addChild(cup.getNode());
    //fill stands
    PlayerColors col = team.getTeamInfo().getPlayerColorsHome();
    stadium.createCrowd(1, 0,
      col.getColor(PlayerColors.COLOR_SHIRT), col.getColor(PlayerColors.COLOR_PANTS),
      true);
    stadium.getCrowd().createAmbienceSound();
    stadium.setAdboardsTexture(team.getTeamInfo().getAdboardsFilename(),
      col.getColor(PlayerColors.COLOR_SHIRT));
    //smoke bombs
    BengalLight bengalLight = new BengalLight(
      BengalLight.TYPE_SMOKEBOMB, new Vector3f(10, 0, 5));
    stadium.getCrowd().addBengalLight(bengalLight);
    bengalLight = new BengalLight(
      BengalLight.TYPE_SMOKEBOMB, new Vector3f(-5, 0, -8));
    stadium.getCrowd().addBengalLight(bengalLight);
    //fireworks
    fireworks = new Fireworks(new Vector3f(0, 15, 0));
    fireworks.addToScene(sceneManager, videoDriver);
    //scoreboard: show "06".
    if (stadium.getScoreboard() != null)
    {
      stadium.getScoreboard().setScore(0, 0);
      stadium.getScoreboard().setScore(1, 6);
    }
  }
  
  
}
