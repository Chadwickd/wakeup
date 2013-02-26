package com.davelabs.wakemehome.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;

import com.davelabs.wakemehome.SearchedLocation;
import com.google.android.gms.maps.model.LatLng;

//Simple SearchedLocationStore implementation that saves a single location only
public class PreferencesFileSearchedLocationStore implements
		SearchedLocationStore {

	private SharedPreferences _preferences;

	public PreferencesFileSearchedLocationStore(Context context)
	{
		_preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
	}
	
	@Override
	public List<SearchedLocation> getAllLocations() {
		List<SearchedLocation> locations = new ArrayList<SearchedLocation>();
		
		SearchedLocation homeLocation = getHomeLocationFromPreferences();
		
		if (homeLocation != null) {
			locations.add(homeLocation);
		}
		
		return locations;
	}

	@Override
	public SearchedLocation getHomeLocation() {
		return getHomeLocationFromPreferences();
	}

	@Override
	public void saveLocation(SearchedLocation location) {
		// TODO Auto-generated method stub
		
	}	

	private SearchedLocation getHomeLocationFromPreferences() {
		SearchedLocation homeLocation = null;
		
		String homeLocationQuery = _preferences.getString("homeLocationQuery", null);
		
		if (homeLocationQuery != null) {
			float lat = _preferences.getFloat("homeLocationLat", 0);
			float lng = _preferences.getFloat("homeLocationLong", 0);

			LatLng latLng = new LatLng(lat, lng);
			
			homeLocation = new SearchedLocation(0, homeLocationQuery, latLng, true, false);
		}
		
		return homeLocation;
	}
}
