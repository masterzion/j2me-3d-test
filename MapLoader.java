/*
 * Created on 17/09/2005
 *
 * TODO LOAD MAP
 * 
 */

/**
 * @author Zion Corp.
 *
 * 
 */
import javax.microedition.m3g.*;

import java.io.*;

public class MapLoader {
    private Group GroupMap;
    private int startx;    
    private int starty;
    private int numrows = 0;    
    private int numcols = 0;

    private Appearance boxApp;
    private Appearance cilApp;            	
    private final static float RADIUS = 0.5f;
    private final static float HEIGHT = 3.0f;    
    
    private char[][] maparray;
    
    public  Group getobjectmap() {
    	return GroupMap;
	}
	
	public void CreateMap(){
		GroupMap = new Group();
		char inchar;
	    System.out.println("Creating Map");		
        for (int i1 = 0; i1 < numrows; i1++) {
            for (int i2 = 0; i2 < numcols; i2++) {
            	inchar = maparray[i2][i1];
            	Image2D bbIms1 = mzlib.loadImage2D("/floor/metal1.jpg");

//            	0.0        x  0.40000153 x -1.6774902
//				0.46335155 x  0.40000153 x 1.6755322

         //   	GroupMap.addChild( WallObject.getwall(bbIms1,1,(i1 * 0.5f), 0 ,(i2 * 0.5f)) );
            	
            	
//            	if ( inchar == 'b' ) GroupMap.addChild( Load3DFile.loadMesh("/test.m3g", 0, 0.0048f, i1/2, 0, i2/2f) );
//            	if ( inchar == 'c' ) GroupMap.addChild( Load3DFile.loadMesh("/test.m3g", 1, 0.0048f, i1/2, 0, i2/2f) );            	
            	    
            } // for i2
        } // for i1
		
	}

	
	

	public void readFile(int lev)
	  // Initialise maze[][] by reading the maze plan from fn
	  {

	      InputStream is = null;
	      String text = "";
	      int inchar;
	      int i3 = 0;
	      char chr;
	      boolean bcount = true;
	      

	  	try {
          is = getClass().getResourceAsStream("/"+ lev+".txt");
          
          while ((inchar = is.read()) != -1) {
          	if (inchar == '\n') text += '\n';
          	if (inchar == 'a') text += 'a';
          	if (inchar == 'b') text += 'b';
          	if (inchar == 'c') text += 'c';          	
          	if (inchar == 's') text += 's';
          	if (inchar == ' ') text += '0';

          	//conta linhas
          	if (inchar == '\n') { 
          		bcount = false;
          		numrows++; 
          	}	

           	// conta colunas 
          	if ( bcount ) { 
           		numcols++; 
           	} // if bcount
          	

          } // while
          is.close();
	      System.out.println(numcols+"x"+numrows);	      
          char [][] inmaparray = new char[numcols][numrows];

        for (int i1 = 0; i1 < numcols; i1++) {
            for (int i2 = 0; i2 < numrows; i2++) {
            	chr = text.charAt(i3);
            	inmaparray[i1][i2] = chr;
            	i3++;
            	if (chr == 's') {
        		  startx = i1;
        		  starty = i2;
            	}
            	
            }
        }    
        maparray = inmaparray; 
          
	  	}// try
		catch (IOException e) 
	    { 
  	    	System.out.println("Error reading maze plan from ");
	    }
	  } // end of readFile()	
	
	  /*
	  private void generatearray(){
		
		int arrayWidth = Mapimg.getWidth();
		int arrayHeight = Mapimg.getHeight();		
		int [][] inmaparray = new int[arrayWidth][arrayHeight];
		int [] rgbDataSource = new int[arrayWidth * arrayHeight];
		int row = 0;
		int col = 0;

        System.out.println("Generate array "+arrayWidth+" x "+arrayHeight);		
		Mapimg.getRGB(rgbDataSource, 0, arrayWidth, 0, 0, arrayWidth, arrayHeight);
		
        System.out.println("RGB array "+rgbDataSource.length);
		
        for (int i = 0; i < rgbDataSource.length; i++) {
        	inmaparray[row][col] = rgbDataSource[i];
        	if (rgbDataSource[i] == -167772160) {
        		startx = row;
        		starty = col;
        	}
        	col++;
        	if (col == arrayHeight) {
       		    row++;
              	col=0;
        	}
        } // for
        maparray = inmaparray;
	}
        */
		
	public void getStart(int x,int y){
		x = startx;
		y = starty;
	}
	
	public void drawconsole(){
		int arrayWidth = numcols;
		int arrayHeight = numrows;
		String str = "";
		
        for (int i1 = 0; i1 < arrayHeight; i1++) {
            for (int i2 = 0; i2 < arrayWidth; i2++) {
            	    str += maparray[i2][i1]+" ";
            } // for i2
            System.out.println(str);
            str = "";
        } // for i1
	}

}
