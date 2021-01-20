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
import com.xenoage.bp2k6.audio.SoundTrack;
import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import java.io.File;

/**
 * Class for the commentator.
 *
 * @author Andreas Wenger
 */
public class Commentator
{

  /**
   * List of commentated events.
   */
  public enum CommentatorEvent
  {
    Idle("00", 0),
    Greeting("01", 0),
    Halftime("02", 1),
    GoalLead("03", 1),
    GoalEqualize("04", 1),
    GoalClearLead("05", 1),
    GoalCatchUp("06", 1),
    GoalOther("07", 1),
    GoalOwnGoal("36", 1, GoalOther),
    GoalPenaltyShootout("38", 1, GoalOther),
    GoalNotGiven("22", 2), //play after normal goal comment
    PostHit("08", 0),
    ShootSave("09", 0),
    ShootMiss("10", 0),
    PenaltyKick("11", 1),
    CornerKick("12", 0),
    Foul("13", 0),
    FoulNoFoul("14", 0), //play _after_ 13
    FoulYellowCard("15", 0), //play _after_ 13
    FoulYellowRedCard("16", 0), //play _after_ 13
    FoulRedCard("17", 0), //play _after_ 13
    Mafioso("18", 0),
    GlassPlateHit("19", 0),
    GlassPlateBroken("20", 0),
    LawnmowerMan("21", 0),
    GreetingFirstLeague("23", 0, Greeting),
    GreetingAllstarsLeague("24", 0, Greeting),
    GreetingWorldCupGroup("25", 0, Greeting),
    GreetingWorldCupEighthFinal("37", 0, Greeting),
    GreetingWorldCupQuarterFinal("26", 0, Greeting),
    GreetingWorldCupSemiFinal("27", 0, Greeting),
    GreetingWorldCupFinal("28", 0, Greeting),
    GreetingWorldCupThirdPlaceMatch("29", 0, Greeting),
    FinalWhistleNarrowVictory("30", 1),
    FinalWhistleClearVictory("31", 1),
    FinalWhistleMatchDrawn("32", 1),
    FinalWhistleOvertime("33", 1),
    FinalWhistlePenaltyShootout("34", 1),
    FinalWhistlePenaltyShootoutFinished("35", 2);
    

    public final String id; //00 - 99
    public final int priority; //0 - 2
    public int commentsCount = 0; //number of comments for this event
    public int answerCount = 0; //number of answers (only needed for D/E)
    public CommentatorEvent replacement = null; //possible replacement comment
    
    CommentatorEvent(String id, int priority)
    { this(id, priority, null); }
    
    CommentatorEvent(String id, int priority, CommentatorEvent replacement)
    { this.id = id; this.priority = priority; this.replacement = replacement; }
  }

  //the current commentator ("A", "B", "C" or "DE")
  private String person = "B";
  //the last event
  private CommentatorEvent lastEvent = null;
  
  //the last foul-commentator (only needed for D/E)
  private char lastFoulPerson;

  //the path where commentary files can be found
  private final String commentaryPath = "sound/voices/";



  //constructor
  public Commentator(String person, int spectatorsCount)
  {
    //look for commentators.xml, perhaps we have to use
    //an other commentator.
    try
    {
      ConfigFile config = new ConfigFile("data/" + commentaryPath +
        Language.getCurrentLanguageID() + "/commentators.xml");
      String replacement = config.getValue(person);
      if (replacement.length() > 0)
      {
        if (replacement.equals("AB"))
          replacement = (MathTools.random() < 0.5f ? "A" : "B");
        else if (replacement.equals("B200A"))
        {
          if (spectatorsCount < 200)
            replacement = (MathTools.random() < 0.5f ? "A" : "B");
          else
            replacement = "A";
        }
        person = replacement;
      }
    }
    catch (Exception ex)
    {
    }
    
    //volume, force commentator
    try
    {
      ConfigFile conf = new ConfigFile("data/config/commentators.xml");
      float volume = conf.getValueFloat("volume");
      GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_COMMENTARY).setVolume(volume);
      String force = conf.getValue("force");
      if (force.length() > 0)
        person = force;
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_WARNINGS, this, "Could not load commentators config!");
    }
    
    this.person = person;
    //count comments for this commentator
    CommentatorEvent events[] = CommentatorEvent.values();
    if (person.equals("DE"))
    {
      //special commentators (D/E)
      for (int i = 0; i < events.length; i++)
      {
        CommentatorEvent e = events[i];
        int i2;
        
        //comments count
        int count = 0;
        for (int iAlt = 0; iAlt < 4; iAlt++)
        {
          //4 alternatives:
          String alt;
          if (iAlt == 0)
            alt = "D0";
          else if (iAlt == 1)
            alt = "D1";
          else if (iAlt == 2)
            alt = "E0";
          else
            alt = "E1";
          for (i2 = 1; i2 < 99; i2++)
          {
            if (!new File("data/" + commentaryPath +
              Language.getCurrentLanguageID() + "/" +
              alt +
              e.id + Cast.intToString(i2, 2) + ".ogg").exists())
              break;
            count++;
          }
        }
        e.commentsCount = count;
        
        //answers count
        count = 0;
        for (int iAlt = 0; iAlt < 2; iAlt++)
        {
          //2 alternatives:
          String alt;
          if (iAlt == 0)
            alt = "D2";
          else
            alt = "E2";
          for (i2 = 1; i2 < 99; i2++)
          {
            if (!new File("data/" + commentaryPath +
              Language.getCurrentLanguageID() + "/" +
              alt +
              e.id + Cast.intToString(i2, 2) + ".ogg").exists())
              break;
            count++;
          }
        }
        e.answerCount = count;
      }
    }
    else
    {
      //normal commentators
      for (int i = 0; i < events.length; i++)
      {
        CommentatorEvent e = events[i];
        int i2;
        for (i2 = 1; i2 < 99; i2++)
        {
          if (!new File("data/" + commentaryPath +
            Language.getCurrentLanguageID() + "/" +
            person + "0" +
            e.id + Cast.intToString(i2, 2) + ".ogg").exists())
            break;
        }
        e.commentsCount = i2 - 1;
      }
    }
  }

  
  /**
   * Reports a new event to the commentator.
   * It is not guaranteed that the commentator really speaks
   * out a comment about it.
   * Returns true, when the comment is really played.
   */
  public boolean newEvent(CommentatorEvent event)
  {
    //only play, if last comment is over or
    //if priority of the new event is higher
    SoundTrack st = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_COMMENTARY);
    if (st.isPlaying() && lastEvent != null &&
      lastEvent.priority >= event.priority)
    {
      //do nothing.
    }
    else
    {
      lastEvent = event;
      
      //if there is no comment, try to find a replacement
      while (event.commentsCount == 0 && event.replacement != null)
      {
        event = event.replacement;
      }
      
      if (event.commentsCount > 0)
      {
        if (person.equals("DE"))
          newEventDE(event);
        else
          newEventABC(event);
      }
      return true;
    }
    return false;
  }
  
  
  
  /**
   * Reports a new event to the commentator A, B or C.
   * It is not guaranteed that the commentator really speaks
   * out a comment about it.
   */
  private void newEventABC(CommentatorEvent event)
  {
    SoundTrack st = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_COMMENTARY);

    if (event.commentsCount > 0)
    {
      int commentIndex = MathTools.randomInt(event.commentsCount) + 1;
      String sound = person + "0" +
        event.id + Cast.intToString(commentIndex, 2) + ".ogg";
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Commentator: Event \"" + event.toString() + 
        "\", Sound: \"" + sound + "\"");
      
      st.setNextSound(commentaryPath +
        Language.getCurrentLanguageID() + "/" + sound, 0, 0);
      st.playNextSound();
    }
  }
  
  
  /**
   * Reports a new event to the commentators D/E.
   * It is not guaranteed that the commentators really speak
   * out a comment about it.
   */
  public void newEventDE(CommentatorEvent event)
  {
    SoundTrack st = GameEngine.getSoundTrack(GameEngine.SOUNDTRACK_COMMENTARY);

    if (event.commentsCount > 0)
    {
      
      String sound = "";
      //find commentary
      int commentAlt = MathTools.randomInt(4);
      int commentIndex = MathTools.randomInt(event.commentsCount);
      do
      {
        String alt;
        if (event == CommentatorEvent.FoulNoFoul ||
          event == CommentatorEvent.FoulYellowCard ||
          event == CommentatorEvent.FoulYellowRedCard ||
          event == CommentatorEvent.FoulRedCard)
        {
          //answer on foul
          alt = (lastFoulPerson == 'D' ? "E0" : "D0");
        }
        else
        {
          //normal comment
          if (commentAlt == 0)
            alt = "D0";
          else if (commentAlt == 1)
            alt = "D1";
          else if (commentAlt == 2)
            alt = "E0";
          else
            alt = "E1";
        }
        if (commentAlt == 0)
          commentIndex = (commentIndex + 1) % event.commentsCount;
        sound = alt + event.id + Cast.intToString(commentIndex + 1, 2) + ".ogg";
        commentAlt = (commentAlt + 1) % 4;
      } while (!new File("data/" + commentaryPath +
        Language.getCurrentLanguageID() + "/" + sound).exists());
      
      Logging.log(Logging.LEVEL_MESSAGES, this,
        "Commentator: Event \"" + event.toString() + 
        "\", Sound: \"" + sound + "\"");
      
      if (event == CommentatorEvent.Foul)
        lastFoulPerson = sound.charAt(0);
      
      st.setNextSound(commentaryPath +
        Language.getCurrentLanguageID() + "/" + sound, 0, 0);
      st.playNextSound();
      
      //answer needed?
      if (sound.charAt(1) == '1' && event.answerCount > 0)
      {
        //find answer
        String p = (sound.charAt(0) == 'D' ? "E2" : "D2");
        sound = "";
        commentIndex = MathTools.randomInt(event.answerCount);
        do
        {
          sound = p + event.id + Cast.intToString(commentIndex + 1, 2) + ".ogg";
          commentIndex = (commentIndex + 1) % event.answerCount;
        } while (!new File("data/" + commentaryPath +
          Language.getCurrentLanguageID() + "/" + sound).exists());
        
        Logging.log(Logging.LEVEL_MESSAGES, this,
          "Commentator answer: Event \"" + event.toString() + 
          "\", Sound: \"" + sound + "\"");
        
        //set answer sound, but do not play it now
        st.setNextSound(commentaryPath +
          Language.getCurrentLanguageID() + "/" + sound, 0, 0);
      }
      
    }
  }

  
  /**
   * Returns the ID of a random commentator
   * dependent on the current game mode.
   */
  public static String getGameModeCommentator()
  {
    float rand = MathTools.random();
    GameMode gameMode = GameEngine.getGameMode();
    if (gameMode == GameMode.Career)
    {
      //career
      int league = GameEngine.getCareer().getCurrentMatchDayLeague();
      if (league == Career.LEAGUE_3)
      {
        //league 3
        //A 80%, B 10%, C 10%
        if (rand < 0.8f)
          return "A";
        else if (rand < 0.9f)
          return "B";
        else
          return "C";
      }
      else if (league == Career.LEAGUE_2)
      {
        //league 2
        //A 20%, B 60%, C 20% 
        if (rand < 0.2f)
          return "A";
        else if (rand < 0.8f)
          return "B";
        else
          return "C";
      }
      else if (league == Career.LEAGUE_1)
      {
        //league 1
        //B 20%, C 50%, D/E 30%
        if (rand < 0.2f)
          return "B";
        else if (rand < 0.7f)
          return "C";
        else
          return "DE";
      }
      else if (league == Career.LEAGUE_ALLSTARS)
      {
        //allstars league: D/E 
        return "DE";
      }
    }
    else if (gameMode == GameMode.WorldCup)
    {
      //world cup: D/E 
      return "DE";
    }
    else if (gameMode == GameMode.FriendlyGame)
    {
      //friendly game
      //30% A, 40% B, 30% C
      if (rand < 0.3f)
        return "A";
      else if (rand < 0.7f)
        return "B";
      else
        return "C";
    }
    return "A";
  }

}
