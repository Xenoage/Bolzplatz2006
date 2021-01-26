// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CAttributes.h"
#include <stdio.h>
#include "fast_atof.h"
#include <string.h>
#include "ITexture.h"
#include "IVideoDriver.h"

namespace irr
{
namespace io
{


// Attribute implemented for boolean values
class CBoolAttribute : public IAttribute
{
public:

	CBoolAttribute(const char* name, bool value)
	{
		Name = name;
		setBool(value);
	}

	bool BoolValue;

	virtual s32 getInt()
	{
		return BoolValue ? 1 : 0;
	}

	virtual f32 getFloat()
	{
		return BoolValue ? 1.0f : 0.0f;
	}

	virtual bool getBool()
	{
		return BoolValue;
	}

	virtual void getString(char* target)
	{
		strcpy(target, BoolValue ? "true" : "false");
	}

	virtual void setInt(s32 intValue)
	{
		BoolValue = (intValue != 0);
	}

	virtual void setFloat(f32 floatValue)
	{
		BoolValue = (floatValue != 0);
	}

	virtual void setBool(bool boolValue)
	{
		BoolValue = boolValue;
	}

	virtual void setString(const char* string)
	{
		BoolValue = strcmp(string, "true") == 0;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_BOOL;
	}

	virtual const wchar_t* getTypeString()
	{
		return L"bool";
	}
};

// Attribute implemented for enumeration literals
class CEnumAttribute : public IAttribute
{
public:

	CEnumAttribute(const char* name, const char* value, const char* const* literals)
	{
		Name = name;
		setEnum(value, literals);
	}

	virtual void setEnum(const char* enumValue, const char* const* enumerationLiterals)
	{
		int literalCount = 0;

		if (enumerationLiterals)
		{
			s32 i;
			for (i=0; enumerationLiterals[i]; ++i)
				++literalCount;

			EnumLiterals.reallocate(literalCount);
			for (i=0; enumerationLiterals[i]; ++i)
				EnumLiterals.push_back(enumerationLiterals[i]);
		}

		setString(enumValue);
	}

	virtual s32 getInt()
	{
		for (s32 i=0; EnumLiterals.size(); ++i)
			if (Value.equals_ignore_case(EnumLiterals[i]))
			{
				return i;
			}

		return -1;
	}

	virtual f32 getFloat()
	{
		return (f32)getInt();
	}

	virtual bool getBool()
	{
		return (getInt() != 0); // does not make a lot of sense, I know
	}

	virtual void getString(char* target)
	{
		strcpy(target, Value.c_str());
	}

	virtual void setInt(s32 intValue)
	{
		if (intValue>=0 && intValue<(s32)EnumLiterals.size())
			Value = EnumLiterals[intValue];
		else
			Value = "";
	}

	virtual void setFloat(f32 floatValue)
	{
		setInt((s32)floatValue);
	};

	virtual void setString(const char* text)
	{
		Value = text;
	}

	virtual const char* getEnum()
	{
		return Value.c_str();
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_ENUM;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"enum";
	}

	core::stringc Value;
	core::array<core::stringc> EnumLiterals;
};


// Attribute implemented for integers
class CIntAttribute : public IAttribute
{
public:

	CIntAttribute(const char* name, s32 value)
	{
		Name = name;
		setInt(value);
	}

	virtual s32 getInt()
	{
		return Value;
	}

	virtual f32 getFloat()
	{
		return (f32)Value;
	}

	virtual bool getBool()
	{
		return (Value != 0);
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%d", Value);
	}

	virtual void setInt(s32 intValue)
	{
		Value = intValue;
	}

	virtual void setFloat(f32 floatValue)
	{
		Value = (s32)floatValue;
	};

	virtual void setString(const char* text)
	{
		Value = atoi(text);
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_INT;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"int";
	}

	s32 Value;
};

// Attribute implemented for floats
class CFloatAttribute : public IAttribute
{
public:

	CFloatAttribute(const char* name, f32 value)
	{
		Name = name;
		setFloat(value);
	}

	virtual s32 getInt()
	{
		return (s32)Value;
	}

	virtual f32 getFloat()
	{
		return Value;
	}

	virtual bool getBool()
	{
		return (Value != 0);
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%f", Value);
	}

	virtual void setInt(s32 intValue)
	{
		Value = (f32)intValue;
	}

	virtual void setFloat(f32 floatValue)
	{
		Value = floatValue;
	};

	virtual void setString(const char* text)
	{
		Value = core::fast_atof(text);
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_FLOAT;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"float";
	}

	f32 Value;
};



// Attribute implemented for floating point colors
class CColorfAttribute : public IAttribute
{
public:

	CColorfAttribute(const char* name, video::SColorf value)
	{
		Name = name;
		Value = value;
	}

	virtual s32 getInt()
	{
		return Value.toSColor().color;
	}

	virtual f32 getFloat()
	{
		return (f32)Value.toSColor().color; // pretty senseless, again
	}

	virtual bool getBool()
	{
		return (getInt() != 0); // true = not black, if anybody cares
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%f, %f, %f, %f", Value.r, Value.g, Value.b, Value.a);
	}

	virtual void setInt(s32 intValue)
	{
		Value = video::SColorf(video::SColor(intValue));
	}

	virtual void setFloat(f32 floatValue)
	{
		Value = video::SColorf(video::SColor((s32)floatValue));
	}

	virtual void setString(const char* text)
	{
		// parse text

		const char* P = (const char*)text;
		Value.set(0,0,0,0);

		for ( int i=0; i<4 && *P; ++i )
		{
			while(*P && P[0]!='-' && ( P[0]==' ' || (P[0] < '0' || P[0] > '9') ) )
				++P;

			// set component

			if ( *P)
			{
				f32 c = 0;
				P = core::fast_atof_move(P, c);
				Value.setColorComponentValue(i, c);
			}
		}
	}

	virtual video::SColorf getColorf()
	{
		return Value;
	}

	virtual video::SColor getColor()
	{
		return Value.toSColor();
	}

	virtual void setColor(video::SColorf color)
	{
		Value = color;
	}

	virtual void setColor(video::SColor color)
	{
		Value = color;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_COLORF;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"colorf";
	}

	video::SColorf Value;
};



// Attribute implemented for colors
class CColorAttribute : public IAttribute
{
public:

	CColorAttribute(const char* name, video::SColor value)
	{
		Name = name;
		Value = value;
	}

	virtual s32 getInt()
	{
		return Value.color;
	}

	virtual f32 getFloat()
	{
		return (f32)Value.color; // pretty senseless, again
	}

	virtual bool getBool()
	{
		return (getInt() != 0); // true = not black, if anybody cares
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%08x", Value.color);
	}

	virtual void setInt(s32 intValue)
	{
		Value.color = intValue;
	}

	virtual void setFloat(f32 floatValue)
	{
		Value.color = (s32)floatValue;
	}

	virtual void setString(const char* text)
	{
		sscanf(text, "%08x", &Value.color);
	}

	virtual video::SColorf getColorf()
	{
		return video::SColorf(Value);
	}

	virtual video::SColor getColor()
	{
		return Value;
	}

	virtual void setColor(video::SColorf color)
	{
		Value = color.toSColor();
	}

	virtual void setColor(video::SColor color)
	{
		Value = color;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_COLOR;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"color";
	}

	video::SColor Value;
};



// Attribute implemented for strings
class CStringAttribute : public IAttribute
{
public:

	CStringAttribute(const char* name, const char* value)
	{
		Name = name;
		setString(value);
	}

	CStringAttribute(const char* name, void* binaryData, s32 lenghtInBytes)
	{
		Name = name;
		setBinary(binaryData, lenghtInBytes);
	}

	virtual s32 getInt()
	{
		return atoi(Value.c_str());
	}

	virtual f32 getFloat()
	{
		return core::fast_atof(Value.c_str());
	}

	virtual bool getBool()
	{
		return Value.equals_ignore_case("true");
	}

	virtual void getString(char* target)
	{
		strcpy(target, Value.c_str());
	}

	virtual void setInt(s32 intValue)
	{
		Value = core::stringc(intValue);
	}

	virtual void setFloat(f32 floatValue)
	{
		char tmp[32];
		sprintf(tmp, "%f", floatValue);
		Value = tmp;
	};

	virtual void setString(const char* text)
	{
		Value = text;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_STRING;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"string";
	}

	virtual void getBinary(void* outdata, s32 maxLenght)
	{
		s32 dataSize = maxLenght;
		c8* datac8 = (c8*)(outdata);
		s32 p = 0;
		const c8* dataString = Value.c_str();

		for (s32 i=0; i<dataSize; ++i)
			datac8[i] = 0;

		while(dataString[p] && p<dataSize)
		{
			s32 v = getByteFromHex((c8)dataString[p*2]) * 16;

			if (dataString[(p*2)+1])
				v += getByteFromHex((c8)dataString[(p*2)+1]);

			datac8[p] = v;
			++p;
		}
	};

	virtual void setBinary(void* data, s32 maxLenght)
	{
		s32 dataSize = maxLenght;
		c8* datac8 = (c8*)(data);
		char tmp[3];
		tmp[2] = 0;
		Value = "";

		for (s32 b=0; b<dataSize; ++b)
		{
			getHexStrFromByte(datac8[b], tmp);
			Value.append(tmp);
		}
	};

	core::stringc Value;

protected:

	static inline s32 getByteFromHex(c8 h)
	{
		if (h >= '0' && h <='9')
			return h-'0';

		if (h >= 'a' && h <='f')
			return h-'a' + 10;

		return 0;
	}

	static inline void getHexStrFromByte(c8 byte, c8* out)
	{
		s32 b = (byte & 0xf0) >> 4;

		for (s32 i=0; i<2; ++i)
		{
			if (b >=0 && b <= 9)
				out[i] = b+'0';
			if (b >=10 && b <= 15)
				out[i] = (b-10)+'a';

			b = byte & 0x0f;
		}
	}
};

// Attribute implemented for binary data
class CBinaryAttribute : public CStringAttribute
{
public:

	CBinaryAttribute(const char* name, void* binaryData, s32 lenghtInBytes)
		: CStringAttribute(name, binaryData, lenghtInBytes)
	{

	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_BINARY;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"binary";
	}
};


// Attribute implemented for 3d vectors
class CVector3DAttribute : public IAttribute
{
public:

	CVector3DAttribute(const char* name, core::vector3df value)
	{
		Name = name;
		Value = value;
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%f, %f, %f", Value.X, Value.Y, Value.Z);
	}

	virtual void setString(const char* text)
	{
		// parse text

		const char* P = (const char*)text;
		Value.set(0,0,0);

		for ( int i=0; i<3 && *P; ++i )
		{
			while(*P && P[0]!='-' && ( P[0]==' ' || (P[0] < '0' || P[0] > '9') ) )
				++P;

			// set component

			if (*P)
			{
				f32 c = 0;
				P = core::fast_atof_move(P, c);

				switch(i)
				{
				case 0: Value.X = c; break;
				case 1: Value.Y = c; break;
				case 2: Value.Z = c; break;
				}
			}
		}
	}

	virtual core::vector3df getVector()
	{
		return Value;
	}

	virtual void setVector(core::vector3df v)
	{
		Value = v;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_VECTOR3D;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"vector3d";
	}

	core::vector3df Value;
};

// Attribute implemented for 2d vectors
class CPosition2DAttribute : public IAttribute
{
public:

	CPosition2DAttribute(const char* name, core::position2df value)
	{
		Name = name;
		Value = value;
	}

	virtual void getString(char* target)
	{
		sprintf(target, "%f, %f", Value.X, Value.Y);
	}

	virtual void setString(const char* text)
	{
		// parse text

		const char* P = (const char*)text;
		Value.X = 0;
		Value.Y = 0;

		for ( int i=0; i<2 && *P; ++i )
		{
			while(*P && P[0]!='-' && ( P[0]==' ' || (P[0] < '0' || P[0] > '9') ) )
				++P;

			// set component

			if (*P)
			{
				f32 c = 0;
				P = core::fast_atof_move(P, c);

				switch(i)
				{
				case 0: Value.X = c; break;
				case 1: Value.Y = c; break;
				}
			}
		}
	}

	virtual core::vector3df getVector()
	{
		return core::vector3df(Value.X, Value.Y, 0);
	}

	virtual core::position2df getPosition()
	{
		return Value;
	}

	virtual void setVector(core::vector3df v)
	{
		Value.X = v.X;
		Value.Y = v.Y;
	}

	virtual void setPosition(core::position2df v)
	{
		Value = v;
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_POSITION2D;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"vector2d";
	}

	core::position2df Value;
};



// Attribute implemented for texture references
class CTextureAttribute : public IAttribute
{
public:

	CTextureAttribute(const char* name, video::ITexture* value, video::IVideoDriver* driver)
		: Value(0), Driver(driver)
	{
		if (Driver)
			Driver->grab();

		Name = name;
		setTexture(value);
	}

	~CTextureAttribute()
	{
		if (Driver)
			Driver->drop();

		if (Value)
			Value->drop();
	}

	virtual video::ITexture* getTexture()
	{
		return Value;
	}

	virtual bool getBool()
	{
		return (Value != 0);
	}

	virtual void getString(char* target)
	{
		if (Value)
			strcpy(target, Value->getName().c_str());
		else
			target[0] = 0x0;
	}

	virtual void setString(const char* text)
	{
		if (Driver)
		{
			if (text && *text)
				setTexture(Driver->getTexture(text));
			else
				setTexture(0);
		}
	}

	virtual void setTexture(video::ITexture* value)
	{
		if (Value)
			Value->drop();

		Value = value;

		if (Value)
			Value->grab();
	}

	virtual E_ATTRIBUTE_TYPE getType()
	{
		return EAT_TEXTURE;
	}


	virtual const wchar_t* getTypeString()
	{
		return L"texture";
	}

	video::ITexture* Value;
	video::IVideoDriver* Driver;
};


} // end namespace io
} // end namespace irr
