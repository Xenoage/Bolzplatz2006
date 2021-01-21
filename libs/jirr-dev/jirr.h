#ifndef _JIRR_H
#define _JIRR_H

#include <irrlicht.h>
#include <string.h>

namespace irr
{
	class SEventQueue: public IEventReceiver
	{
	public:
		bool OnEvent (SEvent event) {
			if (event.EventType == EET_LOG_TEXT_EVENT)
				event.LogEvent.Text = strdup (event.LogEvent.Text);
			eventQueue.push_front (event);
			return false;
		}
		irr::SEvent getEvent () {
			core::list<SEvent>::Iterator it = eventQueue.getLast ();
			SEvent event = *it;
			eventQueue.erase (it);
			return event;
		}
		bool hasEvent () {
			return !eventQueue.empty ();
		}
	
	private:
		core::list<SEvent> eventQueue;
	};	
}


/*namespace irr
{
	namespace core
	{
		template <class T>
		class vector3d
		{
		public:
			bool operator<(const vector3d<T>&other) const
			{
				return X<other.X && Y<other.Y && Z<other.Z;
			}
		};
	}
}*/



/*
// These classes replace core::list<T>::SKListNode and core::list<T>::Iterator
namespace irr 
{
	namespace core 
	{
		template<class T>
		class list;
	
		template<class T>
			//! List element node with pointer to previous and next element in the list.
			struct SKListNode
			{
				SKListNode() : next(0), prev(0) {};
	
				SKListNode* next;
				SKListNode* prev;
				T element;
			};
	
		//! List iterator.
		template<class T>
		class Iterator
		{
		public:
	
			Iterator() : current(0) {};
			Iterator(SKListNode<T>* begin) : current(begin) {};
	
			Iterator& operator ++() { current = current->next; return *this; };
			Iterator& operator --() { current = current->prev; return *this; };
			Iterator operator ++(s32) { Iterator tmp = *this; current = current->next; return tmp; };
	  		Iterator operator --(s32) { Iterator tmp = *this; current = current->prev; return tmp; };
	
			Iterator operator+(s32 num) const
			{
				Iterator tmp = *this;
	
				if (num >= 0)
					while (num-- && tmp.current != 0) ++tmp;
				else
					while (num++ && tmp.current != 0) --tmp;
	
				return tmp;
			}
	
			Iterator& operator+=(s32 num)
			{
				if (num >= 0)
					while (num-- && this->current != 0) ++(*this);
				else
					while (num++ && this->current != 0) --(*this);
	
				return *this;
			}
	
			Iterator operator-(s32 num) const  { return (*this)+(-num);          }
			Iterator operator-=(s32 num) const { (*this)+=(-num);  return *this; }
	
			bool operator ==(const Iterator& other) const { return current == other.current; };
			bool operator !=(const Iterator& other) const { return current != other.current; };
	
			T& operator *() { return current->element; };
	
		private:
	
			friend class list<T>;
	
			SKListNode<T>* current;
		};	
	}
}
*/




// INSERTED BY OAK
namespace irr
{
  extern "C" 
  {
    IrrlichtDevice* createDeviceJava(
                video::E_DRIVER_TYPE deviceType,
                const int w,
                const int h,
                u32 bits,
                bool fullscreen,
                bool stencilbuffer,
                bool vsync,
                IEventReceiver* receiver)
    {
      core::dimension2d<s32> windowSize = core::dimension2d<s32>(w,h);
      return createDevice(deviceType, 
                          windowSize, 
                          bits,
                          fullscreen,
                          stencilbuffer,
                          vsync,
                          receiver);
    }
	 
    /*void swig_module_init()
    {
      printf("swig_module_init (stub) called... \n");
    }*/
  }


  template <class T>
  core::vector3d<T> *createVector3d(T x, T y, T z) 
  {
    return new core::vector3d<T>(x, y, z);
  };


  template <class T>
  core::dimension2d<T> *createDimension2d(T x, T y) 
  {
    return new core::dimension2d<T>(x,y);
  };


  wchar_t *createWchar(const char *str)
  {
/*
    mbstate_t mbstate;
    mbsinit(&mbstate);
    printf("Creating wchar_t: %s\n", str);
    wchar_t *pwc = (wchar_t *)malloc(strlen(str) * sizeof(wchar_t));
    //mbrtowc(pwc, str, strlen(str), &mbstate);
    mbsrtowcs(pwc, &str, strlen(str), &mbstate);
    return pwc;
*/
    wchar_t* temp = new wchar_t[strlen( str )+1];
	 int x;
	 if ( !temp ) return 0;
	
	 for ( x = 0; x < strlen( str ); x++ )
	   temp[x] = (wchar_t)str[x];
	 temp[x] = 0;
	
	 return temp;        
  };

  void instantiateAllDontExecMe()
  {
    core::vector3d<f32> *crapper = createVector3d<f32>(0.0,0.0,0.0);
    core::vector3d<s32> *crapper2 = createVector3d<s32>(0,0,0);
    core::dimension2d<s32> *crapper3 = createDimension2d<s32>(1,1);
    core::dimension2d<f32> *crapper4 = createDimension2d<f32>(1.0,1.0);
  }
  

	// inserted by SD
	namespace gui
	{
		// silly Workaround ...
		IGUIScrollBar* castToIGUIScrollBar(IGUIElement *guiElement)
		{
			return (IGUIScrollBar*) guiElement;
		}
		IGUIContextMenu* castToIGUIContextMenu(IGUIElement *guiElement)
		{
			return (IGUIContextMenu*) guiElement;
		}		
		IGUIFileOpenDialog* castToIGUIFileOpenDialog(IGUIElement *guiElement)
		{
			return (IGUIFileOpenDialog*) guiElement;
		}		
		
	}
}

/*
namespace irr
{
	namespace core
	{
		class vector3df
		{
		public:
			bool operator<(const vector3df &other) const
			{
				return X<other.X && Y<other.Y && Z<other.Z;
			}
		};
	}
}
*/
#endif

