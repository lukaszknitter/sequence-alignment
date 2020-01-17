import algorithms.SequenceGlobalAlignmentWithPenalty;

import java.io.IOException;

/**
 * Bioinformatyka
 * Optymalne zestawienia 2 sekwencji II
 *
 * @author Martyna Gostomska 160408
 * @author Łukasz Knitter 160246
 * @author Tomasz Kononowicz 160839
 **/
public class Main {

	public static void main(String[] args) throws IOException {
		SequenceGlobalAlignmentWithPenalty globalWithPenalty = new SequenceGlobalAlignmentWithPenalty("resources//test_penalty2.txt");
		globalWithPenalty.compute();
	}
}
