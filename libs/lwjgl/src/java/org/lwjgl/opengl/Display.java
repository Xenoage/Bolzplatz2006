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
 * This is the abstract class for a Display in LWJGL. LWJGL displays have some
 * peculiar characteristics:
 *
 * - the display may be closeable by the user or operating system, and may be minimized
 * by the user or operating system
 * - only one display may ever be open at once
 * - the operating system may or may not be able to do fullscreen or windowed displays.
 *
 * @author foo
 */

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashSet;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public final class Display {

	/** The display implementor */
	private static final DisplayImplementation display_impl;

	/** The initial display mode */
	private static final DisplayMode initial_mode;

	/** The current display mode, if created */
	private static DisplayMode current_mode;

	/** Timer for sync() */
	private static long timeNow, timeThen;

	/** X coordinate of the window */
	private static int x = -1;

	/**
	 * Y coordinate of the window. Y in window coordinates is from the top of the display down,
	 * unlike GL, where it is typically at the bottom of the display.
	 */
	private static int y = -1;

	/** Title of the window (never null) */
	private static String title = "Game";

	/** Fullscreen */
	private static boolean fullscreen;

	/** VSync */
	private static boolean vsync;

	/** A unique context object, so we can track different contexts between creates() and destroys() */
	private static PeerInfo peer_info;
	private static Context context;

	private static boolean window_created = false;

	static {
		Sys.initialize();
		display_impl = createDisplayImplementation();
		try {
			current_mode = initial_mode = display_impl.init();
			LWJGLUtil.log("Initial mode: " + initial_mode);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					reset();
				}
			});
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Fetch the Drawable from the Display.
	 *
	 * @return the Drawable corresponding to the Display context, or null it display is
	 *			not created.
	 */
	public static Drawable getDrawable() {
		if (context != null) {
			return new Drawable() {
				public Context getContext() {
					return context;
				}
			};
		} else
			return null;
	}

	private static DisplayImplementation createDisplayImplementation() {
		String class_name;
		switch (LWJGLUtil.getPlatform()) {
			case LWJGLUtil.PLATFORM_LINUX:
			class_name = "org.lwjgl.opengl.LinuxDisplay";
				break;
			case LWJGLUtil.PLATFORM_WINDOWS:
			class_name = "org.lwjgl.opengl.Win32Display";
				break;
			case LWJGLUtil.PLATFORM_MACOSX:
			class_name = "org.lwjgl.opengl.MacOSXDisplay";
				break;
			default:
				throw new IllegalStateException("Unsupported platform");
		}
		try {
			Class display_class = Class.forName(class_name);
			return (DisplayImplementation)display_class.newInstance();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Only constructed by ourselves
	 */
	private Display() {
	}

	/**
	 * Returns the entire list of possible fullscreen display modes as an array, in no
	 * particular order. Any given mode is not guaranteed to be available and
	 * the only certain way to check is to call create() and make sure it works.
	 * Only non-palette-indexed modes are returned (ie. bpp will be 16, 24, or 32).
	 * Only DisplayModes from this call can be used when the Display is in fullscreen
	 * mode.
	 *
	 * @return an array of all display modes the system reckons it can handle.
	 */
	public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
		DisplayMode[] unfilteredModes = display_impl.getAvailableDisplayModes();

		if (unfilteredModes == null) {
			return new DisplayMode[0];
		}

		// We'll use a HashSet to filter out the duplicated modes
		HashSet modes = new HashSet(unfilteredModes.length);

		modes.addAll(Arrays.asList(unfilteredModes));
		DisplayMode[] filteredModes = new DisplayMode[modes.size()];
		modes.toArray(filteredModes);

		LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");

		return filteredModes;
	}

	/**
	 * Return the current display mode, as set by setDisplayMode().
	 * @return The current display mode
	 */
	public static DisplayMode getDisplayMode() {
		return current_mode;
	}

	/**
	 * Set the current display mode. If no OpenGL context has been created, the given mode will apply to
	 * the context when create() is called, and no immediate mode switching will happen. If there is a
	 * context already, it will be resized according to the given mode. If the context is also a
	 * fullscreen context, the mode will also be switched immediately. The native cursor position
	 * is also reset.
	 *
	 * @param mode The new display mode to set
	 * @throws LWJGLException if the display mode could not be set
	 */
	public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
		if (mode == null)
			throw new NullPointerException("mode must be non-null");
		current_mode = mode;
		if (isCreated()) {
			destroyWindow();
			// If mode is not fullscreen capable, make sure we are in windowed mode
			if (!mode.isFullscreen())
				resetFullscreen();
			try {
				if (fullscreen)
					switchDisplayMode();
				createWindow();
			} catch (LWJGLException e) {
				destroyContext();
				display_impl.resetDisplayMode();
				throw e;
			}
		}
	}

	/**
	 * Create the native window peer from the given mode and fullscreen flag.
	 * A native context must exist, and it will be attached to the window.
	 */
	private static void createWindow() throws LWJGLException {
		if (window_created) {
			return;
		}
		int window_x;
		int window_y;
		if (!fullscreen) {
			// if no display location set, center window
			if (x == -1 && y == -1) {
				window_x = Math.max(0, (initial_mode.getWidth() - current_mode.getWidth()) / 2);
				window_y = Math.max(0, (initial_mode.getHeight() - current_mode.getHeight()) / 2);
			} else {
				window_x = x;
				window_y = y;
			}
		} else {
			window_x = 0;
			window_y = 0;
		}
		display_impl.createWindow(current_mode, fullscreen, window_x, window_y);
		makeCurrent();
		
		setTitle(title);
		initControls();
		setVSyncEnabled(vsync);
		window_created = true;
	}

	private static void destroyWindow() {
		if (!window_created) {
			return;
		}
		try {
			if (context.isCurrent()) {
				Context.releaseCurrentContext();
			}
		} catch (LWJGLException e) {
			LWJGLUtil.log("Exception occurred while trying to release context");
		}

		// Automatically destroy keyboard & mouse
		if (Mouse.isCreated()) {
			Mouse.destroy();
		}
		if (Keyboard.isCreated()) {
			Keyboard.destroy();
		}
		display_impl.destroyWindow();
		window_created = false;
	}

	private static void switchDisplayMode() throws LWJGLException {
		if (!current_mode.isFullscreen()) {
			System.out.println("Switching to "+initial_mode);
			setDisplayMode(initial_mode);
		}
		display_impl.switchDisplayMode(current_mode);
	}

	/**
	 * Set the display configuration to the specified gamma, brightness and contrast.
	 * The configuration changes will be reset when destroy() is called.
	 *
	 * @param gamma The gamma value
	 * @param brightness The brightness value between -1.0 and 1.0, inclusive
	 * @param contrast The contrast, larger than 0.0.
	 */
	public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
		if (!isCreated()) {
			throw new LWJGLException("Display not yet created.");
		}
		if (brightness < -1.0f || brightness > 1.0f)
			throw new IllegalArgumentException("Invalid brightness value");
		if (contrast < 0.0f)
			throw new IllegalArgumentException("Invalid contrast value");
		int rampSize = display_impl.getGammaRampLength();
		if (rampSize == 0) {
			throw new LWJGLException("Display configuration not supported");
		}
		FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
		for (int i = 0; i < rampSize; i++) {
			float intensity = (float)i/(rampSize - 1);
			// apply gamma
			float rampEntry = (float)java.lang.Math.pow(intensity, gamma);
			// apply brightness
			rampEntry += brightness;
			// apply contrast
			rampEntry = (rampEntry - 0.5f)*contrast + 0.5f;
			// Clamp entry to [0, 1]
			if (rampEntry > 1.0f)
				rampEntry = 1.0f;
			else if (rampEntry < 0.0f)
				rampEntry = 0.0f;
			gammaRamp.put(i, rampEntry);
		}
		display_impl.setGammaRamp(gammaRamp);
		LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
	}

	/**
	 * Synchronize the display to a capped frame rate. Note that we are being "smart" about the
	 * desired results in our implementation; we automatically subtract 1 from the desired framerate
	 * to prevent just missing the frame time if vsync is set.
	 * @param fps The desired frame rate, in frames per second
	 */
	public static void sync3(int fps) {
		float frameTime = 1.0f / (float) (fps > 1 ? fps - 1 : 1);
		timeNow = Sys.getTime();
		while (timeNow > timeThen && (float) (timeNow - timeThen) / (float) Sys.getTimerResolution() < frameTime) {
			// This is a system-friendly way of allowing other stuff to use CPU if it wants to
			Thread.yield();
			timeNow = Sys.getTime();
		}
		timeThen = timeNow;
	}

	private static long timeLate;

	/**
	 * Alternative sync method which works better on triple-buffered GL displays.
	 *
	 * @param fps The desired frame rate, in frames per second
	 */
	public static void sync2(int fps) {
		long gapTo = Sys.getTimerResolution() / fps + timeThen;
		timeNow = Sys.getTime();

		while (gapTo > timeNow + timeLate) {
			Thread.yield();
			timeNow = Sys.getTime();
		}

		if (gapTo < timeNow)
			timeLate = timeNow - gapTo;
		else
			timeLate = 0;

		timeThen = timeNow;
	}

	/**
	 * Best sync method that works reliably.
	 *
	 * @param fps The desired frame rate, in frames per second
	 */
	public static void sync(int fps) {
		long gapTo = Sys.getTimerResolution() / fps + timeThen;
		timeNow = Sys.getTime();

		while (gapTo > timeNow + timeLate) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
			timeNow = Sys.getTime();
		}

		if (gapTo < timeNow)
			timeLate = timeNow - gapTo;
		else
			timeLate = 0;

		timeThen = timeNow;
	}
	
	/**
	 * @return the X coordinate of the window (always 0 for fullscreen)
	 */
	/*public static int getX() {
		return (fullscreen) ? 0 :  x;
	}*/

	/**
	 * @return the Y coordinate of the window (always 0 for fullscreen)
	 */
	/*public static int getY() {
		return (fullscreen) ? 0 :  y;
	}*/


	/**
	 * @return the title of the window
	 */
	public static String getTitle() {
		return title;
	}

	private static void resetFullscreen() {
		if (Display.fullscreen) {
			Display.fullscreen = false;
			display_impl.resetDisplayMode();
		}
	}

	/**
	 * Set the fullscreen mode of the context. If no context has been created through create(),
	 * the mode will apply when create() is called. If fullscreen is true, the context will become
	 * a fullscreen context and the display mode is switched to the mode given by getDisplayMode(). If
	 * fullscreen is false, the context will become a windowed context with the dimensions given in the
	 * mode returned by getDisplayMode(). The native cursor position is also reset.
	 *
	 * @param fullscreen Specify the fullscreen mode of the context.
	 * @throws LWJGLException If fullscreen is true, and the current DisplayMode instance is not
	 *						  from getAvailableDisplayModes() or if the mode switch fails.
	 */
	public static void setFullscreen(boolean fullscreen) throws LWJGLException {
		if (Display.fullscreen != fullscreen) {
			Display.fullscreen = fullscreen;
			if (!isCreated())
				return;
			destroyWindow();
			try {
				if (fullscreen) {
					switchDisplayMode();
				} else {
					display_impl.resetDisplayMode();
				}
				createWindow();
			} catch (LWJGLException e) {
				destroyContext();
				display_impl.resetDisplayMode();
				throw e;
			}
		}
	}

	/**
	 * @return whether the Display is in fullscreen mode
	 */
	public static boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Set the title of the window. This may be ignored by the underlying OS.
	 * @param newTitle The new window title
	 */
	public static void setTitle(String newTitle) {
		if (newTitle == null) {
			newTitle = "";
		}
		title = newTitle;
		if (isCreated())
			display_impl.setTitle(title);
	}

	/**
	 * @return true if the user or operating system has asked the window to close
	 */
	public static boolean isCloseRequested() {
		if (!isCreated())
			throw new IllegalStateException("Cannot determine close requested state of uncreated window");
		display_impl.update();
		return display_impl.isCloseRequested();
	}

	/**
	 * @return true if the window is visible, false if not
	 */
	public static boolean isVisible() {
		if (!isCreated())
			throw new IllegalStateException("Cannot determine minimized state of uncreated window");
		display_impl.update();
		return display_impl.isVisible();
	}

	/**
	 * @return true if window is active, that is, the foreground display of the operating system.
	 */
	public static boolean isActive() {
		if (!isCreated())
			throw new IllegalStateException("Cannot determine focused state of uncreated window");
		display_impl.update();
		return display_impl.isActive();
	}

	/**
	 * Determine if the window's contents have been damaged by external events.
	 * If you are writing a straightforward game rendering loop and simply paint
	 * every frame regardless, you can ignore this flag altogether. If you are
	 * trying to be kind to other processes you can check this flag and only
	 * redraw when it returns true. The flag is cleared when update() or isDirty() is called.
	 *
	 * @return true if the window has been damaged by external changes
	 * and needs to repaint itself
	 */
	public static boolean isDirty() {
		if (!isCreated())
			throw new IllegalStateException("Cannot determine dirty state of uncreated window");
		display_impl.update();
		return display_impl.isDirty();
	}

	/**
	 * Process operating system events. Call this to update the Display's state and make sure the
	 * input devices receive events. This method is called from update(), and should normally not be called by
	 * the application.
	 */
	public static void processMessages() {
		if (!isCreated())
			throw new IllegalStateException("Cannot update uncreated window");

		display_impl.update();
	}

	/**
	 * Update the window. This calls processMessages(), and if the window is visible
	 * clears the dirty flag and swaps the buffers and polls the input devices.
	 * @throws OpenGLException if an OpenGL error has occured since the last call to GL11.glGetError()
	 */
	public static void update() {
		if (!isCreated())
			throw new IllegalStateException("Cannot update uncreated window");

		// We paint only when the window is visible or dirty
		if (isVisible() || isDirty()) {
			Util.checkGLError();
			try {
				Context.swapBuffers();
			} catch (LWJGLException e) {
				throw new RuntimeException(e);
			}
		}

		processMessages();

		// Poll the input devices while we're here
		if (Mouse.isCreated()) {
			Mouse.poll();
			Mouse.updateCursor();
		}
		if (Keyboard.isCreated()) {
			Keyboard.poll();
		}
	}

	/**
	 * Make the Display the current rendering context for GL calls. Also initialize native stubs.
	 * @throws LWJGLException If the context could not be made current
	 */
	public static void makeCurrent() throws LWJGLException {
		if (!isCreated())
			throw new IllegalStateException("No window created to make current");
		context.makeCurrent();
	}

	/**
	 * Create the OpenGL context. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 *
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @throws LWJGLException
	 */
	public static void create() throws LWJGLException {
		create(new PixelFormat());
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 *
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill.
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format) throws LWJGLException {
		create(pixel_format, null);
	}

	/**
	 * Create the OpenGL context with the given minimum parameters. If isFullscreen() is true or if windowed
	 * context are not supported on the platform, the display mode will be switched to the mode returned by
	 * getDisplayMode(), and a fullscreen context will be created. If isFullscreen() is false, a windowed context
	 * will be created with the dimensions given in the mode returned by getDisplayMode(). If a context can't be
	 * created with the given parameters, a LWJGLException will be thrown.
	 *
	 * <p>The window created will be set up in orthographic 2D projection, with 1:1 pixel ratio with GL coordinates.
	 *
	 * @param pixel_format Describes the minimum specifications the context must fulfill.
	 * @param shared_drawable The Drawable to share context with.
	 * @throws LWJGLException
	 */
	public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
		if (isCreated())
			throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
		if (fullscreen)
			switchDisplayMode();
		try {
			peer_info = display_impl.createPeerInfo(pixel_format);
			context = new Context(peer_info, shared_drawable != null ? shared_drawable.getContext() : null);
			try {
				createWindow();
				initContext();
			} catch (LWJGLException e) {
				destroyContext();
				throw e;
			}
		} catch (LWJGLException e) {
			display_impl.resetDisplayMode();
			throw e;
		}
	}

	private static void initContext() {
		// Put the window into orthographic projection mode with 1:1 pixel ratio.
		// We haven't used GLU here to do this to avoid an unnecessary dependency.
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.0, (double) current_mode.getWidth(), 0.0, (double) current_mode.getHeight(), -1.0, 1.0);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, current_mode.getWidth(), current_mode.getHeight());
	}

	public static DisplayImplementation getImplementation() {
		return display_impl;
	}

	private static void initControls() {
		// Automatically create mouse, keyboard and controller
		if (!Boolean.getBoolean("org.lwjgl.opengl.Display.noinput")) {
			if (!Mouse.isCreated() && !Boolean.getBoolean("org.lwjgl.opengl.Display.nomouse")) {
				try {
					Mouse.create();
				} catch (LWJGLException e) {
					if (LWJGLUtil.DEBUG) {
						e.printStackTrace(System.err);
					} else {
						LWJGLUtil.log("Failed to create Mouse: "+e);
					}
				}
			}
			if (!Keyboard.isCreated() && !Boolean.getBoolean("org.lwjgl.opengl.Display.nokeyboard")) {
				try {
					Keyboard.create();
				} catch (LWJGLException e) {
					if (LWJGLUtil.DEBUG) {
						e.printStackTrace(System.err);
					} else {
						LWJGLUtil.log("Failed to create Keyboard: "+e);
					}
				}
			}
		}
	}

	/**
	 * Destroy the Display. After this call, there will be no current GL rendering context,
	 * regardless of whether the Display was the current rendering context.
	 */
	public static void destroy() {
		if (!isCreated()) {
			return;
		}

		destroyWindow();
		destroyContext();
		x = y = -1;
		reset();
	}

	private static void destroyContext() {
		try {
			context.forceDestroy();
			peer_info.destroy();
		} catch (LWJGLException e) {
			throw new RuntimeException(e);
		} finally {
			context = null;
		}
	}

	/*
	 * Reset display mode if fullscreen. This method is also called from the shutdown hook added
	 * in the static constructor
	 */
	private static void reset() {
		display_impl.resetDisplayMode();
		current_mode = initial_mode;
	}

	/**
	 * @return the unique Display context (or null, if the Display has not been created)
	 */
	public static Context getContext() {
		return context;
	}

	/**
	 * @return true if the window's native peer has been created
	 */
	public static boolean isCreated() {
		return context != null;
	}

	/**
	 * Enable or disable vertical monitor synchronization. This call is a best-attempt at changing
	 * the vertical refresh synchronization of the monitor, and is not guaranteed to be successful.
	 * @param sync true to synchronize; false to ignore synchronization
	 */
	public static void setVSyncEnabled(boolean sync) {
		vsync = sync;
		if (isCreated())
			Context.setVSync(vsync);
	}

	/**
	 * Set the window's location. This is a no-op on fullscreen windows.
	 * The window is clamped to remain entirely on the screen. If you attempt
	 * to position the window such that it would extend off the screen, the window
	 * is simply placed as close to the edge as possible.
   * <br><b>note</b>If no location has been specified (or x == y == -1) the window will be centered
	 * @param x The new window location on the x axis
   * @param y The new window location on the y axis
	 */
	public static void setLocation(int x, int y) {
		if (fullscreen) {
			return;
		}

		// offset if already created
		if(isCreated()) {
			display_impl.reshape(x, y, current_mode.getWidth(), current_mode.getHeight());
		}

		// cache position
		Display.x = x;
		Display.y = y;
	}

	/**
	 * Get the driver adapter string. This is a unique string describing the actual card's hardware, eg. "Geforce2", "PS2",
	 * "Radeon9700". If the adapter cannot be determined, this function returns null.
	 * @return a String
	 */
	public static String getAdapter() {
		return display_impl.getAdapter();
	}

	/**
	 * Get the driver version. This is a vendor/adapter specific version string. If the version cannot be determined,
	 * this function returns null.
	 * @return a String
	 */
	public static String getVersion() {
		return display_impl.getVersion();
	}
}
