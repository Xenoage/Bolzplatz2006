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

@Extension(postfix="ARB", isFinal=false)
public interface ARB_buffer_object {
	/*
	 * Accepted by the <usage> parameter of BufferDataARB:
	 */
	int GL_STREAM_DRAW_ARB = 0x88E0;
	int GL_STREAM_READ_ARB = 0x88E1;
	int GL_STREAM_COPY_ARB = 0x88E2;
	int GL_STATIC_DRAW_ARB = 0x88E4;
	int GL_STATIC_READ_ARB = 0x88E5;
	int GL_STATIC_COPY_ARB = 0x88E6;
	int GL_DYNAMIC_DRAW_ARB = 0x88E8;
	int GL_DYNAMIC_READ_ARB = 0x88E9;
	int GL_DYNAMIC_COPY_ARB = 0x88EA;

	/*
	 * Accepted by the <access> parameter of MapBufferARB:
	*/
	int GL_READ_ONLY_ARB = 0x88B8;
	int GL_WRITE_ONLY_ARB = 0x88B9;
	int GL_READ_WRITE_ARB = 0x88BA;

	/*
	 * Accepted by the <pname> parameter of GetBufferParameterivARB:
	*/
	int GL_BUFFER_SIZE_ARB = 0x8764;
	int GL_BUFFER_USAGE_ARB = 0x8765;
	int GL_BUFFER_ACCESS_ARB = 0x88BB;
	int GL_BUFFER_MAPPED_ARB = 0x88BC;
	int GL_BUFFER_MAP_POINTER_ARB = 0x88BD;

	@Code(	"		StateTracker.bindBuffer(target, buffer);")
	void glBindBufferARB(@GLenum int target, @GLuint int buffer);
	@Code(	"		StateTracker.deleteBuffers(buffers);")
	void glDeleteBuffersARB(@AutoSize("buffers") @GLsizei int n, @Const @GLuint IntBuffer buffers);
	void glGenBuffersARB(@AutoSize("buffers") int n, @GLuint IntBuffer buffers);
	boolean glIsBufferARB(@GLuint int buffer);

	@GenerateAutos
	void glBufferDataARB(@GLenum int target, @AutoSize("data") @GLsizeiptrARB int size,
			@Const
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data, @GLenum int usage);

	void glBufferSubDataARB(@GLenum int target, @GLintptrARB int offset, @AutoSize("data") @GLsizeiptrARB int size,
			@Check
			@Const
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data);

	void glGetBufferSubDataARB(@GLenum int target, @GLintptrARB int offset, @AutoSize("data") @GLsizeiptrARB int size,
			@Check
			@GLbyte
			@GLshort
			@GLint
			@GLfloat
			Buffer data);

	/**
	 * glMapBufferARB maps a gl vertex buffer buffer to a ByteBuffer. The oldBuffer argument can be null,
	 * in which case a new ByteBuffer will be created, pointing to the returned memory. If oldBuffer is non-null,
	 * it will be returned if it points to the same mapped memory, otherwise a new ByteBuffer is created. That
	 * way, an application will normally use glMapBufferARB like this:
	 * <p/>
	 * ByteBuffer mapped_buffer; mapped_buffer = glMapBufferARB(..., ..., ..., null); ... // Another map on the same buffer mapped_buffer = glMapBufferARB(..., ..., ..., mapped_buffer);
	 *
	 * @param result_size   The size of the buffer area.
	 * @param old_buffer    A ByteBuffer. If this argument points to the same address as the new mapping, it will be returned and no new buffer will be created. In that case, size is ignored.
	 *
	 * @return A ByteBuffer representing the mapped buffer memory.
	 */
	@CachedResult
	@GLvoid ByteBuffer glMapBufferARB(@GLenum int target, @GLenum int access);

	boolean glUnmapBufferARB(@GLenum int target);

	@StripPostfix("params")
	void glGetBufferParameterivARB(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("pointer")
	void glGetBufferPointervARB(@GLenum int target, @GLenum int pname, @Result @GLvoid ByteBuffer pointer);
}
