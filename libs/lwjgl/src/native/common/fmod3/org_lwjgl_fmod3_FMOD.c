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
 
#include "org_lwjgl_fmod3_FMOD.h"
#include "extfmod3.h"

static const char* VERSION = "0.97";

/**
 * Concatenate two strings
 */
static char *concatenate(const char *str1, const char *str2) {
	int length1 = strlen(str1);
	int length2 = strlen(str2);
	char *str = (char *)calloc(length1 + length2 + 1, sizeof(char));
	strncpy(str, str1, length1);
	strncpy(str + length1, str2, length2 + 1);
	return str;
}

/*
 * Class:     org_lwjgl_fmod3_FMOD
 * Method:    getNativeLibraryVersion
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_lwjgl_fmod3_FMOD_getNativeLibraryVersion(JNIEnv * env, jclass clazz) {
  return NewStringNative(env, VERSION);
}

/*
 * Class:     org_lwjgl_fmod3_FMOD
 * Method:    nCreate
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_fmod3_FMOD_nCreate(JNIEnv *env, jclass clazz, jobjectArray paths) {
	jsize pathcount = (*env)->GetArrayLength(env, paths);
	int i;
	jstring path;
	char *path_str;
	char *lib_str;
	
	for(i=0;i<pathcount;i++) {
		path = (jstring) (*env)->GetObjectArrayElement(env, paths, i);
		path_str = GetStringNativeChars(env, path);
#ifdef _WIN32
		lib_str = concatenate(path_str, "fmod.dll");
#endif
#ifdef _X11
		lib_str = concatenate(path_str, "libfmod.so");
#endif
#ifdef _MACOSX
		lib_str = concatenate(path_str, "ignored");
#endif	
		printfDebug("Testing '%s'\n", lib_str);
		fmod_create(env, lib_str);
		free(lib_str);
		free(path_str);

		if(fmod_instance != NULL) {
			return;
		}
	}
	throwFMODException(env, "Unable to load fmod library");
}

/*
 * Class:     org_lwjgl_fmod3_FMOD
 * Method:    nDestroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_fmod3_FMOD_nDestroy(JNIEnv *env, jclass clazz) {
  fmod_destroy();
}

/*
 * Class:     org_lwjgl_fmod3_FMOD
 * Method:    FMOD_ErrorString
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_lwjgl_fmod3_FMOD_FMOD_1ErrorString(JNIEnv *env, jclass clazz, jint errorcode) {
  return NewStringNative(env, FMOD_ErrorString(errorcode));  
}
