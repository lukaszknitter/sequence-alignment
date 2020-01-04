package feature;

public class MainTableElement {
	private int value;
	private boolean leftEdge;
	private boolean topEdge;
	private boolean diagonalEdge;

	public MainTableElement(int v, boolean leftValue, boolean topValue, boolean diagonalValue) {
		value = v;
		leftEdge = leftValue;
		topEdge = topValue;
		diagonalEdge = diagonalValue;
	}

	public boolean isLeftEdge() {
		return leftEdge;
	}

	public boolean isTopEdge() {
		return topEdge;
	}

	public boolean isDiagonalEdge() {
		return diagonalEdge;
	}

	public int getValue() {
		return value;
	}
}
