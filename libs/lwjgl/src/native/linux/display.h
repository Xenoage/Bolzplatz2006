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
 * $Id: display.h,v 1.7 2004/12/11 20:09:04 elias_naur Exp $
 *
 * Linux specific library for display handling.
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.7 $
 */

#ifndef _DISPLAY_H
#define _DISPLAY_H

#include <jni.h>
#include "common_tools.h"

typedef enum {XRANDR, XF86VIDMODE, NONE} extension;

extern int getScreenModeWidth(void);
extern int getScreenModeHeight(void);
extern jobject initDisplay(JNIEnv *env, int screen);
extern void switchDisplayMode(JNIEnv * env, jobject mode, int screen);
extern void resetDisplayMode(JNIEnv *env, int screen, bool temporary);
extern jobjectArray getAvailableDisplayModes(JNIEnv * env, int screen);
extern int getGammaRampLength(JNIEnv *env, int screen);
extern void setGammaRamp(JNIEnv *env, jobject gamma_ramp_buffer, int screen);
extern extension getCurrentDisplayModeExtension();
extern void temporaryRestoreMode(JNIEnv *env, int screen);

#endif
