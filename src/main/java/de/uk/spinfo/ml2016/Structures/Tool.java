package de.uk.spinfo.ml2016.Structures;

public class Tool {

	private ToolSub tsc;
	private String name;
	private String context;

	public Tool(String name, String context, ToolSub tsc) {
		this.tsc = tsc;
		this.name = name;
		this.context = context;
	}

	public String getContext() {
		return this.context;
	}

	public void addContext(String newcontext) {
		this.context += newcontext;
	}

}
