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
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.Match.MatchStatus;
import com.xenoage.bp2k6.match.characters.actions.ActionSetFactory;
import com.xenoage.bp2k6.match.characters.actions.TumbleAction;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.match.physics.*;
import com.xenoage.bp2k6.match.referee.Card;
import com.xenoage.bp2k6.match.stadium.Field; //LOM patch
import com.xenoage.bp2k6.util.MathTools;

import javax.vecmath.Vector3f;

import net.sf.jirr.ISceneManager;


/**
 * Abstract base class for soccer players
 * (outfield player and goalkeaper). This class contains
 * all additional methods, that are not
 * in the <code>Character</code>-class
 * but that every player must have, like
 * tackling, passing and shooting.
 * Players can be controlled by the user or by the AI.
 *
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM)
 */
public abstract class Player
  extends Character
  implements PhysicsObject
{
  
  //player information (name, values, ...)
  protected PlayerInfo info;

  //the team this player belongs to
  protected Team team = null;
  
  //position of the player (goalkeeper, defense, forward)
  protected PlayerPosition playerPosition;
  
  //the ball this player currently possesses, or null
  protected Ball ball;
  //time when this player lost the ball (e.g. when he kicked it away)
  protected float ballLostTime;
  //true, when the player has the ball in his hands
  //(only applies for goalkeepers)
  protected boolean ballInHands = false;
  
  //the card this player has
  protected Card card = Card.None;
  
  //does this player really play now? (e.g. red card = false)
  protected boolean playing = true;
  
  //sphere used for collision detection
  protected Sphere collisionSphere;
  protected float collisionSphereRadius = 0.5f;
  
  
  /**
   * Constructor.
   * The parameters <code>team</code> and <code>physicsManager</code>
   * may be <code>null</code>.
   */
  public Player(PlayerInfo info, Team team, PlayerPosition playerPosition,
    CharacterMesh mesh,
    ISceneManager sceneManager, PhysicsManager physicsManager)
  {
    //call super constructor
    super(mesh, sceneManager);

    //set player information
    this.info = info;

    //set the team this player belongs to
    this.team = team;
    
    //set the position of the player
    this.playerPosition = playerPosition;

    //create collision sphere and enable physics
    collisionSphere = new Sphere(new Vector3f(), collisionSphereRadius);
    if (physicsManager != null)
      physicsManager.addPhysicsObject(this);
    setPhysicsEnabled(true);

  }
  
  
  /**
   * Gets information about the player.
   */
  public PlayerInfo getInfo()
  {
    return info;
  }
  
  
  /**
   * Gets the team this player belongs to,
   * or <code>null</code> if unknown.
   */
  public Team getTeam()
  {
    return team;
  }
  
  
  /**
   * Returns the position (defender, forward, ...) of this player.
   */
  public PlayerPosition getPlayerPosition()
  {
    return playerPosition;
  }
  
  
  /**
   * Computes the player's actions, position and animation.
   * Call this function in the main loop.
   */
  @Override public float step()
  {
    float deltaTime = super.step();
    
    //if sprinting, decrease energy
    if (speed == Speed.Run)
      info.energyEvent(PlayerInfo.EnergyEvent.SPRINT,
        deltaTime / getTeam().getMatch().getRealDuration());
    
    //ball-possession-dependent actions
    if (playing) //player still in match?
    {
      if (ball == null)
      {
        //player has currently no ball.
        //if one ball is free and close-by, and if this player did not lose
        //it in the last few milliseconds, and if the current animation stage
        //allows it, take the ball (if ball is slow enough) or
        //the ball rebounds (if it is fast)
        if (GameEngine.getMatchManager().getMatch().getMatchStatus() ==
            Match.MatchStatus.RUNNING &&
            (GameEngine.getFrameTime(true) - ballLostTime > 0.3f) &&
            !animationStage.isTumbling())
        {
          if (animationStage.isDiving())
          {
            //dive for the ball
            diveStep();
          }
          else
          {
            Ball b = team.getMatch().getBall();
            
            //horizontal radius
            float maxDistanceX = 0.3f;
            if (animationStage.isTackling())
              maxDistanceX = 0.6f;
            if (b.getVelocity().lengthSquared() < 2f * 2f)
              maxDistanceX += 0.3f;
            //vertical radius
            float maxDistanceY = 1.5f;
            if (animationStage.isTackling())
              maxDistanceX = 0.5f;
            
            if (MathTools.distanceSquaredWithoutY(b.getPosition(), position) <
              maxDistanceX * maxDistanceX && b.getPosition().y < maxDistanceY)
            {
              if (b.getVelocity().length() > PlayerSettings.getBounceBallSpeed())
              {
                //bounce the ball
                b.setLastPlayer(this); //player touches ball
                ballLostTime = GameEngine.getFrameTime(true);
                b.getVelocity().scale(PlayerSettings.getBounceVelFactor()); //ball bounces
                b.getVelocity().add(new Vector3f( //additional random direction
                  MathTools.randomCenter(PlayerSettings.getBounceVelRandom()),
                  MathTools.randomCenter(PlayerSettings.getBounceVelRandom()),
                  MathTools.randomCenter(PlayerSettings.getBounceVelRandom())));
              }
              else if (b.getPlayer() == null)
              {
                b.setPlayer(this, false);
              }
              else //(b.getPlayer() != null)
              {
                //when tackling, get the ball
                if (animationStage.isTackling())
                {
                  if (b.isInHands())
                  {
                    //keeper has the ball. player can not get it.
                  }
                  else
                  {
                    //get the ball
                    b.setPlayer(this, false);
                  }
                }
              }
            }
            else //too far away from ball
            {
              if (animationStage.isTackling())
              {
                //knock down player from other team?
                Team otherTeam = GameEngine.getMatchManager().getMatch().getOpponentTeam(team);
                Player otherTeamPlayers[] = otherTeam.getPlayers();
                for (int i = 0; i < otherTeamPlayers.length + 1; i++)
                {
                  Character character = null;
                  if (i < otherTeamPlayers.length)
                  {
                    if (otherTeamPlayers[i].isPlaying())
                      character = otherTeamPlayers[i];
                  }
                  else
                  {
                    character = team.getMatch().getReferee();
                  }
                  if (character != null &&
                    MathTools.distanceSquaredWithoutY(
                      character.getPosition(), position) < 0.5f * 0.5f &&
                    !character.getAnimationStage().isTumbling())
                  {
                    //knock down other player/referee
                    character.setPriorityAction(new TumbleAction(character));
                    SoundEffectPool.Sound.PlayerTackleHit.play();
                    team.getMatch().getReferee().getAI().foul(this, character);
                  }
                }
              }
            }
          }
        }
      }
      else //(ball != null)
      {
        if (GameEngine.getMatchManager().getMatch().getMatchStatus() !=
            Match.MatchStatus.RUNNING)
        {
          //game not running. release the ball
          ball.setPlayer(null, false);
          ball = null;
        }
        else
        {
          //ball must follow the player
          ball.setVelocity(new Vector3f(velocity.x, 0f, velocity.z));
          //set ball 0.6 m in front of the player
          Vector3f p = new Vector3f(position);
          p.y = ball.getRadius();
          Vector3f d = new Vector3f();
          MathTools.computeVectorFromYRotation((int) rotationY, d);
          d.scale(0.6f);
          p.add(d);
          ball.setPosition(p);
        }
      }
    }
    
    return deltaTime;
  }
  
  
  /**
   * Passes a ball into the given direction.
   * @param ball   The ball to pass.
   * @param dir    The direction the ball will be passed.
   *               Must be normalized. Normal passes have y=0,
   *               but also other values are allowed for
   *               high passes.
   * @param power  Value between 0 (weak pass) and 1 (very powerful).
   * @param force  If <code>true</code>, the ball is passed even when
   *               this player is not in ball possession.
   */
  @Override public void pass(Ball ball, Vector3f dirPass, float power, boolean force)
  {
    if (this.ball == ball || force)
    {
      Vector3f dir = new Vector3f(dirPass);
      
      dir.scale(PlayerSettings.getPassSpeedHMin() +
        power * (PlayerSettings.getPassSpeedHMax() -
          PlayerSettings.getPassSpeedHMin()));
      dir.y = PlayerSettings.getPassSpeedVMin() + power *
        (PlayerSettings.getPassSpeedVMax() -
          PlayerSettings.getPassSpeedVMin());
      
      //pass the ball
      ball.setVelocity(dir);
      //player loses ball possession
      loseBall();
      //if not running, play shoot animation
      if (!animationStage.isRunning())
        setAnimationStage(AnimationStage.ShootStandLeft);
      //sound
      SoundEffectPool.Sound.BallPass.play();
    }
  }
  
  
  /**
   * Passes a ball to the given character.
   * @param ball       The ball to pass.
   * @param character  The target character
   * @param force      If <code>true</code>, the ball is passed even when
   *                   this character is not in ball possession.
   */
  @Override public void pass(Ball ball, Character character, boolean force)
  {
    //compute direction and speed
    Vector3f direction = new Vector3f();
    direction.sub(character.getPosition(), ball.getPosition());
    float power = direction.length() / 15f;
    if (power < 0f)
      power = 0f;
    else if (power > 1f)
      power = 1f;
    //if target is moving and under AI control,
    //update the target to the future position
    if (character.getSpeed() != Speed.None /* &&
      team.getTeamControl() == TeamControl.AI */)
    {
      //compute duration that ball needs to target
      float ballSpeed = PlayerSettings.getPassSpeedHMin() +
        power * (PlayerSettings.getPassSpeedHMax() -
          PlayerSettings.getPassSpeedHMin());
      ballSpeed *= 1.1f; //correction
      Vector3f distance = new Vector3f();
      distance.sub(character.getPosition(), ball.getPosition());
      float passDuration = distance.length() / ballSpeed;
      //change target
      Vector3f futurePos = new Vector3f();
      Vector3f move = new Vector3f(character.getVelocity());
      move.scale(passDuration);
      futurePos.add(character.getPosition(), move);
      //compute direction
      direction.sub(futurePos, ball.getPosition());
    }
    MathTools.normalize(direction);
    pass(ball, direction, power, force);
  }
  
  
  /**
   * Shoots a ball to the given goal.
   * @param ball       The ball to shoot.
   * @param dirShoot   Where to shoot the ball.
   * @param power      Value between 0 (weak shot) and 1 (very powerful).
   * @param goal       -1: right goal (left team), 1: left goal (right team)
   */
  @Override public void shoot(Ball ball, ShootDirection dirShoot, float power,
    int goal, boolean force)
  {
    if (this.ball == ball || force)
    {
      Vector3f goalPos = new Vector3f(goal * -20f, 0, 0);
      Vector3f dirShootV = new Vector3f();
      float shootY = PlayerSettings.getShootSpeedV();
      float powerScale = 1;
      
      //attack dependent random z error
      float zError = MathTools.randomCenter(2) * (1f - info.getAttack() * 0.1f);
  
      if (dirShoot == ShootDirection.Left)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * 2.4f - position.z + zError);
      }
      else if (dirShoot == ShootDirection.CenterLeft)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * 1.2f - position.z + zError);
      }
      else if (dirShoot == ShootDirection.Center)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          0 - position.z + zError);
      }
      else if (dirShoot == ShootDirection.CenterRight)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * -1.2f - position.z + zError);
      }
      else if (dirShoot == ShootDirection.Right)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * -2.4f - position.z + zError);
      }
      else if (dirShoot == ShootDirection.LiftLeft)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * 1.6f - position.z + zError);
        shootY = PlayerSettings.getShootSpeedLiftV();
        powerScale = PlayerSettings.getShootSpeedLiftHRed();
        dirShoot = ShootDirection.Center;
      }
      else if (dirShoot == ShootDirection.LiftCenter)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          0 - position.z + zError);
        shootY = PlayerSettings.getShootSpeedLiftV();
        powerScale = PlayerSettings.getShootSpeedLiftHRed();
        dirShoot = ShootDirection.Center;
      }
      else if (dirShoot == ShootDirection.LiftRight)
      {
        dirShootV.set(goalPos.x + 0.5f - position.x, 0,
          -goal * -1.6f - position.z + zError);
        shootY = PlayerSettings.getShootSpeedLiftV();
        powerScale = PlayerSettings.getShootSpeedLiftHRed();
        dirShoot = ShootDirection.Center;
      }
      
      MathTools.normalize(dirShootV);
  
      dirShootV.scale((PlayerSettings.getShootSpeedH() +
        PlayerSettings.getShootSpeedHAdd() * power) * powerScale);
      //attack dependent speed
      dirShootV.scale(0.75f + info.getAttack() * 0.05f);
      //shoot height
      dirShootV.y = shootY + power * 2;
      //shoot the ball
      ball.setVelocity(dirShootV);
      //player loses ball possession
      if (ball != null)
        loseBall();
  
      //sound
      SoundEffectPool.Sound.BallShoot.play();
    }
  }
  
  
  /**
   * Kicks/throws a ball with the given velocity.
   * @param ball     The ball to pass.
   * @param velocity New velocity of the ball
   * @param force    If <code>true</code>, the ball is passed even when
   *                 this character is not in ball possession.
   */
  @Override public void kick(Ball ball, Vector3f velocity, boolean anim, boolean force)
  {
    if (this.ball == ball || force)
    {
      //kick/throws the ball
      ball.setVelocity(velocity);
      //player loses ball possession
      loseBall();
      //if anim is set and not running, play shoot animation
      if (anim && !animationStage.isRunning())
        setAnimationStage(AnimationStage.ShootStandLeft);
    }
  }
  
  
  /**
   * Tackles in the running direction.
   */
  @Override public void tackle(Speed speed)
  {
    //player can only tackle if he is not in ball-possession
    //and if not tackling or tumbling already
    if (ball == null && !animationStage.isTackling() &&
      !animationStage.isTumbling())
    {
      //rotate to ball before tackling
      rotateToBall(true);
      
      this.tackleSpeed = speed;
      setAnimationStage(AnimationStage.Tackle);
      
      //decrease player's energy
      info.energyEvent(PlayerInfo.EnergyEvent.TACKLE, 0);
      GameEngine.getMatchManager().getMatch().updateEnergyDisplay(this);

      //tackle sound
      SoundEffectPool.Sound.PlayerTackle.play();

    }
  }
  
  
  /**
   * Tumbles in the running direction.
   * Call this method only from TumbleAction.perform(),
   * use this action to let a player tumble.
   */
  @Override public void tumble()
  {
    //player can only tumble if not if not tumbling already
    if (!animationStage.isTumbling())
    {
      if (MathTools.random() < 0.5f)
        setAnimationStage(AnimationStage.TackleTumble1);
      else
        setAnimationStage(AnimationStage.TackleTumble2);
      //decrease player's energy
      info.energyEvent(PlayerInfo.EnergyEvent.KNOCKEDDOWN, 0);
      GameEngine.getMatchManager().getMatch().updateEnergyDisplay(this);
    }
    //when in ball possession, the player loses it
    loseBall();

    //player cries
    SoundEffectPool.Sound.PlayerCry.play();

  }
  
  
  /**
   * Call this method when this player loses
   * the ball (e.g. when he passed it).
   */
  protected void loseBall()
  {
    if (ball != null)
      ball.setPlayer(null, false);
    ballLostTime = GameEngine.getFrameTime(true);
    ballInHands = false;
  }
  
  
  /**
   * Returns the ball this player is in possession of.
   * Otherwise <code>null</code> is returned.
   */
  public Ball getBall()
  {
    return ball;
  }
  
  
  /**
   * Call this method only from <code>Ball.setPlayer()</code>,
   * when the ball possession of this player has changed.
   */
  public void setBall(Ball ball, boolean inHands)
  {
    this.ball = ball;
    this.ballInHands = inHands;
  }
  
  
  /**
   * Gets the collision sphere of this player-
   */
  public Sphere getCollisionSphere()
  {
    return collisionSphere;
  }
  
  
  /**
   * Rotates this referee. Call this method only if
   * the current animation stage allows rotation.
   * If running, he rotates towards his target.
   * Otherwise he rotates towards the ball.
   */
  @Override public void rotate(float deltaTime)
  {
    Vector3f rotationTarget = null;
    
    if (animationStage.isRunning() && target != null)
    {
      //when running, rotate towards the target
      rotationTarget = target;
    }
    else if (ball == null)
    {
      //when not running and not in ball possession, rotate towards the ball
      //animation for stand-rotation could be added here...
      rotationTarget = team.getMatch().getBall().getPosition();
    }
    
    if (rotationTarget != null &&
      MathTools.distanceSquaredWithoutY(position, rotationTarget) > 0.1f)
    {
      Vector3f dir = new Vector3f();
      dir.sub(rotationTarget, position);

      float dirAngle = MathTools.Vector3fToAngle(dir);
      if (Math.abs(360f + dirAngle - rotationY) % 360f > 5f)
      {
        rotationY = dirAngle;
        nativeRotation.set(
          mesh.getRotation().x,
          mesh.getRotation().y + rotationY,
          mesh.getRotation().z);
        node.setRotation(nativeRotation);
      }
    }
  }
  
  
  /**
   * Rotates the player immediately to the ball, if
   * not in possession of it.
   * If defenseError is <code>true</code>, a defense value
   * dependent random error is added to the rotation.
   */
  protected void rotateToBall(boolean defenseError)
  {
    if (ball == null)
    {
      Vector3f rotationTarget = team.getMatch().getBall().getPosition();
      if (MathTools.distanceSquaredWithoutY(position, rotationTarget) > 0.1f)
      {
        Vector3f dir = new Vector3f();
        dir.sub(rotationTarget, position);

        float dirAngle = MathTools.Vector3fToAngle(dir);
        
        if (defenseError)
          dirAngle = (dirAngle + 360 + MathTools.randomCenter(2) *
            (10 - info.getDefense())) % 360;
        
        rotationY = dirAngle;
        nativeRotation.set(
          mesh.getRotation().x,
          mesh.getRotation().y + rotationY,
          mesh.getRotation().z);
        node.setRotation(nativeRotation);
      }
    } 
  }
  
  
  /**
   * Computes the possible speed of this character.
   */
  @Override protected float computeSpeed()
  {
    float ret = 0;
    //special situation: kickoff (all players have the same speed)
    if (team.getMatch() != null &&
      (team.getMatch().getMatchStatus() == MatchStatus.KICKOFF_TIME ||
      team.getMatch().getMatchStatus() == MatchStatus.KICKOFF_GOAL))
      ret = PlayerSettings.getPlayerSpeed();
    else
      ret = info.computeSpeed(PlayerSettings.getPlayerSpeed());
    return ret;
  }
  
  
  /**
   * Call this method in every step() when the player
   * is diving for the ball.
   */
  protected abstract void diveStep();


  /**
   * Returns <code>true</code>, when the player has the
   * ball in his hands (only applies for goalkeepers).
   */
  public boolean isBallInHands()
  {
    return ballInHands;
  }


  /**
   * Gets the card of the player.
   */
  public Card getCard()
  {
    return card;
  }


  /**
   * Sets the card of the player.
   */
  public void setCard(Card card)
  {
    this.card = card;
    //yellow-red or red card: leave the match
    //but goalkeepers must always stay
    if (card == Card.YellowRed || card == Card.Red &&
      !(this instanceof Goalkeeper))
    {
      this.playing = false;
      //leave the stadium
      ActionSetFactory.applyRedCard(this, team.getMatch().getStadium());
    }
  }


  /**
   * Returns true, if the player is still part of
   * the match (e.g. after a red card it returns false).
   */
  public boolean isPlaying()
  {
    return playing;
  }
  
  
  /**
   * LOM patch: Returns true, if the player is inside
   * the opponents penalty area.
   * 
   * @author Malte Löpmann
   */
  public boolean isInPenaltyArea(Field field, Team opponentTeam)
  {   
    Vector3f position = this.getPosition();

    return (position.x * opponentTeam.getCurrentSide() >
      field.getLength() / 2 - field.getPenaltyLength() &&
      Math.abs(position.z) < field.getPenaltyWidth() / 2);
  }
  
  
}
