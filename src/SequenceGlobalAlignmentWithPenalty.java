import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class SequenceGlobalAlignmentWithPenalty {

	public String filePathString;
	public String firstSequence;
	public String secondSequence;
	protected int[][] C;
	protected int[][] A;
	protected int[][] B;
	protected MainTableElement[][] S;
	protected int firstSequenceLength;
	protected int secondSequenceLength;
	protected int ALPHABET_LENGTH;
	protected int[][] similarityTable;
	protected ArrayList<String> alphabet;
	protected ArrayList<ArrayList<PointInTable>> resultList;

	public SequenceGlobalAlignmentWithPenalty(String filePath) {
		this.filePathString = filePath;
		resultList = new ArrayList<>();
	}

	public void doStuff() throws IOException {
		readAndInitData();
		countTables();
		Utils.printTable(S);
		countResults(S, resultList);
		Utils.printResults(getResultsList(resultList));
	}

	private int penaltyFunction(int n) {
		return 2 * n;
	}

	protected int SimilarityBetweenElements(String a, String b) {
		return similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}

	public void countTables() {
		S = new MainTableElement[firstSequenceLength + 1][secondSequenceLength + 1];
		C = new int[firstSequenceLength + 1][firstSequenceLength + 1];
		A = new int[firstSequenceLength + 1][firstSequenceLength + 1];
		B = new int[firstSequenceLength + 1][firstSequenceLength + 1];

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
				C[i][j] = S[i - 1][j - 1].getValue() + SimilarityBetweenElements(actualCharFirstSq, actualCharSecondSq);

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


	public void readAndInitData() throws IOException {
		Path filePath = Paths.get(filePathString);
		Scanner scanner = new Scanner(filePath);
		firstSequence = scanner.nextLine();
		secondSequence = scanner.nextLine();

		ALPHABET_LENGTH = scanner.nextInt();

		similarityTable = new int[ALPHABET_LENGTH][ALPHABET_LENGTH];
		for (int i = 0; i < ALPHABET_LENGTH; i++) {
			for (int j = 0; j < ALPHABET_LENGTH; j++) {
				similarityTable[i][j] = scanner.nextInt();
			}
		}

		alphabet = new ArrayList<>();
		for (int i = 0; i < ALPHABET_LENGTH; i++) alphabet.add(scanner.next());

		firstSequenceLength = firstSequence.length();
		secondSequenceLength = secondSequence.length();
	}

	public void countResults(MainTableElement[][] table, ArrayList<ArrayList<PointInTable>> list) {
		ArrayList<PointInTable> localResult = new ArrayList<>();
		int x = firstSequenceLength;
		int y = secondSequenceLength;
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
				if (table[x][y].isTopEdge()) {
					actualPoint = new PointInTable(x, y - 1);
					edgesCounter++;
				}
				if (table[x][y].isLeftEdge()) {
					if (edgesCounter != 0) {
						ArrayList<PointInTable> clone = new ArrayList<>();
						for (PointInTable item : localResult) clone.add(item);
						clone.add(new PointInTable(x - 1, y));
						list.add(clone);

					} else {
						actualPoint = new PointInTable(x - 1, y);
					}
					edgesCounter++;
				}
				if (table[x][y].isDiagonalEdge()) {
					if (edgesCounter != 0) {
						ArrayList<PointInTable> clone = new ArrayList<>();
						for (PointInTable item : localResult) clone.add(item);
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

	public ArrayList<String> getResultsList(ArrayList<ArrayList<PointInTable>> lists) {
		ArrayList<String> resultsList = new ArrayList<>();

		int actual_x = 0, actual_y = 0;
		int recent_x = firstSequenceLength + 1;
		int recent_y = secondSequenceLength + 1;
		for (ArrayList<PointInTable> list : lists) {
			String firstOutput = "";
			String secondOutput = "";
			for (PointInTable point : list) {
				actual_x = point.getX();
				actual_y = point.getY();
				if (actual_x != firstSequenceLength || actual_y != secondSequenceLength) {

					if (recent_x == actual_x) {
						firstOutput = firstOutput + (" ");
					} else {
						firstOutput = firstOutput + (firstSequence.charAt(actual_x));
					}
					if (recent_y == actual_y) {
						secondOutput = secondOutput + (" ");
					} else {
						secondOutput = secondOutput + (secondSequence.charAt(actual_y));
					}
				}
				recent_x = actual_x;
				recent_y = actual_y;

			}
			while (actual_x > 0) {
				actual_x--;
				secondOutput = secondOutput + (" ");
				firstOutput = firstOutput + (firstSequence.charAt(actual_x));
			}
			while (actual_y > 0) {
				actual_y--;
				firstOutput = firstOutput + (" ");
				secondOutput = secondOutput + (secondSequence.charAt(actual_y));
			}
			firstOutput = new StringBuilder(firstOutput).reverse().toString();
			secondOutput = new StringBuilder(secondOutput).reverse().toString();

			resultsList.add(firstOutput);
			resultsList.add(secondOutput);
		}
		return resultsList;
	}
}
