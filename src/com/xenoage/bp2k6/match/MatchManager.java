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
package com.xenoage.bp2k6.match;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.GameEngine.GameMode;
import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.gamemodes.reports.AllstarsLeague;
import com.xenoage.bp2k6.gamemodes.reports.MatchInfo;
import com.xenoage.bp2k6.gui.loader.parser.OptionButtonParser;
import com.xenoage.bp2k6.match.camera.*;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.match.stadium.*;
import com.xenoage.bp2k6.match.weather.Weather;
import com.xenoage.bp2k6.util.*;

import java.io.File;
import java.util.ArrayList;
import javax.vecmath.Vector3f;
import net.sf.jirr.*;


/**
 * The <code>MatchManager</code> is the interface between the
 * <code>GameEngine</code> and the other classes in the
 * <code>com.xenoage.bp2k6.match</code> package.
 * Call <code>run()</code> in each frame of the mainloop
 * if a match is running.
 * To prepare for a match, call the methods <code>setTeam(0, ...)</code>,
 * <code>setController(1, ...)</code>,
 * <code>setTeam(1, ...)</code> and <code>setStadium</code> and
 * then <code>startMatch</code>.
 * The match manager contains also some useful objects,
 * like a list of all available teams and stadiums and
 * the mesh of a player.
 *
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM_2)
 */
public class MatchManager
{

  //the current match
  private Match match = null;
  //result of the match, when it is over
  private MatchInfo matchResult = null; 

  //physics
  private PhysicsManager physics;

  //camera
  private Camera camera;

  //the character meshes
  private OutfieldPlayerMesh meshOutfieldPlayer;
  private GoalkeeperMesh meshGoalkeeper;
  private RefereeMesh meshReferee;

  //list of all available teams, stadiums and referees
  private ArrayList<TeamInfo> teams = new ArrayList<TeamInfo>();
  private ArrayList<StadiumInfo> stadiums = new ArrayList<StadiumInfo>();
  private ArrayList<RefereeInfo> referees = new ArrayList<RefereeInfo>();
  private ArrayList<String> weathers = new ArrayList<String>();

  //preparation for the next match
  private String[] nextMatchControllers = new String[2];
  private String[] nextMatchTeams = new String[2];
  private String nextMatchStadium;
  private String nextMatchWeather;
  private String nextMatchReferee;
  private int nextMatchRefereePrejudice = 0;
  
  //the current weather
  Weather weather = null;
  
  //focuses for the active player
  private IAnimatedMeshSceneNode nodeActivePlayerFocus[] = new IAnimatedMeshSceneNode[2];
  private ITexture textureActivePlayerFocus[] = new ITexture[2];
  private vector3df nativePosActivePlayerFocus[] = new vector3df[2];

  //LOM_2 patch
  private IAnimatedMeshSceneNode nodeFreekickWallDistance;
  private ITexture textureFreekickWallDistance;
  

  /**
   * Constructor for the match manager.
   */
  public MatchManager()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this, "Initialize match manager.");

    //init physics engine
    Logging.log(Logging.LEVEL_MESSAGES, this, "Create physics engine...");
    physics = new PhysicsManager();

    //create camera
    Logging.log(Logging.LEVEL_MESSAGES, this, "Create camera...");
    camera = new Camera1(GameEngine.getSceneManager(), GameEngine.getCamera());

    //load outfield player mesh
    meshOutfieldPlayer = new OutfieldPlayerMesh();
    meshOutfieldPlayer.loadMesh("data/meshes/player01.ms3d");
    if (meshOutfieldPlayer.getMesh() == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Could not load the outfield player mesh \"data/meshes/player01.ms3d\"!"));
    }

    //load goalkeeper mesh
    meshGoalkeeper = new GoalkeeperMesh();
    meshGoalkeeper.loadMesh("data/meshes/player02.ms3d");
    if (meshGoalkeeper.getMesh() == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Could not load the player mesh \"data/meshes/player02.ms3d\"!"));
    }
    
    //load referee mesh
    meshReferee = new RefereeMesh();
    meshReferee.loadMesh("data/meshes/player01.ms3d");
    if (meshReferee.getMesh() == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Could not load the referee mesh \"data/meshes/player02.ms3d\"!"));
    }


    //load list of all available teams
    String[] teamFiles = new File("data/teams/").list(FileUtils.getXMLFilter());
    if (teamFiles == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Teams directory does not exist!"));
    }
    else if (teamFiles.length < 2)
    {
      GameEngine.fatalError(this, new Exception(
        "There are not enough teams. At least 2 must exist!"));
    }
    else
    {
      for (int i = 0; i < teamFiles.length; i++)
      {
        String teamID = FileUtils
          .getNameWithoutExtension(new File(teamFiles[i]));
        teams.add(new TeamInfo(teamID));
        Logging.log(Logging.LEVEL_MESSAGES, this, "Team information loaded: \""
          + teamID + "\"");
      }
    }
    
    
    //load list of all available stadiums
    String[] stadiumFiles = new File("data/stadiums/").list(FileUtils.getXMLFilter());
    if (stadiumFiles == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Stadiums directory does not exist!"));
    }
    else if (stadiumFiles.length < 1)
    {
      GameEngine.fatalError(this, new Exception(
        "There are not enough stadiums. At least one must exist!"));
    }
    else
    {
      for (int i = 0; i < stadiumFiles.length; i++)
      {
        String stadiumID = FileUtils
          .getNameWithoutExtension(new File(stadiumFiles[i]));
        stadiums.add(new StadiumInfo(stadiumID));
        Logging.log(Logging.LEVEL_MESSAGES, this, "Stadium information loaded: \""
          + stadiumID + "\"");
      }
    }
    
    
    //load list of all available referees
    String[] refereeFiles = new File("data/referees/").list(FileUtils.getXMLFilter());
    if (refereeFiles == null)
    {
      GameEngine.fatalError(this, new Exception(
        "Referees directory does not exist!"));
    }
    else if (refereeFiles.length < 1)
    {
      GameEngine.fatalError(this, new Exception(
        "There are not enough referees. At least one must exist!"));
    }
    else
    {
      for (int i = 0; i < refereeFiles.length; i++)
      {
        String refereeID = FileUtils
          .getNameWithoutExtension(new File(refereeFiles[i]));
        referees.add(new RefereeInfo(refereeID));
        Logging.log(Logging.LEVEL_MESSAGES, this, "Referee information loaded: \""
          + refereeID + "\"");
      }
    }
    
    
    //load list of all available weathers
    String[] weatherFiles = new File("data/weather/").list(FileUtils.getXMLFilter());
    if (weatherFiles == null || weatherFiles.length < 1)
    {
      GameEngine.fatalError(new OptionButtonParser(), new Exception(
        "No weather files found!"));
    }
    else
    {
      weathers.add(null);
      for (int i = 0; i < weatherFiles.length; i++)
      {
        String weatherID = FileUtils.getNameWithoutExtension(
          new File(weatherFiles[i]));
        //use "day-sunshine" as the first
        if (weatherID.equals("day-sunshine"))
          weathers.set(0, weatherID);
        else
          weathers.add(weatherID);
      }
      if (weathers.get(0) == null)
        weathers.remove(0);
    }
    

    Logging.log(Logging.LEVEL_MESSAGES, this, "MatchManager created.");
  }


  /**
   * Sets a team for the next match.
   * @param index   0 for team 1, 1 for team 2
   * @param id      id of the team to set
   */
  public void setTeam(int index, String id)
  {
    if (id.equals("%randomwcteam1%") || id.equals("%randomwcteam2%"))
    {
      ArrayList<String> wcTeams = WorldCup.getTeamIDs(true);
      if (id.equals("%randomwcteam2%"))
      {
        //not the same as team 1!
        do
        {
          id = wcTeams.get(MathTools.randomInt(wcTeams.size()));
        }
        while (id.equals(nextMatchTeams[0]));
      }
      else
      {
        id = wcTeams.get(MathTools.randomInt(wcTeams.size()));
      }
    }
    nextMatchTeams[index] = id;
  }
  
  
  /**
   * Sets the stadium for the next match.
   * @param id   ID of the stadium to use in the next match
   */
  public void setStadium(String id)
  {
    if (id.equals("%randomwcstadium%"))
    {
      //only use stadiums beginning with "world" or "allstars"
      do
      {
        id = getRandomStadiumID();
      }
      while (!(id.startsWith("world") || id.startsWith("allstars")));
    }
    nextMatchStadium = id;
  }
  
  
  /**
   * Sets the weather for the next match.
   * @param id            id of the weather to set
   * @param applyAtOnce   apply at once (true) or when next match starts (false)
   */
  public void setWeather(String id, boolean applyAtOnce)
  {
    nextMatchWeather = id;
    if (applyAtOnce)
    {
      if (nextMatchWeather.equals("random"))
        nextMatchWeather = getRandomWeatherID();
      weather = new Weather(nextMatchWeather,
        GameEngine.getVideoDriver(), GameEngine.getSceneManager());
    }
  }
  
  
  /**
   * Sets the referee for the next match.
   */
  public void setReferee(String id)
  {
    nextMatchReferee = id;
  }
  
  
  /**
   * Sets the prejudice of the referee for the next match.
   */
  public void setRefereePrejudice(int prejudice)
  {
    nextMatchRefereePrejudice = prejudice;
  }


  /**
   * Sets the controller (human or AI) for the next match.
   * @param index        0 for team 1, 1 for team 2
   * @param controller   "player1" for player 1, "player2" for
   *                     player 2, else: class name of the TeamAI.
   */
  public void setController(int index, String controller)
  {
    if (index == 0 || index == 1)
      nextMatchControllers[index] = controller;
  }
  
  
  /**
   * Returns true, if the next match is a user vs user match.
   */
  public boolean isNextMatchUserVsUser()
  {
    return nextMatchControllers[0] != null &&
      nextMatchControllers[1] != null &&
      nextMatchControllers[0].contains("Player1") &&
      nextMatchControllers[1].contains("Player2");
  }


  /**
   * Returns a list with the ids of all available teams.
   */
  public ArrayList<TeamInfo> getAvailableTeams()
  {
    return teams;
  }
  
  
  /**
   * Returns a list with the available referees.
   */
  public ArrayList<RefereeInfo> getAvailableReferees()
  {
    return referees;
  }
  
  
  /**
   * Gets a random referee.
   */
  public RefereeInfo getRandomReferee()
  {
    return referees.get(MathTools.randomInt(referees.size()));
  }
  
  
  /**
   * Returns a list with the available weathers.
   */
  public ArrayList<String> getAvailableWeathers()
  {
    return weathers;
  }
  
  
  /**
   * Gets a random weather, but it will probably
   * (about 60%) be day-sunshine.
   */
  public String getRandomWeatherID()
  {
    if (weathers.contains("day-sunshine") && MathTools.random() < 0.75f)
      return "day-sunshine";
    else
      return weathers.get(MathTools.randomInt(weathers.size()));
  }
  
  
  /**
   * Returns a list with the ids of all available teams.
   */
  public ArrayList<StadiumInfo> getAvailableStadiums()
  {
    return stadiums;
  }
  
  
  /**
   * Gets the id of a random stadium.
   */
  public String getRandomStadiumID()
  {
    return stadiums.get(MathTools.randomInt(stadiums.size())).getID();
  }
  
  
  /**
   * Gets the index of the given stadium, or 0 if not found
   */
  public int getStadiumIndex(String stadiumID)
  {
    if (stadiumID == null || stadiumID.length() == 0)
      return 0;
    for (int i = 0; i < stadiums.size(); i++)
      if (stadiums.get(i).getID().equals(stadiumID))
        return i;
    return 0;
  }
  
  
  /**
   * Returns the TeamInfo that belongs to the
   * given team ID, <code>null</code> if not found.
   */
  public TeamInfo getTeamInfo(String teamID)
  {
    for (int i = 0; i < teams.size(); i++)
      if (teams.get(i).getID().equals(teamID))
        return teams.get(i);
    return null;
  }
  
  
  /**
   * Returns the RefereeInfo that belongs to the
   * given team ID, <code>null</code> if not found.
   */
  public RefereeInfo getRefereeInfo(String refereeID)
  {
    for (int i = 0; i < referees.size(); i++)
      if (referees.get(i).getID().equals(refereeID))
        return referees.get(i);
    return null;
  }


  /**
   * Starts a new match.
   */
  public void startMatch()
  {
    
    //pause off
    GameEngine.setPause(false);
    
    //clear physics manager
    clearPhysicsManager();
     
    
    ISceneManager smgr = GameEngine.getSceneManager();
    
    
    //create stadium
    if (nextMatchStadium == null || nextMatchStadium.length() == 0)
      GameEngine.fatalError(this, new Exception(
        "Can not start match: Stadium must be set"));
    Stadium stadium = new Stadium(nextMatchStadium);
    stadium.addToScene(smgr, GameEngine.getVideoDriver(), physics);
    
    //physics: add floor (soccer field)
    physics.addFloor();


    //load focuses for active players
    String meshPath = "data/meshes/playerfocus.x";
    IAnimatedMesh meshPlayerFocus = smgr.getMesh(meshPath);
    if (meshPlayerFocus == null)
      GameEngine.fatalError(this, new Exception(
        "Player focus mesh could not be loaded: \"" + meshPath + "\""));
    for (int i = 0; i < 2; i++)
    {
      nativePosActivePlayerFocus[i] = new vector3df();
      nodeActivePlayerFocus[i] = smgr.addAnimatedMeshSceneNode(meshPlayerFocus);
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      nodeActivePlayerFocus[i].setScale(new vector3df(u, u, u));
      nodeActivePlayerFocus[i].setMaterialType(
        E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
      nodeActivePlayerFocus[i].setMaterialFlag(
        E_MATERIAL_FLAG.EMF_LIGHTING, false);
      String texturePath = "data/meshes/playerfocus" + (i + 1) + ".png";
      textureActivePlayerFocus[i] = GameEngine.getVideoDriver()
        .getTexture(texturePath);
      if (textureActivePlayerFocus[i] == null)
        GameEngine.fatalError(this, new Exception(
          "Player focus texture could not be loaded: \"" + texturePath + "\""));
    }
    
    //LOM_2 patch: load freekick-circle-mesh
    //TODO new mesh for freekick-circle? (this uses the playerfocus)
    nodeFreekickWallDistance = smgr.addAnimatedMeshSceneNode(meshPlayerFocus);
    float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
    nodeFreekickWallDistance.setScale(new vector3df(5 * u, u, 5 * u));
    nodeFreekickWallDistance.setMaterialType(
      E_MATERIAL_TYPE.EMT_TRANSPARENT_ADD_COLOR);
    nodeFreekickWallDistance.setMaterialFlag(
      E_MATERIAL_FLAG.EMF_LIGHTING, false);
    nodeFreekickWallDistance.setVisible(false);
    String texturePath = "data/meshes/freekickwalldistance.png";
    textureFreekickWallDistance = GameEngine.getVideoDriver()
      .getTexture(texturePath);
    if (textureFreekickWallDistance == null)
      GameEngine.fatalError(this, new Exception(
        "Freekick circle texture could not be loaded: \"" + texturePath + "\""));
    nodeFreekickWallDistance.setMaterialTexture(0, textureFreekickWallDistance);
    
    //weather
    if (nextMatchWeather != null && nextMatchWeather.length() > 0)
    {
      
      if (GameEngine.getGameMode() == GameMode.Career)
      {
        //career: special decisions
        Career career = GameEngine.getCareer();
        if (career.getCurrentMatchDayLeague() == Career.LEAGUE_3)
        {
          //when career mode and 3. league, avoid most night matches
          int i = 0;
          do
          {
            nextMatchWeather = getRandomWeatherID();
            i++;
          }
          while (nextMatchWeather.startsWith("night") && i < 2);
        }
        else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_ALLSTARS)
        {
          //when career mode and allstars league, try to play at night
          if (MathTools.random() < 0.5)
          {
            nextMatchWeather = "night-moonlight";
          }
          else
          {
            int i = 0;
            do
            {
              nextMatchWeather = getRandomWeatherID();
              i++;
            }
            while (nextMatchWeather.startsWith("day") && i < 3);
          }
        }
      }
      setWeather(nextMatchWeather, true);
    }
    
    //referee
    if (nextMatchReferee == null || nextMatchReferee.length() == 0)
    {
      nextMatchReferee = getRandomReferee().getID();
    }

    //teams set?
    if (nextMatchTeams[0] == null || nextMatchTeams[1] == null)
      GameEngine.fatalError(this, new Exception(
        "Can not start match: Two teams must be set!"));

    //controllers set?
    if (nextMatchControllers[0] == null || nextMatchControllers[1] == null)
      GameEngine.fatalError(this, new Exception(
        "Can not start match: Two controllers must be set!"));
    
    //draw allowed?
    boolean drawAllowed = true;
    if (GameEngine.getGameMode() == GameMode.Career)
    {
      Career career = GameEngine.getCareer();
      if (career.getCurrentMatchDayLeague() == Career.LEAGUE_ALLSTARS &&
        career.getCurrentAllstarsMatchDayNumber() >= AllstarsLeague.MATCHDAY_QUARTER)
        drawAllowed = false;
    }
    else if (GameEngine.getGameMode() == GameMode.WorldCup)
    {
      WorldCup worldCup = GameEngine.getWorldCup();
      if (!worldCup.isCurrentMatchDayGroupMatchDay())
        drawAllowed = false;
    }

    //create match
    match = new Match(nextMatchTeams, nextMatchControllers, stadium,
      nextMatchReferee, nextMatchRefereePrejudice, drawAllowed);
    
    //player focuses only visible, when team controlled by user
    for (int i = 0; i < 2; i++)
    {
      nodeActivePlayerFocus[i].setVisible(
        match.getTeam(i).getTeamControl() != TeamControl.AI);
      if (match.getTeam(i).getTeamControl() == TeamControl.Player1)
        nodeActivePlayerFocus[i].setMaterialTexture(0, textureActivePlayerFocus[0]);
      else if (match.getTeam(i).getTeamControl() == TeamControl.Player2)
        nodeActivePlayerFocus[i].setMaterialTexture(0, textureActivePlayerFocus[1]);
    }

    //set game camera
    GameEngine.activateCamera();
    camera.setMatch(match);
    
    //search for joysticks (perhaps the user added one
    //after the program start)
    GameEngine.getInput().initJoysticks();

  }


  boolean blitz = false;
  public void run()
  {
    
    physics.step(match);

    match.step();
    
    camera.update();
    
    weather.step();

  }


  /**
   * Get the PhysicsManager of this MatchManager.
   */
  public PhysicsManager getPhysicsManager()
  {
    return physics;
  }


  public OutfieldPlayerMesh getOutfieldPlayerMesh()
  {
    return meshOutfieldPlayer;
  }


  public GoalkeeperMesh getGoalkeeperMesh()
  {
    return meshGoalkeeper;
  }
  
  
  public RefereeMesh getRefereeMesh()
  {
    return meshReferee;
  }


  public Match getMatch()
  {
    return match;
  }


  public void setActivePlayerFocusPos(int teamIndex, Vector3f pos)
  {
    if (teamIndex == 0 || teamIndex == 1)
    {
      if (nodeActivePlayerFocus[teamIndex] != null)
      {
        float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
        nativePosActivePlayerFocus[teamIndex].set(pos.x * u, (pos.y + 0.02f)
          * u, pos.z * u);
        nodeActivePlayerFocus[teamIndex]
          .setPosition(nativePosActivePlayerFocus[teamIndex]);
      }
    }
  }
  
  
  /**
   * Call this method when the match is over
   * (usually done by the UIManager when
   * match screen is closed).
   */
  public void clearMatch()
  {
    removeFromScene();
    clearPhysicsManager();
    match = null;
  }
  
  
  /**
   * Remove all irrlicht objects from the used ISceneManager
   */
  public void removeFromScene()
  {
    match.removeFromScene();
    nodeActivePlayerFocus[0].remove();
    nodeActivePlayerFocus[1].remove();
    weather.removeFromScene();
  }
  
  
  /**
   * Clear the physics manager.
   */
  public void clearPhysicsManager()
  {
    physics.clear();
  }


  
  /**
   * Gets the match camera.
   */
  public Camera getCamera()
  {
    return camera;
  }
  
  
  /**
   * Call this method, when the match is over, and
   * give details about the result.
   */
  public void setMatchResult(MatchInfo matchResult)
  {
    this.matchResult = matchResult;
  }
  
  
  /**
   * Gets the result of the last played match.
   */
  public MatchInfo getMatchResult()
  {
    return matchResult;
  }
  
  
  /**
   * Starts a career or worldcup match with the given
   * teams and the given controllers
   * in the given stadium.
   */
  public void startCareerWorldCupMatch(String userTeamID, String teamHomeID,
    String teamVisitorID, String matchScreen)
  {
    MatchManager matchManager = GameEngine.getMatchManager();
    matchManager.setTeam(0, teamHomeID);
    matchManager.setTeam(1, teamVisitorID);
    int userTeamIndex =
      (teamHomeID.equals(userTeamID)) ? 0 : 1;
    matchManager.setController(userTeamIndex, "TeamAI5#Player1");
    matchManager.setController(1 - userTeamIndex, "TeamAI5#AI");
    GameEngine.getUIManager().openScreen("matchloading", null);
  }


  /**
   * Gets the current weather, or <code>null</code>.
   */
  public Weather getWeather()
  {
    return weather;
  }


  
  public String[] getNextMatchTeams()
  {
    return nextMatchTeams;
  }


  
  public String getNextMatchReferee()
  {
    if (nextMatchReferee == null || nextMatchReferee.length() == 0)
    {
      nextMatchReferee = getRandomReferee().getID();
    }
    return nextMatchReferee;
  }
  
  
  /**
   * LOM_2 patch: Shows the distance of the defense wall.
   * 
   * @author Malte Löpmann
   */
  public void showFreekickWallDistance(Vector3f pos)
  {    
    if (nodeFreekickWallDistance != null)
    {
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      vector3df newPos = new vector3df(pos.x * u, (pos.y + 0.02f) * u, pos.z * u);
      nodeFreekickWallDistance.setPosition(newPos);
      nodeFreekickWallDistance.setVisible(true);
    }    
  }
  
  
  /**
   * LOM_2 patch: Hides the distance of the defense wall.
   * 
   * @author Malte Löpmann
   */
  public void hideFreekickWallDistance()
  {    
    if (nodeFreekickWallDistance != null)
    {
      nodeFreekickWallDistance.setVisible(false);
    }    
  }
  
  
}
