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
package com.xenoage.bp2k6.util.math;

import javax.vecmath.Vector3f;

public class Plane
{
  public float a; //normalized normal vector (x)
  public float b; //normalized normal vector (y)
  public float c; //normalized normal vector (z)
  public float d; //signed distance to origin

  public Plane()
  {
  }

  public Plane(Plane template)
  {
    a = template.a;
    b = template.b;
    c = template.c;
    d = template.d;
  }

  public Vector3f getNormal()
  {
    return new Vector3f(a, b, c);
  }
}
