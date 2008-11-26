package hipstersinc.sunflow;

import hipstersinc.sunflow.util.SceneDumpable;
import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;
import org.sunflow.core.Display;
import org.sunflow.image.Color;
import org.sunflow.image.ColorFactory;
import org.sunflow.image.ColorFactory.ColorSpecificationException;

public class SunflowScene implements SceneDumpable {
	/** SunflowAPI instance */
	private SunflowAPI sunflow;
	
	//////////////////////////////////////////////////////////////////
	// Types of filters
	public static final String GAUSSIAN = "gaussian";
	public static final String BLACKMAN_HARRIS = "blackman-harris";
	public static final String SINC = "sinc";
	public static final String TRIANGLE = "triangle";
	public static final String MITCHELL = "mitchell";
	
	//////////////////////////////////////////////////////////////////
	// Scene parameters
	/** Resolution of the image */
	private int width, height;
	
	/** Minimum anti-aliasing resolution */
	private int aaMin;
	
	/** Maximum anti-aliasing resolution */
	private int aaMax;
	
	/** Number of anti-aliasing samples per pixel */
	private int aaSamples;
	
	/** Jitter for sub-pixel iterpolation */
	private boolean jitter = false;
	
	/** Iterpolation filter */
	private String filter = GAUSSIAN;
	
	/** Levels of diffusion */
	private int diffusionDepth;
	
	/** Levels of reflection */
	private int reflectionDepth;
	
	/** Levels of refraction */
	private int refractionDepth;
	
	/** Background Color */
	private float backgroundR=1, backgroundG=1, backgroundB=1;
	
	/** Display we're drawing to */
	private Display display;
	
	//////////////////////////////////////////////////////////////////
	// Global Illumination Parameters
	private boolean globalIllumination = true;
	
	private String giEngine = "igi";
	private int igiSamples = 4; //.TODO: 32
	private int igiSets = 1;
	private float igiC = 0.0003f;
	private int igiBias = 0;
	
	
	/**
	 * Default constructor
	 * @param sunflow SunflowAPI of the scene
	 */
	public SunflowScene(SunflowAPI sunflow, Display display) {
		this.sunflow = sunflow;
		this.display = display;
	}
	
	/**
	 * Sets up a reasonable default environment
	 */
	public final void defaults() {
		setAaMin(0);
		setAaMax(1);
		setAaSamples(1);
		setJitter(false);
		
		setFilter(GAUSSIAN);
		
		setDiffusionDepth(1);
		setReflectionDepth(0);
		setRefractionDepth(0);
	}
	
	/**
	 * Sets up a reasonable environment for a given height and width
	 * @param width
	 * @param height
	 */
	public final void defaults(int width, int height) {
		setWidth(width);
		setHeight(height);
		defaults();
	}

	/**
	 * Writes the parameters to the API
	 */
	public void set() {
		setupScene();
		setupTracing();
		setupIllumination();
		setupBackground();
	}
	
	
	public void background(float r, float g, float b) {
		if(r > 1 || g > 1 || b > 1) {
			r /= 255f;
			g /= 255f;
			b /= 255f;
		}
		backgroundR = r;
		backgroundG = g;
		backgroundB = b;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the aaMin
	 */
	public int getAaMin() {
		return aaMin;
	}

	/**
	 * @param aaMin the aaMin to set
	 */
	public void setAaMin(int aaMin) {
		this.aaMin = aaMin;
	}

	/**
	 * @return the aaMax
	 */
	public int getAaMax() {
		return aaMax;
	}

	/**
	 * @param aaMax the aaMax to set
	 */
	public void setAaMax(int aaMax) {
		this.aaMax = aaMax;
	}

	/**
	 * @return the jitter
	 */
	public boolean isJitter() {
		return jitter;
	}

	/**
	 * @param jitter the jitter to set
	 */
	public void setJitter(boolean jitter) {
		this.jitter = jitter;
	}

	/**
	 * @return the filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return the diffusionDepth
	 */
	public int getDiffusionDepth() {
		return diffusionDepth;
	}

	/**
	 * @param diffusionDepth the diffusionDepth to set
	 */
	public void setDiffusionDepth(int diffusionDepth) {
		this.diffusionDepth = diffusionDepth;
	}

	/**
	 * @return the reflectionDepth
	 */
	public int getReflectionDepth() {
		return reflectionDepth;
	}

	/**
	 * @param reflectionDepth the reflectionDepth to set
	 */
	public void setReflectionDepth(int reflectionDepth) {
		this.reflectionDepth = reflectionDepth;
	}

	/**
	 * @return the refractionDepth
	 */
	public int getRefractionDepth() {
		return refractionDepth;
	}

	/**
	 * @param refractionDepth the refractionDepth to set
	 */
	public void setRefractionDepth(int refractionDepth) {
		this.refractionDepth = refractionDepth;
	}

	/**
	 * @return the aaSamples
	 */
	public int getAaSamples() {
		return aaSamples;
	}

	/**
	 * @param aaSamples the aaSamples to set
	 */
	public void setAaSamples(int aaSamples) {
		this.aaSamples = aaSamples;
	}

	/**
	 * @return the globalIllumination
	 */
	public boolean isGlobalIllumination() {
		return globalIllumination;
	}

	/**
	 * @param globalIllumination the globalIllumination to set
	 */
	public void setGlobalIllumination(boolean globalIllumination) {
		this.globalIllumination = globalIllumination;
	}
	

	/**
	 * @return the giEngine
	 */
	public String getGiEngine() {
		return giEngine;
	}

	/**
	 * @param giEngine the giEngine to set
	 */
	public void setGiEngine(String giEngine) {
		this.giEngine = giEngine;
	}

	/**
	 * @return the igiSamples
	 */
	public int getIgiSamples() {
		return igiSamples;
	}

	/**
	 * @param igiSamples the igiSamples to set
	 */
	public void setIgiSamples(int igiSamples) {
		this.igiSamples = igiSamples;
	}

	/**
	 * @return the igiSets
	 */
	public int getIgiSets() {
		return igiSets;
	}

	/**
	 * @param igiSets the igiSets to set
	 */
	public void setIgiSets(int igiSets) {
		this.igiSets = igiSets;
	}

	/**
	 * @return the igiC
	 */
	public float getIgiC() {
		return igiC;
	}

	/**
	 * @param igiC the igiC to set
	 */
	public void setIgiC(float igiC) {
		this.igiC = igiC;
	}

	/**
	 * @return the igiBias
	 */
	public int getIgiBias() {
		return igiBias;
	}

	/**
	 * @param igiBias the igiBias to set
	 */
	public void setIgiBias(int igiBias) {
		this.igiBias = igiBias;
	}
	
	/**
	 * Sets up antialiasing, resolution, and filters
	 */
	private void setupScene() {
		sunflow.parameter("resolutionX", width);
		sunflow.parameter("resolutionY", height);
		sunflow.parameter("aa.min", aaMin);
		sunflow.parameter("aa.max", aaMax);
		sunflow.parameter("aa.jitter", jitter);
		sunflow.parameter("aa.samples", aaSamples);
		sunflow.parameter("filter", filter);
		sunflow.options(SunflowAPI.DEFAULT_OPTIONS);
	}
	
	/**
	 * Sets up the various tracing depths
	 */
	private void setupTracing() {
		sunflow.parameter("depths.diffuse", diffusionDepth);
		sunflow.parameter("depths.reflection", reflectionDepth);
		sunflow.parameter("depths.refraction", refractionDepth);
		sunflow.options(SunflowAPI.DEFAULT_OPTIONS);
	}
	
	/**
	 * Sets up lighting and global illumination
	 */
	private void setupIllumination() {
		if(globalIllumination) {
			sunflow.parameter("gi.engine", giEngine);
			if(giEngine.equals("igi")) {
				sunflow.parameter("gi.igi.samples", igiSamples);
				sunflow.parameter("gi.igi.sets", igiSets);
				sunflow.parameter("gi.igi.c", igiC);
				sunflow.parameter("gi.igi.bias_samples", igiBias);
			}
		}
		sunflow.options(SunflowAPI.DEFAULT_OPTIONS);
	}
	
	/**
	 * Draws the background to the scene
	 */
	private void setupBackground() {
		float[] values = {backgroundR, backgroundG, backgroundB};
		Color color = null;
		
		try {
			color = ColorFactory.createColor(null, values);
			display.imageBegin(width, height, 16);
			display.imageFill(0, 0, width, height, color, 1);
			display.imageEnd();
		} catch (ColorSpecificationException e) {
			e.printStackTrace();
		}
		
        sunflow.parameter("color", null, backgroundR, backgroundG, backgroundB);
        sunflow.shader("background.shader", "constant");
        sunflow.geometry("background", "background");
        sunflow.parameter("shaders", "background.shader");
        sunflow.instance("background.instance", "background");
	}

	/**
	 * Dump the scene params to Sunflow's Scene format
	 * 
	 */
	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer();
		SceneDumper.comment("Scene Parameters", buf);
		
		int[] resolution = {width, height};
		int[] aa = {aaMin, aaMax};
		
		//  background { color { "sRGB nonlinear" 0.5 0.5 0.5 } }
		SceneDumper.startBlock("background", buf);
		SceneDumper.paramColor(backgroundR, backgroundG, backgroundB, buf);
		SceneDumper.endBlock();
		
		SceneDumper.startBlock("image", buf);
		SceneDumper.param("resolution", resolution, buf);
		SceneDumper.param("aa", aa, buf);
		SceneDumper.param("samples", aaSamples, buf);
		SceneDumper.param("filter", filter, buf);
		SceneDumper.endBlock();
		
		SceneDumper.startBlock("trace-depths", buf);
		SceneDumper.param("diff", diffusionDepth, buf);
		SceneDumper.param("refl", reflectionDepth, buf);
		SceneDumper.param("refr", refractionDepth, buf);
		SceneDumper.endBlock();
		
		// TODO: GI sections
		if(globalIllumination) {
			
		}
		
		return buf;
	}
}
