package de.uk.spinfo.ml2016.Structures;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Components.ContextSearcher;
import de.uk.spinfo.ml2016.Components.Feature;
import de.uk.spinfo.ml2016.Components.FeatureFactory;
import de.uk.spinfo.ml2016.io.TsvParser;
import de.uk.spinfo.ml2016.io.Writer;

public class ModelMaker {
	
	public Model makeModel(String feature, Set<ToolPart> toolPartList) {
		Model model;
		model = createModel(feature, toolPartList);
		Writer.writeB(model);
		return model;
	}

	private Model createModel(String featureString, Set<ToolPart> toolPartList){
		
		Model model = new Model(featureString);
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature(featureString);
		ContextSearcher contextsearcher = new ContextSearcher(feature);
		for(ToolPart toolPart: toolPartList){
			BagOfWords bow = new BagOfWords(featureString, toolPart.getID());
			for(Tool tool: toolPart.getTools()){
				tool.addContext(contextsearcher.getContext(tool.getName()));
				Map<String, Double> boW = feature.processWords(tool.getContext());
				Double totalWordCount = boW.get("totalWordCount");
				boW.remove("totalWordCount");
				tool.setWordMap(boW);
				tool.setWordCount(totalWordCount);
				bow.addTool(tool);
			}
			model.addBoW(bow);
			
		}
		
		return model;
	}
	
}
