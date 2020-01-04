package algorithms;

import data.Data;
import feature.MainTableElement;
import feature.PointInTable;
import utils.PrintUtils;
import utils.SequenceUtils;

import java.io.IOException;
import java.util.ArrayList;

public class SequenceGlobalAlignment {
	private Data data;
	private MainTableElement[][] mainTable;

	public SequenceGlobalAlignment(String filePath) throws IOException {
		this.data = new Data(filePath);
	}

	public void compute() {
		final String tableString = "Global";

		fillScoringTable();
		PrintUtils.printTable(tableString, mainTable);
		final ArrayList<ArrayList<PointInTable>> resultsList = calculateResults();
		final ArrayList<String> resultsStringList = stringifyResultsList(resultsList);
		PrintUtils.printResults(tableString, resultsStringList);
	}

	private void fillScoringTable() {
		final int firstSequenceLength = data.firstSequence.length();
		final int secondSequenceLength = data.secondSequence.length();
		final String firstSequence = data.firstSequence;
		final String secondSequence = data.secondSequence;
		final ArrayList<String> alphabet = data.alphabet;
		final int alphabetLength = alphabet.size();
		final int[][] costTable = data.costTable;

		mainTable = new MainTableElement[firstSequenceLength + 1][secondSequenceLength + 1];
		mainTable[0][0] = new MainTableElement(0, false, false, false);

		for (int i = 1; i < secondSequenceLength + 1; i++) {
			String actualChar = Character.toString(secondSequence.charAt(i - 1));
			int value = mainTable[0][i - 1].getValue() + costTable[alphabet.indexOf(actualChar)][alphabetLength - 1];
			mainTable[0][i] = new MainTableElement(value, false, true, false);
		}
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			String actualChar = Character.toString(firstSequence.charAt(i - 1));
			int value = mainTable[i - 1][0].getValue() + costTable[alphabetLength - 1][alphabet.indexOf(actualChar)];
			mainTable[i][0] = new MainTableElement(value, true, false, false);
		}
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			for (int j = 1; j < secondSequenceLength + 1; j++) {
				mainTable[i][j] = findMin(i, j);
			}
		}
	}

	private MainTableElement findMin(int i, int j) {
		String actualCharFirstSq = Character.toString(data.firstSequence.charAt(i - 1));
		String actualCharSecondSq = Character.toString(data.secondSequence.charAt(j - 1));

		int costBtwChars = data.getCostBetweenElements(actualCharFirstSq, actualCharSecondSq);
		int leftCost = data.getCostBetweenElements(actualCharFirstSq, " ");
		int upCost = data.getCostBetweenElements(" ", actualCharSecondSq);

		int diagonalCost = costBtwChars + mainTable[i - 1][j - 1].getValue();
		int left = leftCost + mainTable[i - 1][j].getValue();
		int up = upCost + mainTable[i][j - 1].getValue();
		int value = Math.min(diagonalCost, Math.min(left, up));

		return new MainTableElement(value, left == value, up == value, diagonalCost == value);
	}

	private ArrayList<ArrayList<PointInTable>> calculateResults() {
		ArrayList<ArrayList<PointInTable>> list = new ArrayList<>();
		ArrayList<PointInTable> localResult = new ArrayList<>();
		int x = data.firstSequence.length();
		int y = data.secondSequence.length();
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
				if (mainTable[x][y].isTopEdge()) {
					actualPoint = new PointInTable(x, y - 1);
					edgesCounter++;
				}
				if (mainTable[x][y].isLeftEdge()) {
					if (edgesCounter != 0) {
						ArrayList<PointInTable> clone = new ArrayList<>(localResult);
						clone.add(new PointInTable(x - 1, y));
						list.add(clone);
					} else {
						actualPoint = new PointInTable(x - 1, y);
					}
					edgesCounter++;
				}
				if (mainTable[x][y].isDiagonalEdge()) {
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

	private ArrayList<String> stringifyResultsList(ArrayList<ArrayList<PointInTable>> lists) {
		final int firstSequenceLength = data.firstSequence.length();
		final int secondSequenceLength = data.secondSequence.length();
		final String firstSequence = data.firstSequence;
		final String secondSequence = data.secondSequence;
		final boolean rnaToAminoAcids = data.rnaToAminoAcids;

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
				secondOutput.append(secondOutput.charAt(actual_y));
			}
			firstOutput = new StringBuilder(new StringBuilder(firstOutput.toString()).reverse().toString());
			secondOutput = new StringBuilder(new StringBuilder(secondOutput.toString()).reverse().toString());
			if (rnaToAminoAcids) {
				firstOutput = new StringBuilder(SequenceUtils.reTranslateSequence(firstOutput.toString()));
				secondOutput = new StringBuilder(SequenceUtils.reTranslateSequence(secondOutput.toString()));
			}
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

