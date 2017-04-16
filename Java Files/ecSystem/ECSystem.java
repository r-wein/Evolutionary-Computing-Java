package ecSystem;

import java.util.*;

import individual.Individual;

public class ECSystem {

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

	public ECSystem(ECSystemParameters parameters) {
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

			// sort expressions based on fitness
			Collections.sort(this.generation);

			// separate out unfit Individuals; those with fitness of -1.0
			this.discardUnfitIndividuals();
			
			// just in case all the Individuals are unfit
			if (this.generation.size() > 0) {

				// records the current most fit Individual
				this.stats.addMostFitExpressionInGeneration(this.generation.get(0));

				// prints most fit individual to screen
				//System.out.println(this.generation.get(0).getFitness());
			}

			// if there are any fit Individuals, is the most fit Individual under threshold
			if (this.generation.size() > 0
					&& this.generation.get(0).getFitness() <= this.parameters.getSuccessThreshold()) {

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

			this.calcDelta.calculateDelta(eachGenome.getProgramGenome(), this.parameters.getXTrainingData(),
					this.parameters.getYTrainingData());

			eachGenome.setFitness(this.calcDelta.getDelta());
			eachGenome.setYValues(this.calcDelta.getYValues());

			this.stats.addToOperationCount();
		}
	}

	private void discardUnfitIndividuals() {
		
		/*
		 * rid the generation list of any unfit Individuals
		 * 
		 */

		while (this.generation.get(0).getFitness() == -1.0) {
			this.generation.remove(0);
			this.stats.addToOperationCount();
		}
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

		// first check if we have enough data to make the decision
		if (this.stagnantCount <= this.parameters.getStagnationThreshold()) {
			// if we don't, nothing more to do but gather more info
			return true;
		} else {
			// if we do, check this generation's most fit Individual against the
			// average most fit Individual within our stagnationThreshold
			return mostFit.getFitness() - pastGenerationAverage() < -1.0;
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

		int loopNumber = population.size();

		// select the correct amount of Individuals to be mutated
		for (int x = 0; x < loopNumber * this.parameters.getMutationPercentage(); x++) {

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
