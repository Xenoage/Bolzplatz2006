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
 * $Id: org_lwjgl_opengl_Win32DisplayPeerInfo.c,v 1.1 2005/02/21 14:46:47 elias_naur Exp $
 *
 * @author elias_naur <elias_naur@users.sourceforge.net>
 * @version $Revision: 1.1 $
 */

#include <jni.h>
#include "Window.h"
#include "org_lwjgl_opengl_Win32DisplayPeerInfo.h"
#include "context.h"
#include "common_tools.h"

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32DisplayPeerInfo_createDummyDC
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	Win32PeerInfo *peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	HWND dummy_hwnd = createDummyWindow(0, 0);
	HDC dummy_hdc;
	if (dummy_hwnd == NULL) {
		throwException(env, "Failed to create a dummy window.");
		return;
	}
	dummy_hdc = GetDC(dummy_hwnd);
	peer_info->format_hwnd = dummy_hwnd;
	peer_info->format_hdc = dummy_hdc;
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32DisplayPeerInfo_nDestroy
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	Win32PeerInfo *peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	closeWindow(&peer_info->format_hwnd, &peer_info->format_hdc);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_Win32DisplayPeerInfo_nInitDC
  (JNIEnv *env, jclass clazz, jobject peer_info_handle) {
	Win32PeerInfo *peer_info = (Win32PeerInfo *)(*env)->GetDirectBufferAddress(env, peer_info_handle);
	int pixel_format;
	peer_info->drawable_hdc = getCurrentHDC();
	pixel_format = GetPixelFormat(peer_info->format_hdc);
	if (pixel_format == 0) {
		throwException(env, "Could not get pixel format from dummy hdc");
		return;
	}
	if (!applyPixelFormat(peer_info->drawable_hdc, pixel_format))
		throwException(env, "Could not apply pixel format to drawable");
}
