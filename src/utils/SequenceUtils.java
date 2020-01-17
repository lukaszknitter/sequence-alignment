package utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class SequenceUtils {

	public static ArrayList<String> getDistinctFromStrings(String first, String second) {
		ArrayList<String> result = new ArrayList<>();
		Set<Character> charsSet = new HashSet<>();
		char[] firstSequenceChars = first.toCharArray();
		char[] secondSequenceChars = second.toCharArray();
		for (char c : firstSequenceChars) {
			charsSet.add(c);
		}
		for (char c : secondSequenceChars) {
			charsSet.add(c);
		}
		for (char c : charsSet) {
			result.add(String.valueOf(c));
		}
		result.sort(Comparator.naturalOrder());
		return result;
	}
}
