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
package com.xenoage.bp2k6.match.stadium.crowd;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.match.stadium.Stand;
import com.xenoage.bp2k6.match.stadium.objects.SimpleStadiumObject;
import com.xenoage.bp2k6.util.*;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3f;

import org.w3c.dom.*;

import net.sf.jirr.*;


/**
 * A block of spectators (crowd). This class
 * contains the spectators of one stand of
 * the stadium and can contain a bengal light.
 * 
 * @author Andreas Wenger
 */
public class StandCrowd
{
  private Stand stand;
  private float standAngleAudienceWave; //position angle of this stand
  
  private int charactersCount = 0;
  private int characters[][]; //[char-index][0=row;1=col]
  private float charactersBaseMood[]; //constant base mod
  
  private ICrowdSceneNode node = null;
  
  //bengal light
  private Vector3f bengalLightPosition = null;
  private BengalLight bengalLight = null;
  
  //flags
  private ArrayList<SimpleStadiumObject> flags = new ArrayList<SimpleStadiumObject>();
  private ArrayList<SimpleStadiumObject> flags2 = new ArrayList<SimpleStadiumObject>();
  private ArrayList<Vector3f> flags2OriginalPosition = new ArrayList<Vector3f>();
  private ArrayList<vector3df> flags2Position = new ArrayList<vector3df>();
  
  private int lastStepIndex;
 
  
  
  /**
   * Create a crowd for the given stand
   * with the given attendance (1 = full,
   * ..., 0 = empty).
   * If it is a fanblock, it has 150% more
   * attendance and particularly fancolors.
   */
  public StandCrowd(Stand stand, float attendance, boolean fanblock,
    ISceneManager sceneManager, IVideoDriver videoDriver, ITexture crowdTexture,
    boolean celebration)
  {
    this.stand = stand;
    standAngleAudienceWave = MathTools.Vector3fToAngle(stand.getPosition());
    try
    {
      Document doc = XMLReader.readFile("data/stands/" + stand.getID() + ".xml");
      Node root = doc.getFirstChild();
      //modification
      Node eModificationList = XMLReader.getChildElement(root, "modifications");
      if (eModificationList == null)
        throw new Exception("No modifications list found!");
      List<Element> eModifications = XMLReader.elements(
        eModificationList, "modification");
      for (int iMod = 0; iMod < eModifications.size(); iMod++)
      {
        Node eMod = eModifications.get(iMod);
        if (XMLReader.readAttributeValue(eMod, "id").equals(stand.getModification()))
        {
          //load all available characters
          Node eCrowd = XMLReader.getChildElement(eMod, "crowd");
          if (eCrowd != null)
          {
            List<Element> listChars = XMLReader.elements(eCrowd, "char");
            //compute number of characters
            if (fanblock)
              attendance *= 2.5f;
            int charCount = (int) (attendance * listChars.size());
            if (charCount < listChars.size())
              charactersCount = charCount;
            else
              charactersCount = listChars.size();
            if (charactersCount == 0)
              return;
            //reduce characters, if necessary
            while (listChars.size() > charactersCount)
            {
              listChars.remove((int) (Math.random() * listChars.size()));
            }
            characters = new int[charactersCount][2];
            charactersBaseMood = new float[charactersCount];
            //ICrowdSceneNode* addCrowdSceneNode(ISceneNode* parent, s32 id,
            //  s32 characterCount,const core::vector3df& position,
            //  const core::vector3df& rotation, const core::vector3df& scale) = 0;
            node = sceneManager.addCrowdSceneNode(
              stand.getNode(), 0, charactersCount,
              new vector3df(0, 0, 0), new vector3df(0, 0, 0), new vector3df(0, 0, 0));
            node.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
            node.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
            node.setMaterialTexture(0, crowdTexture);
            node.setMaterialType(E_MATERIAL_TYPE.EMT_TRANSPARENT_ALPHA_CHANNEL);
            //load characters
            for (int iChar = 0; iChar < listChars.size(); iChar++)
            {
              Node eChar = listChars.get(iChar);
              int type;
              if (fanblock && MathTools.random() < 0.9f)
                type = ((int) (Math.random() * 2)) * 2;
              else
                type = (int) (Math.random() * 4);
              characters[iChar][0] = type;
              float x = XMLReader.readAttributeValueFloat(eChar, "x") +
                MathTools.randomCenter(0.1f);
              float y = XMLReader.readAttributeValueFloat(eChar, "y");
              float z = XMLReader.readAttributeValueFloat(eChar, "z") +
              MathTools.randomCenter(0.1f);
              float rotY = 90 - XMLReader.readAttributeValueFloat(eChar, "roty");
              float size = 1 + MathTools.randomCenter(0.1f);
              //void createCharacter(
              //  s32 characterIndex, f32 x, f32 y, f32 z, f32 rotY, f32 width, f32 height);
              node.createCharacter(iChar, x, y, z,
                (rotY + MathTools.randomCenter(10)) * MathTools.DEG_TO_RAD,
                1.45f * size, 2.6f * size);
              setCharacterFrame(iChar, (int) (Math.random() * 7));
              //base emotion
              charactersBaseMood[iChar] = MathTools.randomCenter(5) +
                (fanblock ? 3 : 0);
              //flag
              if (y > 2 && MathTools.random() > 0.998f - (fanblock ? 0.012 : 0) -
                (celebration ? 0.012 : 0) && !Settings.isVideoLowQuality())
              {
                SimpleStadiumObject flag = new SimpleStadiumObject("flag.ms3d");
                flag.addToScene(sceneManager, videoDriver, null);
                flag.getNode().setPosition(new vector3df(x, y + 0.5f, z));
                float scale = flag.getNode().getScale().getX();
                float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
                flag.getNode().setScale(new vector3df(
                  scale / u, scale / u, scale / u));
                stand.getNode().addChild(flag.getNode());
                flags.add(flag);
              }
              else if (y > 2 && MathTools.random() > 0.997f - (fanblock ? 0.020 : 0) -
                (celebration ? 0.020 : 0) && !Settings.isVideoLowQuality())
              {
                SimpleStadiumObject flag = new SimpleStadiumObject("flag2.x");
                flag.addToScene(sceneManager, videoDriver, null);
                float scale = flag.getNode().getScale().getX();
                float u = GameEngine.UNITS_GAME_TO_IRRLICHT;
                flag.getNode().setScale(new vector3df(
                  scale / u, scale / u, scale / u));
                stand.getNode().addChild(flag.getNode());
                flags2.add(flag);
                Vector3f pos = new Vector3f(x, y + 0.5f, z);
                vector3df nativePos = new vector3df(x, y + 0.5f, z);
                flag.getNode().setPosition(nativePos);
                flags2OriginalPosition.add(pos);
                flags2Position.add(nativePos);
              }
            }
            node.updateBox();
          }
          else
          {
            //this stand has no crowd.
          }
          //load bengal light position
          Node eBengalLight = XMLReader.getChildElement(eMod, "bengallight");
          if (eBengalLight != null)
          {
            bengalLightPosition = new Vector3f(
              XMLReader.readAttributeValueFloat(eBengalLight, "x"),
              XMLReader.readAttributeValueFloat(eBengalLight, "y"),
              XMLReader.readAttributeValueFloat(eBengalLight, "z"));
          }
        }
      }
    }
    catch (Exception ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this,
        "Stand crowd \"" + stand.getID() + "\" , modification \"" + 
        stand.getModification() + "\" could not be loaded! Details:");
      Main.fatalError(this, ex);
    }
  }
  
  
  /**
   * Set animation frame of the given character.
   */
  private void setCharacterFrame(int characterIndex, int frame)
  {
    int t = characters[characterIndex][0];
    characters[characterIndex][1] = frame;
    //frame: 72 x 128 pixel
    float sx = 72f / 512f;
    float sy = 128f / 512f;
    //virtual void setCharacterTCords(s32 characterIndex,
    //  f32 x0, f32 y0, f32 x1, f32 y1, f3r2 x2, f32 y2, f32 x3, f32 y3);
    node.setCharacterTCords(characterIndex,
      (frame + 0) * sx, (t + 1) * sy,
      (frame + 1) * sx, (t + 1) * sy,
      (frame + 1) * sx, (t + 0) * sy,
      (frame + 0) * sx, (t + 0) * sy);
  }
  
  
  /**
   * Removes this stand crowd from the scene.
   */
  public void removeFromScene()
  {
    if (node != null)
      node.remove();
  }
  
  
  /**
   * Computes this stand crowd's actions.
   * Call this function every 20 ms.
   */
  public void step20ms(CrowdMood mood)
  {
    //compute action for each character every 200 ms, but not all
    //characters in one step, but distributed in 20 ms steps
    lastStepIndex = (lastStepIndex + 1) % 10;
    for (int i = lastStepIndex; i < charactersCount; i+=10)
    {
      int frame = characters[i][1];
      if (mood.isAudienceWave())
      {
        //audience wave
        float currentWaveAngle = mood.getAudienceWavePosition();
        if (MathTools.getAngle(currentWaveAngle, standAngleAudienceWave +
          (1.0f * i / charactersCount) * 35f) < 45f)
        {
          //wave is there
          if (frame == 2)
            setCharacterFrame(i, 1); //stand up
          else if (frame == 1)
            setCharacterFrame(i, 0); //stand up
          else if (frame == 0)
            setCharacterFrame(i, 6); //hands up
          else if (frame != 6)
            setCharacterFrame(i, 2); //begin stand up
        }
        else
        {
          //wave is not there
          if (frame == 0)
            setCharacterFrame(i, 1); //sit down
          else if (frame == 1)
            setCharacterFrame(i, 2); //sit down
          else if (frame != 2)
            setCharacterFrame(i, 0); //begin sit down
        }
      }
      else
      {
        //normal emotion
        float charMood = mood.getMoodValue() + charactersBaseMood[i];
        //animation
        if (charMood < -7 || charMood > 5)
        {
          //happy or angry
          if (frame == 2)
            setCharacterFrame(i, 1); //stand up
          else if (frame == 1)
            setCharacterFrame(i, 0); //stand up
          else if (frame == 5)
            setCharacterFrame(i, 4);
          else
            setCharacterFrame(i, 5);
        }
        else
        {
          //normal
          if (frame == 5 || frame == 4)
            setCharacterFrame(i, 0); //sit down
          else if (frame == 0)
            setCharacterFrame(i, 1); //sit down
          else
            setCharacterFrame(i, 2);
        }
      }
    }
    float timeNow = GameEngine.getFrameTime(true);
    for (int i = 0; i < flags2.size(); i++)
    {
      vector3df v = flags2Position.get(i);
      v.setY(flags2OriginalPosition.get(i).y +
        0.3f * MathTools.sin(i * 60 + timeNow * (2 + 1f * (i % 4) / 2)));
      flags2.get(i).getNode().setPosition(v);
    }
  }
  
  
  /**
   * Gets the number of spectators.
   */
  public int getSpectatorsCount()
  {
    return charactersCount;
  }
  
  
  /**
   * Gets the position where a bengal light can appear,
   * or <code>null</code> if not set.
   */
  public Vector3f getBengalLightPosition()
  {
    return bengalLightPosition;
  }


  /**
   * Gets the current bengal light, or null.
   */
  public BengalLight getBengalLight()
  {
    return bengalLight;
  }


  /**
   * Sets the current bengal light.
   */
  public void setBengalLight(BengalLight bengalLight)
  {
    this.bengalLight = bengalLight;
  }


  /**
   * Gets the stand this crowd belongs to.
   */
  public Stand getStand()
  {
    return stand;
  }
  
  
  /**
   * Sets the adboard texture (needed for flags).
   */
  public void setAdboardsTexture(ITexture texFlag1, ITexture texFlag2)
  {
    for (int i = 0; i < flags.size(); i++)
    {
      flags.get(i).setTexture(texFlag1);
      flags.get(i).getNode().setAnimationSpeed(
        800 + (int) MathTools.randomCenter(400));
    }
    for (int i = 0; i < flags2.size(); i++)
    {
      flags2.get(i).getNode().getMaterial(1).setTexture(0, texFlag2);
    }
  }
  
}
