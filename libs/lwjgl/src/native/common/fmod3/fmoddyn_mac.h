#include "fmod.h"

#ifndef _LWJGL_FMOD_DYN_H_
#define _LWJGL_FMOD_DYN_H_

typedef struct {
    void *module;

    signed char       (F_API *FSOUND_SetOutput)(int outputtype);
    signed char       (F_API *FSOUND_SetDriver)(int driver);
    signed char       (F_API *FSOUND_SetMixer)(int mixer);
    signed char       (F_API *FSOUND_SetBufferSize)(int len_ms);
    signed char       (F_API *FSOUND_SetHWND)(void *hwnd);
    signed char       (F_API *FSOUND_SetMinHardwareChannels)(int min);
    signed char       (F_API *FSOUND_SetMaxHardwareChannels)(int max);
    signed char       (F_API *FSOUND_SetMemorySystem)(void *pool, int poollen, FSOUND_ALLOCCALLBACK useralloc, FSOUND_REALLOCCALLBACK userrealloc, FSOUND_FREECALLBACK userfree);
    signed char       (F_API *FSOUND_Init)(int mixrate, int maxsoftwarechannels, unsigned int flags);
    void              (F_API *FSOUND_Close)();
    void              (F_API *FSOUND_Update)();   /* you must call this once a frame */
    void              (F_API *FSOUND_SetSpeakerMode)(unsigned int speakermode);
    void              (F_API *FSOUND_SetSFXMasterVolume)(int volume);
    void              (F_API *FSOUND_SetPanSeperation)(float pansep);
    void              (F_API *FSOUND_File_SetCallbacks)(FSOUND_OPENCALLBACK  useropen, FSOUND_CLOSECALLBACK userclose, FSOUND_READCALLBACK userread, FSOUND_SEEKCALLBACK  userseek, FSOUND_TELLCALLBACK  usertell);
    int               (F_API *FSOUND_GetError)();
    float             (F_API *FSOUND_GetVersion)();
    int               (F_API *FSOUND_GetOutput)();
    void *            (F_API *FSOUND_GetOutputHandle)();
    int               (F_API *FSOUND_GetDriver)();
    int               (F_API *FSOUND_GetMixer)();
    int               (F_API *FSOUND_GetNumDrivers)();
    const char *	  (F_API *FSOUND_GetDriverName)(int id);
    signed char       (F_API *FSOUND_GetDriverCaps)(int id, unsigned int *caps);
    int               (F_API *FSOUND_GetOutputRate)();
    int               (F_API *FSOUND_GetMaxChannels)();
    int               (F_API *FSOUND_GetMaxSamples)();
    int               (F_API *FSOUND_GetSFXMasterVolume)();
    signed char       (F_API *FSOUND_GetNumHWChannels)(int *num2d, int *num3d, int *total);
    int               (F_API *FSOUND_GetChannelsPlaying)();
    float             (F_API *FSOUND_GetCPUUsage)();
    void              (F_API *FSOUND_GetMemoryStats)(unsigned int *currentalloced, unsigned int *maxalloced);
    FSOUND_SAMPLE *   (F_API *FSOUND_Sample_Load)(int index, const char *name_or_data, unsigned int mode, int offset, int length);
    FSOUND_SAMPLE *   (F_API *FSOUND_Sample_Alloc)(int index, int length, unsigned int mode, int deffreq, int defvol, int defpan, int defpri);
    void              (F_API *FSOUND_Sample_Free)(FSOUND_SAMPLE *sptr);
    signed char       (F_API *FSOUND_Sample_Upload)(FSOUND_SAMPLE *sptr, void *srcdata, unsigned int mode);
    signed char       (F_API *FSOUND_Sample_Lock)(FSOUND_SAMPLE *sptr, int offset, int length, void **ptr1, void **ptr2, unsigned int *len1, unsigned int *len2);
    signed char       (F_API *FSOUND_Sample_Unlock)(FSOUND_SAMPLE *sptr, void *ptr1, void *ptr2, unsigned int len1, unsigned int len2);
    signed char       (F_API *FSOUND_Sample_SetMode)(FSOUND_SAMPLE *sptr, unsigned int mode);
    signed char       (F_API *FSOUND_Sample_SetLoopPoints)(FSOUND_SAMPLE *sptr, int loopstart, int loopend);
    signed char       (F_API *FSOUND_Sample_SetDefaults)(FSOUND_SAMPLE *sptr, int deffreq, int defvol, int defpan, int defpri);
    signed char       (F_API *FSOUND_Sample_SetDefaultsEx)(FSOUND_SAMPLE *sptr, int deffreq, int defvol, int defpan, int defpri, int varfreq, int varvol, int varpan);
    signed char       (F_API *FSOUND_Sample_SetMinMaxDistance)(FSOUND_SAMPLE *sptr, float min, float max);
    signed char       (F_API *FSOUND_Sample_SetMaxPlaybacks)(FSOUND_SAMPLE *sptr, int max);
    FSOUND_SAMPLE *   (F_API *FSOUND_Sample_Get)(int sampno);
    const char *      (F_API *FSOUND_Sample_GetName)(FSOUND_SAMPLE *sptr);
    unsigned int      (F_API *FSOUND_Sample_GetLength)(FSOUND_SAMPLE *sptr);
    signed char       (F_API *FSOUND_Sample_GetLoopPoints)(FSOUND_SAMPLE *sptr, int *loopstart, int *loopend);
    signed char       (F_API *FSOUND_Sample_GetDefaults)(FSOUND_SAMPLE *sptr, int *deffreq, int *defvol, int *defpan, int *defpri);
    signed char       (F_API *FSOUND_Sample_GetDefaultsEx)(FSOUND_SAMPLE *sptr, int *deffreq, int *defvol, int *defpan, int *defpri, int *varfreq, int *varvol, int *varpan);
    unsigned int      (F_API *FSOUND_Sample_GetMode)(FSOUND_SAMPLE *sptr);
    signed char       (F_API *FSOUND_Sample_GetMinMaxDistance)(FSOUND_SAMPLE *sptr, float *min, float *max);
    int               (F_API *FSOUND_PlaySound)(int channel, FSOUND_SAMPLE *sptr);
    int               (F_API *FSOUND_PlaySoundEx)(int channel, FSOUND_SAMPLE *sptr, FSOUND_DSPUNIT *dsp, signed char startpaused);
    signed char       (F_API *FSOUND_StopSound)(int channel);
    signed char       (F_API *FSOUND_SetFrequency)(int channel, int freq);
    signed char       (F_API *FSOUND_SetVolume)(int channel, int vol);
    signed char       (F_API *FSOUND_SetVolumeAbsolute)(int channel, int vol);
    signed char       (F_API *FSOUND_SetPan)(int channel, int pan);
    signed char       (F_API *FSOUND_SetSurround)(int channel, signed char surround);
    signed char       (F_API *FSOUND_SetMute)(int channel, signed char mute);
    signed char       (F_API *FSOUND_SetPriority)(int channel, int priority);
    signed char       (F_API *FSOUND_SetReserved)(int channel, signed char reserved);
    signed char       (F_API *FSOUND_SetPaused)(int channel, signed char paused);
    signed char       (F_API *FSOUND_SetLoopMode)(int channel, unsigned int loopmode);
    signed char       (F_API *FSOUND_SetCurrentPosition)(int channel, unsigned int offset);
    signed char       (F_API *FSOUND_3D_SetAttributes)(int channel, const float *pos, const float *vel);
    signed char       (F_API *FSOUND_3D_SetMinMaxDistance)(int channel, float min, float max);
    signed char       (F_API *FSOUND_IsPlaying)(int channel);
    int               (F_API *FSOUND_GetFrequency)(int channel);
    int               (F_API *FSOUND_GetVolume)(int channel);
    int               (F_API *FSOUND_GetAmplitude)(int channel);
    int               (F_API *FSOUND_GetPan)(int channel);
    signed char       (F_API *FSOUND_GetSurround)(int channel);
    signed char       (F_API *FSOUND_GetMute)(int channel);
    int               (F_API *FSOUND_GetPriority)(int channel);
    signed char       (F_API *FSOUND_GetReserved)(int channel);
    signed char       (F_API *FSOUND_GetPaused)(int channel);
    unsigned int      (F_API *FSOUND_GetLoopMode)(int channel);
    unsigned int      (F_API *FSOUND_GetCurrentPosition)(int channel);
    FSOUND_SAMPLE *   (F_API *FSOUND_GetCurrentSample)(int channel);
    signed char       (F_API *FSOUND_GetCurrentLevels)(int channel, float *l, float *r);
    int               (F_API *FSOUND_GetNumSubChannels)(int channel);
    int               (F_API *FSOUND_GetSubChannel)(int channel, int subchannel);
    signed char       (F_API *FSOUND_3D_GetAttributes)(int channel, float *pos, float *vel);
    signed char       (F_API *FSOUND_3D_GetMinMaxDistance)(int channel, float *min, float *max);
    void              (F_API *FSOUND_3D_SetDopplerFactor)(float scale);
    void              (F_API *FSOUND_3D_SetDistanceFactor)(float scale);
    void              (F_API *FSOUND_3D_SetRolloffFactor)(float scale);
    void              (F_API *FSOUND_3D_Listener_SetCurrent)(int current, int numlisteners);  /* use this if you use multiple listeners / splitscreen */
    void              (F_API *FSOUND_3D_Listener_SetAttributes)(const float *pos, const float *vel, float fx, float fy, float fz, float tx, float ty, float tz);
    void              (F_API *FSOUND_3D_Listener_GetAttributes)(float *pos, float *vel, float *fx, float *fy, float *fz, float *tx, float *ty, float *tz);
    int               (F_API *FSOUND_FX_Enable)(int channel, unsigned int fx);    /* See FSOUND_FX_MODES */
    signed char       (F_API *FSOUND_FX_Disable)(int channel);
    signed char       (F_API *FSOUND_FX_SetChorus)(int fxid, float WetDryMix, float Depth, float Feedback, float Frequency, int Waveform, float Delay, int Phase);
    signed char       (F_API *FSOUND_FX_SetCompressor)(int fxid, float Gain, float Attack, float Release, float Threshold, float Ratio, float Predelay);
    signed char       (F_API *FSOUND_FX_SetDistortion)(int fxid, float Gain, float Edge, float PostEQCenterFrequency, float PostEQBandwidth, float PreLowpassCutoff);
    signed char       (F_API *FSOUND_FX_SetEcho)(int fxid, float WetDryMix, float Feedback, float LeftDelay, float RightDelay, int PanDelay);
    signed char       (F_API *FSOUND_FX_SetFlanger)(int fxid, float WetDryMix, float Depth, float Feedback, float Frequency, int Waveform, float Delay, int Phase);
    signed char       (F_API *FSOUND_FX_SetGargle)(int fxid, int RateHz, int WaveShape);
    signed char       (F_API *FSOUND_FX_SetI3DL2Reverb)(int fxid, int Room, int RoomHF, float RoomRolloffFactor, float DecayTime, float DecayHFRatio, int Reflections, float ReflectionsDelay, int Reverb, float ReverbDelay, float Diffusion, float Density, float HFReference);
    signed char       (F_API *FSOUND_FX_SetParamEQ)(int fxid, float Center, float Bandwidth, float Gain);
    signed char       (F_API *FSOUND_FX_SetWavesReverb)(int fxid, float InGain, float ReverbMix, float ReverbTime, float HighFreqRTRatio);  
    signed char       (F_API *FSOUND_Stream_SetBufferSize)(int ms);      /* call this before opening streams, not after */
    FSOUND_STREAM *   (F_API *FSOUND_Stream_Open)(const char *name_or_data, unsigned int mode, int offset, int length);
    FSOUND_STREAM *   (F_API *FSOUND_Stream_Create)(FSOUND_STREAMCALLBACK callback, int length, unsigned int mode, int samplerate, void *userdata);
    signed char       (F_API *FSOUND_Stream_Close)(FSOUND_STREAM *stream);
    int               (F_API *FSOUND_Stream_Play)(int channel, FSOUND_STREAM *stream);
    int               (F_API *FSOUND_Stream_PlayEx)(int channel, FSOUND_STREAM *stream, FSOUND_DSPUNIT *dsp, signed char startpaused);
    signed char       (F_API *FSOUND_Stream_Stop)(FSOUND_STREAM *stream);
    signed char       (F_API *FSOUND_Stream_SetPosition)(FSOUND_STREAM *stream, unsigned int position);
    unsigned int      (F_API *FSOUND_Stream_GetPosition)(FSOUND_STREAM *stream);
    signed char       (F_API *FSOUND_Stream_SetTime)(FSOUND_STREAM *stream, int ms);
    int               (F_API *FSOUND_Stream_GetTime)(FSOUND_STREAM *stream);
    int               (F_API *FSOUND_Stream_GetLength)(FSOUND_STREAM *stream);
    int               (F_API *FSOUND_Stream_GetLengthMs)(FSOUND_STREAM *stream);
    signed char       (F_API *FSOUND_Stream_SetMode)(FSOUND_STREAM *stream, unsigned int mode);
    unsigned int      (F_API *FSOUND_Stream_GetMode)(FSOUND_STREAM *stream);
    signed char       (F_API *FSOUND_Stream_SetLoopPoints)(FSOUND_STREAM *stream, unsigned int loopstartpcm, unsigned int loopendpcm);
    signed char       (F_API *FSOUND_Stream_SetLoopCount)(FSOUND_STREAM *stream, int count);
    int               (F_API *FSOUND_Stream_GetOpenState)(FSOUND_STREAM *stream);
    FSOUND_SAMPLE *   (F_API *FSOUND_Stream_GetSample)(FSOUND_STREAM *stream);   /* every stream contains a sample to playback on */
    FSOUND_DSPUNIT *  (F_API *FSOUND_Stream_CreateDSP)(FSOUND_STREAM *stream, FSOUND_DSPCALLBACK callback, int priority, void *userdata);
    signed char       (F_API *FSOUND_Stream_SetEndCallback)(FSOUND_STREAM *stream, FSOUND_STREAMCALLBACK callback, void *userdata);
    signed char       (F_API *FSOUND_Stream_SetSyncCallback)(FSOUND_STREAM *stream, FSOUND_STREAMCALLBACK callback, void *userdata);
    FSOUND_SYNCPOINT *(F_API *FSOUND_Stream_AddSyncPoint)(FSOUND_STREAM *stream, unsigned int pcmoffset, const char *name);
    signed char       (F_API *FSOUND_Stream_DeleteSyncPoint)(FSOUND_SYNCPOINT *point);
    int               (F_API *FSOUND_Stream_GetNumSyncPoints)(FSOUND_STREAM *stream);
    FSOUND_SYNCPOINT *(F_API *FSOUND_Stream_GetSyncPoint)(FSOUND_STREAM *stream, int index);
    char *            (F_API *FSOUND_Stream_GetSyncPointInfo)(FSOUND_SYNCPOINT *point, unsigned int *pcmoffset);
    signed char       (F_API *FSOUND_Stream_SetSubStream)(FSOUND_STREAM *stream, int index);
    int               (F_API *FSOUND_Stream_GetNumSubStreams)(FSOUND_STREAM *stream);
    signed char       (F_API *FSOUND_Stream_SetSubStreamSentence)(FSOUND_STREAM *stream, const int *sentencelist, int numitems);
    signed char       (F_API *FSOUND_Stream_GetNumTagFields)(FSOUND_STREAM *stream, int *num);
    signed char       (F_API *FSOUND_Stream_GetTagField)(FSOUND_STREAM *stream, int num, int *type, char **name, void **value, int *length);
    signed char       (F_API *FSOUND_Stream_FindTagField)(FSOUND_STREAM *stream, int type, const char *name, void **value, int *length);
    signed char       (F_API *FSOUND_Stream_Net_SetProxy)(const char *proxy);
    char *            (F_API *FSOUND_Stream_Net_GetLastServerStatus)();
    signed char       (F_API *FSOUND_Stream_Net_SetBufferProperties)(int buffersize, int prebuffer_percent, int rebuffer_percent);
    signed char       (F_API *FSOUND_Stream_Net_GetBufferProperties)(int *buffersize, int *prebuffer_percent, int *rebuffer_percent);
    signed char       (F_API *FSOUND_Stream_Net_SetMetadataCallback)(FSOUND_STREAM *stream, FSOUND_METADATACALLBACK callback, void *userdata);
    signed char       (F_API *FSOUND_Stream_Net_GetStatus)(FSOUND_STREAM *stream, int *status, int *bufferpercentused, int *bitrate, unsigned int *flags);
    signed char       (F_API *FSOUND_CD_Play)(char drive, int track);
    void              (F_API *FSOUND_CD_SetPlayMode)(char drive, signed char mode);
    signed char       (F_API *FSOUND_CD_Stop)(char drive);
    signed char       (F_API *FSOUND_CD_SetPaused)(char drive, signed char paused);
    signed char       (F_API *FSOUND_CD_SetVolume)(char drive, int volume);
    signed char       (F_API *FSOUND_CD_SetTrackTime)(char drive, unsigned int ms);
    // Not available on Mac
    //signed char       (F_API *FSOUND_CD_OpenTray)(char drive, signed char open);
    signed char       (F_API *FSOUND_CD_GetPaused)(char drive);
    int               (F_API *FSOUND_CD_GetTrack)(char drive);
    int               (F_API *FSOUND_CD_GetNumTracks)(char drive);
    int               (F_API *FSOUND_CD_GetVolume)(char drive);
    int               (F_API *FSOUND_CD_GetTrackLength)(char drive, int track); 
    int               (F_API *FSOUND_CD_GetTrackTime)(char drive);
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_Create)(FSOUND_DSPCALLBACK callback, int priority, void *userdata);
    void              (F_API *FSOUND_DSP_Free)(FSOUND_DSPUNIT *unit);
    void              (F_API *FSOUND_DSP_SetPriority)(FSOUND_DSPUNIT *unit, int priority);
    int               (F_API *FSOUND_DSP_GetPriority)(FSOUND_DSPUNIT *unit);
    void              (F_API *FSOUND_DSP_SetActive)(FSOUND_DSPUNIT *unit, signed char active);
    signed char       (F_API *FSOUND_DSP_GetActive)(FSOUND_DSPUNIT *unit);
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_GetClearUnit)();
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_GetSFXUnit)();
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_GetMusicUnit)();
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_GetFFTUnit)();
    FSOUND_DSPUNIT *  (F_API *FSOUND_DSP_GetClipAndCopyUnit)();
    signed char       (F_API *FSOUND_DSP_MixBuffers)(void *destbuffer, void *srcbuffer, int len, int freq, int vol, int pan, unsigned int mode);
    void              (F_API *FSOUND_DSP_ClearMixBuffer)();
    int               (F_API *FSOUND_DSP_GetBufferLength)();      /* Length of each DSP update */
    int               (F_API *FSOUND_DSP_GetBufferLengthTotal)(); /* Total buffer length due to FSOUND_SetBufferSize */
    float *           (F_API *FSOUND_DSP_GetSpectrum)();          /* Array of 512 floats - call FSOUND_DSP_SetActive(FSOUND_DSP_GetFFTUnit(), TRUE)) for this to work. */
    signed char       (F_API *FSOUND_Reverb_SetProperties)(const FSOUND_REVERB_PROPERTIES *prop);
    signed char       (F_API *FSOUND_Reverb_GetProperties)(FSOUND_REVERB_PROPERTIES *prop);
    signed char       (F_API *FSOUND_Reverb_SetChannelProperties)(int channel, const FSOUND_REVERB_CHANNELPROPERTIES *prop);
    signed char       (F_API *FSOUND_Reverb_GetChannelProperties)(int channel, FSOUND_REVERB_CHANNELPROPERTIES *prop);
    signed char       (F_API *FSOUND_Record_SetDriver)(int outputtype);
    int               (F_API *FSOUND_Record_GetNumDrivers)();
    const char *     (F_API *FSOUND_Record_GetDriverName)(int id);
    int               (F_API *FSOUND_Record_GetDriver)();
    signed char       (F_API *FSOUND_Record_StartSample)(FSOUND_SAMPLE *sptr, signed char loop);
    signed char       (F_API *FSOUND_Record_Stop)();
    int               (F_API *FSOUND_Record_GetPosition)();  
    FMUSIC_MODULE *   (F_API *FMUSIC_LoadSong)(const char *name);
    FMUSIC_MODULE *   (F_API *FMUSIC_LoadSongEx)(const char *name_or_data, int offset, int length, unsigned int mode, const int *samplelist, int samplelistnum);
    int               (F_API *FMUSIC_GetOpenState)(FMUSIC_MODULE *mod);
    signed char       (F_API *FMUSIC_FreeSong)(FMUSIC_MODULE *mod);
    signed char       (F_API *FMUSIC_PlaySong)(FMUSIC_MODULE *mod);
    signed char       (F_API *FMUSIC_StopSong)(FMUSIC_MODULE *mod);
    void              (F_API *FMUSIC_StopAllSongs)();
    signed char       (F_API *FMUSIC_SetZxxCallback)(FMUSIC_MODULE *mod, FMUSIC_CALLBACK callback);
    signed char       (F_API *FMUSIC_SetRowCallback)(FMUSIC_MODULE *mod, FMUSIC_CALLBACK callback, int rowstep);
    signed char       (F_API *FMUSIC_SetOrderCallback)(FMUSIC_MODULE *mod, FMUSIC_CALLBACK callback, int orderstep);
    signed char       (F_API *FMUSIC_SetInstCallback)(FMUSIC_MODULE *mod, FMUSIC_CALLBACK callback, int instrument);
    signed char       (F_API *FMUSIC_SetSample)(FMUSIC_MODULE *mod, int sampno, FSOUND_SAMPLE *sptr);
    signed char       (F_API *FMUSIC_SetUserData)(FMUSIC_MODULE *mod, void *userdata);
    signed char       (F_API *FMUSIC_OptimizeChannels)(FMUSIC_MODULE *mod, int maxchannels, int minvolume);
    signed char       (F_API *FMUSIC_SetReverb)(signed char reverb);             /* MIDI only */
    signed char       (F_API *FMUSIC_SetLooping)(FMUSIC_MODULE *mod, signed char looping);
    signed char       (F_API *FMUSIC_SetOrder)(FMUSIC_MODULE *mod, int order);
    signed char       (F_API *FMUSIC_SetPaused)(FMUSIC_MODULE *mod, signed char pause);
    signed char       (F_API *FMUSIC_SetMasterVolume)(FMUSIC_MODULE *mod, int volume);
    signed char       (F_API *FMUSIC_SetMasterSpeed)(FMUSIC_MODULE *mode, float speed);
    signed char       (F_API *FMUSIC_SetPanSeperation)(FMUSIC_MODULE *mod, float pansep);
    const char *      (F_API *FMUSIC_GetName)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetType)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetNumOrders)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetNumPatterns)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetNumInstruments)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetNumSamples)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetNumChannels)(FMUSIC_MODULE *mod);
    FSOUND_SAMPLE *   (F_API *FMUSIC_GetSample)(FMUSIC_MODULE *mod, int sampno);
    int               (F_API *FMUSIC_GetPatternLength)(FMUSIC_MODULE *mod, int orderno);
    signed char       (F_API *FMUSIC_IsFinished)(FMUSIC_MODULE *mod);
    signed char       (F_API *FMUSIC_IsPlaying)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetMasterVolume)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetGlobalVolume)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetOrder)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetPattern)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetSpeed)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetBPM)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetRow)(FMUSIC_MODULE *mod);
    signed char       (F_API *FMUSIC_GetPaused)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetTime)(FMUSIC_MODULE *mod);
    int               (F_API *FMUSIC_GetRealChannel)(FMUSIC_MODULE *mod, int modchannel);
    void*			  (F_API *FMUSIC_GetUserData)(FMUSIC_MODULE *mod); 
} FMOD_INSTANCE;

static FMOD_INSTANCE *FMOD_CreateInstance(char *dllName) {
    FMOD_INSTANCE *instance;	
    instance = (FMOD_INSTANCE *)calloc(sizeof(FMOD_INSTANCE), 1);
    if(instance) {
	instance->FSOUND_SetOutput = &FSOUND_SetOutput;
	instance->FSOUND_SetDriver = &FSOUND_SetDriver;
	instance->FSOUND_SetMixer = &FSOUND_SetMixer;
	instance->FSOUND_SetBufferSize = &FSOUND_SetBufferSize;
	instance->FSOUND_SetHWND = &FSOUND_SetHWND;
	instance->FSOUND_SetMinHardwareChannels = &FSOUND_SetMinHardwareChannels;
	instance->FSOUND_SetMaxHardwareChannels = &FSOUND_SetMaxHardwareChannels;
	instance->FSOUND_SetMemorySystem = &FSOUND_SetMemorySystem;
	instance->FSOUND_Init = &FSOUND_Init;
	instance->FSOUND_Close = &FSOUND_Close;
	instance->FSOUND_Update = &FSOUND_Update;
	instance->FSOUND_SetSpeakerMode = &FSOUND_SetSpeakerMode;
	instance->FSOUND_SetSFXMasterVolume = &FSOUND_SetSFXMasterVolume;
	instance->FSOUND_SetPanSeperation = &FSOUND_SetPanSeperation;
	instance->FSOUND_File_SetCallbacks = &FSOUND_File_SetCallbacks;
	instance->FSOUND_GetError = &FSOUND_GetError;
	instance->FSOUND_GetVersion = &FSOUND_GetVersion;
	instance->FSOUND_GetOutput = &FSOUND_GetOutput;
	instance->FSOUND_GetOutputHandle = &FSOUND_GetOutputHandle;
	instance->FSOUND_GetDriver = &FSOUND_GetDriver;
	instance->FSOUND_GetMixer = &FSOUND_GetMixer;
	instance->FSOUND_GetNumDrivers = &FSOUND_GetNumDrivers;
	instance->FSOUND_GetDriverName = &FSOUND_GetDriverName;
	instance->FSOUND_GetDriverCaps = &FSOUND_GetDriverCaps;
	instance->FSOUND_GetOutputRate = &FSOUND_GetOutputRate;
	instance->FSOUND_GetMaxChannels = &FSOUND_GetMaxChannels;
	instance->FSOUND_GetMaxSamples = &FSOUND_GetMaxSamples;
	instance->FSOUND_GetSFXMasterVolume = &FSOUND_GetSFXMasterVolume;
	instance->FSOUND_GetNumHWChannels = &FSOUND_GetNumHWChannels;
	instance->FSOUND_GetChannelsPlaying = &FSOUND_GetChannelsPlaying;
	instance->FSOUND_GetCPUUsage = &FSOUND_GetCPUUsage;
	instance->FSOUND_GetMemoryStats = &FSOUND_GetMemoryStats;
	instance->FSOUND_Sample_Load = &FSOUND_Sample_Load;
	instance->FSOUND_Sample_Alloc = &FSOUND_Sample_Alloc;
	instance->FSOUND_Sample_Free = &FSOUND_Sample_Free;
	instance->FSOUND_Sample_Upload = &FSOUND_Sample_Upload;
	instance->FSOUND_Sample_Lock = &FSOUND_Sample_Lock;
	instance->FSOUND_Sample_Unlock = &FSOUND_Sample_Unlock;
	instance->FSOUND_Sample_SetMode = &FSOUND_Sample_SetMode;
	instance->FSOUND_Sample_SetLoopPoints = &FSOUND_Sample_SetLoopPoints;
	instance->FSOUND_Sample_SetDefaults = &FSOUND_Sample_SetDefaults;
	instance->FSOUND_Sample_SetDefaultsEx = &FSOUND_Sample_SetDefaultsEx;
	instance->FSOUND_Sample_SetMinMaxDistance = &FSOUND_Sample_SetMinMaxDistance;
	instance->FSOUND_Sample_SetMaxPlaybacks = &FSOUND_Sample_SetMaxPlaybacks;
	instance->FSOUND_Sample_Get = &FSOUND_Sample_Get;
	instance->FSOUND_Sample_GetName = &FSOUND_Sample_GetName;
	instance->FSOUND_Sample_GetLength = &FSOUND_Sample_GetLength;
	instance->FSOUND_Sample_GetLoopPoints = &FSOUND_Sample_GetLoopPoints;
	instance->FSOUND_Sample_GetDefaults = &FSOUND_Sample_GetDefaults;
	instance->FSOUND_Sample_GetDefaultsEx = &FSOUND_Sample_GetDefaultsEx;
	instance->FSOUND_Sample_GetMode = &FSOUND_Sample_GetMode;
	instance->FSOUND_Sample_GetMinMaxDistance = &FSOUND_Sample_GetMinMaxDistance;
	instance->FSOUND_PlaySound = &FSOUND_PlaySound;
	instance->FSOUND_PlaySoundEx = &FSOUND_PlaySoundEx;
	instance->FSOUND_StopSound = &FSOUND_StopSound;
	instance->FSOUND_SetFrequency = &FSOUND_SetFrequency;
	instance->FSOUND_SetVolume = &FSOUND_SetVolume;
	instance->FSOUND_SetVolumeAbsolute = &FSOUND_SetVolumeAbsolute;
	instance->FSOUND_SetPan = &FSOUND_SetPan;
	instance->FSOUND_SetSurround = &FSOUND_SetSurround;
	instance->FSOUND_SetMute = &FSOUND_SetMute;
	instance->FSOUND_SetPriority = &FSOUND_SetPriority;
	instance->FSOUND_SetReserved = &FSOUND_SetReserved;
	instance->FSOUND_SetPaused = &FSOUND_SetPaused;
	instance->FSOUND_SetLoopMode = &FSOUND_SetLoopMode;
	instance->FSOUND_SetCurrentPosition = &FSOUND_SetCurrentPosition;
	instance->FSOUND_3D_SetAttributes = &FSOUND_3D_SetAttributes;
	instance->FSOUND_3D_SetMinMaxDistance = &FSOUND_3D_SetMinMaxDistance;
	instance->FSOUND_IsPlaying = &FSOUND_IsPlaying;
	instance->FSOUND_GetFrequency = &FSOUND_GetFrequency;
	instance->FSOUND_GetVolume = &FSOUND_GetVolume;
	instance->FSOUND_GetAmplitude = &FSOUND_GetAmplitude;
	instance->FSOUND_GetPan = &FSOUND_GetPan;
	instance->FSOUND_GetSurround = &FSOUND_GetSurround;
	instance->FSOUND_GetMute = &FSOUND_GetMute;
	instance->FSOUND_GetPriority = &FSOUND_GetPriority;
	instance->FSOUND_GetReserved = &FSOUND_GetReserved;
	instance->FSOUND_GetPaused = &FSOUND_GetPaused;
	instance->FSOUND_GetLoopMode = &FSOUND_GetLoopMode;
	instance->FSOUND_GetCurrentPosition = &FSOUND_GetCurrentPosition;
	instance->FSOUND_GetCurrentSample = &FSOUND_GetCurrentSample;
	instance->FSOUND_GetCurrentLevels = &FSOUND_GetCurrentLevels;
	instance->FSOUND_GetNumSubChannels = &FSOUND_GetNumSubChannels;
	instance->FSOUND_GetSubChannel = &FSOUND_GetSubChannel;
	instance->FSOUND_3D_GetAttributes = &FSOUND_3D_GetAttributes;
	instance->FSOUND_3D_GetMinMaxDistance = &FSOUND_3D_GetMinMaxDistance;
	instance->FSOUND_3D_SetDopplerFactor = &FSOUND_3D_SetDopplerFactor;
	instance->FSOUND_3D_SetDistanceFactor = &FSOUND_3D_SetDistanceFactor;
	instance->FSOUND_3D_SetRolloffFactor = &FSOUND_3D_SetRolloffFactor;
	instance->FSOUND_3D_Listener_SetCurrent = &FSOUND_3D_Listener_SetCurrent;
	instance->FSOUND_3D_Listener_SetAttributes = &FSOUND_3D_Listener_SetAttributes;
	instance->FSOUND_3D_Listener_GetAttributes = &FSOUND_3D_Listener_GetAttributes;
	instance->FSOUND_FX_Enable = &FSOUND_FX_Enable;
	instance->FSOUND_FX_Disable = &FSOUND_FX_Disable;
	instance->FSOUND_FX_SetChorus = &FSOUND_FX_SetChorus;
	instance->FSOUND_FX_SetCompressor = &FSOUND_FX_SetCompressor;
	instance->FSOUND_FX_SetDistortion = &FSOUND_FX_SetDistortion;
	instance->FSOUND_FX_SetEcho = &FSOUND_FX_SetEcho;
	instance->FSOUND_FX_SetFlanger = &FSOUND_FX_SetFlanger;
	instance->FSOUND_FX_SetGargle = &FSOUND_FX_SetGargle;
	instance->FSOUND_FX_SetI3DL2Reverb = &FSOUND_FX_SetI3DL2Reverb;
	instance->FSOUND_FX_SetParamEQ = &FSOUND_FX_SetParamEQ;
	instance->FSOUND_FX_SetWavesReverb = &FSOUND_FX_SetWavesReverb;
	instance->FSOUND_Stream_SetBufferSize = &FSOUND_Stream_SetBufferSize;
	instance->FSOUND_Stream_Open = &FSOUND_Stream_Open;
	instance->FSOUND_Stream_Create = &FSOUND_Stream_Create;
	instance->FSOUND_Stream_Close = &FSOUND_Stream_Close;
	instance->FSOUND_Stream_Play = &FSOUND_Stream_Play;
	instance->FSOUND_Stream_PlayEx = &FSOUND_Stream_PlayEx;
	instance->FSOUND_Stream_Stop = &FSOUND_Stream_Stop;
	instance->FSOUND_Stream_SetPosition = &FSOUND_Stream_SetPosition;
	instance->FSOUND_Stream_GetPosition = &FSOUND_Stream_GetPosition;
	instance->FSOUND_Stream_SetTime = &FSOUND_Stream_SetTime;
	instance->FSOUND_Stream_GetTime = &FSOUND_Stream_GetTime;
	instance->FSOUND_Stream_GetLength = &FSOUND_Stream_GetLength;
	instance->FSOUND_Stream_GetLengthMs = &FSOUND_Stream_GetLengthMs;
	instance->FSOUND_Stream_SetMode = &FSOUND_Stream_SetMode;
	instance->FSOUND_Stream_GetMode = &FSOUND_Stream_GetMode;
	instance->FSOUND_Stream_SetLoopPoints = &FSOUND_Stream_SetLoopPoints;
	instance->FSOUND_Stream_SetLoopCount = &FSOUND_Stream_SetLoopCount;
	instance->FSOUND_Stream_GetOpenState = &FSOUND_Stream_GetOpenState;
	instance->FSOUND_Stream_GetSample = &FSOUND_Stream_GetSample;
	instance->FSOUND_Stream_CreateDSP = &FSOUND_Stream_CreateDSP;
	instance->FSOUND_Stream_SetEndCallback = &FSOUND_Stream_SetEndCallback;
	instance->FSOUND_Stream_SetSyncCallback = &FSOUND_Stream_SetSyncCallback;
	instance->FSOUND_Stream_AddSyncPoint = &FSOUND_Stream_AddSyncPoint;
	instance->FSOUND_Stream_DeleteSyncPoint = &FSOUND_Stream_DeleteSyncPoint;
	instance->FSOUND_Stream_GetNumSyncPoints = &FSOUND_Stream_GetNumSyncPoints;
	instance->FSOUND_Stream_GetSyncPoint = &FSOUND_Stream_GetSyncPoint;
	instance->FSOUND_Stream_GetSyncPointInfo = &FSOUND_Stream_GetSyncPointInfo;
	instance->FSOUND_Stream_SetSubStream = &FSOUND_Stream_SetSubStream;
	instance->FSOUND_Stream_GetNumSubStreams = &FSOUND_Stream_GetNumSubStreams;
	instance->FSOUND_Stream_SetSubStreamSentence = &FSOUND_Stream_SetSubStreamSentence;
	instance->FSOUND_Stream_GetNumTagFields = &FSOUND_Stream_GetNumTagFields;
	instance->FSOUND_Stream_GetTagField = &FSOUND_Stream_GetTagField;
	instance->FSOUND_Stream_FindTagField = &FSOUND_Stream_FindTagField;
	instance->FSOUND_Stream_Net_SetProxy = &FSOUND_Stream_Net_SetProxy;
	instance->FSOUND_Stream_Net_GetLastServerStatus = &FSOUND_Stream_Net_GetLastServerStatus;
	instance->FSOUND_Stream_Net_SetBufferProperties = &FSOUND_Stream_Net_SetBufferProperties;
	instance->FSOUND_Stream_Net_GetBufferProperties = &FSOUND_Stream_Net_GetBufferProperties;
	instance->FSOUND_Stream_Net_SetMetadataCallback = &FSOUND_Stream_Net_SetMetadataCallback;
	instance->FSOUND_Stream_Net_GetStatus = &FSOUND_Stream_Net_GetStatus;
	instance->FSOUND_CD_Play = &FSOUND_CD_Play;
	instance->FSOUND_CD_SetPlayMode = &FSOUND_CD_SetPlayMode;
	instance->FSOUND_CD_Stop = &FSOUND_CD_Stop;
	instance->FSOUND_CD_SetPaused = &FSOUND_CD_SetPaused;
	instance->FSOUND_CD_SetVolume = &FSOUND_CD_SetVolume;
	instance->FSOUND_CD_SetTrackTime = &FSOUND_CD_SetTrackTime;
	
	// Not available on mac
	//instance->FSOUND_CD_OpenTray = &FSOUND_CD_OpenTray_noop;

	instance->FSOUND_CD_GetPaused = &FSOUND_CD_GetPaused;
	instance->FSOUND_CD_GetTrack = &FSOUND_CD_GetTrack;
	instance->FSOUND_CD_GetNumTracks = &FSOUND_CD_GetNumTracks;
	instance->FSOUND_CD_GetVolume = &FSOUND_CD_GetVolume;
	instance->FSOUND_CD_GetTrackLength = &FSOUND_CD_GetTrackLength;
	instance->FSOUND_CD_GetTrackTime = &FSOUND_CD_GetTrackTime;
	instance->FSOUND_DSP_Create = &FSOUND_DSP_Create;
	instance->FSOUND_DSP_Free = &FSOUND_DSP_Free;
	instance->FSOUND_DSP_SetPriority = &FSOUND_DSP_SetPriority;
	instance->FSOUND_DSP_GetPriority = &FSOUND_DSP_GetPriority;
	instance->FSOUND_DSP_SetActive = &FSOUND_DSP_SetActive;
	instance->FSOUND_DSP_GetActive = &FSOUND_DSP_GetActive;
	instance->FSOUND_DSP_GetClearUnit = &FSOUND_DSP_GetClearUnit;
	instance->FSOUND_DSP_GetSFXUnit = &FSOUND_DSP_GetSFXUnit;
	instance->FSOUND_DSP_GetMusicUnit = &FSOUND_DSP_GetMusicUnit;
	instance->FSOUND_DSP_GetFFTUnit = &FSOUND_DSP_GetFFTUnit;
	instance->FSOUND_DSP_GetClipAndCopyUnit = &FSOUND_DSP_GetClipAndCopyUnit;
	instance->FSOUND_DSP_MixBuffers = &FSOUND_DSP_MixBuffers;
	instance->FSOUND_DSP_ClearMixBuffer = &FSOUND_DSP_ClearMixBuffer;
	instance->FSOUND_DSP_GetBufferLength = &FSOUND_DSP_GetBufferLength;
	instance->FSOUND_DSP_GetBufferLengthTotal = &FSOUND_DSP_GetBufferLengthTotal;
	instance->FSOUND_DSP_GetSpectrum = &FSOUND_DSP_GetSpectrum;
	instance->FSOUND_Reverb_SetProperties = &FSOUND_Reverb_SetProperties;
	instance->FSOUND_Reverb_GetProperties = &FSOUND_Reverb_GetProperties;
	instance->FSOUND_Reverb_SetChannelProperties = &FSOUND_Reverb_SetChannelProperties;
	instance->FSOUND_Reverb_GetChannelProperties = &FSOUND_Reverb_GetChannelProperties;
	instance->FSOUND_Record_SetDriver = &FSOUND_Record_SetDriver;
	instance->FSOUND_Record_GetNumDrivers = &FSOUND_Record_GetNumDrivers;
	instance->FSOUND_Record_GetDriverName = &FSOUND_Record_GetDriverName;
	instance->FSOUND_Record_GetDriver = &FSOUND_Record_GetDriver;
	instance->FSOUND_Record_StartSample = &FSOUND_Record_StartSample;
	instance->FSOUND_Record_Stop = &FSOUND_Record_Stop;
	instance->FSOUND_Record_GetPosition = &FSOUND_Record_GetPosition;
	instance->FMUSIC_LoadSong = &FMUSIC_LoadSong;
	instance->FMUSIC_LoadSongEx = &FMUSIC_LoadSongEx;
	instance->FMUSIC_GetOpenState = &FMUSIC_GetOpenState;
	instance->FMUSIC_FreeSong = &FMUSIC_FreeSong;
	instance->FMUSIC_PlaySong = &FMUSIC_PlaySong;
	instance->FMUSIC_StopSong = &FMUSIC_StopSong;
	instance->FMUSIC_StopAllSongs = &FMUSIC_StopAllSongs;
	instance->FMUSIC_SetZxxCallback = &FMUSIC_SetZxxCallback;
	instance->FMUSIC_SetRowCallback = &FMUSIC_SetRowCallback;
	instance->FMUSIC_SetOrderCallback = &FMUSIC_SetOrderCallback;
	instance->FMUSIC_SetInstCallback = &FMUSIC_SetInstCallback;
	instance->FMUSIC_SetSample = &FMUSIC_SetSample;
	instance->FMUSIC_SetUserData = &FMUSIC_SetUserData;
	instance->FMUSIC_OptimizeChannels = &FMUSIC_OptimizeChannels;
	instance->FMUSIC_SetReverb = &FMUSIC_SetReverb;
	instance->FMUSIC_SetLooping = &FMUSIC_SetLooping;
	instance->FMUSIC_SetOrder = &FMUSIC_SetOrder;
	instance->FMUSIC_SetPaused = &FMUSIC_SetPaused;
	instance->FMUSIC_SetMasterVolume = &FMUSIC_SetMasterVolume;
	instance->FMUSIC_SetMasterSpeed = &FMUSIC_SetMasterSpeed;
	instance->FMUSIC_SetPanSeperation = &FMUSIC_SetPanSeperation;
	instance->FMUSIC_GetName = &FMUSIC_GetName;
	instance->FMUSIC_GetType = &FMUSIC_GetType;
	instance->FMUSIC_GetNumOrders = &FMUSIC_GetNumOrders;
	instance->FMUSIC_GetNumPatterns = &FMUSIC_GetNumPatterns;
	instance->FMUSIC_GetNumInstruments = &FMUSIC_GetNumInstruments;
	instance->FMUSIC_GetNumSamples = &FMUSIC_GetNumSamples;
	instance->FMUSIC_GetNumChannels = &FMUSIC_GetNumChannels;
	instance->FMUSIC_GetSample = &FMUSIC_GetSample;
	instance->FMUSIC_GetPatternLength = &FMUSIC_GetPatternLength;
	instance->FMUSIC_IsFinished = &FMUSIC_IsFinished;
	instance->FMUSIC_IsPlaying = &FMUSIC_IsPlaying;
	instance->FMUSIC_GetMasterVolume = &FMUSIC_GetMasterVolume;
	instance->FMUSIC_GetGlobalVolume = &FMUSIC_GetGlobalVolume;
	instance->FMUSIC_GetOrder = &FMUSIC_GetOrder;
	instance->FMUSIC_GetPattern = &FMUSIC_GetPattern;
	instance->FMUSIC_GetSpeed = &FMUSIC_GetSpeed;
	instance->FMUSIC_GetBPM = &FMUSIC_GetBPM;
	instance->FMUSIC_GetRow = &FMUSIC_GetRow;
	instance->FMUSIC_GetPaused = &FMUSIC_GetPaused;
	instance->FMUSIC_GetTime = &FMUSIC_GetTime;
	instance->FMUSIC_GetRealChannel = &FMUSIC_GetRealChannel;
	instance->FMUSIC_GetUserData = &FMUSIC_GetUserData;
    }		
    return instance;
}

static void FMOD_FreeInstance(FMOD_INSTANCE *instance) {
    if (instance)     {
        free(instance);
	instance = NULL;
    }
}
#endif
