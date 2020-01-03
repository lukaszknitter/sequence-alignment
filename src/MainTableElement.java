class MainTableElement {
	private int value;
	private boolean leftEdge;
	private boolean topEdge;
	private boolean diagonalEdge;

	MainTableElement(int v, boolean leftValue, boolean topValue, boolean diagonalValue) {
		value = v;
		leftEdge = leftValue;
		topEdge = topValue;
		diagonalEdge = diagonalValue;
	}

	boolean isLeftEdge() {
		return leftEdge;
	}

	boolean isTopEdge() {
		return topEdge;
	}

	boolean isDiagonalEdge() {
		return diagonalEdge;
	}

	int getValue() {
		return value;
	}

}
