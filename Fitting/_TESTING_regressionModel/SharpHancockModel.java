/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

import _TESTING_regressionModel.Bounds.BoundingCondition;

public class SharpHancockModel extends AvramiModel {

	/**
	 * 
	 * @param parameters double[] [kA, t0, n]
	 * @param isFittable boolean[] [kA, t0, n]
	 * @param parameterBounds Bounds[] [kA, t0, n]
	 */
	public SharpHancockModel(double[] parameters, boolean[] isFittable, Bounds[] parameterBounds) {
		super(parameters, isFittable, parameterBounds);
		kA = parameters[0];
		t0 = parameters[1];
		n = parameters[2];
	}

	@Override
	/**
	 * The val parameter is expected to be ln(t-t0)
	 */
	public double evaluate(double val) {
		kA = parameters[0];
		t0 = parameters[1];
		n = parameters[2];
		return n * val + n * Math.log(kA);
	}

	@Override
	/**
	 * The data parameter is expected to be [t, alpha]
	 */
	public double[][] convertData(double[][] data) {
		double[][] convertedData = new double[data.length][2];
		for(int i = 0; i < data.length; i++) {
			if(data[i][0] > t0 && data[i][1] != 1 && data[i][1] != 0) {
				convertedData[i][1] = Math.log(-1 * Math.log(1 - data[i][1]));
				convertedData[i][0] = Math.log(data[i][0] - t0);
			} else {
				convertedData[i][1] = 0;
				convertedData[i][0] = 0;
			}
		}
		return convertedData;
	}
	@Override
	/**
	 * This should be the time parameter
	 */
	public Bounds convertXBounds(Bounds x) { 
		double lowerVal = x.getLowerBoundValue();
		double upperVal = x.getUpperBoundValue();
		BoundingCondition lowerCondition = x.getLowerBoundCondition();
		BoundingCondition upperCondition = x.getUpperBoundCondition();
		
		if(lowerVal - t0 <= 0) { lowerVal = 0; }
		else { lowerVal = Math.log(lowerVal-t0); }
		if(upperVal != 0) { upperVal = Math.log(lowerVal-t0); }
		
		Bounds newX = new Bounds();
		newX.addBound(lowerVal, lowerCondition);
		newX.addBound(upperVal, upperCondition);
		
		return newX; 
	}
	@Override
	/**
	 * This should be the alpha parameter
	 */
	public Bounds convertYBounds(Bounds y) { 
		double lowerVal = y.getLowerBoundValue();
		double upperVal = y.getUpperBoundValue();
		BoundingCondition lowerCondition = y.getLowerBoundCondition();
		BoundingCondition upperCondition = y.getUpperBoundCondition();
		
		lowerVal = Math.log(-1 * Math.log(1-lowerVal));
		upperVal = Math.log(-1 * Math.log(1-upperVal));
		
		Bounds newY = new Bounds();
		newY.addBound(lowerVal, lowerCondition);
		newY.addBound(upperVal, upperCondition);
		
		return newY; 
	}
	@Override
	public void setSpecificParameter(double parameter,
			ParameterOptions whichParam) throws ParameterSelectionException {
		switch(whichParam) {
		case kA: 
			kA = parameter;
			parameters[0] = kA;
			break;
		case t0:
			t0 = parameter;
			parameters[1] = t0;
			break;
		case n:
			n = parameter;
			parameters[2] = n;
			break;
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParam.toString() + " is therefore not an allowable option.");
		}
	}

	@Override
	public void setSpecificParameterBounds(Bounds specificBounds,
			ParameterOptions whichParam) throws ParameterSelectionException {
		switch(whichParam) {
		case kA: 
			this.parameterBounds[0] = specificBounds;
			break;
		case t0:
			this.parameterBounds[1] = specificBounds;
			break;
		case n:
			this.parameterBounds[2] = specificBounds;
			break;
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParam.toString() + " is therefore not an allowable option.");
		}
	}

	@Override
	public double getSpecificParameter(ParameterOptions whichParameter) throws ParameterSelectionException {
		switch(whichParameter) {
		case kA: 
			return kA;
		case t0:
			return t0;
		case n:
			return n;
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParameter.toString() + " is therefore not an allowable option.");
		}
	}

	@Override
	public double getSpecificStandardDeviation(ParameterOptions whichParameter) throws ParameterSelectionException {
		switch(whichParameter) {
		case kA: 
			return standardDeviation[0];
		case t0:
			return standardDeviation[1];
		case n:
			return standardDeviation[2];
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParameter.toString() + " is therefore not an allowable option.");
		}
	}

	@Override
	public boolean getIsParameterFittable(ParameterOptions whichParameter) throws ParameterSelectionException {
		switch(whichParameter) {
		case kA: 
			return isFittable[0];
		case t0:
			return isFittable[1];
		case n:
			return isFittable[2];
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParameter.toString() + " is therefore not an allowable option.");
		}
	}

	@Override
	public Bounds getSpecificParameterBounds(ParameterOptions whichParameter) throws ParameterSelectionException {
		switch(whichParameter) {
		case kA: 
			return parameterBounds[0];
		case t0:
			return parameterBounds[1];
		case n:
			return parameterBounds[2];
		default:
			throw new ParameterSelectionException("The options for the Sharp-Hancock model are kA, t0 and n.  Your selection of: " +
				whichParameter.toString() + " is therefore not an allowable option.");
		}
	}
	@Override
	public double[] convertParametersTo(double[] parameters) {
		double n = parameters[1];
		double kA = Math.exp(parameters[0] / parameters[1]);
		return new double[] {kA, n};
	}
	
	public String getModelName() { return "Sharp-Hancock"; }
	public String getModelEquation() { return "ln(-ln(1-alpha(t))) = n * ln(t-t0) + n * ln(kA)"; }
	public String getModelEquationWithParameters() { return "ln(-ln(1-alpha(t))) = " + n + "* ln(t-" + t0+ ") + " + n + " * ln(" + kA + ")"; }
}
