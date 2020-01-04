package algorithms;

import data.Data;
import feature.MainTableElement;
import feature.PointInTable;
import utils.PrintUtils;
import utils.SequenceUtils;

import java.io.IOException;
import java.util.ArrayList;


public class SequenceLocalAlignment {
	private Data data;
	private MainTableElement[][] mainTable;
	private int max;

	public SequenceLocalAlignment(String filePath) throws IOException {
		this.data = new Data(filePath);
	}

	public void compute() {
		final String tableString = "Local";

		fillScoringTable();
		PrintUtils.printTable(tableString, mainTable);
		final ArrayList<ArrayList<PointInTable>> resultsList = calculateResults();
		final ArrayList<String> resultsStringList = stringifyResultsList(resultsList);
		PrintUtils.printResults(tableString, resultsStringList);
	}

	public void fillScoringTable() {
		final int firstSequenceLength = data.firstSequence.length();
		final int secondSequenceLength = data.secondSequence.length();

		mainTable = new MainTableElement[firstSequenceLength + 1][secondSequenceLength + 1];
		mainTable[0][0] = new MainTableElement(0, false, false, false);

		for (int i = 1; i < secondSequenceLength + 1; i++) {
			mainTable[0][i] = new MainTableElement(0, false, false, false);
		}

		for (int i = 1; i < firstSequenceLength + 1; i++) {
			mainTable[i][0] = new MainTableElement(0, false, false, false);
		}

		MainTableElement max;
		for (int i = 1; i < firstSequenceLength + 1; i++) {
			for (int j = 1; j < secondSequenceLength + 1; j++) {
				max = findMax(i, j);
				mainTable[i][j] = max;
				if (max.getValue() >= this.max) this.max = max.getValue();
			}
		}
	}

	private MainTableElement findMax(int i, int j) {
		String actualCharFirstSq = Character.toString(data.firstSequence.charAt(i - 1));
		String actualCharSecondSq = Character.toString(data.secondSequence.charAt(j - 1));

		int costBtwChars = data.getCostBetweenElements(actualCharFirstSq, actualCharSecondSq);
		int leftCost = data.getCostBetweenElements(actualCharFirstSq, " ");
		int upCost = data.getCostBetweenElements(" ", actualCharSecondSq);

		int diagonalCost = costBtwChars + mainTable[i - 1][j - 1].getValue();
		int left = leftCost + mainTable[i - 1][j].getValue();
		int up = upCost + mainTable[i][j - 1].getValue();
		int value = Math.max(Math.max(0, diagonalCost), Math.max(left, up));
		if (value != 0) {
			return new MainTableElement(value, left == value, up == value, diagonalCost == value);
		} else {
			return new MainTableElement(value, false, false, false);
		}
	}

	public ArrayList<ArrayList<PointInTable>> calculateResults() {
		ArrayList<ArrayList<PointInTable>> list = new ArrayList<>();
		ArrayList<PointInTable> localResult;
		for (int i = data.firstSequence.length(); i >= 0; i--) {
			for (int j = data.secondSequence.length(); j >= 0; j--) {
				if (mainTable[i][j].getValue() == this.max) {
					PointInTable actualPoint = new PointInTable(i, j);
					localResult = new ArrayList<>();
					localResult.add(actualPoint);
					list.add(localResult);
				}
			}
		}
		PointInTable actualPoint = list.get(0).get(0);
		int counter = 0;
		while (continueCondition(list)) {
			localResult = list.get(counter);
			int x = localResult.get(localResult.size() - 1).getX();
			int y = localResult.get(localResult.size() - 1).getY();
			while (mainTable[x][y].getValue() != 0) {
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

	public ArrayList<String> stringifyResultsList(ArrayList<ArrayList<PointInTable>> lists) {
		ArrayList<String> resultsList = new ArrayList<>();

		int actual_x = 0, actual_y = 0, resent_x = 0, resent_y = 0;
		for (ArrayList<PointInTable> list : lists) {
			StringBuilder firstOutput = new StringBuilder();
			StringBuilder secondOutput = new StringBuilder();
			resent_x = list.get(0).getX();
			resent_y = list.get(0).getY();
			for (PointInTable point : list) {
				actual_x = point.getX();
				actual_y = point.getY();
				if (actual_x != data.firstSequence.length() || actual_y != data.secondSequence.length()) {

					if (resent_x == actual_x) {
						firstOutput.append(" ");
					} else {
						firstOutput.append(data.firstSequence.charAt(actual_x));
					}
					if (resent_y == actual_y) {
						secondOutput.append(" ");
					} else {
						secondOutput.append(data.secondSequence.charAt(actual_y));
					}
				}
				resent_x = actual_x;
				resent_y = actual_y;
			}
			firstOutput = new StringBuilder(new StringBuilder(firstOutput.toString()).reverse().toString());
			secondOutput = new StringBuilder(new StringBuilder(secondOutput.toString()).reverse().toString());
			if (data.rnaToAminoAcids) {
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
			PointInTable lastPoint = list.get(list.size() - 1);
			if (mainTable[lastPoint.getX()][lastPoint.getY()].getValue() != 0) {
				return true;
			}
		}
		return false;
	}
}

