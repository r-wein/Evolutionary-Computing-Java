package unitTests;

import java.util.ArrayList;
import java.util.List;

import ecSystem.ECSystem;
import ecSystem.ECSystemParameters;

public class TestECMain {

	public static void main(String[] args) {

		/*
		 * for this testing, it would create way too long of a file to hand code
		 * in everything, I just systematically changed each variable and
		 * recorded the results. Was able to achieve statement, branch, and
		 * conditional coverage but allParametersSet has too many paths to gain
		 * path coverage
		 * 
		 * 
		 */

		ECSystemParameters parameters = new ECSystemParameters();

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

		// set parameters for our system
		parameters.setPopulationSize(700);
		parameters.setMaxExpressionSize(30);
		parameters.setValuesForXTrainingData(xTrainingData);
		parameters.setValuesForYTrainingData(yTrainingData);
		parameters.setFitnessThreshold(0.2);
		parameters.setStagnationThreshold(50);
		parameters.setMutationPercentage(0.2);
		parameters.setThresholdForSuccess(0.01);

		// new ECSystem instance
		ECSystem evoComp = new ECSystem(parameters);
		
		evoComp.runECSystem();

	}

}
