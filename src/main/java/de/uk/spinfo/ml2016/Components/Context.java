package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.uk.spinfo.ml2016.Structures.Tool;

public class Context {

	public List<String> enrichContext(String tool) {
		List<String> context = new ArrayList<>();
		
		File f = new File("resources/extractedWiki");
		int u = 0;
		for (File folder : f.listFiles()) {
			for (String datafile : folder.list()) {
				context = readDocument(tool, folder, datafile);
			}
		}
		if(context.isEmpty()){
			System.out.println("Info: Kein Kontext gefunden für "+tool);
		}else{
			System.out.println("Info: Kontext gefunden für "+tool);
		}
		return context;
			
		

}

	private List<String> readDocument(String tool, File folder,
			String datafile) {
		List<String> context = new ArrayList<>();
		boolean contextFound = false;
	
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(folder.getAbsolutePath()+"/"+datafile), "UTF8"))) {
			while (bReader.ready()) {
				String line = bReader.readLine();
				line = line.trim();
				if (!line.isEmpty()) {
					if (contextFound == true) {
						context.add(line);
					}
					if (line.startsWith("<doc id")&& line.contains(tool)) {		
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
		
		
	}}
