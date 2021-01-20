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
package com.xenoage.bp2k6.gamemodes.reports;

import com.xenoage.bp2k6.util.XMLReader;
import com.xenoage.bp2k6.util.XMLWriter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * A table for a competition (league or
 * group round of the world cup).
 * @author awr
 *
 */
public class Table
{
  private ArrayList<TableEntry> entries = new ArrayList<TableEntry>();

  
  /**
   * Constructor.
   * Creates a new table with the given teams.
   */
  public Table(ArrayList<String> teamIDs)
  {
    for (int i = 0; i < teamIDs.size(); i++)
    {
      TableEntry entry = new TableEntry(teamIDs.get(i));
      entries.add(entry);
    }
  }
  
  
  /**
   * Constructor.
   * Creates a Table from the given XML element.
   */
  public Table(Node eTable)
  {
    //list of all entries
    Node eEntries = XMLReader.getChildElement(eTable, "entries");
    if (eEntries != null)
    {
      List<Element> listEntries = XMLReader.elements(eEntries, "entry");
      for (int i = 0; i < listEntries.size(); i++)
      {
        TableEntry entry = new TableEntry(listEntries.get(i));
        entries.add(entry);
      }
    }
  }
  
  
  /**
   * Constructor.
   * Creates a copy of the given table.
   */
  public Table(Table table)
  {
    for (int i = 0; i < table.getTableEntriesCount(); i++)
    {
      TableEntry entry = new TableEntry(table.getTableEntry(i + 1));
      entries.add(entry);
    }
  }
  
  
  /**
   * Gets the number of TableEntry objects
   * in this table.
   */
  public int getTableEntriesCount()
  {
    return entries.size();
  }
  
  
  /**
   * Gets the TableEntry at the given place
   * (starting at place 1).
   */
  public TableEntry getTableEntry(int place)
  {
    return entries.get(place - 1);
  }
  
  
  /**
   * Gets the TableEntry with the given team id
   * or <code>null</code> if not found.
   */
  public TableEntry getTableEntry(String teamID)
  {
    for (int i = 0; i < entries.size(); i++)
    {
      if (entries.get(i).getTeamID().equals(teamID))
        return entries.get(i);
    }
    return null;
  }
  
  
  /**
   * Sorts the table by their ranking
   * (@see TableEntry.hasBetterRankingThan).
   * @param userTeamID  id of the user controlled team,
   *                    or <code>null</code>
   */
  public void sortByRanking(String userTeamID)
  {
    int max = entries.size() - 1;
    for (int i = 0; i < max; i++)
    {
      for (int i2 = 0; i2 < max - i; i2++)
      {
        if(entries.get(i2 + 1).hasBetterRankingThan(entries.get(i2), userTeamID))
        {
          TableEntry temp = entries.get(i2);
          entries.remove(i2);
          entries.add(i2 + 1, temp);
        }
      }
    }
  }
  
  
  /**
   * Save this table within the given
   * XML element. A new child element "table"
   * with all information about the table
   * is created.
   */
  public void saveToXML(Node parentElement)
  {
    //create table element
    Node eMatchDay = XMLWriter.addElement("table", parentElement);
    //list of all entries
    Node eEntries = XMLWriter.addElement("entries", eMatchDay);
    for (int i = 0; i < entries.size(); i++)
    {
      TableEntry entry = entries.get(i);
      entry.saveToXML(eEntries);
    }
  }
  
  
  /**
   * Gets the 1 based position of the given team id
   * or 0 if not found.
   */
  public int getPosition(String teamID)
  {
    for (int i = 0; i < entries.size(); i++)
    {
      if (entries.get(i).getTeamID().equals(teamID))
        return i + 1;
    }
    return 0;
  }
  
}
