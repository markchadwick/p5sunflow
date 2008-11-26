package hipstersinc.sunflow.shape;

import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;

public class PSLine extends PSShape {
	private float width;

	public PSLine(SunflowAPI sunflow) {
		super(sunflow);
	}

	public static PSLine fromProcessing(SunflowAPI sunflow,
			float[][] vertices, int vertexCount,
			int[][] lines, int lineCount, float width) {
		PSLine line = new PSLine(sunflow);
		line.setVertices(vertices, vertexCount);
		line.setWidth(width);
		line.setLines(lines, lineCount);
		return line;
	}
	
	@Override
	public void draw() {
		float[] points = new float[vertices.size() * 3];
		for(int i=0; i<vertices.size(); i++) {
			Float[] p = vertices.get(i);
			points[i*3 + 0] = p[0];
			points[i*3 + 1] = p[1];
			points[i*3 + 2] = p[2];
		}
		
		sunflow.parameter("segments", vertices.size()-1);
		sunflow.parameter("widths", width*2);
		sunflow.parameter("points", "point", "vertex", points);
		sunflow.geometry(name, "hair");
		
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer("% Oh hai.  I'm a line\n");
		SceneDumper.startBlock("object", buf);
		SceneDumper.param("shader", getShaderNames()[0], buf);
		SceneDumper.param("type", "hair", buf);
		SceneDumper.param("segments", vertices.size()-1, buf);
		SceneDumper.param("width", width*2, buf);
		
		SceneDumper.param("points", vertices.size()*3, buf);
		SceneDumper.param3f(vertices, buf);
		SceneDumper.endBlock();
		return buf;
	}
}
