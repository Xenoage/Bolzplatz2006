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
package com.xenoage.bp2k6.match.teamai;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.match.characters.actions.*;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.match.referee.RefereeAI; //LOM patch
import com.xenoage.bp2k6.match.stadium.Field;
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;


/**
 * 5th version of a team AI engine.
 * Begun on 2006-02-08.
 *
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM)
 */
public class TeamAI5
  implements TeamAI
{
  
  //the team to control
  private Team team;
  //the opposing team
  private Team opponentTeam;
  //number of players per team
  private int playersCount;
  //the soccer field
  private Field field;
  //the soccer match
  private Match match;
  //the match manager
  private MatchManager matchManager;

  //the kickoff positions of the players
  private Vector3f[] kickoffPositions;
  //the attack positions of the players
  private Vector3f[] attackPositions;
  //the defense positions for of players
  private Vector3f[] defensePositions;
  //the normal positions of the players
  private Vector3f[] normalPositions;
  //the defense penalty kick positions of the players
  private Vector3f[] penaltyDefensePositions;
  //the penalty shootout positions of the players
  private Vector3f[] penaltyShootoutPositions;
  
  //the currently active player (if the team is user controlled,
  //this is always the player the user controls at the moment,
  //if it is a AI team, it may be any player - usually it is the
  //player in ball possession or in the best position to the ball).
  private Player activePlayer = null;
  
  //true, when team is controlled by a human (player 1 or player 2)
  private boolean userControl = false;
  
  //the reaction speed: when does the players notice the new
  //operation? every 200 ms a random value between 0 and 1 is
  //generated. if it is smaller than this value, the player
  //notices the new operation.
  private double reactionSpeed = 0.5;
  
  //the probability, a player passes to another one when
  //there is the chance to do so
  private double passFrequency = 0.2;
  
  //minimal squared distance to the goal that is needed to
  //shoot the ball
  private float shootDistance = 16 * 16;
  
  //the time of the last pass (own team)
  private float lastPassTime;
  //the player the pass should reach
  private int lastPassTarget = -1;
  
  
  //current operation (particular attack type, ...)
  private enum Operation
  {
    //currently no operation. find a new one.
    None,
    //2 players go to the center line (step 1). after the
    //kickoff-whistle player 0 passes to
    //player 1 (step 2). when player 1 gets the ball,
    //the operation ends.
    KickOff,
    //all players run back to defense, one player attacks
    //opponent with ball (no steps, no players)
    //requirements: opposing team has the ball
    Defense,
    //normal attack, run forward, pass to uncovered player,
    //goal shoot (no steps, no players)
    //requirements: own team has the ball
    Attack,
    //goal kick
    //active team:
    //  goalkeeper runs behind the ball, waits a moment,
    //  runs to the ball and kicks it
    //  all other players run to the normal positions
    //non-active team:
    //  run to normal positions
    GoalKick,
    //corner kick
    //active team:
    //  one player runs to the corner,
    //  when there, he waits a moment and runs
    //  to the ball and kicks the ball in front
    //  of the goal
    //  the other players run to the attack positions.
    //non-active team:
    //  run to defense positions
    CornerKick,
    //throw in
    //active team:
    //  one player (the nearest one, operationPlayers[0])
    //  runs to the throw in position,
    //  when there, he waits a moment and throws the ball in, then
    //  the operation ends.
    //  if throw in is in own half, the defense player(s) run to the defense positions,
    //  and the forward player(s) place around the throw in positions.
    //  if throw in is in the opponent's half, the defense player(s) run to default
    //  positions, and the forward player(s) place around the throw in positions.
    //non-active team:
    //  if throw in is in own half, the defense player(s) run to the defense positions,
    //  and one forward player runs to the normal position, the other tries to cover
    //  the throw-in-target
    //  if throw in is in the opponent's half, the defense player(s) run to default
    //  positions, and one forward player runs to the normal position, the other tries to cover
    //  the throw-in-target
    ThrowIn,
    //penalty kick
    //active team:
    //  one player (one of the forward players)
    //  runs a few meters near to the penalty spot.
    //  when there, he waits for the referee whistle and kicks the ball.
    //  Then the operation ends.
    //  The other players run to the normal positions.
    //non-active team:
    //  some players may be angry, then
    //  all players run to the defense positions.
    PenaltyKick,
    //free kick
    //active team:
    //  one player (always the right forward player)
    //  runs a few meters near to the free kick position.
    //  all other players run to the attack positions
    //non-active team:
    //  run to defense positions
    //free kick improvement could be done... build a wall and so on
    FreeKick,
    //emotions after a goal
    //if this team shot the goal, it is happy, otherwise it
    //is sad or angry
    GoalEmotions,
    //half time
    //all players run to the stadium's entrance position
    HalfTime,
    //final whistle
    //dependent on the match result the players are happy
    //or not, then they run to the stadium's entrance position
    FinalWhistle,
    //before penalty shootout
    //players run to the waiting positions
    BeforePenaltyShootout,
    //penalty kick while penalty shootout
    //active team:
    //  one player (one of the outfield players)
    //  runs a few meters near to the penalty spot.
    //  when there, he waits for the referee whistle and kicks the ball.
    //  Then the operation ends.
    //  The other players run to the penalty shootout waiting positions.
    //non-active team:
    //  goallkeeper is in the goal,
    //  all players run to the penalty shootout waiting positions.
    PenaltyShootoutPenaltyKick;
  }
  private Operation operation = Operation.None;
  //current step of the operation (not needed for some operations)
  private int operationStep;
  //players for the current operation (meaning: see enum Operation)
  private Player[] operationPlayers = new Player[2];

  //time when keeper caught the ball
  private float keeperBallInHandsTime = 0;
  
  
  /**
   * Constructor.
   */
  public TeamAI5()
  {
    matchManager = GameEngine.getMatchManager();
  }

  
  /**
   * Call this method before using the AI
   * and when the sides have changed
   */
  public void update(Team ownTeam, Team opponentTeam,
    Field field, Match match)
  {
    this.team = ownTeam;
    this.opponentTeam = opponentTeam;
    playersCount = team.getPlayersCount();
    this.field = field;
    this.match = match;
    
    //user-controlled team?
    userControl = !(this.team.getTeamControl() == TeamControl.AI);
    //set active player
    Player[] players = team.getPlayers();
    setActivePlayer(team.getPlayer(3, 1));
    //create kickoff positions
    kickoffPositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      kickoffPositions[i] = getKickoffPosition(players[i].getPlayerPosition());
    //create attack positions
    attackPositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      attackPositions[i] = getAttackPosition(players[i].getPlayerPosition());
    //create defense positions
    defensePositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      defensePositions[i] = getDefensePosition(players[i].getPlayerPosition());
    //create normal positions
    normalPositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      normalPositions[i] = getNormalPosition(players[i].getPlayerPosition());
    //create defense penalty positions
    penaltyDefensePositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      penaltyDefensePositions[i] =
        getPenaltyDefensePosition(players[i].getPlayerPosition());
    //create penalty shootout positions
    penaltyShootoutPositions = new Vector3f[playersCount];
    for (int i = 0; i < playersCount; i++)
      penaltyShootoutPositions[i] =
        getPenaltyShootoutPosition(players[i].getPlayerPosition());
  }
  

  /**
   * This method is called every 200 ms. It computes the next
   * actions of the team.
   */
  public void step()
  {
    if (team == null)
      return;
    
    //time
    float timeNow = GameEngine.getFrameTime(true);
    //get players
    Player[] players = team.getPlayers();
    //get current side
    int cs = team.getCurrentSide();
    //get time
    float t = GameEngine.getFrameTime(true);
    //match status?
    Match.MatchStatus matchStatus = match.getMatchStatus();
    //players with balls
    Player playerBall = team.getBallPlayer();
    Player playerBallOpponent = opponentTeam.getBallPlayer();
    //ball
    Ball ball = match.getBall();
    //position of goals
    Vector3f goalPos = new Vector3f(-cs * field.getLength() / 2, 0, 0);
    Vector3f goalOwnPos = new Vector3f(cs * field.getLength() / 2, 0, 0);
    
    //decide what to do, dependent on match status
    if (matchStatus == Match.MatchStatus.KICKOFF_TIME ||
      matchStatus == Match.MatchStatus.KICKOFF_GOAL)
    {
      //kickoff. own team?
      if (match.isMatchStatusActiveTeam(team))
      {
        //own team has kickoff.
        //run to kickoff positions
        if (operation != Operation.KickOff)
        {
          operation = Operation.KickOff;
          operationStep = 1;
          //forward players: these are the two players to
          //perfom the kickoff. if not possible, other players
          //must do it
          operationPlayers[0] = team.getPlayer(cs == -1 ? 3 : 4, 1); 
          operationPlayers[1] = team.getRandomForwardPlayer(operationPlayers[0]);
          ActionSetFactory.applyKickOffPosition(
            team, true, (matchStatus == Match.MatchStatus.KICKOFF_TIME),
            match.getStadium(), match, kickoffPositions,
            operationPlayers);
        }
        //ready for kickoff? (both operation players must be on their positions)
        if (MathTools.distanceSquaredWithoutY(operationPlayers[0].getPosition(),
          match.getKickoffPlayerPosition(0, cs)) < 1 &&
          MathTools.distanceSquaredWithoutY(operationPlayers[1].getPosition(),
          match.getKickoffPlayerPosition(1, cs)) < 1)
        {
          match.setMatchStatusActionReady(team);
        }
      }
      else //!match.isMatchStatusActiveTeam(team)
      {
        //opposing team has kickoff.
        //run to kickoff positions.
        if (operation != Operation.KickOff)
        {
          operation = Operation.KickOff;
          ActionSetFactory.applyKickOffPosition(
            team, false, (matchStatus == Match.MatchStatus.KICKOFF_TIME),
            match.getStadium(), match, kickoffPositions, null);
          operationStep = -1; //end this operation when match status == running
        }
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.GOALKICK)
    {
      if (operation != Operation.GoalKick)
      {
        operation = Operation.GoalKick;
        if (match.isMatchStatusActiveTeam(team))
          setActivePlayer(players[0]);
        ActionSetFactory.applyGoalKick(team,
          match.isMatchStatusActiveTeam(team), ball, match,
          normalPositions);
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.CORNERKICK)
    {
      if (operation != Operation.CornerKick)
      {
        operation = Operation.CornerKick;
        if (match.isMatchStatusActiveTeam(team))
        {
          operationPlayers[0] = 
            team.getPlayer(match.getCornerGoalKickCorner() == -1 ? 3 : 4, 1);
          setActivePlayer(operationPlayers[0]);
        }
        else
          operationPlayers[0] = null;
        ActionSetFactory.applyCornerKick(team,
          match.isMatchStatusActiveTeam(team),
          operationPlayers[0], ball, match,
          attackPositions, defensePositions);
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.THROWIN)
    {
      if (operation != Operation.ThrowIn)
      {
        operation = Operation.ThrowIn;
        if (match.isMatchStatusActiveTeam(team))
        {
          operationPlayers[0] = 
            team.getPlayer(-cs * match.getThrowInPosition().z > 0 ? 3 : 4, 1);
          setActivePlayer(team.getRandomForwardPlayer(operationPlayers[0]));
        }
        else
          operationPlayers[0] = null;
        ActionSetFactory.applyThrowIn(team, opponentTeam,
          match.isMatchStatusActiveTeam(team), operationPlayers[0],
          ball, match, defensePositions, normalPositions);
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.PENALTYKICK)
    {
      if (operation != Operation.PenaltyKick)
      {
        operation = Operation.PenaltyKick;
        if (match.isMatchStatusActiveTeam(team))
        {
          operationPlayers[0] = team.getRandomForwardPlayer(null);
          setActivePlayer(operationPlayers[0]);
        }
        else
          operationPlayers[0] = null;
        ActionSetFactory.applyPenaltyKick(team,
          match.isMatchStatusActiveTeam(team),
          operationPlayers[0], ball, match,
          normalPositions, penaltyDefensePositions);
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.FREEKICK)
    {
      if (operation != Operation.FreeKick)
      {
        operation = Operation.FreeKick;
        if (match.isMatchStatusActiveTeam(team))
        {
          operationPlayers[0] = team.getPlayer(4, -1);
          setActivePlayer(operationPlayers[0]);
        }
        else
          operationPlayers[0] = null;
        
        //LOM patch: begin >>>
        //create freekick attack positions
        Vector3f[] freeKickAttackPositions = new Vector3f[playersCount];
        for (int i = 0; i < playersCount; i++)
          freeKickAttackPositions[i] =
            getFreeKickAttackPosition(team.getPlayers()[i].getPlayerPosition());
        
        //create freekick defense positions
        Vector3f[] freeKickDefensePositions = new Vector3f[playersCount];
        for (int i = 0; i < playersCount; i++)
          freeKickDefensePositions[i] =
            getFreeKickDefensePosition(team.getPlayers()[i].getPlayerPosition());
        
        ActionSetFactory.applyFreeKick(team, match.isMatchStatusActiveTeam(team),
            operationPlayers[0], ball, match,
            freeKickAttackPositions, freeKickDefensePositions);
        
        /*
        //unpatched:
        ActionSetFactory.applyFreeKick(team,
          match.isMatchStatusActiveTeam(team),
          operationPlayers[0], ball, match,
          attackPositions, defensePositions);
        */
        //LOM patch: end <<<
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.GOALEMOTIONS)
    {
      //emotions after goal
      if (operation != Operation.GoalEmotions)
      {
        operation = Operation.GoalEmotions;
        //did this team shoot the goal?
        if (match.isGoalTeam(team))
        {
          //this team scored
          if (match.getGoalDifference(team) < 0)
          {
            //if team is still trailing, it is not happy.
            //run back to kickoff positions, because the players
            //want to go on quickly
            for (int i = 0; i < playersCount; i++)
            {
              if (players[i].isPlaying())
              {
                players[i].setActionSet(new SimpleActionSet(
                  new RunToAction(players[i], kickoffPositions[i], Speed.Walk),
                  ActionSet.PRIORITY_NORMAL));
              }
            }
          }
          else
          {
            //team is happy
            //could be extended: create different elation ActionSets
            //all players: elation
            ActionSetFactory.applyGoalElation1(team,
              field, match.getGoalDifference(team),
              kickoffPositions);
          }
        }
        else
        {
          //the other team scored
          if (match.getGoalDifference(team) > 1)
          {
            //if team is still 2 goals leading,
            //it is not really unhappy. no reaction.
            for (int i = 0; i < playersCount; i++)
              ;//players[i].setTarget(kickoffPositions[i]);
          }
          else
          {
            //team is sad
            for (int i = 0; i < playersCount; i++)
            {
              ;//players[i].setEmotion(PlayerX.PlayerEmotion.DESPERATION);
              ;//players[i].setTarget(kickoffPositions[i]);
            }
          }
        }
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.HALFTIME)
    {
      //half time
      if (operation != Operation.HalfTime)
      {
        operation = Operation.HalfTime;
        //all players: run to halftime break
        ActionSetFactory.applyHalfTime(team,
          match.getStadium(), match.getGoalDifference(team));
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.FINALWHISTLE)
    {
      //final whistle
      if (operation != Operation.FinalWhistle)
      {
        operation = Operation.FinalWhistle;
        //emotions
        ActionSetFactory.applyFinalWhistle1(team,
          match.getStadium(), match.getGoalDifference(team));
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.BEFOREPENALTYSHOOTOUT)
    {
      //before penalty shootout
      if (operation != Operation.BeforePenaltyShootout)
      {
        operation = Operation.BeforePenaltyShootout;
        //all players: run to waiting positions
        for (int i = 0; i < playersCount; i++)
        {
          if (players[i].isPlaying())
          {
            players[i].setActionSet(new SimpleActionSet(
              new RunToAction(players[i], penaltyShootoutPositions[i], Speed.Walk),
              ActionSet.PRIORITY_HIGH));
          }
        }
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
    {
      if (operation != Operation.PenaltyShootoutPenaltyKick)
      {
        operation = Operation.PenaltyShootoutPenaltyKick;
        if (match.isMatchStatusActiveTeam(team))
        {
          operationPlayers[0] = team.getPlayer(
            match.getPenaltyShootoutShooterIndex(), 1);
          setActivePlayer(operationPlayers[0]);
        }
        else
          operationPlayers[0] = null;
        ActionSetFactory.applyPenaltyKick(team,
          match.isMatchStatusActiveTeam(team),
          operationPlayers[0], ball, match,
          penaltyShootoutPositions, penaltyShootoutPositions);
      }
    }
    
    
    else if (matchStatus == Match.MatchStatus.RUNNING)
    {

      //match is running

      //KICKOFF
      if (operation == Operation.KickOff)
      {
        //we are in the kickoff phase
        if (operationStep == -1)
        {
          //end operation now
          operation = Operation.None;
        }
        else if (operationStep == 1)
        {
          //perform kickoff
          ((OutfieldPlayer) operationPlayers[0]).setActionSet(
            new SimpleActionSet(new PassAction(operationPlayers[0],
              match.getBall(), new Vector3f(0, 0, -1), 0.2f, true),
              ActionSet.PRIORITY_NORMAL));
          operationStep = 2;
        }
        else if (operationStep > 1)
        {
          operationStep++;
          if (operationStep == 3)
          {
            //after 200 ms, kickoff phase is over
            operation = Operation.None;
          }
        }
      }
      
      
      //ATTACK
      else if (operation == Operation.Attack)
      {
        if (playerBall != null)
        {
          //own team is in possession of the ball
          //run to attack positions
          //defense player without ball: move forward only if ball is 15 m
          //away from goal
          for (int i = 1; i < playersCount; i++) //keeper is computed later
          {
            if (i < 3 /* defense */ && players[i].getBall() == null &&
              -cs * (ball.getPosition().x) < -field.getLength() / 2 + 15)
            {
              //stay behind, defense players!
            }
            else
            {
              //players need some reaction time (but not the ball-player)
              if (players[i].isPlaying() &&
                players[i] == playerBall || Math.random() < reactionSpeed)
              {
                //players[i].setTarget(attackPositions[i]);
                players[i].runTo(attackPositions[i], Speed.Walk);
              }
            }
          }

          if (!userControl && MathTools.distanceSquared(playerBall.getPosition(),
            goalPos) < shootDistance &&
            match.isWayFree(playerBall.getPosition(), goalPos,
              opponentTeam, playerBall, 1, 0)) //1 m clear way
          {
            //if ball-player is near enough to the goal and the
            //way to the goal is clear, shoot (only AI-team)
            float shootPower = 0.2f + (float) Math.random() / 3f;
            if (-cs * playerBall.getPosition().x > field.getLength() / 2 - 5)
              shootPower = 0.25f; //5 m before goal: weak shoot
            playerBall.setActionSet(new SimpleActionSet(
              new ShootAction(playerBall, match.getBall(),
                ShootDirection.getRandomShootDirection(),
                shootPower, cs, false), ActionSet.PRIORITY_NORMAL));
            playerBall = null;
           // operation = Operation.NONE; //operation is over
          }

          if (playerBall != null)
          {
            boolean playerBallSpeed0 = (playerBall.getSpeed() == Speed.None);
            //if way to another player is clear, maybe pass to him (only AI-Team)
            if (!userControl &&
              (playerBallSpeed0 || Math.random() < passFrequency))
            {
              //when player stands, really try to pass now. This is not
              //nice code, but works...
              int numAttempts = playerBallSpeed0 ? 4 : 1;
              //do not pass when nearer than 10 m to own goal
              if (-cs * playerBall.getPosition().x < -field.getLength() / 2 + 10)
                numAttempts = 0;
              //try it with a random player, but he may not be far behind
              for (int i = 0; i < numAttempts; i++)
              {
                int passPlayer = 0;
                /* if (playerBallSpeed0)
                  passPlayer = i;
                else */
                passPlayer = team.getPlayerIndex(1 + MathTools.randomInt(4), 1);
                if (players[passPlayer] != playerBall)
                {
                  if (-cs * (playerBall.getPosition().x -
                      players[passPlayer].getPosition().x) < 10 &&
                      match.isWayFree(ball.getPosition(),
                                      players[passPlayer].getPosition(),
                                      opponentTeam, playerBall, 1, 1))
                                      //1 m clear around player, 2 m clear
                                      //around target player
                  {
                    //playerBall.pass(players[passPlayer], false);
                    playerBall.setActionSet(
                      new SimpleActionSet(
                        new PassAction(playerBall, ball,
                          players[passPlayer], false),
                        ActionSet.PRIORITY_NORMAL));
                    
                    lastPassTime = t;
                    lastPassTarget = passPlayer;
                    //players[passPlayer].setActionSet(
                    //  ActionSetFactory.createWait(players[passPlayer]));
                    break;
                  }
                }
              }
            }

            //when own team attacks, it is not good if everybody is
            //just standing around (except the goalkeeper)...
            //if nobody moves, the ball-player
            //should run to the goal.
            boolean moving = false;
            for (int i = 1; i < playersCount; i++)
            if (players[i].isPlaying() && players[i].getSpeed() != Speed.None)
            {
              moving = true;
              break;
            }
            if (!moving)
              //playerBall.setTarget(goalPos);
              playerBall.runTo(goalPos, Speed.Run);
          }
        }
        else if (playerBallOpponent != null)
        {
          //ball was lost! find a new operation
          operation = Operation.None;
        }
        else
        {
          //nobody is in possession of the ball

          //find someone to run to the ball
          if (lastPassTarget != -1)
          {
            //pass-target-player: get ball
            if (t - lastPassTime > 2)
              lastPassTarget = -1; //after 2 seconds: do not run to ball any more
            else if (t - lastPassTime > 1)
              players[lastPassTarget].runTo(
                ball.getFutureXZPosition(), Speed.Walk);
          }
          else
          {
            //player in best position to the ball: run to it
            Player p = getBestBallPlayer(true);
            p.runTo(ball.getFutureXZPosition(), Speed.Run);
          }


        }
      }

      //DEFENSE
      else if (operation == Operation.Defense)
      {
        //run to defense positions
        for (int i = 1; i < playersCount; i++) //keeper is computer later
        {
          if (players[i].isPlaying())
          {
            //players need some reaction time
            if (Math.random() < reactionSpeed)
              players[i].runTo(defensePositions[i], Speed.Walk);
          }
        }

        
        //player in best position to the ball-player: attack him
        //(but not if a keeper has the ball in his hands!)
        Player playerAttack = getBestBallPlayer(true);
        if (!userControl && !ball.isInHands())
        {
          Vector3f v = new Vector3f(ball.getFutureXZPosition());
          if (playerBallOpponent != null)
          {
            v.x += cs * 1; //run 1 m in front of him
            //tackle, if possible
            if ((MathTools.distanceSquaredWithoutY(playerAttack.getPosition(),
                playerBallOpponent.getPosition()) < 4 || //max 2 m distance to player or...
              MathTools.distanceSquaredWithoutY(playerAttack.getPosition(), v) < 4) && //...to ball
              !playerAttack.getAnimationStage().isTackling() && 
              MathTools.random() < 0.2f &&
              !ball.isInHands())
            {
              playerAttack.setActionSet(new SimpleActionSet(
                new TackleAction(playerAttack, Speed.Run), ActionSet.PRIORITY_HIGH));
            }
          }
          playerAttack.runTo(v, Speed.Run);
        }

        //defense player: stand between opposing ball-player and goal,
        //if ball nearer than 15 m to the goal
        if (playerBallOpponent != null &&
          -cs * ball.getPosition().x < -field.getLength() / 2 + 15)
        {
          Player playerCoverGoal = team.getPlayer(2, -1); //use right defense player
          if (-cs * players[0].getPosition().z > 0)
            playerCoverGoal = team.getPlayer(2, -1); //use left defense player
          if (playerCoverGoal == playerAttack)
            //do not use attack player as goal cover player (nonsense)
            playerCoverGoal = (playerAttack == players[1] ?
              team.getPlayer(2, 1) : team.getPlayer(1, -1));
          Vector3f vCoverGoal = new Vector3f();
          vCoverGoal.sub(playerBallOpponent.getPosition(), goalOwnPos);
          vCoverGoal.normalize();
          vCoverGoal.scale(2); //2 m in front of goal
          vCoverGoal.add(goalOwnPos);
          playerCoverGoal.runTo(vCoverGoal, Speed.Run);
        }

        if (playerBall != null)
        {
          //own team got the ball now! find a new operation
          operation = Operation.None;
        }
      }
      
      else if (operation == Operation.GoalKick)
      {
        //operation is over as soon the match is running again
        //try to attack
        operation = Operation.Attack;
      }
      
      
      else if (operation == Operation.CornerKick)
      {
        //operation is over as soon the match is running again
        //try to attack
        operation = Operation.Attack;
      }
      
      
      else if (operation == Operation.ThrowIn)
      {
        //operation is over as soon the match is running again
        //try to attack
        operation = Operation.Attack;
      }
      
      
      else if (operation == Operation.PenaltyKick)
      {
        //operation is over as soon the match is running again
        //try to attack
        operation = Operation.Attack;
      }
      
      
      else if (operation == Operation.FreeKick)
      {
        //operation is over as soon the match is running again
        //try to attack
        operation = Operation.Attack;
      }
      
      
      //no operation? then find a new one!
      if (operation == Operation.None)
      {
        if (playerBall != null)
        {
          //if team has the ball, attack!
          operation = Operation.Attack;
        }
        else if (playerBallOpponent != null)
        {
          //go back to defense
          operation = Operation.Defense;
        }
      }
      else if (operation == Operation.PenaltyShootoutPenaltyKick)
      {
        //operation is over as soon as the
        //penalty shootout penalty kick was shot
        operation = Operation.None;
      }
      
      
      //GOALKEEPER
      
      float ballDistanceX = (ball.getPosition().x -
        cs * (field.getLength() / 2 - 1));
      float ballFutureZ = (ballDistanceX / -ball.getVelocity().x) *
        ball.getVelocity().z + ball.getPosition().z;
      
      //goalkeeper has the ball? kick ball away!
      if (players[0].getBall() != null)
      {
        boolean kickBall = false;
        if (players[0].isBallInHands())
        {
          //in hands? kick it away after 1 s
          if (keeperBallInHandsTime < 1)
          {
            keeperBallInHandsTime = timeNow;
          }
          else if (timeNow - keeperBallInHandsTime > 1.5f)
          {
            kickBall = true;
            keeperBallInHandsTime = 0;
          }
        }
        else
        {
          //not in hands: kick it away at once
          kickBall = true;
        }
        //kick it now
        if (kickBall)
        {
          //find a free player
          boolean found = false;
          for (int i = 1; i < playersCount; i++)
          {
            //only pass to player, if he:
            //- is playing (still on the place)
            //- does not move
            //- does not stand behind goalkeeper
            //- way to him is free
            //- and goalkeeper has ball it in his hands
            if (players[i].isPlaying() &&
              players[i].getSpeed() == Speed.None &&
              -cs * players[i].getPosition().x > -cs * players[0].getPosition().x &&
              match.isWayFree(ball.getPosition(), players[i].getPosition(),
                opponentTeam, null, 2, 4) &&
              players[0].isBallInHands())
            {
              players[0].pass(ball, players[i], true);
              found = true;
              break;
            }
          }
          if (!found)
          {
            //kick it in random direction, far away
            players[0].kick(ball, new Vector3f(
              (8 + MathTools.randomCenter(1)) * -cs,
              8 + MathTools.randomCenter(2), MathTools.randomCenter(4)),
              true, true);
            SoundEffectPool.Sound.BallPass.play();
          }
        }
      }
      
      float ballToGoalDistance =
        Math.abs(ball.getPosition().x - cs * (field.getLength() / 2));
      
      //ball without player and near the goal and near the goalkeeper and slow?
      //then run to it
      if (ball.getPlayer() == null &&
        ballToGoalDistance < 10 &&
        MathTools.distanceSquared(
          ball.getPosition(), players[0].getPosition()) < 5 * 5 &&
        ball.getVelocity().lengthSquared() < 5 * 5)
      {
        players[0].runTo(new Vector3f(ball.getPosition()),
          Speed.Walk);
        //if the ball was touched by the opponent, try to catch the ball
        if (!team.isPlayerInTeam(ball.getLastPlayer()) &&
          MathTools.distanceSquared(
            ball.getPosition(), players[0].getPosition()) < 3 * 3 &&
            !players[0].getAnimationStage().isDiving())
        {
          Vector3f jump = new Vector3f();
          jump.sub(ball.getPosition(), players[0].getPosition());
          MathTools.normalize(jump);
          float speed = 4;
          players[0].setActionSet(new SimpleActionSet(
            new DiveAction(players[0], jump, speed), ActionSet.PRIORITY_NORMAL));
        }
      }
      //place between ball and goal
      else if (ball.getPlayer() != null)
      {
        //place between player and goal center
        //keeper places on the line 1 m in front of the goal
        float distance = 1;
        Vector3f p = new Vector3f(cs * (field.getLength() / 2 - distance), 0, 0);
        Vector3f target = new Vector3f(p);
        p.sub(ball.getPosition());
        if (cs * ball.getPosition().x < cs * (field.getLength() / 2 - distance - 0.2f))
          target.z = MathTools.clamp(-p.z / p.x, -3, 3);
        if (MathTools.distanceSquared(players[0].getPosition(), target) > 1)
          players[0].runTo(target, Speed.Walk);
      }
      //ball on the way to the goal?
      else if (-cs * ball.getVelocity().x < 0)
      {
        //compute z-position of ball when it reaches the 1-m-before-goalline
        //future z-pos near the goal? then go to this position
        if (Math.abs(ballFutureZ) < 3f && ball.getPlayer() == null)
        {
          players[0].runTo(new Vector3f(cs * (field.getLength() / 2 - 1), 0, ballFutureZ),
            Speed.Walk);
        }
      }
      else
      {
        //run to normal defense position
        players[0].runTo(defensePositions[0], Speed.Walk);
      }
      
      
      //jump?
      if (!team.isPlayerInTeam(ball.getLastPlayer()) &&
        ball.getPlayer() != null &&
        !players[0].getAnimationStage().isDiving() &&
        MathTools.distanceSquared(ball.getPosition(), players[0].getPosition()) < 4)
      {
        //opponent player with ball is in front of the keeper. dive for the ball!
        Vector3f jump = new Vector3f(ball.getPosition());
        Vector3f v = ball.getVelocity();
        jump.x += v.x * 0.3f;
        jump.z += v.z * 0.3f;
        if (cs * players[0].getPosition().x < cs * (field.getLength() / 2 - 1) &&
          jump.x * cs < 0)
        {
          jump.x -= v.x * 0.3f; //do not jump behind goal line
        }
        jump.sub(players[0].getPosition());
        MathTools.normalize(jump);
        players[0].setActionSet(new SimpleActionSet(
          new DiveAction(players[0], jump, 4), ActionSet.PRIORITY_NORMAL));
      }
      else if (-cs * ball.getVelocity().x < 0)
      {
        //opponent had the last ball content and
        //ball reaches goal in the next 0.6 s? then jump!
        float timeToGoal = -1;
        if (ball.getVelocity().x * cs > 0)
          timeToGoal = Math.abs(ballToGoalDistance / ball.getVelocity().x);
        if (Math.abs(ballFutureZ) < 3 &&
          !team.isPlayerInTeam(ball.getLastPlayer()) &&
          timeToGoal > 0f && timeToGoal < 0.6f &&
          !players[0].getAnimationStage().isDiving())
        {
          Vector3f jump = new Vector3f();
          jump.z = ballFutureZ - players[0].getPosition().z;
          float speed = Math.abs(jump.z * 5);
          if (speed > 4) speed = 4;
          MathTools.normalize(jump);
          players[0].setActionSet(new SimpleActionSet(
            new DiveAction(players[0], jump, speed), ActionSet.PRIORITY_NORMAL));
        }
      }
      
    }
    
  }

  
  
  /**
   * This method is called every mainloop frame.
   * Do not use it for making decisions.
   */
  public void frame()
  {
    //player with ball is active
    Player playerBall = team.getBallPlayer();
    if (playerBall != null &&
      playerBall.getPlayerPosition() != PlayerPosition.Goalkeeper)
      setActivePlayer(playerBall);
    //active player has focus
    matchManager.setActivePlayerFocusPos(
      match.getTeamIndex(team), activePlayer.getPosition());
  }
  

  /**
   * Select the next player in the
   * best position to the ball.
   */
  public void selectBestPlayer()
  {
    setActivePlayer(getBestBallPlayer(false));
  }

  
  /**
   * Gets the currently active player.
   */
  public Player getActivePlayer()
  {
    return activePlayer;
  }
  
  
  /**
   * Change the active player.
   */
  public void setActivePlayer(Player player)
  {
    activePlayer = player;
    match.activePlayerChanged(activePlayer);
  }
  
  
  /**
   * Returns the kickoff position for a certain <code>PlayerPosition</code>.
   * This is the position a player stands before kickoff.
   */
  private Vector3f getKickoffPosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 3), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (l / 2 - 8), 0, s * (-w / 2 + 5));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (l / 2 - 8), 0, s * (w / 2 - 5));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * 5, 0, s * (-w / 2 + 6));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * 5, 0, s * (w / 2 - 6));
    ret.add(new Vector3f(
      (float) Math.random() * 2 - 1, 0, (float) Math.random() * 2 - 1));
    return ret;
  }


  /**
   * Returns the attack position for a certain <code>PlayerPosition</code>.
   * This is the position a player runs to during a normal attack.
   */
  private Vector3f getAttackPosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 5), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (-l / 2 + 15), 0, s * (-w / 2 + 7));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (-l / 2 + 15), 0, s * (w / 2 - 7));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * (-l / 2 + 6), 0, s * (-w / 2 + 4));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * (-l / 2 + 6), 0, s * (w / 2 - 4));
    ret.add(new Vector3f(
      (float) Math.random() * 2 - 1, 0, (float) Math.random() * 2 - 1));
    return ret;
  }


  /**
   * Returns the defense position for a certain <code>PlayerPosition</code>.
   * This is the position a player runs when the opponent attacks.
   */
  private Vector3f getDefensePosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 1), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (l / 2 - 5), 0, s * (-w / 2 + 7));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (l / 2 - 5), 0, s * (w / 2 - 7));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (-w / 2 + 4));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (w / 2 - 4));
    ret.add(new Vector3f(
      (float) Math.random() * 2 - 1, 0, (float) Math.random() * 2 - 1));
    return ret;
  }


  /**
   * Returns the normal position for a certain <code>PlayerPosition</code>.
   * This is the position a player runs to when there is a
   * moderate attack situation (like throw in in opponent's half)
   */
  private Vector3f getNormalPosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 3), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * 10, 0, s * (-w / 4 + 1));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * 10, 0, s * (w / 4 - 1));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * -5, 0, s * (-w / 4 + 1));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * -5, 0, s * (w / 4 - 1));
    ret.add(new Vector3f(
      (float) Math.random() * 2 - 1, 0, (float) Math.random() * 2 - 1));
    return ret;
  }
  
  
  /**
   * Returns the defense penalty kick position for a
   * certain <code>PlayerPosition</code>.
   * This is the position a player runs when the opponent has
   * a penalty kick.
   */
  private Vector3f getPenaltyDefensePosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (l / 2 - field.getPenaltyLength() - 2),
        0, s * (-w / 2 + 7));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (l / 2 - field.getPenaltyLength() - 2),
        0, s * (w / 2 - 7));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (-w / 2 + 4));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (w / 2 - 4));
    ret.add(new Vector3f(
      (float) Math.random() * 2 - 1, 0, (float) Math.random() * 2 - 1));
    return ret;
  }
  
  
  /**
   * Returns the penalty shootout (waiting) position for a
   * certain <code>PlayerPosition</code>.
   */
  private Vector3f getPenaltyShootoutPosition(PlayerPosition p)
  {
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(8, 0, s * 2);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(7.5f, 0, s * 3);
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(7.5f, 0, s * 4);
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(8.5f, 0, s * 5);
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(10, 0, s * 6);
    ret.add(new Vector3f(
      MathTools.randomCenter(0.5f), 0, MathTools.randomCenter(0.5f)));
    return ret;
  }
  
  
  /**
   * Returns the player, that is in the best position to the ball,
   * (that means, the player that can attack the ball-player
   * of the opposing team or get the ball if nobody is in
   * possession of it), but not the goalkeeper.
   * @param mayBeActivePlayer   true, if it may be the already active player
   */
  private Player getBestBallPlayer(boolean mayBeActivePlayer)
  {
    Player ret = null;
    float minPoints = Float.MAX_VALUE;
    float points;
    Ball ball = match.getBall();
    Player[] players = team.getPlayers();
    for (int i = 1; i < playersCount; i++)
    {
      if ((!mayBeActivePlayer && activePlayer == players[i])
        || !players[i].isPlaying())
      {
        //ignore active player and not playing players
      }
      else
      {
        points = MathTools.distanceSquared(ball.getPosition(),
          players[i].getPosition());
        if (points < minPoints)
        {
          minPoints = points;
          ret = players[i];
        }
      }
    }
    if (ret != null)
      return ret;
    else
      return players[0];
  }
  
  
  /**
   * LOM patch: Returns the defense position for a
   * certain <code>PlayerPosition</code> during a freekick.
   * 
   * @author Malte Löpmann
   */
  private Vector3f getFreeKickDefensePosition(PlayerPosition p)
  {   
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();  
    
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 1), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (l / 2 - 5), 0, s * (-w / 2 + 7));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (l / 2 - 5), 0, s * (w / 2 - 7));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (-w / 2 + 4));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * (l / 2 - 10), 0, s * (w / 2 - 4));
    
    ret.add(new Vector3f((float) Math.random() * 2 - 1, 0, (float) Math
        .random() * 2 - 1));
    
    //0.5m more than the referee wants to 
    float freekickWallDistance = RefereeAI.freekickWallDistance + 0.5f;
    
    while(MathTools.distance(ret, match.getMatchStatusBallPosition()) <= freekickWallDistance && p != PlayerPosition.Goalkeeper)
    {
      ret.add(new Vector3f(s * (float) Math.random() * 2 - 1, 0, (float) Math
        .random() * 2 - 1));
    }
    
    return ret;
  }
  
  
  /**
   * LOM patch: Returns the attack position for a certain
   * <code>PlayerPosition</code> during a freekick.
   * 
   * @author Malte Löpmann
   */
  private Vector3f getFreeKickAttackPosition(PlayerPosition p)
  {
    float l = field.getLength();
    float w = field.getWidth();
    int s = team.getCurrentSide();
    Vector3f ret;
    if (p == PlayerPosition.Goalkeeper)
      ret = new Vector3f(s * (l / 2 - 5), 0, 0);
    else if (p == PlayerPosition.DefenderLeft)
      ret = new Vector3f(s * (-l / 2 + 15), 0, s * (-w / 2 + 7));
    else if (p == PlayerPosition.DefenderRight)
      ret = new Vector3f(s * (-l / 2 + 15), 0, s * (w / 2 - 7));
    else if (p == PlayerPosition.ForwardLeft)
      ret = new Vector3f(s * (-l / 2 + 6), 0, s * (-w / 2 + 4));
    else //if (p == PlayerPosition.ForwardRight)
      ret = new Vector3f(s * (-l / 2 + 6), 0, s * (w / 2 - 4));
    ret.add(new Vector3f((float) Math.random() * 2 - 1, 0,
      (float) Math.random() * 2 - 1));
    return ret;
  }
  
}