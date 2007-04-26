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
package com.xenoage.bp2k6.match.teamai;

import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.*;
import com.xenoage.bp2k6.match.stadium.Field;

public interface TeamAI
{
  public void update(Team ownTeam, Team opponentTeam,
    Field field, Match match);

  //called every 200 ms
  public void step();

  //called every mainloop frame
  //do not use it for making decisions.
  public void frame();

  //select best player (the player that it
  //in the best position)
  //use this method only for user-controlled AIs!
  public void selectBestPlayer();

  //get the active player
  public Player getActivePlayer();
  
  //Change the active player.
  public void setActivePlayer(Player player);

}
