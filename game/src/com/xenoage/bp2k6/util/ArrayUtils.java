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
package com.xenoage.bp2k6.util;

import java.util.ArrayList;


/**
 * Some util methods for manipulating and reading
 * arrays and ArrayList objects.
 * 
 * @author Andreas Wenger
 */
public class ArrayUtils
{

  
  /**
   * Sorts the given String array alphabetically.
   */
  public static void sortStringArrayAlphabetically(String[] arr)
  {
    String temp;
    for(int i = 0; i < arr.length - 1; i++)
    {
      for (int i2 = 0; i2 < arr.length - i - 1; i2++)
      {
        if(arr[i2].compareTo(arr[i2 + 1]) > 0)
        {
           temp = arr[i2];
           arr[i2] = arr[i2 + 1];
           arr[i2 + 1] = temp;
        }
      }
    }
  }
  
  
  /**
   * Sorts the given String ArrayList alphabetically.
   */
  public static void sortStringArrayAlphabetically(ArrayList<String> arr)
  {
    String temp;
    for(int i = 0; i < arr.size() - 1; i++)
    {
      for (int i2 = 0; i2 < arr.size() - i - 1; i2++)
      {
        if (arr.get(i2).compareTo(arr.get(i2 + 1)) > 0)
        {
           temp = arr.get(i2);
           arr.set(i2, arr.get(i2 + 1));
           arr.set(i2 + 1, temp);
        }
      }
    }
  }
  
  
  /**
   * If the given String is found in the given ArrayList,
   * its index is returned, otherwise -1.
   */
  public static int contains(ArrayList<String> array, String find)
  {
    for (int i = 0; i < array.size(); i++)
      if (array.get(i).equals(find))
        return i;
    return -1;
  }
  
  
  /**
   * If the given String is found in the given ArrayList,
   * it is removed and returned.
   */
  public static String remove(ArrayList<String> array, String find)
  {
    for (int i = 0; i < array.size(); i++)
      if (array.get(i).equals(find))
        return array.remove(i);
    return null;
  }
  
  
  
  
}
