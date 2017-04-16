package randomExpression;

public class ExpressionBranch extends StringExpression {

	/****************************************
	 * MEMBER VARIABLES
	 ****************************************/

	private String branchExpression;

	/****************************************
	 * CONSTRUCTOR
	 ****************************************/

	public ExpressionBranch(int size) {
		super(size);
		this.branchExpression = this.createBranch(super.getBranchSize());
	}

	/****************************************
	 * OVERRIDE
	 ****************************************/

	@Override
	public String toString() {
		return super.toString() + "\nBranch Expression: " + this.branchExpression;
	}

	/****************************************
	 * GETTER / SETTER
	 ****************************************/

	// get the expression
	public String getBranch() {
		return this.branchExpression;
	}

	// set the expression
	public void setBranch(String alteredBranch) {
		this.branchExpression = alteredBranch;
	}

	/****************************************
	 * CREATE BRANCH EXPRESSION
	 ****************************************/

	// where we actually create the expression
	public String createBranch(int depth) {

		/*
		 * create an int that'll hold a random odd number within out range;
		 * number must be odd b/c an even number will cause errors e.g. 5 * 6 /
		 * 7 is find with a length of 5 whereas 5 * 6 / 7 * is not with a length
		 * of 6
		 */
		int equationLength = this.randomOddNumberInRange(depth);

		// blank string to hold constructedExpression
		String constructedExpression = "";

		for (int x = 0; x < equationLength; x++) {

			// if we are dealing with an even number
			// this is where we can add 0...9 or x
			if (x % 2 == 0) {

				constructedExpression = addOperand(constructedExpression);
			} else {
				// if we are dealing with x being odd, let's add an operand
				constructedExpression += super.mathOperator();
			}
		}
		return constructedExpression;
	}

	// how we choose what operand to pick
	private String addOperand(String constructedExpression) {

		// have the computer randomly pick a number between 0 and 9
		int a = randomNumber.nextInt(10);

		// if the computer chose 5-9
		if (a > 4) {
			// let's add a number between 0 and 9
			constructedExpression += randomNumber.nextInt(10);

		} else {
			// if the computer chose 1-4, let's add an x
			constructedExpression += "x";
		}
		return constructedExpression;
	}

	// here's where we ensure a number is even
	private int randomOddNumberInRange(int depth) {

		// pick a random number within range
		int equationLength = randomNumber.nextInt(depth);

		// see if random number is odd, if not add 1 to it
		return equationLength % 2 == 0 ? ++equationLength : equationLength;
	}

	/****************************************
	 * CHECK BRANCH EXPRESSION
	 ****************************************/

	public boolean isValid() {
		for (int x = 0; x < this.branchExpression.length(); x++) {
			if (x % 2 == 0) {
				if (!this.validOperand(this.branchExpression.charAt(x))) {
					return false;
				}
			} else {
				if (!super.validOperator(String.valueOf(this.branchExpression.charAt(x)))
						|| x == this.branchExpression.length() - 1) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean validOperand(char operand) {

		return String.valueOf(operand).matches("[0-9x]+");
	}
}
