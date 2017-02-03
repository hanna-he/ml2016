package de.uk.spinfo.ml2016.wikiContext;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.featuring.Feature;
import de.uk.spinfo.ml2016.featuring.FeatureFactory;
import de.uk.spinfo.ml2016.featuring.Tokenizer;
import de.uk.spinfo.ml2016.structures.Context;

public class ContextSearcher {
	private Tokenizer tokenizer = null;
	private List<Context> perfectMatches;
	private List<Context> noPerfectMatches;
	private Set<String> wordSet;
	private Map<String, String> indexMap;
	private Map<String, List<String>> tokenizedTitles;

	// Klasse soll nur einmal erstellt und danach wiederverwendet werden,
	// egal, ob lemmatisiert, gestemmed oder ge-n-Grammt wird
	// so müssen zb perfectMatches nur einmal berechnet werden -> Laufzeit
	// sparen
	public ContextSearcher(Set<String> wordList, Map<String, String> indexMap) {
		this.wordSet = wordList;
		this.indexMap = indexMap;
		this.tokenizedTitles = new HashMap<>();
	}

	// "Main"-Methode, die alle anderen, privaten Methoden dieser Klasse
	// nacheinander aufruft
	// es wird eine Liste von Contexten zurückgegeben
	public List<Context> getContext(Feature feature) {
		List<Context> allContext = new ArrayList<>();
		if (this.perfectMatches == null) { // soll nur ein einziges Mal
											// ausgeführt werden
			// perfectMatches Pfade finden
			searchPerfectMatches();
			// und auslesen aus WikiDump
			addContextFromPath(this.perfectMatches, feature);
			System.out.println("Für " + this.perfectMatches.size() + " wurden perfekte Matches gefunden");
		}
		// alle anderen Matches zwischen WikiDump-Artikel-Titeln und Strings der
		// wordList finden
		try {
			getOtherMatches(feature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addContextFromPath(this.noPerfectMatches, feature);

		allContext.addAll(noPerfectMatches);
		allContext.addAll(perfectMatches);

		System.out.println("ContextSearcher schreiben in 'gefundene Kontexte' fängt an");
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/gefundeneKontexteMINI" + feature + ".txt", false), "UTF-8"));
			for (Context context : allContext) {
				bWriter.write(context.getTitle() + "\t" + context.getPathIndex() + "\n");
			}
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("ContextSearcher schreiben in 'gefundene Kontexte' beendet");
		return allContext;
	}

	// Map, in der Strings auf perfektMatches aus Indexdatei gemappt werden,
	// soll nur einmal ausgeführt werden (da für alle (Stem/Lemma/nGram) gleich)
	private void searchPerfectMatches() {
		this.perfectMatches = new ArrayList<>();
		this.noPerfectMatches = new ArrayList<>();
		for (String word : wordSet) {
			Context context = new Context(word);
			if (indexMap.containsKey(word)) {
				context.setPath(indexMap.get(word));
				this.perfectMatches.add(context);
			} else {
				this.noPerfectMatches.add(context);
			}
		}
	}

	private void getOtherMatches(Feature feature) throws Exception {
		System.out.println("-- Info: getothermatches anfang --");
		Map<String, String> featuredIndexMap = featureIndexMap(feature);
		for (Context context : this.noPerfectMatches) {
			// tokens abspeichern zur Wiederverwendung (Stem/Lemma)
			List<String> wordsToBeProcessed = new ArrayList<>();
			if (feature.needsTokenizing == true) {
				if (context.getTokenizedTitle().isEmpty()) {
					context.setTokenizedTitle(tokenizeWords(context.getTitle()));
				}
				wordsToBeProcessed = context.getTokenizedTitle();
			} else {
				wordsToBeProcessed.add(context.getTitle());
			}
			context.setFeaturedTitle(feature.processWords(wordsToBeProcessed));
			Map<String, String> featuredIndexMapCopy = new HashMap<>();
			// !! jedes Mal Copy von der Millionen langen IndexMap
			featuredIndexMapCopy.putAll(featuredIndexMap);
			featuredIndexMapCopy.keySet().retainAll(context.getFeaturedTitle());
			context.setPath(new HashSet<String>(featuredIndexMapCopy.values()));

		}
		System.out.println("-- Info: getothermatches ende --");
	}

	// featured die IndexMap; falls vorher tokenisiert werden soll,
	// soll dies nur genau einmal geschehen
	private Map<String, String> featureIndexMap(Feature feature) throws Exception {
		System.out.println("-- Info: featureIndexMap anfang --");
		Map<String, String> featuredIndexMap = new HashMap<>();
		for (String titleOfArticle : this.indexMap.keySet()) {
			List<String> titlesToBeProcessed = new ArrayList<>();
			// tokens abspeichern zur Wiederverwendung (Stem/Lemma)
			if (feature.needsTokenizing == true) {
				titlesToBeProcessed = this.tokenizedTitles.get(titleOfArticle);
				if (titlesToBeProcessed == null) {
					titlesToBeProcessed = tokenizeWords(titleOfArticle);
					tokenizedTitles.put(titleOfArticle, titlesToBeProcessed);
				}

			} else {
				titlesToBeProcessed.add(titleOfArticle);
			}
			String path = indexMap.get(titleOfArticle);
			// später zum auslesen der Artikels werden die
			// Original-Title(titleOfArticle) noch einmal gebraucht
			List<String> featuredWords = feature.processWords(titlesToBeProcessed);
			for (String featuredWord : featuredWords) {
				featuredIndexMap.put(featuredWord, path);
			}
		}

		System.out.println("-- Info: featureIndexMap ende --");
		return featuredIndexMap;
	}

	// ruft für jeden gespeicherten Pfad die readContextFromIndex -Methode auf,
	// welche dann den Artikel aus dem Dump ausliest
	private void addContextFromPath(List<Context> contextList, Feature feature) {
		System.out.println("-- Info: addContextFromPath Anfang --");
		for (Context context : contextList) {
			// sonst wird zb der lemmatisierte Kontext zu dem gestemmten
			// hinzugefügt
			context.clearContext();
			Set<String> possibleArticlesPath = context.getPathIndex();
			if (!possibleArticlesPath.isEmpty()) {
				for (String path : possibleArticlesPath) {
					String[] pathSplit = path.split("\t");
					List<String> wordsOfWikiArticle = WikiReader.readContextFromIndex(pathSplit[1], pathSplit[0]);
					List<String> wordsToBeProcessed = new ArrayList<>();
					// schauen, ob tokenisiert werden soll
					if (feature.needsTokenizing) {
						try {
							wordsToBeProcessed = tokenizeWords(wordsOfWikiArticle);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						wordsToBeProcessed.addAll(wordsOfWikiArticle);
					}

					// Für einen zu findenen Titel im Wikidump, wird nur dann
					// Kontext hinzugefügt,wenn

					List<String> processedWords = feature.processWords(wordsToBeProcessed);
					// 1. der gefeaturte Titel aus mehr als 2 Teilwörtern
					// besteht, aber min 2 davon im Kontext wiedergefunden
					// werden (soll Matches wie Start/Amadeus/merlin <-> Amadeus
					// Mozart) verhindern
					if (context.getFeaturedTitle().size() > 2) {
						int countMatches = 0;
						for (String pieceOfTitle : context.getFeaturedTitle()) {
							if (processedWords.contains(pieceOfTitle)) {
								countMatches++;
							}
						}
						if (countMatches > 1) {
							context.addContext(processedWords);
						}
						// 2. der gefeaturte Titel aus höchstens 2 Wörtern
						// besteht
						// (dann wurde ja schon min einer beim Titelabgleich mit
						// dem Wikititel gefunden
					} else {
						context.addContext(processedWords);
					}
				}
			}
		}
		System.out.println("-- Info: addContextFromPath ende --");
	}

	// Tokenisieren
	private void initalizeTokenizer() {
		try {
			this.tokenizer = new Tokenizer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> tokenizeWords(List<String> toTokenize) throws Exception {
		List<String> tokenizedWords = new ArrayList<>();
		for (String line : toTokenize) {
			line = line.toLowerCase();
			tokenizedWords.addAll(tokenizeWords(line));
		}
		return tokenizedWords;
	}

	public List<String> tokenizeWords(String toTokenize) throws Exception {
		if (this.tokenizer == null) {
			initalizeTokenizer();
		}
		List<String> tokenizedWords = tokenizer.tokenize(toTokenize);
		return tokenizedWords;
	}

	public static void main(String[] args) throws Exception {
		//falls Index-Datei noch nicht erstellt wurde
		Index.makeIndexFile();
		
		Set<String> articlesToSearch = new HashSet<>();
		articlesToSearch.add("Flasche");
		articlesToSearch.add("Lampe");
		articlesToSearch.add("Türe");

		ContextSearcher contextsearcher = new ContextSearcher(articlesToSearch, WikiReader.readIndexFile());
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature("Lemmas");
		List<Context> contextList = contextsearcher.getContext(feature);

	}

}
