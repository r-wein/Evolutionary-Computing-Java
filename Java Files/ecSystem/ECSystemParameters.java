package ecSystem;

import java.util.*;

public class ECSystemParameters {

	/****************************************
	 * MEMBER VARIABLES
	 ****************************************/

	private int generationSize;
	private int genomeSize;
	private List<Integer> xTrainingData;
	private List<Double> yTrainingData;
	private double fitnessThreshold;
	private int stagnationThreshold;
	private double mutationPercentage;
	private double successThreshold;

	/****************************************
	 * CONSTRUCTOR
	 ****************************************/

	public ECSystemParameters() {
		this.generationSize = 0;
		this.genomeSize = 0;
		this.xTrainingData = new ArrayList<Integer>();
		this.yTrainingData = new ArrayList<Double>();
		this.fitnessThreshold = -1.0;
		this.stagnationThreshold = -1;
		this.mutationPercentage = -1.0;
		this.successThreshold = -1.0;
	}

	/****************************************
	 * CHECK
	 ****************************************/

	// this check is to make sure that all variables are set before we start our
	// EC System
	public boolean allParametersSet() {
		return this.generationSize > 49 && this.genomeSize > 4 && this.validTrainingSet()
				&& this.fitnessThreshold > 0.0 && this.stagnationThreshold > 0 && this.mutationPercentage >= 0.0
				&& this.successThreshold >= 0.0;
	}

	/****************************************
	 * GETTERS
	 ****************************************/

	// returns how big we want our generation sizes to be
	public int getPopulationSize() {
		return this.generationSize;
	}

	// returns how big we want our expressions to be
	public int getGenomeSize() {
		return this.genomeSize;
	}

	// returns the x training data
	public List<Integer> getXTrainingData() {
		return this.xTrainingData;
	}

	// returns the y training data
	public List<Double> getYTrainingData() {
		return this.yTrainingData;
	}

	// returns what percentage of evaluated Individuals will be selected for
	// mutation / crossover
	public double getFitnessThreshold() {
		return this.fitnessThreshold;
	}

	// returns how many generations our fitness level can remain unchanged
	// before we start over with a whole new population
	public int getStagnationThreshold() {
		return this.stagnationThreshold;
	}

	// returns what percentage of our fit individuals will be mutated
	public double getMutationPercentage() {
		return this.mutationPercentage;
	}

	// return how close to 0 an expressions needs to be to be considered a
	// success
	public double getSuccessThreshold() {
		return this.successThreshold;
	}

	/****************************************
	 * SETTERS
	 ****************************************/

	// set the generationSize parameter
	public void setPopulationSize(int generationSize) {
		this.generationSize = generationSize > 49 ? generationSize : 0;
	}

	// set the genomeSize parameter
	public void setGenomeSize(int genomeSize) {
		this.genomeSize = genomeSize > 4 ? genomeSize : 0;
	}

	// set the x training data
	public void setValuesForXTrainingData(List<Integer> xTestSet) {
		this.xTrainingData = xTestSet;
	}

	// set the y training data
	public void setValuesForYTrainingData(List<Double> yTestSet) {
		this.yTrainingData = yTestSet;
	}

	// set the percentage of evaluated Individuals that will be selected for
	// mutation / crossover
	public void setFitnessThreshold(double threshold) {
		this.fitnessThreshold = validInput(threshold);
	}

	// sets how many generations our fitness level can remain unchanged
	// before we start over with a whole new population
	public void setStagnationThreshold(int threshold) {
		this.stagnationThreshold = threshold >= 0 ? threshold : -1;
	}

	// set the threshold for what percentage of the fit population we are going
	// to mutate
	public void setMutationPercentage(double mutationPercentage) {
		this.mutationPercentage = validInput(mutationPercentage);
	}

	// set the threshold for how close to 0 our expressions needs to be to be
	// considered a success
	public void setSuccessThreshold(double threshold) {
		this.successThreshold = threshold >= 0 ? threshold : -1.0;
	}
	
	/****************************************
	 * HELPER METHODS
	 ****************************************/
	
	private double validInput(double input) {
		
		return input > 0.0 && input < 1.0 ? input : -1.0;
	}
	
	private boolean validTrainingSet() {
		return this.xTrainingData.size() > 0 && this.yTrainingData.size() > 0 && this.xTrainingData.size() == this.yTrainingData.size();
	}
}
