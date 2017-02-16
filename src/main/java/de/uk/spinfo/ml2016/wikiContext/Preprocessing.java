package de.uk.spinfo.ml2016.wikiContext;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Preprocessing {
	//test

	public static void makeIndexFile() {
		File f = new File("resources/extracted");

		for (File folder : f.listFiles()) {
			for (String datafile : folder.list()) {
				readAndWriteArticles(datafile, folder);
			}
		}
	}

	private static void readAndWriteArticles(String datafile, File folder) {
		String direction = "resources/WikiOneFilePerArticle";
		String title = "";

		List<String> context = new ArrayList<>();
		boolean contextFound = false;
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(folder.toString() + "/" + datafile), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				line = line.toLowerCase();
				if (!line.isEmpty()) {
					if (contextFound == true) {
						context.add(line);
					}
					if (line.startsWith("<doc id")) {
//						title = line.split("title=")[1].toLowerCase().replace("\"", "").replace(">", "");
						
						title = line.split("title=")[1].replace("\"", "").replace(">", "").replace("/", "\\");
						
						contextFound = true;

					}
					if (line.equals("</doc>")) {
						contextFound = false;
						context.remove(line);
				
						try {
							BufferedWriter fWriter = new BufferedWriter(						
								new OutputStreamWriter(new FileOutputStream(direction+"/"+title+".txt", false), "UTF-8"));
							for (String con : context) {
								fWriter.write(con+"\n");
							}

							fWriter.flush();
							fWriter.close();
						} catch (IOException ioe) {
							ioe.printStackTrace();
						}
						context.clear();

					}

				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
public static void main(String[] args){
	final long start = System.currentTimeMillis();
		Preprocessing.makeIndexFile();
		final long end = System.currentTimeMillis();
		System.out.println(end-start);
	}

}
