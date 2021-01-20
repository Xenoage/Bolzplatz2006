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

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.GameEngine.GameMode;
import com.xenoage.bp2k6.audio.*;
import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.gamemodes.WorldCup;
import com.xenoage.bp2k6.gamemodes.reports.Goal;
import com.xenoage.bp2k6.gamemodes.reports.MatchInfo;
import com.xenoage.bp2k6.gui.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.match.Commentator.CommentatorEvent;
import com.xenoage.bp2k6.match.camera.CameraView;
import com.xenoage.bp2k6.match.camera.Flight;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.match.referee.*;
import com.xenoage.bp2k6.match.referee.decisions.*;
import com.xenoage.bp2k6.match.stadium.*;
import com.xenoage.bp2k6.match.stadium.crowd.Crowd;
import com.xenoage.bp2k6.match.stadium.crowd.CrowdMood.CrowdMoodEvent;
import com.xenoage.bp2k6.match.teamai.*;
import com.xenoage.bp2k6.match.weather.Weather;

import java.awt.Color;

import javax.vecmath.Vector3f;

import net.sf.jirr.ITexture;


/**
 * This class contains information about
 * the running match (score, time, ...)
 * 
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM, LOM_2)
 */
public class Match
{
  //parent MatchManager
  private MatchManager matchManager;
  
  //the stadium of the match
  private Stadium stadium;
  
  //the ball
  private Ball ball;

  //the two teams
  private Team[] teams = new Team[2];
  
  //details about the match result
  MatchInfo matchResult;

  //current score (team1 : team2)
  private int[] score = new int[2];
  
  //the referee
  private Referee referee;

  //commentator of this match
  private Commentator commentator;
  private float nextIdleCommentTime = 0;
  
  //the crowd (audience)
  private Crowd crowd;
  private boolean minute80EventFired = false;

  //time factor (realtime / gametime),
  //e.g. 18f for an about 5 min game
  private float timeFactor = 45;
  //current game time in seconds (60 * (1-45, 46-90, 90-105, 106-130))
  private float gameTime = 0;
  
  //draw allowed, or overtime?
  private boolean drawAllowed;
  
  //current time phase (1: first half, 2: second half,
  public enum MatchPhase
  {
    BeforeGame, FirstHalf, HalfTime, SecondHalf, TimeUpRegularTime,
    TimeUpBeforeOvertime, OverTimeFirstHalf, OverTimeHalfTime,
    OverTimeSecondHalf, TimeUpOverTime, TimeUpBeforePenaltyShootout,
    PenaltyShootout, PenaltyShootoutFinished;
  }
  private MatchPhase matchPhase = MatchPhase.BeforeGame;

  //match status
  public enum MatchStatus
  {
    RUNNING, //match is running
    KICKOFF_TIME, //time before kickoff before the game and after the halfime break
    KICKOFF_GOAL, //time before kickoff after a goal
    HALFTIME, //time after the halftime whistle
    FINALWHISTLE, //time after the final whistle (time up)
    THROWIN, //throw in
    CORNERKICK, //corner kick
    GOALKICK, //goalkeeper has goal kick
    GOALEMOTIONS, //emotions after a goal
    PENALTYKICK, //penalty kick
    FREEKICK, //free kick
    BEFOREPENALTYSHOOTOUT, //time before penalty shootout
    PENALTYKICK_PENALTYSHOOTOUT; //penalty kick while penatly shootout
  }
  private MatchStatus matchStatus;
  
  //side changed? value is 1 or -1.
  //1 means: team[0] is left, -1 means: team[1] is left
  private int sideChanged = 1;
  
  //last time step() was called
  private float lastStepTime = 0;
  //last status change time
  private float matchStatusChangeTime = 0;
  //number of already sent events to commentator during the current match status
  private int lastStatusChangeCommented = 0;
  //active team ready to perform the match status action?
  //(like kickoff, free kick, corner kick, ...)
  //must be set by the TeamAI by calling setMatchStatusActionReady()
  private boolean matchStatusActionReady = false;
  //time when action was declared to be ready
  private float matchStatusActionReadyTime;
  //ball position already set after status change?
  boolean matchStatusBallPosSet = false;


  //team that will kick off (changes after goal, half-time, ...)
  private Team kickoffTeam;
  private int startKickoffTeam = 0;
  
  //goal team and goal player
  //the team is the team that scored the goal,
  //the player may also be from the other team if it was an own goal
  //keeper touch is no own goal... remember (in class Ball) the last
  //"real" ball posessor _and_ the last ball "toucher" (needed for
  //throw in, goal kick, corner kick, ...) - not implemented at the moment!
  private int goalTeam;
  private Player goalPlayer;
  //goal given?
  boolean goalGiven = false;
  //faded out after goal?
  private boolean goalFadedOut = false;
  //goal jingle
  private boolean goalJingleStarted = false;
  private SoundEffect goalJingle = null;

  //throw-in position and team
  private Vector3f throwinPos;
  private int throwinTeam;

  //corner kick or goal kick data
  private int cornergoalKickTeam; //index of the team that performs the kick
  private int cornergoalKickCorner; //1: from the right corner, -1, from the left corner
  //private Vector3f cornergoalBallOutPos; //position where ball crossed goal line
  
  //penalty kick
  private int penaltyKickTeam;
  private Card penaltyKickCard;
  
  //free kick
  private int freeKickTeam;
  private Vector3f freeKickPos;
  private Card freeKickCard;
  private boolean freeKickComment;
  
  //energy bar (for shoot power)
  private EnergyBar energyBar = null;
  
  //penalty shootout
  private int penaltyShootoutActiveTeam = 0;
  private int penaltyShootoutShotsCount = 0;
  private int penaltyShootoutScore[] = new int[2];
  private int penaltyShootoutGoal[][] = new int[2][5]; //1: goal, 2: miss, 0: not yet shot
  private boolean penaltyShootoutShot = false; //current shoot already shot?
  private float penaltyShootoutShotTime; //start time of the current shot
  
  //foul not given
  private boolean noFoulWaitingForComment = false;
  private float noFoulCommentTime = 0;
  
  //particle system for trash and so on
  private SimpleParticleSystem particleSystem = null;
  private float lastParticleTime = 0;
  
  //LOM patch:
  //maybe repeat goalkick or freekick and handle delayOfGame
  private boolean goalKickToRepeat = false;   
  private boolean freeKickToRepeat = false;
  private boolean disableFreeKickComment = false;
  private float waitForRepeatTime = 0;
  private Card delayOfGameCard;
  private boolean delayOfGameCommented = false;
  
  
  /**
   * Constructor: Create a match.
   */
  public Match(String teamIDs[], String controllers[], Stadium stadium,
    String refereeID, int refereePrejudice, boolean drawAllowed)
  {
    //match settings
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Loading match settings...");
    try
    {
      ConfigFile matchConfig = new ConfigFile("data/config/match.xml");
      timeFactor = matchConfig.getValueFloat("timefactor");
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Time factor: " + timeFactor);
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Error while loading match settings:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
    }
    
    //draw or overtime?
    this.drawAllowed = drawAllowed;
    
    //stadium
    this.stadium = stadium;

    //load the two teams
    for (int i = 0; i < 2; i++)
    {
      //load team
      Logging.log(Logging.LEVEL_MESSAGES, this, "Loading " + (i + 1) +
        ". team: \"" + teamIDs[i] + "\".");
      teams[i] = new Team(teamIDs[i], this, (i == 1),
        (i == 1 ? teams[0].getTeamInfo().getPlayerColorsHome() : null));

      //load teamAI
      try
      {
        //find TeamAI
        String sTeamAI[] = controllers[i].split("#");
        Class classDefinition = Class.forName(
          "com.xenoage.bp2k6.match.teamai." + sTeamAI[0]);
        TeamAI teamAI = (TeamAI) classDefinition.newInstance();
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Loaded TeamAI \"" + sTeamAI[0] + "\".");
        //find controller (AI, Player1, Player2)
        TeamControl teamControl = TeamControl.AI;
        if (sTeamAI.length > 1)
        {
          if (sTeamAI[1].equals("AI"))
          {
            teamControl = TeamControl.AI;
            Logging.log(Logging.LEVEL_MESSAGES, this, "Set team controller \"AI\".");
          }
          else if (sTeamAI[1].equals("Player1"))
          {
            teamControl = TeamControl.Player1;
            Logging.log(Logging.LEVEL_MESSAGES, this, "Set team controller \"Player1\".");
          }
          else if (sTeamAI[1].equals("Player2"))
          {
            teamControl = TeamControl.Player2;
            Logging.log(Logging.LEVEL_MESSAGES, this, "Set team controller \"Player2\".");
          }
          else
            Logging.log(Logging.LEVEL_WARNINGS, this,
                        "Unknown team controller: \"" + sTeamAI[1] + "\". Using AI.");
        }
        //set TeamAI and controller
        teams[i].setTeamAI(teamAI, teamControl);
      }
      catch (Exception ex)
      {
        Logging.log(Logging.LEVEL_ERRORS, this,
          "Could not load TeamAI \"" + controllers[i] + "\". Details:");
        GameEngine.fatalError(this, ex);
      }
    }

    matchManager = GameEngine.getMatchManager();

    teams[0].setCurrentSide(-1);
    teams[1].setCurrentSide(1);
    kickoffTeam = teams[startKickoffTeam];
    
    //creates the ball
    ball = new Ball(matchManager);
    
    //creates the referee
    referee = new Referee(refereeID, 
      matchManager.getRefereeMesh(), GameEngine.getSceneManager(),
      matchManager.getPhysicsManager(), this);
    referee.getAI().setMatch(this);
    referee.getInfo().setPrejudice(refereePrejudice);
    
    //set referee to stadium entrace position
    Vector3f entrance = new Vector3f(
      stadium.getEntrancePosition());
    referee.setPosition(entrance);
    
    //set players 2 m behind stadium entrance position
    entrance.z += 2;
    entrance.x -= 1;
    teams[0].setPlayersInZRow(entrance);
    entrance.x += 2;
    teams[1].setPlayersInZRow(entrance);
    
    //match result
    matchResult = new MatchInfo(teams[0].getTeamInfo().getID(),
      teams[1].getTeamInfo().getID(), true);
    
    //attendance of the spectators
    Weather weather = matchManager.getWeather();
    float attendance = 0;
    if (GameEngine.getGameMode() == GameMode.FriendlyGame)
    {
      attendance = MathTools.clamp(
        MathTools.random(0.4f) +
          teams[0].getTeamInfo().getAverageAll() / 100f * 4f +
          teams[1].getTeamInfo().getAverageAll() / 100f * 4f,
        0, 1);
    }
    else if (GameEngine.getGameMode() == GameMode.WorldCup)
    {
      attendance = 1;
    }
    else if (GameEngine.getGameMode() == GameMode.Career)
    {
      Career career = GameEngine.getCareer();
      if (career.getCurrentMatchDayLeague() == Career.LEAGUE_3)
        attendance = 0.3f + MathTools.random(0.3f);
      else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_2)
        attendance = 0.5f + MathTools.random(0.3f);
      else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_1)
        attendance = 0.7f + MathTools.random(0.3f);
      else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_ALLSTARS)
        attendance = 1;
    }
    Logging.log(Logging.LEVEL_MESSAGES, "Base attendance: " +
      (int) (attendance * 100) + "%");
    attendance *= stadium.getField().getLawn().crowdAttendance; //lawn
    attendance *= stadium.getStadiumInfo().getGoals().crowdAttendance; //goals
    attendance *= weather.getCrowdFactor();
    attendance *= stadium.getLightingCrowdAttendance(weather.isStadiumLighting());
    attendance *= stadium.getAttractionsCrowdAttendance();
    attendance *= stadium.getScoreboardCrowdAttendance();
    Logging.log(Logging.LEVEL_MESSAGES, "Attendance after all factors: " +
      (int) (attendance * 100) + "%");
    
    //team that is supported by the crowd
    int supportedTeam = 0;
    GameMode gameMode = GameEngine.getGameMode();
    if (gameMode == GameMode.WorldCup)
    {
      //worldcup: always support user's team
      if (GameEngine.getWorldCup().getUserTeamID().equals(teamIDs[1]))
        supportedTeam = 1;
    }
    
    //create the crowd
    Color homeTeamShirtColor =
      teams[0].getTeamInfo().getPlayerColorsHome().getColor(PlayerColors.COLOR_SHIRT);
    Color homeTeamPantsColor =
      teams[0].getTeamInfo().getPlayerColorsHome().getColor(PlayerColors.COLOR_PANTS);
    crowd = stadium.createCrowd(attendance, supportedTeam,
      homeTeamShirtColor, homeTeamPantsColor, false);
    
    //creates a game mode dependent commentator
    commentator = new Commentator(
      Commentator.getGameModeCommentator(), crowd.getSpectatorsCount());
    
    //use the home team's adboards
    String adboards = teams[0].getTeamInfo().getAdboardsFilename();
    stadium.setAdboardsTexture(adboards,
      teams[0].getTeamInfo().getPlayerColorsHome().getColor(PlayerColors.COLOR_SHIRT));
    
    //career: user gets money (entrance fee) when game is in home stadium
    if (gameMode == GameMode.Career)
    {
      Career career = GameEngine.getCareer();
      if (teamIDs[0].equals(career.getUserTeamID()))
      {
        career.setUserMoney(career.getUserMoney() + crowd.getSpectatorsCount());
      }
    }
    
    //if it is a career or worldcup match, use mats
    if (gameMode == GameMode.Career || gameMode == GameMode.WorldCup)
    {
      //use center map
      String centerMat = null;
      int league = -1;
      if (gameMode == GameMode.Career)
      {
        Career career = GameEngine.getCareer();
        league = career.getCurrentMatchDayLeague();
        if (league == Career.LEAGUE_ALLSTARS)
          centerMat = "allstars";
        else if (league == Career.LEAGUE_1)
          centerMat = "league1";
        else if (league == Career.LEAGUE_2)
          centerMat = "league2";
        else
          centerMat = "league3";
      }
      else //world cup
      {
        if (MathTools.random() < 0.5f)
          centerMat = "worldcup1";
        else
          centerMat = "worldcup2";
      }
      //use team maps for worldcup and league 1 or allstar teams
      String teamMats[] = new String[2];
      if (gameMode == GameMode.WorldCup ||
        league == Career.LEAGUE_1 || league == Career.LEAGUE_ALLSTARS)
      {
        teamMats[0] = teams[0].getTeamInfo().getID();
        teamMats[1] = teams[1].getTeamInfo().getID();
      }
      stadium.getField().setMats(centerMat, teamMats[0], teamMats[1]);
    }
    
    //activate floodlights, when it is dark
    if (weather.isStadiumLighting())
    {
      stadium.setFloodlightsOn();
      weather.activateStadiumLighting(stadium.getLightValue());
    }
    
    //weather ambience
    weather.createAmbienceSound(crowd.getSpectatorsCount());
    
    //career match: apply team value modifications
    if (gameMode == GameMode.Career)
    {
      GameEngine.getCareer().applyUserTeamValues(teams[0].getTeamInfo());
      GameEngine.getCareer().applyUserTeamValues(teams[1].getTeamInfo());
    }
    
  }

  /**
   * This method should be called as often as possible
   * (every mainloop frame).
   */
  public void step()
  {
    if (lastStepTime == 0)
    {
      //this method is called the first time
      activePlayerChanged(teams[0].getActivePlayer());
      activePlayerChanged(teams[1].getActivePlayer());
      //find energy bar
      energyBar = (EnergyBar)
        GameEngine.getUIManager().getCurrentScreen().findControl("match.energybar");
      //team logos
      updateTeamLogoDisplay();
      //camera
      matchManager.getCamera().setCameraView(CameraView.Match);
      
      //debug: immediate penalty shootout
      boolean penaltyShootout = ConfigFile.getValueFromFileBoolean(
        "data/config/debug.xml", "penaltyshootout");
      if (penaltyShootout)
      {
        drawAllowed = false;
        matchPhase = MatchPhase.OverTimeSecondHalf;
        timeFactor = 10000;
      }
    }
    else if (lastStepTime > 0 && matchStatus == MatchStatus.RUNNING &&
      matchPhase != MatchPhase.PenaltyShootout)
    {
      float deltaTime = GameEngine.getFrameTime(true) - lastStepTime;
      gameTime += deltaTime * timeFactor;
      updateTimeDisplay();
    }
    lastStepTime = GameEngine.getFrameTime(true);

    if (matchStatusChangeTime == 0)
    {
      //LOM patch
      playEntranceJingle();
      
      //step() is called the first time
      setMatchStatus(MatchStatus.KICKOFF_TIME);
      
      //greeting comment
      CommentatorEvent comment = CommentatorEvent.Greeting;
      GameMode gameMode = GameEngine.getGameMode();
      if (gameMode == GameMode.Career)
      {
        int league = GameEngine.getCareer().getCurrentMatchDayLeague();
        if (league == Career.LEAGUE_1)
          comment = CommentatorEvent.GreetingFirstLeague;
        else if (league == Career.LEAGUE_ALLSTARS)
          comment = CommentatorEvent.GreetingAllstarsLeague;
      }
      else if (gameMode == GameMode.WorldCup)
      {
        int matchDay = GameEngine.getWorldCup().getMatchDayNumber();
        if (matchDay <= WorldCup.getGroupMatchDays())
          comment = CommentatorEvent.GreetingWorldCupGroup;
        else if (matchDay == WorldCup.getGroupMatchDays() + 1)
          comment = CommentatorEvent.GreetingWorldCupEighthFinal;
        else if (matchDay == WorldCup.getGroupMatchDays() + 2)
          comment = CommentatorEvent.GreetingWorldCupQuarterFinal;
        else if (matchDay == WorldCup.getGroupMatchDays() + 3)
          comment = CommentatorEvent.GreetingWorldCupSemiFinal;
        else if (matchDay == WorldCup.getGroupMatchDays() + 4)
          comment = CommentatorEvent.GreetingWorldCupThirdPlaceMatch;
        else if (matchDay == WorldCup.getGroupMatchDays() + 5)
          comment = CommentatorEvent.GreetingWorldCupFinal;
      }
      commentatorEvent(comment);
    }
    else
    {
      //hide energy bar
      energyBar.setVisible(false);
      //move the two teams
      teams[0].step();
      teams[1].step();
      //move the referee
      referee.step();
      //animate the crowd
      crowd.step(this);
      float t = GameEngine.getFrameTime(true);
      //is it time to change the status?
      if (matchStatus == MatchStatus.RUNNING)
      {
        
        //LOM patch: maybe repeat the goalkick
        if(goalKickToRepeat)
        {
          //wait 1s
          if(lastStepTime - waitForRepeatTime > 1.0f)
          {
            waitForRepeatTime = 0;
            this.setMatchStatus(MatchStatus.GOALKICK);
            goalKickToRepeat = false;
          }                 
        }
        else if(freeKickToRepeat)
        {
          //wait 1s
          if(lastStepTime - waitForRepeatTime > 1.0f)
          {
            waitForRepeatTime = 0;
            this.setMatchStatus(MatchStatus.FREEKICK);
            freeKickToRepeat = false;
          }                 
        }
        else
        {
        
          //waiting for no-foul-comment?
          if (noFoulWaitingForComment &&
            lastStepTime > noFoulCommentTime)
          {
            //play no-foul-comment now
            noFoulWaitingForComment = false;
            commentatorEvent(CommentatorEvent.FoulNoFoul);
          }
          
          //idle comment?
          if (nextIdleCommentTime > 0 &&
            nextIdleCommentTime < lastStepTime)
          {
            commentatorEvent(CommentatorEvent.Idle);
            crowd.getMood().reportEvent(CrowdMoodEvent.Boredom, 0, score);
          }
          
          //80th minute?
          if (!minute80EventFired && gameTime > 80 * 60)
          {
            crowd.getMood().reportEvent(CrowdMoodEvent.Minute80, 0, score);
            minute80EventFired = true;
          }
          
          //ball in special position? (goal, throw-in, ...)
          Vector3f p = ball.getPosition();
          float lengthHalf = stadium.getField().getLength() / 2;
          float widthHalf = stadium.getField().getWidth() / 2;
          float xAbs = Math.abs(p.x);
          float zAbs = Math.abs(p.z);
          
          
          if (matchPhase == MatchPhase.PenaltyShootout)
          {
            //special situation: penalty shootout.
            //only two possibilities: ball in goal or not in goal
            if (penaltyShootoutShot &&
              xAbs > lengthHalf && xAbs < lengthHalf + 2 &&
              p.y < 1.8f && p.z > -2 && p.z < 2)
            {
              //goal!
              Logging.log(Logging.LEVEL_MESSAGES, this,
                "Penalty shootout: goal at time " + lastStepTime);
              commentator.newEvent(CommentatorEvent.GoalPenaltyShootout);
              changeScore(penaltyShootoutActiveTeam, 1);
              penaltyShootoutScore[penaltyShootoutActiveTeam]++;
              Goal goal = new Goal(penaltyShootoutActiveTeam, 0, 0,
                Goal.TIMEPHASE_PENALTYSHOOTOUT, 0);
              matchResult.addGoal(goal);
              penaltyShootoutGoal[penaltyShootoutActiveTeam]
                                  [(penaltyShootoutShotsCount / 2) % 5] = 1; //goal
              nextPenaltyShootoutPenaltyKick();
            }
            else if (penaltyShootoutShot &&
              lastStepTime - penaltyShootoutShotTime > 1)
            {
              //time up - sorry, no goal.
              Logging.log(Logging.LEVEL_MESSAGES, this,
                "Penalty shootout: shoot time up at " + lastStepTime);
              penaltyShootoutGoal[penaltyShootoutActiveTeam]
                                  [(penaltyShootoutShotsCount / 2) % 5] = 2; //miss
              nextPenaltyShootoutPenaltyKick();
            }
          }
          else
          {
            
            //normal match phase
            if (xAbs > lengthHalf && xAbs < lengthHalf + 2 &&
              p.y < 1.8f && p.z > -2 && p.z < 2)
            {
              //goal!
              
              goalTeam = (p.x * sideChanged < 0) ? 1 : 0;
              goalGiven = referee.getAI().goal(goalTeam);
    
              setMatchStatus(MatchStatus.GOALEMOTIONS);
              
              goalJingleStarted = false;
              
              goalFadedOut = false;
              int oldScore[] = new int[2];
              oldScore[0] = score[0];
              oldScore[1] = score[1];
              goalPlayer = ball.getLastPlayer();
              
              kickoffTeam = teams[1 - goalTeam];
              
              changeScore(goalTeam, 1); //if not given, subtract again later
              if (goalGiven)
              {
                //add to match result
                Goal goal = new Goal(goalTeam, 0, 0,
                  Goal.TIMEPHASE_FIRSTHALF, 0);
                matchResult.addGoal(goal);
              }
              
              if (!teams[goalTeam].isPlayerInTeam(ball.getLastPlayer()))
              {
                //own goal
                commentatorEvent(Commentator.CommentatorEvent.GoalOwnGoal);
              }
              else if (Math.abs(score[0] - score[1]) > 2)
              {
                //clear lead goal, but if lose team shoots is,
                //use normal comment
                if ((score[0] > score[1] && score[0] > oldScore[0]) ||
                  (score[1] > score[0] && score[1] > oldScore[1]))
                  commentatorEvent(Commentator.CommentatorEvent.GoalClearLead);
                else
                  commentatorEvent(Commentator.CommentatorEvent.GoalOther);
              }
              else if (Math.abs(score[0] - score[1]) == 0)
                commentatorEvent(Commentator.CommentatorEvent.GoalEqualize);
              else if (Math.abs(score[0] - score[1]) == 1)
              {
                //lead or catch-up goal?
                if (oldScore[0] == oldScore[1])
                  commentatorEvent(Commentator.CommentatorEvent.GoalLead);
                else
                  commentatorEvent(Commentator.CommentatorEvent.GoalCatchUp);
              }
              else
              {
                commentatorEvent(Commentator.CommentatorEvent.GoalOther);
              }
              
              //show goal flash text
              GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextgoal");
              
              if (goalGiven)
              {
                //referee whistle
                SoundEffectPool.Sound.Whistle2Short1Long.play();
              }
  
              crowd.getMood().reportEvent(CrowdMoodEvent.Goal, goalTeam, score);
    
            }
            else if (zAbs > widthHalf)
            {
              //throw in!
              setMatchStatus(MatchStatus.THROWIN);
              
              throwinPos = new Vector3f(p);
              if (throwinPos.z > widthHalf)
                throwinPos.z = widthHalf - 0.1f;
              else
                throwinPos.z = -widthHalf + 0.1f;
              throwinPos.y = 0;
              
              Player lastBallPlayer = ball.getLastPlayer();
              int teamIndex = 0;
              if (lastBallPlayer != null)
                teamIndex = (lastBallPlayer.getTeam() == teams[0] ? 0 : 1);
              throwinTeam = (teamIndex + 1) % 2; //the other team
    
              //referee whistle
              SoundEffectPool.Sound.WhistleShort.play();
            }
            else if (xAbs > lengthHalf ||
              //error correction: when ball lies on the net, but
              //goal line was not reached
              (ball.getVelocity().lengthSquared() < 0.01f && p.y > 1.5f))
            {
              //goal kick or corner kick
              Player lastBallPlayer = ball.getLastPlayer();
              int teamIndex = lastBallPlayer.getTeam() == teams[0] ? 0 : 1;
              //the last player that touched the ball was from teams[teamIndex]
              cornergoalKickTeam = (teamIndex + 1) % 2; //the other team
              if (teams[teamIndex].getCurrentSide() == -1)
              {
                if (p.x > 0)
                  setMatchStatus(MatchStatus.GOALKICK);
                else
                  setMatchStatus(MatchStatus.CORNERKICK);
                cornergoalKickCorner = p.z > 0 ? 1 : -1;
              }
              else //teams[teamIndex].getCurrentSide() == 1
              {
                if (p.x > 0)
                  setMatchStatus(MatchStatus.CORNERKICK);
                else
                  setMatchStatus(MatchStatus.GOALKICK);
                cornergoalKickCorner = p.z > 0 ? -1 : 1;
              }
              //referee whistle
              SoundEffectPool.Sound.WhistleLong.play();
              //when new status is goal kick, this was perhaps
              //a goal shoot. if the goal was missed with a narrow margin
              //and enough velocity, comment the event
              if (matchStatus == MatchStatus.GOALKICK && Math.abs(p.z) < 4 &&
                (ball.getVelocity().length() > 8) || (Math.abs(p.y) > 1.8f))
                commentatorEvent(Commentator.CommentatorEvent.ShootMiss);
            }
          }
        }
      }
      else if (matchStatus == MatchStatus.KICKOFF_TIME ||
        matchStatus == MatchStatus.KICKOFF_GOAL)
      {
        if (t - matchStatusChangeTime > 5 &&
          matchStatusActionReady &&
          t - matchStatusActionReadyTime > 0.5f)
        {
          //ready for kickoff
          matchStatusActionReady = false;
          referee.getAI().setReadyForKickoff();
          stadium.getField().removeCenterMat();
        }
        else
        {
          float centerMatOpacity = 1 - (t - (matchStatusChangeTime + 8)) / 2;
          centerMatOpacity = MathTools.clamp(centerMatOpacity, 0, 1);
          //System.out.println(centerMatOpacity);
          stadium.getField().setCenterMatOpacity(centerMatOpacity);
        }
      }
      else if (matchStatus == MatchStatus.GOALEMOTIONS)
      {
        if (t - matchStatusChangeTime > 5f)
        {
          if (goalGiven)
          {
            //kickoff
            setMatchStatus(MatchStatus.KICKOFF_GOAL);
          }
          else
          {
            //goal not given!
            changeScore(goalTeam, -1);
            cornergoalKickTeam = (1 - goalTeam);
            setMatchStatus(MatchStatus.GOALKICK);
            commentator.newEvent(CommentatorEvent.GoalNotGiven);
            crowd.getMood().reportEvent(CrowdMoodEvent.GoalNotGiven, goalTeam, score);
          }
        }
        else if (t - matchStatusChangeTime > 3f && !goalFadedOut)
        {
          //set ball to kickoff point
          ball.setVelocity(new Vector3f(0, 0, 0));
          ball.setPosition(new Vector3f(0, ball.getMinYPosition(), 0));
          //fade out
          goalFadedOut = true;
          Fade.fade(0, 3);
          //stop jingle if goal not given
          if (!goalGiven && goalJingle != null)
            goalJingle.stop();
        }
        else if (t - matchStatusChangeTime > 1f && !goalJingleStarted)
        {
          //home team? then play jingle
          if (goalTeam == 0)
          {
            int jingleType;
            GameMode gameMode = GameEngine.getGameMode();
            if (gameMode == GameMode.WorldCup)
            {
              jingleType = SoundEffectPool.JINGLE_GOAL_LEAGUE1;
            }
            else if (gameMode == GameMode.Career)
            {
              int matchDayLeague = GameEngine.getCareer().getCurrentMatchDayLeague();
              if (matchDayLeague == Career.LEAGUE_3)
                jingleType = SoundEffectPool.JINGLE_GOAL_LEAGUE3;
              else if (matchDayLeague == Career.LEAGUE_2)
                jingleType = SoundEffectPool.JINGLE_GOAL_LEAGUE2;
              else
                jingleType = SoundEffectPool.JINGLE_GOAL_LEAGUE1;
            }
            else
            {
              jingleType = SoundEffectPool.JINGLE_GOAL_RANDOM;
            }
            goalJingle = SoundEffectPool.getRandomJingle(jingleType);
            if (goalJingle != null)
              goalJingle.play();
          }
          goalJingleStarted = true;
        }
      }
      else if (matchStatus == MatchStatus.THROWIN)
      {
        /*
        //throw in
        Ball ball = matchManager.getBall();
        if (t - lastStatusChangeTime > 2 && matchStatusActionReady)
        {
          setMatchStatus(MatchStatus.RUNNING);
        }
        else if (t - lastStatusChangeTime > 1 && !matchStatusBallPosSet &&
          ball.getPosition().x < 1f)
        {
          ball.setVelocity(new Vector3f());
          ball.setPosition(new Vector3f(throwinPos.x, ball.getMinYPosition(), throwinPos.z));
          matchStatusBallPosSet = true;
        } */
        //throw in
        if (matchStatusActionReady)
        {
          setMatchStatus(MatchStatus.RUNNING);
        }
      }
      
      else if (matchStatus == MatchStatus.GOALKICK)
      {
        //goal kick
        if (matchStatusActionReady)
        {
          
          //LOM patch: check if there are opponent players in penalty area          
          if (!referee.getAI().proceedGoalkick(false))
          {
            //repeat goalkick
            goalKickToRepeat = true;
            waitForRepeatTime = lastStepTime;
            this.commentDelayOfGame();
          }
          
          setMatchStatus(MatchStatus.RUNNING);
        }
      }
      
      else if (matchStatus == MatchStatus.CORNERKICK)
      {
        //corner kick
        if (matchStatusActionReady)
        {
          setMatchStatus(MatchStatus.RUNNING);
        }
        if (t - matchStatusChangeTime > 0.5f &&
          lastStatusChangeCommented == 0)
        {
          commentatorEvent(Commentator.CommentatorEvent.CornerKick);
          lastStatusChangeCommented = 1;
        }
      }
      
      
      else if (matchStatus == MatchStatus.PENALTYKICK)
      {
        //penalty kick
        if (matchStatusActionReady)
        {
          setMatchStatus(MatchStatus.RUNNING);
        }
        if (t - matchStatusChangeTime > 2 &&
          lastStatusChangeCommented == 1)
        {
          if (penaltyKickCard == Card.Yellow)
          {
            GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextyellow");
          }
          else if (penaltyKickCard == Card.YellowRed ||
            penaltyKickCard == Card.Red)
          {
            GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextred");
            crowd.getMood().reportEvent(
              CrowdMoodEvent.RedCard, 1-penaltyKickTeam, score);
          }
          lastStatusChangeCommented = 2;
        }
        if (t - matchStatusChangeTime > 0.5f &&
          lastStatusChangeCommented == 0)
        {
          GameEngine.getUIManager().getCurrentScreen().activateGroup(
            "flashtextpenalty");
          commentatorEvent(Commentator.CommentatorEvent.PenaltyKick);
          lastStatusChangeCommented = 1;
          crowd.getMood().reportEvent(CrowdMoodEvent.Penalty, penaltyKickTeam, score);
        }
      }
      
      
      else if (matchStatus == MatchStatus.FREEKICK)
      {
        
        //LOM_2 patch: show Freekick-WallDistance (only if human-controlled) 
        if (this.getNonActiveTeam().getTeamControl() !=
          TeamControl.AI && t - matchStatusChangeTime > 1.5f)
        {                 
          matchManager.showFreekickWallDistance(
            this.getMatchStatusBallPosition());
        }
        
        //free kick
        if (matchStatusActionReady)
        {
          
          //LOM patch: check if there is an opponent near the ball                  
          if(!referee.getAI().proceedFreekick(false))
          {
            freeKickToRepeat = true;
            waitForRepeatTime = lastStepTime;
            //TODO special comment for repeated freekick?
            disableFreeKickComment = true;            
            this.commentDelayOfGame();
          }
          
          setMatchStatus(MatchStatus.RUNNING);
          
          //LOM_2 patch
          matchManager.hideFreekickWallDistance();
          
        }
        if ((freeKickCard != Card.None || freeKickComment) &&
          t - matchStatusChangeTime > 5f &&
          lastStatusChangeCommented == 1 &&
          /* LOM_2 patch: */ !disableFreeKickComment)
        {
          if (freeKickCard == Card.None)
          {
            //no comment here
          }
          else if (freeKickCard == Card.Yellow)
          {
            //answer to foul: yellow card
            if (freeKickComment)
              commentatorEvent(Commentator.CommentatorEvent.FoulYellowCard);
            GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextyellow");
            crowd.getMood().reportEvent(
              CrowdMoodEvent.YellowCard, 1-freeKickTeam, score);
          }
          else if (freeKickCard == Card.YellowRed)
          {
            //answer to foul: yellow-red card
            if (freeKickComment)
              commentatorEvent(Commentator.CommentatorEvent.FoulYellowRedCard);
            GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextred");
            crowd.getMood().reportEvent(
              CrowdMoodEvent.RedCard, 1-freeKickTeam, score);
          }
          else if (freeKickCard == Card.Red)
          {
            //answer to foul: red card
            if (freeKickComment)
              commentatorEvent(Commentator.CommentatorEvent.FoulRedCard);
            GameEngine.getUIManager().getCurrentScreen().activateGroup("flashtextred");
            crowd.getMood().reportEvent(
              CrowdMoodEvent.RedCard, 1-freeKickTeam, score);
          }
          lastStatusChangeCommented = 2;
        }
        else if (t - matchStatusChangeTime > 0.5f &&
          lastStatusChangeCommented == 0 &&
          /* LOM_2 patch: */ !disableFreeKickComment)
        {
          freeKickComment = (MathTools.random() < 0.5f);
          if (freeKickCard != Card.None || freeKickComment)
          {
            freeKickComment = commentatorEvent(Commentator.CommentatorEvent.Foul);
          }
          lastStatusChangeCommented = 1;
        }
        
        //LOM patch
        else if(delayOfGameCard != null && 
          delayOfGameCard != Card.None &&
          !delayOfGameCommented)
        {
          this.commentDelayOfGame();
        }
        
      }
      
      
      else if (matchStatus == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
      {
        //penalty kick while penality shootout
        if (matchStatusActionReady)
        {
          setMatchStatus(MatchStatus.RUNNING);
        }
      }

    }
    
    //animate particle system
    if (lastStepTime - lastParticleTime > 0.2f)
    {
      lastParticleTime = lastStepTime;
      if (particleSystem != null)
      {
        if (particleSystem.step())
        {
          //it is dead now
          particleSystem.removeFromScene();
          particleSystem = null;
        }
      }
    }
  }

  /**
   * Gets the current status of the match.
   */
  public MatchStatus getMatchStatus()
  {
    return matchStatus;
  }

  /**
   * Gets the opposing team to the given team.
   */
  public Team getOpponentTeam(Team ownTeam)
  {
    if (ownTeam == teams[0])
      return teams[1];
    else
      return teams[0];
  }

  /**
   * Returns <code>true</code>, if the given team
   * is the avtive one in the current match status
   * (for example when team has kick off, free kick,
   * corner kick, ...).
   */
  public boolean isMatchStatusActiveTeam(Team team)
  {
    return (getActiveTeam() == team);
  }
  
  
  /**
   * Returns the currently active team
   * in the current match status
   * (for kick off, free kick, goal kick, corner kick, penalty kick).
   */
  public Team getActiveTeam()
  {
    if (matchStatus == MatchStatus.KICKOFF_TIME || 
      matchStatus == MatchStatus.KICKOFF_GOAL)
      return kickoffTeam;
    else if (matchStatus == MatchStatus.CORNERKICK ||
      matchStatus == MatchStatus.GOALKICK)
      return teams[cornergoalKickTeam];
    else if (matchStatus == MatchStatus.THROWIN)
      return teams[throwinTeam];
    else if (matchStatus == MatchStatus.PENALTYKICK)
      return teams[penaltyKickTeam];
    else if (matchStatus == MatchStatus.FREEKICK)
      return teams[freeKickTeam];
    else if (matchStatus == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
      return teams[penaltyShootoutActiveTeam];
    else
      return null;
  }
  
  
  /**
   * LOM_2 patch: Gets the non-active team.
   * 
   * @author Malte Löpmann
   */
  public Team getNonActiveTeam()
  {
    int activeTeamIndex = this.getTeamIndex(getActiveTeam());
    return this.getTeam((activeTeamIndex + 1) %2);
  }
  

  /**
   * Gets the default player position for a
   * kickoff.
   * @param player   0: the player, that kicks the ball first;
   *                 1: the player, that gets the ball first
   */
  public Vector3f getKickoffPlayerPosition(int player, int currentSide)
  {
    if (player == 0)
      return new Vector3f(currentSide * 0.01f, 0, 1f);
    else
      return new Vector3f(currentSide * 0.01f, 0, -3);
  }


  /**
   * Returns <code>true</code>, if no player from team <code>team</code>
   * stands between <code>pos1</code> and <code>pos2</code>, apart from
   * <code>playerBall</code> (may be null) and the goalkeeper,
   * otherwise <code>false</code>.
   * The minimal distance to the line is <code>minDistanceSquared</code>
   * (a smaller distance means: way is not clear). An additional value
   * is added dependent on the position between pos1 and pos2. The ball
   * needs some time to go to pos2, so an object near pos2 has a greater
   * minimal distance: <code>minDistanceSquared</code> +
   * <code>additionalFarDistance</code> (if it is exactly between pos1
   * and pos2, the half <code>additionalFarDistance</code> is added,
   * and so on).
   */
  public boolean isWayFree(Vector3f pos1, Vector3f pos2,
    Team team, Player playerBall, float minDistanceSquared,
    float additionalFarDistance)
  {
    float t = 0;
    for (int i = 1; i < team.getPlayersCount(); i++) //start with index 1 (ignore goalkeeper)
    {
      Player p = team.getPlayers()[i];
      if (p != playerBall && p != null && p.isPlaying() &&
        MathTools.distanceSquaredPointLineSegment2D(
          pos1, pos2, p.getPosition(), t) < minDistanceSquared +
            t * additionalFarDistance)
        return false;
    }
    return true;
  }

  
  /**
   * Gets the current game time in seconds.
   */
  public float getGameTime()
  {
    return gameTime;
  }
  

  /**
   * Gets the current game time in the format 00:00 ...........
   */
  private String getGameTimeString()
  {
    int t = (int) gameTime;
    if (gameTime >= 0)
    {
      String min = String.valueOf(t / 60);
      if (min.length() == 1) min = "0" + min;
      String sec = String.valueOf(t % 60);
      if (sec.length() == 1) sec = "0" + sec;
      return min + ":" + sec;
    }
    else
    {
      //penalty shootout
      String ret = "";
      //show goals / misses
      for (int i = 0; i < 5; i++)
      {
        int s = penaltyShootoutGoal[penaltyShootoutActiveTeam][i];
        if (s == 0)
          ret += "."; //not yet shot
        else if (s == 1)
          ret += "x"; //goal
        else
          ret += "o"; //miss
      }
      return ret;
    }
  }


  /**
   * Changes the match status.
   * Call this method instead of modifying the
   * <code>matchStatus</code> member variable.
   */
  private void setMatchStatus(MatchStatus matchStatus)
  {
    this.matchStatus = matchStatus;
    matchStatusChangeTime = GameEngine.getFrameTime(true);
    lastStatusChangeCommented = 0;
    matchStatusActionReady = false;
    matchStatusActionReadyTime = 0;
    matchStatusBallPosSet = false;
    noFoulWaitingForComment = false;
  }


  /**
   * Send event to commentator.
   * Call this method instead of calling
   * <code>commentary.newEvent()</code>.
   * Returns true, when the comment is really played.
   */
  public boolean commentatorEvent(Commentator.CommentatorEvent event)
  {
    //find new idle comment start time
    float time = 45 * 60 / 5 / timeFactor; //1/5 of a half time
    if (time < 25) time = 25; //but at least 25 seconds
    time += MathTools.random(10); //random additional time
    nextIdleCommentTime = GameEngine.getFrameTime(true) + time;
    //report event
    if (commentator != null && event != null)
      return commentator.newEvent(event);
    return false;
  }


  /**
   * This method must be called by the TeamAI of
   * the active team that has kickoff, free kick,
   * corner kick, throw in, and so on. The action
   * will not be performed until this method is called!
   * If a non-active teams calls this method, it is
   * ignored.
   */
  public void setMatchStatusActionReady(Team team)
  {
    if (!matchStatusActionReady && isMatchStatusActiveTeam(team))
    {
      matchStatusActionReady = true;
      matchStatusActionReadyTime = GameEngine.getFrameTime(true);
    }
  }


  /**
   * Gets the corner, where the corner kick or goal kick
   * will be performed. This method only works, if the
   * current match status is corner kick.
   * @return   -1: left corner, 1: right corner, 0: wrong match status
   */
  public int getCornerGoalKickCorner()
  {
    if ((matchStatus == MatchStatus.CORNERKICK) ||
      matchStatus == MatchStatus.GOALKICK)
    {
      return cornergoalKickCorner;
    }
    return 0;
  }


  /**
   * Gets the position, where the throw-in is located.
   * The returned value is only valid, if the
   * current match status is throw in.
   * Only the x- and z-coordinate is valid, y may be 0.
   */
  public Vector3f getThrowInPosition()
  {
    return throwinPos;
  }


  /**
   * Gets the index of the given team.
   * @return   0 or 1 (or -1, if not found)
   */
  public int getTeamIndex(Team team)
  {
    if (team == teams[0])
      return 0;
    else if (team == teams[1])
      return 1;
    else
      return -1;
  }


  /**
   * Gets the team 0 or 1.
   */
  public Team getTeam(int index)
  {
    return teams[index];
  }



  /**
   * Returns 1, if the given team is on the left side,
   * or 2, if the given team is on the right side,
   * or 0 if it is not playing in this match.
   */
  private int getTeamSide1or2(Team team)
  {
    int ret = 0;
    if (team == teams[0])
      ret = -1;
    else if (team == teams[1])
      ret = 1;
    else
      return 0;
    if (teams[0].getCurrentSide() == 1)
      ret = -ret;
    return (ret == -1) ? 1 : 2;
  }


  /**
   * Call this method from a TeamAI whenever the
   * active player has changed.
   */
  public void activePlayerChanged(Player activePlayer)
  {
    Team team = activePlayer.getTeam();
    int side = getTeamSide1or2(team);
    String s = "match.player" + side + "name";
    Control c = GameEngine.getUIManager().getCurrentScreen().findControl(s);
    if (c != null)
    {
      Label l = (Label) c;
      l.setCaption(activePlayer.getInfo().getName());
    }
    updateEnergyDisplay(activePlayer);
    updateCardDisplay(activePlayer);
  }


  /**
   * Call this method, if the energy value of a player has changed
   * of the active player has changed.
   * If the player is active, the value is shown in the energy display.
   */
  public void updateEnergyDisplay(Player player)
  {
    Team team = player.getTeam();
    if (team.getActivePlayer() == player)
    {
      int side = getTeamSide1or2(team);
      String s = "match.player" + side + "energy";
      Control c = GameEngine.getUIManager().getCurrentScreen().findControl(s);
      if (c != null)
      {
        ImageSwitch imgsw = (ImageSwitch) c;
        imgsw.setSelectedIndex(player.getInfo().getEnergy0to10());
      }
    }
  }
  
  
  /**
   * Call this method, when the time display should be
   * updated.
   */
  public void updateTimeDisplay()
  {
    Control c = GameEngine.getUIManager().getCurrentScreen().findControl(
            "match.time");
    if (c != null)
    {
      Label l = (Label) c;
      l.setCaption(getGameTimeString());
    }
  }
  
  
  /**
   * Call this method at the beginning of the match and
   * when the sides have changed.
   */
  public void updateTeamLogoDisplay()
  {
    for (int i = 0; i < 2; i++)
    {
      String s = "match.team" + 
        (MathTools.clamp(teams[i].getCurrentSide(), 0, 1) + 1) + "logo";
      Control c = GameEngine.getUIManager().getCurrentScreen().findControl(s);
      if (c != null)
      {
        Image img = (Image) c;
        ITexture tex = TextureFactory.loadTexture(
          "data/teams/" + teams[i].getTeamInfo().getID() + "-logo.png", true);
        if (tex == null)
          tex = TextureFactory.loadTexture(
            "data/images/teamunknown-logo.png", true);
        img.setImage(tex);
      }
    }
  }
  
  
  /**
   * Call this method, if the card value of a player has changed
   * of the active player has changed.
   * If the player is active, the value is shown in the card display.
   */
  public void updateCardDisplay(Player player)
  {
    Team team = player.getTeam();
    if (team.getActivePlayer() == player)
    {
      int side = getTeamSide1or2(team);
      String s = "match.player" + side + "card";
      Control c = GameEngine.getUIManager().getCurrentScreen().findControl(s);
      if (c != null)
      {
        ImageSwitch imgsw = (ImageSwitch) c;
        imgsw.setSelectedIndex(player.getCard().getImageIndex());
      }
    }
  }
  
  
  /**
   * Gets the current match phase.
   */
  public MatchPhase getMatchPhase()
  {
    return matchPhase;
  }

  
  /**
   * Sets the current match phase.
   */
  public void setMatchPhase(MatchPhase matchPhase)
  {
    this.matchPhase = matchPhase;
    nextIdleCommentTime = 0; //reset idle comment time
    Logging.log(Logging.LEVEL_MESSAGES, this, "Set match phase:" + matchPhase);
    if (matchPhase == MatchPhase.FirstHalf)
    {
      //kickoff for first half
      setMatchStatus(MatchStatus.RUNNING);
    }
    else if (matchPhase == MatchPhase.HalfTime)
    {
      //half time
      setMatchStatus(MatchStatus.HALFTIME);
      commentator.newEvent(Commentator.CommentatorEvent.Halftime);
      //half time jingle
      playHalftimeJingle();
      Flight halfTimeFlight = GameEngine.getUIManager().getCameraFlight("halftime1");
      if (halfTimeFlight != null)
        matchManager.getCamera().setFlight(halfTimeFlight, stadium);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("halftime");
      //side change
      sideChanged = -sideChanged;
      teams[0].setCurrentSide(1);
      teams[1].setCurrentSide(-1);
      kickoffTeam = teams[1 - startKickoffTeam];
      stadium.getField().exchangeTeamMats();
    }
    else if (matchPhase == MatchPhase.SecondHalf)
    {
      //kickoff for second half
      //set time back to 45:00
      gameTime = 45 * 60;
      setMatchStatus(MatchStatus.RUNNING);
      matchManager.getCamera().setCameraView(CameraView.Match);
    }
    else if (matchPhase == MatchPhase.TimeUpRegularTime)
    {
      //regular time up
      setMatchStatus(MatchStatus.FINALWHISTLE);
      if (Math.abs(score[0] - score[1]) >= 3) //clear victory
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleClearVictory);
      else if (score[0] != score[1]) //narrow victory
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleNarrowVictory);
      else //match drawn
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleMatchDrawn);
      //end jingle
      playMatchEndJingle();
      //camera flies to scoreboard
      flyToScoreboard();
      //crowd
      crowd.getMood().reportEvent(CrowdMoodEvent.EndOfMatch, 0, score);
      //tell the MatchManager the result
      matchManager.setMatchResult(matchResult);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("timeupregulartime");
    }
    else if (matchPhase == MatchPhase.TimeUpBeforeOvertime)
    {
      //break between 90 min and overtime
      setMatchStatus(MatchStatus.HALFTIME);
      commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleOvertime);
      Flight halfTimeFlight = GameEngine.getUIManager().getCameraFlight("halftime1");
      if (halfTimeFlight != null)
        matchManager.getCamera().setFlight(halfTimeFlight, stadium);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("beforeovertime");
      //side change
      sideChanged = -sideChanged;
      teams[0].setCurrentSide(-1);
      teams[1].setCurrentSide(1);
      kickoffTeam = teams[startKickoffTeam];
      stadium.getField().exchangeTeamMats();
    }
    else if (matchPhase == MatchPhase.OverTimeFirstHalf)
    {
      //kickoff for first overtime half
      //set time back to 90:00
      gameTime = 90 * 60;
      setMatchStatus(MatchStatus.RUNNING);
      matchManager.getCamera().setCameraView(CameraView.Match);
    }
    else if (matchPhase == MatchPhase.OverTimeHalfTime)
    {
      //overtime half time
      setMatchStatus(MatchStatus.HALFTIME);
      //half time jingle
      playHalftimeJingle();
      //no comment available.
      Flight halfTimeFlight = GameEngine.getUIManager().getCameraFlight("halftime1");
      if (halfTimeFlight != null)
        matchManager.getCamera().setFlight(halfTimeFlight, stadium);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("overtimehalftime");
      //side change
      sideChanged = -sideChanged;
      teams[0].setCurrentSide(1);
      teams[1].setCurrentSide(-1);
      kickoffTeam = teams[1 - startKickoffTeam];
      stadium.getField().exchangeTeamMats();
    }
    else if (matchPhase == MatchPhase.OverTimeSecondHalf)
    {
      //kickoff for second half
      //set time back to 105:00
      gameTime = 105 * 60;
      setMatchStatus(MatchStatus.RUNNING);
      matchManager.getCamera().setCameraView(CameraView.Match);
    }
    else if (matchPhase == MatchPhase.TimeUpOverTime)
    {
      //overtime time up
      setMatchStatus(MatchStatus.FINALWHISTLE);
      if (Math.abs(score[0] - score[1]) >= 3) //clear victory
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleClearVictory);
      else if (score[0] != score[1]) //narrow victory
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleNarrowVictory);
      else //match drawn
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleMatchDrawn);
      //end jingle
      playMatchEndJingle();
      //camera flies to scoreboard
      flyToScoreboard();
      //crowd
      crowd.getMood().reportEvent(CrowdMoodEvent.EndOfMatch, 0, score);
      //tell the MatchManager the result
      matchManager.setMatchResult(matchResult);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("timeupovertime");
    }
    else if (matchPhase == MatchPhase.TimeUpBeforePenaltyShootout)
    {
      //break between 120 min and penalty shootout
      setMatchStatus(MatchStatus.BEFOREPENALTYSHOOTOUT);
      commentator.newEvent(CommentatorEvent.FinalWhistlePenaltyShootout);
      Flight flight =
        GameEngine.getUIManager().getCameraFlight("beforepenaltyshootout");
      if (flight != null)
        matchManager.getCamera().setFlight(flight, stadium);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("beforepenaltyshootout");
    }
    else if (matchPhase == MatchPhase.PenaltyShootout)
    {
      //penalty shootout
      gameTime = -1;
      updateTimeDisplay();
      updateScoreLabel();
      setMatchStatus(MatchStatus.PENALTYKICK_PENALTYSHOOTOUT);
      matchManager.getCamera().setCameraView(CameraView.Match);
    }
    else if (matchPhase == MatchPhase.PenaltyShootoutFinished)
    {
      //penalty shootout finished
      setMatchStatus(MatchStatus.FINALWHISTLE);
      if (score[0] != score[1]) //narrow victory (always narrow when penalty shootout)
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleNarrowVictory);
      else //match drawn
        commentator.newEvent(Commentator.CommentatorEvent.FinalWhistleMatchDrawn);
      //end jingle
      playMatchEndJingle();
      //camera flies to scoreboard
      flyToScoreboard();
      //crowd
      crowd.getMood().reportEvent(CrowdMoodEvent.EndOfMatch, 0, score);
      //tell the MatchManager the result
      matchManager.setMatchResult(matchResult);
      GameEngine.getUIManager().getCurrentScreen().activateGroup("penaltyshootoutfinished");
    }
  }
  
  
  /**
   * This method is called by the GUI action "matchevent".
   */
  public void matchEvent(String param)
  {
    if (param.equals("secondhalf") &&
      matchPhase == MatchPhase.HalfTime)
    {
      setMatchStatus(MatchStatus.KICKOFF_TIME);
      ball.setPosition(new Vector3f(0, ball.getMinYPosition(), 0));
      ball.setVelocity(new Vector3f(0, 0, 0));
      updateScoreLabel();
      updateTeamLogoDisplay();
    }
    else if (param.equals("overtime") &&
      matchPhase == MatchPhase.TimeUpBeforeOvertime)
    {
      setMatchStatus(MatchStatus.KICKOFF_TIME);
      ball.setPosition(new Vector3f(0, ball.getRadius() + 0.1f, 0));
      ball.setVelocity(new Vector3f(0, 0, 0));
      updateScoreLabel();
      updateTeamLogoDisplay();
    }
    else if (param.equals("overtimesecondhalf") &&
      matchPhase == MatchPhase.OverTimeHalfTime)
    {
      setMatchStatus(MatchStatus.KICKOFF_TIME);
      ball.setPosition(new Vector3f(0, ball.getRadius() + 0.1f, 0));
      ball.setVelocity(new Vector3f(0, 0, 0));
      updateScoreLabel();
      updateTeamLogoDisplay();
    }
    else if (param.equals("penaltyshootout") &&
      matchPhase == MatchPhase.TimeUpBeforePenaltyShootout)
    {
      setMatchPhase(MatchPhase.PenaltyShootout);
      updateTeamLogoDisplay();
    }
  }
  
  
  /**
   * Remove all irrlicht objects from the used ISceneManager
   */
  public void removeFromScene()
  {
    teams[0].removeFromScene();
    teams[1].removeFromScene();
    referee.removeFromScene();
    stadium.removeFromScene();
    ball.removeFromScene();
    if (particleSystem != null)
      particleSystem.removeFromScene();
  }
  
  
  /**
   * Updates the score label.
   */
  public void updateScoreLabel()
  {
    Control c = GameEngine.getUIManager().getCurrentScreen().findControl(
      "match.score");
    if (c != null)
    {
      Label l = (Label) c;
      if (gameTime >= 0)
      {
        if (sideChanged == 1)
          l.setCaption(score[0] + ":" + score[1]);
        else
          l.setCaption(score[1] + ":" + score[0]);
      }
      else
      {
        //penalty shootout: show only penalty goals
        l.setCaption(penaltyShootoutScore[0] + ":" + penaltyShootoutScore[1]);
      }
    }
  }
  
  
  /**
   * Returns <code>true</code>, if the last goal was
   * scored by the given team, otherwise <code>false</code>.
   */
  public boolean isGoalTeam(Team team)
  {
    return teams[goalTeam] == team;
  }
  
  
  /**
   * Returns the shooter of the last goal.
   * This can also be a player from the other goal,
   * when it was an own goal.
   */
  public Player getGoalPlayer()
  {
    return goalPlayer;
  }
  
  
  /**
   * Gets the score difference for the given team.
   * For example 3 means: The given team leads
   * 3 goals, -1 means: The given team trails 1 goal.
   */
  public int getGoalDifference(Team team)
  {
    if (team == teams[0])
      return score[0] - score[1];
    else
      return score[1] - score[0];
  }
  
  
  /**
   * Called by the referee when he is ready
   * for kickoff.
   */
  public void kickoff()
  {
    setMatchStatus(MatchStatus.RUNNING);
  }
  
  
  /**
   * Gets the ball position for the current match status.
   * Works only if match status is goalkick, cornerkick
   * or throw in!
   */
  public Vector3f getMatchStatusBallPosition()
  {
    Vector3f ret = new Vector3f();
    if (matchStatus == MatchStatus.GOALKICK)
    {
      //goal kick
      ret.x = teams[cornergoalKickTeam].getCurrentSide() *
        (stadium.getField().getLength() / 2 - 2.5f);
      ret.y = ball.getMinYPosition();
      ret.z = teams[cornergoalKickTeam].getCurrentSide() *
        cornergoalKickCorner * 2.5f;
    }
    else if (matchStatus == MatchStatus.CORNERKICK)
    {
      //corner kick
      ret.x = -teams[cornergoalKickTeam].getCurrentSide() *
        (stadium.getField().getLength() / 2 - 0.2f);
      ret.y = ball.getMinYPosition();
      ret.z = teams[cornergoalKickTeam].getCurrentSide() *
        cornergoalKickCorner * (stadium.getField().getWidth() / 2 - 0.2f);
    }
    else if (matchStatus == MatchStatus.THROWIN)
    {
      //throw in
      ret.set(throwinPos);
    }
    else if (matchStatus == MatchStatus.PENALTYKICK)
    {
      //penalty kick
      Field field = stadium.getField();
      ret.set(-teams[penaltyKickTeam].getCurrentSide() *
        (field.getLength() / 2 - field.getPenaltySpotDistance()), 0, 0);
    }
    else if (matchStatus == MatchStatus.FREEKICK)
    {
      //free kick
      ret.set(freeKickPos);
    }
    else if (matchStatus == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
    {
      //penalty kick while penalty shootout - always right goal
      Field field = stadium.getField();
      ret.set(field.getLength() / 2 - field.getPenaltySpotDistance(), 0, 0);
    }
    return ret;
  }

  
  /**
   * Gets the time when the match status changed
   * the last time.
   */
  public float getMatchStatusChangeTime()
  {
    return matchStatusChangeTime;
  }
  
  
  /**
   * Gets the ball.
   */
  public Ball getBall()
  {
    return ball;
  }
  
  
  /**
   * Gets the stadium.
   */
  public Stadium getStadium()
  {
    return stadium;
  }

  
  /**
   * Gets the referee of this match.
   */
  public Referee getReferee()
  {
    return referee;
  }
  
  
  /**
   * The referee stops the match with the given decision.
   * This may be a penalty kick decision, a free kick
   * decision and so on.
   */
  public void refereeDecision(RefereeDecision decision)
  {
    //decision
    if (decision instanceof PenaltyKickDecision)
    {
      setMatchStatus(MatchStatus.PENALTYKICK);
      PenaltyKickDecision penaltyKickDecision = (PenaltyKickDecision) decision;
      penaltyKickTeam = (penaltyKickDecision.getTeam() == teams[0] ? 0 : 1);
      penaltyKickCard = penaltyKickDecision.getCard();
      decision.applyActionSet(this);
    }
    else if (decision instanceof FreeKickDecision)
    {
      setMatchStatus(MatchStatus.FREEKICK);
      FreeKickDecision freeKickDecision = (FreeKickDecision) decision;
      freeKickTeam = (freeKickDecision.getTeam() == teams[0] ? 0 : 1);
      freeKickPos = freeKickDecision.getPosition();
      freeKickCard = freeKickDecision.getCard();
      disableFreeKickComment = false; //LOM patch
      decision.applyActionSet(this);
    }
    else if (decision instanceof NoFoulDecision)
    {
      //referee decided for no foul (match goes on)
      noFoulWaitingForComment = false;
      if (MathTools.random() < 0.5f)
      {
        //first play normal foul comment,
        //2 seconds later the no-foul-comment
        noFoulWaitingForComment = commentatorEvent(CommentatorEvent.Foul);
        noFoulCommentTime = GameEngine.getFrameTime(true) + 2;
      }
    }
    
    //LOM patch
    else if (decision instanceof DelayOfGameDecision)
    {
      delayOfGameCard = decision.getCard();
      delayOfGameCommented = false;
      decision.applyActionSet(this);
    }
    
    //apply card
    if (decision.getCard() != Card.None)
    {
      Player cardPlayer = decision.getCardPlayer();
      cardPlayer.setCard(decision.getCard());
      //no update of card display now, because that would be
      //earlier than the referee.
    }
  }
  
  
  /**
   * Gets the real duration of a match, e.g. 90 (seconds),
   * when the time factor is 60.{
   */
  public float getRealDuration()
  {
    return 90 * 60 / timeFactor;
  }
  
  
  /**
   * Gets the crowd.
   */
  public Crowd getCrowd()
  {
    return crowd;
  }

  
  /**
   * Gets the energy bar for shoot power display.
   */
  public EnergyBar getEnergyBar()
  {
    return energyBar;
  }

  
  /**
   * Returns true, when a draw is allowed. Otherwise
   * there must be overtime and perhaps penalty shootout.
   */
  public boolean isDrawAllowed()
  {
    return drawAllowed;
  }
  
  
  /**
   * Gets the current score.
   */
  public int[] getScore()
  {
    return score;
  }
  
  
  /**
   * Gets the index of the current penalty shootout shooter.
   */
  public int getPenaltyShootoutShooterIndex()
  {
    //player 4, 3, 2, 1, 4, 3, 2, 1, 4, ...
    return 4 - ((penaltyShootoutShotsCount / 2) % 4);
  }
  
  
  /**
   * Gets the current duration of the current match status.
   */
  public float getMatchStatusDuration()
  {
    return GameEngine.getFrameTime(true) - matchStatusChangeTime;
  }
  
  
  /**
   * Call this method in the penalty shootout phase
   * when the ball was shot. It has 1 second time to
   * go into the goal after then, otherwise it's no goal.
   */
  public void penaltyShootoutBallShot()
  {
    //ball was shot now
    penaltyShootoutShot = true; 
    penaltyShootoutShotTime = GameEngine.getFrameTime(true);
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Penalty shootout: ball shot at time " + penaltyShootoutShotTime);
  }
  
  
  /**
   * Starts the next penalty shootout penalty kick,
   * or ends the match, dependent on the current score.
   */
  private void nextPenaltyShootoutPenaltyKick()
  {
    penaltyShootoutShotsCount++;
    if (penaltyShootoutShotsCount >= 10 &&
      (penaltyShootoutShotsCount % 2 == 0))
    {
      //after the 10th shot (every team shot 5 times), see
      //if there is a winner
      if (penaltyShootoutScore[0] != penaltyShootoutScore[1])
      {
        setMatchPhase(MatchPhase.PenaltyShootoutFinished);
        updateScoreLabel();
        updateTimeDisplay();
        return;
      }
    }
    else if (penaltyShootoutShotsCount == 10)
    {
      //next round. delete goal displays.
      for (int iTeam = 0; iTeam < 5; iTeam++)
      {
        penaltyShootoutGoal[iTeam][0] = 0;
        penaltyShootoutGoal[iTeam][1] = 0;
      }
    }
    //next shot
    penaltyShootoutActiveTeam = 1 - penaltyShootoutActiveTeam;
    penaltyShootoutShot = false;
    //look if there is already a winner (before 10 shoots)
    boolean over = false;
    if (penaltyShootoutShotsCount < 10)
    {
      int shotsCount = (penaltyShootoutShotsCount - penaltyShootoutActiveTeam) / 2;
      over = (Math.abs(penaltyShootoutScore[0] - penaltyShootoutScore[1]) >
        5 - shotsCount);
    }
    if (over)
      setMatchPhase(MatchPhase.PenaltyShootoutFinished);
    else
      setMatchStatus(MatchStatus.PENALTYKICK_PENALTYSHOOTOUT);
    //show score
    updateScoreLabel();
    updateTimeDisplay();
  }

  
  /**
   * Gets the index of the team that shot the last goal.
   * 0 = home team, 1 = visitor team.
   */
  public int getGoalTeamIndex()
  {
    return goalTeam;
  }
  
  
  /**
   * Call this method instead changing score[] directly!
   * @param team   0 or 1
   * @param change amount of goals, e.g. 1 for a normal goal
   */
  private void changeScore(int team, int change)
  {
    score[team] += change;
    updateScoreLabel();
    if (stadium.getScoreboard() != null)
      stadium.getScoreboard().setScore(team, score[team]);
  }
  
  
  /**
   * LOM patch: Plays the entrance jingle for the home team.
   * 
   * @author Malte Löpmann
   */
  private void playEntranceJingle()
  {
    SoundEffect jingle =
      SoundEffectPool.getEntranceJingleByTeam(
        this.getTeam(0).getTeamInfo().getID());
    if (jingle != null)
      jingle.play();
  }  
  
  
  /**
   * Plays the halftime jingle.
   */
  private void playHalftimeJingle()
  {
    SoundEffect jingle =
      SoundEffectPool.getRandomJingle(SoundEffectPool.JINGLE_HALFTIME);
    if (jingle != null)
      jingle.play();
  }
  
  
  /**
   * Plays the match end jingle (from the view of the user).
   */
  private void playMatchEndJingle()
  {
    int jingleType = 0;
    if (score[0] == score[1])
    {
      jingleType = SoundEffectPool.JINGLE_DRAW;
    }
    else if (score[0] > score[1])
    {
      if (teams[0].getTeamControl() != TeamControl.AI)
        jingleType = SoundEffectPool.JINGLE_VICTORY;
      else
        jingleType = SoundEffectPool.JINGLE_DEFEAT;
    }
    else //(score[0] < score[1])
    {
      if (teams[0].getTeamControl() != TeamControl.AI)
        jingleType = SoundEffectPool.JINGLE_DEFEAT;
      else
        jingleType = SoundEffectPool.JINGLE_VICTORY;
    }
    SoundEffect jingle =
      SoundEffectPool.getRandomJingle(jingleType);
    if (jingle != null)
      jingle.play();
  }
  
  
  /**
   * Lets tha camera fly to the scoreboard.
   */
  private void flyToScoreboard()
  {
    Flight flight =
      GameEngine.getUIManager().getCameraFlight("scoreboard1");
    if (flight != null)
      matchManager.getCamera().setFlight(flight, stadium);
  }
  
  
  /**
   * Creates a simple particle system with the given texture.
   */
  public void createSimpleParticleSystem(Vector3f position, String textureFile)
  {
    if (particleSystem != null)
      particleSystem.removeFromScene();
    particleSystem = new SimpleParticleSystem(position, textureFile,
      GameEngine.getSceneManager(), GameEngine.getVideoDriver());
  }
  
  

  /**
   * LOM patch: Checks if there are opponents in penalty area.
   * If yes, the first found player is returned, otherwise
   * null is returned.
   * 
   * @author Malte Löpmann
   */
  public Player getOpponentInPenaltyArea(int activeTeamIndex)
  {
    Team nonActiveTeam = this.getTeam((activeTeamIndex + 1) % 2);
    Team activeTeam = this.getTeam(activeTeamIndex);
    Field field = this.getStadium().getField();

    for (int i = 0; i < nonActiveTeam.getPlayersCount(); i++)
    {
      Player player = nonActiveTeam.getPlayer(i, -1);
      if (player.isInPenaltyArea(field, activeTeam))
        return player;
    }

    return null;
  }
  
  
  /**
   * LOM patch: Checks if there is an opponent near the ball.
   * If yes, the first found player is returned, otherwise
   * null is returned.
   * 
   * @author Malte Löpmann
   */  
  public Player getOpponentNearBall(int activeTeamIndex, int allowedDistance)
  {
    Team nonActiveTeam = this.getTeam((activeTeamIndex + 1) %2);
    for(int i=0; i<nonActiveTeam.getPlayersCount(); i++)
    {
      Player player = nonActiveTeam.getPlayer(i, -1);
      float distanceToBall = MathTools.distance(
          player.getPosition(), this.getMatchStatusBallPosition());

      if(distanceToBall < allowedDistance)
        return player;      
    }
    return null;
  }
    
  
  /**
   * LOM patch: Plays a "delay of game" comment.
   * 
   * @author Malte Löpmann
   */
  private void commentDelayOfGame()
  {           
    if (delayOfGameCard != Card.None)
    {
      if (delayOfGameCard == Card.Yellow)
      {
        // TODO comment?
        commentatorEvent(Commentator.CommentatorEvent.FoulYellowCard);
        GameEngine.getUIManager().getCurrentScreen().activateGroup(
            "flashtextyellow");
        crowd.getMood().reportEvent(CrowdMoodEvent.YellowCard,
            1 - freeKickTeam, score);
      }
      else if (delayOfGameCard == Card.YellowRed)
      {
        commentatorEvent(Commentator.CommentatorEvent.FoulYellowRedCard);
        GameEngine.getUIManager().getCurrentScreen().activateGroup(
            "flashtextred");
        crowd.getMood().reportEvent(CrowdMoodEvent.RedCard,
            1 - freeKickTeam, score);
      }
      
      delayOfGameCommented = true;
    }
  } 
  
}
