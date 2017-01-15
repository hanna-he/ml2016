package de.uk.spinfo.ml2016.Components;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uk.spinfo.ml2016.Structures.BagOfWords;
import de.uk.spinfo.ml2016.Structures.Model;

public class ChiSquareCalculator {
 
	
	
	public static Map<Integer, List<String>> computeChiSquare(Model model,
			int keylistlength) {
		Map<Integer, List<String>> keyWords = new HashMap<>();
		Map<String, Double> overallWordCounts = mergeMaps(model.getBagOfWordList());
		for(BagOfWords bow: model.getBagOfWordList()){
			int newkeylistlength = keylistlength;
			Map<String, Double> chiSquareMap = new HashMap<>();
			Map<String, Double> sortedChiSquareMap = new LinkedHashMap<>();
			Map<String, Double> wordCountsOfThisCategory = bow.getWordMap();
			for (String word : wordCountsOfThisCategory.keySet()) {
				Double expectedCount = (bow.getTotalWordCount() * overallWordCounts.get(word))
						/ overallWordCounts.get("totalCount");
				Double chiSquare = Math.pow((wordCountsOfThisCategory.get(word) - expectedCount), 2) / expectedCount;
				chiSquareMap.put(word, chiSquare);
			}
			
			sortedChiSquareMap = sort(chiSquareMap);
			
			List<String> allKeyWordList = new LinkedList(sortedChiSquareMap.keySet());
			List<String> bestKeyWords = new LinkedList<>();
			if(allKeyWordList .size()<keylistlength){
				newkeylistlength = allKeyWordList .size();
			}
			for (int i = 0; i < newkeylistlength ; i++) {
				bestKeyWords.add(allKeyWordList.get(i));
			}
			keyWords.put(bow.getID(), bestKeyWords);

		}
		try {
			BufferedWriter bWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("resources/"+keylistlength+"_keyWords_"+model.getFeature()+".txt", false), "UTF-8"));
			for(Integer id : keyWords.keySet()){
				bWriter.write("Class: "+id+"\n");
				for(String word : keyWords.get(id)){
					bWriter.write(word+"\n");
				}
			}
			
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return keyWords;
	}


	private static Map<String, Double> mergeMaps(List<BagOfWords> bowList) {
		Map<String, Double> overallWordCounts = new HashMap<>();
		double overallTotalWordCount = 0.;
		for (BagOfWords bow: bowList) {
			Map<String, Double> wordCountsOfThisCategory = bow.getWordMap();

			for (String word : wordCountsOfThisCategory.keySet()) {
				Double count = overallWordCounts.get(word);
				if (count == null) {
					count = 0.;
				}
				overallWordCounts.put(word, count + wordCountsOfThisCategory.get(word));
			}
			overallTotalWordCount+= bow.getTotalWordCount();
		}
		overallWordCounts.put("totalCount", overallTotalWordCount);
		return overallWordCounts;
	}

	public static <K> LinkedHashMap<K, Double> sort(Map<K, Double> map) {

		ValueComparator bvc = new ValueComparator(map);
		TreeMap<K, Double> tempSortedMap = new TreeMap<K, Double>(bvc);
		LinkedHashMap<K, Double> sortedMap = new LinkedHashMap<>();
		tempSortedMap.putAll(map);

		for (K key : tempSortedMap.keySet()) {
			Double value = map.get(key);
			sortedMap.put(key, value);

		}
		return sortedMap;
	}

}
