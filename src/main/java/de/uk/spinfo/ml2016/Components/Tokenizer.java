package de.uk.spinfo.ml2016.Components;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {

	private SentenceDetectorME sentencer;
	private TokenizerME tokenizer;

	public Tokenizer() throws Exception {
		this.sentencer = new SentenceDetectorME(new SentenceModel(new File("resources/opennlp/de-sentence.bin")));
		this.tokenizer = new TokenizerME(new TokenizerModel(new File("resources/opennlp/de-token.bin")));
	}

	public List<String> tokenize(String string) {
		List<String> result = new ArrayList<String>();
		// Klammern hinzugefügt
		Pattern punctuation = Pattern.compile("[,.;:!?&\\)(]");

		for (String sentence : sentencer.sentDetect(string)) {
			for (String token : tokenizer.tokenize(sentence)) {
//				token = token.toLowerCase();
				// strip punctuation
				if (!punctuation.matcher(token).find()) {
					if (token.contains("/") && !token.equals("/") && !token.equals("//")) {
						String[] split = token.split("/");
						int len = split.length;
						// gibt zb start/amadeus/merlin
						for (int i = 0; i < len; i++) {
							// zb bei Lehrer/in "in" kein eigenständiges Token
							if (!split[i].startsWith("in") && i != 0) {
								result.add(split[i]);
							}
						}
						token = split[0];
					}
					if (token.contains("-")) {
						String[] split = token.split("-");
						int len = split.length;
						for (int i = 1; i < len; i++) {
							result.add(split[i]);
						}
						token = split[0];
					}

				}
				// um m/w rauszuschmeißen
				if (token.length() > 1) {
					result.add(token);
				}
			}
		}

		return result;

	}
	public static void main(String[] args) throws Exception{
		Tokenizer tok = new Tokenizer();
		System.out.println(tok.tokenize("Start/Amadeus/merlin"));
	}

}
