package de.uk.spinfo.ml2016.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Components.Feature;
import de.uk.spinfo.ml2016.Components.FeatureFactory;
import de.uk.spinfo.ml2016.Preprocessing.DumpReader;
import de.uk.spinfo.ml2016.Structures.ModelMaker;
import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.Structures.ToolPart;
import de.uk.spinfo.ml2016.Structures.ToolSub;
import de.uk.spinfo.ml2016.io.TsvParser;

public class Main {
	FeatureFactory featureFactory = new FeatureFactory();

	
	
	public static void main(String[] args){
	File f = new File("resources/toolstest.tsv");
	TsvParser tsvp = new TsvParser();
	tsvp.parseTsv(f);
	ModelMaker mm = new ModelMaker();
	mm.makeModel("Stems", tsvp.getToolPartSet());
//	mm.makeModel("3-Grams", tsvp.getToolPartSet());
	

		


}

}
