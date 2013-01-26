package com.davelabs.wakemehome;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapPinPointActivity extends Activity {

	private GoogleMap _map;
	private Dialog _searchQueryNotFoundDialog;
	private ISearchProvider _searchProvider;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pinpoint);
        _searchProvider = new GeocoderSearchProvider(this);
        
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.searchMap);
        _map = f.getMap();
        
        String searchQuery = getPassedSearchQuery();
        LatLng targetPoint = lookupSearchQuery(searchQuery);
        
        if (targetPoint != null) {
        	moveMapToTargetPoint(targetPoint);
        }
    }

	private void moveMapToTargetPoint(LatLng targetPoint) {
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPoint,_map.getMaxZoomLevel() -5);
		_map.animateCamera(cameraUpdate);
	}

	public LatLng lookupSearchQuery(String searchQuery) {
		
		LatLng result = null;
		
		try {
			result = _searchProvider.getSearchResult(searchQuery);
			
			if (result == null) {
				Dialog d = getSearchQueryNotFoundDialog();
				d.show();
			}
			
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to connect to google maps servers", Toast.LENGTH_SHORT).show();
		}
		
		return result;
	}

	private String getPassedSearchQuery() {
        Bundle extras = this.getIntent().getExtras();
		String passedSearchType = extras.getString("searchQuery");
		return passedSearchType;
	}
	
	public void onLocationOkButtonClicked (View v)  {
    	Intent intent = new Intent(this, MapTrackingActivity.class);
    	intent.putExtra("LocationCoords", _map.getCameraPosition());
    	this.startActivity(intent);
    }
	
	public Dialog getSearchQueryNotFoundDialog()
	{
		if (_searchQueryNotFoundDialog == null) {
			final Activity a = this;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Unable to find the address, please try another search query.");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					a.finish();
				}
			});
			
			_searchQueryNotFoundDialog = builder.create();
		}
		
		return _searchQueryNotFoundDialog;
	}
}
