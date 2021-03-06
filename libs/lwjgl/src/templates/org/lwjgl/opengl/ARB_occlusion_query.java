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

import java.nio.IntBuffer;

import org.lwjgl.util.generator.*;

public interface ARB_occlusion_query {
	/*
	 * Accepted by the <target> parameter of BeginQueryARB, EndQueryARB,
	 * and GetQueryivARB:
	*/
	int GL_SAMPLES_PASSED_ARB = 0x8914;

	/*
	 * Accepted by the <pname> parameter of GetQueryivARB:
	*/
	int GL_QUERY_COUNTER_BITS_ARB = 0x8864;
	int GL_CURRENT_QUERY_ARB = 0x8865;

	/*
	 * Accepted by the <pname> parameter of GetQueryObjectivARB and
	 * GetQueryObjectuivARB:
	*/
	int GL_QUERY_RESULT_ARB = 0x8866;
	int GL_QUERY_RESULT_AVAILABLE_ARB = 0x8867;

	void glGenQueriesARB(@AutoSize("ids") @GLsizei int n, @GLuint IntBuffer ids);

	void glDeleteQueriesARB(@AutoSize("ids") @GLsizei int n, @GLuint IntBuffer ids);

	boolean glIsQueryARB(@GLuint int id);

	void glBeginQueryARB(@GLenum int target, @GLuint int id);

	void glEndQueryARB(@GLenum int target);

	@StripPostfix("params")
	void glGetQueryivARB(@GLenum int target, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectivARB(@GLuint int id, @GLenum int pname, @Check("4") IntBuffer params);

	@StripPostfix("params")
	void glGetQueryObjectuivARB(@GLuint int id, @GLenum int pname, @Check("4") IntBuffer params);
}
