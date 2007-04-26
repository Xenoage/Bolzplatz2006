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
package com.xenoage.bp2k6;

import javax.vecmath.Vector3f;
import net.sf.jirr.*;

import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.audio.javasound.JavaSoundEngine;
import com.xenoage.bp2k6.audio.lwjgl.*;
import com.xenoage.bp2k6.audio.nulldevice.*;
import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.gamemodes.events.CareerEventPool;
import com.xenoage.bp2k6.gamemodes.reports.MatchInfo;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.input.*;
import com.xenoage.bp2k6.match.MatchManager;
import com.xenoage.bp2k6.match.characters.PlayerSettings;
import com.xenoage.bp2k6.util.*;

import java.awt.SplashScreen;



/**
 * This is the game engine of Bolzplatz 2006,
 * which controls the game flow and
 * holds all important variables.
 *
 * @author   Andi
 */
public class GameEngine
{

  //name of the project
  public static final String PROJECT_NAME = "Bolzplatz 2006";
  public static final String PROJECT_NAME_VERSION = "Bolzplatz 2006 - v1.1";
  
  //operating system
  private static boolean osWindows = true;

  //irrlicht objects
  private static IrrlichtDevice irrlichtDevice = null;
  private static IVideoDriver videoDriver = null;
  private static ISceneManager sceneManager = null;
  private static IGUIEnvironment guiEnvironment = null;
  public static boolean VIDEO_MIPMAPS = true;

  //current time
  private static float currentTime = 0;
  private static float gapTime = 0;
  private static float pauseTime = 0;
  private static boolean gamePaused = false;

  //camera
  private static ICameraSceneNode camera = null;

  //ui- and match manager
  private static UIManager uiManager = null;
  private static MatchManager matchManager = null;

  //input engine
  private static Input input = null;

  //sound engine and effects
  private static SoundEngine soundEngine = null;

  //sound tracks
  private static int soundTracksCount = 10;
  private static SoundTrack soundTracks[] = new SoundTrack[soundTracksCount];
  public static int SOUNDTRACK_MUSIC = 0;
  public static int SOUNDTRACK_COMMENTARY = 1;
  public static int SOUNDTRACK_STADIUMAMBIENCE_0 = 2;
  public static int SOUNDTRACK_STADIUMAMBIENCE_1 = 3;
  public static int SOUNDTRACK_STADIUMAMBIENCE_2 = 4;
  public static int SOUNDTRACK_STADIUMAMBIENCE_3 = 5;
  public static int SOUNDTRACK_STADIUMAMBIENCE_4 = 6;
  public static int SOUNDTRACK_STADIUMAMBIENCE_5 = 7;
  public static int SOUNDTRACK_STADIUMAMBIENCE_6 = 8;
  public static int SOUNDTRACK_WEATHERAMBIENCE = 9;

  //screen settings
  private static dimension2di screenDimension = null;
  private static recti screenRect = null;
  private static int screenDepth = 0;
  private static boolean screenFullscreen = false;

  //conversion factor between game engine units and irrlicht units
  //at the moment: game engine: meter, irrlicht: centimeter
  public static final float UNITS_GAME_TO_IRRLICHT = 100f;
  public static final float UNITS_IRRLICHT_TO_GAME = 1f / UNITS_GAME_TO_IRRLICHT;

  //game engine object, only used for logging
  //(without that object "this"-keyword would not work)
  private static GameEngine thisObject = new GameEngine();
  
  //background color (used for clearing the screen)
  private static SColor backgroundColor = new SColor(255, 0, 0, 0);
  
  //game modes
  public enum GameMode
  {
    FriendlyGame,
    Career,
    WorldCup,
    Training
  }
  private static GameMode gameMode;
  private static Career career;
  private static WorldCup worldCup;
  
  //career events
  private static CareerEventPool careerEvents = null;
  
  
  //true, if the video driver is currently rendering to
  //texture, otherwise false
  private static boolean renderToTexture = false;
  
  //when the program should be closed, set this to true
  //and the other threads now that they should end
  private static boolean exit = false;


  /**
   * Initialize the game engine. Call this method at the
   * very beginning.
   */
  public static void init()
  {
    //clear the temp folder
    FileUtils.clearTempFolder();
    
    //operating system
    osWindows = System.getProperty("os.name").startsWith("Windows");

    //read selected screen resolution
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Read screen settings...");
    Resolution resolution = new Resolution();
    int resolutionIndex = -1;
    try
    {
      resolutionIndex = resolution.getSelectedVideoMode();
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_WARNINGS, thisObject,
        ex.getMessage() + " Using default video mode.");
      resolutionIndex = resolution.getDefaultVideoMode();
    }
    if (resolutionIndex > -1)
    {
      //video mode found. load it.
      int[] res = resolution.getVideoModeByIndex(resolutionIndex);
      screenDimension = new dimension2di(res[0], res[1]);
      screenDepth = res[2];
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "Using screen resolution " + res[0] + " x " +
          res[1] + " x " + res[2]);
      //read fullscreen-mode
      try
      {
        screenFullscreen = resolution.getSelectedFullscreenMode();
      }
      catch (Exception ex)
      {
        screenFullscreen = true;
        Logging.log(Logging.LEVEL_WARNINGS, thisObject,
          "Could not read fullscreen mode: " + ex.getMessage() +
          " Using default setting.");
      }
      Logging.log(Logging.LEVEL_MESSAGES, thisObject,
        "Using " + (screenFullscreen ? "fullscreen" : "windowed") + " mode.");
    }
    else
    {
      //no video mode found! (can happen on old machines?)
      //use minimal video mode
      int[] res = resolution.getMinimalVideoMode();
      screenDimension = new dimension2di(res[0], res[1]);
      screenDepth = res[2];
      screenFullscreen = false;
      Logging.log(Logging.LEVEL_WARNINGS, thisObject,
        "Could not read video mode list. Using minimal setting: " +
        res[0] + " x " + res[1] + " x " + res[2] + " in windowed mode.");
    }
    screenRect = new recti(0, 0,
      screenDimension.getWidth(), screenDimension.getHeight());
    
    //hide splash screen
    SplashScreen.getSplashScreen().close();

    //create irrlicht device
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create Irrlicht device...");
    
    irrlichtDevice = Jirr.createDevice(E_DRIVER_TYPE.EDT_OPENGL,
      screenDimension, screenDepth, screenFullscreen, false, false, null);
    if (osWindows)
      irrlichtDevice.setWindowCaption(PROJECT_NAME);
    else
      irrlichtDevice.setWindowCaption(""); //no window caption in unix (unicode problem)

    //get other irrlicht objects
    videoDriver = irrlichtDevice.getVideoDriver();
    sceneManager = irrlichtDevice.getSceneManager();
    guiEnvironment = irrlichtDevice.getGUIEnvironment();

    //use mitmaps and 32 bit textures
    TextureFactory.init(videoDriver);
    GameEngine.getVideoDriver().setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_CREATE_MIP_MAPS, VIDEO_MIPMAPS);
    GameEngine.getVideoDriver().setTextureCreationFlag(
      E_TEXTURE_CREATION_FLAG.ETCF_ALWAYS_32_BIT, true);


    //create input engine
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create input engine...");
    input = new Input(irrlichtDevice);
    //set game mode to false first, because we start with gui
    input.setGameMode(false);
    //set keys assigment
    KeysAssignment.setAssignment(input);


    //create sound engine
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create sound engine...");
    try
    {
      ConfigFile audioConfig = new ConfigFile("data/config/audio.xml");
      String device = audioConfig.getValue("device");
      if (device.equals("javasounddevice"))
        soundEngine = new JavaSoundEngine();
      else if (device.equals("lwjgldevice"))
        soundEngine = new LWJGLSoundEngine();
      else if (device.equals("nulldevice"))
        soundEngine = new NullDeviceSoundEngine();
      else
        throw new Exception("Unknown audio device \"" + device + "\"!");
    }
    catch (Exception ex)
    {
      soundEngine = new NullDeviceSoundEngine();
      Logging.log(Logging.LEVEL_ERRORS, null, "Audio config is invalid! Details:");
      Logging.log(Logging.LEVEL_ERRORS, null, ex);;
    }
    //create sound tracks
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Creating sound tracks...");
    for (int i = 0; i < soundTracksCount; i++)
    {
      soundTracks[i] = soundEngine.createSoundTrack();
      Logging.log(Logging.LEVEL_MESSAGES, null, "SoundTrack " + i + " created.");
    }
    soundTracks[SOUNDTRACK_MUSIC].setRepeatMode(true);
    //create sound effect pool
    SoundEffectPool.init();


    //create camera
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create camera...");
    camera = sceneManager.addCameraSceneNode(null, new vector3df(0,100,-100f), new vector3df(0,0,0f));
    //camera: between 1 m and 500 m
    camera.setFarValue(500f * GameEngine.UNITS_GAME_TO_IRRLICHT);
    camera.setNearValue(1f * GameEngine.UNITS_GAME_TO_IRRLICHT);
    //OBSOLETE: camera.setFOV(0.015f);

    //Load player settings
    PlayerSettings.loadSettings();

    //Create match manager
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create match manager...");
    matchManager = new MatchManager();

    //Init Fadeeffect
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Init Fadeeffect...");
    new Fade(videoDriver);

    //Create ui manager and load first screen
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Create ui manager and load start screen...");
    uiManager = new UIManager();
    uiManager.openScreenNow("mainmenu"); //"worldcuplogo"); //"start"
    
  }



  /**
   * Starts the mainloop and returns not until
   * the program is closing.
   */
  public static void mainloop()
  {
    int fps;
    
    //own thread for sound tracks
    Thread soundTracksThread = new Thread()
    {
      @Override public void run()
      {
        Logging.log(Logging.LEVEL_MESSAGES, thisObject,
          "Thread for SoundTracks started.");
        while (!exit)
        {
          //sound tracks
          try
          {
            for (int i = 0; i < soundTracksCount; i++)
              soundTracks[i].run();
            sleep(50); //20 steps/second are enough for smooth fading
          }
          catch (InterruptedException ex)
          {
          }
        }
        Logging.log(Logging.LEVEL_MESSAGES, thisObject,
          "Thread for SoundTracks ended.");
      }
    };
    soundTracksThread.start();

    float lastFrameTime = 0;
    float lastFPSLogTime = 0;

    while (irrlichtDevice.run())
    {
      //try { Thread.sleep(3000); } catch (Exception ex) { }
      
      //update time
      float newTime = irrlichtDevice.getTimer().getTime() * 0.001f;
      float deltaTime = newTime - lastFrameTime;
      
      //ignore long gaps
      if (lastFrameTime > 0)
      {
        if (deltaTime > 0.5f)
          gapTime += deltaTime;
        else
          currentTime = newTime - gapTime;
      }
      lastFrameTime = newTime;
      
      //paused? gui must go on, but match must stop
      if (gamePaused)
      {
        pauseTime += deltaTime;
      }

      //read input
      processInput();

      //run programs
      uiManager.runProgram();
      
      //draw the scene and gui
      videoDriver.beginScene(true, true, backgroundColor);
      Fade.doFade(); //draw to new rendertarget?
      sceneManager.drawAll(); //draw scene
      uiManager.run(); //draw gui
      Fade.doFade2(videoDriver); //do the Fadeeffekt
      videoDriver.endScene();

      //show current frames per second - TEST
      fps = videoDriver.getFPS();
      if (newTime - lastFPSLogTime > 1)
      {
        irrlichtDevice.setWindowCaption(PROJECT_NAME + " - FPS: " + fps);
      }
      if (newTime - lastFPSLogTime > 10)
      {
        lastFPSLogTime = newTime;
        Logging.log(Logging.LEVEL_MESSAGES, thisObject, "FPS: " + fps);
      }

    }
    exit = true;
    
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Exit mainloop. Jirr device closed.");
    
    //close soundtracks
    try
    {
      for (int i = 0; i < soundTracksCount; i++)
        soundTracks[i].close();
    }
    catch (Exception ex)
    {
    }
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "All SoundTracks closed.");
    
  }


  /**
   * Close the game engine immediately and clear all
   * ressources, if possible
   */
  public static void exit()
  {
    try
    {
      irrlichtDevice.closeDevice();
    }
    catch(Throwable t) {}
  }


  /**
   * Gets current time in seconds. The returned value
   * is updated in each frame in the mainloop.
   * @param matchTime  true for match time, false for gui time
   */
  public static float getFrameTime(boolean matchTime)
  {
    if (matchTime)
      return currentTime - pauseTime;
    else
      return currentTime;
  }


  /**
   * Gets current time in seconds. The returned value
   * is not the time of the current mainloop frame,
   * but the really measured time at the moment
   * of the call of this method.
   */
  public static float getTime(boolean matchTime)
  {
    /*
    irrlichtDevice.getTimer().tick();
    float ret = irrlichtDevice.getTimer().getTime() * 0.001f;
    ret -= gapTime;
    if (matchTime)
      return ret - pauseTime;
    else
      return ret;
    */
    return getFrameTime(matchTime);
  }
  
  
  /**
   * Gets current time in seconds. The returned value
   * is not the time of the current mainloop frame,
   * but the really measured time at the moment
   * of the call of this method.
   */
  public static float getIrrlichtTime()
  {
    irrlichtDevice.getTimer().tick();
    return irrlichtDevice.getTimer().getTime() * 0.001f;
  }



  /**
   * Gets the Irrlicht device.
   */
  public static IrrlichtDevice getIrrlichtDevice()
  {
    return irrlichtDevice;
  }


  /**
   * Gets the Irrlicht video driver.
   */
  public static IVideoDriver getVideoDriver()
  {
    return videoDriver;
  }


  /**
   * Gets the Irrlicht scene manager.
   */
  public static ISceneManager getSceneManager()
  {
    return sceneManager;
  }


  /**
   * Gets the Irrlicht gui environment.
   */
  public static IGUIEnvironment getGUIEnvironment()
  {
    return guiEnvironment;
  }


  /**
   * Gets the ui manager.
   */
  public static UIManager getUIManager()
  {
    return uiManager;
  }


  /**
   * Gets the match manager.
   */
  public static MatchManager getMatchManager()
  {
    return matchManager;
  }


  /**
   * Gets the input engine.
   */
  public static Input getInput()
  {
    return input;
  }


  /**
   * Gets the sound engine.
   */
  public static SoundEngine getSoundEngine()
  {
    return soundEngine;
  }


  /**
   * Gets one of the sound tracks.
   * @param track   the ID of the track (for example SOUNDTRACK_MUSIC)
   */
  public static SoundTrack getSoundTrack(int track)
  {
    try
    {
      return soundTracks[track];
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  
  /**
   * Activates the game engine camera.
   */
  public static void activateCamera()
  {
    sceneManager.setActiveCamera(camera);
  }


  /**
   * Sets the position of the camera.
   */
  public static void setCameraPosition(Vector3f from, Vector3f at)
  {
    CameraUtils.setCameraPosition(camera, from, at);
  }


  /**
   * Gets the dimension (width and height) of the screen.
   */
  public static dimension2di getNativeScreenDimension()
  {
    return screenDimension;
  }


  /**
   * Gets the dimension (width and height) of the screen.
   */
  public static Point2i getScreenDimension()
  {
    return new Point2i(screenDimension.getWidth(),
      screenDimension.getHeight());
  }

  
  /**
   * Gets the rect of the screen.
   */
  public static recti getNativeScreenRect()
  {
    return screenRect;
  }


  /**
   * Returns <code>true</code>, if the current screen setting
   * is fullscreen mode, otherwise <code>false</code>.
   */
  public static boolean getScreenFullscreen()
  {
    return screenFullscreen;
  }


  /**
   * Log a fatal error and close
   */
  public static void fatalError(Object sender, Exception ex)
  {
    Main.fatalError(sender, ex);
  }

  /**
   * Do the Fadeeffect in the next frame
   * @param art    type of fade (0 = random)
   * @param time   time in seconds
   * @see com.xenoage.bp2k6.util.Fade.fade()
   */
  public static void fade(int art, float time){
    Fade.fade(art,time);
  }


  /**
   * Processes the user's input.
   */
  private static void processInput()
  {
    input.update();
    while (!input.noEvents())
    {
      InputEvent event = input.getNextEvent(true);
      uiManager.input(event);
    }
  }
  
  
  /**
   * Gets the camera of the game engine.
   */
  public static ICameraSceneNode getCamera()
  {
    return camera;
  }
  
  
  /**
   * Sets the background color (used for
   * clearing the screen)
   */
  public static void setBackgroundColor(SColor col)
  {
    backgroundColor = col;
  }

  
  /**
   * Sets the current gamemode by a string:
   * "friendlygame", "career", "worldcup" or "training"
   */
  public static void setGameMode(String newGameMode)
  {
    if (newGameMode.equals("friendlygame"))
      gameMode = GameMode.FriendlyGame;
    else if (newGameMode.equals("career"))
      gameMode = GameMode.Career;
    else if (newGameMode.equals("worldcup"))
      gameMode = GameMode.WorldCup;
    else if (newGameMode.equals("training"))
      gameMode = GameMode.Training;
    else
    {
      fatalError(thisObject, new Exception(
        "Unknown gamemode: \"" + newGameMode + "\""));
    }
    setGameMode(gameMode);
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Gamemode set to \"" + gameMode + "\"");
  }
  
  
  /**
   * Sets the current gamemode.
   */
  public static void setGameMode(GameMode newGameMode)
  {
    gameMode = newGameMode;
    if (gameMode == GameMode.Career)
    {
      //load career events, if not done already
      if (careerEvents == null)
        careerEvents = new CareerEventPool();
    }
  }
  
  
  /**
   * Gets the current gamemode.
   */
  public static GameMode getGameMode()
  {
    return gameMode;
  }
  
  
  /**
   * Gets the current career, if gamemode is career.
   * Otherwise <code>null</code> is returned.
   */
  public static Career getCareer()
  {
    if (gameMode == GameMode.Career)
      return career;
    else
      return null;
  }
  
  
  /**
   * Gets the current worldcup, if gamemode is worldcup.
   * Otherwise <code>null</code> is returned.
   */
  public static WorldCup getWorldCup()
  {
    if (gameMode == GameMode.WorldCup)
      return worldCup;
    else
      return null;
  }
  
  
  /**
   * Starts a new career with the given user-team.
   * The gamemode is immediately changed to career.
   */
  public static void startCareer(String userTeamID)
  {
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Starting new career with userteam \"" + userTeamID + "\"");
    setGameMode(GameMode.Career);
    Settings.setGamemodeGameNotSafed(true);
    worldCup = null;
    career = new Career(userTeamID, 2006);
  }
  
  
  /**
   * Loads the career of the given slot.
   * The gamemode is immediately changed to career.
   */
  public static void loadCareer(int slot)
  {
    worldCup = null;
    try
    {
      career = new Career(slot);
      setGameMode(GameMode.Career);
      Settings.setGamemodeGameNotSafed(true);
      uiManager.openScreen("career", null);
    }
    catch (TeamNotFoundException ex)
    {
      uiManager.getCurrentScreen().activateGroup("teamnotfound");
    }
  }
  
  
  /**
   * Starts a new worldcup with the given user-team.
   * The gamemode is immediately changed to worldcup.
   */
  public static void startWorldCup(String userTeamID)
  {
    Logging.log(Logging.LEVEL_MESSAGES, thisObject,
      "Starting new worldcup with userteam \"" + userTeamID + "\"");
    setGameMode(GameMode.WorldCup);
    Settings.setGamemodeGameNotSafed(true);
    career = null;
    worldCup = new WorldCup(userTeamID);
  }
  
  
  /**
   * Loads the worldcup of the given slot.
   * The gamemode is immediately changed to worldcup.
   */
  public static void loadWorldCup(int slot)
  {
    career = null;
    try
    {
      worldCup = new WorldCup(slot);
      setGameMode(GameMode.WorldCup);
      Settings.setGamemodeGameNotSafed(true);
      uiManager.openScreen("worldcup", null);
    }
    catch (TeamNotFoundException ex)
    {
      uiManager.getCurrentScreen().activateGroup("teamnotfound");
    }
  }
  
  
  /**
   * Call this method when a match is over. The GameEngine
   * decides how to proceed.
   */
  public static void matchOver(MatchInfo matchResult)
  {
    if (gameMode == GameMode.FriendlyGame)
    {
      //open the mainmenu
      uiManager.openScreen("mainmenu", "startnologo");
    }
    else if (gameMode == GameMode.Career)
    {
      //tell the career that the user's match is over
      career.matchOver(matchResult);
    }
    else if (gameMode == GameMode.WorldCup)
    {
      //tell the worldcup that the user's match is over
      worldCup.matchOver(matchResult);
    }
  }


  /**
   * Returns <code>true</code> when the video driver
   * is currently rendering to a texture, otherwise
   * <code>false</code>.
   */
  public static boolean isRenderingToTexture()
  {
    return renderToTexture;
  }


  /**
   * Sets the given texture as a render target. If
   * the screen should be used again for rendering,
   * call it with the parameter <code>null</code>.
   */
  public static void setRenderTarget(ITexture renderTarget)
  {
    videoDriver.setRenderTarget(renderTarget);
    renderToTexture = (renderTarget != null);
  }
  
  
  /**
   * Gets the career event pool.
   */
  public static CareerEventPool getCareerEventPool()
  {
    return careerEvents;
  }
  
  
  /**
   * Sets the game to pause (true) or
   * continue (false).
   */
  public static void setPause(boolean pause)
  {
    gamePaused = pause;
  }
  
}
