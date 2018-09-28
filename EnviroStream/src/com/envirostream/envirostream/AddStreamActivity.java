package com.envirostream.envirostream;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AddStreamActivity extends ListActivity{
	
	//Progress Dialog
	private ProgressDialog pDialog;
	
	// Creating a JSON parser object
	JSONParser jParser = new JSONParser();
	ArrayList<HashMap<String,String>> streamsList;
	
	//products JSON array
	JSONArray streams = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_view);
		// Hashmap for ListView
		streamsList = new ArrayList<HashMap<String,String>>();
		// loading products in Background thread
		new LoadAllStreams().execute();		
		saveStream();
	}
	// Save stream method; uses an onclick to save the categories.
	private void saveStream() {
		ListView list = (ListView)findViewById(android.R.id.list);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				String check = streamsList.get(position).toString().replace("{category=", "");
				check = check.replace("}", "");
				String message = "You clicked position " + position + "which is category: " + check;
				Toast.makeText(AddStreamActivity.this, message, Toast.LENGTH_LONG).show();  
				// Saves the category that was clicked into a text file.
				try{
					FileOutputStream fos = openFileOutput("userSettings", Context.MODE_APPEND);
					fos.write(check.getBytes());
					fos.write(("\n").getBytes());
					fos.close();
					File f = new File("userSettings");
					//f.delete();
					Log.e("success: ",check);
				}catch(Exception e){
					e.printStackTrace();
				}
				// Finishes the activity and switches the intent back to the Main.
				AddStreamActivity.this.finish();
				Intent i = new Intent(AddStreamActivity.this, MainActivity.class);
				startActivity(i);
			}
		});
		
	}
		
	/**
	 * background async task to load all streams by making http request
	 */
	class LoadAllStreams extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddStreamActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
		
		/**
		 * getting all products from url
		 */
		
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest("http://www.dev.applications.ene.gov.on.ca/jay/getCategories.php", "GET", params);
           
            // Check your log cat for JSON reponse
            Log.e("All Products: ", json.toString());
            StringBuffer stringBuffer = new StringBuffer();
            try {
                    // products found
                    // Getting Array of Products
                    streams = json.getJSONArray("prod");
                    try{
            			BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("userSettings")));
            			String inputString;
            			while((inputString = inputReader.readLine()) != null){
            				stringBuffer.append(inputString+"\n");		
            			}
            			}catch(IOException e){
            				e.printStackTrace();
            			}	
 
                    // looping through All Products
                    for (int i = 0; i < streams.length(); i++) {
                        JSONObject c = streams.getJSONObject(i);
 
                        // Storing each json item in variable
                        String category = c.getString("category");
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        
                        if(!stringBuffer.toString().contains(category)){
                        	map.put("category", category);
                        	// adding HashList to ArrayList
                            streamsList.add(map);
                        }
                        
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
	/**
	 * After completing background task dismiss the progress dialog
	 */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AddStreamActivity.this, streamsList,
                            R.layout.all_category, new String[] {"category"},
                            new int[] { R.id.stream_category });
                    // updating listview
                    setListAdapter(adapter);              
                }
            });
	
		}  
	}
}


