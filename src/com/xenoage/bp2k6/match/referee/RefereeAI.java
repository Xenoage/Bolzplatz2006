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
package com.xenoage.bp2k6.match.referee;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.Character;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.Referee;
import com.xenoage.bp2k6.match.characters.actions.*;
import com.xenoage.bp2k6.match.characters.enums.Speed;
import com.xenoage.bp2k6.match.referee.decisions.*;
import com.xenoage.bp2k6.match.stadium.Field;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.math.NormalDistribution;

import java.util.ArrayList;

import javax.vecmath.Vector3f;


/**
 * Class for a referee. He makes decisions
 * about goals, fouls, half time and so on.
 * 
 * @author Andreas Wenger
 * @author Malte Löpmann (patches: LOM)
 */
public class RefereeAI
{
  private Referee referee;
  private RefereeInfo info;
  
  private Match match;
  
  private float lastStepTime = 0;
  
  private boolean readyForKickoff = false;
  
  private float overtimeFirstHalf;
  private float overtimeSecondHalf;
  private float overtimeFirstOvertimeHalf;
  private float overtimeSecondOvertimeHalf;
  
  private Vector3f kickOffPos;
  
  private Vector3f currentTarget = new Vector3f();
  private Vector3f lastTarget = new Vector3f();
  
  private float lastDecisionTime = 0;
  private boolean readyToProceed = false;
  private boolean readyToProceedWhistleBlown = false;
  
  //LOM patch: count repeated freekicks, goalkicks, ...  
  private float delayOfGameCount = 0f;
  //LOM patch: distance from player to ball before freekick
  public static int freekickWallDistance = 5;
  //LOM patch: time of game delay
  private float delayOfGameTime = 0;
  
  
  /**
   * Constructor.
   */
  public RefereeAI(Referee referee, RefereeInfo info)
  {
    this.referee = referee;
    this.info = info;
    overtimeFirstHalf = (-0.5f + MathTools.random(4)) * 60f;
    overtimeSecondHalf = (-0.5f + MathTools.random(6)) * 60f;
    overtimeFirstOvertimeHalf = (-0.2f + MathTools.random(2)) * 60f;
    overtimeSecondOvertimeHalf = (-0.2f + MathTools.random(4)) * 60f;
  }
  
  
  /**
   * Sets the current match or
   * deletes it (match == null).
   */
  public void setMatch(Match match)
  {
    this.match = match;
    //important positions
    kickOffPos = new Vector3f(-3, 0, -4);
  }
  
  
  /**
   * Move the referee.
   * Call this method every frame.
   */
  public void step()
  {
    if (match == null)
      return;
    
    //make decisions
    float t = GameEngine.getFrameTime(true);
    if (t - lastStepTime > 0)
      //condition not needed, but could be used to delay reaction
    {
      lastStepTime = t;
      
      //game statistics
      float gameTime = match.getGameTime();
      Match.MatchPhase matchPhase = match.getMatchPhase();
      
      //time-dependent decisions
      if (matchPhase == Match.MatchPhase.BeforeGame)
      {
        if (readyForKickoff)
        {
          //kick off for first half
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Kickoff for first half");
          SoundEffectPool.Sound.Whistle2Short1Long.play();
          match.setMatchPhase(Match.MatchPhase.FirstHalf);
          readyForKickoff = false;
        }
      }
      else if (matchPhase == Match.MatchPhase.FirstHalf)
      {
        if (gameTime > 45 * 60 + overtimeFirstHalf)
        {
          //half time
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Half time whistle");
          runToEntrance();
          SoundEffectPool.Sound.Whistle2Short1Long.play();
          match.setMatchPhase(Match.MatchPhase.HalfTime);
        }
      }
      else if (matchPhase == Match.MatchPhase.HalfTime)
      {
        if (readyForKickoff)
        {
          //kick off for second half
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Kickoff for second half");
          SoundEffectPool.Sound.WhistleLong.play();
          match.setMatchPhase(Match.MatchPhase.SecondHalf);
          readyForKickoff = false;
        }
      }
      else if (matchPhase == Match.MatchPhase.SecondHalf)
      {
        if (gameTime > 90 * 60 + overtimeSecondHalf)
        {
          //90 min: time up or overtime break
          int score[] = match.getScore();
          if (match.isDrawAllowed() || score[0] != score[1])
          {
            //time up
            Logging.log(Logging.LEVEL_MESSAGES, this,
              "Referee: Regular time up whistle. Score: " +
                score[0] + " : " + score[1]);
            runToEntrance();
            SoundEffectPool.Sound.Whistle2Short1Long.play();
            match.setMatchPhase(Match.MatchPhase.TimeUpRegularTime);
          }
          else
          {
            //draw not allowed: play overtime
            Logging.log(Logging.LEVEL_MESSAGES, this,
              "Referee: Regular time up whistle. Score: " +
                score[0] + " : " + score[1] + ". Overtime needed.");
            runToEntrance();
            SoundEffectPool.Sound.Whistle2Short1Long.play();
            match.setMatchPhase(Match.MatchPhase.TimeUpBeforeOvertime);
          }
        }
      }
      else if (matchPhase == Match.MatchPhase.TimeUpBeforeOvertime)
      {
        if (readyForKickoff)
        {
          //kick off for first overtime half
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Kickoff for first overtime half");
          SoundEffectPool.Sound.WhistleLong.play();
          match.setMatchPhase(Match.MatchPhase.OverTimeFirstHalf);
          readyForKickoff = false;
        }
      }
      else if (matchPhase == Match.MatchPhase.OverTimeFirstHalf)
      {
        if (gameTime > 105 * 60 + overtimeFirstOvertimeHalf)
        {
          //overtime half time
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Overtime half time whistle");
          runToEntrance();
          SoundEffectPool.Sound.Whistle2Short1Long.play();
          match.setMatchPhase(Match.MatchPhase.OverTimeHalfTime);
        }
      }
      else if (matchPhase == Match.MatchPhase.OverTimeHalfTime)
      {
        if (readyForKickoff)
        {
          //kick off for second overtime half
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Referee: Kickoff for second overtime half");
          SoundEffectPool.Sound.WhistleLong.play();
          match.setMatchPhase(Match.MatchPhase.OverTimeSecondHalf);
          readyForKickoff = false;
        }
      }
      else if (matchPhase == Match.MatchPhase.OverTimeSecondHalf)
      {
        if (gameTime > 120 * 60 + overtimeSecondOvertimeHalf)
        {
          //120 min: time up or penalty shootout
          int score[] = match.getScore();
          if (score[0] != score[1])
          {
            //time up
            Logging.log(Logging.LEVEL_MESSAGES, this,
              "Referee: Overtime time up whistle. Score: " +
                score[0] + " : " + score[1]);
            runToEntrance();
            SoundEffectPool.Sound.Whistle2Short1Long.play();
            match.setMatchPhase(Match.MatchPhase.TimeUpOverTime);
          }
          else
          {
            //draw not allowed: penalty shootout
            Logging.log(Logging.LEVEL_MESSAGES, this,
              "Referee: Overtime time up whistle. Score: " +
                score[0] + " : " + score[1] + ". Penalty shootout needed.");
            SoundEffectPool.Sound.Whistle2Short1Long.play();
            match.setMatchPhase(Match.MatchPhase.TimeUpBeforePenaltyShootout);
          }
        }
      }
      else if (matchPhase == Match.MatchPhase.TimeUpBeforePenaltyShootout)
      {
        //needs nothing to do here
      }
      
      
      //match-status-dependent decisions
      if (match.getMatchStatus() == Match.MatchStatus.KICKOFF_TIME ||
        match.getMatchStatus() == Match.MatchStatus.KICKOFF_GOAL)
      {
        referee.runTo(kickOffPos, Speed.Walk);
        if (readyForKickoff)
        {
          readyForKickoff = false;
          SoundEffectPool.Sound.WhistleShort.play();
          match.kickoff();
        }
      }
      else if (match.getMatchStatus() == Match.MatchStatus.PENALTYKICK ||
        match.getMatchStatus() == Match.MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
      {
        //before ready for penalty kick, look if players
        //not done...
        //are on their positions...
        if (!readyToProceedWhistleBlown && t - lastDecisionTime > 5 &&
          match.getMatchStatusDuration() > 5)
        {
          //ready for penalty kick
          readyToProceed = true;
          SoundEffectPool.Sound.WhistleLong.play();
          readyToProceedWhistleBlown = true;
        }
      }
      else if (match.getMatchStatus() == Match.MatchStatus.FREEKICK)
      {
        //before ready for free kick, look if players
        //not done...
        //are on their positions...
        if (!readyToProceedWhistleBlown && t - lastDecisionTime > 5)
        {
          
          //LOM patch: check, if there is an opponent near the ball          
          if (this.proceedFreekick(true))
          {
            //unpatched >>>
            //ready for free kick
            readyToProceed = true;
            SoundEffectPool.Sound.WhistleLong.play();
            readyToProceedWhistleBlown = true;
            //unpatched <<<
          }
          else
          {
            readyToProceed = false;
            readyToProceedWhistleBlown = false;
          }
          
        }
        //run between freekick position and ball, but 5 m further up
        currentTarget.set(match.getBall().getPosition()); 
        currentTarget.x += -match.getActiveTeam().getCurrentSide()
          * match.getStadium().getField().getLength() * 0.5f;
        currentTarget.scale(0.5f);
        if (currentTarget.z < match.getStadium().getField().getWidth() / 2 - 5)
          currentTarget.z += 5;
        else
          currentTarget.z -= 5;
        lastTarget.set(currentTarget);
        referee.runTo(currentTarget, Speed.Walk);
      }
      else if (match.getMatchStatus() == Match.MatchStatus.RUNNING)
      {
        currentTarget.set(match.getBall().getPosition());
        currentTarget.scale(0.7f);
        currentTarget.y = 0;
        if (MathTools.distanceSquaredWithoutY(currentTarget, lastTarget) > 5 * 5)
        {
          lastTarget.set(currentTarget);
          referee.runTo(currentTarget, Speed.Walk);
        }
      }
      
    } 
    
  }
  
  
  /**
   * Call this method from <code>Match</code> when
   * all match-members are ready for kickoff
   * (start kickoff, halftime kickoff, goal kickoff, ...)
   */
  public void setReadyForKickoff()
  {
    readyForKickoff = true;
  }
  
  
  /**
   * Returns <code>true</code> when the referee is ready to
   * proceed, e.g. when he is ready for a penalty kick.
   * The whistle was already blown before.
   */
  public boolean isReadyToProceed()
  {
    return readyToProceed;
  }
  
  
  /**
   * Reports a foul to the referee. The AI decides if it is
   * really recognized as a foul or not.
   * @param committer   The player that committed the foul.
   * @param victim      The character that was knocked down. May be
   *                    a player from the other team or the referee.
   */
  public void foul(Player committer, Character victim)
  {
    float foulPoints = 0;
    
    //competence (0 to +3)
    foulPoints += info.getCompetence() / 10f * 3f;
    
    //prejudice (-2 to +2)
    float predj = info.getPrejudice() / 10f * 2f * 
      (match.getTeam(0).isPlayerInTeam(committer) ? 1 : -1);
    foulPoints += predj;
    
    //random factor (-3 to 3)
    foulPoints += NormalDistribution.random() * 6 - 3;
    
    //additional 0.5 points added in version 1.1, so more fouls are recognized
    foulPoints += 0.5f;
    
    if (foulPoints > 2f)
    {
      //recognized as a foul!
      Vector3f foulPos = new Vector3f(victim.getPosition());
      foulPos.y = 0;
      //whistle
      //perhaps he is not confident enough
      //or he is fouled, then his whistle sounds congested 
      if (victim == referee ||
        NormalDistribution.random10() >= info.getCompetence())
      {
        SoundEffectPool.Sound.WhistleCongested.play();
      }
      else
      {
        SoundEffectPool.Sound.Whistle5Short.play();
      }
      //free kick or penalty kick?
      if (victim != referee)
      {
        Card card = getCardForFoul(committer, victim);
        Player victimPlayer = (Player) victim;
        Field field = match.getStadium().getField();
        if (foulPos.x * -victimPlayer.getTeam().getCurrentSide() >
          field.getLength() / 2 - field.getPenaltyLength() &&
          Math.abs(foulPos.z) < field.getPenaltyWidth() / 2)
        {
          //penalty kick
          RefereeDecision decision = new PenaltyKickDecision(
            referee, victimPlayer.getTeam(), card, committer);
          makeDecision(decision);
        }
        else
        {
          //free kick
          RefereeDecision decision = new FreeKickDecision(
            referee, victimPlayer.getTeam(), victim.getPosition(), card, committer);
          makeDecision(decision);
        }
      }
      else
      {
        //referee was the victim. free kick for the other team,
        //but only, if ball is > 5 m away (otherwise it
        //was an accident)
        float distanceToBall = MathTools.distance(
          victim.getPosition(), match.getBall().getPosition());
        if (distanceToBall > 5)
        {
          RefereeDecision decision = new FreeKickDecision(
            referee, match.getOpponentTeam(committer.getTeam()),
            victim.getPosition(), Card.None, committer);
          makeDecision(decision);
        }
      }
    }
    else if (foulPoints > 1f)
    {
      //referee sees it, but decides that it was no foul
      RefereeDecision decision = new NoFoulDecision(referee);
      makeDecision(decision);
    }
  }
  
  
  /**
   * Sends a new decision to the match class.
   * Call this method instead of calling
   * match.refereeDecision() directly!
   */
  private void makeDecision(RefereeDecision decision)
  {
    match.refereeDecision(decision);
    lastDecisionTime = GameEngine.getFrameTime(true); 
    readyToProceedWhistleBlown = false;
    readyToProceed = false;
  }
  
  
  /**
   * Gets the card for a foul. Dependent on the position
   * and viewing direction of the players.
   */
  private Card getCardForFoul(Player committer, Character victim)
  {
    float points = 0;
    
    //compute distance from victim to ball
    float distanceToBall = MathTools.distance(
      victim.getPosition(), match.getBall().getPosition());
    if (distanceToBall > 6)
      points += 2; //ball is far away
    else if (distanceToBall > 3)
      points += 1; //player had no chance to get the ball
    
    //compute foul direction
    float angle = MathTools.getAngle(
      committer.getRotationY(), victim.getRotationY());
    if (angle < 20)
      points += 2; //tackle from behind
    else if (angle < 80)
      points += 1; //tackle from the side
    
    //LOM patch: method extracted
    points += this.getStrictnessAndPrejudice(committer);
    /*
    //unpatched:
    //strictness (-1 to +1)
    points += info.getStrictness() / 10f * 2f - 1f;
    
    //prejudice (-1 to +1)
    float predj = info.getPrejudice() / 10 * 
      (match.getTeam(0).isPlayerInTeam(committer) ? 1 : -1);
    points += predj;
    
    //random factor (-1.5 to +1.5)
    points += NormalDistribution.random() * 3f - 1.5f;
    */
    
    if (points < 1.5f)
    {
      //no card
      return Card.None;
    }
    else if (points < 2.5f)
    {
      //yellow card
      //LOM patch: method extracted
      return this.getYellowCard(committer);
      /*
      //unpatched:
      if (committer.getCard() == Card.Yellow)
      {
        //team can not have more than 2 red cards
        if (committer.getTeam().getRedCardsCount() < 2)
          return Card.YellowRed;
        else
          return Card.None;
      }
      else
      {
        return Card.Yellow;
      }
      */
    }
    else
    {
      //red card
      //team can not have more than 2 red cards
      if (committer.getTeam().getRedCardsCount() < 2)
        return Card.Red;
      else if (committer.getCard() == Card.None)
        return Card.Yellow;
      else
        return Card.None;
    }
  }
  
  
  /**
   * Reports a goal to the referee. The AI decides if it is
   * given (returns true) or not (returns false).
   */
  public boolean goal(int team)
  {
    float rand = MathTools.random();
    
    //competence
    if (NormalDistribution.random10() >= info.getCompetence())
    {
      boolean ret = (rand < 0.9f); //10% of the goals are not given
      if (!ret)
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Goal not given: competence (random: " + rand + ")");
      return ret;
    }
    
    //prejudice
    if ((team == 0 && info.getPrejudice() > 0.1f) ||
      (team == 1 && info.getPrejudice() < -0.1f))
    {
      //referee does not like the team that scored
      if (NormalDistribution.random10() <= Math.abs(info.getPrejudice()))
      {
        boolean ret = (rand < 0.5f); //50% of the goals not given
        if (!ret)
          Logging.log(Logging.LEVEL_MESSAGES, this,
            "Goal not given: prejudice (prejudice: " + info.getPrejudice() +
            ", random: " + rand + ")");
        return ret;
      }
    }

    Logging.log(Logging.LEVEL_MESSAGES, this, "Goal given.");
    return true;
  }
  
  
  /**
   * Run to the stadium entrance position.
   */
  private void runToEntrance()
  {
    Vector3f fieldBorder = new Vector3f(1f, 0, 7);
    ArrayList<Action> actions = new ArrayList<Action>();
    actions.add(new RunToAction(referee,
      fieldBorder, Speed.Walk));
    actions.add(new RunToAction(referee,
      match.getStadium().getEntrancePosition(), Speed.Walk));
    referee.setActionSet(
      new GeneralActionSet(actions, ActionSet.PRIORITY_HIGH));
  }
  
  
  /**
   * LOM patch: Checks if there is someone disturbing the freekick.
   * 
   * @author Malte Löpmann
   */
  public boolean proceedFreekick(boolean regardTime)
  {
    Player player = this.getPlayerDisturbingFreekick();
    if(player != null)
    {     
      this.handleDelayOfGame(player, regardTime);
      return false;
    }
    
    return true;
  }
  
  
  /**
   * LOM patch: Checks if there is someone disturbing the goalkick.
   * 
   * @author Malte Löpmann
   */
  public boolean proceedGoalkick(boolean regardTime)
  {
    Player player = this.getPlayerDisturbingGoalkick();
    if(player != null)
    {
      this.handleDelayOfGame(player, regardTime);
      return false;
    }
    
    return true;
  }
  
    
  
  /**
   * LOM patch: Gets the player that stands near the freekick position, if any.
   * 
   * @author Malte Löpmann
   */
  private Player getPlayerDisturbingFreekick()
  {
    return match.getOpponentNearBall(match.getTeamIndex(match.getActiveTeam()), 
        freekickWallDistance);
  }
  
  
  /**
   * LOM patch: Gets the player that runs inside the penalty area, if any.
   * 
   * @author Malte Löpmann
   */
  private Player getPlayerDisturbingGoalkick()
  {
    return match.getOpponentInPenaltyArea(match.getTeamIndex(match.getActiveTeam()));
  }
  
  
  /**
   * LOM patch: Decide if there is a game delay (and maybe show a card).
   * 
   * @author Malte Löpmann
   */
  private void handleDelayOfGame(Player player, boolean regardTime)
  {       
    if (regardTime)
    {
      // only react, if delay is more than 5s
      if (lastStepTime - delayOfGameTime < 5.0f && delayOfGameTime != 0)
      {
        return;
      }
      else
      {
        if (delayOfGameTime == 0)
        {
          delayOfGameTime = lastStepTime;
          return;
        }
      }   
    }       
    
    //reset delayOfGameTime
    delayOfGameTime = 0;
    
    float points = getStrictnessAndPrejudice(player);
    points += delayOfGameCount / 3f;
    
    Card card;
    if (points < 1.5f)
    {
      //no card
      card = Card.None;
    }
    else
    {       
      card = this.getYellowCard(player);            
    }
    
    RefereeDecision decision = new DelayOfGameDecision(referee, card, player);
    this.makeDecision(decision);
    
    delayOfGameCount++;
  }      
  
  
  /**
   * LOM patch (method extracted): Returns a yellow card for
   * the given player, or none if the team has already two
   * red cards.
   * 
   * @author Andreas Wenger
   */
  private Card getYellowCard(Player player)
  {
    //yellow card
    if (player.getCard() == Card.Yellow)
    {
      //team can not have more than 2 red cards
      if (player.getTeam().getRedCardsCount() < 2)
        return Card.YellowRed;
      else
        return Card.None;
    }
    else
    {
      return Card.Yellow;
    }
  }
  
  
  /**
   * LOM patch (method extracted): Returns the strictness
   * and prejudice points of the referee for the
   * given player.
   * 
   * @author Andreas Wenger
   */
  private float getStrictnessAndPrejudice(Player player)
  {
    float points = 0;
    
    //strictness (-1 to +1)
    points += info.getStrictness() / 10f * 2f - 1f;
    
    //prejudice (-1 to +1)
    float predj = info.getPrejudice() / 10 * 
      (match.getTeam(0).isPlayerInTeam(player) ? 1 : -1);
    points += predj;
    
    //random factor (-1.5 to +1.5)
    points += NormalDistribution.random() * 3f - 1.5f;
    return points;
  }
  
  
}
