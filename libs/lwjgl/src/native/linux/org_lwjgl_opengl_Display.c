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

/**
 * $Id: org_lwjgl_opengl_Display.c,v 1.28 2005/04/14 10:10:46 elias_naur Exp $
 *
 * Linux specific display functions.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.28 $
 */

#include <X11/X.h>
#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <jni.h>
#include <jawt.h>
#include "common_tools.h"
#include "extgl.h"
#include "extgl_glx.h"
#include "Window.h"
#include "context.h"
#include "display.h"
#include "org_lwjgl_opengl_LinuxDisplay.h"
#include "org_lwjgl_opengl_LinuxDisplayPeerInfo.h"

#define ERR_MSG_SIZE 1024

typedef struct {
	unsigned long flags;
	unsigned long functions;
	unsigned long decorations;
	long input_mode;
	unsigned long status;
} MotifWmHints;

#define MWM_HINTS_DECORATIONS   (1L << 1)

typedef enum {FULLSCREEN_LEGACY, FULLSCREEN_NETWM, WINDOWED} window_mode;

static GLXWindow glx_window = None;

static Atom delete_atom;
static Colormap cmap;
static Window current_win;
static window_mode current_window_mode;
static int current_height;
static int current_width;

static bool input_released;

static bool dirty;
static bool vsync_enabled;
static bool minimized;
static bool focused;
static bool closerequested;
static bool grab;

static int current_screen;
static Display *display_connection = NULL;
static bool async_x_error;
static char error_message[ERR_MSG_SIZE];
static Atom warp_atom;

int getCurrentScreen(void) {
	return current_screen;
}

bool checkXError(JNIEnv *env, Display *disp) {
	XSync(disp, False);
	if (async_x_error) {
		async_x_error = false;
		if (env != NULL)
			throwException(env, error_message);
		else
			printfDebug(error_message);
		return false;
	} else
		return true;
}

static int errorHandler(Display *disp, XErrorEvent *error) {
	char err_msg_buffer[ERR_MSG_SIZE];
	XGetErrorText(disp, error->error_code, err_msg_buffer, ERR_MSG_SIZE);
	err_msg_buffer[ERR_MSG_SIZE - 1] = '\0';
	snprintf(error_message, ERR_MSG_SIZE, "X Error - serial: %d, error_code: %s, request_code: %d, minor_code: %d", (int)error->serial, err_msg_buffer, (int)error->request_code, (int)error->minor_code);
	error_message[ERR_MSG_SIZE - 1] = '\0';
	async_x_error = true;
	return 0;
}


Display *getDisplay(void) {
	return display_connection;
}

static void openDisplay(JNIEnv *env) {
	async_x_error = false;
	XSetErrorHandler(errorHandler);
	display_connection = XOpenDisplay(NULL);
	if (display_connection == NULL) {
		throwException(env, "Could not open X display connection");
		return;
	}
	current_screen = XDefaultScreen(getDisplay());
	warp_atom = XInternAtom(display_connection, "_LWJGL_WARP", False);
}

Atom getWarpAtom(void) {
	return warp_atom;
}

static void closeDisplay(void) {
	XCloseDisplay(display_connection);
	display_connection = NULL;
}

static void waitMapped(Window win) {
	XEvent event;
	do {
		XMaskEvent(getDisplay(), StructureNotifyMask, &event);
	} while ((event.type != MapNotify) || (event.xmap.event != win));
}

static void updateInputGrab(void) {
	updatePointerGrab();
	updateKeyboardGrab();
}

static void setRepeatMode(JNIEnv *env, int mode) {
	XKeyboardControl repeat_mode;
	repeat_mode.auto_repeat_mode = mode;
	Display *disp = XOpenDisplay(NULL);
	if (disp == NULL) {
		printfDebugJava(env, "Could not open display to set repeat mode");
		return;
	}
	XChangeKeyboardControl(disp, KBAutoRepeatMode, &repeat_mode);
	XCloseDisplay(disp);
}

static void setDecorations(int dec) {
	Atom motif_hints_atom = XInternAtom(getDisplay(), "_MOTIF_WM_HINTS", False);
	MotifWmHints motif_hints;
	motif_hints.flags = MWM_HINTS_DECORATIONS;
	motif_hints.decorations = dec;
	XChangeProperty (getDisplay(), getCurrentWindow(), motif_hints_atom, motif_hints_atom, 32, PropModeReplace, (unsigned char *)&motif_hints, sizeof(MotifWmHints)/sizeof(long));
}

static bool releaseInput(JNIEnv *env) {
	if (isLegacyFullscreen() || input_released)
		return false;
	input_released = true;
	setRepeatMode(env, AutoRepeatModeDefault);
	updateInputGrab();
	if (current_window_mode == FULLSCREEN_NETWM) {
		XIconifyWindow(getDisplay(), getCurrentWindow(), getCurrentScreen());
		resetDisplayMode(env, getCurrentScreen(), true);
	}
	return true;
}

static void acquireInput(JNIEnv *env) {
	if (isLegacyFullscreen() || !input_released)
		return;
	input_released = false;
	setRepeatMode(env, AutoRepeatModeOff);
	updateInputGrab();
	if (current_window_mode == FULLSCREEN_NETWM) {
		temporaryRestoreMode(env, getCurrentScreen());
	}
}

bool isFullscreen(void) {
	return current_window_mode == FULLSCREEN_LEGACY || current_window_mode == FULLSCREEN_NETWM;
}

bool isLegacyFullscreen(void) {
	return current_window_mode == FULLSCREEN_LEGACY;
}

bool shouldGrab(void) {
	return !input_released && grab;
}

bool isGrabbed(void) {
	return grab;
}

void setGrab(bool new_grab) {
	if (new_grab != grab) {
		grab = new_grab;
		updateInputGrab();
	}
}

static void checkInput(JNIEnv *env) {
	Window win;
	int revert_mode;
	XGetInputFocus(getDisplay(), &win, &revert_mode);
	if (win == current_win) {
		acquireInput(env);
		focused = true;
	} else {
		releaseInput(env);
		focused = false;
	}
}

void handleMessages(JNIEnv *env) {
	XEvent event;
/*	Window win;
	int revert_mode;*/
	while (XPending(getDisplay()) > 0) {
		XNextEvent(getDisplay(), &event);
		if (XFilterEvent(&event, None) == True)
			continue;
		switch (event.type) {
			case ClientMessage:
				if (event.xclient.message_type == warp_atom) {
					handleWarpEvent(&(event.xclient));
				} else if ((event.xclient.format == 32) && ((Atom)event.xclient.data.l[0] == delete_atom))
					closerequested = true;
				break;
/*			case FocusOut:
				XGetInputFocus(getDisplay(), &win, &revert_mode);
				if (win != current_win) {
					releaseInput();
					focused = false;
				}
				break;
			case FocusIn:
				checkInput();
				break;*/
			case MapNotify:
				dirty = true;
				minimized = false;
				break;
			case UnmapNotify:
				dirty = true;
				minimized = true;
				break;
			case Expose:
				dirty = true;
				break;
			case ButtonPress:
				handleButtonPress(&(event.xbutton));
				break;
			case ButtonRelease:
				handleButtonRelease(&(event.xbutton));
				break;
			case MotionNotify:
				handlePointerMotion(&(event.xmotion));
				break;
			case KeyPress:
			case KeyRelease:
				handleKeyEvent(&(event.xkey));
				break;
		}
	}
	checkInput(env);
}

static void setWindowTitle(const char *title) {
	XStoreName(getDisplay(), current_win, title);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_openDisplay(JNIEnv *env, jclass clazz) {
	openDisplay(env);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_closeDisplay(JNIEnv *env, jclass clazz) {
	closeDisplay();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplayPeerInfo_initDrawable(JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	X11PeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	if (peer_info->glx13)
		peer_info->drawable = glx_window;
	else
		peer_info->drawable = getCurrentWindow();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplayPeerInfo_initDefaultPeerInfo(JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject pixel_format) {
	initPeerInfo(env, peer_info_handle, getDisplay(), getCurrentScreen(), pixel_format, true, GLX_WINDOW_BIT, true, false);
}
  
JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetTitle(JNIEnv * env, jclass clazz, jstring title_obj) {
	char * title = GetStringNativeChars(env, title_obj);
	setWindowTitle(title);
	free(title);
}

static void destroyWindow(JNIEnv *env) {
	if (glx_window != None) {
		_glXDestroyWindow(getDisplay(), glx_window);
		glx_window = None;
	}
	XDestroyWindow(getDisplay(), current_win);
	XFreeColormap(getDisplay(), cmap);
	setRepeatMode(env, AutoRepeatModeDefault);
}

static bool isNetWMForceDisabled() {
	char *supported_env = getenv("LWJGL_DISABLE_NETWM");
	return supported_env != NULL;
}

static bool isNetWMFullscreenSupported(JNIEnv *env) {
	unsigned long nitems;
	Atom actual_type;
	int actual_format;
	unsigned long bytes_after;
	Atom *supported_list;
	if (isNetWMForceDisabled())
		return false;
	Atom netwm_supported_atom = XInternAtom(getDisplay(), "_NET_SUPPORTED", False);
	int result = XGetWindowProperty(getDisplay(), RootWindow(getDisplay(), getCurrentScreen()), netwm_supported_atom, 0, 10000, False, AnyPropertyType, &actual_type, &actual_format, &nitems, &bytes_after, (void *)&supported_list);
	if (result != Success) {
		printfDebugJava(env, "Unable to query _NET_SUPPORTED window property");
		return false;
	}
	Atom fullscreen_atom = XInternAtom(getDisplay(), "_NET_WM_STATE_FULLSCREEN", False);
	bool supported = false;
	unsigned long i;
	for (i = 0; i < nitems; i++) {
		if (fullscreen_atom == supported_list[i]) {
			supported = true;
			break;
		}
	}
	XFree(supported_list);
	return supported;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nReshape(JNIEnv *env, jclass clazz, jint x, jint y, jint width, jint height) {
	XMoveWindow(getDisplay(), getCurrentWindow(), x, y);
}

static bool createWindow(JNIEnv* env, X11PeerInfo *peer_info, int x, int y, int width, int height) {
	bool undecorated = getBooleanProperty(env, "org.lwjgl.opengl.Window.undecorated");
	dirty = true;
	focused = true;
	minimized = false;
	closerequested = false;
	vsync_enabled = false;
	grab = false;
	Window root_win;
	Window win;
	XSetWindowAttributes attribs;
	int attribmask;

	input_released = false;
	current_width = width;
	current_height = height;
	root_win = RootWindow(getDisplay(), getCurrentScreen());
	XVisualInfo *vis_info = getVisualInfoFromPeerInfo(env, peer_info);
	if (vis_info == NULL)
		return false;
	cmap = XCreateColormap(getDisplay(), root_win, vis_info->visual, AllocNone);
	attribs.colormap = cmap;
	attribs.event_mask = ExposureMask | /*FocusChangeMask | */VisibilityChangeMask | StructureNotifyMask | KeyPressMask | KeyReleaseMask | ButtonPressMask | ButtonReleaseMask | PointerMotionMask;
	attribs.background_pixel = 0xFF000000;
	attribs.win_gravity = NorthWestGravity;
	attribmask = CWColormap | CWBackPixel | CWEventMask | CWWinGravity;
	if (isLegacyFullscreen()) {
		attribmask |= CWOverrideRedirect;
		attribs.override_redirect = True;
	}
	win = XCreateWindow(getDisplay(), root_win, x, y, width, height, 0, vis_info->depth, InputOutput, vis_info->visual, attribmask, &attribs);
	XFree(vis_info);
	if (!checkXError(env, getDisplay())) {
		XFreeColormap(getDisplay(), cmap);
		return false;
	}
	printfDebugJava(env, "Created window");
	current_win = win;
	if (current_window_mode != WINDOWED || undecorated) {
		// Use Motif decoration hint property and hope the window manager respects them
		setDecorations(0);
	}
	XSizeHints * size_hints = XAllocSizeHints();
	size_hints->flags = PMinSize | PMaxSize;
	size_hints->min_width = width;
	size_hints->max_width = width;
	size_hints->min_height = height;
	size_hints->max_height = height;
	XSetWMNormalHints(getDisplay(), win, size_hints);
	XFree(size_hints);
	delete_atom = XInternAtom(getDisplay(), "WM_DELETE_WINDOW", False);
	XSetWMProtocols(getDisplay(), win, &delete_atom, 1);
	if (current_window_mode == FULLSCREEN_NETWM) {
		Atom fullscreen_atom = XInternAtom(getDisplay(), "_NET_WM_STATE_FULLSCREEN", False);
		XChangeProperty(getDisplay(), getCurrentWindow(), XInternAtom(getDisplay(), "_NET_WM_STATE", False),
						XInternAtom(getDisplay(), "ATOM", False), 32, PropModeReplace, (const unsigned char*)&fullscreen_atom, 1);
	}
	XMapRaised(getDisplay(), win);
	waitMapped(win);
	XClearWindow(getDisplay(), win);
	setRepeatMode(env, AutoRepeatModeOff);
	if (!checkXError(env, getDisplay())) {
		destroyWindow(env);
		return false;
	}
	return true;
}

Window getCurrentWindow(void) {
	return current_win;
}

int getWindowWidth(void) {
	return current_width;
}

int getWindowHeight(void) {
	return current_height;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nUpdate
  (JNIEnv *env, jclass clazz)
{
	handleMessages(env);
}

JNIEXPORT jobjectArray JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetAvailableDisplayModes(JNIEnv *env, jclass clazz) {
	return getAvailableDisplayModes(env, getCurrentScreen());
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSwitchDisplayMode(JNIEnv *env, jclass clazz, jobject mode) {
	switchDisplayMode(env, mode, getCurrentScreen());
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nResetDisplayMode(JNIEnv *env, jclass clazz) {
	resetDisplayMode(env, getCurrentScreen(), false);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nGetGammaRampLength(JNIEnv *env, jclass clazz) {
	return (jint)getGammaRampLength(env, getCurrentScreen());
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nSetGammaRamp(JNIEnv *env, jclass clazz, jobject gamma_buffer) {
	setGammaRamp(env, gamma_buffer, getCurrentScreen());
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nInit(JNIEnv *env, jclass clazz) {
	return initDisplay(env, getCurrentScreen());
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nCreateWindow(JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject mode, jboolean fullscreen, jint x, jint y) {
	bool current_fullscreen = fullscreen == JNI_TRUE;
	if (current_fullscreen) {
		if (getCurrentDisplayModeExtension() == XRANDR && isNetWMFullscreenSupported(env)) {
			printfDebugJava(env, "Using NetWM for fullscreen window");
			current_window_mode = FULLSCREEN_NETWM;
		} else {
			printfDebugJava(env, "Using legacy mode for fullscreen window");
			current_window_mode = FULLSCREEN_LEGACY;
		}
	} else
		current_window_mode = WINDOWED;
	X11PeerInfo *peer_info = (*env)->GetDirectBufferAddress(env, peer_info_handle);
	GLXFBConfig *fb_config = NULL;
	if (peer_info->glx13) {
		fb_config = getFBConfigFromPeerInfo(env, peer_info);
		if (fb_config == NULL)
			return;
	}
	jclass cls_displayMode = (*env)->GetObjectClass(env, mode);
	jfieldID fid_width = (*env)->GetFieldID(env, cls_displayMode, "width", "I");
	jfieldID fid_height = (*env)->GetFieldID(env, cls_displayMode, "height", "I");
	int width = (*env)->GetIntField(env, mode, fid_width);
	int height = (*env)->GetIntField(env, mode, fid_height);
	bool window_created = createWindow(env, peer_info, x, y, width, height);
	if (!window_created) {
		return;
	}
	if (peer_info->glx13) {
		glx_window = _glXCreateWindow(getDisplay(), *fb_config, getCurrentWindow(), NULL);
		XFree(fb_config);
	}
	if (!checkXError(env, getDisplay())) {
		_glXDestroyWindow(getDisplay(), glx_window);
		destroyWindow(env);
	}
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nDestroyWindow(JNIEnv *env, jclass clazz) {
	destroyWindow(env);
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsDirty
  (JNIEnv *env, jclass clazz) {
	bool result = dirty;
	dirty = false;
	return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsVisible
  (JNIEnv *env, jclass clazz) {
	return minimized ? JNI_FALSE : JNI_TRUE;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsCloseRequested
  (JNIEnv *env, jclass clazz) {
	bool saved = closerequested;
	closerequested = false;
	return saved;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_LinuxDisplay_nIsActive
  (JNIEnv *env, jclass clazz) {
	return focused || isLegacyFullscreen() ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_lockAWT(JNIEnv *env, jclass clazz) {
	JAWT jawt;
	jawt.version = JAWT_VERSION_1_4;
	if (JAWT_GetAWT(env, &jawt) != JNI_TRUE) {
		throwGeneralException(env, "java/lang/RuntimeException", "GetAWT failed");
		return;
	}
	jawt.Lock(env);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_LinuxDisplay_unlockAWT(JNIEnv *env, jclass clazz) {
	JAWT jawt;
	jawt.version = JAWT_VERSION_1_4;
	if (JAWT_GetAWT(env, &jawt) != JNI_TRUE) {
		throwGeneralException(env, "java/lang/RuntimeException", "GetAWT failed");
		return;
	}
	jawt.Unlock(env);
}
