package hipstersinc.sunflow.shape;

import hipstersinc.sunflow.util.SceneDumper;

import org.sunflow.SunflowAPI;
import org.sunflow.math.Matrix4;

public class PSSphere extends PSShape {
	private float radius;
	private float x, y, z;
	private boolean shouldTransform = false;
	
	public PSSphere(SunflowAPI sunflow) {
		super(sunflow);
		this.radius = 10;
	}
	
	public PSSphere(SunflowAPI sunflow, float radius, float x, float y, float z) {
		this(sunflow);
		this.radius = radius;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public PSSphere(SunflowAPI sunflow, float radius) {
		this(sunflow, radius, 0, 0, 0);
		shouldTransform = true;
	}

	public void draw() {
		if(shouldTransform) {
			transformSphere();
		}
		
		sunflow.geometry(name, "sphere");
		sunflow.parameter("transform", Matrix4.translation(x, y, z).multiply(Matrix4.scale(radius)));
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}
	
	private void transformSphere() {
		float[] transformedPoints = transform(x, y, z);
		x = transformedPoints[0];
		y = transformedPoints[1];
		z = transformedPoints[2];
	}


	public StringBuffer toScene() {
		StringBuffer buf = new StringBuffer();
		
		transformSphere();
		
		float[] center = {x, y, z};
		
		SceneDumper.startBlock("object", buf);
		SceneDumper.param("shader", getShaderNames()[0], buf);
		SceneDumper.param("type", "sphere", buf);
		SceneDumper.param("name", name, buf);
		SceneDumper.param("c", center, buf);
		SceneDumper.param("r", radius, buf);
		SceneDumper.endBlock();
		return buf;
	}
}
