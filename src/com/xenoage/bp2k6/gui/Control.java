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
import com.xenoage.bp2k6.util.Point2i;
import net.sf.jirr.*;
import java.util.ArrayList;

/**
 * Abstract base class for GUI controls.
 *
 * @author   Andreas Wenger
 */
public abstract class Control
{

  //id of the control
  protected String id;
  //parent screen of the control
  protected Screen parentScreen = null;
  //parent group of the control
  protected Group parentGroup = null;
  //current position (x, y) of the control
  protected Point2i position;
  //protected recti nativeRect = new recti();
  //width and height of the control
  protected Point2i size;
  //the direction and speed (in screen-heights/second) of the control, or null
  protected vector2df velocity = null;
  //start position, target position and end position
  //of the control (used for flying-in/-out controls)
  protected Point2i positionFlyIn = null;
  protected Point2i positionNormal = null;
  protected Point2i positionFlyOut= null;
  //start time and flying duration for a flying-in control
  protected float timeStartFlyIn = 0;
  protected float durationFlyIn = 0;
  //start time and flying duration for a flying-out control
  protected float timeStartFlyOut = 0;
  protected float durationFlyOut = 0;
  //is it possible for the user to interact with the control
  //and can it get the focus?
  protected boolean enabled = false;
  //is this control visible? (if not, it is automatically not enabled)
  protected boolean visible = true;
  //next controls that are activated when the user presses the arrow keys
  protected String nextControls[] = new String[4];

  //actions when the control is loaded
  protected ArrayList<Action> actionsLoad = new ArrayList<Action>();
  //actions when user clicks the control
  protected ArrayList<Action> actionsClick = new ArrayList<Action>();
  //actions when control gets the focus
  protected ArrayList<Action> actionsFocus = new ArrayList<Action>();



  /**
   * @return  the ID of this control
   */
  public String getID()
  {
    return this.id;
  }

  /**
   * @param id    the ID of this control
   */
  public void setID(String id)
  {
    this.id = id;
  }


  /**
   * Gets the parent screen of this control.
   */
  public Screen getParentScreen()
  {
    return this.parentScreen;
  }

  
  /**
   * Sets the parent screen of this control.
   */
  public void setParentScreen(Screen screen)
  {
    this.parentScreen = screen;
  }
  
  
  /**
   * Gets the parent group of this control.
   */
  public Group getParentGroup()
  {
    return this.parentGroup;
  }

  
  /**
   * Sets the parent group of this control.
   */
  public void setParentGroup(Group group)
  {
    this.parentGroup = group;
  }

  
  /**
   * Get position and size of this control.
   * @return  the rectangle (x, y) of the control
   */
  public Point2i getPosition()
  {
    return this.position;
  }

  /**
   * Change the position position this control. The visible area is between
   * (0, 0) and (1024, 768). If the user has selected another resolution,
   * the coordinates are scaled.
   * @param pos   the new position (x, y)
   */
  public void setPosition(Point2i pos)
  {
    this.position = pos;
    positionNormal = new Point2i(pos);
    if (positionFlyIn == null)
      positionFlyIn = new Point2i(pos);
    if (positionFlyOut == null)
      positionFlyOut = new Point2i(pos);
  }

  /**
    * Get the size of this control.
    * @return  the size (width, height) of the control
    */
   public Point2i getSize()
   {
     return this.size;
   }

   /**
    * Change the size of this control. The visible area is between
    * (0, 0) and (1024, 768). If the user has selected another resolution,
    * the coordinates are scaled.
    * @param pos   the new size (width, height)
    */
   public void setSize(Point2i size)
   {
     this.size = size;
   }


  /**
   * Gets the direction and speed (in screen-heights/second) of the control
   * @return  the velocity vector
   */
  public vector2df getVelocity()
  {
    return this.velocity;
  }

  /**
   * Change the direction and speed (in screen-heights/second) of the control
   * @param vel   the new velocity vector
   */
  public void setVelocity(vector2df vel)
  {
    this.velocity = vel;
  }

  
  /**
   * Gets the start position of the control (used for flying-in controls)
   */
  public Point2i getPositionFlyIn()
  {
    return positionFlyIn;
  }

  
  /**
   * Sets the start position of the control (used for flying-in controls)
   */
  public void setPositionFlyIn(Point2i positionFlyIn)
  {
    this.positionFlyIn = positionFlyIn;
  }

  
  /**
   * Gets the start time for a flying-in control
   */
  public float getTimeStartFlyIn()
  {
    return timeStartFlyIn;
  }

  
  /**
   *Sets the start time for a flying-in control
   */
  public void setTimeStartFlyIn(float timeStartFlyIn)
  {
    this.timeStartFlyIn = timeStartFlyIn;
  }

  
  /**
   * Gets the flying duration for a flying-in control
   */
  public float getDurationFlyIn()
  {
    return durationFlyIn;
  }

  
  /**
   * Sets the flying duration for a flying-in control
   */
  public void setDurationFlyIn(float durationFlyIn)
  {
    this.durationFlyIn = durationFlyIn;
  }
  
  
  /**
   * Gets the end position of the control (used for flying-out controls)
   */
  public Point2i getPositionFlyOut()
  {
    return positionFlyOut;
  }

  
  /**
   * Sets the end position of the control (used for flying-out controls)
   */
  public void setPositionFlyOut(Point2i positionFlyOut)
  {
    this.positionFlyOut = positionFlyOut;
  }

  
  /**
   * Gets the start time for a flying-out control
   */
  public float getTimeStartFlyOut()
  {
    return timeStartFlyOut;
  }

  
  /**
   * Sets the start time for a flying-out control
   */
  public void setTimeStartFlyOut(float timeStartFlyOut)
  {
    this.timeStartFlyOut = timeStartFlyOut;
  }

  
  /**
   * Gets the flying duration for a flying-out control
   */
  public float getDurationFlyOut()
  {
    return durationFlyOut;
  }

  
  /**
   * Sets the flying duration for a flying-out control
   */
  public void setDurationFlyOut(float durationFlyOut)
  {
    this.durationFlyOut = durationFlyOut;
  }
  

  /**
   * @return  <code>true</code>, if the user can interact with the control
   */
  public boolean getEnabled()
  {
    return this.enabled;
  }

  
  /**
   * @param enabled   set this to <code>true</code> if the user is allowed
   *                  to interact with the control.
   */
  public void setEnabled(boolean enabled)
  {
    this.enabled = enabled;
  }

  
  /**
   * Get the <code>Control</code> that follows if the user presses
   * an arrow keys.
   * @param dir       Input.up, Input.down, Input.right or Input.left
   * @return          <code>Control</code> that follows if <code>dir</code>
   *                  was pressed, or <code>null</code>
   */
  public Control getNextControl(int dir)
  {
    try
    {
      Control ret = parentScreen.findControl(nextControls[dir]);
      if (ret == null)
      {
        //if not defined for left, try with up
        if (dir == 3)
          ret = parentScreen.findControl(nextControls[0]);
        //if not defined for right, try with down
        else if (dir == 1)
          ret = parentScreen.findControl(nextControls[2]);
      }
      //if control is not visible or not enabled, switch
      //to the next control
      if (ret != null && (!ret.isVisible() || !ret.getEnabled()))
        ret = ret.getNextControl(dir);
      return ret;
    }
    catch (IndexOutOfBoundsException ex)
    {
      return null;
    }
  }

  
  /**
   * Set the <code>Control</code> that follows if the user presses a
   * certain combination of the arrow keys.
   * @param dir       0 = up, 1 = up/right, 2 = right, 3 = down/right,
   *                  4 = down, 5 = down/left, 6 = left, 7 = top/left
   * @param control   id of the <code>Control</code> that follows if <code>dir</code>
   *                  was pressed, or <code>null</code>
   */
  public void setNextControl(int dir, String control)
  {
    try
    {
      nextControls[dir] = control;
    }
    catch (IndexOutOfBoundsException ex) { }
  }
  

  /**
   * Draws the <code>Control</code>. This method should only be called
   * from the <code>draw()</code>-Method in {@link Screen}.
   * Every subclass of <code>Control</code> must overwrite this method.
   * @param driver   active <code>IVideoDriver</code> from the bindenlicht engine
   * @param offset   offset added to the position of the control
   */
  abstract void draw(IVideoDriver driver, Point2i offset);


  /**
   * Computes the current position of the control,
   * dependent on the status of the parent group
   * of the control.
   */
  public Point2i computeCurrentPosition()
  {
    //the current status of the group
    Group.GroupStatus groupStatus = parentGroup.getStatus();
    //the time difference between now and the status changed time
    float groupStatusTime = GameEngine.getFrameTime(false) -
      parentGroup.getStatusChangedTime();
    if (groupStatus == Group.GroupStatus.Open)
    {
      //group is open (shown and active)
      return new Point2i(positionNormal);
    }
    else if (groupStatus == Group.GroupStatus.Closed)
    {
      //group is closed (hidden)
      return new Point2i(positionFlyOut);
    }
    else if (groupStatus == Group.GroupStatus.Opening)
    {
      //group is opening (fly-in)
      if (durationFlyIn > 0)
      {
        float groupWait = parentGroup.getDurationThisGroupWait();
        if (groupStatusTime < timeStartFlyIn + durationFlyIn + groupWait)
        {
          if (groupStatusTime < timeStartFlyIn + groupWait)
            return new Point2i(positionFlyIn);
          else
          {
            float progress =
              (groupStatusTime - timeStartFlyIn - groupWait) / durationFlyIn;
            return new Point2i(
              (int) (positionFlyIn.x + (positionNormal.x - positionFlyIn.x) * progress),
              (int) (positionFlyIn.y + (positionNormal.y - positionFlyIn.y) * progress));
          }
        }
      }
    }
    else if (groupStatus == Group.GroupStatus.Closing)
    {
      //group is closing (fly-out)
      if (durationFlyOut > 0)
      {
        if (groupStatusTime < timeStartFlyOut + durationFlyOut)
        {
          if (groupStatusTime < timeStartFlyOut)
            return new Point2i(positionNormal);
          else
          {
            float progress = (groupStatusTime - timeStartFlyOut) / durationFlyOut;
            return new Point2i(
              (int) (positionNormal.x + (positionFlyOut.x - positionNormal.x) * progress),
              (int) (positionNormal.y + (positionFlyOut.y - positionNormal.y) * progress));
          }
        }
      }
      return new Point2i(positionFlyOut);
    }
    //no special situation: return normal position
    return new Point2i(positionNormal);
  }

  
  /**
   * Gets the value of the control. Makes only sense
   * with optionbuttons, textboxes, ...
   */
  public String getValue()
  {
    return "";
  }


  /**
   * Gets the list of actions connected with the loading of this control.
   */
  public ArrayList<Action> getActionsLoad()
  {
    return this.actionsLoad;
  }


  /**
   * Adds a action connected with the loading of this control.
   */
  public void addActionLoad(Action action)
  {
    this.actionsLoad.add(action);
  }


  /**
   * Gets the list of actions connected with a click on this control.
   */
  public ArrayList<Action> getActionsClick()
  {
    return this.actionsClick;
  }


  /**
   * Adds a action connected with a click on this control.
   */
  public void addActionClick(Action action)
  {
    this.actionsClick.add(action);
  }


  /**
   * Gets the list of actions connected with a focus event on this control.
   */
  public ArrayList<Action> getActionsFocus()
  {
    return this.actionsFocus;
  }


  /**
   * Adds a action connected with a focus event on this control.
   */
  public void addActionFocus(Action action)
  {
    this.actionsFocus.add(action);
  }


  /**
   * Call this method when screen begins to open.
   */
  public void loadEvent()
  {
    for (int i = 0; i < actionsLoad.size(); i++)
      actionsLoad.get(i).perform(this, "load");
  }


  /**
   * Call this method when the control was clicked.
   */
  public void click()
  {
    for (int i = 0; i < actionsClick.size(); i++)
      actionsClick.get(i).perform(this, "click");
  }

  
  /**
   * Is this control visible?
   */
  public boolean isVisible()
  {
    return visible;
  }

  
  /**
   * Sets this control visible or hidden.
   * If it is hidden, its enabled-value is also
   * changed to <code>false</code>.
   */
  public void setVisible(boolean visible)
  {
    this.visible = visible;
    if (!this.visible)
      this.enabled = false;
  }


}
