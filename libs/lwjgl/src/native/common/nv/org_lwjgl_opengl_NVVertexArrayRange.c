/* MACHINE GENERATED FILE, DO NOT EDIT */

#include <jni.h>
#include "extgl.h"

typedef void (APIENTRY *glFreeMemoryNVPROC) (GLvoid * pointer);
typedef GLvoid * (APIENTRY *glAllocateMemoryNVPROC) (GLint size, GLfloat readFrequency, GLfloat writeFrequency, GLfloat priority);
typedef void (APIENTRY *glFlushVertexArrayRangeNVPROC) ();
typedef void (APIENTRY *glVertexArrayRangeNVPROC) (GLsizei size, const GLvoid * pPointer);

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexArrayRange_nglFreeMemoryNV(JNIEnv *env, jclass clazz, jobject pointer, jint pointer_position, jlong function_pointer) {
	GLvoid *pointer_address = ((GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pointer)) + pointer_position));
	glFreeMemoryNVPROC glFreeMemoryNV = (glFreeMemoryNVPROC)((intptr_t)function_pointer);
	glFreeMemoryNV(pointer_address);
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_opengl_NVVertexArrayRange_nglAllocateMemoryNV(JNIEnv *env, jclass clazz, jint size, jfloat readFrequency, jfloat writeFrequency, jfloat priority, jint result_size, jlong function_pointer) {
	glAllocateMemoryNVPROC glAllocateMemoryNV = (glAllocateMemoryNVPROC)((intptr_t)function_pointer);
	GLvoid * __result = glAllocateMemoryNV(size, readFrequency, writeFrequency, priority);
	return safeNewBuffer(env, __result, result_size);
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexArrayRange_nglFlushVertexArrayRangeNV(JNIEnv *env, jclass clazz, jlong function_pointer) {
	glFlushVertexArrayRangeNVPROC glFlushVertexArrayRangeNV = (glFlushVertexArrayRangeNVPROC)((intptr_t)function_pointer);
	glFlushVertexArrayRangeNV();
}

JNIEXPORT void JNICALL Java_org_lwjgl_opengl_NVVertexArrayRange_nglVertexArrayRangeNV(JNIEnv *env, jclass clazz, jint size, jobject pPointer, jint pPointer_position, jlong function_pointer) {
	const GLvoid *pPointer_address = ((const GLvoid *)(((char *)(*env)->GetDirectBufferAddress(env, pPointer)) + pPointer_position));
	glVertexArrayRangeNVPROC glVertexArrayRangeNV = (glVertexArrayRangeNVPROC)((intptr_t)function_pointer);
	glVertexArrayRangeNV(size, pPointer_address);
}

