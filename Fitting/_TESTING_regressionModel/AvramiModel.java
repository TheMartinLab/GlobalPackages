/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package _TESTING_regressionModel;

public class AvramiModel extends RegressionModel {

	protected double kA, t0, n;
	/**
	 * 
	 * @param parameters double[] [kA, t0, n]
	 * @param isFittable boolean[] [kA, t0, n]
	 * @param parameterBounds Bounds[] [kA, t0, n]
	 */
	public AvramiModel(double[] parameters, boolean[] isFittable, Bounds[] parameterBounds) {
		super(parameters, isFittable, parameterBounds);
		kA = parameters[0];
		t0 = parameters[1];
		n = parameters[2];
	}

	@Override
	public double evaluate(double val) {
		kA = parameters[0];
		t0 = parameters[1];
		n = parameters[2];
		return 1-Math.exp(-1 * Math.pow(kA * (val-t0), n));
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
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
			throw new ParameterSelectionException("The options for the Avrami model are kA, t0 and n.  Your selection of: " +
				whichParameter.toString() + " is therefore not an allowable option.");
		}
	}
	public String getModelName() { return "Avrami"; }
	public String getModelEquation() { return "alpha(t) = 1-exp((kA * (t-t0) ) ^ n)"; }
	public String getModelEquationWithParameters() { return "alpha(t) = 1-exp(" + kA + "* (t-" + t0 + ") ) ^ " + n + ")"; }

	@Override
	public double[] convertParametersTo(double[] parameters) { return parameters; }
}
