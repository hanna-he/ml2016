package de.uk.spinfo.ml2016.Structures;

import java.util.List;
import java.util.Map;

public class Tool {

	private ToolSub ts;
	private String name;
	private List<String> context;
	private Map<String, Double> wordMap;
	private Map<Tool, Double> coCounts;
	private Double wordCount;
	
	
	public Tool(String name, List<String> context, ToolSub tsc) {
		this.ts = tsc;
		this.name = name;
		this.context = context;
	}
	public ToolSub getToolSub(){
		return this.ts;
	}
	public String getName(){
		return this.name;
	}

	public List<String> getContext() {
		return this.context;
	}

	public void addContext(List<String> newcontext) {
		this.context.addAll(newcontext);
	}

	public void setWordMap (Map<String, Double> wordMap){
		this.wordMap = wordMap;
	}
	public Map<String, Double> getWordMap (){
		return this.wordMap;
	}

	
	public void setCooccurrenceCounts (Map<Tool, Double> coCounts){
		this.coCounts = coCounts;
	}
	public Map<Tool, Double> getCooccurrenceCounts (){
		return this.coCounts;
	}

	
	public Double getWordCount(){
		return this.wordCount;
	}
	public void setWordCount(Double wordCount){
		this.wordCount = wordCount;
	}
	
	@Override
	public int hashCode(){
		return this.name.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (this.name.equals( ((Tool)o).name ) ){
			return true;
		}
	
		else{
			return false;
		}
	}
	@Override
	public String toString(){
		StringBuilder result = new StringBuilder(this.name+": "+this.ts.getID()+"\n"+"Bag of Words: ");
		for(String str : this.wordMap.keySet()){
			result.append(str+" : "+this.wordMap.get(str)+",\n");
		}
		return result.toString();
	}
}