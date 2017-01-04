package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tartarus.snowball.ext.germanStemmer;

import de.uk.spinfo.ml2016.Structures.Tool;

public class Stems extends Feature{
	
	@Override
//	public Tool processWords(Tool tmptool) {
//		Double toolWordCount = 0.;
//		Map<String, Double> wordMap = new HashMap<>();
//		germanStemmer stemmer = new germanStemmer();
//		try {
//			
//			for (String word: tokenizeWords(tmptool.getContext())) {
//				System.out.println(word);
//				stemmer.setCurrent(word);
//				if (stemmer.stem()) {
//					word = stemmer.getCurrent();
//				}
//				addWord(word, wordMap);
//				toolWordCount++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		tmptool.setWordMap(wordMap);
//		tmptool.setWordCount(toolWordCount);
//		return tmptool;
//	}
	
	
	public Map<String, Double> processWords(List<String> text) {
		Double toolWordCount = 0.;
		Map<String, Double> wordMap = new HashMap<>();
		germanStemmer stemmer = new germanStemmer();
		try {
			
			for (String word: tokenizeWords(text)) {
				stemmer.setCurrent(word);
				if (stemmer.stem()) {
					word = stemmer.getCurrent();
				}
				addWord(word, wordMap);
				toolWordCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		wordMap.put("totalWordCount", toolWordCount);
		
		return wordMap;
	}
		
	

	
	
}
