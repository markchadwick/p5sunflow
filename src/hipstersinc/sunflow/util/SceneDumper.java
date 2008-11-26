package hipstersinc.sunflow.util;

import hipstersinc.P5Sunflow;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;


public class SceneDumper {
	protected P5Sunflow p5sunflow;
	protected BufferedWriter buffer;
	protected List<SceneDumpable> sceneItems;
	private static int blockDepth = 0;
	private static StringBuffer currentBuffer;
	
	public SceneDumper(P5Sunflow p5sunflow, BufferedWriter buffer) {
		this.p5sunflow = p5sunflow;
		this.buffer = buffer;
		sceneItems = new ArrayList<SceneDumpable>();
	}
	
	public StringBuffer getScene() {
		StringBuffer buf = new StringBuffer();
		
		for(SceneDumpable sceneItem : sceneItems) {
			buf.append( sceneItem.toScene() );
		}
		
		return buf;
	}

	public void writeScene() throws IOException {
		for(SceneDumpable sceneItem : sceneItems) {
			buffer.write(sceneItem.toScene().toString());
		}
	}
	
	public void add(SceneDumpable sceneItem) {
		sceneItems.add(sceneItem);
	}
	
	
	//////////////////////////////////////////////////////////////////
	// Utility Stuff
	public static void comment(String s, StringBuffer buf) {
		buf.append( String.format("%%\n%% %s\n%%\n", s) );
	}
	
	public static void param(String param, String value, StringBuffer buf) {
		if(value != null)
			buf.append( String.format("%s%s %s\n", spacing(), param, value) );
	}
	
	public static void param(String param, String[] values, StringBuffer buf) {
		if(values != null  && values.length > 0) {
			buf.append( String.format("%s%s", spacing(), param));
			for(int i=0; i<values.length; i++) {
				buf.append( String.format(" %s", values[i]));
			}
			buf.append("\n");
		}
	}
	
	public static void param(String param, Float value, StringBuffer buf) {
		if(value != null)
			buf.append( String.format("%s%s %f\n", spacing(), param, value) );
	}
	
	public static void param(String param, Integer value, StringBuffer buf) {
		if(value != null)
			buf.append( String.format("%s%s %d\n", spacing(), param, value) );
	}
	
	public static void param(String param, Point3 value, StringBuffer buf) {
		if(value != null)
			buf.append( String.format("%s%s %f %f %f\n", spacing(), param, value.x, value.y, value.z) );
	}
	
	public static void param(String param, Vector3 value, StringBuffer buf) {
		if(value != null)
			buf.append( String.format("%s%s %.0f %.0f %.0f\n", spacing(), param, value.x, value.y, value.z) );
	}
	
	public static void param(String param, int[] values, StringBuffer buf) {
		StringBuffer ints = new StringBuffer();
		if(values != null) {
			
			for(int value : values) {
				ints.append( String.format("%d ", value) );
			}
			buf.append( String.format("%s%s %s\n", spacing(), param, ints.toString()) );
		}
	}
	
	public static void param(String param, float[] values, StringBuffer buf) {
		StringBuffer floats = new StringBuffer();
		if(values != null) {
			
			for(float value : values) {
				floats.append( String.format("%f ", value) );
			}
			buf.append( String.format("%s%s %s\n", spacing(), param, floats.toString()) );
		}
	}
	
	public static void param3f(List<Float[]> values, StringBuffer buf) {
		if(values == null) {
			return;
		}
		
		for(int i=0; i<values.size(); i++) {
			Float[] v = values.get(i);
			buf.append( String.format("%s%f %f %f\n", spacing(1),
					v[0], v[1], v[2]));
		}
	}
	
	public static void param3i(List<Integer[]> values, StringBuffer buf) {
		if(values == null) {
			return;
		}
		
		for(int i=0; i<values.size(); i++) {
			Integer[] v = values.get(i);
			buf.append( String.format("%s%d %d %d\n", spacing(1),
					v[0], v[1], v[2]));
		}
	}
	
	public static void paramColor(float r, float g, float b, StringBuffer buf) {
		buf.append( String.format("%scolor { \"sRGB nonlinear\" %f %f %f }\n", spacing(), r, g, b));
	}
	
	public static void startBlock(String name, StringBuffer buf) {
		buf.append( String.format("%s {\n", name) );
		blockDepth++;
		currentBuffer = buf;
	}
	
	public static void endBlock() {
		blockDepth--;
		currentBuffer.append("}\n\n");
	}
	
	private static String spacing(int offset) {
		if(currentBuffer == null) {
			return "";
		}
		StringBuffer spacing = new StringBuffer();
		for(int i=0; i<blockDepth+offset; i++) {
			spacing.append("  ");
		}
		return spacing.toString();
	}
	
	private static String spacing() {
		return spacing(0);
	}
	
}
