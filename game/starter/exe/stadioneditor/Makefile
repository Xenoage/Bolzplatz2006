# Project: starter
# Makefile created by Dev-C++ 5.11

CPP      = i686-w64-mingw32-g++-win32
CC       = i686-w64-mingw32-gcc-win32
WINDRES  = i686-w64-mingw32-windres
LD       = i686-w64-mingw32-ld
OBJ      = main.o $(RES)
LINKOBJ  = main.o $(RES)
LIBS     = -L"/usr/i686-w64-mingw32/lib" -static-libgcc -mwindows -s
INCS     = -I"/usr/i686-w64-mingw32/include" 
CXXINCS  = -I"/usr/i686-w64-mingw32/include"
BIN      = stadioneditor.exe
CXXFLAGS = $(CXXINCS) -Wfatal-errors
CFLAGS   = $(INCS) -Wfatal-errors
RM       = rm -f

.PHONY: all all-before all-after clean clean-custom

all: all-before $(BIN) all-after

clean: clean-custom
	${RM} $(OBJ) $(BIN)

$(BIN): $(OBJ)
	$(CC) $(LINKOBJ) -o $(BIN) $(LIBS)

main.o: main.c
	$(CC) -c main.c -o main.o $(CFLAGS)

