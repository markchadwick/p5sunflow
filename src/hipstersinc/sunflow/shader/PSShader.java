// TODO: This Needs to Go
package hipstersinc.sunflow.shader;

import hipstersinc.sunflow.util.SceneDumpable;
import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;

public class PSShader implements SceneDumpable {
	/** Default Sunflow API */
	private SunflowAPI sunflow;
	
	//////////////////////////////////////////////////////////////////
	// Types of Shaders
	public static final String DIFFUSE = "diffuse";
	public static final String TEXTURED_DIFFUSE = "textured_diffuse";
	public static final String PHONG = "phong";
	public static final String TEXTURED_PHONG = "textured_phong";
	public static final String AMBIENT_OCCULSION = "ambient_occlusion";
	public static final String TEXTURED_AMBIENT_OCCULSION = "textured_ambient_occlusion";
	public static final String MIRROR = "mirror";
	public static final String GLASS = "glass";
	public static final String WARD = "ward";
	
	//////////////////////////////////////////////////////////////////
	// Shader Attributes
	private float a, r, g, b;
	private float shininess;
	private String type;
	
	private int samples = 12;
	private float maxDist = 50f;
	
	/**
	 * Default Constructor
	 * @param sunflow SunflowAPI
	 * @param a Alpha value (0.0 - 1.0)
	 * @param r Red value (0.0 - 1.0)
	 * @param g Green value (0.0 - 1.0)
	 * @param b Blue value (0.0 - 1.0)
	 * @param shininess
	 */
	public PSShader(SunflowAPI sunflow, float a, float r, float g, float b, float shininess) {
		this.sunflow = sunflow;
		
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
		this.shininess = shininess;
		
		// Resonable Defaults?
		if(a < 1) {
			type = GLASS;
		} else {
			type = DIFFUSE;
		}
	}
	
	public void set(boolean ambientOcculsion) {
		if(ambientOcculsion) {
			setAmbientOcculsion();
		} else if (type.equals(GLASS)) {
			setGlass();
		} else if (type.equals(DIFFUSE)) {
			setDiffuse();
		}
	}
	
	public String getHash() {
		String name = a +"-"+ r +"-"+ g +"-"+ b;
		name = "shader-"+ name.replaceAll("\\.", "");
		return name;
	}
	
	public String getName() {
		return getHash();
	}
	
	
	/**
	 * Ambient Occulsion Shader
	 *
	 */
	private void setAmbientOcculsion() {
		sunflow.parameter("bright", null, r, g, b);
		sunflow.parameter("dark", 	null, 0, 0, 0);
		sunflow.parameter("samples", samples);
		sunflow.parameter("maxdist", maxDist);
		sunflow.shader(getName(), AMBIENT_OCCULSION);
	}
	
	private void setDiffuse() {
		sunflow.parameter("diffuse", null, r, g, b);
		sunflow.shader(getName(), DIFFUSE);
	}
	
	/**
	 * Glass Shader
	 *
	 */
	private void setGlass() {
		sunflow.parameter("color", null, 1f, 1f, 1f);
		sunflow.parameter("eta", 1.55f);
		
		sunflow.parameter("absorption.distance", 0.001f);
		sunflow.parameter("absorption.color", null, 0.3f, 0.3f, 0.6f);
		
		sunflow.shader(getName(), GLASS);
	}
	
	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer();
		buf.append( String.format("%% A:%.2f  R:%.2f  G:%.2f  B:%.2f  S:%.2f\n", a, r, g, b, shininess));

		// TODO: This should do an AO check
		if(true) {
			ambientOcculsionToScene(buf);
		}
		
		return buf;
	}
	
	private void ambientOcculsionToScene(StringBuffer buf) {
		float[] bright = {r, g, b};
		float[] dark = {0, 0, 0};
		
		SceneDumper.startBlock("shader", buf);
		SceneDumper.param("name", getName(), buf);
		SceneDumper.param("type", "amb-occ", buf);
		SceneDumper.param("bright", bright, buf);
		SceneDumper.param("dark", dark, buf);
		SceneDumper.param("samples", samples, buf);
		SceneDumper.param("dist", maxDist, buf);
		SceneDumper.endBlock();
	}
}
