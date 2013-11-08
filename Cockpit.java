/**
 * @author Zion Corp.
 *
 * Cockpit Ship
 */
import javax.microedition.m3g.*;


public class Cockpit 
{
  private Mesh imMesh;

  public void SetVisible(boolean visible) {
  	imMesh.setRenderingEnable(visible);
 }  

  
  public Cockpit(Image2D bbImage) 
  { 
    // build the mesh
    VertexBuffer vb = makeGeometry();
    float x = 0; 
	float y = 0f; //-0.05f 
	float z = -0.15f;  
	float size = 0.13f;

    int[] indicies = {1,2,0,3};  // one quad
    int[] stripLens = {4};
    IndexBuffer ib = new TriangleStripArray(indicies, stripLens);

    Appearance app = makeAppearance(bbImage);
    imMesh = new Mesh(vb, ib, app);

    // scale and position the mesh
    imMesh.scale(size, size, size);
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
