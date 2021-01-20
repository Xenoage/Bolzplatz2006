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
import com.xenoage.bp2k6.match.referee.Card;


/**
 * Decision for a free kick
 * for the given team.
 * 
 * @author Andreas Wenger
 */
public class FreeKickDecision
  extends RefereeDecision
{
  private Team team;
  private Vector3f position;
  
  
  /**
   * Creates a free kick decision for the given
   * team at the given position, and shows the given
   * player the given card
   * (or <code>null</code> if no card should be shown).
   */
  public FreeKickDecision(Referee referee,
    Team team, Vector3f position, Card card, Player cardPlayer)
  {
    super(referee, card, cardPlayer);
    this.team = team;
    this.position = new Vector3f(position);
  }
  
  
  /**
   * Gets the active free kick team.
   */
  public Team getTeam()
  {
    return team;
  }
  
  
  /**
   * Gets the position of this free kick.
   */
  public Vector3f getPosition()
  {
    return position;
  }


  /**
   * Lets the referee point into the direction
   * of the free kick (to the goal of the victim's
   * opponent team).
   */
  @Override public void applyActionSet(Match match)
  {
    ArrayList<Action> actions = new ArrayList<Action>();
    //here the referee should show to the goal and show a card...
    actions.add(new WaitAction(referee, 2));
    //apply action set
    referee.setActionSet(
      new GeneralActionSet(actions, ActionSet.PRIORITY_NORMAL));
  }
  
}
