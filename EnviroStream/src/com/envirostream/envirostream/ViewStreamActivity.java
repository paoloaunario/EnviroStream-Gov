package com.envirostream.envirostream;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.ListActivity;
import android.app.ProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
//import android.widget.ListView;
import android.widget.SimpleAdapter;


// The code from this class is identical to that of AddStreamActivity only difference is there
// is no onclick listener in this class and we add description, link, and title to the hashmap.
public class ViewStreamActivity extends ListActivity{
	
	String categ = MyAppHelper.getInstance().returnCategory();
	//Progress Dialog
	private ProgressDialog pDialog;
	
	// Creating a JSON parser object
	JSONParser jParser = new JSONParser();
	
	ArrayList<HashMap<String,String>> productsList;
		
	//products JSON array
	JSONArray products = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_view);
		
		// Hashmap for ListView
		productsList = new ArrayList<HashMap<String,String>>();

		
		// loading products in Background thread
		new LoadDetailed().execute();
	}
	
	/**
	 * background async task to load all streams by making http request
	 */
	class LoadDetailed extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ViewStreamActivity.this);
            pDialog.setMessage("Loading articles. Please wait...");
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
            params.add(new BasicNameValuePair("category",categ));
            // getting JSON string from URL
            //JSONObject json = jParser.makeHttpRequest("http://www.dev.applications.ene.gov.on.ca/jay/getCategories.php" , "GET", params);
            JSONObject json = jParser.makeHttpRequest("http://www.dev.applications.ene.gov.on.ca/jay/getArticles.php" , "GET", params);
            
            // Check your log cat for JSON reponse
            Log.e("All Articles: ", json.toString());
 
            try {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray("prod");
 
                    // looping through All Products(title,description,link)
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);
 
                        // Storing each json item in variable
                        String title = c.getString("title");
                        String description = c.getString("description");
                        String link = c.getString("link");
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put("title", title);
                        map.put("description", description);
                        map.put("link", link);
 
                        // adding HashList to ArrayList
                        productsList.add(map);
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
                            ViewStreamActivity.this, productsList,
                            R.layout.all_article, new String[] {"title","description","link"},
                            new int[] { R.id.detailTitle,R.id.detailDescription,R.id.detailLink });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
	
		}       
	}

}


