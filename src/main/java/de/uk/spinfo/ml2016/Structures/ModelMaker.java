package de.uk.spinfo.ml2016.Structures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Components.ContextSearcher;
import de.uk.spinfo.ml2016.Components.Cooccurrence;
import de.uk.spinfo.ml2016.Components.Feature;
import de.uk.spinfo.ml2016.Components.FeatureFactory;
import de.uk.spinfo.ml2016.io.TsvParser;
import de.uk.spinfo.ml2016.io.Writer;

public class ModelMaker {
	private int contextFound =0;
	private int contextFoundInOtherTool=0;
	private int totalToolCount=0;
	
	public Model makeModel(String feature, Set<ToolPart> toolPartList) {
		Model model = createModel(feature, toolPartList);
		Writer.writeB(model);
		return model;
	}

	
	private void preprocessing(Set<ToolPart> toolPartSet, ContextSearcher contextsearcher){
		Set<Tool> allTools = new HashSet<>();
		for(ToolPart tp : toolPartSet){
			allTools.addAll(tp.getTools());
		}
		contextsearcher.getPathToTitleMap(allTools);
		System.out.println("preprocessing");
	}
	private Model createModel(String featureString, Set<ToolPart> toolPartSet) {
		Cooccurrence cooccurrence = new Cooccurrence(toolPartSet);
		Model model = new Model(featureString);
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature(featureString);
		ContextSearcher contextsearcher = new ContextSearcher(feature);
		preprocessing(toolPartSet, contextsearcher);
		Set<Tool> toolsWoutContext = new HashSet<>();
		for (ToolPart toolPart : toolPartSet) {
			System.out.println(toolPart.getID());
			BagOfWords bow = new BagOfWords(featureString, toolPart.getID());
			int totalWordCountBoW = 0;
			for (Tool tool : toolPart.getTools()) {
				System.out.println(tool.getName());
				this.totalToolCount++;
				if(contextsearcher.getContext(tool)){
					this.contextFound++;
				}
				Map<String, Double> wordMap = feature.processWords(tool.getContext());
				Double totalWordCountTool = wordMap.get("totalWordCount");
				totalWordCountBoW+= totalWordCountTool;
				wordMap.remove("totalWordCount");
				tool.setWordMap(wordMap);
				tool.setWordCount(totalWordCountTool);
				cooccurrence.countCooccurrence(tool);
				bow.addToolSub(tool.getToolSub());
				if (tool.getContext().isEmpty()) {
					toolsWoutContext.add(tool);
				} else {
					bow.addTool(tool);
				}
			}
			bow.setTotalWordCount(totalWordCountBoW);
			model.addBoW(bow);
			
		}

		// Kontext von referenzierenden Tools hinzufügen
		this.contextFoundInOtherTool = cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);
		int counter = 0;
		for(BagOfWords b: model.getBagOfWordList()){
			for(Tool t : b.getTools()){
				if(t.getContext().isEmpty()){
					counter++;
					System.out.println("Überhaupt keinen Kontext : "+t.getName()+" "+t.getFeaturedName());
				}
			}
		}
		System.out.println(counter);
		
		System.out.println("Insgesamt gibt es "+this.totalToolCount+" Tools \n");
		System.out.println("Für "+this.contextFound+" Tools wurden Wikikontexte gefunden \n");
		System.out.println("Für "+this.contextFoundInOtherTool+" Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für "+(toolsWoutContext.size()-this.contextFoundInOtherTool)+" Tools wurden gar keine Kontexte gefunden \n");
		return model;
	}

}
