/*
 * Created on 20/09/2005
 *
 * Resource Object
 * 
 */

/**
 * @author Zion Corp.
 *
 * Contains the Property of all Resource 
 */
public class Resource {
	public String ResourceType = "Resource Type";
	
	public String MeshFile = "mesh empty";
	public int[] MeshID = {-1};
	public float MeshScale = -1;
	public boolean Animated = false;	
	public int ObjectLife = -1;

	public String TextureFile = "texture empty";
	public int EnemyStrategy = -1;

	
	public boolean IsEnemy = true;
	public boolean CanDestroyed = true;
}

