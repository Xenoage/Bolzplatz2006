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


import org.lwjgl.audio.IDataStream;
import java.nio.ByteBuffer;
import org.lwjgl.audio.StreamInfo;
/**
 * Class DataStream
 * 
 */
public class DataStream implements IDataStream
{
	// Fields
	// 
	protected ByteBuffer c_data;
	protected final String source;
	protected final ByteBuffer vorbis_data;
	// Methods
	// Constructors
	/** Construct from anything. Note: Called from Util only.
	 * @param c_data_structure The ready-to-go, fully initialised direct byte
	 * buffer containing all the necessary data for the back-end.
	 * @param data If reading from a region in memory, we keep a local copy
	 * of the source buffer, to ensure that it doesn't get freed.
	 * @param source_string When creating a DataStream object from a file, this
	 * will contain the file name. What it contains is defined in the calling
	 * member function of the Util class.
	 */ 
	protected DataStream (ByteBuffer c_data_structure, ByteBuffer data, String source_string)
	{
		// keep a local copy so we know it won't be destroyed prematurely
		vorbis_data = data;
		source = source_string;
		c_data = c_data_structure;
	}
	// Operations
	/**
	 * Create an OpenAL buffer and fill it with the given amount of data from the
	 * stream.
	 */
	public native Integer createALBuffer (int length, int sample_size);
	
	/**
	 * Fill the given OpenAL buffer with the specified amount of audio data from
	 * the stream. Returns true on success.
	 */
	public native boolean fillALBuffer (int al_buffer, int length, int sample_size);
		
	/**
	 * Fill the given OpenAL buffer with the specified amount of audio data from
	 * the stream, using the provided ByteBuffer as temporary memory.
	 * This version of FillALBuffer is optimised for efficiency and attempts to
	 * minimise memory allocations.
	 * @param working_memory A pre-allocated direct buffer that may be used for
	 * decoding or preloading data. This implmenentation will attempt to use the
	 * given memory region. If it is not large enough, a new direct buffer will
	 * be allocated and returned.
	 * @param al_buffer The OpenAL buffer to be filled with data.
	 * @param length Maximum duration of the clip to copy into the buffer.
	 * @param sample_size How many bits per PCM sample the data copied
	 * into the buffer should have.
	 * @return null if the call failed. The same object as working_memory if
	 * the space in working_memory was sufficient. A new direct ByteBuffer
	 * object if working_memory  was insufficient. The implementation no longer
	 * requires the new buffer, and the caller is free to use it.
	 */
	public native ByteBuffer fillALBuffer (int al_buffer, int length, int sample_size, ByteBuffer working_memory);
	
	/**
	 * Rewind the audio data stream to the beginning, but do not close it. May
	 * return false if the data stream source does not support rewinding. (e.g.
	 * streaming over a network)
	 */
	public native boolean rewind ( );
		
	
	/**
	 * Fill the relevant parts of the specified stream info object with
	 * information on the data stream.
	 */
	public boolean fillStreamInfo (StreamInfo status)
	{
		if (status == null)
			return false;
		status.track_filename = source;
		C_FillStreamInfo(status);
		return true;
	}
	
	protected native void C_FillStreamInfo(StreamInfo status);
	
	/**
	 * Shut down the underlying C data structures.
	 */
	protected native void finalize();
	
	public native int tellTime();
}

