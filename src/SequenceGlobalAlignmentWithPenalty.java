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
		printTable(S);
		countResults(S, resultList);
		printResults(resultList);
	}

	private int penaltyFunction(int n) {
		return 2 * n;
	}

	protected int SimilarityBetweenElements(String a, String b) {
		int similarity = similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
		return similarity;
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

	public void printTable(MainTableElement[][] table) {
		System.out.println("Main global table:");
		System.out.println("--------------------------");
		String value, separator;
		for (int i = 0; i < table[0].length; i++) {
			for (int j = 0; j < table.length; j++) {
				value = "" + table[j][i].getValue();
				separator = "";
				for (int k = value.length(); k < 5; k++) {
					separator = separator + " ";
				}
				System.out.print(value + separator);
			}
			System.out.println("");
		}
		System.out.println("--------------------------");
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
			x = localResult.get(localResult.size() - 1).x;
			y = localResult.get(localResult.size() - 1).y;
			while (x != 0 && y != 0) {
				int edgesCounter = 0;
				if (table[x][y].isUpEdge()) {
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
				if (actualPoint.x == (x - 1) && actualPoint.y == (y - 1)) {
					x--;
					y--;
				} else if (actualPoint.x == x && actualPoint.y == (y - 1)) {
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
			if (list.get(list.size() - 1).x != 0 && list.get(list.size() - 1).y != 0) {
				return true;
			}
		}
		return false;
	}

	public void printResults(ArrayList<ArrayList<PointInTable>> lists) {
		ArrayList<String> resultsList = getResultsList(lists);
		System.out.println("Global results: " + resultsList.size() / 2);
		System.out.println("--------------------------");
		int i = 0;
		for (String item : resultsList) {
			System.out.println(item);
			if (i++ % 2 == 1) System.out.println("--------");
		}
		System.out.println("--------------------------");
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
				actual_x = point.x;
				actual_y = point.y;
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

	protected class PointInTable {
		int x;
		int y;
		public PointInTable(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}
