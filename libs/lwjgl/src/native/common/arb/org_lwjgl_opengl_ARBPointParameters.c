/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glPointParameterfvARBPROC) (GLenum pname, GLfloat * pfParams);
typedef void (APIENTRY *glPointParameterfARBPROC) (GLenum pname, GLfloat param);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBPointParameters_nglPointParameterfvARB(JNIEnv *env, jclass clazz, jint pname, jobject pfParams, jint pfParams_position, jlong function_pointer) {
	GLfloat *pfParams_address = ((GLfloat *)(*env)->GetDirectBufferAddress(env, pfParams)) + pfParams_position;
	glPointParameterfvARBPROC glPointParameterfvARB = (glPointParameterfvARBPROC)((intptr_t)function_pointer);
	glPointParameterfvARB(pname, pfParams_address);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_ARBPointParameters_nglPointParameterfARB(JNIEnv *env, jclass clazz, jint pname, jfloat param, jlong function_pointer) {
	glPointParameterfARBPROC glPointParameterfARB = (glPointParameterfARBPROC)((intptr_t)function_pointer);
	glPointParameterfARB(pname, param);
}

