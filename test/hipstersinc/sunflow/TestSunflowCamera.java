package hipstersinc.sunflow;

import org.sunflow.math.Matrix4;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

public class TestSunflowCamera extends TestP5SunflowUtil {
	
	//////////////////////////////////////////////////////////////////
	// Dumping
	public void testDefaultDumping() {
		SunflowCamera camera = new SunflowCamera("thinlens", sunflow);
		camera.setEye(new Point3(12, 1, 1));
		camera.setTarget(new Point3(1, 1, 1));
		camera.setUp(new Vector3(0, 1, 0));
		camera.setFieldOfView(2);
		camera.setAspectRatio(3);
		camera.setFocalDistance(4);
		camera.setLensRadius(5);
		
		sfMock.expects(once()).method("parameter").with(eq("transform"), isA(Matrix4.class));
		sfMock.expects(once()).method("parameter").with(eq("fov"), isA(Float.class));
		sfMock.expects(once()).method("parameter").with(eq("aspect"), eq(3f));
		sfMock.expects(once()).method("parameter").with(eq("focus.distance"), eq(4f));
		sfMock.expects(once()).method("parameter").with(eq("lens.radius"), eq(5f));
		
		sfMock.expects(once()).method("camera").with(eq("@sc_1::camera_1"), eq("thinlens"));
		sfMock.expects(once()).method("parameter").with(eq("camera"), eq("@sc_1::camera_1"));
		
		sfMock.expects(once()).method("options").with(eq("::options"));
		
		setScene( camera.toScene().toString() );
	}
}
