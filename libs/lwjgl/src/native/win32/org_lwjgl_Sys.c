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
 * $Id: org_lwjgl_Sys.c,v 1.11 2005/05/12 07:47:07 elias_naur Exp $
 *
 * Win32 system library.
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 1.11 $
 */

#include "Window.h"
#include "mmsystem.h"
#include "org_lwjgl_NativeSysImplementation.h"
#include "common_tools.h"
#include <malloc.h>

/*
 * Class:     org_lwjgl_Sys
 * Method:    getTimerResolution
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_NativeSysImplementation_getTimerResolution
  (JNIEnv * env, jobject ignored)
{
	return (jlong) 1000L;
}

/*
 * Class:     org_lwjgl_Sys
 * Method:    ngetTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_org_lwjgl_NativeSysImplementation_getTime
  (JNIEnv * env, jobject ignored)
{

	MMRESULT result;
	DWORD time;

	result = timeBeginPeriod(1);
	time = timeGetTime();
	result = timeEndPeriod(1);

	return time;
}

/*
 * Class:     org_lwjgl_Sys
 * Method:    alert
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_NativeSysImplementation_alert
  (JNIEnv * env, jobject ignored, jstring title, jstring message)
{
	char * eMessageText = GetStringNativeChars(env, message);
	char * cTitleBarText = GetStringNativeChars(env, title);
	MessageBox(getCurrentHWND(), eMessageText, cTitleBarText, MB_OK | MB_TOPMOST);

	printfDebugJava(env, "*** Alert ***%s\n%s\n", cTitleBarText, eMessageText);
	
	free(eMessageText);
	free(cTitleBarText);
}

/*
 * Class:     org_lwjgl_Sys
 * Method:    openURL
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_NativeSysImplementation_openURL
  (JNIEnv * env, jobject ignored, jstring url)
{
	STARTUPINFO si;
	PROCESS_INFORMATION pi;

	char * urlString = GetStringNativeChars(env, url);

	char command[256];
	strcpy(command, "");
	strcat(command, "rundll32 url.dll,FileProtocolHandler ");
	strncat(command, urlString, 200); // Prevent buffer overflow
	free(urlString);

	ZeroMemory( &si, sizeof(si) );
	si.cb = sizeof(si);
	ZeroMemory( &pi, sizeof(pi) );

	// Start the child process. 
	if( !CreateProcess( NULL, // No module name (use command line). 
		command,		  // Command line. 
		NULL,			 // Process handle not inheritable. 
		NULL,			 // Thread handle not inheritable. 
		FALSE,			// Set handle inheritance to FALSE. 
		0,				// No creation flags. 
		NULL,			 // Use parent's environment block. 
		NULL,			 // Use parent's starting directory. 
		&si,			  // Pointer to STARTUPINFO structure.
		&pi )			 // Pointer to PROCESS_INFORMATION structure.
	) 
	{
		printfDebugJava(env, "Failed to open URL %s", urlString);
		return JNI_FALSE;
	}

	// Close process and thread handles. 
	CloseHandle( pi.hProcess );
	CloseHandle( pi.hThread );

	return JNI_TRUE;
}



const void * getClipboard(int type)
{

	void * ret;
	HANDLE hglb;
	// Open the clipboard
	if (!OpenClipboard(NULL)) 
		return NULL; 

	hglb = GetClipboardData(type); 
	if (hglb != NULL) { 
		ret = GlobalLock(hglb); 
		if (ret != NULL) { 
			GlobalUnlock(hglb); 
		} 
	} 

	// Close the clipboard now we're done
	CloseClipboard(); 

	return ret;

}

JNIEXPORT jstring JNICALL Java_org_lwjgl_NativeSysImplementation_getClipboard
  (JNIEnv * env, jobject ignored)
{
	// Check to see if there's text available in the clipboard
	BOOL textAvailable = IsClipboardFormatAvailable(CF_TEXT);
	BOOL unicodeAvailable = IsClipboardFormatAvailable(CF_UNICODETEXT);

	if (unicodeAvailable) {
		const wchar_t * str = (const wchar_t *) getClipboard(CF_UNICODETEXT);
		return (*env)->NewString(env, str, wcslen(str));
	} else if (textAvailable) {
		return NewStringNative(env, (const char *) getClipboard(CF_TEXT));
	} else {
		return NULL;
	}
}
