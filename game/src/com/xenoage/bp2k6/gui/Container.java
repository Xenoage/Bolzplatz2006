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


/**
 * Interface for GUI containers
 * (Screen and Panel). Containers can
 * have child controls or containers.
 *
 * Java can not handle 2 base classes
 * at one time. We have to define Container
 * as an interface.
 *
 * @author Andi
 */
public interface Container
{

  /**
   * Adds a {@link Control} to this <code>Container</code>.
   * It is added to the
   * end of the list, that means it can mask other controls.
   * @param control   the new {@link Control} to add
   */
  public void addControl(Control control,
    Group parentGroup, Screen parentScreen);

  /**
   * @param id   the ID of the <code>Control</code>
   * @return     the <code>Control</code> on this panel with the
   *             specified ID, or <code>null</code> if not found.
   */
  public Control findControl(String id);

}
