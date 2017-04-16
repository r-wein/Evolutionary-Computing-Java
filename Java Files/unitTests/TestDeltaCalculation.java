package unitTests;

import java.util.ArrayList;
import java.util.List;

import ecSystem.CalculateDelta;

public class TestDeltaCalculation {

	public static void main(String[] args) {

		/*
		 * CalculateDelta is a fairly simple class. Using the two test cases
		 * (x*x-2)/2 and 3/x is enough to gain statement, branch, condition, and
		 * path coverage
		 * 
		 */

		CalculateDelta testDelta = new CalculateDelta();

		// x training data
		List<Integer> xTrainingData = new ArrayList<Integer>();
		xTrainingData.add(-11);
		xTrainingData.add(-8);
		xTrainingData.add(-3);
		xTrainingData.add(-1);
		xTrainingData.add(0);
		xTrainingData.add(1);
		xTrainingData.add(5);
		xTrainingData.add(6);
		xTrainingData.add(20);
		xTrainingData.add(31);

		// y training data
		List<Double> yTrainingData = new ArrayList<Double>();
		yTrainingData.add(60.0);
		yTrainingData.add(31.5);
		yTrainingData.add(4.0);
		yTrainingData.add(0.0);
		yTrainingData.add(-0.5);
		yTrainingData.add(0.0);
		yTrainingData.add(12.0);
		yTrainingData.add(17.5);
		yTrainingData.add(199.5);
		yTrainingData.add(480.0);

		List<String> theExpressions = new ArrayList<String>();

		// this function should work just fine
		theExpressions.add("(x*x-1)/(2)");
		// this function will have an arithmetic error
		theExpressions.add("(3)/(x)");

		List<String> handCalculatedDelta = new ArrayList<String>();

		// this is what we expect the delta to be for the first function
		handCalculatedDelta.add("0");
		// we should not be able to calculate a delta for the second because
		// when the training data is 0, this should detect a divide by 0 error
		handCalculatedDelta.add("Should Throw Exception");
		
		/*
		 * This loops calculates delta for the above 2 equations and prints the results to the console
		 */

		int positionHandCalc = 0;

		for (String eachExpression : theExpressions) {

			System.out.println("Expression: " + eachExpression);

			try {
				testDelta.calculateDelta(eachExpression, xTrainingData, yTrainingData);
				showYComparison(yTrainingData, testDelta.getYValues());
				System.out.println("\nCPU Calculated Delta:  " + testDelta.getDelta());

			} catch (Exception e) {

				System.out.println("\nException attempting to evaluate expression: " + eachExpression);
				System.out.println(
						"Expression cannot be evaluated. Program will throw UnknownFunctionOrVariableException.\n");
			}

			System.out.println("Hand Calculated Delta: " + handCalculatedDelta.get(positionHandCalc++));

			System.out.println("_____________________________________________________________________\n");

		}
	}

	public static void showYComparison(List<Double> trainingData, List<Double> calculatedValues) {
		System.out.println("\nY Training Data\t|\tExpression Y Values\t|\tDelta");
		System.out.println("----------------------------------------------------------------------");

		for (int x = 0; x < trainingData.size(); x++) {

			System.out.printf("%.2f\t\t|\t%.3f\t\t\t|\t%.5f", trainingData.get(x),
					calculatedValues.get(x).doubleValue(), Math.abs(trainingData.get(x) - calculatedValues.get(x)));
			System.out.println();
		}

	}
}
