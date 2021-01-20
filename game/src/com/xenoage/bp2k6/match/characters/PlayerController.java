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

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.gui.EnergyBar;
import com.xenoage.bp2k6.input.Input;
import com.xenoage.bp2k6.match.Team;
import com.xenoage.bp2k6.match.TeamControl;
import com.xenoage.bp2k6.match.Match.MatchStatus;
import com.xenoage.bp2k6.match.characters.actions.*;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.match.stadium.Field;
import com.xenoage.bp2k6.util.MathTools;


/**
 * This class lets the user control the active
 * player of his team.
 * 
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM)
 */
public class PlayerController
{
  private Input input;
  private int user;
  private Team team;
  
  private boolean lastButton1Down = false;
  private boolean lastButton2Down = false;
  private int arrowKeysRun;
  private int arrowKeysShoot;
  private boolean passKeyDown = false;
  private boolean shootKeyDown = false;
  
  private boolean matchStatusBallKicked = false;
  private MatchStatus lastMatchStatus = null;
  
  private float goalKickAngle;
  private float cornerKickAngle;
  private float freeKickAngle;
  
  
  
  /**
   * Creates a controller for the given user (0 or 1)
   * and the given team.
   */
  public PlayerController(Input input, int user, Team team)
  {
    this.input = input;
    this.user = user;
    this.team = team;
  }
  
  
  /**
   * Call this method in every frame when a match
   * is running.
   */
  public void step(com.xenoage.bp2k6.match.Match match,
    float deltaTime)
  {
    
    //energy bar
    EnergyBar energyBar = match.getEnergyBar();
    
    //read arrow keys
    int arrowKeys = input.getArrowKeys(user);
    
    //read action keys
    boolean button1Pressed = input.isPressed(user, input.button1);
    boolean button2Pressed = input.isPressed(user, input.button2);
    boolean button3Pressed = input.isPressed(user, input.button3);

    //key up "event"
    boolean button1KeyUp = (!button1Pressed && lastButton1Down);
    boolean button2KeyUp = (!button2Pressed && lastButton2Down);

    //key down "event"
    boolean button1KeyDown = (button1Pressed && !lastButton1Down);
    boolean button2KeyDown = (button2Pressed && !lastButton2Down);

    //save button states for next step() call
    lastButton1Down = button1Pressed;
    lastButton2Down = button2Pressed;
    
    //match status changed?
    boolean matchStatusChanged = false;
    if (match.getMatchStatus() != lastMatchStatus)
    {
      lastMatchStatus = match.getMatchStatus();
      matchStatusChanged = true;
    }
    
    
    //**********
    //RUNNING
    //**********
    if (match.getMatchStatus() == MatchStatus.RUNNING ||
      /* LOM patch */ !match.isMatchStatusActiveTeam(team))
    {
      //when match status is running, the active player
      //can be controlled
      //LOM patch: non-active team can always be controlled
      
      //reset ball kicked status
      matchStatusBallKicked = false;
      
      Player player = team.getActivePlayer();
      
      //if button1 or button2 is pressed, this is the shooting direction.
      //otherwise it is the running direction
      if (button1Pressed || button2Pressed)
      {
        arrowKeysShoot = arrowKeys;
      }
      else
      {
        arrowKeysRun = arrowKeys;
      }


      if (player.getBall() != null)
      {
        //Player is in ball possession
        if (button1KeyDown)
        {
          //user prepares for pass
          passKeyDown = true;
        }
        else if (button2KeyDown)
        {
          //user prepares for shoot
          shootKeyDown = true;
        }

        //if button1 key up (and before there was a key down), pass!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;
          Vector3f dirPassV;
          if (arrowKeysShoot > 0)
          {
            //pass in arrow keys direction
            dirPassV = getArrowKeysVector3f(arrowKeysShoot); 
          }
          else
          {
            //pass in running direction
            dirPassV = new Vector3f();
            MathTools.computeVectorFromYRotation(
              (int) player.getRotationY(), dirPassV);
          }
          
          //pass power: dependent on the duration of the keypress
          float dur = input.getKeyPressDuration(user, 0);
          if (dur < 0.2f)
          {
            dur = 0.2f;
          }
          else if (dur > 0.8f)
          {
            dur = 0.8f;
          }
          
          float power = dur;

          //pass!
          Player target = player.getTeam().getPlayerInDirection(player, dirPassV);
          if (target != null)
            player.pass(player.getBall(), target, false);
          else
            player.pass(player.getBall(), dirPassV, power, false);

        }
        
        //if button2 key up (and before there was a key down), goal shoot!
        else if (button2KeyUp && shootKeyDown)
        {
          shootKeyDown = false;

          //compute shooting direction
          ShootDirection dirShoot = ShootDirection.fromArrowKeys(
            arrowKeysShoot, team.getCurrentSide());

          //shoot power: dependent on the duration of the keypress
          float dur = input.getKeyPressDuration(user, 1);
          if (dur < 0.2f)
          {
            dur = 0.2f;
          }
          else if (dur > 0.8f)
          {
            dur = 0.8f;
          }
          
          float power = dur;

          //shoot!
          player.shoot(player.getBall(), dirShoot, power,
            team.getCurrentSide(), false);

        }
        
        //shoot preparation: show energy bar
        if (button2Pressed)
        {
          //user prepares for shoot
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 1);
          energyBar.setValue((int) (10 * dur / 0.8f));
        }
        
      }
      else //(ball == null)
      {
        //player is not in ball possession

        //if button1 key down, tackle
        if (button1KeyDown)
        {
          Speed speed = player.getSpeed();
          if (speed == null || speed == Speed.None)
            speed = Speed.Walk;
          player.setActionSet(new SimpleActionSet(
            new TackleAction(player, speed), ActionSet.PRIORITY_HIGH));
        }
        //if button2 key down, change active player
        else if (button2KeyDown)
        {
          team.selectBestPlayer();
        }

      }

      //sprinting
      Speed speed = Speed.Walk;
      if (button3Pressed)
      {
        speed = Speed.Run;
      }
      
      //running
      Vector3f target = getArrowKeysVector3f(arrowKeysRun);
      if (target != null)
      {
        //target.scale(10);
        target.add(player.getPosition());
      }
      player.runTo(target, speed);
    }
    
    
    //**********
    //GOAL KICK
    //**********
    else if (match.getMatchStatus() == MatchStatus.GOALKICK)
    {
      if (match.isMatchStatusActiveTeam(team))
      {
        //active team
        
        //initialize
        if (matchStatusChanged)
        {
          goalKickAngle = -team.getCurrentSide() * 90f * 
          (1 + MathTools.randomCenter(0.1f));;
        }
        
        //int corner = match.getCornerGoalKickCorner();
        Player player = team.getActivePlayer();
        
        //direction with arrow keys
        if ((arrowKeys & Input.left) != 0)
          goalKickAngle -= deltaTime * 30;
        else if ((arrowKeys & Input.right) != 0)
          goalKickAngle += deltaTime * 30;
        
        if (button1KeyDown)
        {
          //user prepares for kick
          passKeyDown = true;
        }
        
        //if button1 key up (and before there was a key down), kick!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;
          Vector3f dir = MathTools.computeVectorFromYRotation(goalKickAngle);
          
          //shoot power: dependent on the duration of the keypress
          float power = (input.getKeyPressDuration(user, 0) - 0.2f) * 2f;
          if (power < 0f)
            power = 0f;
          else if (power > 1f)
            power = 1f;
          
          MathTools.normalize(dir);
          dir.scale(PlayerSettings.getGoalkickSpeedHMin() +
            power * (PlayerSettings.getGoalkickSpeedHMax() -
              PlayerSettings.getGoalkickSpeedHMin()));
          dir.y = PlayerSettings.getGoalkickSpeedVMin() + power *
            (PlayerSettings.getGoalkickSpeedVMax() -
              PlayerSettings.getGoalkickSpeedVMin());
          
          player.kick(match.getBall(), dir, true, true);
          if (power < 0.6f)
            SoundEffectPool.Sound.BallPass.play();
          else
            SoundEffectPool.Sound.BallShoot.play();
          
          //match goes on!
          matchStatusBallKicked = true;

        }
        
        //goalkick preparation: show energy bar
        if (button1Pressed)
        {
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 0);
          energyBar.setValue((int) (2 * 10 * dur));
        }
      }
    }
    
    
    //**********
    //CORNER KICK
    //**********
    else if (match.getMatchStatus() == MatchStatus.CORNERKICK)
    {
      if (match.isMatchStatusActiveTeam(team))
      {
        //active team
        
        //initialize
        if (matchStatusChanged)
        {
          cornerKickAngle = -match.getCornerGoalKickCorner() * 15f * 
            (1 + MathTools.randomCenter(0.1f));
          if (match.getMatchStatusBallPosition().z > 0)
            cornerKickAngle = 180 + cornerKickAngle;
        }
        
        //int corner = match.getCornerGoalKickCorner();
        Player player = team.getActivePlayer();
        
        //direction with arrow keys
        if ((arrowKeys & Input.left) != 0)
          cornerKickAngle -= deltaTime * 30;
        else if ((arrowKeys & Input.right) != 0)
          cornerKickAngle += deltaTime * 30;
        
        if (button1KeyDown)
        {
          //user prepares for kick
          passKeyDown = true;
        }
        
        //if button1 key up (and before there was a key down), kick!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;
          Vector3f dir = MathTools.computeVectorFromYRotation(cornerKickAngle);
          
          //shoot power: dependent on the duration of the keypress
          float power = (input.getKeyPressDuration(user, 0) - 0.2f) * 2f;
          if (power < 0f)
            power = 0f;
          else if (power > 1f)
            power = 1f;
          
          MathTools.normalize(dir);
          dir.scale(PlayerSettings.getCornerkickSpeedHMin() +
            power * (PlayerSettings.getCornerkickSpeedHMax() -
              PlayerSettings.getCornerkickSpeedHMin()));
          dir.y = PlayerSettings.getCornerkickSpeedVMin() + power *
            (PlayerSettings.getCornerkickSpeedVMax() -
              PlayerSettings.getCornerkickSpeedVMin());
          
          player.kick(match.getBall(), dir, true, true);
          if (power < 0.6f)
            SoundEffectPool.Sound.BallPass.play();
          else
            SoundEffectPool.Sound.BallShoot.play();
          
          //match goes on!
          matchStatusBallKicked = true;

        }
        
        //cornerkick preparation: show energy bar
        if (button1Pressed)
        {
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 0);
          energyBar.setValue((int) (2 * 10 * dur));
        }
      }
    }
    
    
    //**********
    //THROW IN
    //**********
    else if (match.getMatchStatus() == MatchStatus.THROWIN)
    {
      Player player = team.getActivePlayer();
      
      //time to throw in?
      if (match.isMatchStatusActiveTeam(team))
      {
        if (button1KeyDown)
        {
          //user prepares for throw in
          passKeyDown = true;
        }
        //if button1 key up (and before there was a key down), throw in!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;
          Vector3f dir = new Vector3f();
          dir.sub(team.getActivePlayer().getPosition(),
            match.getBall().getPosition());
          MathTools.normalize(dir);
          
          //throwin power: dependent on the duration of the keypress
          float power = (input.getKeyPressDuration(user, 0) - 0.2f) * 2f;
          if (power < 0f)
            power = 0f;
          else if (power > 1f)
            power = 1f;
          
          MathTools.normalize(dir);
          dir.scale(PlayerSettings.getThrowinSpeedHMin() +
            power * (PlayerSettings.getThrowinSpeedHMax() -
              PlayerSettings.getThrowinSpeedHMin()));
          dir.y = PlayerSettings.getThrowinSpeedVMin() + power *
            (PlayerSettings.getThrowinSpeedVMax() -
              PlayerSettings.getThrowinSpeedVMin());
          //player must throw onto the field
          if (match.getBall().getPosition().z > 0 && dir.z > -0.1f)
            dir.z = -0.1f;
          else if (match.getBall().getPosition().z < 0 && dir.z < 0.1f)
            dir.z = 0.1f;
          
          //enabled ball physics
          match.getBall().setPhysicsEnabled(true);

          //throw in!
          player.kick(match.getBall(), dir, false, true);
          
          //match goes on!
          matchStatusBallKicked = true;
        }
        
        //throwin preparation: show energy bar
        if (button1Pressed)
        {
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 0);
          energyBar.setValue((int) (2 * 10 * dur));
        }
      }
      
      //sprinting
      Speed speed = Speed.Walk;
      if (button3Pressed)
      {
        speed = Speed.Run;
      }
      //running
      Vector3f target = getArrowKeysVector3f(arrowKeys);
      if (target != null)
      {
        //target.scale(10);
        target.add(player.getPosition());
      }
      player.runTo(target, speed);
    }
    
    
    //**********
    //PENALTY KICK
    //**********
    else if ((match.getMatchStatus() == MatchStatus.PENALTYKICK
      || match.getMatchStatus() == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT) &&
      match.getReferee().getAI().isReadyToProceed())
    {
      if (match.isMatchStatusActiveTeam(team))
      {
        //active team
        
        Player player = team.getActivePlayer();
        
        if (button1KeyDown)
        {
          //user prepares for kick
          passKeyDown = true;
        }
        
        //if button1 key up (and before there was a key down), kick!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;

          //shoot power: dependent on the duration of the keypress
          float power = (input.getKeyPressDuration(user, 0) - 0.2f);
          if (power < 0f)
            power = 0f;
          else if (power > 1f)
            power = 1f;
          
          Vector3f penaltySpot = new Vector3f(match.getMatchStatusBallPosition());
          Vector3f dir = getPenaltyShooterArrowKeysVector3f(
            arrowKeys, power, (penaltySpot.x > 0 ? -1 : 1));
          
          MathTools.normalize(dir);
          dir.scale(PlayerSettings.getPenaltykickSpeedHMin() +
            power * (PlayerSettings.getPenaltykickSpeedHMax() -
              PlayerSettings.getPenaltykickSpeedHMin()));
          //no additional vertical scaling. use vector
          //from getPenaltyShooterArrowKeysVector3f()
          
          player.kick(match.getBall(), dir, true, true);
          if (match.getMatchStatus() == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
            match.penaltyShootoutBallShot(); //ball was shot
          //sound
          if (power < 0.6f)
            SoundEffectPool.Sound.BallPass.play();
          else
            SoundEffectPool.Sound.BallShoot.play();
          
          //match goes on!
          matchStatusBallKicked = true;

        }
        
        //penaltykick preparation: show energy bar
        if (button1Pressed)
        {
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 0);
          energyBar.setValue((int) (10 * dur / 0.8f));
        }
      }
    }
    
    
    //**********
    //FREE KICK
    //**********
    else if (match.getMatchStatus() == MatchStatus.FREEKICK)
    {
      if (match.isMatchStatusActiveTeam(team))
      {
        //active team
        
        //initialize
        if (matchStatusChanged)
        {
          //direction to the goal
          Vector3f freeKickPos = new Vector3f(match.getMatchStatusBallPosition());
          Field field = match.getStadium().getField();
          int side = team.getCurrentSide();
          Vector3f dirToGoal = new Vector3f(
            -side * field.getLength() / 2 - freeKickPos.x, 0,
            -freeKickPos.z + MathTools.randomCenter(3));
          MathTools.normalize(dirToGoal);
          freeKickAngle = MathTools.Vector3fToAngle(dirToGoal);
        }
        
        Player player = team.getActivePlayer();
        
        //direction with arrow keys
        if ((arrowKeys & Input.left) != 0)
          freeKickAngle -= deltaTime * 30;
        else if ((arrowKeys & Input.right) != 0)
          freeKickAngle += deltaTime * 30;
        
        if (button1KeyDown && match.getReferee().getAI().isReadyToProceed())
        {
          //user prepares for kick
          passKeyDown = true;
        }
        
        //if button1 key up (and before there was a key down), kick!
        else if (button1KeyUp && passKeyDown)
        {
          passKeyDown = false;
          Vector3f dir = MathTools.computeVectorFromYRotation(freeKickAngle);
          
          //shoot power: dependent on the duration of the keypress
          float power = (input.getKeyPressDuration(user, 0) - 0.2f);
          if (power < 0f)
            power = 0f;
          else if (power > 1f)
            power = 1f;
          
          MathTools.normalize(dir);
          dir.scale(PlayerSettings.getFreekickSpeedHMin() +
            power * (PlayerSettings.getFreekickSpeedHMax() -
              PlayerSettings.getFreekickSpeedHMin()));
          dir.y = PlayerSettings.getFreekickSpeedVMin() + power *
            (PlayerSettings.getFreekickSpeedVMax() -
              PlayerSettings.getFreekickSpeedVMin());
          
          player.kick(match.getBall(), dir, true, true);
          //sound
          if (power < 0.6f)
            SoundEffectPool.Sound.BallPass.play();
          else
            SoundEffectPool.Sound.BallShoot.play();
          
          //match goes on!
          matchStatusBallKicked = true;

        }
        
        //freekick preparation: show energy bar
        if (button1Pressed)
        {
          energyBar.setVisible(true);
          float dur = input.getKeyPressDuration(user, 0);
          energyBar.setValue((int) (10 * dur / 0.8f));
        }
      }
    }
    
    
  }
  
  

  /**
   * Returns the normalized Vector3f defined by the
   * given arrow keys combination <code>ArrowKeys</code>.
   * If <code>ArrawKeys</code> is 0, <code>null</code> is returned.
   */
  protected Vector3f getArrowKeysVector3f(int ArrowKeys)
  {
    boolean up = (ArrowKeys & Input.up) != 0;
    boolean down = (ArrowKeys & Input.down) != 0;
    boolean left = (ArrowKeys & Input.left) != 0;
    boolean right = (ArrowKeys & Input.right) != 0;
    float v = MathTools.HALFSQRT2;
    if (up)
    {
      if (right)
      {
        return new Vector3f(v, 0, v);
      }
      else if (left)
      {
        return new Vector3f(-v, 0, v);
      }
      else
      {
        return new Vector3f(0, 0, 1);
      }
    }
    else if (down)
    {
      if (right)
      {
        return new Vector3f(v, 0, -v);
      }
      else if (left)
      {
        return new Vector3f(-v, 0, -v);
      }
      else
      {
        return new Vector3f(0, 0, -1);
      }
    }
    else if (left)
    {
      return new Vector3f(-1, 0, 0);
    }
    else if (right)
    {
      return new Vector3f(1, 0, 0);
    }
    else
    {
      return null;
    }
  }
  
  
  /**
   * Returns <code>true</code> when
   *  the user kicked the ball when he was
   * the active player during the following match statuses:
   * goal kick, corner kick, throw in, penalty kick.
   */
  public boolean isMatchStatusBallKicked()
  {
    return matchStatusBallKicked;
  }


  /**
   * Gets the angle of the corner kick in degrees.
   */
  public float getCornerKickAngle()
  {
    return cornerKickAngle;
  }
  
  
  /**
   * Gets the angle of the goal kick in degrees.
   */
  public float getGoalKickAngle()
  {
    return goalKickAngle;
  }
  
  
  /**
   * Gets the angle of the free kick in degrees.
   */
  public float getFreeKickAngle()
  {
    return freeKickAngle;
  }
  
  
  /**
   * Returns the normalized Vector3f for penalty kick defined by the
   * given arrow keys combination <code>arrowKeys</code>.
   * <code>power</code> is a number between 0 and 1. The higher it is,
   * the more the z and y value grow.
   * Examples:
   *   0: Vector3f points to the center of the goal,
   *      regardless of the arrow keys.
   *   1: Vector3f points far over the bar and far beside the post, when
   *      arrow keys are left-up ur right-up.
   * <code>side</code>: -1: left team has penalty kick, 1: right team
   */
  protected Vector3f getPenaltyShooterArrowKeysVector3f(int arrowKeys,
    float power, int side)
  {
    boolean up = (arrowKeys & Input.up) != 0;
    boolean down = (arrowKeys & Input.down) != 0;
    boolean left = (arrowKeys & Input.left) != 0;
    boolean right = (arrowKeys & Input.right) != 0;
    Vector3f ret = new Vector3f(-side, 0, 0);
    //vertical
    if (up)
      ret.y = power * 0.8f;
    else if (down)
      ret.y = 0.1f;
    else
      ret.y = power * 0.4f;
    //horizontal
    if (left)
      ret.z = -side * power * 0.5f;
    else if (right)
      ret.z = side * power * 0.5f;
    else
      ret.z = 0f;
    MathTools.normalize(ret);
    return ret;
  }
  
  
}
