package de.uk.spinfo.ml2016.Structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolSub {

	private String id;

	private String name;

	private ToolPart tp;
	
	private Set<Tool> toolsList;

	public ToolSub(String id, String name, ToolPart tp) {
		this.id = id;
		this.name = name;
		this.tp = tp;
		this.toolsList = new HashSet<>();
	}
	
	public ToolPart getToolPart(){
		return this.tp;
	}
	
	public Set<Tool> getToolList(){
		return this.toolsList;
	}
	public void addTool(Tool tool){
		toolsList.add(tool);
	}
	
	public String getID(){
		return this.id;
	}
	@Override
	public int hashCode(){
		return this.id.hashCode();
	}
	//evtl noch + this.name.hashCode();
	
	@Override
	public boolean equals(Object o){
		if(this == o){
			return true;
		}
		if (this.getToolPart().getID() !=( ((ToolSub)o).getToolPart().getID() ) ){
			return false;
		}
		if (this.id !=( ((ToolSub)o).id ) ){
			return false;
		}
	
		else{
			return true;
		}
	}
}