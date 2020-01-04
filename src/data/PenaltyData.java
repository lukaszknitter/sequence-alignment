package data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class PenaltyData {
	public String firstSequence;
	public String secondSequence;
	public int[][] similarityTable;
	public ArrayList<String> alphabet;
	public int firstSequenceLength;
	public int secondSequenceLength;
	public int ALPHABET_LENGTH;

	public PenaltyData(String filePath) throws IOException {
		readAndInitData(filePath);
	}

	public void readAndInitData(String filePathString) throws IOException {
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

	public int getSimilarityBetweenElements(String a, String b) {
		return similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}
}

