package data;

import feature.AminoAcidsTypes;
import utils.SequenceUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static utils.SequenceUtils.getDistinctFromStrings;

public class Data {
	public MeasureType measureType;
	public String firstSequence;
	public String secondSequence;
	public int[][] distanceTable;
	public int[][] similarityTable;
	public ArrayList<String> alphabet;
	public boolean rnaToAminoAcids;

	public Data(String filePath) throws IOException {
		readAndInitData(filePath);
	}

	public void readAndInitData(String filePathString) throws IOException {
		Path filePath = Paths.get(filePathString);
		Scanner scanner = new Scanner(filePath);
		measureType = MeasureType.valueOf(scanner.nextLine());

		firstSequence = scanner.nextLine();
		secondSequence = scanner.nextLine();

		rnaToAminoAcids = Boolean.parseBoolean(scanner.next());

		if (rnaToAminoAcids) {
			alphabet = new ArrayList<>();
			Arrays.asList(AminoAcidsTypes.values()).forEach(aminoAcidType -> {
				if (!alphabet.contains(aminoAcidType.getAminoAcid()))
					alphabet.add(aminoAcidType.getAminoAcid());
			});
			firstSequence = SequenceUtils.convertSequence(firstSequence);
			secondSequence = SequenceUtils.convertSequence(secondSequence);
		} else {
			alphabet = getDistinctFromStrings(firstSequence, secondSequence);
		}
		alphabet.add(" ");

		int alphabetLength = alphabet.size();

		distanceTable = new int[alphabetLength][alphabetLength];
		similarityTable = new int[alphabetLength][alphabetLength];
		for (int i = 0; i < alphabetLength; i++) {
			for (int j = 0; j < alphabetLength; j++) {
				if (measureType.equals(MeasureType.DISTANCE)) {
					distanceTable[i][j] = scanner.nextInt();
					similarityTable[i][j] = 1 - distanceTable[i][j];
				} else {
					similarityTable[i][j] = scanner.nextInt();
				}
			}
		}
	}

	public int getDistanceBetweenElements(String a, String b) {
		return distanceTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}

	public int getSimilarityBetweenElements(String a, String b) {
		return similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}
}

