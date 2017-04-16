package unitTests;

import java.util.ArrayList;
import java.util.List;

import randomExpression.RandomStringExpression;

public class TestRandomStringExpression {

	public static void main(String[] args) {
		
		/*
		 * This test StringExpression and RandomStringExpression.  They are both fairly simple classes. Using the test cases
		 * =,-,*,/,?,/x*x-1,7+5-2*7,79*++5-2*7 we were able to achieve statement, branch, condition, and
		 * path coverage
		 * 
		 */

		List<RandomStringExpression> stringExp = new ArrayList<RandomStringExpression>();

		List<Integer> branchSizes = new ArrayList<Integer>();
		branchSizes.add(10);
		branchSizes.add(0);

		List<String> expectedValidity1 = new ArrayList<String>();
		expectedValidity1.add("true");
		expectedValidity1.add("Size must be >0, no branch constructed");

		int position = 0;

		System.out.println("Build Random Expression Branches:");

		for (int x = 0; x < 2; x++) {

			System.out.print("\nBranch " + (position + 1) + ": ");

			try {

				stringExp.add(new RandomStringExpression(branchSizes.get(position)));
				System.out.println(stringExp.get(position).getRandomExpression());
				System.out.println("Valid: " + stringExp.get(position).isValid());

			} catch (Exception e) {
				System.out.println("Cannot build branch of size " + branchSizes.get(position) + ".");
			}

			System.out.println("Expected Validity: " + expectedValidity1.get(position));

			position++;
		}

		System.out.println("______________________________________________________________________");

		stringExp = new ArrayList<RandomStringExpression>();

		System.out.println("\nAltering Constructed Branches\n");

		List<String> alteredExpressions = new ArrayList<String>();
		alteredExpressions.add("7+5-2*7");
		alteredExpressions.add("79*++5-2*7");

		List<String> alteredRoots = new ArrayList<String>();
		alteredRoots.add("+");
		alteredRoots.add("-");
		alteredRoots.add("/");
		alteredRoots.add("*");
		alteredRoots.add("?");
		alteredRoots.add("/x*x-1");

		List<String> expectedValidity2 = new ArrayList<String>();
		expectedValidity2.add("true");
		expectedValidity2.add("false");
		expectedValidity2.add("true");
		expectedValidity2.add("false");
		expectedValidity2.add("true");
		expectedValidity2.add("true");
		expectedValidity2.add("true");
		expectedValidity2.add("true");
		expectedValidity2.add("false");
		expectedValidity2.add("false");

		int place = 0;

		for (int i = 0; i < 2; i++) {

			System.out.print("Constructed Branch: ");
			stringExp.add(new RandomStringExpression(15));
			System.out.println(stringExp.get(i).getRandomExpression());

			for (int x = 0; x < 2; x++) {

				if (i == 0) {

					System.out.println("\nAltering The Left Branch:");
					System.out.println("Branch: " + stringExp.get(i).getLeftBranch());
					System.out.println("Altered Branch: " + alteredExpressions.get(x));
					stringExp.get(i).setLeftBranch(alteredExpressions.get(x));
					

				} else if (i == 1) {

					System.out.println("Altering The Right Branch:");
					System.out.println("Branch: " + stringExp.get(i).getRightBranch());
					System.out.println("Altered Branch: " + alteredExpressions.get(x));
					stringExp.get(i).setRightBranch(alteredExpressions.get(x));

				} 

				System.out.print("Valid After Altering Branches:    ");
				System.out.println(stringExp.get(i).isValid());

				System.out.print("Expected Validity After Altering: ");
				System.out.println(expectedValidity2.get(place) + "\n");
				place++;
			}
			
		}
		
		for (int x = 0; x < 6; x++) {
			
			System.out.print("Constructed Branch: ");
			stringExp.add(new RandomStringExpression(15));
			System.out.println(stringExp.get(2).getRandomExpression());


			System.out.println("Altering The Root Operator:");
			System.out.println("Root: " + stringExp.get(2).getRootOperator());
			System.out.println("Altered Root: " + alteredRoots.get(x));
			stringExp.get(2).setRootOperator(alteredRoots.get(x));
			
			System.out.print("Valid After Altering Branches:    ");
			System.out.println(stringExp.get(2).isValid());

			System.out.print("Expected Validity After Altering: ");
			System.out.println(expectedValidity2.get(place) + "\n");
			place++;
		}

		
	}

}
