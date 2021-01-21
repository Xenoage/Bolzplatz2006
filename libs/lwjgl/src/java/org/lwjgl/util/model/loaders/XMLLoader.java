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
package org.lwjgl.util.model.loaders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.Color;
import org.lwjgl.util.model.BoneFrame;
import org.lwjgl.util.model.BonedModel;
import org.lwjgl.util.model.BonedVertex;
import org.lwjgl.util.model.MeshFrame;
import org.lwjgl.util.model.MeshedModel;
import org.lwjgl.util.model.Model;
import org.lwjgl.util.model.Triangle;
import org.lwjgl.util.model.Vertex;
import org.lwjgl.util.model.Weight;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * $Id: XMLLoader.java,v 1.5 2005/05/04 20:59:40 cix_foo Exp $
 * 
 * Loads a Model from an XML document. Construct with an XML Document as the argument,
 * and then retrieve the Model by calling load().
 * 
 * @author $Author: cix_foo $
 * @version $Revision: 1.5 $
 */
public class XMLLoader {
	
	/** The source document */
	private final Document src;
	
	/** Expected number of vertices */
	private int numVertices;
	
	/** Expected number of bones */
	private int numBones;
	
	/**
	 * C'tor
	 */
	public XMLLoader(Document src) {
		this.src = src;
	}
	
	/**
	 * Load the model from the XML document and return it.
	 * @return Model
	 * @throws Exception
	 */
	public Model load() throws Exception {
		String name = XMLUtil.getString(src.getDocumentElement(), "modelname");
		String material = XMLUtil.getString(src.getDocumentElement(), "material");
		numVertices = XMLUtil.getInt(src.getDocumentElement(), "vertexcount");
		if (XMLUtil.getString(src.getDocumentElement(), "type").equals("boned")) {
			// It's a boned model
			numBones = XMLUtil.getInt(src.getDocumentElement(), "bones", 0);
			return new BonedModel(
					name,
					material,
					loadTriangles(),
					loadSkin(),
					loadColor(),
					loadBoneAnimations(), 
					loadBonedVertices()
				);
		} else if (XMLUtil.getString(src.getDocumentElement(), "type").equals("meshed")) {
			// It's a mesh keyframe model
			return new MeshedModel(
					name,
					material,
					loadTriangles(),
					loadSkin(),
					loadColor(),
					loadMeshAnimations()
				);
		} else {
			throw new Exception("Unsupported model type.");
		}
	}
	
	/**
	 * Load all the BonedVertices
	 * @return Vertex[]
	 * @throws Exception
	 */
	private BonedVertex[] loadBonedVertices() throws Exception {
		Element verticesElement = XMLUtil.getChild(src.getDocumentElement(), "vertices");
		if (verticesElement == null) {
			return null;
		}
		List vertexElements = XMLUtil.getChildren(verticesElement, "vertex");
		if (vertexElements.size() != numVertices) {
			throw new Exception("Vertex count incorrect, got "+vertexElements.size()+", expected "+numVertices);
		}
		BonedVertex[] vertices = new BonedVertex[vertexElements.size()];
		int vertexCount = 0;
		for (Iterator i = vertexElements.iterator(); i.hasNext(); ) {
			Element vertexElement = (Element) i.next();
			vertices[vertexCount++] = loadBonedVertex(vertexElement);
		}
		return vertices;
	}
	
	/**
	 * Load the skin
	 * @return Vector2f[]
	 * @throws Exception
	 */
	private Vector2f[] loadSkin() throws Exception {
		Element skinElement = XMLUtil.getChild(src.getDocumentElement(), "skin");
		if (skinElement == null) {
			return null;
		}
		List skinElements = XMLUtil.getChildren(skinElement, "texcoord");
		if (skinElements.size() == 0) {
			return null;
		}
		if (skinElements.size() != numVertices) {
			throw new Exception("Skin count incorrect, got "+skinElements.size()+", expected "+numVertices);
		}
		Vector2f[] skins = new Vector2f[skinElements.size()];
		int skinCount = 0;
		for (Iterator i = skinElements.iterator(); i.hasNext(); ) {
			Element texCoordElement = (Element) i.next();
			skins[skinCount++] = loadTexCoord(texCoordElement);
		}
		return skins;
	}
	
	/**
	 * Load the colour
	 * @return Color[]
	 * @throws Exception
	 */
	private Color[] loadColor() throws Exception {
		Element colorsElement = XMLUtil.getChild(src.getDocumentElement(), "colors");
		if (colorsElement == null) {
			return null;
		}

		List colorElements = XMLUtil.getChildren(colorsElement, "color");
		if (colorElements.size() == 0) {
			return null;
		}
		if (colorElements.size() != numVertices) {
			throw new Exception("Color count incorrect, got "+colorElements.size()+", expected "+numVertices);
		}
		Color[] colors = new Color[colorElements.size()];
		int colorCount = 0;
		for (Iterator i = colorElements.iterator(); i.hasNext(); ) {
			Element colorElement = (Element) i.next();
			colors[colorCount++] = loadColor(colorElement);
		}
		return colors;
	}
	
	/**
	 * Load all the Triangles
	 * @return Triangle[]
	 * @throws Exception
	 */
	private Triangle[] loadTriangles() throws Exception {
		Element meshElement = XMLUtil.getChild(src.getDocumentElement(), "mesh");
		if (meshElement == null) {
			return null;
		}
		List triangleElements = XMLUtil.getChildren(meshElement, "triangle");
		Triangle[] triangles = new Triangle[triangleElements.size()];
		int triangleCount = 0;
		for (Iterator i = triangleElements.iterator(); i.hasNext(); ) {
			Element triangleElement = (Element) i.next();
			triangles[triangleCount++] = loadTriangle(triangleElement);
		}
		return triangles;
	}
	
	/**
	 * Load all the bone animations
	 * @return Map of animation names to BoneFrame[] animations
	 * @throws Exception
	 */
	private Map loadBoneAnimations() throws Exception {
		Element animationElement = XMLUtil.getChild(src.getDocumentElement(), "animation");
		if (animationElement == null) {
			return null;
		}
		List sequenceElements = XMLUtil.getChildren(src.getDocumentElement(), "sequence");
		Map animations = new HashMap(sequenceElements.size());
		for (Iterator i = sequenceElements.iterator(); i.hasNext(); ) {
			Element sequenceElement = (Element) i.next();
			animations.put(XMLUtil.getString(sequenceElement, "sequencename"), loadBonedAnimation(sequenceElement));
		}
		return animations;
	}
	
	/**
	 * Load all the mesh keyframe animations
	 * @return Map of animation names to MeshFrame[] animations
	 * @throws Exception
	 */
	private Map loadMeshAnimations() throws Exception {
		Element animationElement = XMLUtil.getChild(src.getDocumentElement(), "animation");
		if (animationElement == null) {
			return null;
		}
		List sequenceElements = XMLUtil.getChildren(animationElement, "sequence");
		Map animations = new HashMap(sequenceElements.size());
		for (Iterator i = sequenceElements.iterator(); i.hasNext(); ) {
			Element sequenceElement = (Element) i.next();
			animations.put(XMLUtil.getString(sequenceElement, "sequencename"), loadMeshAnimation(sequenceElement));
		}
		return animations;
	}
	
	/**
	 * Load a Vertex from XML
	 * @param vertexElement
	 * @return a Vertex
	 * @throws Exception
	 */
	private BonedVertex loadBonedVertex(Element vertexElement) throws Exception {
		List weightElements = XMLUtil.getChildren(vertexElement, "weight");
		Weight[] weights;
		if (weightElements.size() == 0) {
			weights = null;
		} else {
			weights = new Weight[weightElements.size()];
			int weightCount = 0;
			for (Iterator i = weightElements.iterator(); i.hasNext(); ) {
				Element weightElement = (Element) i.next();
				weights[weightCount++] = loadWeight(weightElement);
			}
		}
			
		return new BonedVertex(
				new Vector3f(
					XMLUtil.getFloat(vertexElement, "x"),
					XMLUtil.getFloat(vertexElement, "y"),
					XMLUtil.getFloat(vertexElement, "z")
				),
				XMLUtil.hasAttribute(vertexElement, "nx") ?
					new Vector3f(
							XMLUtil.getFloat(vertexElement, "nx"),
							XMLUtil.getFloat(vertexElement, "ny"),
							XMLUtil.getFloat(vertexElement, "nz")
						)
					: null,
				weights
			);
	}

	/**
	 * Load a Vertex from XML
	 * @param vertexElement
	 * @return a Vertex
	 * @throws Exception
	 */
	private Vertex loadMeshVertex(Element vertexElement) throws Exception {
		return new Vertex(
				new Vector3f(
					XMLUtil.getFloat(vertexElement, "x"),
					XMLUtil.getFloat(vertexElement, "y"),
					XMLUtil.getFloat(vertexElement, "z")
				),
				XMLUtil.hasAttribute(vertexElement, "nx") ?
					new Vector3f(
							XMLUtil.getFloat(vertexElement, "nx"),
							XMLUtil.getFloat(vertexElement, "ny"),
							XMLUtil.getFloat(vertexElement, "nz")
						)
					: null
			);
	}

	/**
	 * Load a Weight from XML
	 * @param element
	 * @return a Skin
	 * @throws Exception
	 */
	private Weight loadWeight(Element element) throws Exception {
		int bone = XMLUtil.getInt(element, "bone");
		if (bone < 0 || bone >= numBones) {
			throw new Exception("Bone index out of range");
		}
		return new Weight(
				bone,
				XMLUtil.getFloat(element, "weight")
			);
	}
	
	/**
	 * Load a Triangle from XML
	 * @param element
	 * @param numVertices
	 * @return a Triangle
	 * @throws Exception
	 */
	private Triangle loadTriangle(Element element) throws Exception {
		// Perform sanity checks
		int a = XMLUtil.getInt(element, "a");
		if (a < 0 || a >= numVertices) {
			throw new Exception("'a' is out of range");
		}
		int b = XMLUtil.getInt(element, "b");
		if (b < 0 || b >= numVertices) {
			throw new Exception("'b' is out of range");
		}
		int c = XMLUtil.getInt(element, "c");
		if (c < 0 || c >= numVertices) {
			throw new Exception("'c' is out of range");
		}
		if (a == b || a == c || b == c) {
			throw new Exception("Degenerate triangle");
		}
		return new Triangle(
				a,
				b,
				c,
				XMLUtil.getInt(element, "adjacency", 0)
			);
	}
	
	/**
	 * Load a texture coordinate from XML
	 * @param element
	 * @return a Vector2f
	 * @throws Exception
	 */
	private Vector2f loadTexCoord(Element element) throws Exception {
		return new Vector2f(
				XMLUtil.getFloat(element, "u"),
				XMLUtil.getFloat(element, "v")
			);
	}
	
	/**
	 * Load a colour from XML
	 * @param element
	 * @return a Color
	 * @throws Exception
	 */
	private Color loadColor(Element element) throws Exception {
		return new Color(
				XMLUtil.getInt(element, "red"),
				XMLUtil.getInt(element, "green"),
				XMLUtil.getInt(element, "blue"),
				XMLUtil.getInt(element, "alpha", 255)
			);
	}
	
	/**
	 * Load a boned Animation from XML
	 * @param element
	 * @return BoneFrame[] 
	 * @throws Exception
	 */
	private BoneFrame[] loadBonedAnimation(Element element) throws Exception {
		List frameElements = XMLUtil.getChildren(element, "frame");
		BoneFrame[] frames = new BoneFrame[frameElements.size()];
		int frameCount = 0;
		for (Iterator i = frameElements.iterator(); i.hasNext(); ) {
			Element frameElement = (Element) i.next();
			frames[frameCount++] = loadBoneFrame(frameElement);
		}
		Arrays.sort(frames);
		return frames;
	}
	
	/**
	 * Load a mesh Animation from XML
	 * @param element
	 * @return MeshFrame[] 
	 * @throws Exception
	 */
	private MeshFrame[] loadMeshAnimation(Element element) throws Exception {
		List frameElements = XMLUtil.getChildren(element, "frame");
		MeshFrame[] frames = new MeshFrame[frameElements.size()];
		int frameCount = 0;
		for (Iterator i = frameElements.iterator(); i.hasNext(); ) {
			Element frameElement = (Element) i.next();
			frames[frameCount++] = loadMeshFrame(frameElement);
		}
		Arrays.sort(frames);
		return frames;
	}
	
	/**
	 * Load a Frame from XML
	 * @param element
	 * @return BoneFrame 
	 * @throws Exception
	 */
	private BoneFrame loadBoneFrame(Element element) throws Exception {
		List boneElements = XMLUtil.getChildren(element, "bone");
		if (boneElements.size() != numBones) {
			throw new Exception("Expected "+numBones+" bones in frame, only got "+boneElements.size());
		}
		Matrix4f[] bones = new Matrix4f[boneElements.size()];
		int boneCount = 0;
		for (Iterator i = boneElements.iterator(); i.hasNext(); ) {
			Element boneElement = (Element) i.next();
			bones[boneCount++] = loadBone(boneElement);
		}
		return new BoneFrame(
				XMLUtil.getFloat(element, "time"),
				XMLUtil.getString(element, "action", null),
				bones
			);
	}

	/**
	 * Load a Frame from XML
	 * @param element
	 * @return MeshFrame 
	 * @throws Exception
	 */
	private MeshFrame loadMeshFrame(Element element) throws Exception {
		List vertexElements = XMLUtil.getChildren(element, "vertex");
		Vertex[] vertices = new Vertex[vertexElements.size()];
		if (vertices.length != numVertices) {
			throw new Exception("Vertex count incorrect");
		}
		int vertexCount = 0;
		for (Iterator i = vertexElements.iterator(); i.hasNext(); ) {
			Element vertexElement = (Element) i.next();
			vertices[vertexCount++] = loadMeshVertex(vertexElement);
		}
		return new MeshFrame(
				XMLUtil.getFloat(element, "time"),
				XMLUtil.getString(element, "action", null),
				vertices
			);
	}

	/**
	 * Load a Bone from XML
	 * @param element
	 * @return a Matrix4f
	 * @throws Exception
	 */
	private Matrix4f loadBone(Element element) throws Exception {
		Matrix4f ret = new Matrix4f();
		ret.m00 = XMLUtil.getFloat(element, "m00");
		ret.m01 = XMLUtil.getFloat(element, "m01");
		ret.m02 = XMLUtil.getFloat(element, "m02");
		ret.m03 = XMLUtil.getFloat(element, "m03");
		ret.m10 = XMLUtil.getFloat(element, "m10");
		ret.m11 = XMLUtil.getFloat(element, "m11");
		ret.m12 = XMLUtil.getFloat(element, "m12");
		ret.m13 = XMLUtil.getFloat(element, "m13");
		ret.m20 = XMLUtil.getFloat(element, "m20");
		ret.m21 = XMLUtil.getFloat(element, "m21");
		ret.m22 = XMLUtil.getFloat(element, "m22");
		ret.m23 = XMLUtil.getFloat(element, "m23");
		ret.m30 = XMLUtil.getFloat(element, "m30", 0.0f);
		ret.m31 = XMLUtil.getFloat(element, "m31", 0.0f);
		ret.m32 = XMLUtil.getFloat(element, "m32", 0.0f);
		ret.m33 = XMLUtil.getFloat(element, "m33", 1.0f);
		return ret;
	}

}
