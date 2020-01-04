import algorithms.SequenceGlobalAlignment;
import algorithms.SequenceGlobalAlignmentWithPenalty;
import algorithms.SequenceLocalAlignment;

import java.io.IOException;

/**
 * Bioinformatyka
 *
 * @author Martyna Gostomska 160408
 * @author Łukasz Knitter 160246
 * @author Tomasz Kononowicz 160839
**/
public class Main {

    public static void main(String[] args) throws IOException {
        SequenceGlobalAlignment global = new SequenceGlobalAlignment("resources//test_global.txt");
        global.compute();

        SequenceLocalAlignment local = new SequenceLocalAlignment("resources//test_local.txt");
        local.compute();

        SequenceGlobalAlignmentWithPenalty globalWithPenalty = new SequenceGlobalAlignmentWithPenalty("resources//test_penalty.txt");
        globalWithPenalty.compute();
    }
}
