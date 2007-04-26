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
package com.xenoage.bp2k6.input;

/**
 * InputClass
 *
 * @author   Markus Madeja
 * @author   Andreas Wenger
 */

import com.xenoage.bp2k6.GameEngine;
import com.xenoage.bp2k6.util.Logging;

import java.util.Vector;

import net.sf.jirr.*;

import sdljava.SDLMain;
import sdljava.joystick.*;


public class Input
    extends IEventReceiver {
  private int Player = 2; //Spieleranzahl

  //bp2k6 keys
  public static final int left = 1;
  public static final int up = 2;
  public static final int right = 4;
  public static final int down = 8;
  public static final int button1 = 16;
  public static final int button2 = 32;
  public static final int button3 = 64;
  public static final int confirm = 0x0500;
  public static final int cancel = 0x0501;

//Einstellungen: Spieler, Taste, eine Alternative
  private int[][] ControllsKeyboard = new int[Player][7];
//Speichert, welche Taste gedrückt wurde (Tastatur) (Spieleranzahl,Taste)
  private int[][] ControllsJoystick = new int[Player][3];
//Speichert, welche Taste gedrückt wurde (Joystick) (Spieleranzahl,Taste)
  private int[] Pressed = new int[Player];
  private int[] JoyPressed = new int[Player];
//Nur im Game-Modus: Zeitpunkt des Tastendrucks eines Action-Buttons
//(wird benötigt zur Berechnung der Dauer eines Tastendrucks)
//0: Button1, 1: Button2, 2: Button3
  private float[][] pressedTime = new float[Player][3];

//Events speichern
  private Vector<KeyboardEvent> KeyEvents = new Vector<KeyboardEvent> ();
  private Vector<InputEvent> Events = new Vector<InputEvent> ();

  public boolean GameMode = true;
  private SDLJoystick[] Joystick = new SDLJoystick[Player];
  private int numJoysticks = 0;

  private boolean joystickNotPressedUp = true;
  private boolean joystickNotPressedDown = true;
  private boolean joystickNotPressedLeft = true;
  private boolean joystickNotPressedRight = true;
  private boolean joystickNotPressedConfirm = true;
  private boolean joystickNotPressedCancel = true;
  //private boolean keyboardNotPressedConfirm = true;
  //private boolean keyboardNotPressedCancel = true;
  
  private int activeJoystickIndex = 0;
  

  /**
   * Initialisation mit dem IrrlichtDevice, sucht und findet Joystick
   * und legt die Standartsteuerung für Spieler 1 fest
   * @param IrrlichtDevice
   */


  public Input(IrrlichtDevice videoDevice) {

    videoDevice.setEventReceiver(this);

    //TODO: remove comments
    
    ControllsKeyboard[0][0] = KeyboardEvent.KEY_LEFT; //KeyEvent.VK_LEFT;
    ControllsKeyboard[0][1] = KeyboardEvent.KEY_UP; //KeyEvent.VK_UP;
    ControllsKeyboard[0][2] = KeyboardEvent.KEY_RIGHT; //KeyEvent.VK_RIGHT;
    ControllsKeyboard[0][3] = KeyboardEvent.KEY_DOWN; //KeyEvent.VK_DOWN;
    ControllsKeyboard[0][4] = KeyboardEvent.KEY_KEY_A; //97; //KeyEvent.VK_A & shift down!?;
    ControllsKeyboard[0][5] = KeyboardEvent.KEY_KEY_S; //115; //KeyEvent.VK_S;
    ControllsKeyboard[0][6] = KeyboardEvent.KEY_KEY_D; //100; //KeyEvent.VK_D;
    
    ControllsJoystick[0][0] = 1; //Taste 1 (Wert der Taste + 1
    ControllsJoystick[0][1] = 2; //Taste 2  um 0 = nicht belegt
    ControllsJoystick[0][2] = 3; //Taste 3  zu ermöglichen)
    ControllsJoystick[1][0] = 1; //Taste 1
    ControllsJoystick[1][1] = 2; //Taste 2
    ControllsJoystick[1][2] = 3; //Taste 3 von Joy 2 von Spieler 2

    //create joystick/gamepad support
    initJoysticks();    

  }
  
  
  /**
   * Search for Joysticks and activate them.
   */
  public void initJoysticks()
  {
    Logging.log(Logging.LEVEL_MESSAGES, this,
      "Searching for joysticks/gamepads...");
    try
    {
      SDLMain.quitSubSystem(SDLMain.SDL_INIT_JOYSTICK);
      SDLMain.init(SDLMain.SDL_INIT_JOYSTICK);
      //SDLEvent.joystickEventState(SDLEventState.ENABLE);
  
      numJoysticks = SDLJoystick.numJoysticks();
      if (numJoysticks == 0)
      {
        Logging.log(Logging.LEVEL_MESSAGES, this, "No joysticks detected.");
      }
      else
      {
        Logging.log(Logging.LEVEL_MESSAGES, this,
                    numJoysticks + " joystick(s) detected.");
        numJoysticks = java.lang.Math.min(numJoysticks, Player);
        for (int k = 0; k <= numJoysticks - 1; k++)
        {
          Joystick[k] = SDLJoystick.joystickOpen(k);
        }
      }
    }
    catch (Exception Ex)
    {
      Logging.log(Logging.LEVEL_ERRORS, this, "Error while opening joystick!");
      numJoysticks = 0;
    }
  }
  

  /**
   * Liefert die Anzahl der benutzten Joysticks
   */
  public int getNumJoysticks() {
    return numJoysticks;
  }

  /**
   * Wertet je nach GameMode die Steuerung aus. Immer der erste Schritt.
   * @return boolean Neue Events?
   */
  public boolean update() {
    boolean Empty = KeyEvents.isEmpty() & Events.isEmpty();

    if (GameMode)
    {

      //Tastatur auswerten
      while (KeyEvents.isEmpty() == false)
      {
        KeyboardEvent Key = KeyEvents.firstElement();
        
        //we need to create a keyevent when cancel is pressed
        //TODO: remove KeyboardEvent-coment
        if (Key.keyCode == KeyboardEvent.KEY_ESCAPE)
          Events.add(Key);
        
        for (int i = 0; i <= Player - 1; i++)
        {
          for (int j = 0; j <= 6; j++)
          {
              if ( (ControllsKeyboard[i][j] != 0) &&
                  (Key.keyCode == ControllsKeyboard[i][j])) {
                if (Key.pressed) {
                  //Bei Aktionstasten: Wenn Taste gerade erst gedrückt wurde,
                  //neuen Keydown-Zeitpunkt speichern
                  if (j > 3)
                    if ( (Pressed[i] & (1 << j)) == 0)
                      pressedTime[i][j - 4] = GameEngine.getFrameTime(false);
                  //Taste wurde gedrückt
                  Pressed[i] = (1 << j) | Pressed[i];
                }
                else {
                  //Taste ist nicht gedrückt
                  Pressed[i] = (255 ^ (1 << j)) & Pressed[i];
                }
            }
          }
        }
        
        KeyEvents.remove(0);
      }

      //Joystick auswerten
      try
      {
        SDLJoystick.joystickUpdate(); //updates all joysticks
         
        for (int k = 0; k <= numJoysticks - 1; k++)
        {
        
          //Action Buttons
          for (int iBtn = 0; iBtn < 3; iBtn++) {
            if (Joystick[k].joystickNumButtons() >=
                ControllsJoystick[k][iBtn]) {
              if (ControllsJoystick[k][iBtn] != 0) {
                if (Joystick[k].joystickGetButton(ControllsJoystick[k][iBtn] - 1)) {
                  //Wenn Taste gerade erst gedrückt wurde,
                  //neuen KeyDown-Zeitpunkt speichern
                  if ( (JoyPressed[k] & (1 << (4 + iBtn))) == 0) {
                    pressedTime[k][iBtn] = GameEngine.getFrameTime(false);
                  }
                  JoyPressed[k] |= (1 << (4 + iBtn));
                }
                else {
                  JoyPressed[k] &= ~ (1 << (4 + iBtn));
                }
              }
            }
          }

          //Joystick Axis
          JoyPressed[k] &= ~ (0xF); //set first 4 bits back to 0
          if (Joystick[k].joystickGetAxis(0) < -16000)
            JoyPressed[k] |= left;
          else if (Joystick[k].joystickGetAxis(0) > 16000)
            JoyPressed[k] |= right;

          if (Joystick[k].joystickGetAxis(1) < -16000)
            JoyPressed[k] |= up;
          else if (Joystick[k].joystickGetAxis(1) > 16000)
            JoyPressed[k] |= down;

        }
      }
      catch (Exception Ex) {} 

    }
    else
    {
      //GameMode=false


      //Joystick übersetzen
      try {
        
        //read the active joystick
        
        int k = activeJoystickIndex;
        SDLJoystick.joystickUpdate(); //Joystick[k].joystickUpdate();

        //Action Buttons
        boolean joystickConfirm = Joystick[k].joystickGetButton(0);
        boolean joystickCancel = Joystick[k].joystickGetButton(1);
        //confirm
        if (joystickConfirm && joystickNotPressedConfirm)
        {
          Events.add(new KeyboardEvent(confirm, true, true));
          joystickNotPressedConfirm = false;
        }
        else if (!joystickConfirm && !joystickNotPressedConfirm)
        {
          Events.add(new KeyboardEvent(confirm, true, false));
          joystickNotPressedConfirm = true;
        }
        //cancel
        if (joystickCancel && joystickNotPressedCancel)
        {
          Events.add(new KeyboardEvent(cancel, true, true));
          joystickNotPressedCancel = false;
        }
        else if (!joystickCancel && !joystickNotPressedCancel)
        {
          Events.add(new KeyboardEvent(cancel, true, false));
          joystickNotPressedCancel = true;
        }
        
        
        //Joystick Axis
        if (Joystick[k].joystickGetAxis(0) < -16000)
        {
          Events.add(new KeyboardEvent(left, true, joystickNotPressedLeft));
          joystickNotPressedLeft = false;
          joystickNotPressedRight = true;
        }
        else {
          joystickNotPressedLeft = true;
          if (Joystick[k].joystickGetAxis(0) > 16000)
          {
            Events.add(new KeyboardEvent(right, true, joystickNotPressedRight));
            joystickNotPressedRight = false;
          }
          else joystickNotPressedRight = true;
        }

        if (Joystick[k].joystickGetAxis(1) < -16000)
        {
          Events.add(new KeyboardEvent(up, true, joystickNotPressedUp));
          joystickNotPressedUp = false;
          joystickNotPressedDown = true;
        }
        else {
          joystickNotPressedUp = true;
          if (Joystick[k].joystickGetAxis(1) > 16000) {
            Events.add(new KeyboardEvent(down, true, joystickNotPressedDown));
            joystickNotPressedDown = false;
          }
          else joystickNotPressedDown = true;
        }

      }
      catch (Exception Ex) {}

      
      //Keyboard - Hauptschleife
      
      /*
      boolean bconfirm = false;
      boolean bconfirmPressed = false;
      boolean bcancel = false;
      boolean bcancelPressed = false;
      */
      
      while (KeyEvents.isEmpty() == false)
      {
        KeyboardEvent Key = KeyEvents.firstElement();
        Events.add(Key);
        KeyEvents.remove(0);
      }
        
        /*
        if ( (Key.keyCode == KeyboardEvent.KEY_SPACE) ||
            (Key.keyCode == KeyboardEvent.KEY_RETURN))
        {
          bconfirm = true;
          bconfirmPressed = Key.pressed;
        }
        if (Key.keyCode == KeyboardEvent.KEY_ESCAPE)
        {
          bcancel = true;
          bcancelPressed = Key.pressed;
        }
        KeyEvents.remove(0);
      }

      //Keyboard - Neue Events
      if (bconfirm)
      {
        if (bconfirmPressed && keyboardNotPressedConfirm)
        {
          Events.add(new KeyboardEvent(confirm, true, true)); //new confirm-pressed event
          keyboardNotPressedConfirm = false; //key is pressed
        }
        else if (!bconfirmPressed)
        {
          Events.add(new KeyboardEvent(confirm, true, false)); //new confirm-released event
          keyboardNotPressedConfirm = true; //key is released
        }
      }
      if (bcancel)
      {
        if (bcancelPressed && keyboardNotPressedCancel)
        {
          Events.add(new KeyboardEvent(cancel, true, true)); //new cancel-pressed event
          keyboardNotPressedCancel = false; //key is pressed
        }
        else if (!bcancelPressed)
        {
          Events.add(new KeyboardEvent(cancel, true, false)); //new cancel-released event
          keyboardNotPressedCancel = true; //key is released
        }
      }
      */
    }
    

    return !Empty;
  }

  /**
   * Gibt an ob das Keyboard zur Steuerung einer Figur verwendet wird (true) oder
   * ob die Tastenevents pur mit den anderen Events gesendert werden sollen.
   * @param b boolean
   */
  public void setGameMode(boolean b) {
    for (int k = 0; k < Player; k++)
    {
      Pressed[k] = 0;
      JoyPressed[k] = 0;
    }
    GameMode = b;
  }

  /**
   * Checkt ob die entsprechenden Tasten (siehe Konstanten) beim entsprechenden
   * Spieler gedrückt wurden
   *
   * @param PlayerNr Tasten vom ersten oder zweiten Spieler überprüfen?
   * @param Keycode Welche Tasten checken?
   * @return boolean
   */
  public boolean isPressed(int PlayerNr, int Keycode) {
    if ( (PlayerNr < Player) && (PlayerNr >= 0))
      return ( (Keycode & (JoyPressed[PlayerNr] | Pressed[PlayerNr])) ==
              Keycode);

    return false;
  }

  /**
   * Löscht alle Events. (Bei Nichtabfrage wichtig, sonst wird die Liste ewig groß)
   */
  public void clearEvents() {
    Events.clear();
  }

  /**
   * Keine Events mehr da?
   * @return boolean Events noch da?
   */
  public boolean noEvents() {
    return Events.isEmpty();
  }

  /**
   * Weg mit dem Glied der Ketter, her mit dem nächsten!
   */
  public void killNextEvent() {
    Events.remove(0);
  }

  /**
   * Schnappt sich das erste Element der Eventliste
   * @param erase boolean Soll das Event sofort von der Liste gestrichen werden?
   * @return Object Gibt das Event zurück
   */
  public InputEvent getNextEvent(boolean erase) {
    InputEvent e = Events.firstElement();
    if (erase) Events.remove(0);
    return e;
  }

  /**
   * Irrlicht Anbindung
   */
  @Override public boolean OnEvent(SEvent event) {
    if (event.getEventType() == EEVENT_TYPE.EET_KEY_INPUT_EVENT)
    {
      //keyboard event
      int k = event.getKeyInputKey().swigValue(); //get the value of the enum element
      KeyboardEvent kbEvent = new KeyboardEvent(
        k, false, event.isKeyInputPressedDown());
      KeyEvents.add(kbEvent);
      return true;
    }
    else if (event.getEventType() == EEVENT_TYPE.EET_MOUSE_INPUT_EVENT)
    {
      EMOUSE_INPUT_EVENT mouseEvent = event.getMouseInputEvent();
      if (mouseEvent == EMOUSE_INPUT_EVENT.EMIE_MOUSE_MOVED)
      {
        //add mouse event
        Events.add(new MouseEvent(MouseEvent.EVENT_MOVED,
          event.getMouseInputX(), event.getMouseInputY()));
      }
      else if (mouseEvent == EMOUSE_INPUT_EVENT.EMIE_LMOUSE_PRESSED_DOWN)
      {
        //add downleft event
        Events.add(new MouseEvent(MouseEvent.EVENT_DOWNLEFT,
          event.getMouseInputX(), event.getMouseInputY()));
      }
      else if (mouseEvent == EMOUSE_INPUT_EVENT.EMIE_RMOUSE_PRESSED_DOWN)
      {
        //add downleft event
        Events.add(new MouseEvent(MouseEvent.EVENT_DOWNRIGHT,
          event.getMouseInputX(), event.getMouseInputY()));
      }
    }
    return false;
  }

  /**
   * @return <code>true</code>, if key is an arrow key (up, down, left, right)
   */
  public static boolean isArrowKey(int key) {
    return key == left || key == right || key == up || key == down;
  }

  /**
   * Returns the time duration (in ms) of a keypress (duration between
   * the keydown event and the current time).
   * 0: button1, 1: button2, 2: button3
   */
  public float getKeyPressDuration(int player, int buttonIndex) {
    if (pressedTime[player][buttonIndex] == 0f)
    {
      return 0f;
    }
    else
    {
      return GameEngine.getFrameTime(false) - pressedTime[player][buttonIndex];
    }
  }

  /**
   * Returns the combination of all pressed arrow keys
   * of player <code>PlayerNr</code>
   */
  public int getArrowKeys(int PlayerNr) {
    if ( (PlayerNr < Player) && (PlayerNr >= 0)) {
      return 0xF & (JoyPressed[PlayerNr] | Pressed[PlayerNr]);
    }
    return 0;
  }

  /**
   * Sets the keyboard keys for the given player. The given
   * array must contain seven key codes in this order:
   * left, up, right, down, button1, button2, button3
   */
  protected void setKeyboardKeys(int player, int[] keycodes) {
    for (int i = 0; i < 7; i++)
      ControllsKeyboard[player][i] = keycodes[i];
  }

  /**
   * Sets the joystick keys for the given player. The given
   * array must contain three key codes in this order:
   * button1, button2, button3
   */
  protected void setJoystickKeys(int player, int[] buttonindices) {
    for (int i = 0; i < 3; i++)
      ControllsJoystick[player][i] = buttonindices[i];
  }

  
  /**
   * Sets the active joystick index (0 or 1).
   * If there is no second joystick connected, this
   * method does nothing.
   */
  public void setActiveJoystick(int activeJoystickIndex)
  {
    if (numJoysticks > 1 && activeJoystickIndex == 1)
      this.activeJoystickIndex = 1;
    else
      this.activeJoystickIndex = 0;
  }

}

