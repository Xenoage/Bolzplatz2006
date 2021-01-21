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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.util.generator.*;

public interface ARB_vertex_program extends ARB_program {
	/*
	* Accepted by the <cap> parameter of Disable, Enable, and IsEnabled, by the
	* <pname> parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev,
	* and by the <target> parameter of ProgramStringARB, BindProgramARB,
	* ProgramEnvParameter4[df][v]ARB, ProgramLocalParameter4[df][v]ARB,
	* GetProgramEnvParameter[df]vARB, GetProgramLocalParameter[df]vARB,
	* GetProgramivARB, and GetProgramStringARB.
	*/
	int GL_VERTEX_PROGRAM_ARB = 0x8620;

	/*
	* Accepted by the <cap> parameter of Disable, Enable, and IsEnabled, and by
	* the <pname> parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	* GetDoublev:
	*/
	int GL_VERTEX_PROGRAM_POINT_SIZE_ARB = 0x8642;
	int GL_VERTEX_PROGRAM_TWO_SIDE_ARB = 0x8643;
	int GL_COLOR_SUM_ARB = 0x8458;

	/*
	* Accepted by the <pname> parameter of GetVertexAttrib[dfi]vARB:
	*/
	int GL_VERTEX_ATTRIB_ARRAY_ENABLED_ARB = 0x8622;
	int GL_VERTEX_ATTRIB_ARRAY_SIZE_ARB = 0x8623;
	int GL_VERTEX_ATTRIB_ARRAY_STRIDE_ARB = 0x8624;
	int GL_VERTEX_ATTRIB_ARRAY_TYPE_ARB = 0x8625;
	int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED_ARB = 0x886A;
	int GL_CURRENT_VERTEX_ATTRIB_ARB = 0x8626;

	/*
	* Accepted by the <pname> parameter of GetVertexAttribPointervARB:
	*/
	int GL_VERTEX_ATTRIB_ARRAY_POINTER_ARB = 0x8645;

	/*
	* Accepted by the <pname> parameter of GetProgramivARB:
	*/
	int GL_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B0;
	int GL_MAX_PROGRAM_ADDRESS_REGISTERS_ARB = 0x88B1;
	int GL_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B2;
	int GL_MAX_PROGRAM_NATIVE_ADDRESS_REGISTERS_ARB = 0x88B3;

	/*
	* Accepted by the <pname> parameter of GetBooleanv, GetIntegerv,
	* GetFloatv, and GetDoublev:
	*/
	int GL_MAX_VERTEX_ATTRIBS_ARB = 0x8869;

	void glVertexAttrib1sARB(@GLuint int index, short x);

	void glVertexAttrib1fARB(@GLuint int index, float x);

	void glVertexAttrib2sARB(@GLuint int index, short x, short y);

	void glVertexAttrib2fARB(@GLuint int index, float x, float y);

	void glVertexAttrib3sARB(@GLuint int index, short x, short y, short z);

	void glVertexAttrib3fARB(@GLuint int index, float x, float y, float z);

	void glVertexAttrib4sARB(@GLuint int index, short x, short y, short z, short w);

	void glVertexAttrib4fARB(@GLuint int index, float x, float y, float z, float w);

	void glVertexAttrib4NubARB(@GLuint int index, @GLubyte byte x, @GLubyte byte y, @GLubyte byte z, @GLubyte byte w);

	void glVertexAttribPointerARB(@GLuint int index, int size, @AutoType("buffer") @GLenum int type, boolean normalized, @GLsizei int stride,
			@CachedReference
			@BufferObject(BufferKind.ArrayVBO)
			@Check
			@Const
			@GLbyte
			@GLubyte
			@GLshort
			@GLushort
			@GLint
			@GLuint
			@GLfloat
			Buffer buffer);

	void glEnableVertexAttribArrayARB(@GLuint int index);

	void glDisableVertexAttribArrayARB(@GLuint int index);

	@StripPostfix("params")
	void glGetVertexAttribfvARB(@GLuint int index, @GLenum int pname, @Check FloatBuffer params);
	@StripPostfix("params")
	void glGetVertexAttribivARB(@GLuint int index, @GLenum int pname, @Check IntBuffer params);

	@StripPostfix("result")
	void glGetVertexAttribPointervARB(@GLuint int index, @GLenum int pname, @Result @GLvoid ByteBuffer result);
}
