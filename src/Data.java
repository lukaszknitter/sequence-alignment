import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Data {
	public String firstSequence;
	public String secondSequence;
	public int[][] costTable;
	public int[][] similarityTable;
	public ArrayList<String> alphabet;
	public int firstSequenceLength;
	public int secondSequenceLength;
	public int ALPHABET_LENGTH;
	public boolean rnaToAminoAcids;

	public Data(String filePath) throws IOException {
		readAndInitData(filePath);
	}

	public void readAndInitData(String filePathString) throws IOException {
		Path filePath = Paths.get(filePathString);
		Scanner scanner = new Scanner(filePath);
		firstSequence = scanner.nextLine();
		secondSequence = scanner.nextLine();

		ALPHABET_LENGTH = scanner.nextInt();
		rnaToAminoAcids = Boolean.parseBoolean(scanner.next());

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
			firstSequence = SequenceUtils.convertSequence(firstSequence);
			secondSequence = SequenceUtils.convertSequence(secondSequence);
		} else {
			for (int i = 0; i < ALPHABET_LENGTH - 1; i++) alphabet.add(scanner.next());
		}
		alphabet.add(" ");

		firstSequenceLength = firstSequence.length();
		secondSequenceLength = secondSequence.length();
	}

	public int getCostBetweenElements(String a, String b) {
		return costTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}

	public int getSimilarityBetweenElements(String a, String b) {
		return similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
	}
}

