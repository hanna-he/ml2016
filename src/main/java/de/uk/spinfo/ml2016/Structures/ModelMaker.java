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

	public Model makeModel(String feature, Set<ToolPart> toolPartList) {
		Model model;
		model = createModel(feature, toolPartList);
		 Writer.writeB(model);
		return model;
	}

	private Model createModel(String featureString, Set<ToolPart> toolPartList) {
		Cooccurrence cooccurrence = new Cooccurrence(toolPartList);
		Model model = new Model(featureString);
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature(featureString);
		ContextSearcher contextsearcher = new ContextSearcher(feature);
		Set<Tool> toolsWoutContext = new HashSet<>();
		for (ToolPart toolPart : toolPartList) {
			BagOfWords bow = new BagOfWords(featureString, toolPart.getID());
			for (Tool tool : toolPart.getTools()) {
				contextsearcher.getContext(tool);
				Map<String, Double> boW = feature.processWords(tool.getContext());
				Double totalWordCount = boW.get("totalWordCount");
				boW.remove("totalWordCount");
				tool.setWordMap(boW);
				tool.setWordCount(totalWordCount);
				cooccurrence.countCooccurrence(tool);
				if (tool.getContext().isEmpty()) {
					toolsWoutContext.add(tool);
				} else {
					bow.addTool(tool);
				}
			}
			model.addBoW(bow);
		}
		
		//Kontext von referenzierenden Tools hinzufügen
		try {
			BufferedWriter bWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("resources/ToolsWithoutContext.txt", false), "UTF-8"));
		for (Tool toolagain : toolsWoutContext) {
			bWriter.write("\n Für Tool : "+toolagain.getName()+" wurden die Kontexte folgender Tools hinzugefügt:\n");
			cooccurrence.getReferencingTools(toolagain);
			for (Tool refTool : toolagain.getReferencingTools()) {
				bWriter.write(refTool.getName()+", ");
				toolagain.addContext(refTool.getContext());
			}
			toolagain.getToolSub().getToolPart().getID();

			model.addToolToBagOfWordsWithID(toolagain);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return model;
	}

}
