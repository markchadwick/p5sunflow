package hipstersinc.sunflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.sunflow.SunflowAPI;
import org.sunflow.SunflowAPIInterface;
import org.sunflow.core.parser.SCParser;

public class TestP5SunflowUtil extends MockObjectTestCase {
	protected SunflowAPI sunflow;
	protected SunflowAPIInterface sunflowMock;
	protected Mock sfMock;
	protected SCParser parser;
	
	public void setUp() {
		sunflow = new SunflowAPI();
		parser = new SCParser();
		setupSunflowMock();
	}
	
	public void setScene(String s) {
		System.out.println(s);
		try {
			File f = File.createTempFile("p5sunflowtest", ".sc");
			FileWriter fw = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
			fw.close();
			
			parser.parse(f.getAbsolutePath(), sunflowMock);
			f.delete();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private void setupSunflowMock() {
		sfMock = mock(SunflowAPIInterface.class);
		sunflowMock = (SunflowAPIInterface) sfMock.proxy();
	}
}
