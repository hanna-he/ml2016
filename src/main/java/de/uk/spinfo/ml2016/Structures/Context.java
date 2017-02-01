package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Context {
	
	private String title;
	private List<String> context;
	private List<String> featuredTitle;
	private Set<String> pathIndex;
//	private String stemmedtitle;
//	private String lemmatizedtitle;
//	private String nGramedTitle;
	
	public Context(String title){
		this.title=title;
		context = new ArrayList<>();
		pathIndex = new HashSet<>();
	}
	
	public String getTitle(){
		return this.title;
	}
	public void addContext(List<String> context){
		this.context.addAll(context);
	}
//	public void addContext(String context){
//		this.context.add(context);
//	}
	
	public List<String> getContext(){
		return this.context;
	}

	public List<String> getFeaturedTitle() {
		return featuredTitle;
	}

	public void setFeaturedTitle(List<String> featuredTitle) {
		this.featuredTitle = featuredTitle;
	}

	public Set<String> getPathIndex() {
		return pathIndex;
	}

//	public void setPathIndex(Set<String> pathIndex) {
//		this.pathIndex = pathIndex;
//	}
	public void addPath(String pathIndex) {
		this.pathIndex.add(pathIndex);
	}
	

}
