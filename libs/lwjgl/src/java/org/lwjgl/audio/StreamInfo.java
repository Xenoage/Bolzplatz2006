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
import java.util.LinkedList;

/**
 * Class StreamInfo
 * Contains status and meta information about an audio data stream or a streaming playlist.
 */
public class StreamInfo
{
	// Fields
	// 
	public int track_id;
	// 
	public int position_in_track;
	public int track_length;
	// 
	public int position_in_playlist;
	// true when the playlist is playing, or when it is paused
	public boolean playing;
	// true only when the playlist is paused.
	public boolean paused;
	// 
	public String track_filename;
	// 
	public String track_title;
	// 
	public String track_artist;
	/** List of strings contained in the metadata of the source file. */
	public LinkedList meta_strings;
	public String vendor_string;
	// Methods
	// Constructors
	// Empty Constructor
	public StreamInfo ( )
	{
		track_id = -1;
		position_in_track = 0;
		position_in_playlist = 0;
		playing = false;
		paused = false;
	}

	// Operations
}

