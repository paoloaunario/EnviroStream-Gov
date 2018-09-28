package com.envirostream.envirostream;

/*
 * Creating a global variable that can be used throughout all the classes.
 */


public class MyAppHelper {
	private String category = "";
	
	// Returns category (set by the user)
	public String returnCategory(){
		return category;
	}
	// Sets the category
	public void setCategory(String categ){
		category = categ;
	}
	
	MyAppHelper(String category){
		this.category = category;
	}
	static final private MyAppHelper INSTANCE = new MyAppHelper("");
	
	static public MyAppHelper getInstance(){
		return INSTANCE;
	}
	
}
