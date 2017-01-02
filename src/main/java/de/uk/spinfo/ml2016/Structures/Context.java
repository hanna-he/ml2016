package de.uk.spinfo.ml2016.Structures;

public class Context {
	
	private String title;
	private String context;
	private String stemmedtitle;
	private String lemmatizedtitle;
	private String nGramedTitle;
	
	public Context(){
		
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	public void addContext(String context){
		this.context = context;
	}
	
	public void addStemmedTitle(String stitle){
		this.stemmedtitle=stitle;
	}

}
