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


public class KeyboardEvent
  extends InputEvent
{

  public int keyCode;
  public boolean bp2k6Key;
  public boolean pressed;

  
  /**
   * @param keyCode   The bp2k6 or Java/Irrlicht keycode
   * @param bp2k6Key  <code>true</code>, if bp2k6 key, otherwise <code>false</code>
   * @param pressed   <code>true</code>, if keydown, otherwise <code>false</code>
   */
  public KeyboardEvent(int keyCode, boolean bp2k6Key, boolean pressed)
  {
    this.keyCode = keyCode;
    this.bp2k6Key = bp2k6Key;
    this.pressed = pressed;
  }
  
  
  /**
   * Gets the gui key of this keyboard event
   * (may be arrow key or confirm/cancel), or
   * 0 if this is no gui key.
   */
  public int getGUIKey()
  {
    if (!bp2k6Key)
    {
      if (keyCode == KeyboardEvent.KEY_LEFT)
        return Input.left;
      else if (keyCode == KeyboardEvent.KEY_UP)
        return Input.up;
      else if (keyCode == KeyboardEvent.KEY_RIGHT)
        return Input.right;
      else if (keyCode == KeyboardEvent.KEY_DOWN)
        return Input.down;
      else if (keyCode == KeyboardEvent.KEY_SPACE ||
        keyCode == KeyboardEvent.KEY_RETURN)
        return Input.confirm;
      else if (keyCode == KeyboardEvent.KEY_ESCAPE)
        return Input.cancel;
      else
        return 0;
    }
    else
    {
      if (Input.isArrowKey(keyCode) ||
        keyCode == Input.confirm || keyCode == Input.cancel)
        return keyCode;
      else
        return 0;
    }
  }
  
  
  /**
   * OBSOLETE
   * Returns the Java KeyEvent.VK_-constant for the given
   * Irrlicht key code.
   *-/
  public static int getJavaVK(EKEY_CODE key)
  {
    int unknown = 0;
    if (key == EKEY_CODE.KEY_LBUTTON) // Left mouse button
      return unknown;
    else if (key == EKEY_CODE.KEY_RBUTTON) // Right mouse button  
      return unknown;
    else if (key == EKEY_CODE.KEY_CANCEL) // Control-break processing  
      return unknown;
    else if (key == EKEY_CODE.KEY_MBUTTON) // Middle mouse button (three-button mouse)
      return unknown;
    else if (key == EKEY_CODE.KEY_XBUTTON1) // Windows 2000/XP: X1 mouse button 
      return unknown;
    else if (key == EKEY_CODE.KEY_XBUTTON2) // Windows 2000/XP: X2 mouse button
      return unknown;
    else if (key == EKEY_CODE.KEY_BACK) // BACKSPACE key
      return KeyEvent.VK_BACK_SPACE;
    else if (key == EKEY_CODE.KEY_TAB) // TAB key
      return KeyEvent.VK_TAB;
    else if (key == EKEY_CODE.KEY_CLEAR) // CLEAR key
      return KeyEvent.VK_CLEAR;
    else if (key == EKEY_CODE.KEY_RETURN) // ENTER key  
      return KeyEvent.VK_ENTER;
    else if (key == EKEY_CODE.KEY_SHIFT) // SHIFT key 
      return KeyEvent.VK_SHIFT;
    else if (key == EKEY_CODE.KEY_CONTROL) // CTRL key  
      return KeyEvent.VK_CONTROL;
    else if (key == EKEY_CODE.KEY_MENU) // ALT key 
      return KeyEvent.VK_ALT;
    else if (key == EKEY_CODE.KEY_PAUSE) // PAUSE key  
      return KeyEvent.VK_PAUSE;
    else if (key == EKEY_CODE.KEY_CAPITAL) // CAPS LOCK key 
      return KeyEvent.VK_CAPS_LOCK;
    else if (key == EKEY_CODE.KEY_KANA) // IME Kana mode 
      return KeyEvent.VK_KANA;
    else if (key == EKEY_CODE.KEY_HANGUEL) // IME Hanguel mode (maintained for compatibility use KEY_HANGUL)
      return unknown;
    else if (key == EKEY_CODE.KEY_HANGUL) // IME Hangul mode 
      return unknown;
    else if (key == EKEY_CODE.KEY_JUNJA) // IME Junja mode 
      return unknown;
    else if (key == EKEY_CODE.KEY_FINAL) // IME final mode 
      return KeyEvent.VK_FINAL;
    else if (key == EKEY_CODE.KEY_HANJA) // IME Hanja mode 
      return unknown;
    else if (key == EKEY_CODE.KEY_KANJI) // IME Kanji mode 
      return KeyEvent.VK_KANJI;
    else if (key == EKEY_CODE.KEY_ESCAPE) // ESC key  
      return KeyEvent.VK_ESCAPE;
    else if (key == EKEY_CODE.KEY_CONVERT) // IME convert 
      return KeyEvent.VK_CONVERT;
    else if (key == EKEY_CODE.KEY_NONCONVERT) // IME nonconvert 
      return KeyEvent.VK_NONCONVERT;
    else if (key == EKEY_CODE.KEY_ACCEPT) // IME accept 
      return KeyEvent.VK_ACCEPT;
    else if (key == EKEY_CODE.KEY_MODECHANGE) // IME mode change request
      return KeyEvent.VK_MODECHANGE;
    else if (key == EKEY_CODE.KEY_SPACE) // SPACEBAR  
      return KeyEvent.VK_SPACE;
    else if (key == EKEY_CODE.KEY_PRIOR) // PAGE UP key  
      return KeyEvent.VK_PAGE_UP;
    else if (key == EKEY_CODE.KEY_NEXT) // PAGE DOWN key  
      return KeyEvent.VK_PAGE_DOWN;
    else if (key == EKEY_CODE.KEY_END) // END key  
      return KeyEvent.VK_END;
    else if (key == EKEY_CODE.KEY_HOME) // HOME key  
      return KeyEvent.VK_HOME;
    else if (key == EKEY_CODE.KEY_LEFT) // LEFT ARROW key  
      return KeyEvent.VK_LEFT;
    else if (key == EKEY_CODE.KEY_UP) // UP ARROW key  
      return KeyEvent.VK_UP;
    else if (key == EKEY_CODE.KEY_RIGHT) // RIGHT ARROW key  
      return KeyEvent.VK_RIGHT;
    else if (key == EKEY_CODE.KEY_DOWN) // DOWN ARROW key  
      return KeyEvent.VK_DOWN;
    else if (key == EKEY_CODE.KEY_SELECT) // SELECT key  
      return unknown;
    else if (key == EKEY_CODE.KEY_PRINT) // PRINT key
      return unknown;
    else if (key == EKEY_CODE.KEY_EXECUT) // EXECUTE key  
      return unknown;
    else if (key == EKEY_CODE.KEY_SNAPSHOT) // PRINT SCREEN key  
      return KeyEvent.VK_PRINTSCREEN;
    else if (key == EKEY_CODE.KEY_INSERT) // INS key  
      return KeyEvent.VK_INSERT;
    else if (key == EKEY_CODE.KEY_DELETE) // DEL key  
      return KeyEvent.VK_DELETE;
    else if (key == EKEY_CODE.KEY_HELP) // HELP key  
      return KeyEvent.VK_HELP;
    else if (key == EKEY_CODE.KEY_KEY_0) // 0 key  
      return KeyEvent.VK_0;
    else if (key == EKEY_CODE.KEY_KEY_1) // 1 key  
      return KeyEvent.VK_1;
    else if (key == EKEY_CODE.KEY_KEY_2) // 2 key  
      return KeyEvent.VK_2;
    else if (key == EKEY_CODE.KEY_KEY_3) // 3 key  
      return KeyEvent.VK_3;
    else if (key == EKEY_CODE.KEY_KEY_4) // 4 key  
      return KeyEvent.VK_4;
    else if (key == EKEY_CODE.KEY_KEY_5) // 5 key  
      return KeyEvent.VK_5;
    else if (key == EKEY_CODE.KEY_KEY_6) // 6 key  
      return KeyEvent.VK_6;
    else if (key == EKEY_CODE.KEY_KEY_7) // 7 key  
      return KeyEvent.VK_7;
    else if (key == EKEY_CODE.KEY_KEY_8) // 8 key  
      return KeyEvent.VK_8;
    else if (key == EKEY_CODE.KEY_KEY_9) // 9 key  
      return KeyEvent.VK_9;
    else if (key == EKEY_CODE.KEY_KEY_A) // A key  
      return KeyEvent.VK_A;
    else if (key == EKEY_CODE.KEY_KEY_B) // B key  
      return KeyEvent.VK_B;
    else if (key == EKEY_CODE.KEY_KEY_C) // C key  
      return KeyEvent.VK_C;
    else if (key == EKEY_CODE.KEY_KEY_D) // D key  
      return KeyEvent.VK_D;
    else if (key == EKEY_CODE.KEY_KEY_E) // E key  
      return KeyEvent.VK_E;
    else if (key == EKEY_CODE.KEY_KEY_F) // F key  
      return KeyEvent.VK_F;
    else if (key == EKEY_CODE.KEY_KEY_G) // G key  
      return KeyEvent.VK_G;
    else if (key == EKEY_CODE.KEY_KEY_H) // H key  
      return KeyEvent.VK_H;
    else if (key == EKEY_CODE.KEY_KEY_I) // I key  
      return KeyEvent.VK_I;
    else if (key == EKEY_CODE.KEY_KEY_J) // J key  
      return KeyEvent.VK_J;
    else if (key == EKEY_CODE.KEY_KEY_K) // K key  
      return KeyEvent.VK_K;
    else if (key == EKEY_CODE.KEY_KEY_L) // L key  
      return KeyEvent.VK_L;
    else if (key == EKEY_CODE.KEY_KEY_M) // M key  
      return KeyEvent.VK_M;
    else if (key == EKEY_CODE.KEY_KEY_N) // N key  
      return KeyEvent.VK_N;
    else if (key == EKEY_CODE.KEY_KEY_O) // O key  
      return KeyEvent.VK_O;
    else if (key == EKEY_CODE.KEY_KEY_P) // P key  
      return KeyEvent.VK_P;
    else if (key == EKEY_CODE.KEY_KEY_Q) // Q key  
      return KeyEvent.VK_Q;
    else if (key == EKEY_CODE.KEY_KEY_R) // R key  
      return KeyEvent.VK_R;
    else if (key == EKEY_CODE.KEY_KEY_S) // S key  
      return KeyEvent.VK_S;
    else if (key == EKEY_CODE.KEY_KEY_T) // T key  
      return KeyEvent.VK_T;
    else if (key == EKEY_CODE.KEY_KEY_U) // U key  
      return KeyEvent.VK_U;
    else if (key == EKEY_CODE.KEY_KEY_V) // V key  
      return KeyEvent.VK_V;
    else if (key == EKEY_CODE.KEY_KEY_W) // W key  
      return KeyEvent.VK_W;
    else if (key == EKEY_CODE.KEY_KEY_X) // X key  
      return KeyEvent.VK_X;
    else if (key == EKEY_CODE.KEY_KEY_Y) // Y key  
      return KeyEvent.VK_Y;
    else if (key == EKEY_CODE.KEY_KEY_Z) // Z key  
      return KeyEvent.VK_Z;
    else if (key == EKEY_CODE.KEY_LWIN) // Left Windows key (Microsoft® Natural® keyboard)  
      return KeyEvent.VK_WINDOWS;
    else if (key == EKEY_CODE.KEY_RWIN) // Right Windows key (Natural keyboard)  
      return KeyEvent.VK_WINDOWS;
    else if (key == EKEY_CODE.KEY_APPS) //Applications key (Natural keyboard)  
      return unknown;
    else if (key == EKEY_CODE.KEY_SLEEP) // Computer Sleep key 
      return unknown;
    else if (key == EKEY_CODE.KEY_NUMPAD0) // Numeric keypad 0 key  
      return KeyEvent.VK_NUMPAD0;
    else if (key == EKEY_CODE.KEY_NUMPAD1) // Numeric keypad 1 key  
      return KeyEvent.VK_NUMPAD1;
    else if (key == EKEY_CODE.KEY_NUMPAD2) // Numeric keypad 2 key  
      return KeyEvent.VK_NUMPAD2;
    else if (key == EKEY_CODE.KEY_NUMPAD3) // Numeric keypad 3 key  
      return KeyEvent.VK_NUMPAD3;
    else if (key == EKEY_CODE.KEY_NUMPAD4) // Numeric keypad 4 key  
      return KeyEvent.VK_NUMPAD4;
    else if (key == EKEY_CODE.KEY_NUMPAD5) // Numeric keypad 5 key  
      return KeyEvent.VK_NUMPAD5;
    else if (key == EKEY_CODE.KEY_NUMPAD6) // Numeric keypad 6 key  
      return KeyEvent.VK_NUMPAD6;
    else if (key == EKEY_CODE.KEY_NUMPAD7) // Numeric keypad 7 key  
      return KeyEvent.VK_NUMPAD7;
    else if (key == EKEY_CODE.KEY_NUMPAD8) // Numeric keypad 8 key  
      return KeyEvent.VK_NUMPAD8;
    else if (key == EKEY_CODE.KEY_NUMPAD9) // Numeric keypad 9 key  
      return KeyEvent.VK_NUMPAD9;
    else if (key == EKEY_CODE.KEY_MULTIPLY) // Multiply key  
      return KeyEvent.VK_MULTIPLY;
    else if (key == EKEY_CODE.KEY_ADD) // Add key  
      return KeyEvent.VK_ADD;
    else if (key == EKEY_CODE.KEY_SEPARATOR) // Separator key  
      return KeyEvent.VK_SEPARATOR;
    else if (key == EKEY_CODE.KEY_SUBTRACT) // Subtract key  
      return KeyEvent.VK_SUBTRACT;
    else if (key == EKEY_CODE.KEY_DECIMAL) // Decimal key  
      return KeyEvent.VK_DECIMAL;
    else if (key == EKEY_CODE.KEY_DIVIDE) // Divide key  
      return KeyEvent.VK_DIVIDE;
    else if (key == EKEY_CODE.KEY_F1) // F1 key  
      return KeyEvent.VK_F1;
    else if (key == EKEY_CODE.KEY_F2) // F2 key  
      return KeyEvent.VK_F2;
    else if (key == EKEY_CODE.KEY_F3) // F3 key  
      return KeyEvent.VK_F3;
    else if (key == EKEY_CODE.KEY_F4) // F4 key  
      return KeyEvent.VK_F4;
    else if (key == EKEY_CODE.KEY_F5) // F5 key  
      return KeyEvent.VK_F5;
    else if (key == EKEY_CODE.KEY_F6) // F6 key  
      return KeyEvent.VK_F6;
    else if (key == EKEY_CODE.KEY_F7) // F7 key  
      return KeyEvent.VK_F7;
    else if (key == EKEY_CODE.KEY_F8) // F8 key  
      return KeyEvent.VK_F8;
    else if (key == EKEY_CODE.KEY_F9) // F9 key  
      return KeyEvent.VK_F9;
    else if (key == EKEY_CODE.KEY_F10) // F10 key  
      return KeyEvent.VK_F10;
    else if (key == EKEY_CODE.KEY_F11) // F11 key  
      return KeyEvent.VK_F11;
    else if (key == EKEY_CODE.KEY_F12) // F12 key  
      return KeyEvent.VK_F12;
    else if (key == EKEY_CODE.KEY_F13) // F13 key  
      return KeyEvent.VK_F13;
    else if (key == EKEY_CODE.KEY_F14) // F14 key  
      return KeyEvent.VK_F14;
    else if (key == EKEY_CODE.KEY_F15) // F15 key  
      return KeyEvent.VK_F15;
    else if (key == EKEY_CODE.KEY_F16) // F16 key  
      return KeyEvent.VK_F16;
    else if (key == EKEY_CODE.KEY_F17) // F17 key  
      return KeyEvent.VK_F17;
    else if (key == EKEY_CODE.KEY_F18) // F18 key  
      return KeyEvent.VK_F18;
    else if (key == EKEY_CODE.KEY_F19) // F19 key  
      return KeyEvent.VK_F19;
    else if (key == EKEY_CODE.KEY_F20) // F20 key  
      return KeyEvent.VK_F20;
    else if (key == EKEY_CODE.KEY_F21) // F21 key  
      return KeyEvent.VK_F21;
    else if (key == EKEY_CODE.KEY_F22) // F22 key  
      return KeyEvent.VK_F22;
    else if (key == EKEY_CODE.KEY_F23) // F23 key  
      return KeyEvent.VK_F23;
    else if (key == EKEY_CODE.KEY_F24) // F24 key  
      return KeyEvent.VK_F24;
    else if (key == EKEY_CODE.KEY_NUMLOCK) // NUM LOCK key  
      return KeyEvent.VK_NUM_LOCK;
    else if (key == EKEY_CODE.KEY_SCROLL) // SCROLL LOCK key  
      return KeyEvent.VK_SCROLL_LOCK;
    else if (key == EKEY_CODE.KEY_LSHIFT) // Left SHIFT key 
      return KeyEvent.VK_SHIFT;
    else if (key == EKEY_CODE.KEY_RSHIFT) // Right SHIFT key 
      return KeyEvent.VK_SHIFT;
    else if (key == EKEY_CODE.KEY_LCONTROL) // Left CONTROL key 
      return KeyEvent.VK_CONTROL;
    else if (key == EKEY_CODE.KEY_RCONTROL) // Right CONTROL key 
      return KeyEvent.VK_CONTROL;
    else if (key == EKEY_CODE.KEY_LMENU) // Left MENU key 
      return unknown;
    else if (key == EKEY_CODE.KEY_RMENU) // Right MENU key 
      return unknown;
    else if (key == EKEY_CODE.KEY_PLUS) // Plus Key   (+)
      return KeyEvent.VK_PLUS;
    else if (key == EKEY_CODE.KEY_COMMA) // Comma Key  (,)
      return KeyEvent.VK_COMMA;
    else if (key == EKEY_CODE.KEY_MINUS) // Minus Key  (-)
      return KeyEvent.VK_MINUS;
    else if (key == EKEY_CODE.KEY_PERIOD) // Period Key (.)
      return KeyEvent.VK_PERIOD;
    else if (key == EKEY_CODE.KEY_ATTN) // Attn key 
      return unknown;
    else if (key == EKEY_CODE.KEY_CRSEL) // CrSel key 
      return unknown;
    else if (key == EKEY_CODE.KEY_EXSEL) // ExSel key 
      return unknown;
    else if (key == EKEY_CODE.KEY_EREOF) // Erase EOF key 
      return unknown;
    else if (key == EKEY_CODE.KEY_PLAY) // Play key 
      return unknown;
    else if (key == EKEY_CODE.KEY_ZOOM) // Zoom key 
      return unknown;
    else if (key == EKEY_CODE.KEY_PA1) // PA1 key 
      return unknown;
    else if (key == EKEY_CODE.KEY_OEM_CLEAR) // Clear key 
      return unknown;
    else
      return 0;
  } */
  
  
  public static final int KEY_LBUTTON = 0x01; // Left mouse button 
  public static final int KEY_RBUTTON = 0x02; // Right mouse button 
  public static final int KEY_CANCEL = 0x03; // Control-break processing 
  public static final int KEY_MBUTTON = 0x04; // Middle mouse button (three-button mouse) 
  public static final int KEY_XBUTTON1 = 0x05; // Windows 2000/XP: X1 mouse button 
  public static final int KEY_XBUTTON2 = 0x06; // Windows 2000/XP: X2 mouse button 
  public static final int KEY_BACK = 0x08; // BACKSPACE key 
  public static final int KEY_TAB = 0x09; // TAB key 
  public static final int KEY_CLEAR = 0x0C; // CLEAR key 
  public static final int KEY_RETURN = 0x0D; // ENTER key 
  public static final int KEY_SHIFT = 0x10; // SHIFT key 
  public static final int KEY_CONTROL = 0x11; // CTRL key 
  public static final int KEY_MENU = 0x12; // ALT key 
  public static final int KEY_PAUSE = 0x13; // PAUSE key 
  public static final int KEY_CAPITAL = 0x14; // CAPS LOCK key 
  public static final int KEY_KANA = 0x15; // IME Kana mode 
  public static final int KEY_HANGUEL = 0x15; // IME Hanguel mode (maintained for compatibility use public static final int KEY_HANGUL) 
  public static final int KEY_HANGUL = 0x15; // IME Hangul mode 
  public static final int KEY_JUNJA = 0x17; // IME Junja mode 
  public static final int KEY_FINAL = 0x18; // IME final mode 
  public static final int KEY_HANJA = 0x19; // IME Hanja mode 
  public static final int KEY_KANJI = 0x19; // IME Kanji mode 
  public static final int KEY_ESCAPE = 0x1B; // ESC key 
  public static final int KEY_CONVERT = 0x1C; // IME convert 
  public static final int KEY_NONCONVERT = 0x1D; // IME nonconvert 
  public static final int KEY_ACCEPT = 0x1E; // IME accept 
  public static final int KEY_MODECHANGE = 0x1F; // IME mode change request 
  public static final int KEY_SPACE = 0x20; // SPACEBAR 
  public static final int KEY_PRIOR = 0x21; // PAGE UP key 
  public static final int KEY_NEXT = 0x22; // PAGE DOWN key 
  public static final int KEY_END = 0x23; // END key 
  public static final int KEY_HOME = 0x24; // HOME key 
  public static final int KEY_LEFT = 0x25; // LEFT ARROW key 
  public static final int KEY_UP = 0x26; // UP ARROW key 
  public static final int KEY_RIGHT = 0x27; // RIGHT ARROW key 
  public static final int KEY_DOWN = 0x28; // DOWN ARROW key 
  public static final int KEY_SELECT = 0x29; // SELECT key 
  public static final int KEY_PRINT = 0x2A; // PRINT key
  public static final int KEY_EXECUT = 0x2B; // EXECUTE key 
  public static final int KEY_SNAPSHOT = 0x2C; // PRINT SCREEN key 
  public static final int KEY_INSERT = 0x2D; // INS key 
  public static final int KEY_DELETE = 0x2E; // DEL key 
  public static final int KEY_HELP = 0x2F; // HELP key 
  public static final int KEY_KEY_0 = 0x30; // 0 key 
  public static final int KEY_KEY_1 = 0x31; // 1 key 
  public static final int KEY_KEY_2 = 0x32; // 2 key 
  public static final int KEY_KEY_3 = 0x33; // 3 key 
  public static final int KEY_KEY_4 = 0x34; // 4 key 
  public static final int KEY_KEY_5 = 0x35; // 5 key 
  public static final int KEY_KEY_6 = 0x36; // 6 key 
  public static final int KEY_KEY_7 = 0x37; // 7 key 
  public static final int KEY_KEY_8 = 0x38; // 8 key 
  public static final int KEY_KEY_9 = 0x39; // 9 key 
  public static final int KEY_KEY_A = 0x41; // A key 
  public static final int KEY_KEY_B = 0x42; // B key 
  public static final int KEY_KEY_C = 0x43; // C key 
  public static final int KEY_KEY_D = 0x44; // D key 
  public static final int KEY_KEY_E = 0x45; // E key 
  public static final int KEY_KEY_F = 0x46; // F key 
  public static final int KEY_KEY_G = 0x47; // G key 
  public static final int KEY_KEY_H = 0x48; // H key 
  public static final int KEY_KEY_I = 0x49; // I key 
  public static final int KEY_KEY_J = 0x4A; // J key 
  public static final int KEY_KEY_K = 0x4B; // K key 
  public static final int KEY_KEY_L = 0x4C; // L key 
  public static final int KEY_KEY_M = 0x4D; // M key 
  public static final int KEY_KEY_N = 0x4E; // N key 
  public static final int KEY_KEY_O = 0x4F; // O key 
  public static final int KEY_KEY_P = 0x50; // P key 
  public static final int KEY_KEY_Q = 0x51; // Q key 
  public static final int KEY_KEY_R = 0x52; // R key 
  public static final int KEY_KEY_S = 0x53; // S key 
  public static final int KEY_KEY_T = 0x54; // T key 
  public static final int KEY_KEY_U = 0x55; // U key 
  public static final int KEY_KEY_V = 0x56; // V key 
  public static final int KEY_KEY_W = 0x57; // W key 
  public static final int KEY_KEY_X = 0x58; // X key 
  public static final int KEY_KEY_Y = 0x59; // Y key 
  public static final int KEY_KEY_Z = 0x5A; // Z key 
  public static final int KEY_LWIN = 0x5B; // Left Windows key (Microsoft® Natural® keyboard) 
  public static final int KEY_RWIN = 0x5C; // Right Windows key (Natural keyboard) 
  public static final int KEY_APPS = 0x5D; //Applications key (Natural keyboard) 
  public static final int KEY_SLEEP = 0x5F; // Computer Sleep key 
  public static final int KEY_NUMPAD0 = 0x60; // Numeric keypad 0 key 
  public static final int KEY_NUMPAD1 = 0x61; // Numeric keypad 1 key 
  public static final int KEY_NUMPAD2 = 0x62; // Numeric keypad 2 key 
  public static final int KEY_NUMPAD3 = 0x63; // Numeric keypad 3 key 
  public static final int KEY_NUMPAD4 = 0x64; // Numeric keypad 4 key 
  public static final int KEY_NUMPAD5 = 0x65; // Numeric keypad 5 key 
  public static final int KEY_NUMPAD6 = 0x66; // Numeric keypad 6 key 
  public static final int KEY_NUMPAD7 = 0x67; // Numeric keypad 7 key 
  public static final int KEY_NUMPAD8 = 0x68; // Numeric keypad 8 key 
  public static final int KEY_NUMPAD9 = 0x69; // Numeric keypad 9 key 
  public static final int KEY_MULTIPLY = 0x6A; // Multiply key 
  public static final int KEY_ADD = 0x6B; // Add key 
  public static final int KEY_SEPARATOR = 0x6C; // Separator key 
  public static final int KEY_SUBTRACT = 0x6D; // Subtract key 
  public static final int KEY_DECIMAL = 0x6E; // Decimal key 
  public static final int KEY_DIVIDE = 0x6F; // Divide key 
  public static final int KEY_F1 = 0x70; // F1 key 
  public static final int KEY_F2 = 0x71; // F2 key 
  public static final int KEY_F3 = 0x72; // F3 key 
  public static final int KEY_F4 = 0x73; // F4 key 
  public static final int KEY_F5 = 0x74; // F5 key 
  public static final int KEY_F6 = 0x75; // F6 key 
  public static final int KEY_F7 = 0x76; // F7 key 
  public static final int KEY_F8 = 0x77; // F8 key 
  public static final int KEY_F9 = 0x78; // F9 key 
  public static final int KEY_F10 = 0x79; // F10 key 
  public static final int KEY_F11 = 0x7A; // F11 key 
  public static final int KEY_F12 = 0x7B; // F12 key 
  public static final int KEY_F13 = 0x7C; // F13 key 
  public static final int KEY_F14 = 0x7D; // F14 key 
  public static final int KEY_F15 = 0x7E; // F15 key 
  public static final int KEY_F16 = 0x7F; // F16 key 
  public static final int KEY_F17 = 0x80; // F17 key 
  public static final int KEY_F18 = 0x81; // F18 key 
  public static final int KEY_F19 = 0x82; // F19 key 
  public static final int KEY_F20 = 0x83; // F20 key 
  public static final int KEY_F21 = 0x84; // F21 key 
  public static final int KEY_F22 = 0x85; // F22 key 
  public static final int KEY_F23 = 0x86; // F23 key 
  public static final int KEY_F24 = 0x87; // F24 key 
  public static final int KEY_NUMLOCK = 0x90; // NUM LOCK key 
  public static final int KEY_SCROLL = 0x91; // SCROLL LOCK key 
  public static final int KEY_LSHIFT = 0xA0; // Left SHIFT key 
  public static final int KEY_RSHIFT = 0xA1; // Right SHIFT key 
  public static final int KEY_LCONTROL = 0xA2; // Left CONTROL key 
  public static final int KEY_RCONTROL = 0xA3; // Right CONTROL key 
  public static final int KEY_LMENU = 0xA4; // Left MENU key 
  public static final int KEY_RMENU = 0xA5; // Right MENU key 
  public static final int KEY_COMMA = 0xBC; // Comma Key (;)
  public static final int KEY_PLUS = 0xBB; // Plus Key (+)
  public static final int KEY_MINUS = 0xBD; // Minus Key (-)
  public static final int KEY_PERIOD = 0xBE; // Period Key (.)
  public static final int KEY_ATTN = 0xF6; // Attn key 
  public static final int KEY_CRSEL = 0xF7; // CrSel key 
  public static final int KEY_EXSEL = 0xF8; // ExSel key 
  public static final int KEY_EREOF = 0xF9; // Erase EOF key 
  public static final int KEY_PLAY = 0xFA; // Play key 
  public static final int KEY_ZOOM = 0xFB; // Zoom key 
  public static final int KEY_PA1 = 0xFD; // PA1 key 
  public static final int KEY_OEM_CLEAR = 0xFE; // Clear key 

}
