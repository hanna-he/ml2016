package de.uk.spinfo.ml2016.Components;

public class FeatureFactory {
	
	public Feature createFeature (String featureToCreate){
		Feature feature = null;
		if(featureToCreate.contains("Grams")){
			int len = Integer.parseInt(featureToCreate.split("-")[0]);
			feature = new Ngrams(len);
		}
		else if(featureToCreate.equalsIgnoreCase("Stems")){
			feature = new Stems();
		}
		else if(featureToCreate.equalsIgnoreCase("Lemmas")){
			feature = new Lemmas();
		}
		else{
			System.err.println("Invalid Featurename! ");
		}
		return feature;
	}

}
