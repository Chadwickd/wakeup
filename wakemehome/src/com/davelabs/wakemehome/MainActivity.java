package com.davelabs.wakemehome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.davelabs.wakemehome.helpers.NetworkHelper;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.SearchedLocationStoreFactory;
import com.google.android.gms.maps.model.CameraPosition;

public class MainActivity extends Activity {
	private AlertDialog _popup;
	private SearchedLocationStore _store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        populateGUIWithStoredLocations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void populateGUIWithStoredLocations() {
    	_store = SearchedLocationStoreFactory.getStore(this);
		populateHomeLocation();
	}

	private void populateHomeLocation() {
		SearchedLocation homeLocation = _store.getHomeLocation();
		
		if (homeLocation != null) {
			Button takeHomeButton = (Button)this.findViewById(R.id.performTakeHomeButton);
			String takeHomeString = this.getString(R.string.perform_take_home);
			takeHomeButton.setText(takeHomeString + ": " + homeLocation.getSearchQuery());
			takeHomeButton.setEnabled(true);
		}
	}

	public void onSearchButtonClicked(View v)
    {
    	if (!NetworkHelper.networkTurnedOn(this)) {
    		getPopup().show();
    	} else {
    		startSearchActivity();
    	}
    }
    
    private AlertDialog getPopup() {
		if (_popup == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("You don't seem to have internet, we will take you to the settings page to turn it on. Press back to return when you are done");
			builder.setPositiveButton("Go", dialogClickListener);
			_popup = builder.create();
		}
		return _popup;
	}

	private void startSearchActivity() {
    	
    	EditText searchInput = (EditText) this.findViewById(R.id.searchLocationInput);
    	String searchQuery = searchInput.getText().toString();
    			
    	Intent intent = new Intent(this, MapPinPointActivity.class);
    	intent.putExtra("searchQuery", searchQuery);
    	
    	startActivity(intent);
    }

	private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog, int which) {
	    	startActivity(new Intent(Settings.ACTION_SETTINGS));
	    }
	};

	public void onTakeHomeButtonClicked(View v) {
		SearchedLocation home = _store.getHomeLocation();
		CameraPosition.Builder builder = CameraPosition.builder();
		builder.target(home.getTarget());
		builder.zoom(R.integer.zoomLevel);
		CameraPosition position = builder.build();
		
		Intent i = new Intent(this, MapTrackingActivity.class);
		i.putExtra("CameraPosition", position);
		
		startActivity(i);
	}
}
