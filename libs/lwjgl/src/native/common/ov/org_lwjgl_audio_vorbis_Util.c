/* 
Copyright (c) 2005, Phillip Michael Jordan
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
- Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
- Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
- The names of its contributors may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
Phillip Jordan
phillip.m.jordan@gmail.com
http://www-users.york.ac.uk/~pmj110/software/lwjgl/
*/

/* Native implementation for class org_lwjgl_audio_vorbis_Util */
#include "org_lwjgl_audio_vorbis_Util.h"

/* these are part of LWJGL. Please adjust your include path accordingly, if necessary. */
#include "extal.h"

#include <stdio.h>
#include "ov_callback_mem.h"
#include <vorbis/vorbisfile.h>
#include "ov_al_helper.h"

#include "ov_al_datastream.h"

JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_CreateALBufferFromFile
  (JNIEnv *env, jclass me, jstring j_filename, jint sample_size)
{
	unsigned int sample_bytes;
	int res = 0;
	OggVorbis_File ov_file;
	const char* filename;
	FILE* file;
	ALuint al_buffer;
	
	if (sample_size == 16)
	{
		sample_bytes = 2;
	}
	else if (sample_size == 8)
	{
		sample_bytes = 1;
	}
	else
	{
		/* vorbisfile only supports 8 or 16 bit output. */
		return NULL;
	}
	
	filename = (*env)->GetStringUTFChars(env, j_filename, NULL);
	if (filename == NULL)
	{
		return NULL; /* OutOfMemoryError thrown */
	}
	
	file = fopen(filename, "rb");
	(*env)->ReleaseStringUTFChars(env, j_filename, filename);
	
	if (file == NULL)
	{
		/* probably file not found, or wrong permissions, etc. Either way, the journey ends here. */
		return NULL;
	}
	res = ov_open(file, &ov_file, NULL, 0);
	if (res != 0)
	{
		/* failed to open Ogg Vorbis stream */
		fclose(file);
		return NULL;
	}
	
	res = lwjgl_audio_ov_al_buffer_from_vorbis(&ov_file, sample_bytes, &al_buffer);
	/* close down the stream */
	ov_clear(&ov_file);
	if (res == 0)
	{
		jclass classid = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID mid = (*env)->GetMethodID(env, classid, "<init>", "(I)V");
		/* create java.lang.Integer object */
		return (*env)->NewObject(env, classid, mid, al_buffer);
	}
	else
		return NULL;

}

JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateALBufferFromMemory
	(JNIEnv *env, jclass me, jobject source_data, jint sample_size)
{
	unsigned int sample_bytes;
	
	OggVorbis_File ov_file;
	struct audioVorbisMemSource mem;
	int res = 0;
	ALuint al_buffer;
	
	if (sample_size == 16)
	{
		sample_bytes = 2;
	}
	else if (sample_size == 8)
	{
		sample_bytes = 1;
	}
	else
	{
		/* vorbisfile only supports 8 or 16 bit output. */
		return NULL;
	}
	
	/* open memory as an Ogg Vorbis stream */
	res = lwjgl_audio_ov_al_vorbis_open_from_mem(&ov_file, &mem, env, source_data);
	if (res != 0)
	{
		return NULL;
	}
	
	res = lwjgl_audio_ov_al_buffer_from_vorbis(&ov_file, sample_bytes, &al_buffer);
	/* close down the stream */
	ov_clear(&ov_file);
	if (res == 0)
	{
		jclass classid = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID mid = (*env)->GetMethodID(env, classid, "<init>", "(I)V");
		/* create java.lang.Integer object */
		return (*env)->NewObject(env, classid, mid, al_buffer);
	}
	else
		return NULL;
}

jobject lwjgl_audio_ov_al_CreateBufferFromStreamData(JNIEnv* env, struct lwjgl_audio_vorbis_DataStream* data)
{
	vorbis_info* ov_stream_info;
	jobject c_data_buf = NULL;
	/* get information about the stream */
	ov_stream_info = ov_info(&data->ov_stream, -1);
	if (ov_stream_info == NULL)
	{
		printf("Error: Bitstream badly initialised, or something. This should never happen.\n");
		return NULL;
	}
	
	data->pcm_rate = ov_stream_info->rate;
	data->channels = ov_stream_info->channels;
	data->at_end = 0;
	
	/* assign the newly allocated space to a direct byte buffer */
	c_data_buf = (*env)->NewDirectByteBuffer(env, (void*)data, sizeof(struct lwjgl_audio_vorbis_DataStream));
	return c_data_buf;
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateStreamFromFile
	(JNIEnv *env, jclass me, jstring j_filename)
{
	jobject c_data_buf = NULL;
	int res = 0;
	struct lwjgl_audio_vorbis_DataStream* data;
	const char* filename;
	FILE* file;
	
	filename = (*env)->GetStringUTFChars(env, j_filename, NULL);
	if (filename == NULL)
	{
		return NULL; /* OutOfMemoryError thrown */
	}
	
	file = fopen(filename, "rb");
	(*env)->ReleaseStringUTFChars(env, j_filename, filename);
	
	if (file == NULL)
	{
		/* probably file not found, or wrong permissions, etc. Either way, the journey ends here. */
		return NULL;
	}
	
	data = malloc(sizeof(struct lwjgl_audio_vorbis_DataStream));
	res = ov_open(file, &data->ov_stream, NULL, 0);
	if (res != 0)
	{
		/* failed to open Ogg Vorbis stream */
		fclose(file);
		free(data);
		return NULL;
	}
	
	c_data_buf = lwjgl_audio_ov_al_CreateBufferFromStreamData(env, data);
	if (!c_data_buf)
	{
		ov_clear(&data->ov_stream);
		free(data);
	}
	return c_data_buf;
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_Util_C_1CreateStreamFromMemory
	(JNIEnv * env, jclass me, jobject source_data)
{
	jobject c_data_buf = NULL;
	int res = 0;
	struct lwjgl_audio_vorbis_DataStream* data = malloc(sizeof(struct lwjgl_audio_vorbis_DataStream));
	
	/* open memory as an Ogg Vorbis stream */
	res = lwjgl_audio_ov_al_vorbis_open_from_mem(&data->ov_stream, &data->mem_stream, env, source_data);
	if (res != 0)
	{
		free(data);
		return NULL;
	}
	
	c_data_buf = lwjgl_audio_ov_al_CreateBufferFromStreamData(env, data);
	if (!c_data_buf)
	{
		ov_clear(&data->ov_stream);
		free(data);
	}
	return c_data_buf;
}
