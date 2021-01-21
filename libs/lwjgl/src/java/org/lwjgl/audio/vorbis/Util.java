/* 
Copyright (c) 2005, Phillip Michael Jordan
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
- Redistributions of source code must retain the above copyright notice, this
list of conditions and the following disclaimer.
- Redistributions in binary form must reproduce the above copyright notice,
this list of conditions and the following disclaimer in the documentation
and/or other materials provided with the distribution.
- The names of its contributors may not be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/*
Phillip Jordan
phillip.m.jordan@gmail.com
http://www-users.york.ac.uk/~pmj110/software/lwjgl/
*/

package org.lwjgl.audio.vorbis;


import java.nio.ByteBuffer;
import java.lang.Integer;
/**
 * Class Util
 * Non-instantiable utility class acting as a factory object for Ogg Vorbis
 * based data streams, and for shortcut functions for creating OpenAL buffers
 * directly from Ogg Vorbis Files.
 */
public class Util
{
	// Fields
	// Methods
	// Constructors
	// Empty Constructor
	private Util ( )
	{ }
	// Accessor Methods
	// Operations
	/**
	 * Creates a data stream object based on the specified Ogg Vorbis file.
	 */
	public static DataStream CreateStreamFromFile ( String filename)
	{
		ByteBuffer c_data = C_CreateStreamFromFile(filename);
		
		if (c_data == null)
			return null;
		
		return new DataStream(c_data, null, filename);
	}
	public static native ByteBuffer C_CreateStreamFromFile (String filename);

	/**
	 * Creates a data stream object based on the specified Ogg Vorbis data in memory.
	 */
	public static DataStream CreateStreamFromMemory ( ByteBuffer source_data)
	{
		ByteBuffer c_data = C_CreateStreamFromMemory(source_data);
		
		if (c_data == null)
			return null;
		
		return new DataStream(c_data, source_data, "");
	}
	
	public static native ByteBuffer C_CreateStreamFromMemory (ByteBuffer data);
	
	/**
	 * Create a new OpenAL buffer, and fill it with the entire audio data
	 * contained in the specified Ogg Vorbis file.
	 */
	public static native Integer CreateALBufferFromFile ( String filename, int sample_size);
	public static Integer CreateALBufferFromFile ( String filename)
	{
		return CreateALBufferFromFile(filename, 16);
	}

	/**
	 * Create a new OpenAL buffer, and fill it with the entire audio data contained
	 * in the specified Ogg Vorbis data buffer.
	 * @param source_data A buffer containing a full Ogg Vorbis stream. Note:
	 * if source_data is not 'direct', this will incur a performance penalty as it
	 * needs to be converted. Reading will not commence from the current position,
	 * but the beginning of the buffer, and the buffer may be rewound. It is
	 * assumed that the entire contents of the buffer is to be used.
	 * @param sample_size Number of bits in every audio sample of the resulting
	 * OpenAL buffer.
	 */
	public static Integer CreateALBufferFromMemory (ByteBuffer source_data, int sample_size)
	{
		// convert to direct byte buffer if necessary
		ByteBuffer direct_data = null;
		if (source_data == null)
			return -1;
		
		if (source_data.isDirect())
		{
			direct_data = source_data;
		}
		else
		{
			source_data.rewind();
			source_data.limit(source_data.capacity());
			direct_data = ByteBuffer.allocateDirect(source_data.remaining());
			direct_data.put(source_data);
		}
		
		/* pass the Ogg Vorbis data to the C back-end and create and fill an
		 * OpenAL buffer with it.
		 */
		
		return C_CreateALBufferFromMemory(direct_data, sample_size);
	}
	
	/** Like 'public int CreateALBufferFromMemory (ByteBuffer source_data, int
	 * sample_size)' but with the default sample size of 16 bits.
	 */
	public static Integer CreateALBufferFromMemory (ByteBuffer source_data)
	{
		return CreateALBufferFromMemory (source_data, 16);
	}
	
	/** Native implementation for CreateALBufferFromMemory. Can ONLY deal with
	 * a <em>direct</em> ByteBuffer as input, otherwise identical to
	 * CreateALBufferFromMemory.
	 * Note: do not call this function directly.
	 */
	private native static Integer C_CreateALBufferFromMemory(ByteBuffer source_data, int sample_size);
}

