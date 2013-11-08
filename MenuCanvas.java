
/*
 * Created on 24/09/2005
 *
 * Draw game menu 
 * 
 */

/**
 * @author Zion Corp.
 *
 * 
 * 
 */

import javax.microedition.lcdui.*;


/**
 * @author  master
 */
public class MenuCanvas extends Canvas implements CommandListener
{
 private Command exitCmd;
 private Image presententionImage;
 public Image ShipSelectionImage; 
 private StarConfront top;
 public Graphics menuGraphics;
 private int currentstate = 1;
 private int menuselected = 0;
 private int optionsselected = 0;
  
 //Ship selection
 private int shiptype = 0;
 private String civilization;
 public int SPEED  = 0;	  
 public int FIRE   = 0;
 public int SHIELD = 0;

 //Menu selection
 private  WavAudio sndPlayer; 
 

 public void setstate(int gamestate){
 	
 	currentstate = gamestate;
 	repaint(); 	
 }
 
 private void  ChangeShip(boolean next)
 {
 	if ( next ) {
 	  shiptype += 1;
 	} else {
	  shiptype -= 1;
 	}
 	if (shiptype > mzlib.MAX_PLANET) shiptype = 0;
 	if (shiptype == -1) shiptype = mzlib.MAX_PLANET;
 }
 
  

  // ************* SOUND **************
  
  private void sndButton ()
  {
  	if ( StarConfront.getgamevars(1) ) {
  	sndPlayer.Stop();
  	sndPlayer.Play();
    System.out.println("play");
  	}
  }
 

  public void startvars() {
  	sndPlayer = new WavAudio();  	
 	sndPlayer.WavFile(mzlib.SND_BUTTON);
  }
  
  
private void   settopvars(){
	top.setshipvars(FIRE, SHIELD, SPEED);
  }
  

  
private void releasedKey(int gameAction)
{
	if ( (gameAction != LEFT) &&
		 (gameAction != RIGHT)  &&
		 (gameAction != UP)  &&
		 (gameAction != DOWN) ) 	 
	{	
		switch(currentstate) {
	 	case 2:
	 		sndButton();
	 		setstate(3);
	 		break;
	 	case 3: 
	 		sndButton();
	 		if ( menuselected == 0 ) setstate(4);
	 		if ( menuselected == 2 ) setstate(5);	 		
	 		break;
	 	case 4: 
	 	    System.out.println("clo");
	 	  	sndPlayer.ClosePlayer();
	 	  	settopvars();
	 		top.playgame();
	 		break;
	 	case 5:
	 		sndButton();
	 		if ( optionsselected == 3 )  setstate(3);
	 		break;
		}		
	}

	if (currentstate == 4 ) {
	  if (gameAction == LEFT) ChangeShip(false);	
	  if (gameAction == RIGHT) ChangeShip(true);
	  repaint();
	  sndButton();
	}

  	if (currentstate == 3 ) {
   	  if (gameAction == DOWN) menuselected += 1;	
	  if (gameAction == UP ) menuselected -= 1;
		  
	  if ( menuselected == -1 ) menuselected = 0;
	  if ( menuselected == 3 ) menuselected = 2;
		  
	  repaint();
	  sndButton();
   	}

	if (currentstate == 5 ) {
      if (  ( gameAction == LEFT ) || ( gameAction == RIGHT )   ) {
         System.out.println(optionsselected+" > "+StarConfront.getgamevars( optionsselected ));
      	 StarConfront.setgamevars(optionsselected, !( StarConfront.getgamevars( optionsselected ) ) );
         System.out.println(optionsselected+" > "+StarConfront.getgamevars( optionsselected ));
      }   
		
	    if (gameAction == DOWN) optionsselected += 1;	
	    if (gameAction == UP ) optionsselected -= 1;
      
	  
	  if ( optionsselected == -1 ) optionsselected = 0;
	  if ( optionsselected == 4 ) optionsselected = 3;
      repaint();
	  sndButton();
	}	
		
}

private Image getplanet() {
	Image img = null;
	switch (shiptype) {
	case 0: 
		img = mzlib.loadImage(mzlib.PLANET1);
		break;
	case 1: 
		img = mzlib.loadImage(mzlib.PLANET2);
		break;
	case 2: 
		img = mzlib.loadImage(mzlib.PLANET3);
		break;

	}
	

	return img;
}

private Image getship() {
	Image img = null;

	switch (shiptype) {
	case 0: 
		img = mzlib.loadImage(mzlib.SHIP1);
		break;
	case 1: 
		img = mzlib.loadImage(mzlib.SHIP2);
		break;
	case 2: 
		img = mzlib.loadImage(mzlib.SHIP3);
		break;

	}
	return img;	
}

private void gettext() {
	
	switch (shiptype) {
	case 0: 
		civilization = mzlib.CIVILIZATION1;
		SPEED        = 1;	  
		FIRE         = 2;
		SHIELD       = 1;
		break;
	case 1: 
		civilization = mzlib.CIVILIZATION2;
		SPEED       = 2;	  
		FIRE        = 1;
		SHIELD      = 1;
		break;
    case 2: 
     	civilization = mzlib.CIVILIZATION3;
     	SPEED       = 1;	  
     	FIRE        = 1;
     	SHIELD      = 2;
	break;
}
	
}


private void painter(Graphics g)
{
// TODO funcao painter
	if ( g!= null ) {
		switch(currentstate) {
	     case 1:
	     	g.drawImage(mzlib.loadImage(mzlib.IMG_SNLOGO) , 0, 0, Graphics.TOP|Graphics.LEFT);
		 	break;
		 case 2 :  
		    g.drawImage(mzlib.loadImage(mzlib.IMG_LOGO) , 0, 0, Graphics.TOP|Graphics.LEFT);
		    break;
		 case 3 :  
		    g.drawImage(mzlib.loadImage(mzlib.IMG_LOGOMENU) , 0, 0, Graphics.TOP|Graphics.LEFT);
            g.setColor(0x0000FF);  // Blue
            g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.drawString("MENU", 50, 22, Graphics.TOP|Graphics.LEFT);
            
            String str1 = " ";
            String str2 = " ";            
            if ( menuselected == 0 ) {  
               g.setColor(0xFFFF00);  // blue
               str1 = ">";
               str2 = "<";            
            }   
            else {
                str1 = " ";
                str2 = " ";            
            	g.setColor(0xFFFFFF);  // white
            }
            
            g.drawString(str1+" Single Player "+str2, 20, 40, Graphics.TOP|Graphics.LEFT);

            if ( menuselected == 1 ) {  
                g.setColor(0xFFFF00);  // blue
                str1 = ">";
                str2 = "<";            
             }   
             else {
                 str1 = " ";
                 str2 = " ";            
             	g.setColor(0xFFFFFF);  // white
             }
            g.drawString(str1+" Multi Player "+str2, 20, 60, Graphics.TOP|Graphics.LEFT);
            

            if ( menuselected == 2 ) {  
                g.setColor(0xFFFF00);  // blue
                str1 = ">";
                str2 = "<";            
             }   
             else {
                 str1 = " ";
                 str2 = " ";            
             	g.setColor(0xFFFFFF);  // white
             }
            g.drawString(str1+" Options "+str2, 20, 80, Graphics.TOP|Graphics.LEFT);
            
		    break;
		 case 4 :  
		    g.drawImage( mzlib.loadImage(mzlib.IMG_SHIPSELECT) , 0, 0, Graphics.TOP|Graphics.LEFT);
		    g.drawImage( getship() , 20, 10, Graphics.TOP|Graphics.LEFT);
		    g.drawImage( getplanet() , 93, 80, Graphics.TOP|Graphics.LEFT);
	    
		    // PROPERTS
		    gettext();
		    

		    // CIVILIZATION 
            g.setColor(0xFF0000);  // red
            g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.drawString(civilization , 1, 1, Graphics.TOP|Graphics.LEFT);
		    
		    // SPEED 
            g.setColor(0x8888FF);  // WHITE
            g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL));
            g.drawString("SPEED +"+SPEED , 1, 77, Graphics.TOP|Graphics.LEFT);

		    // FIRE 
            g.drawString("FIRE +"+FIRE , 1, 87, Graphics.TOP|Graphics.LEFT);
            
		    // SHIELD 
            g.drawString("SHIELD +"+SHIELD , 1, 97, Graphics.TOP|Graphics.LEFT);
            break;
            
		 case 5 :  
		    g.drawImage(mzlib.loadImage(mzlib.IMG_LOGOMENU) , 0, 0, Graphics.TOP|Graphics.LEFT);
            g.setColor(0x0000FF);  // Blue
            g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
            g.drawString("OPTIONS", 50, 22, Graphics.TOP|Graphics.LEFT);
            
            String str = "ON";
            g.setColor(0xFFFFFF); 
            

            if ( optionsselected == 0 )   
                g.setColor(0xFFFF00);  // blue
             else 
             	g.setColor(0xFFFFFF);  // white

             if ( StarConfront.getgamevars(0) ) 
            	str = "ON";
             else
            	str = "OFF";
            g.drawString("Music "+str, 20, 40, Graphics.TOP|Graphics.LEFT);
            
            if ( optionsselected == 1 )   
                g.setColor(0xFFFF00);  // blue
             else 
             	g.setColor(0xFFFFFF);  // white
            
            if ( StarConfront.getgamevars(1) ) 
            	str = "ON";
            else
            	str = "OFF";
            g.drawString("Sound "+str, 20, 55, Graphics.TOP|Graphics.LEFT);
            

            if ( optionsselected == 2 )   
                g.setColor(0xFFFF00);  // blue
             else 
             	g.setColor(0xFFFFFF);  // white
            if ( StarConfront.getgamevars(2) ) 
            	str = "ON";
            else
            	str = "OFF";
            g.drawString("Vibrate "+str, 20, 70, Graphics.TOP|Graphics.LEFT);

            if ( optionsselected == 3 )   
                g.setColor(0xFFFF00);  // blue
             else 
             	g.setColor(0xFF0000);  // red
            g.drawString("Return", 20, 90, Graphics.TOP|Graphics.LEFT);            
         break;
	   }
	}
}


protected void keyReleased(int keyCode)
{
  int gameAction = getGameAction(keyCode);
  releasedKey(gameAction);
}
	

protected void paint(Graphics g)
{
// TODO funcao painter
	if ( g!= null ) {
		painter(g);
    //   g.drawImage(presententionImage, 0, 0, Graphics.TOP|Graphics.LEFT);
       System.out.println("paint menu = "+currentstate);
	}
}
	
  public MenuCanvas(StarConfront top) 
  { 
    this.top = top;
 }  

	  
  public void commandAction(Command command, Displayable disp)
  { 
    if (command == exitCmd) { // finish
      top.finishGame();
    }
 } // end of commandAction()
	  

    
}