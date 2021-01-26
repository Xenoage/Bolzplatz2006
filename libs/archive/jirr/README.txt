Important: Please use swig 1324 and Makefile1324 if you do need callbacks. Newer versions (including 1.3.31) will produce incorrect code. here   resulting in occasional crashes.
The current and released builds still depend on Makefile1324.

But be aware: swig1324 also needs patching to make everything work as in the binaries! YOu have to update two files. Otherwise there will be errors on make time and some new features will disappear. These files can be found in patchSwig/1324

Lib\java\arrays_java.i
Lib\java\enums.swg

Simply take them and overwrite the old ones in your swih installation (you may backup your files if wanted). 
enums.swg will force you to add
%define SWIGWARN_TYPEMAP_THREAD_UNSAFE_MSG    "470:Thread/reentrant unsafe wrapping, consider returning by value instead." %enddef
to your own .i files when using enums. But that is the only negtive effect I am aware of. irrlicht.i already contains that line.

-------------------------------------------------------------------------------

Before running the Makefile, you should download and extract the iirlicht-engine.

Once this is done, correct the Makefile to point at the location where you extracted the source.

After that, you should be able to run the command

make

And everything should be build... If you have trouble building, 
please use the sourceforge.net forum for this project, it can be found at:

http://sourceforge.net/forum/?group_id=112018


Have Fun !
