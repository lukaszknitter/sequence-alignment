package feature;

public enum AminoAcidsTypes {
	UUU("f"),//("fenyloalanina"),
	UUC("f"),//("fenyloalanina"),
	UCC("s"),//("seryna"),
	UCU("s"),//("seryna"),
	UCA("s"),//("seryna"),
	UCG("s"),//("seryna"),
	AGU("s"),//("seryna"),
	AGC("s"),//("seryna"),
	UAU("t"),//("tyrozyna"),
	UAC("t"),//("tyrozyna"),
	UGU("c"),//("cysteina"),
	UGC("c"),//("cysteina"),
	UUA("l"),//("leucyna"),
	UUG("l"),//("leucyna"),
	CUU("l"),//("leucyna"),
	CUC("l"),//("leucyna"),
	CUA("l"),//("leucyna"),
	CUG("l"),//("leucyna"),
	//UAA  ("Stop"),
	//UGA ("Stop"),
	//UAG  ("Stop"),
	UGG("r"),//("tryptofan"),
	CCC("p"),//("prolina"),
	CCU("p"),//("prolina"),
	CCA("p"),//("prolina"),
	CCG("p"),//("prolina"),
	CAU("h"),//("histydyna"),
	CAC("h"),//("histydyna"),
	CGU("a"),//("arginina"),
	CGC("a"),//("arginina"),
	CGA("a"),//("arginina"),
	CGG("a"),//("arginina"),
	AGA("a"),//("arginina"),
	AGG("a"),//("arginina"),
	CAA("g"),//("glutamina"),
	CAG("g"),//("glutamina"),
	AUC("i"),//("izoleucyna"),
	AUA("i"),//("izoleucyna"),
	AUU("i"),//("izoleucyna"),
	ACU("e"),//("treonina"),
	ACC("e"),//("treonina"),
	ACA("e"),//("treonina"),
	ACG("e"),//("treonina"),
	AAU("n"),//("asparagina"),
	AAC("n"),//("asparagina"),
	GAC("k"),//("asparaginian"),
	GAU("k"),//("asparaginian"),
	AAG("z"),//("lizyna"),
	AAA("z"),//("lizyna"),
	AUG("m"),//("metionina"),
	GUG("w"),// ("walina"),
	GUA("w"),//("walina"),
	GUC("w"),//("walina"),
	GUU("w"),//("walina"),
	GCU("b"),//("alanina"),
	GCC("b"),//("alanina"),
	GCA("b"),//("alanina"),
	GCG("b"),//("alanina"),
	GGU("o"),//("glicyna"),
	GGA("o"),//("glicyna"),
	GGG("o"),//("glicyna"),
	GGC("o"),//("glicyna"),
	GAA("u"),//("glutaminian"),
	GAG("u");//("glutaminian");

	private String aminoAcidName;

	AminoAcidsTypes(String aminoAcidName) {
		this.aminoAcidName = aminoAcidName;
	}

	public String getAminoAcid() {
		return aminoAcidName;
	}
}
