/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package filters;

public final class FilterFactory {
	public static ImageFilter getImageFilter(int FILTER_TYPE) {
		switch(FILTER_TYPE) {
		case ImageFilter.LINEAR: return new LinearFilter();
		case ImageFilter.LOGARITHMIC: return new LogarithmicFilter();
		case ImageFilter.NO_FILTER: return new Unfiltered();
		//case ImageFilter.MEDIAN_FILTER: return new MedianFilter();
		default:
			throw new FilterFactoryException();
		}
	}
	static class FilterFactoryException extends RuntimeException {
		private static final long serialVersionUID = -1230329815360264428L;

		public FilterFactoryException() {
			super();
		}
		public FilterFactoryException(String s) {
			super(s);
		}
	}
}
