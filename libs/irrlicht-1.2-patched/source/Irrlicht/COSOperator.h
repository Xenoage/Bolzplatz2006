// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_OS_OPERATOR_H_INCLUDED__
#define __C_OS_OPERATOR_H_INCLUDED__

#include "IOSOperator.h"
#include "irrString.h"

namespace irr
{

//! The Operating system operator provides operation system specific methods and informations.
class COSOperator : public IOSOperator
{
public:

	// constructor
	COSOperator(const c8* osversion);

	//! destructor
	virtual ~COSOperator();

	//! returns the current operation system version as string.
	virtual const wchar_t* getOperationSystemVersion();

	//! copies text to the clipboard
	virtual void copyToClipboard(const c8* text);

	//! gets text from the clipboard
	//! \return Returns 0 if no string is in there.
	virtual c8* getTextFromClipboard();	

private:

	core::stringw OperationSystem;
};

} // end namespace

#endif

