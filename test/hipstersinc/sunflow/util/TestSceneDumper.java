package hipstersinc.sunflow.util;

import org.sunflow.SunflowAPI;

import junit.framework.TestCase;

public class TestSceneDumper extends TestCase {
	protected SunflowAPI sunflow;
	
	public void testStringParam() {
		StringBuffer buf = new StringBuffer();
		SceneDumper.param("param", "value", buf);
		assertEquals("  param value\n", buf.toString());
	}
}
