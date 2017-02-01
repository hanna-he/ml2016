package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Context;
import de.uk.spinfo.ml2016.Structures.Tool;

public class ContextSearcher {
	// private Feature feature;
	private Map<Tool, Set<String>> possibleTitles;
	private Tokenizer tokenizer = null;
	private List<String> tokenizedTitles;
	private List<String> tokenizedWords;
	// private List<String> tokenizedTools;
	private List<Context> perfectMatches;
	private List<Context> noPerfectMatches;
	private Set<String> wordSet;
	private Map<String, String> indexMap;
	private Map<List<String>, String> tokenizedIndexMap;

	public ContextSearcher(Set<String> wordList, Map<String, String> indexMap) {
		this.wordSet = wordList;
		this.indexMap = indexMap;
	}

	private void initalizeTokenizer() {
		try {
			this.tokenizer = new Tokenizer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> tokenizeWords(List<String> toTokenize) throws Exception {
		List<String> tokenizedWords = new ArrayList<>();

		for (String line : toTokenize) {
			line = line.toLowerCase();
			tokenizedWords.addAll(tokenizeWords(line));
		}
		return tokenizedWords;
	}

	private List<String> tokenizeWords(String toTokenize) throws Exception {
		if (this.tokenizer == null) {
			initalizeTokenizer();
		}
		List<String> tokenizedWords = tokenizer.tokenize(toTokenize);
		return tokenizedWords;
	}

	// Map, in der Strings auf perfektMatches aus Indexdatei gemappt werden,
	//soll nur einmal ausgeführt werden (da für alle (Stem/Lemma/nGram) gleich)
	private void searchPerfectMatches() {
		this.perfectMatches = new ArrayList<>();
		for (String word : wordSet) {
			Context context = new Context(word);
			if (indexMap.containsKey(word.toLowerCase())) {				
				context.addPath(indexMap.get(word));
				this.perfectMatches.add(context);
//				Set<String> fakeSet = new HashSet<>();
//				fakeSet.add(word.toLowerCase() + "\t" + indexMap.get(word));
//				this.perfectMatches.put(word, fakeSet);
//			}else{
//				this.noPerfectMatches.add(context);
			}
		}
	}

	private List<Context> getPerfectMatches() {
		if (this.perfectMatches == null) {
			searchPerfectMatches();
		}
		System.out.println(perfectMatches.size()+" Tools haben einen perfekten Kontext-Match");
		return this.perfectMatches;

	}

	private List<Context> getAllMatches(Feature feature) throws Exception {

		List<String> wordListCopy = new ArrayList<>();
		wordListCopy.addAll(this.wordSet);
		wordListCopy.removeAll(this.perfectMatches.keySet());
		

		Map<String, String> featuredIndexMap = new HashMap<>();
		for (String titleOfArticle : this.indexMap.keySet()) {
			List<String> titlesToBeProcessed = new ArrayList<>();
			//tokens abspeichern zur Wiederverwendung (Stem/Lemma)
			if (feature.needsTokenizing == true) {
				if (this.tokenizedTitles == null) {
					this.tokenizedTitles = tokenizeWords(titleOfArticle);
				}
				titlesToBeProcessed = this.tokenizedTitles;
			} else {
				titlesToBeProcessed.add(titleOfArticle);
			}
			String path = indexMap.get(titleOfArticle);
			tokenizedIndexMap.put(titlesToBeProcessed, path);

			// wird jetzt feature.processWords bei jedem iterieren neu
			// berechnet?
			// also ist es so effizienter oder, wenn man es vorher als
			// List<String> speichert (!aber 2 millionen mal)
			for (String featuredWord : feature.processWords(titlesToBeProcessed)) {
				featuredIndexMap.put(featuredWord, path);
			}

		}
		List<Context> contextList = new ArrayList<>();
		for (String word : wordListCopy) {
			List<String> wordsToBeProcessed = new ArrayList<>();
			//tokens abspeichern zur Wiederverwendung (Stem/Lemma)
			if (feature.needsTokenizing == true) {
				if (this.tokenizedWords == null) {
					this.tokenizedWords = tokenizeWords(word);
				}
				wordsToBeProcessed = this.tokenizedWords;
			} else {
				wordsToBeProcessed.add(word);
			}
			
			Map<String, String> featuredIndexMapCopy = new HashMap<>();
			featuredIndexMapCopy.putAll(featuredIndexMap);
			// hier noch absichern, dass bei mehrteiligen Worten auch mehr als 1
			// teil gefunden wird -> aber wie?
			Context context = new Context(word);
			context.setFeaturedTitle(feature.processWords(wordsToBeProcessed));
			featuredIndexMapCopy.keySet().retainAll(context.getFeaturedTitle());
			context.setPathIndex(new HashSet<String>(featuredIndexMapCopy.values()));
			contextList.add(context);
//			matches.put(word, new HashSet<String>(featuredIndexMapCopy.values()));
		}

//		matches.putAll(getPerfectMatches());
//		return matches;
		return contextList;
	}

	
	
	//noch verändern, dass zb perfectmatches context nur einmal ausgelesen wird
	public List<Context> getContext(Feature feature) {
		List<Context> matches = new ArrayList<>();
		try {
			matches = getAllMatches(feature);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		List<Context> contextList = new ArrayList<>();
		for (Context context : matches) {
			Set<String> possibleArticlesPath = context.getPathIndex();
			if (!possibleArticlesPath.isEmpty()) {
				for (String path : possibleArticlesPath) {
					String[] pathSplit = path.split("\t");
					context.addContext(feature.processWords(readDocument(pathSplit[1], pathSplit[0])));
				}
				System.out.println("Info: Kontext gefunden für " + context.getTitle());

			} else {
				System.out.println("Info: Kein Kontext gefunden für " + context.getTitle());
			}
		}
		return matches;
	}
	
	
	
	
	

	// oder besser zu String zusammen fügen -> was verbraucht weniger
	// Speicher?/geht schneller?
	private static List<String> readDocument(String title, String path) {
		List<String> context = new ArrayList<>();
		boolean contextFound = false;
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				line = line.toLowerCase();
				if (!line.isEmpty()) {
					if (contextFound == true) {
						context.add(line);
					}
					if (line.startsWith("<doc id") && line.contains(title)) {
						contextFound = true;

					}
					if (line.equals("</doc>")) {
						contextFound = false;
						context.remove(line);
					}

				}

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return context;

	}

}
