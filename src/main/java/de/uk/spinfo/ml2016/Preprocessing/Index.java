package de.uk.spinfo.ml2016.Preprocessing;

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

public class Index {

	public static void makeIndexFile() {
		File f = new File("resources/extractedWiki");
		int counter = 0;
		for (File folder : f.listFiles()) {
			for (String datafile : folder.list()) {
				counter += makeIndex(datafile, folder);
			}
		}
		System.out.println(counter);
	}

	private static int makeIndex(String datafile, File folder) {
		int counter =0;
		String direction = "resources/sortedWiki/";
		String title = "";
		try (BufferedWriter fWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(direction + "INDEX.txt", true), "UTF-8"))) {
			try (BufferedReader bReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(folder.toString() + "/" + datafile), "UTF8"))) {
				while (bReader.ready()) {
					String line = bReader.readLine();
					if (line.startsWith("<doc id")) {
						counter++;
						title = line.split("title=")[1].toLowerCase().replace("\"", "").replace(">", "");
//						System.out.println(title);
						fWriter.write(title + "\t" + folder.toString() + "/" + datafile+"\n");
					}
					
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			fWriter.flush();
			fWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return counter;
	}
	public static void main(String[] args){
		
		Index.makeIndexFile();
	}

}
