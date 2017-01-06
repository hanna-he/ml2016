package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tartarus.snowball.ext.germanStemmer;

import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.Structures.ToolPart;

public class Cooccurrence {
	private Set<Tool> toolSet;
	Set<ToolPart> toolPartList;
	
	public Cooccurrence(Set<ToolPart> toolPartList){
		this.toolPartList = toolPartList;
		for(ToolPart toolpart : toolPartList){
			for(Tool tool: toolpart.getTools()){
				this.toolSet.add(tool);
			}
		}
	}


	public Tool countCooccurrence(Tool tool) {
		Map<Tool, Double> coCount = new HashMap<>();
		Map<String, Double> wordMap = tool.getWordMap();
		for (Tool otherTool : this.toolSet) {
			String otherName = otherTool.getName();
			if (wordMap.containsKey(otherName)) {
				coCount.put(otherTool, wordMap.get(otherName));
			}
		}
		tool.setCooccurrenceCounts(coCount);
		return tool;
	}
//	private Set<Tool> getAllToolsAsList(Set<ToolPart> toolPartList){
//		Set<Tool> toolSet = new HashSet<>();
//		for(ToolPart toolpart : toolPartList){
//			for(Tool tool: toolpart.getTools()){
//				toolSet.add(tool);
//			}
//		}
//		return toolSet;
//	}
	
	//gibt die anderen Tools, die in ihrem Kontext dieses Tool nennen zurück
	public Tool getReferencingTools(Tool tool){
		Set<Tool> referencingTools = new HashSet<>();
		for(Tool otherTool: this.toolSet){
			if(otherTool != tool){
				for(String word : tool.getFeaturedName()){
					if(tool.getWordMap().containsKey(word)){
						referencingTools.add(tool);
					}
				}
			}
		}
		tool.setReferencingTools(referencingTools);
		return tool;
	}
	
}
