
// ImageSquare.java
// Andrew Davison, dandrew@fivedots.coe.psu.ac.th, November 2004

/* The ImageSquare is a square of length size, with its center
   positoned at (x,y,z).

   The image wrapped over it (bbImage) can have transparent elements 
   which will not be rendered when the mesh is displayed.
*/

import javax.microedition.m3g.*;


public class SquareMesh 
{
  private Mesh imMesh;

  public SquareMesh(Image2D bbImage, float x, float y, float z, float size) 
  { 
    // build the mesh
    VertexBuffer vb = makeGeometry();

    int[] indicies = {1,2,0,3};  // one quad
    int[] stripLens = {4};
    IndexBuffer ib = new TriangleStripArray(indicies, stripLens);

    Appearance app = makeAppearance(bbImage);

    
    imMesh = new Mesh(vb, ib, app);

    // scale and position the mesh
    float scale2 = size * 0.5f;   // to reduce 2 by 2 image to 1 by 1
    imMesh.scale(scale2, scale2, scale2);
    imMesh.setTranslation(x, y, z);
  }  // end of ImageSquare()


  private VertexBuffer makeGeometry()
  /*  A square centered at the origin on the XZ plane
      with sides of 2 units. No normals. */
  {
    // create vertices
    short[] verts = {-1,-1,0,  1,-1,0,  1,1,0,  -1,1,0};
    VertexArray va = new VertexArray(verts.length/3, 3, 2);
    va.set(0, verts.length/3, verts);

    // create texture coordinates
    short[] tcs = {0,1,  1,1,  1,0,  0,0};
    VertexArray texArray = new VertexArray(tcs.length/2, 2, 2);
    texArray.set(0, tcs.length/2, tcs);

    VertexBuffer vb = new VertexBuffer(); 
    vb.setPositions(va, 1.0f, null); // no scale, bias
    vb.setTexCoords(0, texArray, 1.0f, null);

    return vb;
  }  // end of makeGeometry()


  private Appearance makeAppearance(Image2D bbImage)
  // The appearance comes from the transparent texture in bbImage.
  {
    Appearance app = new Appearance();

    // only display the opaque parts of the texture
    CompositingMode compMode = new CompositingMode();
    compMode.setBlending(CompositingMode.ALPHA);
    app.setCompositingMode(compMode);

    if (bbImage != null) {
      Texture2D tex = new Texture2D(bbImage);
      tex.setFiltering(Texture2D.FILTER_NEAREST, Texture2D.FILTER_NEAREST);
      tex.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
      tex.setBlending(Texture2D.FUNC_REPLACE); 
                // the texture's colours and alpha replace the mesh's
      app.setTexture(0, tex);
    }
    return app;
  }  // end of makeAppearance()


  // -------------------- access method ----------------------

  public Mesh getImageSquareMesh()
  {  return imMesh;  }

} // end of ImageSquare Class
