UNAME=$(shell uname)
OS=win32
ifeq ($(findstring MINGW,$(UNAME)),MINGW)
OS=win32
else
ifeq ($(findstring CYGWIN,$(UNAME)),CYGWIN)
OS=win32
else
ifeq ($(findstring Linux,$(UNAME)),Linux)
OS=linux
endif
endif
endif

CC=gcc
CXX=g++

JAVAC=$(JAVA_HOME)/bin/javac
JAVA=$(JAVA_HOME)/bin/java
JAVAH=$(JAVA_HOME)/bin/javah
JAVA_INCDIR=$(JAVA_HOME)/include
JAVA_LIBDIR=-L$(JAVA_HOME)/jre/lib/i386/client
JAVA_CFLAGS=-I$(JAVA_INCDIR) -I$(JAVA_INCDIR)/$(OS)
JAVA_LDFLAGS=$(JAVA_LIBDIR) -ljvm

IRRLICHT_HOME=../irrlicht-0.14-patched

ifeq ($(OS), linux)
IRRLICHT_LIBDIR=$(IRRLICHT_HOME)/lib/Linux
else
ifeq ($(OS), win32)
IRRLICHT_LIBDIR=$(IRRLICHT_HOME)/lib/Win32-gcc
else
IRRLICHT_LIBDIR=$(IRRLICHT_HOME)/libs
endif
endif

IRRLICHT_INCDIR=$(IRRLICHT_HOME)/include
#IRRLICHT_LDFLAGS=-L$(IRRLICHT_LIBDIR) -lIrrlicht -ljpeg -lz
IRRLICHT_LDFLAGS=-L$(IRRLICHT_LIBDIR)  -lIrrlicht

ifeq ($(OS), win32)
IRRLICHT_LDFLAGS+=-lgdi32 -lglu32 -lopengl32
else
IRRLICHT_LDFLAGS+=-L/usr/X11R6/lib -lX11 -lGL -lXxf86vm -lXext -lc -lGLU
endif
ifeq ($(OS), win32)
#IRRLICHT_CFLAGS=-I$(IRRLICHT_INCDIR) -mno-cygwin -shared 
IRRLICHT_CFLAGS=-I$(IRRLICHT_INCDIR) -mno-cygwin -Wl,--add-stdcall-alias -shared -L$(IRRLICHT_LIBDIR) -lIrrlicht
else
IRRLICHT_CFLAGS=-I$(IRRLICHT_INCDIR)
endif

#SWIG=swig -I/mingw/lib/swig1.3 -I/mingw/lib/swig1.3/java
#SWIG=swig -dump_tree 
SWIG=swig -v
#OAK MOD_SWIG
#SWIG=/home/emanuel/sources/apps/SWIG/swig

CXXFLAGS=$(JAVA_CFLAGS) $(IRRLICHT_CFLAGS) -I. -D__cplusplus

all: clean dirs jar wrapper

src/java/net/sf/jirr/Irrlicht.java: $(IRRLICHT_INCDIR)/irrlicht.h irrlicht.i
	-$(SWIG) -package net.sf.jirr -c++ -I$(IRRLICHT_INCDIR) -java -outdir src/java/net/sf/jirr -o src/native/irrlicht_wrap.cxx irrlicht.i
	patch -p1 <diff.txt

classes/net/sf/jirr/Irrlicht.class: src/java/net/sf/jirr/Irrlicht.java
	$(JAVAC) -sourcepath src/java -d classes $(shell find src/java -name "*.java")

jar: lib/irrlicht.jar

lib/irrlicht.jar: classes/net/sf/jirr/Irrlicht.class
	cd classes && jar -cvf ../lib/irrlicht.jar .
	
	
ifeq ($(OS), win32)
wrapper: lib/irrlicht_wrap.o
lib/irrlicht_wrap.o: src/native/irrlicht_wrap.cxx
	g++ -o lib/irrlicht_wrap.dll src/native/irrlicht_wrap.cxx $(CXXFLAGS) ${IRRLICHT_LDFLAGS}
else	
wrapper: lib/irrlicht_wrap.o libirrlicht_wrap.so
lib/irrlicht_wrap.o: src/native/irrlicht_wrap.cxx
	${CXX} -g -c -o lib/irrlicht_wrap.o src/native/irrlicht_wrap.cxx $(CXXFLAGS)
endif

libirrlicht_wrap.so:	lib/irrlicht_wrap.o
	${CXX} -g -shared lib/irrlicht_wrap.o -o libirrlicht_wrap.so ${IRRLICHT_LDFLAGS}
#lib/exirrlicht_wrap.o: extrafunctions.cxx
#	${CXX} -c -o lib/irrlicht_wrap.o src/native/irrlicht_wrap.cxx $(CXXFLAGS)

net/sf/jirr/tests/TestHelloWorld.class:
	$(JAVAC) -classpath ".:lib/irrlicht.jar" $(shell find net/sf/jirr/ -name "*.java")

tests: jar net/sf/jirr/tests/TestHelloWorld.class
	$(JAVA) -classpath ".:lib/irrlicht.jar" net/sf/jirr/tests/TestHelloWorld

dirs:
	-mkdir -p src/java/net/sf/jirr
	-mkdir -p src/native
	-mkdir -p classes
	-mkdir -p lib

clean:
	-rm -rf src/native/*
	-rm -rf src/java/net/sf/jirr/*
	-rm -rf classes/*
	-rm -rf lib/*
	-rm -f $(shell find net/sf/jirr -name "*.class")

