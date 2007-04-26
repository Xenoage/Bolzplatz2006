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
package com.xenoage.bp2k6.match.referee;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.gamemodes.Career;
import com.xenoage.bp2k6.match.PlayerColors;
import com.xenoage.bp2k6.util.*;


/**
 * This class stores information
 * about a referee.
 *
 * @author Andi
 */
public class RefereeInfo
{
  
  //general information
  private String id;
  private String name;
  
  //colors and pattern (for texture)
  private PlayerColors colors;
  
  //values
  private int competence = 5;
  private int strictness = 5;
  private int loyality = 5;
  private int prejudice = 0;


  /**
   * First constructor possibility:
   * Load team information from XML file.
   */
  public RefereeInfo(String id)
  {
    //load team from XML file
    try
    {
      Document doc = XMLReader.readFile("data/referees/" + id + ".xml");
      Node root = doc.getFirstChild();
      //id
      this.id = XMLReader.readAttributeValue(root, "id");
      if (!id.equals(this.id))
      {
        if (!this.id.equals("newreferee"))
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Filename and ID of referee \"" +
            id + "\" is not identical! Filename: \"" +
            id + ".xml\", ID: \"" + this.id + "\"");
        }
        this.id = id; //use filename for id
      }
      //name
      this.name = XMLReader.readAttributeValue(root, "name");

      //colors
      Node eColors = XMLReader.getChildElement(root, "colors");
      if (eColors != null)
      {
        colors = new PlayerColors();
        Color col;
        try
        {
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColors, "skin"));
          colors.setColor(PlayerColors.COLOR_SKIN, col);
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColors, "shirt"));
          colors.setColor(PlayerColors.COLOR_SHIRT, col);
          String shirt2 = //optional
            XMLReader.readAttributeValue(eColors, "shirt2");
          if (shirt2.length() > 0)
          {
            col = ColorTools.getColor(shirt2);
            colors.setColor(PlayerColors.COLOR_SHIRT2, col);
          }
          else
          {
            colors.setColor(PlayerColors.COLOR_SHIRT2,
              colors.getColor(PlayerColors.COLOR_SHIRT));
          }
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColors, "pants"));
          colors.setColor(PlayerColors.COLOR_PANTS, col);
          col = ColorTools.getColor(
            XMLReader.readAttributeValue(eColors, "shoes"));
          colors.setColor(PlayerColors.COLOR_SHOES, col);
          
          colors.setPattern(XMLReader.readAttributeValue(eColors, "pattern"));
        }
        catch (Exception ex)
        {
          Logging.log(Logging.LEVEL_WARNINGS, this,
            "Referee colors could not be read from \"" + id + ".xml\":");
          Logging.log(Logging.LEVEL_WARNINGS, this, ex);
        }
      }
      
      //values
      Node eValues = XMLReader.getChildElement(root, "values");
      if (eValues != null)
      {
        competence = XMLReader.readAttributeValueInt(eValues, "competence");
        strictness = XMLReader.readAttributeValueInt(eValues, "strictness");
        loyality = XMLReader.readAttributeValueInt(eValues, "loyality");
      }
      else
      {
        Logging.log(Logging.LEVEL_WARNINGS, this,
          "Referee \"" + id + " has no values!");
      }
      
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "RefereeInfo \"" + id + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }


  /**
   * Second constructor possibility:
   * Creates a new referee.
   */
  public RefereeInfo(String id, String name)
  {
    this.id = id;
    this.name = name;
  }


  /**
   * Gets the id of the referee.
   */
  public String getID()
  {
    return id;
  }


  /**
   * Gets the name of the referee.
   */
  public String getName()
  {
    return name;
  }
  
  
  /**
   * Sets the name of the referee.
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Gets the colors of the team.
   */
  public PlayerColors getColors()
  {
    return colors;
  }


  /**
   * Sets the colors of the team.
   */
  public void setColors(PlayerColors colors)
  {
    this.colors = colors;
  }


  /**
   * Gets the competence of the referee.
   */
  public int getCompetence()
  {
    return competence;
  }
  
  
  /**
   * Gets the strictness of the referee.
   */
  public int getStrictness()
  {
    return strictness;
  }
  
  
  /**
   * Gets the loyality of the referee.
   */
  public int getLoyality()
  {
    return loyality;
  }
  
  
  /**
   * Gets the prejudice of the referee.
   * -10: helps team 0, +10: helps team 1.
   */
  public int getPrejudice()
  {
    return prejudice;
  }
  
  
  /**
   * Sets the prejudice of the referee.
   * -10: helps team 0, +10: helps team 1.
   */
  public void setPrejudice(int prejudice)
  {
    this.prejudice = prejudice;
  }
  
  
  /**
   * Gets the cost of a referee corruption.
   */
  public static int getCorruptionCost(int league, int stage)
  {
    if (league == Career.LEAGUE_ALLSTARS)
    {
      if (stage == 1)
        return 800;
      else if (stage == 2)
        return 2400;
      else if (stage == 3)
        return 8000;
    }
    else if (league == Career.LEAGUE_1)
    {
      if (stage == 1)
        return 400;
      else if (stage == 2)
        return 1200;
      else if (stage == 3)
        return 4000;
    }
    else if (league == Career.LEAGUE_2)
    {
      if (stage == 1)
        return 200;
      else if (stage == 2)
        return 600;
      else if (stage == 3)
        return 2000;
    }
    else if (league == Career.LEAGUE_3)
    {
      if (stage == 1)
        return 100;
      else if (stage == 2)
        return 300;
      else if (stage == 3)
        return 1000;
    }
    return 0;
  }

}
