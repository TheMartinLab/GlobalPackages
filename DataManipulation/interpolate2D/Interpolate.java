package interpolate2D;

import io.StringConverter;
import equations_2d.Equation2;
import equations_2d.Gaussian2;

public class Interpolate {

	private static int[] getBounds(double val, double[] axis) {
		int idx = 0;
		
		if(val <= axis[0])
			return new int[] {0};
		
		if(val >= axis[axis.length-1])
			return new int[] {axis.length-1};
		
		while(val >= axis[idx]) {
			idx++;
		}

		return new int[] {idx-1, idx};
	}
	public static double[][] interpolate(double[][] inputData, double[][] axes, 
			double stepX, double stepY, double maxX, double maxY) {
		int nCols = (int) Math.rint(maxX / stepX * 2)+1;
		int nRows = (int) Math.rint(maxY / stepY * 2)+1;
		
		double[][] interpolated = new double[nCols][nRows];
		double xPos = 0, yPos = 0, x0Frac = 0, x1Frac = 0, y0Frac = 0, y1Frac = 0;
		double p00Frac = 0, p01Frac = 0, p10Frac = 0, p11Frac = 0, area = 0;
		double width = 0, height = 0, x0val = 0, x1val = 0, y0val = 0, y1val = 0;
		
		int x[], y[], x0 = 0, x1 = 0, y0 = 0, y1 = 0;
		
		for(int i = 0; i < nCols; i++) {
			xPos = i * stepX - maxX;
			x = getBounds(xPos, axes[0]);
			switch(x.length) {
			case 1:
				x0 = x[0];
				x1 = x[0];
				x0val = axes[0][x0];
				x1val = axes[0][x0];
				x0Frac = 1;
				x1Frac = 0;
				width = 1;
				break;
			case 2:
				x0 = x[0];
				x1 = x[1];
				x0val = axes[0][x0];
				x1val = axes[0][x1];
				width = Math.abs(x1val - x0val);
				x0Frac = Math.abs(xPos - x1val) / width;
				x1Frac = 1-x0Frac;
				break;
			}
			for(int j = 0; j < nRows; j++) {
				if(j == nRows-1)
					System.out.println();
				yPos = j * stepY - maxY;
				
				y = getBounds(yPos, axes[1]);

				switch(y.length) {
				case 1:
					y0 = y[0];
					y1 = y[0];
					y0val = axes[1][y0];
					y1val = axes[1][y0];
					y0Frac = 1;
					y1Frac = 0;
					height = 1;
					break;
				case 2:
					y0 = y[0];
					y1 = y[1];
					y0val = axes[1][y0];
					y1val = axes[1][y1];
					height = Math.abs(y1val - y0val);
					y0Frac = Math.abs(yPos - y1val) / height;
					y1Frac = 1-y0Frac;
					break;
				}
				
				area = height * width;
				if(area < 0 || y0Frac < 0 || x0Frac < 0 || y1Frac < 0 || x1Frac < 0)
					System.out.println("");
				
				p00Frac = x0Frac * y0Frac / area;
				p10Frac = x1Frac * y0Frac / area;
				p01Frac = x0Frac * y1Frac / area;
				p11Frac = x1Frac * y1Frac / area;
				
				interpolated[i][j] = p00Frac * inputData[x0][y0] + p01Frac * inputData[x0][y1] + 
						p10Frac * inputData[x1][y0] + p11Frac * inputData[x1][y1];
				
			}
		}
		
		return interpolated;
	}
	
	public static void main(String[] args) {
		
		double step = 1;
		double minx = 0;
		double miny = 0;
		double maxx = 20;
		double maxy = 25;
		
		double cx = maxx/2;
		double cy = maxy/2;

		double sigx = 1;
		double sigy = 1;
		double mux = 0;
		double muy = 0;
		double A = 10;
		double[] parametersx = new double[] {A, mux, sigx};
		double[] parametersy = new double[] {muy, sigy};
		
		int numx = (int) Math.rint((maxx - minx) / step);
		int numy = (int) Math.rint((maxy - miny) / step);
		
		double[][] vals = new double[numx][numy];
		double[][] axes = new double[2][];
		axes[0] = new double[numx];
		axes[1] = new double[numy];
		
		Equation2 e = new Gaussian2(parametersx, parametersy);
		double x, y;
		for(int i = 0; i < numx; i++) {
			x = i * step - cx;
			axes[0][i] = x;
			for(int j = 0; j < numy; j++) {
				y = j * step - cy;
				if(i == 0)
					axes[1][j] = y;
				
				vals[i][j] = e.evaluate(x, y);
			}
		}
		System.out.println("Original:\n");
		for(int i = 0; i < vals.length; i++) {
			System.out.println(StringConverter.arrayToTabString(vals[i]));
		}
		
		double factor = .75;
		double interpolateStep = step * factor;
		double interpolateMax = maxx * factor * factor;
		
		double[][] interpolated = Interpolate.interpolate(vals, axes, interpolateStep, 
				interpolateStep, interpolateMax, interpolateMax);

		System.out.println("\nInterpolated:\n");
		for(int i = 0; i < interpolated.length; i++) {
			System.out.println(StringConverter.arrayToTabString(interpolated[i]));
		}
		
		
	}
}
