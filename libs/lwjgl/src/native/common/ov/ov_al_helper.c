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

#include "ov_al_helper.h"
#include "extal.h"
#include <vorbis/vorbisfile.h>
#include "ov_callback_mem.h"

#include "../org_lwjgl_openal_al10_fntypes.h"

static int extensions_queried = 0;
static int quadriphonic = 0;
static ALenum AL_FORMAT_QUAD8_LOKI;
static ALenum AL_FORMAT_QUAD16_LOKI;


void lwjgl_audio_ov_al_extensions_query(void);


extern alBufferDataPROC alBufferData;

extern alDeleteBuffersPROC alDeleteBuffers;
extern alGenBuffersPROC alGenBuffers;

extern alGetEnumValuePROC alGetEnumValue;
extern alIsExtensionPresentPROC alIsExtensionPresent;
extern alGetErrorPROC alGetError;


/** Given the size and number of channels in a sample, derive the required
 * OpenAL buffer format.
 * @param channels The number of channels in each sample. Currently supports
 * 1 (mono), 2 (stereo), and, if the extension AL_LOKI_quadriphonic is present,
 * 4 (quadriphonic).
 * @param sample_bytes how many bytes per channel, per sample are to be used.
 * OpenAL currently only supports 1 (8 bits) or 2 (16 bits)
 * @return AL_INVALID_ENUM if the format is not supported, or the corresponding
 * OpenAL format enum value.
 */
ALenum lwjgl_audio_ov_al_get_format(unsigned int channels, unsigned int sample_bytes)
{
	if (!extensions_queried)
		lwjgl_audio_ov_al_extensions_query();
	
	if (sample_bytes == 0 || sample_bytes > 2)
		return AL_INVALID_ENUM;
	
	/*al_format = pcm_channels == 2 ? (sample_bytes == 2 ? AL_FORMAT_STEREO16 : AL_FORMAT_STEREO8) : (sample_bytes == 2 ? AL_FORMAT_MONO16 : AL_FORMAT_MONO8);*/
	switch(channels)
	{
	case 1:
		return (sample_bytes == 2 ? AL_FORMAT_MONO16 : AL_FORMAT_MONO8);
		break;
	case 2:
		return (sample_bytes == 2 ? AL_FORMAT_STEREO16 : AL_FORMAT_STEREO8);
		break;
	case 4:
		if (quadriphonic)
			return (sample_bytes == 2 ? AL_FORMAT_QUAD16_LOKI : AL_FORMAT_QUAD8_LOKI);
		break;
	}
	return AL_INVALID_ENUM;
}

/** Query the implementation for all relevant OpenAL extensions, and set the
 * internal state accordingly. This only needs to be run once, use the static
 * variable extensions_queried to find out whether it needs to be run.
 */
void lwjgl_audio_ov_al_extensions_query(void)
{
	if (alIsExtensionPresent((ALubyte*)"AL_LOKI_quadriphonic"))
	{
		quadriphonic = 1;
		AL_FORMAT_QUAD8_LOKI = alGetEnumValue((ALubyte*)"AL_FORMAT_QUAD8_LOKI");
		AL_FORMAT_QUAD16_LOKI = alGetEnumValue((ALubyte*)"AL_FORMAT_QUAD16_LOKI");
	}
	else
	{
		quadriphonic = 0;
	}
	extensions_queried = 1;
}

size_t lwjgl_audio_ov_al_vorbis_readfill(OggVorbis_File* ov_file, size_t pcm_size, void* buffer, int* bitstream, unsigned int sample_bytes)
{
	char* cur = (char*)buffer;
	size_t remain = pcm_size;
	long read;
	
	/* begin decoding. Fill the entire temporary buffer with PCM data, then
	* create an OpenAL buffer based on it. */
	/* FIXME: Need to find out if endianness-----v   is an issue */
	read = ov_read(ov_file, cur, remain, 0, sample_bytes, 1, bitstream);
	while (read > 0 && read < remain)
	{
		remain -= read;
		cur += read;
		read = ov_read(ov_file, cur, remain, 0, sample_bytes, 1, bitstream);
	}
	remain -= read;
	return pcm_size - remain;
}

int lwjgl_audio_ov_al_buffer_from_vorbis(OggVorbis_File* ov_file, unsigned int sample_bytes, ALuint* out_al_buffer)
{
	vorbis_info* ov_stream_info;
	unsigned int pcm_channels;
	ogg_int64_t pcm_samples;
	size_t pcm_size;
	size_t remain;
	
	/* OpenAL buffer format */
	ALenum al_format;
	ALuint al_buffer;
	ALenum al_error;
	
	int bitstream = 0;
	
	void* dec;
	
	/* get information about the stream */
	ov_stream_info = ov_info(ov_file, -1);
	if (ov_stream_info == NULL)
	{
		printf("Error: Bitstream badly initialised, or something. This should never happen.\n");
		return -1;
	}
	pcm_samples = ov_pcm_total(ov_file, -1);
	if (pcm_samples == OV_EINVAL)
	{
		/* we can only create AL buffers directly from SEEKABLE bitstreams. Note: this should never cause trouble! */
		printf("Error: stream doesn't seem to be seekable, yet is in memory. This should never happen.\n");
		return -1;
	}
	
	/* calculate and allocate space necessary for decoding */
	pcm_channels = ov_stream_info->channels;
	pcm_size = pcm_samples * pcm_channels * sample_bytes;

	/* get corresponding OpenAL	buffer format */
	al_format = lwjgl_audio_ov_al_get_format(pcm_channels, sample_bytes);
	if (al_format == AL_INVALID_ENUM)
	{
		/* required format not supported */
		return -1;
	}
	
	dec = malloc(pcm_size);
	if (dec == NULL)
	{
		/* out of memory */
		return -1;
	}
	
	/* do the actual decoding */
	remain = pcm_size - lwjgl_audio_ov_al_vorbis_readfill(ov_file, pcm_size, dec, &bitstream, sample_bytes);
	
	if (remain > 0)
	{
		printf("Warning: End of bitstream reached before expected end. %ld bytes remaining.\n", (long int)remain);
	}
	
	alGenBuffers(1, &al_buffer);
	alBufferData(al_buffer, al_format, dec, pcm_size - remain, ov_stream_info->rate);
	al_error = alGetError();
	if (al_error != AL_NO_ERROR)
	{
		free(dec);
		alDeleteBuffers(1, &al_buffer);
		return -1;
	}
	free (dec);
	
	*out_al_buffer = al_buffer;
	return 0;
}

int lwjgl_audio_ov_al_vorbis_open_from_mem(OggVorbis_File* ov_file, struct audioVorbisMemSource* mem, JNIEnv *env, jobject data_buffer)
{
	ov_callbacks cb_mem;
	/* open the memory region as an Ogg Vorbis file. */
	/* need special callbacks to read from memory */
	cb_mem.read_func = mem_read;
	cb_mem.seek_func = mem_seek;
	cb_mem.tell_func = mem_tell;
	cb_mem.close_func = mem_close;
	
	/* FIXME: a warning for a cast from an integer appears here; I'm not sure why. */
	mem->data = (*env)->GetDirectBufferAddress(env, data_buffer);
	if (mem->data == NULL)
	{
		/* failure */
		return -1;
	}
	mem->offset = 0;
	mem->length = (*env)->GetDirectBufferCapacity(env, data_buffer);
	
	/* pass the region of memory to vorbisfile */
	return ov_open_callbacks((void*)mem, ov_file, NULL, 0, cb_mem);
}
