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
package org.lwjgl.opengl;

/**
 * This is the Display implementation interface. Display delegates
 * to implementors of this interface. There is one DisplayImplementation
 * for each supported platform.
 * @author elias_naur
 */

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;

public interface DisplayImplementation {

	void createWindow(DisplayMode mode, boolean fullscreen, int x, int y) throws LWJGLException;

	void destroyWindow();

	void switchDisplayMode(DisplayMode mode) throws LWJGLException;

	/**
	 * Reset the display mode to whatever it was when LWJGL was initialized.
	 * Fails silently.
	 */
	void resetDisplayMode();

	/**
	 * Return the length of the gamma ramp arrays. Returns 0 if gamma settings are
	 * unsupported.
	 *
	 * @return the length of each gamma ramp array, or 0 if gamma settings are unsupported.
	 */
	int getGammaRampLength();

	/**
	 * Method to set the gamma ramp.
	 */
	void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException;

	/**
	 * Get the driver adapter string. This is a unique string describing the actual card's hardware, eg. "Geforce2", "PS2",
	 * "Radeon9700". If the adapter cannot be determined, this function returns null.
	 * @return a String
	 */
	String getAdapter();

	/**
	 * Get the driver version. This is a vendor/adapter specific version string. If the version cannot be determined,
	 * this function returns null.
	 * @return a String
	 */
	String getVersion();

	/**
	 * Initialize and return the current display mode.
	 */
	DisplayMode init() throws LWJGLException;

	/**
	 * Implementation of setTitle(). This will read the window's title member
	 * and stash it in the native title of the window.
	 */
	void setTitle(String title);

	boolean isCloseRequested();

	boolean isVisible();
	boolean isActive();

	boolean isDirty();

	/**
	 * Create the native PeerInfo.
	 * @throws LWJGLException
	 */
	PeerInfo createPeerInfo(PixelFormat pixel_format) throws LWJGLException;

//	void destroyPeerInfo();

	/**
	 * Updates the windows internal state. This must be called at least once per video frame
	 * to handle window close requests, moves, paints, etc.
	 */
	void update();

	void reshape(int x, int y, int width, int height);

	/**
	 * Method for getting displaymodes
	 */
	DisplayMode[] getAvailableDisplayModes() throws LWJGLException;

	/*
	 * Mouse methods
	 */
	/** Query of wheel support */
	boolean hasWheel();

	/** Query of button count */
	int getButtonCount();

	/**
	 * Method to create the mouse.
	 */
	void createMouse() throws LWJGLException;

	/**
	 * Method the destroy the mouse
	 */
	void destroyMouse();

	/**
	 * Method to poll the mouse
	 */
	void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons);

	/**
	 * Method to read the keyboard buffer
	 *
	 * @return the total number of events read.
	 */
	int readMouse(IntBuffer buffer, int buffer_position);


	void grabMouse(boolean grab);

	/**
	 * Function to determine native cursor support
	 */
	int getNativeCursorCapabilities();

	/** Method to set the native cursor position */
	void setCursorPosition(int x, int y);
	
	/** Method to set the native cursor */
	void setNativeCursor(Object handle) throws LWJGLException;

	/** Method returning the minimum cursor size */
	int getMinCursorSize();

	/** Method returning the maximum cursor size */
	int getMaxCursorSize();

	/*
	 * Keyboard methods
	 */

	/**
	 * Method to create the keyboard
	 */
	void createKeyboard() throws LWJGLException;

	/**
	 * Method to destroy the keyboard
	 */
	void destroyKeyboard();

	/**
	 * Method to poll the keyboard.
	 *
	 * @param keyDownBuffer the address of a 256-byte buffer to place
	 * key states in.
	 */
	void pollKeyboard(ByteBuffer keyDownBuffer);

	/**
	 * Method to read the keyboard buffer
	 * @return the total number of events read.
	 */
	int readKeyboard(IntBuffer buffer, int buffer_position);

	int isStateKeySet(int key);

	/** Native cursor handles */
	Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException;

	void destroyCursor(Object cursor_handle);

	/* Pbuffer */
	int getPbufferCapabilities();

	/**
	 * Method to test for buffer integrity
	 */
	public boolean isBufferLost(PeerInfo handle);

	/**
	 * Method to create a Pbuffer
	 */
	public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format,
			IntBuffer pixelFormatCaps,
			IntBuffer pBufferAttribs) throws LWJGLException;

	public void setPbufferAttrib(PeerInfo handle, int attrib, int value);

	public void bindTexImageToPbuffer(PeerInfo handle, int buffer);

	public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer);
}
