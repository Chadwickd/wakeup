package com.davelabs.wakemehome;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapPinPointActivity extends Activity {

	private GoogleMap _map;
	private Toast _search_error_toast;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
        	setContentView(R.layout.map_pinpoint);
        	MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.searchMap);
        	_map = f.getMap();
        	String searchQuery = getPassedSearchQuery();
        	LatLng searchedPoint = convertSearchQueryToPoint(searchQuery);
        	moveMapToTargetPoint(searchedPoint);	
        }
        catch (Exception e)
        {
        	Log.e("WakeMeHome", "Error creating MapPinPointActivity", e); 
        	this.finish();
        }
    }

	private void moveMapToTargetPoint(LatLng targetPoint) {
		
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPoint,_map.getMaxZoomLevel() -5);
		_map.animateCamera(cameraUpdate);
	}

	private LatLng convertSearchQueryToPoint(String searchQuery) {
		List<Address> listOfLocations = null;
		Geocoder gc = new Geocoder(this);
		try {
			listOfLocations = gc.getFromLocationName(searchQuery, 1);
			try {
				LatLng targetPosition = new LatLng (listOfLocations.get(0).getLatitude(),listOfLocations.get(0).getLongitude());
				return targetPosition;
			} catch (IndexOutOfBoundsException e) {
				Toast errorToast = getSearchErrorToast();
				errorToast.show();
				return null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to connect to google maps servers", Toast.LENGTH_SHORT).show();
			return null;
		}
		
		
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
	
	public Toast getSearchErrorToast()
	{
		if (_search_error_toast == null)
		{
			_search_error_toast = Toast.makeText(this, "Couldn't find search location. Please try again", Toast.LENGTH_SHORT);
		}
		
		return _search_error_toast;
	}
}
