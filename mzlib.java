/*
 * Created on 08/09/2005
 *
 * Funcoes Comuns 
 */

/**
 * @author Zion Corp.
 *
 * Funcoes Comum
 * Window - Preferences - Java - Code Style - Code Templates
 */

import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Texture2D;

/**
 * @author  master
 */
public class mzlib {

      // ************************ 
	  // *  Constant Image list *
      // ************************	
	
	  //BG constants
      public static final String BG_LEVEL1           = "/sky/starsnebulapic.jpg";
//	  public static final String COCKPIT_IMAGE   = "/guns/cabine3.png";
	  public static final String COCKPIT_IMAGE       = "/guns/cabine11.png";	  
	  public static final String TRANSPARENT_IMAGE   = "/guns/transp.png";
	  //Guns
	  public static final String SMALL_GUN_FIRE_1   = "/guns/shot.png";

	  public static final int MAX_PLANET        = 2;
	  
	  //Planet1
	  public static final String PLANET1        = "/images/planet1.png";
	  public static final String SHIP1          = "/images/ship1.png";
	  public static final String CIVILIZATION1  = "EARTH";
	  public static final int SPEED1            = 1;	  
	  public static final int FIRE1             = 2;
	  public static final int SHIELD1           = 1;
	  	  
	  //Planet2
	  public static final String PLANET2        = "/images/planet2.png";
	  public static final String SHIP2          = "/images/ship2.png";
	  public static final String CIVILIZATION2  = "MARKS";
	  public static final int SPEED2            = 2;	  
	  public static final int FIRE2             = 1;
	  public static final int SHIELD2           = 1;

	  //Planet3
	  public static final String PLANET3        = "/images/planet3.png";
	  public static final String SHIP3          = "/images/ship3.png";
	  public static final String CIVILIZATION3  = "ZENUS";
	  public static final int SPEED3            = 1;	  
	  public static final int FIRE3             = 1;
	  public static final int SHIELD3           = 2;
	  
	  
      //Explosion
	  public static final int EXPLOSION = 13;
	  public static final int MAX_EXPLOSION_SIZE = 26;	  
	  public static int EXPLOSION_SIZE = 26;	
	  
	  //Images
	  public static final String IMG_SNLOGO        = "/images/snlogo.jpg";
	  public static final String IMG_LOGO          = "/images/logo.jpg";
	  public static final String IMG_LOGOMENU      = "/images/logo_menu.jpg";	
	  public static final String IMG_SHIPSELECT    = "/images/ship_select.jpg";
	  
      //Sounds
	  public static final String SND_BUTTON        = "/sounds/apert.wav";	  
	  public static final String SND_READY         = "/sounds/Ready.wav";
	  public static final String SND_LASER         = "/sounds/laser.wav";
	  public static final String SND_EXPLO1        = "/sounds/explo1.wav";
	  public static final String SND_EXPLO2        = "/sounds/explo2.wav";
	  
	  
	  //SndPlayer
	  private static WavAudio sndPlayer;
	  
	  public static final void startSndPlayer(){
	  	sndPlayer = new WavAudio();
	  }
	  
	  
	  // ************* SOUND **************
	  public static final void playsound(String wavfile) {
	  	if ( StarConfront.getgamevars(0) ) {
	  	System.out.println("Loading sound " + wavfile);
	  	sndPlayer.Stop();
	  	sndPlayer.WavFile(wavfile);
	  	sndPlayer.Play();
	  	}
	  }

	  public static final void ClosePlayer(){	  
	  	sndPlayer.ClosePlayer();
	  }
	  
	  // ************* IMAGE **************	  
	  public static final Image loadImage(String fn)
	  { 
	  	Image     img = null;
//    	System.out.println("Loading image " + fn);
	  	try {
	          // Load image	    	
	        img = Image.createImage(fn);
//	     	System.out.println("OK");
	  	}  
	      catch (Exception e) { 
		     System.out.println("Cannot make image from " + fn + " " + e.getMessage()); 
	    } // try
	    return img;      
	  }  // loadImage

	  public static final Image2D loadImage2D(String fn)
	  { 
	  	Image  img;
	  	Image2D img2D = null;  
//    	System.out.println("Loading 2D image " + fn);
	  	try {
            // Load image
	        img = Image.createImage(fn);
            img2D = new Image2D( Image2D.RGBA, img );
//            System.out.println("OK");
	  	}  
	      catch (Exception e) { 
		     System.out.println("Cannot make image from " + fn + " " + e.getMessage()); 
	    } // try
	    return img2D;      
	  }  // loadImage	  
	  

	  
	  public static final Texture2D loadTexture(String fn, boolean Transparence)
	  { 
	  	Texture2D text2d = null;
	  	Image2D   img2D = null;
	  	Image     img = null;
	    int size = 0;
    
	  	try {
	          // Load image	    	
//	    	System.out.println("Loading image " + fn);
	  		img = loadImage(fn);


             if (Transparence) 
               img2D = new Image2D( Image2D.RGBA, img );
             else
               img2D = new Image2D( Image2D.RGB, img );
             
	  	      text2d = new Texture2D(img2D);
	          
        }  
	      catch (Exception e) { 
		     System.out.println("Cannot make image from " + fn + " " + e.getMessage()); 
	    } // try

	    return text2d;      
	  }  // loadImage

	  
	  
	  
	  // ************* SORT **************
	  public static void sortint(int[] elements) {
	    quickSortint(elements, 0, elements.length - 1);
	  }
	  
      public static void sortstring(String[] elements) {
    	quickSortstr(elements, 0, elements.length - 1);
      }


      
      
    private static void quickSortstr(String[] s, int lo0, int hi0) {
	    	int lo = lo0;
	    	int hi = hi0;
	    	String mid;

	    	if (hi0 > lo0) {

	    	    /* Arbitrarily establishing partition element as the midpoint of
	    	     * the array.
	    	     */
	    	    mid = s[(lo0 + hi0) / 2 ].toUpperCase();

	    	    // loop through the array until indices cross
	    	    while(lo <= hi) {
	    		/* find the first element that is greater than or equal to
	    		 * the partition element starting from the left Index.
	    		 */
	    		while((lo < hi0) && (s[lo].toUpperCase().compareTo(mid) < 0)) {
	    		    ++lo;
	    		}

	    		/* find an element that is smaller than or equal to
	    		 * the partition element starting from the right Index.
	    		 */
	    		while((hi > lo0) && (s[hi].toUpperCase().compareTo(mid) > 0)) {
	    		    --hi;
	    		}

	    		// if the indexes have not crossed, swap
	    		if(lo <= hi) {
	    		    String temp;
	    		    temp = s[lo];
	    		    s[lo] = s[hi];
	    		    s[hi] = temp;
	    		    ++lo;
	    		    --hi;
	    		}
	    	    }

	    	    /* If the right index has not reached the left side of array
	    	     * must now sort the left partition.
	    	     */
	    	    if (lo0 < hi) {
	    		quickSortstr(s, lo0, hi);
	    	    }

	    	    /* If the left index has not reached the right side of array
	    	     * must now sort the right partition.
	    	     */
	    	    if (lo < hi0) {
	    		quickSortstr(s, lo, hi0);
	    	    }

	    	}
	        }


	        private static void quickSortint(int[] s, int lo0, int hi0) {
	        	int lo = lo0;
	        	int hi = hi0;
	        	int mid;

	        	if (hi0 > lo0) {

	        	    /* Arbitrarily establishing partition element as the midpoint of
	        	     * the array.
	        	     */
	        	    mid = s[(lo0 + hi0) / 2 ];

	        	    // loop through the array until indices cross
	        	    while(lo <= hi) {
	        		/* find the first element that is greater than or equal to
	        		 * the partition element starting from the left Index.
	        		 */
	        		while((lo < hi0) && (s[lo] < mid))  {
	        		    ++lo;
	        		}

	        		/* find an element that is smaller than or equal to
	        		 * the partition element starting from the right Index.
	        		 */
	        		while((hi > lo0) && (s[hi] > mid ) ) {
	        		    --hi;
	        		}

	        		// if the indexes have not crossed, swap
	        		if(lo <= hi) {
	        		    int temp;
	        		    temp = s[lo];
	        		    s[lo] = s[hi];
	        		    s[hi] = temp;
	        		    ++lo;
	        		    --hi;
	        		}
	        	    }

	        	    /* If the right index has not reached the left side of array
	        	     * must now sort the left partition.
	        	     */
	        	    if (lo0 < hi) {
	        		quickSortint(s, lo0, hi);
	        	    }

	        	    /* If the left index has not reached the right side of array
	        	     * must now sort the right partition.
	        	     */
	        	    if (lo < hi0) {
	        		quickSortint(s, lo, hi0);
	        	    }

	        	}
	            }

	  
 	  
}
