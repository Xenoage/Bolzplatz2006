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

#include "ov_callback_mem.h"

#include <stddef.h>
#include <memory.h>

size_t mem_read(void* ptr, size_t size, size_t count, void* datasource)
{
	struct audioVorbisMemSource* pSrc = (struct audioVorbisMemSource*)datasource;
	ogg_int64_t amount = size * count;
	size_t chunks = count;
	if (amount > pSrc->length - pSrc->offset)
	{
		amount = pSrc->length - pSrc->offset;
		chunks = (size_t) (amount / size);
		amount -= amount % size;
	}
	memcpy(ptr, (char*)pSrc->data + pSrc->offset, (size_t)amount);
	pSrc->offset += amount;
	return chunks;
}

int mem_seek(void* datasource, ogg_int64_t offset, int whence)
{
	struct audioVorbisMemSource* pSrc = (struct audioVorbisMemSource*) datasource;
	switch (whence)
	{
	case SEEK_SET:
		pSrc->offset = offset < 0 ? 0 : (offset > pSrc->length ? pSrc->length : offset);
		return 0;
	case SEEK_CUR:
		pSrc->offset = (offset + pSrc->offset < 0) ? 0 : ((offset + pSrc->offset > pSrc->length) ? pSrc->length : offset + pSrc->offset);
		return 0;
	case SEEK_END:
		pSrc->offset = (offset > 0) ? pSrc->length : ((offset + pSrc->length < 0) ? 0 : offset + pSrc->length);
		return 0;
	}
	return -1;
}

int mem_close(void* datasource)
{
	return 0;
}
long mem_tell(void* datasource)
{
	struct audioVorbisMemSource* pSrc = (struct audioVorbisMemSource*)datasource;
	return (long)pSrc->offset;
}
