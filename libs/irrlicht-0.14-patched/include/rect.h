// Copyright (C) 2002-2005 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#ifndef __IRR_RECT_H_INCLUDED__
#define __IRR_RECT_H_INCLUDED__

#include "irrTypes.h"
#include "dimension2d.h"
#include "position2d.h"

namespace irr
{
namespace core
{

	//!	Rectangle template.
	/** Mostly used by 2D GUI elements and for 2D drawing methods.
	    It has 2 positions instead of position and dimension
		and a fast method for collision detection with other rectangles and points.
	*/
	template <class T>
	class rect
	{
	public:

		rect()
			: UpperLeftCorner(0,0), LowerRightCorner(0,0) {};


		rect(T x, T y, T x2, T y2)
			: UpperLeftCorner(x,y), LowerRightCorner(x2,y2) {};


		rect(const position2d<T>& upperLeft, const position2d<T>& lowerRight)
			: UpperLeftCorner(upperLeft), LowerRightCorner(lowerRight) {};

		rect(const rect<T>& other)
			: UpperLeftCorner(other.UpperLeftCorner), LowerRightCorner(other.LowerRightCorner) {};

		rect(const position2d<T>& pos, const dimension2d<T>& size)
			: UpperLeftCorner(pos), LowerRightCorner(pos.X + size.Width, pos.Y + size.Height) {};


		rect<T> operator+(const position2d<T>& pos) const
		{
			rect<T> ret(*this);
			ret.UpperLeftCorner += pos;
			ret.LowerRightCorner += pos;
			return ret;
		}

		const rect<T>& operator+=(const position2d<T>& pos)
		{
			UpperLeftCorner += pos;
			LowerRightCorner += pos;
			return *this;
		}

        bool operator == (const rect<T>& other) const
		{
			return UpperLeftCorner == other.UpperLeftCorner && LowerRightCorner == other.LowerRightCorner;
		}


		bool operator != (const rect<T>& other) const
		{
			return UpperLeftCorner != other.UpperLeftCorner || LowerRightCorner != other.LowerRightCorner;
		}

		const rect<T>& operator = (const rect<T>& other)
		{
			UpperLeftCorner = other.UpperLeftCorner;
			LowerRightCorner = other.LowerRightCorner;
			return *this;
		}

		//! Returns if a 2d point is within this rectangle.
		//! \param pos: Position to test if it lies within this rectangle.
		//! \return Returns true if the position is within the rectangle, false if not.
		bool isPointInside(const position2d<T>& pos) const
		{
			return UpperLeftCorner.X <= pos.X && UpperLeftCorner.Y <= pos.Y &&
				LowerRightCorner.X >= pos.X && LowerRightCorner.Y >= pos.Y;
		}

		//! Returns if the rectangle collides with an other rectangle.
		bool isRectCollided(const rect<T>& other) const
		{
			return (LowerRightCorner.Y > other.UpperLeftCorner.Y && UpperLeftCorner.Y < other.LowerRightCorner.Y &&
					LowerRightCorner.X > other.UpperLeftCorner.X && UpperLeftCorner.X < other.LowerRightCorner.X);
		}

		//! Clips this rectangle with another one.
		void clipAgainst(const rect<T>& other) 
		{
			if (other.LowerRightCorner.X < LowerRightCorner.X)
				LowerRightCorner.X = other.LowerRightCorner.X;
			if (other.LowerRightCorner.Y < LowerRightCorner.Y)
				LowerRightCorner.Y = other.LowerRightCorner.Y;

			if (other.UpperLeftCorner.X > UpperLeftCorner.X)
				UpperLeftCorner.X = other.UpperLeftCorner.X;
			if (other.UpperLeftCorner.Y > UpperLeftCorner.Y)
				UpperLeftCorner.Y = other.UpperLeftCorner.Y;

			// correct possible invalid rect resulting from clipping
            if (UpperLeftCorner.Y > LowerRightCorner.Y)
                UpperLeftCorner.Y = LowerRightCorner.Y;
            if (UpperLeftCorner.X > LowerRightCorner.X)
                UpperLeftCorner.X = LowerRightCorner.X;
		}

		//! Returns width of rectangle.
		T getWidth() const
		{
			return LowerRightCorner.X - UpperLeftCorner.X;
		}

		//! Returns height of rectangle.
		T getHeight() const
		{
			return LowerRightCorner.Y - UpperLeftCorner.Y;
		}

		//! If the lower right corner of the rect is smaller then the upper left,
		//! the points are swapped.
		void repair()
		{
			if (LowerRightCorner.X < UpperLeftCorner.X)
			{
				s32 t = LowerRightCorner.X;
				LowerRightCorner.X = UpperLeftCorner.X;
				UpperLeftCorner.X = t;
			}

			if (LowerRightCorner.Y < UpperLeftCorner.Y)
			{
				s32 t = LowerRightCorner.Y;
				LowerRightCorner.Y = UpperLeftCorner.Y;
				UpperLeftCorner.Y = t;
			}
		}

		//! Returns if the rect is valid to draw. It could be invalid, if
		//! The UpperLeftCorner is lower or more right than the LowerRightCorner,
		//! or if the area described by the rect is 0.
		bool isValid() const
		{
			// thx to jox for a correction to this method

			s32 xd = LowerRightCorner.X - UpperLeftCorner.X;
			s32 yd = LowerRightCorner.Y - UpperLeftCorner.Y;

			return !(xd < 0 || yd < 0 || (xd == 0 && yd == 0));
		}

		//! Returns the center of the rectangle
		position2d<T> getCenter() const
		{
			return position2d<T>((UpperLeftCorner.X + LowerRightCorner.X) / 2,
				(UpperLeftCorner.Y + LowerRightCorner.Y) / 2);
		}
		
		//! Sets all 4 values of the rectangle (ANDI)
		void set(T x1, T y1, T x2, T y2)
		{
      UpperLeftCorner.X = x1;
      UpperLeftCorner.Y = y1;
      LowerRightCorner.X = x2;
      LowerRightCorner.Y = y2;
    }

		position2d<T> UpperLeftCorner;
		position2d<T> LowerRightCorner;
	};

} // end namespace core
} // end namespace irr


#endif

