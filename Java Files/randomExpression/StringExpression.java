package randomExpression;

import java.util.Random;

public abstract class StringExpression {

	/****************************************
	 * MEMBER VARIABLES
	 ****************************************/

	private int branchSize;
	protected Random randomNumber;
	private String[] operators = { "+", "-", "*", "/" };

	/****************************************
	 * CONSTRUCTOR
	 ****************************************/

	public StringExpression(int size) {
		this.branchSize = size;
		this.randomNumber = new Random();
	}

	/****************************************
	 * OVERRIDE
	 ****************************************/

	@Override
	public String toString() {
		return "Equation Size: " + this.branchSize;
	}

	public int getBranchSize() {
		return this.branchSize;
	}

	/****************************************
	 * METHOD
	 ****************************************/

	/** CHOOSE WHICH OPERAND */
	public String mathOperator() {

		// get a random number between 0 and 3
		int operatorSelection = randomNumber.nextInt(4);

		// depending on the chosen number, return one of the available operands
		return operators[operatorSelection];
	}

	protected boolean validOperator(String operator) {
		if (operator.length() == 1) {
			return operator.equals("+") || operator.equals("-") || operator.equals("/") || operator.equals("*");
		} else {
			return false;
		}
	}

	protected int validSizeInput(int size) {

		if (size > 0) {
			return size;
		} else {
			throw new IllegalArgumentException("Expression size must be greater than 0");
		}
	}
}