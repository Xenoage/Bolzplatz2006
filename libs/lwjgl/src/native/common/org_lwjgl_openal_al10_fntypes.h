#ifndef _INCLUDED_ORG_LWJGL_OPENAL_AL10_FNTYPES_H_
#define _INCLUDED_ORG_LWJGL_OPENAL_AL10_FNTYPES_H_

typedef ALvoid (ALAPIENTRY *alDopplerVelocityPROC) (ALfloat value);
typedef ALvoid (ALAPIENTRY *alDopplerFactorPROC) (ALfloat value);
typedef ALvoid (ALAPIENTRY *alDistanceModelPROC) (ALenum value);
typedef ALvoid (ALAPIENTRY *alSourceUnqueueBuffersPROC) (ALuint source, ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alSourceQueueBuffersPROC) (ALuint source, ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alGetBufferfPROC) (ALuint buffer, ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetBufferiPROC) (ALuint buffer, ALenum pname, ALint* value);
typedef ALvoid (ALAPIENTRY *alBufferDataPROC) (ALuint buffer, ALenum format, ALvoid * data, ALsizei size, ALsizei freq);
typedef ALboolean (ALAPIENTRY *alIsBufferPROC) (ALuint buffer);
typedef ALvoid (ALAPIENTRY *alDeleteBuffersPROC) (ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alGenBuffersPROC) (ALsizei n, ALuint * buffers);
typedef ALvoid (ALAPIENTRY *alSourceRewindPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourceStopPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourcePausePROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourcePlayPROC) (ALuint source);
typedef ALvoid (ALAPIENTRY *alSourceRewindvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourceStopvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourcePausevPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alSourcePlayvPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alGetSourcefvPROC) (ALuint source, ALenum pname, ALfloat * floatdata);
typedef ALvoid (ALAPIENTRY *alGetSourcefPROC) (ALuint source, ALenum pname, ALfloat* value);
typedef ALvoid (ALAPIENTRY *alGetSourceiPROC) (ALuint source, ALenum pname, ALint* value);
typedef ALvoid (ALAPIENTRY *alSource3fPROC) (ALuint source, ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);
typedef ALvoid (ALAPIENTRY *alSourcefvPROC) (ALuint source, ALenum pname, ALfloat * value);
typedef ALvoid (ALAPIENTRY *alSourcefPROC) (ALuint source, ALenum pname, ALfloat value);
typedef ALvoid (ALAPIENTRY *alSourceiPROC) (ALuint source, ALenum pname, ALint value);
typedef ALboolean (ALAPIENTRY *alIsSourcePROC) (ALuint id);
typedef ALvoid (ALAPIENTRY *alDeleteSourcesPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alGenSourcesPROC) (ALsizei n, ALuint * sources);
typedef ALvoid (ALAPIENTRY *alGetListenerfvPROC) (ALenum pname, ALfloat * floatdata);
typedef ALfloat (ALAPIENTRY *alGetListenerfPROC) (ALenum pname);
typedef ALint (ALAPIENTRY *alGetListeneriPROC) (ALenum pname);
typedef ALvoid (ALAPIENTRY *alListener3fPROC) (ALenum pname, ALfloat v1, ALfloat v2, ALfloat v3);
typedef ALvoid (ALAPIENTRY *alListenerfvPROC) (ALenum pname, ALfloat * value);
typedef ALvoid (ALAPIENTRY *alListenerfPROC) (ALenum pname, ALfloat value);
typedef ALvoid (ALAPIENTRY *alListeneriPROC) (ALenum pname, ALint value);
typedef ALenum (ALAPIENTRY *alGetEnumValuePROC) (ALubyte * ename);
typedef ALboolean (ALAPIENTRY *alIsExtensionPresentPROC) (ALubyte * fname);
typedef ALenum (ALAPIENTRY *alGetErrorPROC) ();
typedef ALubyte * (ALAPIENTRY *alGetStringPROC) (ALenum pname);
typedef ALvoid (ALAPIENTRY *alGetFloatvPROC) (ALenum pname, ALfloat * data);
typedef ALvoid (ALAPIENTRY *alGetIntegervPROC) (ALenum pname, ALint * data);
typedef ALfloat (ALAPIENTRY *alGetFloatPROC) (ALenum pname);
typedef ALint (ALAPIENTRY *alGetIntegerPROC) (ALenum pname);
typedef ALboolean (ALAPIENTRY *alGetBooleanPROC) (ALenum pname);
typedef ALboolean (ALAPIENTRY *alIsEnabledPROC) (ALenum capability);
typedef ALvoid (ALAPIENTRY *alDisablePROC) (ALenum capability);
typedef ALvoid (ALAPIENTRY *alEnablePROC) (ALint capability);

#endif
