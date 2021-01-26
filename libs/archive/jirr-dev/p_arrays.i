%include "arrays_java.i"

/* Arrays of primitive types use the following macro. The array typemaps use support functions. */
%define JAVA_P_ARRAYS_TYPEMAPS(NAME, CTYPE, JNITYPE, JNIARRAYTYPE, JTYPE, JFUNCNAME, JNIDESC, M_LENGTH)
%typemap(jni) CTYPE *NAME "JNIARRAYTYPE"
%typemap(jtype) CTYPE *NAME "JTYPE[]"
%typemap(jstype) CTYPE *NAME "JTYPE[]"
%typemap(javain) CTYPE *NAME "$javainput"
%typemap(javaout) CTYPE *NAME {
    return $jnicall;
  }
%typemap(in) CTYPE *NAME (JNITYPE *jarr)
%{  if (!SWIG_JavaArrayIn##JFUNCNAME(jenv, &jarr, &$1, $input)) return $null; %}
%typemap(in) CTYPE *NAME (JNITYPE *jarr)
%{  if ($input && JCALL1(GetArrayLength, jenv, $input) != $1_size) {
    SWIG_JavaThrowException(jenv, SWIG_JavaIndexOutOfBoundsException, "incorrect array size");
    return $null;
  }
  if (!SWIG_JavaArrayIn##JFUNCNAME(jenv, &jarr, &$1, $input)) return $null; %}
%typemap(argout) CTYPE *NAME
%{ SWIG_JavaArrayArgout##JFUNCNAME(jenv, jarr$argnum, $1, $input); %}
%typemap(directorin,descriptor=JNIDESC) CTYPE *
%{
  int sz = arg1->M_LENGTH;
  $input = SWIG_JavaArrayOut##JFUNCNAME(jenv, $1, sz);
%}
%typemap(out) CTYPE *NAME
%{
  int sz = arg1->M_LENGTH;
  $result = SWIG_JavaArrayOut##JFUNCNAME(jenv, $1, sz);
%}
%typemap(freearg) CTYPE *NAME
#if __cplusplus
%{ delete [] $1; %}
#else
%{ free($1); %}
#endif
%typemap(director_in) CTYPE *NAME
%{$result = SWIG_JavaArrayOut##JFUNCNAME(jenv, $1, $1_dim0); %}
%typemap(director_out) CTYPE *NAME (JNITYPE *jarr)
%{  if (!SWIG_JavaArrayIn##JFUNCNAME(jenv, &jarr, &$1, $input)) return $null; %}

%enddef

/* Arrays of proxy classes. The typemaps in this macro make it possible to treat an array of 
 * class/struct/unions as an array of Java classes. 
 * Use the following macro to use these typemaps for an array of class/struct/unions called name:
 * JAVA_P_ARRAYSOFCLASSES(name) */
%define JAVA_P_ARRAYSOFCLASSES(NAME, CTYPE, REALTYPE, JAVATYPE, M_LENGTH)

%typemap(jni) CTYPE *NAME "jlongArray"
%typemap(jtype) CTYPE *NAME "long[]"
%typemap(jstype) CTYPE *NAME "JAVATYPE[]"

%typemap(javain) CTYPE *NAME "JAVATYPE.cArrayUnwrap($javainput)"
%typemap(javaout) CTYPE * NAME {
    return JAVATYPE.cArrayWrap($jnicall, $owner);
  }

%typemap(in) CTYPE *NAME (jlong *jarr, jsize sz)
{
  int i;
  if (!$input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return $null;
  }
  sz = JCALL1(GetArrayLength, jenv, $input);
  jarr = JCALL2(GetLongArrayElements, jenv, $input, 0);
  if (!jarr) {
    return $null;
  }
#if __cplusplus
  $1 = new $*1_ltype[sz];
#else
  $1 = ($1_ltype) calloc(sz, sizeof($*1_ltype));
#endif
  if (!$1) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return $null;
  }
  for (i=0; i<sz; i++) {
    $1[i] = *($&1_ltype)&jarr[i];
  }
}

%typemap(out) CTYPE * NAME
{
  jlong *arr;
  int i, sz;
  sz = arg1->M_LENGTH;
  $result = JCALL1(NewLongArray, jenv, sz);
  if (!$result) {
    return $null;
  }
  arr = JCALL2(GetLongArrayElements, jenv, $result, 0);
  if (!arr) {
    return $null;
  }
  for (i=0; i<sz; i++) {
    arr[i] = 0;
    *($&1_ltype)&arr[i] = &((REALTYPE *)$1)[i];
  }
  JCALL3(ReleaseLongArrayElements, jenv, $result, arr, 0);
}

%typemap(freearg) CTYPE ** NAME
#if __cplusplus
%{ delete [] $1; %}
#else
%{ free($1); %}
#endif

/* Add some code to the proxy class of the array type for converting between type used in 
 * JNI class (long[]) and type used in proxy class ( P_ARRAYSOFCLASSES[] ) */
%typemap(javacode) REALTYPE %{
  protected static long[] cArrayUnwrap(JAVATYPE[] arrayWrapper) {
      long[] cArray = new long[arrayWrapper.length];
      for (int i=0; i<arrayWrapper.length; i++)
        cArray[i] = $javaclassname.getCPtr(arrayWrapper[i]);
      return cArray;
  }

  protected static JAVATYPE[] cArrayWrap(long[] cArray, boolean cMemoryOwn) {
    $javaclassname[] arrayWrapper = new $javaclassname[cArray.length];
    for (int i=0; i<cArray.length; i++)
      arrayWrapper[i] = new $javaclassname(cArray[i], cMemoryOwn);
    return arrayWrapper;
  }
%}

%enddef /* JAVA_P_ARRAYSOFCLASSES */

