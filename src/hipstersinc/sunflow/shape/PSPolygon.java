package hipstersinc.sunflow.shape;

import hipstersinc.sunflow.util.SceneDumper;

import java.util.ArrayList;

import org.sunflow.SunflowAPI;

public class PSPolygon extends PSShape {

	public PSPolygon(SunflowAPI sunflow) {
		super(sunflow);
		
		this.vertices = new ArrayList<Float[]>();
		this.triangles = new ArrayList<Integer[]>();
	}

	public static PSPolygon fromProcessing(SunflowAPI sunflow,
			float[][] vertices, int vertexCount,
			int[][] triangles, int triangleCount) {
		PSPolygon polygon = new PSPolygon(sunflow);
		polygon.setVertices(vertices, vertexCount);
		polygon.setTriangles(triangles, triangleCount);
		return polygon;
	}

	@Override
	public void draw() {
		sunflow.parameter("points", "point", "vertex", flatten(vertices));
		sunflow.parameter("triangles", flatten(triangles));
		sunflow.geometry(name, "triangle_mesh");
		
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}
	
	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer();
		
		SceneDumper.startBlock("object", buf);
		SceneDumper.param("shader", getShaderNames()[0], buf);
		SceneDumper.param("type", "generic-mesh", buf);
		
		SceneDumper.param("points", vertices.size(), buf);
		SceneDumper.param3f(vertices, buf);
		
		SceneDumper.param("triangles", triangles.size(), buf);
		SceneDumper.param3i(triangles, buf);
	
		SceneDumper.param("normals", "none", buf);
		SceneDumper.param("uvs", "none", buf);
		SceneDumper.endBlock();
		
		return buf;
	}
}
