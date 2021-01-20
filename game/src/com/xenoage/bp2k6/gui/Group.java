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

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.input.*;
import com.xenoage.bp2k6.util.*;
import java.util.ArrayList;
import net.sf.jirr.*;




/**
 * A GUI group is a list of controls that
 * belong together.
 * Examples for groups: Buttons for replay,
 * Buttons for halftime actions, ...
 * A screen can have multiple groups, only
 * one group can be active at one time.
 *
 * @author   Andreas Wenger
 */
public class Group
  implements Container
{
  //id of this group
  private String id;
  //parent screen
  private Screen parentScreen;
  
  //list of controls
  private ArrayList<Control> controls = new ArrayList<Control>();
  //active control which has the focus
  private Control controlFocus = null;
  
  //current status of this group
  public enum GroupStatus {
    Opening, //flyin
    Open,    //shown
    Closing, //flyout
    Closed   //hidden
  };
  private GroupStatus status = GroupStatus.Closed;
  private float statusChangedTime;
  
  //duration of opening (flyin) phase
  private float durationFlyIn = 0;
  //duration of closing (flyout) phase
  private float durationFlyOut = 0;
  //duration of the interval between closing-time of this
  //group and opening-time of the next group (this is not
  //always equal to durationFlyOut, because we want to
  //"crossfade" two groups, that means, the old group can
  //still be seen in parts while the next group starts
  //to fly in).
  private float durationNextGroupWait = 0;
  //the time offset before the flyin can really start after
  //setting the status to opening (this is the value of
  //durationNextGroupWait of the last active group)
  private float durationThisGroupWait = 0;
  
  //actions when the group is opened (not when it is loaded)
  protected ArrayList<Action> actionsGroupOpen = new ArrayList<Action>();
  //group-time dependent actions
  protected ArrayList<Action> actionsGroupTime = new ArrayList<Action>();
  //actions when certain keys are pressed
  protected ArrayList<Action> actionsKeypress = new ArrayList<Action>();
  
  //the ActionSets (groups of actions) for this group
  private ArrayList<ActionSet> actionSets = new ArrayList<ActionSet>();
  
  
  /**
   * Constructor.
   */
  public Group(String id, Screen parentScreen)
  {
    this.id = id;
    this.parentScreen = parentScreen;
  }
  
  
  /**
   * Adds a {@link Control} to this group. It is added to the
   * end of the list, that means it can mask other controls.
   * @param control       the new {@link Control} to add
   * @param parentGroup   not used here
   * @param parentScreen  not used here
   */
  public void addControl(Control control,
    Group ParentGroup, Screen parentScreen)
  {
    if (controls.indexOf(control) == -1)
    {
      controls.add(control);
      control.setParentGroup(this);
      control.setParentScreen(this.parentScreen);
    }
  }
  
  
  /**
   * @param id   the ID of the <code>Control</code>
   * @return     the <code>Control</code> in this group with the
   *             specified ID, or <code>null</code> if not found.
   */
  public Control findControl(String id)
  {
    for (int i = 0; i < controls.size(); i++)
    {
      if (controls.get(i).getID().equals(id))
        return controls.get(i);
      else if (controls.get(i) instanceof Panel)
      {
        Container container = (Container) controls.get(i);
        Control c = container.findControl(id);
        if (c != null)
          return c;
      }
    }
    return null;
  }
  
  
  /**
   * Draws this group.
   */
  public void draw(IVideoDriver driver)
  {
    
    //status change?
    if (status == GroupStatus.Opening &&
      GameEngine.getFrameTime(false) >
        statusChangedTime + durationFlyIn + durationThisGroupWait)
    {
      //group is now open. set focus to default control.
      status = GroupStatus.Open;
    }
    else if (status == GroupStatus.Closing &&
      GameEngine.getFrameTime(false) > statusChangedTime + durationFlyOut)
    {
      //group is now closed.
      status = GroupStatus.Closed;
    }
    
    //group is open/opening?
    if (status == GroupStatus.Opening || status == GroupStatus.Open)
    {
      float timeGroupActive = GameEngine.getFrameTime(false) - statusChangedTime;
      //find screen-time dependent actions and perform them if the time has come
      for (int i = 0; i < actionsGroupTime.size(); i++)
      {
        Action a = actionsGroupTime.get(i);
        if (!a.isAlreadyPerformed())
        {
          if (Float.valueOf(a.eventparam) < timeGroupActive)
            a.perform(this, "grouptime");
        }
      }
    }
    
    
    //don't draw closed groups
    if (status != GroupStatus.Closed)
    {
      //draw controls
      for (int i = 0; i < controls.size(); i++)
      {
        controls.get(i).draw(driver, new Point2i());
      }
    }
    
  }
  
  
  /**
   * Raise loadEvent in all controls.
   */
  public void loadEvent()
  {
    for (int i = 0; i < controls.size(); i++)
      controls.get(i).loadEvent();
  }
  
  
  /**
   * Input event for this group.
   * @param key   A constant from the <code>Input</code> class.
   */
  public void input(KeyboardEvent kbEvent)
  {
    
    //no input during the first 0.5 seconds of fly-in!
    if (status == GroupStatus.Opening &&
      GameEngine.getFrameTime(false) - statusChangedTime < 0.5f)
      return;
    
    //keypress-dependent action?
    int guiKey = kbEvent.getGUIKey();
    boolean doReturn = false;
    for (int i = 0; i < actionsKeypress.size(); i++)
    {
      Action a = actionsKeypress.get(i);
      if (guiKey == Input.confirm && a.eventparam.equals("confirm"))
      {
        a.perform(this, "keypress");
        doReturn = true;
      }
      if (guiKey == Input.cancel && a.eventparam.equals("cancel"))
      {
        a.perform(this, "cancel");
        doReturn = true;
      }
    }
    if (doReturn)
      return;
    
    //screen and controls
    if (guiKey == Input.cancel)
    {
      //do nothing (is controlled by keypress events)
    }
    //special case: arrow-left and arrow-right with option button
    else if ((controlFocus instanceof OptionButton) &&
      (guiKey == Input.left || guiKey == Input.right))
    {
      OptionButton c = (OptionButton) controlFocus;
      c.switchOption(guiKey == Input.left ? -1 : 1);
    }
    else if ((controlFocus instanceof KeyChangeButton) &&
      ((KeyChangeButton)controlFocus).isAwaitingKey())
    {
      //key change button waits for user input
      ((KeyChangeButton)controlFocus).input(kbEvent);
    }
    else if (Input.isArrowKey(guiKey))
    {
      //todo: constants
      int keyCode = 0;
      if (guiKey == Input.up) keyCode = 0;
      else if (guiKey == Input.right) keyCode = 1;
      else if (guiKey == Input.down) keyCode = 2;
      else if (guiKey == Input.left) keyCode = 3;
      Control nextControl = null;
      if (controlFocus != null)
        nextControl = controlFocus.getNextControl(keyCode);
      else
      {
        //find the first control that may have the focus
        for (int i = 0; i < controls.size(); i++)
        {
          if (controls.get(i).getEnabled())
          {
            nextControl = controls.get(i);
            break;
          }
        }
      }
      if (nextControl != null)
        setFocus(nextControl);
    }
    else if (guiKey == Input.confirm)
    {
      if (controlFocus instanceof Button)
      {
        Button b = (Button) controlFocus;
        b.click();
      }
      else if (controlFocus instanceof KeyChangeButton)
      {
        KeyChangeButton b = (KeyChangeButton) controlFocus;
        b.click();
      }
      else if (controlFocus instanceof OptionButton)
      {
        OptionButton c = (OptionButton) controlFocus;
        c.click();
      }
      else if (controlFocus instanceof ImageSwitch)
      {
        ImageSwitch c = (ImageSwitch) controlFocus;
        c.switchOption(1);
      }

    }
  }
  
  
  /**
   * Sends a mouse event to the Screen
   */
  public void input(MouseEvent event)
  {
    if (event.type == MouseEvent.EVENT_MOVED)
    {
      //mouse was moved
      //focus the control on the mouse position
      Control c = getControlAt(event.x, event.y);
      if (c != null && c.getEnabled() && controlFocus != c)
      {
        setFocus(c);
      }
    }
    else if (event.type == MouseEvent.EVENT_DOWNLEFT)
    {
      //left mouse button was pressed
      Control c = getControlAt(event.x, event.y);
      if (c != null && c.getEnabled())
      {
        if (c instanceof Button)
        {
          //button: click
          ((Button) c).click();
        }
        else if (c instanceof KeyChangeButton)
        {
          ((KeyChangeButton) c).click();
        }
        else if (c instanceof OptionButton)
        {
          //optionbutton: switch. if there is only one
          //option, send a "click" instead.
          OptionButton ob = (OptionButton) c;
          if (ob.getOptionsCaption().length > 1)
            ob.switchOption(1);
          else
            ob.click();
        }
      }
    }
    else if (event.type == MouseEvent.EVENT_DOWNRIGHT)
    {
      //right mouse button was pressed
      Control c = getControlAt(event.x, event.y);
      if (c != null && c.getEnabled())
      {
        if (c instanceof OptionButton)
        {
          //optionbutton: switch
          OptionButton ob = (OptionButton) c;
          if (ob.getOptionsCaption().length > 1)
            ob.switchOption(-1);
        }
      }
    }
  }
  
  
  /**
   * Sets the focus to the specified {@link Control},
   * if it belongs to this group.
   * @param id   the id of {@link Control} that
   *             gets the focus, or <code>null</code>
   */
  public void setFocus(String id)
  {
    Control c = findControl(id);
    if (c != null)
      setFocus(c);
    else
      Logging.log(Logging.LEVEL_WARNINGS, this,
        "Can not find control \"" + id + "\" in the active group!");
  }
  
  
  /**
   * Sets the focus to the specified {@link Control},
   * if it belongs to this group.
   * @param control  the {@link Control} that gets the focus, or <code>null</code>
   */
  public void setFocus(Control control)
  {
    controlFocus = control;
    ArrayList<Action> a = controlFocus.getActionsFocus();
    for (int i = 0; i < a.size(); i++)
      a.get(i).perform(controlFocus, "focus");
  }
  
  
  /**
   * Gets the control, that has the focus, or <code>null</code>
   */
  public Control getFocusControl()
  {
    return controlFocus;
  }
  
  
  /**
   * Gets the ID of this group.
   */
  public String getID()
  {
    return this.id;
  }


  /**
   * Returns the status of this group.
   */
  public GroupStatus getStatus()
  {
    return status;
  }


  /**
   * Returns the time, when the status was changed
   * the last time.
   */
  public float getStatusChangedTime()
  {
    return statusChangedTime;
  }
  
  
  /**
   * Activates this group.
   */
  public void show()
  {
    status = GroupStatus.Opening;
    statusChangedTime = GameEngine.getTime(false);
    //raise group open events
    for (int i = 0; i < actionsGroupOpen.size(); i++)
      actionsGroupOpen.get(i).perform(this, "groupopen");
    //reset group time events
    for (int i = 0; i < actionsGroupTime.size(); i++)
      actionsGroupTime.get(i).alreadyPerformed = false;
  }
  
  
  /**
   * Deactivates this group.
   */
  public void hide()
  {
    status = GroupStatus.Closing;
    statusChangedTime = GameEngine.getFrameTime(false);
    //reset all group time events
    for (int i = 0; i < actionsGroupTime.size(); i++)
      actionsGroupTime.get(i).alreadyPerformed = false;
  }

  
  /**
   * Gets the duration of opening (flyin) phase.
   */
  public float getDurationFlyIn()
  {
    return durationFlyIn;
  }

  
  /**
   * Sets the duration of the opening (flyin) phase.
   */
  public void setDurationFlyIn(float durationFlyIn)
  {
    this.durationFlyIn = durationFlyIn;
  }

  
  /**
   * Gets the duration of the closing (flyout) phase.
   */
  public float getDurationFlyOut()
  {
    return durationFlyOut;
  }

  
  /**
   * Sets the duration of the closing (flyout) phase.
   */
  public void setDurationFlyOut(float durationFlyOut)
  {
    this.durationFlyOut = durationFlyOut;
  }

  
  /**
   * Gets the duration of the interval between closing-time of this
   * group and opening-time of the next group (this is not
   * always equal to durationFlyOut, because we want to
   * "crossfade" two groups, that means, the old group can
   * still be seen in parts while the next group starts
   * to fly in).
   */
  public float getDurationNextGroupWait()
  {
    return durationNextGroupWait;
  }

  
  /**
   * Sets the duration of the interval between closing-time of this
   * group and opening-time of the next group (this is not
   * always equal to durationFlyOut, because we want to
   * "crossfade" two groups, that means, the old group can
   * still be seen in parts while the next group starts
   * to fly in).
   */
  public void setDurationNextGroupWait(float durationNextGroupWait)
  {
    this.durationNextGroupWait = durationNextGroupWait;
  }

  
  /**
   * Gets the time offset before the flyin can really start after
   * setting the status to opening (this is the value of
   * durationNextGroupWait)
   */
  public float getDurationThisGroupWait()
  {
    return durationThisGroupWait;
  }
  
  
  /**
   * Sets the time offset before the flyin can really start after
   * setting the status to opening (this is the value of
   * durationNextGroupWait)
   */
  public void setDurationThisGroupWait(float durationThisGroupWait)
  {
    this.durationThisGroupWait = durationThisGroupWait;
  }
  
  
  /**
   * Gets the list of actions that are performed
   * when the group is opened (not when it is loaded).
   */
  public ArrayList<Action> getActionsGroupOpen()
  {
    return this.actionsGroupOpen;
  }


  /**
   * Adds a action that is performed
   * when the group is opened (not when it is loaded).
   */
  public void addActionGroupOpen(Action action)
  {
    this.actionsGroupOpen.add(action);
  }
  
  
  /**
   * Gets the list of group-time dependent actions.
   */
  public ArrayList<Action> getActionsGroupTime()
  {
    return this.actionsGroupTime;
  }


  /**
   * Adds a group-time dependent action.
   */
  public void addActionGroupTime(Action action)
  {
    this.actionsGroupTime.add(action);
  }
  
  
  /**
   * Gets the list of actions that are performed
   * when certain keys are pressed.
   */
  public ArrayList<Action> getActionsKeypress()
  {
    return this.actionsKeypress;
  }


  /**
   * Adds a action that is performed
   * when a certain key is pressed.
   */
  public void addActionKeypress(Action action)
  {
    this.actionsKeypress.add(action);
  }


  
  /**
   * Gets the parent screen of this group.
   */
  public Screen getParentScreen()
  {
    return parentScreen;
  }
  
  
  /**
   * Gets the control at the given position,
   * or <code>null</code> if there is none.
   */
  private Control getControlAt(int x, int y)
  {
    float scaleX = parentScreen.getUIManager().getScaleX();
    float scaleY = parentScreen.getUIManager().getScaleY();
    for (int i = 0; i < controls.size(); i++)
    {
      Control c = controls.get(i);
      Point2i p = c.computeCurrentPosition();
      if (p.x * scaleX <= x && p.y * scaleY <= y &&
        (p.x + c.getSize().x) * scaleX >= x &&
        (p.y + c.getSize().y) * scaleY >= y)
      {
        if (c.isVisible() && c.getEnabled())
          return c;
      }
    }
    return null;
  }
  
  
  /**
   * Adds a {@link ActionSet} to this group.
   */
  public void addActionSet(ActionSet actionSet)
  {
    actionSets.add(actionSet);
  }
  
  
  /**
   * Returns the {@link ActionSet} with the given ID,
   * or <code>null</code> if not found,
   */
  public ActionSet findActionSet(String id)
  {
    for (int i = 0; i < actionSets.size(); i++)
    {
      if (actionSets.get(i).getID().equals(id))
        return actionSets.get(i);
    }
    return null;
  }


  /**
   * Gets the list of controls of this group.
   */
  public ArrayList<Control> getControls()
  {
    return controls;
  }
  
}
