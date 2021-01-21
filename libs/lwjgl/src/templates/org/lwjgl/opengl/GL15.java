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

import java.nio.*;

public interface GL15 {
	// ----------------------------------------------------------------------
	// ---------------------- ARB_vertex_buffer_object ----------------------
	// ----------------------------------------------------------------------

	int GL_ARRAY_BUFFER = 0x8892;
	int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
	int GL_ARRAY_BUFFER_BINDING = 0x8894;
	int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
	int GL_VERTEX_ARRAY_BUFFER_BINDING = 0x8896;
	int GL_NORMAL_ARRAY_BUFFER_BINDING = 0x8897;
	int GL_COLOR_ARRAY_BUFFER_BINDING = 0x8898;
	int GL_INDEX_ARRAY_BUFFER_BINDING = 0x8899;
	int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 0x889A;
	int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 0x889B;
	int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 0x889C;
	int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 0x889D;
	int GL_WEIGHT_ARRAY_BUFFER_BINDING = 0x889E;
	int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
	int GL_STREAM_DRAW = 0x88E0;
	int GL_STREAM_READ = 0x88E1;
	int GL_STREAM_COPY = 0x88E2;
	int GL_STATIC_DRAW = 0x88E4;
	int GL_STATIC_READ = 0x88E5;
	int GL_STATIC_COPY = 0x88E6;
	int GL_DYNAMIC_DRAW = 0x88E8;
	int GL_DYNAMIC_READ = 0x88E9;
	int GL_DYNAMIC_COPY = 0x88EA;
	int GL_READ_ONLY = 0x88B8;
	int GL_WRITE_ONLY = 0x88B9;
	int GL_READ_WRITE = 0x88BA;
	int GL_BUFFER_SIZE = 0x8764;
	int GL_BUFFER_USAGE = 0x8765;
	int GL_BUFFER_ACCESS = 0x88BB;
	int GL_BUFFER_MAPPED = 0x88BC;
	int GL_BUFFER_MAP_POINTER = 0x88BD;

	@Code(	"		StateTracker.bindBuffer(target, buffer);")
	void glBindBuffer(@GLenum int target, @GLuint int buffer);
	@Code(	"		StateTracker.deleteBuffers(buffers);")
	void glDeleteBuffers(@AutoSize("buffers") @GLsizei int n, @Const @GLuint IntBuffer buffers);

	void glGenBuffers(@AutoSize("buffers") @GLsizei int n, @GLuint IntBuffer buffers);
	boolean glIsBuffer(@GLuint int buffer);

	@GenerateAutos
	void glBufferData(@GLenum int target, @AutoSize("data") @GLsizeiptr int size,
			@Const
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data, @GLenum int usage);
	void glBufferSubData(@GLenum int target, @GLintptr int offset, @AutoSize("data") @GLsizeiptr int size,
			@Check
			@Const
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data);

	void glGetBufferSubData(@GLenum int target, @GLintptr int offset, @AutoSize("data") @GLsizeiptr int size,
			@Check
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data);

	/**
	 * glMapBuffer maps a gl vertex buffer buffer to a ByteBuffer. The oldBuffer argument can be null, in which case a new
	 * ByteBuffer will be created, pointing to the returned memory. If oldBuffer is non-null, it will be returned if it points to
	 * the same mapped memory, otherwise a new ByteBuffer is created. That way, an application will normally use glMapBuffer like
	 * this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBuffer(..., ..., ..., null); ... // Another map on the same buffer
	 * mapped_buffer = glMapBuffer(..., ..., ..., mapped_buffer);
	 *
	 * @param result_size	The size of the buffer area.
	 * @param old_buffer	A ByteBuffer. If this argument points to the same address as the new mapping, it will be returned and no
	 *                  new buffer will be created. In that case, size is ignored.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@CachedResult
	@GLvoid ByteBuffer glMapBuffer(@GLenum int target, @GLenum int access);

	boolean glUnmapBuffer(@GLenum int target);

	@StripPostfix("params")
	void glGetBufferParameteriv(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("pointer")
	void glGetBufferPointerv(@GLenum int target, @GLenum int pname, @Result @GLvoid ByteBuffer pointer);

	// -----------------------------------------------------------------
	// ---------------------- ARB_occlusion_query ----------------------
	// -----------------------------------------------------------------

	/*
	* Accepted by the <target> parameter of BeginQuery, EndQuery,
	* and GetQueryiv:
	*/
	int GL_SAMPLES_PASSED = 0x8914;

	/*
	Accepted by the <pname> parameter of GetQueryiv:
	*/
	int GL_QUERY_COUNTER_BITS = 0x8864;
	int GL_CURRENT_QUERY = 0x8865;

	/*
	Accepted by the <pname> parameter of GetQueryObjectiv and
	GetQueryObjectuiv:
	*/
	int GL_QUERY_RESULT = 0x8866;
	int GL_QUERY_RESULT_AVAILABLE = 0x8867;

	void glGenQueries(@AutoSize("ids") @GLsizei int n, @GLuint IntBuffer ids);
	void glDeleteQueries(@AutoSize("ids") @GLsizei int n, @GLuint IntBuffer ids);
	boolean glIsQuery(@GLuint int id);

	void glBeginQuery(@GLenum int target, @GLuint int id);
	void glEndQuery(@GLenum int target);

	@StripPostfix("params")
	void glGetQueryiv(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectiv(@GLenum int id, @GLenum int pname, @Check("4") @GLint IntBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectuiv(@GLenum int id, @GLenum int pname, @Check("4") @GLuint IntBuffer params);
}
