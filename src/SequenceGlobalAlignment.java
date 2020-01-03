import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class SequenceGlobalAlignment {
	public String filePathString;
	public String firstSequence;
	public String secondSequence;
	protected int[][] costTable;
	protected int[][] similarityTable;
	protected ArrayList<String> alphabet;
	protected MainTableElement[][] mainTable;
	protected int firstSequenceLength;
	protected int secondSequenceLength;
	protected ArrayList<ArrayList<PointInTable>> resultList;
	protected int ALPHABET_LENGTH;
	protected boolean rnaToAminoAcids;

	public SequenceGlobalAlignment(String filePath) {
		this.filePathString = filePath;
		resultList = new ArrayList<>();
	}

	public void doStuff() throws IOException {
		readAndInitData();
		countTable();
		printTable(mainTable);
		countResults(mainTable, resultList);
		printResults(resultList);
	}

	public void readAndInitData() throws IOException {
		Path filePath = Paths.get(filePathString);
		Scanner scanner = new Scanner(filePath);
		firstSequence = scanner.nextLine();
		secondSequence = scanner.nextLine();

		ALPHABET_LENGTH = scanner.nextInt();
		rnaToAminoAcids = new Boolean(scanner.next());

		costTable = new int[ALPHABET_LENGTH][ALPHABET_LENGTH];
		similarityTable = new int[ALPHABET_LENGTH][ALPHABET_LENGTH];
		for (int i = 0; i < ALPHABET_LENGTH; i++) {
			for (int j = 0; j < ALPHABET_LENGTH; j++) {
				costTable[i][j] = scanner.nextInt();
				similarityTable[i][j] = 1 - costTable[i][j];
			}
		}
		alphabet = new ArrayList<>();
		if (rnaToAminoAcids) {
			Arrays.asList(AminoAcidsTypes.values()).forEach(aminoAcidType -> {
				if (!alphabet.contains(aminoAcidType.getAminoAcid()))
					alphabet.add(aminoAcidType.getAminoAcid());
			});
			firstSequence = convertSequence(firstSequence);
			secondSequence = convertSequence(secondSequence);
		} else {
			for (int i = 0; i < ALPHABET_LENGTH - 1; i++) alphabet.add(scanner.next());
		}
		alphabet.add(" ");

		firstSequenceLength = firstSequence.length();
		secondSequenceLength = secondSequence.length();
	}

	private String convertSequence(String sequence) {
		String convertedSequence = "";
		for (int i = 0; i < sequence.length(); i += 3) {
			convertedSequence = convertedSequence + AminoAcidsTypes.valueOf(sequence.substring(i, i + 3)).getAminoAcid();
		}
		return convertedSequence;
	}

	public void countTable() {
		mainTable = new MainTableElement[firstSequenceLength + 1][secondSequenceLength + 1];
		mainTable[0][0] = new MainTableElement(0, false, false, false);

		for (int i = 1; i < secondSequenceLength + 1; i++) {
			String actualChar = Character.toString(secondSequence.charAt(i - 1));
			int value = mainTable[0][i - 1].getValue() + costTable[alphabet.indexOf(actualChar)][ALPHABET_LENGTH - 1];
			mainTable[0][i] = new MainTableElement(value, false, true, false);
		}
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			String actualChar = Character.toString(firstSequence.charAt(i - 1));
			int value = mainTable[i - 1][0].getValue() + costTable[ALPHABET_LENGTH - 1][alphabet.indexOf(actualChar)];
			mainTable[i][0] = new MainTableElement(value, true, false, false);
		}
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			for (int j = 1; j < secondSequenceLength + 1; j++) {
				mainTable[i][j] = findMin(i, j);
			}
		}
	}

	protected int CostBetweenElements(String a, String b) {
		int cost = costTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
		return cost;
	}


	private MainTableElement findMin(int i, int j) {
		String actualCharFirstSq = Character.toString(firstSequence.charAt(i - 1));
		String actualCharSecondSq = Character.toString(secondSequence.charAt(j - 1));

		int costBtwChars = CostBetweenElements(actualCharFirstSq, actualCharSecondSq);
		int leftCost = CostBetweenElements(actualCharFirstSq, " ");
		int upCost = CostBetweenElements(" ", actualCharSecondSq);

		int diagonalCost = costBtwChars + mainTable[i - 1][j - 1].getValue();
		int left = leftCost + mainTable[i - 1][j].getValue();
		int up = upCost + mainTable[i][j - 1].getValue();
		int value = Math.min(diagonalCost, Math.min(left, up));

		return new MainTableElement(value, left == value, up == value, diagonalCost == value);
	}

	public void printTable(MainTableElement[][] table) {
		System.out.println("Main global table:");
		System.out.println("--------------------------");
		for (int i = 0; i < table[0].length; i++) {
			for (int j = 0; j < table.length; j++) {
				System.out.print(table[j][i].getValue() + " ");
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
				secondOutput = secondOutput + (secondOutput.charAt(actual_y));
			}
			firstOutput = new StringBuilder(firstOutput).reverse().toString();
			secondOutput = new StringBuilder(secondOutput).reverse().toString();
			if (rnaToAminoAcids) {
				firstOutput = reTranslateSequence(firstOutput);
				secondOutput = reTranslateSequence(secondOutput);
			}
			resultsList.add(firstOutput);
			resultsList.add(secondOutput);
		}
		return resultsList;
	}

	protected String reTranslateSequence(String sequence) {
		String convertedSequence = "";
		for (int i = 0; i < sequence.length(); i++) {
			convertedSequence = convertedSequence + getAminoAcidKodon(sequence.substring(i, i + 1));
		}
		return convertedSequence;
	}

	private String getAminoAcidKodon(String aminoAcid) {
		for (AminoAcidsTypes aminoAcidType : AminoAcidsTypes.values()) {
			if (aminoAcid.equals(aminoAcidType.getAminoAcid())) {
				return aminoAcidType.toString();
			}
		}
		return "   ";
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

	protected class PointInTable {
		int x;
		int y;
		public PointInTable(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
}

