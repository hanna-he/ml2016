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
		// for(ToolPart tp: toolPartList){
		// System.out.println(tp.getID());
		// }
		Model model = createModel(feature, toolPartList);
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
			System.out.println(toolPart.getID());
			BagOfWords bow = new BagOfWords(featureString, toolPart.getID());
			for (Tool tool : toolPart.getTools()) {
				this.totalToolCount++;
				if(contextsearcher.getContext(tool)){
					this.contextFound++;
				};
				Map<String, Double> wordMap = feature.processWords(tool.getContext());
				Double totalWordCount = wordMap.get("totalWordCount");
				wordMap.remove("totalWordCount");
				tool.setWordMap(wordMap);
				tool.setWordCount(totalWordCount);
				cooccurrence.countCooccurrence(tool);
				bow.addToolSub(tool.getToolSub());
				if (tool.getContext().isEmpty()) {
					toolsWoutContext.add(tool);
				} else {
					bow.addTool(tool);
				}
			}
			model.addBoW(bow);
		}

		// Kontext von referenzierenden Tools hinzufügen
		this.contextFoundInOtherTool = cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);
	
		
		System.out.println("Insgesamt gibt es "+this.totalToolCount+" Tools \n");
		System.out.println("Für "+this.contextFound+" Tools wurden Wikikontexte gefunden \n");
		System.out.println("Für "+this.contextFoundInOtherTool+" Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für "+(this.totalToolCount-this.contextFoundInOtherTool)+" Tools wurden gar keine Kontexte gefunden \n");
		return model;
	}

}
