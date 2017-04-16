package unitTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ecSystem.CalculateDelta;
import individual.Individual;

public class TestECMainInnerWorkings {

	/*
	 * This class relies on some hand coded data and some generated data. A
	 * check of the operations in the console leads to the conclusion that this
	 * test has statement, branch, condition, and path coverage
	 * 
	 * Due to how information is passed around in the real EC System some of
	 * these methods had to be slightly modified to work for the unit tests,
	 * this is most clearly seen with the stagnation testing where it requires a
	 * number of generations to run before this method will kick in, I had to
	 * alter the parameters for this method to make it test what I wanted
	 */

	public static Random rand = new Random();
	public static CalculateDelta calcDelta = new CalculateDelta();

	public static void main(String[] args) {

		// tests create the initial population
		List<Individual> population = createInitialPopulation(1, 700, 30);

		// these two print statements should be the same
		System.out.println("POPULATION");
		System.out.println("Population Size: " + population.size());
		System.out.println("Entered PopSize: 700\n");

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

		// test our EC System evaluate function with the newly generated
		// population and supplied training data
		evaluateEachExpression(population, xTrainingData, yTrainingData);

		// test our EC System discard function with our new population
		population = discardUnfitIndividuals(700, population);

		// print statements to check if everything matches
		System.out.println("DISCARD");
		System.out.println("New Population Size After Discarding Unfit Expressions: " + population.size());

		System.out.println("\nSELECTION");
		System.out.println("Apply Fitness Threshold Of 0.4");
		System.out.println("Expected Selected Size: " + (population.size() * 0.4));

		// test our selection method with our population
		population = performSelection(population, 0.4);
		Collections.sort(population);

		// print statements to check if everything matches
		System.out.println("Selcted Size (NOTE: should equal expected when rounded up): " + population.size());

		// Stagnation has the most conditions and paths to check so we make sure
		// they are all done here, there are 3 possible paths we need to check
		// the first being the system has not run enough loops to check
		// stagnation, we have run enough loops and the fitness is improving,
		// and we have run enough loops and the fitness is not improving
		System.out.println("\nFITNESS IMPROVING");
		System.out.println("Have Not Reached Stagnation Threshold, Should Return TRUE: "
				+ testFitnessImproving(45, 50, population, 0.0));
		System.out.println("Reached Stagnation Threshold, Fitness Below, Should Return TRUE: "
				+ testFitnessImproving(50, 50, population, 5000.0));
		System.out.println("Reached Stagnation Threshold, Fitness Equal or Above, Should Return FALSE: "
				+ testFitnessImproving(55, 50, population, 0.0));

		// check if our mutation method performs as expected
		System.out.println("\nMUTATION");
		System.out.println("Mutation Percentage 0.2");
		System.out.println("Expected Mutation Size: " + (population.size() * 0.2));
		List<Individual> mutatedPopulation = mutationSelection(population, population.size(), 0.2, 1);
		System.out.println(
				"Mutated Population Size (NOTE: should equal expected when rounded up): " + mutatedPopulation.size());

		// check if our crossover method performs as expected
		System.out.println("\nCROSSOVER");
		System.out.println("Expected Number Of Children: " + (700 - mutatedPopulation.size() - population.size()));
		List<Individual> children = crossoverSelection(population, (700 - mutatedPopulation.size() - population.size()),
				1);
		System.out.println("Number Of Children Programs: " + children.size());

		// make sure our three lists are combined to create a population of 700
		System.out.println("\nNEW GENERATION");
		System.out.println("Expected Size: 700");
		System.out.println("Mutated Programs: " + mutatedPopulation.size());
		System.out.println("Parent Programs: " + population.size());
		System.out.println("Children Programs: " + children.size());
		System.out.println("Total Programs In New Generation: "
				+ (population.size() + children.size() + mutatedPopulation.size()));
	}

	private static List<Individual> createInitialPopulation(int genNum, int popSize, int genSize) {

		/*
		 * check the parameters for generationSize and genomeSize and create as
		 * many new Individual objects as needed
		 * 
		 */

		// create a new list to hold the new Individuals
		List<Individual> population = new ArrayList<Individual>();

		// add the specified number of new Individuals to the list
		for (int x = 0; x < popSize; x++) {
			population.add(new Individual(genSize, genNum));
		}

		// return the newly created list of Individuals
		return population;
	}

	/****************************************
	 * SELECTION
	 ****************************************/

	private static void evaluateEachExpression(List<Individual> generation, List<Integer> xTestSet,
			List<Double> yTestSet) {

		/*
		 * for every program in the generation List, compute y for each x value
		 * in the training set
		 */
		for (Individual eachGenome : generation) {

			calcDelta.calculateDelta(eachGenome.getProgramGenome(), xTestSet, yTestSet);

			eachGenome.setFitness(calcDelta.getDelta());
			eachGenome.setYValues(calcDelta.getYValues());
		}
	}

	private static List<Individual> discardUnfitIndividuals(int popSize, List<Individual> generation) {

		/*
		 * loop through generation list and only add Individuals we were able to
		 * calculate fitness to fitPrograms List
		 */
		List<Individual> fitPrograms = new ArrayList<Individual>();

		for (int x = 0; x < popSize; x++) {
			if (generation.get(x).getFitness() >= 0.0) {
				fitPrograms.add(generation.get(x));
			}

		}
		return fitPrograms;
	}

	private static List<Individual> performSelection(List<Individual> generation, double fitnessThreshold) {

		/*
		 * loop through the Individuals we were able to generate a fitness value
		 * for and separate out the most fit ones
		 */

		// get a list to hold the selected Individuals
		List<Individual> selectedIndividuals = new ArrayList<Individual>();

		// loop through the fit Individuals under the fitness threshold
		for (int x = 0; x < generation.size() * fitnessThreshold; x++) {

			// add those Individuals to the selected Individuals list
			selectedIndividuals.add(generation.get(x));
		}

		// return the completed list
		return selectedIndividuals;
	}

	/****************************************
	 * FITNESS IMPROVING
	 ****************************************/

	private static boolean testFitnessImproving(int stagCount, int stagThreshold, List<Individual> generation,
			double testAvg) {

		/*
		 * has our fitness stopped improving?
		 */

		// first check if we have enough data to make the decision
		if (stagCount <= stagThreshold) {
			// if we don't, nothing more to do but gather more info
			return true;
		} else {
			// if we do, check this generation's most fit Individual against the
			// average most fit Individual within our stagnationThreshold
			return generation.get(0).getFitness() - testAvg < -0.001;
		}
	}

	/****************************************
	 * MUTATION
	 ****************************************/

	private static List<Individual> mutationSelection(List<Individual> population, int size, double mutationPercent,
			int genNum) {

		/*
		 * at random, separate out the number of Individuals specified in the
		 * parameters, remove them from the main population, mutate them, and
		 * return a new list of the mutated Individuals
		 */

		// create new list to hold mutated Individuals
		List<Individual> mutatedPopulation = new ArrayList<Individual>();

		// select the correct amount of Individuals to be mutated
		for (int x = 0; x < size * mutationPercent; x++) {

			// select a number within the range of the population size
			int selection = rand.nextInt(population.size());

			// get the Individual at that location and mutate them recording
			// what generation this is happening in
			population.get(selection).mutate(genNum);
			// add that Individual to the mutated list
			mutatedPopulation.add(population.get(selection));
			// remove them from the population
			population.remove(selection);
		}

		// return our list with mutated Individuals
		return mutatedPopulation;
	}

	/****************************************
	 * CROSSOVER
	 ****************************************/

	private static List<Individual> crossoverSelection(List<Individual> population, int numberOfChildrenNeeded,
			int genNum) {

		/*
		 * this selected at random which Individuals to cross
		 */

		// create a new list to hold the children
		List<Individual> children = new ArrayList<Individual>();

		// run as many times as need to fill up the population
		for (int x = 0; x < numberOfChildrenNeeded; x++) {

			// select two Individuals at random
			int parentOne = rand.nextInt(population.size());
			int parentTwo = rand.nextInt(population.size());

			// breed them to get a child
			children.add(population.get(parentOne).crossExpressions(population.get(parentTwo), genNum));
		}

		// return our list with the children
		return children;
	}

}
