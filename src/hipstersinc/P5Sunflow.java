package hipstersinc;

import hipstersinc.sunflow.ProcessingDisplay;
import hipstersinc.sunflow.SunflowCamera;
import hipstersinc.sunflow.SunflowScene;
import hipstersinc.sunflow.light.PSLight;
import hipstersinc.sunflow.shader.PSShader;
import hipstersinc.sunflow.shape.PSLine;
import hipstersinc.sunflow.shape.PSPolygon;
import hipstersinc.sunflow.shape.PSShape;
import hipstersinc.sunflow.shape.PSSphere;
import hipstersinc.sunflow.util.SceneDumper;

import java.awt.Toolkit;
import java.awt.image.DirectColorModel;
import java.awt.image.MemoryImageSource;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sunflow.SunflowAPI;


import processing.core.PApplet;
import processing.core.PGraphics3D;

public class P5Sunflow extends PGraphics3D {
	
	//////////////////////////////////////////////////////////////////
	// Public accessors
	public SunflowScene scene;
	public SunflowCamera camera;
	protected List<PSLight> lights;
	
	//////////////////////////////////////////////////////////////////
	// Sunflow parameters
	protected SunflowAPI sunflow;
	protected PApplet applet;
	protected ProcessingDisplay display;
	private PSShader currentShader;
	private HashMap<String, PSShader> shaders;
	public boolean hasRenderedFrame = false;
	private boolean dumpScene = false;
	private boolean showPreview = false;
	private String dumpingFileName = null;
	
	//////////////////////////////////////////////////////////////////
	// Sunflow Primitives
	private List<PSShape> shapes;
	
	//////////////////////////////////////////////////////////////////
	// Processing Color/Image data
	private DirectColorModel cm;
	private MemoryImageSource mis;
	
	/**
	 * Default constructor.  Called by processing
	 * @param width
	 * @param height
	 * @param applet
	 */
	public P5Sunflow(int width, int height, PApplet applet) {
		super(width, height, applet);
		this.applet = applet;
		
		if(applet != null) {
			applet.width = width;
			applet.height = height;
			applet.registerDraw(this);
		}
		
		this.display = new ProcessingDisplay(applet, this);
		this.scene = new SunflowScene(sunflow, display);
		this.shapes = new ArrayList<PSShape>();
	}
	
	
	protected void allocate() {
		sunflow = new SunflowAPI();
		lights = new ArrayList<PSLight>();
		
	    pixelCount = width * height;
	    pixels = new int[pixelCount];
	    
	    backgroundColor |= 0xff000000;
	    for (int i = 0; i < pixelCount; i++) pixels[i] = backgroundColor;
	    
	    // Stolen directly from processing source
	    if(mainDrawingSurface) {
	        cm = new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0x000000ff);;
	        mis = new MemoryImageSource(width, height, pixels, 0, width);
	        mis.setFullBufferUpdates(true);
	        mis.setAnimated(true);
	        image = Toolkit.getDefaultToolkit().createImage(mis);
	    }
	}
	
	protected void deallocate() {
		this.display = null;
		this.scene = null;
		this.shapes = null;
		this.sunflow = null;
		this.lights = null;
		this.pixels = null;
		this.cm = null;
		this.mis = null;
		this.image = null;
//		System.gc();
	}

	//////////////////////////////////////////////////////////////////
	// Processing Drawing Methods
	
	public void beginDraw() {
		hasRenderedFrame = false;
		colorMode(RGB, 255);
		
		sunflow.reset();
	    shaders = new HashMap<String, PSShader>();
		
	    camera = new SunflowCamera(SunflowCamera.PINHOLE, sunflow);
		scene.defaults(width, height);
		
		super.beginDraw();
	}
	
	public void draw() {
		if(!hasRenderedFrame) {
			render();
		}
	}
	
	public void endDraw() {
	    if (mis != null) {
	        mis.newPixels(pixels, cm, 0, width);
	    }
	    updatePixels();
	    insideDraw = false;
	}
	
	// TODO: This is gross
	private void render() {
		hasRenderedFrame = true;

		if(lights.size() == 0) {
			scene.setGlobalIllumination(true);
			pointlight(1, 1, 1, 0, 0, 0);
		} else {
			scene.setGlobalIllumination(true);
		}
		
		for(PSShape shape: shapes) {
			shape.draw();
		}
		
		for(PSLight light : lights) {
			light.set();
		}
		
		setupCamera();
		scene.set();
		display.showPreview(showPreview);
		
		if(dumpScene) {
			if(dumpingFileName == null) {
				// TODO: Nope!
				System.out.println("NOPE!");
//				System.out.println(dump());
			} else {
				dump(dumpingFileName);
			}
		} else {
			sunflow.render(SunflowAPI.DEFAULT_OPTIONS, display);
		}
		
		deallocate();
	}
	
	protected void render_triangles() {
		if(triangleCount > 0) {
			PSPolygon shape = PSPolygon.fromProcessing(sunflow, vertices, vertexCount, triangles, triangleCount);
			shape.setShader(currentFillShader());
			shapes.add(shape);
		}
	}
	
	
	protected void render_lines() {
		if(lineCount > 0) {
			PSLine line = PSLine.fromProcessing(sunflow, vertices, vertexCount, lines, lineCount, strokeWeight);
			line.addTransform(forwardTransform);
			line.setShader(currentFillShader());
			shapes.add(line);
		}
	}
	

	private void setupCamera() {	    
	    camera.setEye(0, 0, cameraZ);
	    camera.setTarget(0, 0, 0);
	    camera.setUp(0, 1, 0);
	    
	    camera.setFieldOfView(cameraFOV);
	    camera.setAspectRatio((float)width/height);
	    
	    camera.set();
	}
	
	
	//////////////////////////////////////////////////////////////////
	// Background Methods
	public void background(int i) {
		float v = i/255f;
		background(v, v, v);
	}
	
	public void background(int r, int g, int b) {
		background(r/255f, g/255f, b/255f);
	}
	
	public void background(float r, float g, float b) {
		if(r > 1 || g > 1 || b > 1) {
			r /= 255f;
			g /= 255f;
			b /= 255f;
		}
		scene.background(r, g, b);
	}
	

	protected void clear() {
		for(int i=0; i<pixelCount; i++) {
			pixels[i] = backgroundColor;
		}
	}

	//////////////////////////////////////////////////////////////////
	// Optimized Shape Methods
	public void sphere(float radius) {
		PSSphere sphere = new PSSphere(sunflow, radius);
		sphere.setShader(currentFillShader());
		sphere.addTransform(forwardTransform);
		shapes.add(sphere);
	}
	

	//////////////////////////////////////////////////////////////////
	// Overridden methods to ensure rendering has happened before
	// we try to grab pixels in the project
	
	public void save(String fileName) {
		if(!hasRenderedFrame) {
			render();
		}
		super.save(fileName);
	}
	
	//////////////////////////////////////////////////////////////////
	// Lighting

	@Deprecated
	public void pointlight(float r, float g, float b, float x, float y, float z) {
		PSLight point = new PSLight(sunflow, PSLight.POINT, 100, x, y, z);
		lights.add(point);
		lightCount++;
	}
	
	//////////////////////////////////////////////////////////////////
	// Shaders

	private PSShader currentFillShader() {
		PSShader shader = new PSShader(sunflow, calcA, calcR, calcG, calcB, shininess);
		if(!shaders.keySet().contains(shader.getHash())) {
			shader.set( scene.isGlobalIllumination() );
			shaders.put(shader.getHash(), shader);
		}
		currentShader = shader;
		return currentShader;
	}
	
	//////////////////////////////////////////////////////////////////
	// Debugging Methods
	public void dumpScene() {
		dumpScene = true;
	}
	
	public void dumpScene(String fileName) {
		dumpScene();
		dumpingFileName = fileName;
	}
	
	public void dump(BufferedWriter writer) {
		System.out.println("Memdump Start");
		SceneDumper dumper = new SceneDumper(this, writer);
		
		setupCamera();
		
		dumper.add(scene);
		dumper.add(camera);
		
		for(PSLight light : lights) {
			dumper.add(light);
		}
		
		for(PSShader shader: shaders.values()) {
			dumper.add(shader);
		}
		
		for(PSShape shape : shapes) {
			dumper.add(shape);
		}
		
		System.out.println("Done Memdump -- writing");
		try {
			dumper.writeScene();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done that");
	}

	public void showPreview(boolean preview) {
		this.showPreview = preview;
	}
	
	public void showPreview() {
		showPreview(true);
	}

	public void dump(String fileName) {
		try {
			FileWriter fWriter = new FileWriter(fileName);
			BufferedWriter buffer = new BufferedWriter(fWriter);
			dump(buffer);
			buffer.close();
			fWriter.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
}

