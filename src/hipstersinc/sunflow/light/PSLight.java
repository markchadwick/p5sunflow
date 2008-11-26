package hipstersinc.sunflow.light;

import hipstersinc.sunflow.SunflowCamera;
import hipstersinc.sunflow.util.SceneDumpable;
import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;
import org.sunflow.math.Point3;


public class PSLight implements SceneDumpable {
	/** SunflowAPI to write to */
	private SunflowAPI sunflow;
	
	/** Type of light, as defined in the "Types of Lights" section below */
	private String type;
	
	//////////////////////////////////////////////////////////////////
	// Types of lights
	public static final String MESH = "triangle_mesh";
	public static final String POINT = "point";
	public static final String SPHERE = "sphere";
	public static final String DIRECTIONAL = "directional";
	public static final String IBL = "ibl";
	public static final String SUN_SKY = "sunsky";
	public static final String CORNELL_BOX = "cornell_box";

	//////////////////////////////////////////////////////////////////
	// Light Shapes
	protected float[] vertices = {};
	protected int[] triangles = {};
	
	//////////////////////////////////////////////////////////////////
	// Light Position
	float x, y, z;
	
	//////////////////////////////////////////////////////////////////
	// Light Attributes
	protected float power;
	protected float[] radiance = {3, 3, 3};
	protected int samples = 32;
	protected String name;

	/**
	 * Default Constructor
	 * @param sunflow SunflowAPI
	 * @param type
	 */
	public PSLight(SunflowAPI sunflow, String type) {
		this(sunflow, type, 1, 0, 0, 0);
	}
	
	/**
	 * Constructor with power and position
	 * @param sunflow
	 * @param type
	 * @param x
	 * @param y
	 * @param z
	 */
	public PSLight(SunflowAPI sunflow, String type, float power, float x, float y, float z) {
		this.sunflow = sunflow;
		this.type = type;
		
		this.power = power;
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.name = type +"_light_"+ this.hashCode();
	}
	
	/**
	 * Write this light to the SunflowAPI
	 *
	 */
	public final void set() {
		if(type.equals(MESH)) {
			setMeshlight();
		} else if (type.equals(POINT)){
			setPointlight();
		} else {
			System.err.println("Unknown Light Type: "+ type);
		}
	}
	
	/**
	 * Returns a meshlight directly behind the camera
	 * @param sunflow
	 * @return
	 */
	public static final PSLight centerMesh(SunflowAPI sunflow, SunflowCamera camera) {
		Point3 cameraPosition = camera.getEye();
		return defaultMesh(sunflow, cameraPosition.x, cameraPosition.y, cameraPosition.z + 10);
	}
	
	public static final PSLight rightMesh(SunflowAPI sunflow, SunflowCamera camera) {
		Point3 cameraPosition = camera.getEye();
		return defaultMesh(sunflow,
				cameraPosition.x + cameraPosition.z,
				cameraPosition.y,
				cameraPosition.z);
	}
	
	public static final PSLight leftMesh(SunflowAPI sunflow, SunflowCamera camera) {
		Point3 cameraPosition = camera.getEye();
		return defaultMesh(sunflow,
				cameraPosition.x - cameraPosition.z,
				cameraPosition.y,
				cameraPosition.z);
	}
	
	/**
	 * Returns a square meshlight pointed directly down for the point {x, y, z}
	 * @param sunflow
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private static final PSLight defaultMesh(SunflowAPI sunflow, float x, float y, float z) {
		PSLight light = new PSLight(sunflow, MESH);
		
		float size = z;
		float[] vertices = {
				x,		y,		z,
				x,		y-size,	z,
				x+size,	y,		z,
				x,		y+size,	z,
				x-size,	y,		z,
		};
		int[] triangles = {
				2, 1, 0,
				3, 2, 0,
				4, 3, 0,
				1, 4, 0
		};
		
		light.setVertices(vertices);
		light.setTriangles(triangles);
		light.setRadiance(5);
		
		return light;
	}
	
	/**
	 * @return the triangles
	 */
	public int[] getTriangles() {
		return triangles;
	}

	/**
	 * @param triangles the triangles to set
	 */
	public void setTriangles(int[] triangles) {
		this.triangles = triangles;
	}

	/**
	 * @return the vertices
	 */
	public float[] getVertices() {
		return vertices;
	}

	/**
	 * @param vertices the vertices to set
	 */
	public void setVertices(float[] vertices) {
		this.vertices = vertices;
	}
	
	public void setRadiance(float f) {
		radiance[0] = f;
		radiance[1] = f;
		radiance[2] = f;
	}
	
	/**
	 * Sets a meshlight on the API
	 */
	private void setMeshlight() {
		sunflow.parameter("radiance", null, radiance[0], radiance[1], radiance[2]);
		sunflow.parameter("samples", samples);
		sunflow.parameter("points", "point", "vertex", vertices);
		sunflow.parameter("triangles", triangles);
		sunflow.light(name, MESH);
	}
	
	/**
	 * Sets a point light to the API
	 */
	private void setPointlight() {
		sunflow.parameter("power", null, 1f, 1f, 1f);;
		sunflow.parameter("center", new Point3(x, y, z));
		sunflow.light(name, POINT);
	}
	
	public StringBuffer toScene() {
		// Convert internal radiance to color/power
		float maxRadiance = 0;
		float[] color = new float[3];
		float power = radiance[0]/3 + radiance[1]/3 + radiance[2]/3;
		for(float r : radiance) {
			maxRadiance = Math.max(r, maxRadiance);
			color[0] = radiance[0]/maxRadiance;
			color[1] = radiance[1]/maxRadiance;
			color[2] = radiance[2]/maxRadiance;
		}
		
		StringBuffer buf = new StringBuffer();
		
		SceneDumper.startBlock("light", buf);
		SceneDumper.param("type", type, buf);
		
		if(type.equals(POINT)) {
			float[] center = {x, y, z};
			SceneDumper.paramColor(color[0], color[1], color[2], buf);
			SceneDumper.param("power", maxRadiance, buf);
			SceneDumper.param("p", center, buf);
		}
		
		SceneDumper.endBlock();
		
		return buf;
	}
}