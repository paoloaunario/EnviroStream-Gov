package com.envirostream.envirostream;

public class Stream {
	private String category;
	private int icon;
	
	//Constructor for stream
	public Stream(String category, int icon){
		super();
		this.category = category;
		this.icon = icon;
	}
	
	// Setter and Getters for Icon
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	// Setter and Getters for Category
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
