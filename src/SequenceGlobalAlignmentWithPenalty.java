import java.io.IOException;
import java.util.ArrayList;

public class SequenceGlobalAlignmentWithPenalty {
	private MainTableElement[][] S;
	private PenaltyData data;

	public SequenceGlobalAlignmentWithPenalty(String filePath) throws IOException {
		this.data = new PenaltyData(filePath);
	}

	public void doStuff() {
		fillScoringTables();
		PrintUtils.printTable(S);
		final ArrayList<ArrayList<PointInTable>> resultList = countResults();
		PrintUtils.printResults(getResultsList(resultList));
	}

	private int penaltyFunction(int n) {
		return 2 * n;
	}

	private void fillScoringTables() {
		final int firstSequenceLength = data.firstSequenceLength;
		final int secondSequenceLength = data.secondSequenceLength;
		final String firstSequence = data.firstSequence;
		final String secondSequence = data.secondSequence;

		S = new MainTableElement[firstSequenceLength + 1][secondSequenceLength + 1];
		int[][] C = new int[firstSequenceLength + 1][firstSequenceLength + 1];
		int[][] A = new int[firstSequenceLength + 1][firstSequenceLength + 1];
		int[][] B = new int[firstSequenceLength + 1][firstSequenceLength + 1];

		S[0][0] = new MainTableElement(0, false, false, false);
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			S[i][0] = new MainTableElement(-penaltyFunction(i), true, false, false);
			B[i][0] = -penaltyFunction(i);
			C[i][0] = S[i][0].getValue();
		}
		for (int i = 1; i < secondSequenceLength + 1; i++) {
			S[0][i] = new MainTableElement(-penaltyFunction(i), false, true, false);
			A[0][i] = -penaltyFunction(i);
			C[0][i] = S[0][i].getValue();
		}

		int actual_max;
		int max;
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			for (int j = 1; j < secondSequenceLength + 1; j++) {
				String actualCharFirstSq = Character.toString(firstSequence.charAt(i - 1));
				String actualCharSecondSq = Character.toString(secondSequence.charAt(j - 1));
				C[i][j] = S[i - 1][j - 1].getValue() + data.getSimilarityBetweenElements(actualCharFirstSq, actualCharSecondSq);

				max = -10000;
				for (int k = 0; k < j; k++) {
					actual_max = Math.max(B[i][k], C[i][k]) - penaltyFunction(j - k);
					if (actual_max > max) {
						max = actual_max;
					}
				}
				A[i][j] = max;

				max = -10000;
				for (int k = 0; k < i; k++) {
					actual_max = Math.max(A[k][j], C[k][j]) - penaltyFunction(i - k);
					if (actual_max > max) {
						max = actual_max;
					}
				}
				B[i][j] = max;

				S[i][j] = findMax(B[i][j], A[i][j], C[i][j]);
			}
		}
	}

	private MainTableElement findMax(int left, int up, int diagonal) {
		int max = Math.max(Math.max(left, up), diagonal);

		return new MainTableElement(max, max == left, max == up, max == diagonal);
	}

	private ArrayList<ArrayList<PointInTable>> countResults() {
		ArrayList<ArrayList<PointInTable>> list = new ArrayList<>();
		ArrayList<PointInTable> localResult = new ArrayList<>();
		int x = data.firstSequenceLength;
		int y = data.secondSequenceLength;
		PointInTable actualPoint = new PointInTable(x, y);
		localResult.add(actualPoint);
		list.add(localResult);
		int counter = 0;
		while (continueCondition(list)) {
			localResult = list.get(counter);
			x = localResult.get(localResult.size() - 1).getX();
			y = localResult.get(localResult.size() - 1).getY();
			while (x != 0 && y != 0) {
				int edgesCounter = 0;
				if (S[x][y].isTopEdge()) {
					actualPoint = new PointInTable(x, y - 1);
					edgesCounter++;
				}
				if (S[x][y].isLeftEdge()) {
					if (edgesCounter != 0) {
						ArrayList<PointInTable> clone = new ArrayList<>(localResult);
						clone.add(new PointInTable(x - 1, y));
						list.add(clone);

					} else {
						actualPoint = new PointInTable(x - 1, y);
					}
					edgesCounter++;
				}
				if (S[x][y].isDiagonalEdge()) {
					if (edgesCounter != 0) {
						ArrayList<PointInTable> clone = new ArrayList<>(localResult);
						clone.add(new PointInTable(x - 1, y - 1));
						list.add(clone);

					} else {
						actualPoint = new PointInTable(x - 1, y - 1);
					}
					edgesCounter++;
				}
				if (actualPoint.getX() == (x - 1) && actualPoint.getY() == (y - 1)) {
					x--;
					y--;
				} else if (actualPoint.getX() == x && actualPoint.getY() == (y - 1)) {
					y--;
				} else {
					x--;
				}
				localResult.add(actualPoint);
				if (edgesCounter == 0) {
					list.remove(localResult);
					counter--;
					break;
				}
			}
			counter++;
		}
		return list;
	}

	private ArrayList<String> getResultsList(ArrayList<ArrayList<PointInTable>> lists) {
		final int firstSequenceLength = data.firstSequenceLength;
		final int secondSequenceLength = data.secondSequenceLength;
		final String firstSequence = data.firstSequence;
		final String secondSequence = data.secondSequence;

		ArrayList<String> resultsList = new ArrayList<>();

		int actual_x = 0, actual_y = 0;
		int recent_x = firstSequenceLength + 1;
		int recent_y = secondSequenceLength + 1;
		for (ArrayList<PointInTable> list : lists) {
			StringBuilder firstOutput = new StringBuilder();
			StringBuilder secondOutput = new StringBuilder();
			for (PointInTable point : list) {
				actual_x = point.getX();
				actual_y = point.getY();
				if (actual_x != firstSequenceLength || actual_y != secondSequenceLength) {

					if (recent_x == actual_x) {
						firstOutput.append(" ");
					} else {
						firstOutput.append(firstSequence.charAt(actual_x));
					}
					if (recent_y == actual_y) {
						secondOutput.append(" ");
					} else {
						secondOutput.append(secondSequence.charAt(actual_y));
					}
				}
				recent_x = actual_x;
				recent_y = actual_y;

			}
			while (actual_x > 0) {
				actual_x--;
				secondOutput.append(" ");
				firstOutput.append(firstSequence.charAt(actual_x));
			}
			while (actual_y > 0) {
				actual_y--;
				firstOutput.append(" ");
				secondOutput.append(secondSequence.charAt(actual_y));
			}
			firstOutput = new StringBuilder(new StringBuilder(firstOutput.toString()).reverse().toString());
			secondOutput = new StringBuilder(new StringBuilder(secondOutput.toString()).reverse().toString());

			resultsList.add(firstOutput.toString());
			resultsList.add(secondOutput.toString());
		}
		return resultsList;
	}

	private boolean continueCondition(ArrayList<ArrayList<PointInTable>> lists) {
		if (lists.isEmpty()) return true;
		for (ArrayList<PointInTable> list : lists) {
			if (list.get(list.size() - 1).getX() != 0 && list.get(list.size() - 1).getY() != 0) {
				return true;
			}
		}
		return false;
	}
}
