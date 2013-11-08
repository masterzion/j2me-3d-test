

import java.util.Random;
import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;



/**
 * @author  master
 */
public class GameDrawCanvas extends Canvas implements CommandListener
{
  private static boolean isMoveable = false;

  private Command exitCmd; 
  private static final int STEP = 1;
 
  private Graphics3D iG3D;
  private static Background backGnd;
  
  private StarConfront top;
  public static World scene;
  public static ShipCamera mobCam;
  private Image cockpitImage;
  private int gundamage = 1;
  private int shieldpower = 1;
  private int shipspeed   = 1;
  
  public static int Fuel       = 999;
  public static int Life       = 999;  
  public static int Fire       = 999;  
  
  public static FireManager shotMng;

//  private Image hitIm;    // hit image
  private int xHit;       // x-coord of hit image on screen

  private Random rnd;     // for selecting the background and floor images

 
  private Image2D backIm = null;
  public static int width;
  public static int height;
  
  public void SetShipValues(int shipfire, int shipshield, int shipspeed) {
  	gundamage = shipfire;
  	shieldpower = shipfire;  	
  	
  }
  
  

  public GameDrawCanvas(StarConfront top) 
  { 
    this.top = top;

    exitCmd = new Command("Exit", Command.EXIT, 0);
    addCommand(exitCmd);
    setCommandListener(this);
    
//    loadHitImage(); 
    rnd = new Random();

    iG3D = Graphics3D.getInstance();
    scene = new World();   // create scene graph
    buildScene();

    // PlayReady
  	if ( StarConfront.getgamevars(1) ) {
      mzlib.playsound(mzlib.SND_READY);
  	}
  }  // end of GunnerCanvas()


  
  public void commandAction(Command command, Displayable disp)
  { 
    if (command == exitCmd) { // finish
      shotMng.closeSounds();
      top.finishGame();
    }
    else   // execution should not get here
      System.out.println("Command Action Error: " + command);
  } // end of commandAction()


  private void buildScene()
  // add elements to the scene
  {
  	addCamera();
    addLights();
    addBackground();
    addModel();
    //addMap();    

 	
    shotMng = new FireManager(scene, mobCam); // add explosions stuff
  } // end of buildScene()

  private void addMap(){
  	MapLoader map = new MapLoader();
  	map.readFile(1);
  	map.CreateMap();
  	scene.addChild( map.getobjectmap() );
  }
  
  private void addResource(int ResourceType, float x, float y, float z, ResourceList list, int ID){
  	int[] nobjects = new int[1];
  	Group modelGroup = null;
  	Resource res = list.getResource(ResourceType);

    modelGroup = Load3DFile.getMeshFile(res, x, y, z, ID);  	
  	scene.addChild(modelGroup);  	
  }
  
  
  private void addModel()
  {
  	ResourceList list = new ResourceList();
  	
  	//           SHIP           POS      LIST  ID
  	addResource(list.ENEMYSHIP,       0, 0, -10, list, 1);
  	addResource(list.ENEMYSHIP,       -10, 0, -20, list, 2);
  	addResource(list.ENEMYSHIP,       -20, 0, -30, list, 3);
  	addResource(list.ENEMYSHIP,       -30, 0, -40, list, 4);  	
//  	addResource(list.HUNTERSHIP,       0, 0, -8, list, 3);  	
  	
  }  // end of addModel()

/*
  private Image getCockpitImage(){  
  Image source; // the image to be copied
  source = Image.createImage(...);
  Image copy = Image.createImage(source.getWidth(), source.getHeight());
  Graphics g = copy.getGraphics();
  g.drawImage(source, 0, 0, TOP|LEFT);
  return copy;
  }
  */
  
  private void addCamera()
  // the camera is a MobileGunCamera object
  {
  	height = getHeight();
  	width = getWidth();
    mobCam = new ShipCamera( getWidth(), getHeight());
    ShipCamera.MOVE_INCR = (ShipCamera.MOVE_INCR * top.getshipspeed() ); 
    scene.addChild( mobCam.getCameraGroup() );
    scene.setActiveCamera( mobCam.getCamera() );
    cockpitImage = mzlib.loadImage(mzlib.COCKPIT_IMAGE);

  }  // end of addCamera()


  private void addLights()
  {
    Light light = new Light();  // default white, directional light
    light.setIntensity(1.25f);  // make it a bit brighter
    light.setOrientation(-45.0f, 1.0f, 0, 0); // pointing down and into scene
    scene.addChild(light);
  }  // end of addLights()


  private void addBackground()
  /* Use a background image. The choice of image
     is made at random from 3 possibilities.
  */
  {
  	backGnd = new Background();

    backIm = mzlib.loadImage2D(mzlib.BG_LEVEL1);
   

    if (backIm != null) {
    	 backGnd.setImage( backIm );
 
    	 backGnd.setImageMode(Background.REPEAT, Background.REPEAT);
    	 backGnd.setCrop(backGnd.getCropX(), backGnd.getCropY(), width, height); 
         isMoveable = true;
    	}
    	else {
    	 backGnd.setColor(0x00bffe); // a light blue background
    	}
    scene.setBackground(backGnd);
  } // end of addBackground()



  // ------------ process key presses and releases ------------

  protected void keyPressed(int keyCode)
  { 
    int gameAction = getGameAction(keyCode);
    if (gameAction == Canvas.FIRE) {
    	if ( ( ShipCamera.blive ) && ( Fire > 0 ) ) {
           Fire -= 1;
           shotMng.shoot(gundamage);
    	}   
    }  
    else
      mobCam.pressedKey(gameAction);
  } // end of keyPressed()


  protected void keyReleased(int keyCode)
  { int gameAction = getGameAction(keyCode);
    mobCam.releasedKey(gameAction);
  }


  // ------------------ update and paint the canvas ------------------

  public void update()
  // called by the timer task in GunnerM3G
  {
    mobCam.update();    // update the camera
    shotMng.update();   // update the shot manager

    repaint();
  }  // end of update()


  protected void paint(Graphics g)
  {
    iG3D.bindTarget(g);
    try {
      iG3D.render(scene);
    }
    catch(Exception e)
    { e.printStackTrace();  }
    finally {
      iG3D.releaseTarget();
    }
    if ( g != null ) { 
    	
    	if ( ShipCamera.blive  ) {
    	   g.drawImage(cockpitImage, 0, 0, Graphics.TOP|Graphics.LEFT);
           g.setColor(0xFF0000);  // red

           g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
           g.drawString( String.valueOf(Fuel) , 69,65, Graphics.TOP|Graphics.LEFT);
           g.drawString( String.valueOf(Life) , 69,76, Graphics.TOP|Graphics.LEFT);
           g.drawString( String.valueOf(Fire) , 69,87, Graphics.TOP|Graphics.LEFT);
           
           g.setColor(0xFFFF00);  // yellow
           g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
           g.drawString( "F" , 62,65, Graphics.TOP|Graphics.LEFT);
           g.drawString( "L" , 62,76, Graphics.TOP|Graphics.LEFT);
           g.drawString( "S" , 62,87, Graphics.TOP|Graphics.LEFT);
           
    	}
        if ( shotMng.hitMade() ) {
//      g.drawImage( hitIm, xHit, 10, Graphics.TOP|Graphics.LEFT);

             if ( shotMng.getEnemyDestroyed() ) {
                  g.setColor(0xFF0000);  // red
                  g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE));
                  g.drawString( shotMng.getEnemyName() + " destroyed!" , 5,5, Graphics.TOP|Graphics.LEFT);
             }	
         }     
    }
  } // end of paint()
  
  // Move background functions
  public static void moveRight(int factor)
  {
  try {
    if (isMoveable)  backGnd.setCrop(backGnd.getCropX()-(STEP*factor), backGnd.getCropY(), width, height);
    scene.setBackground(backGnd);    
  }  
  catch (Exception e) { 
    System.out.println("error " + e.getMessage()); 
   } // try
  }
  
  public static void moveLeft(int factor)
  {
  try {	
  	if (isMoveable) backGnd.setCrop(backGnd.getCropX()+(STEP*factor), backGnd.getCropY(), width, height);
    scene.setBackground(backGnd);
  }
  	catch (Exception e) { 
        System.out.println("error " + e.getMessage()); 
       } // try
  }
  
  public static void moveUp(int factor)
  {
  try {	
  	if (isMoveable)  backGnd.setCrop(backGnd.getCropX(),backGnd.getCropY()+(STEP*factor), width, height);
    scene.setBackground(backGnd);
  }
  	catch (Exception e) { 
        System.out.println("error " + e.getMessage()); 
       } // try
  }
  
  public static void moveDown(int factor)
  {
  try {	
  	if (isMoveable) backGnd.setCrop(backGnd.getCropX(), backGnd.getCropY()-(STEP*factor), width, height);
    scene.setBackground(backGnd);  	
  }
  catch (Exception e) { 
    System.out.println("error " + e.getMessage()); 
   } // try
  }  


  
  // ---------------image loading -----------------------------
  /*  private void loadHitImage()
    {
      hitIm = null;
      xHit = 0;
      try {
        hitIm = Image.createImage(mzlib.IMG_HIT);
      }
      catch (Exception e)
      { System.out.println("Hit image not found"); }

      if (hitIm != null)
        xHit = (getWidth()/2) - (hitIm.getWidth()/2);
    } // end of loadHitImage;
  */
  
  
}  // end of GunnerCanvas class

