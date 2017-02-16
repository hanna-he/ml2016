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

public class Index {

	public static void makeIndexFile() {
		String fileOutputName = "resources/sortedWiki/indexNeu.txt";
		File writeIn = new File(fileOutputName);
		int i =0;
		if (writeIn.exists()) {
			writeIn.renameTo(new File("resources/sortedWiki/INDEX_alt.txt"));
		}
		File f = new File("resources/WikiOneFilePerArticle_klein");
		try (BufferedWriter fWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(fileOutputName, true), "UTF-8"))) {

			for (String datafile : f.list()) {
				fWriter.write(datafile + "\t" + f.toString()+"/" + datafile + "\n");
				i ++;
			}
			fWriter.flush();
			fWriter.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println(i+ " Zeilen im Index");

	}

	
	public static void main(String[] args) {

		Index.makeIndexFile();
	}

}
