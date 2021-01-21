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
 * $Id: org_lwjgl_opengl_Win32ContextImplementation.c,v 1.3 2005/05/12 07:47:07 elias_naur Exp $
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.3 $
 */

#include <jni.h>
#include "org_lwjgl_opengl_Win32ContextImplementation.h"
#include "context.h"
#include "common_tools.h"

typedef struct {
	WGLExtensions extensions;
	HGLRC context;
} Win32Context;

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nCreate
  (JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject shared_context_handle) {
	Win32PeerInfo *peer_info;
	Win32Context *shared_context_info;
	Win32Context *context_info;
	HGLRC context;
	HGLRC shared_context = NULL;
	HDC saved_hdc;
	HGLRC saved_context;
	WGLExtensions extensions;
	jobject context_handle = newJavaManagedByteBuffer(env, sizeof(Win32Context));
	
	if (context_handle == NULL) {
		throwException(env, "Could not create handle buffer");
		return NULL;
	}
	peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	context = wglCreateContext(peer_info->format_hdc);
	if (context == NULL) {
		throwException(env, "Could not create context");
		return NULL;
	}
	if (shared_context_handle != NULL) {
		shared_context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, shared_context_handle);
		shared_context = shared_context_info->context;
		if (!wglShareLists(shared_context, context)) {
			wglDeleteContext(context);
			throwException(env, "Could not share contexts");
			return NULL;
		}
	}
	saved_hdc = wglGetCurrentDC();
	saved_context = wglGetCurrentContext();
	if (!wglMakeCurrent(peer_info->format_hdc, context)) {
		wglMakeCurrent(saved_hdc, saved_context);
		wglDeleteContext(context);
		throwException(env, "Could not make context current");
		return NULL;
	}
	extgl_InitWGL(&extensions);
	if (!wglMakeCurrent(saved_hdc, saved_context))
		printfDebugJava(env, "Failed to restore current context");
	context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, context_handle);
	context_info->context = context;
	context_info->extensions = extensions;
	return context_handle;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nSwapBuffers
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	Win32PeerInfo *peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	SwapBuffers(peer_info->drawable_hdc);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nReleaseCurrentContext
  (JNIEnv *env, jclass clazz) {
	wglMakeCurrent(NULL, NULL);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nMakeCurrent
  (JNIEnv *env, jclass clazz, jobject peer_info_handle, jobject context_handle) {
	Win32Context *context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, context_handle);
	Win32PeerInfo *peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	if (!wglMakeCurrent(peer_info->drawable_hdc, context_info->context))
		throwException(env, "Could not make context current");
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nIsCurrent
  (JNIEnv *env, jclass clazz, jobject context_handle) {
	Win32Context *context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, context_handle);
	return wglGetCurrentContext() == context_info->context;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nSetVSync
  (JNIEnv *env, jclass clazz, jobject context_handle, jboolean enable) {
	Win32Context *context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, context_handle);
	if (context_info->extensions.WGL_EXT_swap_control) {
		if (enable == JNI_TRUE) {
			context_info->extensions.wglSwapIntervalEXT(1);
		} else {
			context_info->extensions.wglSwapIntervalEXT(0);
		}
	}
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32ContextImplementation_nDestroy
  (JNIEnv *env, jclass clazz, jobject context_handle) {
	Win32Context *context_info = (Win32Context *)(*env)->GetDirectBufferAddress(env, context_handle);
	wglDeleteContext(context_info->context);
}
