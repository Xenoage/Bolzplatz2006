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
package org.lwjgl.util.vector;

import java.io.Serializable;
import java.nio.FloatBuffer;

/**
 * $Id: Matrix2f.java,v 1.2 2004/06/12 20:28:29 matzon Exp $
 *
 * Holds a 2x2 matrix
 *
 * @author cix_foo <cix_foo@users.sourceforge.net>
 * @version $Revision: 1.2 $
 */

public class Matrix2f extends Matrix implements Serializable {
	
	public float m00 = 1.0f, m01, m10, m11 = 1.0f;
	
	/**
	 * Constructor for Matrix2f.
	 */
	public Matrix2f() {
	}
	
	/**
	 * Constructor
	 */
	public Matrix2f(Matrix2f src) {
		load(src);
	}
	
	/**
	 * Load from another matrix2f
	 * @param src The source matrix
	 * @return this
	 */
	public Matrix2f load(Matrix2f src) {
		
		m00 = src.m00;
		m01 = src.m01;
		m10 = src.m10;
		m11 = src.m11;
		
		return this;
	}
	
	/**
	 * Load from a float buffer. The buffer stores the matrix in column major
	 * (OpenGL) order.
	 * 
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public Matrix load(FloatBuffer buf) {
		
		m00 = buf.get();
		m01 = buf.get();
		m10 = buf.get();
		m11 = buf.get();
		
		return this;
	}
	
	/**
	 * Load from a float buffer. The buffer stores the matrix in row major
	 * (mathematical) order.
	 * 
	 * @param buf A float buffer to read from
	 * @return this
	 */
	public Matrix loadTranspose(FloatBuffer buf) {
		
		m00 = buf.get();
		m10 = buf.get();
		m01 = buf.get();
		m11 = buf.get();
		
		return this;
	}	
	
	/**
	 * Store this matrix in a float buffer. The matrix is stored in column
	 * major (openGL) order.
	 * @param buf The buffer to store this matrix in
	 */
	public Matrix store(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m01);
		buf.put(m10);
		buf.put(m11);
		return this;
	}
	
	/**
	 * Store this matrix in a float buffer. The matrix is stored in row
	 * major (maths) order.
	 * @param buf The buffer to store this matrix in
	 */
	public Matrix storeTranspose(FloatBuffer buf) {
		buf.put(m00);
		buf.put(m10);
		buf.put(m01);
		buf.put(m11);
		return this;
	}	
	
	
	
	/**
	 * Add two matrices together and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f add(Matrix2f left, Matrix2f right, Matrix2f dest) {
		
		Matrix2f temp = null;
		
		if (dest == null)
			dest = new Matrix2f();
		else if (dest == left || dest == right) {
			temp = dest;
			dest = new Matrix2f();
		}
			
		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;
		
		if (temp != null) {
			temp.load(dest);
			return temp;
		} else
			return dest;		
	} 

	/**
	 * Subtract the right matrix from the left and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f sub(Matrix2f left, Matrix2f right, Matrix2f dest) {
		
		Matrix2f temp = null;
		
		if (dest == null)
			dest = new Matrix2f();
		else if (dest == left || dest == right) {
			temp = dest;
			dest = new Matrix2f();
		}
			
		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;
		
		if (temp != null) {
			temp.load(dest);
			return temp;
		} else
			return dest;		
	} 

	/**
	 * Multiply the right matrix by the left and place the result in a third matrix.
	 * @param left The left source matrix
	 * @param right The right source matrix
	 * @param dest The destination matrix, or null if a new one is to be created
	 * @return the destination matrix
	 */
	public static Matrix2f mul(Matrix2f left, Matrix2f right, Matrix2f dest) {
		
		Matrix2f temp = null;
		
		if (dest == null)
			dest = new Matrix2f();
		else if (dest == left || dest == right) {
			temp = dest;
			dest = new Matrix2f();
		}
			
		dest.m00 = left.m00 * right.m00 + left.m10 * right.m01;
		dest.m01 = left.m01 * right.m00 + left.m11 * right.m01;
		dest.m10 = left.m00 * right.m10 + left.m10 * right.m11;
		dest.m11 = left.m01 * right.m10 + left.m11 * right.m11;
			
		if (temp != null) {
			temp.load(dest);
			return temp;
		} else
			return dest;		
	}
	
	/**
	 * Transform a Vector by a matrix and return the result in a destination
	 * vector. 
	 * @param left The left matrix
	 * @param right The right vector
	 * @param dest The destination vector, or null if a new one is to be created
	 * @return the destination vector
	 */
	public static Vector2f transform(Matrix2f left, Vector2f right, Vector2f dest) {
		
		Vector2f temp = null;
		
		if (dest == null)
			dest = new Vector2f();
		else if (dest == right) {
			temp = dest;
			dest = new Vector2f();
		}
			
		dest.x = left.m00 * right.x + left.m10 * right.y;
		dest.y = left.m01 * right.x + left.m11 * right.y;			

		if (temp != null) {
			temp.set(dest);
			return temp;
		} else
			return dest;		
	}
	
	/**
	 * Transpose this matrix
	 * @return this
	 */
	public Matrix transpose() {
		float temp;
		
		temp = m01;
		m01 = m10;
		m10 = temp;
		return this;
	}
	
	/**
	 * Transpose this matrix and place the result in another matrix.
	 * @param dest The destination matrix or null if a new matrix is to be created
	 * @return the transposed matrix
	 */
	public Matrix2f transpose(Matrix2f dest) {
		
		if (dest == null)
			dest = new Matrix2f();
			
		if (dest == this)
			transpose();
		else {
			dest.m01 = m10;
			dest.m10 = m01;
		}
			
		return dest;
	}	

	/**
	 * Invert this matrix
	 * @return this if successful, null otherwise
	 */
	public Matrix invert() 
	{
		
		/*
		 *inv(A) = 1/det(A) * adj(A);
		 */
		
		float determinant = determinant();
		if (determinant != 0) {
			float determinant_inv = 1f/determinant;
			float t00 =  m11*determinant_inv;
			float t01 = -m01*determinant_inv;
			float t11 =  m00*determinant_inv;
			float t10 = -m10*determinant_inv;
		
			m00 = t00;
			m01 = t01;
			m10 = t10;
			m11 = t11;
			return this;
		} else
		 	return null;
	}

	/**
	 * Returns a string representation of this matrix
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(m00).append(' ').append(m10).append(' ').append('\n');
		buf.append(m01).append(' ').append(m11).append(' ').append('\n');
		return buf.toString();
	}
	
	/**
	 * Negate this matrix
	 * @return this
	 */
	public Matrix negate() {
		
		m00 = -m00;
		m01 = -m01;
		m10 = -m10;
		m11 = -m11;
		
		return this;
	}
	
	/**
	 * Negate this matrix and stash the result in another matrix.
	 * @param dest The destination matrix, or null if a new matrix is to be created
	 * @return the negated matrix
	 */
	public Matrix2f negate(Matrix2f dest) {
		
		if (dest == null)
			dest = new Matrix2f();
		
		dest.m00 = -m00;
		dest.m01 = -m01;
		dest.m10 = -m10;
		dest.m11 = -m11;
		
		return dest;
	}
	
	/**
	 * Set this matrix to be the identity matrix.
	 * @return this
	 */
	public Matrix setIdentity() {
		m00 = 1.0f;
		m01 = 0.0f;
		m10 = 0.0f;
		m11 = 1.0f;
		return this;
	}
	
	/**
	 * Set this matrix to 0.
	 * @return this
	 */
	public Matrix setZero() {
		m00 = 0.0f;
		m01 = 0.0f;
		m10 = 0.0f;
		m11 = 0.0f;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.lwjgl.vector.Matrix#determinant()
	 */
	public float determinant() {
		return m00 * m11 - m01*m10;
	}

}
