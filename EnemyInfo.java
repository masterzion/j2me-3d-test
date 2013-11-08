
/**
 * @author  master
 */
public class EnemyInfo
{
  
  private String name;
  private int id;
  private int numLives;
  
  public EnemyInfo(String nm, int id, int lives)
  { name = nm;
    this.id = id;
    numLives = lives;
  }

  
  /**
 * @return  Returns the name.
 * @uml.property  name="name"
 */
public String getName()
  { return name;  }

  public int getID()
  { return id;  }

  /**
 * @return  Returns the numLives.
 * @uml.property  name="numLives"
 */
public int getNumLives()
  {  return numLives;  }

  /**
 * @param numLives  The numLives to set.
 * @uml.property  name="numLives"
 */
public int setNumLives( int lives )
  {  return numLives = lives;  }

  
  public boolean loseALife()
  {
    if (numLives > 0) {
      numLives--;
      return true;
    }
    else  // can't set to negative
      return false;
  } // end of loseALife()

} 