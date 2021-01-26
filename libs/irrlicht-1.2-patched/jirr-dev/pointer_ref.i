%define POINTER_REF_TYPEMAP(CTYPE, JTYPE)
%typemap(jni)    CTYPE & "jlong"
%typemap(jtype)  CTYPE & "long"
%typemap(jstype) CTYPE & "JTYPE"
%typemap(javain) CTYPE & "$javainput"
%typemap(javaout) CTYPE & {
    return $jnicall;
  }  
%typemap(javadirectorin) CTYPE & "new JTYPE($jniinput, false)"
%typemap(javadirectorout) CTYPE & "JTYPE.getCPtr($javacall)"  
%typemap(javaout) CTYPE & {
    long cPtr = $jnicall;
    return (cPtr == 0) ? null : new JTYPE(cPtr, $owner);
  }
%typemap(javain) CTYPE & "JTYPE.getCPtr($javainput)"
%typemap(in) CTYPE & %{ $1 = ($1_ltype)&$input; %}
%typemap(out) CTYPE &
%{ *($1_ltype)&$result = *$1; %} 
%enddef
