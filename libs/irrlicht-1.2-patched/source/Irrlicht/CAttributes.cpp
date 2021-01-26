// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CAttributes.h"
#include "CAttributeImpl.h"
#include "ITexture.h"
#include "IXMLWriter.h"
#include "IVideoDriver.h"

namespace irr
{
namespace io
{

char CAttributes::tmpBuffer[1024];

CAttributes::CAttributes(video::IVideoDriver* driver)
: Driver(driver)
{
	if (Driver)
		Driver->grab();
}

CAttributes::~CAttributes()
{
	clear();

	if (Driver)
		Driver->drop();
}


//! Removes all attributes
void CAttributes::clear()
{
	for (int i=0; i<(int)Attributes.size(); ++i)
		Attributes[i]->drop();

	Attributes.clear();
}


//! Sets a string attribute. 
//! \param attributeName: Name for the attribute
//! \param value: Value for the attribute. Set this to 0 to delete the attribute
void CAttributes::setAttribute(const c8* attributeName, const c8* value)
{
	for (int i=0; i<(int)Attributes.size(); ++i)
		if (Attributes[i]->Name == attributeName)
		{
			if (!value)
			{
				Attributes[i]->drop();
				Attributes.erase(i);
			}
			else
				Attributes[i]->setString(value);

			return;
		}

	if (value)
	{
		Attributes.push_back(new CStringAttribute(attributeName, value));
	}
}

//! Gets a string attribute.
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setStringAttribute() 
//! or 0 if attribute is not set.
core::stringc CAttributes::getAttributeAsString(const c8* attributeName)
{
	core::stringc str;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
	{
		att->getString(tmpBuffer);
		str = tmpBuffer;
	}

	return str;
}

//! Gets a string attribute.
//! \param attributeName: Name of the attribute to get.
//! \param target: Buffer where the string is copied to.
void CAttributes::getAttributeAsString(const c8* attributeName, char* target)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->getString(target);
	else
		target[0] = 0;
}

//! Returns string attribute value by index. 
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
core::stringc CAttributes::getAttributeAsString(s32 index)
{
	core::stringc str;

	if (index >= 0 && index < (int)Attributes.size())
	{
		Attributes[index]->getString(tmpBuffer);
		str = tmpBuffer;
	}

	return str;
}


//! Returns attribute index from name, -1 if not found
s32 CAttributes::findAttribute(const c8* attributeName)
{
	for (int i=0; i<(int)Attributes.size(); ++i)
		if (Attributes[i]->Name == attributeName)
			return i;

	return -1;
}


IAttribute* CAttributes::getAttributeP(const c8* attributeName)
{
	for (int i=0; i<(int)Attributes.size(); ++i)
		if (Attributes[i]->Name == attributeName)
			return Attributes[i];

	return 0;
}


//! Sets a attribute as boolean value
void CAttributes::setAttribute(const c8* attributeName, bool value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setBool(value);
	else
	{
		Attributes.push_back(new CBoolAttribute(attributeName, value));
	}
}

//! Gets a attribute as boolean value
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute() as bool
//! or 0 if attribute is not set.
bool CAttributes::getAttributeAsBool(const c8* attributeName)
{
	bool ret = false;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getBool();

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return ret;
}

//! Sets a attribute as integer value
void CAttributes::setAttribute(const c8* attributeName, s32 value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setInt(value);
	else
	{
		Attributes.push_back(new CIntAttribute(attributeName, value));
	}
}

//! Gets a attribute as integer value
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute() as integer
//! or 0 if attribute is not set.
s32 CAttributes::getAttributeAsInt(const c8* attributeName)
{
	s32 ret = 0;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getInt();

	return ret;
}

//! Sets a attribute as float value
void CAttributes::setAttribute(const c8* attributeName, f32 value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setFloat(value);
	else
	{
		Attributes.push_back(new CFloatAttribute(attributeName, value));
	}
}

//! Gets a attribute as integer value
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute() as float value
//! or 0 if attribute is not set.
f32 CAttributes::getAttributeAsFloat(const c8* attributeName)
{
	f32 ret = 0;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getFloat();

	return ret;
}

//! Sets a attribute as color
void CAttributes::setAttribute(const c8* attributeName, video::SColor value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setColor(value);
	else
	{
		Attributes.push_back(new CColorAttribute(attributeName, value));
	}
}

//! Gets an attribute as color
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute()
video::SColor CAttributes::getAttributeAsColor(const c8* attributeName)
{
	video::SColor ret(0);

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getColor();

	return ret;
}

//! Sets a attribute as floating point color
void CAttributes::setAttribute(const c8* attributeName, video::SColorf value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setColor(value);
	else
	{
		Attributes.push_back(new CColorfAttribute(attributeName, value));
	}
}

//! Gets an attribute as floating point color
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute()
video::SColorf CAttributes::getAttributeAsColorf(const c8* attributeName)
{
	video::SColorf ret;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getColorf();

	return ret;
}

//! Sets a attribute as 2d position
void CAttributes::setAttribute(const c8* attributeName, core::position2df value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setPosition(value);
	else
	{
		Attributes.push_back(new CPosition2DAttribute(attributeName, value));
	}
}

//! Gets an attribute as 2d position
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute()
core::position2df CAttributes::getAttributeAsPosition2d(const c8* attributeName)
{
	core::position2df ret;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getPosition();

	return ret;
}

//! Sets a attribute as vector
void CAttributes::setAttribute(const c8* attributeName, core::vector3df value)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setVector(value);
	else
	{
		Attributes.push_back(new CVector3DAttribute(attributeName, value));
	}
}

//! Gets an attribute as vector
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute()
core::vector3df CAttributes::getAttributeAsVector3d(const c8* attributeName)
{
	core::vector3df ret;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getVector();

	return ret;
}

//! Sets an attribute as binary data
void CAttributes::setAttribute(const c8* attributeName, void* data, s32 dataSizeInBytes )
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setBinary(data, dataSizeInBytes);
	else
	{
		Attributes.push_back(new CBinaryAttribute(attributeName, data, dataSizeInBytes));
	}
}

//! Gets an attribute as binary data
//! \param attributeName: Name of the attribute to get.
void CAttributes::getAttributeAsBinaryData(const c8* attributeName, void* outData, s32 maxSizeInBytes)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->getBinary(outData, maxSizeInBytes);
}

//! Sets an attribute as enumeration
void CAttributes::setAttribute(const c8* attributeName, const char* enumValue, const char* const* enumerationLiterals)
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setEnum(enumValue, enumerationLiterals);
	else
	{
		Attributes.push_back(new CEnumAttribute(attributeName, enumValue, enumerationLiterals));
	}
}

//! Gets an attribute as enumeration
//! \param attributeName: Name of the attribute to get.
//! \return Returns value of the attribute previously set by setAttribute()
const char* CAttributes::getAttributeAsEnumeration(const c8* attributeName)
{
	const char* ret = 0;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getEnum();

	return ret;
}

//! Gets an attribute as enumeration
s32 CAttributes::getAttributeAsEnumeration(const c8* attributeName, const char* const* enumerationLiteralsToUse)
{
	s32 ret = -1;

	IAttribute* att = getAttributeP(attributeName);

	if (enumerationLiteralsToUse && att)
	{
		const char* value = att->getEnum();
		if (value)
		{
			for (s32 i=0; enumerationLiteralsToUse[i]; ++i) 
				if (!strcmp(value, enumerationLiteralsToUse[i]))
					return i;
		}
	}

	return ret;
}

//! Gets the list of enumeration literals of an enumeration attribute 
//! \param attributeName: Name of the attribute to get.
void CAttributes::getAttributeEnumerationLiteralsOfEnumeration(const c8* attributeName, core::array<core::stringc>& outLiterals)
{
	IAttribute* att = getAttributeP(attributeName);

	if (att && att->getType() == EAT_ENUM)
		outLiterals = ((CEnumAttribute*)att)->EnumLiterals;
}

//! Sets an attribute as texture reference
void CAttributes::setAttribute(const c8* attributeName, video::ITexture* value )
{
	IAttribute* att = getAttributeP(attributeName);
	if (att)
		att->setTexture(value);
	else
	{
		Attributes.push_back(new CTextureAttribute(attributeName, value, Driver));
	}
}


//! Gets an attribute as texture reference
//! \param attributeName: Name of the attribute to get.
video::ITexture* CAttributes::getAttributeAsTexture(const c8* attributeName)
{
	video::ITexture* tex = 0;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		tex = att->getTexture();

	return tex;
}

//! Gets an attribute as texture reference
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
video::ITexture* CAttributes::getAttributeAsTexture(s32 index)
{
	video::ITexture* tex = 0;

	if (index >= 0 && index < (s32)Attributes.size())
		tex = Attributes[index]->getTexture();

	return tex;
}


//! Returns amount of string attributes set in this scene manager.
s32 CAttributes::getAttributeCount()
{
	return Attributes.size();
}

//! Returns string attribute name by index. 
//! \param index: Index value, must be between 0 and getStringAttributeCount()-1.
const c8* CAttributes::getAttributeName(s32 index)
{
	if (index < 0 || index >= (int)Attributes.size())
		return 0;

	return Attributes[index]->Name.c_str();
}

//! Returns the type of an attribute
E_ATTRIBUTE_TYPE CAttributes::getAttributeType(const c8* attributeName)
{
	E_ATTRIBUTE_TYPE ret = EAT_UNKNOWN;

	IAttribute* att = getAttributeP(attributeName);
	if (att)
		ret = att->getType();

	return ret;
}

//! Returns attribute type by index. 
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
E_ATTRIBUTE_TYPE CAttributes::getAttributeType(s32 index)
{
	if (index < 0 || index >= (int)Attributes.size())
		return EAT_UNKNOWN;

	return Attributes[index]->getType();
}

//! Gets an attribute as boolean value
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
bool CAttributes::getAttributeAsBool(s32 index)
{
	bool ret = false;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getBool();

	_IRR_IMPLEMENT_MANAGED_MARSHALLING_BUGFIX;
	return ret;
}

//! Gets an attribute as integer value
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
s32 CAttributes::getAttributeAsInt(s32 index)
{
	s32 ret = 0;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getInt();

	return ret;
}

//! Gets an attribute as float value
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
f32 CAttributes::getAttributeAsFloat(s32 index)
{
	f32 ret = 0;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getFloat();

	return ret;
}

//! Gets an attribute as color
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
video::SColor CAttributes::getAttributeAsColor(s32 index)
{
	video::SColor ret;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getColor();

	return ret;
}

//! Gets an attribute as floating point color
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
video::SColorf CAttributes::getAttributeAsColorf(s32 index)
{
	video::SColorf ret;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getColorf();

	return ret;
}

//! Gets an attribute as 3d vector
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
core::vector3df CAttributes::getAttributeAsVector3d(s32 index)
{
	core::vector3df ret;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getVector();

	return ret;
}

//! Gets an attribute as 3d vector
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
core::position2df CAttributes::getAttributeAsPosition2d(s32 index)
{
	core::position2df ret;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getPosition();

	return ret;
}


//! Gets an attribute as binary data
///! \param index: Index value, must be between 0 and getAttributeCount()-1.
void CAttributes::getAttributeAsBinaryData(s32 index, void* outData, s32 maxSizeInBytes)
{
	if (index >= 0 && index < (s32)Attributes.size())
		Attributes[index]->getBinary(outData, maxSizeInBytes);
}

//! Gets an attribute as enumeration
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
const char* CAttributes::getAttributeAsEnumeration(s32 index)
{
	const char* ret = 0;

	if (index >= 0 && index < (s32)Attributes.size())
		ret = Attributes[index]->getEnum();

	return ret;
}

//! Gets an attribute as enumeration
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
s32 CAttributes::getAttributeAsEnumeration(s32 index, const char* const* enumerationLiteralsToUse)
{
	if (index >= 0 && index < (s32)Attributes.size())
	{
		IAttribute* att = Attributes[index];

		if (enumerationLiteralsToUse && att)
		{
			const char* value = att->getEnum();
			if (value)
			{
				for (s32 i=0; enumerationLiteralsToUse[i]; ++i) 
					if (!strcmp(value, enumerationLiteralsToUse[i]))
						return i;
			}
		}
	}

	return -1;
}

//! Gets the list of enumeration literals of an enumeration attribute 
//! \param index: Index value, must be between 0 and getAttributeCount()-1.
void CAttributes::getAttributeEnumerationLiteralsOfEnumeration(s32 index, core::array<core::stringc>& outLiterals)
{
	if (index >= 0 && index < (s32)Attributes.size() && Attributes[index]->getType() == EAT_ENUM)
		outLiterals = ((CEnumAttribute*)Attributes[index])->EnumLiterals;
}


//! Adds an attribute as integer
void CAttributes::addInt(const c8* attributeName, s32 value)
{
	Attributes.push_back(new CIntAttribute(attributeName, value));
}

//! Adds an attribute as float
void CAttributes::addFloat(const c8* attributeName, f32 value)
{
	Attributes.push_back(new CFloatAttribute(attributeName, value));
}

//! Adds an attribute as string
void CAttributes::addString(const c8* attributeName, const char* value)
{
	Attributes.push_back(new CStringAttribute(attributeName, value));
}

//! Adds an attribute as bool
void CAttributes::addBool(const c8* attributeName, bool value)
{
	Attributes.push_back(new CBoolAttribute(attributeName, value));
}

//! Adds an attribute as enum
void CAttributes::addEnum(const c8* attributeName, const char* enumValue, const char* const* enumerationLiterals)
{
	Attributes.push_back(new CEnumAttribute(attributeName, enumValue, enumerationLiterals));
}

//! Adds an attribute as enum
void CAttributes::addEnum(const c8* attributeName, s32 enumValue, const char* const* enumerationLiterals)
{
	addEnum(attributeName, "", enumerationLiterals);
	Attributes.getLast()->setInt(enumValue);
}

//! Adds an attribute as color
void CAttributes::addColor(const c8* attributeName, video::SColor value)
{
	Attributes.push_back(new CColorAttribute(attributeName, value));
}

//! Adds an attribute as floating point color
void CAttributes::addColorf(const c8* attributeName, video::SColorf value)
{
	Attributes.push_back(new CColorfAttribute(attributeName, value));
}

//! Adds an attribute as 3d vector
void CAttributes::addVector3d(const c8* attributeName, core::vector3df value)
{
	Attributes.push_back(new CVector3DAttribute(attributeName, value));
}

//! Adds an attribute as 2d position
void CAttributes::addPosition2d(const c8* attributeName, core::position2df value)
{
	Attributes.push_back(new CPosition2DAttribute(attributeName, value));
}

//! Adds an attribute as binary data
void CAttributes::addBinary(const c8* attributeName, void* data, s32 dataSizeInBytes)
{
	Attributes.push_back(new CBinaryAttribute(attributeName, data, dataSizeInBytes));
}

//! Adds an attribute as texture reference
void CAttributes::addTexture(const c8* attributeName, video::ITexture* texture)
{
	Attributes.push_back(new CTextureAttribute(attributeName, texture, Driver));
}

//! Returns if an attribute with a name exists
bool CAttributes::existsAttribute(const c8* attributeName)
{
	return getAttributeP(attributeName) != 0;  
}

//! Sets an attribute value as string. 
//! \param attributeName: Name for the attribute
void CAttributes::setAttribute(s32 index, const c8* value)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setString(value);
}

//! Sets an attribute as boolean value
void CAttributes::setAttribute(s32 index, bool value)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setBool(value);
}

//! Sets an attribute as integer value
void CAttributes::setAttribute(s32 index, s32 value)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setInt(value);
}

//! Sets a attribute as float value
void CAttributes::setAttribute(s32 index, f32 value)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setFloat(value);
}

//! Sets a attribute as color
void CAttributes::setAttribute(s32 index, video::SColor color)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setColor(color);
}

//! Sets a attribute as floating point color
void CAttributes::setAttribute(s32 index, video::SColorf color)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setColor(color);
}

//! Sets a attribute as vector
void CAttributes::setAttribute(s32 index, core::vector3df v)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setVector(v);
}

//! Sets an attribute as binary data
void CAttributes::setAttribute(s32 index, void* data, s32 dataSizeInBytes )
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setBinary(data, dataSizeInBytes);
}

//! Sets an attribute as enumeration
void CAttributes::setAttribute(s32 index, const char* enumValue, const char* const* enumerationLiterals)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setEnum(enumValue, enumerationLiterals);
}


//! Sets an attribute as texture reference
void CAttributes::setAttribute(s32 index, video::ITexture* texture)
{
	if (index >= 0 && index < (s32)Attributes.size() )
		Attributes[index]->setTexture(texture);
}


//! Reads attributes from a xml file.
//! \param readCurrentElementOnly: If set to true, reading only works if current element has the name 'attributes'.
//! IF set to false, the first appearing list attributes are read.
bool CAttributes::read(irr::io::IXMLReader* reader, bool readCurrentElementOnly)
{
	if (!reader)
		return false;

	clear();

	const core::stringw elementName = L"attributes";

	if (readCurrentElementOnly)
	{
		if (elementName != reader->getNodeName())
			return false;
	}

	while(reader->read())
	{
		switch(reader->getNodeType())
		{
		case io::EXN_ELEMENT:
			readAttributeFromXML(reader);
			break;
		case io::EXN_ELEMENT_END:
			if (elementName == reader->getNodeName())
				return true;
			break;
		}
	}

	return true;
}


void CAttributes::readAttributeFromXML(io::IXMLReader* reader)
{
	core::stringw element = reader->getNodeName();
	core::stringc name = reader->getAttributeValue(L"name");

	if (element == L"enum")
	{
		addEnum(name.c_str(), 0, 0);
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"color")
	{
		addColor(name.c_str(), video::SColor());
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"colorf")
	{
		addColorf(name.c_str(), video::SColorf());
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"float")
	{
		addFloat(name.c_str(), 0);
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"int")
	{
		addInt(name.c_str(), 0);
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"string")
	{
		addString(name.c_str(), "");
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"texture")
	{
		addTexture(name.c_str(), 0);
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"vector3d")
	{
		addVector3d(name.c_str(), core::vector3df());
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
	else
	if (element == L"bool")
	{
		addBool(name.c_str(), 0);
		Attributes.getLast()->setStringW(reader->getAttributeValue(L"value"));
	}
}

//! Write these attributes into a xml file
bool CAttributes::write(io::IXMLWriter* writer)
{
	if (!writer)
		return false;

	writer->writeElement(L"attributes", false);
	writer->writeLineBreak();

	for (s32 i=0; i<(s32)Attributes.size(); ++i)
	{
		Attributes[i]->getString(tmpBuffer);

		writer->writeElement(
			Attributes[i]->getTypeString(), true, 
			L"name", core::stringw(Attributes[i]->Name.c_str()).c_str(),
			L"value", core::stringw(tmpBuffer).c_str());

		writer->writeLineBreak();
	}

	writer->writeClosingTag(L"attributes");
	writer->writeLineBreak();

	return true;
}


} // end namespace io
} // end namespace irr

