package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.Map;

import org.tartarus.snowball.ext.germanStemmer;

import de.uk.spinfo.ml2016.Structures.Tool;

public class Stems extends Feature{
	
	@Override
	public Tool processWords(Tool tmptool) {
		Double toolWordCount = 0.;
		Map<String, Double> wordMap = new HashMap<>();
		germanStemmer stemmer = new germanStemmer();
		// vorher noch tokenisieren
		for (String word : tmptool.getContext()) {
			stemmer.setCurrent(word);
			if (stemmer.stem()) {
				word = stemmer.getCurrent();
			}
			addWord(word, wordMap);
			toolWordCount++;
		}
		tmptool.setWordMap(wordMap);
		tmptool.setWordCount(toolWordCount);
		return tmptool;
	}
		
	

	
	
}
