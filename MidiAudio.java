//Copyright (c) 2005 Sony Ericsson Mobile Communications AB
//
// This software is provided "AS IS," without a warranty of any kind. 
// ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, 
// INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A 
// PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. 
//
// THIS SOFTWARE IS COMPLEMENTARY OF JAYWAY AB (www.jayway.se)


import java.io.IOException;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import java.io.InputStream;

public class MidiAudio
{
	  // Resource (audio) names
	  protected static final String[] RESOURCE_MAP = {
	    "2001.mid",
	  	"Nightwish.mid",
	    "Roots.mid"  		
	  };
	  
	  /**
	   * Returns specified resource as stream
	   * @param resourceId  The id of the resource.
	   * @return            An inputstream to the resource.
	   */
	  public static InputStream getResource(int resourceId)
	  {
	    return MidiAudio.class.getResourceAsStream("/bgmusic/" + RESOURCE_MAP[resourceId]);
	  }
	
  /** Theplayers */
  protected  static Player[] m_sounds = new Player[3];

  /** Current playing player index */
  protected  static int m_currentPlayer = -1;
  
  protected  static final String TYPE_AMR = "audio/amr";
  protected  static final String TYPE_MIDI = "audio/midi";
  
  
  public static final int SND_2001             = 0;
  public static final int SND_NIGHTWISH        = 1;
  public static final int SND_ROOTS            = 2;  
  public static final int AUDIO_OFF		       = 5;  

  
  
  public static synchronized void playSound(int snd)
  {
    // Start sound
      // No player, create one
  	if ( StarConfront.getgamevars(0) ) { 
  	if (snd != AUDIO_OFF) {
  		
      if (m_sounds[snd] == null)
      {
        createSound(snd);
      }
      
      // Start player
      Player player = m_sounds[snd];
      if (player != null)
      {
        try
        {
          player.start();
          m_currentPlayer = snd;
        }
        catch (MediaException e)
        {
          e.printStackTrace();
        }
      }
   	}
  	}
  }
  
  /**
   * Stops specified sound if it is playing.
   * @param snd		The id of the sound to stop.
   */
  public static synchronized void stopSound(int snd)
  {
    if (m_sounds[snd] != null)
    {
      try
      {
        m_sounds[snd].stop();
      }
      catch (MediaException e)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Stops all sounds and cleans up resources.
   */
  public void shutdown()
  {
    for (int i = 0; i < m_sounds.length; i++)
    {
      stopSound(i);
      if (m_sounds[i] != null)
      {
        m_sounds[i].deallocate();
      }
    }
  }
  
  /**
   * Creates a player for specified sound
   * and popuplates the Player array.
   * @param snd	The sound to create
   */
  protected static void createSound(int snd)
  {
    try
    {
      int rsc = 0;
      String type = TYPE_AMR;
      type = TYPE_MIDI;
      rsc = snd;

      
       m_sounds[snd] = Manager.createPlayer(getResource(rsc), type);
       m_sounds[snd].setLoopCount(-1);		// loop intro tune for ever and ever

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (MediaException e)
    {
      e.printStackTrace();
    }
  }

  /** Prevent instantiation */
  private MidiAudio() {}
}