package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.List;

public class Model {

	private List<BagOfWords> bowList;
	protected String feature;

	public Model(String feature) {
		this.feature = feature;
		bowList = new ArrayList<>();
	}

	public void addBoW(BagOfWords bow) {
		if (this.bowList.contains(bow)) {
			this.bowList.remove(bow);
		}
		this.bowList.add(bow);
	}

	public List<BagOfWords> getBagOfWordList() {
		return this.bowList;
	}

	public void addToolToBagOfWordsWithID(Tool tool) {
		int toolID = tool.getToolSub().getToolPart().getID();
		boolean found = false;
		for (BagOfWords bowTmp : this.bowList) {
			if (bowTmp.getID() == toolID) {
				found = true;
				bowTmp.addTool(tool);
			}
		}
		if(found == false){
			BagOfWords bow = new BagOfWords(toolID);
			bow.addTool(tool);
			this.bowList.add(bow);
		}
		//evtl noch neues BOW anlegen, falls es es noch nicht gibt
	}

	public String getFeature() {
		return this.feature;
	}



}
