package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

import de.uk.spinfo.ml2016.Structures.Tool;

public class ContextSearcher {
	private Feature feature;
	private Map<Tool, Set<String>> possibleTitles;

	public ContextSearcher(Feature feature) {
		this.feature = feature;
		

	}


	private Map<Tool, List<String>> getFeaturedNamesMap(Set<Tool> toolsList) {
		Map<Tool, List<String>> nameMap = new HashMap<>(toolsList.size());
		for (Tool tool : toolsList) {
			List<String> toolnames = new ArrayList<>();
			toolnames.add(tool.getName());
			Set<String> featuredToolname = this.feature.processWords(toolnames).keySet();
			featuredToolname.remove("totalWordCount");
			tool.setFeaturedName(featuredToolname);
			toolnames.addAll(featuredToolname);
			nameMap.put(tool, toolnames);
		}
		
		System.out.println("ContextSearcher getFeauturedNamesMap");
		return nameMap;
	}
	
	private static List<String> readFile(){
		List<String> index = new ArrayList<>();
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream("resources/sortedWiki/index.txt"), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				index.add(line);
			}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		System.out.println("Index eingelesen");
		return index;
	}

	public Map<Tool, Set<String>> getPathToTitleMap(Set<Tool> toolsList) {
		Map<Tool, List<String>> nameMap = getFeaturedNamesMap(toolsList);
		
		this.possibleTitles = new HashMap<>();
		List<String> index = readFile();
		int ind =0;
		for(String line: index){
			System.out.println("index zeile "+ind++);
				String title = line.split("\t")[0];
				List<String> titleList = new ArrayList<>();
				titleList.add(title);
				titleList.addAll(this.feature.processWords(titleList).keySet());
				titleList.remove("totalWordCount");
//				tmpfeaturedWords.remove("totalWordCount");
//				titleList.addAll(tmpfeaturedWords);
//				//sinnvoll?
//				tmpfeaturedWords = null;
				for (Tool tool : nameMap.keySet()) {
					
					Set<String> titles = this.possibleTitles.get(tool);
					if (titles == null) {
						titles = new HashSet<>();
					}
					for (String toolname : nameMap.get(tool)) {
						for (String word : titleList) {
							if (toolname.equalsIgnoreCase(word)) {

								titles.add(line);

							}
						}
					}
					this.possibleTitles.put(tool, titles);
				}

			}
		
		System.out.println("ContextSearcher schreiben in 'gefundene Kontexte' fängt an");
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/gefundeneKontexte.txt", false), "UTF-8"));
			for(Tool tool: possibleTitles.keySet()){
				bWriter.write(tool.getName()+"\t"+possibleTitles.get(tool)+"\n");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return possibleTitles;

	}

	public boolean getContext(Tool tool) {
		//geht es schneller die possibleTitles nach dem Tool abzusuchen, um den featuredName zu bekommen,
		//oder den Toolname neu zu featuren (=lemmatisieren, stemmen o. n-grammen)
		for(Tool othertool: this.possibleTitles.keySet()){
			if (othertool == tool){
				tool = othertool;
			}
		}
		boolean contextFound = false;
		List<String> context = new ArrayList<>();
		Set<String> possibleArticles = this.possibleTitles.get(tool);

		if (!possibleArticles.isEmpty()) {
			contextFound = true;
			System.out.println("Info: Kontext gefunden für " + tool.getName() + "(" + tool.getFeaturedName() + ")");
			for (String pathToArticleAndTitle : possibleArticles) {
				context.addAll(
						readDocument(pathToArticleAndTitle.split("\t")[0], pathToArticleAndTitle.split("\t")[1]));
				

			}
		} else {
			System.out.println("Info: Kein Kontext gefunden für " + tool.getName() + "(" + tool.getFeaturedName() + ")");

		}
		tool.addContext(context);
		return contextFound;

	}
	
	
//	//alt
//	private List<String> getPathToTitle(Tool tool) {
//		String toolname = tool.getName();
//		List<String> possibleTitles = new ArrayList<>();
//		try (BufferedReader bReader = new BufferedReader(
//				new InputStreamReader(new FileInputStream("resources/sortedWiki/indexSehrKurz.txt"), "UTF8"))) {
//			while (bReader.ready()) {
//				String line = bReader.readLine();
//				String title = line.split("\t")[0];
//				if (title.contains(toolname)) {
//					possibleTitles.add(line);
//				} else {
//					// System.out.println(title + " " + toolname);
//					List<String> fakeList = new ArrayList<>();
//					fakeList.add(title);
//					Set<String> featuredWords = this.feature.processWords(fakeList).keySet();
//					List<String> anotherFakeList = new ArrayList<>();
//					featuredWords.remove("totalWordCount");
//					anotherFakeList.add(toolname);
//					Set<String> featuredToolname = this.feature.processWords(anotherFakeList).keySet();
//					featuredToolname.remove("totalWordCount");
//					tool.setFeaturedName(featuredToolname);
//					// dauert sehr lange
//					for (String toolpartname : featuredToolname) {
//						for (String word : featuredWords) {
//							if (toolpartname.equals(word)) {
//								possibleTitles.add(line);
//							}
//						}
//					}
//				}
//
//			}
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//
//		return possibleTitles;
//
//	}

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
