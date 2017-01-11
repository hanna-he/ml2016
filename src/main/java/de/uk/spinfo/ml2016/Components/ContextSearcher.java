package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Tool;

public class ContextSearcher {
	private Feature feature;
	

	public ContextSearcher(Feature feature) {
		this.feature = feature;

	}

	// public List<String> enrichContext(String tool) {
	// List<String> context = new ArrayList<>();
	//
	// File f = new File("resources/extractedWiki");
	// int u = 0;
	// for (File folder : f.listFiles()) {
	// for (String datafile : folder.list()) {
	// context = readDocument(tool, folder, datafile);
	// }
	// }
	// if (context.isEmpty()) {
	// System.out.println("Info: Kein Kontext gefunden für " + tool);
	// } else {
	// System.out.println("Info: Kontext gefunden für " + tool);
	// }
	// return context;
	//
	// }

	public boolean getContext(Tool tool) {
		boolean contextFound = false;
		String toolname = tool.getName();
		List<String> context = new ArrayList<>();
		List<String> possibleArticles = getPathToTitle(tool);

		if (!possibleArticles.isEmpty()) {
			contextFound=true;
			for (String pathToArticleAndTitle : possibleArticles) {
				context.addAll(
						readDocument(pathToArticleAndTitle.split("\t")[0], pathToArticleAndTitle.split("\t")[1]));
				System.out.println("Info: Kontext gefunden für " + toolname+ "("+tool.getFeaturedName()+")");
				

			}
		} else {
			System.out.println("Info: Kein Kontext gefunden für " + toolname+"("+tool.getFeaturedName()+")");

		}
		tool.addContext(context);
		return contextFound;
		
	}

	private List<String> getPathToTitle(Tool tool) {
		String toolname = tool.getName();
		List<String> possibleTitles = new ArrayList<>();
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream("resources/sortedWiki/index.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				String title = line.split("\t")[0];
				if (title.contains(toolname)) {
					possibleTitles.add(line);
				} else {
					// System.out.println(title + " " + toolname);
					List<String> fakeList = new ArrayList<>();
					fakeList.add(title);
					Set<String> featuredWords = this.feature.processWords(fakeList).keySet();
					List<String> anotherFakeList = new ArrayList<>();
					featuredWords.remove("totalWordCount");
					anotherFakeList.add(toolname);
					Set<String> featuredToolname = this.feature.processWords(anotherFakeList).keySet();
					featuredToolname.remove("totalWordCount");
					tool.setFeaturedName(featuredToolname);
					// dauert sehr lange
					for (String toolpartname : featuredToolname) {
						for (String word : featuredWords) {
							if (toolpartname.equals(word)) {
								possibleTitles.add(line);
							}
						}
					}
				}

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return possibleTitles;

	}

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
