package de.uk.spinfo.ml2016.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uk.spinfo.ml2016.Structures.Tool;
import de.uk.spinfo.ml2016.Structures.ToolPart;
import de.uk.spinfo.ml2016.Structures.ToolSub;

public class TsvParser {

	private Set<Tool> tools = new HashSet<>();
	private Set<ToolPart> toolParts = new HashSet<>();
	private Set<ToolSub> toolSubs = new HashSet<>();

	public Set<Tool> getTools() {
		return tools;
	}

	public Set<ToolPart> getToolParts() {
		return toolParts;
	}

	public Set<ToolSub> getToolSubs() {
		return toolSubs;
	}

	public void parseTsv(File file) {
		try (BufferedReader bReader = new BufferedReader(new FileReader(file))) {

			// bReader.readLine();
			while (bReader.ready()) {

				String line = bReader.readLine();

				String[] lineSplit = line.split("\t");
				// zb Z.978 quasi leer, 1498 Kontext leer
				if (!lineSplit[0].isEmpty() && !lineSplit[1].isEmpty()) {
					String name = lineSplit[0];
					Integer toolNumber = Integer.valueOf(Character.toString(lineSplit[2].charAt(1)));
					Character toolSubId = lineSplit[2].charAt(2);

					String toolSubName = lineSplit[3];
					String context = lineSplit[11];

					if (!context.equals("NA")) {

						ToolPart tp = new ToolPart(toolNumber);

						ToolSub ts = new ToolSub(toolSubId, toolSubName, tp);

						Tool tool = new Tool(name, context, ts);

						tools.add(tool);
						toolSubs.add(ts);
						toolParts.add(tp);
					}

				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		System.out.println(tools.size());
	}

	public static void main(String[] args) {
		File f = new File("resources/tools.tsv");
		TsvParser tp = new TsvParser();
		tp.parseTsv(f);
	}
}
