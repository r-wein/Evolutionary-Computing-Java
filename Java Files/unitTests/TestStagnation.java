package unitTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ecSystem.CalculateDelta;
import ecSystem.ECSystemParameters;
import ecSystem.ECSystemStatistics;
import individual.Individual;

public class TestStagnation {

	public static void main(String[] args) {

		/*
		 * this test is just a more thorough vetting of the stagnation function
		 * since we had issues earlier in development, it is the EC System with
		 * a number of targeted print statements, no real data to export other
		 * than watching the print statements and confirming the system is
		 * running as expected
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
		TestStagnation evoComp = new TestStagnation(parameters);

		evoComp.runECSystem();

	}

	/****************************************
	 * MEMBER VARIABLES
	 ****************************************/

	private ECSystemParameters parameters;
	private ECSystemStatistics stats;
	private List<Individual> generation;
	private int stagnantCount;
	private Random rand;
	private CalculateDelta calcDelta;

	/****************************************
	 * CONSTRUCTOR
	 ****************************************/

	public TestStagnation(ECSystemParameters parameters) {
		this.parameters = parameters;
		this.stats = new ECSystemStatistics();
		this.generation = new ArrayList<Individual>();
		this.stagnantCount = 0;
		this.rand = new Random();
		this.calcDelta = new CalculateDelta();
	}

	/****************************************
	 * GETTERS
	 ****************************************/

	// returns the statistics from runECSystem
	public ECSystemStatistics getSystemStatistics() {
		return this.stats;
	}

	/****************************************
	 * RESET
	 ****************************************/

	private void resetECSystem() {
		this.stats = new ECSystemStatistics();
		this.generation = new ArrayList<Individual>();
		this.stagnantCount = 0;
	}

	/****************************************
	 * EC SYSTEM
	 ****************************************/

	public boolean runECSystem() {

		this.resetECSystem();

		/*
		 * do not run if all parameters are not set
		 */
		if (!this.parameters.allParametersSet()) {
			System.out.println("You have not set all system parameters to run EC System");
			return false;
		}

		// starts the timer
		this.stats.startStopWatch();

		// create initial population
		this.generation = this.createInitialPopulation(1);

		// sentinel for our main ECSystem loop
		boolean equivalentExpressionNotFound = true;

		while (equivalentExpressionNotFound) {

			// evaluate fitness for each Individual in the generation list
			this.evaluateEachExpression();

			// separate out unfit Individuals; those with fitness of -1.0
			this.generation = this.discardUnfitIndividuals();

			// sort expressions based on fitness
			Collections.sort(this.generation);

			// records the current most fit Individual
			this.stats.addMostFitExpressionInGeneration(this.generation.get(0));

			// prints most fit individual to screen
			// System.out.println(this.generation.get(0).getFitness());

			// if the most fit Individual is under threshold
			if (this.generation.get(0).getFitness() <= this.parameters.getThresholdForSuccess()) {

				/** SUCCESS! **/

				/*
				 * let's leave the loop and record our succeeding Individual
				 */
				equivalentExpressionNotFound = false;
				this.stats.setSuccessfulIndividual(this.generation.get(0));

			} else {

				this.createNewGeneration();
			}

			this.stagnantCount++;
		}

		// when we've exited the loop
		// record how much time its been
		this.stats.endStopWatch();
		return true;
	}

	private void createNewGeneration() {
		// get programs within fitnessThreshold
		List<Individual> mostFitIndividuals = this.performSelection();

		// increment generation count
		this.stats.addToGenerationTotal();

		if (mostFitIndividuals.size() > 1 && this.fitnessImproving(mostFitIndividuals.get(0))) {

			// select random Individuals to mutate
			List<Individual> mutatedIndividuals = this.mutationSelection(mostFitIndividuals);

			// determine the number of children we need
			int numberOfChildrenNeeded = this.parameters.getPopulationSize() - mostFitIndividuals.size()
					- mutatedIndividuals.size();

			// crossover non-mutated Individuals
			List<Individual> children = this.crossoverSelection(mostFitIndividuals, numberOfChildrenNeeded,
					this.stats.getGenerationCount());

			// combine mutated/parents/children into new generation
			this.generation = new ArrayList<>(mostFitIndividuals);
			this.generation.addAll(mutatedIndividuals);
			this.generation.addAll(children);

		} else {

			// if our fitness has become stagnant, create a whole new
			// population and reset the stagnantCount
			this.generation = this.createInitialPopulation(this.stats.getGenerationCount());
			this.stagnantCount = 0;
			this.stats.recordECSystemReboot();
		}
	}

	/****************************************
	 * RANDOMLY CREATE GENERATION
	 ****************************************/

	private List<Individual> createInitialPopulation(int genNum) {

		/*
		 * check the parameters for generationSize and genomeSize and create as
		 * many new Individual objects as needed
		 * 
		 */

		// create a new list to hold the new Individuals
		List<Individual> population = new ArrayList<Individual>();

		// add the specified number of new Individuals to the list
		for (int x = 0; x < this.parameters.getPopulationSize(); x++) {
			population.add(new Individual(this.parameters.getGenomeSize(), genNum));
			this.stats.addToOperationCount();
		}

		this.stats.addToGenerationTotal();
		this.stagnantCount++;

		// return the newly created list of Individuals
		return population;
	}

	/****************************************
	 * SELECTION
	 ****************************************/

	private void evaluateEachExpression() {

		/*
		 * for every program in the generation List, compute y for each x value
		 * in the training set
		 */
		for (Individual eachGenome : this.generation) {

			this.calcDelta.calculateDelta(eachGenome.getProgramGenome(), this.parameters.getXTestSet(),
					this.parameters.getYTestSet());

			eachGenome.setFitness(this.calcDelta.getDelta());
			eachGenome.setYValues(this.calcDelta.getYValues());

			this.stats.addToOperationCount();
		}
	}

	private List<Individual> discardUnfitIndividuals() {

		/*
		 * loop through generation list and only add Individuals we were able to
		 * calculate fitness to fitPrograms List
		 */
		List<Individual> fitPrograms = new ArrayList<Individual>();

		for (int x = 0; x < this.parameters.getPopulationSize(); x++) {
			if (this.generation.get(x).getFitness() >= 0.0) {
				fitPrograms.add(this.generation.get(x));
			}
			this.stats.addToOperationCount();
		}
		return fitPrograms;
	}

	private List<Individual> performSelection() {

		/*
		 * loop through the Individuals we were able to generate a fitness value
		 * for and separate out the most fit ones
		 */

		// get a list to hold the selected Individuals
		List<Individual> selectedIndividuals = new ArrayList<Individual>();

		// loop through the fit Individuals under the fitness threshold
		for (int x = 0; x < this.generation.size() * this.parameters.getFitnessThreshold(); x++) {

			// add those Individuals to the selected Individuals list
			selectedIndividuals.add(this.generation.get(x));

			this.stats.addToOperationCount();
		}

		// return the completed list
		return selectedIndividuals;
	}

	/****************************************
	 * FITNESS IMPROVING
	 ****************************************/

	private boolean fitnessImproving(Individual mostFit) {

		this.stats.addToOperationCount();

		/*
		 * has our fitness stopped improving?
		 */

		System.out.println("Stagnation Count: " + this.stagnantCount);

		// first check if we have enough data to make the decision
		if (this.stagnantCount <= this.parameters.getStagnationThreshold()) {

			System.out.println("No Test, Not Enough Generations Yet");

			// if we don't, nothing more to do but gather more info
			return true;
		} else {

			double pastGen = pastGenerationAverage();

			System.out.println("Test Past Generations");
			System.out.println("MostFit: " + this.generation.get(0).getFitness());
			System.out.println("Average Fitness: " + pastGen);
			System.out.println("Reboot?  " + !(mostFit.getFitness() - pastGen < -0.001));
			// if we do, check this generation's most fit Individual against the
			// average most fit Individual within our stagnationThreshold
			return mostFit.getFitness() - pastGen < -0.001;
		}
	}

	private double pastGenerationAverage() {

		/*
		 * loop through past generations most fit Individual and find the
		 * average
		 */

		// variable to hold fitness total
		double total = 0.0;
		int position = this.stats.getMostFitFitnessValues().size() - this.parameters.getStagnationThreshold() - 1;

		// loop through the appropriate programs
		for (int x = 0; x < this.parameters.getStagnationThreshold(); x++) {

			// get the most fit programs fitness and add its total to our
			// variable
			total += this.stats.getMostFitFitnessValues().get(position++);
			this.stats.addToOperationCount();
		}

		// return the average of that total
		return total / (double) (this.parameters.getStagnationThreshold());
	}

	/****************************************
	 * MUTATION
	 ****************************************/

	private List<Individual> mutationSelection(List<Individual> population) {

		/*
		 * at random, separate out the number of Individuals specified in the
		 * parameters, remove them from the main population, mutate them, and
		 * return a new list of the mutated Individuals
		 */

		// create new list to hold mutated Individuals
		List<Individual> mutatedPopulation = new ArrayList<Individual>();

		// select the correct amount of Individuals to be mutated
		for (int x = 0; x < population.size() * this.parameters.getMutationPercentage(); x++) {

			// select a number within the range of the population size
			int selection = this.rand.nextInt(population.size());

			// get the Individual at that location and mutate them recording
			// what generation this is happening in
			population.get(selection).mutate(this.stats.getGenerationCount());
			// add that Individual to the mutated list
			mutatedPopulation.add(population.get(selection));
			// remove them from the population
			population.remove(selection);

			this.stats.addToOperationCount();
		}

		// return our list with mutated Individuals
		return mutatedPopulation;
	}

	/****************************************
	 * CROSSOVER
	 ****************************************/

	private List<Individual> crossoverSelection(List<Individual> population, int numberOfChildrenNeeded, int genNum) {

		/*
		 * this selected at random which Individuals to cross
		 */

		// create a new list to hold the children
		List<Individual> children = new ArrayList<Individual>();

		// run as many times as need to fill up the population
		for (int x = 0; x < numberOfChildrenNeeded; x++) {

			// select two Individuals at random
			int parentOne = this.rand.nextInt(population.size());
			int parentTwo = this.rand.nextInt(population.size());

			// breed them to get a child
			children.add(population.get(parentOne).crossExpressions(population.get(parentTwo), genNum));

			this.stats.addToOperationCount();
		}

		// return our list with the children
		return children;
	}

}
