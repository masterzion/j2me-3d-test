/*
 * Created on 14/09/2005
 *
 * Manipulação de arquivos M3G
 * 
 * loadSceneModel  le todo conteudo do arquivo m3g
 * loadSceneObject le determinada mesh dentro de world
 * loadRootMesh    le os objetos dentro do objectgroup root 
 * 
 */

/**
 * @author Jairo M. DeBarros Junior
 *
 * Arquivo para leitura de 
 * 
 */
import javax.microedition.m3g.*;

public class Load3DFile {


public static final  Group getMeshFile(Resource res, float xMove, float yMove, float zMove, int ID)
{ 
       Mesh mGroup = null; 
	   Object3D[] parts = null;
	   try {
	       parts = Loader.load(res.MeshFile);
	       } 
	   catch (Exception e) 
	   {  
	   	e.printStackTrace();  
	   }
	   //debug 
	   MeshDebug.examineObjects(parts, "", null, 0);
   
	   World root = (World) parts[0];  

	   
	   Group posGroup = new Group();
	   for (int i=0; i < res.MeshID.length-4; i++) {
	   	 mGroup = (Mesh)root.find(res.MeshID[i]);
	   	 System.out.println("add mesh " + res.MeshID[i] );
	   	
	   	 mGroup.setOrientation(0,0,0,0);
	   	 //mGroup.setUserObject( new EnemyInfo("Enemy ", ID, lives) ); 
	     root.removeChild(mGroup);  
	     posGroup.addChild(mGroup);
	   }
	   
	   System.out.println("end loop");	   
	   /*	   Group posGroup = new Group();
	   posGroup.setOrientation(-90.0f, 1.0f, 0, 0);  // rotate -90 around x-axis
	   posGroup.setTranslation(xMove, yMove+0.1f, zMove);  
	   posGroup.scale(res.MeshScale, res.MeshScale, res.MeshScale);
	   
//	   mzlib.sortint(res.id);
	   
	   
	   
	   

	     System.out.println("Loading model from " + res.MeshFile + " - " + res.MeshID.length  + " items " );

	     for (int i=0; i < res.MeshID.length; i++) {   
	        System.out.println("add mesh " + res.MeshID[i] );
	      	 mGroup = (Mesh)root.getChild(res.MeshID[i]);
	      	 mGroup.setOrientation(0,0,0,0);
	      	mGroup.setUserObject( new EnemyInfo(res.ResourceType, ID, res.ObjectLife) );  
             root.removeChild(mGroup);  
	        System.out.println("OK" );     
	      }
	*/
	   
	   
        posGroup.addChild(mGroup);
	    posGroup.setUserObject( new EnemyInfo("Enemy ", ID, res.ObjectLife) );
        return posGroup;

	
	
/*	
	System.out.println("Loading "+res.ResourceType+" from " + res.MeshFile + " ( mesh " + res.MeshID + " )" );
   Object3D[] parts = null;
   try {
       parts = Loader.load(res.MeshFile);
       } 
   catch (Exception e) 
   {  
   	e.printStackTrace();  
   }

   MeshDebug.examineObjects(parts, "", null, 0);
   
   World root = (World) parts[0];  
   
   Group posGroup = new Group();
   posGroup.setOrientation(-90.0f, 1.0f, 0, 0);  // rotate -90 around x-axis
   posGroup.setTranslation(xMove, yMove, zMove);
   System.out.println("put model at "+"x"+xMove+"x"+yMove+"x"+zMove  );
   posGroup.scale(res.MeshScale, res.MeshScale, res.MeshScale);
*/   
   /*

   for (int i=0; i < id.length; i++) {   
     System.out.println("Loading model from " + fn + " ( mesh " + id[i] + " )" );
   	 Mesh mGroup = (Mesh)root.getChild(id[i]);
   	 mGroup.setOrientation(0,0,0,0);
   	 mGroup.setUserObject( new EnemyInfo("Enemy ", ID, lives) ); 
     root.removeChild(mGroup);  
     posGroup.addChild(mGroup);
     System.out.println("OK" );     
   }

*/

   
/*
   VertexBuffer vb = (VertexBuffer)root.find(2);
   IndexBuffer ib =  (TriangleStripArray)root.find(8);
   Material mat =  (Material)root.find(6);
   
   Appearance app = new Appearance();
   app.setMaterial( mat );

   Texture2D tex = mzlib.loadTexture(res.TextureFile, true);
   app.setTexture(0, tex);

   
   Mesh model = new Mesh(vb, ib, app);
   posGroup.addChild(model);
*/
   
      

   /*
   Appearance app = mGroup.getAppearance(0);

   Texture2D tex = mzlib.loadTexture(res.TextureFile, false);
   app.setTexture(0, tex);
   
   mGroup.setAppearance(0, app);
*/
} 


}
