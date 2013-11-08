/*
 * Created on 20/09/2005
 *
 * All Possible Resources
 * 
 */

/**
 * 
 * Complete list of all game object  
 *  
 */
public class ResourceList {
	
	public final int ENEMYSHIP      = 0;  
	public final int HUNTERSHIP     = 1;
	public final int DESTROYERSHIP  = 2;
	public final int BOX            = 3;	
	
	private final String[] Resource_Type = {
		    "Enemy Ship",  
		    "Hunter Ship",
		    "Destroyer Ship",			
		    "Box",
	};
	
	
	private final String[] Mesh_Files = {
		    "nave1.m3g",  
		    "nave2.m3g",
		    "nave3.m3g",
		    "test.m3g",			
		  };

	private final String[] Texture_Files = {
		    "metal.jpg",  
		    "metal.jpg",
		    "metal.jpg",
		    "metal.jpg",			
		  };

	private final int[][] Mesh_ID = {
		    {1,2,3,4},  
		    {1,0},
			{0},
			{2},			
		  };
	
	private final boolean[] Is_Enemy = {
		    true,  
		    true,
		    true,
			false,
		  };

	private final boolean[] Can_Destroy = {
		    true,  
		    true,
		    true,
			false,
		  };
	
	private final int[] Object_Life = {
		    10,  
		    3,
		    3,
			10,
		  };
	
	private final int[] Enemy_Strategy = {
		    0,  
		    0,
		    1,
			-1,
		  };
	private final float[] Mesh_Scale = {
			0.5f,  
			0.1f,
			0.1f,
			0.1f,
	};

	private final boolean[] Animated_Mesh = {
		    false,  
		    false,
		    false,
		    false,
		  };
	

	
	
	public Resource getResource(int ID){
		Resource res = new Resource();
		
		res.MeshFile      = "/mesh/"+Mesh_Files[ID];
		res.MeshID        = Mesh_ID[ID];
		res.ResourceType  = Resource_Type[ID];
		res.IsEnemy       = Is_Enemy[ID];
		res.CanDestroyed  = Can_Destroy[ID];
		res.ObjectLife    = Object_Life[ID];
		res.EnemyStrategy = Enemy_Strategy[ID];
		res.MeshScale     = Mesh_Scale[ID];
		res.Animated      = Animated_Mesh[ID];
		res.TextureFile   = "/texture/"+Texture_Files[ID];		
		
		return res;
	}
	

}
