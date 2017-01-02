package de.uk.spinfo.ml2016.Preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.WikiModel;

public class DumpReader {
	
	//noch nicht im Einsatz
	//zum sortieren des Dumps in Ordner des Anfangsbuchstabens und darin in Files des 2. buchstabens (-> es muss nicht
	//immer alles durchsucht werden) 
	//Problem: bei Artikeln, die aus mehr als einem Wort bestehen -> wohin sollen sie?

	public static void bla() {
		File f = new File("resources/extractedWiki");

		for (File folder : f.listFiles()) {
			for (String datafile : folder.list()) {
				readAndWriteDocument(datafile, folder);
			}
		}
	}

private static void readAndWriteDocument(String datafile, File folder) {
	List<String> context = new ArrayList<>();
	boolean contextFound = false;
	String direction = "resources/sortedWiki/";
	String saveFolder="";


	try (BufferedReader bReader = new BufferedReader(
			new InputStreamReader(new FileInputStream(folder.getAbsolutePath()+"/"+datafile), "UTF8"))) {
		while (bReader.ready()) {
			String line = bReader.readLine();
			line = line.trim();
		
			if (!line.isEmpty()) {
				
				if (contextFound == true) {
					context.add(line);
				}
				if (line.startsWith("<doc id")){
					saveFolder = line.split("title=")[1].toLowerCase().replace("\"", "").replace(">", "");
					System.out.println(saveFolder);
					System.out.println(saveFolder.charAt(0));
					contextFound = true;
					context.add(line);
																
				}
				if (line.equals("</doc>")) {
					File tmpFile = new File(direction+saveFolder.charAt(0));
					if(!tmpFile.exists()){
						tmpFile.mkdirs();
					}
					try (BufferedWriter fWriter = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(direction+saveFolder.charAt(0)+"/"+saveFolder.charAt(1), true),
									"UTF-8"))) {
						for(String s : context){
						fWriter.write(s+"\n");
						}
						fWriter.write("\n");
						fWriter.flush();
						fWriter.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					contextFound = false;
					context.clear();
				}

			}

		}
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}

	
	
	
}

public static void parseFile(String pathToFile) {

	try {
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(pathToFile + ".xml"), "UTF8"));
		InputSource inputSource = new InputSource(reader);
		WikiContentHandler postch = new WikiContentHandler(pathToFile );

		xmlReader.setContentHandler(postch);

		xmlReader.parse(inputSource);

		

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (SAXException e) {
		e.printStackTrace();
	}

}



// "https://www.mywiki.com/wiki/${title}");
// wikiModel
// String plainStr = wikiModel.render(new PlainTextConverter(), TEST);

// System.out.print(plainStr);
}
