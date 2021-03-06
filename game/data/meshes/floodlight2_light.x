xof 0303txt 0032
template FVFData {
 <b6e70a0e-8ef9-4e83-94ad-ecc8b0c04897>
 DWORD dwFVF;
 DWORD nDWords;
 array DWORD data[nDWords];
}

template EffectInstance {
 <e331f7e4-0559-4cc2-8e99-1cec1657928f>
 STRING EffectFilename;
 [...]
}

template EffectParamFloats {
 <3014b9a0-62f5-478c-9b86-e4ac9f4e418b>
 STRING ParamName;
 DWORD nFloats;
 array FLOAT Floats[nFloats];
}

template EffectParamString {
 <1dbc4c88-94c1-46ee-9076-2c28818c9481>
 STRING ParamName;
 STRING Value;
}

template EffectParamDWord {
 <e13963bc-ae51-4c5d-b00f-cfa3a9d97ce5>
 STRING ParamName;
 DWORD Value;
}


Material PDX01_-_Default {
 1.000000;1.000000;1.000000;1.000000;;
 3.200000;
 0.000000;0.000000;0.000000;;
 0.000000;0.000000;0.000000;;

 TextureFilename {
  "floodlight2_light.png";
 }
}

Mesh Plane01 {
 4;
 3.532018;19.388380;0.395891;,
 1.532018;19.388380;0.395891;,
 3.532018;21.286146;1.027144;,
 1.532018;21.286146;1.027144;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane02 {
 4;
 0.092249;19.388380;0.395891;,
 -1.907751;19.388380;0.395891;,
 0.092249;21.286146;1.027144;,
 -1.907751;21.286146;1.027145;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane03 {
 4;
 1.788380;19.388380;0.573150;,
 -0.211620;19.388380;0.573151;,
 1.788380;21.286146;1.204404;,
 -0.211620;21.286146;1.204404;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane04 {
 4;
 -1.604591;19.388380;0.573151;,
 -3.604591;19.388380;0.573151;,
 -1.604591;21.286146;1.204404;,
 -3.604591;21.286146;1.204404;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane05 {
 4;
 0.092249;17.732626;0.395891;,
 -1.907751;17.732626;0.395891;,
 0.092249;19.630392;1.027144;,
 -1.907751;19.630392;1.027145;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane06 {
 4;
 1.788380;17.732626;0.573150;,
 -0.211620;17.732626;0.573151;,
 1.788380;19.630392;1.204404;,
 -0.211620;19.630392;1.204404;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane07 {
 4;
 -1.604591;17.732626;0.573151;,
 -3.604591;17.732626;0.573151;,
 -1.604591;19.630392;1.204404;,
 -3.604591;19.630392;1.204405;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane08 {
 4;
 3.532018;17.732626;0.395891;,
 1.532018;17.732626;0.395891;,
 3.532018;19.630392;1.027144;,
 1.532018;19.630392;1.027144;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane09 {
 4;
 0.092249;15.913357;0.395891;,
 -1.907751;15.913357;0.395891;,
 0.092249;17.811123;1.027144;,
 -1.907751;17.811123;1.027145;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane10 {
 4;
 1.788380;15.913357;0.573151;,
 -0.211620;15.913357;0.573151;,
 1.788380;17.811123;1.204404;,
 -0.211620;17.811123;1.204404;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane11 {
 4;
 -1.604591;15.913357;0.573151;,
 -3.604591;15.913357;0.573151;,
 -1.604591;17.811123;1.204404;,
 -3.604591;17.811123;1.204405;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}

Mesh Plane12 {
 4;
 3.532018;15.913357;0.395891;,
 1.532018;15.913357;0.395891;,
 3.532018;17.811123;1.027144;,
 1.532018;17.811123;1.027144;;
 2;
 3;2,3,0;,
 3;1,0,3;;

 MeshNormals  {
  1;
  0.000000;-0.315627;0.948883;;
  2;
  3;0,0,0;,
  3;0,0,0;;
 }

 MeshMaterialList  {
  1;
  2;
  0,
  0;
  { PDX01_-_Default }
 }

 MeshTextureCoords  {
  4;
  0.000000;1.000000;,
  1.000000;1.000000;,
  0.000000;0.000000;,
  1.000000;0.000000;;
 }
}