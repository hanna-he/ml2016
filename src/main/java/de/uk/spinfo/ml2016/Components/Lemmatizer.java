package de.uk.spinfo.ml2016.Components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import is2.data.SentenceData09;

public class Lemmatizer {

	private is2.lemmatizer.Lemmatizer lemmatizer;

	public Lemmatizer() {
		this.lemmatizer = new is2.lemmatizer.Lemmatizer("resources/matetools/lemma-ger-3.6.model");
	}

	public List<String> lemmatize(String string) throws Exception {
		SentenceData09 sentence = new SentenceData09();
		List<String> tokens = new Tokenizer().tokenize(string);

		tokens.add(0, "<root>");
		sentence.init(tokens.toArray(new String[0]));
		sentence = lemmatizer.apply(sentence);

		// return new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length)));
		
		// strip sentence delimiters
		return new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length)))
				.stream().filter(e -> !e.equals("--")).collect(Collectors.toList());

	}

}
