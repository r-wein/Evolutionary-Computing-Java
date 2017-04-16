package main;

import java.util.*;

import ecSystem.ECSystem;
import ecSystem.ECSystemParameters;

public class ECMain {

	public static void main(String[] args) {

		ECSystemParameters parameters = new ECSystemParameters();

		// x training data
		List<Integer> xTrainingData = new ArrayList<Integer>();
		xTrainingData.add(-31);
		xTrainingData.add(-11);
		xTrainingData.add(-8);
		xTrainingData.add(-4);
		xTrainingData.add(-1);
		xTrainingData.add(1);
		xTrainingData.add(5);
		xTrainingData.add(10);
		xTrainingData.add(20);
		xTrainingData.add(31);

		// y training data
		List<Double> yTrainingData = new ArrayList<Double>();
		yTrainingData.add(640.0);
		yTrainingData.add(80.0);
		yTrainingData.add(42.0);
		yTrainingData.add(10.0);
		yTrainingData.add(0.0);
		yTrainingData.add(0.0);
		yTrainingData.add(16.0);
		yTrainingData.add(66.0);
		yTrainingData.add(266.0);
		yTrainingData.add(640.0);

		// set parameters for our system
		parameters.setPopulationSize(700);
		parameters.setGenomeSize(30);
		parameters.setValuesForXTrainingData(xTrainingData);
		parameters.setValuesForYTrainingData(yTrainingData);
		parameters.setFitnessThreshold(0.2);
		parameters.setStagnationThreshold(45);
		parameters.setMutationPercentage(0.05);
		parameters.setSuccessThreshold(0.01);

		ECSystem evoComp = new ECSystem(parameters);
		
		evoComp.runECSystem();
		
		System.out.println(evoComp.getSystemStatistics().toString());
	
	}
}
