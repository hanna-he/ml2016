package de.uk.spinfo.ml2016.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.google.gson.Gson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.uk.spinfo.ml2016.Structures.BagOfWords;
import de.uk.spinfo.ml2016.Structures.Model;
import de.uk.spinfo.ml2016.Structures.Tool;

public class Writer {

	
	public static void writeB(Model model) {
		String feature = model.getFeature();
		try {
			BufferedWriter bWriter = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream("resources/json/" + feature + ".json", false), "UTF-8"));

			JSONObject obj1 = new JSONObject();
			obj1.put("feature", feature);
			JSONArray jsClassList = new JSONArray();
			for (BagOfWords bow : model.getBagOfWordList()) {
				JSONObject obj2 = new JSONObject();
				
				JSONArray jsBowList = new JSONArray();
				for(String bowWord: bow.getWords()){
//					JSONObject jsBowWord = new JSONObject();
//					jsBowWord.put(bowWord, bow.getMap().get(bowWord));
					jsBowList.add(bowWord);
				}
				
				obj2.put("Bag_Of_Words", jsBowList);
				
				JSONObject jsTool = new JSONObject();
				for (Tool tool : bow.getTools()) {

					JSONArray jsWordList = new JSONArray();
					for (String word : tool.getWordMap().keySet()) {
						JSONObject jsWord = new JSONObject();
						jsWord.put(word, tool.getWordMap().get(word));
						jsWordList.add(jsWord);
					}
					jsTool.put(tool.getName(), jsWordList);
				}
				obj2.put("Tools", jsTool);
				obj2.put("ClassId", bow.getID());
				jsClassList.add(obj2);
			}
			obj1.put("Classes", jsClassList);
			bWriter.write(obj1.toJSONString());
			System.out.println(obj1.toJSONString());
			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	//momentan nicht in Gebrauch
	public static void writeBagOfWords(Model model) {
		String feature = model.getFeature();
		Gson gson = new Gson();
		try {
			BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("resources/json/" + feature + ".json", false), "UTF-8"));
			for (BagOfWords bow : model.getBagOfWordList()) {
				bWriter.write(gson.toJson(bow.getMap()));
			}
			// bWriter.write(gson.toJson(model));

			bWriter.flush();
			bWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	

}
