class MainTableElement {
	private int value;
	private boolean[] direction;

	MainTableElement(int v, boolean leftValue, boolean upValue, boolean diagonalValue) {
		value = v;
		this.direction = new boolean[]{leftValue, upValue, diagonalValue};
	}

	int getValue() {
		return value;
	}

	boolean isLeftEdge() {
		return direction[0];
	}

	boolean isUpEdge() {
		return direction[1];
	}

	boolean isDiagonalEdge() {
		return direction[2];
	}
}
