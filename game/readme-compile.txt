*********************************************************
* Bolzplatz 2006 / Slam Soccer 2006 / Coup de Foot 2006 *
*              Compilation Information                  *
*********************************************************


bp2k6 is programmed in Java (platform independent), but also
uses native platform dependent libraries (e.g. Irrlicht).

If you want to change the game engine, only the Java code is
interesting for you.
You need only to recompile the native libraries if you
need new binaries (e.g. for 64bit) or modified the underlying
libraries.


Path
****

Copy this directory (where this file, readme-compile.txt, is)
to the main folder of the game (where game.jar is).


How to compile bp2k6 (Java)
***************************

Use Eclipse 3.1 or higher. You need also at least Java 1.5,
enable all Java 1.5 features! The project definition file
".project" is included in this archive. Make sure that
all .jar files in the lib-folder are placed
in the classpath.
A jar builder for "game.jar" (contains all classes) is
also included. Update the path for the .jar output
before you build it.

If you encounter any problems, please ask in the forum:
http://www.xenoage.com/bp2k6/forum/viewforum.php?f=4


How to compile the native libraries
***********************************


Irrlicht
--------

Windows:

First compile Irrlicht (libsrc/irrlicht-0.14-patched).
Use Bloodshed Dev-C++ (Irrlicht.dev) or the Makefile.
The name of the binary is "Irrlicht.dll". Copy it
to the main folder of the game (where game.jar is).

Linux: 

First compile Irrlicht (libsrc/irrlicht-0.14-patched)
with the makefile. A file named libIrrlicht.a should
be generated, which we will need now for Jirr: 


Jirr
----

General:

Install SWIG, the Simplified Wrapper and Interface
Generator. It is needed to create the Java binding of
Irrlicht. Make sure to install version 1.3.24, later
versions may not work (we still do not know, why).

Windows:

Use the makefile in libsrc/jirr-dev.
Rename the binary to "irrlicht_wrap.dll" and copy
it into the folder "lib/jirr".

Linux:

Use the makefile in libsrc/jirr-dev.
Rename the binary to "libirrlicht_wrap.so" and copy
it into the folder "lib/jirr".


LWJGL
-----

Windows/Linux:
Create the empty subdirectoy "bin".
Build LWJGL with Ant (build.xml):
First call "ant", then "ant jars", and ignore the warnings.
Copy lwjgl.jar and lwjgl_util.jar (subfolder libs) to
the directoy lib/lwjgl.

Windows:
Build the native library:
build.bat in platform_build/win32_ms_cmdline
Copy the resulting binary in lib/lwjgl and rename it
to "lwjgl.dll".

Linux:
Build the native library: Makefile in src/native/linux.
Copy the resulting binary in lib/lwjgl and rename it
to "liblwjgl.so".


Also read the readme files of the respective libraries, they
contain important information.


If you encounter any problems, please ask in the forum:
http://www.xenoage.com/bp2k6/forum/viewforum.php?f=4


---
Andi




