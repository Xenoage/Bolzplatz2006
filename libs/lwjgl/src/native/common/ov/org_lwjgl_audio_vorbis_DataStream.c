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

#include "org_lwjgl_audio_vorbis_DataStream.h"

#include "ov_al_helper.h"
#include "ov_al_datastream.h"

#include "../org_lwjgl_openal_al10_fntypes.h"

extern alGenBuffersPROC alGenBuffers;
extern alDeleteBuffersPROC alDeleteBuffers;
extern alIsBufferPROC alIsBuffer;
extern alBufferDataPROC alBufferData;
extern alGetErrorPROC alGetError;
/** Retrieves the C data structure associated with the given DataStream object
 * Note: based on an example in the official JNI book.
 */
static struct lwjgl_audio_vorbis_DataStream* GetCData(JNIEnv* env, jobject stream)
{
	jobject obj_buffer;
	jfieldID fid;
	/* Get a reference to obj’s class */
	jclass cls = (*env)->GetObjectClass(env, stream);
	
	/* Look for the instance field s in cls */
	fid = (*env)->GetFieldID(env, cls, "c_data", "Ljava/nio/ByteBuffer;");
	if (fid == NULL)
	{
		/* failed to find the field */
		return NULL; 
	}
	/* Read the instance field s */
	obj_buffer = (*env)->GetObjectField(env, stream, fid);
	
	return (struct lwjgl_audio_vorbis_DataStream*)(*env)->GetDirectBufferAddress(env, obj_buffer);
}


static int SampleSizeInBytes(int sample_size)
{
	return (sample_size == 16 ? 2 : (sample_size == 8 ? 1 : -1));
}

JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_DataStream_createALBuffer
  (JNIEnv * env, jobject me, jint length, jint sample_size)
{
	ALuint al_buffer;
	
	alGenBuffers(1, &al_buffer);
	
	if (JNI_TRUE == Java_org_lwjgl_audio_vorbis_DataStream_fillALBuffer__III(env, me, al_buffer, length, sample_size))
	{
		jclass classid = (*env)->FindClass(env, "java/lang/Integer");
		jmethodID mid = (*env)->GetMethodID(env, classid, "<init>", "(I)V");
		/* create java.lang.Integer object */
		return (*env)->NewObject(env, classid, mid, al_buffer);
	}
	else
	{
		alDeleteBuffers(1, &al_buffer);
		return NULL;
	}
}

inline int lwjgl_audio_vorbis_DataStream_read_fillbuffer(struct lwjgl_audio_vorbis_DataStream* c_data, size_t pcm_size, int sample_bytes, ALenum al_format, ALenum al_buffer, void* dest)
{
	ALenum al_error;
	size_t read;
	read = lwjgl_audio_ov_al_vorbis_readfill(&c_data->ov_stream, pcm_size, dest, &c_data->ov_bitstream, sample_bytes);
	if (read < pcm_size)
		c_data->at_end = 1;
	
	if (read == 0)
	{
		return 0;
	}
	
	alBufferData(al_buffer, al_format, dest, read, c_data->pcm_rate);
	al_error = alGetError();
	return (al_error == AL_NO_ERROR) ? 1 : 0;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_audio_vorbis_DataStream_fillALBuffer__III
  (JNIEnv * env, jobject me, jint al_buffer, jint length, jint sample_size)
{
	int sample_bytes;
	size_t pcm_size;
	ALenum al_format;
	void* tmp;
	int res;
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);
	
	if (!alIsBuffer(al_buffer) || c_data->at_end || length <= 0)
		return JNI_FALSE;
	
	sample_bytes = SampleSizeInBytes(sample_size);
	if (sample_bytes <= 0)
		return JNI_FALSE;
	
	al_format = lwjgl_audio_ov_al_get_format(c_data->channels, sample_bytes);
	if (al_format == AL_INVALID_ENUM)
		return JNI_FALSE;
	
	/* length is in milliseconds, make sure we get an integer number of PCM samples though */
	pcm_size = c_data->pcm_rate * length;
	pcm_size /= 1000;
	pcm_size *= sample_bytes * c_data->channels;
	
	tmp = malloc(pcm_size);
	res = lwjgl_audio_vorbis_DataStream_read_fillbuffer(c_data, pcm_size, sample_bytes, al_format, al_buffer, tmp);
	free(tmp);
	return res == 0 ? JNI_FALSE : JNI_TRUE;
}


JNIEXPORT jobject JNICALL Java_org_lwjgl_audio_vorbis_DataStream_fillALBuffer__IIILjava_nio_ByteBuffer_2
		(JNIEnv *env, jobject me, jint al_buffer, jint length, jint sample_size, jobject working_buffer)
{
	int sample_bytes;
	size_t pcm_size;
	ALenum al_format;
	void* tmp;
	int res;
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);
	
	if (!alIsBuffer(al_buffer) || c_data->at_end || length <= 0)
		return JNI_FALSE;
	
	sample_bytes = SampleSizeInBytes(sample_size);
	if (sample_bytes <= 0)
		return JNI_FALSE;
	
	al_format = lwjgl_audio_ov_al_get_format(c_data->channels, sample_bytes);
	if (al_format == AL_INVALID_ENUM)
		return JNI_FALSE;
	
	/* length is in milliseconds, make sure we get an integer number of PCM samples though */
	pcm_size = c_data->pcm_rate * length;
	pcm_size /= 1000;
	pcm_size *= sample_bytes * c_data->channels;
	
	/* test whether we need to create a new buffer */
	if (working_buffer == NULL || pcm_size > (*env)->GetDirectBufferCapacity(env, working_buffer))
	{
		jclass cls = (*env)->FindClass(env, "java/nio/ByteBuffer");
		jmethodID mid = (*env)->GetStaticMethodID(env, cls, "allocateDirect", "(I)Ljava/nio/ByteBuffer;");
		if (mid == NULL)
		{
			printf("Method not found: BUG!\n");
			return NULL;
		}
		working_buffer = (*env)->CallStaticObjectMethod(env, cls, mid, pcm_size);
	}
		
	tmp = (*env)->GetDirectBufferAddress(env, working_buffer);
	res = lwjgl_audio_vorbis_DataStream_read_fillbuffer(c_data, pcm_size, sample_bytes, al_format, al_buffer, tmp);
	return res == 0 ? NULL : working_buffer;
}

JNIEXPORT jboolean JNICALL Java_org_lwjgl_audio_vorbis_DataStream_rewind
  (JNIEnv * env, jobject me)
{
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);

	return 0 == ov_pcm_seek(&c_data->ov_stream, 0) ? (c_data->at_end = 0, JNI_TRUE) : JNI_FALSE;
}

JNIEXPORT void JNICALL Java_org_lwjgl_audio_vorbis_DataStream_C_1FillStreamInfo
	(JNIEnv *env, jobject me, jobject info)
{
	int i;
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);
	vorbis_comment* comment = ov_comment(&c_data->ov_stream, -1);
	jclass stream_info;
	jobject meta_strings;
	jclass linked_list;
	jmethodID add_last;
	jmethodID construct_linked_list;
	jfieldID track_title, track_artist, meta_string_field, vendor_string, track_length;
	jstring str;
	int res, len;
	double length = ov_time_total(&c_data->ov_stream, -1);
	
	stream_info = (*env)->GetObjectClass(env, info);
	vendor_string = (*env)->GetFieldID(env, stream_info, "vendor_string", "Ljava/lang/String;");
	track_length = (*env)->GetFieldID(env, stream_info, "track_length", "I");
	if (vendor_string == NULL)
	{
		printf("Field vendor_string not found. Please recompile LWJGL.\n");
		return;
	}
	
	str = (*env)->NewStringUTF(env, comment->vendor);
	(*env)->SetObjectField(env, info, vendor_string, str);
	
	
	(*env)->SetIntField(env, info, track_length, (int)(length * 1000.0));
	
	if (comment->comments == 0)
		return;
	
	track_title = (*env)->GetFieldID(env, stream_info, "track_title", "Ljava/lang/String;");
	track_artist = (*env)->GetFieldID(env, stream_info, "track_artist", "Ljava/lang/String;");
	meta_string_field = (*env)->GetFieldID(env, stream_info, "meta_strings", "Ljava/util/LinkedList;");
	if (track_title == NULL || track_artist == NULL || meta_string_field == NULL)
	{
		printf("Field 'track_title', 'track_artist', or 'meta_strings' not found. Please recompile LWJGL.\n");
		return;
	}
	
	linked_list = (*env)->FindClass(env, "java/util/LinkedList");
	/*
	if (linked_list == NULL)
	{
		printf("Class 'java.util.LinkedList' not found. This should never ever happen.\n");
		return;
	}
	*/
	add_last = (*env)->GetMethodID(env, linked_list, "addLast", "(Ljava/lang/Object;)V");
	construct_linked_list = (*env)->GetMethodID(env, linked_list, "<init>", "()V");
	/*
	if (add_last == NULL || construct_linked_list == NULL)
	{
		printf("Method 'addLast' or the constructor for 'java.util.LinkedList' not found. This should never ever happen.\n");
		return;
	}
	*/
	/* construct new linked list to store this stuff in */
	meta_strings = (*env)->NewObject(env, linked_list, construct_linked_list);
	/*
	if (meta_strings == NULL)
	{
		printf("Constructing java.util.LinkedList failed. This should never happen.\n");
		return;
	}
	*/
	for (i = 0; i < comment->comments; ++i)
	{
		str = (*env)->NewStringUTF(env, comment->user_comments[i]);
		(*env)->CallVoidMethod(env, meta_strings, add_last, str);
		/* check if it's a known string */
		len = comment->comment_lengths[i];
		if (len >= 6)
		{
			res = strncmp(comment->user_comments[i], "TITLE=", 6);
			if (res == 0)
			{
				/* known title string */
				str = (*env)->NewStringUTF(env, comment->user_comments[i] + 6);
				(*env)->SetObjectField(env, info, track_title, str);
			}
			else if (len >= 7)
			{
				res = strncmp(comment->user_comments[i], "ARTIST=", 7);
				if (res == 0)
				{
					/* known title string */
					str = (*env)->NewStringUTF(env, comment->user_comments[i] + 7);
					(*env)->SetObjectField(env, info, track_artist, str);
				}
			}
		}
	}
	/* set the info object's linked list */
	(*env)->SetObjectField(env, info, meta_string_field, meta_strings);
	return;
}

JNIEXPORT void JNICALL Java_org_lwjgl_audio_vorbis_DataStream_finalize
  (JNIEnv * env, jobject me)
{
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);
	ov_clear(&(c_data->ov_stream));
	free(c_data);
}

JNIEXPORT jint JNICALL Java_org_lwjgl_audio_vorbis_DataStream_tellTime
	(JNIEnv * env, jobject me)
{
	struct lwjgl_audio_vorbis_DataStream* c_data = GetCData(env, me);
	double time = ov_time_tell(&c_data->ov_stream);
	return (int)(time * 1000.0);
}

