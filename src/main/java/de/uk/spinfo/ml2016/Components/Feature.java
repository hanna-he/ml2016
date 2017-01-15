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
	protected Tokenizer tokenizer;
	public Feature(){
		try {
			this.tokenizer = new Tokenizer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


//	public abstract Tool processWords(Tool tool);
	public abstract Map<String, Double> processWords(List<String> text);
	
	protected void addWord(String word, Map<String, Double> bagOfAllWords) {
		Double count = bagOfAllWords.get(word);
		if (count == null) {
			count = 0.;
		}
		bagOfAllWords.put(word, ++count);
		
	}
	protected List<String> tokenizeWords(List<String> toTokenize) throws Exception{
		List<String> tokenizedWords = new ArrayList<>();
	
		for(String line: toTokenize){
			line = line.toLowerCase();
			tokenizedWords.addAll(tokenizer.tokenize(line));
			}
		return tokenizedWords;
	}


	public void filterStopwords(Map<String, Double> wordMap){
		Map<String, Double> result = new HashMap<>();
		double minusWordCount=0.;
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
		Set<String> featuredStopwords = processWords(stopwords).keySet();
		for(String word : wordMap.keySet()){
			if(featuredStopwords.contains(word)){
				minusWordCount+=wordMap.get(word);
				
			}else{
				result.put(word, wordMap.get(word));
			}
		}
		double oldWordCount = wordMap.get("totalWordCount");
		double newWordCount = oldWordCount- minusWordCount;
		result.put("totalWordCount", newWordCount);
	}

	

}
