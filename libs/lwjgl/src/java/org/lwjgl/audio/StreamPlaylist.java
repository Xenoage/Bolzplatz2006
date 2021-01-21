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
import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.ListIterator;
import org.lwjgl.openal.AL10;
import org.lwjgl.audio.IDataStream;

/**
 * Class StreamPlaylist
 * Class for playing back a series of data streams. A certain number of buffers
 * are used in rotation, each containing only a short clip of each data stream
 * rather than loading the entire data stream into a buffer.
 */
public class StreamPlaylist
{
        public static final int PLAYSTATUS_STOPPED = 0;
        public static final int PLAYSTATUS_PLAYING = 1;
        public static final int PLAYSTATUS_PAUSED = 2;
        public static final int PLAYSTATUS_FINISHED = 3;

        protected static ByteBuffer working_mem;
        // Fields
        /** The playlist. IDataStream instances are allowed to appear more than once for
         * repeated playback ONLY if it supports rewinding.
         */
        protected LinkedList enqueued_streams;
        /// An iterator pointing to the data stream currently being played back.
        protected ListIterator current_iter;

        protected Integer track_time_separator;
        /** Queue of objects of type Integer containing times (in milliseconds) since the
         * beginning of the current track.
         * The object 'track_time_separator' (value: -1) denotes the end of a track, and
         * means completed_track_time should be updated.
         * Each object on the queue corresponds to an OpenAL buffer on the source queue. */
        protected LinkedList buffer_end_track_time;
        protected int completed_track_time;
        protected int current_track_time;

        protected IDataStream current_stream;

        /// status of playback
        protected int status;

        protected int al_source;

        protected int sample_size;

        protected IntBuffer al_buffers;
        /** Maximum length, in milliseconds, for each OpenAL buffer used for streaming */
        protected int buffer_length;

        protected boolean loop;

        protected StreamInfo info;
        // Methods
        // Constructors
        // Empty Constructor
        public StreamPlaylist ( )
        {
                enqueued_streams = new LinkedList();
                status = PLAYSTATUS_STOPPED;
                al_buffers = BufferUtils.createIntBuffer(4);
                AL10.alGenBuffers(al_buffers);
                buffer_length = 500;
                IntBuffer source = BufferUtils.createIntBuffer(1);
                AL10.alGenSources(source);
                al_source = source.get(0);
                sample_size = 16;
                loop = false;
                info = new StreamInfo();
                track_time_separator = new Integer(-1);
                buffer_end_track_time = new LinkedList();
        }

        protected void finalize()
        {
                stop();
                IntBuffer source = BufferUtils.createIntBuffer(1);
                source.put(al_source);
                AL10.alDeleteSources(source);
                AL10.alDeleteBuffers(al_buffers);
        }

        /** Advances the playlist by one IDataStream object, if possible.
         * @return false if no more items remain in the playlist, true otherwise.
         */
        protected boolean advanceStream()
        {
                if (!current_iter.hasNext())
                {
                        if (loop)
                        {
                                current_stream = (IDataStream)enqueued_streams.getFirst();
                                current_iter = enqueued_streams.listIterator(0);
                                current_stream.fillStreamInfo(info);
                        }
                        else
                        {
                                status = PLAYSTATUS_FINISHED;
                                current_stream.rewind();
                                current_stream = null;
                                return false;
                        }
                }
                else
                {

                        info.track_id = current_iter.nextIndex();
                        current_stream = (IDataStream)current_iter.next();
                        current_stream.fillStreamInfo(info);
                }
                return true;
        }


        // Operations
        /**
         * Begin playing back the playlist, or unpause playback.
         * @return False if the playlist is empty.
         */
        public boolean play ( )
        {
                // if the stream is paused, this becomes really easy.
                if (status == PLAYSTATUS_PAUSED)
                {
                        AL10.alSourcePlay(al_source);
                        status = PLAYSTATUS_PLAYING;
                        info.playing = true;
                        info.paused = false;
                        update();
                        return true;
                }

                if (status != PLAYSTATUS_STOPPED)
                        return false;
                if (enqueued_streams.isEmpty())
                        return false;

                IntBuffer cur_buffer = BufferUtils.createIntBuffer(1);
                int cur;

                current_iter = enqueued_streams.listIterator(0);
                current_stream = (IDataStream)current_iter.next();
                current_stream.fillStreamInfo(info);

                cur = al_buffers.get(0);
                cur_buffer.put(0, cur);

                info.track_id = 0;
                info.position_in_track = 0;
                info.position_in_playlist = 0;
                // fill the first streaming buffer...
                ByteBuffer mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                while (mem == null)
                {
                        if (!current_iter.hasNext())
                        {
                                current_stream = null;
                                return false;
                        }
                        current_stream = (IDataStream)current_iter.next();
                        current_stream.fillStreamInfo(info);
                        mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                }
                buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                if (mem != working_mem)
                {
                        working_mem = mem;
                }
                // ... and play it.
                AL10.alSourceQueueBuffers(al_source, cur_buffer);
                AL10.alSourcePlay(al_source);

                // now fill up the others
                for (int i = 1; i < al_buffers.capacity(); ++i)
                {
                        cur = al_buffers.get(i);

                        mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                        if (mem == null)
                        {
                                buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                                buffer_end_track_time.addLast(track_time_separator);
                                do
                                {
                                        current_stream.rewind();
                                        if (!advanceStream())
                                                return true;
                                        mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                                }
                                while (mem == null);
                        }
                        buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                        if (mem != working_mem)
                        {
                                working_mem = mem;
                        }

                        // add the finished buffer to the queue
                        cur_buffer.put(0, cur);
                        AL10.alSourceQueueBuffers(al_source, cur_buffer);
                }
                status = PLAYSTATUS_PLAYING;
                info.playing = true;
                return true;
        }
        /**
         * Pauses playback.
         * @return False if the stream is currently not playing.
         */
        public boolean pause ( )
        {
                if (status != PLAYSTATUS_PLAYING)
                        return false;

                AL10.alSourcePause(al_source);
                status = PLAYSTATUS_PAUSED;
                info.paused = true;
                return true;
        }
        /**
         * Stops playback.
         */
        public void stop ( )
        {
                if (status == PLAYSTATUS_STOPPED)
                        return;
                AL10.alSourceStop(al_source);

                int free = AL10.alGetSourcei(al_source, AL10.AL_BUFFERS_PROCESSED);
                IntBuffer freed = BufferUtils.createIntBuffer(free);

                AL10.alSourceUnqueueBuffers(al_source, freed);
                if (current_stream != null)
                {
                        current_stream.rewind();
                        current_stream = null;
                }
                info.paused = false;
                info.playing = false;
                status = PLAYSTATUS_STOPPED;
                buffer_end_track_time.clear();
        }
        /**
         * Enqueues an audio data stream into the playlist.
         */
        public boolean addDataStream ( IDataStream stream)
        {
                if (stream == null)
                        return false;
                enqueued_streams.addLast(stream);
                if (enqueued_streams.size() == 1 && status == PLAYSTATUS_STOPPED)
                        stream.fillStreamInfo(info);
                return true;
        }
        /**
         * Stops playback if playing or paused, and clears the entire playlist.
         */
        public void clearPlayList ( )
        {
                if (status != PLAYSTATUS_STOPPED)
                        stop();
                current_stream = null;
                current_iter = null;
                enqueued_streams.clear();
        }
        /**
         * Enqueues a number of audio data streams into the playlist.
         */
        public boolean addDataStreams ( LinkedList streams)
        {
                if (streams == null)
                        return false;
                if (streams.size() == 0)
                        return true;
                if (enqueued_streams.size() == 0 && status == PLAYSTATUS_STOPPED)
                        ((IDataStream)streams.getFirst()).fillStreamInfo(info);
                enqueued_streams.addAll(streams);
                return true;
        }
        /**
         * This function must be called periodically (ideally about once every buffer length) in order to keep streaming up.
         */
        public void update ( )
        {
        try {
                if (status == PLAYSTATUS_PLAYING)
                {
                        int free = AL10.alGetSourcei(al_source, AL10.AL_BUFFERS_PROCESSED);
                        {
                                Integer timeobj;
                                for (int i = 0; i < free; ++i)
                                {
                                        timeobj = (Integer)buffer_end_track_time.remove();
                                        if (timeobj == track_time_separator)
                                                completed_track_time += current_track_time;
                                        else
                                                current_track_time = timeobj.intValue();
                                }
                                info.position_in_track = current_track_time;
                                info.position_in_playlist = completed_track_time + current_track_time;
                        }

                        //IntBuffer freed = BufferUtils.createIntBuffer(1);
                        IntBuffer freed = null;
                        if (free > 0)
                        {
                          freed = BufferUtils.createIntBuffer(free);
                          AL10.alSourceUnqueueBuffers(al_source, freed); //TEST
                        }


                        // pop any processed buffers off the source's queue and recycle them
                        //for (; free > 0; --free)
                        for (int i = 0; i < free; i++)
                        {
                                //AL10.alSourceUnqueueBuffers(al_source, freed);
                                int cur = freed.get(i);

                                ByteBuffer mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                                if (mem == null)
                                {
                                        buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                                        buffer_end_track_time.addLast(track_time_separator);
                                        do
                                        {
                                                //ANDI'S LOOP PATCH (this line only)
                                                if (loop) current_stream.rewind();
                                                //ORIGINAL: current_stream.rewind();
                                                if (!advanceStream())
                                                        return;
                                                mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                                        }
                                        while (mem == null);
                                }
                                buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                                if (mem != working_mem)
                                {
                                        working_mem = mem;
                                }

                                // add the finished buffer to the queue
                                //ALT AL10.alSourceQueueBuffers(al_source, freed);
                                IntBuffer reinsert = BufferUtils.createIntBuffer(1);
                                reinsert.put(0, cur);
                                AL10.alSourceQueueBuffers(al_source, reinsert);

                                // if update is being called too infrequently, keep restarting playback
                                int source_state = AL10.alGetSourcei(al_source, AL10.AL_SOURCE_STATE);
                                if(source_state == AL10.AL_STOPPED)
                                {
                                        AL10.alSourcePlay(al_source);
                                }
                        }
                }
                else if (status == PLAYSTATUS_FINISHED)
                {
                        int source_state = AL10.alGetSourcei(al_source, AL10.AL_SOURCE_STATE);
                        if(source_state == AL10.AL_STOPPED)
                        {
                                info.paused = false;
                                info.playing = false;
                                status = PLAYSTATUS_STOPPED;
                        }
                        // at this stage, just keep removing buffers from the queue until OpenAL has dealt with them
                        int free = AL10.alGetSourcei(al_source, AL10.AL_BUFFERS_PROCESSED);
                        {
                                Integer timeobj;
                                for (int i = 0; i < free; ++i)
                                {
                                        timeobj = (Integer)buffer_end_track_time.remove();
                                        if (timeobj == track_time_separator)
                                                completed_track_time += current_track_time;
                                        else
                                                current_track_time = timeobj.intValue();
                                }
                                info.position_in_track = current_track_time;
                                info.position_in_playlist = completed_track_time + current_track_time;
                        }
                        IntBuffer freed = BufferUtils.createIntBuffer(free);

                        AL10.alSourceUnqueueBuffers(al_source, freed);
                }
} catch (Exception ex) {
  ex.printStackTrace();
}
        }

        /**
         * Returns the StreamInfo object which contains the current state of the
         * playlist. Note that this object is automatically updated every time the
         * method Update() is called, and doesn't need to be requested using
         * GetStreamInfo() more than once.
         */
        public StreamInfo getStreamInfo ( )
        {
                return info;
        }
        /**
         * Sets some important stuff for stream playback.
         * Defaults are: 4 buffers of 500ms length each, preferred sample size 16 bits.<br>
         * Note: It is not possible to reduce the number of buffers while the playlist
         * is playing or paused.<br>
         * Note: If this call fails, <i>no</i> settings will have been changed.
         * @param settings Structure containing the new streaming settings. All values
         * are copied, the caller is free to use the object for other purposes after
         * SetStreamingSettings returns.
         * @return true on success, false on failure.
         */
        public boolean setStreamingSettings ( StreamingSettings settings)
        {
                if (settings == null)
                        return false;

                if (settings.buffers < 2 || settings.buffer_length <= 0 || settings.sample_size <= 0)
                        return false;

                int buffers = al_buffers.capacity();

                // can't reduce number of buffers while they are in use.
                if (status != PLAYSTATUS_STOPPED && settings.buffers < buffers)
                        return false;

                buffer_length = settings.buffer_length;
                sample_size = settings.sample_size;
                if (settings.buffers == buffers)
                        return true;

                IntBuffer new_buf = BufferUtils.createIntBuffer(settings.buffers);
                if (settings.buffers < buffers)
                {
                        int i;
                        for (i = 0; i < settings.buffers; ++i)
                                new_buf.put(i, al_buffers.get(i));

                        al_buffers.position(settings.buffers);
                        AL10.alDeleteBuffers(al_buffers.slice());
                }
                else
                {
                        // copy contents, then generate some more buffers and, if necessary, fill them with data.
                        new_buf.position(0);
                        new_buf.put(al_buffers);

                        new_buf.position(buffers);

                        IntBuffer remain = new_buf.slice();
                        AL10.alGenBuffers(remain);

                        if (status == PLAYSTATUS_PLAYING || status == PLAYSTATUS_PAUSED)
                        {
                                for (int i = buffers; i < settings.buffers; ++i)
                                {
                                        final int cur = new_buf.get(i);
                                        ByteBuffer mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);

                                        if (mem == null)
                                        {
                                                buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                                                buffer_end_track_time.addLast(track_time_separator);
                                                do
                                                {
                                                        //current_stream.rewind();
                                                        if (!advanceStream())
                                                        {
                                                                // stopped mid-way, add the buffers that we did fill to the list
                                                                new_buf.position(buffers);
                                                                new_buf.limit(i);
                                                                AL10.alSourceQueueBuffers(al_source, new_buf.slice());
                                                                new_buf.position(0);
                                                                new_buf.limit(settings.buffers);
                                                                al_buffers = new_buf;
                                                                return true;
                                                        }
                                                        mem = current_stream.fillALBuffer(cur, buffer_length, sample_size, working_mem);
                                                }
                                                while (mem == null);
                                        }
                                        buffer_end_track_time.addLast(new Integer(current_stream.tellTime()));
                                        if (mem != working_mem)
                                        {
                                                working_mem = mem;
                                        }
                                }

                                // add the finished buffer to the queue
                                AL10.alSourceQueueBuffers(al_source, remain);

                                // if update is being called too infrequently, keep restarting playback
                                int source_state = AL10.alGetSourcei(al_source, AL10.AL_SOURCE_STATE);
                                if(source_state == AL10.AL_STOPPED)
                                {
                                        AL10.alSourcePlay(al_source);
                                }
                        }
                }
                al_buffers = new_buf;
                return true;
        }
        /**
         * Returns the current settings for stream playback.
         */
        public StreamingSettings getStreamingSettings ( )
        {
                StreamingSettings settings = new StreamingSettings();
                settings.buffers = al_buffers.capacity();
                settings.buffer_length = buffer_length;
                settings.sample_size = sample_size;
                return settings;
        }
        /**
         * Enables or disables repeat mode. In repeat mode, once the end of the
         * playlist is reached, playback is restarted from the beginning of the
         * playlist. Default is: DISABLED.
         */
        public void enableRepeatMode ( boolean repeat)
        {
                loop = repeat;
        }

        /**
         * Removes completed IDataStream objects from the beginning of the playlist.
         * This will also work in repeat mode, but obviously doesn't make a lot of
         * sense in that case.
         * @return the number of stream sources purged.
         */
        public int purge()
        {
                int i = 0;
                if (current_stream == null)
                {
                        i = enqueued_streams.size();
                        enqueued_streams.clear();
                        return i;
                }
                for (;!enqueued_streams.isEmpty() && enqueued_streams.getFirst() != current_stream; ++i)
                {
                        enqueued_streams.remove();
                }
                return i;
        }

        /** Set the gain (volume) of the stream. Clamped to the range (0,1).
         * Default is 1.0.
         * @param gain The desired playback volume.
         * @return true on success.
         */
        public boolean setGain(float gain)
        {
                AL10.alSourcef(al_source, AL10.AL_GAIN, gain);
                int al_error = AL10.alGetError();
                if (al_error != AL10.AL_NO_ERROR)
                        return false;
                return true;
        }

        /** Returns the current setting for the gain. Can be modified using SetGain()
         * @return a negative number on failure, a positive number or 0 on success.
         */
        public float getGain()
        {
                float gain = AL10.alGetSourcef(al_source, AL10.AL_GAIN);
                int al_error = AL10.alGetError();
                if (al_error != AL10.AL_NO_ERROR)
                        return -1.0f;
                return gain;
        }
        
        
        //ANDI: gets the current status directly from this class
        public int getStatus()
        {
          return status;
        }
}
