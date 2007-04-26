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
package com.xenoage.bp2k6.gui;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.GameEngine.GameMode;
import com.xenoage.bp2k6.gamemodes.*;
import com.xenoage.bp2k6.gamemodes.reports.AllstarsLeague;
import com.xenoage.bp2k6.gamemodes.reports.MatchDay;
import com.xenoage.bp2k6.gamemodes.reports.MatchInfo;
import com.xenoage.bp2k6.gamemodes.reports.Table;
import com.xenoage.bp2k6.gamemodes.reports.TableEntry;
import com.xenoage.bp2k6.match.MatchManager;
import com.xenoage.bp2k6.match.TeamInfo;
import com.xenoage.bp2k6.match.referee.RefereeInfo;
import com.xenoage.bp2k6.match.stadium.Stadium;
import com.xenoage.bp2k6.match.stadium.StadiumInfo;
import com.xenoage.bp2k6.match.stadium.attractions.*;
import com.xenoage.bp2k6.match.stadium.floodlight.Floodlight;
import com.xenoage.bp2k6.match.stadium.scoreboard.Scoreboard;
import com.xenoage.bp2k6.util.ConfigFile;
import com.xenoage.bp2k6.util.MathTools;
import com.xenoage.bp2k6.util.language.Language;


/**
 * This class knows all GUI variables
 * and replaces them with values
 * from the game.
 * 
 * @author Andreas Wenger
 */
public class Variables
{
  
  /**
   * Replace the variables in the given String
   * and returns the result.
   */
  public static String replaceVariables(String s)
  {
    String ret = new String(s);
    GameMode gameMode = GameEngine.getGameMode();
    
    //use vocabulary?
    if (ret.startsWith("%lang:"))
    {
      ret = Language.get(ret.substring(6, ret.length() - 1));
    }
    
    //career variables
    if (gameMode == GameMode.Career)
    {
      //career specific info
      Career career = GameEngine.getCareer();
      
      //general info
      int currentMatchDayType = career.getCurrentMatchDayType();
      int currentMatchDayLeague = career.getCurrentMatchDayLeague();
      MatchInfo currentMatch = career.getCurrentUserMatch();
      if (currentMatch != null)
      {
        ret = replace(ret, "%career.hometeam.id%",
          currentMatch.getTeamHomeID());
        ret = replace(ret, "%career.visitorteam.id%",
          currentMatch.getTeamVisitorID());
        TeamInfo hometeam = 
          GameEngine.getMatchManager().getTeamInfo(currentMatch.getTeamHomeID());
        ret = replace(ret, "%career.hometeam.name%",
          hometeam.getName());
        TeamInfo visitorTeam = 
          GameEngine.getMatchManager().getTeamInfo(currentMatch.getTeamVisitorID());
        ret = replace(ret, "%career.visitorteam.name%",
          visitorTeam.getName());
        //info about current opponent
        String currentOpponent;
        if (currentMatch.getTeamHomeID().equals(career.getUserTeamID()))
          currentOpponent = currentMatch.getTeamVisitorID();
        else
          currentOpponent = currentMatch.getTeamHomeID();
        TeamInfo opponent =
          GameEngine.getMatchManager().getTeamInfo(currentOpponent);
        ret = replace(ret, "%career.currentopponent.name%",
          opponent.getName());
        
        
        //name of the league of the current matchday - also allstars
        ret = replace(ret, "%career.currentmatchdayleaguename%",
          career.getLeagueName(career.getCurrentMatchDayLeague()));
        
        //id of the current league - not allstars
        int l = career.getCurrentUserLeague();
        if (l == Career.LEAGUE_ALLSTARS) l = Career.LEAGUE_1;
        ret = replace(ret, "%career.currentleagueid%", career.getLeagueID(l));
        
        //id of the current league - not allstars
        ret = replace(ret, "%career.currentleaguename%", career.getLeagueName(l));
        
        ret = replace(ret, "%career.currentmatchdayname%",
          career.getMatchDayName(
            currentMatchDayType == Career.MATCHDAY_ALLSTARS ?
              career.getCurrentAllstarsMatchDayNumber() :
              career.getCurrentLeagueMatchDayNumber(),
              currentMatchDayLeague));
        
        ret = replace(ret, "%career.currentallstarsmatchdayname%",
          career.getMatchDayName(
            currentMatchDayType == Career.MATCHDAY_ALLSTARS ?
              career.getCurrentAllstarsMatchDayNumber() :
              career.getCurrentLeagueMatchDayNumber(),
              currentMatchDayLeague));
        
        //music for current matchday
        if (career.getCurrentMatchDayLeague() == Career.LEAGUE_ALLSTARS)
          ret = replace(ret, "%career.currentmatchdaymusic%", "music/allstars.ogg");
        else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_3)
          ret = replace(ret, "%career.currentmatchdaymusic%", "music/villageleague.ogg");
        else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_2)
          ret = replace(ret, "%career.currentmatchdaymusic%", "music/league.ogg");
        else if (career.getCurrentMatchDayLeague() == Career.LEAGUE_1)
          ret = replace(ret, "%career.currentmatchdaymusic%", "music/league.ogg");
        
        //music for higher league
        if (ret.indexOf("%career.higherleaguemusic%") > -1)
        {
          int league = career.getCurrentUserLeague() - 1;
          if (league < Career.LEAGUE_1)
            ret = replace(ret, "%career.higherleaguemusic%", "music/allstars.ogg");
          else
            ret = replace(ret, "%career.higherleaguemusic%", "music/league.ogg");
        }
        
        //index of the current league - not allstars:
        //0: league1, 1: league2, 2: league 3
        int leagueindex = career.getCurrentUserLeague();
        if (leagueindex == Career.LEAGUE_1 || leagueindex == career.LEAGUE_ALLSTARS)
          leagueindex = 0;
        else if (leagueindex == Career.LEAGUE_2)
          leagueindex = 1;
        else
          leagueindex = 2;
        ret = replace(ret, "%career.currentleagueindex%",
          String.valueOf(leagueindex));
        
        //the next referee
        if (ret.indexOf("%career.nextreferee") > -1)
        {
          RefereeInfo ref = career.getNextRefereeInfo();
          ret = replace(ret, "%career.nextreferee.name%", ref.getName());
          ret = replace(ret, "%career.nextreferee.loyalitydescription%",
            Language.get("loyality" + (int) (ref.getLoyality() / 2)));
        }
        if (ret.indexOf("%career.refereecorruption%") > -1)
        {
          if (career.isNextRefereeCorruptionAttempted())
            ret = replace(ret, "%career.refereecorruption%", "refereealreadycorrupted");
          else
            ret = replace(ret, "%career.refereecorruption%", "refereecorruption");
        }
        
        //current event
        if (ret.indexOf("%career.event") > -1 && career.getCurrentEvent() != null)
        {
          ret = replace(ret, "%career.event.title%",
            career.getCurrentEvent().getTitle());
          ret = replace(ret, "%career.event.text%",
            career.getCurrentEvent().getText());
        }
        
        //current league matchday number
        int leagueMatchDayNumber = career.getCurrentLeagueMatchDayNumber();
        ret = replace(ret, "%career.currentleaguematchdayminus1%",
          String.valueOf(leagueMatchDayNumber - 1));
        ret = replace(ret, "%career.currentleaguematchdayminus1max17%",
          String.valueOf(leagueMatchDayNumber - 1 > 17 ?
            17 : leagueMatchDayNumber - 1));
        if (leagueMatchDayNumber > 18) leagueMatchDayNumber = 18;
        ret = replace(ret, "%career.currentleaguematchday%",
          String.valueOf(leagueMatchDayNumber));
        
        //current allstars league matchday number
        int allstarsMatchDayNumber = career.getCurrentAllstarsMatchDayNumber();
        ret = replace(ret, "%career.currentallstarsmatchday%",
          String.valueOf(allstarsMatchDayNumber));
        ret = replace(ret, "%career.currentallstarsmatchdayminus1%",
          String.valueOf(allstarsMatchDayNumber - 1));
        ret = replace(ret, "%career.currentallstarsmatchdaymax6%",
          String.valueOf(allstarsMatchDayNumber > 6 ? 6 : allstarsMatchDayNumber));
        ret = replace(ret, "%career.currentallstarsmatchdayminus1max5%",
          String.valueOf(allstarsMatchDayNumber - 1 > 5 ?
            5 : allstarsMatchDayNumber - 1));
        
        int currentAllstarsGroup = career.getAllstars().getGroup(career.getUserTeamID());
        if (currentAllstarsGroup == -1) currentAllstarsGroup = 0;
        ret = replace(ret, "%career.currentallstarsgroup%",
          String.valueOf(currentAllstarsGroup));
        ret = replace(ret, "%career.currentyear%",
          String.valueOf(career.getCurrentYear()));
        ret = replace(ret, "%career.usermoney%",
          String.valueOf(career.getUserMoney()));
        ret = replace(ret, "%career.userteam.name%",
          GameEngine.getMatchManager().getTeamInfo(
            career.getUserTeamID()).getName());
        ret = replace(ret, "%career.isuserteaminallstarsleague%",
          career.getCurrentUserLeague() == Career.LEAGUE_ALLSTARS ?
            "true" : "false");
        //matchday stage: 1: group matchdays, 2: quarter finals,
        //3: semi finals, 3rdplace, final
        int matchDayStage = 1;
        if (career.getCurrentAllstarsMatchDayNumber() >=
          AllstarsLeague.MATCHDAY_SEMI)
          matchDayStage = 3;
        else if (career.getCurrentAllstarsMatchDayNumber() >=
          AllstarsLeague.MATCHDAY_QUARTER)
          matchDayStage = 2;
        ret = replace(ret, "%career.allstarsmatchdaystage%",
          String.valueOf(matchDayStage));
        //league table
        if (ret.indexOf("%career.viewedtable.") > -1)
        {
          //league
          String sLeague = Settings.getCareerViewedTableLeague();
          int league;
          if (sLeague.equals("league1"))
            league = Career.LEAGUE_1;
          else if (sLeague.equals("league2"))
            league = Career.LEAGUE_2;
          else
            league = Career.LEAGUE_3;
          //matchday
          int matchDayNumber = Settings.getCareerViewedTableMatchDay();
          //replace variables
          Table table = null;
          if (matchDayNumber == 0)
            table = career.getLeague(league).getEmptyTable();
          else
            table = career.getLeague(league).getMatchDay(matchDayNumber).getTable();
          for (int i = 1; i <= 10; i++)
          {
            TableEntry entry = table.getTableEntry(i);
            ret = replace(ret, "%career.viewedtable.name" + i + "%",
              GameEngine.getMatchManager().getTeamInfo(entry.getTeamID()).getName());
            ret = replace(ret, "%career.viewedtable.won" + i + "%",
              String.valueOf(entry.getGamesWon()));
            ret = replace(ret, "%career.viewedtable.drawn" + i + "%",
              String.valueOf(entry.getGamesDrawn()));
            ret = replace(ret, "%career.viewedtable.lost" + i + "%",
              String.valueOf(entry.getGamesLost()));
            String goalDifference = String.valueOf(entry.getGoalDifference());
            if (!goalDifference.startsWith("-"))
              goalDifference = "+" + goalDifference;
            ret = replace(ret, "%career.viewedtable.goals" + i + "%",
              goalDifference);
            ret = replace(ret, "%career.viewedtable.points" + i + "%",
              String.valueOf(entry.getPoints()));
          }
        }
        //league fixtures
        if (ret.indexOf("%career.viewedfixtures.") > -1)
        {
          //matchday
          int matchDayNumber = Settings.getCareerViewedFixturesMatchDay();
          if (matchDayNumber > 18) matchDayNumber = 18;
          //replace variables
          ret = replace(ret, "%career.viewedfixtures.matchday%",
            String.valueOf(matchDayNumber));
          MatchDay matchDay = career.getLeague(
            career.getCurrentUserLeague()).getMatchDay(matchDayNumber);
          for (int i = 1; i <= 5; i++)
          {
            MatchInfo matchInfo = matchDay.getMatch(i - 1);
            TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamHomeID());
            ret = replace(ret,
              "%career.viewedfixtures.match" + i + ".name1%",
              team.getName());
            team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamVisitorID());
            ret = replace(ret,
              "%career.viewedfixtures.match" + i + ".name2%",
              team.getName());
            if (matchDay.isMatchDayPlayed())
            {
              String score = matchInfo.getScore()[0] + ":" + 
              matchInfo.getScore()[1];
              ret = replace(ret,
                "%career.viewedfixtures.match" + i + ".result%", score);
            }
            else
            {
              ret = replace(ret,
                "%career.viewedfixtures.match" + i + ".result%", ":");
            }
          }
        }
        //allstars
        if (ret.indexOf("%career.viewedallstars.") > -1)
        {
          //group
          int group = Settings.getCareerViewedAllstarsGroup();
          ret = replace(ret, "%career.viewedallstars.groupname%",
            career.getAllstars().getGroupName(
              Settings.getCareerViewedAllstarsGroup()));
          //matchdays: matches and their results
          int matchDayNumber = Settings.getCareerViewedAllstarsMatchDay();
          ret = replace(ret, "%career.viewedallstars.matchday%",
            String.valueOf(matchDayNumber));
          
          //allstars group matches
          if (matchDayNumber <= AllstarsLeague.MATCHDAY_GROUP6)
          {
            
            MatchDay matchDay = career.getAllstars().getMatchDay(
              matchDayNumber, group);
            for (int i = 1; i <= matchDay.getMatchCount(); i++)
            {
              MatchInfo matchInfo = matchDay.getMatch(i - 1);
              if (matchInfo != null)
              {
                //matchday exists (it is already known who is playing)
                TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamHomeID());
                ret = replace(ret,
                  "%career.viewedallstars.match" + i + ".name1%",
                  team.getName());
                team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamVisitorID());
                ret = replace(ret,
                  "%career.viewedallstars.match" + i + ".name2%",
                  team.getName());
                if (matchDay.isMatchDayPlayed())
                {
                  String score = matchInfo.getScore()[0] + ":" + 
                  matchInfo.getScore()[1];
                  ret = replace(ret,
                    "%career.viewedallstars.match" + i + ".result%", score);
                }
                else
                {
                  ret = replace(ret,
                    "%career.viewedallstars.match" + i + ".result%", ":");
                }
              }
            }
          }
          
          //allstars group table
          if (matchDayNumber <= AllstarsLeague.MATCHDAY_GROUP6)
          {
            Table table = null;
            int tableDay = matchDayNumber;
            if (tableDay >= career.getCurrentAllstarsMatchDayNumber())
              tableDay = career.getCurrentAllstarsMatchDayNumber() - 1;
            if (tableDay == 0)
              table = career.getAllstars().getEmptyTable(group);
            else
              table = career.getAllstars().getMatchDay(tableDay, group).getTable();
            for (int i = 1; i <= 4; i++)
            {
              TableEntry entry = table.getTableEntry(i);
              ret = replace(ret, "%career.viewedallstars.name" + i + "%",
                GameEngine.getMatchManager().getTeamInfo(entry.getTeamID()).getName());
              ret = replace(ret, "%career.viewedallstars.won" + i + "%",
                String.valueOf(entry.getGamesWon()));
              ret = replace(ret, "%career.viewedallstars.drawn" + i + "%",
                String.valueOf(entry.getGamesDrawn()));
              ret = replace(ret, "%career.viewedallstars.lost" + i + "%",
                String.valueOf(entry.getGamesLost()));
              String goalDifference = String.valueOf(entry.getGoalDifference());
              if (!goalDifference.startsWith("-"))
                goalDifference = "+" + goalDifference;
              ret = replace(ret, "%career.viewedallstars.goals" + i + "%",
                goalDifference);
              ret = replace(ret, "%career.viewedallstars.points" + i + "%",
                String.valueOf(entry.getPoints()));
            }
          }
          
          //allstars quarter final matches
          if (ret.indexOf("%career.viewedallstars.quarter") > -1)
          {
            MatchDay matchDay = career.getAllstars().getMatchDay(
              AllstarsLeague.MATCHDAY_QUARTER, 0);
            for (int i = 1; i <= matchDay.getMatchCount(); i++)
            {
              MatchInfo matchInfo = matchDay.getMatch(i - 1);
              if (matchInfo != null)
              {
                //matchday exists (it is already known who is playing)
                TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamHomeID());
                ret = replace(ret,
                  "%career.viewedallstars.quarter" + i + ".name1%",
                  team.getName());
                team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamVisitorID());
                ret = replace(ret,
                  "%career.viewedallstars.quarter" + i + ".name2%",
                  team.getName());
                if (matchDay.isMatchDayPlayed())
                {
                  String score = matchInfo.getScore()[0] + ":" + 
                  matchInfo.getScore()[1];
                  ret = replace(ret,
                    "%career.viewedallstars.quarter" + i + ".result%", score);
                }
                else
                {
                  ret = replace(ret,
                    "%career.viewedallstars.quarter" + i + ".result%", ":");
                }
              }
              else
              {
                //matchday does not exist (it is not known who will be playing)
                //quarter finals:
                //1. group a : 2. group b
                //1. group b : 2. group c
                //...
                ret = replace(ret,
                  "%career.viewedallstars.quarter" + i + ".name1%",
                  Language.get("firstplace") + " " +
                  career.getAllstars().getGroupName(i - 1));
                ret = replace(ret,
                  "%career.viewedallstars.quarter" + i + ".name2%",
                  Language.get("secondplace") + " " +
                  career.getAllstars().getGroupName(i % 4));
                ret = replace(ret,
                  "%career.viewedallstars.quarter" + i + ".result%", ":");
              }
            }
          }
          
          
          //allstars semi matches
          if (ret.indexOf("%career.viewedallstars.semi") > -1)
          {
            MatchDay matchDay = career.getAllstars().getMatchDay(
              AllstarsLeague.MATCHDAY_SEMI, 0);
            for (int i = 1; i <= matchDay.getMatchCount(); i++)
            {
              MatchInfo matchInfo = matchDay.getMatch(i - 1);
              if (matchInfo != null)
              {
                //matchday exists (it is already known who is playing)
                TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamHomeID());
                ret = replace(ret,
                  "%career.viewedallstars.semi" + i + ".name1%",
                  team.getName());
                team = GameEngine.getMatchManager().getTeamInfo(
                  matchInfo.getTeamVisitorID());
                ret = replace(ret,
                  "%career.viewedallstars.semi" + i + ".name2%",
                  team.getName());
                if (matchDay.isMatchDayPlayed())
                {
                  String score = matchInfo.getScore()[0] + ":" + 
                  matchInfo.getScore()[1];
                  ret = replace(ret,
                    "%career.viewedallstars.semi" + i + ".result%", score);
                }
                else
                {
                  ret = replace(ret,
                    "%career.viewedallstars.semi" + i + ".result%", ":");
                }
              }
              else
              {
                //matchday does not exist (it is not known who will be playing)
                String unknown = Language.get("yetunknown");
                ret = replace(ret,
                  "%career.viewedallstars.semi" + i + ".name1%", unknown);
                ret = replace(ret,
                  "%career.viewedallstars.semi" + i + ".name2%", unknown);
                ret = replace(ret,
                  "%career.viewedallstars.semi" + i + ".result%", ":");
              }
            }
          }
          
          
          //allstars 3rdplace matche
          if (ret.indexOf("%career.viewedallstars.3rdplace") > -1)
          {
            MatchDay matchDay = career.getAllstars().getMatchDay(
              AllstarsLeague.MATCHDAY_THIRDPLACE, 0);
            MatchInfo matchInfo = matchDay.getMatch(0);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%career.viewedallstars.3rdplace.name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%career.viewedallstars.3rdplace.name2%",
                team.getName());
              if (matchDay.isMatchDayPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%career.viewedallstars.3rdplace.result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%career.viewedallstars.3rdplace.result%", ":");
              }
            }
            else
            {
              //matchday does not exist (it is not known who will be playing)
              String unknown = Language.get("yetunknown");
              ret = replace(ret,
                "%career.viewedallstars.3rdplace.name1%", unknown);
              ret = replace(ret,
                "%career.viewedallstars.3rdplace.name2%", unknown);
              ret = replace(ret,
                "%career.viewedallstars.3rdplace.result%", ":");
            }
          }
          
          //allstars final match
          if (ret.indexOf("%career.viewedallstars.final") > -1)
          {
            MatchDay matchDay = career.getAllstars().getMatchDay(
              AllstarsLeague.MATCHDAY_FINAL, 0);
            MatchInfo matchInfo = matchDay.getMatch(0);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%career.viewedallstars.final.name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%career.viewedallstars.final.name2%",
                team.getName());
              if (matchDay.isMatchDayPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%career.viewedallstars.final.result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%career.viewedallstars.final.result%", ":");
              }
            }
            else
            {
              //matchday does not exist (it is not known who will be playing)
              String unknown = Language.get("yetunknown");
              ret = replace(ret,
                "%career.viewedallstars.final.name1%", unknown);
              ret = replace(ret,
                "%career.viewedallstars.final.name2%", unknown);
              ret = replace(ret,
                "%career.viewedallstars.final.result%", ":");
            }
          }
          
          
        }
      }
      
      //stadium information
      if (ret.indexOf("%career.stadium") > -1)
      {
        Stadium stadium = career.getUserStadium();
        StadiumInfo stadiumInfo = stadium.getStadiumInfo();
        
        ret = replace(ret, "%career.stadium.id%",
          String.valueOf(stadiumInfo.getID()));
        ret = replace(ret, "%career.stadium.maxspectators%",
          String.valueOf(stadiumInfo.getMaxSpectators()));
        ret = replace(ret, "%career.stadium.adboardspercent%",
          String.valueOf(
            (int) (stadium.getCareerAdboardsRelativeNumber() * 100)));
        ret = replace(ret, "%career.stadium.lawn%",
          stadium.getField().getLawn().getLocalName());
        ret = replace(ret, "%career.stadium.goals%",
          stadiumInfo.getGoalsName());
        ret = replace(ret, "%career.stadium.lighting%",
          stadium.getLightingQuality());
        ret = replace(ret, "%career.stadium.scoreboard%",
          stadium.getScoreBoardName());
        ret = replace(ret, "%career.stadium.attractions%",
          stadium.getAttractionsQualityLocalString());
        
        ret = replace(ret, "%career.stadium.goals1cost%",
          Language.get("alreadybought"));
        ret = replace(ret, "%career.stadium.goals2cost%",
          stadiumInfo.isWoodenGoalsBought() ?
            Language.get("alreadybought") : String.valueOf(
              stadiumInfo.getGoalTypeCost(StadiumInfo.Goals.WoodenGoals)) + " €");
        ret = replace(ret, "%career.stadium.goals3cost%",
          stadiumInfo.isAluminumGoalsBought() ?
            Language.get("alreadybought") : String.valueOf(
              stadiumInfo.getGoalTypeCost(StadiumInfo.Goals.AluminumGoals)) + " €");
        
        ret = replace(ret, "%career.stadium.stage0name%",
          Language.get("stadium_stage0name"));
        ret = replace(ret, "%career.stadium.stage1name%",
          Language.get("stadium_stage1name"));
        ret = replace(ret, "%career.stadium.stage2name%",
          Language.get("stadium_stage2name"));
        ret = replace(ret, "%career.stadium.stage3name%",
          Language.get("stadium_stage3name"));
        
        if (ret.indexOf("%career.stadium.buildblock") > -1)
        {
          int stage = Settings.getCareerStadiumBuildBlock();
          ret = replace(ret, "%career.stadium.buildblock.name%",
            Language.get("stadium_stage" + stage + "name"));
          for (int i = 1; i <= 8; i++)
          {
            if (stage == 0)
            {
              if (stadium.isCareerAdboardBlockBuilt(i))
              {
                ret = replace(ret, "%career.stadium.buildblock.cost" + i + "%",
                  Language.get("built"));
              }
              else
              {
                ret = replace(ret, "%career.stadium.buildblock.cost" + i + "%",
                  String.valueOf(stadium.getCareerBlockCost(0, i)) + " €");
              }
            }
            else if (stadium.isCareerStandBlockBuilt(stage, i))
            {
              ret = replace(ret, "%career.stadium.buildblock.cost" + i + "%",
                Language.get("built"));
            }
            else
            {
              ret = replace(ret, "%career.stadium.buildblock.cost" + i + "%",
                String.valueOf(stadium.getCareerBlockCost(stage, i)) + " €");
            }
          }
        }
        
        if (ret.indexOf("%career.stadium.light") > -1)
        {
          for (int i = 1; i <= 4; i++)
          {
            ret = replace(ret, "%career.stadium.light" + i + "%",
              stadium.getCareerLightLocalString(i));
          }
        }
        
        if (ret.indexOf("%career.stadium.buildlight") > -1)
        {
          int position = Settings.getCareerStadiumBuildLight();
          ret = replace(ret, "%career.stadium.buildlight.name%",
            Language.get("stadium_light" + position + "name"));
          for (int i = 1; i <= 3; i++)
          {
            if (stadium.getCareerLight(position) != null &&
              stadium.getCareerLight(position).getStage() == i)
            {
              ret = replace(ret, "%career.stadium.buildlight.cost" + i + "%",
                Language.get("built"));
            }
            else
            {
              ret = replace(ret, "%career.stadium.buildlight.cost" + i + "%",
                String.valueOf(Floodlight.getCost(i)) + " €");
            }
          }
        }
        
        ret = replace(ret, "%career.stadium.lawncarecost%",
          String.valueOf(stadium.getField().getLawn().lawnCareCost));
        
        if (ret.indexOf("%career.stadium.scoreboard") > -1)
        {
          for (int i = 1; i <= 3; i++)
          {
            if (stadium.getScoreboard() != null &&
              stadium.getScoreboard().getStage() == i)
            {
              ret = replace(ret, "%career.stadium.scoreboard" + i + ".cost%",
                Language.get("built"));
            }
            else
            {
              ret = replace(ret, "%career.stadium.scoreboard" + i + ".cost%",
                String.valueOf(Scoreboard.getCost(i)) + " €");
            }
          }
        }
        
        
        if (ret.indexOf("%career.stadium.attractions") > -1)
        {
          //hotdog stand
          ret = replace(ret, "%career.stadium.attractions.hotdogstand.cost%",
            (stadium.getHotDogStand() != null ?
              Language.get("built") : String.valueOf(HotDogStand.getCost()) + " €"));
          //betting office
          ret = replace(ret, "%career.stadium.attractions.bettingoffice.cost%",
            (stadium.getBettingOffice() != null ?
              Language.get("built") : String.valueOf(BettingOffice.getCost()) + " €"));
          //beer bar
          ret = replace(ret, "%career.stadium.attractions.beerbar.cost%",
            (stadium.getBeerBar() != null ?
              Language.get("built") : String.valueOf(BeerBar.getCost()) + " €"));
          //track
          ret = replace(ret, "%career.stadium.attractions.track.cost%",
            (stadium.getTrack() != null ?
              Language.get("built") : String.valueOf(Track.getCost()) + " €"));
          //high jump
          ret = replace(ret, "%career.stadium.attractions.highjump.cost%",
            (stadium.getHighJump() != null ?
              Language.get("built") : String.valueOf(HighJump.getCost()) + " €"));
        }
        
      }
      
      //referee corruption
      if (ret.indexOf("%career.corruption") > -1)
      {
        for (int i = 1; i <= 3; i++)
        {
          ret = replace(ret, "%career.corruption.cost" + i + "%",
            String.valueOf(RefereeInfo.getCorruptionCost(
              career.getCurrentMatchDayLeague(), i)));
        }
      }
      
    }
    else if (gameMode == GameMode.WorldCup)
    {
      //worldcup specific info
      WorldCup worldCup = GameEngine.getWorldCup();
      if (worldCup != null)
      {
        int matchDayNumber = worldCup.getMatchDayNumber();
        
        //general info
        TeamInfo userTeam =
          GameEngine.getMatchManager().getTeamInfo(worldCup.getUserTeamID());
        ret = replace(ret, "%worldcup.userteam.id%", userTeam.getID());
        ret = replace(ret, "%worldcup.userteam.name%", userTeam.getName());
        //current match with the user's team
        MatchInfo currentMatch = worldCup.getUserMatch();
        if (currentMatch != null)
        {
          ret = replace(ret, "%worldcup.hometeam.id%", currentMatch.getTeamHomeID());
          ret = replace(ret, "%worldcup.visitorteam.id%", currentMatch.getTeamVisitorID());
          ret = replace(ret, "%worldcup.opponent.name%",
            GameEngine.getMatchManager().getTeamInfo(
              currentMatch.getOpponentID(worldCup.getUserTeamID())).getName());
        }
        //the user's group
        ret = replace(ret, "%worldcup.usergroup%",
          String.valueOf(worldCup.getUserGroupIndex()));
        //name of the current matchday
        ret = replace(ret, "%worldcup.currentmatchdayname%",
          WorldCupInfo.getMatchDayName(matchDayNumber));
        //the current matchday number, but max 3
        ret = replace(ret, "%worldcup.currentmatchdaymax3%",
          String.valueOf(MathTools.trim(matchDayNumber, 3)));
        //the current matchday number, but max 3
        ret = replace(ret, "%worldcup.currentmatchdayminus1max2%",
          String.valueOf(MathTools.trim(matchDayNumber - 1, 2)));
        
        //the next referee
        if (ret.indexOf("%worldcup.nextreferee") > -1)
        {
          RefereeInfo ref = GameEngine.getMatchManager().getRefereeInfo(
            worldCup.getNextRefereeID());
          ret = replace(ret, "%worldcup.nextreferee.name%", ref.getName());
        }
        
        //champion team
        MatchInfo finalMatch = worldCup.getFinalMatch(WorldCup.FINAL_FINAL);
        if (finalMatch != null)
        {
          ret = replace(ret, "%worldcup.champion.name%",
            GameEngine.getMatchManager().getTeamInfo(
              finalMatch.getWinnerTeamID()).getName());
        }
        
        //viewed group/final
        
        //the viewed matchday number
        int viewMatchday = Settings.getWorldcupViewMatchday();
        ret = replace(ret, "%worldcup.view.matchday%",
          String.valueOf(viewMatchday));
        
        //group
        int viewGroup = Settings.getWorldcupViewGroup();
        ret = replace(ret, "%worldcup.view.group.name%",
          WorldCupInfo.getGroupName(viewGroup));
        
        //worldcup group matches
        if (viewMatchday <= WorldCupInfo.groupMatchDays)
        {
          MatchDay matchDay = worldCup.getGroup(viewGroup).getMatchDay(viewMatchday);
          for (int i = 1; i <= matchDay.getMatchCount(); i++)
          {
            MatchInfo matchInfo = matchDay.getMatch(i - 1);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%worldcup.view.match" + i + ".name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%worldcup.view.match" + i + ".name2%",
                team.getName());
              if (matchDay.isMatchDayPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%worldcup.view.match" + i + ".result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%worldcup.view.match" + i + ".result%", ":");
              }
            }
          }
        }
        
        //worldcup group table
        if (viewMatchday <= WorldCupInfo.groupMatchDays)
        {
          Table table = null;
          int tableDay = viewMatchday;
          if (tableDay >= matchDayNumber)
            tableDay = matchDayNumber - 1;
          if (tableDay == 0)
            table = worldCup.getGroup(viewGroup).getEmptyTable();
          else
            table = worldCup.getGroup(viewGroup).getMatchDay(tableDay).getTable();
          for (int i = 1; i <= 4; i++)
          {
            TableEntry entry = table.getTableEntry(i);
            ret = replace(ret, "%worldcup.view.name" + i + "%",
              GameEngine.getMatchManager().getTeamInfo(entry.getTeamID()).getName());
            ret = replace(ret, "%worldcup.view.won" + i + "%",
              String.valueOf(entry.getGamesWon()));
            ret = replace(ret, "%worldcup.view.drawn" + i + "%",
              String.valueOf(entry.getGamesDrawn()));
            ret = replace(ret, "%worldcup.view.lost" + i + "%",
              String.valueOf(entry.getGamesLost()));
            String goalDifference = String.valueOf(entry.getGoalDifference());
            if (!goalDifference.startsWith("-"))
              goalDifference = "+" + goalDifference;
            ret = replace(ret, "%worldcup.view.goals" + i + "%",
              goalDifference);
            ret = replace(ret, "%worldcup.view.points" + i + "%",
              String.valueOf(entry.getPoints()));
          }
        }
        
        
        //worldcup eighth final matches
        if (ret.indexOf("%worldcup.eighth") > -1)
        {
          for (int i = 1; i <= 8; i++)
          {
            MatchInfo matchInfo = worldCup.getFinalMatch(i + WorldCup.FINAL_EIGHTH1 - 1);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%worldcup.eighth" + i + ".name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%worldcup.eighth" + i + ".name2%",
                team.getName());
              if (matchInfo.isPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%worldcup.eighth" + i + ".result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%worldcup.eighth" + i + ".result%", ":");
              }
            }
            else
            {
              //matchday does not exist (it is not known who will be playing)
              //eighth finals:
              //a1:b2; c1:d2; e1:f2; g1:h2;
              //b1:a2; d1:c2; f1:e2; h1:g2;
              //...
              ret = replace(ret,
                "%worldcup.eighth" + i + ".name1%",
                Language.get("firstplace") + " " +
                WorldCupInfo.getGroupName(i - 1));
              ret = replace(ret,
                "%worldcup.eighth" + i + ".name2%",
                Language.get("secondplace") + " " +
                WorldCupInfo.getGroupName(i % 8));
              ret = replace(ret,
                "%worldcup.eighth" + i + ".result%", ":");
            }
          }
        }
        
        
        //worldcup quarter final matches
        if (ret.indexOf("%worldcup.quarter") > -1)
        {
          for (int i = 1; i <= 4; i++)
          {
            MatchInfo matchInfo = worldCup.getFinalMatch(i + WorldCup.FINAL_QUARTER1 - 1);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%worldcup.quarter" + i + ".name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%worldcup.quarter" + i + ".name2%",
                team.getName());
              if (matchInfo.isPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%worldcup.quarter" + i + ".result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%worldcup.quarter" + i + ".result%", ":");
              }
            }
            else
            {
              //matchday does not exist (it is not known who will be playing)
              //quarter finals:
              //winner eight 1 : winner eight 2
              //winner eight 3 : winner eight 4
              //...
              ret = replace(ret,
                "%worldcup.quarter" + i + ".name1%",
                Language.get("winner") + " " +
                Language.get("eighthfinal") + " " + ((i - 1) * 2 + 1));
              ret = replace(ret,
                "%worldcup.quarter" + i + ".name2%",
                Language.get("winner") + " " +
                Language.get("eighthfinal") + " " + ((i - 1) * 2 + 2));
              ret = replace(ret,
                "%worldcup.quarter" + i + ".result%", ":");
            }
          }
        }
        
        
        //worldcup semi matches
        if (ret.indexOf("%worldcup.semi") > -1)
        {
          for (int i = 1; i <= 2; i++)
          {
            MatchInfo matchInfo = worldCup.getFinalMatch(i + WorldCup.FINAL_SEMI1 - 1);
            if (matchInfo != null)
            {
              //matchday exists (it is already known who is playing)
              TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamHomeID());
              ret = replace(ret,
                "%worldcup.semi" + i + ".name1%",
                team.getName());
              team = GameEngine.getMatchManager().getTeamInfo(
                matchInfo.getTeamVisitorID());
              ret = replace(ret,
                "%worldcup.semi" + i + ".name2%",
                team.getName());
              if (matchInfo.isPlayed())
              {
                String score = matchInfo.getScore()[0] + ":" + 
                matchInfo.getScore()[1];
                ret = replace(ret,
                  "%worldcup.semi" + i + ".result%", score);
              }
              else
              {
                ret = replace(ret,
                  "%worldcup.semi" + i + ".result%", ":");
              }
            }
            else
            {
              //matchday does not exist (it is not known who will be playing)
              ret = replace(ret,
                "%worldcup.semi" + i + ".name1%",
                Language.get("winner") + " " +
                Language.get("quarterfinal") + " " + ((i - 1) * 2 + 1));
              ret = replace(ret,
                "%worldcup.semi" + i + ".name2%",
                Language.get("winner") + " " +
                Language.get("quarterfinal") + " " + ((i - 1) * 2 + 2));
              ret = replace(ret,
                "%worldcup.semi" + i + ".result%", ":");
            }
          }
        }
        
        
        //worldcup 3rdplace matche
        if (ret.indexOf("%worldcup.3rdplace") > -1)
        {
          MatchInfo matchInfo = worldCup.getFinalMatch(WorldCup.FINAL_THIRDPLACE);
          if (matchInfo != null)
          {
            //matchday exists (it is already known who is playing)
            TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamHomeID());
            ret = replace(ret,
              "%worldcup.3rdplace.name1%",
              team.getName());
            team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamVisitorID());
            ret = replace(ret,
              "%worldcup.3rdplace.name2%",
              team.getName());
            if (matchInfo.isPlayed())
            {
              String score = matchInfo.getScore()[0] + ":" + 
              matchInfo.getScore()[1];
              ret = replace(ret,
                "%worldcup.3rdplace.result%", score);
            }
            else
            {
              ret = replace(ret,
                "%worldcup.3rdplace.result%", ":");
            }
          }
          else
          {
            //matchday does not exist (it is not known who will be playing)
            ret = replace(ret,
              "%worldcup.3rdplace.name1%",
              Language.get("loser") + " " +
              Language.get("semifinal") + " 1");
            ret = replace(ret,
              "%worldcup.3rdplace.name2%",
              Language.get("loser") + " " +
              Language.get("semifinal") + " 2");
            ret = replace(ret,
              "%worldcup.3rdplace.result%", ":");
          }
        }
        
        //worldcup final match
        if (ret.indexOf("%worldcup.final") > -1)
        {
          MatchInfo matchInfo = worldCup.getFinalMatch(WorldCup.FINAL_FINAL);
          if (matchInfo != null)
          {
            //matchday exists (it is already known who is playing)
            TeamInfo team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamHomeID());
            ret = replace(ret,
              "%worldcup.final.name1%",
              team.getName());
            team = GameEngine.getMatchManager().getTeamInfo(
              matchInfo.getTeamVisitorID());
            ret = replace(ret,
              "%worldcup.final.name2%",
              team.getName());
            if (matchInfo.isPlayed())
            {
              String score = matchInfo.getScore()[0] + ":" + 
              matchInfo.getScore()[1];
              ret = replace(ret,
                "%worldcup.final.result%", score);
            }
            else
            {
              ret = replace(ret,
                "%worldcup.final.result%", ":");
            }
          }
          else
          {
            //matchday does not exist (it is not known who will be playing)
            ret = replace(ret,
              "%worldcup.final.name1%",
              Language.get("winner") + " " +
              Language.get("semifinal") + " 1");
            ret = replace(ret,
              "%worldcup.final.name2%",
              Language.get("winner") + " " +
              Language.get("semifinal") + " 2");
            ret = replace(ret,
              "%worldcup.final.result%", ":");
          }
        }
        
        
      }
    }
    else if (gameMode == GameMode.FriendlyGame)
    {
      //friendly game variables
    }
    
    //savegames
    if (ret.indexOf("%savegame.career") > -1)
    {
      for (int i = 1; i <= 5; i++)
      {
        ret = replace(ret, "%savegame.career" + i + "%",
          SaveGames.getCareerSlotInfo(i));
      }
    }
    if (ret.indexOf("%savegame.worldcup") > -1)
    {
      for (int i = 1; i <= 5; i++)
      {
        ret = replace(ret, "%savegame.worldcup" + i + "%",
          SaveGames.getWorldCupSlotInfo(i));
      }
    }
    
    //team info
    if (ret.indexOf("%teaminfo.") > -1)
    {
      String teamID = Settings.getTeamInfoTeam();
      if (teamID != null && teamID.length() > 0)
      {
        TeamInfo teamInfo = new TeamInfo(teamID);
        ret = replace(ret, "%teaminfo.name%",
          teamInfo.getName());
        ret = replace(ret, "%teaminfo.attack%",
          String.valueOf((int) (teamInfo.getAverageAttack() + 0.5f)));
        ret = replace(ret, "%teaminfo.defense%",
          String.valueOf((int) (teamInfo.getAverageDefense() + 0.5f)));
        ret = replace(ret, "%teaminfo.speed%",
          String.valueOf((int) (teamInfo.getAverageSpeed() + 0.5f)));
        ret = replace(ret, "%teaminfo.stamina%",
          String.valueOf((int) (teamInfo.getAverageStamina() + 0.5f)));
      }
    }
    
    //file not found
    if (ret.indexOf("%gamemode.") > -1)
    {
      ret = replace(ret, "%gamemode.missingteam%", SaveGames.getMissingTeamID());
    }
    
    //match loading screen
    if (ret.indexOf("%match.") > -1)
    {
      MatchManager matchManager = GameEngine.getMatchManager();
      ret = replace(ret, "%match.team1.id%",
        matchManager.getNextMatchTeams()[0]);
      ret = replace(ret, "%match.team2.id%",
        matchManager.getNextMatchTeams()[1]);
      ret = replace(ret, "%match.team1.name%",
        matchManager.getTeamInfo(matchManager.getNextMatchTeams()[0]).getName());
      ret = replace(ret, "%match.team2.name%",
        matchManager.getTeamInfo(matchManager.getNextMatchTeams()[1]).getName());
      ret = replace(ret, "%match.referee.name%",
        matchManager.getRefereeInfo(matchManager.getNextMatchReferee()).getName());
      ret = replace(ret, "%match.team1.stadium.index%",
        String.valueOf(matchManager.getStadiumIndex(
          matchManager.getTeamInfo(matchManager.getNextMatchTeams()[0]).getStadium())));
      
    }
    
    //setting
    if (ret.indexOf("%setting.") > -1)
    {
      if (ret.equals("%setting.matchduration.index%"))
      {
        float timeFactor = ConfigFile.getValueFromFileFloat(
          "data/config/match.xml", "timefactor");
        if (timeFactor == 90)
          ret = "0";
        else if (timeFactor == 45)
          ret = "1";
        else if (timeFactor == 30)
          ret = "2";
        else if (timeFactor == 22.5f)
          ret = "3";
        else if (timeFactor == 18)
          ret = "4";
        else
          ret = "0";   
      }
    }
    
    
    return ret;
  }
  
  
  /**
   * Replaces the given String, if found.
   */
  private static String replace(
    String template, String variable, String replacement)
  {
    if (template.indexOf(variable) > -1)
    {
      if (replacement != null)
      {
        return template.replaceAll(variable, replacement);
      }
      else
      {
        return template.replaceAll(variable, "%null%");
      }
    }
    else
      return template;
  }

}
