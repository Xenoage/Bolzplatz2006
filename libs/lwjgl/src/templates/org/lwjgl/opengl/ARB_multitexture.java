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
package org.lwjgl.opengl;

import org.lwjgl.util.generator.*;

public interface ARB_multitexture {
	int GL_TEXTURE0_ARB = 0x84C0;
	int GL_TEXTURE1_ARB = 0x84C1;
	int GL_TEXTURE2_ARB = 0x84C2;
	int GL_TEXTURE3_ARB = 0x84C3;
	int GL_TEXTURE4_ARB = 0x84C4;
	int GL_TEXTURE5_ARB = 0x84C5;
	int GL_TEXTURE6_ARB = 0x84C6;
	int GL_TEXTURE7_ARB = 0x84C7;
	int GL_TEXTURE8_ARB = 0x84C8;
	int GL_TEXTURE9_ARB = 0x84C9;
	int GL_TEXTURE10_ARB = 0x84CA;
	int GL_TEXTURE11_ARB = 0x84CB;
	int GL_TEXTURE12_ARB = 0x84CC;
	int GL_TEXTURE13_ARB = 0x84CD;
	int GL_TEXTURE14_ARB = 0x84CE;
	int GL_TEXTURE15_ARB = 0x84CF;
	int GL_TEXTURE16_ARB = 0x84D0;
	int GL_TEXTURE17_ARB = 0x84D1;
	int GL_TEXTURE18_ARB = 0x84D2;
	int GL_TEXTURE19_ARB = 0x84D3;
	int GL_TEXTURE20_ARB = 0x84D4;
	int GL_TEXTURE21_ARB = 0x84D5;
	int GL_TEXTURE22_ARB = 0x84D6;
	int GL_TEXTURE23_ARB = 0x84D7;
	int GL_TEXTURE24_ARB = 0x84D8;
	int GL_TEXTURE25_ARB = 0x84D9;
	int GL_TEXTURE26_ARB = 0x84DA;
	int GL_TEXTURE27_ARB = 0x84DB;
	int GL_TEXTURE28_ARB = 0x84DC;
	int GL_TEXTURE29_ARB = 0x84DD;
	int GL_TEXTURE30_ARB = 0x84DE;
	int GL_TEXTURE31_ARB = 0x84DF;
	int GL_ACTIVE_TEXTURE_ARB = 0x84E0;
	int GL_CLIENT_ACTIVE_TEXTURE_ARB = 0x84E1;
	int GL_MAX_TEXTURE_UNITS_ARB = 0x84E2;

	void glClientActiveTextureARB(@GLenum int texture);

	void glActiveTextureARB(@GLenum int texture);

	void glMultiTexCoord1fARB(@GLenum int target, float s);

	void glMultiTexCoord1iARB(@GLenum int target, int s);

	void glMultiTexCoord1sARB(@GLenum int target, short s);

	void glMultiTexCoord2fARB(@GLenum int target, float s, float t);

	void glMultiTexCoord2iARB(@GLenum int target, int s, int t);

	void glMultiTexCoord2sARB(@GLenum int target, short s, short t);

	void glMultiTexCoord3fARB(@GLenum int target, float s, float t, float r);

	void glMultiTexCoord3iARB(@GLenum int target, int s, int t, int r);

	void glMultiTexCoord3sARB(@GLenum int target, short s, short t, short r);

	void glMultiTexCoord4fARB(@GLenum int target, float s, float t, float r, float q);

	void glMultiTexCoord4iARB(@GLenum int target, int s, int t, int r, int q);

	void glMultiTexCoord4sARB(@GLenum int target, short s, short t, short r, short q);
}
