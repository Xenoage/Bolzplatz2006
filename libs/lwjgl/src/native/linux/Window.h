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
 * $Id: Window.h,v 1.26 2005/02/20 11:24:22 elias_naur Exp $
 *
 * Include file to access public window features
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.26 $
 */

#ifndef _LWJGL_WINDOW_H_INCLUDED_
	#define _LWJGL_WINDOW_H_INCLUDED_

	#include <jni.h>
	#include <X11/X.h>
	#include <X11/Xlib.h>
	#include <X11/Xutil.h>
	#include "extgl.h"
	#include "extgl_glx.h"

	/*
	 * release input (keyboard, mouse)
	 */
	extern void handleMessages(JNIEnv *env);

	extern bool checkXError(JNIEnv *env, Display *display);
	extern Atom getWarpAtom(void);
	/*
	 * Various functions to release/acquire keyboard and mouse
	 */
	extern void handleWarpEvent(XClientMessageEvent *);
	extern void handlePointerMotion(XMotionEvent *);
	extern void handleButtonPress(XButtonEvent *);
	extern void handleButtonRelease(XButtonEvent *);
	extern void handleKeyEvent(XKeyEvent *);
	extern void updatePointerGrab(void);
	extern void updateKeyboardGrab(void);
	extern void setGrab(bool);
	extern bool isGrabbed(void);
	extern bool shouldGrab(void);

	/*
	 * get the current window width
	 */
	extern int getWindowWidth(void);
	
	/*
	 * get the current window height
	 */
	extern int getWindowHeight(void);
	
	/*
	 * get the current display
	 */
	extern Display *getDisplay(void);
	
	/*
	 * get the current screen
	 */
	extern int getCurrentScreen(void);
	
	/*
	 * get the current window
	 */
	extern Window getCurrentWindow(void);

	/*
	 * Return true if we are in fullscreen mode
	 */
	extern bool isFullscreen(void);
	
	/*
	 * Return true if we are in exclusive fullscreen mode
	 */
	extern bool isLegacyFullscreen(void);

#endif /* _LWJGL_WINDOW_H_INCLUDED_ */
