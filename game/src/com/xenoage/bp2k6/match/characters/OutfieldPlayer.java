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
package com.xenoage.bp2k6.match.characters;

import javax.vecmath.*;

import net.sf.jirr.*;

import com.xenoage.bp2k6.*;
import com.xenoage.bp2k6.audio.SoundEffectPool;
import com.xenoage.bp2k6.match.*;
import com.xenoage.bp2k6.match.characters.enums.*;
import com.xenoage.bp2k6.util.*;


/**
 * Class for an outfield player.
 *
 * @author   Andreas Wenger
 */
public class OutfieldPlayer
  extends Player
{

  //the node of the player's cap (null, if player has no cap)
  private ISceneNode nodeHeaddress = null;
  private boolean headdressColorTemplate = false;


  /**
   * Constructor.
   * The parameters <code>team</code> and <code>physicsManager</code>
   * may be <code>null</code>.
   */
  public OutfieldPlayer(PlayerInfo info, Team team, PlayerPosition playerPosition,
    OutfieldPlayerMesh mesh, ISceneManager sceneManager, PhysicsManager physicsManager)
  {

    //call super constructor
    super(info, team, playerPosition, mesh, sceneManager, physicsManager);

    ISceneManager smgr = GameEngine.getSceneManager();
   
    //glasses...
    if (info.getGlasses().equals("glasses"))
    {
      IAnimatedMesh meshGlasses = smgr.getMesh("data/meshes/glasses-2.ms3d");
      ISceneNode nodeGlasses = smgr.addAnimatedMeshSceneNode(meshGlasses);
      nodeGlasses.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      nodeGlasses.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      nodeGlasses.setRotation(new vector3df(0, -90, -90));
      nodeGlasses.setPosition(new vector3df(-21, 0, 0)); //y: 0
      ISceneNode playerHead = node.getMS3DJointNode("Bip01 HeadNub");
      playerHead.addChild(nodeGlasses); //*-/
    }

    //cap...
    if (info.getHeaddress().length() == 0 ||
      info.getHeaddress().equals("cap"))
    {
      headdressColorTemplate = true;
      IAnimatedMesh meshCap = smgr.getMesh("data/meshes/cap.ms3d");
      nodeHeaddress = smgr.addAnimatedMeshSceneNode(meshCap);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      nodeHeaddress.setRotation(new vector3df(0, -90, -90));
      nodeHeaddress.setPosition(new vector3df(-20, 0, 0));
      nodeHeaddress.setMaterialTexture(0, GameEngine.getVideoDriver().getTexture(
        "data/meshes/cap.png"));
      ISceneNode playerHead = node.getMS3DJointNode("Bip01 HeadNub");
      playerHead.addChild(nodeHeaddress);
    }
    else if (info.getHeaddress().equals("iro"))
    {
      IAnimatedMesh meshHeaddress = smgr.getMesh("data/meshes/iro.x");
      nodeHeaddress = smgr.addAnimatedMeshSceneNode(meshHeaddress);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      nodeHeaddress.setRotation(new vector3df(0, 90, -90));
      nodeHeaddress.setPosition(new vector3df(-20, 0, 0));
      ISceneNode playerHead = node.getMS3DJointNode("Bip01 HeadNub");
      playerHead.addChild(nodeHeaddress);
    }
    else if (info.getHeaddress().equals("ted"))
    {
      IAnimatedMesh meshHeaddress = smgr.getMesh("data/meshes/ted.x");
      nodeHeaddress = smgr.addAnimatedMeshSceneNode(meshHeaddress);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      nodeHeaddress.setRotation(new vector3df(0, 90, -90));
      nodeHeaddress.setPosition(new vector3df(-20, 0, 0));
      ISceneNode playerHead = node.getMS3DJointNode("Bip01 HeadNub");
      playerHead.addChild(nodeHeaddress);
    }
    else if (info.getHeaddress().equals("bobblecap"))
    {
      IAnimatedMesh meshHeaddress = smgr.getMesh("data/meshes/bobble_cap.x");
      nodeHeaddress = smgr.addAnimatedMeshSceneNode(meshHeaddress);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_LIGHTING, true);
      nodeHeaddress.setMaterialFlag(E_MATERIAL_FLAG.EMF_FOG_ENABLE, true);
      nodeHeaddress.setRotation(new vector3df(0, 90, -90));
      nodeHeaddress.setPosition(new vector3df(-20, 0, 0));
      ISceneNode playerHead = node.getMS3DJointNode("Bip01 HeadNub");
      playerHead.addChild(nodeHeaddress);
    }
    
  }


  /**
   * Sets the textures of the player.
   * @param texturePlayer   Texture of the player (body with cloths)
   * @param textureCap      Texture of the cap (may be null, of no cap used)
   */
  public void setTexture(ITexture textureBody, ITexture textureCap)
  {
    super.setTexture(textureBody);
    if (nodeHeaddress != null && textureCap != null && headdressColorTemplate)
    {
      nodeHeaddress.setMaterialTexture(0, textureCap);
    }
  }
  
  
  /**
   * Updates the current animation.
   * If we are at the end of an animation stage,
   * a new animation stage will be started.
   */
  @Override public void updateAnimation()
   {
     if (lastFrame > node.getFrameNr() || animationStage == AnimationStage.None)
     {
       //animation stage is over. use the default
       //follower animation.
       AnimationStage s;
       if (animationStage == AnimationStage.RunLeft)
       {
         s = AnimationStage.RunRight;
       }
       else if (animationStage == AnimationStage.RunRight)
       {
         s = AnimationStage.RunLeft;
       }
       else if (animationStage == AnimationStage.Wait)
       {
         //when waiting, use bored-animation from time to time
         double m = Math.random();
         if (m < 0.05)
         {
           s = AnimationStage.WaitBored1;
         }
         else if (m < 0.1)
         {
           s = AnimationStage.WaitBored2;
         }
         else
         {
           s = AnimationStage.Wait;
         }
       }
       else if (animationStage.isTumbling())
       {
         //stop player after or tumbling
         s = AnimationStage.Wait;
         setTarget(null, null);
       }
       else if (animationStage.isEmotion())
       {
         //emotion is over
         s = AnimationStage.Wait;
       }
       else
       {
         //default: waiting
         s = AnimationStage.Wait;
       }
       setAnimationStage(s);
     }
     lastFrame = node.getFrameNr();
   }


  /**
   * Shows immediately the given emotion and stops
   * the movement of this character.
   */
  @Override public void setEmotion(Emotion emotion, boolean sound)
  {
    setTarget(null, null);
    if (emotion == Emotion.Elation)
    {
      //player is happy
      if (MathTools.random() < 0.5f)
        setAnimationStage(AnimationStage.ElationApplause);
      else
        setAnimationStage(AnimationStage.ElationJump);
      if (sound)
        SoundEffectPool.Sound.PlayerCheer.play();
    }
    else if (emotion == Emotion.Desperation)
    {
      //player is unhappy
      setAnimationStage(AnimationStage.DesperationHeadshake);
      if (sound)
        SoundEffectPool.Sound.PlayerRail.play();
    }
  }
  
  
  /**
   * Dives for the ball with the given velocity:
   * Not supported.
   */
  @Override public void dive(Vector3f direction, float speed)
  {
  }
  
  
  /**
   * Call this method in every step() when the player
   * is diving for the ball:
   * Not supported (outfield player does not dive for the ball)
   */
  @Override protected void diveStep()
  {
  }

}
