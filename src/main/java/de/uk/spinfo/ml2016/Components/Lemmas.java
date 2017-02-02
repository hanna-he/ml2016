package de.uk.spinfo.ml2016.Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.uk.spinfo.ml2016.Structures.Tool;
import is2.data.SentenceData09;

public class Lemmas extends Feature {
	private is2.lemmatizer.Lemmatizer lemmatizer;

	public Lemmas() {
		super();
		this.lemmatizer = new is2.lemmatizer.Lemmatizer("resources/matetools/lemma-ger-3.6.model");
	}

	@Override
	public List<String> processWords(List<String> tokens) {
		List<String> sentenceAsList = new ArrayList<>();
			if (!tokens.isEmpty()) {
				if ((tokens.size() == 1) && (tokens.get(0).isEmpty())) {

				} else {
					SentenceData09 sentence = new SentenceData09();				
					tokens.add(0, "<root>");
					sentence.init(tokens.toArray(new String[0]));
					sentence = lemmatizer.apply(sentence);

					sentenceAsList = Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length))
							.stream().filter(e -> !e.equals("--")).collect(Collectors.toList());

				}
			}

		return sentenceAsList;
	}

	


}
