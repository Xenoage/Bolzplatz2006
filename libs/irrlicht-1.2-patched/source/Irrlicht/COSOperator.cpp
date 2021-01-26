// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "COSOperator.h"
#include "IrrCompileConfig.h"

#ifdef _IRR_WINDOWS_
#include <windows.h>
#else
#include <string.h>
#endif

#ifdef MACOSX
#include "OSXClipboard.h"
#endif


namespace irr
{


// constructor
COSOperator::COSOperator(const c8* osVersion)
{
	OperationSystem = osVersion;
}


//! destructor
COSOperator::~COSOperator()
{
}


//! returns the current operation system version as string.
const wchar_t* COSOperator::getOperationSystemVersion()
{
	return OperationSystem.c_str();
}


//! copies text to the clipboard
void COSOperator::copyToClipboard(const c8* text)
{
	if (strlen(text)==0)
		return;

// Windows version
#ifdef _IRR_WINDOWS_
	if (!OpenClipboard(0) || text == 0)
		return;

	EmptyClipboard();

	HGLOBAL clipbuffer;
	char * buffer;

	clipbuffer = GlobalAlloc(GMEM_DDESHARE, strlen(text)+1);
	buffer = (char*)GlobalLock(clipbuffer);

	strcpy(buffer, text);

	GlobalUnlock(clipbuffer);
	SetClipboardData(CF_TEXT, clipbuffer);
	CloseClipboard();
#endif

#ifdef MACOSX
	OSXCopyToClipboard(text);
#endif
}


//! gets text from the clipboard
//! \return Returns 0 if no string is in there.
c8* COSOperator::getTextFromClipboard()
{
#ifdef _IRR_WINDOWS_
	if (!OpenClipboard(NULL))
		return 0;
	
	char * buffer = 0;

	HANDLE hData = GetClipboardData( CF_TEXT );
	buffer = (char*)GlobalLock( hData );
	GlobalUnlock( hData );
	CloseClipboard();
	return buffer;
#else
#ifdef MACOSX
	return (OSXCopyFromClipboard());
#else
	return 0;
#endif
#endif
}



} // end namespace

