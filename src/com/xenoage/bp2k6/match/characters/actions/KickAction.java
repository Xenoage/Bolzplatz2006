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

import com.xenoage.bp2k6.audio.SoundEffectPool.Sound;
import com.xenoage.bp2k6.match.Ball;
import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.Match.MatchStatus;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.enums.*;

import javax.vecmath.Vector3f;


/**
 * This action lets a Character kick/throw
 * the given ball with the given
 * velocity. 
 * 
 * @author Andreas Wenger
 *
 */
public class KickAction
  implements Action
{
  
  private com.xenoage.bp2k6.match.characters.Character character;
  private Ball ball;
  private Vector3f velocity;
  private boolean anim;
  private boolean force;
  private Sound soundEffect;

  
  /**
   * This action lets a Character kick/throw
   * the given ball with the given
   * velocity. 
   * If <code>anim</code> is <code>true</code>, a kick animation
   * is shown (if possible).
   * If <code>force</code> is <code>true</code>, the
   * pass is even performed if the character is not in ball
   * possession (otherwise he does not kick it). 
   */
  public KickAction(com.xenoage.bp2k6.match.characters.Character character,
    Ball ball, Vector3f velocity, boolean anim, boolean force,
    Sound soundEffect)
  {
    this.character = character;
    this.ball = ball;
    this.velocity = velocity;
    this.anim = anim;
    this.force = force;
    this.soundEffect = soundEffect;
  }
  

  /**
   * Returns <code>true</code>, because this
   * action can be interrupted immediately.
   */
  public boolean isInterruptible()
  {
    return true;
  }
  
  
  /**
   * Performs the pass.
   */
  public void perform()
  {
    character.kick(ball, velocity, anim, force);
    //penalty shootout: report kick
    if (character instanceof Player)
    {
      Player player = (Player) character;
      Match match = player.getTeam().getMatch();
      if (match.getMatchStatus() == MatchStatus.PENALTYKICK_PENALTYSHOOTOUT)
        match.penaltyShootoutBallShot(); //ball was shot
    }
    if (soundEffect != null)
      soundEffect.play();
  }
  
  
  /**
   * Returns <code>true</code>, when the kick is over.
   * When running or <code>anim</code> is false, this applies immediately,
   * if standing, this applies when the kick animation
   * is over.
   */
  public boolean isDone()
  {
    return !anim ||
      (character.getAnimationStage() != AnimationStage.ShootStandLeft &&
        character.getAnimationStage() != AnimationStage.ShootStandRight);
  }
  
  
}

