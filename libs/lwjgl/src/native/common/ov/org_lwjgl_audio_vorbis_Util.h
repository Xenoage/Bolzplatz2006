/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_audio_vorbis_Util */

#ifndef _Included_org_lwjgl_audio_vorbis_Util
#define _Included_org_lwjgl_audio_vorbis_Util
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_lwjgl_audio_vorbis_Util
 * Method:    C_CreateStreamFromFile
 * Signature: (Ljava/lang/String;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateStreamFromFile
  (JNIEnv *, jclass, jstring);

/*
 * Class:     org_lwjgl_audio_vorbis_Util
 * Method:    C_CreateStreamFromMemory
 * Signature: (Ljava/nio/ByteBuffer;)Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateStreamFromMemory
  (JNIEnv *, jclass, jobject);

/*
 * Class:     org_lwjgl_audio_vorbis_Util
 * Method:    CreateALBufferFromFile
 * Signature: (Ljava/lang/String;I)Ljava/lang/Integer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_CreateALBufferFromFile
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     org_lwjgl_audio_vorbis_Util
 * Method:    C_CreateALBufferFromMemory
 * Signature: (Ljava/nio/ByteBuffer;I)Ljava/lang/Integer;
 */
JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateALBufferFromMemory
  (JNIEnv *, jclass, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
