package individual;

import java.util.ArrayList;
import java.util.List;

public class IndividualStatistics {

	/********************************************
	 ** MEMBER VARIABLES
	 ********************************************/

	private int birthGeneration;
	private String[] parents;
	private int mutationCount;
	private List<Integer> mutateGenerations;
	private int crossTotal;

	/********************************************
	 ** CONSTRUCTORS
	 ********************************************/

	public IndividualStatistics() {
		this.mutationCount = 0;
		this.mutateGenerations = new ArrayList<Integer>();
		this.crossTotal = 0;
	}

	public IndividualStatistics(int birthGen) {
		this();
		this.birthGeneration = birthGen;
		this.parents = null;
	}

	public IndividualStatistics(int birthGen, String[] parents) {
		this();
		this.birthGeneration = birthGen;
		this.parents = parents;
	}

	/********************************************
	 ** OVERRIDE
	 ********************************************/

	@Override
	public String toString() {

		return "Birth Generation: " + this.birthGeneration + "\n" + this.getParents() + "\nMutation Count: "
				+ this.mutationCount + "\nMutation Generations: " + this.mutateGenerations.toString()
				+ "\nTimes Crossed: " + this.crossTotal;

	}

	/********************************************
	 ** GETTERS
	 ********************************************/

	// returns the generation this Individual was born in
	public int getBirthGeneration() {
		return this.birthGeneration;
	}

	// returns the Individual's parents (prints no parents if 1st Generation)
	public String getParents() {

		String parents = "Parents: ";

		if (this.parents != null) {
			return parents + this.parents[0] + ", " + this.parents[1];
		} else {
			return parents + "No Parents, 1st Generation";
		}
	}

	// returns the number of times this Individual mutated
	public int getMutationCount() {
		return this.mutationCount;
	}

	// return in what generations the Individual mutate
	public List<Integer> getMutateGenerations() {
		return mutateGenerations;
	}

	// returns the number of times this Individual was crossed with another to
	// make a child Individual
	public int getCrossTotal() {
		return crossTotal;
	}

	/********************************************
	 ** METHODS
	 ********************************************/

	// increments mutation count and logs in what generation this Individual mutated
	public void expressionMutated(int generationMutated) {
		this.mutationCount++;
		this.mutateGenerations.add(generationMutated);
	}

	// logs that this Individual has crossed with another
	public void expressionCrossed() {
		this.crossTotal++;
	}
}
