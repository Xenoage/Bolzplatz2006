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
package org.lwjgl.openal;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

/**
 * $Id: ALCcontext.java,v 1.13 2005/04/12 10:30:03 elias_naur Exp $
 * <br>
 * Wrapper class, to make ALC contexts behave like the orginal api.
 *
 * @author Brian Matzon <brian@matzon.dk>
 * @version $Revision: 1.13 $
 */
final class ALCcontext {

	/** address of actual context */
	final long context;

	/** 
	 * Creates a new instance of ALCcontext 
	 * 
	 * @param context address of actual context
	 */
	ALCcontext(long context) {
		this.context = context;
	}

	static IntBuffer createAttributeList(int contextFrequency, int contextRefresh, int contextSynchronized) {
		IntBuffer attribList = BufferUtils.createIntBuffer(7);

		attribList.put(ALC.ALC_FREQUENCY);
		attribList.put(contextFrequency);
		attribList.put(ALC.ALC_REFRESH);
		attribList.put(contextRefresh);
		attribList.put(ALC.ALC_SYNC);
		attribList.put(contextSynchronized);
		attribList.put(0); //terminating int

		return attribList;
	}
}
