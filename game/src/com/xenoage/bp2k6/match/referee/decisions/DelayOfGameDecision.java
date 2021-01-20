package com.xenoage.bp2k6.match.referee.decisions;

import java.util.ArrayList;

import com.xenoage.bp2k6.match.Match;
import com.xenoage.bp2k6.match.characters.Player;
import com.xenoage.bp2k6.match.characters.Referee;
import com.xenoage.bp2k6.match.characters.actions.Action;
import com.xenoage.bp2k6.match.characters.actions.ActionSet;
import com.xenoage.bp2k6.match.characters.actions.GeneralActionSet;
import com.xenoage.bp2k6.match.characters.actions.WaitAction;
import com.xenoage.bp2k6.match.referee.Card;


/**
 * This class is needed, if a player is
 * disturbing a freekick or goalkick.
 * 
 * This class belongs to the patch LOM.
 * 
 * @author Malte Löpmann
 */
public class DelayOfGameDecision
	extends RefereeDecision
{
  
	/**
   * Creates a DelayOfGameDecision
   */
  public DelayOfGameDecision(Referee referee,
    Card card, Player cardPlayer)
  {
    super(referee, card, cardPlayer);    
  }
  
  
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
