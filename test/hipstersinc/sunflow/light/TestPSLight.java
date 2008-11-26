package hipstersinc.sunflow.light;

import org.sunflow.math.Point3;

import hipstersinc.sunflow.TestP5SunflowUtil;

public class TestPSLight extends TestP5SunflowUtil {
	
	public void testPointLight() {
		PSLight light = new PSLight(sunflow, PSLight.POINT);

		float[] power = {3, 3, 3};
		
		sfMock.expects(once()).method("parameter").with(eq("center"), isA(Point3.class));
		sfMock.expects(once()).method("parameter").with(eq("power"), eq(null), eq(power));
		sfMock.expects(once()).method("light").with(eq("@sc_1::pointlight_1"), eq("point"));
		
		setScene(light.toScene().toString());
	}
}
