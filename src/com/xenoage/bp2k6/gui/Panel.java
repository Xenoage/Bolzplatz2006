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

import com.xenoage.bp2k6.util.Point2i;
import java.util.ArrayList;
import net.sf.jirr.IVideoDriver;


/**
 * Class for a GUI panel.
 * A panel is a control that has child controls. It allows
 * grouping of controls, so they can easily moved together.
 *
 * @author   Andi
 */
public class Panel
  extends Control
  implements Container
{

  //list of controls
  private ArrayList<Control> controls = new ArrayList<Control>();


  /**
   * Draws the <code>Panel</code>.
   * @see <code>Control.draw</code>
   */
  @Override void draw(IVideoDriver driver, Point2i offset)
  {
    if (!this.visible)
      return;
    
    Point2i drawPos = new Point2i(position);

    //compute current position
    drawPos = computeCurrentPosition();

    drawPos.add(offset);

    for (int i = 0; i < controls.size(); i++)
    {
      controls.get(i).draw(driver, drawPos);
    }
  }


  /**
   * Adds a {@link Control} to this <code>Panel</code>.
   * It is added to the
   * end of the list, that means it can mask other controls.
   * @param control   the new {@link Control} to add
   */
  public void addControl(Control control, Group parentGroup, Screen parentScreen)
  {
    if (controls.indexOf(control) == -1)
    {
      controls.add(control);
      control.setParentGroup(parentGroup);
      control.setParentScreen(parentScreen);
    }
  }

  /**
   * @param id   the ID of the <code>Control</code>
   * @return     the <code>Control</code> on this panel with the
   *             specified ID, or <code>null</code> if not found.
   */
  public Control findControl(String id)
  {
    for (int i = 0; i < controls.size(); i++)
    {
      if (controls.get(i).getID().equals(id))
        return controls.get(i);
      else if (controls.get(i) instanceof Container)
      {
        Container container = (Container) controls.get(i);
        Control c = container.findControl(id);
        if (c != null)
          return c;
      }
    }
    return null;
  }



}
