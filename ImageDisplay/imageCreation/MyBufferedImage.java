/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package imageCreation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import colorScheme.ColorScheme;

import filters.ImageFilter;

public class MyBufferedImage {

	enum Property { ImageFilter, ColorType }
	private BufferedImage bi;
	private ImageFilter imageFilter;
	private ColorScheme colorScheme;
	public void fill(double[][] data) {
		double[][] filteredData = imageFilter.apply(data);
		WritableRaster raster = bi.getRaster();
		ColorModel model = bi.getColorModel();
		Color color;
		Object colorData;
		double I = 0;
		for(int i = 0; i < filteredData.length; i++) {
			for(int j = 0; j < filteredData[i].length; j++) {
				I = filteredData[i][j];
				color = colorScheme.getColor(I);
				colorData = model.getDataElements(color.getRGB(), null);
				raster.setDataElements(i, j, colorData);
			}
		}
	}
	
	public void setProperty(Property aProperty, Object toSet) {
		switch(aProperty) {
		case ImageFilter:
			imageFilter = (ImageFilter) toSet;
			break;
		case ColorType:
			colorScheme = (ColorScheme) toSet;
			break;
		}
	}
}
