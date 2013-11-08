import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;

/**
 * @author  master
 */
public class ShipCamera
{
// initial camera position
private static final float X_ANGLE = -10.0f;
private static final float X_POS = 0.0f; 
private static final float Y_POS = 0.4f; 
private static final float Z_POS = 2.0f; 

// translation and rotation increments
private static final float ANGLE_INCR = 5.0f;   // degrees
public static float MOVE_INCR = 0.01f;

// booleans for remembering key presses
private boolean upPressed = false;
private boolean downPressed = false;
private boolean leftPressed = false;
private boolean rightPressed = false;
private boolean timerdesabilitado = true;

// for examining the camera's (x,y,z) position
private Transform trans = new Transform();
private float[] transMat = new float[16];
private float xCoord;

private float yCoord;

private float zCoord;

// for storing the camera's current x- and y- rotations
private float xAngle;

// for storing the camera's current x- and y- rotations
private float yAngle;
private Transform rotTrans = new Transform();

// scene graph elements for the camera
private Group transGroup;
private Camera cam;
private Mesh shotMesh;
private Cockpit cockpitObject; 

private Timer iTimer;
private int nColisionCounter;
public static boolean blive = true;


public ShipCamera(int width, int height)

{
 cam = new Camera();
 float aspectRatio = ((float) width) / ((float) height);
 cam.setPerspective(70.0f, aspectRatio, 0.1f, 50.0f);
 cam.postRotate(-2.0f, 1.0f, 0, 0);  // apply x-axis rotations to camera

 // gun image mesh
 Image2D imGun = mzlib.loadImage2D(mzlib.TRANSPARENT_IMAGE);
 cockpitObject = new Cockpit(imGun);
 Mesh cockpitMesh = cockpitObject.getImageSquareMesh();
 cockpitMesh.setPickingEnable(false);
 

 // shot image mesh
 Image2D imShot = mzlib.loadImage2D(mzlib.SMALL_GUN_FIRE_1);
 SquareMesh imSqShot = new SquareMesh(imShot, 0, 0f, -0.30f, 0.1f);
 shotMesh = imSqShot.getImageSquareMesh();
 shotMesh.setPickingEnable(false);
 shotMesh.setRenderingEnable(false);  // invisible intially
 

 // put eveything together
 // transGroup handles both camera translation and y-axis rotations
 transGroup = new Group();      // no initial rotation
 trans.postTranslate(X_POS, Y_POS, Z_POS);
 transGroup.setTransform(trans);
 transGroup.addChild(cam);
 transGroup.addChild(cockpitMesh);
 transGroup.addChild(shotMesh);
 startTimer();

 yAngle = 0.0f;
}  // end of MobileGunCamera()

protected void startTimer() {
    iTimer = new Timer();
    iTimer.schedule(new MyTimerTask(), 0, 1);
  	System.out.println("Timer started");    
}

class MyTimerTask extends TimerTask {
    synchronized public void run() {
    	updateMove();
    }
}


public Group getCameraGroup()
{  return transGroup; }

public Camera getCamera()
{  return cam; }




// ----- handle key presses / releases, and return details ------

public void pressedKey(int gameAction)
{
 switch(gameAction) {
   case Canvas.UP:    upPressed = true;    break;
   case Canvas.DOWN:  downPressed = true;  break;
   case Canvas.LEFT:  leftPressed = true;  break;
   case Canvas.RIGHT: rightPressed = true; break;
   default: break;
 }
}  // end of pressedKey()


public void releasedKey(int gameAction)
{
 switch(gameAction) {
   case Canvas.UP:    upPressed = false;    break;
   case Canvas.DOWN:  downPressed = false;  break;
   case Canvas.LEFT:  leftPressed = false;  break;
   case Canvas.RIGHT: rightPressed = false; break;
   default: break;
 }
}  // end of releasedKey()



// ------------ position and direction access methods ------------

public float[] getPosition()
// return the current position of the camera
{ storePosition();
 return new float[] { xCoord, yCoord, zCoord };
}  // end of getPosition()


private boolean checkShipColision () {
    float[] camPos = GameDrawCanvas.mobCam.getPosition();   // camera's current position
    float[] camDir = GameDrawCanvas.mobCam.getDirection();  // camera's current fwd direction
    RayIntersection ri = new RayIntersection();
    GameDrawCanvas.Fuel -= 1;
    if (GameDrawCanvas.scene.pick(-1, camPos[0], camPos[1], camPos[2],
                       camDir[0], camDir[1], camDir[2], ri)) {   // hit something?
      float distance = ri.getDistance();  // normalized distance to the hit thing
      return  (distance < 0.5f);
    } else  {
    	return false;  
    }


}

private void storePosition()
// extract the current (x,y,z) position from transGroup
{
   transGroup.getCompositeTransform(trans);
  
   trans.get(transMat);
   xCoord = transMat[3];
   yCoord = transMat[7];
   zCoord = transMat[11];
   transGroup.getCompositeTransform(trans);
 
 
}  // end of storePosition()


public float[] getDirection()
/* Rotate the vector (0,0,-1) by the current y-axis rotation.
  (0,0,-1) represents the initial forward direction for the
  camera. The result after the rotation will be the camera's
  current forward direction. 
*/
{ float[] zVec = {0, 0, -1.0f, 0};
 rotTrans.transform(zVec);
 return new float[] { zVec[0], zVec[1], zVec[2] };
} // end of getDirection()


// ------------- update camera position and rotation ---------------

public void update()
{
 if (upPressed || downPressed || leftPressed || rightPressed) updateRotation(); 
}  // end of update()


private void updateMove()
{
	  nColisionCounter+=1;
	  if (nColisionCounter > 20) {
	  	nColisionCounter = 0;
	  	if ( checkShipColision() &&  blive ) {
	  		cockpitObject.SetVisible(false);
	  		blive = false;
	  		transGroup.getTransform(trans);
	  		trans.postTranslate(0, 0, 1);
	  		transGroup.setTransform(trans);
	  		GameDrawCanvas.shotMng.ShipColision(xCoord, yCoord, zCoord); 
	  		StarConfront.vibrate(0, 0, 5);
	  			
	  	} 	
	  }

	
  transGroup.getTransform(trans);
  trans.postTranslate(0, 0, -MOVE_INCR);
  transGroup.setTransform(trans);
}  // end of updateMove()



private void updateRotation()
{
  if (upPressed) {  // rotate up around x-axis
    xAngle += ANGLE_INCR;
    GameDrawCanvas.moveDown(3);  // background moves down
  }
  else if (downPressed) {  // rotate down around x-axis
    xAngle -= ANGLE_INCR;
    GameDrawCanvas.moveUp(3);    // background moves up
  }
  else if (leftPressed) {   // rotate left around the y-axis
    yAngle += ANGLE_INCR;
    GameDrawCanvas.moveRight(3); // background moves right
  }
  else if (rightPressed) {  // rotate right around the y-axis
    yAngle -= ANGLE_INCR;
    GameDrawCanvas.moveLeft(3);  // background moves left
  }
  // angle values are modulo 360 degrees
  if (xAngle >= 360.0f)
    xAngle -= 360.0f;
  else if (xAngle <= -360.0f)
    xAngle += 360.0f;

  if (yAngle >= 360.0f)
    yAngle -= 360.0f;
  else if (yAngle <= -360.0f)
    yAngle += 360.0f;

    storePosition();
    trans.setIdentity();
    trans.postTranslate(xCoord, yCoord, zCoord);
    trans.postRotate(yAngle, 0, 1.0f, 0);
    trans.postRotate(xAngle, 1.0f, 0, 0);
    transGroup.setTransform(trans);
  
}  


// -------------- display shot ------------

public void setShotVisible(boolean b)
{  shotMesh.setRenderingEnable(b);  }

} // end of MobileGunCamera class
