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
package com.xenoage.bp2k6.match.stadium;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * This class contains information
 * about a stadium.
 *
 * @author Andi
 */
public class StadiumInfo
{
  //general information
  private String id;
  private String name;
  
  //maximum number of spectators
  private int maxSpectators;
  
  //goals
  public enum Goals {
    GarbageCans(0.4f),
    WoodenGoals(0.7f),
    AluminumGoals(1.0f);
    
    public float crowdAttendance;
    Goals (float crowdAttendance)
    {
      this.crowdAttendance = crowdAttendance;
    }
  };
  private Goals goals = Goals.AluminumGoals; //used goal
  private boolean woodenGoalsBought = false;
  private boolean aluminumGoalsBought = false;
  
  
  /**
   * First constructor possibility:
   * Load stadium information from XML file.
   * @param id     The id of the stadium.
   */
  public StadiumInfo(String id)
  {
    //load stadium info from XML file
    try
    {
      Document doc;
      //first look in config/newstadium folder; if not found,
      //look in temp folder if not found,
      //look in stadiums folder
      if (new java.io.File("data/config/newstadium/" + id + ".xml").exists())
        doc = XMLReader.readFile("data/config/newstadium/" + id + ".xml");
      else if (new java.io.File("data/temp/" + id + ".xml").exists())
        doc = XMLReader.readFile("data/temp/" + id + ".xml");
      else
        doc = XMLReader.readFile("data/stadiums/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      if (!id.equals(this.id))
      {
        if (!this.id.equals("newstadium"))
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Filename and ID of stadium \"" +
            id + "\" is not identical! Filename: \"" +
            id + ".xml\", ID: \"" + this.id + "\"");
        }
        this.id = id; //use filename for id
      }
      //name
      this.name = XMLReader.readAttributeValue(root, "name");
      //goals
      Node eGoals = XMLReader.getChildElement(root, "goals");
      if (eGoals != null)
      {
        String goalID = XMLReader.readAttributeValue(eGoals, "id");
        if (goalID.equals("garbagecans"))
          goals = Goals.GarbageCans;
        else if (goalID.equals("woodengoals"))
          goals = Goals.WoodenGoals;
        else
          goals = Goals.AluminumGoals;
        //other goals available? (already bought)
        woodenGoalsBought = XMLReader.readAttributeValueBoolean(eGoals, "wooden");
        aluminumGoalsBought = XMLReader.readAttributeValueBoolean(eGoals, "aluminum");
      }
      //maximum numbers of spectators
      Node eStands = XMLReader.getChildElement(root, "stands");
      if (eStands != null)
      {
        maxSpectators = XMLReader.readAttributeValueInt(eStands, "maxspectators");
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "StadiumInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }


  /**
   * Second constructor possibility:
   * Creates a new stadium.
   */
  public StadiumInfo(String id, String name)
  {
    this.id = id;
    this.name = name;
  }
  
  
  /**
   * Gets the id of the stadium.
   */
  public String getID()
  {
    return id;
  }
  
  
  /**
   * Sets the id of the stadium.
   */
  public void setID(String id)
  {
    this.id = id;
  }


  /**
   * Gets the name of the stadium.
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Sets the name of the stadium.
   */
  public void setName(String name)
  {
    this.name = name;
  }


  /**
   * Gets the type of the goals.
   */
  public Goals getGoals()
  {
    return goals;
  }


  /**
   * Sets the type of the goals.
   */
  public void setGoals(Goals goals)
  {
    this.goals = goals;
    if (goals == Goals.WoodenGoals)
      woodenGoalsBought = true;
    else if (goals == Goals.AluminumGoals)
      aluminumGoalsBought = true;
  }
  
  
  /**
   * Gets the local name of the goals.
   */
  public String getGoalsName()
  {
    if (goals == Goals.GarbageCans)
      return Language.get("goal1");
    else if (goals == Goals.WoodenGoals)
      return Language.get("goal2");
    else
      return Language.get("goal3");
  }


  /**
   * Gets the maximum number of spectators.
   */
  public int getMaxSpectators()
  {
    return maxSpectators;
  }


  /**
   * Sets the maximum number of spectators.
   */
  void setMaxSpectators(int maxSpectators)
  {
    this.maxSpectators = maxSpectators;
  }
  

  /**
   * Returns <code>true</code>, if the user already
   * bought an aluminum goal.
   */
  public boolean isAluminumGoalsBought()
  {
    return aluminumGoalsBought;
  }


  /**
   * Sets, if the user already bought an aluminum goal.
   */
  void setAluminumGoalsBought(boolean aluminumGoalsBought)
  {
    this.aluminumGoalsBought = aluminumGoalsBought;
  }


  /**
   * Returns <code>true</code>, if the user already
   * bought a wooden goal.
   */
  public boolean isWoodenGoalsBought()
  {
    return woodenGoalsBought;
  }


  /**
   * Sets, if the user already bought a wooden goal.
   */
  void setWoodenGoalsBought(boolean woodenGoalsBought)
  {
    this.woodenGoalsBought = woodenGoalsBought;
  }
  
  
  /**
   * Gets the cost of the given goal type.
   */
  public int getGoalTypeCost(Goals goalType)
  {
    try
    {
      ConfigFile cost = new ConfigFile("data/config/cost.xml");
      if (goalType == Goals.WoodenGoals)
        return cost.getValueInt("goal2");
      else if (goalType == Goals.AluminumGoals)
        return cost.getValueInt("goal3");
      else
        return 0;
    }
    catch (Exception ex)
    {
      return 0;
    }
  }
  
}
