/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

import java.util.Arrays;

import _TESTING_regressionModel.Bounds.BoundingCondition;

public abstract class RegressionModel {
	
	protected double[] parameters;
	protected double[] standardDeviation;
	protected boolean[] isFittable;
	protected Bounds[] parameterBounds;
	
	public RegressionModel(double[] parameters, boolean[] isFittable, Bounds[] parameterBounds) {
		setParameters(parameters);
		setFittable(isFittable);
		setParameterBounds(parameterBounds);
		standardDeviation = new double[parameters.length];
	}
	
	/* ABSTRACT MODEL METHODS */
	public abstract double evaluate(double val);
	public abstract double[] convertParametersTo(double[] parameters);
	/* CONCRETE MODEL METHODS */
	public double[][] convertData(double[][] data) { return data; }
	public Bounds convertXBounds(Bounds x) { return x; }
	public Bounds convertYBounds(Bounds y) { return y; }
	public boolean checkParameters() {
		for(int i = 0; i < parameters.length; i++) {
			if(!parameterBounds[i].checkWithinBounds(parameters[i])) {
				return false;
			}
		}
		return true;
	}
	/* CONCRETE SETTERS */
	public void setParameters(double[] parameters) {
		if(parameterBounds != null) {
			for(int i = 0; i < parameters.length; i++) {
				parameters[i] = parameterBounds[i].withinBounds(parameters[i]);
			}
		}
		this.parameters = Arrays.copyOf(parameters, parameters.length);
	}

	public void setStandardDeviation(double[] standardDeviation) {
		int stdDevIdx = 0;
		this.standardDeviation = new double[isFittable.length];
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { this.standardDeviation[i] = standardDeviation[stdDevIdx++]; }
			else { this.standardDeviation[i] = 0; }
		}
	}
	public void setFittable(boolean[] isFittable) {
		this.isFittable = Arrays.copyOf(isFittable, isFittable.length);
	}
	public void setParameterBounds(Bounds[] parameterBounds) {
		if(parameterBounds == null) { parameterBounds = new Bounds[parameters.length]; }
		for(int i = 0; i < parameterBounds.length && (parameterBounds[i] == null || parameterBounds[i].lower == null); i++) {
			parameterBounds[i] = new Bounds();
			parameterBounds[i].addBound(0, BoundingCondition.GREATER_THAN);
		}
		this.parameterBounds = parameterBounds;
	}
	/* CONCRETE GETTERS */
	public double[] getAllParameters() { return Arrays.copyOf(parameters, parameters.length); }
	public double[] getAllStandardDeviations() { return Arrays.copyOf(standardDeviation, standardDeviation.length); }
	public boolean[] getAreParametersFittable() { return Arrays.copyOf(isFittable, isFittable.length); }
	public Bounds[] getAllParameterBounds(Bounds[] parameterBounds) { return parameterBounds; }
	public int determineNumberOfFittableParameters() {
		int numFittable = 0;
		for(int i = 0; i < isFittable.length; i++) {
			if(isFittable[i]) { numFittable++; }
		}
		return numFittable;
	}
	/* ABSTRACT SETTERS */
	/**
	 * 
	 * @param parameter The new parameter
	 * @param whichParam Use the enum "ParameterOptions" to select which parameter is being modified.
	 * @throws ParameterSelectionException If a parameter is selected that is not relevant to the RegressionModel 
	 * selected then this exception will be thrown.
	 */
	public abstract void setSpecificParameter(double parameter, ParameterOptions whichParam)
		throws ParameterSelectionException;
	public abstract void setSpecificParameterBounds(Bounds specificBounds, ParameterOptions whichParam)
			throws ParameterSelectionException;
		
	/* ABSTRACT GETTERS */
	public abstract double getSpecificParameter(ParameterOptions whichParameter);
	public abstract double getSpecificStandardDeviation(ParameterOptions whichParameter);
	public abstract boolean getIsParameterFittable(ParameterOptions whichParameter);
	public abstract Bounds getSpecificParameterBounds(ParameterOptions whichParameter);
	public abstract String getModelName();
	public abstract String getModelEquation();
	public abstract String getModelEquationWithParameters();
	public String toString() { return getModelName(); }
	
	public enum ParameterOptions {
		kA, t0, n
	}
}
