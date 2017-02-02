package de.uk.spinfo.ml2016.Components;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tartarus.snowball.ext.germanStemmer;

import de.uk.spinfo.ml2016.Structures.Model;
import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.Structures.ToolPart;

public class Cooccurrence {
	private Set<Tool> toolSet;
	Set<ToolPart> toolPartList;
	private int referenceListLength = 3;

	public Cooccurrence(Set<Tool> toolset) {
		this.toolSet = toolset;
	}

	public void countCooccurrence(Tool tool) {
		Set<Tool> coCount = new HashSet<>();
		Map<String, Double> wordMap = tool.getWordMap();
		for (Tool otherTool : this.toolSet) {
			List<String> otherName = otherTool.getFeaturedName();
			if (wordMap.keySet().containsAll(otherName)) {
				coCount.add(otherTool);
			}
		}
		tool.setCooccurrenceCounts(coCount);
	}

	// gibt die anderen Tools, die in ihrem Kontext dieses Tool nennen zurück
	private void getReferencingTools(Tool tool) {
		Map<Tool, Double> referencingToolsWithNumber = new HashMap<>();
		Map<Tool, Double> sortedReferencingToolsWithNumber = new HashMap<>();
		for (Tool otherTool : this.toolSet) {
			if (otherTool != tool) {
				for (String word : tool.getFeaturedName()) {
					if (otherTool.getWordMap().containsKey(word)) {
						referencingToolsWithNumber.put(otherTool, otherTool.getWordMap().get(word));
					}
				}
			}
		}
		sortedReferencingToolsWithNumber = ChiSquareCalculator.sort(referencingToolsWithNumber);
		List<Tool> allReferencesList = new ArrayList<Tool>(sortedReferencingToolsWithNumber.keySet());

		int len = 0;
		if (allReferencesList.size() < this.referenceListLength) {
			len = allReferencesList.size();
		} else {
			len = this.referenceListLength;
		}
		Set<Tool> bestReferences = new HashSet<>(len);
		for (int i = 0; i < len; i++) {
			bestReferences.add(allReferencesList.get(i));
		}
		tool.setReferencingTools(bestReferences);

	}

	public int enrichContextWithReferencingTools(List<Tool> toolsWoutContext, Model model) {
		int contextFound = 0;
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/ToolsWithoutContext_" + model.getFeature() + "_Mini.txt", false),
					"UTF-8"));
			for (Tool toolagain : toolsWoutContext) {
				getReferencingTools(toolagain);
				if (!toolagain.getReferencingTools().isEmpty()) {
					contextFound++;

					bWriter.write("\n Für Tool : " + toolagain.getName()
							+ " wurden die Kontexte folgender Tools hinzugefügt:\n");

					for (Tool refTool : toolagain.getReferencingTools()) {
						bWriter.write(refTool.getName() + ", ");
						toolagain.addContext(refTool.getContext());
					}
				}

				model.addToolToBagOfWordsWithID(toolagain);
			}
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contextFound;
	}

}
