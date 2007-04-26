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

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.match.characters.enums.PlayerPosition;
import com.xenoage.bp2k6.match.referee.Card;
import com.xenoage.bp2k6.match.teamai.TeamAI;
import com.xenoage.bp2k6.util.*;

import javax.vecmath.Vector3f;

import net.sf.jirr.ITexture;


/**
 * Class for a team with 4 players and 1 goalkeeper
 * It contains all information about a team
 * but also all objects that are needed to simulate
 * and display the team in a match.
 *
 * @author Andi
 */
public class Team
{
  private TeamInfo teamInfo;
  private final int playersCount = 5;
  private Player[] players = new Player[playersCount];

  //textures 
  private ITexture playerTexture;
  private ITexture textureCap;
  private ITexture textureGoalkeeper;

  private TeamAI teamAI;
  private int currentSide = -1; //-1: left team, 1: right team
  private Player ballPlayer = null; //player with ball possession

  private TeamControl teamControl = TeamControl.AI;
  private PlayerController playerController;

  //needed for move()
  private float lastAITime;

  //the current match
  private Match match;
  
  //last time the step()-method method was called
  protected float lastStepTime = 0;


  /**
   * Constructor for a team.
   * @param otherTeamColors   if not null, this team uses colors
   *                          that are not confusable with the given ones
   */
  public Team(String id, Match match,
    boolean visitorTeam, PlayerColors otherTeamColors)
  {
    //read team information from XML file
    teamInfo = new TeamInfo(id);
    
    //find colors
    PlayerColors colors;
    if (!visitorTeam)
    {
      //home team: use home colors
      colors = teamInfo.getPlayerColorsHome();
    }
    else
    {
      //use visitor colors
      colors = teamInfo.getPlayerColorsAway();
      //confusable with given colors?
      if (otherTeamColors != null && colors != null &&
        colors.isColorSimilarTo(otherTeamColors))
      {
        colors = teamInfo.getPlayerColorsHome(); //use home colors
        //still confusable with given colors?
        if (otherTeamColors != null && colors != null &&
          colors.isColorSimilarTo(otherTeamColors))
        {
          //try these alternative colors
          boolean found = false;
          for (int i = 0; i < 3; i++)
          {
            colors.setPattern("");
            if (i == 0)
            {
              colors.setColor(PlayerColors.COLOR_SHIRT, ColorTools.getColor("E2E2EC"));
              colors.setColor(PlayerColors.COLOR_PANTS, ColorTools.getColor("27272B"));
            }
            else if (i == 1)
            {
              colors.setColor(PlayerColors.COLOR_SHIRT, ColorTools.getColor("E5C515"));
              colors.setColor(PlayerColors.COLOR_PANTS, ColorTools.getColor("E1DCBF"));
            }
            else if (i == 2)
            {
              colors.setColor(PlayerColors.COLOR_SHIRT, ColorTools.getColor("4C5CD7"));
              colors.setColor(PlayerColors.COLOR_PANTS, ColorTools.getColor("38383D"));
            }
            if (!colors.isColorSimilarTo(otherTeamColors))
            {
              found = true;
              break;
            }
          }
          //still not found? then use inverse colors
          if (!found)
            colors = otherTeamColors.getInverseColors();
        }
      }
    }
    
    //create player texture
    String dest = "data/temp/playercolor-" + this.toString() + ".png";
    if (colors != null)
    {
      if (colors.createTexture("outfieldplayer", true, dest))
      {
        playerTexture = GameEngine.getVideoDriver().getTexture(dest);
        new java.io.File(dest).delete();
      }
    }

    //create cap texture
    dest = "data/temp/playercolorcap-" + this.toString() + ".png";
    if (colors != null)
    {
      if (colors.createTexture("playercap", false, dest))
      {
        textureCap = GameEngine.getVideoDriver().getTexture(dest);
        new java.io.File(dest).delete();
      }
    }

    //create goalkeeper texture
    dest = "data/temp/goalkeepercolor-" + this.toString() + ".png";
    if (teamInfo.getPlayerColorsGoalkeeper().createTexture(
      "goalkeeper", false, dest))
    {
      textureGoalkeeper = GameEngine.getVideoDriver().getTexture(dest);
      new java.io.File(dest).delete();
    }

    //create the <playersCount> players
    //goalkeeper
    players[0] = new Goalkeeper(teamInfo.getPlayerInfo()[0], this,
      PlayerPosition.Goalkeeper,
      GameEngine.getMatchManager().getGoalkeeperMesh(),
      GameEngine.getSceneManager(),
      GameEngine.getMatchManager().getPhysicsManager());
    //outfield players
    for (int i = 1; i < playersCount; i++)
    {
      PlayerPosition playerPosition;
      switch (i)
      {
        case 1: playerPosition = PlayerPosition.DefenderLeft; break;
        case 2: playerPosition = PlayerPosition.DefenderRight; break;
        case 3: playerPosition = PlayerPosition.ForwardLeft; break;
        default: playerPosition = PlayerPosition.ForwardRight; break;
      }
      players[i] = new OutfieldPlayer(teamInfo.getPlayerInfo()[i], this,
        playerPosition,
        GameEngine.getMatchManager().getOutfieldPlayerMesh(),
        GameEngine.getSceneManager(),
        GameEngine.getMatchManager().getPhysicsManager());
    }


    //apply textures to players
    players[0].setTexture(textureGoalkeeper);
    for (int i = 1; i < playersCount; i++)
    {
      ((OutfieldPlayer) players[i]).setTexture(playerTexture, textureCap);
    }


    //current match
    this.match = match;
  }


  /**
   * Computes the actions of the players.
   * Call this method in every frame.
   */
  public void step()
  {
    if (lastStepTime > 0)
    {
      float deltaTime = GameEngine.getFrameTime(true) - lastStepTime;
      //AI - but only every 200 ms
      if (teamAI != null)
      {
        float t = GameEngine.getFrameTime(true);
        if (t - lastAITime > 0.2f)
        {
          lastAITime = t;
          teamAI.step();
        }
        teamAI.frame();
      }
      //user control?
      if (playerController != null)
        playerController.step(match, deltaTime);
      //move players
      for (int i = 0; i < playersCount; i++)
        players[i].step();
      //team in ball possession?
      ballPlayer = null;
      for (int i = 0; i < playersCount; i++)
      {
        if (players[i].getBall() != null)
        {
          ballPlayer = players[i];
        }
      }
    }
    lastStepTime = GameEngine.getFrameTime(true);
  }


  public Player[] getPlayers()
  {
    return players;
  }

  public int getCurrentSide()
  {
    return currentSide;
  }

  
  public void setCurrentSide(int currentSide)
  {
    this.currentSide = currentSide;
    teamAI.update(this, match.getOpponentTeam(this),
      match.getStadium().getField(), match);
  }

  
  /**
   * Gets the player with ball possession, or <code>null</code>
   */
  public Player getBallPlayer()
  {
    return ballPlayer;
  }
  
  
  /**
   * Returns <code>true</code> if the given player is in this
   * team, otherwise <code>false</code>.
   */
  public boolean isPlayerInTeam(Player player)
  {
    for (int i = 0; i < playersCount; i++)
      if (players[i] == player)
        return true;
    return false;
  }

  
  public void setTeamAI(TeamAI teamAI, TeamControl teamControl)
  {
    this.teamControl = teamControl;
    this.teamAI = teamAI;
    teamAI.update(this, match.getOpponentTeam(this),
      match.getStadium().getField(), match);
    
    //create player controller for user control,
    //if needed
    if (teamControl == TeamControl.Player1)
      playerController = new PlayerController(
        GameEngine.getInput(), 0, this);
    else if (teamControl == TeamControl.Player2)
      playerController = new PlayerController(
        GameEngine.getInput(), 1, this);
  }


  /**
   * For user-controlled teams: Select the best player
   * (not that with the best values, but e.g. nearest
   * to the ball).
   */
  public void selectBestPlayer()
  {
    if (teamAI != null)
    {
      teamAI.selectBestPlayer();
    }
  }
  
  
  /**
   * Sets the active player.
   */
  public void setActivePlayer(Player player)
  {
    if (teamAI != null)
    {
      teamAI.setActivePlayer(player);
    }
  }


  /**
   * Team controlled by AI or by human?
   */
  public TeamControl getTeamControl()
  {
    return teamControl;
  }


  /**
   * Gets the currently active player (this one
   * that has the focus).
   */
  public Player getActivePlayer()
  {
    return teamAI.getActivePlayer();
  }


  /**
   * Gets the number of players.
   * Red cards are not regarded. This is the number
   * of players at the beginning of the match.
   */
  public int getPlayersCount()
  {
    return playersCount;
  }
  
  
  /**
   * Sets the players in a row to the given
   * position (each player 2 m in z-axis behind the other).
   * The keeper is the first one, the last one is the right
   * forward player.
   */
  public void setPlayersInZRow(Vector3f pos)
  {
    Vector3f v = new Vector3f(pos);
    for (int i = 0; i < playersCount; i++)
    {
      players[i].setPosition(v);
      v.z += (pos.z > 0 ? -2 : 2);
    }
  }
  
  
  /**
   * Remove all irrlicht objects from the used ISceneManager
   */
  public void removeFromScene()
  {
    for (int i = 0; i < playersCount; i++)
    {
      players[i].removeFromScene();
    }
  }


  /**
   * Gets more information about the team.
   */
  public TeamInfo getTeamInfo()
  {
    return teamInfo;
  }


  /**
   * Gets the user controller class of the team, or
   * <code>null</code> if controlled by AI.
   */
  public PlayerController getPlayerController()
  {
    return playerController;
  }
  
  
  /**
   * Gets the player of this team that is in the
   * given direction relative to the given player,
   * or <code>null</code> if there is none.
   */
  public Player getPlayerInDirection(Player from, Vector3f direction)
  {
    for (int i = playersCount - 1; i > -1; i--) //start with forward players
    {
      Player p = players[i];
      if (p.isPlaying() && p != from) //player can not see himself
      {
        //find out angle difference between player and target
        Vector3f vecFromToTarget = new Vector3f();
        vecFromToTarget.sub(p.getPosition(), from.getPosition());
        if (MathTools.getAngle(
          MathTools.Vector3fToAngle(vecFromToTarget),
          MathTools.Vector3fToAngle(direction)) < 30)
        {
          //when angle is smaller than 30 degrees, this is the target
          return p;
        }
      }
    }
    return null;
  }


  /**
   * Gets the current match of this team,
   * or <code>null</code>.
   */
  public Match getMatch()
  {
    return match;
  }
  
  
  /**
   * Gets a playing (not red-carded) forward player,
   * or, if not available, a defense one or the keeper,
   * but not the given one.
   */
  public Player getRandomForwardPlayer(Player except)
  {
    int i = 3 + MathTools.randomInt(2); //try 3 or 4
    if (players[i].isPlaying() && players[i] != except)
    {
      return players[i];
    }
    else
    {
      for (i = playersCount - 1; i > 0; i--)
      {
        if (players[i].isPlaying() && players[i] != except)
          return players[i];
      }
      return players[0];
    }
  }
  
  
  /**
   * Gets the player with the given index.
   * If he is not playing (e.g. red card), the
   * following player is returned: If alternative
   * is -1, the next playing player with a smaller
   * index is returned, otherwise the next playing
   * player with a higher index is returned.
   * If nobody is found, the goalkeeper is returned.
   */
  public Player getPlayer(int index, int alternativeDir)
  {
    return players[getPlayerIndex(index, alternativeDir)];
  }
  
  
  /**
   * Gets the player with the given index.
   * If he is not playing (e.g. red card), the
   * following player is returned: If alternative
   * is -1, the next playing player with a smaller
   * index is returned, otherwise the next playing
   * player with a higher index is returned.
   * If nobody is found, the goalkeeper is returned.
   */
  public int getPlayerIndex(int index, int alternativeDir)
  {
    if (players[index].isPlaying())
    {
      return index;
    }
    else
    {
      if (alternativeDir > -1)
      {
        //find player with higher index
        for (int i = index + 1; i < playersCount; i++)
        {
          if (players[i].isPlaying())
            return i;
        }
      }
      //find player with lower index
      for (int i = index - 1; i > 0; i--)
      {
        if (players[i].isPlaying())
          return i;
      }
      //return the keeper
      return 0;
    }
  }
  
  
  /**
   * Gets the number of red and yellow-red cards
   * this team has got already.
   */
  public int getRedCardsCount()
  {
    int ret = 0;
    for (int i = 0; i < playersCount; i++)
    {
      if (players[i].getCard() == Card.Red ||
        players[i].getCard() == Card.YellowRed)
      {
        ret++;
      }
    }
    return ret;
  }
  

}
