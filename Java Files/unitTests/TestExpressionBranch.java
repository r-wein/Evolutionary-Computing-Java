package unitTests;

import java.util.ArrayList;
import java.util.List;

import randomExpression.ExpressionBranch;

public class TestExpressionBranch {

	public static void main(String[] args) {
		
		/*
		 * 
		 * ExpressionBranch is a fairly simple class. Using the test cases 10
		 * 7+5-2*7, *, 79*++5-2*7 we were able to gain statement, branch, condition, and
		 * path coverage
		 * 
		 */

		List<ExpressionBranch> branches = new ArrayList<ExpressionBranch>();
		
		// test branch size input in String Expression
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

				branches.add(new ExpressionBranch(branchSizes.get(position)));
				System.out.println(branches.get(position).getBranch());
				System.out.println("Valid: " + branches.get(position).isValid());

			} catch (Exception e) {
				System.out.println("Cannot build branch of size " + branchSizes.get(position) + ".");
			}

			System.out.println("Expected Validity: " + expectedValidity1.get(position));

			position++;
		}

		System.out.println("______________________________________________________________________");

		branches = new ArrayList<ExpressionBranch>();

		System.out.println("\nAltering Constructed Branches\n");

		List<String> alteredBranches = new ArrayList<String>();
		alteredBranches.add("7+5-2*7");
		alteredBranches.add("*");
		alteredBranches.add("79*++5-2*7");

		List<String> expectedValidity2 = new ArrayList<String>();
		expectedValidity2.add("true");
		expectedValidity2.add("false");
		expectedValidity2.add("false");


		for (int x = 0; x < 3; x++) {

			System.out.print("Constructed Branch: ");
			branches.add(new ExpressionBranch(15));
			System.out.println(branches.get(x).getBranch());
			System.out.println("Altered Branch: " + alteredBranches.get(x));
			branches.get(x).setBranch(alteredBranches.get(x));

			System.out.print("Valid After Altering Branches:    ");
			System.out.println(branches.get(x).isValid());

			System.out.print("Expected Validity After Altering: ");
			System.out.println(expectedValidity2.get(x) + "\n");

		}
	}
}
