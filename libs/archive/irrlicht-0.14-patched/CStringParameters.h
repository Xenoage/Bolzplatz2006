// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __C_STRING_PARAMETERS_H_INCLUDED__
#define __C_STRING_PARAMETERS_H_INCLUDED__

#include "IStringParameters.h"
#include "irrString.h"
#include "irrArray.h"

namespace irr
{

//! Implementation of the IStringParameters interface
class CStringParameters : public IStringParameters
{
public:

	//! Sets a string parameter. 
	//! \param parameterName: Name for the parameter
	//! \param value: Value for the parameter. Set this to 0 to delete the parameter
	virtual void setParameter(const c8* parameterName, const c8* value);

	//! Gets a string parameter.
	//! \param parameterName: Name of the parameter to get.
	//! \return Returns value of the parameter previously set by setStringParameter() 
	//! or 0 if parameter is not set.
	virtual const c8* getParameter(const c8* parameterName);

	//! Sets a parameter as boolean value
	virtual void setParameter(const c8* parameterName, bool value);

	//! Gets a parameter as boolean value
	//! \param parameterName: Name of the parameter to get.
	//! \return Returns value of the parameter previously set by setParameter() as bool
	//! or 0 if parameter is not set.
	virtual bool getParameterAsBool(const c8* parameterName);

	//! Sets a parameter as integer value
	virtual void setParameter(const c8* parameterName, s32 value);

	//! Gets a parameter as integer value
	//! \param parameterName: Name of the parameter to get.
	//! \return Returns value of the parameter previously set by setParameter() as integer
	//! or 0 if parameter is not set.
	virtual s32 getParameterAsInt(const c8* parameterName);

	//! Sets a parameter as float value
	virtual void setParameter(const c8* parameterName, f32 value);

	//! Gets a parameter as integer value
	//! \param parameterName: Name of the parameter to get.
	//! \return Returns value of the parameter previously set by setParameter() as float value
	//! or 0 if parameter is not set.
	virtual f32 getParameterAsFloat(const c8* parameterName);

	//! Returns amount of string parameters set in this scene manager.
	virtual s32 getParameterCount();

	//! Returns string parameter name by index. 
	//! \param index: Index value, must be between 0 and getStringParameterCount()-1.
	virtual const c8* getParameterName(s32 index);

	//! Returns string parameter value by index. 
	//! \param index: Index value, must be between 0 and getStringParameterCount()-1.
	virtual const c8* getParameterValue(s32 index);	

protected:

	// A good idea would be to use a map. However, we don't have one because
	// we don't use STL and there is no map container currently in irrlicht. So
	// we use an array.

	struct SStringPair
	{
		core::stringc Name;
		core::stringc Value;
	};

	core::array<SStringPair> Parameters;

	SStringPair* getParameterP(const c8* parameterName);
};

} // end namespace 

#endif

