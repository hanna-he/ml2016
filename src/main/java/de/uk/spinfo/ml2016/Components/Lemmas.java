package de.uk.spinfo.ml2016.Components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
		try {
			List<String> tokens = tokenizeWords(text);
			if (!tokens.isEmpty()) {
				if ((tokens.size() == 1) && (tokens.get(0).isEmpty())) {

				} else {
					SentenceData09 sentence = new SentenceData09();
					List<String> sentenceAsList = new ArrayList<>();
					tokens.add(0, "<root>");
					sentence.init(tokens.toArray(new String[0]));
					sentence = lemmatizer.apply(sentence);

					sentenceAsList = Arrays.asList(Arrays.copyOfRange(sentence.plemmas, 1, sentence.plemmas.length))
							.stream().filter(e -> !e.equals("--")).collect(Collectors.toList());

					for (String word : sentenceAsList) {
						addWord(word, wordMap);
						// System.out.println(word);
						toolWordCount++;
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();

		}
		wordMap.put("totalWordCount", toolWordCount);

		return wordMap;
	}

	public static void main(String[] args) throws Exception {
		Lemmas lemma = new Lemmas();
		String a = "m/w";
		System.out.println(a.split("/")[0].length());
		System.out.println(a.split("/")[1].length());

		List<String> bla = new ArrayList<>();
		// wieso fliegt bei dem Lemmatizer "w" raus?
		bla.add("m/w");
		List<String> tokens = lemma.tokenizeWords(bla);
		for (String str : tokens) {
			System.out.println("String : " + str);
		}

		System.out.println("bla : " + lemma.processWords(bla));

		List<String> text = new ArrayList<>();
		// String g = "a";
		// text.add(g);
		text.add("");
		if (!text.isEmpty()) {
			if ((text.size() == 1) && (text.get(0).isEmpty())) {

			} else {
				System.out.println("sowieso");
			}
		}
		// System.out.println("sowieso");
		text.add("hier-da");
		text.add("m-w");
		text.add("m/w");
		text.add("steht/w");
		text.add("(hund)");
		text.add("tiere(Katzen)");
		text.add("Lehrer-Schüler/innen");
		text.add("diagnose-software/bus-system");
		List<String> tokens2 = lemma.tokenizeWords(text);
		for (String str : tokens2) {
			System.out.println(str);
		}
		System.out.println("text : " + lemma.processWords(text));
	}

}
