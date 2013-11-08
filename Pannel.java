import javax.microedition.m3g.*;
import javax.microedition.lcdui.*;
import java.util.*;



public class Pannel 
{
  private final static int MAX_COUNTER = 100;
   // used by the counter in the string example

  private final static int IM_SIZE = 32;
      // size of the image square, im

  private Group cameraGroup;   // used for camera alignment
  private Mesh bbMesh;
  private Appearance app;

  // image related objects
  private Image im;            // for holding the dynamic image
  private Graphics graphicIm;  // the graphics context for im
  private int[] pixels;        // holds pixels extracted from im
  private byte[] imBytes;      // holds bytes extracted from pixels[]

  private int counter;    // used in the incrementing counter string example
  private Font font;
  private Random rnd;     // used in the random colours example


  public Pannel(Group camGroup, float size) 
  { 
    cameraGroup = camGroup;

    // build the mesh
    VertexBuffer vb = makeGeometry();

    int[] indicies = {1,2,0,3};  // one quad
    int[] stripLens = {4};
    IndexBuffer ib = new TriangleStripArray(indicies, stripLens);

    // create image objects
    im = Image.createImage(IM_SIZE, IM_SIZE); // holds the image
	graphicIm = im.getGraphics();             // graphics context for im
    pixels = new int[IM_SIZE * IM_SIZE];      // holds pixels pulled from im
    imBytes = new byte[4 * pixels.length];    // holds bytes pulled from the pixels

    rnd = new Random();
    counter = 0;
    font = Font.getFont(Font.FONT_STATIC_TEXT, 
                         Font.STYLE_BOLD, Font.SIZE_LARGE);

    // set the board's initial appearance
    // app = makeAppearance( dynamicTexture(""+counter));   // strings example
    app = makeAppearance( dynamicTexture());    // random colours example
       // edit to choose which example to show

    bbMesh = new Mesh(vb, ib, app);

    // scale and position the mesh
    float scale2 = size * 0.5f;   // to reduce 2 by 2 image to 1 by 1
    bbMesh.scale(scale2, scale2, scale2);
    bbMesh.setTranslation(0, scale2, 0);

    bbMesh.setAlignment(cameraGroup, Node.Z_AXIS, null, Node.NONE); 
             // only use z-axis alignment with the camera
  }  // end of DynaBoard()


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
  {  bbMesh.setTranslation(x,y,z); }


  public void update()
  /* Update the camera's position and redraw the dynamic billboard.
     For the strings example, the counter must be incremented,
     modulo MAX_COUNTER. */
  { 
    bbMesh.align(cameraGroup);   // update alignment
/*
    // strings example
    if (counter == MAX_COUNTER)
      counter = 0;    // reset the counter
    app.setTexture(0, dynamicTexture("" + counter));
    counter++;
*/
    app.setTexture(0, dynamicTexture());    // random colours example
       // edit these lines to choose which example to show
  } // end of update()



  // -------------------- access methods ----------------------

  public Mesh getBoardMesh()
  {  return bbMesh;  }


  // ---------- texture creation from a dynamic image ---------------


  private Texture2D dynamicTexture()
  // example 1: create a texture from a random mix of colours
  {
    makeRandomImage();    // initialise global Image object, im
    Image2D im2D = convertImage(im);      // Image --> Image2D
    Texture2D tex = createTexture(im2D);  // Image2D --> Texture2D
    return tex;
  }  // end of dynamicTexture()


  private void makeRandomImage()
  /* Initialise the global Image object, im, by writing to its
     associated Graphics object, graphicIm.

     Any white elements will become transparent when Image is
     converted to Image2D.

     The random image is a mix of randomly placed red and green squares.
  */
  {
    // a white background 
    graphicIm.setColor(0xFFFFFF);  // white
    graphicIm.fillRect(0, 0, IM_SIZE, IM_SIZE);

    int numPixels = IM_SIZE * IM_SIZE;

    // draw a series of randomly placed red squares
    graphicIm.setColor(0xFF0000);  // red
    for (int i=0; i < numPixels/4; i++)
      graphicIm.fillRect( rnd.nextInt(IM_SIZE), rnd.nextInt(IM_SIZE), 1, 1);

    // draw a series of randomly placed green squares
    graphicIm.setColor(0x00FF00);  // green
    for (int i=0; i < numPixels/4; i++)
      graphicIm.fillRect( rnd.nextInt(IM_SIZE), rnd.nextInt(IM_SIZE), 1, 1);
  }  // end of makeRandomImage()



  // ------------- example 2 ---------------------------

  private Texture2D dynamicTexture(String str)
  // example 2: create a texture using a string
  {
    makeStringImage(str);     // initialise global Image object, im
    Image2D im2D = convertImage(im);      // Image --> Image2D
    Texture2D tex = createTexture(im2D);  // Image2D --> Texture2D
    return tex;
  }  // end of dynamicTexture()



  private void makeStringImage(String str)
  /* Initialise the global Image object, im, by writing to its
     associated Graphics object, graphicIm.

     Any white elements will become transparent when Image is
     converted to Image2D.

     The string, str, is drawn in the middle of the image in a 
     large bold black font.
  */
  {
    // a white background 
    graphicIm.setColor(0xFFFFFF);  // white
    graphicIm.fillRect(0, 0, IM_SIZE, IM_SIZE);

    // set the font to be black, large, and bold
    graphicIm.setColor(0x000000); 
    graphicIm.setFont(font);

    // draw the string in the center of the image
    int strWidth = font.stringWidth(str);
    int strHeight = font.getHeight();
    int xOff = (IM_SIZE - strWidth)/2;
    int yOff = (IM_SIZE - strHeight)/2;
    graphicIm.drawString(str, xOff, yOff, Graphics.TOP|Graphics.LEFT);
  }  // end of makeStringImage()


  // --------------- image --> texture methods -----------------

  private Image2D convertImage(Image im)
  /* The im Image is converted to Image2D. In the process, the
     image's ARGB format is changed to RGBA, and any white 
     pixels become fully transparent.

     The Image data is extracted as an array of pixels, with
     each pixel in ARGB format. The red, green, and blue components
     are extracted as integers, then stored in a byte array
     in the order RGBA. The alpha part (A) is opaque by default,
     but is transparent if RGB are white.

     The byte array is used to create the Image2D object.
  */
  {
    int alphaVal, redVal, greenVal, blueVal;

    // extracts ARGB pixel data from im into pixels[]
    im.getRGB(pixels, 0, IM_SIZE, 0, 0, IM_SIZE, IM_SIZE);

    // store pixels in byte array, imBytes[]
    for(int i=0; i < pixels.length; i++){
      // extract RGB components from the current pixel as integers
      redVal = (pixels[i]>>16)&255;
      greenVal = (pixels[i]>>8)&255;
      blueVal = pixels[i]&255;
		 
      // set alpha to be transparent if colour is white
      alphaVal = 255;   // opaque by default
      if((redVal==255) && (greenVal==255) && (blueVal==255))  // is white?
        alphaVal = 0; // fully transparent

      // store components in RGBA order in byte array
      imBytes[i*4] = (byte)redVal;
      imBytes[(i*4)+1] = (byte)greenVal;
      imBytes[(i*4)+2] = (byte)blueVal;
      imBytes[(i*4)+3] = (byte)alphaVal;
    }

    // use the byte array to create Image2D object
    Image2D im2D = new Image2D(Image2D.RGBA, IM_SIZE, IM_SIZE, imBytes);
    return im2D;
  }  // end of convertImage()


  private Texture2D createTexture(Image2D im2D)
  /* Use the Image2D to create a single texture which covers the
     board's surface and replaces any appearance it may have. */
  {
    Texture2D tex = new Texture2D(im2D);
    tex.setFiltering(Texture2D.FILTER_NEAREST, Texture2D.FILTER_NEAREST);
    tex.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
    tex.setBlending(Texture2D.FUNC_REPLACE); 

    return tex;
  }  // end of createTexture()

} // end of DynaBoard Class
