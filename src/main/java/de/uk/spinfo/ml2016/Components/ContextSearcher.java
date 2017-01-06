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
	Feature feature;

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

	public Tool getContext(Tool tool) {
		String toolname = tool.getName();
		List<String> context = new ArrayList<>();
		Map<Tool,List<String>> pathToTitleOfTool = new HashMap<>();
		List<String> possibleArticles = pathToTitleOfTool.get(tool);
		if (!context.isEmpty()) {
			for (String pathToArticleAndTitle : possibleArticles) {
				context.addAll(
						readDocument(pathToArticleAndTitle.split("\t")[0], pathToArticleAndTitle.split("\t")[1]));
				System.out.println("Info: Kontext gefunden für " + toolname);
			}
		} else {
			System.out.println("Info: Kein Kontext gefunden für " + toolname);

		}
		tool.addContext(context);
		return tool;
	}

	private Map<Tool,List<String>> getPathToTitle(Tool tool) {
		Map<Tool,List<String>> result = new HashMap<>();
		String toolname = tool.getName();
		List<String> possibleTitles = new ArrayList<>();
		// FeatureFactory featurefactory = new FeatureFactory();
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream("resources/sortedWiki/index.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				String title = line.split("\t")[0];
				if (title.contains(toolname)) {
					possibleTitles.add(line);
				} else {
					System.out.println(title+" "+toolname);
					List<String> fakeList = new ArrayList<>();
					fakeList.add(title);
					Set<String> featuredWords = this.feature.processWords(fakeList).keySet();
					List<String> anotherFakeList = new ArrayList<>();
					featuredWords.remove("totalWordCount");
					anotherFakeList.add(toolname);
					Set<String> featuredToolname = this.feature.processWords(anotherFakeList).keySet();
					featuredToolname.remove("totalWordCount");
					tool.setFeaturedName(featuredToolname);
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
		result.put(tool, possibleTitles);

		return result;

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
					}

				}

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return context;

	}

	// test:
//	public static void main(String[] args) {
//		FeatureFactory ff = new FeatureFactory();
//		ContextSearcher cs = new ContextSearcher(ff.createFeature("Stems"));
//		cs.getContext("labor");
//
//	}
}
