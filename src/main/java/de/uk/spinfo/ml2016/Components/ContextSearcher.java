package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Context;
import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.io.Reader;

public class ContextSearcher {
	private Tokenizer tokenizer = null;
	private List<Context> perfectMatches;
	private List<Context> noPerfectMatches;
	private Set<String> wordSet;
	private Map<String, String> indexMap;
	private Map<String, List<String>> tokenizedTitles;

	//Klasse soll nur einmal erstellt und danach wiederverwendet werden,
	//egal, ob lemmatisiert, gestemmed oder ge-n-Grammt wird
	//so müssen zb perfectMatches nur einmal berechnet werden -> Laufzeit sparen
	public ContextSearcher(Set<String> wordList, Map<String, String> indexMap) {
		this.wordSet = wordList;
		this.indexMap = indexMap;
		this.tokenizedTitles = new HashMap<>();
	}
	//"Main"-Methode, die alle anderen, privaten Methoden dieser Klasse nacheinander aufruft
	//es wird eine Liste von Contexten zurückgegeben
	public List<Context> getContext(Feature feature) {
		List<Context> allContext = new ArrayList<>();
		if (this.perfectMatches == null) { // soll nur ein einziges Mal
											// ausgeführt werden
			// perfectMatches Pfade finden
			searchPerfectMatches();
			// und auslesen aus WikiDump
			addContextFromPath(this.perfectMatches, feature);
			System.out.println("Für "+this.perfectMatches.size()+" wurden perfekte Matches gefunden");
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
			BufferedWriter bWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("resources/gefundeneKontexteMINI.txt", false), "UTF-8"));
			for (Context context : allContext) {
				bWriter.write(context.getTitle() + "\t" + context.getPathIndex() + "\n");
			}
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allContext;
	}

	// Map, in der Strings auf perfektMatches aus Indexdatei gemappt werden,
	// soll nur einmal ausgeführt werden (da für alle (Stem/Lemma/nGram) gleich)
	private void searchPerfectMatches() {
		this.perfectMatches = new ArrayList<>();
		this.noPerfectMatches= new ArrayList<>();
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
			// TODO: hier noch absichern, dass bei mehrteiligen Worten
			// (Start/Amadeus/merlin) auch mehr als 1
			// teil im Titel des WikiArtikel gefunden wird -> aber wie?
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
				titlesToBeProcessed= this.tokenizedTitles.get(titleOfArticle);
				if(titlesToBeProcessed == null){
					titlesToBeProcessed=tokenizeWords(titleOfArticle);
					tokenizedTitles.put(titleOfArticle, titlesToBeProcessed);
				}
			
			} else {
				titlesToBeProcessed.add(titleOfArticle);
			}
			String path = indexMap.get(titleOfArticle);
			// später zum auslesen der Artikels werden die
			// Original-Title(titleOfArticle) noch einmal gebraucht

			// wird jetzt feature.processWords bei jedem iterieren neu
			// berechnet?
			// also ist es so effizienter oder, wenn man es vorher als
			// List<String> speichert 
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
//					System.out.println("pathSPlit :"+pathSplit[1]+" , "+pathSplit[0]);
					context.addContext(feature.processWords(readContextFromIndex(pathSplit[1], pathSplit[0])));
				}
//				System.out.println("Info: Kontext gefunden für " + context.getTitle());
//
//			} else {
//				System.out.println("Info: Kein Kontext gefunden für " + context.getTitle());
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

	// liest den Artikel mit dem Titel titel aus dem Pfad path aus
	private static List<String> readContextFromIndex(String title, String path) {
		// oder return-Objekt besser ein String? -> was verbraucht weniger
		// Speicher?/geht schneller?
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
	
	
	
	
	public static void main(String[]args){
		Set<String> articlesToSearch = new HashSet<>();
		articlesToSearch.add("Flasche");
		articlesToSearch.add("Lampe");
		articlesToSearch.add("Türe");
		
		ContextSearcher contextsearcher = new ContextSearcher(articlesToSearch, Reader.readIndexFile());
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature("Lemmas");
		List<Context> contextList = contextsearcher.getContext(feature);
	}

}
