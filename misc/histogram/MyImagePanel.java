/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package histogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

class MyImagePanel extends JPanel {
	
	private static final long serialVersionUID = 2101081238869295297L;
	private int rawWidth;
	private int rawHeight;
	private BufferedImage image;
	private ImageIcon imageIcon;
	private JLabel imageWindow;
	private int displayWidth;
	private int displayHeight;

	public BufferedImage getBufferedImage() { return image; }
	
	public int getDisplayWidth() { return displayWidth; }
	public int getDisplayHeight() { return displayHeight; }
	public MyImagePanel(int displayWidth, int displayHeight) {
		setMaximumSize(new Dimension(displayWidth, displayHeight));
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
	}
	public BufferedImage createTestImage (int iWidth, int iHeight) {
		System.out.println("createTestImage entered \n");
		rawWidth = iWidth; rawHeight = iHeight;
		displayWidth = iWidth; displayHeight = iHeight;
		image = new BufferedImage(displayWidth, displayHeight, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		ColorModel model = image.getColorModel();
		Color fractalColor = Color.red;
		int argb = fractalColor.getRGB();
		
		Object colorData = model.getDataElements(argb, null);
		for (int i = 0; i < displayWidth; i++) {
			for (int j = 0; j < displayHeight ; j++) {
				//fractalColor = new Color(((i)%256), ((j)%256), ((i*j)%256));
				fractalColor = new Color((((j%(i+1)) - (i%(j+1)))+displayHeight)%128, 
						(((j%(i+1)) + (i%(j+1)))+displayHeight)%128, (((j%(i+1)) + (i%(j+1)))+displayHeight)%128);
				argb = fractalColor.getRGB();
				colorData = model.getDataElements(argb, null);
				raster.setDataElements(i,j,colorData);
			}
		}
		imageIcon = new ImageIcon(image);
		imageWindow = new JLabel(imageIcon);
		this.add(imageWindow, BorderLayout.CENTER);
		return image;
	}
	public BufferedImage create2dHistogram(double[][] xy, double x_step, double x_min, double x_max,
			double y_step, double y_min, double y_max, boolean logPlot) {
		
		int imageWidth = (int) (Math.round((x_max-x_min)/x_step)+1);
		int imageHeight = (int) (Math.round((y_max-y_min)/y_step)+1);
		
		double[][] pixels = new double[xy.length][xy[0].length];
		rawWidth = imageWidth;
		rawHeight = imageHeight;
		image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		WritableRaster raster = image.getRaster();
		ColorModel model = image.getColorModel();
		Color fractalColor = Color.red;
		int argb = fractalColor.getRGB();
		Object colorData = model.getDataElements(argb, null);

		if(logPlot) {
			for(int a = 0; a < pixels.length; a++) {
	       		for(int b = 0; b < pixels[a].length; b++) {
	       			pixels[a][b] = Math.max(0, Math.log(xy[a][b]));
	       		}
			}
		}
		
		double maxVal = 0;
		for(int a = 0; a < pixels.length; a++) {
       		for(int b = 0; b < pixels[a].length; b++) {
       			if(pixels[a][b] > maxVal) { maxVal = pixels[a][b]; }
       		}
		}
		int idx = 0;
       	for(int a = 0; a < pixels.length; a++) {
       		for(int b = pixels[a].length-1; b >= 0; b--) {
            	int color = (int)((double)pixels[a][b] / (double)maxVal *255);
            	color = 255-color;
//            	System.out.println(color);
    			fractalColor = new Color(color, color, color);
    			argb = fractalColor.getRGB();
    			colorData = model.getDataElements(argb, null);
    			raster.setDataElements(a, pixels[a].length-1-b, colorData);
       		}
       	}

        
		imageIcon.setImage(image.getScaledInstance(displayWidth, displayHeight, 8));
		imageWindow.getGraphics().drawImage(image, 0, 0, displayWidth, displayHeight, 0, 0, rawWidth, rawHeight, null);
		return image;
	}
}