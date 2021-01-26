//%template(vector3dfarray) irr::core::array<irr::core::vector3df>;

// in vector3d.h (irrlicht/include) einfügen: 
// bool operator<(const vector3d<T>&other) const { return X<other.X && Y<other.Y && Z<other.Z;};

// muß automatisch gehen!!! Swig-Doku wälzen!

// 050527: Sonst patch!


