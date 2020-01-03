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
        SequenceGlobalAlignment test = new SequenceGlobalAlignment("resources//test_global.txt");
        test.doStuff();

        SequenceLocalAlignment test2 = new SequenceLocalAlignment("resources//test_local.txt");
        test2.doStuff();

        SequenceGlobalAlignmentWithPenalty test3 = new SequenceGlobalAlignmentWithPenalty("resources//test_penalty.txt");
        test3.doStuff();
    }
}
