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
		Pattern punctuation = Pattern.compile("[,.;:!?&\\-]");

		for (String sentence : sentencer.sentDetect(string))
			for (String token : tokenizer.tokenize(sentence))
				// strip punctuation
				if (!punctuation.matcher(token).find())
					result.add(token);

		return result;
	}

}
