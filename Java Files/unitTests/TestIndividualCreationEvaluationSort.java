package unitTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ecSystem.CalculateDelta;
import individual.Individual;

public class TestIndividualCreationEvaluationSort {

	public static void main(String[] args) {
		
		/*
		 * This class cannot take any input since it creates everything
		 * automatically. I had to closely inspect the results after several runs to
		 * ensure that we achieved the coverage we were expecting
		 */

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
		
		List<Individual> testInd = new ArrayList<Individual>();
		
		CalculateDelta delta = new CalculateDelta();
		
		for (int x = 0; x < 10; x++) {
			testInd.add(new Individual(15, 1));
			System.out.println("Created Expression: " + testInd.get(x).getProgramGenome());
			delta.calculateDelta(testInd.get(x).getProgramGenome(), xTrainingData, yTrainingData);
			testInd.get(x).setFitness(delta.getDelta());
			testInd.get(x).setYValues(delta.getYValues());
			showYComparison(yTrainingData, testInd.get(x).getYValues());
			System.out.println("===========================================================================\n");
		}
		
		
		
		System.out.println("Sort Expressions: -1.0 Means Arithemtic Error (Program Unfit).\n");
		
		Collections.sort(testInd);
		
		int place = 1;
		
		System.out.println("Place\t|\tFitness Value\t|\tExpression");
		System.out.println("---------------------------------------------------------------------------");
		
		for (Individual eachExpression : testInd) {
			
			System.out.printf("%d\t|\t%.3f     \t|\t%s", place++, eachExpression.getFitness(),eachExpression.getProgramGenome());
		System.out.println();
		}
		
		System.out.println("===========================================================================\n");
		
		
		
		System.out.println("Expressions Fit For Selection\n");
		
		List<Individual> fitPrograms = discardUnfitIndividuals(testInd);
		
		int placeAfterDiscard = 1;
		
		System.out.println("Place\t|\tFitness Value\t|\tExpression");
		System.out.println("---------------------------------------------------------------------------");
		
		for (Individual eachExpression : fitPrograms) {
			
			System.out.printf("%d\t|\t%.3f     \t|\t%s", placeAfterDiscard++, eachExpression.getFitness(),eachExpression.getProgramGenome());
		System.out.println();
		}
		
		
		
	}

	public static void showYComparison(List<Double> trainingData, List<Double> calculatedValues) {
		
		if (calculatedValues != null) {
			System.out.println("\nY Training Data\t|\tExpression Y Values\t|\tDelta");
			System.out.println("---------------------------------------------------------------------------");

		
		for (int x = 0; x < trainingData.size(); x++) {
			

			System.out.printf("%.2f\t\t|\t%.3f     \t\t|\t%.5f", trainingData.get(x),
					calculatedValues.get(x).doubleValue(), Math.abs(trainingData.get(x) - calculatedValues.get(x)));
			System.out.println();
		}
		
		System.out.println();
		} else {
			System.out.println("Arithemtic Error Evaluating Expression.  Expression Unfit.\n");
		}
		
		
	}
	
	public static List<Individual> discardUnfitIndividuals(List<Individual> testPrograms) {

		/*
		 * loop through generation list and only add Individuals we were able to
		 * calculate fitness to fitPrograms List
		 */
		List<Individual> fitPrograms = new ArrayList<Individual>();

		for (int x = 0; x < testPrograms.size(); x++) {
			if (testPrograms.get(x).getFitness() >= 0.0) {
				fitPrograms.add(testPrograms.get(x));
			}
		}
		return fitPrograms;
	}
}
