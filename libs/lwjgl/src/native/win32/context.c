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
 * $Id: context.c,v 1.8 2005/05/06 09:40:36 elias_naur Exp $
 *
 * Base Win32 display
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 1.8 $
 */

#include <malloc.h>
#include "common_tools.h"
#include "extgl.h"
#include "extgl_wgl.h"
#include "context.h"

extern HINSTANCE dll_handle;                     // Handle to the LWJGL dll

#define _CONTEXT_PRIVATE_CLASS_NAME "__lwjgl_context_class_name"

/*
 * Register the LWJGL window class.
 * Returns true for success, or false for failure
 */
bool registerWindow(WNDPROC win_proc, LPCTSTR class_name)
{
	WNDCLASS windowClass;
	windowClass.style = CS_OWNDC;
	windowClass.lpfnWndProc = win_proc;
	windowClass.cbClsExtra = 0;
	windowClass.cbWndExtra = 0;
	windowClass.hInstance = dll_handle;
	windowClass.hIcon = LoadIcon(NULL, IDI_APPLICATION);
	windowClass.hCursor = LoadCursor(NULL, IDC_ARROW);
	windowClass.hbrBackground = (HBRUSH)GetStockObject(BLACK_BRUSH);
	windowClass.lpszMenuName = NULL;
	windowClass.lpszClassName = class_name;

	if (RegisterClass(&windowClass) == 0) {
		printfDebug("Failed to register window class\n");
		return false;
	}
	return true;
}

static LRESULT CALLBACK dummyWindowProc(HWND hwnd, UINT msg, WPARAM wParam, LPARAM lParam) {
	return DefWindowProc(hwnd, msg, wParam, lParam);
}

bool applyPixelFormat(HDC hdc, int iPixelFormat) {
	PIXELFORMATDESCRIPTOR desc;
	if (DescribePixelFormat(hdc, iPixelFormat, sizeof(PIXELFORMATDESCRIPTOR), &desc) == 0) {
		return false;
	}

	// make that the pixel format of the device context 
	if (SetPixelFormat(hdc, iPixelFormat, &desc) == FALSE) {
		return false;
	}
	return true;
}

/*
 * Close the window
 */
void closeWindow(HWND *hwnd, HDC *hdc)
{
	// Release device context
	if (*hdc != NULL && *hwnd != NULL) {
		ReleaseDC(*hwnd, *hdc);
		*hdc = NULL;
	}

	// Close the window
	if (*hwnd != NULL) {
		ShowWindow(*hwnd, SW_HIDE);
		DestroyWindow(*hwnd);
		*hwnd = NULL;
	}
}

void getWindowFlags(DWORD *windowflags_return, DWORD *exstyle_return, bool fullscreen, bool undecorated) {
	DWORD exstyle, windowflags;
	if (fullscreen) {
		exstyle = WS_EX_APPWINDOW | WS_EX_TOPMOST;
		windowflags = WS_POPUP;
	} else if (undecorated) {
		exstyle = WS_EX_APPWINDOW;
		windowflags = WS_POPUP;
	} else {
		exstyle = WS_EX_APPWINDOW;
		windowflags = WS_OVERLAPPED | WS_BORDER | WS_CAPTION | WS_MINIMIZEBOX | WS_SYSMENU;
	}
	windowflags = windowflags | WS_CLIPCHILDREN | WS_CLIPSIBLINGS;
	*windowflags_return = windowflags;
	*exstyle_return = exstyle;
}

/*
 * Create a window with the specified title, position, size, and
 * fullscreen attribute. The window will have DirectInput associated
 * with it.
 * 
 * Returns true for success, or false for failure
 */
HWND createWindow(LPCTSTR window_class_name, int x, int y, int width, int height, bool fullscreen, bool undecorated)
{
	RECT clientSize;
	DWORD exstyle, windowflags;
	HWND new_hwnd;
	
	getWindowFlags(&windowflags, &exstyle, fullscreen, undecorated);

	// If we're not a fullscreen window, adjust the height to account for the
	// height of the title bar (unless undecorated)
	clientSize.bottom = height;
	clientSize.left = 0;
	clientSize.right = width;
	clientSize.top = 0;
	
	AdjustWindowRectEx(
	  &clientSize,    // client-rectangle structure
	  windowflags,    // window styles
	  FALSE,       // menu-present option
	  exstyle   // extended window style
	);
	// Create the window now, using that class:
	new_hwnd = CreateWindowEx (
			exstyle, 
			window_class_name,
			"",
			windowflags,
			x, y, clientSize.right - clientSize.left, clientSize.bottom - clientSize.top,
			NULL,
			NULL,
			dll_handle,
			NULL);

	return new_hwnd;
}

static int findPixelFormatARBFromBPP(JNIEnv *env, HDC hdc, WGLExtensions *extensions, jobject pixel_format, jobject pixelFormatCaps, int bpp, bool window, bool pbuffer, bool double_buffer) {
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int alpha = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "alpha", "I"));
	int depth = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "depth", "I"));
	int stencil = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "stencil", "I"));
	int samples = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "samples", "I"));
	int num_aux_buffers = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "num_aux_buffers", "I"));
	int accum_bpp = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "accum_bpp", "I"));
	int accum_alpha = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "accum_alpha", "I"));
	jboolean stereo = (*env)->GetBooleanField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "stereo", "Z"));
	int iPixelFormat;
	unsigned int num_formats_returned;
	attrib_list_t attrib_list;
	GLuint *pixelFormatCaps_ptr;
	jlong pixelFormatCapsSize;
	BOOL result;
	jlong i;

	initAttribList(&attrib_list);
	if (window) {
		putAttrib(&attrib_list, WGL_DRAW_TO_WINDOW_ARB); putAttrib(&attrib_list, TRUE);
	} 
	if (pbuffer) {
		putAttrib(&attrib_list, WGL_DRAW_TO_PBUFFER_ARB); putAttrib(&attrib_list, TRUE);
	}
	if (!getBooleanProperty(env, "org.lwjgl.opengl.Display.allowSoftwareOpenGL"))
		putAttrib(&attrib_list, WGL_ACCELERATION_ARB); putAttrib(&attrib_list, WGL_FULL_ACCELERATION_ARB);
	putAttrib(&attrib_list, WGL_PIXEL_TYPE_ARB); putAttrib(&attrib_list, WGL_TYPE_RGBA_ARB);
	putAttrib(&attrib_list, WGL_DOUBLE_BUFFER_ARB); putAttrib(&attrib_list, double_buffer ? TRUE : FALSE);
	putAttrib(&attrib_list, WGL_SUPPORT_OPENGL_ARB); putAttrib(&attrib_list, TRUE);
	putAttrib(&attrib_list, WGL_COLOR_BITS_ARB); putAttrib(&attrib_list, bpp);
	putAttrib(&attrib_list, WGL_ALPHA_BITS_ARB); putAttrib(&attrib_list, alpha);
	putAttrib(&attrib_list, WGL_DEPTH_BITS_ARB); putAttrib(&attrib_list, depth);
	putAttrib(&attrib_list, WGL_STENCIL_BITS_ARB); putAttrib(&attrib_list, stencil);
	// Assume caller checked extension availability
	if (samples > 0) {
		putAttrib(&attrib_list, WGL_SAMPLE_BUFFERS_ARB); putAttrib(&attrib_list, 1);
		putAttrib(&attrib_list, WGL_SAMPLES_ARB); putAttrib(&attrib_list, samples);
	}
	putAttrib(&attrib_list, WGL_ACCUM_BITS_ARB); putAttrib(&attrib_list, accum_bpp);
	putAttrib(&attrib_list, WGL_ACCUM_ALPHA_BITS_ARB); putAttrib(&attrib_list, accum_alpha);
	putAttrib(&attrib_list, WGL_STEREO_ARB); putAttrib(&attrib_list, stereo ? TRUE : FALSE);
	putAttrib(&attrib_list, WGL_AUX_BUFFERS_ARB); putAttrib(&attrib_list, num_aux_buffers);
	// Assume caller checked extension availability
	if ( pixelFormatCaps != NULL ) {
		pixelFormatCaps_ptr = (GLuint *)(*env)->GetDirectBufferAddress(env, pixelFormatCaps);
		pixelFormatCapsSize = (*env)->GetDirectBufferCapacity(env, pixelFormatCaps);

		for (i = 0; i < pixelFormatCapsSize; i++)
			putAttrib(&attrib_list, pixelFormatCaps_ptr[i]);
	}
	putAttrib(&attrib_list, 0); putAttrib(&attrib_list, 0);
	result = extensions->wglChoosePixelFormatARB(hdc, attrib_list.attribs, NULL, 1, &iPixelFormat, &num_formats_returned);

	if (result == FALSE || num_formats_returned < 1) {
		return -1;
	}
	return iPixelFormat;
}

static int findPixelFormatARB(JNIEnv *env, HDC hdc, WGLExtensions *extensions, jobject pixel_format, jobject pixelFormatCaps, bool use_hdc_bpp, bool window, bool pbuffer, bool double_buffer) {
	int bpp;
	int iPixelFormat;
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	if (use_hdc_bpp) {
		bpp = GetDeviceCaps(hdc, BITSPIXEL);
		iPixelFormat = findPixelFormatARBFromBPP(env, hdc, extensions, pixel_format, pixelFormatCaps, bpp, window, pbuffer, double_buffer);
		if (iPixelFormat == -1)
			bpp = 16;
		else
			return iPixelFormat;
	} else
		bpp = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "bpp", "I"));
	return findPixelFormatARBFromBPP(env, hdc, extensions, pixel_format, pixelFormatCaps, bpp, window, pbuffer, double_buffer);
}

/*
 * Find an appropriate pixel format
 */
static int findPixelFormatFromBPP(JNIEnv *env, HDC hdc, jobject pixel_format, int bpp, bool double_buffer)
{
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int alpha = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "alpha", "I"));
	int depth = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "depth", "I"));
	int stencil = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "stencil", "I"));
	int samples = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "samples", "I"));
	int num_aux_buffers = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "num_aux_buffers", "I"));
	int accum_bpp = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "accum_bpp", "I"));
	int accum_alpha = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "accum_alpha", "I"));
	jboolean stereo = (*env)->GetBooleanField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "stereo", "Z"));
	unsigned int flags = PFD_DRAW_TO_WINDOW |   // support window 
		PFD_SUPPORT_OPENGL |
		(double_buffer ? PFD_DOUBLEBUFFER : 0) |
		(stereo ? PFD_STEREO : 0);
	PIXELFORMATDESCRIPTOR desc;
	int iPixelFormat;
	PIXELFORMATDESCRIPTOR pfd = { 
		sizeof(PIXELFORMATDESCRIPTOR),   // size of this pfd 
		1,                     // version number 
		flags,         // RGBA type 
		PFD_TYPE_RGBA,
		(BYTE)bpp,       
		0, 0, 0, 0, 0, 0,      // color bits ignored 
		(BYTE)alpha,       
		0,                     // shift bit ignored 
		accum_bpp + accum_alpha,                     // no accumulation buffer 
		0, 0, 0, 0,            // accum bits ignored 
		(BYTE)depth,       
		(BYTE)stencil,     
		num_aux_buffers, 
		PFD_MAIN_PLANE,        // main layer
		0,                     // reserved 
		0, 0, 0                // layer masks ignored
	};
	// get the best available match of pixel format for the device context  
	iPixelFormat = ChoosePixelFormat(hdc, &pfd);
	if (iPixelFormat == 0) {
		printfDebugJava(env, "Failed to choose pixel format");
		return -1;
	}

	if (DescribePixelFormat(hdc, iPixelFormat, sizeof(PIXELFORMATDESCRIPTOR), &desc) == 0) {
		printfDebugJava(env, "Could not describe pixel format");
		return -1;
	}

	if (desc.cColorBits < bpp) {
		printfDebugJava(env, "Insufficient color precision");
		return -1;
	}

	if (desc.cAlphaBits < alpha) {
		printfDebugJava(env, "Insufficient alpha precision");
		return -1;
	}

	if (desc.cStencilBits < stencil) {
		printfDebugJava(env, "Insufficient stencil precision");
		return -1;
	}

	if (desc.cDepthBits < depth) {
		printfDebugJava(env, "Insufficient depth buffer precision");
		return -1;
	}

	if ((desc.dwFlags & PFD_GENERIC_FORMAT) != 0 || (desc.dwFlags & PFD_GENERIC_ACCELERATED) != 0) {
		jboolean allowSoftwareOpenGL = getBooleanProperty(env, "org.lwjgl.opengl.Display.allowSoftwareOpenGL");
		// secondary check for software override
		if(!allowSoftwareOpenGL) {
			printfDebugJava(env, "Pixel format not accelerated");
			return -1;
		}
	}

	if ((desc.dwFlags & flags) != flags) {
		printfDebugJava(env, "Capabilities not supported");
		return -1;
	}
	return iPixelFormat;
}

static int findPixelFormatDefault(JNIEnv *env, HDC hdc, jobject pixel_format, bool use_hdc_bpp, bool double_buffer) {
	int bpp;
	int iPixelFormat;
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	if (use_hdc_bpp) {
		bpp = GetDeviceCaps(hdc, BITSPIXEL);
		iPixelFormat = findPixelFormatFromBPP(env, hdc, pixel_format, bpp, double_buffer);
		if (iPixelFormat == -1)
			bpp = 16;
		else
			return iPixelFormat;
	} else
		bpp = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "bpp", "I"));
	return findPixelFormatFromBPP(env, hdc, pixel_format, bpp, double_buffer);
}

int findPixelFormatOnDC(JNIEnv *env, HDC hdc, jobject pixel_format, jobject pixelFormatCaps, bool use_hdc_bpp, bool window, bool pbuffer, bool double_buffer) {
	HGLRC dummy_hglrc;
	HDC saved_current_hdc;
	HGLRC saved_current_hglrc;
	WGLExtensions extensions;
	int pixel_format_id;
	jclass cls_pixel_format = (*env)->GetObjectClass(env, pixel_format);
	int samples = (int)(*env)->GetIntField(env, pixel_format, (*env)->GetFieldID(env, cls_pixel_format, "samples", "I"));
	bool use_arb_selection = samples > 0 || pbuffer || pixelFormatCaps != NULL;
	pixel_format_id = findPixelFormatDefault(env, hdc, pixel_format, use_hdc_bpp, double_buffer);
	if (use_arb_selection) {
		if (!applyPixelFormat(hdc, pixel_format_id)) {
			throwException(env, "Could not apply pixel format to window");
			return -1;
		}
		dummy_hglrc = wglCreateContext(hdc);
		if (dummy_hglrc == NULL) {
			throwException(env, "Failed to create OpenGL rendering context");
			return -1;
		}
		// Save the current HDC and HGLRC to avoid disruption
		saved_current_hdc = wglGetCurrentDC();
		saved_current_hglrc = wglGetCurrentContext();
		if (!wglMakeCurrent(hdc, dummy_hglrc)) {
			wglMakeCurrent(saved_current_hdc, saved_current_hglrc);
			wglDeleteContext(dummy_hglrc);
			throwException(env, "Could not bind context to dummy window");
			return -1;
		}
		extgl_InitWGL(&extensions);
		
		if (!extensions.WGL_ARB_pixel_format) {
			wglMakeCurrent(saved_current_hdc, saved_current_hglrc);
			wglDeleteContext(dummy_hglrc);
			throwException(env, "No support for WGL_ARB_pixel_format");
			return -1;
		}
		if (samples > 0 && !extensions.WGL_ARB_multisample) {
			wglMakeCurrent(saved_current_hdc, saved_current_hglrc);
			wglDeleteContext(dummy_hglrc);
			throwException(env, "No support for WGL_ARB_multisample");
			return -1;
		}
		if (pixelFormatCaps != NULL && !extensions.WGL_ARB_render_texture) {
			wglMakeCurrent(saved_current_hdc, saved_current_hglrc);
			wglDeleteContext(dummy_hglrc);
			throwException(env, "No support for WGL_ARB_render_texture");
			return -1;
		}
		pixel_format_id = findPixelFormatARB(env, hdc, &extensions, pixel_format, pixelFormatCaps, use_hdc_bpp, window, pbuffer, double_buffer);
		wglMakeCurrent(saved_current_hdc, saved_current_hglrc);
		wglDeleteContext(dummy_hglrc);
	}
	if (pixel_format_id == -1) {
		throwException(env, "Could not find a valid pixel format");
	}
	return pixel_format_id;
}

static bool registerDummyWindow() {
	static bool window_registered = false;
	if (!window_registered) {
		if (!registerWindow(dummyWindowProc, _CONTEXT_PRIVATE_CLASS_NAME)) {
			return false;
		}
		window_registered = true;
	}
	return true;
}

HWND createDummyWindow(int origin_x, int origin_y) {
	if (!registerDummyWindow())
		return NULL;
	return createWindow(_CONTEXT_PRIVATE_CLASS_NAME, origin_x, origin_y, 1, 1, false, false);
}

int findPixelFormat(JNIEnv *env, int origin_x, int origin_y, jobject pixel_format, jobject pixelFormatCaps, bool use_hdc_bpp, bool window, bool pbuffer, bool double_buffer) {
	HWND dummy_hwnd;
	HDC dummy_hdc;
	int pixel_format_id;
	dummy_hwnd = createDummyWindow(origin_x, origin_y);
	if (dummy_hwnd == NULL) {
		throwException(env, "Failed to create the dummy window.");
		return -1;
	}
	dummy_hdc = GetDC(dummy_hwnd);
	pixel_format_id = findPixelFormatOnDC(env, dummy_hdc, pixel_format, pixelFormatCaps, use_hdc_bpp, window, pbuffer, double_buffer);
	closeWindow(&dummy_hwnd, &dummy_hdc);
	return pixel_format_id;
}
