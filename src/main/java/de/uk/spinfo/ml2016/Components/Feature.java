package de.uk.spinfo.ml2016.Components;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.uk.spinfo.ml2016.Structures.Tool;

public abstract class Feature {


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
		Tokenizer tokenizer = new Tokenizer();
		
		for(String line: toTokenize){
			line = line.toLowerCase();
			tokenizedWords.addAll(tokenizer.tokenize(line));
		}
		return tokenizedWords;
	}


	public void filterStopwords(){
		
	}

	

}
