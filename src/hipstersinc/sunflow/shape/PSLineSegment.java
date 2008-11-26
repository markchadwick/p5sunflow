package hipstersinc.sunflow.shape;

import org.sunflow.SunflowAPI;
import org.sunflow.math.Matrix4;

import processing.core.PConstants;

class PSLineSegment extends PSShape {
	
	private float x0, y0, z0;
	private float x1, y1, z1;
	private float width;
	
	public PSLineSegment(SunflowAPI sunflow, Float[] start, Float[] end, float width) {
		super(sunflow);
		
		this.width = width;
		
		this.x0 = start[0];
		this.y0 = start[1];
		this.z0 = start[2];
		
		this.x1 = end[0];
		this.y1 = end[1];
		this.z1 = end[2];
	}

	public void oldDraw() {
		float dx = x0 - x1;
		float dy = y0 - y1;
		float dz = z0 - z1;
		float length = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
		
		float xRot = (float) (Math.atan2(z1-z0, y1-y0));
		float yRot = (float) (Math.atan2(x1-x0, z1-z0));
		float zRot = (float) (Math.atan2(y1-y0, x1-x0));

		System.out.println( String.format("x Rotation: %f", xRot/PConstants.TWO_PI) );
		System.out.println( String.format("Y Rotation: %f", yRot/PConstants.TWO_PI) );
		System.out.println( String.format("Z Rotation: %f", zRot/PConstants.TWO_PI) );

		Matrix4 translate = Matrix4.IDENTITY.multiply( Matrix4.translation((x0+x1)/2, (y0+y1)/2, (z0+z1)/2) );
		Matrix4 scale = Matrix4.IDENTITY.multiply( Matrix4.scale(width, width, length/2) );
		Matrix4 rotate = Matrix4.IDENTITY
		.multiply( Matrix4.rotateZ(zRot) )
		.multiply( Matrix4.rotateX(xRot) )
		.multiply( Matrix4.rotateY(yRot) );
		
		Matrix4 m = Matrix4.IDENTITY;
		m = scale.multiply(m);
		m = rotate.multiply(m);
		m = translate.multiply(m);
		

		
		sunflow.geometry(name, "cylinder");
		sunflow.parameter("transform", m);
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}
	
	@Override
	public void draw() {
		float[] points = {x0, y0, z0, x1, y1, z1};
		sunflow.parameter("segments", 1);
		sunflow.parameter("widths", width*2);
		sunflow.parameter("points", "point", "vertex", points);
		sunflow.geometry(name, "hair");
		
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}

	public void draw1() {
		float dx = x0 - x1;
		float dy = y0 - y1;
		float dz = z0 - z1;
		float length = (float) Math.sqrt(dx*dx + dy*dy + dz*dz);
		
		float xRot = (float) (Math.atan2(z1-z0, y1-y0));
		float yRot = (float) (Math.atan2(x1-x0, z1-z0));
		float zRot = (float) (Math.atan2(y1-y0, x1-x0));

		Matrix4 translate = Matrix4.IDENTITY.multiply( Matrix4.translation((x0+x1)/2, (y0+y1)/2, (z0+z1)/2) );
		Matrix4 scale = Matrix4.IDENTITY.multiply( Matrix4.scale(width, width, length/2) );
		Matrix4 rotate = Matrix4.IDENTITY
		.multiply( Matrix4.rotateZ(zRot) )
		.multiply( Matrix4.rotateX(xRot) )
		.multiply( Matrix4.rotateY(yRot) );
		
		Matrix4 m = Matrix4.IDENTITY;
		m = scale.multiply(m);
		m = rotate.multiply(m);
		m = translate.multiply(m);

		sunflow.geometry(name, "cylinder");
		sunflow.parameter("transform", m);
		sunflow.parameter("shaders", getShaderNames());
		sunflow.instance(getInstanceName(), name);
	}
	
	public StringBuffer toScene() {
		return new StringBuffer("% UNIMPL - Line Segment\n\n");
	}
}
