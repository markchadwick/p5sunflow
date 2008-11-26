package hipstersinc.sunflow.shape;

import java.util.ArrayList;
import java.util.List;

import hipstersinc.sunflow.TestP5SunflowUtil;
import hipstersinc.sunflow.shader.PSShader;

public class TestPSPolygon extends TestP5SunflowUtil {
	
	public void testDefaultDumping() {
		PSPolygon poly = new PSPolygon(sunflow);
		PSShader shader = new PSShader(sunflow, 1, 1, 1, 1, 1);
		poly.setShader(shader);
		
		List<Float[]> points = new ArrayList<Float[]>();
		List<Integer[]> triangles = new ArrayList<Integer[]>();
		
		Float[] point1 = {0f, -10f, 10f};
		Float[] point2 = {10f, 10f, 6f};
		Float[] point3 = {-10f, 10f, 2f};
		Float[] point4 = {20f, 0f, 0f};
		points.add(point1);
		points.add(point2);
		points.add(point3);
		points.add(point4);
		
		Integer[] triangle1 = {0, 1, 2};
		Integer[] triangle2 = {0, 3, 1};
		triangles.add(triangle1);
		triangles.add(triangle2);
		
		poly.setVertices(points);
		poly.setTriangles(triangles);
		
		
		float[] shaderDark = {0, 0, 0}; 
		float[] shaderBright = {1, 1, 1};
		String[] shaders = {"shader-10-10-10-10"};
		
		// Setup Shader
		sfMock.expects(once()).method("parameter").with(eq("dark"), eq(null), eq(shaderDark));
		sfMock.expects(once()).method("parameter").with(eq("bright"), eq(null), eq(shaderBright));
		sfMock.expects(once()).method("parameter").with(eq("samples"), eq(128));
		sfMock.expects(once()).method("parameter").with(eq("maxdist"), eq(100f));
		sfMock.expects(once()).method("shader").with(eq("shader-10-10-10-10"), eq("ambient_occlusion"));
		
		setScene( shader.toScene().append(poly.toScene()).toString() );
		
		
//		PSSphere sphere = new PSSphere(sunflow);
//		PSShader shader = new PSShader(sunflow, 1, 1, 1, 1, 1);
//		sphere.setShader(shader);
//		assertNotNull(sphere);
//
//		float[] shaderDark = {0, 0, 0}; 
//		float[] shaderBright = {1, 1, 1};
//		String[] shaders = {"shader-10-10-10-10"};
//		
//		// Setup Shader
//		sfMock.expects(once()).method("parameter").with(eq("dark"), eq(null), eq(shaderDark));
//		sfMock.expects(once()).method("parameter").with(eq("bright"), eq(null), eq(shaderBright));
//		sfMock.expects(once()).method("parameter").with(eq("samples"), eq(128));
//		sfMock.expects(once()).method("parameter").with(eq("maxdist"), eq(10f));
//		sfMock.expects(once()).method("shader").with(eq("shader-10-10-10-10"), eq("ambient_occlusion"));
//		
//		// Setup Sphere
//		sfMock.expects(once()).method("parameter").with(eq("shaders"), eq(shaders));
//		sfMock.expects(once()).method("parameter").with(eq("transform"), isA(Matrix4.class));
//		sfMock.expects(once()).method("geometry").with(eq(sphere.getName()), eq("sphere"));
//		
//		// Instance Sphere
//		sfMock.expects(once()).method("instance").with(eq(sphere.getInstanceName()), eq(sphere.getName()));
//		
//		setScene( shader.toScene().append(sphere.toScene()).toString() );
	}
}
