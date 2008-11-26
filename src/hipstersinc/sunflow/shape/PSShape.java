package hipstersinc.sunflow.shape;

import hipstersinc.sunflow.shader.PSShader;
import hipstersinc.sunflow.util.SceneDumpable;

import java.util.ArrayList;
import java.util.List;

import org.sunflow.SunflowAPI;

import processing.core.PGraphics;
import processing.core.PMatrix;

public abstract class PSShape implements SceneDumpable {
	//////////////////////////////////////////////////////////////////
	// Sunflow Specifics
	protected SunflowAPI sunflow;
	protected int id;
	protected String name;
	
	//////////////////////////////////////////////////////////////////
	// Shaders
	protected List<PSShader> shaders;
	
	//////////////////////////////////////////////////////////////////
	// Spacial
	protected PMatrix transform;
	
	//////////////////////////////////////////////////////////////////
	// Shape Implementation
	List<Float[]> vertices;
	List<Integer[]> triangles;
	List<Integer[]> lines;

	/**
	 * Generic Shape Constructor
	 * @param sunflow
	 */
	public PSShape(SunflowAPI sunflow) {
		this.sunflow = sunflow;
		this.id = this.hashCode();
		this.shaders = new ArrayList<PSShader>();
		this.transform = new PMatrix();
		
		generateName();
	}

	public abstract void draw();
	
	
	//////////////////////////////////////////////////////////////////
	// Shaders
	
	public void setShaders(List<PSShader> shaders) {
		this.shaders = shaders;
	}
	
	public void setShader(PSShader shader) {
		this.shaders = new ArrayList<PSShader>();
		this.shaders.add(shader);
	}
	
	public List<PSShader> getShaders() {
		return shaders;
	}
	
	protected String[] getShaderNames() {
		String[] shaderStrings = new String[shaders.size()];
		for(int i=0; i<shaders.size(); i++) {
			shaderStrings[i] = shaders.get(i).getName();
		}
		return shaderStrings;
	}

	
	//////////////////////////////////////////////////////////////////
	// Convertion from 2d to 1d datasets
	
	protected float[] flatten(List<Float[]> in) {
		if(in.size() < 1) {
			return new float[0];
		}
		
		int size = in.get(0).length;
		float[] out = new float[in.size() * size];
		for(int i=0; i<in.size(); i++) {
			Float[] segment = in.get(i);
			for(int j=0; j<size; j++) {
				out[size*i + j] = segment[j];
			}
		}
		return out;
	}
	

	protected int[] flatten(List<Integer[]> in) {
		if(in.size() < 1) {
			return new int[0];
		}
		
		int size = in.get(0).length;
		int[] out = new int[in.size() * size];
		for(int i=0; i<in.size(); i++) {
			Integer[] segment = in.get(i);
			for(int j=0; j<size; j++) {
				out[size*i + j] = segment[j];
			}
		}
		return out;
	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// Transforms
	
	public void addTransform(PMatrix matrix) {
		this.transform.apply(matrix);
	}
	
	protected float[] transform(float x, float y, float z) {
		float[] in = {x, y, z};
		return transform(in);
	}
	
	protected Float[] transform(Float[] points) {
		float[] in = {points[0], points[1], points[2]};
		float[] out = new float[3];
		transform.mult3(in, out);
		return new Float[]{out[0], out[1], out[2]};
	}

	protected float[] transform(float[] in) {
		float[] out = new float[3];
		transform.mult3(in, out);
		return out;
	}

	
	//////////////////////////////////////////////////////////////////
	// Getters & Settings
	
	protected String getInstanceName() {
		return String.format("%s.instance", this.name);
	}
	
	public List<Float[]> getVertices() {
		return vertices;
	}

	public void setVertices(List<Float[]> vertices) {
		this.vertices = vertices;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * This assumes we're talking about vertices coming straight from Processing.
	 * @param vertices
	 * @param triangleCount
	 */
	protected void setVertices(float[][] vertices, int vertCount) {
		this.vertices = new ArrayList<Float[]>();
		
		for(int i=0; i<vertCount; i++) {
			Float[] p = {
					vertices[i][PGraphics.VX],
					vertices[i][PGraphics.VY],
					vertices[i][PGraphics.VZ]
			};
			this.vertices.add(p);
		}
	}

	/**
	 * This assumes we're talking about triangles coming straight from Processing.
	 * @param triangles
	 * @param triangleCount
	 */
	protected void setTriangles(int[][] triangles, int triangleCount) {
		this.triangles = new ArrayList<Integer[]>();
		
		for(int i=0; i<triangleCount; i++) {
			Integer[] t = {
					triangles[i][1],
					triangles[i][2],
					triangles[i][3]
			};
			
			this.triangles.add(t);
		}
	}
	
	/**
	 * This assumes we're talking about lines coming straight from Processing.
	 * @param lines
	 * @param lineCount
	 */
	protected void setLines(int[][] lines, int lineCount) {
		this.lines = new ArrayList<Integer[]>();
		
		for(int i=0; i<lineCount; i++) {
			Integer[] l = {
					lines[i][1],
					lines[i][2],
					lines[i][3]
			};
			
			this.lines.add(l);
		}
	}
	
	public List<Integer[]> getTriangles() {
		return triangles;
	}

	public void setTriangles(List<Integer[]> triangles) {
		this.triangles = triangles;
	}
	
	
	
	//////////////////////////////////////////////////////////////////
	// Utility
	
	private void generateName() {
		this.name = String.format("%s_%d", getClass().getSimpleName(), this.id);
	}
}
