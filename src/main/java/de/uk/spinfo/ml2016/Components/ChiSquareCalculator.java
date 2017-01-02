package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChiSquareCalculator {

	//ToDo: kann mit neuem Stand der Dinge vereinfacht werden 
	
	
	public static Map<String, List<String>> computeChiSquare(Map<String, List<String>> boWforAllCategories,
			int keylistlength) {
		Map<String, List<String>> keyWords = new HashMap<>();
		// Map<String, Map<String, Double>> chiSquareOfAllCategories = new
		// HashMap<>();

		Map<String, Map<String, Double>> wordCountsOfAllCategories = countWords(boWforAllCategories);
		Map<String, Double> overallWordCounts = mergeMaps(wordCountsOfAllCategories);

		for (String category : wordCountsOfAllCategories.keySet()) {
			Map<String, Double> chiSquareMap = new HashMap<>();
			Map<String, Double> sortedChiSquareMap = new LinkedHashMap<>();

			Map<String, Double> wordCountsOfThisCategory = wordCountsOfAllCategories.get(category);
			for (String word : wordCountsOfThisCategory.keySet()) {
				Double expectedCount = (wordCountsOfThisCategory.get("totalCount") * overallWordCounts.get(word))
						/ overallWordCounts.get("totalCount");
				Double chiSquare = Math.pow((wordCountsOfThisCategory.get(word) - expectedCount), 2) / expectedCount;
				chiSquareMap.put(word, chiSquare);
			}
			sortedChiSquareMap = sort(chiSquareMap);
			// chiSquareOfAllCategories.put(category, sortedChiSquareMap);

			List<String> allKeyWordList = new LinkedList(sortedChiSquareMap.keySet());
			List<String> bestKeyWords = new LinkedList<>();
			for (int i = 1; i < keylistlength + 1; i++) {
				bestKeyWords.add(allKeyWordList.get(i));
			}
			keyWords.put(category, bestKeyWords);

		}

		return keyWords;
	}

	private static Map<String, Map<String, Double>> countWords(Map<String, List<String>> boWforAllCategories) {
		Map<String, Map<String, Double>> wordCountsOfAllCategories = new HashMap<>();
		for (String category : boWforAllCategories.keySet()) {
			double totalWordsInThisCategory = 0;
			List<String> wordsOfCategory = boWforAllCategories.get(category);
			Map<String, Double> wordCountsOfThisCategory = new HashMap<>();
			for (String word : wordsOfCategory) {
				Double count = wordCountsOfThisCategory.get(word);
				if (count == null) {
					count = 0.;
				}
				wordCountsOfThisCategory.put(word, count++);
				totalWordsInThisCategory++;
			}
			wordCountsOfThisCategory.put("totalCount", totalWordsInThisCategory);
			wordCountsOfAllCategories.put(category, wordCountsOfThisCategory);
		}
		return wordCountsOfAllCategories;
	}

	private static Map<String, Double> mergeMaps(Map<String, Map<String, Double>> wordCountsOfAllCategories) {
		Map<String, Double> overallWordCounts = new HashMap<>();

		for (String category : wordCountsOfAllCategories.keySet()) {
			Map<String, Double> wordCountsOfThisCategory = wordCountsOfAllCategories.get(category);

			for (String word : wordCountsOfThisCategory.keySet()) {
				Double count = overallWordCounts.get(word);
				if (count == null) {
					count = 0.;
				}
				overallWordCounts.put(word, count + wordCountsOfThisCategory.get(word));
			}
		}
		return overallWordCounts;
	}

	private static LinkedHashMap<String, Double> sort(Map<String, Double> map) {

		ValueComparator bvc = new ValueComparator(map);
		TreeMap<String, Double> tempSortedMap = new TreeMap<String, Double>(bvc);
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
		tempSortedMap.putAll(map);

		for (String key : tempSortedMap.keySet()) {
			Double value = map.get(key);
			sortedMap.put(key, value);

		}
		return sortedMap;
	}

}
