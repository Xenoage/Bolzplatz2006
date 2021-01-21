@echo off


rem *** <ANDI> ***

D:
cd D:\Werkstatt\Programmierung\Repository\bp2k6\branches\lwjgl\platform_build\win32_ms_cmdline

set PLTSDKHOME=D:\Programme\Visual Studio .NET\Vc7\PlatformSDK
set CHOME=D:\Programme\Visual Studio .NET\Vc7
set ALHOME=D:\Download\Libraries\OpenAL\openal_040405\openal
set DXHOME=F:\DX8SDK
set OVSDK=D:\Download\Libraries\OggVorbis\OggVorbis-win32sdk-1.0.1\oggvorbis-win32sdk-1.0.1
set PATH=%PATH%;%CHOME%;D:\Programme\Visual Studio .NET\Vc7\bin;D:\Programme\Visual Studio .NET\Common7\IDE;D:\Download\Libraries\OpenAL\openal_040405\openal\include\AL

rem *** </ANDI> ***

if "%JAVA_HOME%" == "" goto errorjavahome
if "%PLTSDKHOME%" == "" goto errorpltsdkhome
if "%CHOME%" == "" goto errorchome
if "%ALHOME%" == "" goto erroralhome
if "%DXHOME%" == "" goto errordxhome
set COPTIONS=/Wp64 /I"%DXHOME%\include" /I"%CHOME%\include" /I"%PLTSDKHOME%\include" /I"%JAVA_HOME%\include" /I"%JAVA_HOME%\include\win32" /I"%ALHOME%\include\AL" /I"..\..\src\native\common" /I"%OVSDK%\include" /Ox /Ob2 /Oi /Ot /Oy /FD /EHsc /MT /Gy /W2 /c /nologo /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_USRDLL" /D "LWJGL_EXPORTS" /D "_WINDLL"
rem *************************************************
rem ** Build using free compiler requires jawt.dll **
rem ** and awt.dll in current dir. /delayload      **
rem ** fixes this, but is not available in free		 **
rem ** toolkit. Alternatively, remove AWT support  **
rem ** all together (only works on win32)          **
rem *************************************************
rem set LINKEROPTS=/link /LIBPATH:"%JAVA_HOME%\lib" /LIBPATH:"%ALHOME%\libs" /LIBPATH:"%DXHOME%\Lib" /LIBPATH:"%PLTSDKHOME%\Lib" /LIBPATH:"%CHOME%\Lib" /SUBSYSTEM:WINDOWS /OPT:REF /OPT:ICF /MACHINE:X86 /NOLOGO /DLL
rem set LIBS=dinput.lib dxguid.lib OpenGL32.Lib Version.lib user32.lib Gdi32.lib Advapi32.lib jawt.lib winmm.lib

rem *************************************************
rem ** Use the following lines below to build      **
rem ** using the commercial toolkit which allows   **
rem ** delayload option. �e. Release build.        **
rem *************************************************
set LINKEROPTS=/link /LIBPATH:"%JAVA_HOME%\lib" /LIBPATH:"%ALHOME%\libs" /LIBPATH:"%DXHOME%\Lib" /LIBPATH:"%PLTSDKHOME%\Lib" /LIBPATH:"%CHOME%\Lib" /LIBPATH:"%OVSDK%\lib" /SUBSYSTEM:WINDOWS /OPT:REF /OPT:ICF /MACHINE:X86 /NOLOGO /DLL /DELAYLOAD:jawt.dll
set LIBS=Kernel32.lib dinput.lib dxguid.lib OpenGL32.Lib Version.lib user32.lib Gdi32.lib Advapi32.lib jawt.lib delayimp.lib winmm.lib vorbisfile_static.lib ogg_static.lib

rem Andi:
for %%x in (..\..\src\native\common\ov\*.c) do cl %COPTIONS% %%x

for %%x in (..\..\src\native\win32\*.c) do cl %COPTIONS% %%x
for %%x in (..\..\src\native\common\*.c) do cl %COPTIONS% %%x
for %%x in (..\..\src\native\common\arb\*.c) do cl %COPTIONS% %%x
for %%x in (..\..\src\native\common\nv\*.c) do cl %COPTIONS% %%x
for %%x in (..\..\src\native\common\ext\*.c) do cl %COPTIONS% %%x
for %%x in (..\..\src\native\common\ati\*.c) do cl %COPTIONS% %%x


cl /LD /Felwjgl.dll *.obj %LINKEROPTS% %LIBS%

del *.obj *.exp *.lib

*** <ANDI>do not copy</ANDI> ***
rem copy lwjgl.dll ..\..\libs\

goto end

:errorjavahome
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------
echo JAVA_HOME not set.
echo.
goto error

:errorpltsdkhome
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------
echo PLTSDKHOME not set.
echo.
goto error

:errorchome
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------
echo CHOME not set.
echo.
goto error

:erroralhome
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------
echo ALHOME not set.
echo.
goto error

:errordxhome
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------
echo DXHOME not set.
echo.
goto error

:error
echo The following environment variables are required to be set.
echo JAVA_HOME  The root directory where Java is installed
echo PLTSDKHOME  "   "       "        "  Platform SDK is installed
echo CHOME       "   "       "        "  Visual C++ toolkit is installed
echo ALHOME      "   "       "        "  OpenAl is installed
echo DXHOME      "   "       "        "  DirectX is installed
echo -------------------------
echo --     ** ERROR **     --
echo -------------------------

:end
