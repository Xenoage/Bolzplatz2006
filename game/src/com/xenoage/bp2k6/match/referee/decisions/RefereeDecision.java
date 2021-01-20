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

import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.Referee;
import com.xenoage.bp2k6.match.referee.Card;


/**
 * Abstract base class for a referee decision.
 * May be for example a penalty kick or
 * a free kick. Any decision can also have
 * a card (yellow, red) for a given player.
 * 
 * @author Andreas Wenger
 */
public abstract class RefereeDecision
{
  protected Referee referee;
  protected Card card = null;
  protected Player cardPlayer = null;
  
  
  /**
   * Creates a referee decision with the given
   * card and player, that gets the card.
   */
  public RefereeDecision(Referee referee, Card card, Player cardPlayer)
  {
    this.referee = referee;
    this.card = card;
    this.cardPlayer = cardPlayer;
  }
  
  
  /**
   * Creates and applies action sets for the
   * referee and perhaps some players of both teams,
   * that show this referee decision.
   */
  public abstract void applyActionSet(Match match);


  
  public Card getCard()
  {
    return card;
  }


  
  public Player getCardPlayer()
  {
    return cardPlayer;
  }
 
  
}
