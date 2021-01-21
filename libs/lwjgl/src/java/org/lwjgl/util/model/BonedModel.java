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

import java.util.Map;

import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;


/**
 * $Id: BonedModel.java,v 1.4 2004/06/12 20:28:27 matzon Exp $
 * 
 * A simple animated, skinned, boned 3D model, consisting of a single mesh
 * with a single material. The model's internal format is optimised for RAM storage;
 * no metadata is held in memory - in other words this is not a good class to use
 * for, say, a 3d modelling application.
 * 
 * @author $Author: matzon $
 * @version $Revision: 1.4 $
 */
public class BonedModel extends Model {
	
	public static final long serialVersionUID = 1L;
	
	/** Vertices */
	private final BonedVertex[] vertex;
	
	/**
	 * C'tor
	 * @param material
	 * @param triangle
	 * @param skin[]
	 * @param color[]
	 * @param animation
	 * @param vertex
	 */
	public BonedModel(String name, String material, Triangle[] triangle, Vector2f[] skin, Color[] color, Map animation, BonedVertex[] vertex) {
		super(name, material, triangle, skin, color, animation);
		this.vertex = vertex;
	}
	
	/**
	 * @return Returns the vertices
	 */
	public BonedVertex[] getVertex() {
		return vertex;
	}
	

}
