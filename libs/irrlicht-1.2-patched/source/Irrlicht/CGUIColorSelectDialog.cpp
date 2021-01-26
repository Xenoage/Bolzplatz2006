// Copyright (C) 2002-2006 Nikolaus Gebhardt
// This file is part of the "Irrlicht Engine".
// For conditions of distribution and use, see copyright notice in irrlicht.h

#include "CGUIColorSelectDialog.h"
#include "IGUISkin.h"
#include "IGUIEnvironment.h"
#include "IVideoDriver.h"
#include "IGUIButton.h"
#include "IGUIStaticText.h"
#include "IGUIFont.h"
#include "GUIIcons.h"
#include "IFileList.h"
#include "os.h"
#include "SoftwareDriver2_helper.h"

namespace irr
{
namespace video
{
	//! Class representing a color in HSV format
	/**	The color values for hue, saturation, value
	are stored in a 32 bit floating point variable.
	*/
	class SColorHSL
	{
	public:
		void setfromRGB ( const SColor &color );
		void settoRGB ( SColor &color );

		f32 Hue;
		f32 Saturation;
		f32 Luminance;
	private:
		u32 toRGB1(f32 rm1, f32 rm2, f32 rh) const;
	};

	void SColorHSL::setfromRGB ( const SColor &color )
	{
	}

	void SColorHSL::settoRGB ( SColor &color )
	{
		if ( Saturation == 0.0) // grey
		{
			u8 c = (u8) ( Luminance * 255.0 );
			color.setRed ( c );
			color.setGreen ( c );
			color.setBlue ( c );
			return;
		}

		f32 rm1, rm2;
			
		if ( Luminance <= 0.5f )
		{
			rm2 = Luminance + Luminance * Saturation;  
		}
		else
		{
			rm2 = Luminance + Saturation - Luminance * Saturation;
		}

		rm1 = 2.0f * Luminance - rm2;   

		color.setRed ( toRGB1(rm1, rm2, Hue + (120.0f * core::DEGTORAD )) );
		color.setGreen ( toRGB1(rm1, rm2, Hue) );
		color.setBlue ( toRGB1(rm1, rm2, Hue - (120.0f * core::DEGTORAD) ) );
	}


	u32 SColorHSL::toRGB1(f32 rm1, f32 rm2, f32 rh) const
	{
		while ( rh > 2.f * core::PI )
			rh -= 2.f * core::PI;

		while ( rh < 0.f )
			rh += 2.f * core::PI;

		if      (rh <  60.0f * core::DEGTORAD ) rm1 = rm1 + (rm2 - rm1) * rh / (60.0f * core::DEGTORAD);
		else if (rh < 180.0f * core::DEGTORAD ) rm1 = rm2;
		else if (rh < 240.0f * core::DEGTORAD ) rm1 = rm1 + (rm2 - rm1) * ( ( 240.0f * core::DEGTORAD ) - rh) / (60.0f * core::DEGTORAD);
		                
		return (u32) (rm1 * 255.f);
	}
}
namespace gui
{

const s32 CSD_WIDTH = 350;
const s32 CSD_HEIGHT = 300;

struct sTemplate
{
	const wchar_t *pre;
	const wchar_t *init;
	const wchar_t *pos;
	int x, y;
	int range_down ,range_up;
};

static const sTemplate Template [] =
{
	{ L"A:", L"0", 0,20,165, 0, 255 },
	{ L"R:", L"0", 0,20,205, 0, 255 },
	{ L"G:", L"0", 0,20,230, 0, 255 },
	{ L"B:", L"0", 0,20,255, 0, 255 },
	{ L"H:", L"0", L"°",180,205, 0, 360 },
	{ L"S:", L"0", L"%",180,230, 0, 100 },
	{ L"L:", L"0", L"%",180,255, 0, 100 },
};

//! constructor
CGUIColorSelectDialog::CGUIColorSelectDialog( const wchar_t* title, IGUIEnvironment* environment, IGUIElement* parent, s32 id)
: IGUIColorSelectDialog(environment, parent, id,
 core::rect<s32>((parent->getAbsolutePosition().getWidth()-CSD_WIDTH)/2,
					(parent->getAbsolutePosition().getHeight()-CSD_HEIGHT)/2,	
					(parent->getAbsolutePosition().getWidth()-CSD_WIDTH)/2+CSD_WIDTH,
					(parent->getAbsolutePosition().getHeight()-CSD_HEIGHT)/2+CSD_HEIGHT)),	
  Dragging(false)
{
	#ifdef _DEBUG
	IGUIElement::setDebugName("CGUIColorSelectDialog");
	#endif

	Text = title;

	s32 buttonw = environment->getSkin()->getSize(EGDS_WINDOW_BUTTON_WIDTH);
	s32 posx = RelativeRect.getWidth() - buttonw - 4;

	CloseButton = Environment->addButton(core::rect<s32>(posx, 3, posx + buttonw, 3 + buttonw), this, -1, GUI_ICON_WINDOW_CLOSE);
	CloseButton->setOverrideFont(Environment->getBuiltInFont());
	CloseButton->grab();

	OKButton = Environment->addButton(core::rect<s32>(RelativeRect.getWidth()-80, 30, RelativeRect.getWidth()-10, 50), this, -1, L"OK");
	OKButton->grab();

	CancelButton = Environment->addButton(core::rect<s32>(RelativeRect.getWidth()-80, 55, RelativeRect.getWidth()-10, 75), this, -1, L"Cancel");
	CancelButton->grab();

	core::rect<s32> r;

	video::IVideoDriver* driver = Environment->getVideoDriver();
	ColorRing.Texture = driver->getTexture ( "#colorring" );
	if ( 0 == ColorRing.Texture )
	{
		buildColorRing ( core::dimension2d<s32> ( 128, 128  ), 1,  Environment->getSkin ()->getColor (EGDC_3D_SHADOW ).color );
	}

	r.UpperLeftCorner.X = 20;
	r.UpperLeftCorner.Y = 20;
	ColorRing.Control = Environment->addImage ( ColorRing.Texture, r.UpperLeftCorner, true, this );
	ColorRing.Control->grab();

	for ( u32 i = 0; i != sizeof (Template) / sizeof ( sTemplate ); ++i )
	{
		if ( Template[i].pre )
		{
			r.UpperLeftCorner.X = Template[i].x;
			r.UpperLeftCorner.Y = Template[i].y;
			r.LowerRightCorner.X = r.UpperLeftCorner.X + 15;
			r.LowerRightCorner.Y = r.UpperLeftCorner.Y + 20;
			Environment->addStaticText( Template[i].pre, r, false, false, this);
		}

		if ( Template[i].pos )
		{
			r.UpperLeftCorner.X = Template[i].x + 52;
			r.UpperLeftCorner.Y = Template[i].y;
			r.LowerRightCorner.X = r.UpperLeftCorner.X + 15;
			r.LowerRightCorner.Y = r.UpperLeftCorner.Y + 20;
			Environment->addStaticText( Template[i].pos, r, false, false, this);
		}

		SBatteryItem item;

		r.UpperLeftCorner.X = Template[i].x + 15;
		r.UpperLeftCorner.Y = Template[i].y;
		r.LowerRightCorner.X = r.UpperLeftCorner.X + 35;
		r.LowerRightCorner.Y = r.UpperLeftCorner.Y + 20;

		item.Edit = Environment->addEditBox( Template[i].init, r, true, this);
		item.Edit->grab();

		r.UpperLeftCorner.X = Template[i].x + 70;
		r.UpperLeftCorner.Y = Template[i].y + 4;
		r.LowerRightCorner.X = r.UpperLeftCorner.X + 60;
		r.LowerRightCorner.Y = r.UpperLeftCorner.Y + 12;

		item.Scrollbar = Environment->addScrollBar(true, r, this);
		item.Scrollbar->grab ();
		item.Scrollbar->setMax ( Template[i].range_up - Template[i].range_down );
		item.Scrollbar->setSmallStep ( 1 );

		Battery.push_back ( item );
	}


}



//! destructor
CGUIColorSelectDialog::~CGUIColorSelectDialog()
{
	if (CloseButton)
		CloseButton->drop();

	if (OKButton)
		OKButton->drop();

	if (CancelButton)
		CancelButton->drop();

	for ( u32 i = 0; i != Battery.size ();++i )
	{
		Battery[i].Edit->drop ();
		Battery[i].Scrollbar->drop ();
	}

	if ( ColorRing.Control )
	{
		ColorRing.Control->drop ();
	}

}

//! renders a antialiased, colored ring
void CGUIColorSelectDialog::buildColorRing( const core::dimension2d<s32> & dim, s32 supersample, const u32 borderColor )
{
	video::CImage *RawTexture;

	core::dimension2d<s32> d;
	d.Width = dim.Width * supersample;
	d.Height = dim.Height * supersample;

	RawTexture = new video::CImage ( irr::video::ECF_A8R8G8B8, d );

	RawTexture->fill ( 0x00808080 );


	u8 * data= (u8*) RawTexture->lock();
	const u32 pitch = RawTexture->getPitch ();

	s32 radiusOut = ( d.Width / 2 ) - 4;

	const s32 fullR2 = radiusOut * radiusOut;


	video::SColorHSL hsl;
	video::SColor rgb;

	rgb.color = 0xFF000000;
	hsl.Luminance = 0.5f;
	hsl.Saturation = 1.f;

	

	core::position2d<s32> p;
	u32 *dst;
	for ( p.Y = -radiusOut;  p.Y <= radiusOut;  p.Y += 1  )
	{
		s32 y2 = p.Y * p.Y;

		dst = (u32*) ( (u8* ) data + ( ( 4 + p.Y + radiusOut ) * pitch ) + (4 << 2 ) );

		for (	p.X = -radiusOut; p.X <= radiusOut; p.X += 1, dst += 1 )
		{
			s32 r2 = y2 + ( p.X * p.X );

			// test point in circle
			s32 testa = r2 - fullR2;

			if ( testa < 0 )
			{
				// dotproduct u ( x,y ) * v ( 1, 0 ) = cosinus(a)

				f32 r = (f32) sqrt ( (f32) r2 );

				// normalize, dotproduct = xnorm
				f32 t = (f32) 1.0 / r;
				f32 xn = -p.X * t;

				hsl.Hue = (f32) acos ( xn );
				if ( p.Y > 0 )
					hsl.Hue = (2.f * core::PI ) - hsl.Hue;

				hsl.Hue -= core::PI / 2.f;

				f32 rTest = r / radiusOut;
/*
				if ( rTest < 0.25f )
				{
					hsl.Luminance = rTest / 0.25f;
					hsl.Saturation = 0.f;
					hsl.settoRGB  ( rgb );
					*dst = rgb.color;
				}
				else
				if ( rTest < 0.4f )
				{
					hsl.Saturation = ( rTest - 0.25f ) / 0.15f;
					hsl.Luminance = 1.f - ( hsl.Saturation / 2.4f );
					hsl.Luminance = 0.5f;
					hsl.settoRGB  ( rgb );
					// *dst = rgb.color;
				}
				else
				if ( rTest < 0.75f )
				{
					hsl.Luminance = 0.5f;
					hsl.Saturation = 1.f;
					hsl.settoRGB  ( rgb );
					*dst = rgb.color;
				}
				else
				if ( rTest < 0.98f )
				{
					hsl.Luminance = 0.5f - ( ( rTest - 0.75f ) / 0.75f );
					hsl.Saturation = 1.f;
					hsl.settoRGB  ( rgb );
					*dst = rgb.color;
				}
*/

				if ( rTest >= 0.5f )
				{
					hsl.Luminance = 0.5f;
					hsl.Saturation = 1.f;
					hsl.settoRGB  ( rgb );
					*dst = rgb.color;
				}

				if ( rTest >= 0.5f && rTest <= 0.55f )
				{
					u32 alpha = (s32) ( (rTest - 0.5f ) * ( 255.f / 0.05f ) );
					*dst = *dst & 0x00ffffff | (alpha << 24);
				}

				if ( rTest >= 0.95f )
				{
					u32 alpha = (s32) ( (rTest - 0.95f ) * ( 255.f / 0.05f ) );
					alpha = 255 - alpha;
					*dst = *dst & 0x00ffffff | (alpha << 24);
				}

			}
		}

	}

	RawTexture->unlock ();

	if ( supersample > 1 )
	{
		video::CImage * filter = new video::CImage(irr::video::ECF_A8R8G8B8, dim );
		RawTexture->copyToScalingBoxFilter ( filter, 0 );
		RawTexture->drop ();
		RawTexture = filter;
	}
	

	video::IVideoDriver* driver = Environment->getVideoDriver();

	bool generateMipLevels = driver->getTextureCreationFlag( video::ETCF_CREATE_MIP_MAPS );
	driver->setTextureCreationFlag( video::ETCF_CREATE_MIP_MAPS, false);

	ColorRing.Texture = driver->addTexture ( "#colorring", RawTexture  );
	RawTexture->drop ();

	driver->setTextureCreationFlag( video::ETCF_CREATE_MIP_MAPS, generateMipLevels);

}



//! called if an event happened.
bool CGUIColorSelectDialog::OnEvent(SEvent event)
{

	switch(event.EventType)
	{
		case EET_GUI_EVENT:
		switch(event.GUIEvent.EventType)
		{
			case EGET_SCROLL_BAR_CHANGED:
			{
				for ( u32 i = 0; i!= Battery.size (); ++i )
				{
					if ( event.GUIEvent.Caller == Battery[i].Scrollbar )
					{
						s32 pos = Battery[i].Scrollbar->getPos ();
						s32 value = Template[i].range_down + ( pos );
						core::stringw s ( value );
						Battery[i].Edit->setText ( s.c_str() );
					}
				}
				return true;
			} break;

		case EGET_ELEMENT_FOCUS_LOST:
			Dragging = false;
			break;
		case EGET_BUTTON_CLICKED:
			if (event.GUIEvent.Caller == CloseButton ||
				event.GUIEvent.Caller == CancelButton)
			{
				sendCancelEvent();
				remove();
				return true;
			}
			else
			if (event.GUIEvent.Caller == OKButton)
			{
				sendSelectedEvent();
				remove();
				return true;
			}
			break;

		case EGET_LISTBOX_CHANGED:
			{
			}
			break;

		case EGET_LISTBOX_SELECTED_AGAIN:
			{
			}
			break;
		}
		break;
	case EET_MOUSE_INPUT_EVENT:
		switch(event.MouseInput.Event)
		{
		case EMIE_LMOUSE_PRESSED_DOWN:
			DragStart.X = event.MouseInput.X;
			DragStart.Y = event.MouseInput.Y;
			Dragging = true;
			Environment->setFocus(this);
			return true;
		case EMIE_LMOUSE_LEFT_UP:
			Dragging = false;
			Environment->removeFocus(this);
			return true;
		case EMIE_MOUSE_MOVED:
			if (Dragging)
			{
				move(core::position2d<s32>(event.MouseInput.X - DragStart.X, event.MouseInput.Y - DragStart.Y));
				DragStart.X = event.MouseInput.X;
				DragStart.Y = event.MouseInput.Y;
				return true;
			}
			break;
		}
	}

	return Parent ? Parent->OnEvent(event) : false;
}


//! draws the element and its children
void CGUIColorSelectDialog::draw()
{
	if (!IsVisible)
		return;

	IGUISkin* skin = Environment->getSkin();

	core::rect<s32> rect = AbsoluteRect;

	rect = skin->draw3DWindowBackground(this, true, skin->getColor(EGDC_ACTIVE_BORDER), 
		rect, &AbsoluteClippingRect);

	if (Text.size())
	{
		rect.UpperLeftCorner.X += 2;
		rect.LowerRightCorner.X -= skin->getSize(EGDS_WINDOW_BUTTON_WIDTH) + 5;

		IGUIFont* font = skin->getFont();
		if (font)
			font->draw(Text.c_str(), rect, skin->getColor(EGDC_ACTIVE_CAPTION), false, true, 
			&AbsoluteClippingRect);
	}

	IGUIElement::draw();
}




//! sends the event that the file has been selected.
void CGUIColorSelectDialog::sendSelectedEvent()
{
	SEvent event;
	event.EventType = EET_GUI_EVENT;
	event.GUIEvent.Caller = this;
	event.GUIEvent.EventType = EGET_FILE_SELECTED;
	Parent->OnEvent(event);
}

//! sends the event that the file choose process has been canceld
void CGUIColorSelectDialog::sendCancelEvent()
{
	SEvent event;
	event.EventType = EET_GUI_EVENT;
	event.GUIEvent.Caller = this;
	event.GUIEvent.EventType = EGET_FILE_CHOOSE_DIALOG_CANCELLED;
	Parent->OnEvent(event);
}

} // end namespace gui
} // end namespace irr
