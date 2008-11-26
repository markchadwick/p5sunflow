package hipstersinc.sunflow.shape;

import hipstersinc.sunflow.TestP5SunflowUtil;
import hipstersinc.sunflow.shader.PSShader;

import org.sunflow.math.Matrix4;

public class TestPSLine extends TestP5SunflowUtil {
	
	//////////////////////////////////////////////////////////////////
	// Dumping
	public void testDefaultDumping() {
		PSSphere sphere = new PSSphere(sunflow);
		PSShader shader = new PSShader(sunflow, 1, 1, 1, 1, 1);
		sphere.setShader(shader);
		assertNotNull(sphere);

		float[] shaderDark = {0, 0, 0}; 
		float[] shaderBright = {1, 1, 1};
		String[] shaders = {"shader-10-10-10-10"};
		
		// Setup Shader
		sfMock.expects(once()).method("parameter").with(eq("dark"), eq(null), eq(shaderDark));
		sfMock.expects(once()).method("parameter").with(eq("bright"), eq(null), eq(shaderBright));
		sfMock.expects(once()).method("parameter").with(eq("samples"), eq(128));
		sfMock.expects(once()).method("parameter").with(eq("maxdist"), eq(10f));
		sfMock.expects(once()).method("shader").with(eq("shader-10-10-10-10"), eq("ambient_occlusion"));
		
		// Setup Sphere
		sfMock.expects(once()).method("parameter").with(eq("shaders"), eq(shaders));
		sfMock.expects(once()).method("parameter").with(eq("transform"), isA(Matrix4.class));
		sfMock.expects(once()).method("geometry").with(eq(sphere.getName()), eq("sphere"));
		
		// Instance Sphere
		sfMock.expects(once()).method("instance").with(eq(sphere.getInstanceName()), eq(sphere.getName()));
		
		setScene( shader.toScene().append(sphere.toScene()).toString() );
	}
}
