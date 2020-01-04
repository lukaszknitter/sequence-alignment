package utils;

import feature.AminoAcidsTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SequenceUtils {
	public static String convertSequence(String sequence) {
		StringBuilder convertedSequence = new StringBuilder();
		for (int i = 0; i < sequence.length(); i += 3) {
			convertedSequence.append(AminoAcidsTypes.valueOf(sequence.substring(i, i + 3)).getAminoAcid());
		}
		return convertedSequence.toString();
	}

	public static String reTranslateSequence(String sequence) {
		StringBuilder convertedSequence = new StringBuilder();
		for (int i = 0; i < sequence.length(); i++) {
			convertedSequence.append(getAminoAcidCodon(sequence.substring(i, i + 1)));
		}
		return convertedSequence.toString();
	}

	public static String getAminoAcidCodon(String aminoAcid) {
		for (AminoAcidsTypes aminoAcidType : AminoAcidsTypes.values()) {
			if (aminoAcid.equals(aminoAcidType.getAminoAcid())) {
				return aminoAcidType.toString();
			}
		}
		return "   ";
	}

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
		return result;
	}
}