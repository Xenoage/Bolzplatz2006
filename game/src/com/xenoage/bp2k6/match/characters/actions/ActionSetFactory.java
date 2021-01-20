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
package com.xenoage.bp2k6.match.characters.actions;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.camera.CameraView;
import com.xenoage.bp2k6.match.characters.Character;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.PlayerSettings;
import com.xenoage.bp2k6.match.characters.enums.Emotion;
import com.xenoage.bp2k6.match.characters.enums.Speed;
import com.xenoage.bp2k6.match.stadium.Field;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.match.stadium.attractions.BeerBar;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.math.NormalDistribution;


/**
 * This class contains methods that create
 * often needed or complicated ActionSets.
 * 
 * @author Andreas Wenger
 */
public class ActionSetFactory
{

  /**
   * Creates a "none" action set with low priority.
   */
  public static ActionSet createNo()
  {
    return new SimpleActionSet(
      new NoAction(), ActionSet.PRIORITY_LOW);
  }
  
  
  /**
   * Creates a "waiting" action set with low priority.
   */
  public static ActionSet createWait(
    com.xenoage.bp2k6.match.characters.Character character)
  {
    return new SimpleActionSet(
      new WaitAction(character), ActionSet.PRIORITY_LOW);
  }
  
  
  /**
   * Creates and applies a kick off positions action (at the beginning
   * of the game and after halftime).
   * The players and the referee are set to the stadium's entrance position,
   * then they run to the kick off position.
   */
  public static void applyKickOffPosition(
    Team team, boolean activeTeam, boolean fromEntrance,
    Stadium stadium, Match match,
    Vector3f kickoffPositions[], Player operationPlayers[])
  {
    int side = team.getCurrentSide(); //sides have changed already
    Player players[] = team.getPlayers();
    Vector3f fieldBorder = stadium.getFieldBorderPosition();
    fieldBorder.x = side * 1f;
    Vector3f entrance = stadium.getEntrancePosition();
    if (fromEntrance)
    {
      //align in a row
      for (int i = 0; i < 5; i++)
      {
        players[i].setPosition(new Vector3f(
          entrance.x + side * 1f, 0, entrance.z + i * 2));
      }
      match.getReferee().setPosition(new Vector3f(
        entrance.x, 0, entrance.z - 1));
    }
    //players (not the 2 active ones) run to kickoff positions
    int max = (activeTeam ? 3 : 5);
    for (int i = 0; i < max; i++)
    {
      if (players[i].isPlaying())
      {
        ArrayList<Action> actions = new ArrayList<Action>();
        if (fromEntrance)
        {
          actions.add(new RunToAction(players[i], fieldBorder, Speed.Walk));
        }
        actions.add(new RunToAction(players[i], kickoffPositions[i], Speed.Walk));
        players[i].setActionSet(new GeneralActionSet(actions, ActionSet.PRIORITY_NORMAL));
      }
    }
    if (activeTeam)
    {
      //go to center line
      operationPlayers[0].setActionSet(new SimpleActionSet(
        new RunToAction(operationPlayers[0],
          match.getKickoffPlayerPosition(0, side), Speed.Walk),
        ActionSet.PRIORITY_NORMAL));
      operationPlayers[1].setActionSet(new SimpleActionSet(
        new RunToAction(operationPlayers[1],
          match.getKickoffPlayerPosition(1, side), Speed.Walk),
        ActionSet.PRIORITY_NORMAL));
      //this team touches the ball
      match.getBall().setLastPlayer(operationPlayers[0]);
    }
  }
  
  
  /**
   * Creates and applies a half time action set to the given team:
   * All players of the given team run to stadium's entrance position.
   * If the team lies back or is leading, perhaps show an
   * emotion before.
   */
  public static void applyHalfTime(
    Team team, Stadium stadium, int goalDifference)
  {
    int side = -team.getCurrentSide(); //sides have changed already
    Vector3f fieldBorder = stadium.getFieldBorderPosition();
    fieldBorder.x = side * 1f;
    Player players[] = team.getPlayers();
    for (int i = 0; i < team.getPlayersCount(); i++)
    {
      if (players[i].isPlaying())
      {
        ArrayList<Action> actions = new ArrayList<Action>();
        if (goalDifference > 0 &&
          MathTools.random() * (goalDifference) > 0.2f)
        {
          //player is happy
          actions.add(new EmotionAction(players[i], Emotion.Elation, false));
        }
        else if (goalDifference < 0 &&
          MathTools.random() * (-goalDifference) > 0.2f)
        {
          //player is sad
          actions.add(new EmotionAction(players[i], Emotion.Desperation, false));
        }
        //run to border of field
        actions.add(new RunToAction(players[i],
          fieldBorder, Speed.Walk));
        //run to entrance position
        actions.add(new RunToAction(players[i],
          stadium.getEntrancePosition(), Speed.Walk));
        players[i].setActionSet(
          new GeneralActionSet(actions, ActionSet.PRIORITY_HIGH));
      }
    }
  }
  
  
  /**
   * Creates and applies a goal elation action set to the given team:
   * All players of the given (apart from the goalkeeper) run to
   * a certain position, jubilate, and run back to kick off
   * positions.
   */
  public static void applyGoalElation1(
    Team team, Field field, int goalDifference,
    Vector3f kickoffPositions[])
  {
    int side = team.getCurrentSide();
    Vector3f posElation = new Vector3f(
      -side * (field.getLength() / 2 - 8), 0, -5);
    //outfield players: emotion
    //goalkeeper has no emotion
    boolean sound = true; //(team.getTeamControl() == TeamControl.Player1);
    for (int i = 1; i < team.getPlayersCount(); i++)
    {
      Player player = team.getPlayers()[i];
      if (player.isPlaying())
      {
        ArrayList<Action> actions = new ArrayList<Action>();
        Vector3f position = new Vector3f(posElation);
        position.x += MathTools.randomCenter(2);
        position.z += MathTools.randomCenter(2);
        actions.add(new RunToAction(player, position, Speed.Run));
        //the nearer the player is to the elation point,
        //the longer he jubilates (so that they can jubilate together,
        //becase players that are further away need longer to get there)
        int distance = (int)
          (MathTools.distance(posElation, player.getPosition()) * 0.1f);
        if (distance > 1) distance = 1;
        for (int i2 = 2; i2 > distance; i2--)
        {
          actions.add(new EmotionAction(player, Emotion.Elation, sound));
          sound = false;
        }
        actions.add(new RunToAction(player, kickoffPositions[i], Speed.Walk));
        player.setActionSet(new GeneralActionSet(actions,
          ActionSet.PRIORITY_HIGH));
      }
    }
  }
  
  
  /**
   * Creates and applies a goalkick action set to the given team:
   * Active team:
   * The goalkeeper runs behind the ball, then:
   * If not user-controlled team:
   *   Waits 2s, runs to the ball and kicks it.
   * If user-controlled team:
   *   Change camera to goalkick view and give
   *   the control to the user.
   * All other players run to the normal positions.
   * Non-active team: Run to normal positions.
   */
  public static void applyGoalKick(
    Team team, boolean activeTeam, Ball ball, Match match,
    Vector3f normalPositions[])
  {
    int side = team.getCurrentSide();
    Vector3f goalKickBallPosition = match.getMatchStatusBallPosition();
    //goalkeeper
    Player player = team.getPlayers()[0];
    if (activeTeam)
    {
      //active goalkeeper: run behind the ball, wait 2s,
      //run to the ball and kick it
      ArrayList<Action> actions = new ArrayList<Action>();
      //wait 1s
      actions.add(new WaitAction(player, 1f));
      //set ball to goal kick position
      actions.add(new BallAction(ball,
        goalKickBallPosition, new Vector3f(), true));
      //run behind ball
      Vector3f position = new Vector3f(goalKickBallPosition);
      position.x += side * 2.5f;
      actions.add(new RunToAction(player, position, Speed.Walk));
      //team controlled by user?
      if (team.getTeamControl() == TeamControl.AI)
      {
        //AI team
        //wait 2 s
        actions.add(new WaitAction(player, 2f));
        //run to the ball
        actions.add(new RunToAction(player, goalKickBallPosition, Speed.Walk));
        //kick the ball
        float power = 0.25f + (MathTools.random() * 0.5f) ;
        Vector3f dir = new Vector3f(-side, 0, MathTools.randomCenter(0.4f));
        MathTools.normalize(dir);
        dir.scale(PlayerSettings.getGoalkickSpeedHMin() +
          power * (PlayerSettings.getGoalkickSpeedHMax() -
            PlayerSettings.getGoalkickSpeedHMin()));
        dir.y = PlayerSettings.getGoalkickSpeedVMin() + power *
          (PlayerSettings.getGoalkickSpeedVMax() -
            PlayerSettings.getGoalkickSpeedVMin());
        actions.add(new KickAction(player, ball, dir, true, true,
          (power < 0.6 ? SoundEffectPool.Sound.BallPass :
            SoundEffectPool.Sound.BallShoot)));
      }
      else
      {
        //user team
        //camera: cornerkick view
        actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
          CameraView.GoalKick));
        //wait for user's kick
        actions.add(new WaitForUserBallKickAction(team.getPlayers()[0]));
        //camera: match view
        actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
          CameraView.Match));
      }
      ball.setLastPlayer(player); //keeper touches the ball
      //after this the action is performed
      actions.add(new MatchStatusActionReadyAction(match, team));
      player.setActionSet(new GeneralActionSet(actions,
        ActionSet.PRIORITY_HIGH));
    }
    else
    {
      //inactive goalkeeper: run to normal positions
      player.setActionSet(new SimpleActionSet(
        new RunToAction(player, normalPositions[0], Speed.Walk),
        ActionSet.PRIORITY_NORMAL));
    }
    //outfield players
    //run to normal positions
    for (int i = 1; i < team.getPlayersCount(); i++)
    {
      player = team.getPlayers()[i];
      if (player.isPlaying())
      {
        player.setActionSet(new SimpleActionSet(
          new RunToAction(player, normalPositions[i], Speed.Walk),
          ActionSet.PRIORITY_NORMAL));
      }
    }
  }
  
  
  /**
   * Creates and applies a cornerkick action set to the given team:
   * Active team:
   * The given player runs to the corner, then:
   * If not user-controlled team:
   *   When there, he waits a moment and runs
   *   to the ball and kicks the ball in front
   *   of the goal.
   * If user controlled-team:
   *   Change camera to cornerkick view and give
   *   the control to the user.
   * The other players run to the attack positions.
   * Non-active team:
   * Run to defense positions (activeCornerKickPlayer ignored)
   */
  public static void applyCornerKick(Team team, boolean activeTeam,
    Player activeCornerKickPlayer, Ball ball, Match match,
    Vector3f attackPositions[], Vector3f defensePositions[])
  {
    int side = team.getCurrentSide();
    Vector3f cornerKickBallPosition = match.getMatchStatusBallPosition();
    Vector3f cornerKickPlayerPosition = new Vector3f(
      -side * (match.getStadium().getField().getLength() / 2 + 2), 0,
      side * match.getCornerGoalKickCorner() * (match.getStadium().getField().getWidth() / 2 + 2));
    //active player
    if (activeTeam && activeCornerKickPlayer != null)
    {
      //active player: run to the corner, wait 1s,
      //run to the ball and kick it
      ArrayList<Action> actions = new ArrayList<Action>();
      //wait 1 s
      actions.add(new WaitAction(activeCornerKickPlayer, 1f));
      //gag: if there is a beer bar near the corner, sometimes run to
      //it and drink a beer, before the corner kick is performed
      BeerBar beerBar = match.getStadium().getBeerBar();
      if (Math.random() < 0.2f && beerBar != null &&
        MathTools.distance(beerBar.getPosition(), cornerKickPlayerPosition) < 10f)
      {
        //run to beer bar
        Vector3f c = cornerKickPlayerPosition;
        Vector3f b = beerBar.getPosition();
        Vector3f p = new Vector3f();
        p.x = c.x + (b.x - c.x - 2.2f);
        p.z = c.z + (b.z - c.z - 2.2f);
        actions.add(new RunToAction(activeCornerKickPlayer, p, Speed.Walk));
        //drink a beer
        actions.add(new BallAction(ball, b, new Vector3f(), false));
        actions.add(new SoundAction(SoundEffectPool.Sound.SpecialBeer));
        actions.add(new WaitAction(activeCornerKickPlayer, 6));
      }
      //set ball to corner kick position
      actions.add(new BallAction(ball,
        cornerKickBallPosition, new Vector3f(), true));
      //run behind ball
      actions.add(new RunToAction(activeCornerKickPlayer,
        cornerKickPlayerPosition, Speed.Walk));
      //team controlled by user?
      if (team.getTeamControl() == TeamControl.AI)
      {
        //AI team
        //wait 1 s
        actions.add(new WaitAction(activeCornerKickPlayer, 1f));
        //run to the ball
        actions.add(new RunToAction(activeCornerKickPlayer,
          cornerKickBallPosition, Speed.Walk));
        //kick the ball
        float power = 0.15f + (MathTools.random() * 0.5f);
        Vector3f dir = new Vector3f();
        dir.x = side * (2f + MathTools.randomCenter(2.5f));
        dir.z = -side * match.getCornerGoalKickCorner() * 6;
        MathTools.normalize(dir);
        dir.scale(PlayerSettings.getCornerkickSpeedHMin() +
          power * (PlayerSettings.getCornerkickSpeedHMax() -
            PlayerSettings.getCornerkickSpeedHMin()));
        dir.y = PlayerSettings.getCornerkickSpeedVMin() + power *
          (PlayerSettings.getCornerkickSpeedVMax() -
            PlayerSettings.getCornerkickSpeedVMin());
        actions.add(new KickAction(activeCornerKickPlayer, ball, dir, true, true,
          (power < 0.6 ? SoundEffectPool.Sound.BallPass :
            SoundEffectPool.Sound.BallShoot)));
      }
      else
      {
        //user team
        //camera: cornerkick view
        actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
          CameraView.CornerKick));
        //wait for user's kick
        actions.add(new WaitForUserBallKickAction(activeCornerKickPlayer));
        //camera: match view
        actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
          CameraView.Match));
      }
      ball.setLastPlayer(activeCornerKickPlayer); //player touches the ball
      //after this the action is performed
      actions.add(new MatchStatusActionReadyAction(match, team));
      activeCornerKickPlayer.setActionSet(new GeneralActionSet(actions,
        ActionSet.PRIORITY_NORMAL));
    }
    //other players:
    //run to attack or defense positions
    for (int i = 0; i < team.getPlayersCount(); i++)
    {
      Player player = team.getPlayers()[i];
      if (player.isPlaying() && player != activeCornerKickPlayer)
      {
        Vector3f target = new Vector3f();
        if (activeTeam)
        {
          //active team runs to attack positions
          //but player 1 defends the room behind
          if (i != 1)
          {
            target.set(attackPositions[i]);
          }
          else
          {
            target.set(0, 0, 0);
          }
        }
        else
        {
          //nonactive team runs to defense positions
          target.set(defensePositions[i]);
        }
        player.setActionSet(new SimpleActionSet(
          new RunToAction(player, target, Speed.Walk),
          ActionSet.PRIORITY_NORMAL));
      }
    }
  }
  
  
  /**
   * Creates and applies a throw in action set to the given team:
   * Active team:
   * The given player runs to the throw in position,
   * when there, he waits 1 s and throws the ball in.
   * If throw in is in own half, the defense player(s) run to the defense positions,
   * and the forward player(s) place around the throw in positions.
   * If throw in is in the opponent's half, the defense player(s) run to default
   * positions, and the forward player(s) place around the throw in positions.
   * Non-active team:
   * If throw in is in own half, the defense player(s) run to the defense positions,
   * and one forward player runs to the normal position, the other tries to cover
   * the throw-in-target.
   * If throw in is in the opponent's half, the defense player(s) run to default
   * positions, and one forward player runs to the normal position,
   * the other tries to cover the throw-in-target.
   * If it is a user-controlled team, the user-player can run to where he wants,
   * but the throw-in-player can not be controlled. If the user-team has throw-in,
   * the throw-in is performed (ball is thrown to the active player) as soon
   * as the user presses button 1.
   */
  public static void applyThrowIn(Team team, Team opponentTeam,
    boolean activeTeam, Player activeThrowInPlayer, Ball ball, Match match,
    Vector3f defensePositions[], Vector3f normalPositions[])
  {
    int side = team.getCurrentSide();
    
    //throw in ball position
    Vector3f throwInBallPosition = match.getMatchStatusBallPosition();
    
    //throw in on front or back side?
    int z = throwInBallPosition.z > 0 ? 1 : -1;
    
    //throw in player position
    Vector3f throwInPlayerPosition = new Vector3f(throwInBallPosition);
    throwInPlayerPosition.z += z * 0.8f;
    
    //target player position: place 5 m away from the line
    Vector3f targetPlayerPosition = new Vector3f(throwInBallPosition);
    targetPlayerPosition.z -= z * 5f;
    if (Math.abs(targetPlayerPosition.x) > match.getStadium().getField().getLength() / 2 - 4)
    {
      //if throw in is less then 4 m away from goal-line, place
      //target players more inwards
      targetPlayerPosition.x = targetPlayerPosition.x > 0 ?
        targetPlayerPosition.x - 3 : targetPlayerPosition.x + 3;
    }
      
    //active throw-in player
    if (activeTeam && activeThrowInPlayer != null)
    {
      //active player: run to the throw in position,
      //take the ball, wait 1s, and throw it in
      ArrayList<Action> actions = new ArrayList<Action>();
      //run to the throw in position
      actions.add(new RunToAction(activeThrowInPlayer,
        throwInPlayerPosition, Speed.Walk));
      //"take" the ball
      Vector3f ballPos = new Vector3f(throwInBallPosition);
      ballPos.y = 1f;
      actions.add(new BallAction(ball,
        ballPos, new Vector3f(), false));
      //wait 1 s
      actions.add(new WaitAction(activeThrowInPlayer, 1f));
      //team controlled by user?
      if (team.getTeamControl() == TeamControl.AI)
      {
        //AI team
        //throws the ball in, in random direction
        float power = 0.15f + (MathTools.random() * 0.5f);
        Vector3f dir = new Vector3f(
          MathTools.randomCenter(1f), 0, -z * MathTools.random());
        float length = match.getStadium().getField().getLength();
        //don't throw over goal line
        if ((throwInBallPosition.x < -length / 2 + 5 && dir.x < 0) ||
          (throwInBallPosition.x > length / 2 - 5 && dir.x > 0))
          dir.x = -dir.x;
        MathTools.normalize(dir); 
        dir.scale(PlayerSettings.getThrowinSpeedHMin() +
          power * (PlayerSettings.getThrowinSpeedHMax() -
            PlayerSettings.getThrowinSpeedHMin()));
        dir.y = PlayerSettings.getThrowinSpeedVMin() + power *
          (PlayerSettings.getThrowinSpeedVMax() -
            PlayerSettings.getThrowinSpeedVMin());
        actions.add(new BallAction(ball, null, null, true));
        actions.add(new KickAction(activeThrowInPlayer, ball, dir, false, true,
          null));
      }
      else
      {
        //user team
        //wait for user's kick
        actions.add(new WaitForUserBallKickAction(activeThrowInPlayer));
      }
      ball.setLastPlayer(activeThrowInPlayer); //player touches the ball
      //after this the action is performed
      actions.add(new MatchStatusActionReadyAction(match, team));
      activeThrowInPlayer.setActionSet(new GeneralActionSet(actions,
        ActionSet.PRIORITY_HIGH));
    }
    
    //is this the active team?
    if (match.isMatchStatusActiveTeam(team))
    {
      //move active team
      //defense players
      if (throwInBallPosition.x * side > 0)
      {
        //throw in is in our half. run to defense positions.
        for (int i = 1; i < 3; i++)
        {
          Player player = team.getPlayers()[i];
          if (player.isPlaying() && player != activeThrowInPlayer)
          {
            player.setActionSet(new SimpleActionSet(
              new RunToAction(player, defensePositions[i], Speed.Walk),
              ActionSet.PRIORITY_NORMAL));
          }
        }
      }
      else
      {
        //throw in is not in our half. run to moderate attack positions.
        for (int i = 1; i < 3; i++)
        {
          Player player = team.getPlayers()[i];
          if (player.isPlaying() && player != activeThrowInPlayer)
          {
            player.setActionSet(new SimpleActionSet(
              new RunToAction(player, normalPositions[i], Speed.Walk),
              ActionSet.PRIORITY_NORMAL));
          }
        }
      }
      Vector3f p = new Vector3f();
      //forward players: throw-in-targets
      for (int i = 3; i < 5; i++)
      {
        Player player = team.getPlayers()[i];
        if (player.isPlaying() && player != activeThrowInPlayer)
        {
          p = new Vector3f(targetPlayerPosition);
          p.x += (i == 3 ? -1 : 1) * 3 + MathTools.randomCenter(2);
          p.z += MathTools.randomCenter(2);
          player.setActionSet(new SimpleActionSet(
            new RunToAction(player, p, Speed.Walk),
            ActionSet.PRIORITY_NORMAL));
        }
      }
    }
    else
    {
      //move non-active team
      Player players[] = team.getPlayers();
      //defense players
      if (throwInBallPosition.x * side > 0)
      {
        //throw in is in our half. run to defense positions.
        for (int i = 1; i < 3; i++)
        {
          if (players[i].isPlaying())
          {
            players[i].setActionSet(new SimpleActionSet(
              new RunToAction(players[i], defensePositions[i], Speed.Walk),
              ActionSet.PRIORITY_NORMAL));
          }
        }
      }
      else
      {
        //throw in is not in our half. run to moderate attack positions.
        for (int i = 1; i < 3; i++)
        {
          if (players[i].isPlaying())
          {
            players[i].setActionSet(new SimpleActionSet(
              new RunToAction(players[i], normalPositions[i], Speed.Walk),
              ActionSet.PRIORITY_NORMAL));
          }
        }
      }
      //1. forward player: run to moderate attack position
      if (players[3].isPlaying())
      {
        players[3].setActionSet(new SimpleActionSet(
          new RunToAction(players[3], normalPositions[3], Speed.Walk),
          ActionSet.PRIORITY_NORMAL));
      }
      //2. forward player: should cover throw-in-target,
      //but at the moment random position...
      if (players[4].isPlaying())
      {
        Vector3f throwInCover = new Vector3f(targetPlayerPosition);
        throwInCover.z += (throwInCover.z > 0 ? -1 : 1) * 4;
        throwInCover.x += MathTools.randomCenter(4);
        players[4].setActionSet(new SimpleActionSet(
          new RunToAction(players[4], throwInCover, Speed.Walk),
          ActionSet.PRIORITY_NORMAL));
      }
    }
    
  }
  
  
  
  /**
   * Creates and applies a penalty kick action set to the given team:
   * Active team:
   *   The given player runs a few meters near to the penalty spot.
   *   When there, he waits for the referee whistle and kicks the ball.
   *   Then the operation ends.
   *   The other players run to the normal positions.
   * Non-active team:
   *   Some players may be angry, then
   *   all players tun to the defense positions.
   */
  public static void applyPenaltyKick(Team team, boolean activeTeam,
    Player activePenaltyKickPlayer, Ball ball, Match match,
    Vector3f activePositions[], Vector3f nonactivePositions[])
  {
    Vector3f penaltySpot = new Vector3f(match.getMatchStatusBallPosition());
    //side: -1 when shoot onto left goal, 1 when shoot onto right goal
    int side = (penaltySpot.x > 0 ? 1 : -1);
    if (activeTeam)
    {
      //active player
      ArrayList<Action> actions = new ArrayList<Action>();
      //run to a position near the penalty spot
      Vector3f penaltyPlayerPos = new Vector3f(penaltySpot);
      penaltyPlayerPos.x += -side * 3;
      penaltyPlayerPos.z += side * 1;
      actions.add(new RunToAction(activePenaltyKickPlayer,
        penaltyPlayerPos, Speed.Walk));
      //set ball to penalty spot
      actions.add(new BallAction(match.getBall(),
        penaltySpot, new Vector3f(), true));
      //camera: penalty kick view
      actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
        CameraView.PenaltyKickFront));
      //wait for referee's whistle
      actions.add(new WaitForRefereeReadyAction(activePenaltyKickPlayer,
        match.getReferee()));
      //team controlled by user?
      if (team.getTeamControl() == TeamControl.AI)
      {
        //AI team
        //wait 2 s
        actions.add(new WaitAction(activePenaltyKickPlayer, 2f));
        //run to ball
        actions.add(new RunToAction(activePenaltyKickPlayer,
          penaltySpot, Speed.Walk));
        //kick the ball
        float power;
        Vector3f dir = new Vector3f();
        dir.x = side;
        if (NormalDistribution.random10() >
          activePenaltyKickPlayer.getInfo().getAttack())
        {
          //random shot (bad)
          power = MathTools.random();
          dir.z = MathTools.randomCenter(0.5f);
        }
        else
        {
          //good placed kick
          power = 0.5f + (MathTools.random() * 0.25f);
          dir.z = MathTools.random() < 0.5f ? -0.2f : 0.2f;
        }
        MathTools.normalize(dir);
        dir.scale(PlayerSettings.getPenaltykickSpeedHMin() +
          power * (PlayerSettings.getPenaltykickSpeedHMax() -
            PlayerSettings.getPenaltykickSpeedHMin()));
        dir.y = PlayerSettings.getPenaltykickSpeedVMin() +
          power * (PlayerSettings.getPenaltykickSpeedVMax() -
            PlayerSettings.getPenaltykickSpeedVMin());
        actions.add(new KickAction(activePenaltyKickPlayer, ball, dir, true, true,
          (power < 0.6 ? SoundEffectPool.Sound.BallPass :
            SoundEffectPool.Sound.BallShoot)));
      }
      else
      {
        //user team
        //wait for user's kick
        actions.add(new WaitForUserBallKickAction(activePenaltyKickPlayer));
      }
      
      ball.setLastPlayer(activePenaltyKickPlayer); //player touches the ball
      //keeper reaction time
      actions.add(new WaitAction(activePenaltyKickPlayer,
        0.12f + 0.1f * MathTools.random()));
      //after this the action is performed
      actions.add(new MatchStatusActionReadyAction(match, team));
      activePenaltyKickPlayer.setActionSet(new GeneralActionSet(actions,
        ActionSet.PRIORITY_HIGH));
      //wait 1 s
      actions.add(new WaitAction(activePenaltyKickPlayer, 1f));
      //camera: match view
      actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
        CameraView.Match));
    }
    //other players:
    //run to given positions
    for (int i = 0; i < team.getPlayersCount(); i++)
    {
      Player player = team.getPlayers()[i];
      if (player.isPlaying() && player != activePenaltyKickPlayer)
      {
        if (i == 0 && !activeTeam)
        {
          //non-active-team goalkeeper: run into goal!
          float fieldLength = match.getStadium().getField().getLength();
          player.setActionSet(new SimpleActionSet(
            new RunToAction(player,
              new Vector3f(side * (fieldLength / 2 - 0.8f), 0, MathTools.randomCenter(0.3f)),
              Speed.Walk),
            ActionSet.PRIORITY_HIGH));
        }
        else
        {
          player.setActionSet(new SimpleActionSet(
            new RunToAction(player,
              (activeTeam ? activePositions[i] : nonactivePositions[i]),
              Speed.Walk),
            ActionSet.PRIORITY_HIGH));
        }
      }
    }
      
  }
  
  
  
  /**
   * Creates a free kick action set.
   */
  public static void applyFreeKick(Team team, boolean activeTeam,
    Player activeFreeKickPlayer, Ball ball, Match match,
    Vector3f activePositions[], Vector3f nonactivePositions[])
  {
    int side = team.getCurrentSide();
    //free kick position
    Vector3f freeKickPos = new Vector3f(match.getMatchStatusBallPosition());
    //near to the goal?
    Field field = match.getStadium().getField();
    boolean nearToGoal = (-side * freeKickPos.x > field.getLength() / 2 - 15f);
    //direction to the goal
    Vector3f dirToGoal = new Vector3f(
      -side * field.getLength() / 2 - freeKickPos.x, 0,
      -freeKickPos.z + MathTools.randomCenter(3));
    MathTools.normalize(dirToGoal);
    //is this the active team?
    if (activeTeam)
    {
      //active player
      ArrayList<Action> actions = new ArrayList<Action>();
      //run to a position near the free kick spot
      Vector3f freekickPlayerPos = new Vector3f(dirToGoal);
      freekickPlayerPos.scale(-3);
      freekickPlayerPos.add(freeKickPos);
      actions.add(new RunToAction(activeFreeKickPlayer,
        freekickPlayerPos, Speed.Walk));
      //set ball to free kick position
      actions.add(new BallAction(match.getBall(),
        freeKickPos, new Vector3f(), true));
      //team controlled by user?
      if (team.getTeamControl() == TeamControl.AI)
      {
        //AI team
        //wait for referee's whistle
        actions.add(new WaitForRefereeReadyAction(activeFreeKickPlayer,
          match.getReferee()));
        //wait 2 s
        actions.add(new WaitAction(activeFreeKickPlayer, 2f));
        //run to ball
        actions.add(new RunToAction(activeFreeKickPlayer,
          freeKickPos, Speed.Walk));
        //kick the ball
        float power;
        Vector3f dir = new Vector3f();
        if (!nearToGoal || MathTools.random() < 0.3f)
        {
          //pass to free player
          Character target = team.getRandomForwardPlayer(activeFreeKickPlayer);
          actions.add(new PassAction(activeFreeKickPlayer, ball, target, true));
        }
        else
        {
          //goal shoot
          dir.set(dirToGoal);
          power = 0.5f + (MathTools.random() * 0.5f);
          dir.scale(PlayerSettings.getFreekickSpeedHMin() +
            power * (PlayerSettings.getFreekickSpeedHMax() -
              PlayerSettings.getFreekickSpeedHMin()));
          dir.y = PlayerSettings.getFreekickSpeedVMin() +
            power / 2 * (PlayerSettings.getFreekickSpeedVMax() -
              PlayerSettings.getFreekickSpeedVMin());
          actions.add(new KickAction(activeFreeKickPlayer, ball, dir, true, true,
            (power < 0.6 ? SoundEffectPool.Sound.BallPass :
              SoundEffectPool.Sound.BallShoot)));
        }
      }
      else
      {
        //user team
        //if near to the goal: camera: free kick view
        if (true) //(nearToGoal)
        {
          actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
            CameraView.FreeKick));
        }
        //wait for user's kick
        actions.add(new WaitForUserBallKickAction(activeFreeKickPlayer));
        //if near to the goal: change chamera back to match view
        if (true) //(nearToGoal)
        {
          actions.add(new CameraAction(GameEngine.getMatchManager().getCamera(),
            CameraView.Match));
        }
      }
      
      ball.setLastPlayer(activeFreeKickPlayer); //player touches the ball
      //keeper reaction time
      actions.add(new WaitAction(activeFreeKickPlayer,
        0.12f + 0.1f * MathTools.random()));
      //after this the action is performed
      actions.add(new MatchStatusActionReadyAction(match, team));
      activeFreeKickPlayer.setActionSet(new GeneralActionSet(actions,
        ActionSet.PRIORITY_HIGH));
    }
    //other players:
    //run to given positions
    for (int i = 0; i < team.getPlayersCount(); i++)
    {
      Player player = team.getPlayers()[i];
      if (player.isPlaying() && player != activeFreeKickPlayer)
      {
        player.setActionSet(new SimpleActionSet(
          new RunToAction(player,
            (activeTeam ? activePositions[i] : nonactivePositions[i]),
            Speed.Walk),
          ActionSet.PRIORITY_HIGH));
      }
    }
  }
  
  
  
  /**
   * Creates and applies a final whistle action set to the given team:
   * The players show emotion.
   * Could be extended: more actions, like running to the fancurve, and so on
   */
  public static void applyFinalWhistle1(
    Team team, Stadium stadium, int goalDifference)
  {
    for (int i = 0; i < team.getPlayersCount(); i++)
    {
      Player player = team.getPlayers()[i];
      if (player.isPlaying())
      {
        ArrayList<Action> actions = new ArrayList<Action>();
        if (goalDifference > 0 &&
          MathTools.random() * (goalDifference) > 0.2f)
        {
          //player is happy
          actions.add(new EmotionAction(player, Emotion.Elation, false));
        }
        else if (goalDifference < 0 &&
          MathTools.random() * (-goalDifference) > 0.2f)
        {
          //player is sad
          actions.add(new EmotionAction(player, Emotion.Desperation, false));
        }
        //wait (action set must have at least one action)
        actions.add(new WaitAction(player));
        player.setActionSet(
          new GeneralActionSet(actions, ActionSet.PRIORITY_HIGH));
      }
    }
  }
  
  
  /**
   * Creates and applies a red card action set to the given player:
   * He waits 4 seconds, rails and shows a sad emotion,
   * then he leaves the stadium.
   */
  public static void applyRedCard(Player player, Stadium stadium)
  {
    ArrayList<Action> actions = new ArrayList<Action>();
    //wait 4 seconds
    actions.add(new WaitAction(player, 4));
    //player is sad
    actions.add(new EmotionAction(player, Emotion.Desperation, true));
    //run to border of field
    Vector3f fieldBorder = stadium.getFieldBorderPosition();
    actions.add(new RunToAction(player,
      fieldBorder, Speed.Walk));
    //run to entrance position
    actions.add(new RunToAction(player,
      stadium.getEntrancePosition(), Speed.Walk));
    //hide player
    actions.add(new VisibilityAction(player, false));
    player.setActionSet(
      new GeneralActionSet(actions, ActionSet.PRIORITY_VERYHIGH));
  }
  
}
