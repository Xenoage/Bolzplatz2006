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

package org.lwjgl.audio;
import org.lwjgl.audio.StreamInfo;
import java.nio.ByteBuffer;
import java.lang.Integer;
/**
 * Interface IDataStream
 * Interface for representing audio data sources, such as files, or areas in memory. This will generally be implemented for each data source type, such as audio formats. It is also possible to create different implementations for different types of storage, such as network streaming versus local file.
 * 
 * Note: using the same data stream object for more than one data receiver, e.g. a StreamPlaylist, will result in undefined behaviour.
 */
public interface IDataStream
{
	// Methods
	// Constructors
	// Accessor Methods
	// Operations
	/**
	 * Create an OpenAL buffer and fill it with the given amount of data from the stream. Returns 
	 */
	public Integer createALBuffer (int length, int sample_size);
		
	
	/**
	 * Fill the given OpenAL buffer with the specified amount of audio data from the stream.
	 * @param al_buffer The OpenAL buffer to be filled with data.
	 * @param length Maximum duration of the clip to copy into the buffer.
	 * @param sample_size How many bits per PCM sample the data copied
	 * into the buffer should have.
	 * @return true on success, false on failure.
	 */
	public boolean fillALBuffer (int al_buffer, int length, int sample_size);
		
	/**
	 * Fill the given OpenAL buffer with the specified amount of audio data from
	 * the stream, using the provided ByteBuffer as temporary memory.
	 * This version of FillALBuffer is optimised for efficiency and attempts to
	 * minimise memory allocations.
	 * @param working_memory A pre-allocated direct buffer that may be used for
	 * decoding or preloading data. The implementation is free to ignore this
	 * parameter, and the caller should make no assumption about the contents
	 * after this function returns.
	 * @param al_buffer The OpenAL buffer to be filled with data.
	 * @param length Maximum duration of the clip to copy into the buffer.
	 * @param sample_size How many bits per PCM sample the data copied
	 * into the buffer should have.
	 * @return null if the call failed. The same object as working_memory if
	 * the space in working_memory was sufficient, or if the implementation does
	 * not expose decoding memory. A new direct ByteBuffer object if working_memory
	 * was insufficient. The implementation no longer requires the new buffer,
	 * and the caller is free to re-use it.
	 */
	public ByteBuffer fillALBuffer (int al_buffer, int length, int sample_size, ByteBuffer working_memory);
	
	/**
	 * Rewind the audio data stream to the beginning, but do not close it. May return false if the data stream source does not support rewinding. (e.g. streaming over a network)
	 */
	public boolean rewind ( );
		
	
	/**
	 * Fill the relevant parts of the specified stream info object with information on the data stream.
	 */
	public boolean fillStreamInfo ( StreamInfo status);
	
	/** The time, in milliseconds, of the current position in the DataStream,
	 * since the beginning of the stream.
	 * @return Time in milliseconds, or a negative number on failure */
	public int tellTime();
}

