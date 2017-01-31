package de.uk.spinfo.ml2016.Components;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Tool;

public abstract class Feature {
	public boolean needsTokenizing = false;
	
//	public List<String> processWords(List<String> text){
//		List<String> processedWords = new ArrayList<>();
//		for(String words : text){
//			processedWords.addAll(processWords(words));
//		}
//		return processedWords;
//	}
	public abstract List<String> processWords(List<String> text);
	
//	protected void addWord(String word, Map<String, Double> bagOfAllWords) {
//		Double count = bagOfAllWords.get(word);
//		if (count == null) {
//			count = 0.;
//		}
//		bagOfAllWords.put(word, ++count);
//		
//	}
	

	
	
	public List<String> filterStopwords(List<String> featuredText){
		List<String> stopwords = new ArrayList<>();
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/stopwords.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				stopwords.add(line);
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		List<String> featuredStopwords = processWords(stopwords);
		featuredText.removeAll(featuredStopwords);
		return featuredText;
	}

	

}
