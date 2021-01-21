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
package org.lwjgl.util.model;

import java.io.Serializable;

import org.lwjgl.util.vector.ReadableVector3f;

/**
 * $Id: Vertex.java,v 1.5 2004/06/12 20:28:28 matzon Exp $
 * 
 * A single vertex in a mesh.
 * 
 * @author $Author: matzon $
 * @version $Revision: 1.5 $
 */
public class Vertex implements Serializable {
	
	public static final long serialVersionUID = 1L;
	
	/** Coordinates */
	private final ReadableVector3f coords;
	
	/** Normal */
	private final ReadableVector3f normal;
	
	/**
	 * C'tor
	 * @param coords
	 * @param normal
	 */
	public Vertex(ReadableVector3f coords, ReadableVector3f normal) {
		this.coords = coords;
		this.normal = normal;
	}

	/**
	 * @return Returns the coords.
	 */
	public ReadableVector3f getCoords() {
		return coords;
	}
	
	/**
	 * @return Returns the normal.
	 */
	public ReadableVector3f getNormal() {
		return normal;
	}
}
