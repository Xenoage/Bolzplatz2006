/*
 * Copyright (c) 2002-2004 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.input;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

/**
 * $Id: Keyboard.java,v 1.61 2005/05/05 10:16:46 elias_naur Exp $
 * <br>
 * A raw Keyboard interface. This can be used to poll the current state of the
 * keys, or read all the keyboard presses / releases since the last read.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 1.61 $
 */
public class Keyboard {
	/**
	 * The special character meaning that no
	 * character was translated for the event.
	 */
	public static final char CHAR_NONE          = '\0';

	/**
	 * The special keycode meaning that only the
	 * translated character is valid.
	 */
	public static final int KEY_NONE            = 0x00;

	public static final int KEY_ESCAPE          = 0x01;
	public static final int KEY_1               = 0x02;
	public static final int KEY_2               = 0x03;
	public static final int KEY_3               = 0x04;
	public static final int KEY_4               = 0x05;
	public static final int KEY_5               = 0x06;
	public static final int KEY_6               = 0x07;
	public static final int KEY_7               = 0x08;
	public static final int KEY_8               = 0x09;
	public static final int KEY_9               = 0x0A;
	public static final int KEY_0               = 0x0B;
	public static final int KEY_MINUS           = 0x0C; /* - on main keyboard */
	public static final int KEY_EQUALS          = 0x0D;
	public static final int KEY_BACK            = 0x0E; /* backspace */
	public static final int KEY_TAB             = 0x0F;
	public static final int KEY_Q               = 0x10;
	public static final int KEY_W               = 0x11;
	public static final int KEY_E               = 0x12;
	public static final int KEY_R               = 0x13;
	public static final int KEY_T               = 0x14;
	public static final int KEY_Y               = 0x15;
	public static final int KEY_U               = 0x16;
	public static final int KEY_I               = 0x17;
	public static final int KEY_O               = 0x18;
	public static final int KEY_P               = 0x19;
	public static final int KEY_LBRACKET        = 0x1A;
	public static final int KEY_RBRACKET        = 0x1B;
	public static final int KEY_RETURN          = 0x1C; /* Enter on main keyboard */
	public static final int KEY_LCONTROL        = 0x1D;
	public static final int KEY_A               = 0x1E;
	public static final int KEY_S               = 0x1F;
	public static final int KEY_D               = 0x20;
	public static final int KEY_F               = 0x21;
	public static final int KEY_G               = 0x22;
	public static final int KEY_H               = 0x23;
	public static final int KEY_J               = 0x24;
	public static final int KEY_K               = 0x25;
	public static final int KEY_L               = 0x26;
	public static final int KEY_SEMICOLON       = 0x27;
	public static final int KEY_APOSTROPHE      = 0x28;
	public static final int KEY_GRAVE           = 0x29; /* accent grave */
	public static final int KEY_LSHIFT          = 0x2A;
	public static final int KEY_BACKSLASH       = 0x2B;
	public static final int KEY_Z               = 0x2C;
	public static final int KEY_X               = 0x2D;
	public static final int KEY_C               = 0x2E;
	public static final int KEY_V               = 0x2F;
	public static final int KEY_B               = 0x30;
	public static final int KEY_N               = 0x31;
	public static final int KEY_M               = 0x32;
	public static final int KEY_COMMA           = 0x33;
	public static final int KEY_PERIOD          = 0x34; /* . on main keyboard */
	public static final int KEY_SLASH           = 0x35; /* / on main keyboard */
	public static final int KEY_RSHIFT          = 0x36;
	public static final int KEY_MULTIPLY        = 0x37; /* * on numeric keypad */
	public static final int KEY_LMENU           = 0x38; /* left Alt */
	public static final int KEY_SPACE           = 0x39;
	public static final int KEY_CAPITAL         = 0x3A;
	public static final int KEY_F1              = 0x3B;
	public static final int KEY_F2              = 0x3C;
	public static final int KEY_F3              = 0x3D;
	public static final int KEY_F4              = 0x3E;
	public static final int KEY_F5              = 0x3F;
	public static final int KEY_F6              = 0x40;
	public static final int KEY_F7              = 0x41;
	public static final int KEY_F8              = 0x42;
	public static final int KEY_F9              = 0x43;
	public static final int KEY_F10             = 0x44;
	public static final int KEY_NUMLOCK         = 0x45;
	public static final int KEY_SCROLL          = 0x46; /* Scroll Lock */
	public static final int KEY_NUMPAD7         = 0x47;
	public static final int KEY_NUMPAD8         = 0x48;
	public static final int KEY_NUMPAD9         = 0x49;
	public static final int KEY_SUBTRACT        = 0x4A; /* - on numeric keypad */
	public static final int KEY_NUMPAD4         = 0x4B;
	public static final int KEY_NUMPAD5         = 0x4C;
	public static final int KEY_NUMPAD6         = 0x4D;
	public static final int KEY_ADD             = 0x4E; /* + on numeric keypad */
	public static final int KEY_NUMPAD1         = 0x4F;
	public static final int KEY_NUMPAD2         = 0x50;
	public static final int KEY_NUMPAD3         = 0x51;
	public static final int KEY_NUMPAD0         = 0x52;
	public static final int KEY_DECIMAL         = 0x53; /* . on numeric keypad */
	public static final int KEY_F11             = 0x57;
	public static final int KEY_F12             = 0x58;
	public static final int KEY_F13             = 0x64; /*                     (NEC PC98) */
	public static final int KEY_F14             = 0x65; /*                     (NEC PC98) */
	public static final int KEY_F15             = 0x66; /*                     (NEC PC98) */
	public static final int KEY_KANA            = 0x70; /* (Japanese keyboard)            */
	public static final int KEY_CONVERT         = 0x79; /* (Japanese keyboard)            */
	public static final int KEY_NOCONVERT       = 0x7B; /* (Japanese keyboard)            */
	public static final int KEY_YEN             = 0x7D; /* (Japanese keyboard)            */
	public static final int KEY_NUMPADEQUALS    = 0x8D; /* = on numeric keypad (NEC PC98) */
	public static final int KEY_CIRCUMFLEX      = 0x90; /* (Japanese keyboard)            */
	public static final int KEY_AT              = 0x91; /*                     (NEC PC98) */
	public static final int KEY_COLON           = 0x92; /*                     (NEC PC98) */
	public static final int KEY_UNDERLINE       = 0x93; /*                     (NEC PC98) */
	public static final int KEY_KANJI           = 0x94; /* (Japanese keyboard)            */
	public static final int KEY_STOP            = 0x95; /*                     (NEC PC98) */
	public static final int KEY_AX              = 0x96; /*                     (Japan AX) */
	public static final int KEY_UNLABELED       = 0x97; /*                        (J3100) */
	public static final int KEY_NUMPADENTER     = 0x9C; /* Enter on numeric keypad */
	public static final int KEY_RCONTROL        = 0x9D;
	public static final int KEY_NUMPADCOMMA     = 0xB3; /* , on numeric keypad (NEC PC98) */
	public static final int KEY_DIVIDE          = 0xB5; /* / on numeric keypad */
	public static final int KEY_SYSRQ           = 0xB7;
	public static final int KEY_RMENU           = 0xB8; /* right Alt */
	public static final int KEY_PAUSE           = 0xC5; /* Pause */
	public static final int KEY_HOME            = 0xC7; /* Home on arrow keypad */
	public static final int KEY_UP              = 0xC8; /* UpArrow on arrow keypad */
	public static final int KEY_PRIOR           = 0xC9; /* PgUp on arrow keypad */
	public static final int KEY_LEFT            = 0xCB; /* LeftArrow on arrow keypad */
	public static final int KEY_RIGHT           = 0xCD; /* RightArrow on arrow keypad */
	public static final int KEY_END             = 0xCF; /* End on arrow keypad */
	public static final int KEY_DOWN            = 0xD0; /* DownArrow on arrow keypad */
	public static final int KEY_NEXT            = 0xD1; /* PgDn on arrow keypad */
	public static final int KEY_INSERT          = 0xD2; /* Insert on arrow keypad */
	public static final int KEY_DELETE          = 0xD3; /* Delete on arrow keypad */
	public static final int KEY_LWIN            = 0xDB; /* Left Windows key */
	public static final int KEY_RWIN            = 0xDC; /* Right Windows key */
	public static final int KEY_APPS            = 0xDD; /* AppMenu key */
	public static final int KEY_POWER           = 0xDE;
	public static final int KEY_SLEEP           = 0xDF;

	public static final int STATE_ON							= 0;
	public static final int STATE_OFF						 = 1;
	public static final int STATE_UNKNOWN				 = 2;

	public static final int KEYBOARD_SIZE = 256;

	/** Buffer size in events */
	private static final int BUFFER_SIZE = 50;
	/** Event size in elements */
	private static final int EVENT_SIZE = 3;

	/** Key names */
	private static final String[] keyName = new String[255];
	private static final Map keyMap = new HashMap(253);
	private static int counter;

	static {
		// Use reflection to find out key names
		Field[] field = Keyboard.class.getFields();
		try {
			for (int i = 0; i < field.length; i++) {
				if (Modifier.isStatic(field[i].getModifiers())
					&& Modifier.isPublic(field[i].getModifiers())
					&& Modifier.isFinal(field[i].getModifiers())
					&& field[i].getType().equals(int.class)
					&& field[i].getName().startsWith("KEY_")) {

					int key = field[i].getInt(null);
					String name = field[i].getName().substring(4);
					keyName[key] = name;
					keyMap.put(name, new Integer(key));
					counter ++;
				}

			}
		} catch (Exception e) {
		}

	}

	/** The number of keys supported */
	private static final int keyCount = counter;

	/** Has the keyboard been created? */
	private static boolean created;

	/** The keys status from the last poll */
	private static final ByteBuffer keyDownBuffer = BufferUtils.createByteBuffer(KEYBOARD_SIZE);

	/**
	 * The key events from the last read: a sequence of pairs of key number,
	 * followed by state. The state is followed by
	 * a 2 byte java char representing the translated character.
	 */
	private static IntBuffer readBuffer;

	/** The current keyboard character being examined */
	private static char eventCharacter;

	/** The current keyboard event key being examined */
	private static int eventKey;

	/** The current state of the key being examined in the event queue */
	private static boolean eventState;

	/** One time initialization */
	private static boolean initialized;

	/**
	 * Keyboard cannot be constructed.
	 */
	private Keyboard() {
	}

	/**
	 * Static initialization
	 */
	private static void initialize() {
		if (initialized)
			return;
		Sys.initialize();
		initialized = true;
	}

	/**
	 * "Create" the keyboard. The display must first have been created. The
	 * reason for this is so the keyboard has a window to "focus" in.
	 *
	 * @throws LWJGLException if the keyboard could not be created for any reason
	 */
	public static void create() throws LWJGLException {
		if (!Display.isCreated())
			throw new IllegalStateException("Display must be created before you can create Keyboard");
		if (!initialized)
			initialize();
		if (created)
			return;
		Display.getImplementation().createKeyboard();
		created = true;
		readBuffer = BufferUtils.createIntBuffer(EVENT_SIZE*BUFFER_SIZE);
		reset();
	}

	private static void reset() {
		readBuffer.limit(0);
		for (int i = 0; i < keyDownBuffer.remaining(); i++)
			keyDownBuffer.put(i, (byte)0);
		eventCharacter = 0;
		eventKey = 0;
		eventState = false;
	}

	/**
	 * @return true if the keyboard has been created
	 */
	public static boolean isCreated() {
		return created;
	}

	/**
	 * "Destroy" the keyboard
	 */
	public static void destroy() {
		if (!created)
			return;
		created = false;
		Display.getImplementation().destroyKeyboard();
		reset();
	}

	/**
	 * Polls the keyboard for its current state. Access the polled values using the
	 * <code>isKeyDown</code> method.
	 * By using this method, it is possible to "miss" keyboard keys if you don't
	 * poll fast enough. 
	 *
	 * To use buffered values, you have to call <code>next</code> for each event you
	 * want to read. You can query which key caused the event by using
	 * <code>getEventKey</code>. To get the state of that key, for that event, use
	 * <code>getEventKeyState</code> - finally use <code>getEventCharacter</code> to get the
	 * character for that event.
	 *
	 * @see org.lwjgl.input.Keyboard#isKeyDown(int key)
	 * @see org.lwjgl.input.Keyboard#isStateKeySet(int key)
	 * @see org.lwjgl.input.Keyboard#next()
	 * @see org.lwjgl.input.Keyboard#getEventKey()
	 * @see org.lwjgl.input.Keyboard#getEventKeyState()
	 * @see org.lwjgl.input.Keyboard#getEventCharacter()
	 */
	public static void poll() {
		if (!created)
			throw new IllegalStateException("Keyboard must be created before you can poll the device");
		Display.getImplementation().pollKeyboard(keyDownBuffer);
		read();
	}

	private static void read() {
		readBuffer.compact();
		int numEvents = Display.getImplementation().readKeyboard(readBuffer, readBuffer.position());
		readBuffer.position(readBuffer.position() + numEvents*EVENT_SIZE);
		readBuffer.flip();
	}

	/**
	 * Checks to see if a key is down.
	 * @param key Keycode to check
	 * @return true if the key is down according to the last poll()
	 */
	public static boolean isKeyDown(int key) {
		if (!created)
			throw new IllegalStateException("Keyboard must be created before you can query key state");
		return keyDownBuffer.get(key) != 0;
	}

	/**
	 * Checks whether one of the state keys are "active"
	 *
	 * @param key State key to test (KEY_CAPITAL | KEY_NUMLOCK | KEY_SYSRQ)
	 * @return STATE_ON if on, STATE_OFF if off and STATE_UNKNOWN if the state is unknown
	 */
	public static int isStateKeySet(int key) {
		if (!created)
			throw new IllegalStateException("Keyboard must be created before you can query key state");
		return Display.getImplementation().isStateKeySet(key);
	}

	/**
	 * Gets a key's name
	 * @param key The key
	 * @return a String with the key's human readable name in it or null if the key is unnamed
	 */
	public static String getKeyName(int key) {
		return keyName[key];
	}

	/**
	 * Get's a key's index. If the key is unrecognised then KEY_NONE is returned.
	 * @param keyName The key name
	 */
	public static int getKeyIndex(String keyName) {
		Integer ret = (Integer) keyMap.get(keyName);
		if (ret == null)
			return KEY_NONE;
		else
			return ret.intValue();
	}

	/**
	 * Gets the number of keyboard events waiting after doing a buffer enabled poll().
	 * @return the number of keyboard events
	 */
	public static int getNumKeyboardEvents() {
		if (!created)
			throw new IllegalStateException("Keyboard must be created before you can read events");
		return readBuffer.remaining()/EVENT_SIZE;
	}

	/**
	 * Gets the next keyboard event. You can query which key caused the event by using
	 * <code>getEventKey</code>. To get the state of that key, for that event, use
	 * <code>getEventKeyState</code> - finally use <code>getEventCharacter</code> to get the
	 * character for that event.
	 *
	 * @see org.lwjgl.input.Keyboard#getEventKey()
	 * @see org.lwjgl.input.Keyboard#getEventKeyState()
	 * @see org.lwjgl.input.Keyboard#getEventCharacter()
	 * @return true if a keyboard event was read, false otherwise
	 */
	public static boolean next() {
		if (!created)
			throw new IllegalStateException("Keyboard must be created before you can read events");

		if (readBuffer.hasRemaining()) {
			eventKey = readBuffer.get() & 0xFF;
			eventState = readBuffer.get() != 0;
			int eventCharacterInt = readBuffer.get() & 0xFFFF;
			eventCharacter = (char)eventCharacterInt;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @return Number of keys on this keyboard
	 */
	public static int getKeyCount() {
		return keyCount;
	}

	/**
	 * @return The character from the current event
	 */
	public static char getEventCharacter() {
		return eventCharacter;
	}

	/**
   * Please note that the key code returned is NOT valid against the
   * current keyboard layout. To get the actual character pressed call
   * getEventCharacter
   * 
	 * @return The key from the current event
	 */
	public static int getEventKey() {
		return eventKey;
	}

	/**
	 * Gets the state of the tkey that generated the
	 * current event
	 *
	 * @return True if key was down, or false if released
	 */
	public static boolean getEventKeyState() {
		return eventState;
	}
}
