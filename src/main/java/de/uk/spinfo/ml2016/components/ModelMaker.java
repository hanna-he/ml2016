package de.uk.spinfo.ml2016.components;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.uk.spinfo.ml2016.featuring.Feature;
import de.uk.spinfo.ml2016.featuring.FeatureFactory;
import de.uk.spinfo.ml2016.io.TsvParser;
import de.uk.spinfo.ml2016.structures.BagOfWords;
import de.uk.spinfo.ml2016.structures.Context;
import de.uk.spinfo.ml2016.structures.Model;
import de.uk.spinfo.ml2016.structures.Tool;
import de.uk.spinfo.ml2016.wikiContext.ContextSearcher;
import de.uk.spinfo.ml2016.wikiContext.WikiReader;

public class ModelMaker {
	private Map<String, Tool> toolMap = new HashMap<>();
	private ContextSearcher contextsearcher;
	private FeatureFactory featureFactory = new FeatureFactory();

	public Model makeModel(String featureString) {
		int contextFoundinTsv = 0;
		int contextFoundinTsvAndWiki = 0;
		int contextFoundinWiki = 0;
		int contextFoundInOtherTool = 0;

		Model model = new Model(featureString);
		// Tsv einlesen
		if (toolMap.isEmpty()) {
			readTSV();
			System.out.println("-- Info: Tsv eingelesen --");
		}

		// Feature durch FeatureFactory erstellen lassen
		Feature feature = featureFactory.createFeature(featureString);

		// IndexListe einlesen
		Map<String, String> indexMap = WikiReader.readIndexFile();

		// ContextSearcher mit Liste der ToolNamen und IndexListe erstellen
		if (contextsearcher == null) {
			contextsearcher = new ContextSearcher(this.toolMap.keySet(), indexMap);
		}

		// Kontexte aus Wikipedia-Dump auslesen
		List<Context> contextList = contextsearcher.getContext(feature);

		// Kontexte der Tsv-Datei featuren
		featureToolContext(feature);

		// Context-Objekte mit entsprechenden Tool-Objekten zusammenführen
		List<Tool> toolsWoutContext = new ArrayList<>();
		for (Context contextObj : contextList) {
			Tool tool = this.toolMap.get(contextObj.getTitle());

			List<String> contextFromWiki = contextObj.getContext();
			List<String> contextFromTsv = tool.getContext();

			// Statistik erstellen und ausgeben
			if (!contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsvAndWiki++;
			}
			if (!contextFromWiki.isEmpty() && contextFromTsv.isEmpty()) {
				contextFoundinWiki++;

			}
			if (contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsv++;
			}
			if (contextFromWiki.isEmpty() && contextFromTsv.isEmpty()) {
				toolsWoutContext.add(tool);
			}
			tool.addFeaturedContext(contextFromWiki);
			tool.setFeaturedName(contextObj.getFeaturedTitle());
			makeWordMap(tool);
			model.addToolToBagOfWordsWithID(tool);

		}
		Cooccurrence cooccurrence = new Cooccurrence(new HashSet<Tool>(this.toolMap.values()));
		contextFoundInOtherTool = cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);

		
		
		System.out.println("-----Statistik-----");
		System.out.println(featureString + "\n");
		System.out.println("Insgesamt gibt es " + this.toolMap.size() + " Tools \n");
		System.out.println("Für " + contextFoundinTsv + " Tools wurde die Kontext nur in der Tsv-Datei gefunden \n");
		System.out.println("Für " + contextFoundinWiki + " Tools wurde Kontext nur im Wiki-Dump gefunden \n");
		System.out.println("Für " + contextFoundinTsvAndWiki
				+ " Tools wurde sowohl Kontext in der Tsv-Datei als auch im Wiki-Dump gefunden \n");
		System.out.println(
				"Für " + contextFoundInOtherTool + " Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für " + (toolsWoutContext.size() - contextFoundInOtherTool)
				+ " Tools wurde gar keine Kontexte gefunden \n");

		

			
		return model;
	}
	
	
	
	
	

	private void readTSV() {
		File f = new File("resources/tools.tsv");
		TsvParser tsvp = new TsvParser();
		tsvp.parseTsv(f);
		this.toolMap = tsvp.getTools();
	}

	private void featureToolContext(Feature feature) {
		for (String word : this.toolMap.keySet()) {
			Tool tool = this.toolMap.get(word);
			List<String> wordsToBeProcessed = new ArrayList<>();
			tool.clearFeaturedContext();
			if (feature.needsTokenizing) {
				if (tool.getTokenizedTsvcontext().isEmpty()) {
					try {
						tool.setTokenizedTsvcontext(contextsearcher.tokenizeWords(tool.getContext()));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				wordsToBeProcessed = tool.getTokenizedTsvcontext();
			} else {
				wordsToBeProcessed.addAll(tool.getContext());
			}
			List<String> bla = feature.processWords(wordsToBeProcessed);
			tool.addFeaturedContext(bla);
			toolMap.put(word, tool);
		}
	}

	private void makeWordMap(Tool tool) {
		Map<String, Double> bagOfWords = new HashMap<>();
		double wordCount = 0.;
		for (String word : tool.getFeaturedContext()) {
			wordCount++;
			Double count = bagOfWords.get(word);
			if (count == null) {
				count = 0.;
			}
			bagOfWords.put(word, ++count);
		}
		// System.out.println(bagOfWords);
		tool.setWordMap(bagOfWords);
		tool.setWordCount(wordCount);
	}

	

}
