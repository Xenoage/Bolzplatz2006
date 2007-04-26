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
package com.xenoage.bp2k6.match.characters.enums;

import com.xenoage.bp2k6.util.MathTools;


/**
 * This enumeration contains all possible
 * animation stages for character classes.
 * Not all animations are supported by
 * all character subclasses.
 * 
 * @author Andreas Wenger
 */
public enum AnimationStage
{
  //nothing
  None(-1, true, true),
  //running (right leg forward)
  RunRight(0, true, true),
  //running (left leg forward)
  RunLeft(1, true, true),
  //shooting (while standing, with left foot)
  ShootStandLeft(2, false, false),
  //shooting (while standing, with right foot)
  ShootStandRight(3, false, false),
  //shooting (while running, with left foot) - not used at the moment
  ShootRunLeft(4, true, true),
  //shooting (while running, with right foot) - not used at the moment
  ShootRunRight(5, true, true),
  //normal waiting
  Wait(6, true, true),
  //waiting 1 (special animation when player is bored)
  WaitBored1(7, true, true),
  //waiting 2 (special animation when player is bored)
  WaitBored2(8, true, true),
  //jumping when happy
  ElationJump(9, false, false),
  //applauding when happy
  ElationApplause(10, false, false),
  //shaking the head when sad
  DesperationHeadshake(11, false, false),
  //tackling
  Tackle(12, false, false),
  //falling when fouled 1
  TackleTumble1(13, false, false),
  //falling when fouled 2
  TackleTumble2(14, false, false),
  //jumping to avoid foul 1
  TackleJump1(15, false, false),
  //jumping to avoid foul 2
  TackleJump2(16, false, false),
  //diving for the ball (left)
  DiveLeft(17, false, false),
  //diving for the ball (right)
  DiveRight(18, false, false),
  //throw in
  ThrowIn(19, false, true),
  //cup in hans
  CupInHands(20, false, false);

  //number of animation stages
  public static int animationCount = 21;
  
  //ID of the animation stage
  private int id;
  
  //if true, the character can change its speed
  //while this animation stage is performed,
  //if false, the speed of the beginning
  //applies for the whole animation stage
  private boolean flexibleSpeed;
  
  //if true, the character can change its direction
  //while this animation stage is performed,
  //if false, the direction of the beginning
  //applies for the whole animation stage
  private boolean flexibleRotation;
  
  
  
  AnimationStage(int id, boolean flexibleSpeed, boolean flexibleRotation)
  {
    this.id = id;
    this.flexibleSpeed = flexibleSpeed;
    this.flexibleRotation = flexibleRotation;
  }

  public int getID()
  {
    return id;
  }

  public boolean isSpeedFlexible()
  {
    return flexibleSpeed;
  }
  
  public boolean isRotationFlexible()
  {
    return flexibleRotation;
  }
  

  /**
   * Returns <code>true</code>, when animation stage is waiting.
  */
  public boolean isWaiting()
  {
    return (this == AnimationStage.Wait ||
      this == AnimationStage.WaitBored1 ||
      this == AnimationStage.WaitBored2);
  }
  
  
  /**
   * Returns <code>true</code>, when animation stage is tackling.
   */
  public boolean isTackling()
  {
    return (this == AnimationStage.Tackle);
  }

  
  /**
   * Returns <code>true</code>, when animation stage is tumbling.
   */
  public boolean isTumbling()
  {
    return (this == AnimationStage.TackleTumble1 ||
      this == AnimationStage.TackleTumble2);
  }
  
  
  /**
   * Returns <code>true</code>, when animation stage is
   * an emotion.
   */
  public boolean isEmotion()
  {
    return (this == AnimationStage.ElationJump ||
      this == AnimationStage.ElationApplause ||
      this == AnimationStage.DesperationHeadshake ||
      this == AnimationStage.WaitBored1 ||
      this == AnimationStage.WaitBored2);
  }
  
  
  /**
   * Returns <code>true</code>, when animation stage is
   * running.
   */
  public boolean isRunning()
  {
    return (this == AnimationStage.RunLeft ||
      this == AnimationStage.RunRight ||
      this == AnimationStage.ShootRunLeft ||
      this == AnimationStage.ShootRunRight);
  }
  
  
  /**
   * Returns <code>true</code>, when animation stage is
   * diving
   */
  public boolean isDiving()
  {
    return (this == AnimationStage.DiveLeft ||
      this == AnimationStage.DiveRight);
  }
  
  
  /**
   * Gets a random elation animation stage.
   * A extended set used: The player can also show
   * the wait-animation and tackle jump 1 or 2.
   */
  public static AnimationStage getRandomElation()
  {
    float rand = MathTools.random();
    if (rand < 0.3)
      return AnimationStage.ElationApplause;
    else if (rand < 0.6)
      return AnimationStage.ElationJump;
    else if (rand < 0.7)
      return AnimationStage.WaitBored1;
    else if (rand < 0.8)
      return AnimationStage.Wait;
    else if (rand < 0.9)
      return AnimationStage.TackleJump1;
    else
      return AnimationStage.TackleJump2;
    
  }
  
}
