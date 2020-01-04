package data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static utils.SequenceUtils.getDistinctFromStrings;

public class PenaltyData {
	public MeasureType measureType;
	public String firstSequence;
	public String secondSequence;
	public int[][] similarityTable;
	public ArrayList<String> alphabet;

	public PenaltyData(String filePath) throws IOException {
		readAndInitData(filePath);
	}

	public void readAndInitData(String filePathString) throws IOException {
		Path filePath = Paths.get(filePathString);
		Scanner scanner = new Scanner(filePath);
		measureType = MeasureType.valueOf(scanner.nextLine());

		firstSequence = scanner.nextLine();
		secondSequence = scanner.nextLine();

		alphabet = getDistinctFromStrings(firstSequence, secondSequence);

		int alphabetLength = alphabet.size();

		similarityTable = new int[alphabetLength][alphabetLength];
		for (int i = 0; i < alphabetLength; i++) {
			for (int j = 0; j < alphabetLength; j++) {
				similarityTable[i][j] = scanner.nextInt();
			}
		}

	}

	public int getSimilarityBetweenElements(String a, String b) {
		return similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}
}

