package de.uk.spinfo.ml2016.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uk.spinfo.ml2016.Components.ContextSearcher;
import de.uk.spinfo.ml2016.Components.Tokenizer;
import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.Structures.ToolPart;
import de.uk.spinfo.ml2016.Structures.ToolSub;

public class TsvParser {

	private Map<String, Tool> tools = new HashMap<>();
	private Map<String, ToolSub> toolSubs = new HashMap<>();
	private Map<Integer, ToolPart> toolParts = new HashMap<>();
	

	public Map<String, Tool> getTools() {
		return tools;
	}

	public Map<Integer, ToolPart>  getToolParts() {
		return toolParts;
	}
	
	public Set<ToolPart> getToolPartSet(){
		Set<ToolPart> tpset = new HashSet<>();
		for(Integer i : toolParts.keySet()){
			tpset.add(toolParts.get(i));
		}
		return tpset;
	}

	public Map<String, ToolSub> getToolSubs() {
		return toolSubs;
	}

	public void parseTsv(File file) {
			
		try (BufferedReader bReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF8"))){

			// bReader.readLine();
			while (bReader.ready()) {

				String line = bReader.readLine();

				String[] lineSplit = line.split("\t");
				// zb Z.978 quasi leer, 1498 Kontext leer
				if (!lineSplit[0].isEmpty() && !lineSplit[1].isEmpty()) {
					List<String> contextList = new ArrayList<>();
					String name = lineSplit[0];
					
					Integer toolNumber = Integer.valueOf(Character.toString(lineSplit[2].charAt(1)));
					String toolSubId =  lineSplit[2].replace(")", "").replace("(", "");

					String toolSubName = lineSplit[3];
//					contextList = context.enrichContext(name);
//					contextList = tokenizer.tokenize(lineSplit[11]);
					if(!lineSplit[11].equalsIgnoreCase("na")){
						contextList.add(lineSplit[11]);
					}
					
					
					ToolPart tp = new ToolPart(toolNumber);

					ToolSub ts = new ToolSub(toolSubId, toolSubName, tp);

					Tool tool = new Tool(name, contextList, ts);
					
					if(! tools.containsKey(name)){
						tools.put(name,tool);
					}
					ToolSub tmpToolsub = toolSubs.get(toolSubId);
						if (tmpToolsub == null){
							tmpToolsub=ts;
						}
						tmpToolsub.addTool(tool);
						toolSubs.put(toolSubId, ts);
					
					ToolPart tmpToolpart = toolParts.get(toolNumber);
					if(tmpToolpart == null){
						tmpToolpart = tp;
					}
					tmpToolpart.addToolSub(tmpToolsub);
					
					toolParts.put(toolNumber, tmpToolpart);
				}

			}			

		} catch (

		IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	

	public List<String> enrichContext() {
		List<String> context = new ArrayList<>();

		return context;
	}

	public static void main(String[] args) {
		

	}
}
