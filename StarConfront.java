
import java.util.*;
import java.util.Timer;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;


/**
 * @author  master
 */
public class StarConfront extends MIDlet 
{
private static final int PERIOD = 50;   // in ms

private static Timer timer;
private GameDrawCanvas gameCanvas = null;
public int shipfire   = 0;
public int shipshield = 0;
public int shipspeed  =  0;
private static Display gamedisplay;


public static boolean gamesound = true;
public static boolean gamemusic = true;
public static boolean gamevibrate  =  true;


public void setshipvars(int FIRE, int SHIELD, int SPEED){
	shipfire = FIRE;
	shipshield = SHIELD;
	shipspeed  =  SPEED;
}


public static void setgamevars(int type, boolean Value ){
	switch(type) {
	
	case 0: 
		gamemusic = Value;
		if ( Value ) 
			MidiAudio.playSound(MidiAudio.SND_2001);
		else
			MidiAudio.stopSound(MidiAudio.SND_2001);
	break;
	
	case 1: 
		gamesound = Value;
	break;
	
	case 2: 
		gamevibrate  =  Value;
  		vibrate(0, 0, 5);
	break;
   }
}

public static boolean getgamevars(int type){

boolean returnvalue = true;	
	switch(type) {
	
	case 0: 
		returnvalue = gamemusic;
	break;
	
	case 1: 
		returnvalue = gamesound;
	break;
	
	case 2: 
		returnvalue =  gamevibrate;
	break;		
   } 
	return returnvalue;
}


public int getshipspeed(){
	return shipspeed;
}



private MenuCanvas menuCanvas = null;

public static void fsleep(long time )
{
try {
	Thread.sleep(time);
   	}
   catch (Exception e) 
   {  
   	e.printStackTrace();
   	System.out.println(e.getMessage());
   }
}



public void presentention()
{
	MidiAudio.playSound(MidiAudio.SND_2001);
    menuCanvas = new MenuCanvas(this);
    
    gamedisplay = Display.getDisplay(this);
    
    menuCanvas.setstate(1);
    Display.getDisplay(this).setCurrent( menuCanvas );
    menuCanvas.startvars(); 
	
    fsleep(120);
  //  fsleep(12000);
    menuCanvas.setstate(2);
}


public static void vibrate(final int onInterval, final int offInterval, final int repeat)
{
  if ( gamevibrate = false ) return;
  new Thread(new Runnable() {
    public void run()
    {
      for (int i = 0; i < repeat; i++)
      {
      	gamedisplay.vibrate(onInterval);
        try
        {
          Thread.sleep(onInterval);
        } catch (InterruptedException e) {}
        if (i < repeat - 1)
        {
        	gamedisplay.vibrate(0);
          try
          {
            Thread.sleep(offInterval);
          } catch (InterruptedException e) {}
        }
      }
      gamedisplay.vibrate(0);
    }
  },"Bzzzer").start();
}	  



public void startApp() 
{
    mzlib.startSndPlayer();
    presentention();
}


public void pauseApp() {}

public void destroyApp(boolean unconditional) {}

public void finishGame()
{ timer.cancel();    // stop the timer
 notifyDestroyed();  
}


public void playgame()
{
	timer = new Timer();  

	MidiAudio.stopSound(MidiAudio.SND_2001);
    gameCanvas = new GameDrawCanvas(this);    
	Display.getDisplay(this).setCurrent( gameCanvas );
	
	gameCanvas.SetShipValues(shipfire, shipshield, shipspeed);

	
	
	
    timer.schedule( new GunnerTimer(), 0, PERIOD);   // tick every PERIOD ms
    if ( gamemusic ) MidiAudio.playSound(MidiAudio.SND_ROOTS);    
}  



// -------------------------------------------

class GunnerTimer extends TimerTask 
{
 public void run() 
 { if(gameCanvas != null)
 	gameCanvas.update();   // update the scene
 }
}

} // end of GunnerM3G class