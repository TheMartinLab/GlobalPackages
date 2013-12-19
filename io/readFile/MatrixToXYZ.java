/*******************************************************************************
 * Copyright (c) 2013 Eric Dill -- eddill@ncsu.edu. North Carolina State University. All rights reserved.
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Eric Dill -- eddill@ncsu.edu - initial API and implementation
 ******************************************************************************/
package readFile;

public class MatrixToXYZ {

	public static double[][] ConvertMatrixToXYZ(double[][] data) {
		int numRows = data.length;
		int numCols= data[0].length;
		double[][] vals = new double[(numRows-1) * (numCols)][3];
		
		double rowIdx, colIdx;
		int newRowIdx = 0;
		for(int i = 1; i < numRows; i++) {
			rowIdx = data[i][0];
			for(int j = 1; j < numCols; j++, newRowIdx++) {
				colIdx = data[0][j];
				vals[newRowIdx][0] = rowIdx;
				vals[newRowIdx][1] = colIdx;
				vals[newRowIdx][2] = data[i][j];
			}
		}
		
		return vals;
	}
}
