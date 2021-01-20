/**
 * Bolzplatz 2006
 * Copyright (C) 2006 by Xenoage Software
 * 
 * Attention: This file is part of the Gold-Edition
 * of Bolzplatz 2006 and is not released under the GPL!
 * Ask info@xenoage.com if you want to modify it.
 */
package com.xenoage.bp2k6.tools.stadiumeditor;

import java.awt.event.KeyEvent;

import javax.swing.JOptionPane;
import javax.vecmath.*;

import com.xenoage.bp2k6.Main;
import com.xenoage.bp2k6.match.stadium.*;
import com.xenoage.bp2k6.match.stadium.StadiumInfo.Goals;
import com.xenoage.bp2k6.match.stadium.attractions.*;
import com.xenoage.bp2k6.match.stadium.floodlight.Floodlight;
import com.xenoage.bp2k6.match.stadium.objects.SimpleStadiumObject;
import com.xenoage.bp2k6.match.stadium.scoreboard.Scoreboard;
import com.xenoage.bp2k6.match.terrain.*;
import com.xenoage.bp2k6.util.*;
import com.xenoage.bp2k6.util.language.Language;
import com.xenoage.bp2k6.tools.stadiumeditor.gui.MainFrame;

import net.sf.jirr.*;


/**
 * This is the main class of the
 * stadium editor (developer mode).
 * 
 * @author Andreas Wenger
 */
public class StadiumEditor
{
  
  public static final String version = "1.1";
  
  //operating system
  private boolean osWindows = false;

  //main window
  MainFrame mainFrame;
  
  //current stadium
  Stadium stadium = null;
  
  //current settings
  boolean terrainVisible = true;
  boolean gridAlign = true;
  
  //selected object (null, a Stand, ...)
  StadiumObject selectedObject;
  
  
  /**
   * Constructor.
   * Starts the stadium editor.
   */
  public StadiumEditor()
  {
    //running in Windows?
    String osName = System.getProperty("os.name");
    if (osName.startsWith("Windows"))
      osWindows = true;
    
    mainFrame = new MainFrame(this);
    if (osWindows)
      mainFrame.setVisible(true);
    
    //IrrlichtWindow irrlichtWindow = new IrrlichtWindow(null);
    //irrlichtWindow.createIrrlichtDevice();
    //while (true);
  }

  
  /**
   * Called by the MainFrame when it is completely loaded.
   */
  public void mainFrameLoaded()
  {
    //create new stadium
    createNewStadium();
  }
  
  
  /**
   * Starts the stadium editor standalone application.
   */
  public static void main(String[] args)
  {
    //clear the temp folder
    FileUtils.clearTempFolder();
    
    Main.setFatalErrorMsgBoxEnabled(true);
    Language.loadLanguage(Language.getSelectedLanguageID());
    
    new StadiumEditor();
  }
  
  
  /**
   * Loads the specified stadium.
   */
  public void loadStadium(String id)
  {
    mainFrame.startEdit();
    if (stadium != null)
      stadium.removeFromScene();
    stadium = new Stadium(id);
    stadium.getStadiumInfo().setID("editor-stadium");
    stadium.addToScene(mainFrame.getSceneManager(), mainFrame.getVideoDriver(), null);
    mainFrame.updateStadiumData();
    setTerrainVisible(terrainVisible);
    mainFrame.endEdit();
  }
  
  
  /**
   * Gets the current stadium.
   */
  public Stadium getStadium()
  {
    return stadium;
  }
  
  
  /**
   * Sets the terrain visibility.
   */
  public void setTerrainVisible(boolean visible)
  {
    terrainVisible = visible;
    if (stadium != null)
    {
      Terrain terrain = stadium.getTerrain();
      if (terrain != null)
        terrain.setVisible(terrainVisible);
    }
  }
  
  
  /**
   * Sets the grid alignment on or off.
   */
  public void setGridAlign(boolean b)
  {
    gridAlign = b;
  }
  
  
  /**
   * Sets the terrain by its ID, or <code>null</code> to delete it.
   */
  public void setTerrain(String id)
  {
    stadium.setTerrain(id, mainFrame.getSceneManager(), mainFrame.getVideoDriver());
    mainFrame.repaint3DView();
    mainFrame.updateStadiumData();
  }
  
  
  /**
   * Save the current stadium as a XML file:
   * "data/stadiums/" + id + ".xml"
   */
  public void saveStadium(String id)
  {
    stadium.getStadiumInfo().setID(id);
    stadium.getStadiumInfo().setName(mainFrame.getStadiumName());
    try
    {
      stadium.saveToXMLFile(false);
      JOptionPane.showMessageDialog(mainFrame,
        "Stadium \"" + id + "\" saved.");
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Saving stadium \"" + id + "\" failed! Details:");
      Logging.log(this, ex);
      showMessageBox(
        "Error: Stadion could not be saved.\nSee log.txt for details.", true);
    }
    mainFrame.updateStadiumData();
  }
  
  
  /**
   * Adds a new stand to the stadium.
   */
  public void addNewStand(String id)
  {
    mainFrame.startEdit();
    Stand s = stadium.addStand(id, new Vector3f(0, 0, 0), new Vector3f(),
      mainFrame.getSceneManager(), mainFrame.getVideoDriver());
    stadium.unselectAll();
    selectedObject = s;
    s.setSelected(true);
    mainFrame.updateStadiumData();
    mainFrame.endEdit(); 
    mainFrame.repaint3DView();
    mainFrame.focus3DView();
  }
  
  
  /**
   * Adds a new adboard to the stadium.
   */
  public void addNewAdboard(String id)
  {
    mainFrame.startEdit();
    Adboard a = stadium.addAdboard(id, new Vector3f(0, 0, 0), new Vector3f());
    stadium.unselectAll();
    selectedObject = a;
    a.setSelected(true);
    mainFrame.updateStadiumData();
    mainFrame.endEdit();
    mainFrame.repaint3DView();
    mainFrame.focus3DView();
  }
  
  
  /**
   * Adds a new floodlight to the stadium.
   */
  public void addNewFloodlight(int stage)
  {
    mainFrame.startEdit();
    Floodlight f = stadium.addFloodlight(stage, 0);
    stadium.unselectAll();
    selectedObject = f;
    f.setSelected(true);
    mainFrame.updateStadiumData();
    mainFrame.endEdit();
    mainFrame.repaint3DView();
    mainFrame.focus3DView();
  }
  
  
  /**
   * Sets the scoreboard of the stadium.
   */
  public void setScoreboard(int stage)
  {
    mainFrame.startEdit();
    Scoreboard s = stadium.setScoreboard(stage);
    stadium.unselectAll();
    selectedObject = s;
    s.setSelected(true);
    mainFrame.updateStadiumData();
    mainFrame.endEdit();
    mainFrame.repaint3DView();
    mainFrame.focus3DView();
  }
  
  
  /**
   * Adds a new attraction to the stadium.
   */
  public void addNewAttraction(String id)
  {
    mainFrame.startEdit();
    StadiumObject obj = null;
    //hot dog stand
    if (id.equals("hotdogstand"))
    {
      if (stadium.getHotDogStand() != null)
      {
        JOptionPane.showMessageDialog(null, Language.get("attractionalreadybuilt"));
      }
      else
      {
        stadium.setHotDogStand(new HotDogStand());
        obj = stadium.getHotDogStand();
      }
    }
    //betting office
    if (id.equals("bettingoffice"))
    {
      if (stadium.getBettingOffice() != null)
      {
        JOptionPane.showMessageDialog(null, Language.get("attractionalreadybuilt"));
      }
      else
      {
        stadium.setBettingOffice(new BettingOffice());
        obj = stadium.getBettingOffice();
      }
    }
    //beer bar
    if (id.equals("beerbar"))
    {
      if (stadium.getBeerBar() != null)
      {
        JOptionPane.showMessageDialog(null, Language.get("attractionalreadybuilt"));
      }
      else
      {
        
        stadium.setBeerBar(new BeerBar());
        obj = stadium.getBeerBar();
      }
    }
    //track
    if (id.equals("track"))
    {
      if (stadium.getTrack() != null)
      {
        JOptionPane.showMessageDialog(null, Language.get("attractionalreadybuilt"));
      }
      else
      {
        stadium.setTrack(new Track());
        obj = stadium.getTrack();
      }
    }
    //high jump
    if (id.equals("highjump"))
    {
      if (stadium.getHighJump() != null)
      {
        JOptionPane.showMessageDialog(null, Language.get("attractionalreadybuilt"));
      }
      else
      {
        stadium.setHighJump(new HighJump());
        obj = stadium.getHighJump();
      }
    }
    
    if (obj != null)
    {
      stadium.unselectAll();
      selectedObject = obj;
      obj.setSelected(true);
      mainFrame.updateStadiumData();
      mainFrame.repaint3DView();
      mainFrame.focus3DView();
    }
    mainFrame.endEdit();
    
  }
  
  
  /**
   * Adds a new 3d object to the stadium.
   */
  public void addNewObject(String id)
  {
    mainFrame.startEdit();
    SimpleStadiumObject o = stadium.addSimpleObject(id);
    stadium.unselectAll();
    selectedObject = o;
    o.setSelected(true);
    mainFrame.updateStadiumData();
    mainFrame.endEdit();
    mainFrame.repaint3DView();
    mainFrame.focus3DView();
  }
  
  
  /**
   * User selected a node (or <code>null</code>)
   */
  public void selectNode(ISceneNode node)
  {
    selectedObject = null;
    stadium.unselectAll();
    StadiumObject obj = stadium.findStadiumObject(node);
    if (obj != null)
    {
      selectedObject = obj;
      obj.setSelected(true);
    }
    mainFrame.repaint3DView();
  }

  
  /**
   * User pressed a key.
   */
  public void keyPressed(int keyCode)
  {
    mainFrame.startEdit();
    if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_UP ||
      keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_DOWN ||
      keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_Y)
    {
      boolean largeStep = false; //KeyEvent.isShiftDown();
      if (keyCode == KeyEvent.VK_LEFT)
        moveObject(0, largeStep);
      else if (keyCode == KeyEvent.VK_UP)
        moveObject(1, largeStep);
      else if (keyCode == KeyEvent.VK_RIGHT)
        moveObject(2, largeStep);
      else if (keyCode == KeyEvent.VK_DOWN)
        moveObject(3, largeStep);
      else if (keyCode == KeyEvent.VK_W)
        moveObject(4, largeStep);
      else if (keyCode == KeyEvent.VK_Y)
        moveObject(5, largeStep);
    }
    else if (keyCode == KeyEvent.VK_DELETE)
    {
      if (selectedObject != null)
      {
        if (selectedObject instanceof Stand)
        {
          stadium.removeStand((Stand) selectedObject);
        }
        else if (selectedObject instanceof Adboard)
        {
          stadium.removeAdboard((Adboard) selectedObject);
        }
        else if (selectedObject instanceof Scoreboard)
        {
          stadium.removeScoreboard();
        }
        else if (selectedObject instanceof Floodlight)
        {
          stadium.removeFloodlight((Floodlight) selectedObject);
        }
        else if (selectedObject instanceof SimpleStadiumObject)
        {
          stadium.removeSimpleObject((SimpleStadiumObject) selectedObject);
        }
        else
        {
          stadium.removeAttraction(selectedObject);
        }
        mainFrame.updateStadiumData();
        mainFrame.repaint3DView();
      }
    }
    else if (keyCode == KeyEvent.VK_A ||
      keyCode == KeyEvent.VK_S)
    {
      if (selectedObject != null && selectedObject instanceof Stand)
      {
        Stand stand = (Stand) selectedObject;
        if (keyCode == KeyEvent.VK_A)
          stand.addYRotation(-90);
        else
          stand.addYRotation(90);
        mainFrame.updateStadiumData();
        mainFrame.repaint3DView();
      }
      else if (selectedObject != null)
      {
        StadiumObject obj = (StadiumObject) selectedObject;
        if (keyCode == KeyEvent.VK_A)
          obj.addYRotation(-15f);
        else
          obj.addYRotation(15f);
        mainFrame.updateStadiumData();
        mainFrame.repaint3DView();
      }
    }
    else if (keyCode == KeyEvent.VK_M)
    {
      //change stand modification
      if (selectedObject != null && selectedObject instanceof Stand)
      {
        Stand standOld = (Stand) selectedObject;
        Stand standNew = standOld.getNextModification();
        stadium.unselectAll();
        stadium.removeStand(standOld);
        stadium.addStand(standNew);
        standNew.addToScene(mainFrame.getSceneManager(), mainFrame.getVideoDriver());
        selectedObject = standNew;
        standNew.setSelected(true);
        mainFrame.updateStadiumData();
        mainFrame.repaint3DView();
      }
    }
    mainFrame.endEdit();
  }
  
  
  /**
   * Move the selected object.
   * @param dir 0: left, 1: top, 2: right, 3: down
   * @param largeStep if <code>true</code>, 1m, else 0.01m
   */
  public void moveObject(int dir, boolean largeStep)
  {
    mainFrame.startEdit();
    float step = 0.01f; //1 cm
    if (largeStep) step = 1; //1 m
    if (selectedObject != null && selectedObject instanceof Stand)
    {
      Stand stand = (Stand) selectedObject;
      if (dir == 0)
        moveStand(stand, -step, 0);
      else if (dir == 1)
        moveStand(stand, 0, step);
      else if (dir == 2)
        moveStand(stand, step, 0);
      else if (dir == 3)
        moveStand(stand, 0, -step);
    }
    else if (selectedObject != null)
    {
      StadiumObject obj = (StadiumObject) selectedObject;
      if (dir == 0)
        moveStadiumObject(obj, -step, 0, 0);
      else if (dir == 1)
        moveStadiumObject(obj, 0, 0, step);
      else if (dir == 2)
        moveStadiumObject(obj, step, 0, 0);
      else if (dir == 3)
        moveStadiumObject(obj, 0, 0, -step);
      else if (dir == 4)
        moveStadiumObject(obj, 0, step, 0);
      else if (dir == 5)
        moveStadiumObject(obj, 0, -step, 0);
    }
    mainFrame.endEdit();
  }
  
  
  
  /**
   * Move the given stand.
   */
  private void moveStand(Stand stand, float x, float z)
  {
    mainFrame.startEdit();
    if (!gridAlign)
    {
      Vector3f pos = new Vector3f(stand.getPosition());
      pos.x += x;
      pos.z += z;
      stand.setPosition(pos);
    }
    else //gridAlign == true
    {
      Vector3f pos = new Vector3f(stand.getPosition());
      Point2f size = new Point2f(stand.getSize());
      Vector3f rot = stand.getRotation();
      if (Math.abs(rot.y - 90) < 0.1 ||
        Math.abs(rot.y - 270) < 0.1)
      {
        //if stand is rotated by 90 degrees, width and height
        //are interchanged
        float temp = size.x;
        size.x = size.y;
        size.y = temp;
      }
      pos.x = ((int) (pos.x - size.x / 2) / 10) * 10 + size.x / 2;
      pos.z = ((int) (pos.z - size.y / 2) / 10) * 10 + size.y / 2;
      if (x < 0)
        pos.x -= 10;
      else if (x > 0)
        pos.x += 10;
      if (z < 0)
        pos.z -= 10;
      else if (z > 0)
        pos.z += 10;
      stand.setPosition(pos);
    }
    mainFrame.endEdit();
    mainFrame.updateStadiumData();
    mainFrame.repaint3DView();
  }
  
  
  /**
   * Move the given stadium object.
   */
  private void moveStadiumObject(StadiumObject obj, float x, float y, float z)
  {
    mainFrame.startEdit();
    if (!gridAlign)
    {
      Vector3f pos = new Vector3f(obj.getPosition());
      pos.x += x;
      pos.y += y;
      pos.z += z;
      obj.setPosition(pos);
    }
    else //gridAlign == true
    {
      Vector3f pos = new Vector3f(obj.getPosition());
      //pos.x = ((int) (pos.x - size.x / 2) / 10) * 10 + size.x / 2;
      //pos.z = ((int) (pos.z - size.y / 2) / 10) * 10 + size.y / 2;
      if (x < 0)
        pos.x -= 1;
      else if (x > 0)
        pos.x += 1;
      if (y < 0)
        pos.y -= 1;
      else if (y > 0)
        pos.y += 1;
      if (z < 0)
        pos.z -= 1;
      else if (z > 0)
        pos.z += 1;
      obj.setPosition(pos);
    }
    mainFrame.endEdit();
    mainFrame.updateStadiumData();
    mainFrame.repaint3DView();
  }
  
  
  /**
   * Creates a new stadium.
   */
  public void createNewStadium()
  {
    mainFrame.startEdit();
    loadStadium("newstadium");
    stadium.getStadiumInfo().setID("editor-stadium");
    mainFrame.endEdit();
  }
  
  
  /**
   * @return Returns <code>true</code>, if the stadium
   * is empty (no terrain, no stands, ...)
   */
  public boolean isStadiumEmpty()
  {
    if (stadium == null) return true;
    if (stadium.getTerrain() != null) return false;
    if (stadium.getStandsCount() > 0) return false;
    return true;
  }
  
  
  /**
   * Closes the stadium editor
   */
  public void close()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Stadiumeditor is closing...");
    Logging.close();
    System.exit(0);
  }
  
  
  /**
   * Sets the goals ("1" to "3")
   */
  public void setGoals(int id)
  {
    mainFrame.startEdit();
    try
    {
      if (id == 1)
        stadium.getStadiumInfo().setGoals(Goals.GarbageCans);
      else if (id == 2)
        stadium.getStadiumInfo().setGoals(Goals.WoodenGoals);
      else if (id == 3)
        stadium.getStadiumInfo().setGoals(Goals.AluminumGoals);
      stadium.saveToXMLFile(true);
      loadStadium(stadium.getStadiumInfo().getID());
      mainFrame.repaint3DView();
      mainFrame.updateStadiumData();
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Could set goals and reload stadium:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
    }
    mainFrame.endEdit();
  }
  
  
  /**
   * Sets the lawn ("lawn1" to "lawn4")
   */
  public void setLawn(String id)
  {
    mainFrame.startEdit();
    try
    {
      if (id.equals("lawn1"))
        stadium.getField().setLawnQuality(0);
      else if (id.equals("lawn2"))
        stadium.getField().setLawnQuality(7);
      else if (id.equals("lawn3"))
        stadium.getField().setLawnQuality(12);
      else
        stadium.getField().setLawnQuality(17);
      stadium.saveToXMLFile(true);
      loadStadium(stadium.getStadiumInfo().getID());
      mainFrame.repaint3DView();
      mainFrame.updateStadiumData();
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Could set lawn and reload stadium:");
      Logging.log(Logging.LEVEL_ERRORS, this, ex);
    }
    mainFrame.endEdit();
  }
  
  
  private void showMessageBox(String text, boolean warning)
  {
    JOptionPane.showMessageDialog(mainFrame, text,
      Language.get("stadiumeditor_title"),
      (warning ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE));
  }


  /**
   * Returns true, if the operating system is Windows.
   */
  public boolean isOSWindows()
  {
    return osWindows;
  }
  
}
