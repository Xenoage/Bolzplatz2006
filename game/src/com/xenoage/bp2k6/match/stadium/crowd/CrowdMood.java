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
package com.xenoage.bp2k6.match.stadium.crowd;

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.math.NormalDistribution;


/**
 * This class controls the mood of the crowd.
 * 
 * @author Andreas Wenger
 */
public class CrowdMood
{
  
  public enum CrowdMoodEvent
  {
    Goal, GoalNotGiven, YellowCard, RedCard, Penalty, Boredom, Minute80, EndOfMatch;
  }
  
  //team, which the crowd supports. 0 = home team, 1 = visitor team
  private int team = 0;
  
  //-10: very unhappy, 0: normal, 10: very happy
  private float baseMood = 3; //start with 3
  private float spontaneousMood = 0;
  private int ambienceIndex = 3;
  
  private boolean audienceWave = false; //mexican wave active?
  private float audienceWavePosition; //angle between 0 and 360 degrees
  private float nextAudienceWaveTime = 0; 
  
  private float lastStepTime;
  
  private float nextSoundEffectTime = 0;
  
  
  /**
   * Constructor.
   */
  public CrowdMood(int team)
  {
    this.team = team;
  }
  
  
  /**
   * Call this method as often as possible,
   * but it does not need to called in every
   * frame.
   */
  public void step(Crowd crowd)
  {
    float timeNow = GameEngine.getFrameTime(true);
    if (lastStepTime > 0)
    {
      float deltaTime = timeNow - lastStepTime;
      float mood = baseMood + spontaneousMood;
      
      //spontaneous mood cools down
      float loss = deltaTime * 0.8f;
      if (spontaneousMood > 0)
      {
        spontaneousMood = MathTools.clamp(
          spontaneousMood - loss, 0, 100);
      }
      else
      {
        spontaneousMood = MathTools.clamp(
          spontaneousMood + loss, -100, 0);
      }
      computeAmbienceIndex();
      
      //sound effects
      if (timeNow > nextSoundEffectTime)
      {
        //the more spectators, the more sound effects
        if (nextSoundEffectTime > 0 &&
          crowd.getSpectatorsCount() > MathTools.random(8000))
        {
          float rand = MathTools.random();
          if (rand < 0.3f)
            SoundEffectPool.Sound.CrowdFanfare.play();
          else if (rand < 0.7f)
            SoundEffectPool.Sound.CrowdDrumming.play();
          else
            SoundEffectPool.Sound.CrowdHorn.play();
          nextSoundEffectTime = 0;
        }
        if (mood >= 0 && nextSoundEffectTime == 0)
          nextSoundEffectTime = timeNow + 6 +
            MathTools.clamp(10 - mood, 0, 20);
      }
      
      //audience wave
      if (timeNow > nextAudienceWaveTime)
      {
        nextAudienceWaveTime += 15f; //check again in 15 seconds
        //start audience wave?
        audienceWave = (mood > 5 &&
          crowd.getSpectatorsCount() > 2000 && crowd.getAttendance() > 0.9f &&
          mood - 5 > NormalDistribution.random10()); 
      }
      audienceWavePosition = (audienceWavePosition + deltaTime * 60) % 360;
      
    }
    lastStepTime = timeNow;
  }
  
  
  /**
   * Gets the current mood value as a
   * ambience sound index (between 0 = very unhappy
   * and 6 = very happy)
   */
  public int getAmbienceIndex()
  {
    return ambienceIndex;
  }
  
  
  /**
   * Computes the ambient sound index.
   */
  private void computeAmbienceIndex()
  {
    float mood = baseMood + spontaneousMood;
    if (mood < -8)
      ambienceIndex = 0;
    else if (mood < -5)
      ambienceIndex = 1;
    else if (mood < -2)
      ambienceIndex = 2;
    else if (mood < 2)
      ambienceIndex = 3;
    else if (mood < 5)
      ambienceIndex = 4;
    else if (mood < 8)
      ambienceIndex = 5;
    else
      ambienceIndex = 6;
  }
  
  
  /**
   * Reports an event.
   */
  public void reportEvent(CrowdMoodEvent event, int eventTeam, int score[])
  {
    //eventTeam: 0 = own team, 1 = opponent team
    if (this.team != 0)
      eventTeam = 1 - eventTeam;
    
    if (event == CrowdMoodEvent.Goal)
    {
      //goal!
      if (eventTeam == 0)
      {
        //own team
        if (score[0] > score[1] + 1)
          changeMood(1, 5);
        else if (score[0] > score[1])
          changeMood(3, 15); //lead goal
        else if (score[0] == score[1])
          changeMood(2, 10); //equalizer
        else
          changeMood(1, 5); //catch up goal
      }
      else
      {
        //other team
        if (score[1] > score[0] + 1)
          changeMood(-1, -5);
        else if (score[1] > score[0])
          changeMood(-3, -15); //lead goal
        else if (score[1] == score[0])
          changeMood(-2, -10); //equalizer
        else
          changeMood(-1, -5); //catch up goal
      }
    }
    
    else if (event == CrowdMoodEvent.GoalNotGiven)
    {
      //goal not given
      if (eventTeam == 0)
        changeMood(-5, -15);
      else
        changeMood(0, 5);
    }
    
    else if (event == CrowdMoodEvent.YellowCard)
    {
      //yellow card
      if (eventTeam == 0)
        changeMood(0, -10);
      else
        changeMood(0, 5);
    }
    
    else if (event == CrowdMoodEvent.YellowCard)
    {
      //red card
      if (eventTeam == 0)
        changeMood(-3, -15);
      else
        changeMood(1, 10);
    }
    
    else if (event == CrowdMoodEvent.Penalty)
    {
      //red card
      if (eventTeam == 0)
        changeMood(1, 15);
      else
        changeMood(-1, -15);
    }
    
    else if (event == CrowdMoodEvent.Boredom)
    {
      //boredom
      changeMood(-2, 0);
    }
    
    else if (event == CrowdMoodEvent.Minute80)
    {
      //shortly before the game ends (80th minute),
      //if still draw: shout encouragement
      if (score[0] == score[1])
        changeMood(2, 8);
    }
    
    else if (event == CrowdMoodEvent.EndOfMatch)
    {
      //end of match
      if (score[0] > score[1])
        changeMood(3, 10);
      else if (score[0] == score[1])
        changeMood(0, -5);
      else
        changeMood(-3, -10);
    }
    
  }
  
  
  /**
   * Changes the mood.
   */
  private void changeMood(float addBase, float addSpontaneous)
  {
    baseMood = baseMood + addBase;
    spontaneousMood = spontaneousMood + addSpontaneous;
    computeAmbienceIndex();
  }
  
  
  /**
   * Gets the mood value (normally between -10 and 10).
   */
  public float getMoodValue()
  {
    return baseMood + spontaneousMood;
  }


  /**
   * Returns true, when an audience wave is running.
   */
  public boolean isAudienceWave()
  {
    return audienceWave;
  }


  /**
   * Gets the audience wave position in degrees.
   */
  public float getAudienceWavePosition()
  {
    return audienceWavePosition;
  }


  
}
