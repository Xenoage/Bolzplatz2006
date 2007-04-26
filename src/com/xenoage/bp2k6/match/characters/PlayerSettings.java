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
package com.xenoage.bp2k6.match.characters;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class stores the settings for all players,
 * like default speed, pass speed, animation
 * speed, and so on.
 * The values are loaded from the xml file
 * "/data/config/player.xml"
 *
 * @author Andi
 */
public class PlayerSettings
{

  //player settings
  private static float playerSpeed;
  private static float playerSprintFactor;
  private static float playerRotationSpeed;

  //pass settings
  private static float passSpeedHMin;
  private static float passSpeedVMin;
  private static float passSpeedHMax;
  private static float passSpeedVMax;

  //shoot settings
  private static float shootSpeedH;
  private static float shootSpeedHAdd;
  private static float shootSpeedV;
  private static float shootSpeedLiftV;
  private static float shootSpeedLiftHRed;
  
  //goalkick settings
  private static float goalkickSpeedHMin;
  private static float goalkickSpeedVMin;
  private static float goalkickSpeedHMax;
  private static float goalkickSpeedVMax;
  
  //cornerkick settings
  private static float cornerkickSpeedHMin;
  private static float cornerkickSpeedVMin;
  private static float cornerkickSpeedHMax;
  private static float cornerkickSpeedVMax;
  
  //throwin settings
  private static float throwinSpeedHMin;
  private static float throwinSpeedVMin;
  private static float throwinSpeedHMax;
  private static float throwinSpeedVMax;
  
  //penaltykick settings
  private static float penaltykickSpeedHMin;
  private static float penaltykickSpeedVMin;
  private static float penaltykickSpeedHMax;
  private static float penaltykickSpeedVMax;
  
  //freekick settings
  private static float freekickSpeedHMin;
  private static float freekickSpeedVMin;
  private static float freekickSpeedHMax;
  private static float freekickSpeedVMax; 
  
  //bounce settings
  private static float bounceBallSpeed;
  private static float bounceGoalkeeperBallSpeed;
  private static float bounceVelFactor;
  private static float bounceVelRandom;


  /**
   * Load settings from "/data/config/player.xml"
   * Call this method before working with any
   * classes from com.xenoage.bp2k6.match.players.
   */
  public static void loadSettings()
  {
    //load settings from XML file
    try
    {
      
      Document doc = XMLReader.readFile("data/config/player.xml");
      Node root = doc.getFirstChild();
      Node e = null;

      //player settings
      e = XMLReader.getChildElement(root, "player");
      playerSpeed = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speed"));
      playerSprintFactor = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "sprintfactor"));
      playerRotationSpeed = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "rotationspeed"));

      //pass settings
      e = XMLReader.getChildElement(root, "pass");
      passSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      passSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      passSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      passSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));

      //shoot settings
      e = XMLReader.getChildElement(root, "shoot");
      shootSpeedH = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedh"));
      shootSpeedHAdd = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhadd"));
      shootSpeedV = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedv"));
      shootSpeedLiftV = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedliftv"));
      shootSpeedLiftHRed = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedlifthred"));
      
      //goalkick settings
      e = XMLReader.getChildElement(root, "goalkick");
      goalkickSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      goalkickSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      goalkickSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      goalkickSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));
      
      //cornerkick settings
      e = XMLReader.getChildElement(root, "cornerkick");
      cornerkickSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      cornerkickSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      cornerkickSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      cornerkickSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));
      
      //throwin settings
      e = XMLReader.getChildElement(root, "throwin");
      throwinSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      throwinSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      throwinSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      throwinSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));
      
      //penaltykick settings
      e = XMLReader.getChildElement(root, "penaltykick");
      penaltykickSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      penaltykickSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      penaltykickSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      penaltykickSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));
      
      //freekick settings
      e = XMLReader.getChildElement(root, "freekick");
      freekickSpeedHMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmin"));
      freekickSpeedVMin = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmin"));
      freekickSpeedHMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedhmax"));
      freekickSpeedVMax = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "speedvmax"));
      
      //bounce settings
      e = XMLReader.getChildElement(root, "bounce");
      bounceBallSpeed = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "ballspeed"));
      bounceGoalkeeperBallSpeed = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "goalkeeperballspeed"));
      bounceVelFactor = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "velfactor"));
      bounceVelRandom = XMLReader.readElementValueFloat(
        XMLReader.getChildElement(e, "velrandom"));
      
      
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, new PlayerSettings(),
        "Player settings could not be read from \"data/config/player.xml\"" +
          " could not be loaded! Details:");
      GameEngine.fatalError(new PlayerSettings(), ex);
    }
  }


  /**
   * Gets the player-speed in m/s.
   */
  public static float getPlayerSpeed()
  {
    return playerSpeed;
  }


  /**
   * Gets the speed factor for sprinting.
   */
  public static float getPlayerSprintFactor()
  {
    return playerSprintFactor;
  }


  /**
   * Gets the player-rotation-speed in degrees/s.
   */
  public static float getPlayerRotationSpeed()
  {
    return playerRotationSpeed;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a pass in m/s.
   */
  public static float getPassSpeedHMin()
  {
    return passSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a pass in m/s.
   */
  public static float getPassSpeedVMin()
  {
    return passSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a pass in m/s.
   */
  public static float getPassSpeedHMax()
  {
    return passSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a pass in m/s.
   */
  public static float getPassSpeedVMax()
  {
    return passSpeedVMax;
  }


  /**
   * Gets the horizontal ball-speed of a goal-shoot in m/s.
   */
  public static float getShootSpeedH()
  {
    return shootSpeedH;
  }


  /**
   * Gets the additional keypress-duration dependent horizontal speed of a goal-shoot in m/s.
   */
  public static float getShootSpeedHAdd()
  {
    return shootSpeedHAdd;
  }


  /**
   * Gets the vertical ball-speed of a normal goal-shoot in m/s.
   */
  public static float getShootSpeedV()
  {
    return shootSpeedV;
  }


  /**
   * Gets the vertical ball-speed of a "lifted" goal-shoot in m/s.
   */
  public static float getShootSpeedLiftV()
  {
    return shootSpeedLiftV;
  }


  /**
   * Gets the horizontal ball-speed-reduction of a "lifted" goal shoot
   * (e.g. 0.1 = 10% of normal speed).
   */
  public static float getShootSpeedLiftHRed()
  {
    return shootSpeedLiftHRed;
  }



  /**
   * Gets the minimal horizontal ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedHMin()
  {
    return goalkickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedVMin()
  {
    return goalkickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedHMax()
  {
    return goalkickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a goalkick in m/s.
   */
  public static float getGoalkickSpeedVMax()
  {
    return goalkickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedHMin()
  {
    return cornerkickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedVMin()
  {
    return cornerkickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedHMax()
  {
    return cornerkickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a cornerkick in m/s.
   */
  public static float getCornerkickSpeedVMax()
  {
    return cornerkickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedHMin()
  {
    return throwinSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedVMin()
  {
    return throwinSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedHMax()
  {
    return throwinSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a throwin in m/s.
   */
  public static float getThrowinSpeedVMax()
  {
    return throwinSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedHMin()
  {
    return penaltykickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedVMin()
  {
    return penaltykickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedHMax()
  {
    return penaltykickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a penaltykick in m/s.
   */
  public static float getPenaltykickSpeedVMax()
  {
    return penaltykickSpeedVMax;
  }
  
  
  /**
   * Gets the minimal horizontal ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedHMin()
  {
    return freekickSpeedHMin;
  }
  
  
  /**
   * Gets the minimal vertical ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedVMin()
  {
    return freekickSpeedVMin;
  }

  
  /**
   * Gets the maximal horizontal ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedHMax()
  {
    return freekickSpeedHMax;
  }

  
  /**
   * Gets the maximal vertical ballspeed of a freekick in m/s.
   */
  public static float getFreekickSpeedVMax()
  {
    return freekickSpeedVMax;
  }


  /**
   * Gets the minimal speed in m/s where a player
   * can not get the ball, but bounces it.
   */
  public static float getBounceBallSpeed()
  {
    return bounceBallSpeed;
  }
  
  
  /**
   * Gets the minimal speed in m/s where a goalkeeper
   * can not catch the ball, but bounces it.
   */
  public static float getBounceGoalkeeperBallSpeed()
  {
    return bounceGoalkeeperBallSpeed;
  }


  /**
   * This number is multiplied with the ball velocity when bouncing.
   */
  public static float getBounceVelFactor()
  {
    return bounceVelFactor;
  }


  /**
   * After bounce
   * [randomCenter(velrandom),randomCenter(velrandom),randomCenter(velrandom)]
   * is added to the ball velocity.
   */
  public static float getBounceVelRandom()
  {
    return bounceVelRandom;
  }

}
