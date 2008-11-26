package hipstersinc.sunflow;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import hipstersinc.P5Sunflow;

import org.sunflow.core.Display;
import org.sunflow.image.Color;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ProcessingDisplay implements Display {
	protected PApplet applet;
	protected PGraphics g;
	protected P5Sunflow renderer;
	
	private boolean showPreview = false;
	private JFrame previewFrame;
	private P5SunflowPreview preview;
	
	protected int width, height;
	
	public ProcessingDisplay(PApplet applet, P5Sunflow renderer) {
		this.applet = applet;
		
		if(applet != null) {
			this.g = applet.g;
		}
		
		this.renderer = renderer;
	}
	
	public void imageBegin(int w, int h, int bucketSize) {
		this.width = renderer.width;
		this.height = renderer.height;
		renderer.loadPixels();
		
		if(showPreview) {
			setupPreview();
		}
	}

	public void imageEnd() {
		renderer.updatePixels();
		
		if(showPreview) {
			teardownPreview();
		}
	}

	public void imageFill(int x, int y, int w, int h, Color c, float alpha) {
		int color = p5Color(c, alpha);
		for(int i=0; i<renderer.pixelCount; i++) { renderer.pixels[i] = color; }
	}

	public void imagePrepare(int x, int y, int w, int h, int id) {
	}

	public void imageUpdate(int x, int y, int w, int h, Color[] data, float[] alpha) {
		int[] pixels = renderer.pixels;
		int loopCount = 0;
		for(int yPos=y; yPos < (y+h); yPos++) {
			int thisY = (height-1) - yPos;
			for(int xPos=x; xPos < (x+w); xPos++) {
				int index = (thisY * width) + xPos;
				setPixel(pixels, index, data[loopCount], alpha[loopCount]);
				loopCount++;
			}
		}
		
		if(showPreview) {
			updatePreview(pixels);
		}
	}
	
	private void setPixel(int[] pixels, int i, Color c, float alpha) {
		if(alpha == 1)
			pixels[i] = p5Color(c, alpha);
		else {
			pixels[i] = p5Color(c, alpha);
		}
	}

	private int p5Color(Color c, float alpha) {
		float[] colors = c.getRGB();

		int a = (int)Math.min((alpha * 255), 255);
		int r =	(int)Math.min((colors[0] * 255), 255);
		int g =	(int)Math.min((colors[1] * 255), 255);
		int b = (int)Math.min((colors[2] * 255), 255);
		
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
	
	public void showPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}
	
	
	//////////////////////////////////////////////////////////////////
	// Preview Window Methods
	private void setupPreview() {
		if(previewFrame == null && preview == null) {
			previewFrame = new JFrame("P5Sunflow Preview");
			previewFrame.setLayout(new BorderLayout());
			preview = new P5SunflowPreview();
			previewFrame.add(preview, BorderLayout.CENTER);
			previewFrame.setSize(width, height);
			previewFrame.setVisible(true);
			preview.setup(width, height);
			preview.init();
		} else {
			preview.setup(width, height);
		}
	}
	
	private void teardownPreview() {
	}
	
	private void updatePreview(int[] pixels) {
//		preview.update(pixels);
//		preview.redraw();
	}
}
