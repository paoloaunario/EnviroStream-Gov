package com.envirostream.envirostream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {
	Button btnAddStream;
	private List<Stream> myFeed = new ArrayList<Stream>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        
        //Add Button
        btnAddStream = (Button) findViewById(R.id.btnAddStream);
        
        populateFeed();
        populateStreamView();
        registerClick();
        
        //view the "Add Stream" 
        btnAddStream.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//launching the add stream activity
				//MainActivity.this.finish();
				Intent i = new Intent(getApplicationContext(),AddStreamActivity.class);
				startActivity(i);
			}
		});               
    }
    @Override
    public void onBackPressed(){
		startActivity(new Intent(this,AddStreamActivity.class));
	}
    
	private void populateFeed() {
		// TODO populate the feed
		//Creates a string buffer reads from the text file created to see what categories have been chosen.
		try{
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput("userSettings")));
			String inputString;
			StringBuffer stringBuffer = new StringBuffer();
			while((inputString = inputReader.readLine()) != null){
				stringBuffer.append(inputString+"\n");
				myFeed.add(new Stream(inputString.toString(),R.drawable.test));
			}
			//Log.e("swaggaaaa: ",stringBuffer.toString());
		}catch(IOException e){
			e.printStackTrace();
		}	
	}
	
	//onclick listener that checks what category is clicked on the homepage and directs the user to that detailed view.
	private void registerClick() {
		ListView list = (ListView)findViewById(R.id.streamListView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				Stream clickedStream = myFeed.get(position);
				String message = "You clicked position " + position + "which is category: " + clickedStream.getCategory();
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
				MyAppHelper.getInstance().setCategory(clickedStream.getCategory());
				
				//Switches the intent from the home view to the detailed view.
				Intent i = new Intent(getApplicationContext(),ViewStreamActivity.class);
				startActivity(i);
			}
		});
		
	}
	
	private void populateStreamView() {
		// TODO populate the view
		ArrayAdapter<Stream> adapter = new MyStreamAdapter();
		ListView list = (ListView) findViewById(R.id.streamListView);
		list.setAdapter(adapter);
	}
	
	private class MyStreamAdapter extends ArrayAdapter<Stream>{
		public MyStreamAdapter(){
			super(MainActivity.this,R.layout.home_view,myFeed);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// checking if the view is null
			View streamView = convertView;
			if(streamView == null){
				streamView = getLayoutInflater().inflate(R.layout.home_view, parent, false);
			}
			
			//Find the current feed
			Stream currentStream = myFeed.get(position);
			
			//fill the view(icon)
			ImageView imageView = (ImageView)streamView.findViewById(R.id.stream_icon);
			imageView.setImageResource(currentStream.getIcon());
			
			//Category
			TextView categoryText = (TextView)streamView.findViewById(R.id.stream_category);
			categoryText.setText(currentStream.getCategory());
			
			return streamView;
			
		}	
	}
}
