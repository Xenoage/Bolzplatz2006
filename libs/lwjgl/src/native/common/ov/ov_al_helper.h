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

#ifndef _INCLUDED_OV_AL_HELPER_H_
#define _INCLUDED_OV_AL_HELPER_H_

#include "extal.h"
#include <vorbis/vorbisfile.h>
#include "ov_callback_mem.h"
#include <jni.h>

ALenum lwjgl_audio_ov_al_get_format(unsigned int channels, unsigned int sample_bytes);
size_t lwjgl_audio_ov_al_vorbis_readfill(OggVorbis_File* ov_file, size_t pcm_size, void* buffer, int* bitstream, unsigned int sample_bytes);
int lwjgl_audio_ov_al_buffer_from_vorbis(OggVorbis_File* ov_file, unsigned int sample_bytes, ALuint* out_al_buffer);
int lwjgl_audio_ov_al_vorbis_open_from_mem(OggVorbis_File* ov_file, struct audioVorbisMemSource* mem, JNIEnv *env, jobject data_buffer);
#endif
