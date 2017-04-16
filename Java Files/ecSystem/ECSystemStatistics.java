package ecSystem;

import java.util.*;

import individual.Individual;

public class ECSystemStatistics {

	/********************************************
	 ** MEMBER VARIABLES
	 ********************************************/

	private Individual expressionEquivalent;
	private List<Individual> mostFitProgramsInGeneration;
	private List<Double> mostFitFitnessValues;
	private Map<Integer, List<Double>> mostFitYValues;
	private int numberOfGenerations;
	private int rebootTotal;
	private long startTime;
	private double runTime;
	private long operationCount;

	/********************************************
	 ** CONSTRUCTOR
	 ********************************************/

	public ECSystemStatistics() {
		this.expressionEquivalent = null;
		this.mostFitProgramsInGeneration = new ArrayList<Individual>();
		this.mostFitFitnessValues = new ArrayList<Double>();
		this.mostFitYValues = new HashMap<Integer, List<Double>>();
		this.numberOfGenerations = 0;
		this.rebootTotal = 0;
		this.startTime = 0;
		this.runTime = 0;
		this.operationCount = 0;
	}

	/********************************************
	 ** OVERRIDE
	 ********************************************/

	@Override
	public String toString() {
		return "EC System Stats\n" + "\nNumber Of Generations: " + this.numberOfGenerations
				+ "\n\nEquivalent Expression:\n" + this.expressionEquivalent.toString() + "\n\nEC System Run Time: "
				+ this.runTime + "\nEC System Reboot Amount: " + this.rebootTotal + "\n\nTotal Operations In ECSystem: "
				+ this.operationCount;
	}

	/********************************************
	 ** BASIC GETTERS
	 ********************************************/

	// returns the Individual object which met success threshold
	public Individual getExpressionEquivalent() {
		return this.expressionEquivalent;
	}

	// return a list of the most fit Individuals from each Generation
	public List<Individual> getMostFitIndividuals() {
		return this.mostFitProgramsInGeneration;
	}

	// returns the fitness value for the most fit Individual in each generation
	public List<Double> getMostFitFitnessValues() {
		return this.mostFitFitnessValues;
	}

	// returns all the y values for the most fit Individual in each generation
	public Map<Integer, List<Double>> getYPlotsForMostFitIndividuals() {
		return this.mostFitYValues;
	}

	// return number of Generations
	public int getGenerationCount() {
		return this.numberOfGenerations;
	}

	// returns the length it took to find an equivalent expression in seconds
	public double getProgramRunTime() {
		return this.runTime;
	}

	// returns the number of time ECSystem needed to reboot due to stagnant
	// fitness
	public int getRebootTotal() {
		return this.rebootTotal;
	}

	// returns the number of method calls in our ECSystem
	public long getOperationCount() {
		return this.operationCount;
	}

	/********************************************
	 ** SETTERS
	 ********************************************/

	// when our ECSystem find an equivalent expression, this is how we record it
	public void setSuccessfulIndividual(Individual expressionEquivalent) {
		this.expressionEquivalent = expressionEquivalent;
	}

	// add the most fit expression in each generation to our list
	public void addMostFitExpressionInGeneration(Individual mostFit) {
		this.mostFitProgramsInGeneration.add(mostFit);
		this.mostFitFitnessValues.add(mostFit.getFitness());
		this.mostFitYValues.put(this.getGenerationCount(), mostFit.getYValues());
	}

	/********************************************
	 ** WATCHER METHODS
	 ********************************************/

	// increment generation total
	public void addToGenerationTotal() {
		this.numberOfGenerations++;
	}

	// record that we had to reboot ECSystem
	public void recordECSystemReboot() {
		this.rebootTotal++;
	}

	// increment method call counter
	public void addToOperationCount() {
		this.operationCount++;
	}

	/********************************************
	 ** TIME METHODS
	 ********************************************/

	// start the clock
	public void startStopWatch() {
		this.startTime = System.currentTimeMillis();
	}

	// end the clock and record the time
	public void endStopWatch() {
		long endTime = System.currentTimeMillis();
		this.runTime = (endTime - this.startTime) / 1000.0;
	}
}
