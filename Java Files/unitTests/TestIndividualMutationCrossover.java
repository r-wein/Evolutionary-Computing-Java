package unitTests;

import java.util.*;

import individual.Individual;

public class TestIndividualMutationCrossover {

	/*
	 * This class cannot take any input since it creates everything
	 * automatically. I had to closely inspect the results after several runs to
	 * ensure that we achieved the coverage we were expecting
	 */

	public static void main(String[] args) {

		List<Individual> testInd = new ArrayList<Individual>();

		for (int x = 0; x < 10; x++) {
			testInd.add(new Individual(5, 1));
		}

		showGrid(testInd, "Randomly Create 10 Expressions");

		for (int x = 0; x < 10; x++) {
			testInd.get(x).mutate(2);
		}

		showGrid(testInd, "Mutate The 10 Expressions");

		Random rand = new Random();

		List<Individual> children = new ArrayList<Individual>();

		for (int x = 0; x < 10; x++) {

			int firstParent = rand.nextInt(testInd.size());
			int secondParent = rand.nextInt(testInd.size());

			while (firstParent == secondParent) {
				secondParent = rand.nextInt(testInd.size());
			}

			children.add(testInd.get(firstParent).crossExpressions(testInd.get(secondParent), 3));
		}

		showGrid(children, "Offspring Of 10 Randomly Crossed Expressions");

	}

	public static void showGrid(List<Individual> testGroup, String title) {

		System.out.println("\t" + title + "\n");
		System.out.println("   #\t|\tExpression\t\t|\tisValid");
		System.out.println("_______________________________________________________");

		int place = 0;

		for (Individual eachIndividual : testGroup) {
			System.out.printf("   %d\t|\t%s  \t\t|\t%s", place++, eachIndividual.getProgramGenome(),
					eachIndividual.getGeneticMakeup().isValid());
			System.out.println();

		}

		System.out.println("\n====================================================================\n");
	}
}
