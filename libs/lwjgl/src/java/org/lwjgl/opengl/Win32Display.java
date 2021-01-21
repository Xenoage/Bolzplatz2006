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
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Cursor;

final class Win32Display implements DisplayImplementation {
	private static Win32DisplayPeerInfo peer_info;

	public void createWindow(DisplayMode mode, boolean fullscreen, int x, int y) throws LWJGLException {
		nCreateWindow(mode, fullscreen, x, y);
		peer_info.initDC();
	}
	private native void nCreateWindow(DisplayMode mode, boolean fullscreen, int x, int y) throws LWJGLException;
	public native void destroyWindow();
	public native void switchDisplayMode(DisplayMode mode) throws LWJGLException;
	public native void resetDisplayMode();
	public native int getGammaRampLength();
	public native void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException;
	public native String getAdapter();
	public native String getVersion();
	public native DisplayMode init() throws LWJGLException;
	public native void setTitle(String title);
	public native boolean isCloseRequested();
	public native boolean isVisible();
	public native boolean isActive();
	public native boolean isDirty();
	public PeerInfo createPeerInfo(PixelFormat pixel_format) throws LWJGLException {
		peer_info = new Win32DisplayPeerInfo(pixel_format);
		return peer_info;
	}
	public void update() {
		nUpdate();
		if (didMaximize()) {
			/**
			 * WORKAROUND:
			 * Making the context current (redundantly) when the window
			 * is maximized helps some gfx recover from fullscreen
			 */
			try {
				if (Display.getContext().isCurrent())
					Display.getContext().makeCurrent();
			} catch (LWJGLException e) {
				LWJGLUtil.log("Exception occurred while trying to make context current: " + e);
			}
		}
	}
	private native void nUpdate();
	private native boolean didMaximize();

	public native void reshape(int x, int y, int width, int height);
	public native DisplayMode[] getAvailableDisplayModes() throws LWJGLException;

	/* Mouse */
	public native boolean hasWheel();
	public native int getButtonCount();
	public native void createMouse() throws LWJGLException;
	public native void destroyMouse();
	public native void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons);
	public native int readMouse(IntBuffer buffer, int buffer_position);
	public native void grabMouse(boolean grab);
	public int getNativeCursorCapabilities() {
		return Cursor.CURSOR_ONE_BIT_TRANSPARENCY;
	}

	public native void setCursorPosition(int x, int y);
	public native void setNativeCursor(Object handle) throws LWJGLException;
	public native int getMinCursorSize();
	public native int getMaxCursorSize();

	/* Keyboard */
	public native void createKeyboard() throws LWJGLException;
	public native void destroyKeyboard();
	public native void pollKeyboard(ByteBuffer keyDownBuffer);
	public native int readKeyboard(IntBuffer buffer, int buffer_position);
	public native int isStateKeySet(int key);

	public native ByteBuffer nCreateCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, int images_offset, IntBuffer delays, int delays_offset) throws LWJGLException;

	public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
		return nCreateCursor(width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, delays != null ? delays.position() : -1);
	}

	public native void destroyCursor(Object cursorHandle);
	public int getPbufferCapabilities() {
		try {
		// Return the capabilities of a minimum pixel format
			return nGetPbufferCapabilities(new PixelFormat(0, 0, 0, 0, 0, 0, 0, 0, false));
		} catch (LWJGLException e) {
			LWJGLUtil.log("Exception occurred while determining pbuffer capabilities: " + e);
			return 0;
		}
	}
	private native int nGetPbufferCapabilities(PixelFormat format) throws LWJGLException;
	
	public boolean isBufferLost(PeerInfo handle) {
		return ((Win32PbufferPeerInfo)handle).isBufferLost();
	}

	public PeerInfo createPbuffer(int width, int height, PixelFormat pixel_format,
			IntBuffer pixelFormatCaps,
			IntBuffer pBufferAttribs) throws LWJGLException {
		return new Win32PbufferPeerInfo(width, height, pixel_format, pixelFormatCaps, pBufferAttribs);
	}
	
	public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
		((Win32PbufferPeerInfo)handle).setPbufferAttrib(attrib, value);
	}

	public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
		((Win32PbufferPeerInfo)handle).bindTexImageToPbuffer(buffer);
	}
	
	public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
		((Win32PbufferPeerInfo)handle).releaseTexImageFromPbuffer(buffer);
	}
}
