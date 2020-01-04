class SequenceUtils {
	public static String convertSequence(String sequence) {
		StringBuilder convertedSequence = new StringBuilder();
		for (int i = 0; i < sequence.length(); i += 3) {
			convertedSequence.append(AminoAcidsTypes.valueOf(sequence.substring(i, i + 3)).getAminoAcid());
		}
		return convertedSequence.toString();
	}

	public static String reTranslateSequence(String sequence) {
		String convertedSequence = "";
		for (int i = 0; i < sequence.length(); i++) {
			convertedSequence = convertedSequence + getAminoAcidKodon(sequence.substring(i, i + 1));
		}
		return convertedSequence;
	}

	public static String getAminoAcidKodon(String aminoAcid) {
		for (AminoAcidsTypes aminoAcidType : AminoAcidsTypes.values()) {
			if (aminoAcid.equals(aminoAcidType.getAminoAcid())) {
				return aminoAcidType.toString();
			}
		}
		return "   ";
	}
}