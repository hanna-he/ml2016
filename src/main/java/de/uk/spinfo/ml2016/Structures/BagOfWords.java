package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BagOfWords {
	private String feature;
	private Integer id;
	private Map<String, Double> wordMap;
	private List<Tool> toolsList;

	
	public BagOfWords(String feature, int id){
		this.feature=feature;
		this.id = id;
		this.wordMap = new HashMap<>();
		this.toolsList = new ArrayList<>();
	}
	
	
	public int getID(){
		return this.id;
	}
	public void addTool(Tool tool){
		this.toolsList.add(tool);		
	}
	public List<Tool> getTools(){
		return this.toolsList;
	}
	public Set<String> getWords(){
		if(this.wordMap.isEmpty()){
			processWordMap();
		}
		return wordMap.keySet();
	}
	
	public Map<String, Double> getMap(){
		if(this.wordMap.isEmpty()){
			processWordMap();
		}
		return this.wordMap;
	}
	
	private void processWordMap(){
		for(Tool tool: toolsList){
			for(String s : tool.getWordMap().keySet()){
				Double counter = this.wordMap.get(s);
				if(counter == null){
					counter = 0.;
				}
				this.wordMap.put(s, counter+tool.getWordMap().get(s));
			}
		}
	}
	public String toString(){
		StringBuilder s = new StringBuilder();
		for(String word: this.wordMap.keySet()){
			s.append(word +" : "+wordMap.get(word)+"\n");
		}
		return s.toString();
	}
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (!this.feature.equals( ((BagOfWords)o).feature ) ){
			return false;
		}
		if(this.id != ((BagOfWords)o).id){
			return false;
		}
	
		else{
			return true;
		}
	}
	@Override
	public int hashCode(){
		return this.feature.hashCode()+this.getID();
	}

	
	
	
	
}
