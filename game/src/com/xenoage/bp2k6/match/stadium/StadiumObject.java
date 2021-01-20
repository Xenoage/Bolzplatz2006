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


import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.util.*;

import javax.vecmath.*;

import org.w3c.dom.Node;

import net.sf.jirr.*;


/**
 * Abstract base class for stadium objects
 * (stands, adboards, ...)
 *
 * @author Andreas Wenger
 */
public abstract class StadiumObject
{
  
  //id of the object
  protected String id;
  
  //position
  protected Vector3f position = new Vector3f();
  //rotation
  protected Vector3f rotation = new Vector3f();
  
  //scene nodes of this object (usually only one, but may be more)
  //can also be null, if object is not loaded into scene
  protected IAnimatedMeshSceneNode node = null;
  
  
  
  /**
   * Creates a new stadium objectfile.
   * @param id The id of the stadium object
   */
  public StadiumObject(String id)
  {
    this.id = id;
  }
  
  
  /**
   * Gets the position of this object.
   */
  public Vector3f getPosition()
  {
    return position;
  }
  
  
  /**
   * Sets the position of this object.
   */
  public void setPosition(Vector3f position)
  {
    this.position = position;
    if (node != null)
    {
      float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
      node.setPosition(new vector3df(
        position.x * u, position.y * u, position.z * u));
      node.updateAbsolutePosition();
    }
  }
  
  
  /**
   * Gets the rotation of this object.
   */
  public Vector3f getRotation()
  {
    return rotation;
  }
  
  
  /**
   * Sets the rotation of the object.
   */
  public void setRotation(Vector3f rotation)
  {
    this.rotation = rotation;
    if (node != null)
    {
      node.setRotation(Cast.Vector3ftovector3df(rotation));
      node.updateAbsolutePosition();
    }
  }
  
  
  /**
   * Add the given value in degrees to the y-rotation.
   */
  public void addYRotation(float yrot)
  {
    rotation.y = (rotation.y + yrot + 360) % 360;
    setRotation(rotation);
  }
  
  
  /**
   * Removes this object from the scene.
   */
  public void removeFromScene()
  {
    if (node != null)
      node.remove();
  }
  
  
  /**
   * Sets the visibility on or off.
   */
  public void setVisible(boolean visible)
  {
    if (node != null)
      node.setVisible(visible);
  }
  
  
  /**
   * Sets selection state on or off
   * (when selected, object glows blue)
   */
  public void setSelected(boolean selected)
  {
    if (node != null)
    {
      SMaterial m = node.getMaterial(0);
      if (selected)
      {
        m.setAmbientColor(new SColor(255, 0, 0, 255));
        m.setEmissiveColor(new SColor(255, 100, 100, 255));
        node.setDebugDataVisible(E_DEBUG_SCENE_TYPE.EDS_FULL);
      }
      else
      {
        m.setAmbientColor(new SColor(255, 255, 255, 255));
        m.setEmissiveColor(new SColor(0, 0, 0, 0));
        node.setDebugDataVisible(E_DEBUG_SCENE_TYPE.EDS_OFF);
      }
    }
  }


  
  public String getID()
  {
    return id;
  }
  
  
  public IAnimatedMeshSceneNode getNode()
  {
    return node;
  }
  
  
  /**
   * Gets the id of the node, or -1 if the node
   * does not exist.
   * @return
   */
  public int getNodeID()
  {
    if (node != null)
      return node.getID();
    else
      return -1;
  }
  
  
  /**
   * Adds the position and rotation of this object to
   * the given XML element.
   */
  public void addPositionAndRotationToXMLElement(Node e)
  {
    Node ePos = XMLWriter.addElement("position", e);
    XMLWriter.addAttribute(ePos, "x", String.valueOf(position.x));
    XMLWriter.addAttribute(ePos, "y", String.valueOf(position.y));
    XMLWriter.addAttribute(ePos, "z", String.valueOf(position.z));
    Node eRot = XMLWriter.addElement("rotation", e);
    XMLWriter.addAttribute(eRot, "x", String.valueOf(rotation.x));
    XMLWriter.addAttribute(eRot, "y", String.valueOf(rotation.y));
    XMLWriter.addAttribute(eRot, "z", String.valueOf(rotation.z));
  }
  
  
  /**
   * Sets the position and rotation of this object
   * to the values from the given XML element.
   */
  public void setPositionAndRotationFromXMLElement(Node e)
  {
    Node ePos = XMLReader.getChildElement(e, "position");
    if (ePos != null)
    {
      setPosition(new Vector3f(
        XMLReader.readAttributeValueFloat(ePos, "x"),
        XMLReader.readAttributeValueFloat(ePos, "y"),
        XMLReader.readAttributeValueFloat(ePos, "z")));
    }
    Node eRot = XMLReader.getChildElement(e, "rotation");
    if (eRot != null)
    {
      setRotation(new Vector3f(
        XMLReader.readAttributeValueFloat(eRot, "x"),
        XMLReader.readAttributeValueFloat(eRot, "y"),
        XMLReader.readAttributeValueFloat(eRot, "z")));
    }
  }

  
}
