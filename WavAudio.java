import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;

/*
 * Created on 16/09/2005
 *
 * Wave Play Unit
 * 
 */

/**
 * @author Zion Corp.
 *
 *  Play every game sound
 * 
 */
public class WavAudio implements PlayerListener
{
	private Player comumplayer;
	/*******************************************/
	
	 // PlayReady
	  public void WavFile(String snd )
	  {
	  	comumplayer = loadSound(snd);
	  } // end of playShotSound()
	  
	  public void ClosePlayer(){
	  	comumplayer.close();
	  }

	  public void Play(){	  
	  	playSound(comumplayer);
	  }
	  
	  public void Stop(){	  
	  	stopSound(comumplayer);
	  }

	  private void stopSound(Player p)
		  {
		    if (p != null) {
		      if (p.getState() == Player.STARTED) {
			    try{
	                p.setMediaTime(0);
    		    	p.stop();
			    }
		        catch(Exception ex)
		        { System.out.println("Could not play sound ");  }
		      }
		    }
		  }  // end of playSound()

	  private void playSound(Player p)
	  // Only start a player if it's in a PREFETECHED state.
	  {
	    if (p != null) {
	      if (p.getState() == Player.PREFETCHED) {
		    try{
              p.setMediaTime(0);    // rewind (a safety precaution)
	          p.start();
		    }
	        catch(Exception ex)
	        { System.out.println("Could not play sound ");  }
	      }
	    }
	  }  // end of playSound()

	  
	  	  private Player[] loadSounds(String fn)
	  // load num copies of sound in fn
	  {
	    Player[] ps = new Player[1];
	      ps[0] = loadSound(fn);
	    return ps;
	  }  // end of loadSounds()

	  private Player loadSound(String fn)
	  {
	    Player p = null;
		try{
	      InputStream in = getClass().getResourceAsStream(fn);
	      p = Manager.createPlayer(in,"audio/x-wav");
	      p.realize();
	      p.prefetch();    // move player to PREFETECHED state
	      p.addPlayerListener(this);
		}
	    catch(Exception ex)
	    { System.out.println("Could not load sound in " + fn);  }

	    return p;
	  } // end of loadSound()
	  
	  public void playerUpdate(Player p, String event, Object eventData)
	  // reset the player, ready for its next use
	  {
		if (event == END_OF_MEDIA) {
	      try {
	        p.stop();           // back to PREFETECHED state
			p.setMediaTime(0);  // rewind to the beginning
	      }
	       catch(Exception ex) {}
	    }
	  }  // end of playerUpdate()
	  
	
}
