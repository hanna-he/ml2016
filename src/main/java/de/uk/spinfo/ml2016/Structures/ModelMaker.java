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
		int noContextFound = 0;

		Model model = new Model(featureString);
		if (toolMap.isEmpty()) {
			readTSV();
		}
		if (wikiContext == null) {
			wikiContext = new WikiContext(this.toolMap.keySet());
		}
		List<Context> contextList = wikiContext.getWikiContext(featureString);
		List<Tool> toolsWoutContext = new ArrayList<>();
		for (Context contextObj : contextList) {
			Tool tool = this.toolMap.get(contextObj.getTitle());
			// wie genau verhält sich hier die auswertung?
			List<String> contextFromWiki = contextObj.getContext();
			List<String> contextFromTsv = tool.getContext();
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
			makeWordMap(tool);
			model.addToolToBagOfWordsWithID(tool);
			
		}
		Cooccurrence cooccurrence = new Cooccurrence();
		cooccurrence.enrichContextWithReferencingTools(toolsWoutContext, model);

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

	

	


}
