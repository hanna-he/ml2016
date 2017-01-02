package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.List;

public class Model {
	
	private List<BagOfWords> bowList;
	protected String feature;
	
	public Model(String feature){
		this.feature = feature;
		bowList = new ArrayList<>();
	}
	
	public void addBoW(BagOfWords bow){
		this.bowList.add(bow);
	}
	
	public List<BagOfWords> getBagOfWordList(){
		return this.bowList;
	}
	
	public String getFeature(){
		return this.feature;
	}
	@Override
	public String toString(){
		String result = this.feature;
		return result;
	}
	

}
