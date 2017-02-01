package de.uk.spinfo.ml2016.Structures;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Components.ContextSearcher;
import de.uk.spinfo.ml2016.Components.Cooccurrence;
import de.uk.spinfo.ml2016.Components.Feature;
import de.uk.spinfo.ml2016.Components.FeatureFactory;
import de.uk.spinfo.ml2016.Components.WikiContext;
import de.uk.spinfo.ml2016.io.TsvParser;
import de.uk.spinfo.ml2016.io.Writer;

public class ModelMaker {
	private Set<ToolPart> toolPartSet = new HashSet<>();
	private Map<String, Tool> toolMap = new HashMap<>();
	private WikiContext wikiContext;

//	public Model makeModel(String feature, Set<ToolPart> toolPartList) {
//		// hier preprocessing einbauen
//		// getperfectMatches(Tools)-> Map<Tool, Set<String>>
//
//		Model model = createModel(feature, toolPartList);
//		Writer.writeB(model);
//		return model;
//	}

	public void readTSV() {
		File f = new File("resources/tools.tsv");
		TsvParser tsvp = new TsvParser();
		tsvp.parseTsv(f);
		this.toolMap = tsvp.getTools();
	}

	public Model makeModelNeu(String featureString) {
		int contextFoundinTsv = 0;
		int contextFoundinTsvAndWiki = 0;
		int contextFoundinWiki = 0;
		int contextFoundInOtherTool = 0;

		Model model = new Model(featureString);
		//Tsv einlesen
		if (toolMap.isEmpty()) {
			readTSV();
			System.out.println("-- Info: Tsv eingelsen --");
		}
		
		//Kontexte aus Wikipedia-Dump auslesen
		if (wikiContext == null) {
			wikiContext = new WikiContext(this.toolMap.keySet());
		}
		List<Context> contextList = wikiContext.getWikiContext(featureString);
		
		List<Tool> toolsWoutContext = new ArrayList<>();
		for (Context contextObj : contextList) {
			Tool tool = this.toolMap.get(contextObj.getTitle());
			
			List<String> contextFromWiki = contextObj.getContext();
			List<String> contextFromTsv = tool.getContext();
			//wird hier alles richtig ausgewertet???
			if (!contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsvAndWiki++;
			}
			if (!contextFromWiki.isEmpty() && contextFromTsv.isEmpty()) {
				contextFoundinWiki++;

			}
			if (contextFromWiki.isEmpty() && !contextFromTsv.isEmpty()) {
				contextFoundinTsv++;
			}else{
				toolsWoutContext.add(tool);
			}
			tool.addContext(contextFromWiki);
			tool.setFeaturedName(contextObj.getFeaturedTitle());
			makeWordMap(tool);
			model.addToolToBagOfWordsWithID(tool);
			
		}
		Cooccurrence cooccurrence = new Cooccurrence(new HashSet<Tool>(this.toolMap.values()));
		contextFoundInOtherTool= cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);
		
		
		
		System.out.println("-----Statistik-----");
		System.out.println("Insgesamt gibt es "+this.toolMap.size()+" Tools \n");
		System.out.println("Für "+contextFoundinTsv+" Tools wurden Kontext in der Tsv-Datei gefunden \n");
		System.out.println("Für "+contextFoundinWiki+" Tools wurden Kontext in dem Wiki-Dump gefunden \n");
		System.out.println("Für "+contextFoundinTsvAndWiki+" Tools wurden sowohl Kontext in der Tsv-Datei als auch im Wiki-Dump gefunden \n");
		System.out.println("Für "+contextFoundInOtherTool+" Tools wurde der Kontext von referenzierenden Tools verwendet \n");
		System.out.println("Für "+(toolsWoutContext.size()-contextFoundInOtherTool)+" Tools wurden gar keine Kontexte gefunden \n");
		
		
		
		// getKontextPathFromWikiNormal();
		// getFeaturedKontextPathFromWiki();
		// readKontext();
		// featureKontext();
		// getContextFromReferencingTools();
		// writeModelOutputStream();
		// writeModelImportantInformations();
		return model;
	}

	private void makeWordMap(Tool tool) {
		Map<String, Double> bagOfWords = new HashMap<>();
		double wordCount = 0.;
		for (String word : tool.getContext()) {
			wordCount++;
			Double count = bagOfWords.get(word);
			if (count == null) {
				count = 0.;
			}
			bagOfWords.put(word, ++count);
		}
		tool.setWordMap(bagOfWords);
		tool.setWordCount(wordCount);
	}
	
	public static void main(String[] args){
		ModelMaker mm = new ModelMaker();
		mm.makeModelNeu("Stems");
	}

	

	


}
