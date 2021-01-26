// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __IRR_MATH_H_INCLUDED__
#define __IRR_MATH_H_INCLUDED__

#include "irrTypes.h"

namespace irr
{
namespace core
{
	//! Rounding error constant often used when comparing f32 values.
	const f32 ROUNDING_ERROR	= 0.0001f;

	//! Constant for PI.
	const f32 PI				= 3.14159f;

	//! Constant for converting bettween degrees and radiants.
	const f64 GRAD_PI		= 180.0 / 3.1415926535897932384626433832795;

	//! Constant for converting bettween degrees and radiants.
	const f64 GRAD_PI2		= 3.1415926535897932384626433832795 / 180.0;

	//! returns minimum of two values. Own implementation to get rid of the STL.
	template<class T>
	inline const T min_(const T a, const T b)
	{
		return a < b ? a : b;
	}
	
	//! returns minimum of two values. Own implementation to get rid of the STL
	template<class T>
	inline T max_(const T a, const T b)
	{
		return a < b ? b : a;
	}

	//! returns if a float equals the other one, taking floating 
	//! point rounding errors into account
	inline bool equals(f32 a, f32 b)
	{
		return (a + ROUNDING_ERROR > b) && (a - ROUNDING_ERROR < b);
	}

} // end namespace core
}// end namespace irr

#endif

