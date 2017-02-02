package de.uk.spinfo.ml2016.Components;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Tool;

public abstract class Feature {
	public boolean needsTokenizing = false;

	public abstract List<String> processWords(List<String> text);
	

	
	public List<String> filterStopwords(List<String> featuredText){
		List<String> stopwords = new ArrayList<>();
		try (BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream("resources/stopwords.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				stopwords.add(line);
			}
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		List<String> featuredStopwords = processWords(stopwords);
		featuredText.removeAll(featuredStopwords);
		return featuredText;
	}

	

}
