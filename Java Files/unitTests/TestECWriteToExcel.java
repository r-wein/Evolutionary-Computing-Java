package unitTests;

import java.util.*;

import ecSystem.ECSystem;
import ecSystem.ECSystemParameters;
import excel.ExportToExcel;

public class TestECWriteToExcel {

	public static void main(String[] args) {

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

		System.out.println(evoComp.getSystemStatistics().toString());

		ExportToExcel export = new ExportToExcel();
		export.setFileName("List Export");
		export.setSheetName("List");
		export.addTestingData(evoComp.getSystemStatistics().getMostFitFitnessValues());
		export.writeDataToExcel();

		Map<Integer, List<Double>> dataToWrite = new HashMap<Integer, List<Double>>();
		int position = 0;
		
		for (int x = 0; x < 100; x++) {

			evoComp.runECSystem();
			dataToWrite.put(position++, evoComp.getSystemStatistics().getMostFitFitnessValues());
		}

		export.setFileName("Map Export");
		export.setSheetName("Map");
		export.addMapForTesting(dataToWrite);
		export.writeDataToExcel();

	}
}
