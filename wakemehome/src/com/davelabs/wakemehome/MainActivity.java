package com.davelabs.wakemehome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
	private AlertDialog _popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onSearchButtonClicked(View v)
    {
    	//if (!isOnline()) {}
    	EditText searchInput = (EditText) this.findViewById(R.id.searchLocationInput);
    	String searchQuery = searchInput.getText().toString();
    			
    	Intent intent = new Intent(this, MapPinPointActivity.class);
    	intent.putExtra("searchQuery", searchQuery);
    	
    	this.startActivity(intent);
    }
    
    
    public boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

	public AlertDialog getPopup() {
		if (_popup == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You don't seem to have internet, we will take you to the settings page to turn it on. Press back to return when you are done");
			builder.setPositiveButton("Go", dialogClickListener);
			_popup = builder.create();
		}
		return _popup;
	}
	
	private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	            
	    }
	};

	
}
