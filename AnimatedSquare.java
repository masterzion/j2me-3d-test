
//AnimBillboard.java
//Andrew Davison, dandrew@fivedots.coe.psu.ac.th, November 2004

/* The AnimBillboard is a square of length size, on top of the
origin on the XZ plane. The animation is a sequence of images
(used as textures) which are shown in quick succession on the
 face of the board.

When the board is made visible, the image sequence is run 
through once (updated with each call to update()), and then
becomes invisible again.

The board can be positioned anywhere with setPosition().

The board keeps its z-axis aligned with the camera position.

The images wrapped over the board can have transparent elements 
which will not be rendered when the mesh is displayed.
*/

import javax.microedition.m3g.*;

/**
 * @author  master
 */
public class AnimatedSquare 
{
private Mesh bbMesh;
private Appearance app;

private Texture2D[] textures;   // holds the images
private int numImages;

private int currImIdx;

private boolean isVisible;  // is the animated board visible?

private Group cameraGroup;   // used for camera alignment


public AnimatedSquare(Image2D[] bbIms, Group camGroup, float size) 
{ 
 cameraGroup = camGroup;

 // build the mesh
 VertexBuffer vb = makeGeometry();

 int[] indicies = {1,2,0,3};  // one quad
 int[] stripLens = {4};
 IndexBuffer ib = new TriangleStripArray(indicies, stripLens);

 numImages = mzlib.EXPLOSION_SIZE;
 currImIdx = 0;
 textures = makeTextures(bbIms, numImages);

 app = makeAppearance( textures[currImIdx] );

 bbMesh = new Mesh(vb, ib, app);

 // scale and position the mesh
 float scale2 = size * 0.5f;   // to reduce 2 by 2 image to 1 by 1
 bbMesh.scale(scale2, scale2, scale2);
 bbMesh.setTranslation(0, scale2, 0);

 bbMesh.setAlignment(cameraGroup, Node.Z_AXIS, null, Node.NONE); 
          // only use z-axis alignment with the camera
 bbMesh.setPickingEnable(false);

 setVisible(false);
}  // end of AnimBillboard()


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


private Texture2D[] makeTextures(Image2D[] ims, int num)
// convert the images array into an array of textures
{
 Texture2D[] textures = new Texture2D[num];

 for (int i=0; i < num; i++) {
   if (ims[i] != null) {
     textures[i] = new Texture2D(ims[i]);
     textures[i].setFiltering(Texture2D.FILTER_NEAREST, Texture2D.FILTER_NEAREST);
     textures[i].setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
     textures[i].setBlending(Texture2D.FUNC_REPLACE); 
   }    // the texture's colours and alpha replace the mesh's
   else
     System.out.println("Image " + i + " is null");
 }
 return textures;
}  // end of makeTextures()


private Appearance makeAppearance(Texture2D tex)
// The appearance comes from the transparent texture in tex.
{
 app = new Appearance();    // no material

 // Only display the opaque parts of the texture
 CompositingMode compMode = new CompositingMode();
 compMode.setBlending(CompositingMode.ALPHA);
 app.setCompositingMode(compMode);

 app.setTexture(0, tex);

 return app;
}  // end of makeAppearance()


// -------------------- animate the board -----------------


public void setPosition(float x, float y, float z)
// position the board
{  bbMesh.setTranslation(x,y,z);
   System.out.println("Loading model at "+x+"x"+y+"x"+z);
}


/**
 * @param isVisible  The isVisible to set.
 * @uml.property  name="isVisible"
 */
public void setVisible(boolean visible)
/* Make the board visible or invisible. If the board is made visible, 
  then its image display index (currImIdx) is reset.
*/
{ isVisible = visible;
 if (isVisible) {
   bbMesh.setRenderingEnable(true);
   currImIdx = 0;    // reset the image display index
 }
 else
   bbMesh.setRenderingEnable(false);
}  // end of setVisible()



public void update()
/* If the board is visible, show the current image in the 
  sequence, and finish by incrementing the image index. 

  When the end of the sequence is reached, make the board
  invisible.
*/
{ if (isVisible) {
   bbMesh.align(cameraGroup);   // update alignment
   if (currImIdx == numImages)
     setVisible(false);
   else
     app.setTexture(0, textures[currImIdx]);  // show image
   currImIdx = currImIdx+1;   // set to next image index
 }
} // end of update()



// -------------------- access methods ----------------------

public Mesh getAnimBillboardMesh()
{  return bbMesh;  }


/**
 * @return
 * @uml.property  name="isVisible"
 */
public boolean isVisible()
// is the billboard visible?
{  return isVisible; } 


} // end of AnimBillboard Class
