
/*
 * Created on 15/09/2005
 *
 * Unit de debug de Mesh
 */

/**
 * @author Zion Corp.
 *
 */

import javax.microedition.m3g.Group;
import javax.microedition.m3g.Material;
import javax.microedition.m3g.Object3D;
import javax.microedition.m3g.Transform;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;


public class MeshDebug {
	
//	 globals for looking at the transform in a Group node
	private static float trans[] = new float[16];
	private static Transform modelTransform = new Transform();
	private static Group modelGroup; // top-level Group node for the model
	private static boolean foundGroup; // Group node found during search?
	private static boolean isSceneRoot;//	 is found Group node the root of the scene graph?
	
	
	public static void printlnIndent(String Texto, int Level)
	{
	   String inTexto = "";	
		for (int i=0; i < Level; i++) {inTexto += "    ";}
		System.out.print(inTexto+Texto+"\n");
	}

	public static void examineObjects(Object3D[] parts, String section, Object3D parent, int level)
	{
	  String name, subsection;
	   for (int i=0; i < parts.length; i++) {
	     name = parts[i].getClass().getName();
	     subsection = section + (i+1) + ".";
	     printlnIndent(subsection + " " + name + ", ID: " +parts[i].getUserID(), level);
	     examineNode(parts[i], name, parent, level);
	     examineRefs(parts[i], subsection, level+1);
	   }
	}


	private static void examineNode(Object3D part, String name, Object3D parent, int level)
	{
	   if (name.endsWith("Group")) {
			storeGroup((Group) part, parent, level);
			examineGroup((Group) part, level);
		}
		else if (name.endsWith("VertexBuffer"))	examineVertexBuffer((VertexBuffer) part, level);
		else if (name.endsWith("Material"))   examineMaterial( (Material) part, level);
	} // end of examineObjects()


	private static void examineMaterial(Material m, int level)
	{
	  int ambColour = m.getColor(Material.AMBIENT);
	  int diffColour = m.getColor(Material.DIFFUSE);
	  int emisColour = m.getColor(Material.EMISSIVE);
	  int specColour = m.getColor(Material.SPECULAR);
	  printlnIndent("Colours: " + ambColour + ", " + diffColour+", " +emisColour + ", " + specColour+", " + m.getShininess(), level);
	  printlnIndent("Colours in Hex: " + Integer.toHexString(ambColour)+ ", " + Integer.toHexString(diffColour) + ", "+ Integer.toHexString(emisColour) + ", "+ Integer.toHexString(specColour), level);
	}

	private static void examineGroup(Group g, int level)
	{
	   g.getCompositeTransform(modelTransform);
	    modelTransform.get(trans);
	    for(int i=0; i < 16; i= i+4)
	    	printlnIndent( round2dp(trans[i]) + " " +
	    	round2dp(trans[i+1]) + " " +
	    	round2dp(trans[i+2]) + " " +
	    	round2dp(trans[i+3]), level);
	}


	private static void examineVertexBuffer(VertexBuffer vb, int level)
	{
	   float[] scaleBias = new float[4];
	   VertexArray va = vb.getPositions(scaleBias);
	   printlnIndent("Positions: " + (va != null) +"; No. Coords: " + (vb.getVertexCount()/3), level);
	   if (va != null) printlnIndent("Posn ID: " + va.getUserID() +"; User Object: " + (va.getUserObject() != null), level);
	   printlnIndent("Scale/Bias: " + round2dp(scaleBias[0]) + " " +round2dp(scaleBias[1]) + " " + round2dp(scaleBias[2]) + " " +round2dp(scaleBias[3]), level);
	   int texCount = 0;
	   while (true) {
	      if (vb.getTexCoords(texCount, scaleBias) == null) break;
	      texCount++;
	   }
	   printlnIndent("Colours: " + (vb.getColors() != null) + "; Normals: " + (vb.getNormals() != null) + "; No. of textures: " + texCount, level);
	   
	} // end of examineVertexBuffer()
	/*
	static void traverseGraph(Object3D obj)
	{ // a top-down recursive traverse
		int numRefs = obj.getReferences(null);
		if (numRefs > 0) {
		  Object3D[] objArray = new Object3D[numRefs];
		  obj.getReferences(objArray);
		  for (int i = 0; i < numRefs; i++) {
//		    processObject(objArray[i]); // process object i
		    traverseGraph(objArray[i]); // and its children
		  }
		}
	}
	*/

	private static void storeGroup(Group g, Object3D parent, int level)
	{
	   if (!foundGroup) { // if not already found a Group node
	         modelGroup = g;
	         System.out.println("modelGroup assigned Group with ID " +g.getUserID() + "; at level " + level);
	         foundGroup = true;
	         if (level != 0) { // the Group is not the root node
	            isSceneRoot = false;
	            if (parent instanceof Group) {
	                        Group gPar = (Group) parent;
	                        gPar.removeChild(modelGroup);
	                        System.out.println("Detached Group from parent with ID " +gPar.getUserID());
	             } 
	            else  {
	              System.out.println("Could not detach Group; set to null");
	              modelGroup = null; // since cannot attach it to our scene
	            }
	        }
	    }
	} // end of storeGroup()

	private static float round2dp(float num)
//	 round num to 2 decimal places
	{  return ((int)((num+0.005)*100.0f))/100.0f;  }

	private static void examineRefs(Object3D obj, String section, int level)
	{
	   int numRefs = obj.getReferences(null);
	   if (numRefs > 0) {
	      Object3D[] parts = new Object3D[numRefs];
	      obj.getReferences(parts);
	      examineObjects(parts, section, obj, level);
	   }
	}


}
