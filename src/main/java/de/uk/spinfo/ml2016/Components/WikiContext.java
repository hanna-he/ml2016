package de.uk.spinfo.ml2016.Components;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Context;
import de.uk.spinfo.ml2016.io.Reader;

public class WikiContext {
	private Map<String, String> indexMap;
	private ContextSearcher contextsearcher;
	
	public WikiContext(Set<String> wordList){
		indexMap = Reader.readIndexFile();
		contextsearcher = new ContextSearcher(wordList, indexMap);
	}
	
	
	
	public List<Context> getWikiContext(String featureString){
		FeatureFactory featureFactory = new FeatureFactory();
		Feature feature = featureFactory.createFeature(featureString);
		List<Context> contextList = contextsearcher.getContext(feature);
		return contextList;
	}

}
