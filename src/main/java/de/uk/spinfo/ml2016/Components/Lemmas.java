package de.uk.spinfo.ml2016.Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public Map<String, Double> processWords(List<String> text) {
		Double toolWordCount = 0.;
		Map<String, Double> wordMap = new HashMap<>();

		SentenceData09 sentence = new SentenceData09();
		List<String> sentenceAsList = new ArrayList<>();
		try{
		List<String> tokens = tokenizeWords(text);
//		System.out.println("Tokens: "+tokens.toString());

		tokens.add(0, "<root>");
		sentence.init(tokens.toArray(new String[0]));
		sentence = lemmatizer.apply(sentence);
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		sentenceAsList = 
				Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length)).stream()
						.filter(e -> !e.equals("--")).collect(Collectors.toList());
		for (String word : sentenceAsList) {
			addWord(word, wordMap);
//			System.out.println(word);
			toolWordCount++;
		}

		wordMap.put("totalWordCount", toolWordCount);
		return wordMap;
	}

}
