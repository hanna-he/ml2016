package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.List;

public class Context {
	
	private String title;
	private List<String> context;
//	private String stemmedtitle;
//	private String lemmatizedtitle;
//	private String nGramedTitle;
	
	public Context(String title){
		this.title=title;
		context = new ArrayList<>();
	}
	
	public String getTitle(){
		return this.title;
	}
	public void addContext(List<String> context){
		this.context.addAll(context);
	}
	
	public List<String> getContext(){
		return this.context;
	}

}
