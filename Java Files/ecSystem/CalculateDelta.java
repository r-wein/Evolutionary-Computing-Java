package ecSystem;

import java.util.ArrayList;
import java.util.List;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculateDelta {
	
	/********************************************
	 ** CALCULATE FITNESS
	 ********************************************/
	
	private double delta;
	private List<Double> yValues;
	private Expression evaluate;
	
	/********************************************
	 ** CALCULATE FITNESS
	 ********************************************/
	
	public CalculateDelta() {
		this.delta = -1.0;
		this.yValues = null;
		this.evaluate = null;
	}
	
	/********************************************
	 ** OVERRIDE
	 ********************************************/
	
	@Override
	public String toString() {
		return "Delta: " + this.delta + "\nY Values: " + this.yValues.toString();
	}
	
	/********************************************
	 ** GETTERS
	 ********************************************/

	// returns the current fitness of the Individual
	public double getDelta() {
		return this.delta;
	}

	// returns the y values for this expression
	public List<Double> getYValues() {
		return this.yValues;
	}
	
	/********************************************
	 ** CALCULATE DELTA
	 ********************************************/

	public void calculateDelta(String expression, List<Integer> xTrainingData, List<Double> yTrainingData) {

		// use the x training data to calculate the y values for the expression
		this.evaluateXTrainingData(expression, xTrainingData);

		/*
		 * if there are any arithmetic errors with calculating the y values
		 * (i.e. 0 in the denominator), that expression is deemed unfit and will
		 * not be compared to the y training set
		 */
		if (this.yValues != null) {
			this.evaluateYTrainingData(yTrainingData);
		}
	}

	private boolean evaluateXTrainingData(String expression, List<Integer> xTrainingData) {

		// clear the previous y values for new values
		this.yValues = new ArrayList<Double>();

		// set the most recent expression to be evaluated
		this.evaluate = new ExpressionBuilder(expression).variable("x").build();

		// test each x value
		for (int theXValue : xTrainingData) {

			// do the math
			try {

				this.evaluate.setVariable("x", theXValue);
				this.yValues.add(this.evaluate.evaluate());

				// if there is an error
			} catch (ArithmeticException e) {
				// clear the y values
				this.yValues = null;
				return false;
			}
		}
		return true;
	}

	private void evaluateYTrainingData(List<Double> yTrainingData) {
		
		// clear the current delta
		this.delta = 0.0;

		// add up the differences between the training data y and the y values
		// just generated
		for (int x = 0; x < yTrainingData.size(); x++) {
			this.delta += Math.abs(yTrainingData.get(x) - this.yValues.get(x));
		}
	}

}
