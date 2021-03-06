/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_lwjgl_devil_ILUT */

#ifndef _Included_org_lwjgl_devil_ILUT
#define _Included_org_lwjgl_devil_ILUT
#ifdef __cplusplus
extern "C" {
#endif
#undef org_lwjgl_devil_ILUT_ILUT_OPENGL_BIT
#define org_lwjgl_devil_ILUT_ILUT_OPENGL_BIT 1L
#undef org_lwjgl_devil_ILUT_ILUT_ALL_ATTRIB_BITS
#define org_lwjgl_devil_ILUT_ILUT_ALL_ATTRIB_BITS 1048575L
#undef org_lwjgl_devil_ILUT_ILUT_INVALID_ENUM
#define org_lwjgl_devil_ILUT_ILUT_INVALID_ENUM 1281L
#undef org_lwjgl_devil_ILUT_ILUT_OUT_OF_MEMORY
#define org_lwjgl_devil_ILUT_ILUT_OUT_OF_MEMORY 1282L
#undef org_lwjgl_devil_ILUT_ILUT_INVALID_VALUE
#define org_lwjgl_devil_ILUT_ILUT_INVALID_VALUE 1285L
#undef org_lwjgl_devil_ILUT_ILUT_ILLEGAL_OPERATION
#define org_lwjgl_devil_ILUT_ILUT_ILLEGAL_OPERATION 1286L
#undef org_lwjgl_devil_ILUT_ILUT_INVALID_PARAM
#define org_lwjgl_devil_ILUT_ILUT_INVALID_PARAM 1289L
#undef org_lwjgl_devil_ILUT_ILUT_COULD_NOT_OPEN_FILE
#define org_lwjgl_devil_ILUT_ILUT_COULD_NOT_OPEN_FILE 1290L
#undef org_lwjgl_devil_ILUT_ILUT_STACK_OVERFLOW
#define org_lwjgl_devil_ILUT_ILUT_STACK_OVERFLOW 1294L
#undef org_lwjgl_devil_ILUT_ILUT_STACK_UNDERFLOW
#define org_lwjgl_devil_ILUT_ILUT_STACK_UNDERFLOW 1295L
#undef org_lwjgl_devil_ILUT_ILUT_BAD_DIMENSIONS
#define org_lwjgl_devil_ILUT_ILUT_BAD_DIMENSIONS 1297L
#undef org_lwjgl_devil_ILUT_ILUT_NOT_SUPPORTED
#define org_lwjgl_devil_ILUT_ILUT_NOT_SUPPORTED 1360L
#undef org_lwjgl_devil_ILUT_ILUT_PALETTE_MODE
#define org_lwjgl_devil_ILUT_ILUT_PALETTE_MODE 1536L
#undef org_lwjgl_devil_ILUT_ILUT_OPENGL_CONV
#define org_lwjgl_devil_ILUT_ILUT_OPENGL_CONV 1552L
#undef org_lwjgl_devil_ILUT_ILUT_MAXTEX_WIDTH
#define org_lwjgl_devil_ILUT_ILUT_MAXTEX_WIDTH 1584L
#undef org_lwjgl_devil_ILUT_ILUT_MAXTEX_HEIGHT
#define org_lwjgl_devil_ILUT_ILUT_MAXTEX_HEIGHT 1585L
#undef org_lwjgl_devil_ILUT_ILUT_MAXTEX_DEPTH
#define org_lwjgl_devil_ILUT_ILUT_MAXTEX_DEPTH 1586L
#undef org_lwjgl_devil_ILUT_ILUT_GL_USE_S3TC
#define org_lwjgl_devil_ILUT_ILUT_GL_USE_S3TC 1588L
#undef org_lwjgl_devil_ILUT_ILUT_GL_GEN_S3TC
#define org_lwjgl_devil_ILUT_ILUT_GL_GEN_S3TC 1589L
#undef org_lwjgl_devil_ILUT_ILUT_GL_AUTODETECT_TEXTURE_TARGET
#define org_lwjgl_devil_ILUT_ILUT_GL_AUTODETECT_TEXTURE_TARGET 2055L
#undef org_lwjgl_devil_ILUT_ILUT_OPENGL
#define org_lwjgl_devil_ILUT_ILUT_OPENGL 0L
#undef org_lwjgl_devil_ILUT_ILUT_VENDOR
#define org_lwjgl_devil_ILUT_ILUT_VENDOR 7936L
#undef org_lwjgl_devil_ILUT_ILUT_VERSION_NUM
#define org_lwjgl_devil_ILUT_ILUT_VERSION_NUM 3554L
/* Inaccessible static: created */
/* Inaccessible static: class_00024org_00024lwjgl_00024devil_00024ILU */
/* Inaccessible static: class_00024org_00024lwjgl_00024devil_00024ILUT */
/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutRenderer
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutRenderer
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutDisable
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutDisable
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutEnable
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutEnable
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGetBoolean
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGetBoolean
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGetInteger
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_devil_ILUT_ilutGetInteger
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGetBooleanv
 * Signature: (ILjava/nio/ByteBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutGetBooleanv
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGetIntegerv
 * Signature: (ILjava/nio/IntBuffer;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutGetIntegerv
  (JNIEnv *, jclass, jint, jobject);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGetString
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_lwjgl_devil_ILUT_ilutGetString
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutInit
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutInit
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutIsDisabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutIsDisabled
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutIsEnabled
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutIsEnabled
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutPopAttrib
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutPopAttrib
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutPushAttrib
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutPushAttrib
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutSetInteger
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_ilutSetInteger
  (JNIEnv *, jclass, jint, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLBindTexImage
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_devil_ILUT_ilutGLBindTexImage
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLBindMipmaps
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_devil_ILUT_ilutGLBindMipmaps
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLBuildMipmaps
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLBuildMipmaps
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLLoadImage
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_org_lwjgl_devil_ILUT_ilutGLLoadImage
  (JNIEnv *, jclass, jstring);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLScreen
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLScreen
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLScreenie
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLScreenie
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLSaveImage
 * Signature: (Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLSaveImage
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLSetTex
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLSetTex
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    ilutGLTexImage
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_lwjgl_devil_ILUT_ilutGLTexImage
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    initNativeStubs
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_initNativeStubs
  (JNIEnv *, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    resetNativeStubs
 * Signature: (Ljava/lang/Class;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_resetNativeStubs
  (JNIEnv *, jclass, jclass);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    nCreate
 * Signature: ([Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_nCreate
  (JNIEnv *, jclass, jobjectArray);

/*
 * Class:     org_lwjgl_devil_ILUT
 * Method:    nDestroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_lwjgl_devil_ILUT_nDestroy
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
