import javax.microedition.m3g.*;



/**
 * @author  master
 */
public class FireManager
{
  // max value for the fired shot counter
  private static final int FIRED_SHOT_MAX = 10;

  private World scene;
  private ShipCamera mobCam;
  public AnimatedSquare exploBoard;   // the animated explosions billboard

  // manages the fired shot and explosions sounds

  // Counter to control how long the fired shot image is displayed
  private int firedShotCounter = 0;
  private boolean bEnemyDestroyed = false;
  private String EnemyName = "";  
  private WavAudio shotsnd;
  private WavAudio explosionsnd;
  private WavAudio explosionsnd2; 
  
  private int hitEnemyID = -1;   // ID of last Enemy hit 
  
  public void ShipColision(float x, float y, float z){
  	 exploBoard.setPosition(x, y, z );
     exploBoard.setVisible(true);
     exploBoard.update();
     
     ShipCamera.MOVE_INCR = 0;

     exploBoard.setPosition(x,y,z);
     exploBoard.setVisible(true);
     bEnemyDestroyed = true;
     if ( StarConfront.getgamevars(1) ) {
       explosionsnd.Play();
     }
     
     
 }
  
  
  public FireManager(World s, ShipCamera mc)
  {
    scene = s;
    mobCam = mc;
    Image2D  TextureImage = null;

    // set up animated explosions billboard
    System.out.println("Adding explosion billboard");
    Image2D[] ims = loadImages("/explosion/", mzlib.EXPLOSION_SIZE);
    Group camGroup = mobCam.getCameraGroup();
    exploBoard = new AnimatedSquare(ims, camGroup, 1.5f);
    scene.addChild( exploBoard.getAnimBillboardMesh() );
    
    
    shotsnd = new WavAudio();
    shotsnd.WavFile(mzlib.SND_LASER);

    explosionsnd = new WavAudio();
    explosionsnd.WavFile(mzlib.SND_EXPLO1);    

    explosionsnd2 = new WavAudio();
    explosionsnd2.WavFile(mzlib.SND_EXPLO2);    
    
  }  // end of ShotManager()


  public void update()
  /* Update the explosion display, and decrement the fired shot
     counter. When the fired shot counter reaches 0,
     make the image invisible. */
  { 
    exploBoard.update(); 

    if (firedShotCounter == 0)     // decide whether to hide the fired shot image
      mobCam.setShotVisible(false);   // hide it
    else
      firedShotCounter--;   // keep counting down
  } // end of update()


  // --------------- Explosion -----------------------------
  private Image2D[] loadImages(String path, int num)
  // load files called path + 0-(num-1) + ".gif"
  {
  	Image2D[] ims = new Image2D[num];
    for (int i=0; i<num; i++)
      ims[i] = mzlib.loadImage2D(path + i + ".png");
    return ims;
  }  // end of loadImages()
  

  // ------------------- shoot the gun -----------------

  public int shoot(int damage)
  {
    // show fired shot image and play a sound
    firedShotCounter = FIRED_SHOT_MAX;
    mobCam.setShotVisible(true);
    

    // initialise sound resources for shooting
  	if ( StarConfront.getgamevars(1) ) {
      shotsnd.Stop();
      shotsnd.Play();
  	}

    // fire a picking ray out in the camera's forward direction
    RayIntersection ri = new RayIntersection();
    float[] camPos = mobCam.getPosition();   // camera's current position
    float[] camDir = mobCam.getDirection();  // camera's current fwd direction

    bEnemyDestroyed = false;    
    
    /*
    if (scene.pick(-1, camPos[0], camPos[1], camPos[2],
            camDir[0], camDir[1], camDir[2], ri)) 
    */

    scene.pick(-1, camPos[0], camPos[1], camPos[2], camDir[0]-1, camDir[1], camDir[2], ri);
    //GameDrawCanvas.scene.pick(-1, (GameDrawCanvas.width/2), (GameDrawCanvas.height/2), GameDrawCanvas.scene.getActiveCamera(), ri);
    
    //scene.pick(-1,(GameDrawCanvas.width/2), (GameDrawCanvas.height/2), scene.getActiveCamera(), ri);
//    System.out.println("fire check "+GameDrawCanvas.width/2+"x"+GameDrawCanvas.height/2+" = "+(ri.getIntersected() != null));
    
	
    if (ri.getIntersected() != null)  {   // hit something?
      float distance = ri.getDistance();  // normalized distance to the hit thing
      checkHit(ri, damage);

      // calculate the coords of the hit thing
      float x = camPos[0] + (camDir[0] * distance);
      float y = camPos[1] + (camDir[1] * distance);
      float z = camPos[2] + (camDir[2] * distance);

      // show explosion at (x,y,z) and play a sound
      
      exploBoard.setPosition(x,y,z);
      exploBoard.setVisible(true);
      bEnemyDestroyed = true;
      // initialise sound resources for shooting

    }
    return hitEnemyID;
  }  // end of shootGun()


  private void checkHit(RayIntersection ri, int damage)
  // find out what's been hit
  {
     hitEnemyID = -1;   // reset to default value
     Node selected = ri.getIntersected();
     if (selected instanceof Mesh) {
       // System.out.println("Hit a mesh");
       Mesh m = (Mesh) selected;
       m.animate(0);
       EnemyInfo pInfo = (EnemyInfo) m.getUserObject();
	   System.out.println("target" +pInfo.getNumLives());
       if (pInfo != null) {
         hitEnemyID = pInfo.getID();  // save ID
         pInfo.setNumLives(pInfo.getNumLives() - damage);
         hitEnemy(pInfo, m, damage);
       }
     }
  } // end of checkHit()


  private void hitEnemy(EnemyInfo pInfo, Mesh m, int damage)
  {
    if (pInfo.getNumLives() < 1) {    // last life just gone
      mzlib.EXPLOSION_SIZE = mzlib.MAX_EXPLOSION_SIZE;
  	  EnemyName = pInfo.getName();
      System.out.println("Killed " + pInfo.getName() + "; ID: " + hitEnemyID);
      m.setRenderingEnable(false);
      m.setPickingEnable(false);
      if ( StarConfront.getgamevars(1) ) {
        explosionsnd.Play();
      }  
    }
    else {
  	  mzlib.EXPLOSION_SIZE = mzlib.EXPLOSION;
      System.out.println("Hit " + pInfo.getName() +"; ID: " + hitEnemyID + "; lives left: " + pInfo.getNumLives());
      pInfo.loseALife();
      if ( StarConfront.getgamevars(1) ) {
        explosionsnd2.Play();      
      }
    }
  }  



  public boolean hitMade()
  // a hit has occurred if the explosion is visible
  {  return exploBoard.isVisible();  }


  public int getHitID()
  {  return hitEnemyID; }

  public boolean getEnemyDestroyed()
  {  return bEnemyDestroyed; }
 
  /**
 * @return  Returns the enemyName.
 * @uml.property  name="enemyName"
 */
public String getEnemyName()
  {  return EnemyName; }
  
  // ------------------ sound ops ---------------------


  public void closeSounds()
  { 
  	explosionsnd.ClosePlayer();
    shotsnd.ClosePlayer();
    mzlib.ClosePlayer();
  }

}  // end of ShotManager class