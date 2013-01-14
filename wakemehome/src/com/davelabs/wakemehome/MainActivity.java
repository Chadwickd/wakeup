package com.davelabs.wakemehome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      //Delete this when we no longer want a default value in the search
        ((EditText) this.findViewById(R.id.searchLocationInput)).setText("EC1N 8NX");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onSearchButtonClicked(View v)
    {
    	EditText searchInput = (EditText) this.findViewById(R.id.searchLocationInput);
    	String searchQuery = searchInput.getText().toString();
    			
    	Intent intent = new Intent(this, MapPinPointActivity.class);
    	intent.putExtra("searchQuery", searchQuery);
    	
    	this.startActivity(intent);
    }
}
