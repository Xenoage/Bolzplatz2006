// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CStringParameters.h"
#include <stdio.h>
#include "fast_atof.h"

namespace irr
{

//! Sets a string parameter. 
//! \param parameterName: Name for the parameter
//! \param value: Value for the parameter. Set this to 0 to delete the parameter
void CStringParameters::setParameter(const c8* parameterName, const c8* value)
{
	for (int i=0; i<(int)Parameters.size(); ++i)
		if (Parameters[i].Name == parameterName)
		{
			if (!value)
				Parameters.erase(i);
			else
				Parameters[i].Value = value;
			return;
		}

	if (value)
	{
		Parameters.push_back(SStringPair());
		Parameters.getLast().Name = parameterName;
		Parameters.getLast().Value = value;
	}
}

//! Gets a string parameter.
//! \param parameterName: Name of the parameter to get.
//! \return Returns value of the parameter previously set by setStringParameter() 
//! or 0 if parameter is not set.
const c8* CStringParameters::getParameter(const c8* parameterName)
{
	for (int i=0; i<(int)Parameters.size(); ++i)
		if (Parameters[i].Name == parameterName)
			return Parameters[i].Value.c_str();

	return 0;
}

CStringParameters::SStringPair* CStringParameters::getParameterP(const c8* parameterName)
{
	for (int i=0; i<(int)Parameters.size(); ++i)
		if (Parameters[i].Name == parameterName)
			return &Parameters[i];

	return 0;
}


//! Sets a parameter as boolean value
void CStringParameters::setParameter(const c8* parameterName, bool value)
{
	setParameter(parameterName, value ? "true" : "false");
}

//! Gets a parameter as boolean value
//! \param parameterName: Name of the parameter to get.
//! \return Returns value of the parameter previously set by setParameter() as bool
//! or 0 if parameter is not set.
bool CStringParameters::getParameterAsBool(const c8* parameterName)
{
	bool ret = false;

	SStringPair* p = getParameterP(parameterName);
	if (p)
		ret = (p->Value == "true");

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return ret;
}

//! Sets a parameter as integer value
void CStringParameters::setParameter(const c8* parameterName, s32 value)
{
	core::stringc s = value;
	setParameter(parameterName, s.c_str());
}

//! Gets a parameter as integer value
//! \param parameterName: Name of the parameter to get.
//! \return Returns value of the parameter previously set by setParameter() as integer
//! or 0 if parameter is not set.
s32 CStringParameters::getParameterAsInt(const c8* parameterName)
{
	return (s32)getParameterAsFloat(parameterName);
}

//! Sets a parameter as float value
void CStringParameters::setParameter(const c8* parameterName, f32 value)
{
	char tmp[32];
	sprintf(tmp, "%f", value);
	setParameter(parameterName, tmp);
}

//! Gets a parameter as integer value
//! \param parameterName: Name of the parameter to get.
//! \return Returns value of the parameter previously set by setParameter() as float value
//! or 0 if parameter is not set.
f32 CStringParameters::getParameterAsFloat(const c8* parameterName)
{
	SStringPair* p = getParameterP(parameterName);
	if (!p)
		return false;

	return core::fast_atof(p->Value.c_str());
}


//! Returns amount of string parameters set in this scene manager.
s32 CStringParameters::getParameterCount()
{
	return Parameters.size();
}

//! Returns string parameter name by index. 
//! \param index: Index value, must be between 0 and getStringParameterCount()-1.
const c8* CStringParameters::getParameterName(s32 index)
{
	if (index < 0 || index >= (int)Parameters.size())
		return 0;

	return Parameters[index].Name.c_str();
}

//! Returns string parameter value by index. 
//! \param index: Index value, must be between 0 and getStringParameterCount()-1.
const c8* CStringParameters::getParameterValue(s32 index)
{
	if (index < 0 || index >= (int)Parameters.size())
		return 0;

	return Parameters[index].Value.c_str();
}

} // end namespace

