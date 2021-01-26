%{
#include "wchar.h"

int wstrlen(const wchar_t *s)
{
	int cnt = 0;
	while(*s++)
		cnt++;
	return cnt;
}
%}

%typemap(jni) wchar_t *                                         "jstring"
%typemap(jtype) wchar_t *                                         "String"
%typemap(jstype) wchar_t *                                         "String"


/* char * - treat as String */
%typemap(in) wchar_t * { 
  $1 = 0;
  if ($input) {
    $1 = ($1_ltype)JCALL2(GetStringChars, jenv, $input, 0);
    if (!$1) return $null;
  }
}

%typemap(directorin, descriptor="Ljava/lang/String;") wchar_t * { 
  $input = 0;
  if ($1) {
    $input = JCALL1(NewString, jenv, $1);
    if (!$input) return $null;
  }
}
%typemap(freearg) wchar_t * { if ($1) JCALL2(ReleaseStringChars, jenv, $input, (const jchar *) $1); }
%typemap(out) wchar_t * { if($1) $result = JCALL2(NewString, jenv, (const jchar *) $1, wstrlen ($1)); }
%typemap(javadirectorin) wchar_t * "$jniinput"
%typemap(javadirectorout) wchar_t * "$javacall"

%typemap(throws) wchar_t * {
  SWIG_JavaThrowException(jenv, SWIG_JavaRuntimeException, $1);
  return $null;
}
%typemap(javain) wchar_t * "$javainput"
%typemap(javaout) wchar_t * {
    return $jnicall;
  }
