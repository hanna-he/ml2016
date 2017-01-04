package de.uk.spinfo.ml2016.Structures;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private Model createModel(String feature, Set<ToolPart> toolPartList){
		
		Model model = new Model(feature);
		FeatureFactory featureFactory = new FeatureFactory();
		for(ToolPart toolPart: toolPartList){
			BagOfWords bow = new BagOfWords(feature, toolPart.getID());
			for(Tool tool: toolPart.getTools()){
				Map<String, Double> boW = featureFactory.createFeature(feature).processWords(tool.getContext());
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
