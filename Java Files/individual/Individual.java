package individual;

import java.util.*;

import randomExpression.ExpressionBranch;
import randomExpression.RandomStringExpression;

public class Individual implements Comparable<Individual> {

	/********************************************
	 ** MEMBER VARIABLES
	 ********************************************/

	private RandomStringExpression geneticMakeup;
	private IndividualStatistics stats;
	private Random randomNumber;
	private double fitness;
	private List<Double> yValues;

	/********************************************
	 ** CONSTRUCTOR
	 ********************************************/

	// default constructor
	public Individual(RandomStringExpression genome) {
		this.geneticMakeup = genome;
		this.randomNumber = new Random();
		this.yValues = new ArrayList<Double>();
	}

	// constructor for generated Individuals
	public Individual(int expressionSizeLimit, int birthGeneration) {
		this(new RandomStringExpression(expressionSizeLimit));
		this.stats = new IndividualStatistics(birthGeneration);
	}

	// constructor for bred Individuals
	public Individual(RandomStringExpression child, int birthGeneration, String[] parents) {
		this(child);
		this.stats = new IndividualStatistics(birthGeneration, parents);
	}

	/********************************************
	 ** OVERRIDE
	 ********************************************/

	@Override
	public String toString() {

		/*
		 * prints to console the expression, its current fitness level, parents,
		 * whether or not the Individual mutated and in what generations it
		 * mutated, and it this Individual bred at all
		 */

		return "Genome: " + this.getProgramGenome() + "\nFitness: " + this.fitness + "\nY Values: "
				+ this.yValues.toString() + "\n" + this.getIndividualStats();
	}

	@Override
	public int compareTo(Individual compareIndividual) {

		/*
		 * this is from the Comparable interface, it allows for easy sorting
		 * amongst objects. In this case we are comparing fitness values where
		 * it will sort Individuals in terms of who has the lowest fitness value
		 * (from most fit to least fit)
		 */

		if (this.fitness == compareIndividual.fitness) {
			return 0;
		} else {
			return this.fitness > compareIndividual.fitness ? 1 : -1;
		}

	}

	/********************************************
	 ** GETTERS
	 ********************************************/

	// returns the RandomProgram Object
	public RandomStringExpression getGeneticMakeup() {
		return this.geneticMakeup;
	}

	// returns the randomly constructed program
	public String getProgramGenome() {
		return this.geneticMakeup.getRandomExpression();
	}

	// returns the Individuals stats
	public String getIndividualStats() {
		return this.stats.toString();
	}

	// returns the fitness of the Individual
	public double getFitness() {
		return this.fitness;
	}

	// returns the Y Values for the Individual
	public List<Double> getYValues() {
		return this.yValues;
	}

	/********************************************
	 ** GETTERS
	 ********************************************/

	// allows us to set the fitness level for this Individual
	public void setFitness(double fit) {
		this.fitness = fit;
	}

	// allows us to set the Y Values for this Individual
	public void setYValues(List<Double> yVal) {
		this.yValues = yVal;
	}

	/********************************************
	 ** MUTATE METHODS
	 ********************************************/

	public void mutate(int genNum) {

		// selects right, left, or operand
		int numSelection = this.randomNumber.nextInt(11);

		if (numSelection < 3) {
			// mutate the root operator
			rootOperatorMutation();
		} else {
			// mutate one of the branches
			branchMutation(numSelection);
		}

		this.stats.expressionMutated(genNum);
	}

	private void branchMutation(int numSelection) {

		/*
		 * if the random number selected is less than 7, were dealing with the
		 * left side of the equation if the number is 7 or greater, we are
		 * dealing with the right side
		 */
		String branchToAlter = numSelection < 7 ? this.geneticMakeup.getLeftBranch() : this.geneticMakeup.getRightBranch();

		// select two points within the longer string
		int[] genomeSelection = this.getRandomGenomePoints(branchToAlter.length());

		/*
		 * find that selection, mutate it, and place it back into the string at
		 * the correct point
		 */
		int mutatedSize = genomeSelection[1] - genomeSelection[0] > 0 ? genomeSelection[1] - genomeSelection[0] : 1;
		
		ExpressionBranch bitString = new ExpressionBranch(mutatedSize);
		
		String firstPart = branchToAlter.substring(0, genomeSelection[0]);
		
		String secondPart = branchToAlter.substring(genomeSelection[1] + 1, branchToAlter.length());
		
		branchToAlter = firstPart + bitString.getBranch() + secondPart;

		// make sure we update the correct side
		if (numSelection < 7) {
			this.geneticMakeup.setLeftBranch(branchToAlter);
		} else {
			this.geneticMakeup.setRightBranch(branchToAlter);
		}
	}

	private void rootOperatorMutation() {

		// get the middle operand from the random program
		String rootToAlter = this.geneticMakeup.getRootOperator();

		// make sure the operand does not mutate to itself
		boolean somethingNew = false;
		while (!somethingNew) {

			// get a random operand
			String operand = this.geneticMakeup.mathOperator();

			// if it is not the same as the current operand
			if (!operand.equals(rootToAlter)) {

				// change the string and exit loop
				rootToAlter = operand;
				this.geneticMakeup.setRootOperator(rootToAlter);
				somethingNew = true;
			}
		}
	}

	/********************************************
	 ** CROSSOVER METHODS
	 ********************************************/

	public Individual crossExpressions(Individual otherGenome, int genNumber) {

		// record crossing
		this.stats.expressionCrossed();

		// get the parts needed to make a new child
		String[] child = cross(otherGenome);

		// create the new child
		RandomStringExpression crossedChild = new RandomStringExpression(this.geneticMakeup.getBranchSize(), child);

		// return the completed child
		return new Individual(crossedChild, genNumber, new String[] {this.getProgramGenome(), otherGenome.getProgramGenome()});
	}

	private String[] cross(Individual otherGenome) {

		// size is 3 because we need a left branch, a root operator, and a right
		// branch
		String[] child = new String[3];

		// pick true or false randomly and assign either the left or right side
		// of the
		boolean myLeftSide = this.randomNumber.nextBoolean();
		boolean theirLeftSide = this.randomNumber.nextBoolean();

		// get the returned swapped genomes
		String[] swapped = swapGenome(new boolean[] {myLeftSide, theirLeftSide}, otherGenome);

		// update both genome's left side
		if (myLeftSide && theirLeftSide) {
			child[0] = swapped[0];
			child[1] = this.geneticMakeup.getRootOperator();
			child[2] = otherGenome.geneticMakeup.getRightBranch();

			// update this genome's left and other's right
		} else if (myLeftSide && !theirLeftSide) {
			child[0] = swapped[0];
			child[1] = this.geneticMakeup.getRootOperator();
			child[2] = swapped[1];

			// update this genome's right and other's left
		} else if (!myLeftSide && theirLeftSide) {
			child[0] = swapped[1];
			child[1] = otherGenome.geneticMakeup.getRootOperator();
			child[2] = this.geneticMakeup.getLeftBranch();

			// update both genome's right side
		} else {
			child[0] = swapped[1];
			child[1] = otherGenome.geneticMakeup.getRootOperator();
			child[2] = swapped[0];
		}

		return child;
	}

	private String[] swapGenome(boolean[] sides, Individual otherGenome) {

		// get the correct side
		String myGenomeCross = getSide(sides[0], this);
		String theirGenomeCross = getSide(sides[1], otherGenome);

		// get a random selection from both myGenome and otherGenome
		int[] myGenomeSelection = this.getRandomGenomePoints(myGenomeCross.length());
		String getMySelection = myGenomeCross.substring(myGenomeSelection[0], myGenomeSelection[1] + 1);

		int[] theirGenomeSelection = this.getRandomGenomePoints(theirGenomeCross.length());
		String getTheirSelection = theirGenomeCross.substring(theirGenomeSelection[0], theirGenomeSelection[1] + 1);

		// swap the selected sections
		String myAlteredGenome = myGenomeCross.substring(0, myGenomeSelection[0]) + getTheirSelection
				+ myGenomeCross.substring(myGenomeSelection[1] + 1, myGenomeCross.length());
		String theirAlteredGenome = theirGenomeCross.substring(0, theirGenomeSelection[0]) + getMySelection
				+ theirGenomeCross.substring(theirGenomeSelection[1] + 1, theirGenomeCross.length());

		// collect the swapped parts
		String[] swapped = { myAlteredGenome, theirAlteredGenome };

		return swapped;
	}

	private String getSide(boolean side, Individual genome) {
		// return the correct side of the equation
		return side ? genome.geneticMakeup.getLeftBranch() : genome.geneticMakeup.getRightBranch();
	}

	/********************************************
	 ** PRIVATE METHODS
	 ********************************************/

	private int[] getRandomGenomePoints(int length) {

		/*
		 * if the length of the equation side is 1, no need to do the rest of
		 * the logic, just return that one point
		 */
		if (length == 1) {
			return new int[] {0,0};
		} else {
			return getEvenPoints(length);
		}
	}

	private int[] getEvenPoints(int length) {
		
		int[] randomPoints = getRandomPoints(length);

		/*
		 * make sure both points are even; this ensure that we do not swap a
		 * operator with an operand, we are only switching operands
		 */
		randomPoints[0] = this.makeEven(randomPoints[0]);
		randomPoints[1] = this.makeEven(randomPoints[1]);

		// order the points and return them
		return orderPoints(randomPoints);
	}
	
	private int[] orderPoints (int[] randomPoints) {
		
		// find out which point is earlier in the String
		if (randomPoints[0] > randomPoints[1]) {
			int temp = randomPoints[0];
			randomPoints[0] = randomPoints[1];
			randomPoints[1] = temp;
		}

		return randomPoints;
	}
	
	private int[] getRandomPoints(int length) {
		
		// select two random points within the given range
		int firstPoint = this.randomNumber.nextInt(length);
		int secondPoint = this.randomNumber.nextInt(length);

		// if we select the same point, find another one
		while (firstPoint == secondPoint) {
			secondPoint = this.randomNumber.nextInt(length);
		}
		
		return new int[] {firstPoint, secondPoint};
	}

	private int makeEven(int number) {

		// check if the number provided is divisible by 2
		if (number % 2 == 0) {
			// if so, return that number
			return number;
		} else {
			// if not, subtract 1
			return number - 1;
		}
	}
}
