package hipstersinc.sunflow;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class P5SunflowPreview extends Applet {
	private static final long serialVersionUID = 1217073147340426413L;
	private int width, height;
	
	public void setup(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void paint(Graphics g) {
		System.out.println("Tick!");
	}
	
	public void init() {
		this.setBackground(Color.GRAY);
	}
}
