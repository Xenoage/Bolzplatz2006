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
package com.xenoage.bp2k6.match.referee.decisions;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.Team;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.Referee;
import com.xenoage.bp2k6.match.characters.actions.*;
import com.xenoage.bp2k6.match.characters.enums.Speed;
import com.xenoage.bp2k6.match.referee.Card;
import com.xenoage.bp2k6.match.stadium.Field;


/**
 * Decision for a penalty kick
 * for the given team.
 * 
 * @author Andreas Wenger
 */
public class PenaltyKickDecision
  extends RefereeDecision
{
  private Team team;
  
  
  /**
   * Creates a penalty kick decision for the given
   * team, and shows the given player the given card
   * (or <code>null</code> if no card should be shown).
   */
  public PenaltyKickDecision(Referee referee,
    Team penaltyKickTeam,
    Card card, Player cardPlayer)
  {
    super(referee, card, cardPlayer);
    this.team = penaltyKickTeam;
  }
  
  
  /**
   * Gets the active penalty kick team.
   */
  public Team getTeam()
  {
    return team;
  }


  /**
   * Lets the referee run to the penalty point.
   * There he shows to the goal. Then he runs back
   * to let the victim's team perform the penalty kick.
   */
  @Override public void applyActionSet(Match match)
  {
    ArrayList<Action> actions = new ArrayList<Action>();
    Field field = match.getStadium().getField();
    Vector3f penaltySpot = new Vector3f(-team.getCurrentSide() *
      (field.getLength() / 2 - field.getPenaltySpotDistance()), 0, 0);
    //run to penalty point
    actions.add(new RunToAction(referee,
      penaltySpot, Speed.Run));
    //here the referee should show to the goal and show a card...
    actions.add(new WaitAction(referee, 2));
    //run a few meters away
    Vector3f refereePos = new Vector3f(penaltySpot);
    refereePos.z = 5;
    actions.add(new RunToAction(referee, refereePos, Speed.Walk));
    //apply action set
    referee.setActionSet(
      new GeneralActionSet(actions, ActionSet.PRIORITY_NORMAL));
  }
  
}
