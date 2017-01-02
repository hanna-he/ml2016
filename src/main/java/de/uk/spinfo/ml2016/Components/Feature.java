package de.uk.spinfo.ml2016.Components;


import java.util.Map;
import de.uk.spinfo.ml2016.Structures.Tool;

public abstract class Feature {


	public abstract Tool processWords(Tool tool);
	
	protected void addWord(String word, Map<String, Double> bagOfAllWords) {
		Double count = bagOfAllWords.get(word);
		if (count == null) {
			count = 0.;
		}
		bagOfAllWords.put(word, ++count);
		
	}


	public void filterStopwords(){
		
	}

	

}
