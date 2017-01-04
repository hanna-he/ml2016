package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uk.spinfo.ml2016.Structures.Tool;

public class Ngrams extends Feature {
	private int len;

	public Ngrams(int len) {
		super();
		this.len = len;
	}

//	@Override
//	public Tool processWords(Tool tmptool) {
//		Double toolWordCount = 0.;
//		Map<String, Double> wordMap = new HashMap<>();
//
//		for (String line : tmptool.getContext()) {
//			line = line.replace(" ", "_");
//			line = line.toLowerCase();
//			// String[] result = new String[parts.length - len + 1];
//			for (int i = 0; i < line.length() - len + 1; i++) {
//				// StringBuilder sb = new StringBuilder();
//				// for(int k = 0; k < len; k++) {
//				// sb.append(line[i+k]);
//				// }
//				String sub = line.substring(i, i + len);
//				addWord(sub, wordMap);
////				addWord(sub, bagOfAllWords);
//				toolWordCount++;
//			}
//		}
//		tmptool.setWordMap(wordMap);
//		tmptool.setWordCount(toolWordCount);
//		return tmptool;
//	}
	
	public Map<String, Double> processWords(List<String> text) {
		Double toolWordCount = 0.;
		Map<String, Double> wordMap = new HashMap<>();

		for (String line : text) {
			line = line.replace(" ", "_");
			line = line.toLowerCase();
			for (int i = 0; i < line.length() - len + 1; i++) {
				String sub = line.substring(i, i + len);
				addWord(sub, wordMap);
				toolWordCount++;
			}
		}
		wordMap.put("totalWordCount", toolWordCount);
		
		return wordMap;
	}

}
